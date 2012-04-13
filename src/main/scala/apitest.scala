package li2latex

import org.scribe.builder.api.LinkedInApi
import org.scribe.builder.ServiceBuilder
import org.scribe.oauth.OAuthService
import org.scribe.model.{Verifier, Verb, OAuthRequest, Token}
import org.scribe.exceptions.OAuthException

object APITestConsole {
  private lazy val builder = new ServiceBuilder
  private lazy val service: OAuthService =
    builder provider (new LinkedInApi) apiKey (AppConfig.API_KEY) apiSecret (AppConfig.API_SECRET) build()
  private lazy val accessToken = getAccessToken

  /*
   * A API URL, e.g. http://api.linkedin.com/v1/people/~:(educations)
   */
  val getByUrl: String => String =
    url => {
      val token = accessToken.get
      val req = new OAuthRequest(Verb.GET, url)
      service.signRequest(token, req)
      req.send.getBody
    }

  private val fieldToUrl: String => String =
    field => String.format(AppConfig.API_DATA_URL, field)

  /*
   * A profile field, e.g. educations
   */
  val getByField: String => String = fieldToUrl andThen getByUrl

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
        case _   => None
      }

      nextFunc map { f =>
        print("Enter your " + {if (f == getByField) "Field" else "URL"} + ": ")
        f(readLine())
      } foreach { resp =>
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

  private def getAccessToken : Option[Token] = {
      // Get the Request Token
      val reqToken = service.getRequestToken
      val authUrl = service.getAuthorizationUrl(reqToken)
      println("Please open the following authorization URL:\n")
      println(authUrl)
      println("\nPaste your verifier here:")
      val v = new Verifier(readLine())
      // Get the Access Token
      try {
        val accessToken = service.getAccessToken(reqToken, v)
        Some(accessToken)
      } catch {
        case e: OAuthException => None
      }
  }
}
