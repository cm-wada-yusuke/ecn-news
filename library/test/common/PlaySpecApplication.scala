package common

import org.scalatestplus.play.PlaySpec
import play.api.http.{ HeaderNames, HttpProtocol, HttpVerbs, Status }
import play.api.test._

class PlaySpecApplication extends PlaySpec
    with PlayRunners
    with HeaderNames
    with Status
    with HttpProtocol
    with DefaultAwaitTimeout
    with ResultExtractors
    with Writeables
    with RouteInvokers
    with FutureAwaits
    with HttpVerbs

