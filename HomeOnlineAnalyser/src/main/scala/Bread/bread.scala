package Bread

import java.util.Calendar

import spray.json.DefaultJsonProtocol


/**
 * Created by rik on 11/11/15.
 */
case class Bread(productid: String, timestamp: Long, event: String, user: String) {

  def getDate : Calendar = {
    val cal = Calendar.getInstance()
    cal.setTimeInMillis(timestamp*1000+60*60*1000) //add an hour, because erwins server sucks
    cal
  }

  override def toString() : String =
  {
    productid+"; "+getDate.toInstant+"; "+timestamp+"; "+event
  }
}

object BreadJsonProtocol extends DefaultJsonProtocol {
  implicit val dataFormat = jsonFormat4(Bread)
}

object BreadToCsv
{
  def breadToCsv(bread: Bread) : String = {
    return bread.productid+";"+bread.user+";"+bread.event+";"+bread.timestamp
  }
}
// productid; user; event;timestamp