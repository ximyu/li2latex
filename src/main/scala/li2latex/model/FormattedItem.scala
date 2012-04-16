package li2latex.model

import java.util.Date
import li2latex.config.AppConfig
import li2latex.template.TemplateProvider

trait FormattedItem {
  def generateLatex(tmplProv: TemplateProvider): String
}

case class FormattedWholeDocument(personName:  String,
                                  allSections: Seq[FormattedItem]
                                 ) extends FormattedItem {
  def generateLatex(tmplProv: TemplateProvider): String = tmplProv generateWholeDocument this
}

case class FormattedSection(sectionTitle: String,
                            allItems: Seq[FormattedItem]
                           ) extends FormattedItem {
  def generateLatex(tmplProv: TemplateProvider): String = tmplProv generateSection this
}

case class FormattedContactInfo(address: String,
                                phone:   String,
                                email:   String
                               ) extends FormattedItem {
  def generateLatex(tmplProv: TemplateProvider): String = tmplProv generateContactInfoFields this
}

case class FormattedSectionItem(itemTitle:    String,
                                startDate:    Date,
                                endDate:      Date,
                                itemContents: Seq[FormattedBulletPointItem]
                               ) extends FormattedItem {
  import AppConfig.DATE_FORMATTER.format

  def getStartAndEndDateStr = format(startDate) + " --- " + format(endDate)

  def generateLatex(tmplProv: TemplateProvider): String = tmplProv generateSectionItem this
}

case class FormattedSectionItemWithLocation(itemTitle: String,
                                            itemSubTitle: String,
                                            location: String,
                                            startDate: Date,
                                            endDate: Date,
                                            itemContents: Seq[FormattedBulletPointItem]
                                           ) extends FormattedItem {
  import AppConfig.DATE_FORMATTER.format

  def getStartAndEndDateStr = format(startDate) + " --- " + format(endDate)

  def generateLatex(tmplProv: TemplateProvider): String = tmplProv generateSectionItemWithLocation this
}

case class PlainFormattedSectionItem(itemTitle: String,
                                     content:   String
                                    ) extends FormattedItem {
  def generateLatex(tmplProv: TemplateProvider): String = tmplProv generatePlainSectionItem this
}

case class FormattedBulletPointItem(content: String) extends FormattedItem {
  def generateLatex(tmplProv: TemplateProvider): String = tmplProv generateBulletPointItem this
}