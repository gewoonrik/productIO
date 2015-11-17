package Online

/**
 * Created by rik on 08/11/15.
 */

import java.util.Calendar

import Online.DataJsonProtocol._
import spray.json._

import scala.collection.immutable.SortedMap
import scala.io.Source


object Main {
  def main(args: Array[String]) {
    val json = Source.fromURL("http://erwinvaneyk.nl/api/home/online").mkString
    val jsonAST = json.parseJson
    val validMacs = List("40:F3:08:F2:9B:79","78:6C:1C:C7:05:46", "88:C9:D0:EB:22:AF","BC:F5:AC:FB:31:71")
    val data = jsonAST.convertTo[List[OnlineData]]
      .filter(data => data.mac != "")
      .filter(data => data.user == "Rik")
      .filter(data=> data.getDate.get(Calendar.DAY_OF_MONTH)>6)
      .groupBy(data => data.getDate.get(Calendar.DAY_OF_WEEK))
      .map(tuple =>
          (tuple._1,
            tuple._2.groupBy(data => data.getDate.get(Calendar.HOUR_OF_DAY))
              .map(tuple => (tuple._1, tuple._2.flatMap(
              data => data.getMacs.filter({
                mac =>     validMacs.contains(mac)
              })).distinct.size))
          ))

    println(SortedMap(data.get(Calendar.SUNDAY).get.toSeq:_*))

  }
}
