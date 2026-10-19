# Skip verdict cross-audit, batch 2 of 21

Assignment: `assignments/pool-aab-skip-verdict-audit-02.md`. Auditor: vm3, 2026-10-19, claim `claims/pool-aab-skip-verdict-audit-02.md`. Each item was checked against the floor in `material/skip-audit/verdict-rubric.md`: a closeout claim in `claims/` naming the ticket with a DONE line, the named draft branch on the fork with `drafts/<KEY>/record.md` and `jira-comment.md`, the pinned head the record re-checked at (matched to `evidence_head` in `material/skip-audit/queue-current.json`, which is `498a09aea97a` for every item here), the Jira status read in that pass, a code path with file and line or a reproduction, a substantive record, and a verdict whose draft suffix matches.

Reading of "closeout claim": as in batch 01, the test is a claim whose job was the closeout of that ticket's verdict. Several claims in `claims/` name keys from this batch and carry DONE lines, but they are review or repro rounds (listed in the Claim column). They are recorded, not counted as closeouts. The literal wording of the floor could be read to accept them; that reading is an open question for the vm1 watch, see Questions below.

Own-host check: three records name vm3 as the worker of the reading they cite (SOLR-11378, SOLR-11246, SOLR-11009). They are `deferred-own-host`. The records were not audited by this host.

Result for every item: no pass. No item has a closeout claim in `claims/`.

