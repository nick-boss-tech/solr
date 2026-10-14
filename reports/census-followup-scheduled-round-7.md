# Census follow-up, scheduled round 7 (settlements, a reconciliation, and a review of the SOLR-16630 conversion gate)

- Trigger: the two-hourly scheduled check. Recorded tip `c1ad20c92b4`; live tip at the check `d5b2be3a78f`. Claim: `claims/pool-census-followup-round-7-scheduled.md` (pushed at `04da62a6ffc`).
- The new commits before the check were vm2's and the main side's records: the SOLR-17215 and 17393 receipts ruled green, the SOLR-16887 BATS runner-defect classification, the SOLR-15712 version-gate investigation and a corrected test queued, the owner rulings of 2026-10-14, the owner items brief, the maintainer request rule, and the SOLR-16630 all-sleeps conversion at `136145b598d`, which the receipt records as failed in all four runs.
- Worktree: `wt/pr-prepare-suggester`. Live fork tips checked by `git ls-remote origin` for every ticket.
- Read only for branches: no build, test, Gradle or gate run; no commit or push on any `solr-*-submit` branch; no GitHub write; no receipt, gate file, claim, PR or the owner brief edited.
- Written by the parent from the three subagent replies. The subagents could not write report `.md` files.

## Most important: the live PR is on a failing head

- PR #5106 (SOLR-16630) is on head `136145b598d`, pushed ahead of its gate at the owner's direction. The last green head is `7ab3c03a1c`. The receipt says a fix is in progress.
- **Record integrity issue.** Commit `f313b72` (16:32Z) recorded the conversion gate as green, citing runs from 16:40Z to 17:08Z. Commit `d5b2be3` (16:35Z) corrected it to FAILED. The XML files cited by the correction have mtimes from 16:14Z to 16:20Z, which predate the green account. The record says only that the green account came from "gate-log output". The record cannot show where it came from.

## Review of the SOLR-16630 failure (read only, slice S3)

- **Recorded failure.** A `NullPointerException` at `TestCoordinatorRole.java:364`, in `liveNodes.contains(nrtJettyF.getNodeName())` inside the add-retry wait predicate. It happened in all four runs: 9 tests, 1 failure each, `testNRTRestart`. Seeds `16630C0FFEE16630` (runs 1 to 3) and `16630DEADBEEF16630` (run 4). The run files are `g16630-allsleeps-postpush-run1.xml` to `run4.xml`.
- **Likely cause (strongest).** The NRT node name is null while the node is stopped. `getNodeName()` returns null when `getCoreContainer()` is null (`JettySolrRunner.java:452`). `liveNodes` is built with `Set.copyOf` (`ClusterState.java:277`), which throws on `contains(null)`. This is supported by the source, not by a run.
- **Inference, not in the record.** The wait at line 272, right after the stop, probably throws first. The manipulator then dies before "restarting NRT jetty", and line 364 is downstream. The separator is a stop-window `getNodeName()` print, and the absence of that line from all four XML files.
- **Ruled out.** A timeout rethrows `SolrException`, not an NPE, so a flake does not explain the recorded NPE.
- **Warn-and-proceed.** None of the bounded-wait timeouts caused the recorded failure. The NPE escapes the catch at line 272. A green run with a warning at 272 or 276 has not verified the outage.

**Wait-by-wait (new waits in the conversion):**

| Wait (line) | Event | Bound | On timeout |
|---|---|---|---|
| 272 | NRT leaves live nodes | 2 min | warn; NPE escapes |
| 276 | main countdown (319) | 2 min | warn; restart NRT |
| 307 to 316 | PULL served | 2 min | exit; 329 fails |
| 351, 358 | NRT ACTIVE or ZK change | 30 s; 2 min | rethrow; fails |
| 394 to 411 | NRT serves id:345 | 2 min | exit; 418 fails |
| 453 to 466 | N queries | 1 min | break; no failure |
| 257, 285 (carried) | countdowns | 2 min | warn; proceed |
| 589 (helper) | any state change | 100 ms | silent |

Skipping 272 or 276 is caught only indirectly, at 329. Skipping 307, 351 or 394 changes pace only.

## Settlements (slice S1 and S2)

