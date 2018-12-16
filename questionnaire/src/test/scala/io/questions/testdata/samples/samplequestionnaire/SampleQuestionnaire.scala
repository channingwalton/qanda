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

package io.questions.testdata.samples.samplequestionnaire

import cats.syntax.show._
import io.questions.testdata.ExampleComponents
import io.questions.model.questionnaire.FieldName.FieldNameStringSyntax
import io.questions.model.questionnaire.QuestionText.QuestionTextSyntax
import io.questions.model.questionnaire.integrity.IntegrityError
import io.questions.model.questionnaire.nodekey.NodeKey
import io.questions.model.questionnaire.{ Element, QuestionnaireId, QuestionnaireNode }
import io.questions.testdata.samples.enumerations._
import io.questions.testdata.ExampleComponents

object SampleQuestionnaire {
  val id: QuestionnaireId = QuestionnaireId.random

  val questionnaire: QuestionnaireNode = ExampleComponents.standard(
    NodeKey.random,
    "applicant".fieldName,
    "Applicant".text,
    Element.NonRepeatingParent(
      SampleQuestionsSection.section,
      DependentQuestionsSection.section,
      OtherElementsSection.section
    ),
    enums = Map(
      Country.name            → Country.values,
      Gender.name             → Gender.values,
      RelationshipStatus.name → RelationshipStatus.values,
      Title.name              → Title.values,
      YesNo.name              → YesNo.values,
      Currencies.name         → Currencies.values
    ),
    questionnaireId = id
  )

  private val integrity: Set[IntegrityError] = questionnaire.checkIntegrity
  if (integrity.nonEmpty) println(s"Integrity errors found in the SampleQuestionnaire:\n${integrity.map(_.show).mkString("\n")}") //scalastyle:ignore
}
