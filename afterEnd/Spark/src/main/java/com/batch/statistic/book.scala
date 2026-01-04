package com.batch.statistic

import org.apache.spark.sql.functions.{col, lit, substring}
import org.apache.spark.sql.{SaveMode, SparkSession}

object book {

  def main(args: Array[String]): Unit = {

    val task = if (args.length > 0) args(0) else "all"

    val spark = SparkSession.builder()
      .appName("treating")
      .master("local[*]")
      .getOrCreate()

    val sc = spark.sparkContext
    sc.setLogLevel("WARN")

    val bookDF = spark.read.format("jdbc")
      .option("url", "jdbc:mysql://master:3306/douban")
      .option("driver", "com.mysql.cj.jdbc.Driver")
      .option("user", "root")
      .option("password", "123456")
      .option("dbtable", "books")
      .load()

    bookDF.createOrReplaceTempView("books")

    task match {
      case "type_cnt" => typeCount(spark)
      case "press_cnt" => pressCount(spark)
      case "year_cnt" => yearCount(spark)
      case "page_cnt" => pageCount(spark)
      case "price_cnt" => priceCount(spark)
      case "city_cnt" => cityCount(spark)
      case "region_cnt" => regionCount(spark)
      case "all" =>


        typeCount(spark)
        pressCount(spark)
        yearCount(spark)
        pageCount(spark)
        priceCount(spark)
        cityCount(spark)
        regionCount(spark)
      case _ => println(s"未知任务类型: $task")
    }


    spark.close()
  }

  def writeToMySQL(df: org.apache.spark.sql.DataFrame, table: String): Unit = {
    df.write
      .format("jdbc")
      .option("url", "jdbc:mysql://master:3306/douban")
      .option("driver", "com.mysql.cj.jdbc.Driver")
      .option("user", "root")
      .option("password", "123456")
      .option("dbtable", table)
      .mode(SaveMode.Overwrite)
      .save()
  }

  def typeCount(spark: SparkSession): Unit = {
    println("正在统计书籍类型数量...")
    val df = spark.sql("SELECT type, COUNT(id) AS type_cnt FROM books GROUP BY type")
    writeToMySQL(df, "book_type_cnt")
  }

  def pressCount(spark: SparkSession): Unit = {
    println("正在统计出版社出品数量...")
    val df = spark.sql(
      """
        |SELECT press, COUNT(id) AS press_count
        |FROM books
        |WHERE press RLIKE '出版社'
        |GROUP BY press
        |ORDER BY press_count DESC
        |""".stripMargin)
    writeToMySQL(df, "book_press_cnt")
  }

  def yearCount(spark: SparkSession): Unit = {
    println("正在统计年份出版数量...")
    val df = spark.sql(
      """
        |SELECT year, COUNT(year) AS year_cnt
        |FROM (
        |  SELECT YEAR(year) AS year
        |  FROM books
        |  WHERE year IS NOT NULL
        |) t
        |GROUP BY year
        |HAVING year IS NOT NULL
        |""".stripMargin)
    writeToMySQL(df, "book_year_cnt")
  }

  def pageCount(spark: SparkSession): Unit = {
    println("正在统计页数分布...")
    val df = spark.sql(
      """
        |SELECT grads, count(grads)
        |FROM (
        |  SELECT CASE
        |    WHEN page_size < 100 THEN '<100'
        |    WHEN page_size < 500 THEN '<500'
        |    WHEN page_size < 1000 THEN '<1000'
        |    ELSE '>1001'
        |  END AS grads
        |  FROM books
        |) t
        |GROUP BY grads
        |""".stripMargin)
    writeToMySQL(df, "book_page_cnt")
  }

  def priceCount(spark: SparkSession): Unit = {
    println("正在统计价格分布...")
    val df = spark.sql(
      """
        |SELECT
        |  CASE
        |    WHEN price < 10 THEN '0~10'
        |    WHEN price < 20 THEN '10~20'
        |    WHEN price < 50 THEN '20~50'
        |    WHEN price < 100 THEN '50~100'
        |    WHEN price < 500 THEN '100~500'
        |    WHEN price < 1000 THEN '500~1000'
        |    WHEN price < 5000 THEN '1000~5000'
        |    ELSE '5000以上'
        |  END AS price_grade,
        |  COUNT(*) AS price_count
        |FROM (
        |  SELECT CAST(REGEXP_EXTRACT(price, '([0-9]+\\.?[0-9]*)', 1) AS DOUBLE) AS price
        |  FROM books
        |  WHERE price IS NOT NULL AND price != ''
        |) t
        |GROUP BY price_grade
        |""".stripMargin)
    writeToMySQL(df, "book_price_cnt")
  }

  def cityCount(spark: SparkSession): Unit = {
    println("正在统计城市分布...")
    val df = spark.sql(
      """
        |SELECT city, COUNT(*) AS city_cnt
        |FROM (
        |  SELECT REGEXP_EXTRACT(TRIM(press), '^([\\u4e00-\\u9fa5]{2})', 1) AS city
        |  FROM books
        |) t
        |GROUP BY city
        |""".stripMargin)
    writeToMySQL(df, "book_city_cnt")
  }

  def regionCount(spark: SparkSession): Unit = {
    println("正在统计国内外出品数量...")

    import spark.implicits._

    val df = spark.sql("SELECT id, author FROM books")
      .withColumn("firstChar", substring(col("author"), 1, 1))

    val result = df.map { row =>
        val id = row.getAs[String]("id")
        val firstChar = row.getAs[String]("firstChar")
        val region = if (firstChar == "[" || firstChar == "(") "国外" else "国内"
        (id, region)
      }.toDF("id", "region")
      .groupBy("region")
      .count()
      .withColumnRenamed("count", "value")

    writeToMySQL(result, "book_region_cnt")
  }
}
