# Skip verdict cross-audit, batch 4 of 21

Assignment: `assignments/pool-aab-skip-verdict-audit-04.md`. Auditor: vm2, 2026-10-19, claim `claims/pool-aab-skip-verdict-audit-04.md`. Each item was checked against the floor in `material/skip-audit/verdict-rubric.md`: a closeout claim in `claims/` naming the ticket with a DONE line, the named draft branch on the fork with `drafts/<KEY>/record.md` and `jira-comment.md`, the pinned head the record cites (matched to `evidence_head` in `material/skip-audit/queue-current.json`, which is `498a09aea97a` for every item here), the Jira status read in that pass, a code path with file and line or a reproduction, a substantive record, and a verdict whose draft suffix matches.

Checks run on the fork at tip `c2ec9fbc364`. Every draft branch exists with its suffix matching the recorded verdict, both files are present for all 25, and every record cites the pinned head. No item passed, because no file in `claims/` names any of the 25 tickets, so no closeout claim with a DONE line exists for any of them.

Own-host check: 14 records state "Checked by: vm2" for the reading they cite (from the B2 or C reading, and for SOLR-10096 the B3 reading). Those items are `deferred-own-host`; this host did not audit them.

Result for the suspects: each record says "JIRA was not re-read for this record" (the Jira status quoted in some records comes from an audit-3 or earlier reading, not this pass). No suspect cites a source file path with a line number.

