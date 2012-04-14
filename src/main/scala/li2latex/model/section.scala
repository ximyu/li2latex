package li2latex.model

import li2latex.oauth.OAuthFields

case class LinkedInFields(name: String) {
  def >>(sectionTitle: String) = Section(name, sectionTitle, ParserSelector.getParser(name))
}

object LinkedInFields {
  implicit def stringToLinkedInFields(fields: String): LinkedInFields = LinkedInFields(fields)
}

case class Section(fields: String, title: String, parser: Option[FieldsParser]) extends OAuthFields {
  def getFormattedItems: Seq[FormattedItem] =
    parser map ( _ parseOAuthResponse getOAuthResponse ) getOrElse Nil
}