- **SOLR-17215:** SATISFIED. `pr-drafts/suggester/SOLR-17215.md` at gated head `04d35df186d`. Counts match the receipt, dated 2026-10-14. The pre-fix leg fails on base with `java.lang.RuntimeException: Exception during query` (1 failure, 0 errors). Byte count is about 4,379 characters, above the guide (mostly link URLs), not trimmed.
- **SOLR-17393:** SATISFIED. `pr-drafts/suggester/SOLR-17393.md` at head `626241e647e`. Counts match the receipt. The pre-fix leg fails on base in `testEqualWeightsDoNotDependOnShardResponseOrder` (merge-order assertion, 1 failure). The bold summary is narrowed to that failing test. About 4,239 characters, not trimmed.
- **SOLR-17372:** no draft under `pr-drafts/`, so nothing to retire in the drafts. Stale text remains in `reports/morning-openings-slates-2026-10-14.md`: lines 402 to 404 say the 17215 and 17393 receipts are OPEN (they are now written), and line 430 says the retire word is owed (the ruling is at line 557). The main side owns the slates report.
- **SOLR-15712:** HOLD kept. The banner in `pr-drafts/schema-analysis/SOLR-15712.md` now states the `2d65ed6329` failure (4 of 5 focused tests failed, in field-type initialization), the `6403804633e` classification (a test-schema defect, by elimination, not confirmed), the corrected test (queued, not run), and the next step. Head citations match `cb988a2ff4c`.
- **SOLR-16887:** no draft under `pr-drafts/`. Nothing written.

## Owner items brief reconciliation (read only, nothing decided)

1. **SOLR-12505**, call 1c: mismatch. The draft has no Eric Pugh attribution, and no Jira check is recorded.
2. **SOLR-18368**: consistent. Its 2026-10-14 Jira check is not recorded, and the record shows a Resolved-versus-Open conflict.
3. **SOLR-18339**: consistent. Its 2026-10-14 SOLR-18336 check is not recorded.
4. **SOLR-17752**: mismatch. The build-docs round 1 decision D3 adopted a rebase onto `d38e0f1623`, not `14c7aac0d151` as the brief says. A recommendation exists.
5. **SOLR-9598**: consistent, except that no OWNER NOTE is in the draft.
6. **PR #5010**: mismatch. The SolrCloud round 1 answers adopted cutting the CI sentence, but a later record says checks pass. The seed `4C2E85AFC002DC` is not in any pr-prepare record.

## Gate jobs filed this round

None.

## Owner decisions this round needs

1. **SOLR-16630, the live PR.** Head `136145b598d` is the failing conversion. Options include reverting the PR head to `7ab3c03a1c`, the last green head, or fixing the conversion. The reviewer's request stays open.
2. **SOLR-16630, the record.** Decide how to handle the green account in `f313b72`. The record cannot show where it came from.
3. **SOLR-17215 and 17393.** Their drafts are satisfied, but both are over the length guide. Trim or waive.
4. The three owner-brief mismatches above (SOLR-12505, SOLR-17752 and PR #5010).

## Still open from earlier rounds

- SOLR-8954: owner decision 1.
- SOLR-14200 and SOLR-15326: retire or keep.
- SOLR-17433: retire or ship.
- SOLR-10390: withdraw as superseded, or keep the test as a guard.
- SOLR-3684: submit or retire.
- SOLR-17987: confirm the rename is deferred to opening.
- SOLR-3923: a branch fix before its premise run.
- SOLR-9968: confirm the top-up gate ran at `c31d2ff1a3f`.
- SOLR-14231: vm2 to restate the counts and add the fail-before line.
- SOLR-4754: HOLD from round 1.
- SOLR-8628 and SOLR-10234: opening held on gates and decision 8.
- SOLR-3865: owner decision 8.
- SOLR-13838: a fail-before run and the ticket text are owed.
- SOLR-14098: the ledger date conflict is with the main side.
- SOLR-18278: owner retire call.
- SOLR-16887: BATS job re-issued after a runner-defect classification.
- SOLR-15906: the vm2 gate on the sort-paren reading is OPEN.
- SOLR-10641: ship-alone against the DISCUSS item 11 in the answers file.
- SOLR-17292: receipt reconcile.
