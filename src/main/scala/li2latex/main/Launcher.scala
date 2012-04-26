package li2latex.main

import li2latex.model.Resume
import li2latex.model.LinkedInFields._
import com.weiglewilczek.slf4s.Logging
import scalax.file.Path

object Launcher extends App with Logging{

  private def generateResume() {
    val sections = Seq(
      "contact" >> "Contact Info" >> "phone-numbers,im-accounts,main-address",
      "positions" >> "Work Experience",
      "educations" >> "Education",
      "skills" >> "Skills",
      "projects" >> "Project Experience"
    )
    cleanup()
    print("Enter the output TeX file name [Default \"resume.tex\"]: ")
    val ValidTexFileRegex = """(.+?)\.tex""".r
    val texOutputFilePath = Console.readLine().trim match {
      case ValidTexFileRegex(pathPrefix) => pathPrefix + ".tex"
      case _                             => "resume.tex"
    }
    print("Enter the local fix-up file name [Default \"myfixup.xml\"]: ")
    val fixUpFilePath = Console.readLine().trim match {
      case ""       => "myfixup.xml"
      case name @ _ => name
    }
    val sampleResume1 = Resume(
      filePath = texOutputFilePath,
      fixUpFilePath = fixUpFilePath,
      sections = sections
    )

    logger.info("Start generating your resume...")

    sampleResume1 generateResume()

    logger.info("Finished generating the TeX file of your resume. Please run pdflatex to generate the PDF version of your resume. Thanks for using.")
  }

  // This method will clean up output of previous runs
  // in the project directory
  private def cleanup() {
    Path(".") ** "*.{tex,log,pdf,cls}" foreach (_.delete())
  }

  println("Welcome to li2latex, please choose what you would like to do:")
  print("[1] Generate TeX resume file; [2] Test out LinkedIn API; [3] Clean up environment; [Default 1]: ")
  Console.readLine().trim match {
    case "2" => APITestConsole.start()
    case "3" => cleanup()
    case _   => generateResume()
  }
}
