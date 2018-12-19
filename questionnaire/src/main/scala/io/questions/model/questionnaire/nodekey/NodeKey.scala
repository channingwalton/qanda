package io.questions.model.questionnaire.nodekey

import cats.syntax.show._
import cats.{ Eq, Show }
import io.circe.{ Decoder, Encoder }
import io.questions.model.EncoderHelpers

final case class NodeKey(base: NodeKeyBase, extension: NodeKeyExtension)

object NodeKey extends EncoderHelpers {
  def random: NodeKey                                 = NodeKey(NodeKeyBase.random, NodeKeyExtension.random)
  def withRandomExtension(base: NodeKeyBase): NodeKey = NodeKey(base, NodeKeyExtension.random)
  def apply(base: String): NodeKey                    = NodeKey(NodeKeyBase.unsafe(base), NodeKeyExtension.random)

  implicit val encoder: Encoder[NodeKey] = deriveCustomEncoder
  implicit val decoder: Decoder[NodeKey] = deriveCustomDecoder

  implicit val show: Show[NodeKey] = (t: NodeKey) => s"${t.base.show}.${t.extension.show}"

  implicit val equals: Eq[NodeKey] = Eq.fromUniversalEquals[NodeKey]
}
