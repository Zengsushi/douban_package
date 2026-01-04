package com.batch.statistic

import org.apache.spark.sql.{SaveMode, SparkSession}

object movie {

  def main(args: Array[String]): Unit = {
    System.setProperty("HADOOP_USER_NAME", "hadoop")

    val task = if (args.nonEmpty) args(0) else "all"

    val spark = SparkSession.builder()
      .appName("Douban Movie Analytics")
      .master("local[*]")
      .enableHiveSupport()
      .getOrCreate()

    spark.sparkContext.setLogLevel("WARN")
    import spark.implicits._

    // 使用 Hive 库
    spark.sql("USE douban")

    // 获取当前日期（用于分区写入）
    val do_date = spark.sql("SELECT current_date()").as[String].head()

    // 任务调度
    task match {
      case "year_cnt" =>
        println("▶ 统计各年份上映电影中...")
        mvYearCount(spark)

      case "score_cnt" =>
        println("▶ 统计电影分数中...")
        mvScoreCount(spark, do_date)

      case "region_cnt" =>
        println("▶ 统计地区数量中...")
        mvRegionCount(spark, do_date)

      case "category" =>
        println("▶ 统计剧场版本中...")
        mvCategoryCount(spark, do_date)

      case "type_cnt" =>
        println("▶ 统计电影类型中...")
        mvTypeCount(spark, do_date)

      case "mv_score_count_top250" =>
        println("▶ 统计电影 Top250 中...")
        mvTop250(spark, do_date)
      case "all" =>
        println("▶ 执行所有任务中...")
        mvYearCount(spark)
        mvScoreCount(spark, do_date)
        mvRegionCount(spark, do_date)
        mvCategoryCount(spark, do_date)
        mvTypeCount(spark, do_date)
        mvTop250(spark, do_date)

      case _ =>
        println(s"⚠ 未知任务: $task")
    }

    spark.close()
  }

  /** 通用写入 MySQL 函数 */
  private def writeToMySQL(spark: SparkSession, hiveTable: String, mysqlTable: String): Unit = {
    println(s"▶ 将 Hive 表 [$hiveTable] 同步写入 MySQL 表 [$mysqlTable]")
    spark.sql(s"SELECT * FROM $hiveTable")
      .write
      .format("jdbc")
      .option("url", "jdbc:mysql://master:3306/douban")
      .option("driver", "com.mysql.cj.jdbc.Driver")
      .option("user", "root")
      .option("password", "123456")
      .option("dbtable", mysqlTable)
      .mode(SaveMode.Overwrite)
      .save()
    println(s"✔ 数据写入 [$mysqlTable] 完成")
  }

  /** 年份统计 */
  private def mvYearCount(spark: SparkSession): Unit = {
    val sqlList = Seq(
      """
        |CREATE OR REPLACE TEMP VIEW year AS
        |SELECT year(YEAR) AS year,
        |       CASE WHEN region LIKE '%中国%' THEN '国内' ELSE '国外' END AS region
        |FROM movies
        |WHERE year != ''
      """.stripMargin,
      "TRUNCATE TABLE year_cnt",
      """
        |INSERT OVERWRITE TABLE year_cnt
        |SELECT row_number() OVER (ORDER BY out.year_cnt),
        |       out.year,
        |       out.region,
        |       out.year_cnt,
        |       COALESCE(inn.region, '国内'),
        |       COALESCE(inn.year_cnt, 0)
        |FROM (
        |  SELECT year, region, COUNT(*) AS year_cnt
        |  FROM year
        |  WHERE region = '国外'
        |  GROUP BY year, region
        |) out
        |LEFT JOIN (
        |  SELECT year, region, COUNT(*) AS year_cnt
        |  FROM year
        |  WHERE region = '国内'
        |  GROUP BY year, region
        |) inn ON out.year = inn.year
      """.stripMargin
    )
    sqlList.foreach(spark.sql)
    writeToMySQL(spark, "year_cnt", "year_cnt")
  }

