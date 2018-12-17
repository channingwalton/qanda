package io.questions.util.specs

import io.questions._
import shapeless.{ ops, Coproduct, HList, LabelledGeneric }

trait ShapelessUtils {
  class NameHelper[A] {
    def apply[C <: Coproduct, K <: HList]()(implicit
                                            gen: LabelledGeneric.Aux[A, C],
                                            keys: ops.union.Keys.Aux[C, K],
                                            toSet: ops.hlist.ToTraversable.Aux[K, Set, Symbol]): Set[String] = {
      // we need the implicit param 'gen' for Shapeless machinery but compiler says it's not used, so we have to do some small trick that hopefully JIT will optimise
      discard { gen }
      toSet(keys()).map(_.name)
    }
  }

  def adtMembers[A]: NameHelper[A] = new NameHelper[A]
}
