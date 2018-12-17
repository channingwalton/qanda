package io.questions.testdata

import io.questions.model.questionnaire.nodekey.NodeKey
import io.questions.model.questionnaire.{ Element, FieldName, QuestionText, QuestionnaireNode }
import io.questions.testdata.samples.questions.PersonalQuestions

object PersonIdentifierQuestionnaire {
  val questionnaires: List[QuestionnaireNode] = List(
    OfficerQuestionnaire.officerNameFromOpenCorporates,
    QuestionnaireNode(NodeKey.random,
                      FieldName(""),
                      QuestionText(""),
                      Element.NonRepeatingParent(PersonalQuestions.currentFirstName, PersonalQuestions.currentLastName))
  )
}
