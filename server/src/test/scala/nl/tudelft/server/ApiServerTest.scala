package nl.tudelft.server

import org.scalatra.test.scalatest._
import org.scalatest.FunSuiteLike

class ApiServerTest extends ScalatraSuite with FunSuiteLike {
  addServlet(classOf[ApiServer], "/*")

  test("Issue new product OUT-event") {
    delete("/product/1", Map("user" -> "test")) {
      status should equal (200)
    }
  }

  test("Post new product IN-event") {
    post("/product/1", Map("user" -> "test")) {
      status should equal (200)
    }
  }

  test("Post new mac addresses") {
    post("/home/online", "{\n    \"macs\": [\n \"123\",\n \"124\"\n],\n    \"timestamp\": 1337\n}", Map("user" -> "test")) {
      status should equal (200)
    }
  }
}