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
import io.questions.model.questionnaire._
import io.questions.testdata.samples.samplequestionnaire.QuestionTypes._
import io.questions.testdata.samples.enumerations.{ Country, Currencies, YesNo }

object OfficerRelationshipQuestionnaire {
  val id: QuestionnaireId = QuestionnaireId.random

  val position: QuestionnaireNode =
    stringQuestion("position".fieldName, "Position".text)

  val occupation: QuestionnaireNode =
    stringQuestion("occupation".fieldName, "Occupation".text)

  val inactive: QuestionnaireNode =
    yesNoQuestion("inactive".fieldName, "Inactive?".text)

  val startDate: QuestionnaireNode =
    dateQuestion("startDate".fieldName, "Start Date".text)

  val endDate: QuestionnaireNode =
    dateQuestion("endDate".fieldName, "End Date".text).makeOptional

  val informationSection: QuestionnaireNode = pageQuestion(
    "information".fieldName,
    "Officer Information".text,
    position,
    occupation,
    inactive,
    startDate,
    endDate
  )

  val questionnaire: QuestionnaireNode = ExampleComponents.standard(
    NodeKey.random,
    "officer".fieldName,
    "Officer".text,
    Element.NonRepeatingParent(
      informationSection
    ),
    enums = Map(
      Country.name    → Country.values,
      YesNo.name      → YesNo.values,
      Currencies.name → Currencies.values
    ),
    questionnaireId = id
  )
}
