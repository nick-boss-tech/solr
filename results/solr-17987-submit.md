# Sweep result (steps 0 and 1 only): solr-17987-submit

Not a full gate. No claim was taken; this is read-only preparation per VM1's interim instruction.

- Ticket: 17987
- Head swept: dba26c39877a6b7c94baf1cafee3a919c8b544fa (TSV head e8c22910f50747259e1075001bae5309163de6ab, MOVED since TSV)
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
diff --git a/solr/core/src/java/org/apache/solr/util/circuitbreaker/CPUCircuitBreaker.java b/solr/core/src/java/org/apache/solr/util/circuitbreaker/CPUCircuitBreaker.java
index aed579c599..53c16a1936 100644
--- a/solr/core/src/java/org/apache/solr/util/circuitbreaker/CPUCircuitBreaker.java
+++ b/solr/core/src/java/org/apache/solr/util/circuitbreaker/CPUCircuitBreaker.java
@@ -113,9 +113,7 @@ public class CPUCircuitBreaker extends CircuitBreaker implements SolrCoreAware {
       throw new IllegalStateException("JVM metrics disabled. Cannot calculate CPU usage");
     }
 
-    return reader
-        .collect(name -> name.contains("jvm_system_cpu_utilization"))
-        .stream()
+    return reader.collect(name -> name.contains("jvm_system_cpu_utilization")).stream()
         .filter(GaugeSnapshot.class::isInstance)
         .map(GaugeSnapshot.class::cast)
         .map(GaugeSnapshot::getDataPoints)

 M solr/core/src/java/org/apache/solr/util/circuitbreaker/CPUCircuitBreaker.java
```
