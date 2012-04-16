package li2latex.config

import java.text.SimpleDateFormat

object AppConfig {
  val API_KEY = "p8fo989psm4n"
  val API_SECRET = "gdQ3X9ZqXpL1IjzY"

  val API_DATA_URL = "http://api.linkedin.com/v1/people/~:(%s)"

  val DEFAULT_CONTACT_INFO_FIELDS = "phone-numbers,im-accounts,main-address"

  val DATE_FORMATTER = new SimpleDateFormat("MM/yyyy")
}
