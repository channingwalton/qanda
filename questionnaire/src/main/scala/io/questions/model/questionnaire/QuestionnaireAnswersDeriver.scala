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

package io.questions.model.questionnaire

import cats.data.NonEmptyList
import cats.syntax.eq._
import io.questions.derivedvalues.DerivedValuesResult.{ InsufficientData, Result }
import io.questions.model.questionnaire.Element.{ Derived, Enumerations, Info, Parent, Primitive }

object QuestionnaireAnswersDeriver {
  def apply(root: QuestionnaireNode): QuestionnaireNode = {
    def recurse(n: QuestionnaireNode): QuestionnaireNode =
      n.element match {
        case p: Parent       ⇒ updateParent(n, p, p.children.map(recurse))
        case _: Primitive    ⇒ n
        case d: Derived      ⇒ derived(root, n, d)
        case _: Info         ⇒ n
        case _: Enumerations ⇒ n
      }

    val updated = recurse(root)
    if (updated.eq(root)) root else apply(updated)
  }

  private def updateParent(n: QuestionnaireNode, p: Parent, newChildren: NonEmptyList[QuestionnaireNode]): QuestionnaireNode =
    if (newChildren === p.children) n else n.copy(element = p.copy(children = newChildren))

  // We should be handling type mismatch errors here
  private def derived(root: QuestionnaireNode, n: QuestionnaireNode, d: Derived): QuestionnaireNode =
    d.deriver(root, n.key) match {
      case _: InsufficientData ⇒ insufficientData(n, d)
      case i: Result           ⇒ applyResult(n, d, i.result)
      case _                   ⇒ n
    }

  private def insufficientData(n: QuestionnaireNode, d: Derived): QuestionnaireNode = updateDerivedAnswer(n, d, d.answer.blank)

  private def applyResult(n: QuestionnaireNode, d: Derived, deriverAnswer: PrimitiveAnswer): QuestionnaireNode =
    if (d.answer.getClass != deriverAnswer.getClass)
      throw new IllegalArgumentException(s"Expected derived answer of type ${d.answer.getClass}. Got $deriverAnswer")
    else updateDerivedAnswer(n, d, deriverAnswer)

  private def updateDerivedAnswer(n: QuestionnaireNode, d: Derived, answer: PrimitiveAnswer): QuestionnaireNode =
    if (d.answer === answer) n else n.copy(element = d.copy(answer = answer))
}
