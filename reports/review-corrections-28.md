# Review corrections to the 28 drafts: round report

Claim: `claims/review-corrections-28.md` (commit `c37b71d7dac`). Source: `material/review-opened-28-answers.md` (commit `7cda66bd41a`), the seven group reports `reports/review-opened-28-g1.md` to `reports/review-opened-28-g7.md`, and the restored SOLR-7022 receipt at the same commit. Drafts: `pr-drafts/update-processing/`.

Done 2026-10-09 by Claude Code (AI agent), working for Nick Shanin. Seven subagents edited four drafts each. The lead checked every modified draft for em dashes, internal wording and length, and made three further fixes. Nothing was posted. No PR body or title was changed. No submit branch was edited. No builds, Gradle or tests were run.

## What changed

Every one of the 28 drafts was edited, except SOLR-16356, which had one change to its Limits sentence.

- **Description and Proof findings** from the group reports, as the answers accept them.
- **Decision O2 (follow-up wording).** Limits that name a gap now state that a follow-up submission is planned. "On request" phrasing is removed.
- **Decision O3 (SOLR-5505).** The always-on `[core]` format stays, with type `changed`. A choice section poses the opt-in alternative.
- **Decision O4 (SOLR-6045).** The mixed-field claim is scoped to the merger path. Limits name the factory path with a planned follow-up.
- **Decision O5 (SOLR-12705).** The default (atomic operands are mutated by default) and the per-class opt-out are stated plainly.
- **Decision O8 (SOLR-5887).** The claim is scoped to the add-command path. The `ClassificationUpdateProcessor` bypass is named in Limits.
- **SOLR-7022.** The Proof follows the restored receipt: the original gate at `a2ed957063b` on 2026-10-05, and the top-up at the live tip. "Never gated" is removed.
- **SOLR-16673.** The gate facts the draft cited earlier are restored, labelled with their head `d5c19e64ba1b`, from the receipts addendum. The current receipt records only the top-up.
- **SOLR-13696.** The choice heading names the create-alias fix, so it no longer reads as one production fix against the body's two.
- **SOLR-6065.** One line that used "review" in public text now says "a question for maintainers".

## Held back, not changed

1. **PR titles (O1).** The drafts have no title field. The titles stay as they are until the owner decides.
2. **SOLR-7504 null-count defect (O7).** The fix is in progress on the branch, so the draft keeps "A null counts as 0". That sentence is false on the current branch until the fix lands. The draft should not flip until it does.

## For your confirmation

1. **Planned follow-up commitments.** Decision O2 turns several Limits gaps into planned follow-up submissions. The drafts now state those plans in SOLR-3657, 5065, 5754, 5887, 5939, 5941, 6045, 6065, 6973, 7022, 7504, 11475, 12245, 12703, 12705, 14262, 14718, 16356, 16655, 16673, 16910, and 13696. Each is a commitment to upstream maintainers, so please confirm each one, or tell me which to strike.
2. **Gaps still without a follow-up sentence.** SOLR-5065 (test coverage and reference-guide bullets), SOLR-12705 (two bullets), SOLR-14718 ("Two paths still name one request"), SOLR-14262 (autocommit bullet), and SOLR-7022 (seed sensitivity). These were left as the group reports instructed. Confirm whether they need a planned sentence.
3. **SOLR-7022 test bullet.** The `DirectUpdateHandler2CommitWaitTest` bullet describes the premise result the receipt names. Its class does not have its own run result in the receipt. Confirm the bullet can stay as it is, or drop it.
4. **Unrecorded details kept on purpose.** SOLR-13696 and SOLR-13943 still say "fresh JUnit XML", and SOLR-16655 says the same. SOLR-16356's Proof still says "as recorded in the gate entry". None of these is in a receipt. Strike them, or accept them as the gate's own description.
5. **Length.** Several drafts are over the guide: SOLR-13696 (7,720 characters), SOLR-5939 (7,518), SOLR-6045 (7,169), SOLR-5941 (6,144), SOLR-12705 (6,502), SOLR-14718 (5,842), and SOLR-13943 (5,590). Trim or accept.
6. **SOLR-16673 Proof.** The restored gate facts come from the receipts addendum, not from the current receipt. Confirm that the addendum is an acceptable source for the earlier head.

## Not applied to the PRs

No PR body, title or label was changed. The main side applies the corrected drafts to the live PRs after the owner rules on the calls above and on the title rule.

## Not done

No gate, build or test was run. No submit branch was changed. No PR was edited, flipped, or closed.
