package modules

import com.google.inject.AbstractModule
import play.api.libs.concurrent.AkkaGuiceSupport
import workers.{ NewsProcessSupervisor, WorkerBootstrap }

class WorkerBootstrapModule extends AbstractModule with AkkaGuiceSupport {

  override def configure(): Unit = {
    bind(classOf[WorkerBootstrap]).asEagerSingleton()

    bindActor[NewsProcessSupervisor]("newsprocessor")
  }
}

