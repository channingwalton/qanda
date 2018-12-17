package io.questions.testdata

import io.questions.model.questionnaire.FieldName.FieldNameStringSyntax
import io.questions.model.questionnaire.QuestionText.QuestionTextSyntax
import io.questions.model.questionnaire.nodekey.NodeKey
import io.questions.model.questionnaire._
import io.questions.testdata.samples.samplequestionnaire.QuestionTypes._
import io.questions.testdata.samples.enumerations.{ Country, Currencies, YesNo }

object OfficerRelationshipQuestionnaire {
  val id: QuestionnaireId = QuestionnaireId.random

  val position: QuestionnaireNode =
    stringQuestion("position".fieldName, "Position".text)

  val occupation: QuestionnaireNode =
    stringQuestion("occupation".fieldName, "Occupation".text)

  val inactive: QuestionnaireNode =
    yesNoQuestion("inactive".fieldName, "Inactive?".text)

  val startDate: QuestionnaireNode =
    dateQuestion("startDate".fieldName, "Start Date".text)

  val endDate: QuestionnaireNode =
    dateQuestion("endDate".fieldName, "End Date".text).makeOptional

  val informationSection: QuestionnaireNode = pageQuestion(
    "information".fieldName,
    "Officer Information".text,
    position,
    occupation,
    inactive,
    startDate,
    endDate
  )

  val questionnaire: QuestionnaireNode = ExampleComponents.standard(
    NodeKey.random,
    "officer".fieldName,
    "Officer".text,
    Element.NonRepeatingParent(
      informationSection
    ),
    enums = Map(
      Country.name    → Country.values,
      YesNo.name      → YesNo.values,
      Currencies.name → Currencies.values
    ),
    questionnaireId = id
  )
}
