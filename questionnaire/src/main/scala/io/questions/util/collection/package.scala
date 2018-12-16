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

import cats.Monoid

package object collection {
  implicit def SetMonoid[T]: Monoid[Set[T]] = new Monoid[Set[T]] {
    override def empty: Set[T] = Set.empty

    override def combine(x: Set[T], y: Set[T]): Set[T] =
      x ++ y
  }

  implicit def MapMonoid[K, V]: Monoid[Map[K, V]] = new Monoid[Map[K, V]] {
    override def empty: Map[K, V] = Map.empty

    override def combine(x: Map[K, V], y: Map[K, V]): Map[K, V] = x ++ y
  }
}
