package io.questions
import io.questions.model.questionnaire.ElementValues
import io.questions.util.specs.{ Generators, QuestionnaireUtils, ShapelessUtils }
import org.scalatest._
import org.scalatest.prop.PropertyChecks

trait QuestionsSpec
    extends FreeSpec
    with MustMatchers
    with PropertyChecks
    with BeforeAndAfter
    with OptionValues
    with EitherValues
    with ElementValues
    with QuestionnaireUtils
    with Generators
    with ShapelessUtils
