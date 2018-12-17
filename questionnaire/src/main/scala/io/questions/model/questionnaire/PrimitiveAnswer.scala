package io.questions.model.questionnaire

import java.time._

import cats.implicits._
import cats.{ Eq, Show }
import io.circe.{ Decoder, Encoder }
import io.circe.java8.time._
import io.questions.model._
import io.questions.util._

sealed trait PrimitiveAnswer extends Product with Serializable {
  def blank: PrimitiveAnswer
  def isAnswered: Boolean
}

object PrimitiveAnswer extends EncoderHelpers {
  final case class StringAnswer(answer: Option[String] = None) extends PrimitiveAnswer {
    override def blank: PrimitiveAnswer = copy(answer = None)
    override def isAnswered: Boolean    = answer.isDefined
  }

  final case class IntAnswer(answer: Option[Int] = None) extends PrimitiveAnswer {
    override def blank: PrimitiveAnswer = copy(answer = None)
    override def isAnswered: Boolean    = answer.isDefined
  }

  final case class BigDecimalAnswer(answer: Option[BigDecimal] = None) extends PrimitiveAnswer {
    override def blank: PrimitiveAnswer = copy(answer = None)
    override def isAnswered: Boolean    = answer.isDefined
  }

  // answer stores the key of the enumeration, not the display value!
  final case class EnumerationAnswer(answer: Option[String] = None, enumeration: EnumerationName) extends PrimitiveAnswer {
    override def blank: PrimitiveAnswer = copy(answer = None)
    override def isAnswered: Boolean    = answer.isDefined
  }

  final case class DateTimeAnswer(date: Option[LocalDate] = None, time: Option[LocalTime] = None, zone: Option[ZoneId] = None)
      extends PrimitiveAnswer {
    override def blank: PrimitiveAnswer = copy(date = None, time = None, zone = None)
    override def isAnswered: Boolean    = date.isDefined

    def asLocalDate: Either[String, LocalDate] = date.toRight("No LocalDate available")
    def asLocalDateTime: Either[String, LocalDateTime] =
      (date, time) match {
        case (Some(d), Some(t)) ⇒ LocalDateTime.of(d, t).asRight[String]
        case _                  ⇒ "Can't construct a LocalDateTime without a LocalDate or a LocalTime".asLeft[LocalDateTime]
      }
    def asLocalTime: Either[String, LocalTime] = time.toRight("No LocalTime available")

    def asOffsetTime: Either[String, OffsetTime] =
      (date, time, zone) match {
        case (Some(d), Some(t), Some(z)) ⇒
          val offset = z.getRules.getOffset(LocalDateTime.of(d, t))
          OffsetTime.of(t, offset).asRight[String]
        case _ ⇒ "Can't construct an OffsetTime without a LocalDate, LocalTime, or a ZoneId".asLeft[OffsetTime]
      }

    def asZonedDateTime: Either[String, ZonedDateTime] =
      (date, time, zone) match {
        case (Some(d), Some(t), Some(z)) ⇒
          val zoneOffset = z.getRules.getOffset(LocalDateTime.of(d, t))
          ZonedDateTime.of(d, t, zoneOffset).asRight[String]
        case _ ⇒ "Can't construct a ZonedDateTime without a LocalDate, LocalTime, or a ZoneId".asLeft[ZonedDateTime]
      }
  }

  // scalastyle:off cyclomatic.complexity
  def valueEquals(a1: PrimitiveAnswer, a2: PrimitiveAnswer): Either[String, Boolean] =
    (a1, a2) match {
      case (s1: StringAnswer, s2: StringAnswer) ⇒ (s1.answer === s2.answer).asRight

      case (s1: IntAnswer, s2: IntAnswer) ⇒ (s1.answer === s2.answer).asRight

      case (s1: BigDecimalAnswer, s2: BigDecimalAnswer) ⇒ (s1.answer === s2.answer).asRight

      case (s1: EnumerationAnswer, s2: EnumerationAnswer) ⇒ (s1.answer === s2.answer).asRight

      case (s1: DateTimeAnswer, s2: DateTimeAnswer) ⇒
        (s1.date === s2.date && s1.time === s2.time && s1.zone === s2.zone).asRight

      // special case to treat string and enumeration as equivalent for predicates purposes
      case (s1: StringAnswer, s2: EnumerationAnswer) ⇒ (s1.answer === s2.answer).asRight

      case (s1: EnumerationAnswer, s2: StringAnswer) ⇒ (s1.answer === s2.answer).asRight

      case (s1, s2) ⇒ s"Mismatched types in valueEquals: ${s1.getClass} && ${s2.getClass}".asLeft
    }
  // scalastyle:on cyclomatic.complexity

  // scalastyle:off cyclomatic.complexity
  def typeCheck(a1: PrimitiveAnswer, a2: PrimitiveAnswer): Either[String, Boolean] = (a1, a2) match {
    case (_: StringAnswer, _: StringAnswer) ⇒ true.asRight

    case (_: IntAnswer, _: IntAnswer) ⇒ true.asRight

    case (_: BigDecimalAnswer, _: BigDecimalAnswer) ⇒ true.asRight

    case (_: EnumerationAnswer, _: EnumerationAnswer) ⇒ true.asRight

    case (_: DateTimeAnswer, _: DateTimeAnswer) ⇒ true.asRight

    // Special cases for integrity checks in predicates, so we can work with enumerations as strings.
    case (_: StringAnswer, _: EnumerationAnswer) ⇒ true.asRight

    case (_: EnumerationAnswer, _: StringAnswer) ⇒ true.asRight

    case (s1, s2) ⇒ s"Mismatched types in typeCheck: ${s1.getClass} && ${s2.getClass}".asLeft
  }
  // scalastyle:on cyclomatic.complexity

  implicit val show: Show[PrimitiveAnswer] = {
    case s: StringAnswer      => s"StringAnswer[${s.answer}]"
    case i: IntAnswer         => s"IntAnswer[${i.answer}]"
    case bd: BigDecimalAnswer => s"BigDecimalAnswer[${bd.answer}]"
    case e: EnumerationAnswer => s"EnumerationAnswer[${e.answer}]"
    case d: DateTimeAnswer    => s"DateTimeAnswer[date=${d.date}][time=${d.time}][zone=${d.zone}]"
  }

  implicit val encoder: Encoder[PrimitiveAnswer] = deriveCustomEncoder
  implicit val decoder: Decoder[PrimitiveAnswer] = deriveCustomDecoder

  implicit val equal: Eq[PrimitiveAnswer] = Eq.fromUniversalEquals[PrimitiveAnswer]
}
