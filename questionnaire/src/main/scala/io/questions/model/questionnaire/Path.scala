package io.questions.model.questionnaire

import cats.{ Eval, Show }
import cats.data.NonEmptyList
import cats.syntax.either._
import cats.syntax.eq._
import cats.syntax.show._
import shapeless.syntax.typeable._
import io.circe.{ Decoder, Encoder }
import io.questions.model.EncoderHelpers
import io.questions.model.questionnaire.Element.Parent
import io.questions.model.questionnaire.PathElement.{ Ancestor, Descendant, Relative, Root }
import io.questions.model.questionnaire.nodekey.{ NodeKey, NodeKeyBase }

sealed trait PathElement extends Product with Serializable {
  def apply(nodes: AncestorList): Either[String, AncestorList]
}

object PathElement extends EncoderHelpers {
  case object Root extends PathElement {
    def apply(nodes: AncestorList): Either[String, AncestorList] =
      nodes.rootList.asRight
  }

  case object Relative extends PathElement {
    def apply(nodes: AncestorList): Either[String, AncestorList] =
      nodes.asRight
  }

  case class Descendant(keyBase: NodeKeyBase) extends PathElement {
    def apply(nodes: AncestorList): Either[String, AncestorList] =
      findNonRepeatingDescendant(keyBase, nodes)
        .toRight(failedToFindDescendant(keyBase, nodes.current.key))
  }

  case class Ancestor(keyBase: NodeKeyBase) extends PathElement {
    def apply(nodes: AncestorList): Either[String, AncestorList] =
      nodes
        .find(keyBase)
        .toRight(failedToFindAncestor(keyBase, nodes.current.key))
  }

  def failedToFindAncestor(keyBase: NodeKeyBase, fromNodeKey: NodeKey): String =
    show"Failed to find an ancestor with NodeKeyBase $keyBase from node $fromNodeKey"

  def failedToFindDescendant(keyBase: NodeKeyBase, fromNodeKey: NodeKey): String =
    show"Failed to find a descendant with NodeKeyBase $keyBase from node $fromNodeKey"

  implicit val encoder: Encoder[PathElement] = deriveCustomEncoder
  implicit val decoder: Decoder[PathElement] = deriveCustomDecoder

  private def findNonRepeatingDescendant(keyBase: NodeKeyBase, extendedNodes: AncestorList): Option[AncestorList] =
    if (extendedNodes.current.keyBase === keyBase) Option(extendedNodes)
    else
      extendedNodes.current.element.cast[Parent].filterNot(_.repeating).flatMap { p ⇒
        p.toList.foldLeft(Option.empty[AncestorList]) { (result, child) ⇒
          result match {
            case Some(_) ⇒ result
            case None    ⇒ findNonRepeatingDescendant(keyBase, child :: extendedNodes)
          }
        }
      }

}

case class Path(elements: NonEmptyList[PathElement]) {
  def apply(ancestors: AncestorList): Either[String, AncestorList] =
    elements
      .foldRight(Eval.now(ancestors.asRight[String])) { (element: PathElement, acc: Eval[Either[String, AncestorList]]) ⇒
        acc.map(_.flatMap(element(_)))
      }
      .value

  def keyBases(): Set[NodeKeyBase] =
    elements
      .map {
        case Root                => None
        case Relative            => None
        case Descendant(keyBase) => Option(keyBase)
        case Ancestor(keyBase)   => Option(keyBase)
      }
      .toList
      .toSet
      .flatten
}

object Path extends EncoderHelpers {
  val root     = Path(NonEmptyList.of(PathElement.Root))
  val relative = Path(NonEmptyList.of(PathElement.Relative))

  implicit class PathSyntax(current: Path) {
    import PathElement._
    def /(descendant: NodeKeyBase): Path    = down(descendant)
    def down(descendant: NodeKeyBase): Path = Path(Descendant(descendant) :: current.elements)

    def \(ancestor: NodeKeyBase): Path  = up(ancestor)
    def up(ancestor: NodeKeyBase): Path = Path(Ancestor(ancestor) :: current.elements)
  }

  implicit val show: Show[Path] = new Show[Path] {
    import PathElement._

    override def show(t: Path): String =
      (t.elements.toList.map {
        case Root                ⇒ "root"
        case Relative            ⇒ "relative"
        case Descendant(keyBase) ⇒ show"/$keyBase"
        case Ancestor(keyBase)   ⇒ show"\\$keyBase"
      } reverse) mkString
  }

  implicit val encoder: Encoder[Path] = deriveCustomEncoder
  implicit val decoder: Decoder[Path] = deriveCustomDecoder

}
