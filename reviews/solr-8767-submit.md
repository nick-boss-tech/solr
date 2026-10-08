# solr-8767-submit

- Branch: origin/solr-8767-submit
- Head: 3b5f2d235732 (matches the listed head; fork tip unchanged after fetch 2026-10-08)
- Base: upstream/main at 9d7cc2884e8a (merge-base 14c7aac0d151, 45 commits behind, 2 commits ahead)
- Scope: 2 commits, 3 files. `RealTimeGetComponent.java` (removes the copy-field-target filters from the update-log and index-backed `/get` paths: `removeCopyFieldTargets` deleted, the filter in `toSolrDoc(Document, IndexSchema)` removed; the atomic-reconstruction filter in `toSolrInputDocument` kept), `TestRealTimeGet.java` (+47, three tests: `testCopyFieldTargetSetDirectly`, `testCopyFieldTargetSetDirectlyAfterCommit`, `testAtomicUpdateOfDocumentWithCopyFieldTarget`), changelog `SOLR-8767-rtg-copy-field-targets.yml` (`type: fixed`, 7 lines). No `SOLR-8767-TESTING.md` on the tip.
- Verdict: Needs work (the ticket case is fixed; the change to `/get` output and its consequences are not decided or documented)
- Reviewer: claude-haiku-5-5 (Claude Code), round 36 Group B review, 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. Every claim rests on reading the diff and the code at the listed head.

## Delta check against the bulk review

Bulk review `research/branch-reviews/round-28/SOLR-8767-review.md` (verdict Needs work) was written at the same head (3b5f2d235732). No delta.

- Bulk F1 (MEDIUM, read/modify/write and Cloud MLT consequences of returning copy targets from `/get`): **confirmed by the code path.** The consequences are real in the code; whether they are acceptable is the owner's call (owner call 1). See finding 1.
- Bulk changelog gap: **confirmed.** See finding 2.
- Stale comment (not in the bulk review): **new finding.** See finding 3.

## Findings (ranked)

1. **MEDIUM, verified by reading. Returning copy targets from `/get` changes the read/modify/write and MLT inputs.**
   - The index-backed conversion `toSolrDoc(Document, IndexSchema)` (`RealTimeGetComponent.java`, around `:889`) no longer skips copy-field targets. A `/get` response therefore includes them (the new tests assert `author_s`).
   - The converter `toSolrInputDocument` (`:860`, `if ((!sf.hasDocValues() && !sf.stored()) || schema.isCopyFieldTarget(sf)) continue;`) still skips them. It is the SolrDocument-to-SolrInputDocument conversion on the atomic path (its caller chain was read only as far as the bulk review says). The `/get` output and that conversion now disagree, which the branch's third test relies on.
   - A client that reads with `/get` and resubmits the full document sends the copy target as a value. `DocumentBuilder` adds copy-field values into the target (`DocumentBuilder.java` `:183-203`, the copy loop at `:350-356`), and `addField` rejects a second value for a single-valued field (`DocumentBuilder.java:177`, "multiple values encountered for non multiValued field"). So a single-valued copy target can make the resubmitted document fail. Reading the anchors confirms the code path; the failure itself was not run (hypothesis until a test shows it).
   - Cloud MLT reads the document fields that RTG returns (the comment at `CloudMLTQParser.java:118-119` says so), and its default field collection consumes them (bulk review). So the copy-target fields now enter MLT's default field set. `getFieldValuesIncludingCopyField` (`:70`, `:115`) is the helper that handles copy targets for MLT. See finding 3.
   - The bulk review frames this as an owner/compatibility decision and not a demonstrated defect. Verified: the decision is not made, and the changelog does not record these effects (finding 2).

2. **LOW, verified. The changelog names the headline change only.** `changelog/unreleased/SOLR-8767-rtg-copy-field-targets.yml` (7 lines) says `/get` now returns copy-field targets "matching what a search returns after a commit". It does not mention the atomic read/modify/write caveat, the single-valued resubmit failure (finding 1), or the MLT default-field change.

3. **LOW, verified (new). A comment in `CloudMLTQParser` is now wrong.** `CloudMLTQParser.java:118-119` says "Fields created using copyField are not included in documents returned by RealTime Get." That is no longer true for `/get`. The comment should be corrected in this branch, or the branch's behavior described in the MLT code. The bulk review also asks for this correction.

## Owner calls (not decided here)

1. **Should `/get` return stored copy-field targets?** The branch makes that change and tests it. The consequences are finding 1 (read/modify/write resubmit, MLT default fields). Options the owner can pick from: keep the change and document the caveats in the changelog and upgrade notes; or restrict the change so the atomic and resubmit paths stay consistent with `/get`. The handoff records this as the owner's standing direction call. It is posed here and not decided.

## Proposed fixes (not applied; the owner decides)

- Finding 1: once owner call 1 is answered, either document the resubmit and MLT effects, or add a test for the single-valued resubmit case and decide its result.
- Finding 2: extend the changelog text to the caveats in finding 1.
- Finding 3: correct the `CloudMLTQParser` comment in this branch.

## Interactions with other branches

- None found in the bulk round notes for this ticket.

## Not checked

- Not compiled, formatted, or run. No Gradle, no `drain`, no test runs.
- The single-valued resubmit failure (finding 1) was not run; the `DocumentBuilder` anchors were confirmed by reading.
- The behavior of `CloudMLTQParser` with the new `/get` output was not traced beyond the comment and the helper's name.
- The owner's direction record for this call was not found in the workspace notes; the bulk review is the only source for the "standing direction" label.
- No GitHub or JIRA writes were made.
