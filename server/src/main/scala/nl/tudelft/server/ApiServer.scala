package nl.tudelft.server

import com.mongodb.casbah.Imports
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.TypeImports
import nl.tudelft.server.models.{MacRecordingEvent, ProductEvent}
import org.bson.types.BSONTimestamp
import org.scalatra.{InternalServerError, Ok, ActionResult}
import org.json4s.jackson.Serialization
import org.slf4j.LoggerFactory

// JSON-related libraries
import org.json4s.{DefaultFormats, Formats}
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.json4s.JsonDSL._
import org.json4s.jackson.Serialization.{read, write}
// JSON handling support from Scalatra

object ApiServer {

  val logger =  LoggerFactory.getLogger(getClass)

  // Test (after vagrant up)
  var mongoDBAddress = "localhost:27017"
  try {
    // Production
    mongoDBAddress = sys.env("MONGODB_PORT_27017_TCP_ADDR") + ":" + sys.env("MONGODB_PORT_27017_TCP_PORT")
  } catch {
    case e : NoSuchElementException => logger.warn("No environment variables found for mongo-database! " +
      "Falling back to dev-settings: localhost:27017")
  }

  val mongoClient = MongoClient(mongoDBAddress , 27017)
}

class ApiServer extends ProductioStack {

  // Sets up automatic case class to JSON output serialization, required by
  // the JValueResult trait.
  protected implicit val jsonFormats: Formats = DefaultFormats

  val logger =  LoggerFactory.getLogger(getClass)

  /**
   * Add a product
   *
   * Example: curl -X POST --header "user: test" http://localhost:8080/api/product/123
   */
  post("/api/product/:id") {
    val user = request.getHeader("user")
    val id = params("id")
    try {
      val result = ProductEvent.create(new BSONTimestamp(), id,  "IN", user)
      logger.info(user + " posted " + id + "  => " + result)
      write(result._1)
    } catch {
      case e: Throwable => InternalServerError("Failed to add product, due to: " + e.getMessage)
    }
  }

  /**
   * Remove a product
   *
   * Example: curl -X DELETE --header "user: test" http://localhost:8080/api/product/123
   */
  delete("/api/product/:id") {
    val user = request.getHeader("user")
    val id = params("id")

    try {
      val result = ProductEvent.create(new BSONTimestamp(), id, "OUT", user)
      logger.info(user + " deleted " + id + "  => " + result)
      write(result._1)
    } catch {
      case e: Throwable => InternalServerError("Failed to delete product, due to: " + e.getMessage)
    }
  }

  get("/api/product") {
    write(ProductEvent.all())
  }

  // Homeserver
  /**
   * Add a mac recording
   *
   * Example: curl -X POST -H "Content-Type: application/json" --header "user: test" -d '{ "macs" : ["mac1"], "timestamp" : 13233  }' http://localhost:8080/api/home/online
   */
  post("/api/home/online") {
    val user = request.getHeader("user")
    var result = Ok()
    var macRecording : Option[MacRecordingInput] = None
    val body = request.body
    if(body == null || body.length == 0) {
      InternalServerError("No body provided")
    }
    
    try {
      macRecording = Some(parse(body).extract[MacRecordingInput])
    } catch {
      case e : Throwable => {
        InternalServerError("Bad input provided: " + e.getMessage)
        e.printStackTrace()
      }
    }
    try {
      macRecording.map(_.Macs).foreach(mac => {
//        macCollection += MongoDBObject("mac" -> mac, "time" -> macRecording.get.Timestamp, "user" -> user)
        MacRecordingEvent.create(macRecording.get.Timestamp, mac.mkString("|"), user)
      })
    } catch {
      case e: Throwable => result = InternalServerError("Failed to store entity in mongodb: " + e.getMessage);
    }
    logger.info(user + " posted macs " + macRecording + "  => " + result)
    result
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
