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

package io.questions.testdata.samples.enumerations

import cats.data.NonEmptyList
import io.questions.model.questionnaire.{ EnumerationName, EnumerationValue }

object Currencies {
  val name = EnumerationName("Currencies")

  val AED = Currency("AED", "United Arab Emirates Dirham")
  val AFN = Currency("AFN", "Afghanistan Afghani")
  val ALL = Currency("ALL", "Albania Lek")
  val AMD = Currency("AMD", "Armenia Dram")
  val ANG = Currency("ANG", "Netherlands Antilles Guilder")
  val AOA = Currency("AOA", "Angola Kwanza")
  val ARS = Currency("ARS", "Argentina Peso")
  val AUD = Currency("AUD", "Australia Dollar")
  val AWG = Currency("AWG", "Aruba Guilder")
  val AZN = Currency("AZN", "Azerbaijan New Manat")
  val BAM = Currency("BAM", "Bosnia and Herzegovina Convertible Marka")
  val BBD = Currency("BBD", "Barbados Dollar")
  val BDT = Currency("BDT", "Bangladesh Taka")
  val BGN = Currency("BGN", "Bulgaria Lev")
  val BHD = Currency("BHD", "Bahrain Dinar")
  val BIF = Currency("BIF", "Burundi Franc")
  val BMD = Currency("BMD", "Bermuda Dollar")
  val BND = Currency("BND", "Brunei Darussalam Dollar")
  val BOB = Currency("BOB", "Bolivia Boliviano")
  val BRL = Currency("BRL", "Brazil Real")
  val BSD = Currency("BSD", "Bahamas Dollar")
  val BTN = Currency("BTN", "Bhutan Ngultrum")
  val BWP = Currency("BWP", "Botswana Pula")
  val BYR = Currency("BYR", "Belarus Ruble")
  val BZD = Currency("BZD", "Belize Dollar")
  val CAD = Currency("CAD", "Canada Dollar")
  val CDF = Currency("CDF", "Congo/Kinshasa Franc")
  val CHF = Currency("CHF", "Switzerland Franc")
  val CLP = Currency("CLP", "Chile Peso")
  val CNY = Currency("CNY", "China Yuan Renminbi")
  val COP = Currency("COP", "Colombia Peso")
  val CRC = Currency("CRC", "Costa Rica Colon")
  val CUC = Currency("CUC", "Cuba Convertible Peso")
  val CUP = Currency("CUP", "Cuba Peso")
  val CVE = Currency("CVE", "Cape Verde Escudo")
  val CZK = Currency("CZK", "Czech Republic Koruna")
  val DJF = Currency("DJF", "Djibouti Franc")
  val DKK = Currency("DKK", "Denmark Krone")
  val DOP = Currency("DOP", "Dominican Republic Peso")
  val DZD = Currency("DZD", "Algeria Dinar")
  val EGP = Currency("EGP", "Egypt Pound")
  val ERN = Currency("ERN", "Eritrea Nakfa")
  val ETB = Currency("ETB", "Ethiopia Birr")
  val EUR = Currency("EUR", "Euro Member Countries")
  val FJD = Currency("FJD", "Fiji Dollar")
  val FKP = Currency("FKP", "Falkland Islands Currency(Malvinas) Pound")
  val GBP = Currency("GBP", "United Kingdom Pound")
  val GEL = Currency("GEL", "Georgia Lari")
  val GGP = Currency("GGP", "Guernsey Pound")
  val GHS = Currency("GHS", "Ghana Cedi")
  val GIP = Currency("GIP", "Gibraltar Pound")
  val GMD = Currency("GMD", "Gambia Dalasi")
  val GNF = Currency("GNF", "Guinea Franc")
  val GTQ = Currency("GTQ", "Guatemala Quetzal")
  val GYD = Currency("GYD", "Guyana Dollar")
  val HKD = Currency("HKD", "Hong Kong Dollar")
  val HNL = Currency("HNL", "Honduras Lempira")
  val HRK = Currency("HRK", "Croatia Kuna")
  val HTG = Currency("HTG", "Haiti Gourde")
  val HUF = Currency("HUF", "Hungary Forint")
  val IDR = Currency("IDR", "Indonesia Rupiah")
  val ILS = Currency("ILS", "Israel Shekel")
  val IMP = Currency("IMP", "Isle of Man Pound")
  val INR = Currency("INR", "India Rupee")
  val IQD = Currency("IQD", "Iraq Dinar")
  val IRR = Currency("IRR", "Iran Rial")
  val ISK = Currency("ISK", "Iceland Krona")
  val JEP = Currency("JEP", "Jersey Pound")
  val JMD = Currency("JMD", "Jamaica Dollar")
  val JOD = Currency("JOD", "Jordan Dinar")
  val JPY = Currency("JPY", "Japan Yen")
  val KES = Currency("KES", "Kenya Shilling")
  val KGS = Currency("KGS", "Kyrgyzstan Som")
  val KHR = Currency("KHR", "Cambodia Riel")
  val KMF = Currency("KMF", "Comoros Franc")
  val KPW = Currency("KPW", "Korea Currency(North) Won")
  val KRW = Currency("KRW", "Korea Currency(South) Won")
  val KWD = Currency("KWD", "Kuwait Dinar")
  val KYD = Currency("KYD", "Cayman Islands Dollar")
  val KZT = Currency("KZT", "Kazakhstan Tenge")
  val LAK = Currency("LAK", "Laos Kip")
  val LBP = Currency("LBP", "Lebanon Pound")
  val LKR = Currency("LKR", "Sri Lanka Rupee")
  val LRD = Currency("LRD", "Liberia Dollar")
  val LSL = Currency("LSL", "Lesotho Loti")
  val LTL = Currency("LTL", "Lithuania Litas")
  val LVL = Currency("LVL", "Latvia Lat")
  val LYD = Currency("LYD", "Libya Dinar")
  val MAD = Currency("MAD", "Morocco Dirham")
  val MDL = Currency("MDL", "Moldova Leu")
  val MGA = Currency("MGA", "Madagascar Ariary")
  val MKD = Currency("MKD", "Macedonia Denar")
  val MMK = Currency("MMK", "Myanmar Currency(Burma) Kyat")
  val MNT = Currency("MNT", "Mongolia Tughrik")
  val MOP = Currency("MOP", "Macau Pataca")
  val MRO = Currency("MRO", "Mauritania Ouguiya")
  val MUR = Currency("MUR", "Mauritius Rupee")
  val MVR = Currency("MVR", "Maldives Currency(Maldive Islands) Rufiyaa")
  val MWK = Currency("MWK", "Malawi Kwacha")
  val MXN = Currency("MXN", "Mexico Peso")
  val MYR = Currency("MYR", "Malaysia Ringgit")
  val MZN = Currency("MZN", "Mozambique Metical")
  val NAD = Currency("NAD", "Namibia Dollar")
  val NGN = Currency("NGN", "Nigeria Naira")
  val NIO = Currency("NIO", "Nicaragua Cordoba")
  val NOK = Currency("NOK", "Norway Krone")
  val NPR = Currency("NPR", "Nepal Rupee")
  val NZD = Currency("NZD", "New Zealand Dollar")
  val OMR = Currency("OMR", "Oman Rial")
  val PAB = Currency("PAB", "Panama Balboa")
  val PEN = Currency("PEN", "Peru Nuevo Sol")
  val PGK = Currency("PGK", "Papua New Guinea Kina")
  val PHP = Currency("PHP", "Philippines Peso")
  val PKR = Currency("PKR", "Pakistan Rupee")
  val PLN = Currency("PLN", "Poland Zloty")
  val PYG = Currency("PYG", "Paraguay Guarani")
  val QAR = Currency("QAR", "Qatar Riyal")
  val RON = Currency("RON", "Romania New Leu")
  val RSD = Currency("RSD", "Serbia Dinar")
  val RUB = Currency("RUB", "Russia Ruble")
  val RWF = Currency("RWF", "Rwanda Franc")
  val SAR = Currency("SAR", "Saudi Arabia Riyal")
  val SBD = Currency("SBD", "Solomon Islands Dollar")
  val SCR = Currency("SCR", "Seychelles Rupee")
  val SDG = Currency("SDG", "Sudan Pound")
  val SEK = Currency("SEK", "Sweden Krona")
  val SGD = Currency("SGD", "Singapore Dollar")
  val SHP = Currency("SHP", "Saint Helena Pound")
  val SLL = Currency("SLL", "Sierra Leone Leone")
  val SOS = Currency("SOS", "Somalia Shilling")
  val SRD = Currency("SRD", "Suriname Dollar")
  val SSP = Currency("SSP", "South Sudanese pound")
  val STD = Currency("STD", "São Tomé and Príncipe Dobra")
  val SVC = Currency("SVC", "El Salvador Colon")
  val SYP = Currency("SYP", "Syria Pound")
  val SZL = Currency("SZL", "Swaziland Lilangeni")
  val THB = Currency("THB", "Thailand Baht")
  val TJS = Currency("TJS", "Tajikistan Somoni")
  val TMT = Currency("TMT", "Turkmenistan Manat")
  val TND = Currency("TND", "Tunisia Dinar")
  val TOP = Currency("TOP", "Tonga Pa'anga")
  val TRY = Currency("TRY", "Turkey Lira")
  val TTD = Currency("TTD", "Trinidad and Tobago Dollar")
  val TVD = Currency("TVD", "Tuvalu Dollar")
  val TWD = Currency("TWD", "Taiwan New Dollar")
  val TZS = Currency("TZS", "Tanzania Shilling")
  val UAH = Currency("UAH", "Ukraine Hryvna")
  val UGX = Currency("UGX", "Uganda Shilling")
  val USD = Currency("USD", "United States Dollar")
  val UYU = Currency("UYU", "Uruguay Peso")
  val UZS = Currency("UZS", "Uzbekistan Som")
  val VEF = Currency("VEF", "Venezuela Bolivar")
  val VND = Currency("VND", "Viet Nam Dong")
  val VUV = Currency("VUV", "Vanuatu Vatu")
  val WST = Currency("WST", "Samoa Tala")
  val XAF = Currency("XAF", "Communauté Financière Africaine Currency(BEAC) CFA Franc BEAC")
  val XCD = Currency("XCD", "East Caribbean Dollar")
  val XOF = Currency("XOF", "Communauté Financière Africaine Currency(BCEAO) Franc")
  val XPF = Currency("XPF", "Comptoirs Français du Pacifique Currency(CFP) Franc")
  val YER = Currency("YER", "Yemen Rial")
  val ZAR = Currency("ZAR", "South Africa Rand")
  val ZMW = Currency("ZMW", "Zambia Kwacha")
  val ZWD = Currency("ZWD", "Zimbabwe Dollar")

