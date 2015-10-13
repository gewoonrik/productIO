package nl.tudelft.server

import com.mongodb.casbah.Imports._

class server extends ProductioStack {

  val mongoClient = MongoClient("localhost", 27017)
  val productCollection = mongoClient("productio")("products")

  /*
   POST product { id }
   */
  post("/product/:id") {
    val user = request.getHeader("user")
    val id = params("id")
    val entry = MongoDBObject("productid" -> id, "time" -> new BSONTimestamp(), "event" -> "IN", "user" -> user)
    var result = "OK"
    try {
      productCollection += entry
    } catch {
      case e: Throwable => result = e.getMessage;
    }
    println(user + " posted " + id + "  => " + result)
    result
  }

  delete("/product/:id") {
    val user = request.getHeader("user")
    val id = params("id")
    val entry = MongoDBObject("productid" -> id, "time" -> new BSONTimestamp(), "event" -> "OUT", "user" -> user)
    var result = "OK"
    try {
      productCollection += engenry
    } catch {
      case e: Throwable => result = e.getMessage;
    }
    println(user + " deleted " + id + "  => " + result)
    result
  }
}