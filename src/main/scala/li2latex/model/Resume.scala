package li2latex.model

import li2latex.config.AppConfig
import li2latex.template.{TemplateConstants, DefaultTemplateProvider, TemplateProvider}
import java.io.File
import sys.process._

case class Resume(val name:             String = "My Resume", // The name of your resume template
                  val filePath:         String = "resume.tex", // The output file path
                  val templateProvider: TemplateProvider = DefaultTemplateProvider,
                  val config:           Map[String, String] = Map(),
                  val sections:         Seq[Section]) {
  def generateResume() {
    // Pull each section and generate the intermediate TeX file
    // Then call pdflatex to generate the pdf version of resume
    val texFileContent = getFormattedWholeDocument.generateLatex(templateProvider)
    texFileContent #> new File(getTexFilePath)
  }

  def getFormattedWholeDocument: FormattedWholeDocument = null

  def getTexFilePath: String = ""
}

