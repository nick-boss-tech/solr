# Sweep result (steps 0 and 1 only): solr-17612-submit

Not a full gate. No claim was taken; this is read-only preparation per VM1's interim instruction.

- Ticket: 17612
- Head swept: cd0426e40a72bd0649da40e26a4e89628026e06b (TSV head cd0426e40a72bd0649da40e26a4e89628026e06b)
- Base (merge-base with apache/solr main e34067ae64): e2cdb2d7e8ae0be4cf6cf0606206c67d89e7278f
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
diff --git a/solr/core/src/java/org/apache/solr/handler/component/SpellCheckComponent.java b/solr/core/src/java/org/apache/solr/handler/component/SpellCheckComponent.java
index c8846f3bd3..0cfbf18950 100644
--- a/solr/core/src/java/org/apache/solr/handler/component/SpellCheckComponent.java
+++ b/solr/core/src/java/org/apache/solr/handler/component/SpellCheckComponent.java
@@ -168,7 +168,8 @@ public class SpellCheckComponent extends SearchComponent implements SolrCoreAwar
         int alternativeTermCount =
             params.getInt(SpellingParams.SPELLCHECK_ALTERNATIVE_TERM_COUNT, 0);
         // If specified, this can be a discrete # of results, or a percentage of fq results.
-        // A fractional maxResultsForSuggest is a share of the filtered doc count across *all* shards,
+        // A fractional maxResultsForSuggest is a share of the filtered doc count across *all*
+        // shards,
         // which a single shard cannot know. The coordinator computes it in finishStage from the
         // per-shard counts that each shard reports back (see "maxResultsByFilters" below).
         final boolean deferToCoordinator = shardRequest && isFractionalMaxResultsForSuggest(params);
@@ -352,6 +353,7 @@ public class SpellCheckComponent extends SearchComponent implements SolrCoreAwar
     }
     return total == null ? null : (int) Math.min(total, Integer.MAX_VALUE);
   }
+
   protected void addCollationsToResponse(
       SolrParams params,
       SpellingResult spellingResult,
@@ -465,9 +467,7 @@ public class SpellCheckComponent extends SearchComponent implements SolrCoreAwar
     final Integer maxResultsForSuggest =
         maxResultsForSuggest(
             rb,
-            isFractionalMaxResultsForSuggest(params)
-                ? sumMaxResultsByFiltersFromShards(rb)
-                : null);
+            isFractionalMaxResultsForSuggest(params) ? sumMaxResultsByFiltersFromShards(rb) : null);
     int count = rb.req.getParams().getInt(SPELLCHECK_COUNT, 1);
     int numSug = Math.max(count, AbstractLuceneSpellChecker.DEFAULT_SUGGESTION_COUNT);
 

 M solr/core/src/java/org/apache/solr/handler/component/SpellCheckComponent.java
```
