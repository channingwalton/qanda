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
