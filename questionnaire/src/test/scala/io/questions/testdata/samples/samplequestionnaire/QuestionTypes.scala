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

import io.questions.model.questionnaire.FieldName._
import io.questions.model.questionnaire.QuestionText._
import io.questions.model.questionnaire.NodeMetadata.{
  DateQuestion,
  DateTimeQuestion,
  DateTimeWithZoneQuestion,
  MoneyQuestion,
  TimeQuestion,
  TimeWithZoneQuestion
}
import io.questions.model.questionnaire._
import io.questions.model.questionnaire.nodekey.NodeKey
import io.questions.testdata.samples.enumerations._

object QuestionTypes {
  def info(name: FieldName, text: QuestionText, message: String): QuestionnaireNode =
    QuestionnaireNode(NodeKey.random, name, text, Element.Info(message))

  def derived(name: FieldName, text: QuestionText, answer: PrimitiveAnswer, deriver: AnswerDeriver): QuestionnaireNode =
    QuestionnaireNode(NodeKey.random, name, text, Element.Derived(answer, deriver))

  def sectionQuestion(name: FieldName, text: QuestionText, first: QuestionnaireNode, rest: QuestionnaireNode*): QuestionnaireNode =
    QuestionnaireNode(NodeKey.random, name, text, Element.NonRepeatingParent(first, rest: _*), metadata = List(NodeMetadata.SectionTag))

  def pageQuestion(name: FieldName, text: QuestionText, first: QuestionnaireNode, rest: QuestionnaireNode*): QuestionnaireNode =
    QuestionnaireNode(NodeKey.random, name, text, Element.NonRepeatingParent(first, rest: _*), metadata = List(NodeMetadata.PageTag))

  def yesNoQuestion(name: FieldName, text: QuestionText): QuestionnaireNode   = enumerationQuestion(name, text, YesNo.name)
  def titleQuestion(name: FieldName, text: QuestionText): QuestionnaireNode   = enumerationQuestion(name, text, Title.name)
  def genderQuestion(name: FieldName, text: QuestionText): QuestionnaireNode  = enumerationQuestion(name, text, Gender.name)
  def countryQuestion(name: FieldName, text: QuestionText): QuestionnaireNode = enumerationQuestion(name, text, Country.name)

  def stringQuestion(name: FieldName, text: QuestionText): QuestionnaireNode =
    QuestionnaireNode(NodeKey.random, name, text, Element.Primitive(PrimitiveAnswer.StringAnswer()))

  def numericQuestion(name: FieldName, text: QuestionText): QuestionnaireNode =
    QuestionnaireNode(NodeKey.random, name, text, Element.Primitive(PrimitiveAnswer.IntAnswer()))

  def bigDecimalQuestion(name: FieldName, text: QuestionText): QuestionnaireNode =
    QuestionnaireNode(NodeKey.random, name, text, Element.Primitive(PrimitiveAnswer.BigDecimalAnswer()))

  def dateQuestion(name: FieldName, text: QuestionText): QuestionnaireNode =
    QuestionnaireNode(NodeKey.random, name, text, Element.Primitive(PrimitiveAnswer.DateTimeAnswer()), metadata = DateQuestion :: Nil)

  def dateTimeQuestion(name: FieldName, text: QuestionText): QuestionnaireNode =
    QuestionnaireNode(NodeKey.random, name, text, Element.Primitive(PrimitiveAnswer.DateTimeAnswer()), metadata = DateTimeQuestion :: Nil)

  def dateTimeWithTimeZoneQuestion(name: FieldName, text: QuestionText): QuestionnaireNode =
    QuestionnaireNode(NodeKey.random,
                      name,
                      text,
                      Element.Primitive(PrimitiveAnswer.DateTimeAnswer()),
                      metadata = DateTimeWithZoneQuestion :: Nil)

  def timeQuestion(name: FieldName, text: QuestionText): QuestionnaireNode =
    QuestionnaireNode(NodeKey.random, name, text, Element.Primitive(PrimitiveAnswer.DateTimeAnswer()), metadata = TimeQuestion :: Nil)

  def timeWithTimeZoneQuestion(name: FieldName, text: QuestionText): QuestionnaireNode =
    QuestionnaireNode(NodeKey.random,
                      name,
                      text,
                      Element.Primitive(PrimitiveAnswer.DateTimeAnswer()),
                      metadata = TimeWithZoneQuestion :: Nil)

  def enumerationQuestion(fieldName: FieldName, text: QuestionText, name: EnumerationName): QuestionnaireNode =
    QuestionnaireNode(NodeKey.random, fieldName, text, Element.Primitive(PrimitiveAnswer.EnumerationAnswer(None, name)))

  def moneyQuestion(name: FieldName,
                    text: QuestionText,
                    optional: QuestionnaireNodePredicate = QuestionnaireNodePredicate.False,
                    validation: Validation = Validation.AlwaysValid,
                    visibility: QuestionnaireNodePredicate = QuestionnaireNodePredicate.True,
                    editability: QuestionnaireNodePredicate = QuestionnaireNodePredicate.True,
                    additionalMeta: Seq[NodeMetadata] = Seq.empty): QuestionnaireNode = {
    val currency = enumerationQuestion("currency".fieldName, "Currency".text, Currencies.name)
    val amount   = bigDecimalQuestion("amount".fieldName, "Amount".text)
    val element  = Element.NonRepeatingParent(currency, amount)
    QuestionnaireNode(NodeKey.random, name, text, element, optional, validation, visibility, editability, additionalMeta :+ MoneyQuestion)
  }

}
