package io.questions.model.questionnaire

import io.questions.util.TypedUUID

package object nodekey {
  type NodeKeyBase = TypedUUID[NodeKeyBase.type]

  object NodeKeyBase {
    def random: NodeKeyBase               = TypedUUID.random[NodeKeyBase.type]()
    def unsafe(uuid: String): NodeKeyBase = TypedUUID.unsafe(uuid)
  }

  type NodeKeyExtension = TypedUUID[NodeKeyExtension.type]

  object NodeKeyExtension {
    def random: NodeKeyExtension               = TypedUUID.random[NodeKeyExtension.type]()
    def unsafe(uuid: String): NodeKeyExtension = TypedUUID.unsafe(uuid)
  }
}
