package org.gooru.navigatemap.infra.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ashish on 26/2/17.
 */
public enum CurrentItemSubtype {

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
  OADebate("oa.debate"),
  OAOthers("oa.others");

  private final String name;
  private static final Map<String, CurrentItemSubtype> LOOKUP = new HashMap<>(values().length);

  static {
    for (CurrentItemSubtype subtype : values()) {
      LOOKUP.put(subtype.name, subtype);
    }
  }

  CurrentItemSubtype(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public static CurrentItemSubtype builder(String subtype) {
    CurrentItemSubtype result = LOOKUP.get(subtype);
    if (result == null) {
      throw new IllegalArgumentException("Invalid subtype: " + subtype);
    }
    return result;
  }
}
