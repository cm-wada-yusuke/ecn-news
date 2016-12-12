package workers.config

import scala.concurrent.duration.FiniteDuration

/**
 * ニュース・ワーカーの設定VO。
 */
case class NewsConfig(pollingInterval: FiniteDuration)
