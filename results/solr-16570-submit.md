# Sweep result (steps 0 and 1 only): solr-16570-submit

Not a full gate. No claim was taken; this is read-only preparation per VM1's interim instruction.

- Ticket: 16570
- Head swept: 974c44f9608261adbdb6a2aa9d1f7d7250ccec43 (TSV head 974c44f9608261adbdb6a2aa9d1f7d7250ccec43)
- Base (merge-base with apache/solr main e34067ae64): cabedd1d968059215188f4e7563fb303241899ed
- JDK: Temurin 21.0.12.1+1 (openjdk version 21.0.12.1), Gradle 9.7.0 wrapper
- Verdict: DRIFT

## Step 0: changelog YAML parse
- Result: PASS
- 1 fragment(s) parsed

## Step 1: tidy
- Task: :solr:core:tidy
- rc: 0
- gradle/libs.versions.toml pruned by tidy and restored: False
- Other modification (drift): YES

### Drift diff
```
diff --git a/solr/core/src/test/org/apache/solr/search/TestCollapseQParserPlugin.java b/solr/core/src/test/org/apache/solr/search/TestCollapseQParserPlugin.java
index b53bffc0e9..c935a5bf99 100644
--- a/solr/core/src/test/org/apache/solr/search/TestCollapseQParserPlugin.java
+++ b/solr/core/src/test/org/apache/solr/search/TestCollapseQParserPlugin.java
@@ -1035,7 +1035,13 @@ public class TestCollapseQParserPlugin extends SolrTestCaseJ4 {
     for (String hint : Arrays.asList("", " hint=top_fc")) {
       assertQ(
           "collapse" + hint,
-          req("q", "*:*", "fq", "{!collapse field=" + f + " max=test_i" + hint + "}", "sort", "id asc"),
+          req(
+              "q",
+              "*:*",
+              "fq",
+              "{!collapse field=" + f + " max=test_i" + hint + "}",
+              "sort",
+              "id asc"),
           "*[count(//doc)=2]",
           "//result/doc[1]/str[@name='id'][.='2']",
           "//result/doc[2]/str[@name='id'][.='3']");

 M solr/core/src/test/org/apache/solr/search/TestCollapseQParserPlugin.java
```
