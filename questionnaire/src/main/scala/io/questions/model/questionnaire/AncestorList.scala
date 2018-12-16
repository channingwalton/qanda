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
import io.questions.model.questionnaire.nodekey.NodeKeyBase

sealed trait AncestorList {
  def current: QuestionnaireNode
  def root: QuestionnaireNode
  def rootList: AncestorList
  def ::(node: QuestionnaireNode): AncestorList
  def find(base: NodeKeyBase): Option[AncestorList]
}

object AncestorList {
  case class AncestorList0(root: QuestionnaireNode, ancestors: NonEmptyList[QuestionnaireNode]) extends AncestorList {
    def current: QuestionnaireNode                = ancestors.head
    def rootList: AncestorList                    = apply(root)
    def ::(node: QuestionnaireNode): AncestorList = copy(ancestors = node :: ancestors)
    def find(base: NodeKeyBase): Option[AncestorList] =
      NonEmptyList.fromList(ancestors.toList.dropWhile(_.keyBase =!= base)).map(AncestorList0(root, _))
  }

  def apply(root: QuestionnaireNode): AncestorList = AncestorList0(root, NonEmptyList.of(root))
}
