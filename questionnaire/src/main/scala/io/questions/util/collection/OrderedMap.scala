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

package io.questions.util.collection

import cats.instances.either._
import cats.instances.list._
import cats.syntax.traverse._
import io.circe.Decoder.Result
import io.circe._

sealed trait OrderedMap[K, +V] {
  def +[V1 >: V](kv: (K, V1)): OrderedMap[K, V1]

  def get(key: K): Option[V]

  def apply(key: K): V

  def keys: Seq[K]
}

object OrderedMap {
  private case class OrderedMap0[K, +V](keys: List[K], map: Map[K, V]) extends OrderedMap[K, V] {
    override def +[V1 >: V](kv: (K, V1)): OrderedMap[K, V1] =
      OrderedMap0[K, V1]((kv._1 :: keys.reverse).reverse, map + kv)

    override def get(key: K): Option[V] = map.get(key)

    override def apply(key: K): V = map(key)
  }

  def empty[K, V]: OrderedMap[K, V] = OrderedMap0[K, V](List.empty[K], Map.empty[K, V])

  def apply[K, V](kvs: (K, V)*): OrderedMap[K, V] = kvs.foldLeft(empty[K, V])((acc, kv) ⇒ acc + kv)

  implicit def encoder[V](implicit valueEncoder: Encoder[V]): Encoder[OrderedMap[String, V]] =
    (m: OrderedMap[String, V]) => Json.fromFields(m.keys.map(key ⇒ key → valueEncoder(m(key))))

  implicit def decoder[V](implicit valueDecoder: Decoder[V]): Decoder[OrderedMap[String, V]] = (c: HCursor) => {
    val foo: List[Result[(String, V)]] = c.keys.getOrElse(Seq.empty).map(field ⇒ c.downField(field).as[V].map(v ⇒ field → v)).toList

    foo
      .sequence[Result, (String, V)]
      .map { l ⇒
        OrderedMap(l: _*)
      }
  }
}
