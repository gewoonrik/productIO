package nl.tudelft.server.models

import com.mongodb.casbah.MongoConnection
import com.mongodb.casbah.commons.MongoDBObject
import com.novus.salat.dao.SalatDAO
import org.bson.types.{BSONTimestamp, ObjectId}
import com.novus.salat._
import com.novus.salat.annotations._
import com.novus.salat.global._

case class MacRecordingEvent(_id: ObjectId = new ObjectId, mac : String, time: BSONTimestamp, user : String) extends Event

object MacRecordingEventDAO extends SalatDAO[MacRecordingEvent, ObjectId](
  collection = MongoConnection()("productio")("macs"))

object MacRecordingEvent {
  def all(): List[MacRecordingEvent] = MacRecordingEventDAO.find(MongoDBObject.empty).toList

  def create(time: BSONTimestamp, mac : String, user : String) : Option[ObjectId] = {
    MacRecordingEventDAO.insert(MacRecordingEvent(time = time, mac = mac, user = user))
  }
}
