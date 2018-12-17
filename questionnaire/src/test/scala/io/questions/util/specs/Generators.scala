package io.questions.util.specs

import java.time.{ LocalDate, LocalTime, ZoneId }

import cats.{ Functor, Semigroupal }
import io.questions.model.questionnaire.PrimitiveAnswer
import org.scalacheck.Gen
import cats.syntax.apply._
import io.questions.model.questionnaire.PrimitiveAnswer._
import io.questions.testdata.samples.enumerations.Country
import org.scalacheck.Gen.asciiPrintableChar
import com.fortysevendeg.scalacheck.datetime.jdk8.GenJdk8

//scalastyle:off
trait Generators {

  implicit object GenTC extends Functor[Gen] with Semigroupal[Gen] {
    override def map[A, B](fa: Gen[A])(f: A ⇒ B): Gen[B] = fa.map(f)

    override def product[A, B](fa: Gen[A], fb: Gen[B]): Gen[(A, B)] =
      for {
        a ← fa
        b ← fb
      } yield (a, b)
  }

  def aString(length: Int = 10): String =
    Gen.listOfN(length, asciiPrintableChar).map(_.mkString).sample.get

  def pair[A, B](a: Gen[A], b: Gen[B]): Gen[(A, B)] =
    (a, b).mapN((a, b) ⇒ (a, b))

  val keyValue: Gen[(String, String)] =
    pair(nonEmptyString(), nonEmptyString())

  val nonEmptyMap: Gen[Map[String, String]] =
    Gen.nonEmptyMap(keyValue)

  def nonEmptyString(min: Int = 1, max: Int = 10): Gen[String] = {
    assert(min < max, "min must be smaller than max")
    Gen.choose(min, max - 2).map(aString(_) + "-")
  }

  def stringAnswerGen: Gen[StringAnswer] =
    Gen.option(nonEmptyString()).map(StringAnswer)

  def intAnswerGen: Gen[IntAnswer] =
    Gen.option(Gen.posNum[Int]).map(IntAnswer)

  def bigdecimalAnswerGen: Gen[BigDecimalAnswer] =
    Gen.option(Gen.posNum[Long].map(BigDecimal(_))).map(BigDecimalAnswer)

  def enumerationAnswerGen: Gen[EnumerationAnswer] =
    Gen.option(Gen.oneOf(Country.values.toList.map(_.key))).map(EnumerationAnswer(_, Country.name))

  def localDateGen: Gen[LocalDate] = GenJdk8.genZonedDateTime.map(_.toLocalDate)

  def localTimeGen: Gen[LocalTime] = GenJdk8.genZonedDateTime.map(_.toLocalTime)

  def zoneTimeGen: Gen[ZoneId] = GenJdk8.genZonedDateTime.map(_.getZone)

  def dateTimeAnswerGen: Gen[DateTimeAnswer] =
    for {
      date <- Gen.option(localDateGen)
      time ← Gen.option(localTimeGen)
      zone ← Gen.option(zoneTimeGen)
    } yield DateTimeAnswer(date, time, zone)

  def primitiveAnswerGen: Gen[PrimitiveAnswer] =
    Gen.oneOf(
      stringAnswerGen,
      intAnswerGen,
      bigdecimalAnswerGen,
      enumerationAnswerGen,
      dateTimeAnswerGen
    )
}
