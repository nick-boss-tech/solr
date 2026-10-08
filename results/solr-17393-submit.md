# Sweep result (steps 0 and 1 only): solr-17393-submit

Not a full gate. No claim was taken; this is read-only preparation per VM1's interim instruction.

- Ticket: 17393
- Head swept: dd6c82924fff6b8f7cedaf020494c2f06ab502a2 (TSV head dd6c82924fff6b8f7cedaf020494c2f06ab502a2)
- Base (merge-base with apache/solr main e34067ae64): c3cdf7b46e8cfff3673f76d881f32cf8e7b00622
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
diff --git a/solr/core/src/java/org/apache/solr/handler/component/SuggestComponent.java b/solr/core/src/java/org/apache/solr/handler/component/SuggestComponent.java
index 0cbb50446f..0be9d7f2b3 100644
--- a/solr/core/src/java/org/apache/solr/handler/component/SuggestComponent.java
+++ b/solr/core/src/java/org/apache/solr/handler/component/SuggestComponent.java
@@ -346,8 +346,8 @@ public class SuggestComponent extends SearchComponent
   /**
    * Given a list of {@link SuggesterResult} and <code>count</code> returns a {@link
    * SuggesterResult} containing <code>count</code> number of {@link LookupResult}, sorted by their
-   * associated weights (highest first), with ties broken by suggestion text so that the merged order
-   * does not depend on which shard answered first
+   * associated weights (highest first), with ties broken by suggestion text so that the merged
+   * order does not depend on which shard answered first
    */
   static SuggesterResult merge(List<SuggesterResult> suggesterResults, int count) {
     SuggesterResult result = new SuggesterResult();

 M solr/core/src/java/org/apache/solr/handler/component/SuggestComponent.java
```
