package io.questions.model.questionnaire.integrity

import cats.data.NonEmptySet
import io.questions.model.questionnaire.QuestionnaireNode
import io.questions.model.questionnaire.nodekey.NodeKey

import scala.collection.immutable.SortedSet

object UniqueKeyIntegrityCheck {
  def apply(questionnaireNode: QuestionnaireNode): IntegrityErrorSet = {
    // _1 contains keys found so far, _2 contains duplicates
    def check(s: (Set[NodeKey], Set[NodeKey]), qn: QuestionnaireNode): (Set[NodeKey], Set[NodeKey]) =
      if (s._1.contains(qn.key)) (s._1, s._2 + qn.key) else (s._1 + qn.key, s._2)

    val duplicates = questionnaireNode
      .foldLeft((Set.empty[NodeKey], Set.empty[NodeKey]))(check)
      ._2

    NonEmptySet
      .fromSet(SortedSet(duplicates.toSeq: _*))
      .map(keys ⇒ IntegrityError.NonUniqueKey(keys))
      .toSet
  }
}
