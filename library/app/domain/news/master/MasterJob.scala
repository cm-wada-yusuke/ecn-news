package domain.news.master

import domain.news.Job

case class MasterJob(
    newsId: String,
    title: String
) extends Job
