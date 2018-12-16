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

import cats.syntax.either._
import cats.syntax.show._
import cats.Show
import io.circe.{ Decoder, Encoder }
import io.questions.model.EncoderHelpers
import io.questions.model.questionnaire.Element.{ Derived, Enumerations, Info, Parent, Primitive }
import io.questions.model.questionnaire.nodekey.NodeKeyBase

trait QuestionnaireNodePredicateApply {
  def apply(ancestors: AncestorList): PredicateResult
  def apply(root: QuestionnaireNode): PredicateResult = apply(AncestorList(root))
}

sealed trait QuestionnaireNodePredicate extends QuestionnaireNodePredicateApply with Product with Serializable {
  def keyBases: Set[NodeKeyBase]
}

object QuestionnaireNodePredicate extends EncoderHelpers {
  case object False extends QuestionnaireNodePredicate {
    def apply(ancestors: AncestorList): PredicateResult = false.asRight

    override def keyBases: Set[NodeKeyBase] = Set.empty
  }

  case object True extends QuestionnaireNodePredicate {
    def apply(ancestors: AncestorList): PredicateResult = true.asRight
    override def keyBases: Set[NodeKeyBase]             = Set.empty
  }

  case class Not(p: QuestionnaireNodePredicate) extends QuestionnaireNodePredicate {
    def apply(ancestors: AncestorList): PredicateResult = p(ancestors).map(x ⇒ !x)
    override def keyBases: Set[NodeKeyBase]             = p.keyBases
  }

  case class And(left: QuestionnaireNodePredicate, right: QuestionnaireNodePredicate) extends QuestionnaireNodePredicate {
    def apply(ancestors: AncestorList): PredicateResult =
      left(ancestors).flatMap(v ⇒ if (v) right(ancestors) else v.asRight)
    override def keyBases: Set[NodeKeyBase] = left.keyBases ++ right.keyBases
  }

  case class Or(left: QuestionnaireNodePredicate, right: QuestionnaireNodePredicate) extends QuestionnaireNodePredicate {
    def apply(ancestors: AncestorList): PredicateResult =
      left(ancestors).flatMap(v ⇒ if (v) v.asRight else right(ancestors))
    override def keyBases: Set[NodeKeyBase] = left.keyBases ++ right.keyBases
  }

  case class NodeSelector(path: Path, predicate: QuestionnaireNodePredicate) extends QuestionnaireNodePredicate {
    def apply(ancestors: AncestorList): PredicateResult = path(ancestors).flatMap(predicate(_))
    override def keyBases: Set[NodeKeyBase]             = predicate.keyBases ++ path.keyBases
  }

  case class AnswerEquals(answer: PrimitiveAnswer) extends QuestionnaireNodePredicate {
    def apply(ancestors: AncestorList): PredicateResult =
      ancestors.current.getAnswer.flatMap(a ⇒ PrimitiveAnswer.valueEquals(answer, a))
    override def keyBases: Set[NodeKeyBase] = Set.empty
  }

  case object AnswerExists extends QuestionnaireNodePredicate {
    def apply(ancestors: AncestorList): PredicateResult =
      ancestors.current.getAnswer.flatMap(_ ⇒ true.asRight[String])

    override def keyBases: Set[NodeKeyBase] = Set.empty
  }

  case object IsAlphabetic extends QuestionnaireNodePredicate {
    private def isAlphabetic(s: String) = s.forall(Character.isLetter)

    def apply(ancestors: AncestorList): PredicateResult =
      ancestors.current
        .getAnswerAs[PrimitiveAnswer.StringAnswer]
        .flatMap(_.answer.fold(true)(isAlphabetic).asRight)

    override def keyBases: Set[NodeKeyBase] = Set.empty
  }

  abstract class Quantification(predicate: QuestionnaireNodePredicate) extends QuestionnaireNodePredicateApply {

    def eval(l: List[Boolean]): Boolean

    def op(ancestors: AncestorList, value: Element.Parent): Boolean = {
      val r2 = value.toList.map(q ⇒ predicate(q :: ancestors).exists(identity))
      eval(r2)
    }

    def apply(ancestors: AncestorList): PredicateResult =
      ancestors.current.element match {
        case p: Parent       => Right(op(ancestors, p))
        case _: Primitive    => wrongElement(ancestors.current, "Element.Primitive", "Element.Parent")
        case _: Derived      => wrongElement(ancestors.current, "Element.Derived", "Element.Parent")
        case _: Info         => wrongElement(ancestors.current, "Element.Info", "Element.Parent")
        case _: Enumerations => wrongElement(ancestors.current, "Element.Enumerations", "Element.Parent")
      }
  }

  private def wrongElement(n: QuestionnaireNode, actual: String, expected: String): Either[String, Boolean] =
    Left(s"QuestionnaireNode ${n.key.show} is a $actual not $expected")

