package Online

import java.util.Calendar

import org.joda.time.DateTime
import spray.json.DefaultJsonProtocol

/**
 * Created by rik on 08/11/15.
 */
case class OnlineData(mac: String, time: Long, user: String) {

  def getDate : Calendar = {
    val cal = Calendar.getInstance()
    cal.setTimeInMillis(time*1000+60*60*1000) //add an hour, because erwins server sucks
    cal
  }

  def getDateTime : DateTime = {
    new DateTime(time*1000+60*60*1000) //add an hour, because erwins server sucks

  }

  def getMacs : List[String] = {
    mac.split("\\|").filter(str => !str.isEmpty).toList
  }
}

object DataJsonProtocol extends DefaultJsonProtocol {
  implicit val dataFormat = jsonFormat3(OnlineData)
}