# Sweep result (steps 0 and 1 only): solr-10641-submit

Not a full gate. No claim was taken; this is read-only preparation per VM1's interim instruction.

- Ticket: 10641
- Head swept: 4ab4bd2040f3cadca839717842a85ea09f32dcc3 (TSV head 4ab4bd2040f3cadca839717842a85ea09f32dcc3)
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
diff --git a/solr/core/src/test/org/apache/solr/cloud/OverseerTaskQueueTest.java b/solr/core/src/test/org/apache/solr/cloud/OverseerTaskQueueTest.java
index 917ff0f56f..51967aaed3 100644
--- a/solr/core/src/test/org/apache/solr/cloud/OverseerTaskQueueTest.java
+++ b/solr/core/src/test/org/apache/solr/cloud/OverseerTaskQueueTest.java
@@ -52,8 +52,7 @@ public class OverseerTaskQueueTest extends DistributedQueueTest {
 
     assertFalse("request node must be gone", zkClient.exists(event.getId()));
     assertEquals(
-        "response",
-        new String(zkClient.getData(watchID, null, null), StandardCharsets.UTF_8));
+        "response", new String(zkClient.getData(watchID, null, null), StandardCharsets.UTF_8));
   }
 
   @Test

 M solr/core/src/test/org/apache/solr/cloud/OverseerTaskQueueTest.java
```
