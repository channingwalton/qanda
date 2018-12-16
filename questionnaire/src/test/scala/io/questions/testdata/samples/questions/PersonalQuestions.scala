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

package io.questions.testdata.samples.questions

import io.questions.model.questionnaire.FieldName.FieldNameStringSyntax
import io.questions.model.questionnaire.QuestionText.QuestionTextSyntax
import io.questions.model.questionnaire._
import io.questions.model.questionnaire.QuestionnaireNodePredicate._
import io.questions.testdata.samples.samplequestionnaire.QuestionTypes.{ stringQuestion, titleQuestion }

object PersonalQuestions {
  def title: QuestionnaireNode = titleQuestion("title".fieldName, "Title".text)
  def firstName: QuestionnaireNode =
    stringQuestion("firstName".fieldName, "First name".text)
      .validateSelf(_.isAlphabetic, "Only letters are allowed")

  def middleNames: QuestionnaireNode =
    stringQuestion("middleNames".fieldName, "Middle names".text).makeOptional.validateSelf(_.isAlphabetic, "Only letters are allowed")
  def lastName: QuestionnaireNode =
    stringQuestion("lastName".fieldName, "Last name".text).validateSelf(_.isAlphabetic, "Only letters are allowed")

  def fullName(fieldName: FieldName, label: String): QuestionnaireNode =
    nonRepeatingParentQuestion(fieldName, label.text, title, firstName, middleNames, lastName)

  val currentFirstName: QuestionnaireNode = firstName
  val currentLastName: QuestionnaireNode  = lastName
  val currentFullName: QuestionnaireNode =
    nonRepeatingParentQuestion("currentFullName".fieldName, "Name".text, title, currentFirstName, middleNames, currentLastName)
}