  case class Exists(predicate: QuestionnaireNodePredicate) extends Quantification(predicate) with QuestionnaireNodePredicate {
    def eval(l: List[Boolean]): Boolean     = l.exists(identity)
    override def keyBases: Set[NodeKeyBase] = predicate.keyBases
  }

  case class ExistsExactlyOne(predicate: QuestionnaireNodePredicate) extends Quantification(predicate) with QuestionnaireNodePredicate {
    def eval(l: List[Boolean]): Boolean     = l.count(identity) == 1
    override def keyBases: Set[NodeKeyBase] = predicate.keyBases
  }

  case class ForAll(predicate: QuestionnaireNodePredicate) extends Quantification(predicate) with QuestionnaireNodePredicate {
    def eval(l: List[Boolean]): Boolean     = l.forall(identity)
    override def keyBases: Set[NodeKeyBase] = predicate.keyBases
  }

  implicit class PredicatePathSyntax(path: Path) {
    def ===(v: Int): QuestionnaireNodePredicate    = NodeSelector(path, AnswerEquals(PrimitiveAnswer.IntAnswer(Some(v))))
    def ===(v: String): QuestionnaireNodePredicate = NodeSelector(path, AnswerEquals(PrimitiveAnswer.StringAnswer(Some(v))))

    def =!=(v: Int): QuestionnaireNodePredicate    = Not(path === v)
    def =!=(v: String): QuestionnaireNodePredicate = Not(path === v)

    def isAnswered: QuestionnaireNodePredicate   = NodeSelector(path, AnswerExists)
    def isAlphabetic: QuestionnaireNodePredicate = NodeSelector(path, IsAlphabetic)

    def Ǝ(pred: QuestionnaireNodePredicate): QuestionnaireNodePredicate  = NodeSelector(path, Exists(pred))
    def Ǝ1(pred: QuestionnaireNodePredicate): QuestionnaireNodePredicate = NodeSelector(path, ExistsExactlyOne(pred))
    def ∀(pred: QuestionnaireNodePredicate): QuestionnaireNodePredicate  = NodeSelector(path, ForAll(pred))
  }

  implicit class PredicateSyntax(p1: QuestionnaireNodePredicate) {
    def unary_!(): QuestionnaireNodePredicate                           = Not(p1)
    def &&(p2: QuestionnaireNodePredicate): QuestionnaireNodePredicate  = And(p1, p2)
    def ∧(p2: QuestionnaireNodePredicate): QuestionnaireNodePredicate   = And(p1, p2)
    def ||(p2: QuestionnaireNodePredicate): QuestionnaireNodePredicate  = Or(p1, p2)
    def ∨(p2: QuestionnaireNodePredicate): QuestionnaireNodePredicate   = Or(p1, p2)
    def xor(p2: QuestionnaireNodePredicate): QuestionnaireNodePredicate = (p1 && !p2) || (!p1 && p2)
    def ⊕(p2: QuestionnaireNodePredicate): QuestionnaireNodePredicate   = xor(p2)
    def ⊻(p2: QuestionnaireNodePredicate): QuestionnaireNodePredicate   = xor(p2)
  }

  implicit class QuestionnaireNodeSelfValidationSyntax(node: QuestionnaireNode) {
    def validateSelf(pf: Path ⇒ QuestionnaireNodePredicate, message: String): QuestionnaireNode =
      node.copy(validation = Validation.Simple(pf(Path.relative / node.key.base), message))
  }

  implicit val encoder: Encoder[QuestionnaireNodePredicate] = deriveCustomEncoder
  implicit val decoder: Decoder[QuestionnaireNodePredicate] = deriveCustomDecoder

  // scalastyle:off cyclomatic.complexity
  implicit val show: Show[QuestionnaireNodePredicate] = new Show[QuestionnaireNodePredicate] {
    override def show(t: QuestionnaireNodePredicate): String = t match {
      case False                         => "False"
      case True                          => "True"
      case Not(p)                        => s"!(${show(p)})"
      case And(left, right)              => s"(${show(left)}) && (${show(right)})"
      case Or(left, right)               => s"(${show(left)}) || (${show(right)})"
      case NodeSelector(path, predicate) => s"(${path.show}) ${show(predicate)}"
      case AnswerEquals(answer)          => s"=== $answer"
      case AnswerExists                  => "isAnswered"
      case IsAlphabetic                  => "isAlphabetic"
      case Exists(predicate)             => s"Ǝ(${show(predicate)})"
      case ExistsExactlyOne(predicate)   => s"Ǝ1(${show(predicate)})"
      case ForAll(predicate)             => s"∀(${show(predicate)})"
    }
  }
  // scalastyle:on cyclomatic.complexity
}
