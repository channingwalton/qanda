package io.questions.model.questionnaire

import java.io.{ PrintWriter, StringWriter }

import cats.{ Eq, Monoid, Show }
import cats.data.NonEmptyList
import cats.instances.int._
import cats.syntax.eq._
import cats.syntax.monoid._
import cats.syntax.show._
import io.circe.{ Decoder, Encoder }
import io.questions.model.EncoderHelpers
import io.questions.model.questionnaire.Element.{ Derived, Enumerations, Info, Parent, Primitive }
import io.questions.model.questionnaire.integrity.{ Integrity, IntegrityError }
import io.questions.model.questionnaire.nodekey.{ NodeKey, NodeKeyBase, NodeKeyExtension }
import io.questions.util.collection.SetMonoid
import shapeless.Typeable
import shapeless.syntax.typeable._

case class QuestionnaireNode(key: NodeKey,
                             name: FieldName,
                             text: QuestionText,
                             element: Element,
                             optional: QuestionnaireNodePredicate = QuestionnaireNodePredicate.False,
                             validation: Validation = Validation.AlwaysValid,
                             visibility: QuestionnaireNodePredicate = QuestionnaireNodePredicate.True,
                             editability: QuestionnaireNodePredicate = QuestionnaireNodePredicate.True,
                             metadata: Seq[NodeMetadata] = Seq.empty) {

  def keyBase: NodeKeyBase = key.base

  def reduce[T](f: QuestionnaireNode ⇒ T)(implicit m: Monoid[T]): T =
    foldLeft(m.empty)(_ |+| f(_))

  def foldLeft[T](z: T)(f: (T, QuestionnaireNode) ⇒ T): T = {
    val x = f(z, this)
    element match {
      case p: Parent ⇒ p.children.foldLeft(x)((t, qn) ⇒ qn.foldLeft(t)(f))
      case _         ⇒ x
    }
  }

  def collectKeys: Set[NodeKey] =
    reduce(q ⇒ Set(q.key))

  def collectAnswered: Set[(NodeKey, PrimitiveAnswer)] =
    reduce(q ⇒ q.getAnswer.fold(_ ⇒ Set.empty, ans ⇒ if (ans.isAnswered) Set((q.key, ans)) else Set.empty))

  def collect(meta: NodeMetadata): Set[QuestionnaireNode] = reduce(q ⇒ if (q.metadata.contains(meta)) Set(q) else Set.empty)

  def appendChild(repeatingParentKey: NodeKey, extension: NodeKeyExtension): Either[String, QuestionnaireNode] =
    QuestionnaireNodeChildAppender(this, repeatingParentKey, extension)

  def removeChild(repeatingParentKey: NodeKey, childNodeKey: NodeKey): Either[String, QuestionnaireNode] =
    QuestionnaireNodeChildRemover(this, repeatingParentKey, childNodeKey)

  def updateMetadata(questionKey: NodeKey, newMetadata: Seq[NodeMetadata]): Either[String, QuestionnaireNode] =
    QuestionnaireNodeMetadataUpdater(this, questionKey, newMetadata)

  def getRepeatingChildNodes: Either[String, NonEmptyList[QuestionnaireNode]] = element match {
    case p: Parent if p.repeating ⇒ Right(p.children)
    case _                        ⇒ Left(s"QuestionnaireNode.getRepeatingChildNodes: ${key.show} is a non-repeating Element.Parent")
  }

  def blank: QuestionnaireNode = copy(element = element.blank)

  def getAnswer(aKey: NodeKey): Either[String, PrimitiveAnswer] = find(aKey).flatMap(_.getAnswer)

  def getAnswerAs[U <: PrimitiveAnswer](aKey: NodeKey)(implicit castU: Typeable[U]): Either[String, U] =
    find(aKey).flatMap(_.getAnswer).flatMap(_.cast[U].toRight(s"Unable to cast answer for ${aKey.show}"))

  def modifyAnswer(aKey: NodeKey, f: PrimitiveAnswer ⇒ PrimitiveAnswer): Either[String, QuestionnaireNode] =
    getAnswer(aKey).map(f).flatMap(answer(aKey, _))

  def modifyAnswerAs[U <: PrimitiveAnswer](aKey: NodeKey, f: U ⇒ U)(implicit castU: Typeable[U]): Either[String, QuestionnaireNode] =
    getAnswerAs[U](aKey).map(f).flatMap(answer(aKey, _))

  private val notAPrimitiveAnswer = Left(s"The node for ${key.show} does not contain a primitive answer")

  def getAnswer: Either[String, PrimitiveAnswer] =
    element match {
      case p: Primitive    ⇒ Right(p.answer)
      case d: Derived      ⇒ Right(d.answer)
      case _: Parent       ⇒ notAPrimitiveAnswer
      case _: Info         ⇒ notAPrimitiveAnswer
      case _: Enumerations ⇒ notAPrimitiveAnswer
    }

  def getAnswerAs[U <: PrimitiveAnswer](implicit castU: Typeable[U]): Either[String, U] =
    getAnswer.flatMap(_.cast[U].toRight(s"Unable to cast answer for ${key.show}"))

  def find(keyBase: NodeKeyBase): Set[QuestionnaireNode] =
    foldLeft(Set.empty[QuestionnaireNode]) { (acc, n) ⇒
      if (n.key.base === keyBase) acc + n else acc
    }

  def findOne(keyBase: NodeKeyBase): Either[String, QuestionnaireNode] = {
    val found = find(keyBase)
    if (found.size > 1) Left(show"Attempt to find a node with keyBase $keyBase at or below $key found ${found.size} nodes.")
    else
      found.headOption.map(Right(_)) getOrElse Left(
        show"Attempt to find a node with keyBase $keyBase at or below $key didn't find any nodes."
      )
  }

  private def unknownKey(aKey: NodeKey): Either[String, Nothing] = {
    val w  = new StringWriter()
    val pw = new PrintWriter(w)
    new Exception().printStackTrace(pw)
    pw.close()
    Left(s"Key ${aKey.show} is unknown: $w")
  }

  def find(aKey: NodeKey): Either[String, QuestionnaireNode] = {
    val found = foldLeft(Set.empty[QuestionnaireNode]) { (acc, n) ⇒
      if (n.key === aKey) acc + n else acc
    }

    if (found.size > 1) Left(show"Attempt to find a node with key $aKey at or below $key found ${found.size} nodes.")
    else found.headOption.map(Right(_)) getOrElse unknownKey(aKey)
  }

  def answer(aKey: NodeKey, value: PrimitiveAnswer): Either[String, QuestionnaireNode] =
    QuestionnaireNodeAnswerUpdater(this, aKey, value)

  def derive: QuestionnaireNode = QuestionnaireAnswersDeriver(this)

  def checkIntegrity: Set[IntegrityError] = Integrity.check(this)
}

