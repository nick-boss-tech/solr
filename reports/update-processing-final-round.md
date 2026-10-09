# Final round report: update processing

Round: `assignments/update-processing-final-round.md`. Claim: `claims/update-processing-final-round.md`. Material: `material/update-processing-final-round.md`. Draft format: `pr-formula.md`.

Done 2026-10-09 by Claude Code (AI agent), working for Nick Shanin. Three subagents reviewed the branches. The lead agent checked their results and made the commits.

## Summary

- 17 in-scope branches: 12 drafts written, 5 held.
- SOLR-18505 (closing item): the live pull request description edit is prepared and checked. It is not applied. It waits for the owner's confirmation.
- No Gradle, builds, or tests were run. No submit branch was edited or checked out. Nothing was posted to GitHub or Jira.
- Every head was checked against the live fork tip. All 18 match. SOLR-3657 and SOLR-16655 match the head their audits name.

## Per-branch rows

Proof sources are the files or records named in each draft. Where a source gives no counts, the draft says so and gives none.

| Ticket | Head verified | Proof source | Result | Notes |
|---|---|---|---|---|
| SOLR-4841 | `f8850ffd4214`, yes | Audit evidence section (no counts) | **Held** | The changelog type `fixed` does not match. The ticket asks for a public constructor, which is a new capability, so `added` fits. The owner decides whether to change the fragment on the branch. The tree is otherwise clean. |
| SOLR-5505 | `44c444aa5cd3`, yes | Audit evidence section (no counts) | Draft: `pr-drafts/update-processing/SOLR-5505.md` | Base failure checked by reading only. The audit's "opt-out" claim is not supported, so the draft does not make it. |
| SOLR-5887 | `c4c57ef7bcbd`, yes | Audit evidence section (no counts) | Draft: `pr-drafts/update-processing/SOLR-5887.md` | The audit says the base message lacks the field name. It does not: base already names the field. The draft says only the core-name check fails on base. |
| SOLR-6973 | `fa5b59ba07b4`, yes (the audit's head `4c6092614e5a` has moved on) | Receipt, 2026-10-08: focused class 7 of 7 at this head. Base run of the same class: 7 tests, 1 failure | Draft: `pr-drafts/update-processing/SOLR-6973.md` (Proof replaced) | The receipt does not name the failing test. The draft says the new test is identified by reading. Pass-through versus reject is a choice the draft poses. |
| SOLR-5939 | `f8d4bdbea518`, yes | Merged-tree receipt `1ddbf36202d`, 26 of 26 | **Held** | The receipt's merged tree is not in this repository. A fresh merge of the two heads gives `f2e33340f0ea`. The same merge on `upstream/main` gives `38b73afa1e58`. The owner or the main side must confirm the tree and head pair. At this head, the audit's blocking items 1, 2 and 10 are addressed. The audit itself is for an older head. |
| SOLR-5754 | `7fbe0128d8b0`, yes | Same merged-tree receipt as SOLR-5939 | **Held** | `SOLR-5754-TESTING.md` sits at the repo root in the branch diff, with internal text. Remove it, then re-verify the head. The receipt issue above applies too. |
| SOLR-12864 | `b9c6c1e71ffa`, yes | Material receipt, `JsonLoaderTest` 32 of 32, 2026-10-08 | Draft: `pr-drafts/update-processing/SOLR-12864.md` | Test-only. Keeping it as coverage matches the DECIDED option B in `TESTING.md`. No base run of the pin is recorded, so the draft says the pin is expected to pass. |
| SOLR-3657 | `14edaca577c0`, yes (the audit names `14edaca577c`) | Audit evidence section. CI run `37592597553` success, as recorded in the audit | Draft: `pr-drafts/update-processing/SOLR-3657.md` | The changelog fragment has a suffixed name, and the draft cites that name. Base failure checked by reading. |
| SOLR-13265 | `c134b34aa27f`, yes | None. No test-queue or gate record exists at this head | Draft: `pr-drafts/update-processing/SOLR-13265.md`. **Not postable yet.** | The Proof says no test run is recorded. The test's assertion also passes when the datapoint is absent (audit finding 3). Do not post until a fail-before verdict exists. |
| SOLR-7022 | `6a233ab2fdb0`, yes | Audit evidence section. CI run `37558668356` success | Draft: `pr-drafts/update-processing/SOLR-7022.md` | The changelog fragment says "for example an autocommit during core reload". The audit says that cause is unproven. The draft states the cause as an interrupt and makes no source claim. **Reword the fragment on the branch before posting.** The branch was not edited. |
| SOLR-11483 | `4431a250f665`, yes | Audit evidence section. CI run `37494684481` success | Draft: `pr-drafts/update-processing/SOLR-11483.md`. **Not postable yet.** | Audit finding 1 (the owner's call on the default) is still open. The draft has a choice section for it. Retention growth is in Limits. |
| SOLR-5941 | `bf17e860d9ea`, yes | Receipt, 2026-10-08: four focused classes green. Pre-fix run at `62516cc338e` | **Held** | The branch's autocommit doc says the synthetic request has "no request parameters beyond the commit itself". `CommitTracker` sets `commit_end_point` on it (line 278). Fix the sentence, then re-verify. Also, `DistributedZkUpdateProcessor` acts on any request with `commit_end_point=true`, not only autocommits. That wider effect must be disclosed or the check narrowed. |
| SOLR-14262 | `1e8d2b0075d7`, yes | Audit evidence section. GitHub run `37642970685` success, on `ci/14262-recovery-r35` | Draft: `pr-drafts/update-processing/SOLR-14262.md` | The `commitIgnored` header contract is posed as a choice for the owner. Cloud behavior was not traced, and the draft says so. The audit's low-severity null-check hypothesis is left out. |
| SOLR-16655 | `aa7898d972a7`, yes | Audit evidence section (no counts) | Draft: `pr-drafts/update-processing/SOLR-16655.md`. **Owner decisions before posting.** | (1) Wider effect: the walk into child documents runs even when the parent field is not selected (`FieldMutatingUpdateProcessor.java` lines 128-141). No test covers it. Test it or accept it. (2) The upgrade-note wording is the subagent's reading of the diff. Confirm it. (3) The changelog type `fixed` may fit worse than `changed`, since indexed values can change. (4) Overlap with SOLR-12705: merge conflicts in `FieldMutatingUpdateProcessor.java`. |
| SOLR-16673 | `d5c19e64ba1b`, yes | Material receipt of 2026-10-08. **Not on disk** | **Held** | The receipt the material cites is not in this workspace. The only record, `research/test-queue/results/SOLR-16673.json`, shows 43 of 43 and spotless success. It is dated 2026-10-03 and has no head SHA. There is no fail-before file. Release it with a receipt that names `d5c19e64ba1b` and gives the pre-fix result. |
| SOLR-16356 | `39c0585072f0`, yes | `research/test-queue/results/SOLR-16356.json` and `.failbefore.json`, at receipt head `dcb16c775d62`, dated 2026-10-03 | Draft: `pr-drafts/update-processing/SOLR-16356.md`. **Owner confirms the receipt is enough.** | The material's receipt (2026-10-08, at `39c0585072f0`) is not on disk. The draft cites the receipt that is on disk and states its head. The three files the branch changes are identical at `dcb16c775d62` and `39c0585072f0`. That was checked. The fail-before ran on base `56ec140e`. Tidy and Error Prone are left out of Proof, since no head is recorded for them. |
| SOLR-16910 | `9fce3e9a7058`, yes | Audit evidence section. The third gate run is on the main side, not on disk. No counts | Draft: `pr-drafts/update-processing/SOLR-16910.md` | The `SolrCore.Request` item is in Limits, per the DECIDED narrow scope. The audit's "Not ready" predates that decision. |

## SOLR-18505 (closing item): prepared, not applied

- The live pull request for `solr-18505-submit` is open. Its head is `e28739b4069d`, and it was last updated 2026-10-07 05:38 UTC.
- Comments: none. Issue comments, inline review comments, and reviews are all zero. `pr view --comments` fails on this token, so the counts come from the REST endpoints. No maintainer comment is recent.
- **Before**, which occurs exactly once in the live body: "Gate re-run at the new head: tidy clean, Error Prone compile clean, `:solr:core:check -x test` green, and `DirectUpdateHandlerTest` passes 7/7 locally; a GitHub Actions corroboration run (37577283523) was dispatched for the same class at this head."
- **After**, proposed: "Gate re-run at the new head: tidy clean, Error Prone compile clean, `:solr:core:check -x test` green, and `DirectUpdateHandlerTest` passes 7/7 locally; a GitHub Actions corroboration run (37577283523) was dispatched on the fork for the same class at `71a978d5982`, which is head `e28739b4069` plus one commit that adds a CI workflow file."
- The audit gives no verbatim sentence. It says the corroboration run was on the fork at `71a978d5982`, and that the description could name that commit. The "after" wording is the subagent's. **The owner must confirm the wording.**
- The `71a978d5982` commit was checked to be `e28739b4069` plus one commit that adds `.github/workflows/fork-test-runner.yml`.
- Proposed full body, with only that sentence changed: `scratchpad/SOLR-18505-body-proposed.md`. The subagent diffed it against the live body. Only that sentence differs. It must be diffed again just before the edit.
- The live body is about 5,200 characters, which is above the formula's length guide. That is outside this edit.

## Owner decisions

1. SOLR-4841: change the changelog type on the branch, or accept `fixed`.
2. SOLR-5939 and SOLR-5754: confirm the merged tree and head pair. Remove the root `SOLR-5754-TESTING.md`.
3. SOLR-5941: fix the autocommit doc sentence. Disclose or narrow the `commit_end_point` check in `DistributedZkUpdateProcessor`.
4. SOLR-13265: a fail-before verdict is needed before the draft can be posted.
5. SOLR-7022: reword the changelog fragment on the branch before posting.
6. SOLR-11483: decide audit finding 1 (the default) before posting.
7. SOLR-16655: test or accept the wider descent effect. Confirm the upgrade-note wording. Pick the changelog type.
8. SOLR-16673: a receipt naming `d5c19e64ba1b` with the pre-fix result.
9. SOLR-16356: confirm that the receipt at `dcb16c775d62`, with the identical branch files, is enough for Proof.
10. SOLR-18505: confirm the "after" sentence, then authorize the edit to the live description.

## Checks

- Drafts: no em dashes and no PR numbers. Citations link to the full head SHA of each branch.
- Changelog paths are the real fragment names. The `SOLR-<ticket>.yml` pattern in the formula does not match the repo's names, so the drafts cite the real paths.
- No drafted branch has a root-level file in its diff. The held SOLR-5754 branch does, as the table says.
- The material's receipts for SOLR-16673 and SOLR-16356 do not match the records on disk. The table above says so.
