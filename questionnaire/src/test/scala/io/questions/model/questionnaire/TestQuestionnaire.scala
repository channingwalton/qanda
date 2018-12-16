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

import io.questions.testdata.ExampleComponents
import io.questions.model.questionnaire.Element.{ NonRepeatingParent, Primitive, RepeatingParent }
import io.questions.model.questionnaire.FieldName.FieldNameStringSyntax
import io.questions.model.questionnaire.NodeMetadata.{ PageTag, SectionTag }
import io.questions.model.questionnaire.PrimitiveAnswer.{ IntAnswer, StringAnswer }
import io.questions.model.questionnaire.QuestionText.QuestionTextSyntax
import io.questions.model.questionnaire.nodekey.{ NodeKey, NodeKeyExtension }
import io.questions.testdata.ExampleComponents
import io.questions.testdata.samples.enumerations.Country
import io.questions.testdata.samples.samplequestionnaire.QuestionTypes.countryQuestion
object TestQuestionnaire {
  // a questionnaire
  val address: QuestionnaireNode =
    QuestionnaireNode(
      NodeKey.random,
      "address".fieldName,
      "Address".text,
      NonRepeatingParent(
        QuestionnaireNode(nodekey.NodeKey.random, FieldName("line1"), QuestionText("Line 1"), Primitive(StringAnswer(None))),
        QuestionnaireNode(nodekey.NodeKey.random, FieldName("line2"), QuestionText("Line 2"), Primitive(StringAnswer(None))),
        QuestionnaireNode(nodekey.NodeKey.random, FieldName("fromDate"), QuestionText("From"), Primitive(StringAnswer(None))),
      )
    )

  val firstName: QuestionnaireNode =
    QuestionnaireNode(nodekey.NodeKey.random, FieldName("firstName"), QuestionText("First name"), Primitive(StringAnswer(None)))
  val lastName: QuestionnaireNode =
    QuestionnaireNode(nodekey.NodeKey.random, FieldName("lastName"), QuestionText("Last name"), Primitive(StringAnswer(None)))
  val age: QuestionnaireNode =
    QuestionnaireNode(nodekey.NodeKey.random, FieldName("age"), QuestionText("Age"), Primitive(IntAnswer(None)))

  val homeAddresses: QuestionnaireNode =
    QuestionnaireNode(nodekey.NodeKey.random, FieldName("homeAddresses"), QuestionText("Home Addresses"), RepeatingParent(address))

  val countryOfNationality: QuestionnaireNode =
    QuestionnaireNode(nodekey.NodeKey.random, FieldName("nationality"), QuestionText("Nationality"), Primitive(StringAnswer(None)))
  val countryOfBirth: QuestionnaireNode = countryQuestion("countryOfBirth".fieldName, "Country of birth".text)
  val countriesOfNationality: QuestionnaireNode =
    QuestionnaireNode(nodekey.NodeKey.random,
                      FieldName("nationalities"),
                      QuestionText("Nationalities"),
                      RepeatingParent(countryOfNationality))

  val firstPage: QuestionnaireNode =
    QuestionnaireNode(
      NodeKey.random,
      FieldName("firstPage"),
      QuestionText("First Page"),
      NonRepeatingParent(firstName, lastName),
      metadata = Seq(PageTag)
    )
  val secondPage: QuestionnaireNode =
    QuestionnaireNode(
      NodeKey.random,
      FieldName("secondPage"),
      QuestionText("Second Page"),
      NonRepeatingParent(homeAddresses),
      metadata = Seq(PageTag)
    )
  val thirdPage: QuestionnaireNode =
    QuestionnaireNode(
      NodeKey.random,
      FieldName("thirdPage"),
      QuestionText("Third Page"),
      NonRepeatingParent(age),
      metadata = Seq(PageTag)
    )
  val fourthPage: QuestionnaireNode =
    QuestionnaireNode(
      NodeKey.random,
      FieldName("fourthPage"),
      QuestionText("Fourth Page"),
      NonRepeatingParent(countryOfBirth, countriesOfNationality),
      metadata = Seq(PageTag)
    )
  val firstSection: QuestionnaireNode =
    QuestionnaireNode(
      NodeKey.random,
      FieldName("firstSection"),
      QuestionText("First Section"),
      NonRepeatingParent(firstPage, secondPage),
      metadata = Seq(SectionTag)
    )

  val secondSection: QuestionnaireNode =
    QuestionnaireNode(
      NodeKey.random,
      FieldName("secondSection"),
      QuestionText("Second Section"),
      NonRepeatingParent(thirdPage, fourthPage),
      metadata = Seq(SectionTag)
    )

  def questionnaire: QuestionnaireNode =
    ExampleComponents
      .standard(
        NodeKey.random,
        "personalQuestions".fieldName,
        "Personal Questions".text,
        NonRepeatingParent(firstSection, secondSection),
        enums = Map(
          Country.name → Country.values
        )
      )

  implicit private def stringToFieldName(s: String): FieldName = FieldName(s)

  private val homeAddressesKey = (questionnaire /-/ "homeAddresses").key
  private val nationalitiesKey = (questionnaire /-/ "nationalities").key

  val filledQuestionnaire: QuestionnaireNode = {
    for {
      q1  ← questionnaire.answerByPath(_ /-/ "firstName", StringAnswer(Some("Channing")))
      q2  ← q1.answerByPath(_ /-/ "lastName", StringAnswer(Some("Walton")))
      q3  ← q2.answerByPath(_ /-/ "age", IntAnswer(Some(60)))
      q4  ← q3.answerByPath(_ /-/ "homeAddresses" / 0 / "line1", StringAnswer(Some("14 Orchid Drive")))
      q5  ← q4.answerByPath(_ /-/ "homeAddresses" / 0 / "line2", StringAnswer(Some("Woking")))
      q6  ← q5.appendChild(homeAddressesKey, NodeKeyExtension.random)
      q7  ← q6.answerByPath(_ /-/ "homeAddresses" / 1 / "line1", StringAnswer(Some("Bloomfield Close")))
      q8  ← q7.answerByPath(_ /-/ "homeAddresses" / 1 / "line2", StringAnswer(Some("Woking")))
      q9  ← q8.appendChild(homeAddressesKey, NodeKeyExtension.random)
      q10 ← q9.answerByPath(_ /-/ "homeAddresses" / 2 / "line1", StringAnswer(Some("Bisley")))
      q11 ← q10.answerByPath(_ /-/ "homeAddresses" / 2 / "line2", StringAnswer(Some("Woking")))
      q12 ← q11.answerByPath(_ /-/ "nationalities" / 0, StringAnswer(Some("UK")))
      q13 ← q12.appendChild(nationalitiesKey, NodeKeyExtension.random)
      q14 ← q13.answerByPath(_ /-/ "nationalities" / 1, StringAnswer(Some("USA")))
    } yield q14
  }.fold(err ⇒ throw new RuntimeException(err), identity)
}

// scalastyle:off
object PrintTestQuestionnaire extends App {
  import TestQuestionnaire._
  println(QuestionnaireNode.show(filledQuestionnaire))

  import QuestionnaireNodePredicate._
  import Path._

  implicit private def stringToFieldName(s: String): FieldName = FieldName(s)

  println(
    (root / homeAddresses.keyBase) Ǝ (root / (filledQuestionnaire /-/ "homeAddresses" / 0 / "line1").keyBase === "Bisley") apply filledQuestionnaire
  )
  println(
    (root / homeAddresses.keyBase) ∀ (root / (filledQuestionnaire /-/ "homeAddresses" / 0 / "line2").keyBase === "Woking") apply filledQuestionnaire
  )
}
