package li2latex.model

import li2latex.oauth.OAuthFields
import xml.NodeSeq

case class LinkedInFields(name: String) {
  def >>(sectionTitle: String) = Section(name, sectionTitle, ParserSelector.getParser(name))
}

object LinkedInFields {
  implicit def stringToLinkedInFields(fields: String): LinkedInFields = LinkedInFields(fields)
}

case class Section(fields: String, title: String, parser: Option[FieldsParser]) extends OAuthFields {
  def getFormattedItems: Seq[FormattedItem] =
    liftM2(fp => ns => fp parseOAuthResponse ns)(parser, getOAuthResponse) getOrElse Nil

  // A not so generalized implementation of liftM2 which mimics
  // liftM2 :: (Monad m) => (a1 -> a2 -> r) -> m a1 -> m a2 -> m r
  // in Haskell
  private def liftM2(f: FieldsParser => NodeSeq => Seq[FormattedItem])
            (m1: Option[FieldsParser], m2: Option[NodeSeq]): Option[Seq[FormattedItem]] = {
    for {
      fp <- m1
      ns <- m2
    } yield f(fp)(ns)
  }
}