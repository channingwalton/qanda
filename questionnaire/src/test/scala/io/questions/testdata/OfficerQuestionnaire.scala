package io.questions.testdata

import io.questions.model.questionnaire.FieldName.FieldNameStringSyntax
import io.questions.model.questionnaire.QuestionText.QuestionTextSyntax
import io.questions.model.questionnaire.nodekey.NodeKey
import io.questions.model.questionnaire.{ Element, QuestionnaireId, QuestionnaireNode, QuestionnaireNodePredicate }
import io.questions.testdata.samples.questions.PersonalQuestions
import io.questions.testdata.samples.samplequestionnaire.QuestionTypes._
import io.questions.testdata.samples.enumerations.{ Title, YesNo }

object OfficerQuestionnaire {
  val id: QuestionnaireId = QuestionnaireId.random

  val officerNameFromOpenCorporates: QuestionnaireNode =
    stringQuestion("officerName".fieldName, "Officer Name (OpenCorporates)".text).copy(editability = QuestionnaireNodePredicate.False)

  val officerName: QuestionnaireNode =
    PersonalQuestions.currentFullName

//  val favouritePet: QuestionnaireNode =
//    stringQuestion("pet".fieldName, "Favourite Pet".text)

  val informationSection: QuestionnaireNode = pageQuestion(
    "information".fieldName,
    "Officer Information".text,
    officerNameFromOpenCorporates,
    officerName
//    ,favouritePet
  )

  val approvalQuestion: QuestionnaireNode =
    ExampleComponents.approvalQuestion("applicationApproved".fieldName, "Do you approve this officer?".text)

  val approvalPage: QuestionnaireNode = pageQuestion(
    "approvalPage".fieldName,
    "Approval".text,
    approvalQuestion
  )

  val questionnaire: QuestionnaireNode = ExampleComponents.standard(
    NodeKey.random,
    "officer".fieldName,
    "Officer".text,
    Element.NonRepeatingParent(
      informationSection,
      approvalPage
    ),
    enums = Map(
      YesNo.name → YesNo.values,
      Title.name → Title.values
    ),
    questionnaireId = id
  )
}
