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
