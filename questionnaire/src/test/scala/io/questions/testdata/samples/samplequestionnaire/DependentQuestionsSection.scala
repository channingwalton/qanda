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

import io.questions.testdata.samples.questions.PersonalQuestions._
import io.questions.model.questionnaire.FieldName._
import io.questions.model.questionnaire.Path.{ root, PathSyntax }
import io.questions.model.questionnaire.QuestionText._
import io.questions.model.questionnaire.QuestionnaireNodePredicate._
import io.questions.model.questionnaire.{ repeatingParentQuestion, QuestionnaireNode }
import io.questions.testdata.samples.samplequestionnaire.QuestionTypes._
import io.questions.testdata.samples.enumerations.{ RelationshipStatus, YesNo }

object DependentQuestionsSection {
  val haveYouHadAnyOtherNamesInThePast: QuestionnaireNode =
    yesNoQuestion("haveYouHadAnyOtherNamesInThePast".fieldName, "Have you had any other names in the past?".text)

  val previousFullName: QuestionnaireNode =
    repeatingParentQuestion("previousNames".fieldName,
                            "Previous Name (Parent)".text,
                            fullName("previousName".fieldName, "Previous name (Node)"))
      .visibleWhen(root / haveYouHadAnyOtherNamesInThePast.keyBase === YesNo.yes.key)

  val relationshipStatus: QuestionnaireNode =
    enumerationQuestion("relationshipStatus".fieldName, "Relationship status".text, RelationshipStatus.name)

  val partnerName: QuestionnaireNode =
    fullName("partnerName".fieldName, "Partner's name")
      .visibleWhen(root / relationshipStatus.keyBase === RelationshipStatus.marriedOrCivilPartnership.key)

  val visibilityEffectsPage: QuestionnaireNode = pageQuestion(
    "visibilityQuestionsPage".fieldName,
    "Visibility Effects".text,
    haveYouHadAnyOtherNamesInThePast,
    previousFullName,
    relationshipStatus,
    partnerName
  )

  val section: QuestionnaireNode = sectionQuestion(
    "dependentQuestions".fieldName,
    "Dependent Questions".text,
    visibilityEffectsPage
  )
}
