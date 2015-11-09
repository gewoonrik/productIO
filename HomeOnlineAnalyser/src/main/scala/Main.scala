/**
 * Created by rik on 08/11/15.
 */

import java.util.Calendar

import scala.collection.immutable.SortedMap
import scala.io.Source
import spray.json._
import DataJsonProtocol._


object Main {
  def main(args: Array[String]) {
    val json = Source.fromURL("http://erwinvaneyk.nl/api/home/online").mkString
    val jsonAST = json.parseJson

    val data = jsonAST.convertTo[List[Data]]
      .filter(data => data.mac != "")
      .filter(data => data.user == "Rik")
      .groupBy(data => data.getDate.get(Calendar.DAY_OF_WEEK))
      .map(tuple =>
          (tuple._1,
            tuple._2.groupBy(data => data.getDate.get(Calendar.HOUR_OF_DAY))
              .map(tuple => (tuple._1, tuple._2.flatMap(data => data.getMacs).distinct.size))
          ))

    println(SortedMap(data.get(2).get.toSeq:_*))

  }
}