  val allCurrencies: NonEmptyList[Currency] = NonEmptyList.of(
    AED,
    AFN,
    ALL,
    AMD,
    ANG,
    AOA,
    ARS,
    AUD,
    AWG,
    AZN,
    BAM,
    BBD,
    BDT,
    BGN,
    BHD,
    BIF,
    BMD,
    BND,
    BOB,
    BRL,
    BSD,
    BTN,
    BWP,
    BYR,
    BZD,
    CAD,
    CDF,
    CHF,
    CLP,
    CNY,
    COP,
    CRC,
    CUC,
    CUP,
    CVE,
    CZK,
    DJF,
    DKK,
    DOP,
    DZD,
    EGP,
    ERN,
    ETB,
    EUR,
    FJD,
    FKP,
    GBP,
    GEL,
    GGP,
    GHS,
    GIP,
    GMD,
    GNF,
    GTQ,
    GYD,
    HKD,
    HNL,
    HRK,
    HTG,
    HUF,
    IDR,
    ILS,
    IMP,
    INR,
    IQD,
    IRR,
    ISK,
    JEP,
    JMD,
    JOD,
    JPY,
    KES,
    KGS,
    KHR,
    KMF,
    KPW,
    KRW,
    KWD,
    KYD,
    KZT,
    LAK,
    LBP,
    LKR,
    LRD,
    LSL,
    LTL,
    LVL,
    LYD,
    MAD,
    MDL,
    MGA,
    MKD,
    MMK,
    MNT,
    MOP,
    MRO,
    MUR,
    MVR,
    MWK,
    MXN,
    MYR,
    MZN,
    NAD,
    NGN,
    NIO,
    NOK,
    NPR,
    NZD,
    OMR,
    PAB,
    PEN,
    PGK,
    PHP,
    PKR,
    PLN,
    PYG,
    QAR,
    RON,
    RSD,
    RUB,
    RWF,
    SAR,
    SBD,
    SCR,
    SDG,
    SEK,
    SGD,
    SHP,
    SLL,
    SOS,
    SRD,
    SSP,
    STD,
    SVC,
    SYP,
    SZL,
    THB,
    TJS,
    TMT,
    TND,
    TOP,
    TRY,
    TTD,
    TVD,
    TWD,
    TZS,
    UAH,
    UGX,
    USD,
    UYU,
    UZS,
    VEF,
    VND,
    VUV,
    WST,
    XAF,
    XCD,
    XOF,
    XPF,
    YER,
    ZAR,
    ZMW,
    ZWD
  )

  val values: NonEmptyList[EnumerationValue] = allCurrencies.map(c ⇒ EnumerationValue(c.isoCode, c.name))

  def byIsoCode(isoCode: String): Option[Currency] = allCurrencies.find(_.isoCode == isoCode)
}
