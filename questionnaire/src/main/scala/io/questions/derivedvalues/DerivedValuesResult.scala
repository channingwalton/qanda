package io.questions.derivedvalues

import io.questions.model.questionnaire.PrimitiveAnswer

trait DerivedValuesResult

object DerivedValuesResult {
  case class InsufficientData(msg: String)   extends DerivedValuesResult
  case class Result(result: PrimitiveAnswer) extends DerivedValuesResult

  def insufficientData(msg: String): DerivedValuesResult = InsufficientData(msg)
  def intResult(i: Int): DerivedValuesResult             = Result(PrimitiveAnswer.IntAnswer(Some(i)))
  def stringResult(s: String): DerivedValuesResult       = Result(PrimitiveAnswer.StringAnswer(Some(s)))
}
