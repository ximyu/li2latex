package li2latex

import org.scribe.builder.api.LinkedInApi
import org.scribe.builder.ServiceBuilder
import org.scribe.oauth.OAuthService
import org.scribe.model.Token

object APITest {
  val builder = new ServiceBuilder
  val service: OAuthService =
    builder provider (new LinkedInApi) apiKey (AppConfig.API_KEY) apiSecret (AppConfig.API_SECRET) build()
  val accessToken = getAccessToken

  val getByUrl: String => String =
    url => ""

  val getByField: String => String =
    field => ""

  private def getAccessToken : Option[Token] = None
}