| # | Ticket | Result | Claim found | Draft branch head | Missing |
| --- | --- | --- | --- | --- | --- |
| 1 | SOLR-11378 | deferred-own-host (keep-open) | review claim `pool-skip-review-removed-round-2.md` (vm3, DONE) | `c34792305c` (solr-11378-draft-open) | record cites the vm3 B2 reading as its source |
| 2 | SOLR-11353 | suspect (keep-open) | none | `383d788b4c` (solr-11353-draft-open) | no closeout claim; pinned head not re-checked (reading is against upstream `c8ab71430f`); Jira status from the audit-3 reading, not read in this pass; no file:line |
| 3 | SOLR-11301 | suspect (keep-open) | none | `52b6e87c4c` (solr-11301-draft-open) | no closeout claim; Jira status not read in this pass |
| 4 | SOLR-11292 | suspect (keep-open) | none | `525fdc9042` (solr-11292-draft-open) | no closeout claim; pinned head not re-checked (upstream `c8ab71430f`); Jira status from the audit-3 reading; no file:line |
| 5 | SOLR-11290 | suspect (keep-open) | none | `5bd3b6eebf` (solr-11290-draft-open) | no closeout claim; Jira status from the B3 reading, not read in this pass |
| 6 | SOLR-11274 | suspect (keep-open) | none | `b6d4088bd9` (solr-11274-draft-open) | no closeout claim; Jira status not read; no file:line |
| 7 | SOLR-11269 | suspect (keep-open) | review claim `pool-skip-review-committed-support-round-1.md` (vm2, DONE) | `3d3c895a11` (solr-11269-draft-open) | no closeout claim; Jira status from the B3 reading, not read in this pass |
| 8 | SOLR-11259 | suspect (close-obsolete) | repro claim `pool-skip-repro-flake-locate-round-1.md` (vm5, DONE; class recorded not on main) | `e4204b9193` (solr-11259-draft-close) | no closeout claim; Jira status not read. The absence grep was re-run at `498a09aea97a` and returns no files, so the code part holds |
| 9 | SOLR-11247 | suspect (keep-open) | repro claim `pool-skip-repro-flake-round-1.md` (vm2, DONE; pass 1 green at seed 21FA8A11BC9942C6) | `8511dc4f27` (solr-11247-draft-open) | no closeout claim; Jira status not read; no file:line |
| 10 | SOLR-11246 | deferred-own-host (close-fixed) | review claim `pool-skip-review-removed-round-2.md` (vm3, DONE) | `54e402e5ff` (solr-11246-draft-close) | record cites the vm3 B2 reading as its source |
| 11 | SOLR-11214 | suspect (keep-open) | repro claim `pool-skip-repro-flake-locate-round-1.md` (vm5, DONE; no class named, not run) | `895798c66e` (solr-11214-draft-open) | no closeout claim; Jira status not read; no file:line |
| 12 | SOLR-11208 | suspect (keep-open) | repro claim `pool-skip-repro-flake-locate-round-1.md` (vm5, DONE; no class named, not run) | `60ae14217b` (solr-11208-draft-open) | no closeout claim; Jira status not read; no file:line |
| 13 | SOLR-11193 | suspect (keep-open) | none | `29a02d762d` (solr-11193-draft-open) | no closeout claim; Jira status not read; no file:line |
| 14 | SOLR-11175 | suspect (close-answered) | none | `74db969f66` (solr-11175-draft-close) | no closeout claim; Jira status from the B3 reading, not read in this pass; no docs link and no quoted answering sentence |
| 15 | SOLR-11168 | suspect (keep-open) | repro claim `pool-skip-repro-flake-locate-round-1.md` (vm5, DONE; class recorded not on main) | `da65d218c3` (solr-11168-draft-open) | no closeout claim; Jira status not read; the record checks only the exact name `ConcurrentUpdateSolrClientTest`. The test family at the pin (`ConcurrentUpdateJdkSolrClientTest`, `ConcurrentUpdateJettySolrClientTest`, `ConcurrentUpdateSolrClientTestBase`, 9 ConcurrentUpdate files) is not searched |
| 16 | SOLR-11150 | suspect (keep-open) | none | `582d9b36a2` (solr-11150-draft-open) | no closeout claim; Jira status from the C3 reading, not read in this pass |
| 17 | SOLR-11139 | suspect (keep-open) | none | `031f10c4a2` (solr-11139-draft-open) | no closeout claim; Jira status from the C3 reading, not read in this pass |
| 18 | SOLR-11083 | suspect (keep-open) | none | `ef6a3c5d79` (solr-11083-draft-open) | no closeout claim; pinned head not re-checked (upstream `c8ab71430f`); Jira status from the audit-3 reading; no file:line |
| 19 | SOLR-11058 | suspect (keep-open) | repro claim `pool-skip-repro-flake-round-1.md` (vm2, DONE; failed at seed F982A35C90FB67A5 on two runs) | `0101774bfe` (solr-11058-draft-open) | no closeout claim; Jira status not read; no file:line in the record |
| 20 | SOLR-11034 | suspect (keep-open) | none | `0a2503333d` (solr-11034-draft-open) | no closeout claim; pinned head not re-checked (upstream `c8ab71430f`); Jira status from the audit-3 reading; no file:line |
| 21 | SOLR-11009 | deferred-own-host (close-fixed) | review claim `pool-skip-review-removed-round-2.md` (vm3, DONE) | `8ea024b4c7` (solr-11009-draft-close) | record cites the vm3 B2 reading as its source |
| 22 | SOLR-11008 | suspect (close-fixed) | none | `696ba496d5` (solr-11008-draft-close) | no closeout claim; the test named in the ticket, `testPropertyFilter`, is absent at the pin (`git grep` empty) and the record does not say so. Fix commit `7c4584bd4f` is an ancestor of the pin and line 40 of `MetricsHandlerTest.java` matches, but the record does not link that commit to the named test; the pinned head is given short (`498a09aea9`) |
| 23 | SOLR-10982 | suspect (keep-open) | none | `0d4196e411` (solr-10982-draft-open) | no closeout claim; pinned head not re-checked (upstream `c8ab71430f`); Jira status from the audit-5 reading; no file:line |
| 24 | SOLR-11022 | suspect (keep-open) | none | `78d3403ffc` (solr-11022-draft-open) | no closeout claim; Jira status from the C3 reading, not read in this pass |
| 25 | SOLR-10976 | suspect (keep-open) | repro claim `pool-skip-repro-flake-round-1.md` (vm2, DONE; pass 1 green at seed 5DF3F7F65065E53C) | `6a9ed12c60` (solr-10976-draft-open) | no closeout claim; Jira status not read; no file:line |

Count: pass 0, suspect 22, deferred 3 (SOLR-11378, SOLR-11246, SOLR-11009).

## Questions for the vm1 watch

1. The floor says "a claim in `claims/` naming the ticket carries a DONE line". Read literally, the review and repro rounds that name 11 of these keys would satisfy it. This batch and batch 01 read it as a closeout claim. The watch should confirm which reading governs. Under the literal reading, rows 7, 8, 9, 11, 12, 15, 19 and 25 would still fail on the Jira status check, and each has no file:line or reproduction that closes it, so the result would not change.
2. SOLR-11168 and SOLR-11259: the queue marks the named classes absent, and the repro round recorded them "not on main". SOLR-11168 names a test family that is present at the pin; the verdict may need a second search before close-obsolete or keep-open is settled.
3. SOLR-11008: the record's fix commit and the ticket's named test do not match. The record needs to say whether `testPropertyFilter` was removed or renamed.
