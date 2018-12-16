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

package io.questions.derivedvalues

import cats.syntax.show._
import javax.script.Invocable
import javax.script.ScriptEngineManager

import io.questions.model.questionnaire.QuestionnaireNode
import io.questions.model.questionnaire.nodekey.NodeKey

import io.questions.discard

object ScriptRunner {

  @SuppressWarnings(Array("org.wartremover.warts.AsInstanceOf"))
  def apply(script: String, functionName: String, questionnaire: QuestionnaireNode, currentNode: NodeKey): DerivedValuesResult = {
    val engine = new ScriptEngineManager().getEngineByName("nashorn")
    discard(engine.eval(script))
    engine
      .asInstanceOf[Invocable]
      .invokeFunction("scriptRunner", functionName, JsonEncoder(questionnaire).spaces2, currentNode.base.show, currentNode.extension.show)
      .asInstanceOf[DerivedValuesResult]
  }
}
