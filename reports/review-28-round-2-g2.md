# Review round 2, group 2: SOLR-5754, SOLR-5887, SOLR-5939, SOLR-5941

Checked 2026-10-09 by Claude Code (AI agent), working for Nick Shanin. Read only. Final drafts read from `origin/pr-prepare` at `pr-drafts/update-processing/`. Receipts read from `receipts/`. Live PR state read with `gh pr view` (no writes). No builds, no Gradle, no tests. Nothing was edited, committed, or posted.

## Summary

| Ticket | PR | Verdict | Remaining item |
|---|---|---|---|
| SOLR-5754 | 5073 | Not ready | Proof tree label (regression from the corrections round) |
| SOLR-5887 | 5074 | Not ready | Bold claim not scoped to the add path; title (owner call) |
| SOLR-5939 | 5075 | Not ready | Title names `StreamingSolrServer`, which no code has; tree label; "tip" wording |
| SOLR-5941 | 5076 | Not ready | Base-not-run sentence missing from Proof; title class name (minor, owner call) |

Live bodies: all four are byte-identical to the final drafts. All four heads match their receipts. All four PRs are drafts (`isDraft` true).

Every proposed title change is a public write and needs owner ratification.

---

## SOLR-5754 (PR 5073)

Verdict: **not ready. One item: the Proof tree label (replacement 1).**

Part 1, round 1 findings and corrections:
- Title (round 1 finding 1): applied differently. Live title is `SOLR-5754: StreamingSolrClients.getErrors() returns a snapshot copy instead of the live list`. It names a real class and matches the change. Accepted under the owner's accuracy ruling.
- Proof run count (round 1 finding 2): applied as specified in substance. The draft now reads "It passed 2 of 2 in the same combined run." The SOLR-5939 receipt records StreamingSolrClientsTest 2/2.
- Regression, not in round 1: the corrections commit (`f49a194dd02`) changed "on a local merge commit ... `1ddbf36202d`, whose tree is `f2e33340f0ea`" to "with merged tree `1ddbf36202d`". `1ddbf36202d` is a commit id, not a tree. Tree `f2e33340f0ea` exists in the clone. The draft now gives the wrong kind of id for the tree.
- Bold summary: present in all four sections. Follow-up sentence: present ("A follow-up submission is planned to test that window.").
- Internal vocabulary: no hits for gate, receipt, ledger, JUnit XML, rc=, takeover, live tip, pre-fix proof. Line 26 names an internal file and says it was "checked directly" (optional replacement 2).
- Part 2, disposition 5 ("live tip" replaced with "current head" phrasing): applied.

Part 3 items:
- Replacement 1 (Proof, the combined-run bullet, currently line 24). Replace the whole line with:
  `- Combined run, 2026-10-08, at head `7fbe0128d8b0`: 26 of 26 tests pass, on a local merge commit of SOLR-5939 and this change, `1ddbf36202d`, whose tree is `f2e33340f0ea`.
- Optional replacement 2 (Proof, currently line 26). Replace the whole line with:
  "- The current head `46b919e2d4e8` differs from `7fbe0128d8b0` only by the removal of one root file that is not part of the change."
- Owner decision: the body is 3,762 characters, over the 3,500 guide. It is not one of the seven long drafts the owner accepted. Accept or trim.

Body: byte-identical to the draft. Head `46b919e2d4e8` matches the receipt. Title as above.

---

## SOLR-5887 (PR 5074)

Verdict: **not ready. Two items: the bold claim in "What this change does" (replacement 3, required) and the title (replacement 4, owner call).**

Part 1, round 1 findings and corrections:
- Wrapper sentence (round 1 finding 2, first sentence): applied as specified. The add paths in `AddUpdateCommand` now go through the wrapper.
- Classification bypass (round 1 finding 2, second sentence; owner ruling O8): applied differently. The sentence moved from "What this change does" to Limits, with a planned follow-up. This matches O8, and the bypass is named in Limits.
- Limits test list (round 1 finding 3): applied as specified. `DocumentBuilderTest` is removed from the list. A separate bullet records 17 of 17 on 2026-10-07, which matches the receipt.
- Scoped claim: the bold line under "What this change does" reads "The error text gets a `core <name>:` prefix." It does not carry the add-path scope that O8 requires. The body scopes it, but the bold line is the claim a reader takes away.
- Title (round 1 finding 1): not applied. The owner ruling left 24 titles standing. The live title, `SOLR-5887: Document exception don't give core information`, describes the old behavior. After the change, add-command errors name the core and the classification path does not.
- Follow-up sentences: both Limits gaps carry one.
- Internal vocabulary: no hits.

Part 3 items:
- Replacement 3 (bold line under "What this change does"). Replace with:
  `**On add commands, a bad-document error gets a `core <name>:` prefix.**`
- Replacement 4 (title, owner call). Replace the live title with:
  `SOLR-5887: Document errors from add commands now name the core, except in the classification processor`
- Owner decision: the body is 3,768 characters, over the guide, and not among the seven accepted long drafts. Accept or trim.

