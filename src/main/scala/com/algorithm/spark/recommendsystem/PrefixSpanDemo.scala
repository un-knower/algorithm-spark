package com.algorithm.spark.recommendsystem

import org.apache.spark.mllib.fpm.PrefixSpan
import org.apache.spark.{SparkConf, SparkContext}

object PrefixSpanDemo {

  def main(args: Array[String]): Unit = {
    System.setProperty("hadoop.home.dir", "D:\\software2\\hadoop-2.7.4")
    val conf = new SparkConf().setAppName("PrefixSpanDemo")
      .setMaster("local[*]")
    val sc = new SparkContext(conf)

    val sequences = sc.parallelize(Seq(
      Array(Array(1, 2), Array(3)),
      Array(Array(1), Array(3, 2), Array(1, 2)),
      Array(Array(1, 2), Array(5)),
      Array(Array(6))
    ), 2).cache()
    val prefixSpan = new PrefixSpan()
      .setMinSupport(0.5)
      .setMaxPatternLength(5)
    val model = prefixSpan.run(sequences)
    model.freqSequences.collect().foreach { freqSequence =>
      println(
        freqSequence.sequence.map(_.mkString("[", ", ", "]")).mkString("[", ", ", "]") +
          ", " + freqSequence.freq)
    }

    sc.stop()
  }

}
