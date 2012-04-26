package li2latex.util

import scalax.file.Path
import xml.{XML, NodeSeq}

object LocalFixUp {

  import scala.collection.mutable.Map

  private val projectStartEndDates = Map[String, Tuple2[String, String]]()
  private val positionLocations = Map[String, String]()

  def initFixUpLookup(fixUpFilePath: String) {
    val fixUpXML: NodeSeq = XML.loadString(Path(fixUpFilePath).slurpString())
    (fixUpXML \\ "fix") foreach {node =>
      node.attributes.asAttrMap.get("scope") match {
        case Some("positions") =>
          (for {
            title    <- (node \\ "title").headOption.map(_.text)
            company  <- (node \\ "company-name").headOption.map(_.text)
            location <- (node \\ "location").headOption.map(_.text)
          } yield ((title + "::" + company) -> location)) foreach (positionLocations += _)
        case Some("projects")  =>
          (for {
            name      <- (node \\ "name").headOption.map(_.text)
            startDate <- (node \\ "start-date").headOption.map(_.text)
            endDate   <- (node \\ "end-date").headOption.map(_.text)
          } yield (name -> (startDate, endDate))) foreach (projectStartEndDates += _)
        case _                 => // Do nothing
      }
    }
  }

  def lookupStartEndDate(projectName: String) = projectStartEndDates get projectName

  def lookupLocation(positionTitle: String, positionCompany: String) =
    positionLocations get (positionTitle + "::" + positionCompany)
}
