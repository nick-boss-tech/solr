# Plain-language check, drafts set A (review-plain-language-28-a)

Read-only check of 12 drafts. Previous version: `bc61a3ce3d3`. Current version: `HEAD` (004575c037b). Receipts read at `HEAD`.

Method: checks 1 and 2 by diff and a scan of heads, dates and counts. Check 3 by `git cat-file -e` on every distinct head in each current draft. Check 4 by term search. Check 5 compares each Proof sentence that reports a run, a read, or a comparison against the ticket receipt. Descriptions of what a test checks are code claims that receipts do not record, so they are not flagged.

## SOLR-3657
1. None dropped (14edaca577c0, full form kept).
2. 17/17, 11/11, 2 failures, 2026-10-07 kept. The word "PASS" was dropped; no count changed.
3. Resolves.
4. None.
5. None.

## SOLR-4841
1. None dropped (b6b2b8f10e9827e3, e4c878627108, f8850ffd421).
2. 1/1, 13/13, 13/13, 4/4, 2026-10-07 kept. "fails" became "does not compile"; the receipt records a compile failure, so this is accurate.
3. All resolve.
4. None.
5. None. "checked directly" for e4c878627108 matches the receipt's "read file by file".

## SOLR-5065
1. None dropped (0cc328310f8, ab894a996c8a).
2. 44/44, 2/2, 2026-10-09 kept.
3. Resolves.
4. None.
5. None.

## SOLR-5505
1. None dropped (44c444aa5cd3, full form).
2. "exactly 1 failure", 2/2, 2/2, 2026-10-07 kept.
3. Resolves.
4. None.
5. None.

## SOLR-5754
1. None dropped (7fbe0128d8b0, 46b919e2d4e8).
2. 26 of 26, 19/19, 2/2, 2026-10-08 kept. "GREEN" dropped.
3. 7fbe0128d8b0 and 46b919e2d4e8 resolve. The merged tree 1ddbf36202d is prose, not a link target, and does not exist in this clone (unchanged from previous).
4. None.
5. FINDING (pre-existing): head 7fbe0128d8b0, the combined-run head, appears in no receipt. The receipt names only merged tree 1ddbf36202d. "checked directly" for the 46b919e2d4e8 removal is supported by the receipt's tree comparison.

## SOLR-5939
1. None dropped (f8d4bdbea518 both forms, 46b919e2d4e8, 7fbe0128d8b0, 1ddbf36202d).
2. 26 of 26, 2026-10-08, and the 26-test breakdown kept.
3. Heads resolve. 1ddbf36202d as in SOLR-5754.
4. None.
5. FINDING (pre-existing): head 7fbe0128d8b0 is in no receipt, as in SOLR-5754. Other Proof sentences are supported.

## SOLR-5941
1. None dropped (62516cc338ef, a4df7bfd214b, cabedd1d968).
2. 2/2, 1/1, 1/1, 1/1, 2026-10-09 kept. "Pre-fix proof passed" became "Without this fix ... fails at head 62516cc338ef"; the receipt supports this.
3. Resolves.
4. None.
5. MINOR (pre-existing): "This was checked by reading the base code." The receipt does not mention a reading check.

## SOLR-6065
1. None dropped (3d2cec9e1ab3).
2. 1 of 1, 8 of 8, 2026-10-09, and the 2026-10-06 date in the Choice section kept.
3. Resolves.
4. None.
5. None.

## SOLR-6973
1. None dropped (fa5b59ba07b4, full form).
2. 7 of 7, 2026-10-08 kept.
3. Resolves.
4. None.
5. FINDING (pre-existing, unchanged this pass): "The new test fails on the base code (by reading)" and "By reading, the new test is the one that breaks." The receipt records a base run: "On base production the class runs 7 tests with 1 failure." It does not name the failing test or mention reading.

## SOLR-7022
1. DELIBERATE DROP: 6a233ab2fdb was removed with its sentence (owner ruling, item 3). Its link target db357868610b stays elsewhere. FLAG: `git show --stat 6a233ab2fdb` adds `DirectUpdateHandler2CommitWaitTest.java` (162 lines, a new test class). The receipt's top-up entry says the gate-to-tip range changes only text and lists 6a233ab2fdb in it. The receipt is wrong on this point, and the public text no longer discloses the added class.
2. The "2026-10-05 run" date went from 2 occurrences to 1 (the removed sentence). 2 of 2 and 7 of 7 kept. "`:solr:core:check -x test` returns 0" became "the module checks pass"; the receipt shows rc=0, so this is supported.
3. 5b822b7e8e9, a2ed957063b, db357868610b resolve.
4. None.
5. "The two last commits change wording only" matches the receipt's list of text changes, but it does not cover 6a233ab2fdb (see 1).

## SOLR-7504
1. None dropped (22b77196e662, 2fe06bfd917f, 97d973814336).
2. 30 of 30, 0 failures, 1 failure in 30, 42 of 42, 2026-10-09 kept.
3. Resolves.
4. None.
5. None.

## SOLR-11475
1. None dropped (0de48e492fd4).
2. Five classes at 1 of 1, 2026-10-08 kept.
3. Resolves.
4. None.
5. None.

## Summary
Drafts with any finding: SOLR-5754 and SOLR-5939 (check 5, head 7fbe0128d8b0 in no receipt, pre-existing); SOLR-5941 (check 5, minor, pre-existing); SOLR-6973 (check 5, pre-existing); SOLR-7022 (checks 1 and 5: deliberate drop, but the receipt contradicts git).

Observation, not a finding: "live tip" appears in SOLR-4841, SOLR-5754, SOLR-5939 and SOLR-7022. It is not on the owner's term list.

Process note: temporary extracts were written to the Temp folder and deleted. No draft, branch, PR or receipt was changed.
