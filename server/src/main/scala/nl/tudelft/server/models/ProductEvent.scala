package nl.tudelft.server.models

import com.mongodb.casbah.MongoConnection
import com.mongodb.casbah.commons.MongoDBObject
import com.novus.salat.dao.SalatDAO
import nl.tudelft.server.ApiServer
import org.bson.types.{BSONTimestamp, ObjectId}
import com.novus.salat._
import com.novus.salat.annotations._
import com.novus.salat.global._

case class ProductEvent(_id: ObjectId = new ObjectId, productid : String, time : BSONTimestamp,
                        timestamp : Option[Long], event : String, user : String) extends Event {
}

object ProductEventDAO extends SalatDAO[ProductEvent, ObjectId](
  collection = ApiServer.mongoClient("productio")("products"))

object ProductEvent {
  def all(): List[ProductEvent] = addTimestamps(ProductEventDAO.find(MongoDBObject.empty).toList)

  def create(time: BSONTimestamp, productid : String, event : String, user : String): (ProductEvent, ObjectId) = {
    val product = ProductEvent(time = time, timestamp = Some(time.getTime), event = event, productid = productid, user = user)
    val mongoId = ProductEventDAO.insert(product).get
    (product, mongoId)
  }

  def addTimestamps(products : List[ProductEvent]): List[ProductEvent] = {
    products.map(product => product.copy(timestamp = Some(product.time.getTime)))
  }
}
