package io.questions.model.questionnaire

import io.questions.util.TypedUUID

package object nodekey {

  type NodeKeyExtension = TypedUUID[NodeKeyExtension.type]

  object NodeKeyExtension {
    def random: NodeKeyExtension               = TypedUUID.random[NodeKeyExtension.type]()
    def unsafe(uuid: String): NodeKeyExtension = TypedUUID.unsafe(uuid)
  }
}
