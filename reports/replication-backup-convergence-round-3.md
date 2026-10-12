# Replication and backup convergence, round 3

Verdict: **NOT CONVERGED.** Both re-checked drafts (SOLR-8430 and SOLR-9091) still hold open items.

Counts: drafts re-checked 2. Items confirmed landed 2, on the gate record and the receipt: the SOLR-8430 cap and test fixture fix are gated green at `0d64c35c5a`, and the RB-R1-03 retitle is recorded as landed in the gate record (its object was not fetched, so the title was not read on the branch). Items still open: RB-R1-01 (blocking) and RB-R2-01 (should-fix) for SOLR-8430, which are now unblocked by the gate but not yet applied to the draft text; RB-R1-04 (should-fix, branch-side) for SOLR-9091; and RB-R2-02 (note), which waits on RB-R1-04. New findings RB-R3-nn: 3 (1 should-fix, 2 notes).

Scope: `assignments/pool-replication-backup-convergence-round-3.md`, checked against the round 2 register and response. One read-only subagent did the review. Nothing was edited, posted, built or fetched.

## Verdict per draft

- **SOLR-8430: NOT CONVERGED.** Head check: HOLD. The draft names `49af21be58927a62b50bd7ba1df1688110fba63e` (the OWNER NOTE line 1, the links at lines 20 and 42, and the Proof at line 26). The live tip is `0d64c35c5a7d3caffa9981fd1e1a61e1d818edf5`, the gated head.
  - The gate is closed: `gates/SOLR-8430.md` is DONE with GATE GREEN at `0d64c35c5a`, ended 2026-10-12T04:41:31Z, and `receipts/SOLR-8430.md` is refreshed at that head. The earlier `49af21be` green (2026-10-07) is superseded.
  - RB-R1-01 (blocking) is still open in the text. The draft is pre-cap: line 38 keeps "Limiters stay in memory for the life of the node, one for each rate value that a request has used." No "64" appears anywhere in the draft.
  - RB-R2-01 (should-fix) is still open in the text: line 18 lacks "for up to 64 distinct rate values", line 20 lacks ", up to 64 distinct values", and line 32 still reads "This change keeps one limiter per rate value."
  - The OWNER NOTE lines 1 and 2 still list steps that are now done on the record. RB-R3-02 replaces them.
- **SOLR-9091: NOT CONVERGED.** Head check: ok. The draft names `e6bfa5c626812f1452ed8c0158697ab8653a40d2`, which is the live tip.
  - RB-R1-04 (should-fix, branch-side) is still open. The fragment at `e6bfa5c` still reads "...fails the restore (and rolls back) instead of being switched in silently." The round 1 replacement has not landed. The code confirms it: the verify runs inside the download (lines 180 and 187), and the throw reaches `doRestore` before `modifyIndexProps`, so there is no rollback.
  - RB-R2-02 (note) is conditional and not yet due. The links at lines 15, 23 and 33 are correct for the live tip. Line 23 lacks "Only the changelog title changed after this verified run." It is due when RB-R1-04 lands.

## Carried findings

- **RB-R1-01 (SOLR-8430, blocking):** STILL OPEN in the text. The gate condition is met; the draft pass is owed.
- **RB-R2-01 (SOLR-8430, should-fix):** STILL OPEN in the text.
- **RB-R1-03 (SOLR-8430, branch-side):** CONFIRMED ON RECORD, NOT CHECKED ON THE OBJECT. The record title matches the round 1 replacement word for word. The commit objects `0d64c35c5a` and `094f0716a8a3` are not in the local object store, so the title was not read on the branch.
- **RB-R1-04 (SOLR-9091, should-fix, branch-side):** STILL OPEN (above).
- **RB-R2-02 (SOLR-9091, note):** STILL OPEN, CONDITIONAL (above).

## New findings

- **RB-R3-01 (SOLR-8430, Proof, should-fix).** Line 26 says the probe "passed at this head". The probe ran at `49af21be` on 2026-10-07. Neither gate record lists a probe run; both list only the compile-failing branch leg. When the Proof is re-pointed to the gated head, that sentence would assert an unrecorded result. Replacement for line 26: "`ReplicationRateLimiterTest` 4/4 at `0d64c35c5a7d3caffa9981fd1e1a61e1d818edf5`, verified 2026-10-12. The tests check that two streams at the same rate get one limiter, that different rates get different limiters, that rate 0 gives an unlimited limiter, and that rates past the cap of 64 get their own limiter. On the pre-cap code at `49af21be58927a62b50bd7ba1df1688110fba63e`, with the new test kept and a test-only hook added, the cap test fails at its cap assertion. The base-code leg is a compile failure, so it is inconclusive by construction. A separate behavior check, not included in this change, ran one test against the base code and passed at `49af21be58927a62b50bd7ba1df1688110fba63e` on 2026-10-07. It failed on "two concurrent streams at the same configured rate must share one throttle." It has not been run at the gated head."
- **RB-R3-02 (SOLR-8430, OWNER NOTE, note).** Lines 1 and 2 say steps (1) re-run at the new head and (2) apply the changelog title are owed. Both are done on the record. Replacement for lines 1 and 2: "OWNER NOTE (not PR text; delete before posting). HOLD. Written against `0d64c35c5a7d3caffa9981fd1e1a61e1d818edf5`. The cap and the fixture fix are gated green at this head (`receipts/SOLR-8430.md`). Before posting: (1) apply the post-cap text for RB-R1-01 and RB-R2-01, including the first Limits bullet, the 64-value sentence, the three bounded statements, and links and Proof at this head; (2) take the choice call (DISCUSS, not taken: one limiter per rate value, or one per node)."
- **RB-R3-03 (SOLR-8430, line 20, note).** "A rate of 0 still means no throttle, and unthrottled streams share one unlimited limiter." Once the map is full (64 values), a rate-0 stream would not share. Suggested: "...and unthrottled streams share one unlimited limiter until the map is full." The capped code was not checked, because its object is absent locally, so the pass should confirm it.

## Not checked

- The capped `ReplicationAPIBase.java`, the gated test file and the RB-R1-03 fragment at the gated head. The objects are absent locally, and a fetch is outside the read-only rule.
- The master receipts ledger, the takeover log and security material.

## Convergence line

NOT CONVERGED. SOLR-8430 is on HOLD, with RB-R1-01 and RB-R2-01 to apply in the draft pass that the gate now permits. SOLR-9091 waits on the RB-R1-04 retitle.
