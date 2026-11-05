# PR 5293 childfield missing-value gate: receipt, round 1

- Gate: `gates/pr-5293-child-sort-missing-round-1.md`. Executed by: phone agent (Linux VM), 2026-11-05T02:58Z to 03:07Z, after one documented host setup repair (the two pinned commits were fetched into the local object store before any test ran; no test result comes from that first attempt).
- Candidate head: `769ca6e177d77acd3f1993a4a24bd3257ae243c0` on `pr-5293-solr-18622-child-sort-missing` (nick-boss-tech/solr). Contributor parent and live PR 5293 head: `978e2b1e352c7c39a68f7b952e6ab12eabf80890`. Both pins re-verified through the GitHub API before the run and again after it; neither moved.
- Runner: `~/workspace/tools/solr-gradle.sh` (Gradle 9.7.0, vendored JDK 21), one build at a time under the host build lock, clean standalone worktrees per leg (`5293-tidy`, `5293-base`, `5293-cand`). Seed: `18622C0DE`. The candidate test legs ran in a pristine checkout at the exact head; the tidy preflight ran in its own disposable checkout, so no result here comes from a modified tree.

## Verdict

GATE FAILED, as a factual reproduction receipt. Both runtime questions the gate posed reproduce at the candidate head: R1 and R2 below. The test patch does its job; it exposes two defects in the Lucene 10.6 code the PR upgrades to. No Solr workaround is proposed, and the contributor's deliberate null parent missing-value choice is untouched. Separately, the candidate head is not tidy-clean (one formatting join), so even the passing legs would need a tidy repair commit and a superseding gate before any clean tested-fix claim.

## Checks, independently

- Tidy preflight at `769ca6e177`: root `tidy` rc 0, but it reformats one line in `TestNestedDocsSort.java` (the `mixed.addChildDocument(...)` call at line 89 joins onto one line). Formatting-only; retained at `~/workspace/tools/gate-5293/tidy-diff.txt`. Repair requirement recorded; no commit pushed by the gate host.
- Baseline at contributor parent `978e2b1e35`, full `TestNestedDocsSort`, seed `18622C0DE`: PASS, rc 0, 11 of 11 tests, zero failures, errors, or skips (fresh XML in `gate-5293/baseline-xml/`).
- Candidate `testNumericChildMissingValues`: PASS, rc 0, 1 of 1.
- Candidate `testStringChildMissingFirst`: FAIL, rc 1. This is R2. The sort collection throws `java.lang.AssertionError` from `PackedLongValues.get` (PackedLongValues.java:119), reached through `FieldCacheImpl$SortedDocValuesImpl$Iter.lookupOrd` (FieldCacheImpl.java:892), `ToParentDocValues$SortedDVs.lookupOrd` (ToParentDocValues.java:108), called from `TermOrdValComparator$TermOrdValLeafComparator.copy` (TermOrdValComparator.java:344). The comparator asks the child doc-values for a term ordinal that is the synthetic missing-value sentinel, exactly the path the Windows report flagged at `SortedDVs.ordValue` lines 95-103.
- Candidate `testStringChildMissingLast`: PASS, rc 0, 1 of 1.
- Candidate `testFilteredChildrenDoNotSupplyMissingValues`: FAIL, rc 1. This is R1. No exception; the response order is wrong. The assertion expects parent `3` first; the actual order is `2, 3, 4, 5, 1`. The mixed parent (id 2, one valued matching child and one excluded sibling without the field) is sorted as if the excluded sibling supplied the missing value, matching the Windows report's reading of `ToParentDocValues.advanceExact` lines 310-333 (physical block size counted, only filtered valued children accumulated).
- Step 3 (full candidate suite, `TestCloudNestedDocsSort`, root `check -x test`): UNRUN. The gate runs these only if all candidate methods pass.
- Step 4 (the two `ChildFieldValueSourceParser` mutations): UNRUN, same condition. The runtime failures above are stronger evidence than the synthetic mutations would have been.
- Final remote read: fork branch still `769ca6e177`, PR 5293 still open at `978e2b1e35`.

## Attribution

