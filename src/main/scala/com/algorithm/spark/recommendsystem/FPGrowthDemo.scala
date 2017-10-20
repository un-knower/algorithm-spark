package com.algorithm.spark.recommendsystem

import org.apache.spark.mllib.fpm.FPGrowth
import org.apache.spark.{SparkConf, SparkContext}

object FPGrowthDemo {

  def main(args: Array[String]): Unit = {
    System.setProperty("hadoop.home.dir", "D:\\software2\\hadoop-2.7.4")
    val conf = new SparkConf().setAppName("FPGrowthDemo")
      .setMaster("local[*]")
    val sc = new SparkContext(conf)

    val dataArray: Array[Array[String]] = Array(Array("A", "B", "C", "E", "F","O"),
      Array("A", "C", "G"), Array("E","I"), Array("A", "C","D","E","G"),
      Array("A", "C", "E","G","L"), Array("E","J"), Array("A","B","C","E","F","P"),
      Array("A","C","D"), Array("A","C","E","G","M"), Array("A","C","E","G","N"))
    val dataRDD = sc.makeRDD(dataArray, 2)
    val fpg: FPGrowth = new FPGrowth()
      .setMinSupport(0.2)
      .setNumPartitions(2)
    val model =  fpg.run(dataRDD)
    model.freqItemsets.collect().foreach(println)
    model.freqItemsets.collect().sortBy(item => item.freq).foreach { itemset =>
      println(itemset.items.mkString("[", ",", "]") + ", " + itemset.freq)
    }

    sc.stop()
  }

}
