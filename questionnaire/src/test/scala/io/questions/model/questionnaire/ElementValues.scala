package io.questions.model.questionnaire

import io.questions.model.questionnaire.Element.{ Parent, Primitive }

trait ElementValues {
  implicit class ElementValuesSyntax(e: Element) {
    def parent: Element.Parent = e match {
      case p: Parent => p
      case x         => throw new IllegalArgumentException(s"Expected a Parent. Got $x")
    }

    def primitive: Element.Primitive = e match {
      case p: Primitive => p
      case x            => throw new IllegalArgumentException(s"Expected a Primitive. Got $x")
    }
  }
}
