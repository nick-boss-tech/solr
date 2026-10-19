# Skip verdict cross-audit, batch 7 of 21

Assignment: `assignments/pool-aab-skip-verdict-audit-07.md`. Auditor: vm2, 2026-10-19, claim `claims/pool-aab-skip-verdict-audit-07.md`. Each item was checked against the floor in `material/skip-audit/verdict-rubric.md`: a closeout claim in `claims/` naming the ticket with a DONE line, the named draft branch on the fork with `drafts/<KEY>/record.md` and `jira-comment.md`, the pinned head the record cites (matched to `evidence_head` in `material/skip-audit/queue-current.json`, which is `498a09aea97a` for every item here), the Jira status read in that pass, a code path with file and line or a reproduction, a substantive record, and a verdict whose draft suffix matches.

Checks run on the fork at tip `6bb6cf8763d`. Every draft branch exists with its suffix matching the recorded verdict, both files are present for all 25, and every record cites the pinned head. No item passed: every record says "JIRA was not re-read for this record", and no file in `claims/` holds a closeout claim with a DONE line for any of the 25 tickets.

Own-host check: 13 records state "Checked by: vm2" for the reading they cite. Items SOLR-9328 and SOLR-9144 cite reproduction seeds (1B3B3071A669191C and C216E516B8C5627A) from vm2's own repro round `claims/pool-skip-repro-flake-round-1.md`. These 15 items are `deferred-own-host`; this host did not audit them.

| # | Ticket | Result | Claim found | Draft branch head | Missing |
| --- | --- | --- | --- | --- | --- |
| 1 | SOLR-9328 | deferred-own-host (keep-open) | pool-skip-repro-flake-round-1.md (vm2 repro round, DONE) | `7b933f7d9233` (solr-9328-draft-open) | the record's reproduction seed is the vm2 repro round's seed; not audited by this host |
| 2 | SOLR-9312 | deferred-own-host (keep-open) | not checked (own host) | `0211e422a9de` (solr-9312-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 3 | SOLR-9267 | deferred-own-host (close-fixed) | not checked (own host) | `99ff95e7d886` (solr-9267-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 4 | SOLR-9260 | suspect (keep-open) | none naming the ticket as a closeout | `c99d6cc55398` (solr-9260-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 5 | SOLR-9250 | deferred-own-host (keep-open) | not checked (own host) | `04194c53f708` (solr-9250-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 6 | SOLR-9248 | deferred-own-host (close-obsolete) | not checked (own host) | `68eb8727fb3f` (solr-9248-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 7 | SOLR-9230 | suspect (keep-open) | none naming the ticket as a closeout | `91af64d29ce5` (solr-9230-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 8 | SOLR-9226 | suspect (keep-open) | none naming the ticket as a closeout | `7afaef1bdcb8` (solr-9226-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 9 | SOLR-9223 | suspect (keep-open) | none naming the ticket as a closeout | `47ddd7f348b5` (solr-9223-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 10 | SOLR-9214 | suspect (keep-open) | none naming the ticket as a closeout | `6bb9e29eb34c` (solr-9214-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 11 | SOLR-9213 | deferred-own-host (keep-open) | not checked (own host) | `deecb09a844f` (solr-9213-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 12 | SOLR-9208 | deferred-own-host (keep-open) | not checked (own host) | `48a45573c0fd` (solr-9208-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 13 | SOLR-9206 | deferred-own-host (keep-open) | not checked (own host) | `169cf0934883` (solr-9206-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 14 | SOLR-9197 | suspect (keep-open) | none naming the ticket as a closeout | `d503325df1c8` (solr-9197-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 15 | SOLR-9183 | deferred-own-host (close-obsolete) | not checked (own host) | `19ff1e967724` (solr-9183-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 16 | SOLR-9182 | suspect (keep-open) | pool-skip-repro-flake-locate-round-1.md (repro round, not a closeout; ticket recorded as not run) | `f995c3e23d9e` (solr-9182-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 17 | SOLR-9174 | suspect (keep-open) | none naming the ticket as a closeout | `aa4424eba297` (solr-9174-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 18 | SOLR-9173 | deferred-own-host (keep-open) | not checked (own host) | `3be5a641e965` (solr-9173-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 19 | SOLR-9144 | deferred-own-host (keep-open) | pool-skip-repro-flake-round-1.md (vm2 repro round, DONE) | `a39f94b6d36c` (solr-9144-draft-open) | the record's reproduction seed is the vm2 repro round's seed; not audited by this host |
| 20 | SOLR-9129 | suspect (keep-open) | none naming the ticket as a closeout | `980c63a74fe4` (solr-9129-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 21 | SOLR-9126 | deferred-own-host (close-fixed) | not checked (own host) | `09881bcd1ada` (solr-9126-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 22 | SOLR-9123 | suspect (keep-open) | none naming the ticket as a closeout | `0ee1e297bab5` (solr-9123-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 23 | SOLR-9122 | deferred-own-host (keep-open) | not checked (own host) | `8994b9ee1f36` (solr-9122-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 24 | SOLR-9094 | deferred-own-host (close-fixed) | not checked (own host) | `02bd7324ad3b` (solr-9094-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 25 | SOLR-9089 | deferred-own-host (keep-open) | not checked (own host) | `c4b1d7cdc30f` (solr-9089-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |

Count: pass 0, suspect 10, deferred 15.

## Questions for the vm1 watch

1. The remediation-current.json question from batch 04 still stands. The 10 suspects are not appended to `material/skip-audit/remediation-current.json`, because the batch assignment says auditors do not edit it while `pool/cron-prompt.md` says the pool maintains it.
2. Every record in this batch says the Jira status was not re-read in the pass that wrote it. Records need a Jira read on the current pass before any of them can pass.
