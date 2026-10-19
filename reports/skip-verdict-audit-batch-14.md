# Skip verdict cross-audit, batch 14 of 21

Assignment: `assignments/pool-aab-skip-verdict-audit-14.md`. Auditor: vm2, 2026-10-19, claim `claims/pool-aab-skip-verdict-audit-14.md`. Each item was checked against the floor in `material/skip-audit/verdict-rubric.md`: a closeout claim in `claims/` naming the ticket with a DONE line, the named draft branch on the fork with `drafts/<KEY>/record.md` and `jira-comment.md`, the pinned head the record cites (matched to `evidence_head` in `material/skip-audit/queue-current.json`, which is `498a09aea97a` for every item here), the Jira status read in that pass, a code path with file and line or a reproduction, a substantive record, and a verdict whose draft suffix matches.

Checks run on the fork at tip `984fbc1ebf7`. Every draft branch exists with its suffix matching the recorded verdict, both files are present for all 25, and every record cites the pinned head. One item passes: SOLR-5941 has a vm5 closeout with a DONE line, a Jira read dated 2026-10-19, file-and-line code paths, and a keep-open verdict that names the missing design decision. Eight are suspects. SOLR-5939 has a vm4 closeout, but its pr-needed verdict is not backed by a reproduction, as the rubric requires. Seven suspects have no closeout claim with a DONE line and say JIRA was not re-read in this pass.

Own-host check: 16 items are `deferred-own-host`. Fifteen records state "Checked by: vm2" for the reading they cite. SOLR-6287 cites the seed of vm2's own repro round (`claims/pool-skip-repro-flake-round-1.md`, seed 5D0AB02F48FB5320); this host did not audit it.

| # | Ticket | Result | Claim found | Draft branch head | Missing |
| --- | --- | --- | --- | --- | --- |
| 1 | SOLR-6525 | deferred-own-host (keep-open) | not checked (own host) | `404f03c49fe2` (solr-6525-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 2 | SOLR-6468 | deferred-own-host (close-obsolete) | not checked (own host) | `fa124d2da00e` (solr-6468-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 3 | SOLR-6429 | deferred-own-host (keep-open) | not checked (own host) | `e2d1577c7498` (solr-6429-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 4 | SOLR-6395 | deferred-own-host (keep-open) | not checked (own host) | `5e6699a2993a` (solr-6395-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 5 | SOLR-6366 | deferred-own-host (close-fixed) | not checked (own host) | `779be63c9204` (solr-6366-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 6 | SOLR-6364 | suspect (keep-open) | none naming the ticket as a closeout | `d23993bb599a` (solr-6364-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 7 | SOLR-6335 | suspect (keep-open) | none naming the ticket as a closeout | `e1fed69be912` (solr-6335-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 8 | SOLR-6334 | deferred-own-host (keep-open) | not checked (own host) | `70dcbd815d09` (solr-6334-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 9 | SOLR-6303 | deferred-own-host (close-obsolete) | not checked (own host) | `a1af25037aac` (solr-6303-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 10 | SOLR-6287 | deferred-own-host (keep-open) | not checked (own host) | `a5a387d10580` (solr-6287-draft-open) | the record's reproduction seed is the seed of a vm2 round; not audited by this host |
| 11 | SOLR-6255 | deferred-own-host (keep-open) | not checked (own host) | `620926f511e5` (solr-6255-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 12 | SOLR-6199 | suspect (keep-open) | none naming the ticket as a closeout | `3042e38ce127` (solr-6199-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 13 | SOLR-6184 | deferred-own-host (keep-open) | not checked (own host) | `aafe064f02eb` (solr-6184-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 14 | SOLR-6166 | deferred-own-host (close-obsolete) | not checked (own host) | `46143907c1fe` (solr-6166-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 15 | SOLR-6142 | deferred-own-host (close-obsolete) | not checked (own host) | `938f29c961be` (solr-6142-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 16 | SOLR-6099 | suspect (keep-open) | none naming the ticket as a closeout | `c6b433172489` (solr-6099-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 17 | SOLR-6004 | suspect (keep-open) | none naming the ticket as a closeout | `2b5bd9837ef4` (solr-6004-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 18 | SOLR-5995 | deferred-own-host (keep-open) | not checked (own host) | `9bf3c849de96` (solr-5995-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 19 | SOLR-5990 | deferred-own-host (keep-open) | not checked (own host) | `a10972c1ced1` (solr-5990-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 20 | SOLR-5962 | suspect (keep-open) | none naming the ticket as a closeout | `07f59065725b` (solr-5962-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 21 | SOLR-5948 | suspect (keep-open) | none naming the ticket as a closeout | `370259e1380d` (solr-5948-draft-open) | no closeout claim with a DONE line names this ticket; the record says JIRA was not re-read in this pass; no source file path with a line number cited; no reproduction attempt recorded |
| 22 | SOLR-5941 | pass (keep-open) | skip-SOLR-5941-vm5.md (vm5 closeout, DONE) | `2577f472eb2d` (solr-5941-draft-open) | none |
| 23 | SOLR-5939 | suspect (pr-needed) | skip-SOLR-5939-vm4.md (vm4 closeout, DONE) | `07e223e1e9d6` (solr-5939-draft-pr) | the code-path minimum is met (file and line, StreamingSolrClients.java:66-78 and :131-146), but the pr-needed row of the rubric says the draft says the problem was reproduced; the record says no test was run and is not conclusive on reproduction |
| 24 | SOLR-5919 | deferred-own-host (keep-open) | not checked (own host) | `c3bcee01e058` (solr-5919-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 25 | SOLR-5878 | deferred-own-host (keep-open) | not checked (own host) | `1e336dde7e7c` (solr-5878-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |

Count: pass 1, suspect 8, deferred 16.

## Questions for the vm1 watch

1. The remediation-current.json question from batch 04 still stands. The 8 suspects are not appended to `material/skip-audit/remediation-current.json`, because the batch assignment says auditors do not edit it while `pool/cron-prompt.md` says the pool maintains it.
2. The rubric's pr-needed row says the draft says the problem was reproduced, but its minimum-evidence line accepts a code-path argument with the line that causes the failure. SOLR-5939 meets the second and not the first. Decide which governs.
3. Several records in this batch say the Jira status was not re-read in the pass that wrote them. SOLR-5941 read it on 2026-10-19. Records need a Jira read on the current pass before they can pass.
