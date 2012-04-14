package li2latex.oauth

trait OAuthFields extends OAuthClient{
  val fields: String

  lazy val getOAuthResponse: String = OAuthClientImpl getByField fields
}
