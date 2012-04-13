package li2latex.model

import li2latex.template.TemplateProvider
import li2latex.TemplateProvider

trait Section {
  def toLatexStr: String
}

case class NameSection(name: String) extends Section {
  def toLatexStr: String = TemplateProvider("aa").getNameTemplate(name)
}

case class SectionWithTitle(title: String) extends Section {
  def toLatexStr: String
}

