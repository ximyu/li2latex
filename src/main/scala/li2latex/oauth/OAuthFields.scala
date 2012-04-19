package li2latex.oauth

import xml.{NodeSeq, XML}
import li2latex.model.Section


trait OAuthFields {
  self: Section =>

  import scala.xml.Utility.trim

  lazy val getOAuthResponse: Option[NodeSeq] =
    trim(XML.loadString(OAuthClientImpl getByField fields).head) match {
      case <person>{ ns @ _* }</person> => Some(ns)
      case <error>{ _* }</error>        => None
      case _                            => None
    }

  lazy val getOAuthResponseRaw: String = OAuthClientImpl getByField fields
}
