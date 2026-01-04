package com.batch.rinse

import org.apache.spark.sql.SparkSession

object book {


  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().master("local[*]").appName(" bookRinse").getOrCreate()
    val sc = spark.sparkContext
    sc.setLogLevel("WARN")


    val booksDF = spark.read.format("csv").option("Sep", ",").load("data/books.csv")
    val booksTB = booksDF.toDF(
      "id",
      "title",
      "type",
      "author",
      "press",
      "producer",
      "year",
      "page_size",
      "price",
      "build",
      "series",
      "ISBN"
    )
    booksTB.createTempView("book")
    booksTB.select("id").distinct().toDF("id").createTempView("disBook")


    spark.sql(
        """
          |select book.*
          |from book
          |join disBook on disBook.id = book.id
          |""".stripMargin)
      .repartition(1)
      .write
      .format("csv")
      .mode("overwrite")
      .save("output")

    spark.close()


  }
}
