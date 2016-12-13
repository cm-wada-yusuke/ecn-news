package controllers

import javax.inject.Inject

import play.api.mvc.{ Action, AnyContent, Controller }

class ApplicationController @Inject()(
    environment: Environment
) extends Controller {

  def healthCheck: Action[AnyContent] = Action {
    Ok("Environment: " + environment.name)
  }

}
