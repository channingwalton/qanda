package io.questions.testdata.samples.enumerations

import cats.data.NonEmptyList
import io.questions.model.questionnaire.{ EnumerationName, EnumerationValue }

object RelationshipStatus {
  val name = EnumerationName("RelationshipStatus")

  val marriedOrCivilPartnership: EnumerationValue = EnumerationValue.of("Married/Civil partnership")
  val single: EnumerationValue                    = EnumerationValue.of("Single")
  val separated: EnumerationValue                 = EnumerationValue.of("Separated")
  val widowed: EnumerationValue                   = EnumerationValue.of("Widowed")
  val divorcedOrDissolved: EnumerationValue       = EnumerationValue.of("Divorced/Dissolved")
  val livingWithParents: EnumerationValue         = EnumerationValue.of("Living with parents")

  val values: NonEmptyList[EnumerationValue] = NonEmptyList
    .of(
      marriedOrCivilPartnership,
      single,
      separated,
      widowed,
      divorcedOrDissolved,
      livingWithParents
    )
}
