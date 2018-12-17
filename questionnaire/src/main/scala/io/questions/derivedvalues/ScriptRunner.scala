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

    discard(engine.eval(s"""|var scriptRunner = function(functionName, answersJson, currentNodeKey, currentNodeKeyExtension) {
                            |  var answers = JSON.parse(answersJson);
                            |  return eval(functionName)(answers, currentNodeKey, currentNodeKeyExtension);
                            |};
                            |
                            |var DerivedValuesResult = Java.type('io.questions.derivedvalues.DerivedValuesResult');
                            |
                            |var insufficientData = function(msg) {
                            |  return DerivedValuesResult.insufficientData(msg);
                            |};
                            |
                            |var intResult = function(value) {
                            |  return DerivedValuesResult.intResult(value);
                            |};
                            |
                            |var stringResult = function(value) {
                            |  return DerivedValuesResult.stringResult(value);
                            |};""".stripMargin))

    discard(engine.eval(script))
    engine
      .asInstanceOf[Invocable]
      .invokeFunction("scriptRunner", functionName, JsonEncoder(questionnaire).spaces2, currentNode.base.show, currentNode.extension.show)
      .asInstanceOf[DerivedValuesResult]
  }
}