object QuestionnaireNode extends EncoderHelpers {

  implicit val encoder: Encoder[QuestionnaireNode] = deriveCustomEncoder
  implicit val decoder: Decoder[QuestionnaireNode] = deriveCustomDecoder

  implicit val equals: Eq[QuestionnaireNode] = Eq.fromUniversalEquals[QuestionnaireNode]

  implicit val show: Show[QuestionnaireNode] =
    (t: QuestionnaireNode) => QuestionnaireNode.show(t)

  def show(questionnaire: QuestionnaireNode): String = {
    def toString(q: QuestionnaireNode, d: String): String =
      q.element match {
        case p: Parent       ⇒ "\n" + p.toList.map(q ⇒ s(q, d + " ")).mkString("\n")
        case p: Primitive    ⇒ s" (${q.key.show}) ${p.answer}"
        case d: Derived      ⇒ s" (${q.key.show}) ${d.answer} [derived]"
        case a: Info         ⇒ s" (${q.key.show}) ${a.text} [info]"
        case _: Enumerations ⇒ s" (${q.key.show}) [enumerations]"
      }

    def s(q: QuestionnaireNode, d: String): String =
      d + q.text.value + s" (${q.key.show}) (optional:${q.optional.show})" + toString(q, d)

    s(questionnaire, "")
  }
}
