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

package io.questions.util

import cats.syntax.either._
import java.util.UUID

import cats.kernel.Order
import cats.{ Eq, Show }
import io.circe._

final case class TypedUUID[+T](id: UUID) extends AnyVal {
  def as[U]: TypedUUID[U] = TypedUUID(id)
}

object TypedUUID {
  def unapply[T](s: String): Option[TypedUUID[T]]                               = Either.catchNonFatal(UUID.fromString(s)).toOption.map(TypedUUID[T])
  def unsafe[T](s: String): TypedUUID[T]                                        = TypedUUID[T](UUID.fromString(s))
  def fromString[T](s: String, failure: ⇒ String): Either[String, TypedUUID[T]] = unapply(s).toRight(failure)

  implicit def encoder[T]: Encoder[TypedUUID[T]] = (a: TypedUUID[T]) => Json.fromString(a.id.toString)
  implicit def decoder[T]: Decoder[TypedUUID[T]] =
    (c: HCursor) => c.as[String].flatMap(s ⇒ unapply(s).toRight(DecodingFailure(s"Could not parse '$s' as a TypedUUID", Nil)))

  implicit def show[T]: Show[TypedUUID[T]] = (t: TypedUUID[T]) => t.id.toString

  implicit def equals[T]: Eq[TypedUUID[T]] = Eq.fromUniversalEquals[TypedUUID[T]]

  def random[T](): TypedUUID[T] = TypedUUID(UUID.randomUUID)

  implicit def ordering[T]: Ordering[TypedUUID[T]] = Ordering.by[TypedUUID[T], String] { _.id.toString }

  implicit def order[T]: Order[TypedUUID[T]] = Order.fromOrdering

}
