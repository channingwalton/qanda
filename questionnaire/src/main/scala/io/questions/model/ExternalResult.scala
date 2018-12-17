package io.questions.model

import cats.kernel.Eq
import io.circe.{ Decoder, Encoder }

// results contains all rows to display. Each row has pairs of values we use in UI to map answers to the questionnaire
sealed trait ExternalResult
final case class ResultList(results: List[FieldResultList]) extends ExternalResult
final case class RequestError(message: String)              extends ExternalResult

final case class FieldResultList(id: String, fieldResults: List[FieldResult])
final case class FieldResult(key: String, value: String)

object ExternalResult extends EncoderHelpers {

  implicit val encoder: Encoder[ExternalResult] = deriveCustomEncoder
  implicit val decoder: Decoder[ExternalResult] = deriveCustomDecoder

  implicit val equals: Eq[ExternalResult] = Eq.fromUniversalEquals[ExternalResult]

  implicit val encoderFieldResult: Encoder[FieldResult] = deriveCustomEncoder
  implicit val decoderFieldResult: Decoder[FieldResult] = deriveCustomDecoder

  implicit val equalsFieldResult: Eq[FieldResult] = Eq.fromUniversalEquals[FieldResult]

  implicit val encoderFieldResultList: Encoder[FieldResultList] = deriveCustomEncoder
  implicit val decoderFieldResultList: Decoder[FieldResultList] = deriveCustomDecoder

  implicit val equalsFieldResultList: Eq[FieldResultList] = Eq.fromUniversalEquals[FieldResultList]
}
