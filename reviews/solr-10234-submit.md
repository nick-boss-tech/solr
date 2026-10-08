# solr-10234-submit

- Branch: origin/solr-10234-submit
- Head: 16825538a766 (matches the listed head; checked against the ticket worktree)
- Base: upstream/main at 9d7cc2884e8a (merge-base cabedd1d9680, 16 commits behind, 3 commits ahead)
- Scope: 3 commits, 5 files, +90. `solr/test-framework/src/java/org/apache/solr/cloud/SolrCloudTestCase.java` and `solr/test-framework/src/java/org/apache/solr/BaseDistributedSearchTestCase.java` (each gains `@LuceneTestCase.SuppressFileSystems({"ExtrasFS", "HandleLimitFS"})`, with comments), `solr/core/src/test/org/apache/solr/cloud/CloudTestBaseFileSystemsTest.java` (new, reflection check of the three base classes), changelog `changelog/unreleased/SOLR-10234-cloud-tests-no-handle-limit.yml` (`type: other`, author Nick Shanin), and `SOLR-10234-TESTING.md` (author's hypothetical-reproduction note, left in place).
- Verdict: Nearly
- Reviewer: review-agent A3 (claude-haiku-5-5, Claude Code), 2026-10-08
- Patch: none

Nothing here was compiled, formatted, or run. No Gradle, no spotless, no test run, no fail-before proof. Every claim below rests on reading the diff and the code at the listed head. `SOLR-10234-TESTING.md` is labeled "hypothetical reproduction (nothing was compiled or run)" and is treated as unverified.

## Findings (ranked)

1. **Owner call A (see below). Suppressing `HandleLimitFS` removes a leak check for every multi-node test.** The annotation goes on `SolrCloudTestCase` and `BaseDistributedSearchTestCase`. By inheritance it reaches `AbstractFullDistribZkTestBase` and every cloud and distributed test subclass that does not declare its own annotation. `HandleLimitFS` exists to catch file-handle leaks, so those tests lose that check. The author calls the choice a "design pick" over a per-node limit, which would need a Lucene-side change. Pose it. Not patched.

2. **LOW, verified. The test method has no `@Test`.** `CloudTestBaseFileSystemsTest.java` declares `public void testMultiNodeBaseClassesSuppressHandleLimitFs()` with no annotation. Its siblings in the cloud package are not checked here. `TestSlowCompositeReaderWrapper` on `main` has two unannotated `test*` methods, so this codebase runs such methods; the omission is a convention note, not a proven defect.

3. **Verified (checked, no issue). Imports compile by reading.** `SolrCloudTestCase.java:45` already imports `org.apache.lucene.tests.util.LuceneTestCase`, so `@LuceneTestCase.SuppressFileSystems(...)` resolves. `BaseDistributedSearchTestCase` gains the same import in the diff. The test's `SuppressFileSystems` import matches the form `SolrTestCaseJ4` uses (`SolrTestCaseJ4.java:73`).

4. **Verified (checked, no issue). Inheritance path.** `AbstractFullDistribZkTestBase` (`solr/test-framework/src/java/org/apache/solr/cloud/AbstractFullDistribZkTestBase.java:130`) declares no annotation of its own. So the test's `getAnnotation` on that class depends on the annotation being inherited from `BaseDistributedSearchTestCase`. The author states that the annotation is `@Inherited`. That is not verifiable here, because the annotation lives in the Lucene test-framework jar.

5. **Verified (checked, no issue). The repeated `ExtrasFS` is needed.** `SolrTestCaseJ4` carries `@SuppressFileSystems("ExtrasFS")` (`SolrTestCaseJ4.java:147`). A subclass annotation replaces the parent's per the author's note, so dropping `ExtrasFS` from the new annotations would re-enable it for every cloud test. The branch keeps it.

## Owner calls (not decided here)

- **A. Suppress or limit.** Options: (a) keep the suppression on all multi-node base classes, as the branch does, and accept the loss of handle-leak detection there; (b) a per-node or per-JVM limit, which needs a Lucene-side change; (c) suppress only in the tests that hit the limit. Pose it. Not patched.

## Not checked

- Nothing compiled, formatted, or run. No focused test, no Spotless, no Error Prone.
- Lucene's semantics: `@SuppressFileSystems` being `@Inherited`, and the provider names `"HandleLimitFS"` and `"ExtrasFS"` matching the simple class names. Both live in the Lucene test-framework jar, not in this tree. If the name is wrong, the annotation does nothing, and the reflection test still passes, because it checks that the string is present, not that Lucene honours it.
- Whether the 2048-handle failure the ticket describes is still reachable after the change. The author says the test does not provoke it.
- `SOLR-10234-TESTING.md` is treated as unverified. Left in place.
