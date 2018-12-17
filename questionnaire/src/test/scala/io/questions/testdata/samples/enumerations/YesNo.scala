package io.questions.testdata.samples.enumerations

import cats.data.NonEmptyList
import io.questions.model.questionnaire.{ EnumerationName, EnumerationValue }

object YesNo {
  val name = EnumerationName("YesNo")

  val yes: EnumerationValue = EnumerationValue.of("Yes")
  val no: EnumerationValue  = EnumerationValue.of("No")

  val values: NonEmptyList[EnumerationValue] = NonEmptyList
    .of(
      yes,
      no
    )

  def fromBoolean(b: Boolean): EnumerationValue =
    if (b) yes else no
}
