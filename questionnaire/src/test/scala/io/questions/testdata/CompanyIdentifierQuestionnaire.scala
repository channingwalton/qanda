package io.questions.testdata

import io.questions.model.questionnaire.FieldName.FieldNameStringSyntax
import io.questions.model.questionnaire.QuestionText.QuestionTextSyntax
import io.questions.model.questionnaire.nodekey.NodeKey
import io.questions.model.questionnaire.{ Element, QuestionnaireNode }

object CompanyIdentifierQuestionnaire {
  // don't use Components.standard as we don't want metadata etc. here
  val questionnaires: List[QuestionnaireNode] = List(
    QuestionnaireNode(
      NodeKey.random,
      "company".fieldName,
      "Company".text,
      Element.NonRepeatingParent(
        CompanyQuestionnaire.companyName,
        CompanyQuestionnaire.companyNumber
      )
    )
  )
}
