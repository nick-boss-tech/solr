# Claim: corrections to the 28 drafts from the internal review

Claimed 2026-10-09 by Claude Code (AI agent), working for Nick Shanin.

Scope: `material/review-opened-28-answers.md` (commit `7cda66bd41a`), which accepts the description and Proof findings of `reports/review-opened-28.md` and records the owner's decisions. Input: the seven group reports `reports/review-opened-28-g1.md` to `reports/review-opened-28-g7.md`, and the restored SOLR-7022 receipt at the same commit. Output: edits to the 28 drafts under `pr-drafts/update-processing/`, and a round report.

Applied:
- The description and Proof findings in each group report, as the answers accept them.
- Follow-up wording: Limits name the gap and state that a follow-up submission is planned. "On request" phrasing comes out (O2).
- SOLR-5505: keep the always-on `[core]` format, type `changed`, and add a Choices section that poses the opt-in alternative (O3).
- SOLR-6045: scope the claim to the merger path, and name the factory path in Limits with a planned follow-up (O4).
- SOLR-12705: state the default (mutate atomic operands) and the per-class opt-out plainly (O5, decided on the record).
- SOLR-5887: scope the claim to the paths the change covers, and name the `ClassificationUpdateProcessor` bypass in Limits with a planned follow-up (O8).
- SOLR-7022: the Proof follows the restored receipt, which records the original gate at `a2ed957063b` on 2026-10-05 and the top-up at the live tip. The "never gated" claim is removed.

Not applied, left for the owner or the fix lane:
- PR titles (O1). The drafts carry no title field, and the titles stay as they are.
- SOLR-7504's null-count defect (O7). The branch fix and re-gate are in progress on the main side. The draft's "a null counts as 0" sentence stays, and the subagent reports it, so the fix lane can decide.
- Any change to a PR body or title. The main side applies the corrected drafts to the PRs after the owner rules.

Split: seven subagents, four drafts each. Each subagent reads its group report and the answers file, edits only its four drafts, and writes nothing else. The lead commits and pushes.

Not in scope: PR edits, comments, submit-branch edits, builds, Gradle, and test runs.
