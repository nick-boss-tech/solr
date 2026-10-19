package org.apache.solr.handler.export;

import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.index.LogDocMergePolicyFactory;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestSolr15571Repro extends SolrTestCaseJ4 {

  @BeforeClass
  public static void beforeClass() throws Exception {
    systemSetPropertySolrTestsMergePolicyFactory(LogDocMergePolicyFactory.class.getName());
    initCore("solrconfig-sortingresponse.xml", "schema-15571.xml");
  }

  @Test
  public void testReporterFieldList() throws Exception {
    assertU(delQ("*:*"));
    assertU(commit());
    String base = "http://ns.example/organization/clinical_trials/";
    for (int d = 1; d <= 12; d++) {
      String id = "http://identifiers.org/clinicaltrials/NCT0000" + d;
      assertU(
          adoc(
              "id", id,
              "label", "study " + d,
              "Collaborator_rt_source_ss", base + "nct0000" + d + "/org_a",
              "Collaborator_rt_source_ss", base + "nct0000" + d + "/org_b",
              "Collaborator_ss", " ",
              "Collaborator_ss", "  ",
              "Collaborator_ss", "<" + base + "nct0000" + d + "/org_c>"));
      if (d % 4 == 0) assertU(commit());
    }
    assertU(commit());
    String stored =
        h.query(
            req(
                "q", "Collaborator_ss:\" \"",
                "fl", "id, label,Collaborator_rt_source_ss, Collaborator_ss",
                "sort", "id asc", "rows", "10", "wt", "json"));
    String exported =
        h.query(
            reqWithPath(
                "/export",
                "q", "Collaborator_ss:\" \"",
                "fl", "id, label,Collaborator_rt_source_ss, Collaborator_ss",
                "sort", "id asc"));
    System.out.println("STORED15571B " + stored);
    System.out.println("EXPORT15571B " + exported);
  }

  @Test
  public void testVariantReindexDeleteMerge() throws Exception {
    assertU(delQ("*:*"));
    assertU(commit());
    String base = "http://ns.example/organization/clinical_trials/";
    for (int d = 1; d <= 12; d++) {
      String id = "NCT0000" + d;
      assertU(adoc("id", id, "label", "study " + d, "Collaborator_ss", " ", "Collaborator_ss", "<" + base + d + ">"));
      if (d % 3 == 0) assertU(commit());
    }
    assertU(commit());
    for (int d = 1; d <= 12; d += 2) {
      assertU(adoc("id", "NCT0000" + d, "label", "updated " + d, "Collaborator_ss", " ", "Collaborator_ss", "  ", "Collaborator_ss", "<" + base + "u" + d + ">"));
    }
    assertU(commit());
    assertU(delI("NCT000004"));
    assertU(commit());
    assertU(optimize());
    String stored = h.query(req("q", "Collaborator_ss:\" \"", "fl", "id,label,Collaborator_ss", "sort", "id asc", "rows", "20", "wt", "json"));
    String exported = h.query(reqWithPath("/export", "q", "Collaborator_ss:\" \"", "fl", "id,label,Collaborator_ss", "sort", "id asc"));
    System.out.println("STORED15571C " + stored);
    System.out.println("EXPORT15571C " + exported);
  }
}
