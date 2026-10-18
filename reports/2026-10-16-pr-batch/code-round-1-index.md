# Code round 1, 2026-10-16 review follow-ups: index

Assignment: `assignments/pool-review-2026-10-16-code-round-1.md`. Claim: `claims/pool-review-2026-10-16-code-round-1.md` (vm5). Run on 2026-10-18 from vm5. No production change was committed to any fork branch.

| Item | Ticket (PR) | Status | Deliverable | Note |
| --- | --- | --- | --- | --- |
| 1 | SOLR-10424 (5159) | Done by vm2 per `claims/pool-review-2026-10-16-code-round-1-item-1.md` | `reports/2026-10-16-pr-batch/SOLR-10424-check.md` | That file is not on origin/coordination at `6890f7c618` or later; main side to confirm. Not re-run by vm5. |
| 2 | SOLR-7390 (5136) | Resolved: stale test updated on the PR branch | `SOLR-7390-check.md` | `testMarker` expected an unknown `[elev]` to be ignored; commit `4ca1ce4da1` makes it a 400. Fixed in `edd451863f`, focused test passes. |
| 3 | SOLR-6975 (5135) | Resolved: cycle fixed in `6c033ba6ea`; paging gap documented as a limitation | `SOLR-6975-check.md` | Leading `_docid_` no longer forms a comparator cycle; PR body updated. Repeats and gaps across pages are stated as known, not fixed. |
| 4 | SOLR-6759 (5154) | Resolved: test trips inside the expand search (`bdeb07d03b`) | `SOLR-6759-check.md` | Count 6 trips inside the search; the test fails on the parent code without the `finally` and passes at the PR head. PR body corrected. |
| 5 | SOLR-8939 (5146) | Refuted for string uniqueKey; gap for non-string keys | `SOLR-8939-check.md` | Duplicates are rejected in `ShardDocQueue.push`; no null slot. `resultIds` has no in-tree subclass. |

Test sources for items 3 and 5 are under `SOLR-6975-tests/` and `SOLR-8939-tests/`. They are not committed to the fork branches.
