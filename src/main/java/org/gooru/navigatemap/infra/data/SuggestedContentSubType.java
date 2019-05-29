package org.gooru.navigatemap.infra.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ashish on 17/11/17.
 */
public enum SuggestedContentSubType {
  SignatureAssessment("signature-assessment"),
  SignatureCollection("signature-collection"),
  OAProjectPoster("oa.project.poster"),
  OAProjectPresentation("oa.project.presentation"),
  OAProjectVideo("oa.project.video"),
  OAProjectDiorama("oa.project.diorama"),
  OAProjectBrochure("oa.project.brochure"),
  OAProjectModel("oa.project.model"),
  OASeminar("oa.seminar"),
  OAShortAnswer("oa.short_answer"),
  OAExtendedResponses("oa.extended_response"),
  OAResearchPaper("oa.research_paper"),
  OAPositionPaper("oa.position_paper"),
  OALabReport("oa.lab_report"),
  OAExplanationArgument("oa.explanation_argument"),
  OADebate("oa.debate");

  private final String name;

  SuggestedContentSubType(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  private static final Map<String, SuggestedContentSubType> LOOKUP = new HashMap<>(values().length);

  static {
    for (SuggestedContentSubType suggestedContentSubType : values()) {
      LOOKUP.put(suggestedContentSubType.name, suggestedContentSubType);
    }
  }

  public static SuggestedContentSubType builder(String type) {
    SuggestedContentSubType result = LOOKUP.get(type);
    if (result == null) {
      throw new IllegalArgumentException("Invalid suggested content sub type: " + type);
    }
    return result;
  }

}
