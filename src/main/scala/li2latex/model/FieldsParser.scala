package li2latex.model

import xml.NodeSeq

sealed trait FieldsParser {
  val parseOAuthResponse: NodeSeq => Seq[FormattedItem]
}

object PositionsParser extends FieldsParser {
  val parseOAuthResponse: NodeSeq => Seq[FormattedItem] = resp => Nil
}

object PublicationsParser extends FieldsParser {
  val parseOAuthResponse: NodeSeq => Seq[FormattedItem] = resp => Nil
}

object PatentsParser extends FieldsParser {
  val parseOAuthResponse: NodeSeq => Seq[FormattedItem] = resp => Nil
}

object LanguagesParser extends FieldsParser {
  val parseOAuthResponse: NodeSeq => Seq[FormattedItem] = resp => Nil
}

object SkillsParser extends FieldsParser {
  val parseOAuthResponse: NodeSeq => Seq[FormattedItem] = resp => Nil
}

object CertificationsParser extends FieldsParser {
  val parseOAuthResponse: NodeSeq => Seq[FormattedItem] = resp => Nil
}

object EducationsParser extends FieldsParser {
  val parseOAuthResponse: NodeSeq => Seq[FormattedItem] = resp => Nil
}

object CoursesParser extends FieldsParser {
  val parseOAuthResponse: NodeSeq => Seq[FormattedItem] = resp => Nil
}

object VolunteersParser extends FieldsParser {
  val parseOAuthResponse: NodeSeq => Seq[FormattedItem] = resp => Nil
}

object ProjectsParser extends FieldsParser {
  val parseOAuthResponse: NodeSeq => Seq[FormattedItem] = resp => Nil
}

object HonorsParser extends FieldsParser {
  val parseOAuthResponse: NodeSeq => Seq[FormattedItem] = resp => Nil
}

object SummaryParser extends FieldsParser {
  val parseOAuthResponse: NodeSeq => Seq[FormattedItem] = resp => Nil
}

object ParserSelector {
  val getParser: String => Option[FieldsParser] = fields =>
    fields.trim.toLowerCase match {
      case "position"       | "positions"       => Some(PositionsParser)
      case "publication"    | "publications"    => Some(PublicationsParser)
      case "patent"         | "patents"         => Some(PatentsParser)
      case "language"       | "languages"       => Some(LanguagesParser)
      case "skill"          | "skills"          => Some(SkillsParser)
      case "certification"  | "certifications"  => Some(CertificationsParser)
      case "education"      | "educations"      => Some(EducationsParser)
      case "course"         | "courses"         => Some(CoursesParser)
      case "volunteer"      | "volunteers"      => Some(VolunteersParser)
      case "project"        | "projects"        => Some(ProjectsParser)
      case "honor"          | "honors"          => Some(HonorsParser)
      case "summary"                            => Some(SummaryParser)
      case _                                    => None
    }
}