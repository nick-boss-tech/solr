# Pool assignment: decisions on open items under the decision ladder, round 1

Capability tags: `review`. Intended host: windows. Staffing: the review agent, with read-only checks where needed. Claim: `claims/pool-decisions-round-1.md`.

## Purpose

Six open items were held for the owner: packaging commits, attributed counts, SOLR-2632 disposition, SOLR-13136 behaviour, SOLR-15805 force update, and SOLR-15358 record conflict. The owner has asked that these be decided here under the decision ladder, not returned as questions. Each decision is recorded with its information, its rung, and the draft edits it causes. Actual sends, posts and PR writes stay with the owner.

## Ladder applied

1. Get more information where it is missing (receipts, object store, labelled runs).
2. Where a recorded recommendation exists, adopt it. Where none exists, take the harder, more thorough option.
3. Defer optional items as named Limits follow-ups with a stated plan.

## Decisions

- **Packaging commits (SOLR-11356, SOLR-12161).** Receipts give a labelled run at the fork tip and say the packaging commit only removes a handoff doc. Adopt the tip: swap the named head and links to the fork tip, keep the labelled run. Same-tree equality is taken from the receipt, not verified here, because the packaged object is absent.
- **Packaging commits (SOLR-10364, SOLR-11678).** No labelled run exists at the fork tip. The information cannot be obtained here. Stay on HOLD.
- **Attributed counts.** Counts whose only source is a takeover log or ledger citation are not carried into drafts. This is the rule already applied to dates. SOLR-4367's owner placeholder is replaced by a named Limits follow-up.
- **SOLR-2632.** The receipt recommends (a) or (b). Option (a) needs a new commit that cannot be gated here. Adopt (b): a Jira comment with the finding and no PR. The PR draft is not sent as a PR.
- **SOLR-13136.** The live tip keeps the shard in CONSTRUCTION after a failure. This matches the PR title and changelog and avoids deleting acknowledged writes. Adopt the live behaviour. The receipt's "consistency pass" label is wrong and is corrected in the report. A main-side gate at the tip stays owed.
- **SOLR-15805 force update.** The new fork tip has an identical tree to the named head. Accept it, and record the force update as information.
- **SOLR-15358 record conflict.** The 2026-10-12 proof run with the fix reverted supersedes the 2026-10-03 pre-fix claim. The 2026-10-03 date for the with-fix pass comes only from the ledger, so it is removed.
- **SOLR-11939.** Keep the 2026-10-12 date. The receipt body carries a dated verification at the head.

## Rules

- No builds, no tests, no Gradle, no posting, no PR write, no force push.
- No em dashes or en dashes in authored text. No PR numbers in file names or commit messages.

## Deliverable

`reports/decisions-round-1.md`, one section per decision with information, rung, and draft edits. Mark the claim DONE in the same push.
