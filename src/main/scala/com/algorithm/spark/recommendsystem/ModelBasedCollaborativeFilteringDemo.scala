package com.algorithm.spark.recommendsystem

import org.apache.spark.mllib.recommendation.{ALS, Rating}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * author: tangjun
  * time: 2017/10/20 15:05
  * description:
  */
object ModelBasedCollaborativeFilteringDemo {

  def main(args: Array[String]): Unit = {
    System.setProperty("hadoop.home.dir", "D:\\software2\\hadoop-2.7.4")
    val conf = new SparkConf().setAppName("ModelBasedCollaborativeFilteringDemo")
      .setMaster("local[*]")
    val sc = new SparkContext(conf)

    // Load and parse the data
    val data = sc.textFile("data/mllib/als/test.data")
    val ratings = data.map(_.split(',') match { case Array(user, item, rate) =>
      Rating(user.toInt, item.toInt, rate.toDouble)
    })

    // Build the recommendation model using ALS
    val rank = 10
    val numIterations = 10
    val model = ALS.train(ratings, rank, numIterations, 0.01)

    // Evaluate the model on rating data
    val usersProducts = ratings.map { case Rating(user, product, rate) =>
      (user, product)
    }
    val predictions =
      model.predict(usersProducts).map { case Rating(user, product, rate) =>
        ((user, product), rate)
      }
    val ratesAndPreds = ratings.map { case Rating(user, product, rate) =>
      ((user, product), rate)
    }.join(predictions)
    val MSE = ratesAndPreds.map { case ((user, product), (r1, r2)) =>
      val err = (r1 - r2)
      err * err
    }.mean()
    println("Mean Squared Error = " + MSE)

    sc.stop()
  }

}
