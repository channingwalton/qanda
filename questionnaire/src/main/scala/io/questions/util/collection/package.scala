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
