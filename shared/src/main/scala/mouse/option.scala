package mouse

import scala.util.{Failure, Success, Try}

trait OptionSyntax {
  implicit final def optionSyntaxMouse[A](oa: Option[A]): OptionOps[A] = new OptionOps(oa)
}

final class OptionOps[A](val oa: Option[A]) extends AnyVal {

  def cata[B](some: A => B, none: => B): B = oa.fold[B](none)(some)

  def toTry(ex: =>Throwable): Try[A] = oa match {
    case Some(x) => Success(x)
    case None => Failure(ex)
  }

  def toTryMsg(msg: =>String): Try[A] = toTry(new RuntimeException(msg))

  /**
   * Same as oa.toRight except that it fixes the type to Either[B, A]
   * On Scala prior to 2.12, toRight returns `Serializable with Product with Either[B, A]`
   */
  def right[B](b: => B) : Either[B, A] = oa.toRight(b)

  /**
   * Same as oa.toLeft except that it fixes the type to Either[A, B]
   * On Scala prior to 2.12, toLeft returns `Serializable with Product with Either[A, B]`
   */
  def left[B](b: => B) : Either[A, B] = oa.toLeft(b)
}
