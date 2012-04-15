package li2latex.samples

import li2latex.model.Resume
import li2latex.model.LinkedInFields._

object SampleResumes {
  val sampleResume1 = Resume(
    sections = Seq(
      "positions" >> "Work Experiences",
      "educations" >> "Education"
    )
  )
}
