package io.questions.model.questionnaire

import cats.data.NonEmptyList
import cats.syntax.eq._
import io.questions.model.questionnaire.nodekey.NodeKeyBase

sealed trait AncestorList {
  def current: QuestionnaireNode
  def root: QuestionnaireNode
  def rootList: AncestorList
  def ::(node: QuestionnaireNode): AncestorList
  def find(base: NodeKeyBase): Option[AncestorList]
}

object AncestorList {
  case class AncestorList0(root: QuestionnaireNode, ancestors: NonEmptyList[QuestionnaireNode]) extends AncestorList {
    def current: QuestionnaireNode                = ancestors.head
    def rootList: AncestorList                    = apply(root)
    def ::(node: QuestionnaireNode): AncestorList = copy(ancestors = node :: ancestors)
    def find(base: NodeKeyBase): Option[AncestorList] =
      NonEmptyList.fromList(ancestors.toList.dropWhile(_.keyBase =!= base)).map(AncestorList0(root, _))
  }

  def apply(root: QuestionnaireNode): AncestorList = AncestorList0(root, NonEmptyList.of(root))
}
