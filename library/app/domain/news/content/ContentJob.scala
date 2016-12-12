package domain.news.content

import domain.news.Job

case class ContentJob(
    userId: Long,
    newsId: String
) extends Job
