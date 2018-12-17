package io.questions.model.questionnaire.completion

import cats.instances.either._
import cats.instances.list._
import cats.syntax.foldable._
import io.questions.model.questionnaire.Element.{ Derived, Enumerations, Info, Parent, Primitive }
import io.questions.model.questionnaire.{ PrimitiveAnswer, QuestionnaireNode }

object CompletionEvaluator {
  def apply(root: QuestionnaireNode): Either[String, Completion] = {
    def eval(questionnaire: QuestionnaireNode): Either[String, Completion] =
      questionnaire
        .visibility(root)
        .flatMap(
          visible ⇒
            questionnaire
              .optional(root)
              .flatMap(
                opt ⇒
                  if (opt || !visible) Right(Completion(0, 0))
                  else
                    questionnaire.element match {
                      case p: Parent       ⇒ p.toList.foldMap(eval)
                      case p: Primitive    ⇒ Right(answerCompletion(p.answer))
                      case d: Derived      ⇒ Right(answerCompletion(d.answer))
                      case _: Info         ⇒ Right(Completion(0, 0))
                      case _: Enumerations ⇒ Right(Completion(0, 0))
                  }
            )
        )

    eval(root)
  }

  def answerCompletion(primAnswer: PrimitiveAnswer): Completion = primAnswer match {
    case p: PrimitiveAnswer ⇒ Completion(1, if (p.isAnswered) 1 else 0)
  }
}