Body: byte-identical to the draft. Head `c4c57ef7bcbd` matches the receipt.

---

## SOLR-5939 (PR 5075)

Verdict: **not ready. Three items: the title (replacement 5, required under the owner's accuracy ruling), the Proof tree label (replacement 6), and the "tip" wording (in replacement 6).**

Part 1, round 1 findings and corrections:
- Title (round 1 finding 1): missing under the owner's O1 ruling. The live title, `SOLR-5939: Wrong request potentially on Error from StreamingSolrServer`, names a class that does not exist. Neither base `cabedd1d968` nor `upstream/main` contains `StreamingSolrServer`. The owner's ruling fixed SOLR-5754 for the same defect, so this title should be fixed on the same rule.
- "Before, the map kept them" (round 1 finding 2): applied differently. The draft now reads "The map is new in this change ([L59-L60]), and a request enters it when it is handed to a client ([L78])." The false comparison is gone. The claim checks out against the head anchors in the group 2 round 1 report.
- Combined-run head (round 1 finding 3): applied as specified in substance. The Proof now names SOLR-5754 at `7fbe0128d8b0`. The tree label regression from the corrections round is repeated here (replacement 6).
- Part 2, disposition 5: "tip" remains in "The SOLR-5754 tip `46b919e2d4e8`". The disposition asks for "current head" phrasing. Applied differently (replacement 6).
- Follow-up sentences: both Limits gaps carry one.
- Internal vocabulary: no hits. "delegate" is the ordinary verb and is not a hit.

Part 3 items:
- Replacement 5 (title, required). Replace the live title with:
  `SOLR-5939: A failed merged update stream now records its error against each request in the stream`
- Replacement 6 (Proof, the combined-run bullet, currently line 36). Replace the whole line with:
  "- The combined run (verified 2026-10-08) passed 26 of 26 focused tests on a local merge commit of this head with SOLR-5754 at `7fbe0128d8b0`. The merge commit is `1ddbf36202d`, and its tree is `f2e33340f0ea`. This head `f8d4bdbea518` is the current head and the head the combined run tested. The current SOLR-5754 head `46b919e2d4e8` only removes a root file. The 26 tests are:"
- The body is 7,540 characters. It is one of the seven long drafts the owner accepted.

Body: byte-identical to the draft. Head `f8d4bdbea518` matches the receipt.

---

## SOLR-5941 (PR 5076)

Verdict: **not ready. One item: the base-not-run sentence in Proof (replacement 7). The title class name is a minor owner call (replacement 8).**

Part 1, round 1 findings and corrections:
- Title (round 1 finding 1): not applied. The live title, `SOLR-5941: CommitTracker should use the default UpdateProcessingChain for autocommit`, is accurate in meaning. "UpdateProcessingChain" is not a class name. The class is `UpdateRequestProcessorChain` (imported by `CommitTracker.java`). This is a minor accuracy point under the owner's ruling.
- tidy and Error Prone (round 1 finding 2): applied as specified. Both are removed. The line now reads "The module checks pass at this head." The receipt records module check rc=0.
- Pre-fix result (round 1 finding 3): applied differently. The draft reads "Without this fix, the new test fails at head `62516cc338ef`." The head matches the receipt. The gate label and parent-head detail were dropped, as the plain-language ruling requires. The sentence "Base `cabedd1d968` was not run" was also dropped. Without it, a reader can take the Proof as a base run.
- Five-class not-re-run list (round 1 finding 4): applied as specified. The line is deleted.
- Anchor (round 1 finding 5): applied as specified. The link is `#L71-L112`.
- Limits follow-up (round 1 note): applied under O2. The line reads "A follow-up submission is planned to cover it."
- Part 2, disposition 6: applied. "Checked by reading the base code" is gone. Method phrases the receipt does not record are removed. The run claims match the receipt: AutoCommitUpdateChainTest 2/2, CommitThroughNonLeaderTest 1/1, ParallelCommitExecutionTest 1/1, HttpPartitionOnCommitTest 1/1, on 2026-10-09 at `a4df7bfd214b`.
- Internal vocabulary: no hits.

Part 3 items:
- Replacement 7 (Proof, the pre-fix bullet, currently the line that reads "Without this fix, the new test fails at head `62516cc338ef`."). Replace with:
  "- Without this fix, the new test fails at head `62516cc338ef`. Base `cabedd1d968` was not run for this test."
- Replacement 8 (title, minor owner call). Replace the live title with:
  `SOLR-5941: CommitTracker should use the default UpdateRequestProcessorChain for autocommit`
- The body is 6,029 characters. It is one of the seven long drafts the owner accepted.

Body: byte-identical to the draft. Head `a4df7bfd214b` matches the receipt.

---

## Notes for the lead (not PR text)

- The receipts for SOLR-5754 and SOLR-5939 say "merged tree 1ddbf36202d". That is the same label error as in the drafts. Correct the receipts to "merge commit 1ddbf36202d, tree f2e33340f0ea".
- Titles are public writes. Replacements 4, 5, and 8 need owner ratification before any PR is edited.
