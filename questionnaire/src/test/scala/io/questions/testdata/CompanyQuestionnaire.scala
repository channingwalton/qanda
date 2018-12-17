package io.questions.testdata

import io.questions.model.questionnaire.FieldName.FieldNameStringSyntax
import io.questions.model.questionnaire.QuestionText.QuestionTextSyntax
import io.questions.model.questionnaire.nodekey.NodeKey
import io.questions.model.questionnaire._
import io.questions.testdata.samples.samplequestionnaire.QuestionTypes._
import io.questions.testdata.samples.enumerations._

object CompanyQuestionnaire {
  val id: QuestionnaireId = QuestionnaireId.random

  val codeId: QuestionnaireNode =
    stringQuestion("codeId".fieldName, "Id".text)

  val codeDescription: QuestionnaireNode =
    stringQuestion("codeDescription".fieldName, "Description".text)

  val codeSchemeName: QuestionnaireNode =
    stringQuestion("codeSchemeName".fieldName, "Scheme Name".text)

  val codeSchemeId: QuestionnaireNode =
    stringQuestion("codeSchemeId".fieldName, "Scheme Id".text)

  val industryCode: QuestionnaireNode =
    nonRepeatingParentQuestion("industryCode".fieldName, "".text, codeId, codeDescription, codeSchemeId, codeSchemeName)

  val industryCodes: QuestionnaireNode =
    repeatingParentQuestion("industryCodes".fieldName, "Industry Codes".text, industryCode)

  val industryCodesPage: QuestionnaireNode = pageQuestion(
    "industryCodes".fieldName,
    "Industry Codes".text,
    industryCodes
  )

  val registeredStreetAddress: QuestionnaireNode =
    stringQuestion("registeredStreetAddress".fieldName, "Street Address".text)

  val registeredLocality: QuestionnaireNode =
    stringQuestion("registeredLocality".fieldName, "Locality".text)

  val registeredRegion: QuestionnaireNode =
    stringQuestion("registeredRegion".fieldName, "Region".text)

  val registeredPostalCode: QuestionnaireNode =
    stringQuestion("registeredPostalCode".fieldName, "Postal Code".text)

  val registeredCountry: QuestionnaireNode =
    enumerationQuestion("registeredCountry".fieldName, "Country".text, Country.name)

  val contactDetailsPage: QuestionnaireNode = pageQuestion(
    "contactDetails".fieldName,
    "Contact Details".text,
    registeredStreetAddress,
    registeredLocality,
    registeredRegion,
    registeredPostalCode,
    registeredCountry
  )

  val companyName: QuestionnaireNode =
    stringQuestion("companyName".fieldName, "Company Name".text)

  val companyNumber: QuestionnaireNode =
    stringQuestion("companyNumber".fieldName, "Company Number".text)

  val incorporationDate: QuestionnaireNode =
    dateQuestion("incorporationDate".fieldName, "Incorporation Date".text)

  val companyType: QuestionnaireNode =
    stringQuestion("companyType".fieldName, "Company Type".text)

  val registryUrl: QuestionnaireNode =
    stringQuestion("registryUrl".fieldName, "Registry URL".text)

  val inactive: QuestionnaireNode =
    yesNoQuestion("inactive".fieldName, "Inactive?".text)

  val detailsPage: QuestionnaireNode = pageQuestion(
    "relationships".fieldName,
    "Relationships".text,
    companyName,
    companyNumber,
    incorporationDate,
    companyType,
    registryUrl,
    inactive
  )

  val informationSection: QuestionnaireNode = sectionQuestion(
    "information".fieldName,
    "Company Information".text,
    detailsPage,
    contactDetailsPage,
    industryCodesPage
  )

  val approvalQuestion: QuestionnaireNode =
    ExampleComponents.approvalQuestion("applicationApproved".fieldName, "Do you approve this company?".text)

  val approvalPage: QuestionnaireNode = pageQuestion(
    "approvalPage".fieldName,
    "Approval".text,
    approvalQuestion
  )

  val questionnaire: QuestionnaireNode = ExampleComponents.standard(
    NodeKey.random,
    "company".fieldName,
    "Company".text,
    Element.NonRepeatingParent(
      informationSection,
      approvalPage
    ),
    enums = Map(
      Country.name    → Country.values,
      YesNo.name      → YesNo.values,
      Currencies.name → Currencies.values
    ),
    questionnaireId = id
  )
}
