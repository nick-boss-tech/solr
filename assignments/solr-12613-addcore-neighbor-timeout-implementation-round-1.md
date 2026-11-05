# Assignment: SOLR-12613 Add Core neighbor timeout implementation triage, round 1

Status: OPEN (implementation diagnosis/repair; any source fix requires separate exact-head proof).
Intended host: any available pool host with the implementation, review, and records tags; a capable Linux gate/Selenium host is required for later gate proof.
Tags: implementation, review, records; gate and selenium for any separately claimed proof job.

## Owner direction and boundary

Nick directed the completed round-2 gate result back to implementation for the unresolved Add Core neighbor failure. The round-2 gate remains NOT GREEN. This assignment does not diagnose the cause and does not turn the timeout into a source regression or a flake by assumption.

Do not modify the existing `pr-5110-core-menu-lifecycle` F2 branch, the Apache contributor PR #5110, its comments, the round-2 receipt/failure verdict, or another worker's records. No public PR/Jira comment or PR opening is authorized. Any code fix must be a separately claimed, ticket-scoped follow-up branch with an ordinary commit and verified pushed SHA; do not push to or rewrite F2/P. Keep the existing failed gate receipt unchanged. A source change needs a new exact-head Linux proof job/receipt before it can be considered ready; this assignment is not a gate rerun.

## Exact inputs and current evidence

- Original PR/base P: `6b5d830132b941faa85856347099ade8b190be0e`.
- Candidate F2: `b0e096ae19ad637107227e1bc04e951b9023a680`. Its `P..F2` diff is limited to `app.js` and `AdminUiNodePickerTest.java`; `AdminUiCoreAdminStandaloneTest.testAddCoreViaUi` itself was not changed.
- F2's full 17-method neighbor selection at seed `5110C0AE` was 16/17: `testAddCoreViaUi` timed out waiting to type `addedcore` into `By.id: add_name` (0 errors, 0 skips). P passed 17/17 at the same seed. P and F2 both passed 17/17 at `12613F00D`.
- The older Windows synthetic-head run was also 16/17 with the same method timing out at `12613F00D`; the current P/F2 runs at that seed did not reproduce it. That history does not establish a cause.

Evidence is pinned to board commit `268dde55a9b034280616d733919d2eb1ddc40a0d`:

- [Round-2 receipt](https://github.com/nick-boss-tech/solr/blob/268dde55a9b034280616d733919d2eb1ddc40a0d/receipts/SOLR-12613-pr-head-lifecycle-round-2.md), [failure verdict](https://github.com/nick-boss-tech/solr/blob/268dde55a9b034280616d733919d2eb1ddc40a0d/claims/pool-phone-agent-2-gate-SOLR-12613-pr-head-lifecycle-round-2-verdict-failed.md), and [evidence index](https://github.com/nick-boss-tech/solr/blob/268dde55a9b034280616d733919d2eb1ddc40a0d/receipts/SOLR-12613-pr-head-lifecycle-round-2-evidence/evidence-index.md).
- [F2 failing run log](https://github.com/nick-boss-tech/solr/blob/268dde55a9b034280616d733919d2eb1ddc40a0d/receipts/SOLR-12613-pr-head-lifecycle-round-2-evidence/steps/neighbors/f2-neighbors-5110C0AE/f2-neighbors-5110C0AE.log), [fresh CoreAdmin XML](https://github.com/nick-boss-tech/solr/blob/268dde55a9b034280616d733919d2eb1ddc40a0d/receipts/SOLR-12613-pr-head-lifecycle-round-2-evidence/steps/neighbors/f2-neighbors-5110C0AE/f2-neighbors-5110C0AE.xml/TEST-org.apache.solr.webapp.AdminUiCoreAdminStandaloneTest.xml), [failure screenshot](https://github.com/nick-boss-tech/solr/blob/268dde55a9b034280616d733919d2eb1ddc40a0d/receipts/SOLR-12613-pr-head-lifecycle-round-2-evidence/steps/neighbors/f2-neighbors-5110C0AE/failure-debug/ui-failure-testAddCoreViaUi-001/screenshot.png), [captured page HTML](https://github.com/nick-boss-tech/solr/blob/268dde55a9b034280616d733919d2eb1ddc40a0d/receipts/SOLR-12613-pr-head-lifecycle-round-2-evidence/steps/neighbors/f2-neighbors-5110C0AE/failure-debug/ui-failure-testAddCoreViaUi-001/page.html), and [captured browser console](https://github.com/nick-boss-tech/solr/blob/268dde55a9b034280616d733919d2eb1ddc40a0d/receipts/SOLR-12613-pr-head-lifecycle-round-2-evidence/steps/neighbors/f2-neighbors-5110C0AE/failure-debug/ui-failure-testAddCoreViaUi-001/console.log).
- Controls: [P at `5110C0AE` JUnit summary](https://github.com/nick-boss-tech/solr/blob/268dde55a9b034280616d733919d2eb1ddc40a0d/receipts/SOLR-12613-pr-head-lifecycle-round-2-evidence/steps/neighbors/p-neighbors-5110C0AE/junit-summary.txt), [P at `12613F00D` JUnit summary](https://github.com/nick-boss-tech/solr/blob/268dde55a9b034280616d733919d2eb1ddc40a0d/receipts/SOLR-12613-pr-head-lifecycle-round-2-evidence/steps/neighbors/p-neighbors-12613F00D/junit-summary.txt), and [F2 at `12613F00D` JUnit summary](https://github.com/nick-boss-tech/solr/blob/268dde55a9b034280616d733919d2eb1ddc40a0d/receipts/SOLR-12613-pr-head-lifecycle-round-2-evidence/steps/neighbors/f2-neighbors-12613F00D/junit-summary.txt).
- [Prior Windows browser receipt](https://github.com/nick-boss-tech/solr/blob/268dde55a9b034280616d733919d2eb1ddc40a0d/receipts/SOLR-12613-browser-verification-round-1.md) and [historical failure screenshot](https://github.com/nick-boss-tech/solr/blob/268dde55a9b034280616d733919d2eb1ddc40a0d/receipts/SOLR-12613-browser-verification-round-1-evidence/screenshots/neighbor-add-core-failure.png).

## Scope and deliverables

1. Claim this assignment before work. Check the existing SOLR-12613 C3 lifecycle assignment/report for overlap, but keep this slice bounded to the Add Core flow and the exact `testAddCoreViaUi` timeout.
2. Inspect the test, Add Core UI path, and captured failure state; determine whether there is a reproducible product defect, a test/fixture readiness problem, or an environment-sensitive intermittent failure. Do not infer the cause solely from the three pass/fail comparisons above. A narrow diagnostic reproduction may be used to settle cause; it is not a replacement gate and must not be described as clearing the failed selection.
3. If evidence supports a code/test fix, implement only that scoped change on a new ticket-keyed follow-up branch, preserve F2 and PR #5110, commit and push normally, and read back the exact SHA. If no fix is justified or the failure does not reproduce, record that result and the evidence without changing source.
4. Add `reports/solr-12613-addcore-neighbor-timeout-implementation-round-1.md` with the source paths/lines examined, exact diagnosis and limits, commands/seeds/host if a diagnostic run occurs, and any branch/head/diff. If a source change is made, post a separate exact-head gate job and obtain a fresh Linux receipt before any green/readiness claim; do not treat this assignment as authorization for phone-agent-2 to rerun the round-2 gate.

This is a new bounded implementation slice. The existing adjacent implementation and Selenium assignments remain unchanged; the current round-2 receipt remains NOT GREEN until a separate authorized gate satisfies its full proof requirements.
