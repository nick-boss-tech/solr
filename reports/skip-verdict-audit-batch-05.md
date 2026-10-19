# Skip verdict cross-audit, batch 5 of 21

Assignment: `assignments/pool-aab-skip-verdict-audit-05.md`. Auditor: vm2, 2026-10-19, claim `claims/pool-aab-skip-verdict-audit-05.md`. Each item was checked against the floor in `material/skip-audit/verdict-rubric.md`: a closeout claim in `claims/` naming the ticket with a DONE line, the named draft branch on the fork with `drafts/<KEY>/record.md` and `jira-comment.md`, the pinned head the record cites (matched to `evidence_head` in `material/skip-audit/queue-current.json`, which is `498a09aea97a` for every item here), the Jira status read in that pass, a code path with file and line or a reproduction, a substantive record, and a verdict whose draft suffix matches.

Checks run on the fork at tip `0ed605c6de6`. Every draft branch exists with its suffix matching the recorded verdict, both files are present for all 25, and every record cites the pinned head. No item passed: every record says "JIRA was not re-read for this record", so no item shows the Jira status read in this pass.

Own-host check: 19 records state "Checked by: vm2" for the reading they cite, and item 2 (SOLR-9965) cites the seed of vm2's own repro round. These 20 items are `deferred-own-host`; this host did not audit them.

| # | Ticket | Result | Claim found | Draft branch head | Missing |
| --- | --- | --- | --- | --- | --- |
| 1 | SOLR-9970 | deferred-own-host (keep-open) | not checked (own host) | `fdf090c5753c` (solr-9970-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 2 | SOLR-9965 | deferred-own-host (keep-open) | pool-skip-repro-flake-round-1.md (vm2 repro round, DONE; seed cited in record) | `ddfe0607ecf6` (solr-9965-draft-open) | the record's reproduction seed 6EF7EAAC35553A06 is the vm2 repro round's seed; not audited by this host |
| 3 | SOLR-9942 | deferred-own-host (keep-open) | not checked (own host) | `b7272269fd39` (solr-9942-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 4 | SOLR-9925 | deferred-own-host (keep-open) | not checked (own host) | `b68d6afd9351` (solr-9925-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 5 | SOLR-9913 | deferred-own-host (close-obsolete) | not checked (own host) | `2a39c8a04630` (solr-9913-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 6 | SOLR-9872 | deferred-own-host (close-obsolete) | not checked (own host) | `1ad9c69cd34c` (solr-9872-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 7 | SOLR-9871 | deferred-own-host (keep-open) | not checked (own host) | `c12e00879d45` (solr-9871-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 8 | SOLR-9866 | deferred-own-host (keep-open) | not checked (own host) | `cc3f19c8ef8c` (solr-9866-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 9 | SOLR-9841 | suspect (keep-open) | none | `e5c510910c11` (solr-9841-draft-open) | no closeout claim in claims/ names this ticket; the record says JIRA was not re-read in this pass; no source file path cited; the record says no reproduction was attempted |
| 10 | SOLR-9830 | deferred-own-host (close-fixed) | not checked (own host) | `d88c84dbe1c9` (solr-9830-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 11 | SOLR-9826 | deferred-own-host (close-fixed) | not checked (own host) | `5eff8ec78476` (solr-9826-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 12 | SOLR-9825 | deferred-own-host (keep-open) | not checked (own host) | `1c06c32bf2f6` (solr-9825-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 13 | SOLR-9809 | deferred-own-host (keep-open) | not checked (own host) | `e25ab9e09488` (solr-9809-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 14 | SOLR-9806 | suspect (keep-open) | none | `9b3c05875aef` (solr-9806-draft-open) | no closeout claim in claims/ names this ticket; the record says JIRA was not re-read in this pass; no source file path cited; the record says no reproduction was attempted |
| 15 | SOLR-9791 | deferred-own-host (keep-open) | not checked (own host) | `635ef4f133d7` (solr-9791-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 16 | SOLR-9769 | deferred-own-host (keep-open) | not checked (own host) | `b5263bd2bac8` (solr-9769-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 17 | SOLR-9765 | suspect (keep-open) | none | `55b6a4d098f2` (solr-9765-draft-open) | no closeout claim in claims/ names this ticket; the record says JIRA was not re-read in this pass; no source file path cited; the record says no reproduction was attempted |
| 18 | SOLR-9754 | deferred-own-host (close-obsolete) | not checked (own host) | `bb98bdd4999c` (solr-9754-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 19 | SOLR-9749 | deferred-own-host (close-obsolete) | not checked (own host) | `1ea1255d814a` (solr-9749-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 20 | SOLR-9741 | deferred-own-host (close-obsolete) | not checked (own host) | `2d65cd331829` (solr-9741-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 21 | SOLR-9723 | deferred-own-host (keep-open) | not checked (own host) | `001ee84d9c9a` (solr-9723-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 22 | SOLR-9722 | suspect (keep-open) | none | `7bbd15a74b99` (solr-9722-draft-open) | no closeout claim in claims/ names this ticket; the record says JIRA was not re-read in this pass; no source file path cited; the record says no reproduction was attempted |
| 23 | SOLR-9713 | deferred-own-host (keep-open) | not checked (own host) | `9475cd2b5ee4` (solr-9713-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 24 | SOLR-9677 | deferred-own-host (close-fixed) | not checked (own host) | `b0ea0ee2154a` (solr-9677-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 25 | SOLR-9664 | suspect (keep-open) | none | `f0c4a42f968f` (solr-9664-draft-open) | no closeout claim in claims/ names this ticket; the record says JIRA was not re-read in this pass; no source file path cited; the record says no reproduction was attempted |

Count: pass 0, suspect 5, deferred 20.

## Questions for the vm1 watch

1. The remediation-current.json question from batch 04 still stands. The 5 suspects are not appended to `material/skip-audit/remediation-current.json`, because the batch assignment says auditors do not edit it while `pool/cron-prompt.md` says the pool maintains it.
2. Every record in this batch says the Jira status was not re-read in the pass that wrote it. Records need a Jira read on the current pass before any of them can pass.
