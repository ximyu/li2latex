package li2latex.samples

import li2latex.model.Resume
import li2latex.model.LinkedInFields._

object Samples {
  val sampleResume1 = Resume(Seq(
    "positions" >> "Work Experiences",
    "educations" >> "Education"
  ))
}
