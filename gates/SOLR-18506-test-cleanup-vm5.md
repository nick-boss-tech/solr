# Gate job: SOLR-18506 (TestThinCache cleanup after assertion failure)

Status: DONE. Candidate `solr-18506-test-cleanup` at `5262d15678d0f21ec1b0a9cfcbf968252a0de5aa`. Claim: `claims/pool-vm5-solr-18506-test-cleanup.md`. Deliverables: `reports/solr-18506-test-cleanup-vm5.md`, `receipts/SOLR-18506.md`.

- Branch: `solr-18506-test-cleanup`. Head: `5262d15678d0f21ec1b0a9cfcbf968252a0de5aa`, pushed from vm5 on 2026-10-23.
- Prior gated head: `77c019e1ff0e922e3c2350379dece841651b1d44` (PR branch `solr-18506-submit`, not changed).
- Intended host: vm5, with a one-off direct Gradle run because the inode guard cannot evaluate `/workspace`.
- Module: `:solr:core`. Focused class: `org.apache.solr.search.TestThinCache`. Seed: `1668E8BD438E6830`.
- Diff scope against `77c019e1ff0e`: `TestThinCache.java` only, 70 insertions and 71 deletions (2 and 3 ignoring whitespace).

## Steps

0. Changelog: not applicable (test only).
1. Tidy: rc 0, no changes. Log `gates/logs/vm5/SOLR-18506-test-cleanup/tidy-1.log`.
2. Error Prone compile: rc 0, 0 errors. Log `errorprone-1.log`.
3. Controlled failure: rc 1 from the injected `AssertionError`; `testSimple` failed on the injected message; `SOLR18506-PROOF contexts before=60 after=60`. Log `proof-leg.log`; XML under `proof-leg.xml/`. A first attempt failed to compile (unreachable statement after the injected throw; no test ran), kept as `proof-leg-compile-error.log`.
4. Focused head run: rc 0, TestThinCache 2 tests, 0 failures, 0 errors, 0 skipped, counted from fresh XML under `focused-final.xml/`.
5. Module check `:solr:core:check -x test`: rc 0. Log `check-1.log`.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Inode guard | decided: one-off direct Gradle run under the vm5 lock | The guard cannot evaluate the NFS mount; owner direction 2026-10-23 | Nick Shanin |
| Proof method | decided: temporary failure and `@After` count, reverted before the final runs | none | vm5 |
| PR actions | decided: none | No change to PR 5029, no new PR, no comments, no Jira | Nick Shanin |

## On green

Receipt section written at the top of `receipts/SOLR-18506.md`. Claim and assignment marked DONE in the same push.

## Checklist

- [x] head confirmed by ls-remote
- [x] diff scope confirmed
- [x] step 0 changelog not applicable
- [x] step 1 tidy clean
- [x] step 2 Error Prone compile
- [x] step 3 proof leg, expected failure by content
- [x] step 4 focused head run, counted from fresh XML
- [x] step 5 module check
- [x] receipt written
- [x] job DONE, claim DONE
