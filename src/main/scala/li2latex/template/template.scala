package li2latex.template

class TemplateProvider(templateName: String) {
  val getNameTemplate: String => String =
    name => String.format("""
                          \documentclass[margin,line]{resume}
                          \usepackage{array}

                          \begin{document}
                          \name{\Large Zhe Fu}
                          \begin{resume}""", name)
}

object TemplateProvider {
  def apply(templateName: String) = new TemplateProvider(templateName)
}
