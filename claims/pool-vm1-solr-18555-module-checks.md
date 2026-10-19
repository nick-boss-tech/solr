# Claim: SOLR-18555 module checks, SolrJ and ref guide, plus SolrJ tests

- Claimant host: vm1 (phone agent session, at the owner's direction).
- Status: DONE.
- Job or assignment: `assignments/pool-solr-18555-module-checks.md`.
- Slice: the whole assignment at head b99259cba3572cc08586b2456c78780c814a2bf7: confirm head, detached worktree, `:solr:solrj:check -x test`, `:solr:solr-ref-guide:check -x test`, `:solr:solrj:test` counted from fresh JUnit XML, then the receipt section at the top of `receipts/SOLR-18555.md`.
- Capability tags used: gate.
- Lock note: on vm1 the Gradle runs serialize on the shared test-queue lock `~/workspace/tools/.solr-test-queue.lock` (blocking flock) through `~/workspace/tools/solr-gradle.sh`, which is this host's form of the assignment's host-lock step. JAVA_HOME is set by that script to the vendored JDK 21.
- Started: 2026-10-19T18:20:06Z (UTC).

Heartbeat: 2026-10-19T18:20:06Z

Result: 2026-10-19: items 1 to 6 recorded in receipts/SOLR-18555.md (new top section). SolrJ check rc=0; SolrJ tests 1,357 tests, 0 failures, 0 errors, 45 skipped across 140 classes; ref guide check rc=1 at the head and rc=1 identically at base c8ab71430f6e (buildLocalAntoraSite cannot run in a linked worktree), recorded as pre-existing. Assignment and this claim set DONE in the same push.

DONE: 2026-10-19. Deliverable: receipts/SOLR-18555.md.
