# Skip verdict cross-audit, batch 10 of 21

Amended 2026-10-19: SOLR-7992 was first marked suspect. Its claim `claims/pool-skip-repro-flake-round-1.md` names vm2 as claimant, so it is deferred-own-host. Count corrected below.

Assignment: `assignments/pool-aab-skip-verdict-audit-10.md`. Auditor: vm2, 2026-10-19, claim `claims/pool-aab-skip-verdict-audit-10.md`. Each item was checked against the floor in `material/skip-audit/verdict-rubric.md`: a closeout claim in `claims/` naming the ticket with a DONE line, the named draft branch on the fork with `drafts/<KEY>/record.md` and `jira-comment.md`, the pinned head the record cites, the Jira status read in that pass, a code path with file and line or a reproduction, a substantive record, and a verdict whose draft suffix matches.

Checks run on the fork and on the coordination branch at tip `03848f346df`. All 25 draft branches exist with the suffix matching the recorded verdict. Every record cites the pinned head `498a09aea97a` and is between 206 and 365 words. No item passed. Six items have no closeout claim with a DONE line naming the ticket, and their records contain no file-and-line code citation. SOLR-7992 has a claim, `claims/pool-skip-repro-flake-round-1.md`, but it names vm2 as claimant, so it is own-host.

Own-host check: 18 records state "Checked by: vm2" for the reading they cite. These 18, and SOLR-7992, are `deferred-own-host`; this host did not audit them. This follows the same reading as batch 09.

| # | Ticket | Result | Claim found | Draft branch head | Missing |
| --- | --- | --- | --- | --- | --- |
| 8171 | SOLR-8171 | deferred-own-host (keep-open) | not checked (own host) | `941e89c854b6` (solr-8171-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 8158 | SOLR-8158 | deferred-own-host (close-obsolete) | not checked (own host) | `b01cbde9a961` (solr-8158-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 8117 | SOLR-8117 | deferred-own-host (close-obsolete) | not checked (own host) | `e5ba0ce9806f` (solr-8117-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 8106 | SOLR-8106 | deferred-own-host (keep-open) | not checked (own host) | `fba476f033a5` (solr-8106-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 8098 | SOLR-8098 | deferred-own-host (keep-open) | not checked (own host) | `7752d5c7b715` (solr-8098-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 8089 | SOLR-8089 | deferred-own-host (close-obsolete) | not checked (own host) | `d1a269088a59` (solr-8089-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 8087 | SOLR-8087 | deferred-own-host (close-obsolete) | not checked (own host) | `145de2d0aeba` (solr-8087-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 8079 | SOLR-8079 | deferred-own-host (keep-open) | not checked (own host) | `3e413e646482` (solr-8079-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 8076 | SOLR-8076 | deferred-own-host (close-obsolete) | not checked (own host) | `f465f5f1b94e` (solr-8076-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 8067 | SOLR-8067 | suspect (keep-open) | none naming the ticket as a closeout | `0e839f19bd8d` (solr-8067-draft-open) | no closeout claim with a DONE line names this ticket; no file-and-line code citation found in the record |
| 8030 | SOLR-8030 | suspect (keep-open) | none naming the ticket as a closeout | `5f68ccb00bae` (solr-8030-draft-open) | no closeout claim with a DONE line names this ticket; no file-and-line code citation found in the record |
| 8024 | SOLR-8024 | deferred-own-host (close-fixed) | not checked (own host) | `44e1dba34a91` (solr-8024-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 8005 | SOLR-8005 | suspect (keep-open) | none naming the ticket as a closeout | `ecd7a6a396d9` (solr-8005-draft-open) | no closeout claim with a DONE line names this ticket; no file-and-line code citation found in the record |
| 7998 | SOLR-7998 | deferred-own-host (keep-open) | not checked (own host) | `735006832862` (solr-7998-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 7992 | SOLR-7992 | deferred-own-host (keep-open) | claims/pool-skip-repro-flake-round-1.md (claimant vm2) | `afbcf4192234` (solr-7992-draft-open) | the closeout claim names vm2 as the claimant |
| 7982 | SOLR-7982 | deferred-own-host (keep-open) | not checked (own host) | `eefa3d2797c7` (solr-7982-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 7973 | SOLR-7973 | deferred-own-host (close-obsolete) | not checked (own host) | `d71df168b8cb` (solr-7973-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 7953 | SOLR-7953 | deferred-own-host (close-fixed) | not checked (own host) | `b2ecbe0499a0` (solr-7953-draft-close) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 7947 | SOLR-7947 | deferred-own-host (keep-open) | not checked (own host) | `4a82d93f05a4` (solr-7947-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 7932 | SOLR-7932 | suspect (keep-open) | none naming the ticket as a closeout | `0965b9065a1d` (solr-7932-draft-open) | no closeout claim with a DONE line names this ticket; no file-and-line code citation found in the record |
| 7927 | SOLR-7927 | suspect (keep-open) | none naming the ticket as a closeout | `1d3a0557e228` (solr-7927-draft-open) | no closeout claim with a DONE line names this ticket; no file-and-line code citation found in the record |
| 7899 | SOLR-7899 | suspect (keep-open) | none naming the ticket as a closeout | `d0d74c3dc5d3` (solr-7899-draft-open) | no closeout claim with a DONE line names this ticket; no file-and-line code citation found in the record |
| 7867 | SOLR-7867 | deferred-own-host (keep-open) | not checked (own host) | `0bbe56dfdef4` (solr-7867-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 7846 | SOLR-7846 | deferred-own-host (keep-open) | not checked (own host) | `eff2751b4c36` (solr-7846-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |
| 7806 | SOLR-7806 | deferred-own-host (keep-open) | not checked (own host) | `93b4805e39a9` (solr-7806-draft-open) | the record names vm2 as the checker of the reading it cites ("Checked by: vm2") |

Count: pass 0, suspect 6, deferred 19.
