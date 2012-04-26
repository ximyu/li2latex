package li2latex.model

import java.util.{Calendar, GregorianCalendar}
import li2latex.config.AppConfig
import scala.Predef._
import xml.{XML, Node, NodeSeq}
import li2latex.oauth.{OAuthClientImpl}
import com.weiglewilczek.slf4s.Logging
import scala.{Left, Option}
import li2latex.util.LocalFixUp

sealed trait FieldsParser extends Logging {
  private val NODE_COUNT_NOT_MATCH = "The number of nodes doesn't match. The XML reponse must be corrupted. Input fields: "

  protected val thisField: String

  protected val extractFromXML: Node => Option[FormattedItem]

  private val nodeCountMatches: NodeSeq => String => Boolean = input => field =>
    (input headOption) flatMap (_.attributes.asAttrMap.get("total")) map (_.toInt == (input \ field).length ) getOrElse false

  def parseOAuthResponse: NodeSeq => Either[String, Seq[FormattedItem]] = resp => {
    logger.debug("LinkedIn response for field " + thisField)
    logger.debug(resp.toString())
    if (!nodeCountMatches(resp)(thisField)) Left(NODE_COUNT_NOT_MATCH + thisField)
    else Right(for {
      node <- resp \ thisField
      item <- extractFromXML(node)
    } yield item)
  }

  // If the content have bulletpoints indicated by "*", then try to separate all
  // bulletpoints into a Seq of String
  protected val parseContentIntoBulletPoints: String => Seq[String] = content =>
    content.split("\\*").map(_.trim).filter(!_.isEmpty)

  private def buildCalendar(month: Int, year: Int): Calendar = {
    val cal = new GregorianCalendar()
    cal.set(Calendar.MONTH, month)
    cal.set(Calendar.YEAR, year)
    cal
  }

  protected val getFormattedDateStr: String => String => String =
    year => month => AppConfig.DATE_FORMATTER.format(buildCalendar(month.toInt, year.toInt).getTime)

  protected val getTextValue: NodeSeq => Seq[String] => Option[String] =
    node => paths => paths.foldLeft(node)(_ \\ _).headOption.map(_.text).map(_.replace("#", "\\#").replace("&", "\\&"))
}

object NameParser {
  val fields = "formatted-name"

  def getOAuthResponse = XML loadString (OAuthClientImpl getByField fields) match {
    case <person>{ ns @ _* }</person> => Some(ns)
    case <error>{ _* }</error>        => None
    case _                            => None
  }

  val formattedName:Option[String] = getOAuthResponse.flatMap(resp => (resp \\ fields).headOption.map(_.text))
}

object ContactInfoParser extends FieldsParser {
  protected val thisField = AppConfig.DEFAULT_CONTACT_INFO_FIELDS

  protected val extractFromXML: Node => Option[FormattedItem] = node => Some(PlainFormattedSectionItem("Unimplmented",""))

  override def parseOAuthResponse: NodeSeq => Either[String, Seq[FormattedItem]] = resp => (for {
    phoneNumber <- (resp \ "phone-number" \ "phone-number").headOption.map(_.text)
    email       <- getTextValue(resp)(Seq("im-account-name"))
    address     <- getTextValue(resp)(Seq("main-address"))
  } yield FormattedContactInfo(address, phoneNumber, email)) match {
    case Some(x) => Right(Seq(x))
    case None    => Left("Cannot parse contact info")
  }

}

object PositionsParser extends FieldsParser {
  protected val thisField = "position"

  protected val extractFromXML: Node => Option[FormattedItem] = node => for {
    title        <- getTextValue(node)(Seq("title"))
    company      <- getTextValue(node)(Seq("company", "name"))
    startYear    <- getTextValue(node)(Seq("start-date", "year"))
    startMonth   <- getTextValue(node)(Seq("start-date", "month"))
    summary      <- getTextValue(node)(Seq("summary"))
    location     <- LocalFixUp.lookupLocation(title, company)
    isCurrentStr <- getTextValue(node)(Seq("is-current"))
    isCurrent    = isCurrentStr.toBoolean
    summarySeq   = parseContentIntoBulletPoints(summary)
    items        = summarySeq map { new FormattedBulletPointItem(_) }
    startDateStr = getFormattedDateStr(startYear)(startMonth)
  } yield {
    val endDateStr = if (isCurrent) "Present" else {
      val endYear  = getTextValue(node)(Seq("end-date", "year")).get
      val endMonth = getTextValue(node)(Seq("end-date", "month")).get
      getFormattedDateStr(endYear)(endMonth)
    }
    FormattedSectionItemWithLocation(company, title, location, startDateStr, endDateStr, items)
  }
}

object PublicationsParser extends FieldsParser {
  protected val thisField = "publication"

  protected val extractFromXML: Node => Option[FormattedItem] = node => for {
    title <- getTextValue(node)(Seq("title"))
    year  <- getTextValue(node)(Seq("year"))
    month <- getTextValue(node)(Seq("month"))
  } yield PlainFormattedSectionItem("", title + ", " + getFormattedDateStr(year)(month))
}

//object PatentsParser extends FieldsParser {
//  protected val thisField = "patent"
//  protected val extractFromXML: Node => FormattedItem = node => {
//    PlainFormattedSectionItem("", "")
//  }
//}

//object LanguagesParser extends FieldsParser {
//  protected val thisField = "language"
//  protected val extractFromXML: Node => FormattedItem = node => {
//    PlainFormattedSectionItem("", "")
//  }
//}

object SkillsParser extends FieldsParser {
  protected val thisField = "skill"

