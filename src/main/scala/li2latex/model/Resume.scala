package li2latex.model

case class Resume(sections: Seq[Section]) {
  def this() = this(Nil)
}
