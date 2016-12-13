package modules

import javax.inject.Named

import akka.actor.ActorSystem
import com.amazonaws.regions.{ Region, Regions }
import com.amazonaws.services.sqs.AmazonSQSClient
import com.google.inject.{ AbstractModule, Provides, Singleton }
import controllers.Environment
import infrastructure.NewsQueueClient
import infrastructure.config.NewsQueueConfig
import play.api.Configuration
import services.NewsQueue
import workers.config.NewsConfig

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

class DependencyModule extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[NewsQueue]).to(classOf[NewsQueueClient])
  }

  @Provides
  def provideEnvironmentConfig(configuration: Configuration): Environment =
    Environment(configuration.getString("environment").get)

  @Provides
  @Singleton
  @Named("aws")
  def provideAWSExecutionContext(system: ActorSystem): ExecutionContext =
    system.dispatchers.lookup("executionContexts.aws")

  @Provides
  def provideSqsClient: AmazonSQSClient = {
    val sQSClient = new AmazonSQSClient()
    sQSClient.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1))
    sQSClient
  }

  @Provides
  @Singleton
  def providePointUpdateQueueURL(configuration: Configuration): NewsQueueConfig =
    NewsQueueConfig(configuration.getString("sqs.news.queueURL").get)

  @Provides
  def provideNewsConfig(configuration: Configuration): NewsConfig =
    NewsConfig(configuration.getInt("worker.news.pollingInterval").get.seconds)


}
