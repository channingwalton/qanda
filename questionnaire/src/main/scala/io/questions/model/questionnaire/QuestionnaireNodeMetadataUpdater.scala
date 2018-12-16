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

import cats.syntax.eq._
import io.questions.model.questionnaire.Element.Parent
import io.questions.model.questionnaire.nodekey.NodeKey

object QuestionnaireNodeMetadataUpdater {
  def apply(rootNode: QuestionnaireNode, targetNodeKey: NodeKey, newMetadata: Seq[NodeMetadata]): Either[String, QuestionnaireNode] =
    for {
      _ ← rootNode.find(targetNodeKey)
    } yield updateMetadata(rootNode, targetNodeKey, newMetadata)

  private def updateMetadata(current: QuestionnaireNode, targetNodeKey: NodeKey, newMetadata: Seq[NodeMetadata]): QuestionnaireNode =
    current.element match {
      case _ if current.key === targetNodeKey ⇒
        current.copy(metadata = newMetadata)
      case p: Parent ⇒
        current.copy(element = p.copy(children = p.children.map(updateMetadata(_, targetNodeKey, newMetadata))))
      case _ ⇒ current
    }

}
