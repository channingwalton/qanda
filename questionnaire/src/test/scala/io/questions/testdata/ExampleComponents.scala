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

package io.questions.testdata

import java.util.UUID

import cats.data.NonEmptyList
import io.questions.model.questionnaire.Element.Primitive
import io.questions.model.questionnaire.Path.root
import io.questions.model.questionnaire.PrimitiveAnswer.{ EnumerationAnswer, StringAnswer }
import io.questions.model.questionnaire._
import io.questions.model.questionnaire.nodekey.NodeKey
import io.questions.testdata.samples.enumerations.YesNo
import io.questions.model.questionnaire.QuestionnaireNodePredicate._
import Components._

object ExampleComponents {

  val isBackOfficeUserKey: NodeKey = NodeKey("d5a26526-02a8-41cc-9030-03cc83d6e3c1")

  def id(uuid: UUID): QuestionnaireNode =
    QuestionnaireNode(
      idKey,
      FieldName("id"),
      QuestionText("ID"),
      Primitive(StringAnswer(Some(uuid.toString)))
    )

  def isBackOfficeUser: QuestionnaireNode =
    QuestionnaireNode(
      isBackOfficeUserKey,
      FieldName("isBackOfficeUser"),
      QuestionText("Is the current user a backoffice user?"),
      Primitive(EnumerationAnswer(None, YesNo.name))
    )

  def metadata(questionnaireId: QuestionnaireId): QuestionnaireNode =
    QuestionnaireNode(
      metadataKey,
      FieldName("metadata"),
      QuestionText("metadata"),
      Element.NonRepeatingParent(id(questionnaireId.id), isBackOfficeUser),
      visibility = QuestionnaireNodePredicate.False
    )

  // scalastyle:off parameter.number
  def standard(key: NodeKey,
               name: FieldName,
               text: QuestionText,
               element: Element.Parent,
               optional: QuestionnaireNodePredicate = QuestionnaireNodePredicate.False,
               validation: Validation = Validation.AlwaysValid,
               visibility: QuestionnaireNodePredicate = QuestionnaireNodePredicate.True,
               editability: QuestionnaireNodePredicate = QuestionnaireNodePredicate.True,
               meta: Seq[NodeMetadata] = Seq.empty,
               enums: Map[EnumerationName, NonEmptyList[EnumerationValue]] = Map.empty,
               questionnaireId: QuestionnaireId = QuestionnaireId.random): QuestionnaireNode =
    QuestionnaireNode(
      key: NodeKey,
      name,
      text,
      element.copy(children = enumerations(enums) :: metadata(questionnaireId) :: element.children),
      optional,
      validation,
      visibility,
      editability,
      meta
    )

  def approvalQuestion(fieldName: FieldName, question: QuestionText): QuestionnaireNode =
    QuestionnaireNode(
      NodeKey.random,
      fieldName,
      question,
      Element.Primitive(PrimitiveAnswer.EnumerationAnswer(None, YesNo.name)),
      visibility = root / ExampleComponents.isBackOfficeUserKey.base === YesNo.yes.key
    )
}
