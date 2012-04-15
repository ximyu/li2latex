package li2latex.oauth

import xml.{NodeSeq, XML}


trait OAuthFields {
  val fields: String

  lazy val getOAuthResponse: Option[NodeSeq] =
    XML loadString (OAuthClientImpl getByField fields) match {
      case <person>{ ns @ _* }</person> => Some(ns)
      case <error>{ _* }</error>        => None
      case _                            => None
    }

  lazy val getOAuthResponseRaw: String = OAuthClientImpl getByField fields
}
