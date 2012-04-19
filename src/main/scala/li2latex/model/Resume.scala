package li2latex.model

import li2latex.config.AppConfig
import li2latex.template.{TemplateConstants, DefaultTemplateProvider, TemplateProvider}
import sys.process._
import com.weiglewilczek.slf4s.Logging
import java.io.{FileWriter, File}

case class Resume(name:             String = "My Resume", // The name of your resume template
                  filePath:         String = "resume.tex", // The output file path
                  templateProvider: TemplateProvider = DefaultTemplateProvider,
                  sections:         Seq[Section]) extends Logging {

  def using[A <: {def close(): Unit}, B](param: A)(f: A => B): B =
    try { f(param) } finally { param.close() }

  def generateResume() {
    // Pull each section and generate the intermediate TeX file
    // Then call pdflatex to generate the pdf version of resume
    val texFileContent = getFormattedWholeDocument.generateLatex(templateProvider)
    logger.debug("Generated TeX file content:")
    logger.debug(texFileContent)
    using (new FileWriter(filePath)) {
      fileWriter => fileWriter.write(texFileContent)
    }
    logger.info("Output file generated at:" +  new File(filePath).getAbsolutePath)
  }

  def getFormattedWholeDocument: FormattedWholeDocument = {
    val sectionItems: Seq[FormattedItem] = sections.map(section => FormattedSection(section.title, section.getFormattedItems))
    FormattedWholeDocument(NameParser.formattedName.getOrElse("Oops... Your name not found"), sectionItems)
  }

  //def getTexFilePath: String = ""
}

