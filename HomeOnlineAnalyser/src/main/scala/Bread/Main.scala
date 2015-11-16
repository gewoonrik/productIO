package Bread


import java.util.Calendar

import scala.io.Source
import spray.json._

import BreadJsonProtocol._

/**
 * Created by rik on 11/11/15.
 */
object Main {

  def main(args: Array[String]) {
    val json = Source.fromURL("http://erwinvaneyk.nl/api/product").mkString
    val jsonAST = json.parseJson

    val data = jsonAST.convertTo[List[Bread]]
    val daysToTime = scala.collection.mutable.HashMap.empty[Int,List[Long]]

    val breadQueue = scala.collection.mutable.Queue[Bread]()

    val preprocessedData = new Preprocess().doPreprocessing(data)

//    println("before "+data.length)
//    println("after "+preprocessedData.length)


    val string = preprocessedData.map(BreadToCsv.breadToCsv).mkString("\n")
    println("productid;user;event;timestamp")
    println(string)

    for(bread <- preprocessedData)  {
      if(bread.event == "IN") {
        breadQueue += bread
      }
      else if(breadQueue.nonEmpty)  {
        val inEvent = breadQueue.dequeue()
        val dayOfWeek = inEvent.getDate.get(Calendar.DAY_OF_WEEK)
        val inMap : List[Long] = daysToTime.getOrElse(dayOfWeek, List[Long]())
        val timeDifference : Long = ((bread.timestamp - inEvent.timestamp)/(60*60*24.0)).asInstanceOf[Long]
        //if(timeDifference <= 3) {
          daysToTime += dayOfWeek -> (inMap :+ timeDifference)
        //}
        println(timeDifference)
      }
    }
    println(daysToTime)


  }
}
