/*
 * Copyright 2018 TBD
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
