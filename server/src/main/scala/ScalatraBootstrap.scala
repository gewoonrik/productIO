import nl.tudelft._
import nl.tudelft.server.server
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context.mount(new server, "/api/")
  }
}
