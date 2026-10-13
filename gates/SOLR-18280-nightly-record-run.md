# Gate job: SOLR-18280 nightly record run at the live tip

- Branch: solr-18280-submit. Head: `0da92abd9e96616ed817a25da47078c753766119` (live tip, verified by ls-remote at posting, 2026-10-13).
- Status: DONE 2026-10-13T14:23Z (vm2, gate backlog coordinator). Head run 25 of 25, 0 failures, seed 18280C0FFEE18280; see receipts/SOLR-18280.md. Claim claims/pool-vm2-gate-backlog-18280-nightly-record-run.md DONE. Queued as /workspace/gates/g18280-gate.sh.
- Capability tags: gate.
- Intended host: any capable Linux host (vm1 or vm2), under the WORKFLOW.md claim rules. Windows never claims build work.
- Job type: record run only. No branch changes, no pushes to the submit branch.
- Why: the live PR body claims a run dated 2026-10-04, a "2 of 2", "24 of 25" runs (twice), and a base comparison. None of it is backed by a record (reports/pr-drift-round-1.md). The pr-drift record run of 2026-10-13T09:26Z executed the head and base legs with the nightly group off: every TestReplicationHandler test is `@Nightly`, so all 25 tests skipped on both legs and no test executed. A later nightly run (2026-10-13, recorded in receipts/SOLR-18280.md) ran the head at 25 tests with 1 failure, doTestIndexAndConfigAliasReplication's clock-based startAt wait (36 seconds), classified a timing flake, and the base leg at 25 of 25 on identical production (the branch is test-only, no production delta). That still leaves no clean head count the body can carry, and the receipt's next step (one head rerun, or a stated flake) is open. This job is the one head run that settles it: it either backs a clean count, or it gives the counts the body must be corrected to.

## Run

1. Claim this job under WORKFLOW.md before starting.
2. Verify the branch tip by ls-remote. If the tip has moved past `0da92abd9e96`, run at the live tip and record the head you ran.
3. Run through the claiming host's normal gate route (on vm1, ~/workspace/tools/solr-gradle.sh; never ./gradlew): module `:solr:core`, focused class `org.apache.solr.handler.TestReplicationHandler` (package verified by grepping the branch at posting: solr/core/src/test/org/apache/solr/handler/TestReplicationHandler.java), with `-Ptests.nightly=true`, counted from fresh JUnit XML.
4. Seed: no seed is recorded in gates/pr-drift-round-1-run-records.md or receipts/SOLR-18280.md for the earlier runs. Pick a seed and record it with the result.

## Flake note

A separate dated run today (2026-10-13) recorded one clock-based startAt timing failure at the head, with the base leg at 25 of 25, classified a timing flake (receipts/SOLR-18280.md). If this run reproduces a failure in that same test (doTestIndexAndConfigAliasReplication, the startAt wait), record it and do not relaunch: two same-shape failures are a classification question for the main side, not a rerun. A failure in any other test is likewise recorded, not relaunched.

## Deliverable

- The counts (head, seed, tests, failures, skipped, failure names and lines) recorded in receipts/SOLR-18280.md and in the completion note below.
- If the counts contradict the body, the completion note names each unsupported claim by the wording reports/pr-drift-round-1.md quotes (the 2026-10-04 run, the "2 of 2", each "24 of 25", the base comparison) and states the recorded count that replaces it. The main side or the review loop applies the correction to the body; this job does not edit the body.

## Completion note (the claimant fills this in)

- Head run at: 0da92abd9e96616ed817a25da47078c753766119 / seed: 18280C0FFEE18280 / tests 25, failures 0, errors 0, skipped 0 (fresh JUnit XML, logs/vm2/18280-head-xml).
- Body claims now backed by a record: the head count of 25 of 25 with the nightly group on at 0da92abd9e96 (no failure names). The base comparison is backed by the 25 of 25 base leg at 0d2a4649c79a recorded in receipts/SOLR-18280.md, on identical production.
- Claims named for correction, with the recorded counts that replace them: the "24 of 25" counts (twice) are replaced by 25 of 25 at this head (seed 18280C0FFEE18280). The 2026-10-04 run and the "2 of 2" have no record at 0da92abd9e96 and are not backed by this run; the main side decides whether to cut them or record them.

## Checklist

- [x] claimed; live tip verified by ls-remote
- [x] nightly head run executed, counts from fresh JUnit XML
- [x] seed and failure names recorded
- [x] receipts/SOLR-18280.md updated; completion note filled
- [x] job and claim DONE
