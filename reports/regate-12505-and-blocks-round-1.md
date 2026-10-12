# SOLR-12505 re-gate check and remaining internal blocks, round 1

Verdict. The SOLR-12505 draft is HOLD AT CURRENT HEAD. Its named head `5f20e1171bc8` equals the fork tip and the vm2 claim head. The gate will move the head, the Proof counts and the new-test sentence, and it gives no replacement text yet. Seven owner-note blocks were moved from draft bodies to the internal-blocks report, verbatim, and removed.

Inputs: `assignments/pool-regate-12505-and-blocks-round-1.md`, claim `claims/pool-regate-12505-and-blocks-round-1.md`. One read-only subagent did the SOLR-12505 check. The review agent did the block removals.

## SOLR-12505 (`pr-drafts/streaming/SOLR-12505.md`): HOLD AT CURRENT HEAD

Head and links.
- `5f20e1171bc8` (lines 17, 23, 25, 43) equals the claim head and the fork tip.
- `14c7aac0d151` (lines 9, 11, 31, 41) is the base merge-base. It does not move at the re-gate.
- Every GitHub link path exists at the cited head. The line ranges match the code they cite.
- The JIRA links were not checked, because this pass has no network access.

Production delta. The claim says "FetchStream.java only, plus one new test in StreamDecoratorTest". `git diff --numstat` against the base shows three files: `FetchStream.java` (+3), `StreamDecoratorTest.java` (+64, including two imports and the new test) and a new changelog fragment, `changelog/unreleased/SOLR-12505-fetch-stream-deftype.yml` (+9). The claim omits the changelog fragment. The draft links the fragment at line 43 but has no sentence about it.

Sentences the gate changes. The claim and the job give instructions, not replacement text.
1. Line 25, the Proof head, counts and date. The current sentence reads "With this change, StreamDecoratorTest runs 56 tests, 5 skipped, 0 failures at head `5f20e1171bc8`, verified 2026-10-04." Counts must come from fresh JUnit XML at the new head. The date must change with the run.
2. Lines 17 and 23, the code links. The fix adds lines, so the line ranges move. Re-point them at the new head and check the ranges there.
3. Line 43, the changelog link. Re-point it. The fragment text is unchanged.
4. Line 21, "The new test fails on the base FetchStream and passes with this change." The job's proof baseline is the pre-fix head, not the merge-base, so this sentence needs re-checking against the new run.
5. Missing text. The job requires a sentence in "What this change does" for the route the fix takes. The route is not chosen yet, so the sentence cannot be written.
6. Lines 29 to 33 ("A choice to check") and line 37 ("Covered") may need review once the route is in.

Job state. `gates/SOLR-12505-fetchstream-fix.md` line 5 still says "OPEN, unclaimed". The claim (`claims/pool-vm2-gate-backlog-12505.md`, heartbeat 20:35Z) says "CLAIMED by vm2". The job file was not updated. The main side should reconcile it.

## Internal-looking text, not moved

The subagent flagged two kinds of text in the SOLR-12505 body:
- Line 41, "It has not been rebased." This is a branch-status sentence in outbound prose. It is not an owner note, so it stays for now. The author should decide whether a reader needs it once the gate has landed.
- Lines 27 to 33 and 45 to 47 (the "A choice to check" section and the AI assistance paragraph). These are outbound prose and stay.

## Internal blocks removed

Seven owner-note blocks were removed. Each is copied verbatim into `reports/internal-blocks-round-1.md`:
- `query-parsing/SOLR-11391.md` (lines 1 and 2, "HOLD: not for posting").
- `query-parsing/SOLR-12608.md` (lines 1 and 2, "HOLD: not for posting").
- `search-components/SOLR-15319.md` (lines 49 to 63, "REVIEWER NOTES").
- `search-components/SOLR-15331.md` (lines 39 to 48, "Owner notes, not for posting").
- `search-components/SOLR-17051.md` (lines 54 to 63, "Owner notes, not for posting").
- `search-components/SOLR-17155.md` (lines 21 and 22, "Owner to confirm the options").
- `search-components/SOLR-6831.md` (lines 41 to 50, "NOT FOR POSTING").

Each file still ends with its AI assistance section.

## Still open

- `search-components/mergeids-skipped-shard-totals.md` has no ticket number yet, so it is not in this round.
- SOLR-12505 waits for the gate and for the route decision.

## Not done

- No builds, tests, Gradle runs, PR or comment writes, or gate-job edits.
- No JIRA check.
