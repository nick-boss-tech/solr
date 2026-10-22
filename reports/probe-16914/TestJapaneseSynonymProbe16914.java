package org.apache.solr.search;

import org.apache.solr.SolrTestCaseJ4;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestJapaneseSynonymProbe16914 extends SolrTestCaseJ4 {

  @BeforeClass
  public static void beforeClass() throws Exception {
    initCore("solrconfig.xml", "schema-ja-16914.xml");
  }

  @Test
  public void testIndexSynonymSearch() throws Exception {
    assertU(adoc("id", "1", "text_ja", "東京の天気"));
    assertU(adoc("id", "2", "text_ja", "ビール"));
    assertU(commit());
    assertQ(req("q", "text_ja:東京"), "//result[@numFound='1']");
    assertQ(req("q", "text_ja:とうきょう"), "//result[@numFound='1']");
    assertQ(req("q", "text_ja:beer"), "//result[@numFound='1']");
    assertQ(req("q", "text_ja:ビール"), "//result[@numFound='1']");
  }
}
