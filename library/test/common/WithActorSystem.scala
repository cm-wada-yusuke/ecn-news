package common

import akka.actor.ActorSystem
import akka.testkit.{ ImplicitSender, TestKit }
import org.scalatest.{ BeforeAndAfterAll, Suite }

abstract class WithActorSystem extends TestKit(ActorSystem("TestSystem"))
    with Suite
    with BeforeAndAfterAll
    with ImplicitSender {

  override protected def afterAll(): Unit = {
    system.terminate()
    Thread.sleep(1000) // wait for all actor system terminate.
  }

}
