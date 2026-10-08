# Sweep result (steps 0 and 1 only): solr-15712-submit

Not a full gate. No claim was taken; this is read-only preparation per VM1's interim instruction.

- Ticket: 15712
- Head swept: 555f9cab6b72d4c3fd8ac5f18bc5a3447b3c6107 (TSV head 555f9cab6b72d4c3fd8ac5f18bc5a3447b3c6107)
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
diff --git a/solr/core/src/test/org/apache/solr/schema/TestCollationFieldDocValues.java b/solr/core/src/test/org/apache/solr/schema/TestCollationFieldDocValues.java
index 017af8682c..fbe0b0e514 100644
--- a/solr/core/src/test/org/apache/solr/schema/TestCollationFieldDocValues.java
+++ b/solr/core/src/test/org/apache/solr/schema/TestCollationFieldDocValues.java
@@ -190,8 +190,8 @@ public class TestCollationFieldDocValues extends SolrTestCaseJ4 {
   }
 
   /**
-   * Collation keys are not UTF-8, so they must never be returned as stored values: the docValues
-   * of a collation field do not default to useDocValuesAsStored, and {@code fl=*} skips them.
+   * Collation keys are not UTF-8, so they must never be returned as stored values: the docValues of
+   * a collation field do not default to useDocValuesAsStored, and {@code fl=*} skips them.
    */
   public void testDocValuesAreNotUsedAsStored() {
     for (String name :

 M solr/core/src/test/org/apache/solr/schema/TestCollationFieldDocValues.java
```