  private val CAT_PROG = "Programming Languages"
  private val CAT_OS   = "Operating Systems"
  private val CAT_DB   = "Databases"
  private val CAT_WEB  = "Web Development"

  protected val extractFromXML: Node => Option[FormattedItem] = node => for {
    name <- getTextValue(node)(Seq("name"))
  } yield PlainFormattedSectionItem("", name)

  override def parseOAuthResponse: NodeSeq => Either[String, Seq[FormattedItem]] = resp => {
    import scala.collection.mutable.Map
    val skillCategories: Map[String, List[String]] =
      Map(CAT_PROG -> Nil,
          CAT_OS   -> Nil,
          CAT_DB   -> Nil,
          CAT_WEB  -> Nil)
    var currentCategory: String = "Programming Languages"
    super.parseOAuthResponse(resp).right.foreach{items =>
      items.foreach{item =>
        item match {
          case PlainFormattedSectionItem(_, x) => skillCategories.get(x) match {
            case Some(_) => currentCategory = x
            case None    => skillCategories.put(currentCategory, skillCategories.get(currentCategory).getOrElse(Nil) ++ List(x))
          }
          case _ => ()
        }
      }
    }
    //Right((for ((k, lst) <- skillCategories) yield PlainFormattedSectionItem(k, lst.reduceLeft(_ + ", " + _))).toSeq)
    // Hard coding the four categories here as iterating through the Map will not yield expected order
    Right(Seq(
      PlainFormattedSectionItem(CAT_PROG, skillCategories.get(CAT_PROG).get.reduceLeft(_ + ", " + _)),
      PlainFormattedSectionItem(CAT_OS, skillCategories.get(CAT_OS).get.reduceLeft(_ + ", " + _)),
      PlainFormattedSectionItem(CAT_DB, skillCategories.get(CAT_DB).get.reduceLeft(_ + ", " + _)),
      PlainFormattedSectionItem(CAT_WEB, skillCategories.get(CAT_WEB).get.reduceLeft(_ + ", " + _))
    ))
  }
}

//object CertificationsParser extends FieldsParser {
//  protected val thisField = "position"
//  protected val extractFromXML: Node => FormattedItem = node => {
//
//  }
//}

object EducationsParser extends FieldsParser {
  protected val thisField = "education"

  protected val extractFromXML: Node => Option[FormattedItem] = node => for {
    school     <- getTextValue(node)(Seq("school-name"))
    degree     <- getTextValue(node)(Seq("degree"))
    major      <- getTextValue(node)(Seq("field-of-study"))
    startYear  <- getTextValue(node)(Seq("start-date"))
    endYear    <- getTextValue(node)(Seq("end-date"))
  } yield {
    val activitiesOpt = getTextValue(node)(Seq("activities"))
    val items = Seq(new FormattedBulletPointItem(degree + " in " + major)) ++ {
      activitiesOpt match {
        case None => Nil
        case Some(activities: String) => (parseContentIntoBulletPoints(activities) map { new FormattedBulletPointItem(_) })
      }
    }
    FormattedSectionItem(school, startYear, endYear, items)
  }
}

//object CoursesParser extends FieldsParser {
//  protected val thisField = "course"
//  protected val extractFromXML: Node => FormattedItem = node => {
//    PlainFormattedSectionItem("", "")
//  }
//}

//object VolunteersParser extends FieldsParser {
//  protected val thisField = "volunteer-experience"
//  protected val extractFromXML: Node => FormattedItem = node => {
//    PlainFormattedSectionItem("", "")
//  }
//}

object ProjectsParser extends FieldsParser {
  protected val thisField = "project"

  protected val extractFromXML: Node => Option[FormattedItem] = node => for {
    name                 <- getTextValue(node)(Seq("name"))
    description          <- getTextValue(node)(Seq("description"))
    (startDate, endDate) <- LocalFixUp.lookupStartEndDate(name)
    descItems   = parseContentIntoBulletPoints(description) map { new FormattedBulletPointItem(_) }
  } yield FormattedSectionItem(name, startDate, endDate, descItems)

  override def parseOAuthResponse: NodeSeq => Either[String, Seq[FormattedItem]] = resp => {
    val formattedItemDate: FormattedItem => Long = item =>
      item match {
        case i: FormattedSectionItem             => 0 - AppConfig.DATE_FORMATTER.parse(i.startDateStr).getTime
        case _                                   => -1
      }

    super.parseOAuthResponse(resp).right.map(_ sortBy formattedItemDate)
  }
}

//object SummaryParser extends FieldsParser {
//  protected val thisField = "summary"
//  protected val extractFromXML: Node => FormattedItem = node => {
//    PlainFormattedSectionItem("", "")
//  }
//}

object ParserSelector {
  val getParser: String => Option[FieldsParser] = fields =>
    fields.trim.toLowerCase match {
      case "position"       | "positions"       => Some(PositionsParser)
      case "publication"    | "publications"    => Some(PublicationsParser)
//      case "patent"         | "patents"         => Some(PatentsParser)
//      case "language"       | "languages"       => Some(LanguagesParser)
      case "skill"          | "skills"          => Some(SkillsParser)
//      case "certification"  | "certifications"  => Some(CertificationsParser)
      case "education"      | "educations"      => Some(EducationsParser)
//      case "course"         | "courses"         => Some(CoursesParser)
//      case "volunteer"      | "volunteers"      => Some(VolunteersParser)
      case "project"        | "projects"        => Some(ProjectsParser)
//      case "summary"                            => Some(SummaryParser)
      case "contact"                            => Some(ContactInfoParser)
      case _                                    => None
    }
}