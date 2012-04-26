package li2latex.util

import java.net.MalformedURLException

object Util {
  def isRemoteResource(body: => Unit): Result =
    try {
      body; Ok
    } catch {
      case e: MalformedURLException => Failed
    }
}

trait Result { def otherwise(onError: => Unit): Unit}
case object Ok extends Result { def otherwise(onError: => Unit) = () /* do nothing */}
case object Failed extends Result { def otherwise(onError: => Unit) = onError}