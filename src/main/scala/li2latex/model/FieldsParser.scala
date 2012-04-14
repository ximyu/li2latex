package li2latex.model

import sys.Prop.FileProp

sealed trait FieldsParser {
  val parseOAuthResponse: String => Seq[FormattedItem]
}

object PositionsParser extends FieldsParser {
  val parseOAuthResponse: String => Seq[FormattedItem] = resp => Nil
}

object CompaniesParser extends FieldsParser {
  val parseOAuthResponse: String => Seq[FormattedItem] = resp => Nil
}

object PublicationsParser extends FieldsParser {
  val parseOAuthResponse: String => Seq[FormattedItem] = resp => Nil
}

object PatentsParser extends FieldsParser {
  val parseOAuthResponse: String => Seq[FormattedItem] = resp => Nil
}

object LanguagesParser extends FieldsParser {
  val parseOAuthResponse: String => Seq[FormattedItem] = resp => Nil
}

object SkillsParser extends FieldsParser {
  val parseOAuthResponse: String => Seq[FormattedItem] = resp => Nil
}

object CertificationsParser extends FieldsParser {
  val parseOAuthResponse: String => Seq[FormattedItem] = resp => Nil
}

object EducationsParser extends FieldsParser {
  val parseOAuthResponse: String => Seq[FormattedItem] = resp => Nil
}

object CoursesParser extends FieldsParser {
  val parseOAuthResponse: String => Seq[FormattedItem] = resp => Nil
}

object VolunteersParser extends FieldsParser {
  val parseOAuthResponse: String => Seq[FormattedItem] = resp => Nil
}

object RecommendationsParser extends FieldsParser {
  val parseOAuthResponse: String => Seq[FormattedItem] = resp => Nil
}

object ParserSelector {
  val getParser: String => Option[FieldsParser] = fields =>
    fields.trim.toLowerCase match {
      case "position"       | "positions"       => Some(PositionsParser)
      case "company"        | "companies"       => Some(CompaniesParser)
      case "publication"    | "publications"    => Some(PublicationsParser)
      case "patent"         | "patents"         => Some(PatentsParser)
      case "language"       | "languages"       => Some(LanguagesParser)
      case "skill"          | "skills"          => Some(SkillsParser)
      case "certification"  | "certifications"  => Some(CertificationsParser)
      case "education"      | "educations"      => Some(EducationsParser)
      case "course"         | "courses"         => Some(CoursesParser)
      case "volunteer"      | "volunteers"      => Some(VolunteersParser)
      case "recommendation" | "recommendations" => Some(RecommendationsParser)
      case _                                    => None
    }
}