package Online


import org.joda.time.LocalDate


/**
 * Created by rik on 17/11/15.
 */
case class Day(realDay: LocalDate, onlineData: List[OnlineData]) { /*hourToPersons : Map[Int, Int]) {*/
  val validMacs = List("40:F3:08:F2:9B:79", "78:6C:1C:C7:05:46", "88:C9:D0:EB:22:AF", "BC:F5:AC:FB:31:71")


  private def getPeopleHomePerHour() : Map[Int, Int] = {
    onlineData
      .groupBy(data => data.getDateTime.getHourOfDay)
      .map({
      case (hour, data: List[OnlineData]) => {
        hour -> data.flatMap(
          data => data.getMacs
            .filter(mac => validMacs.contains(mac))
        ).distinct.size
      }
    })
  }

  //returns the macs of mobile phones online, per hour
  private def getMobileMacsPerHour() : Map[Int, List[String]] = {
    onlineData
      .groupBy(data => data.getDateTime.getHourOfDay)
      .map({
      case (hour, data: List[OnlineData]) => {
        hour -> data.flatMap(
          data => data.getMacs
            .filter(mac => validMacs.contains(mac))
        ).distinct
      }
    })
  }

  // not including endHour, but including startHour
  def getAveragePeopleHomeBetween(startHour: Int, endHour: Int): Double = {
    val peopleHomePerHour = getPeopleHomePerHour

    val peopleHome =
      (startHour until endHour)
        .filter(peopleHomePerHour.isDefinedAt)
        .map(peopleHomePerHour.get)
        .map(_.get)

    peopleHome.sum/peopleHome.size.asInstanceOf[Double]
  }

  def getDistinctPeopleHomeBetween(startHour: Int, endHour: Int): Int = {
    val macsPerHour = getMobileMacsPerHour
      (startHour until endHour)
        .filter(macsPerHour.isDefinedAt)
        .map(macsPerHour.get)
        .flatMap(_.get)
        .distinct
        .size
  }
}


object Day {

  def getDays : List[Day] = {
    import Online.DataJsonProtocol._
    import spray.json._
    import scala.io.Source
    val json = Source.fromURL("http://erwinvaneyk.nl/api/home/online").mkString
    val jsonAST = json.parseJson
    val data = jsonAST.convertTo[List[OnlineData]]
    onlineDataToDays(data)
  }

  def onlineDataToDays(onlineData : List[OnlineData]) : List[Day] = {
    onlineData
      .filter(data => data.mac != "")
      .filter(data => data.user == "Rik")
      .groupBy(data => new LocalDate(data.getDateTime))
      .map {
      case (day, data: List[OnlineData]) => {
        Day(day, data)
      }
    }.toList
  }


}