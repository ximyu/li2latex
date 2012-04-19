package li2latex.model

import li2latex.oauth.OAuthFields
import com.weiglewilczek.slf4s.Logging
import xml.NodeSeq

case class LinkedInFields(name: String) {
  def >>(sectionTitle: String) = Section(name, sectionTitle, ParserSelector.getParser(name))
}

object LinkedInFields {
  implicit def stringToLinkedInFields(fields: String): LinkedInFields = LinkedInFields(fields)
}

case class Section(var fields: String, title: String, parser: Option[FieldsParser]) extends OAuthFields with Logging {
  def getFormattedItems: Seq[FormattedItem] =
    ((liftM2(fp => ns => fp parseOAuthResponse ns)(parser, getOAuthResponse) getOrElse Left("Received error response from LinkedIn")): Either[String, Seq[FormattedItem]]) match {
      case Left(msg) => logger.warn("Error: " + msg); Nil
      case Right(result @ Seq(_*)) => result
    }

  def >>(reqFields: String): Section = {
    fields = reqFields
    this
  }

  // A not so generalized implementation of liftM2 which mimics
  // liftM2 :: (Monad m) => (a1 -> a2 -> r) -> m a1 -> m a2 -> m r
  // in Haskell
  private def liftM2(f: FieldsParser => NodeSeq => Either[String, Seq[FormattedItem]])
            (m1: Option[FieldsParser], m2: Option[NodeSeq]): Option[Either[String, Seq[FormattedItem]]] = {
    for {
      fp <- m1
      ns <- m2
    } yield f(fp)(ns)
  }
}