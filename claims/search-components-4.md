# Claim: Search components round 1, sub-batch 4 (search core, grouping, stats, REST and managed resources)

Claimed 2026-10-09 by Claude Code (AI agent), working for Nick Shanin.

Scope: `assignments/search-components-4.md` (commit `a2fdce70c2c`). Twenty-two tickets: SOLR-6207, 7520, 8051, 8088, 9595, 10694, 11310, 12044, 13851, 14381, 14931, 15144, 15319, 15479, 15895, 16444, 17155, 17372, 17791, 17841, 18196, 18506. Audit first; drafts under `pr-drafts/search-components/` for the draftable tickets.

## Heads checked live on 2026-10-09

`git ls-remote`, then a read-only fetch. Named heads match the live heads except where marked.

| Ticket | Branch | Live head | Named in the assignment | Result |
|---|---|---|---|---|
| 6207 | `solr-6207-submit` | `b099a9f1be5a` | `b099a9f1be5` | matches |
| 7520 | `solr-7520-submit` | `10b6931e1c05` | `10b6931e1c0` | matches |
| 8051 | `solr-8051-submit` | `55b24f64a8bd` | `44588ce6719` | **MOVED**: the assignment names `44588ce6719`; live is `55b24f64a8bd`. Audit the live head and flag the move. No gate |
| 8088 | `solr-8088-submit` | `567efa9b78c3` | `2398c9bea08` | **MOVED**: the assignment names `2398c9bea08`; live is `567efa9b78c3`. Audit the live head and flag the move. No gate |
| 9595 | `solr-9595-submit` | `78f5524476a6` | `7ff1350ab7b` | **MOVED**: the assignment names `7ff1350ab7b`; live is `78f5524476a6`. Audit the live head and flag the move. No gate |
| 10694 | `solr-10694-submit` | `64e86811548b` | `093d90c62de` | **MOVED**: the assignment names `093d90c62de`; live is `64e86811548b`. Audit the live head and flag the move. No gate |
| 11310 | `solr-11310-submit` | `e38ddec5279c` | `e38ddec5279` | matches |
| 12044 | `solr-12044-submit` | `c92bbf9349b1` | `6185b96e52a` | **MOVED** since the assignment, but matches the new receipt's gated head `c92bbf9349b1`. Gated; the new receipt is the record |
| 13851 | `solr-13851-submit` | `b60f4d642d1e` | `b60f4d642d1` | matches; submission held, audit only |
| 14381 | `solr-14381-submit` | `a28b3f672cb4` | `a28b3f672cb` | matches |
| 14931 | `solr-14931-submit` | `1a7d678d9a15` | `1a7d678d9a1` | matches |
| 15144 | `solr-15144-submit` | `68b0fc31e056` | `68b0fc31e05` | matches; qualified, owner options |
| 15319 | `solr-15319-submit` | `4bda91f46fc1` | `4bda91f46fc` | matches |
| 15479 | `solr-15479-submit` | `57bd53ce4d07` | `57bd53ce4d0` | matches |
| 15895 | `solr-15895-submit` | `02930909397a` | `02930909397` | matches |
| 16444 | `solr-16444-submit` | `8ffee94a5e75` | `8ffee94a5e7` | matches |
| 17155 | `solr-17155-submit` | `1413237f7a75` | `1413237f7a7` | matches |
| 17372 | `solr-17372-submit` | `048862fda8ab` | `048862fda8a` | matches; gate failed, audit only |
| 17791 | `solr-17791-submit` | `a39c1c973766` | `a39c1c97376` | matches |
| 17841 | `solr-17841-submit` | `478731e67602` | `478731e6760` | matches; retire candidate, audit only |
| 18196 | `solr-18196-submit` | `f3248fe23104` | `f3248fe2310` | matches; already merged via PR #4995, audit only |
| 18506 | `solr-18506-submit` | `77c019e1ff0e` | `77c019e1ff0` (live PR #5029) | matches; consistency pass only |

## Shared rules for every part

- Read only, except the report file and the drafts for your part. No commit, push, checkout, reset, merge, stash, or fetch of anything new. `git show`, `git log`, `git diff`, `git merge-tree`, `git rev-parse`, `git cat-file -e`, `git grep`, `git ls-tree`, and `git merge-base` are fine. Trial merges with `git merge-tree --write-tree` are fine if they write no ref.
- No builds, no Gradle, no tests, no test runs of any kind.
- `gh pr view <number> --repo apache/solr --json 'title,body,headRefOid,mergeable,mergeStateStatus,state'` (one quoted field list) and `gh pr checks` are the only `gh` calls allowed, and only for a live-PR consistency pass. Nothing that writes.
- Post nothing anywhere. No PR, no comment, no JIRA. No new Jira ticket unless someone asks.
- Proof numbers come only from the receipt named for the ticket. Gate logs are often not on disk; say so.
- Drafts follow `pr-formula.md`: the AI header as line 1, the Jira link line, a bold one-line summary in each section, "What happens today", "What this change does", "Proof", "A choice to check" (only with a live alternative and a pointed question), "Limits", the Changelog line with a link, and the AI assistance footer. Length guide about 3,500 characters. Each draft names the head it was written against in its Proof. Proof states what ran, at which head, what passed, and that the new test fails without the fix, when a receipt supports that. A Proof marked inconclusive, pin, failed, or classified is stated as such. No draft may claim a GitHub run conclusion the record does not show.
- Public text carries no internal process vocabulary (gate, receipt, ledger, handoff, top-up, takeover, audit, rc=0, JUnit XML, pre-fix proof as a label, "submission" as a workspace word, internal log names, "premise run").
- Plain words and short sentences. No em dash and no en dash in anything you write.
- Lucene version claims: check against both the 9.x and 10.x lines wherever a draft names Lucene behavior, and say which line you checked.
- A moved tip, or a diff that differs from the receipt, is flagged, not silently adopted.
- Every finding: file and line, evidence (SHA and line, receipt line, or command output), and exact replacement wording. Mark FIX or NOTE. If you cannot check something, say so under "Not checked". Do not guess.

## Parts

Six subagents. Reports go to `reports/search-components-4-<part>.md`. Drafts go to `pr-drafts/search-components/`.

**Part s1: ExactStatsCache pair (SOLR-8051 and 15319).** Both change `ExactStatsCache.java`: 8051 fixes the global stats NPE and has no gate; 15319 fixes the distributed IDF keying and is gated (`TestDistribIDF` 4 of 4, `TestExactStatsCacheLegacyResponse` 3 of 3). The receipt says the dispatched GitHub runs' conclusions are not in the record; no draft may claim them. State how the two diffs compose and which should land first. Draft 15319 if draftable. 8051 is audit only; flag the moved head.

**Part s2: Grouping family (SOLR-7520, 14381, 14931, 17155).** 7520 (focused counts 1, 1, 17, 2; the coordinator gap goes in Limits with a follow-up offer, per the receipt). 14381 (compat test 2 of 2, parity test 9 of 9). 14931 (`TestMacros` 2 and `TestShardMacroExpansion` 1). 17155 (`TopGroupsResultTransformerTest` 1 of 1; GitHub evidence on record is at the pre-conversion head only, as the receipt states). Check overlapping hunks across 7520, 14381 and 17155 in CommandHandler, Grouping and the shard result transformers; 17155's transformer test sits in the same serializer family as 14381's compat test. Draft the draftable ones.

**Part s3: Rerank pair (SOLR-11310 and 15479).** Both share `TestReRankQParserPlugin` and the rerank path: 11310 on the ReRankCollector side, 15479 on the SolrIndexSearcher maxScore side. 11310 is gated (`TestLTRReRankingPipeline` 4 of 4, `TestReRankQParserPlugin` 11 of 11); its receipt names the owner position to confirm before opening, posed as a Choice. 15479 is gated (`TestReRankQParserPlugin` 14 of 14); the maxScore window question is a Choice, per its receipt. State the landing order. Draft both if draftable.

**Part s4: Managed resources triple (SOLR-15895, 16444, 17791).** All three change the managed resources family (`ManagedResourceStorage`, `RestManager`, `ManagedResource`). All three are gated and draftable candidates. Check the three diffs against each other. 15895: counts 2 of 2 and 5 of 5, proof INCONCLUSIVE as recorded; no draft may state it as a pass. 16444: counts 5 of 5 and 3 of 3; the GitHub run's conclusion is not in the record. 17791: counts 2, 10 and 3; the API shape call is deferred in the record, and both options go in the PR text. Draft the draftable ones.

**Part s5: Qualified, failed, and pin (SOLR-15144, 17372, 17841, 6207).** 15144: gated on the unit record, but qualified by the round 35 end-to-end finding and three owner options in its receipt. Audit the options; present it as an owner decision, with option (a)'s corrected claims stated if the audit supports them. Note that option (b) widens to `SearchHandler`, which crosses into sub-batch 2's handler components; note it as a cross-batch consequence, without auditing `SearchHandler`. 17372: GATE FAILED at the live tip on premise non-reproduction; head legs pass; audit only; do not draft it, and do not reuse the round 35 report's Proof claims. 17841: coverage only, proof not applicable by construction; a retire candidate with the retire call pending; audit only. 6207: a documentation pin, `SolrQueryRequestBaseTest` 1 of 1, with no fail-before by design, as its receipt states; draft it if draftable and say the pin plainly.

**Part s6: Audit only and consistency pass (SOLR-8088, 9595, 10694, 12044, 13851, 18196, and the live PR 18506).** 8088, 9595 and 10694 have no gate and their heads moved since the assignment: audit the live head, flag each move, and check any TESTING.md the branch carries. 12044 is gated at `c92bbf9349b1` per its new receipt: confirm that receipt, and draft it if draftable. 13851 is gated green as-is but submission-held regardless: audit only, do not draft. 18196 is already merged via PR #4995 at `f3248fe2310`: the branch is redundant with main and a retire candidate; audit only, do not draft. 18506 is a live PR (#5029) at `77c019e1ff0`, gate green, test only: consistency pass only. The corrected Jira description v2 still awaits the owner's paste, and v1 must not be used. Flag any description drift; do not draft new PR text.

## Deliverables

1. `reports/search-components-4-s1.md` through `-s6.md`. The lead writes `reports/search-components-4.md`, with per-ticket verdicts (draftable, held with reason, audit-only result for the no-gate, held, failed-gate, retire and merged tickets, consistency result for the live PR), the interaction results, and the owner decisions.
2. Drafts in `pr-drafts/search-components/` for the draftable tickets, each naming its head.

Not in scope: opening PRs, posting comments, editing branches or live PR descriptions, builds, Gradle, and test runs.

---
Status: DONE. Marked 2026-10-10 by vm1 (main agent) at Nick's direction, because the completed claim had not been marked. Completion verified from the deliverable on this branch: reports/search-components-4.md. The work was performed by the original claimant; this mark is a record correction, not a new claim.
