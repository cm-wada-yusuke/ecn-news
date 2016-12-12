package common

import akka.actor.{ ActorSelection, ActorSystem }

trait NewsActorTestHelper {

  def retrieveSupervisorActorRef(implicit system: ActorSystem): ActorSelection =
    system.actorSelection("/user/newsprocessor")

}
