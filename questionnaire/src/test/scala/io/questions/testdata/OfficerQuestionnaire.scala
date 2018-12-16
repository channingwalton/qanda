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

import io.questions.model.questionnaire.FieldName.FieldNameStringSyntax
import io.questions.model.questionnaire.QuestionText.QuestionTextSyntax
import io.questions.model.questionnaire.nodekey.NodeKey
import io.questions.model.questionnaire.{ Element, QuestionnaireId, QuestionnaireNode, QuestionnaireNodePredicate }
import io.questions.testdata.samples.questions.PersonalQuestions
import io.questions.testdata.samples.samplequestionnaire.QuestionTypes._
import io.questions.testdata.samples.enumerations.{ Title, YesNo }

object OfficerQuestionnaire {
  val id: QuestionnaireId = QuestionnaireId.random

  val officerNameFromOpenCorporates: QuestionnaireNode =
    stringQuestion("officerName".fieldName, "Officer Name (OpenCorporates)".text).copy(editability = QuestionnaireNodePredicate.False)

  val officerName: QuestionnaireNode =
    PersonalQuestions.currentFullName

//  val favouritePet: QuestionnaireNode =
//    stringQuestion("pet".fieldName, "Favourite Pet".text)

  val informationSection: QuestionnaireNode = pageQuestion(
    "information".fieldName,
    "Officer Information".text,
    officerNameFromOpenCorporates,
    officerName
//    ,favouritePet
  )

  val approvalQuestion: QuestionnaireNode =
    ExampleComponents.approvalQuestion("applicationApproved".fieldName, "Do you approve this officer?".text)

  val approvalPage: QuestionnaireNode = pageQuestion(
    "approvalPage".fieldName,
    "Approval".text,
    approvalQuestion
  )

  val questionnaire: QuestionnaireNode = ExampleComponents.standard(
    NodeKey.random,
    "officer".fieldName,
    "Officer".text,
    Element.NonRepeatingParent(
      informationSection,
      approvalPage
    ),
    enums = Map(
      YesNo.name → YesNo.values,
      Title.name → Title.values
    ),
    questionnaireId = id
  )
}
