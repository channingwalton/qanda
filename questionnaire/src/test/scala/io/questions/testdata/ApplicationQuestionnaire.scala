package io.questions.testdata

import eu.timepit.refined.auto._
import io.questions.model.questionnaire.FieldName.FieldNameStringSyntax
import io.questions.model.questionnaire.QuestionText.QuestionTextSyntax
import io.questions.model.questionnaire.nodekey.NodeKey
import io.questions.model.questionnaire.{ Element, QuestionnaireId, QuestionnaireNode }
import io.questions.testdata.samples.enumerations.YesNo
import io.questions.testdata.samples.samplequestionnaire.QuestionTypes.pageQuestion
object ApplicationQuestionnaire {
  val id: QuestionnaireId = QuestionnaireId.random

  val approvalQuestion: QuestionnaireNode =
    ExampleComponents.approvalQuestion("applicationApproved".fieldName, "Do you approve this application?".text)

  val approvalPage: QuestionnaireNode = pageQuestion(
    "approvalPage".fieldName,
    "Approval".text,
    approvalQuestion
  )

  val questionnaire: QuestionnaireNode = ExampleComponents.standard(
    NodeKey.random,
    "parties".fieldName,
    "Parties".text,
    Element.NonRepeatingParent(
      approvalPage
    ),
    enums = Map(
      YesNo.name → YesNo.values,
    ),
    questionnaireId = id
  )
}
