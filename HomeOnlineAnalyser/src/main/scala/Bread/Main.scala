package Bread


import java.util.Calendar

import scala.collection.immutable.SortedMap
import scala.io.Source
import spray.json._

import BreadEventJsonProtocol._

/**
 * Created by rik on 11/11/15.
 */
object Main {

  def main(args: Array[String]) {
    val json = Source.fromURL("http://erwinvaneyk.nl/api/product").mkString
    val jsonAST = json.parseJson

    val data = jsonAST.convertTo[List[BreadEvent]]
    val daysToTime = scala.collection.mutable.HashMap.empty[Int,List[Double]]


    val preprocessedData = new Preprocess().doPreprocessing(data)

//    println("before "+data.length)
//    println("after "+preprocessedData.length)


    val string = preprocessedData.mkString("\n")
    //println("productid;user;event;timestamp")
    //println(string)

    val breadQueue = scala.collection.mutable.Queue[BreadEvent]()

    for(bread <- preprocessedData)  {
      if(bread.event == "IN") {
        breadQueue += bread
      }
      else if(breadQueue.nonEmpty)  {
        val inEvent = breadQueue.dequeue()
        val dayOfWeek = inEvent.getDate.get(Calendar.DAY_OF_WEEK)
        val inMap : List[Double] = daysToTime.getOrElse(dayOfWeek, List[Double]())
        val timeDifference = (bread.timestamp - inEvent.timestamp) / (60 * 60 * 24.0)
        println(timeDifference)
        println(inEvent)
        println
        daysToTime += dayOfWeek -> (inMap :+ timeDifference)
      }
    }
    println(SortedMap(daysToTime.toSeq:_*))



  }
}
