# Skip verdict cross-audit, batch 17 of 21

Auditor: vm5. Date: 2026-10-19 UTC. Assignment: `assignments/pool-aab-skip-verdict-audit-17.md`. Claim: `claims/pool-aab-skip-verdict-audit-17.md`.

Method, per item: read the draft branch head on the fork; check `drafts/<KEY>/record.md` and `drafts/<KEY>/jira-comment.md` exist; check `claims/` for a claim naming the ticket with a DONE line; check the record's "Checked by" host, and defer any record that names vm5; compare the pinned head with the assignment's evidence head (all 25 are `498a09aea9`, matching); check the branch suffix against the verdict (all 25 match); read the record body against the minimum evidence for its verdict. No JIRA or GitHub read or post, no build, no change to any audited ticket, claim, draft or queue record.

Rung used (decision ladder): rung 3, following the assignment's floor, applied as in batches 03 and 06: a Jira status cited from an earlier round is not a read in this pass (rung 4, recorded choice).

Missing-item codes for suspects:
- J: the record does not name a Jira status and date read in this record's pass. Every record says "JIRA was not re-read for this record" or cites an earlier round's reading.
- C: no `claims/` file names the ticket with a DONE line.
- L: no file and line code path read at the pinned head. The rubric asks for this on every verdict, so a reproduction-only record still needs one.
- G: close-obsolete without the grep command and its empty result run at the pinned head.

| Ticket | Result | Claim found | Draft head | Missing (suspects only) |
| --- | --- | --- | --- | --- |
| SOLR-4674 | suspect | none | 91dcb20dfc75 | J, C, L (audit evidence only) |
| SOLR-4669 | suspect | none | a37bd180aa58 | J, C, L (audit evidence only) |
| SOLR-4659 | suspect | none | 55dedb0cecf8 | J, C |
| SOLR-4635 | deferred-own-host | none | 5c7e8e4be213 | not scored (record says "Checked by: vm5") |
| SOLR-4610 | suspect | none | dbddad1413c6 | J, C |
| SOLR-4593 | suspect | none | 15684b3dd02e | J, C, L (no code path; one passing run of a flaky test) |
| SOLR-4558 | suspect | none | 6b6f17c63798 | J, C, L (audit evidence only) |
| SOLR-4533 | suspect | none | 516c343366c4 | J, C |
| SOLR-4522 | suspect | none | 5d22dcea76da | J, C |
| SOLR-4501 | deferred-own-host | none | a4e90580cecb | not scored (record says "Checked by: vm5") |
| SOLR-4499 | suspect | none | dc16c9b3cf36 | J, C |
| SOLR-4486 | suspect | none | f4cdab47891e | J, C, L (audit evidence only) |
| SOLR-4427 | deferred-own-host | none | 71152d38dc42 | not scored (record says "Checked by: vm5") |
| SOLR-4423 | deferred-own-host | none | 5b41a3c52be3 | not scored (record says "Checked by: vm5") |
| SOLR-4414 | suspect | none | 0694e2eaf5ee | J, C |
| SOLR-4390 | suspect | none | 19bfb1424f15 | J, C |
| SOLR-4382 | suspect | none | ae1cab8f9ac0 | J, C |
| SOLR-4344 | suspect | none | b4d5b23d7020 | J, C |
| SOLR-4338 | suspect | none | 24b857ea4bc9 | J, C |
| SOLR-4304 | suspect | none | 9e3ea8708f8b | J, C |
| SOLR-4277 | suspect | none | 4d9736f8c0e7 | J, C |
| SOLR-4274 | suspect | none | 79f49e30fe99 | J, C, L (audit evidence only) |
| SOLR-4183 | suspect | none | 626939f011de | J, C, G (grep result taken from the B1 reading, not re-run), L (no file and line) |
| SOLR-4169 | suspect | none | 6cd85caf7cd5 | J, C, L (audit evidence only) |
| SOLR-4146 | suspect | none | 059d978772bb | J, C, L (audit evidence only) |

Result count: 0 pass, 21 suspect, 4 deferred-own-host (SOLR-4635, 4501, 4427, 4423).

## Notes for the vm1 watch

- The four deferred records (SOLR-4635, 4501, 4427, 4423) are close-fixed and were all closed by vm5 on 2026-10-18T21:32:17Z. They are left for the vm1 watch or another host.
- J is the common failure. No record in this batch reads the Jira status in its own pass.
- The close-fixed records that are scored (SOLR-4522, 4390, 4382) cite a fixing commit that is an ancestor of the pinned head and a file and line where the changed code is present. Only J and C are missing on them.
- SOLR-4593 cites a run that passed once. The rubric's repro route needs a second run or the same seed twice, and it also needs a file-and-line code path.
