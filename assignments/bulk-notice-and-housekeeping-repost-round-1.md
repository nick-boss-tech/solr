# Repost: bulk-update notice draft and housekeeping phase 0, round 1

Status: OPEN

Posted by the phone agent at the owner's direction, 2026-10-24. Two assignments have sat OPEN and unclaimed since the morning of 2026-10-24. This file re-offers them. Each unit is claimed separately (one claim, one unit); the original assignment files hold the full rules and are not edited by this repost.

## Unit 1: the dev-list bulk-update notice draft

- Source assignment: `assignments/jira-bulk-update-notice-round-1.md` (rules, decisions, and limits there govern).
- Claim as `claims/repost-bulk-notice-round-1.md`. Tags: `draft`, `records`.
- Deliverable: `drafts/jira-bulk-update-notice-draft.md`.
- Facts verified since the original posting, so the draft need not stall on them: the shipped EOL versions, checked read-only against `releases/lucene-solr/*` tags on apache/solr (230 tags): 4.9, 5.0, 5.2, 5.5, 6.0, 6.2, 7.0 all shipped. 6.7 never shipped (the 6.x line ends at 6.6.6); issues carrying fixVersion 6.7 are planned-but-unreleased and must not be worded as an EOL release. The draft may name the seven shipped versions with that source stated.
- Still the owner's, still placeholders in the draft: category 2 of the plan, the post date, and the first sample slice (71 or 113 tickets).

## Unit 2: housekeeping phase 0 census

- Source assignment: `assignments/jira-housekeeping-pipeline-round-1.md`, phase 0 only (rules, exclusions, and claim shape there govern; one claim per batch of at most 100, `claims/jira-housekeeping-<batch>.md`, tags `records` and `review`).
- Deliverable: `receipts/jira-housekeeping/census/` (refreshed counts, the tier table, the skip-audit overlap list).
- Feed note: the harmonization store (`assignments/harmonize-audits-comment-drafts-round-1.md`, `material/harmonized-backlog/`) is being built to feed this phase. Phase 0 consumes that store's overlap records as they land instead of re-deriving the overlap; the census refresh itself does not wait for the store.

## Limits

- This repost changes no rule in either source assignment. Drafts and records only; nothing posts to the dev list, Jira, or GitHub.
- This file closes when both units have live claims; it is not itself claimable.
