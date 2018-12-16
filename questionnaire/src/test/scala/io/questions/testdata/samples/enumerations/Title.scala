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

object Title {
  val name = EnumerationName("Title")

  val values: NonEmptyList[EnumerationValue] = NonEmptyList
    .of(
      "Mr",
      "Mrs",
      "Miss",
      "Ms",
      "Dr",
      "Admiral",
      "Ambassador",
      "Archbishop",
      "Asst. Commissioner",
      "Baron",
      "Baroness",
      "Baronet",
      "Bishop",
      "Brother",
      "Captain",
      "Cardinal",
      "Chief",
      "Chief Inspector",
      "Colonel",
      "Count",
      "Countess",
      "Dame",
      "Dean",
      "Duchess",
      "Duke",
      "Earl",
      "Father",
      "General",
      "Governor",
      "Her Excellency",
      "Her Royal Highness",
      "His Royal Highness",
      "HRH Prince",
      "HRH Princess",
      "Judge",
      "Lady",
      "Lord",
      "Lieutenant",
      "Madam",
      "Madame",
      "Mademoiselle",
      "Major",
      "Major General",
      "Mayor",
      "Mayoress",
      "President",
      "Prince",
      "Princess",
      "Professor",
      "Rabbi",
      "Reverend",
      "Right Reverend",
      "Sheikh",
      "Sir",
      "The Right Honourable",
      "Viscount",
      "Viscountess"
    )
    .map(EnumerationValue.of)
}
