package li2latex.template

class TemplateProvider(templateName: String) {
  val getNameTemplate: String => String =
    name => String.format("""""", name)
}

object TemplateProvider {
  def apply(templateName: String) = new TemplateProvider(templateName)
}