  /** 分数统计 */
  private def mvScoreCount(spark: SparkSession, do_date: String): Unit = {
    val sqlList = Seq(
      "TRUNCATE TABLE score_cnt",
      s"""
         |INSERT OVERWRITE TABLE score_cnt PARTITION (dt = '$do_date')
         |SELECT row_number() OVER (ORDER BY CAST(score AS DOUBLE)),
         |       CAST(COALESCE(score, 0) AS DOUBLE),
         |       COUNT(id)
         |FROM movies
         |GROUP BY score
         |""".stripMargin
    )
    sqlList.foreach(spark.sql)
    writeToMySQL(spark, "score_cnt", "score_cnt")
  }

  /** 地区统计 */
  private def mvRegionCount(spark: SparkSession, do_date: String): Unit = {
    val sqlList = Seq(
      "TRUNCATE TABLE region_cnt",
      s"""
         |INSERT OVERWRITE TABLE region_cnt PARTITION (dt = '$do_date')
         |SELECT row_number() OVER (ORDER BY CAST(rating_count AS INT) DESC),
         |       title,
         |       region,
         |       CEIL(score) / 2 AS score,
         |       CASE
         |         WHEN region RLIKE '中国(大陆|香港|台湾|澳门)?' THEN '国内'
         |         ELSE '国外'
         |       END AS about
         |FROM (
         |  SELECT split(title, ' ')[0] AS title,
         |         split(region, '/')[0] AS region,
         |         score,
         |         rating_count
         |  FROM movies
         |) t
         |""".stripMargin
    )
    sqlList.foreach(spark.sql)
    writeToMySQL(spark, "region_cnt", "region_cnt")
  }

  /** 类型统计 */
  private def mvTypeCount(spark: SparkSession, do_date: String): Unit = {
    val sqlList = Seq(
      "TRUNCATE TABLE type_cnt",
      s"""
         |INSERT OVERWRITE TABLE type_cnt PARTITION (dt = '$do_date')
         |SELECT row_number() OVER (ORDER BY type),
         |       type,
         |       COUNT(*) AS type_cnt
         |FROM (
         |  SELECT explode(split(type, '/')) AS type
         |  FROM movies
         |) t
         |GROUP BY type
         |""".stripMargin
    )
    sqlList.foreach(spark.sql)
    writeToMySQL(spark, "type_cnt", "type_cnt")
  }

  /** 剧场版本统计 */
  private def mvCategoryCount(spark: SparkSession, do_date: String): Unit = {
    val sqlList = Seq(
      "TRUNCATE TABLE category",
      s"""
         |INSERT OVERWRITE TABLE category PARTITION (dt = '$do_date')
         |SELECT row_number() OVER (ORDER BY version),
         |       version,
         |       COUNT(*) AS theater
         |FROM (
         |  SELECT IF(version = '', '标准版', version) AS version
         |  FROM (
         |    SELECT regexp_replace(regexp_extract(mv_time, '\\\\(([^)]+)\\\\)', 1), '中国大陆/', '') AS version
         |    FROM movies
         |  ) t1
         |  WHERE version RLIKE '(版|重映)' AND version IS NOT NULL
         |) t2
         |GROUP BY version
         |""".stripMargin
    )
    sqlList.foreach(spark.sql)
    writeToMySQL(spark, "category", "category")
  }

  /** 高分 Top250 */
  private def mvTop250(spark: SparkSession, do_date: String): Unit = {
    val sqlList = Seq(
      "TRUNCATE TABLE mv_sc_top250",
      s"""
         |INSERT OVERWRITE TABLE mv_sc_top250 PARTITION (dt = '$do_date')
         |SELECT m.id, m.title, c.count
         |FROM (
         |  SELECT id, split(title, ' ')[0] AS title, score
         |  FROM movies
         |  WHERE score > 8.5 AND rating_count > 100000
         |) m
         |JOIN mv_count c ON m.id = c.mv_id
         |ORDER BY c.count DESC
         |LIMIT 250
         |""".stripMargin
    )
    sqlList.foreach(spark.sql)
    writeToMySQL(spark, "mv_sc_top250", "mv_score_count_top250")
  }

}
