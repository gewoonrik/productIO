package Bread

import java.util.Calendar

import BreadEventJsonProtocol._
import spray.json._

import scala.io.Source

/**
 * Created by rik on 11/11/15.
 */
object BreadBeginEnd {

  def main(args: Array[String]) {
    val json = Source.fromURL("http://erwinvaneyk.nl/api/product").mkString
    val jsonAST = json.parseJson

    val data = jsonAST.convertTo[List[BreadEvent]]


    val preprocessedData = new Preprocess().doPreprocessing(data)


    val breadQueue = scala.collection.mutable.Queue[BreadEvent]()

    var cur = 1
    for(bread <- preprocessedData)  {
      if(bread.event == "IN") {
        breadQueue += bread
      }
      else if(breadQueue.nonEmpty)  {
        val inEvent = breadQueue.dequeue()
        println(cur+";"+inEvent.timestamp+";"+bread.timestamp)
        cur+=1
      }
    }



  }
}
