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
