# Claim: SOLR-14199 record job on vm1

Claimant host: vm1. Job: gates/SOLR-14199-record.md.
Capability tags: gate (record runs only). Staffing: 1.
Started: 2026-10-12T14:24:00Z (UTC).
Branch: solr-14199-submit at head e743c90da79a155fd6e6161233af5d924dc9dad1 (live tip, verified by ls-remote at claim time). No branch change under this job.

Job shape, per gates/SOLR-14199-record.md: record only; no branch commit, no push of the branch, no test edit. Run 1: org.apache.solr.search.TestSolrQueryParser in :solr:core at the head; enumerate every testcase from the fresh JUnit XML, compare against the methods declared in the head's TestSolrQueryParser.java, and identify the 37th test (the XML testcase with no declared method of the same name) and its origin; if the XML count is not 37, record the actual count and the full comparison. Run 2: org.apache.solr.schema.SchemaVersionSpecificBehaviorTest in :solr:core at the head; record the outcome either way (counts and log name on a pass; counts, failing method and failure text verbatim on a failure). The schema round 1 s1 report predicts run 2 fails; if it fails, the draft HOLD stands and no fix is made under this job.

On vm1 all Gradle work goes through ~/workspace/tools/solr-gradle.sh (vendored JDK 21 and Gradle 9.7.0, proxy relay, shared dependency cache), never ./gradlew.

Worktree: ~/workspace/solr-worktrees/solr-14199-record. Runner: ~/workspace/tools/g14199-record.sh (blocking flock on ~/workspace/tools/.solr-test-queue.lock). Log: ~/workspace/tools/g14199-record.log.

On completion: receipts/SOLR-14199.md carries both records, gates/SOLR-14199-record.md is marked DONE, and this claim is marked DONE. The draft and its HOLD markers are not edited by this job.

Heartbeat: 2026-10-12T14:24:00Z (UTC) (claim; runner starts detached on vm1).

- Status: DONE, 2026-10-12T14:33:15Z (UTC). Both record runs completed at head e743c90da79 (log g14199-record.log; counts from the fresh JUnit XML of each run). Run 1: TestSolrQueryParser 37 tests, 0 failures, 0 errors, 0 skipped; the 37th test is `testSynonymsBoost_singleConceptQueryMultiTermSynonymWithMultipleBoost_shouldParseMultiplicativeBoostedQuery`, declared in the head file at lines 1416 to 1418 with its signature wrapped across lines, which is why same-line counts found only 36 declarations. Run 2: SchemaVersionSpecificBehaviorTest 1 test, 1 failure (`testVersionBehavior`, "java.lang.AssertionError: int field's type has wrong omitNorm for ver=1.0 expected:<false> but was:<true>"), the failure the schema round 1 s1 report predicted; recorded verbatim in receipts/SOLR-14199.md, not fixed under this job, and the draft HOLD stands. Deliverables: receipts/SOLR-14199.md (record runs section), gates/SOLR-14199-record.md marked DONE. No branch commit, no test edit.
