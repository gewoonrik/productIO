package nl.tudelft.server

import com.mongodb.casbah.Imports
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.TypeImports
import nl.tudelft.server.models.{MacRecordingEvent, ProductEvent}
import org.scalatra.{InternalServerError, Ok, ActionResult}

// JSON-related libraries
import org.json4s.{DefaultFormats, Formats}
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.json4s.JsonDSL._
import org.json4s.jackson.Serialization.{read, write}
// JSON handling support from Scalatra


class Server extends ProductioStack {

  val mongoDBAddress = sys.env("MONGODB_PORT_27017_TCP_ADDR") + ":" + sys.env("MONGODB_PORT_27017_TCP_PORT")

  val mongoClient = MongoClient(mongoDBAddress , 27017)

  // Sets up automatic case class to JSON output serialization, required by
  // the JValueResult trait.
  protected implicit val jsonFormats: Formats = DefaultFormats

  /*s
   POST product { id }
   */
  post("/api/product/:id") {
    val user = request.getHeader("user")
    val id = params("id")
//    val entry = MongoDBObject("productid" -> id, "time" -> new BSONTimestamp(), "event" -> "IN", "user" -> user)
    var result = Ok()
    ProductEvent.create(new BSONTimestamp(), "IN", user)
    println(user + " posted " + id + "  => " + result)
    result
  }

  delete("/api/product/:id") {
    val user = request.getHeader("user")
    val id = params("id")
//    val entry = MongoDBObject("productid" -> id, "time" -> new BSONTimestamp(), "event" -> "OUT", "user" -> user)
    var result = Ok()
    ProductEvent.create(new BSONTimestamp(), "OUT", user)
    println(user + " deleted " + id + "  => " + result)
    result
  }

  post("/api/home/online") {
    val user = request.getHeader("user")
    var result = Ok()
    var macRecording : Option[MacRecordingInput] = None;
    try {
      macRecording = Some(parse(request.body).extract[MacRecordingInput])
    } catch {
      case e : Throwable => {
        result = InternalServerError("Bad input provided: " + e.getMessage)
        e.printStackTrace()
      }
    }
    try {
      macRecording.map(_.Macs).foreach(mac => {
//        macCollection += MongoDBObject("mac" -> mac, "time" -> macRecording.get.timestamp, "user" -> user)
        MacRecordingEvent.create(new BSONTimestamp, mac.toString, user)
      })
    } catch {
      case e: Throwable => result = InternalServerError("Failed to store entity in mongodb: " + e.getMessage);
    }
    println(user + " posted macs " + macRecording + "  => " + result)
    result
  }

  get("/api/product") {
    write(ProductEvent.all())
  }

  get("/api/home/online") {
    write(MacRecordingEvent.all())
  }

  get("/") {
    "API OK"
  }
}

// Input Json2s DAOs
case class MacRecordingInput(Macs : Array[String], Timestamp: Long)
