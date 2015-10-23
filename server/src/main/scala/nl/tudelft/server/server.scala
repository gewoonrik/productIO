package nl.tudelft.server

import com.mongodb.casbah.Imports._
import org.scalatra.{InternalServerError, Ok, ActionResult}

// JSON-related libraries
import org.json4s.{DefaultFormats, Formats}
import org.json4s._
import org.json4s.jackson.JsonMethods._
// JSON handling support from Scalatra


class Server extends ProductioStack {

  val mongoDBAddress = sys.env("MONGODB_PORT_27017_TCP_ADDR") + ":" + sys.env("MONGODB_PORT_27017_TCP_PORT")

  val mongoClient = MongoClient(mongoDBAddress , 27017)
  val productCollection = mongoClient("productio")("products")
  val macCollection = mongoClient("productio")("macs")

  // Sets up automatic case class to JSON output serialization, required by
  // the JValueResult trait.
  protected implicit val jsonFormats: Formats = DefaultFormats

  /*s
   POST product { id }
   */
  post("/api/product/:id") {
    val user = request.getHeader("user")
    val id = params("id")
    val entry = MongoDBObject("productid" -> id, "time" -> new BSONTimestamp(), "event" -> "IN", "user" -> user)
    var result = Ok()
    try {
      productCollection += entry
    } catch {
      case e: Throwable => result = InternalServerError(e.getMessage);
    }
    println(user + " posted " + id + "  => " + result)
    result
  }

  delete("/api/product/:id") {
    val user = request.getHeader("user")
    val id = params("id")
    val entry = MongoDBObject("productid" -> id, "time" -> new BSONTimestamp(), "event" -> "OUT", "user" -> user)
    var result = Ok()
    try {
      productCollection += entry
    } catch {
      case e: Throwable => result = InternalServerError(e.getMessage);
    }
    println(user + " deleted " + id + "  => " + result)
    result
  }

  post("/api/home/online") {
    val user = request.getHeader("user")
    var result = Ok()
    var macRecording : Option[MacRecording] = None
    try {
      println(request.body)
      macRecording = Some(parse(request.body).extract[MacRecording])
    } catch {
      case e : Throwable => {
        result = InternalServerError("Bad input provided: " + e.getMessage)
        e.printStackTrace();
      }
    }
    try {
      macRecording.map(_.Macs).foreach(mac => {
        macCollection += MongoDBObject("mac" -> mac, "time" -> macRecording.get.Timestamp, "user" -> user)
      })
    } catch {
      case e: Throwable => result = InternalServerError("Failed to store entity in mongodb: " + e.getMessage);
    }
    println(user + " posted macs " + macRecording + "  => " + result)
    result
  }

  get("/") {
    "API OK"
  }
}

case class MacRecording(Macs : Array[String], Timestamp: Long)