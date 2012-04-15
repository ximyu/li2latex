package li2latex.model

case class Resume(name:     String = "My Resume", // The name of your resume template
                  filePath: String = "resume.tex", // The output file path
                  config:   Map[String, String] = Map(),
                  sections: Seq[Section]) {
  def generateResume() {
    // Pull each section and generate the intermediate TeX file
    // Then call pdflatex to generate the pdf version of resume
  }
}

