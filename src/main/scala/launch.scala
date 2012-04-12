package li2latex

import org.scribe.model._
import org.scribe.oauth._
import org.scribe.builder.ServiceBuilder
import org.scribe.builder.api._

object Launcher extends App {

  val service: OAuthService = new ServiceBuilder()
                              .provider(new LinkedInApi)
                              .apiKey(AppConfig.API_KEY)
                              .apiSecret(AppConfig.API_SECRET)
                              .build()
  // Get the Request Token
  val reqToken = service getRequestToken ()
  val authUrl = service getAuthorizationUrl reqToken
  Console println "Please open the following authorization URL:\n"
  Console println authUrl
  Console println "Paste your verifier here:"
  val v = new Verifier(Console readLine ())
  // Get the Access Token
  val accessToken: Token = service getAccessToken (reqToken, v)
  // Generate an OAuth request
  val req = new OAuthRequest(Verb.GET, "http://api.linkedin.com/v1/people/~:(positions)")
  service signRequest (accessToken, req)
  val resp = req send ()
  Console println (resp getBody ())
}
