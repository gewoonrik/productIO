import java.util.{Calendar, Date}

import spray.json.DefaultJsonProtocol

/**
 * Created by rik on 08/11/15.
 */
case class Data(mac: String, time: Long, user: String) {

  def getDate : Calendar = {
    val cal = Calendar.getInstance()
    cal.setTimeInMillis(time*1000+60*60*1000) //add an hour, because erwins server sucks
    cal
  }

  def getMacs : List[String] = {
    mac.split("\\|").filter(str => !str.isEmpty).toList
  }
}

object DataJsonProtocol extends DefaultJsonProtocol {
  implicit val dataFormat = jsonFormat3(Data)
}