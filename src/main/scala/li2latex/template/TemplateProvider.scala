package li2latex.template

import li2latex.model._


trait TemplateProvider {
  def generateLatex(item: FormattedItem): String = item.generateLatex(this)

  def generateWholeDocument(item: FormattedWholeDocument): String

  def generateSection(item: FormattedSection): String

  def generateSectionItem(item: FormattedSectionItem): String

  def generateSectionItemWithLocation(item: FormattedSectionItemWithLocation): String

  def generatePlainSectionItem(item: PlainFormattedSectionItem): String

  def generateBulletPointItem(item: FormattedBulletPointItem): String

  def generateContactInfoFields(item: FormattedContactInfo): String
}

object DefaultTemplateProvider extends TemplateProvider {

  private val clsFilePath = "http://dl.dropbox.com/u/6824415/li2latex/resume.cls"

  private val documentTemplate =
"""\\documentclass[margin,line]{resume}
\\usepackage{array}

\\begin{document}
""" + TemplateConstants.PERSON_NAME_TEMPLATE +
TemplateConstants.SECTIONS_CONTAINER_TEMPLATE +
"""\\end{document}"""

  private val personNameTemplate =
    """\\name{\\Large """ + TemplateConstants.PERSON_NAME + """}"""

  private val sectionsContainerTemplate =
"""
\\begin{resume}
""" + TemplateConstants.ALL_SECTIONS + """
\\end{resume}
"""

  private val sectionTemplate =
"""
\\section{\\mysidestyle """ + TemplateConstants.SECTION_TITLE + """}
""" + TemplateConstants.SECTION_CONTENT + """
"""

  private val sectionItemTemplate =
"""
\\textbf{""" + TemplateConstants.ITEM_TITLE + """} \\hfill \\textbf{""" + TemplateConstants.ITEM_DATE + """}
\\vspace{-3mm}\\\\\\vspace{-1mm}
""" + TemplateConstants.ITEM_CONTENT + """
"""

  private val sectionWithLocItemTemplate =
"""
\\textbf{""" + TemplateConstants.ITEM_TITLE + """} \\hfill \\textbf{""" + TemplateConstants.ITEM_LOC + """}\\
""" + TemplateConstants.ITEM_SUBTITLE + """\hfill \textbf{""" + TemplateConstants.ITEM_DATE + """}
\\vspace{-3mm}\\\\\\vspace{-1mm}
""" + TemplateConstants.ITEM_CONTENT + """
"""

  private val plainSectionItemTemplate =
"""
\textbf{""" + TemplateConstants.ITEM_TITLE + """}: """ + TemplateConstants.ITEM_CONTENT + """
"""

  private val bulletPointsContainerTemplate =
"""
\\begin{list2}
""" + TemplateConstants.ALL_BULLET_ITEMS + """
\\end{list2}
"""

  private val bulletPointTemplate =
    """\\item """ + TemplateConstants.BULLET_ITEM

  private val contactInfoTemplate =
"""
""" + TemplateConstants.CONTACT_ADDRESS_LINE1 + """ \hfill """ + TemplateConstants.CONTACT_EMAIL + """\\
""" + TemplateConstants.CONTACT_ADDRESS_LINE2 + """ \hfill """ + TemplateConstants.CONTACT_PHONE + """
"""

  private def foldAllItems(allItems: Seq[FormattedItem]): String = {
    allItems.foldLeft(new StringBuilder){(sb, sec) =>
      sb append sec.generateLatex(this)}.toString()
  }

  private def foldAllBulletPointItems(allItems: Seq[FormattedBulletPointItem]): String = {
    val allItemStr = allItems.foldLeft(new StringBuilder){(sb, sec) =>
      sb append sec.generateLatex(this)}.toString()
    bulletPointsContainerTemplate.replace(TemplateConstants.ALL_BULLET_ITEMS, allItemStr)
  }

  def generateWholeDocument(item: FormattedWholeDocument): String = documentTemplate
    .replace(TemplateConstants.PERSON_NAME_TEMPLATE,
      personNameTemplate.replace(TemplateConstants.PERSON_NAME, item.personName))
    .replace(TemplateConstants.SECTIONS_CONTAINER_TEMPLATE,
      sectionsContainerTemplate.replace(TemplateConstants.ALL_SECTIONS, foldAllItems(item.allSections)))

  def generateSection(item: FormattedSection): String = sectionTemplate
    .replace(TemplateConstants.SECTION_TITLE, item.sectionTitle)
    .replace(TemplateConstants.SECTION_CONTENT, foldAllItems(item.allItems))

  def generateSectionItem(item: FormattedSectionItem): String = sectionItemTemplate
    .replace(TemplateConstants.ITEM_TITLE, item.itemTitle)
    .replace(TemplateConstants.ITEM_DATE, item.getStartAndEndDateStr)
    .replace(TemplateConstants.ITEM_CONTENT, foldAllBulletPointItems(item.itemContents))

  def generateSectionItemWithLocation(item: FormattedSectionItemWithLocation): String = sectionWithLocItemTemplate
    .replace(TemplateConstants.ITEM_TITLE, item.itemTitle)
    .replace(TemplateConstants.ITEM_LOC, item.location)
    .replace(TemplateConstants.ITEM_DATE, item.getStartAndEndDateStr)
    .replace(TemplateConstants.ITEM_SUBTITLE, item.itemSubTitle)
    .replace(TemplateConstants.ITEM_CONTENT, foldAllBulletPointItems(item.itemContents))

  def generatePlainSectionItem(item: PlainFormattedSectionItem): String = plainSectionItemTemplate
    .replace(TemplateConstants.ITEM_TITLE, item.itemTitle)
    .replace(TemplateConstants.ITEM_CONTENT, item.content)

  def generateBulletPointItem(item: FormattedBulletPointItem): String = bulletPointTemplate
    .replace(TemplateConstants.BULLET_ITEM, item.content)

  def generateContactInfoFields(item: FormattedContactInfo): String = {
    def splitAddress(addStr: String): (String, String) = {
      val (line1Rev, line2Rev) = addStr.trim.reverse.toCharArray.toList.span(_ != '\n').swap
      (line1Rev.mkString.trim.reverse, line2Rev.mkString.trim.reverse)
    }

    val (line1, line2) = splitAddress(item.address)
    contactInfoTemplate.replace(TemplateConstants.CONTACT_ADDRESS_LINE1, line1)
                       .replace(TemplateConstants.CONTACT_ADDRESS_LINE2, line2)
                       .replace(TemplateConstants.CONTACT_EMAIL, item.email)
                       .replace(TemplateConstants.CONTACT_PHONE, item.phone)
  }
}