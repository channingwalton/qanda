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

package io.questions.testdata.samples.enumerations

import cats.data.NonEmptyList
import io.questions.model.questionnaire.{ EnumerationName, EnumerationValue }

object RelationshipStatus {
  val name = EnumerationName("RelationshipStatus")

  val marriedOrCivilPartnership: EnumerationValue = EnumerationValue.of("Married/Civil partnership")
  val single: EnumerationValue                    = EnumerationValue.of("Single")
  val separated: EnumerationValue                 = EnumerationValue.of("Separated")
  val widowed: EnumerationValue                   = EnumerationValue.of("Widowed")
  val divorcedOrDissolved: EnumerationValue       = EnumerationValue.of("Divorced/Dissolved")
  val livingWithParents: EnumerationValue         = EnumerationValue.of("Living with parents")

  val values: NonEmptyList[EnumerationValue] = NonEmptyList
    .of(
      marriedOrCivilPartnership,
      single,
      separated,
      widowed,
      divorcedOrDissolved,
      livingWithParents
    )
}
