package io.questions.model.questionnaire

import io.circe.{ Decoder, Encoder }
import io.questions.model.EncoderHelpers

sealed trait NodeMetadata extends Product with Serializable

object NodeMetadata extends EncoderHelpers {

  sealed trait LayoutMetadata extends NodeMetadata
  case object SectionTag      extends LayoutMetadata
  case object PageTag         extends LayoutMetadata

  sealed trait UIRenderMetadata        extends NodeMetadata
  case object MoneyQuestion            extends UIRenderMetadata
  case object DateQuestion             extends UIRenderMetadata
  case object DateTimeQuestion         extends UIRenderMetadata
  case object DateTimeWithZoneQuestion extends UIRenderMetadata
  case object TimeQuestion             extends UIRenderMetadata
  case object TimeWithZoneQuestion     extends UIRenderMetadata

  implicit val encoder: Encoder[NodeMetadata] = deriveCustomEncoder
  implicit val decoder: Decoder[NodeMetadata] = deriveCustomDecoder
}
