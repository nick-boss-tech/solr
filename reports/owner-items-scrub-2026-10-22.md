# Owner items scrub: newdev and Blocker/Critical review round 1

Date: 2026-10-22. Author: vm5. Covers every item the review round (`assignments/pool-newdev-critblock-review-round-1.md`) left as "owner item", "owner decision" or "rung 5". Each item is settled here by the decision ladder (`material/decision-ladder.md`). None is left for the owner. The implementation of each decided item is in `assignments/pool-owner-items-scrub-implementation-round-1.md`.

Rule applied: a question is settled at the first rung that answers it. Rung 5 is used only for a public action (PR opening, JIRA post, merge, branch deletion). Those are not decided by a review host, so they are recorded as "no action" rather than asked.

## Decisions

| Unit | Ticket | Item (as the review left it) | Decision | Rung | Evidence |
| --- | --- | --- | --- | --- | --- |
| 5 | SOLR-18552 (9.x) | Add `solr.cmd` default on 9.x? | Yes. Add the matching `set "SCRIPT_SOLR_OPTS=..."` line to `solr/bin/solr.cmd` on the 9.x line. | 1 | Main line sets the default in both launchers (unit 4). The 9.x line only adds the bash line, so Windows users get no default. |
| 5 | SOLR-18552 (9.x) | No changelog on 9.x | Add the changelog entry that main has (`changelog/unreleased/SOLR-18552-mmap-arena-permits.yml`), adjusted to the 9.x line. | 1 | Main carries the entry. A user-visible launcher default needs one on each line. |
| 7 | SOLR-11650 | Redaction fails open for a raw `/` or `?` in a password | Fix it: the userinfo ends at the last `@` before the first `/`, `?` or `#` after the scheme. Test both characters in the password. | 3 | Ticket: credentials must not appear in replication details. Code: `URLUtil.USER_INFO` (`solr/solrj/src/java/org/apache/solr/common/util/URLUtil.java`). |
| 9 | SOLR-14200 | Empty-shard behavior in `SignificantTermsStream` and `DeepRandomStream` (implementer's failure mode) | Throw a clear `IllegalArgumentException` that names the collection and the missing manual shard entry. Do not return an empty result, and do not leave the `ArithmeticException`. | 4 | A silent empty result hides a misconfigured manual shards entry. `TupleStream.getShards` already returns an empty list for a missing entry, so the two streams must say so instead of guessing. |
| 8 | SOLR-12743 | Delete banked branch `solr-12743-submit`? | No action. The branch stays as it is. Agents do not delete branches, and nothing needs the deletion. | 4 | Ticket is RETIRED. The branch is a record. |
| 8 | SOLR-12743 | Fresh investigation on Caffeine-based caches? | Not in scope. Ticket is retired; no new investigation this round. | 3 | Retired ticket; the assignment does not ask for it. |
| 11 | SOLR-15326 | Retire or keep the branch? | Keep. Gate green at `dab6a46631`. The extra `SchemaRequest.UniqueKey` round trip is accepted and recorded; no cache is added this round. | 4 | Gate receipt and review pass 3 (`reports/newdev-critblock-review-round-1/solr-15326-submit.md`). |
| 12 | SOLR-16155 | Open a PR? | No PR. Upstream PR #1151 covers the ticket. The branch is kept as the record of the fork change. | 1 | Upstream fix exists (receipt `receipts/SOLR-16155.md`). |
| 12 | SOLR-16155 | Keep dropping `msg=` from the error response? | Keep, and keep the changelog disclosure. | 4 | The ticket asks for values to be left out, and a parse message can carry a value. The cause stays on the exception for server logs. |
| 14 | SOLR-16914 | Docs-only change does not reproduce the reporter's failure | Run the reporter's configuration at the head as a probe. If it fails, add the fix; if it passes, the docs are the resolution and the ticket states that. | 2 | Only a run separates the two readings. Assigned in the implementation round. |
| 16 | SOLR-17252 | Q2 (emacs in the list) and Q3 (eager validation) | Keep the owner's recorded positions in `receipts/SOLR-17252.md`. Not reopened. | 3 | Owner positions are already written down. |
| 17 | SOLR-17297 | Module-stage SPI orphaning (probe at `g17297r35-probe.log`) | Keep the shipped branch as gated. Do not widen the branch. The residual is a follow-up assignment, not part of this fix. | 4 | Ticket is about lib plugin ordering. The residual needs a loader-design change, which would widen the branch. The probe is kept as evidence in the follow-up. |
| 18 | SOLR-17433 | Request timeout unbounded for every cached client | Keep as stated behavior. Decision D1 already settled it (`reports/decision-log-2026-10-21.md`). PR body states the 60 s socket floor and the unbounded request timeout together. | 3 | Ticket asks for no timeout while a stream returns content. Every `SolrClientCache` caller is a stream, graph, SQL or commit-stream path. |
| 17842 | SOLR-17842 | Narrow to guidance, or keep open for published results? | Narrow. The PR states that it adds guidance and publishes no results. The placeholder commit `9da1242b1d` is not added. | 3 | Ticket asks for results; branch delivers guidance only. The PR body says so. |
| 7550 | SOLR-7550 | Other 500s on the versions request now fail the sync | Keep. | 3 | Ticket asks that only the core-failed case count as success. The narrowing is what the ticket asks. |
| 8767 | SOLR-8767 | `/get` now returns copyField targets (scope) | Keep. Stored copy targets are returned; that is the ticket's stated expectation. | 3 | Ticket intent. Review pass 2 checked the wildcard path in code. |
| 8767 | SOLR-8767 | No `fl=*` test; no multivalued feedback test | Add both tests on the branch. Each must fail on the base or on a mutation before it counts (rung 2). | 2 | Assigned. |
| 12718 | SOLR-12718 | Which of the two branches to merge? | Merge `solr-12718-streamcontext-cache` (`b08cfcc1c2` plus changelog `a0c4f926e4`). The table branch `solr-12718` is retired as a duplicate, not deleted. | 1 | Owner-decided implementation (row 7, option B) is on the decided branch; the code is identical; the decided branch has the changelog. |
| 12718 | SOLR-12718 | No test calls the new constructor | Add the test on the decided branch. | 2 | Assigned. |
| 18458 | SOLR-18458 | `debug=results` widened in the fallback | Narrow to the ticket: `debug=all` and `debug=true` only, as before. | 3 | Ticket asks for `debug=all`; no test pins the widening. |
| 18297 | SOLR-18297 | Stray `pr-draft/SOLR-18297.md` in the code commit | Remove it from the tree. | 1 | A PR description does not belong in the branch tree. |
| 18297 | SOLR-18297 | Lockfile gap | Assigned: confirm the gap from the build files and fix it if the lock is missing for a dependency that changed in this branch. | 2 | Only a check of the build files settles it. |
| 18010 | SOLR-18010 | Atomic temp-file write, or narrow version? | Narrow, as Nick decided on 2026-10-22 (`reports/newdev-critblock-review-round-1/solr-18010-narrow-decision.md`). Implement the narrow version. | 3 | Nick's written decision. |
| 18010 | SOLR-18010 | Owner change, Windows, symlink policy, `#4916` lock, new-file mode 0600 | Settled: the lock is the one on main (`#5065`, rung 1, round 3 rebase). The policy widening is not in this PR (repo-wide; recorded as a maintainer choice in the PR body). Mode 0600 for a new file is kept (rung 4, `solr-18010-submit.md`). Owner change and Windows are stated as untested in the PR body. | 4 | Round 3 and round 4 records. |

## What stays open

Nothing in this round is left for the owner. The only public actions in the table are "no PR" (SOLR-16155) and "no branch deletion" (SOLR-12743), and both are "no action", which needs no approval.

## Second pass: items outside the review round

| Item | Decision | Rung | Implementation |
| --- | --- | --- | --- |
| SOLR-4841 follow-up | Satisfied. Review rounds 2 and 3 are done at `bf1b01d0dc`, with the mutation run (`expected:<[sv]> but was:<[]>`). Change the header from NEEDS-OWNER to SATISFIED. Filing the ticket and PR opening stay with Nick. | 2 | Header edit only |
| SOLR-8954 | Do not land `solr-8954-submit`; add no test-only follow-up. The all-shards fix is carried by `solr-8009-submit`, gated 10 of 10 at `e600375aae`. | 1 | None on this branch |
| SOLR-15357 | Keep the branch narrow at `e9e7cc3a0c`. The RTG and `fl=*` gap stays in Limits. Widening is a scope change with no recorded recommendation, so it is not taken. | 4 | None |
| SOLR-18119 | Label `solr-18119-submit` retired in place, as the 2026-10-14 owner rule says. Do not close PR #4999 or delete the branch. | 3 | Label only |
| SOLR-18280 (pr-drift run records) | Record the 12:55Z head failure as a clock-based timing flake in `receipts/SOLR-18280.md`. The head is not counted as 25 of 25. | 4 | Receipt text; a rerun is optional |
| SOLR-17055 (vm2 row 1) | Keep choice (a): `maxScore` on a score sort in both paths, which the distributed path already reports (`QueryComponent.java` lines 1086, 1167 to 1169, 1293 on main). | 1 | Code only after the response contract is set (see owner list) |
| SOLR-4374 (vm2 row 4) | Include the changelog wording and the function-alias Limits line on the branch. | 3 | Branch commit, then gate |
| SOLR-17612 (round 5 threshold) | Keep the gated rule: minimum over filters of summed per-filter shard counts. | 1 | None on gated head `3a456c27dd` |
| pr-review-4042 | Do not adopt `SolrXPathTestCase`; keep the existing test style. The review posts without that point. | 4 | None |
| pr-review-4626 | Keep the existing SolrJ request timeout default. D1 covers only `SolrClientCache` streaming callers, so the default does not change. | 3 | None |

## Still for the owner: public actions and public contracts

The ladder reserves rung 5 for public commitments and public contracts. These cannot be settled inside the pool:

- Post the reviews for pr-review-4042, 4626 and 4874 (the drafts are ready).
- Set the release target for removing `SolrPing` (pr-review-4874).
- Approve the fork push for SOLR-17842 (`assignments/pool-vm2-open-decisions-round-1.md` row 2). The assignment reserves that push for an owner yes.
- Rule on the SOLR-17055 response contract (a public API change).
- Approve filing the SOLR-4841 ticket and opening its PR.
- Decide whether to close PR #4999 once PR #5061 is in.

## Notes

- The earlier status file (`assignments/vm5-status-for-nick-2026-10-22.md`) listed these as decisions for Nick. This record supersedes that list.
- Rung use follows `material/decision-ladder.md`. Each row cites the record it relies on.
