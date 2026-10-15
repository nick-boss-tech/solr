# Pool assignment: fresh re-check of the three new PR drafts, round 1

Status: DONE

Capability tags: `review`. Intended host: windows. Staffing: one read-only subagent per draft (three in parallel at most). Claim: this round runs under the standing coordinator claim, claims/coordinator-review-rounds.md.

## Purpose

SOLR-6711, SOLR-17612 and SOLR-16013 were drafted on 2026-10-12 and have had one pass each. Fresh checks on earlier drafts caught real errors on the first try, and late revisions have been the main source of rework. This round is a full fresh pass on each draft before it is treated as settled.

## Drafts

- `pr-drafts/replication-backup/SOLR-6711.md`, receipt `receipts/SOLR-6711.md`, head `73c5e6e0d243`, merge-base `cabedd1d968`.
- `pr-drafts/spellcheck/SOLR-17612.md`, receipt `receipts/SOLR-17612.md`, head `3a456c27dda5`, pre-fix tree `dd2708fbb7e5` (per the assignment that commissioned it).
- `pr-drafts/solrcloud/SOLR-16013.md`, receipt `receipts/SOLR-16013.md`, head `408317d7e241` (rewritten after the history scrub; the draft was re-pointed to it).

## Checks for each draft

1. Every head, short head and link SHA is the named head, and every link target exists at that commit with the stated path (`git cat-file -e <sha>:<path>`).
2. Every line-range anchor covers what the sentence says, at the commit the link names.
3. Every count, test name, date and pass/fail statement matches the receipt's top section. Dates must be the receipt's recorded dates.
4. Every behaviour claim is either in the receipt or follows from the code at the named head. Anything that is neither is a finding.
5. Plain language, no internal process words, no em dash, no PR numbers, the AI header and footer present.

## Rules

- Read-only on drafts and receipts. The review side patches in a later round, under the patch-loop directive, only where the fix is mechanical (head SHA, count, date, changelog link target).
- Decisions follow the standing ladder. Missing information: get it from the record or the code. Where the fix is effort, do it. Where a statement is optional, defer it as a named Limits follow-up with a plan. A recorded recommendation is adopted as the decision.
- No builds, no tests, no Gradle, no posting, no PRs.

## Deliverable

`reports/fresh-recheck-new-drafts-round-1.md`: verdict first, then one section per draft with PASS or FAIL per check, exact replacement text for each FAIL, and the rung of the ladder used for any judgment.

Rules for the claim: WORKFLOW.md binds (claim before work, hourly heartbeats, subagent cap of 6 to 7 across all hosts, mark the claim DONE in the same push as the deliverable).
