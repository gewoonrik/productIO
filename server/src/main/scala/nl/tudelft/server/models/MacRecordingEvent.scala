package nl.tudelft.server.models

import com.mongodb.casbah.MongoConnection
import com.mongodb.casbah.commons.MongoDBObject
import com.novus.salat.dao.SalatDAO
import nl.tudelft.server.ApiServer
import org.bson.types.{BSONTimestamp, ObjectId}
import com.novus.salat._
import com.novus.salat.annotations._
import com.novus.salat.global._

case class MacRecordingEvent(_id: ObjectId = new ObjectId, id : Option[String], mac : String, time: Long, user : String) extends Event

object MacRecordingEventDAO extends SalatDAO[MacRecordingEvent, ObjectId](
  collection = ApiServer.mongoClient("productio")("macs"))

object MacRecordingEvent {
  def all(): List[MacRecordingEvent] = MacRecordingEventDAO.find(MongoDBObject.empty).toList

  def create(time: Long, mac : String, user : String) : Option[ObjectId] = {
    MacRecordingEventDAO.insert(MacRecordingEvent(time = time, mac = mac, id = None, user = user))
  }
}
