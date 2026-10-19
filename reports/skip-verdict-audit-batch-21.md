# Skip verdict cross-audit, batch 21 of 21

Auditor: vm5. Date: 2026-10-19 UTC. Assignment: `assignments/pool-aab-skip-verdict-audit-21.md`. Claim: `claims/pool-aab-skip-verdict-audit-21.md`.

Method, per item: read the draft branch head on the fork; check `drafts/<KEY>/record.md` and `drafts/<KEY>/jira-comment.md` exist; check `claims/` for a claim naming the ticket with a DONE line and its claimant host; check whether the closeout claim or the record names vm5 as the worker, and defer if so; compare the pinned head with the assignment's evidence head (all six are `498a09aea97a`, matching); check the branch suffix against the verdict (all six match); read the record body against the minimum evidence for its verdict. No JIRA or GitHub read or post, no build, no change to any audited ticket, claim, draft or queue record.

Rung used (decision ladder): rung 1 and 3. The own-host question for SOLR-1780 and SOLR-1785 was settled by reading their records' source reading (rung 1): both cite the C1 slice reading, and that slice's claim names vm5 as claimant. Recorded as deferred-own-host.

Missing-item codes for suspects:
- J: the record does not name a Jira status and date read in this record's pass.
- C: no `claims/` file names the ticket with a DONE line.
- L: no file and line code path read at the pinned head.

| Ticket | Result | Claim found | Draft head | Missing (suspects only) |
| --- | --- | --- | --- | --- |
| SOLR-1785 | deferred-own-host | claims/pool-skip-review-stale-slice-1.md (DONE 2026-10-18T07:40Z, claimant vm5) | 9d44e0674866 | not scored (record cites the C1 slice reading, whose claim names vm5) |
| SOLR-1780 | deferred-own-host | claims/pool-skip-review-stale-slice-1.md (DONE 2026-10-18T07:40Z, claimant vm5) | 95762dd7ab18 | not scored (record cites the C1 slice reading, whose claim names vm5) |
| SOLR-1658 | suspect | none | 1dedf4ca4cc8 | J, C |
| SOLR-1557 | pass | claims/skip-SOLR-1557-vm4.md (DONE 2026-10-19T07:45:00Z, claimant vm4) | 868231790333 | none |
| SOLR-1542 | pass | claims/skip-SOLR-1542-vm4.md (DONE 2026-10-19T07:40:00Z, claimant vm4) | 175f3e05da08 | none |
| SOLR-1247 | pass | claims/skip-SOLR-1247-vm4.md (DONE 2026-10-19T07:30:00Z, claimant vm4) | e0d9d8332f55 | none |

Result count: 3 pass, 1 suspect, 2 deferred-own-host (SOLR-1785, 1780).

Checks behind the passes: each names a Jira status and a date read on 2026-10-19 in the record's own pass, cites file-and-line code paths at the pinned head, and states what the verdict does not cover and what would discharge keep-open. Each has a DONE claim by vm4 that names the ticket.

## Notes for the vm1 watch

- SOLR-1780 and SOLR-1785 are deferred because the slice reading they cite is vm5's C1 claim (`pool-skip-review-stale-slice-1`). The records themselves name vm2 as the checker. If the watch reads the rule as naming the record's checker only, these two should be audited by another host.
- SOLR-1247 and SOLR-1542 also appear in the C1 slice table with a verdict from vm5's earlier reading. That reading is not their closeout, which is vm4's claim, so they are scored.
- SOLR-1658 is a close-fixed record whose reading is the B1 round (vm3). Its fixing commit is an ancestor of the pinned head and the cited test lines are present. It fails only on J and C.
