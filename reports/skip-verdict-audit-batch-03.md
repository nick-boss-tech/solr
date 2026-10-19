# Skip verdict cross-audit, batch 3 of 21

Auditor: vm5. Date: 2026-10-19 UTC. Assignment: `assignments/pool-aab-skip-verdict-audit-03.md`. Claim: `claims/pool-aab-skip-verdict-audit-03.md`.

Method, per item: read the draft branch head on the fork; check `drafts/<KEY>/record.md` and `drafts/<KEY>/jira-comment.md` exist; check `claims/` for a claim naming the ticket with a DONE line; compare the record's pinned head with `evidence_head` in `material/skip-audit/queue-current.json` (all 25 are `498a09aea97aac86c2cc7ac114113b946d249942`, matching); check the branch suffix against the verdict (all 25 match); read the record body against the minimum evidence for its verdict. No JIRA or GitHub read or post, no build, no change to any audited ticket, claim, draft or queue record.

Rung used (decision ladder): rung 3, following the assignment's floor. The floor says the record must name the Jira status read in that pass. Where a record cites a status from an earlier round, this audit reads that as not a read in this pass (rung 4, recorded choice). Owner may overrule; the effect is on the Jira column only.

Missing-item codes for suspects:
- J: the record does not name a Jira status and date read in this record's pass. Every record says "JIRA was not re-read for this record" or cites an earlier round's reading.
- C: no `claims/` file names the ticket with a DONE line.
- L: no file and line code path read at the pinned head. The rubric asks for this on every verdict, so a reproduction-only record still needs one. A path read only at upstream main counts as missing.
- G: close-obsolete without the grep command and its empty result run at the pinned head.
- F: close-fixed without a file-and-line check that the changed code is present at the head (the ancestor check is present for each close-fixed record).

| Ticket | Result | Claim found | Draft head | Missing (suspects only) |
| --- | --- | --- | --- | --- |
| SOLR-10974 | suspect | none | 0914ec5b9656 | J, C |
| SOLR-10960 | suspect | none | 6b7352c03b12 | J, C, L (audit evidence only; "not re-checked at the pinned head") |
| SOLR-10959 | suspect | none | 5d7f1c41f1f6 | J, C, G (no grep command), L (no file and line) |
| SOLR-10952 | suspect | none | 5fd188fb5532 | J, C, L (audit evidence only; "not re-checked at the pinned head") |
| SOLR-10936 | suspect | none | d9dabd1b5fe0 | J, C, L (Lucene frame, no Solr file and line, no reproduction) |
| SOLR-10917 | suspect | none | e16218d0e984 | J, C |
| SOLR-10904 | suspect | none | 99dca04f83dd | J, C, G (no grep command), L (no file and line) |
| SOLR-10896 | suspect | none | 620ac6c9aa20 | J, C |
| SOLR-10889 | suspect | none | 9b94ba0b502e | J, C, G (no grep command), L (no file and line) |
| SOLR-10856 | suspect | none | faba992af6b1 | J, C |
| SOLR-10841 | suspect | claims/pool-skip-repro-flake-round-1.md (DONE 2026-10-18T08:20Z) | 0e937780a9ed | J, L (no code path read for this record) |
| SOLR-10806 | suspect | none | 13b65f3d22b6 | J, C |
| SOLR-10759 | suspect | claims/pool-skip-repro-flake-round-1.md (DONE 2026-10-18T08:20Z) | a8e0bc2d51f2 | J, L (no code path read for this record) |
| SOLR-10751 | suspect | none | 4b27815dc844 | J, C, L (audit evidence only) |
| SOLR-10726 | suspect | none | 6c882b453658 | J, C, L (audit evidence only) |
| SOLR-10712 | suspect | none | 79d4979f4920 | J, C |
| SOLR-10709 | suspect | none | 062cdc8f18b6 | J, C |
| SOLR-10706 | suspect | none | bb7c60953b4d | J, C |
| SOLR-10650 | suspect | none | 4e05a4ef4c96 | J, C |
| SOLR-10604 | suspect | none | 722c819002ec | J, C, L (cites upstream main c8ab71430f, not the pinned fork head) |
| SOLR-10601 | suspect | claims/pool-skip-review-committed-support-round-1.md (DONE 2026-10-18T05:20Z) | b24aca0b32e6 | J, L (commit 17563ce81f is an ancestor, but no file and line for the changed code or its presence) |
| SOLR-10597 | suspect | none | ec8c4b876bd9 | J, C |
| SOLR-10592 | suspect | claims/pool-skip-repro-flake-round-1.md (DONE 2026-10-18T08:20Z) | 945e9845af8a | J, L (no code path read for this record) |
| SOLR-10534 | suspect | none | beaa92a50a67 | J, C |
| SOLR-10533 | suspect | none | d88ba3596b1c | J, C, L (audit evidence only) |

Result count: 0 pass, 25 suspect, 0 deferred-own-host (no record names vm5 as the worker; the records name vm2 and vm3 as the readers of their B-round and C-round scopes).

## Notes for the vm1 watch

- J is the common failure. The records themselves say the Jira status was not re-read, so the watch can settle the Jira question by one ruling and not 25 separate ones.
- Records for 10841, 10759 and 10592 are candid about flaky-test results (one passing run, or a reproduction whose environment cause is not ruled out). Their keep-open verdicts rest on the run data, which fits the rubric's repro route, but the rubric also asks for a file-and-line code path on every verdict, so L is missing too.
- 10960 and 10952 are close to identical in their verdict text. The bodies differ by ticket lines, so the duplicate test passes, but the gap analysis is reused word for word across the audit-evidence keep-open records (10960, 10952, 10751, 10726, 10604, 10533).
