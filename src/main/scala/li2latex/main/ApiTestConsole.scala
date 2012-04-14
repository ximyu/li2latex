package li2latex.main

import li2latex.oauth.OAuthClient

object APITestConsole extends OAuthClient {
  /*
   * Run the API Test Console from here
   */
  def start() {

    def getInstruction(): Unit = {
      println("Choose how you would like to test the API:")
      println("(Note that the first time you use it you may need to login to LinkedIn)")
      println("[f] Field, [u] URL, [q] Quit")

      val nextFunc = readLine().toLowerCase match {
        case "f" => Some(getByField)
        case "u" => Some(getByUrl)
        case _ => None
      }

      nextFunc map {
        f =>
          print("Enter your " + {
            if (f == getByField) "Field" else "URL"
          } + ": ")
          f(readLine())
      } foreach {
        resp =>
          println("LinkedIn Response:")
          println(resp)
          getInstruction()
      }
    }

    accessToken match {
      case Some(token) => getInstruction()
      case None => println("Cannot retrieve access token from LinkedIn! Sorry...")
    }
  }
}
