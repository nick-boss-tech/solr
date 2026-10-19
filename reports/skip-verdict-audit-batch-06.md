# Skip verdict cross-audit, batch 6 of 21

Auditor: vm5. Date: 2026-10-19 UTC. Assignment: `assignments/pool-aab-skip-verdict-audit-06.md`. Claim: `claims/pool-aab-skip-verdict-audit-06.md`.

Method, per item: read the draft branch head on the fork; check `drafts/<KEY>/record.md` and `drafts/<KEY>/jira-comment.md` exist; check `claims/` for a claim naming the ticket with a DONE line, and for the claimant host of that claim; compare the record's pinned head with `evidence_head` in `material/skip-audit/queue-current.json` (all 25 are `498a09aea97aac86c2cc7ac114113b946d249942`, matching); check the branch suffix against the verdict (all 25 match); read the record body against the minimum evidence for its verdict. No JIRA or GitHub read or post, no build, no change to any audited ticket, claim, draft or queue record.

Rung used (decision ladder): rung 3, following the assignment's floor, applied as in batch 03: a Jira status cited from an earlier round is not a read in this pass (rung 4, recorded choice).

Missing-item codes for suspects:
- J: the record does not name a Jira status and date read in this record's pass. Every record says "JIRA was not re-read for this record" or cites an earlier round's reading.
- C: no `claims/` file names the ticket with a DONE line.
- L: no file and line code path read at the pinned head. The rubric asks for this on every verdict, so a reproduction-only record still needs one.
- G: close-obsolete without the grep command and its empty result run at the pinned head.

| Ticket | Result | Claim found | Draft head | Missing (suspects only) |
| --- | --- | --- | --- | --- |
| SOLR-9648 | suspect | none | 3b9a03baf21d | J, C, L (audit evidence only) |
| SOLR-9645 | suspect | none | 420b34c67317 | J, C, L (audit evidence only) |
| SOLR-9622 | suspect | none | ec05cb107ea1 | J, C, L (audit evidence only) |
| SOLR-9599 | suspect | none | dc3d4674c8fc | J, C, L (audit evidence only) |
| SOLR-9594 | suspect | none | d0d1c7256d06 | J, C, L (audit evidence only) |
| SOLR-9593 | suspect | none | 8352a571d15f | J, C, L (no code path; the cited run skipped every test, and no missing piece is named) |
| SOLR-9578 | suspect | none | 7fb68fbfa3a8 | J, C, L (audit evidence only) |
| SOLR-9573 | suspect | none | c0c7c8e5faff | J, C, L (fix version only, "not code-verified") |
| SOLR-9571 | suspect | none | f89c520ef139 | J, C |
| SOLR-9550 | suspect | none | 0968814202f7 | J, C, L (audit evidence only) |
| SOLR-9514 | suspect | none | 20b2db7d109a | J, C, L (audit evidence only) |
| SOLR-9492 | suspect | none | 5000beb98a90 | J, C |
| SOLR-9477 | suspect | none | ab3aff3cd15e | J, C, L (audit evidence only) |
| SOLR-9468 | suspect | none | d6ccaaf54230 | J, C |
| SOLR-9428 | suspect | claims/pool-skip-repro-flake-round-1.md (DONE 2026-10-18T08:20Z) | d8029f0ce897 | J, L (no code path read for this record) |
| SOLR-9427 | suspect | claims/pool-skip-repro-flake-round-1.md (DONE 2026-10-18T08:20Z) | f3f7de15a253 | J, L (no code path read for this record) |
| SOLR-9426 | deferred-own-host | claims/pool-skip-repro-flake-locate-round-1.md (DONE 2026-10-18T06:55Z, claimant vm5) | 36eb08028ab4 | not scored |
| SOLR-9400 | suspect | none | 8dd8fe238312 | J, C, G (grep result taken from the B1 reading, not re-run), L (no file and line) |
| SOLR-9398 | suspect | none | 99f045139f85 | J, C |
| SOLR-9390 | suspect | claims/pool-skip-repro-flake-round-1.md (DONE 2026-10-18T08:20Z) | bc97c30cd969 | J, L (no code path read for this record) |
| SOLR-9369 | suspect | none | 118e27e26f67 | J, C, L (audit evidence only) |
| SOLR-9366 | suspect | none | 93f112a69712 | J, C, G (grep result taken from the B1 reading, not re-run) |
| SOLR-9361 | suspect | none | 2748c65da695 | J, C, L (audit evidence only) |
| SOLR-9352 | suspect | none | 563e306889b2 | J, C |
| SOLR-9329 | suspect | none | 0f8d2685cf8a | J, C |

Result count: 0 pass, 24 suspect, 1 deferred-own-host (SOLR-9426).

## Notes for the vm1 watch

- SOLR-9426 is deferred-own-host. Its closeout claim `claims/pool-skip-repro-flake-locate-round-1.md` names vm5 as claimant, and the run its record cites (seed 420562093AFD92BD) is that claim's run. Left for the vm1 watch or another host.
- J is the common failure again: no record in this batch reads the Jira status in its own pass.
- The repro-based keep-open records (SOLR-9428, 9427, 9390) have a DONE claim and a single passing run each. The rubric's repro route is met in shape, but each record also needs a file-and-line code path, so L is missing.
- SOLR-9593 cites a run in which every test was skipped, so it exercised nothing and is not evidence either way.
- SOLR-9400 and SOLR-9366 are close-obsolete with a grep result carried over from an earlier round; the rubric asks for the grep run at the pinned head.
