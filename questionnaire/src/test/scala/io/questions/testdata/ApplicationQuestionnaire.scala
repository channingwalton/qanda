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

import eu.timepit.refined.auto._
import io.questions.model.questionnaire.FieldName.FieldNameStringSyntax
import io.questions.model.questionnaire.QuestionText.QuestionTextSyntax
import io.questions.model.questionnaire.nodekey.NodeKey
import io.questions.model.questionnaire.{ Element, QuestionnaireId, QuestionnaireNode }
import io.questions.testdata.samples.enumerations.YesNo
import io.questions.testdata.samples.samplequestionnaire.QuestionTypes.pageQuestion
object ApplicationQuestionnaire {
  val id: QuestionnaireId = QuestionnaireId.random

  val approvalQuestion: QuestionnaireNode =
    ExampleComponents.approvalQuestion("applicationApproved".fieldName, "Do you approve this application?".text)

  val approvalPage: QuestionnaireNode = pageQuestion(
    "approvalPage".fieldName,
    "Approval".text,
    approvalQuestion
  )

  val questionnaire: QuestionnaireNode = ExampleComponents.standard(
    NodeKey.random,
    "parties".fieldName,
    "Parties".text,
    Element.NonRepeatingParent(
      approvalPage
    ),
    enums = Map(
      YesNo.name → YesNo.values,
    ),
    questionnaireId = id
  )
}
