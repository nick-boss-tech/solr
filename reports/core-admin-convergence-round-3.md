# Core admin convergence, round 3

Verdict: **NOT CONVERGED.** All three re-checked drafts (SOLR-4502, 8576 and 12916) still hold one open should-fix item each. None of the three branch or gate items has moved since round 2.

Counts: drafts re-checked 3. Items confirmed landed 0. Items still open 3 should-fix (CA-R1-01, CA-R1-24, CA-R1-34). Owed on landing: CA-R2-03 (note, SOLR-4502) and the CA-R1-21 post-fix sentence (SOLR-8576, owed together with CA-R1-24). New findings CA-R3-nn: 0.

Scope: `assignments/pool-core-admin-convergence-round-3.md`. Heads were checked with `git ls-remote origin refs/heads/<branch>`. All three match the heads the drafts name, so none is on hold. One read-only subagent did the review, and the coordinator checked the gate and fragment state it reports. Nothing was edited, posted or built.

## Verdict per draft

- **SOLR-4502: NOT CONVERGED.** CA-R1-01 (should-fix, branch-side) still open. The fragment `changelog/unreleased/SOLR-4502-create-before-load.yml` at `0ee4c644ee63` still reads "instead of creating a core that later hits a NullPointerException on search". The round 1 replacement is absent, and `gates/SOLR-4502-title-fix.md` is OPEN and unclaimed. On landing, lines 15, 21, 33 and 37 move to the new head, and the top-up gate is owed (CA-R2-03).
- **SOLR-8576: NOT CONVERGED.** CA-R1-24 (should-fix) still open. `gates/SOLR-8576-alias-fix.md` is OPEN and unclaimed, and `solr-8576-submit` is unchanged at `4c46f95c7851`. The `[OWED BEFORE POSTING]` marker still stands at line 24. CA-R1-21's post-fix sentence stays owed until the fix lands; the pre-fix sentence on line 15 is correct for the head the draft names.
- **SOLR-12916: NOT CONVERGED.** CA-R1-34 (should-fix, branch-side) still open. The fragment `changelog/unreleased/SOLR-12916-query-sender-flat-queries.yml` at `50b1f4bcab03` still names only the Config API. The round 1 replacement is absent, and `gates/SOLR-12916-title-fix.md` is OPEN and unclaimed. Commit `1a4eb16c567` is the original fragment commit, not the retitle. The changelog link on line 38 points at the live head, which is correct now and moves on landing.

## Carried findings

- **CA-R1-01:** STILL OPEN (above).
- **CA-R1-24:** STILL OPEN (above).
- **CA-R1-34:** STILL OPEN (above).
- **CA-R1-35:** CONFIRMED at `50b1f4bc`. The links to `QuerySenderListener.java` lines 108 to 110 and 118 to 119 and to the test's lines 50 to 67 match the draft.
- **CA-R1-21:** applied in its pre-fix form. The test at `4c46f95c` asserts both create messages (lines 1173 and 1182 to 1183) and checks the original collection and the absence of an alias-named collection (lines 1186 to 1188).

## Fresh-eyes checks

No new findings. Each claim below was checked against the branch code and the receipt, and each is consistent:

- **SOLR-4502:** the guard at lines 1503 to 1507 sits before `inFlightCreations.add`. The receipt counts (26 run, 0 failed, 0 errors, 3 skipped) match.
- **SOLR-8576:** the diff from the merge-base `b6b2b8f10e98` touches only the test file and the changelog, so "test-only" holds. `CreateCollectionCmd` at base lines 127 to 135 holds both messages.
- **SOLR-12916:** the diff from `cabedd1d9680` touches three files. The base loop at lines 107 to 121 matches the logged-and-skipped behavior.

## Record notes for the main side

- `receipts/SOLR-4502.md` and `receipts/SOLR-12916.md` headers still name the pre-packaging tips (`4491f5162c1d` and `ebe5db374336`) as "live tip verified 2026-10-10". The gated heads are the live tips now, so the gates cover the live state, and the header lines are stale.
- The VM2 backlog claims for SOLR-4502 and SOLR-12916 say the packaging commit is local only and not pushed. The receipts say pushed, and `ls-remote` shows the packaged heads on origin, so the receipts are current.

## Not checked

- The master receipts ledger, the takeover log and security material.

## Convergence line

NOT CONVERGED. Three drafts hold one open should-fix each: SOLR-4502 (CA-R1-01), SOLR-8576 (CA-R1-24) and SOLR-12916 (CA-R1-34). All three wait on a branch commit or a gate job.
