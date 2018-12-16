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
