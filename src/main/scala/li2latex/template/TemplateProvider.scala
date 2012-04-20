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

  private val documentTemplate = String.format(
    """|\documentclass[margin,line]{resume}
       |\%s{array}
       |
       |\begin{document}
       |%s
       |%s
       |\end{document}""".stripMargin,
    "usepackage", // to get rid of unicode escape
    TemplateConstants.PERSON_NAME_TEMPLATE,
    TemplateConstants.SECTIONS_CONTAINER_TEMPLATE)

  private val personNameTemplate = String.format(
    """\name{\Large %s}""",
    TemplateConstants.PERSON_NAME)

  private val sectionsContainerTemplate = String.format(
    """|\begin{resume}
       |%s
       |\end{resume}""".stripMargin,
    TemplateConstants.ALL_SECTIONS)

  private val sectionTemplate = String.format(
    """|
       |
       |\section{\mysidestyle %s}
       |%s""".stripMargin,
    TemplateConstants.SECTION_TITLE,
    TemplateConstants.SECTION_CONTENT)

  private val sectionItemTemplate = String.format(
    """|
       |\textbf{%s} \hfill \textbf{%s}
       |\vspace{-3mm}\\\vspace{-1mm}
       |%s""".stripMargin,
    TemplateConstants.ITEM_TITLE,
    TemplateConstants.ITEM_DATE,
    TemplateConstants.ITEM_CONTENT)

  private val sectionWithLocItemTemplate = String.format(
    """|
       |\textbf{%s} \hfill \textbf{%s}\\
       |%s\hfill \textbf{%s}
       |\vspace{-3mm}\\\vspace{-1mm}
       |%s""".stripMargin,
    TemplateConstants.ITEM_TITLE,
    TemplateConstants.ITEM_LOC,
    TemplateConstants.ITEM_SUBTITLE,
    TemplateConstants.ITEM_DATE,
    TemplateConstants.ITEM_CONTENT)

  private val plainSectionItemTemplate = String.format(
    """|
       |\textbf{%s}: %s""".stripMargin,
    TemplateConstants.ITEM_TITLE,
    TemplateConstants.ITEM_CONTENT)

  private val bulletPointsContainerTemplate = String.format(
    """|\begin{list2}
       |%s
       |\end{list2}""".stripMargin,
    TemplateConstants.ALL_BULLET_ITEMS)

  private val bulletPointTemplate = String.format(
    """|
       |\item %s""".stripMargin,
    TemplateConstants.BULLET_ITEM)

  private val contactInfoTemplate = String.format(
    """|
       |%s \hfill %s\\
       |%s \hfill %s""".stripMargin,
    TemplateConstants.CONTACT_ADDRESS_LINE1,
    TemplateConstants.CONTACT_EMAIL,
    TemplateConstants.CONTACT_ADDRESS_LINE2,
    TemplateConstants.CONTACT_PHONE)

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
    // Split the address into 2 lines: 1) street address, 2) city, state and zip code
    // Since line breaks are erased after applying scala.xml.Utility.trim,
    // we can only do a split by ',' here.
    def splitAddress(addStr: String): (String, String) = {
      val (line1, line2) = addStr.trim.reverse.toCharArray.toList.span(_ != ',').swap
      (line1.mkString.reverse.trim, line2.mkString.reverse.trim)
    }

    val (line1, line2) = splitAddress(item.address)
    contactInfoTemplate.replace(TemplateConstants.CONTACT_ADDRESS_LINE1, line1)
                       .replace(TemplateConstants.CONTACT_ADDRESS_LINE2, line2)
                       .replace(TemplateConstants.CONTACT_EMAIL, item.email)
                       .replace(TemplateConstants.CONTACT_PHONE, item.phone)
  }
}