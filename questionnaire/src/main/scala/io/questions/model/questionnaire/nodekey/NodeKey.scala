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

package io.questions.model.questionnaire.nodekey

import cats.syntax.show._
import cats.{ Eq, Show }
import io.circe.{ Decoder, Encoder }
import io.questions.model.EncoderHelpers
import io.questions.util.TypedUUID

final case class NodeKey(base: NodeKeyBase, extension: NodeKeyExtension)

object NodeKey extends EncoderHelpers {
  def random: NodeKey                                 = NodeKey(NodeKeyBase.random, NodeKeyExtension.random)
  def withRandomExtension(base: NodeKeyBase): NodeKey = NodeKey(base, NodeKeyExtension.random)
  def apply(uuid: String): NodeKey                    = NodeKey(TypedUUID.unsafe(uuid), TypedUUID.unsafe(uuid))

  implicit val encoder: Encoder[NodeKey] = deriveCustomEncoder
  implicit val decoder: Decoder[NodeKey] = deriveCustomDecoder

  implicit val show: Show[NodeKey] = (t: NodeKey) => s"${t.base.show}.${t.extension.show}"

  implicit val equals: Eq[NodeKey] = Eq.fromUniversalEquals[NodeKey]
}
