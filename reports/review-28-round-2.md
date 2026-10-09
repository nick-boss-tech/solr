# Review round 2 for the 28 update-processing PRs: roll-up

Assignment: `assignments/review-28-round-2.md` (commit `db860669c5b`). Claim: `claims/review-28-round-2.md` (commit `0870cb8f092`). Group reports: `reports/review-28-round-2-g1.md` to `reports/review-28-round-2-g7.md`. Read only: nothing was edited, committed, or posted to GitHub in this round. Exact replacement texts are in the group reports; this roll-up lists each one by reference.

Done 2026-10-09 by Claude Code (AI agent), working for Nick Shanin. Seven subagents checked four PRs each, against the final drafts at `origin/pr-prepare` (`0d23d001594`) and the live bodies on apache/solr. The lead merged the group reports.

## Verdicts

**Ready to flip (8):** 3657 (PR 5069), 6065 (5078), 11475 (5082), 12245 (5084), 12864 (5086), 14262 (5090), 16356 (5092), 16910 (5095).

**Ready, the owner's call to flip (1):** 7504 (5081). The null-count fix at `2fe06bfd917f` matches its receipt. The receipt's History line gives "25 and 43 tests" for `22b77196e662`, where the count read 29 and 42. Reconcile the receipt.

**Ready once the owner accepts the length (1):** 5065 (5071). The draft is 3,901 characters.

**Body ready, owner call on the title (2):** 13696 (5088), 14718 (5091). See the title items below.

**Not ready, a fix is needed (16):** 4841 (5070), 5505 (5072), 5754 (5073), 5887 (5074), 5939 (5075), 5941 (5076), 6045 (5077), 6973 (5079), 7022 (5080), 11483 (5083), 12703 (5085), 13265 (5087), 13943 (5089), 16655 (5093), 16673 (5094), 12705 (5096).

## Fixes, by kind

**Proof and text that the record does not support.**
- 5754 and 5939 (our correction round's error): the merged tree is labelled "merged tree 1ddbf36202d". `1ddbf36202d` is a merge commit, and its tree is `f2e33340f0ea`. Restore the commit-and-tree wording in both. 5754 also has a root-file sentence that names an internal file; optional.
- 5887: the bold line under "What this change does" claims every path. Scope it to the add-command path, as the group report gives.
- 5941: the sentence that the base was not run was dropped. Restore it, as the group report gives.
- 4841: the bold Proof line says the new test "fails on base". The receipt and the live body say it does not compile. Use the group report's replacement.
- 7022: the bold Proof summary says the fix passes its tests, which overstates it. The "commit-level test" in Limits refers to `DirectUpdateHandler2CommitWaitTest`, which Proof does not name. Use the group report's replacements 4 and 5.
- 13943: the sentence "This failure is pre-existing, and this change does not cause it" goes beyond the record. Delete it, with " This change does not fix that test."
- 6973: Limits names a gap with no planned follow-up. Add the group report's replacement 1.
- 16655: the planned follow-up sentence is missing from Limits. The group report gives the sentence.
- 16673: the Schema Designer gap has no planned follow-up sentence. The group report gives it.
- 5505: two gaps in Limits have no follow-up sentence. The group report gives the proposed sentence.
- 5065: Limits needs the planned follow-up sentence, which was added. Two bullets (test coverage and reference guide) have no follow-up sentence. Owner call.

**Branch content, not the description.** These need a change on the submit branch and a re-gate. This review does not make them.
- 6045 (5077): the branch changelog states the mixed-field rule without the merger-path scope. Re-gate needed.
- 12703 (5085): the branch changelog says "confusing RunUpdateProcessor failure", which round 1 flagged as unsupported. Re-gate needed.

**Titles.** The owner ruled that titles must be accurate. These are inaccurate on their face.
- 11483 (5083): the live title reverses the condition. Use the group report's replacement.
- 13265 (5087): "no logs" does not describe the change. Use the group report's replacement.
- 5939 (5075): the title names `StreamingSolrServer`, which no code contains. Use the group report's replacement.
- 14718 (5091): "Multiple flaws" describes the ticket, not this one-flaw change. Owner call on the proposed title.
- 13696 (5088): the title names only commitWithin, and omits the two fixes. Owner call on the proposed title.
- 5887 (5074) and 5941 (5076): the titles are owner calls, with replacements in the group reports.
- 5094, 6973: optional accuracy changes, with replacements in the group reports.

## Owner calls and discrepancies

1. **Two live titles changed without a record.** SOLR-12245 (5084) now reads "Name the replica in failed distributed update errors", and SOLR-12864 (5086) now reads "Add test coverage for echo with mapUniqueKeyOnly in JSON updates". The owner's rulings name four title fixes, and these two are not among them. Confirm these were intended, and record them.
2. **Follow-up commitments.** The ruling says 22 drafts carry a planned follow-up sentence. A count finds 20. SOLR-5065, 12705, 14718 and 14262 carry one, although the ruling says those four carry none. Decide which way to go: keep the four sentences, or remove them.
3. **SOLR-12705 follow-up sentence.** Its Limits sentence says a follow-up submission is planned, which conflicts with the ruling. The group report recommends deleting it. Confirm the deletion.
4. **Length.** 4841 (3,862 characters) and 5065 (3,901) are not among the seven long drafts the owner accepted. Trim or accept.
5. **SOLR-13943 follow-up gaps.** Its Limits gaps have no planned follow-up. The ruling does not list 13943 among the 22 ratified drafts. Owner call.
6. **SOLR-7504 flip.** The fix is in and the body is ready. The flip is the owner's call.
7. **Branch changes.** 6045 and 12703 need a changelog change and a re-gate. The null-count defect is already fixed in 7504.

## Not done

Nothing was edited, committed, or posted to GitHub in this round. No PR, comment, title, label, or submit branch was changed. No build, Gradle run, or test was run.
