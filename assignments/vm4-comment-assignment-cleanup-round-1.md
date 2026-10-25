# Assignment for vm4: clean up the backlog comment assignment so it can be found (round 1)

Status: OPEN
Intended host: vm4 (explicit, at the owner's direction, 2026-10-25)
Tags: records

## Why

vm4's open-work survey (assignments/vm4-to-phone-agent-open-work-round-1.md) never mentioned the backlog comment-drafting work, and the owner concluded it could not be found from where a host looks. The work exists and is fully specified; it is spread across three generations of files with one stale host line. Clean that up: verify the map below against the branch tip, publish one locator, and report any ambiguity that remains.

## Where the comment assignment lives (verified at tip a06f271b81a, 2026-10-25)

Repository: nick-boss-tech/solr. Branch: coordination-ui. All paths are on that branch.

- Operative entry point (claim from here): `assignments/backlog-first-100-pack-slices-round-1.md`. Four production units of 25 drafts plus one assembly unit, claimable by any pool Linux host as `claims/backlog-first-100-unit-<N>.md`. No unit is claimed yet.
- Rules parent: `assignments/backlog-first-100-pack-round-1.md`. Bug eligibility for this pack, exclusions, the live check at fill time, and the spot-QA step live here.
- Grandparent: `assignments/harmonize-audits-comment-drafts-round-1.md`. It built the store and the family structure; its no-Bugs rule and its QA-before-H2 gate are superseded for the pack by the pack file. Do not apply them to the pack.
- Source records: `material/harmonized-backlog/INDEX.md` and `material/harmonized-backlog/batches/h1-*.json` (use the latest revision per batch).
- Family texts (done, do not redo): `drafts/jira-comments/closure-not-a-bug.md`, `closure-fixed.md`, `closure-obsolete.md`, `closure-duplicate.md`, `ping-stale.md`, `status-note.md`.
- Filled drafts land at: `jira/jira-comment-<KEY>-draft.md`. Pack index lands at: `reports/backlog-first-100-pack-round-1.md`. Ledger stub lands at: `receipts/comment-ledger/STUB-backlog-first-100.md`.
- Known stale paperwork: vm2's six claims `claims/harmonize-h2-*.md` still read LIVE, but their deliverables (the family texts above) exist; treat the families as delivered.
- Not this work: the Critical comment line is separate, at `assignments/critical-tickets-comments-round-1.md` with its directed file `assignments/phone-agent-2-critical-closure-drafts-round-1.md`. It is not part of the backlog pack and is not vm4's to clean up here.

## Cleanup work

1. Re-verify every path above at the current branch tip and record the tip SHA and a presence line per path.
2. Publish one locator at `assignments/comment-drafting-locator-round-1.md`: a standing index (not a work unit) stating the map above in the same order: entry point, rules parent, grandparent with its superseded parts named, source records, family texts, where drafts and the index and the ledger stub land, and who may claim (any pool Linux host; the pack parent's "intended host vm2" line is superseded by the slices file).
3. Report at `reports/vm4-comment-assignment-cleanup-round-1.md`: the verification table, the locator's commit, and every remaining ambiguity you found, each either resolved by naming the governing file or flagged for the owner.

## Limits

Add-only: publish the locator and the report; do not edit the pack files, the slices file, or any other host's claims. No draft production and no claiming a production unit inside this assignment; claiming one afterwards, under the slices file, is separate. No Jira or GitHub text.
