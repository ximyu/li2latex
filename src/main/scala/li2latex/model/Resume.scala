package li2latex.model

import li2latex.template.{DefaultTemplateProvider, TemplateProvider}
import com.weiglewilczek.slf4s.Logging
import java.io.{FileWriter, File}
import java.net.URL
import li2latex.util.LocalFixUp


case class Resume(name:             String = "My Resume", // The name of your resume template
                  filePath:         String = "resume.tex", // The output file path
                  fixUpFilePath:    String = "myfixup.xml", // The XML file containing fix-up info
                  templateProvider: TemplateProvider = DefaultTemplateProvider,
                  sections:         Seq[Section]) extends Logging {

  def generateResume() {
    import scalax.io.Resource
    // Pull each section and generate the intermediate TeX file
    // Then call pdflatex to generate the pdf version of resume

    logger.info("Ensure the cls file for the resume exists on the same path as output text file")
    ensureClsFile()
    LocalFixUp.initFixUpLookup(fixUpFilePath)

    val texFileContent = getFormattedWholeDocument.generateLatex(templateProvider)
    logger.debug("Generated TeX file content:")
    logger.debug(texFileContent)
    Resource.fromFile(filePath).write(texFileContent)
    logger.info("Output file generated at:" +  new File(filePath).getAbsolutePath)
  }

  def ensureClsFile() {
    import scalax.file.Path
    import scalax.io.JavaConverters._
    import li2latex.util.Util._

    // For example origFilePath is /Users/xxx/aa.txt and newFileName is aa.pdf
    // this method will return an Option[Path], which could be Some[Path(/Users/xxx/aa.pdf)]
    def getPathInSameDir(origFilePath: String, newFileName: String): Option[Path] =
      Path(origFilePath).toAbsolute.parent map (_ / newFileName)

    isRemoteResource {
      val clsFileURL = new URL(templateProvider.clsFilePath)
      val clsFilePath = getPathInSameDir(filePath, clsFileURL.getPath.split("/").last)
      clsFilePath foreach  (_.write(clsFileURL.asInput.bytes))
    } otherwise {
      import scalax.io.Resource._
      // then the cls file path provided should be an absolute path for a local file
      val clsFilePath = getPathInSameDir(filePath, templateProvider.clsFilePath.split("[/\\\\]").last) // consider both linux and windows file paths
      fromFile(templateProvider.clsFilePath) copyDataTo fromFile(clsFilePath.get.path)
    }
  }

  def getFormattedWholeDocument: FormattedWholeDocument = {
    val sectionItems: Seq[FormattedItem] = sections.map(section => FormattedSection(section.title, section.getFormattedItems))
    FormattedWholeDocument(NameParser.formattedName.getOrElse("Oops... Your name not found"), sectionItems)
  }

  //def getTexFilePath: String = ""
}

