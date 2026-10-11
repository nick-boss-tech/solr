# Review confidence round 4: live PR body fidelity

Assignment: `assignments/pool-review-confidence-round-4.md`. Claim: `claims/pool-review-confidence-round-4.md`. Lead: the windows review agent. Body fidelity only: this round does not reopen code review, rerun tests, or decide a PR's readiness.

Part reports, with the exact replacement text for each drift item:

- `reports/review-confidence-round-4-a.md`: SOLR-18178 (PR 4968); SOLR-18391 (PR 5027 and PR 4997, two bodies); SOLR-18129 (PR 5000); SOLR-16130 (PR 5004); SOLR-18482 (PR 5009).
- `reports/review-confidence-round-4-b.md`: SOLR-12849 (PR 5011); SOLR-13568 (PR 5014); SOLR-13706 (PR 5015); SOLR-13097 (PR 5016, conflicting, changes requested; fidelity only); SOLR-18505 (PR 5028).
- `reports/review-confidence-round-4-c.md`: SOLR-18506 (PR 5029); SOLR-18119 (PR 5061); SOLR-18523 (PR 5062).

Read-only. Nothing was edited on a live PR, commented, reviewed, or pushed to a branch. No build, test or gate ran.

## Verdict counts

Fourteen bodies: **14 DRIFT, 0 CONSISTENT.** Every API head matched `git ls-remote` on origin and the receipt's gated head, so no head mismatch was found.

| Body | PR | Verdict | Part report |
|---|---|---|---|
| SOLR-18178 | 4968 | DRIFT (3 items) | a |
| SOLR-18391 (open) | 5027 | DRIFT (5 items) | a |
| SOLR-18391 (superseded draft) | 4997 | DRIFT (2 items) | a |
| SOLR-18129 | 5000 | DRIFT (5 items) | a |
| SOLR-16130 | 5004 | DRIFT (1 item) | a |
| SOLR-18482 | 5009 | DRIFT (2 items) | a |
| SOLR-12849 | 5011 | DRIFT (3 items) | b |
| SOLR-13568 | 5014 | DRIFT (1 item) | b |
| SOLR-13706 | 5015 | DRIFT (2 items) | b |
| SOLR-13097 | 5016 | DRIFT (4 items); fidelity only, not a readiness call | b |
| SOLR-18505 | 5028 | DRIFT (3 items) | b |
| SOLR-18506 | 5029 | DRIFT (6 items) | c |
| SOLR-18119 | 5061 | DRIFT (7 items) | c |
| SOLR-18523 | 5062 | DRIFT (3 items) | c |

## Cross-cutting drift

- **Changelog line not linked at the head.** Four or more bodies name the changelog file as plain text. The formula requires a link at the head SHA. Seen in PR 4968, 5027, 4997, 5000, 5009, and 5011, 5014, 5015 and 5016 (the round b part report).
- **Internal vocabulary in public text.** "Crave run" and "battery" (PR 5028); "planning runs" (PR 5029); process words in the PR 5011 body, which also lacks bold section summaries.
- **Run claims with no receipt support.** The common pattern is a run, a count, or a date stated in the body that the receipt does not record: a local re-run and base results (PR 5000); an unrecorded mutation run and an earlier 4/4 run (PR 5027); a teardown error and a percentage (PR 5029); the Windows and CPython 3.12.12 comparisons and a planted-error run (PRs 5061 and 5062).
- **Pre-change citations at the head.** Several bodies cite symptom code at the head SHA or upstream main instead of the merge-base, and do not say so (PRs 5011, 5014, 5061, 5062 and others). The part reports list each one.
- **Counts that disagree with the record.** PR 4997 says 17 added lines; the diff stat says 14. PR 5015's summary says every new test fails with an NPE, but one of them fails on a key assertion. PR 5016 names two failing cases where the receipt counts three.

## Record conflicts flagged, no replacement chosen

These need the main side to decide, not an edit.

1. **SOLR-18506 (PR 5029).** Commit `3c098439882` is titled "Caffeine eviction notification race". The live body says "Not a timing race", and the receipt says delivery is synchronous. Commit subjects cannot change without a force push.
2. **SOLR-18482 (PR 5009).** The receipt's lines 4 and 5 disagree about the 2026-10-06 confirmation run. The docs-only head `49ca9099d8e` is recorded as "no Gradle gate was run for it", but the same receipt calls the run "at this head". The main side must say which SHA the run covered.
3. **SOLR-18391 (PR 5027).** The receipt gives no gate date for the focused runs at `adcda10b501`.
4. **Head dates.** Heads `6b92223bc24` (PR 5011), `ac5d60c214c` (PR 5014), `590dd5c24d9` (PR 5015) and `e28739b4069` (PR 5028) are timestamped 2026-10-07 UTC. Their receipts and bodies record the gate or verification on 2026-10-06. The main side confirms the date convention.
5. **SOLR-18119 (PR 5061).** The superseded branch's receipt deleted its changelog entry as "not changelog worthy for a release-manager-only script change". The live branch adds the entry (type other). The owner confirms which policy applies.
6. **SOLR-18119 (PR 5061), the superseded PR.** PR #4999 is still open, and the body presents it as a live alternative. Closing it is the owner's call.

## Not judged here

- Jira history in the PR 5061 body and its Choice section; check against Jira before any posting.
- CI run status for each PR, and the Lucene PRs the PR 5062 body cites.
- Receipt goal files and logs, which are not in this worktree.

## Decisions for the main side

- Apply the replacement text in the part reports to the live bodies. Each part report lists the old and new text.
- Link the changelog line at the head SHA on every body that names it.
- Resolve the record conflicts above before any body is marked ready.
- PR 5016 (SOLR-13097) is conflicting with changes requested; this round's fidelity verdict says nothing about its readiness.
