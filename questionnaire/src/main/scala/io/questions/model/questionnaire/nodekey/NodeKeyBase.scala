package io.questions.model.questionnaire.nodekey

import cats.kernel.Order
import cats.{ Eq, Show }
import java.util.UUID
import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection._
import io.circe._

final case class NodeKeyBase(nodeKeyBase: String Refined NonEmpty) {
  val value = nodeKeyBase.value
}

object NodeKeyBase {
  def random: NodeKeyBase = unsafe(UUID.randomUUID().toString)
  def unsafe(uuid: String): NodeKeyBase =
    unapply(uuid).getOrElse(throw new RuntimeException("NodeKeyBase value cannot be empty"))

  def unapply(uuid: String): Option[NodeKeyBase] =
    refineV[NonEmpty](uuid).toOption.map(NodeKeyBase(_))

  implicit def encoder[T]: Encoder[NodeKeyBase] = (a: NodeKeyBase) => Json.fromString(a.value)
  implicit def decoder[T]: Decoder[NodeKeyBase] =
    (c: HCursor) => c.as[String].flatMap(s ⇒ NodeKeyBase.unapply(s).toRight(DecodingFailure(s"Could not parse '$s' as a TypedUUID", Nil)))

  implicit val nodeKeyBaseEq: Eq[NodeKeyBase] = Eq.fromUniversalEquals[NodeKeyBase]

  implicit val nodeKeyBaseShow: Show[NodeKeyBase] =
    (nkb: NodeKeyBase) => nkb.value

  implicit def ordering[T]: Ordering[NodeKeyBase] = Ordering.by[NodeKeyBase, String] { _.value }

  implicit val order: Order[NodeKeyBase] = Order.fromOrdering
}
