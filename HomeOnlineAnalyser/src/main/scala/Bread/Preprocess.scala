package Bread

import scala.collection.mutable

/**
 * Created by rik on 11/11/15.
 */
class Preprocess {

  //is used for padding when using sliding windows
  val dummyBreadEvent = BreadEvent("null", 0, "IN", "null")

  def doPreprocessing(breadEvents : List[BreadEvent]): List[BreadEvent] =  {
    breadEvents.sliding(3)
    (removeFastCheckinCheckout _ andThen removeDoubleEvents andThen removeFaultyevents andThen transformNestedBreads) (breadEvents)
  }

  def removeFastCheckinCheckout(breadEvents : List[BreadEvent]): List[BreadEvent] =  {
    //filters two events
    //when: check in -> check out of a bread is under 10 seconds, it filters both
    (dummyBreadEvent +: breadEvents :+ dummyBreadEvent).sliding(3)
      .filterNot({
        case List(prev, cur, next) => {
          (prev.event != cur.event && cur.timestamp - prev.timestamp <= 10 && prev.productid == cur.productid) ||
            (cur.event != next.event && next.timestamp - cur.timestamp <= 10 && cur.productid == next.productid)

        }
      })
      .map({
        case List(prev, cur, next) => cur
      }).toList
  }

  def removeDoubleEvents(breadEvents : List[BreadEvent]): List[BreadEvent] =  {
    (dummyBreadEvent +: breadEvents).sliding(2)
      .filterNot({
        case List(prev, cur) => {
          prev.productid == cur.productid && cur.timestamp-prev.timestamp <= 10 && prev.event == cur.event
        }
      })
      .map({
      case List(prev, cur)=> cur
    }).toList
  }

  def removeFaultyevents(breadEvents : List[BreadEvent]) : List[BreadEvent] = {
    breadEvents.filter(bread =>{
      bread.timestamp <1444996256 || bread.timestamp >1445672111
    })
      .filter(bread => {
      bread.timestamp != 1444813252
    })
  }

  def transformNestedBreads(breadEvents : List[BreadEvent]) : List[BreadEvent] = {
    val result = new scala.collection.mutable.ListBuffer[BreadEvent]()
    val nestedBreads = new scala.collection.mutable.Queue[BreadEvent]()

    for(breadEvent <- breadEvents)  {
      if(breadEvent.event == "IN")  {
        nestedBreads.enqueue(breadEvent)
      }
      else if(breadEvent.event == "OUT")  {
        val inEvent = nestedBreads.dequeue()
        result += inEvent
        result += breadEvent
        if(nestedBreads.nonEmpty) {
          val nested = nestedBreads.dequeue()
          val newCheckIn = BreadEvent(nested.productid, breadEvent.timestamp, "IN", nested.user)
          nestedBreads.+=:(newCheckIn)
        }
      }
    }
    result.toList
  }



//  def transformNestedBreads(breadEvents : List[Bread]) : List[Bread] = {
//    case a@Bread(p1, _, "IN",_) :: (b@Bread(p2, _, "OUT",_) :: tail) if p1 == p2 => a :: (b :: tail)
//
//  }

}
