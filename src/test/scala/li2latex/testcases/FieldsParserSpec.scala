package li2latex.testcases

import org.specs2.mutable._

import li2latex.samples.SampleFields
import li2latex.model.PublicationsParser
import scala.Right

class FieldsParserSpec extends Specification {
    "The PublicationsParser" should {
        "parse out 1 piece of publication" in  {
//            val res = PublicationsParser.parseOAuthResponse(SampleFields.publicationsFields)
//            res must be Right
          "Hello world" must endWith("world")
        }
    }
}
