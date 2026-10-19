# Skip verdict cross-audit, batch 12 of 21

Assignment: `assignments/pool-aab-skip-verdict-audit-12.md`. Auditor: vm2, 2026-10-19, claim `claims/pool-aab-skip-verdict-audit-12.md`. Each item was checked against the floor in `material/skip-audit/verdict-rubric.md`: a closeout claim in `claims/` naming the ticket with a DONE line, the named draft branch on the fork with `drafts/<KEY>/record.md` and `jira-comment.md`, the pinned head the record cites (matched to `evidence_head` in `material/skip-audit/queue-current.json`, which is `498a09aea97a` for every item here), the Jira status read in that pass, a code path with file and line or a reproduction, a substantive record, and a verdict whose draft suffix matches.

Checks run on the fork at tip `b9c02aa087f`. Every draft branch exists with its suffix matching the recorded verdict, both files are present for all 25, and every record cites the pinned head. No item passed: no claim in `claims/` is a closeout with a DONE line for any of the 25 tickets, and every record says JIRA was not re-read for this record.

Own-host check: 11 records state "Checked by: vm2" for the reading they cite. These items are `deferred-own-host`; this host did not audit them.

| # | Ticket | Result | Claim found | Draft branch head | Missing |
| --- | --- | --- | --- | --- | --- |
| 1 | SOLR-7257 | suspect (keep-open) | none naming the ticket as a closeout | `fed8e5b43f5c` (solr-7257-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 2 | SOLR-7249 | deferred-own-host (keep-open) | not checked (own host) | `9160f67e903a` (solr-7249-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 3 | SOLR-7247 | suspect (keep-open) | none naming the ticket as a closeout | `4f67d8bb0fef` (solr-7247-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 4 | SOLR-7235 | suspect (keep-open) | none naming the ticket as a closeout | `97528e787f5d` (solr-7235-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 5 | SOLR-7221 | deferred-own-host (close-obsolete) | not checked (own host) | `ae93f3d345cb` (solr-7221-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 6 | SOLR-7198 | suspect (keep-open) | none naming the ticket as a closeout | `b059accce1a0` (solr-7198-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 7 | SOLR-7192 | deferred-own-host (keep-open) | not checked (own host) | `374a66023f09` (solr-7192-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 8 | SOLR-7153 | deferred-own-host (close-fixed) | not checked (own host) | `4b9958b3fa4a` (solr-7153-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 9 | SOLR-7149 | suspect (keep-open) | none naming the ticket as a closeout | `086b982f92d4` (solr-7149-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 10 | SOLR-7088 | deferred-own-host (close-fixed) | not checked (own host) | `7a5e73dd8a20` (solr-7088-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 11 | SOLR-7075 | suspect (keep-open) | none naming the ticket as a closeout | `f81877f4d105` (solr-7075-draft-open) | no closeout claim with a DONE line names this ticket; JIRA was not re-read in this pass; no source file path with a line number cited; the one reproduction run passed, the record calls it inconclusive, and the rerun was not run |
| 12 | SOLR-7071 | suspect (keep-open) | none naming the ticket as a closeout | `b1d5bbd81a48` (solr-7071-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 13 | SOLR-7070 | suspect (keep-open) | none naming the ticket as a closeout | `a7e51f23d3ad` (solr-7070-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 14 | SOLR-7069 | suspect (keep-open) | none naming the ticket as a closeout | `4110376c82b0` (solr-7069-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 15 | SOLR-7035 | suspect (keep-open) | none naming the ticket as a closeout | `3965602fbd22` (solr-7035-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 16 | SOLR-7009 | deferred-own-host (close-fixed) | not checked (own host) | `ca2b548111a8` (solr-7009-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 17 | SOLR-7000 | suspect (keep-open) | none naming the ticket as a closeout | `c8901f596247` (solr-7000-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 18 | SOLR-6977 | suspect (keep-open) | none naming the ticket as a closeout | `86c2ae18bd81` (solr-6977-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 19 | SOLR-6934 | deferred-own-host (keep-open) | not checked (own host) | `7dc68091cc53` (solr-6934-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 20 | SOLR-6929 | deferred-own-host (keep-open) | not checked (own host) | `f1a76cbf5505` (solr-6929-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 21 | SOLR-6922 | deferred-own-host (keep-open) | not checked (own host) | `e5a98494ebb1` (solr-6922-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 22 | SOLR-6914 | suspect (keep-open) | none naming the ticket as a closeout | `971555af3f68` (solr-6914-draft-open) | no closeout claim with a DONE line names this ticket; JIRA was not re-read in this pass; no source file path with a line number cited; the one reproduction run passed, the record calls it inconclusive, and the rerun was not run |
| 23 | SOLR-6911 | suspect (keep-open) | none naming the ticket as a closeout | `27bd51a2a762` (solr-6911-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 24 | SOLR-6903 | deferred-own-host (close-fixed) | not checked (own host) | `5c74cba86e34` (solr-6903-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 25 | SOLR-6890 | deferred-own-host (close-obsolete) | not checked (own host) | `78eda753d48b` (solr-6890-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |

Count: pass 0, suspect 14, deferred 11.

## Questions for the vm1 watch

1. The remediation-current.json question from batch 04 still stands. The 14 suspects are not appended to `material/skip-audit/remediation-current.json`, because the batch assignment says auditors do not edit it while `pool/cron-prompt.md` says the pool maintains it.
2. Every record in this batch says the Jira status was not re-read in the pass that wrote it. Records need a Jira read on the current pass before they can pass.
