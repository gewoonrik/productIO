package nl.tudelft.server.models

import com.mongodb.casbah.MongoConnection
import com.mongodb.casbah.commons.MongoDBObject
import com.novus.salat.dao.SalatDAO
import org.bson.types.{BSONTimestamp, ObjectId}
import com.novus.salat._
import com.novus.salat.annotations._
import com.novus.salat.global._

case class ProductEvent(_id: ObjectId = new ObjectId, time: BSONTimestamp, event : String, user : String) extends Event

object ProductEventDAO extends SalatDAO[ProductEvent, ObjectId](
  collection = MongoConnection()("productio")("product"))

object ProductEvent {
  def all(): List[ProductEvent] = ProductEventDAO.find(MongoDBObject.empty).toList

  def create(time: BSONTimestamp, event : String, user : String): Option[ObjectId] = {
    ProductEventDAO.insert(ProductEvent(time = time, event = event, user = user))
  }
}