Both failures sit inside Lucene 10.6 (`org.apache.lucene.search.join.ToParentDocValues` and its interaction with `TermOrdValComparator`), not in Solr wiring. The Solr side passes the configured missing values through the new Lucene constructor as the PR intends; the baseline suite passes at the contributor head; the two companion methods (numeric missing values, string missing-last) pass at the candidate head. The defects are in how the dependency counts filtered children and how it represents a missing string value during MAX selection.

## Repair recommendation

- File the two defects against Lucene with these tests as the reproduction case, or hand the reproduction to the PR author to carry upstream; the fix belongs in `ToParentDocValues`, not in this PR and not in Solr.
- The test patch is worth keeping as the reproduction vehicle: tidy-repair it (one formatting join), and it can be offered to the author as a pinned diff. Until the dependency is fixed, two of its four methods fail by design; that must be stated wherever the patch is offered, and it cannot be presented as a tested fix.
- PR 5293 itself (the Lucene upgrade) is not blocked by this gate's verdict on the evidence here: the pre-existing suite passes at its head. Whether shipping the upgrade ahead of the Lucene fixes is acceptable is the author's and reviewers' call; the childfield sort defects are edge behaviors the upgrade's new code path exposes.

## Draft author-facing result (not posted; posting needs Nick's go)

For PR 5293, as a comment:

Houston, I ran the new missing-value tests for the childfield sort against this PR's head on JDK 21 (seed 18622C0DE). The existing TestNestedDocsSort suite passes 11 of 11 at your head, and two of the four new methods pass. The other two fail inside Lucene's ToParentDocValues:

* Filtered children: a parent whose block contains an excluded sibling without the sort field sorts as if that sibling supplied the missing value (order 2, 3, 4, 5, 1 where the test expects 3 first). advanceExact appears to count the physical block while accumulating only filtered children with values.
* String missing-first, descending: collection throws from PackedLongValues.get, reached via ToParentDocValues$SortedDVs.lookupOrd from TermOrdValComparator.copy; the missing-value ordinal sentinel is passed to a term lookup.

Both look like Lucene 10.6 defects rather than Solr wiring. The test patch is on my fork as a pinned diff if it is useful as the reproduction: https://github.com/nick-boss-tech/solr/compare/978e2b1e352c7c39a68f7b952e6ab12eabf80890...769ca6e177d77acd3f1993a4a24bd3257ae243c0 (it needs one tidy formatting fix, noted in my notes). I am happy to file these against Lucene with the tests as the repro case.

## Addendum, 2026-11-05T03:19Z to 03:22Z: Lucene 10.4.0 comparison (at Nick's direction)

The same four candidate methods were run at the PR base `07ccc73cd24e` (which pins Lucene 10.4.0) with only the test patch applied (the two test files; no production changes), same seed `18622C0DE`, clean worktree `5293-lucene104`, logs and XML under `~/workspace/tools/gate-5293-lucene104/`. Results split the verdict:

- `testFilteredChildrenDoNotSupplyMissingValues` (R1): PASSES on 10.4.0, fails on 10.6.0. A genuine regression in the upgraded stack: a query that orders correctly today orders wrong after the upgrade.
- `testStringChildMissingFirst` (R2): FAILS on 10.4.0 too, but on ordering (actual order 3, 4, 5, 1, 2; the missing parent sorts last although missing-first was requested), never with the exception. Not an outcome regression; the case never worked. The 10.6 code changes its failure mode from silently wrong order to a thrown exception during collection.
- `testNumericChildMissingValues`: FAILS on 10.4.0 (same wrong order, missing parent last) and PASSES on 10.6.0. The upgrade plus the new missing-value wiring fixes this case.
- `testStringChildMissingLast`: passes on both.

Context for the split: on 10.4.0 the Solr parser never passed a child missing value into the join sort (the old `ToParentBlockJoinSortField` constructor has no missing-value inputs); PR 5293's parser change wires `missingValue` in. So the 10.4 failures above are the old stack ignoring the requested missing-value placement, while R1's flip from pass to fail is the one case where behavior that was correct becomes incorrect.

## Evidence on the gate host

- Logs and fresh JUnit XML for every executed leg: `~/workspace/tools/gate-5293/` on the phone agent host (`master-a.log`, `tidy-preflight.log`, `tidy-diff.txt`, `baseline.log`, `baseline-xml/`, `cand-<method>.log`, `cand-<method>-xml/`).
