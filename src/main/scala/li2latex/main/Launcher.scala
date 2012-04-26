package li2latex.main

import li2latex.model.Resume
import li2latex.model.LinkedInFields._
import com.weiglewilczek.slf4s.Logging

object Launcher extends App with Logging{

  private def generateResume() {
    val sections = Seq(
      "contact" >> "Contact Info" >> "phone-numbers,im-accounts,main-address",
      "positions" >> "Work Experience",
      "educations" >> "Education",
      "skills" >> "Skills",
      "projects" >> "Project Experience"
    )
    logger.info("Enter the output TeX file name [Default \"resume.tex\"]: ")
    val ValidTexFileRegex = """(.+?)\.tex""".r
    val sampleResume1 = Console.readLine().trim match {
      case ValidTexFileRegex(pathPrefix) => Resume(
        filePath = pathPrefix + ".tex",
        sections = sections
      )
      case _ => Resume(sections = sections)
    }

    logger.info("Start generating your resume...")

    sampleResume1 generateResume()

    logger.info("Finished generating your resume. Thanks for using.")
  }

  logger.info("Welcome to li2latex, please choose what you would like to do:")
  logger.info("[1] Generate TeX resume file; [2] Test out LinkedIn API; [Default 1]: ")
  Console.readLine().trim match {
    case "2" => APITestConsole.start()
    case _   => generateResume()
  }
}
