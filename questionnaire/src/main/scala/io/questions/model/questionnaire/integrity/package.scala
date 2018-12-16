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

package io.questions.model.questionnaire

import cats.kernel.Order
import io.questions.model.questionnaire.nodekey.{ NodeKey, NodeKeyBase, NodeKeyExtension }

package object integrity {
  implicit val nodeKeyOrdering: Ordering[NodeKey] = Ordering.by[NodeKey, (NodeKeyBase, NodeKeyExtension)] { nk ⇒
    (nk.base, nk.extension)
  }
  implicit val nodeKeyOrder: Order[NodeKey] = Order.fromOrdering

  type IntegrityErrorSet = Set[IntegrityError]
}
