package io.questions.model.questionnaire.integrity

import io.questions.model.questionnaire.QuestionnaireNode

object Integrity {
  def check(questionnaireNode: QuestionnaireNode): Set[IntegrityError] =
    PredicateKeyIntegrityCheck(questionnaireNode) ++
    TypeIntegrityCheck(questionnaireNode) ++
    UniqueKeyIntegrityCheck(questionnaireNode)
}
