# Highlighting round 1, group 2 report

Scope: SOLR-3704 and SOLR-4540 (drafts), SOLR-16885 (audit only). Worktree HEAD 63128a22a5f. Nothing was committed, pushed, or changed on any branch. No builds, Gradle, tests, PR or comment writes. Read-only git only.

## Live tips (git ls-remote, this run)

- solr-3704-submit: de63d4e5d5d1ddde0da6a100a631e254c078bd55. Matches the claim.
- solr-4540-submit: 62c06439fb0653b299ec54f110e4f94e420cea9b. Matches the claim.
- solr-16885-submit: 2b9b80119a92d787970b02b1472bfea6d2fa3613. Matches the claim.
- Merge-base with upstream/main (8e62c2686882) is cabedd1d968 for all three. None of the files these branches touch changed on upstream since that merge-base (37 commits checked).

## SOLR-3704: DRAFTED at de63d4e5d5d1ddde0da6a100a631e254c078bd55

- Draft: pr-drafts/highlighting/SOLR-3704.md (about 2,760 visible characters).
- Changelog fragment: changelog/unreleased/SOLR-3704-highlight-date-point.yml (on the branch, unchanged).
- Receipt check: head, HighlighterTest 36 of 36, the base failure (1 failure, Invalid Date String:'1343779201999'), and the extra runs (LukeRequestHandlerTest 8 of 8, TestPointFields 104 of 104) all match the receipt. No disagreement.
- The receipt names no verification date for the gate run. The draft omits the date. Add one only from g3704-gate.log, which I did not find in the workspace.
- Limits: the ledger review notes for 3704 were not found (searched research/ for the ticket and for "ledger"; only PARKED.md lists it). The draft's Limits come from the diff: the docValues-only Date branch is untested, date unique keys are untested, and UnifiedHighlighter is unchanged.
- Code reading, not a run: the test field is stored. The Date branch covers non-stored docValues date fields (SolrDocumentFetcher decorate path). The record does not show whether both changes are needed for the new test to pass.
- Changelog title says "(or a Date.toString())". The record does not show that base case failing. Consider trimming.
- No "choice to check" section. A possible live alternative: format in the highlighter only and leave DatePointField.toExternal alone, which avoids the Luke and unique-key output change. No design note for 3704 was found to show it was considered.

## SOLR-4540: DRAFTED with one OPEN item, at 62c06439fb0653b299ec54f110e4f94e420cea9b

- Draft: pr-drafts/highlighting/SOLR-4540.md (about 2,400 visible characters). Not postable until the OPEN line is filled.
- OPEN: the base-code result for FastVectorHighlighterTest.testFieldAbsentFromDocIsSkipped. The receipt says "premise grounded by run" but records no base failure text and no counts. The draft states no fail-before result.
- Code reading, not a run: on base, each absent field still reaches getSolrFragmentsBuilder (DefaultSolrHighlighter L641; the throw is at L415-L421). An unknown builder name should therefore return 400 on base. That fits the test design, but it is not a record.
- Changelog fragment: changelog/unreleased/SOLR-4540-fvh-skip-absent-fields.yml. Its title says the change fixes wildcard hl.fl slowness. No timing exists for this change. Reword the title or add a timing run (lead's call).
- The receipt's GitHub run 37622803991 has no URL in the receipt, so the draft gives no link. The draft lists it as a successful run with no count.
- Behavior change stated in the draft: a bad fragmentsBuilder name no longer fails a request for a field a document lacks.

## SOLR-16885: AUDIT ONLY (no draft)

- Head matches the live tip. The branch matches the receipt: no gate, no run, and SOLR-16885-TESTING.md is still at the repo root (added in 2b9b80119a9). No disagreement.
- Branch claim (code): SolrExtendedUnifiedHighlighter.getOffsetSource (UnifiedSolrHighlighter.java L274-L288) returns ANALYSIS for a field that Lucene would read from term vectors when the schema field lacks termPositions. An explicit hl.offsetSource still wins. Schema adds text_tv_offsets (schema-unifiedhighlight.xml L40) and text4 (L53). Test: TestUnifiedSolrHighlighter.testTermVectorOffsetsWithoutPositions (L85).
- Note claims: hypothetical and unrun. The exception may not reproduce on the current Lucene. If it does not, the test passes without the change and the branch is a pin only. The Lucene quote about TERM_VECTORS is not checkable here: the Lucene UnifiedHighlighter source is not in this checkout.
- A premise run (main side) must show, on upstream main plus only the branch test file and schema: testTermVectorOffsetsWithoutPositions fails with the ticket's IndexOutOfBoundsException (or misses the two em tags). With the change applied, the test passes and highlights "crappy" and "document". If the base passes, the bug does not reproduce, and the result is NOT_PROVEN.
- Decision point for later: the ticket says "not likely a fix in Solr" and the workaround is a schema change plus reindex. The branch changes the default for every term-vector field without positions, which changes passages and speed for those fields (the note itself says analysis is slower). A maintainer could reject that, so it is a real choice once the premise is known.
- Hygiene before any PR: SOLR-16885-TESTING.md must not ship. The changelog title is a folded scalar (">"), so check it against sibling fragments.

## Owner decisions

1. 3704: add a choice section (highlighter-only versus the DatePointField.toExternal change), or leave it out.
2. 3704: keep both changes, or is the toExternal change alone enough? Needs a main-side run.
3. 4540: reword the changelog title to match the proof, or add a timing run.
4. 4540: fill the OPEN base-result line from the run record before posting.
5. 16885: after the premise run, decide whether a Solr-side workaround is wanted, given the ticket's "not likely a fix in Solr".

## Receipt disagreements

- 3704: none on heads or counts. The verification date is not named.
- 4540: the receipt does not record the base failure, and "gate green" lists no checks. The draft holds a placeholder and claims no checks.
- 16885: none.
