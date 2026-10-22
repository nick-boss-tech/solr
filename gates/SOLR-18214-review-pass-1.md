# Gate job: SOLR-18214 review pass 1 (Base configset during configset creation is not optional)

Status: DONE (GATE GREEN at c00cacddf6, vm4, 2026-10-22T22:25:58Z)

- Branch: solr-18214-empty-configset. Head: `c00cacddf62d67001b13b8aa2c2287a012a5807e` (ls-remote 2026-10-22T21:51:22Z, from the claim). Prior gated head: `c00cacddf6` (none on this unit).
- Intended host: vm4. Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.cloud.TestCloneConfigSetWithoutBase`. Seed: `18214C0FFEE18214`.
- Why: pass 1 of the review round for unit 39.

## Checks

- [x] head confirmed by ls-remote
- [x] diff scope confirmed
- [x] step 0 changelog YAML parse, rc=0 (`changelog/unreleased/SOLR-18214.yml`)
- [x] step 1 tidy, rc=0, status file empty
- [x] step 2 Error Prone compile `:solr:core:compileTestJava -Pvalidation.errorprone=true`, rc=0 (22:11:10)
- [x] step 3 proof leg (base `ConfigSetCmds.java` and `CloneConfigSet.java` in worktree `proof-18214`, test at head): rc=1. Expected failure `testOmittedBaseCreatesConfigSetThatLoadsACore` with `Base ConfigSet does not exist: null` (the ticket's symptom). Tests 1, failed 1.
- [x] step 4 focused head run, from fresh XML: TestCloneConfigSetWithoutBase tests=1, skipped=0, failures=0, errors=0 (ts 2026-10-22T22:22:08Z). rc=0.
- [x] step 5 module check `:solr:core:check -x test`, rc=0 (22:25:58).

Logs: `/workspace/gates/logs/vm4/18214/`.

## Deliverable

- Pass 1 result in `reports/newdev-critblock-review-round-1/solr-18214-empty-configset.md`.
- Receipt section in `receipts/SOLR-18214.md`, top section.
