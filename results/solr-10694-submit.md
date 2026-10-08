# Sweep result (steps 0 and 1 only): solr-10694-submit

Not a full gate. No claim was taken; this is read-only preparation per VM1's interim instruction.

- Ticket: 10694
- Head swept: 093d90c62dedd8701276dded16e740d451d3a6df (TSV head 093d90c62dedd8701276dded16e740d451d3a6df)
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
diff --git a/solr/core/src/java/org/apache/solr/response/CSVResponseWriter.java b/solr/core/src/java/org/apache/solr/response/CSVResponseWriter.java
index 3627ef8fbd..20b90dbd3c 100644
--- a/solr/core/src/java/org/apache/solr/response/CSVResponseWriter.java
+++ b/solr/core/src/java/org/apache/solr/response/CSVResponseWriter.java
@@ -448,8 +448,8 @@ class CSVWriter extends TabularResponseWriter {
 
   /**
    * Writes one cell value. Values with no flat form (maps, lists, NamedLists, {@link MapWriter},
-   * {@link IteratorWriter}, ...) are written as compact JSON; the tabular base class writes
-   * nothing for them, which would drop the cell and shift the columns after it.
+   * {@link IteratorWriter}, ...) are written as compact JSON; the tabular base class writes nothing
+   * for them, which would drop the cell and shift the columns after it.
    */
   private void writeCellVal(String name, Object val) throws IOException {
     if (val instanceof Map

 M solr/core/src/java/org/apache/solr/response/CSVResponseWriter.java
```
