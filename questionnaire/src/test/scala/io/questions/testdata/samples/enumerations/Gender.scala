package io.questions.testdata.samples.enumerations

import cats.data.NonEmptyList
import io.questions.model.questionnaire.{ EnumerationName, EnumerationValue }

object Gender {
  val name = EnumerationName("Gender")

  val values: NonEmptyList[EnumerationValue] = NonEmptyList
    .of(
      "Male",
      "Female"
    )
    .map(EnumerationValue.of)
}
