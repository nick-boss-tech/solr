# Skip verdict cross-audit, batch 20 of 21

Assignment: `assignments/pool-aab-skip-verdict-audit-20.md`. Auditor: vm2, 2026-10-19, claim `claims/pool-aab-skip-verdict-audit-20.md`. Each item was checked against the floor in `material/skip-audit/verdict-rubric.md`: a closeout claim in `claims/` naming the ticket with a DONE line, the named draft branch on the fork with `drafts/<KEY>/record.md` and `jira-comment.md`, the pinned head the record cites, the Jira status read in that pass, a code path with file and line or a reproduction, a substantive record, and a verdict whose draft suffix matches.

Checks run on the fork and on the coordination branch at tip `369d35bcf2d`. All 25 draft branches exist with the suffix matching the recorded verdict. Every record cites the pinned head `498a09aea97a`; four of them cite it as the 10-character prefix `498a09aea9`. Record lengths run from 182 to 375 words. No item passed. Thirteen items are suspect. Twelve items are deferred-own-host.

Own-host check: 11 records state "Checked by: vm2" for the reading they cite. SOLR-2865 is named in `claims/pool-skip-repro-flake-round-1.md`, whose claimant is vm2. These 12 are `deferred-own-host`; this host did not audit them.

| # | Ticket | Result | Claim found | Draft branch head | Missing |
| --- | --- | --- | --- | --- | --- |
| 3201 | SOLR-3201 | deferred-own-host (close-obsolete) | not checked (own host) | `822c6d0b1a80` (solr-3201-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 3128 | SOLR-3128 | suspect (keep-open) | none naming the ticket as a closeout | `cc0bdd06ebe7` (solr-3128-draft-open) | no closeout claim with a DONE line names this ticket; no file-and-line code citation in the record |
| 3124 | SOLR-3124 | deferred-own-host (keep-open) | not checked (own host) | `c4837d9898bc` (solr-3124-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 3064 | SOLR-3064 | deferred-own-host (close-obsolete) | not checked (own host) | `ad7cd43c0a8d` (solr-3064-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 3050 | SOLR-3050 | deferred-own-host (keep-open) | not checked (own host) | `0e3bc445ac1c` (solr-3050-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 3034 | SOLR-3034 | deferred-own-host (keep-open) | not checked (own host) | `edc00d2d5f84` (solr-3034-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 3027 | SOLR-3027 | deferred-own-host (keep-open) | not checked (own host) | `5ba72947a708` (solr-3027-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 3023 | SOLR-3023 | deferred-own-host (keep-open) | not checked (own host) | `8cbb274db6e1` (solr-3023-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 3008 | SOLR-3008 | suspect (keep-open) | none naming the ticket as a closeout | `9a5e63d30b42` (solr-3008-draft-open) | no closeout claim with a DONE line names this ticket; no file-and-line code citation in the record |
| 2976 | SOLR-2976 | deferred-own-host (close-fixed) | not checked (own host) | `9c40a4369cd6` (solr-2976-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 2936 | SOLR-2936 | suspect (close-fixed) | none naming the ticket as a closeout | `0167dd2ab307` (solr-2936-draft-close) | no closeout claim with a DONE line names this ticket; no file-and-line code citation in the record |
| 2928 | SOLR-2928 | suspect (keep-open) | none naming the ticket as a closeout | `d18ce99d0ee0` (solr-2928-draft-open) | no closeout claim with a DONE line names this ticket; no file-and-line code citation in the record |
| 2913 | SOLR-2913 | suspect (close-fixed) | none naming the ticket as a closeout | `744987e276dd` (solr-2913-draft-close) | no closeout claim with a DONE line names this ticket |
| 2900 | SOLR-2900 | suspect (keep-open) | none naming the ticket as a closeout | `9763def0481b` (solr-2900-draft-open) | no closeout claim with a DONE line names this ticket; no file-and-line code citation in the record |
| 2865 | SOLR-2865 | deferred-own-host (keep-open) | claims/pool-skip-repro-flake-round-1.md (claimant vm2) | `6ff98b8d8b9e` (solr-2865-draft-open) | the closeout claim names vm2 as the claimant |
| 2855 | SOLR-2855 | suspect (keep-open) | none naming the ticket as a closeout | `ea6cf464d66e` (solr-2855-draft-open) | no closeout claim with a DONE line names this ticket; no file-and-line code citation in the record |
| 2845 | SOLR-2845 | deferred-own-host (keep-open) | not checked (own host) | `54af01caec7f` (solr-2845-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 2751 | SOLR-2751 | deferred-own-host (keep-open) | not checked (own host) | `c941098bafd1` (solr-2751-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 2720 | SOLR-2720 | suspect (keep-open) | none naming the ticket as a closeout | `3a17804a5161` (solr-2720-draft-open) | no closeout claim with a DONE line names this ticket; no file-and-line code citation in the record |
| 2715 | SOLR-2715 | suspect (keep-open) | claims/pool-skip-repro-flake-locate-round-1.md (DONE, records SOLR-2715 as not run) | `2ab78984c2f3` (solr-2715-draft-open) | no closeout claim names this ticket; the only claim records it as not run |
| 2516 | SOLR-2516 | suspect (keep-open) | none naming the ticket as a closeout | `fa3d2aaf34ba` (solr-2516-draft-open) | no closeout claim with a DONE line names this ticket; no file-and-line code citation in the record |
| 2507 | SOLR-2507 | deferred-own-host (keep-open) | not checked (own host) | `0515aae95b6f` (solr-2507-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 2435 | SOLR-2435 | suspect (close-fixed) | none naming the ticket as a closeout | `9a493cb3110d` (solr-2435-draft-close) | no closeout claim with a DONE line names this ticket; no file-and-line code citation in the record |
| 2326 | SOLR-2326 | suspect (keep-open) | claims/skip-SOLR-2326-vm4.md (DONE, vm4) | `330a9bc4405d` (solr-2326-draft-open) | the code path cites TestReplicationHandler.java with no line number |
| 2274 | SOLR-2274 | suspect (close-fixed) | none naming the ticket as a closeout | `22e8c7874bca` (solr-2274-draft-close) | no closeout claim with a DONE line names this ticket |

Count: pass 0, suspect 13, deferred 12.
