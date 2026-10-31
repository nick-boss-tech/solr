# PR 5268 Phase A: dependency repair recorded at H1, round 1

Status: DONE on vm5, 2026-10-31 00:58 UTC. Claim: claims/pr-5268-container-platform-fixes-linux-round-1.md.

- H0 (initial implementation head): 83a5c0d6e9f8758e26dddc217d7a3eec2339600c.
- H1 (pushed repair head, read back from origin/pr-5268-container-platform-fixes): a18d913f2908f0ddf244b43384bd3caecbe9bc6e.
- Commands on Linux, under /workspace/tools/.gradle-vm5.lock, JDK 21.0.12.1+1, Gradle 9.7.0 wrapper:
  - `./gradlew updateLicenses resolveAndLockAll --write-locks`: BUILD SUCCESSFUL in 5m 48s, log gates/logs/vm5/pr-5268-container-platform-fixes-round-1/phaseA-lock.log on this host.
  - `./gradlew tidy`: BUILD SUCCESSFUL in 2m 24s, log tidy.log in the same directory.
- Lockfile review: 22 gradle.lockfile files changed. Removed lines are three existing coordinates (commons-codec 1.22.1, jna 5.19.1, commons-lang3 3.20.0) with their configuration lists extended; no pinned version changed. New lock entries are testcontainers 2.0.5, docker-java 3.7.1 and its transports, duct-tape 1.0.8, commons-compress 1.28.0, and org.jetbrains:annotations 26.1.0.
- Tidy also reformatted solr/test-framework/src/java/org/apache/solr/util/TestcontainersUtil.java and solr/cross-dc-manager/src/test/org/apache/solr/util/TestcontainersUtilTest.java (line wrapping only).
- Commit a18d913f29 is authored by Nick Shanin with no trailers.

Files in H1 relative to H0:
- solr/api/gradle.lockfile
- solr/benchmark/gradle.lockfile
- solr/core/gradle.lockfile
- solr/cross-dc-manager/src/test/org/apache/solr/util/TestcontainersUtilTest.java
- solr/modules/analysis-extras/gradle.lockfile
- solr/modules/clustering/gradle.lockfile
- solr/modules/cross-dc/gradle.lockfile
- solr/modules/cuvs/gradle.lockfile
- solr/modules/gcs-repository/gradle.lockfile
- solr/modules/jwt-auth/gradle.lockfile
- solr/modules/langid/gradle.lockfile
- solr/modules/language-models/gradle.lockfile
- solr/modules/ltr/gradle.lockfile
- solr/modules/opentelemetry/gradle.lockfile
- solr/modules/scripting/gradle.lockfile
- solr/modules/sql/gradle.lockfile
- solr/solr-ref-guide/gradle.lockfile
- solr/solrj-jetty/gradle.lockfile
- solr/solrj-streaming/gradle.lockfile
- solr/solrj-zookeeper/gradle.lockfile
- solr/solrj/gradle.lockfile
- solr/test-framework/gradle.lockfile
- solr/test-framework/src/java/org/apache/solr/util/TestcontainersUtil.java
- solr/webapp/gradle.lockfile

Not yet proven: Phase B and Phase C legs. Phase B legs 4 and 5 need Docker, which vm5 does not have.
