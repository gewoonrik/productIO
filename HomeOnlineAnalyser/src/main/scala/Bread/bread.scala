package Bread

import com.github.nscala_time.time.Imports._
import org.joda.time.Days

import scala.io.Source

/**
 * Created by rik on 17/11/15.
 */
case class Bread(productId : String, in: Long, out: Long) {

  def getDateTimeIn : DateTime = {
    val dateTimeIn = new DateTime(in*1000+60*60*1000)
    //if the check in was after 13:59, then don't count that day
    if(dateTimeIn.getHourOfDay > 13) {
      return dateTimeIn + 1.day
    }
    dateTimeIn
  }

  def getDateTimeOut : DateTime = {
    val dateTimeOut = new DateTime(out*1000+60*60*1000)
    //if the check out was before 11 then don't count this day, since it was gone for most of the day, probably at the start
    if(dateTimeOut.getHourOfDay < 11) {
      return dateTimeOut - 1.day
    }
    dateTimeOut

  }

  def getDaysEaten : List[DateTime] = {
    val in = getDateTimeIn
    val out = getDateTimeOut

    val dayIn = new LocalDate(in)
    val dayOut = new LocalDate(out)
    val numberOfDays = Days.daysBetween(dayIn, dayOut).getDays
    (0 to numberOfDays)
      .map(days => in +days.days)
      .toList
  }

}



object Bread
{

  //transforms tuples of checkin-checkouts to a bread
  //assumes that there are no nested breads in the eventstream
  private def breadEventsToBreads(breadEvents: List[BreadEvent]) : List[Bread] = {
    breadEvents.sliding(2,2)
      .filter(events => events(0).event != events(1).event)
      .map(events => Bread(events(0).productid, events(0).timestamp, events(1).timestamp)).toList
  }

  def getBreads : List[Bread] = {
    import BreadEventJsonProtocol._
    import spray.json._

    val json = Source.fromURL("http://erwinvaneyk.nl/api/product").mkString
    val jsonAST = json.parseJson

    val data = jsonAST.convertTo[List[BreadEvent]]
    val preprocessedData = new Preprocess().doPreprocessing(data)

    breadEventsToBreads(preprocessedData)
  }

}