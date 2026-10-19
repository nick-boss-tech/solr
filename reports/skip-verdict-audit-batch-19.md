# Skip verdict cross-audit, batch 19 of 21

Assignment: `assignments/pool-aab-skip-verdict-audit-19.md`. Auditor: vm2, 2026-10-19, claim `claims/pool-aab-skip-verdict-audit-19.md`. Each item was checked against the floor in `material/skip-audit/verdict-rubric.md`: a closeout claim in `claims/` naming the ticket with a DONE line, the named draft branch on the fork with `drafts/<KEY>/record.md` and `jira-comment.md`, the pinned head the record cites (matched to `evidence_head` in `material/skip-audit/queue-current.json`, which is `498a09aea97a` for every item here), the Jira status read in that pass, a code path with file and line or a reproduction, a substantive record, and a verdict whose draft suffix matches.

Checks run on the fork at tip `390d9f74d71`. Every draft branch exists with its suffix matching the recorded verdict, both files are present for all 25, and every record cites the pinned head. No item passed: no claim in `claims/` is a closeout with a DONE line for any of the 13 suspects.

Own-host check: 12 items are `deferred-own-host`. 12 records state "Checked by: vm2" for the reading they cite. 0 record a reproduction seed from vm2's own repro round (`claims/pool-skip-repro-flake-round-1.md`); this host did not audit them.

| # | Ticket | Result | Claim found | Draft branch head | Missing |
| --- | --- | --- | --- | --- | --- |
| 1 | SOLR-3765 | suspect (keep-open) | none naming the ticket as a closeout | `577cb3b4ae77` (solr-3765-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 2 | SOLR-3744 | suspect (keep-open) | none naming the ticket as a closeout | `e89d3241671d` (solr-3744-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 3 | SOLR-3742 | deferred-own-host (keep-open) | not checked (own host) | `247dac93cc24` (solr-3742-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 4 | SOLR-3741 | deferred-own-host (close-fixed) | not checked (own host) | `ce5761e55a2b` (solr-3741-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 5 | SOLR-3740 | deferred-own-host (close-fixed) | not checked (own host) | `fac617c0d024` (solr-3740-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 6 | SOLR-3739 | deferred-own-host (close-fixed) | not checked (own host) | `7be45ce0ebd2` (solr-3739-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 7 | SOLR-3721 | deferred-own-host (keep-open) | not checked (own host) | `2e505b1f34e2` (solr-3721-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 8 | SOLR-3716 | deferred-own-host (keep-open) | not checked (own host) | `c873fd3fc6b9` (solr-3716-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 9 | SOLR-3697 | suspect (close-fixed) | none naming the ticket as a closeout | `d605adb758f8` (solr-3697-draft-close) | no closeout claim with a DONE line names this ticket; no reproduction attempt recorded |
| 10 | SOLR-3694 | suspect (close-fixed) | none naming the ticket as a closeout | `f5d658d0b696` (solr-3694-draft-close) | no closeout claim with a DONE line names this ticket; no source file path with a line number cited; no reproduction attempt recorded |
| 11 | SOLR-3693 | suspect (close-fixed) | none naming the ticket as a closeout | `959f9f7df44f` (solr-3693-draft-close) | no closeout claim with a DONE line names this ticket; no reproduction attempt recorded |
| 12 | SOLR-3692 | deferred-own-host (keep-open) | not checked (own host) | `34c8c5e35273` (solr-3692-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 13 | SOLR-3676 | suspect (keep-open) | none naming the ticket as a closeout | `bb71ca7734ec` (solr-3676-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 14 | SOLR-3605 | deferred-own-host (keep-open) | not checked (own host) | `8e8259dac8e8` (solr-3605-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 15 | SOLR-3576 | deferred-own-host (keep-open) | not checked (own host) | `73c8737f3eb7` (solr-3576-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 16 | SOLR-3539 | suspect (keep-open) | none naming the ticket as a closeout | `8a0ef2a02209` (solr-3539-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 17 | SOLR-3523 | deferred-own-host (keep-open) | not checked (own host) | `af382b681e86` (solr-3523-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 18 | SOLR-3473 | suspect (keep-open) | none naming the ticket as a closeout | `3819c021b328` (solr-3473-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 19 | SOLR-3461 | suspect (keep-open) | none naming the ticket as a closeout | `910d2b8b9fb0` (solr-3461-draft-open) | no closeout claim with a DONE line names this ticket; JIRA was not re-read in this pass; no source file path with a line number cited; the one reproduction run passed, the record calls it inconclusive, and the rerun was not run |
| 20 | SOLR-3452 | deferred-own-host (keep-open) | not checked (own host) | `3b6ddd0977a3` (solr-3452-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 21 | SOLR-3447 | suspect (keep-open) | none naming the ticket as a closeout | `24ce4bd37dbc` (solr-3447-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 22 | SOLR-3408 | suspect (close-fixed) | none naming the ticket as a closeout | `fc6e8a2c771f` (solr-3408-draft-close) | no closeout claim with a DONE line names this ticket; no reproduction attempt recorded |
| 23 | SOLR-3407 | suspect (keep-open) | none naming the ticket as a closeout | `5db7473f9e29` (solr-3407-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 24 | SOLR-3380 | deferred-own-host (keep-open) | not checked (own host) | `c8fbb4f7978c` (solr-3380-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 25 | SOLR-3220 | suspect (keep-open) | pool-skip-repro-flake-round-1.md (mention the ticket; none is a closeout with a DONE line) | `e3d4dcf98eec` (solr-3220-draft-open) | no closeout claim with a DONE line names this ticket; JIRA was not re-read in this pass; no source file path with a line number cited; the one reproduction run passed, the record calls it inconclusive, and the rerun was not run |

Count: pass 0, suspect 13, deferred 12.

## Questions for the vm1 watch

1. The remediation-current.json question from batch 04 still stands. The 13 suspects are not appended to `material/skip-audit/remediation-current.json`, because the batch assignment says auditors do not edit it while `pool/cron-prompt.md` says the pool maintains it.
2. Most records in this batch say the Jira status was not re-read in the pass that wrote them. Records need a Jira read on the current pass before they can pass.
