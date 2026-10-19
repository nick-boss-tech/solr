# Skip verdict cross-audit, batch 13 of 21

Assignment: `assignments/pool-aab-skip-verdict-audit-13.md`. Auditor: vm2, 2026-10-19, claim `claims/pool-aab-skip-verdict-audit-13.md`. Each item was checked against the floor in `material/skip-audit/verdict-rubric.md`: a closeout claim in `claims/` naming the ticket with a DONE line, the named draft branch on the fork with `drafts/<KEY>/record.md` and `jira-comment.md`, the pinned head the record cites (matched to `evidence_head` in `material/skip-audit/queue-current.json`, which is `498a09aea97a` for every item here), the Jira status read in that pass, a code path with file and line or a reproduction, a substantive record, and a verdict whose draft suffix matches.

Checks run on the fork at tip `3a70204e82d`. Every draft branch exists with its suffix matching the recorded verdict, both files are present for all 25, and every record cites the pinned head. No item passed: no claim in `claims/` is a closeout with a DONE line for any of the 25 tickets, and every record says JIRA was not re-read for this record.

Own-host check: 13 records state "Checked by: vm2" for the reading they cite. These items are `deferred-own-host`; this host did not audit them.

| # | Ticket | Result | Claim found | Draft branch head | Missing |
| --- | --- | --- | --- | --- | --- |
| 1 | SOLR-6875 | deferred-own-host (keep-open) | not checked (own host) | `8955e1468d40` (solr-6875-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 2 | SOLR-6846 | suspect (keep-open) | none naming the ticket as a closeout | `c9fdae5e7bf8` (solr-6846-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 3 | SOLR-6837 | suspect (keep-open) | none naming the ticket as a closeout | `992cef0da79f` (solr-6837-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 4 | SOLR-6836 | deferred-own-host (keep-open) | not checked (own host) | `b37f202c64e2` (solr-6836-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 5 | SOLR-6828 | deferred-own-host (keep-open) | not checked (own host) | `0a25bef15618` (solr-6828-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 6 | SOLR-6822 | deferred-own-host (close-fixed) | not checked (own host) | `006fd830d4a5` (solr-6822-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 7 | SOLR-6813 | suspect (keep-open) | none naming the ticket as a closeout | `93bdac312cb4` (solr-6813-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 8 | SOLR-6811 | deferred-own-host (close-obsolete) | not checked (own host) | `e0132f562db2` (solr-6811-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 9 | SOLR-6785 | suspect (keep-open) | none naming the ticket as a closeout | `ade8534f2c84` (solr-6785-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 10 | SOLR-6782 | deferred-own-host (close-obsolete) | not checked (own host) | `fe3c565566f4` (solr-6782-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 11 | SOLR-6756 | deferred-own-host (close-obsolete) | not checked (own host) | `978256ae1bf5` (solr-6756-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 12 | SOLR-6719 | suspect (keep-open) | none naming the ticket as a closeout | `857efe97a4f4` (solr-6719-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 13 | SOLR-6717 | suspect (keep-open) | none naming the ticket as a closeout | `0a909a3b09df` (solr-6717-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 14 | SOLR-6714 | suspect (keep-open) | none naming the ticket as a closeout | `0c63df08f6bf` (solr-6714-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 15 | SOLR-6712 | deferred-own-host (keep-open) | not checked (own host) | `9c0a6155d0c1` (solr-6712-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 16 | SOLR-6678 | deferred-own-host (keep-open) | not checked (own host) | `ceab613c82e6` (solr-6678-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 17 | SOLR-6649 | suspect (keep-open) | none naming the ticket as a closeout | `c01982d0834d` (solr-6649-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 18 | SOLR-6621 | suspect (keep-open) | none naming the ticket as a closeout | `785ec365e761` (solr-6621-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 19 | SOLR-6593 | suspect (keep-open) | none naming the ticket as a closeout | `3d7c333e3ec5` (solr-6593-draft-open) | no closeout claim with a DONE line names this ticket; JIRA was not re-read in this pass; no source file path with a line number cited; the one reproduction run passed, the record calls it inconclusive, and the rerun was not run |
| 20 | SOLR-6588 | deferred-own-host (close-answered) | not checked (own host) | `d7eee4d10e28` (solr-6588-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 21 | SOLR-6582 | deferred-own-host (close-obsolete) | not checked (own host) | `b21293044a5c` (solr-6582-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 22 | SOLR-6576 | suspect (keep-open) | none naming the ticket as a closeout | `a8d304720691` (solr-6576-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 23 | SOLR-6561 | deferred-own-host (close-obsolete) | not checked (own host) | `fd927b0811cd` (solr-6561-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 24 | SOLR-6537 | suspect (keep-open) | none naming the ticket as a closeout | `6f32fa127d94` (solr-6537-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 25 | SOLR-6534 | deferred-own-host (close-fixed) | not checked (own host) | `049118671bea` (solr-6534-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |

Count: pass 0, suspect 12, deferred 13.

## Questions for the vm1 watch

1. The remediation-current.json question from batch 04 still stands. The 12 suspects are not appended to `material/skip-audit/remediation-current.json`, because the batch assignment says auditors do not edit it while `pool/cron-prompt.md` says the pool maintains it.
2. Every record in this batch says the Jira status was not re-read in the pass that wrote it. Records need a Jira read on the current pass before they can pass.
