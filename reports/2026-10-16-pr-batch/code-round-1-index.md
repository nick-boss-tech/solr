# Code round 1, 2026-10-16 review follow-ups: index

Assignment: `assignments/pool-review-2026-10-16-code-round-1.md`. Claim: `claims/pool-review-2026-10-16-code-round-1.md` (vm5). Run on 2026-10-18 from vm5. No production change was committed to any fork branch.

| Item | Ticket (PR) | Status | Deliverable | Note |
| --- | --- | --- | --- | --- |
| 1 | SOLR-10424 (5159) | Done by vm2 per `claims/pool-review-2026-10-16-code-round-1-item-1.md` | `reports/2026-10-16-pr-batch/SOLR-10424-check.md` | That file is not on origin/coordination at `6890f7c618` or later; main side to confirm. Not re-run by vm5. |
| 2 | SOLR-7390 (5136) | Escalated: the test fails at head | `SOLR-7390-check.md` | `testMarker` expects an unknown `[elev]` to be ignored; commit `4ca1ce4da1` makes it a 400. Test not changed. |
| 3 | SOLR-6975 (5135) | Escalated: both checks confirmed | `SOLR-6975-check.md` | Paging repeats and gaps at head; merge comparator cycle with `_docid_` leading. Pre-fix code crashes with a 500 instead. |
| 4 | SOLR-6759 (5154) | Escalated: the test does not reach the new block | `SOLR-6759-check.md` | Test passes with the `finally` removed; trip counts 1 to 3 tried. No corrected test committed. |
| 5 | SOLR-8939 (5146) | Refuted for string uniqueKey; gap for non-string keys | `SOLR-8939-check.md` | Duplicates are rejected in `ShardDocQueue.push`; no null slot. `resultIds` has no in-tree subclass. |

Test sources for items 3 and 5 are under `SOLR-6975-tests/` and `SOLR-8939-tests/`. They are not committed to the fork branches.
