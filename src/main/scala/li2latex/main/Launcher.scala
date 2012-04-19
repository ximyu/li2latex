package li2latex.main

import li2latex.model.Resume
import li2latex.model.LinkedInFields._
import com.weiglewilczek.slf4s.Logging

object Launcher extends App with Logging{

//  APITestConsole.start
  val sampleResume1 = Resume(
    sections = Seq(
      "contact" >> "Contact Info" >> "phone-numbers,im-accounts,main-address",
      "positions" >> "Work Experiences",
      "educations" >> "Education",
      //"skills" >> "Skills",
      "projects" >> "Project Experiences"
    )
  )

  logger.info("Start generating your resume...")

  sampleResume1 generateResume()

  logger.info("Finished generating your resume. Thanks for using.")
}