| # | Ticket | Result | Claim found | Draft branch head | Missing |
| --- | --- | --- | --- | --- | --- |
| 1 | SOLR-10512 | suspect (keep-open) | none | `560bc1268ef8` (solr-10512-draft-open) | no closeout claim in claims/ names this ticket; the record says JIRA was not re-read in this pass; no source file path cited |
| 2 | SOLR-10511 | deferred-own-host (close-fixed) | none | `09c333b1a98e` (solr-10511-draft-close) | the record names vm2 as the reader of the cited reading ("Checked by: vm2"); not audited by this host |
| 3 | SOLR-10497 | deferred-own-host (close-fixed) | none | `cca8eec1548f` (solr-10497-draft-close) | the record names vm2 as the reader of the cited reading ("Checked by: vm2"); not audited by this host |
| 4 | SOLR-10407 | deferred-own-host (close-obsolete) | none | `56aa5fe04443` (solr-10407-draft-close) | the record names vm2 as the reader of the cited reading ("Checked by: vm2"); not audited by this host |
| 5 | SOLR-10395 | deferred-own-host (keep-open) | none | `88f82743a315` (solr-10395-draft-open) | the record names vm2 as the reader of the cited reading ("Checked by: vm2"); not audited by this host |
| 6 | SOLR-10363 | suspect (keep-open) | none | `27cc47070861` (solr-10363-draft-open) | no closeout claim in claims/ names this ticket; the record says JIRA was not re-read in this pass; no source file path cited |
| 7 | SOLR-10334 | deferred-own-host (keep-open) | none | `a9e176624b09` (solr-10334-draft-open) | the record names vm2 as the reader of the cited reading ("Checked by: vm2"); not audited by this host |
| 8 | SOLR-10332 | suspect (keep-open) | none | `f2ae2ee700a0` (solr-10332-draft-open) | no closeout claim in claims/ names this ticket; the record says JIRA was not re-read in this pass; no source file path cited |
| 9 | SOLR-10326 | deferred-own-host (close-obsolete) | none | `dd04b513f092` (solr-10326-draft-close) | the record names vm2 as the reader of the cited reading ("Checked by: vm2"); not audited by this host |
| 10 | SOLR-10325 | deferred-own-host (keep-open) | none | `b53522cd0077` (solr-10325-draft-open) | the record names vm2 as the reader of the cited reading ("Checked by: vm2"); not audited by this host |
| 11 | SOLR-10284 | suspect (keep-open) | none | `5e82ca5059e1` (solr-10284-draft-open) | no closeout claim in claims/ names this ticket; the record says JIRA was not re-read in this pass; no source file path cited |
| 12 | SOLR-10256 | suspect (keep-open) | none | `935e58c0afe8` (solr-10256-draft-open) | no closeout claim in claims/ names this ticket; the record says JIRA was not re-read in this pass; no source file path cited |
| 13 | SOLR-10242 | deferred-own-host (keep-open) | none | `a6b2b97a3478` (solr-10242-draft-open) | the record names vm2 as the reader of the cited reading ("Checked by: vm2"); not audited by this host |
| 14 | SOLR-10201 | deferred-own-host (keep-open) | none | `71fa895db709` (solr-10201-draft-open) | the record names vm2 as the reader of the cited reading ("Checked by: vm2"); not audited by this host |
| 15 | SOLR-10197 | suspect (keep-open) | none | `b4f330aa913c` (solr-10197-draft-open) | no closeout claim in claims/ names this ticket; the record says JIRA was not re-read in this pass; no source file path cited |
| 16 | SOLR-10112 | suspect (keep-open) | none | `593c1b074bc5` (solr-10112-draft-open) | no closeout claim in claims/ names this ticket; the record says JIRA was not re-read in this pass; no source file path cited |
| 17 | SOLR-10110 | deferred-own-host (keep-open) | none | `1af887b3f805` (solr-10110-draft-open) | the record names vm2 as the reader of the cited reading ("Checked by: vm2"); not audited by this host |
| 18 | SOLR-10096 | deferred-own-host (close-answered) | none | `573243540171` (solr-10096-draft-close) | the record names vm2 as the reader of the cited reading ("Checked by: vm2"); not audited by this host |
| 19 | SOLR-10084 | suspect (keep-open) | none | `5a76ecfb6dd1` (solr-10084-draft-open) | no closeout claim in claims/ names this ticket; the record says JIRA was not re-read in this pass; no source file path cited |
| 20 | SOLR-10041 | deferred-own-host (close-obsolete) | none | `6389478bff4f` (solr-10041-draft-close) | the record names vm2 as the reader of the cited reading ("Checked by: vm2"); not audited by this host |
| 21 | SOLR-10030 | suspect (keep-open) | none | `c45dc037b72f` (solr-10030-draft-open) | no closeout claim in claims/ names this ticket; the record says JIRA was not re-read in this pass; no source file path cited |
| 22 | SOLR-10022 | deferred-own-host (close-obsolete) | none | `165dfab310a6` (solr-10022-draft-close) | the record names vm2 as the reader of the cited reading ("Checked by: vm2"); not audited by this host |
| 23 | SOLR-10016 | suspect (keep-open) | none | `f562120593fd` (solr-10016-draft-open) | no closeout claim in claims/ names this ticket; the record says JIRA was not re-read in this pass; no source file path cited |
| 24 | SOLR-10010 | deferred-own-host (keep-open) | none | `8af9f98f3c3e` (solr-10010-draft-open) | the record names vm2 as the reader of the cited reading ("Checked by: vm2"); not audited by this host |
| 25 | SOLR-9978 | suspect (keep-open) | none | `65a2804cfe9b` (solr-9978-draft-open) | no closeout claim in claims/ names this ticket; the record says JIRA was not re-read in this pass; no source file path cited |

Count: pass 0, suspect 11, deferred 14.

## Questions for the vm1 watch

1. The batch says suspects enter `material/skip-audit/remediation-current.json` through the vm1 watch, and that auditors do not edit it. `pool/cron-prompt.md` now says the pool maintains that file, and the owner's complaints file (items 2 and 4) says the vm1 watch is retired. The 11 suspects are not appended to remediation-current.json in this push. Confirm which path governs.
2. The 25 records were written at audit rounds 3, 5 and 6. Every record says the Jira status was not re-read in the pass that wrote it. Records need a Jira read on the current pass before any of them can pass.
