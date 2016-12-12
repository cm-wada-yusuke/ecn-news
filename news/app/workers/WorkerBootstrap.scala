package workers

import javax.inject.Named

import akka.actor.ActorRef
import com.google.inject.{ Inject, Singleton }

/**
 * ワーカー起動モジュール
 */
@Singleton
class WorkerBootstrap @Inject()(
    @Named("newsprocessor") newsProcessor: ActorRef
)
