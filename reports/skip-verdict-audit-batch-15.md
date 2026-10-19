# Skip verdict cross-audit, batch 15 of 21

Assignment: `assignments/pool-aab-skip-verdict-audit-15.md`. Auditor: vm2, 2026-10-19, claim `claims/pool-aab-skip-verdict-audit-15.md`. Each item was checked against the floor in `material/skip-audit/verdict-rubric.md`: a closeout claim in `claims/` naming the ticket with a DONE line, the named draft branch on the fork with `drafts/<KEY>/record.md` and `jira-comment.md`, the pinned head the record cites, the Jira status read in that pass, a code path with file and line or a reproduction, a substantive record, and a verdict whose draft suffix matches.

Checks run on the fork and on the coordination branch at tip `8d800c66c30`. All 25 draft branches exist with the suffix matching the recorded verdict. Every record except SOLR-5516 cites the pinned head `498a09aea97a`, and every record is between 207 and 776 words. One item passes. Eight items are suspect. Sixteen items are deferred-own-host.

Own-host check: 13 records state "Checked by: vm2" for the reading they cite. SOLR-5791, SOLR-5792 and SOLR-5793 are named in `claims/pool-skip-repro-flake-round-1.md`, whose claimant is vm2. These 16 are `deferred-own-host`; this host did not audit them.

| # | Ticket | Result | Claim found | Draft branch head | Missing |
| --- | --- | --- | --- | --- | --- |
| 5850 | SOLR-5850 | deferred-own-host (close-obsolete) | not checked (own host) | `dbd7daf5b4cf` (solr-5850-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 5845 | SOLR-5845 | deferred-own-host (keep-open) | not checked (own host) | `4c7f98dc0f92` (solr-5845-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 5840 | SOLR-5840 | suspect (keep-open) | none naming the ticket as a closeout | `b7f663e1a7af` (solr-5840-draft-open) | no closeout claim with a DONE line names this ticket; no file-and-line code citation in the record |
| 5833 | SOLR-5833 | deferred-own-host (close-obsolete) | not checked (own host) | `7e49176e54fc` (solr-5833-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 5822 | SOLR-5822 | suspect (keep-open) | none naming the ticket as a closeout | `60879dbb191b` (solr-5822-draft-open) | no closeout claim with a DONE line names this ticket; no file-and-line code citation in the record |
| 5812 | SOLR-5812 | suspect (keep-open) | none naming the ticket as a closeout | `3f89063ffe94` (solr-5812-draft-open) | no closeout claim with a DONE line names this ticket; no file-and-line code citation in the record |
| 5793 | SOLR-5793 | deferred-own-host (keep-open) | claims/pool-skip-repro-flake-round-1.md (claimant vm2) | `e7dd69d8b6f9` (solr-5793-draft-open) | the closeout claim names vm2 as the claimant |
| 5792 | SOLR-5792 | deferred-own-host (keep-open) | claims/pool-skip-repro-flake-round-1.md (claimant vm2) | `fd6cc3643a04` (solr-5792-draft-open) | the closeout claim names vm2 as the claimant |
| 5791 | SOLR-5791 | deferred-own-host (keep-open) | claims/pool-skip-repro-flake-round-1.md (claimant vm2) | `ef86478b1959` (solr-5791-draft-open) | the closeout claim names vm2 as the claimant |
| 5788 | SOLR-5788 | deferred-own-host (keep-open) | not checked (own host) | `77246c1f979c` (solr-5788-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 5772 | SOLR-5772 | deferred-own-host (keep-open) | not checked (own host) | `6a81246366cf` (solr-5772-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 5759 | SOLR-5759 | deferred-own-host (keep-open) | not checked (own host) | `58179ca88cb8` (solr-5759-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 5736 | SOLR-5736 | deferred-own-host (keep-open) | not checked (own host) | `332699ba1cbf` (solr-5736-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 5719 | SOLR-5719 | suspect (keep-open) | none naming the ticket as a closeout | `b52a6903f7db` (solr-5719-draft-open) | no closeout claim with a DONE line names this ticket; no file-and-line code citation in the record |
| 5717 | SOLR-5717 | deferred-own-host (close-fixed) | not checked (own host) | `7efcef149224` (solr-5717-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 5712 | SOLR-5712 | deferred-own-host (close-fixed) | not checked (own host) | `dbdd15b37877` (solr-5712-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 5628 | SOLR-5628 | pass (keep-open) | claims/pool-vm5-skip-repro-16430-5628-round-1.md (DONE, names SOLR-5628) | `ed0f4e5a98be` (solr-5628-draft-open) | none |
| 5592 | SOLR-5592 | suspect (keep-open) | none naming the ticket as a closeout | `64ec6add168f` (solr-5592-draft-open) | no closeout claim with a DONE line names this ticket; no file-and-line code citation in the record |
| 5579 | SOLR-5579 | deferred-own-host (keep-open) | not checked (own host) | `1dd0cee6ec71` (solr-5579-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 5566 | SOLR-5566 | deferred-own-host (keep-open) | not checked (own host) | `0d94acd80b84` (solr-5566-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 5560 | SOLR-5560 | suspect (keep-open) | none naming the ticket as a closeout | `fb9fe362f5d3` (solr-5560-draft-open) | no closeout claim with a DONE line names this ticket; no file-and-line code citation in the record |
| 5559 | SOLR-5559 | deferred-own-host (close-fixed) | not checked (own host) | `c2a7db755760` (solr-5559-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 5516 | SOLR-5516 | suspect (keep-open) | claims/pool-skip-repro-5516-round-1.md (HANDED, not conclusive) | `f596a2426d6c` (solr-5516-draft-open) | the record does not cite the pinned head sha (it reads "Pinned head: fork origin/main ."); the claim was closed as HANDED, not conclusive |
| 5508 | SOLR-5508 | deferred-own-host (keep-open) | not checked (own host) | `757ea1417fc7` (solr-5508-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 5485 | SOLR-5485 | suspect (keep-open) | none naming the ticket as a closeout | `bd3dfe752702` (solr-5485-draft-open) | no closeout claim with a DONE line names this ticket; no file-and-line code citation in the record |

Count: pass 1, suspect 8, deferred 16.
