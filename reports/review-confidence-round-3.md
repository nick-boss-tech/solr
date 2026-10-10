# Review confidence round 3: roll-up

Assignment: `assignments/pool-review-confidence-round-3.md`. Claim: `claims/pool-review-confidence-round-3.md`. Lead: the windows review agent. Part reports: `reports/review-confidence-round-3-a1.md` and `-a2.md` (slice A), `-b.md` (slice B), `-c.md` (slice C). Four subagents, all read-only.

No PR edit, branch edit, comment or build. Verified before writing: PR #5034 (cited by #5061) is CLOSED, and #5012 is MERGED.

## Verdict counts

| Slice | Scope | Counts |
|---|---|---|
| A1 | live PRs #4968, #4997, #5027, #4998, #5000, #5004, #5009, #5011, #5012 | CONSISTENT 2, DRIFT 7, UNREAD 0 |
| A2 | live PRs #5014, #5015, #5016, #5028, #5029, #5030, #5031, #5061, #5062 | CONSISTENT 0, DRIFT 9, UNREAD 0 |
| B | 29 VM2 gate jobs in the round 1 and round 2 backlogs | CLEAR 12, FIX 17 |
| C | SOLR-16630, SOLR-12651, SOLR-17987 | DRIFT 2, no draft 1 |

Slice A totals: 2 CONSISTENT and 16 DRIFT across 18 live PRs. Slice C: both drafts that exist have drift, and the third ticket has no draft on the tip.

## Slice A1 (PRs #4968 to #5012)

Heads match the fork tips for all nine PRs. All eight receipts are on the tip, except that #4997 has no receipt for its head. No CI failures, pending or action_required checks were recorded for these PRs.

DRIFT: #4968, #4997, #5027, #5000, #5004, #5009, #5012.

- **#5012 is MERGED.** The receipt still says awaiting merge. This is a main-side record fix.
- **#4997** has no receipt for its current head.

The per-PR lines, facts and fixes are in `reports/review-confidence-round-3-a1.md`.

CONSISTENT: #4998, #5011.

## Slice A2 (PRs #5014 to #5062)

All nine are DRIFT. Main causes:

- Seven bodies (#5014, #5015, #5016, #5028, #5029, #5030, #5031) have no bold one-line summary for each section, which the formula requires.
- Many Proof numbers are not in the receipts. #5016's receipt is stale at 3 of 3.
- #5030 has 25 of 31 links at `100ad2e0df69`, not the live head. #5031 has 8 links at `caf3dbf4d8db`.
- #5062 links an internal review record at another SHA and uses bare Lucene PR numbers.
- #5015's title hides the wider serialization change.
- #5061 calls #5034 a live route. **#5034 is CLOSED** (verified by the lead).
- #5029's CI run failed at its head, in `GCSInstallShardTest`, which is not this PR's test. The failure is a state to report, not a code finding about #5029.

The per-PR lines and fixes are in `reports/review-confidence-round-3-a2.md`.

## Slice B: VM2 gate job files

All 29 jobs have heads that match the gate file, the backlog and the receipt, and each head is on the fork. No head mismatch was found.

CLEAR (12): SOLR-4502, SOLR-9091, SOLR-9382, the 9865 and 17287 recount, SOLR-10390, SOLR-13705, SOLR-9852, SOLR-10882, SOLR-3498, SOLR-10364, SOLR-12347, SOLR-12161.

FIX (17), as job-file fixes for the main side. The exact lines and corrections are in `reports/review-confidence-round-3-b.md`.

- **SOLR-12998.** The backlog says claimable; the job is BLOCKED.
- **SOLR-18391.** The backlog says claimable; the job is DONE with a proof mismatch.
- **SOLR-5011.** The proof revert shape breaks compile.
- **SOLR-12916.** No round-trip vehicle.
- **SOLR-16499.** The proof reverts a file the test needs.
- **SOLR-5262 premise.** No vehicle to show the premise.
- **SOLR-11650 base run.** Must revert three production files.
- **SOLR-11356.** The run class is not named.
- **SOLR-14187.** The base cannot compile.
- **SOLR-10667.** No task or base is named.
- **SOLR-11678.** Module text, and base compile.
- **SOLR-6430, SOLR-7119.** Named in the report.
- **SOLR-11700.** The docs task is not named.
- **SOLR-17356.** Wording; the expected outcome is NO GATE.
- **SOLR-16322.** No failing vehicle.
- **SOLR-17722.** The worktree is not the content type; the mock test is not the premise.

No gate runs under this round, so none of these jobs has been rerun.

## Slice C: near-opening drafts

- **SOLR-16630** (`pr-drafts/flaky-fixes/SOLR-16630.md`): DRIFT. The branch changelog title overstates the two-minute fallback. Retitling it moves the head, so the 13 SHA uses in the draft need re-heading. Remove the seeds and the word "focused" from the Proof (lines 27, 29, 39). Fix "fixed time ... chosen at random" (line 11) and "on record" (lines 9, 31). Confirm "reopened" in Jira, since the ticket text is not in the workspace.
- **SOLR-12651** (`pr-drafts/solrcloud/SOLR-12651.md`): DRIFT. Attribute the Choice to Tomas Lobbe and add the "optional, even as default" option (line 29, from the local Jira snapshot). Change the Proof opener (line 21) to "with RestoreCmd.java reverted". The branch changelog title overstates (lines 2 and 3); fixing it moves the head.
- **SOLR-17987**: no draft exists on the tip. The premise of this slice is wrong. Drop it, or mark it "no draft yet".

Character counts (link-stripped): SOLR-16630 2,894; SOLR-12651 2,738. Both are within the formula's guide.

## Not done

No PR body edit, comment, review, close, branch edit or Jira write. No build, Gradle, test or gate run. The lead did not change any draft or job file. Every finding goes to the main side.
