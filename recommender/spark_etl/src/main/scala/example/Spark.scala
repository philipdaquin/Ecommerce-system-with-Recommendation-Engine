package com.example

import java.util.logging.Logger
import java.util.Properties
import org.apache.spark.sql.{SparkSession, DataFrame, SaveMode}
import org.apache.spark.sql.functions.{count, desc, rank, sum}
import org.apache.spark.sql.expressions.Window

object Spark { 


  def loadTable(
    spark: SparkSession, 
    sourceUrl: String, 
    properties: Properties, 
    tableName: String): DataFrame = {
      spark.read.jdbc(sourceUrl, tableName, properties)
  }

  def saveTable( dataframe: DataFrame, target: String, properties: Properties, tableName: String): Unit = {
    dataframe.write.mode(SaveMode.Overwrite)
      .jdbc(target, tableName, properties)
    
  }


  def main(args: Array[String]): Unit = {

    val sourceUrl = "jdbc:postgresql://localhost:5432/data-db"
    val targetUrl = "jdbc:postgresql://localhost:5432/bestseller-db"
    val username = "postgres"
    val password = "password"
    
    val properties = new Properties()
    properties.put("user", username)
    properties.put("password", password)

    val sparkSession = SparkSession
      .builder()
      .appName("ETL")
      .master("local[*]")
      .getOrCreate()

    // Read tables from database into dataframes
    val products = loadTable(sparkSession, sourceUrl, properties, "products")
    val order = loadTable(sparkSession, sourceUrl, properties, "order")
    val orderItems = loadTable(sparkSession, sourceUrl, properties, "orderItems")
    
    val userPurchases = order
      .join(orderItems, "order_id")
      .select("user_id", "product_id")
      .orderBy("user_id")
      .distinct()
    
    val saleAmountOfProduct = userPurchases
      .join(products, "product_id")
      .groupBy("product_id", "category_id")
      .agg(count("*").alias("sale_amount"))
      .orderBy(desc("sale_amount"))
    
    val spec = Window.partitionBy("category_id").orderBy(desc("sale_amount"))
    val saleRanking = saleAmountOfProduct.withColumn("rank",rank.over(spec))

    val bestSellers = saleRanking
      .filter("rank <= 10")
      .select("product_id", "cateogory_id", "sale_amount")
    
    saveTable(products, targetUrl, properties, "products")
    saveTable(bestSellers, targetUrl, properties, "bestseller_product")


    sparkSession.stop()

    
  }
}