# Claim: Search components round 1, sub-batch 2 (handler components)

Claimed 2026-10-09 by Claude Code (AI agent), working for Nick Shanin.

Scope: `assignments/search-components-2.md` (commit `12fc625a7d2`). Twenty-one tickets: SOLR-3044, 6759, 6975, 7550, 8009, 8020, 8767, 8939, 8954, 9124, 10305, 11470, 13568, 13876, 14451, 15018, 17055, 17539, 17748, 17976, 18109. Audit first; drafts under `pr-drafts/search-components/` for the draftable tickets.

## Heads checked live on 2026-10-09

`git ls-remote`, then a read-only fetch. Named heads match the live heads except where marked.

| Ticket | Branch | Live head | Named in the assignment | Result |
|---|---|---|---|---|
| 3044 | `solr-3044-submit` | `04b877e9de18` | `04b877e9de1` | matches; parked, audit only |
| 6759 | `solr-6759-submit` | `62974ef8d18c` | `62974ef8d18` | matches; no gate, audit only |
| 6975 | `solr-6975-submit` | `761aa629bb83` | `761aa629bb8` | matches |
| 7550 | `solr-7550-submit` | `687165651f92` | `687165651f9` | matches |
| 8009 | `solr-8009-submit` | `8795661ddc98` | `8795661ddc9` | matches |
| 8020 | `solr-8020-submit` | `79f790523d2f` | `79f790523d2` | matches |
| 8767 | `solr-8767-submit` | `3b5f2d235732` | `3b5f2d23573` | matches |
| 8939 | `solr-8939-submit` | `a855a2d8965c` | `a855a2d8965` | matches |
| 8954 | `solr-8954-submit` | `1d981abe7000` | `1d981abe700` | matches |
| 9124 | `solr-9124-submit` | `dfc2518214fb` | `101e12d2085` | **MOVED**: the assignment names `101e12d2085`; live is `dfc2518214fb`. Audit the live head and flag the move |
| 10305 | `solr-10305-submit` | `0cf26e5f331b` | `c21ca8c0e75` | **MOVED**: the assignment names `c21ca8c0e75`; live is `0cf26e5f331b`. Audit the live head and flag the move |
| 11470 | `solr-11470-submit` | `f44c294da374` | `f44c294da37` | matches |
| 13568 | `solr-13568-submit` | `ac5d60c214cf` | `ac5d60c214c` (live PR #5014) | matches; consistency pass only |
| 13876 | `solr-13876-submit` | `2e110473dbad` | `2e110473dba` | matches |
| 14451 | `solr-14451-submit` | `e60891d8716d` | `e60891d8716` | matches |
| 15018 | `solr-15018-submit` | `d0f29b4630c0` | `d0f29b4630c` | matches |
| 17055 | `solr-17055-submit` | `925a130e2621` | `f0c401a290b` | **MOVED**: the assignment names `f0c401a290b`; live is `925a130e2621`. Audit the live head and flag the move |
| 17539 | `solr-17539-submit` | `f9d201a278b9` | `bce505f45ac` gated, live tip `f9d201a278b` | matches the live tip; the head difference is flagged, not resolved |
| 17748 | `solr-17748-submit` | `dfacaf347668` | `dfacaf34766` | matches |
| 17976 | `solr-17976-submit` | `56ea43c448ed` | `56ea43c448e` | matches |
| 18109 | `solr-18109-submit` | `b19395e1f60a` | `b19395e1f60` | matches; test only |

## Shared rules for every part

- Read only, except the report file and the drafts for your part. No commit, push, checkout, reset, merge, stash, or fetch of anything new. `git show`, `git log`, `git diff`, `git merge-tree`, `git rev-parse`, `git cat-file -e`, `git grep`, `git ls-tree`, and `git merge-base` are fine. Trial merges with `git merge-tree --write-tree` are fine if they write no ref.
- No builds, no Gradle, no tests, no test runs of any kind.
- `gh pr view <number> --repo apache/solr --json 'title,body,headRefOid,mergeable,mergeStateStatus,state'` (one quoted field list) and `gh pr checks` are the only `gh` calls allowed, and only for a live-PR consistency pass. Nothing that writes.
- Post nothing anywhere. No PR, no comment, no JIRA. No new Jira ticket unless someone asks.
- Proof numbers come only from the receipt named for the ticket. Gate logs are often not on disk; say so.
- Drafts follow `pr-formula.md`: the AI header as line 1, the Jira link line, a bold one-line summary in each section, "What happens today", "What this change does", "Proof", "A choice to check" (only with a live alternative and a pointed question), "Limits", the Changelog line with a link, and the AI assistance footer. Length guide about 3,500 characters. Each draft names the head it was written against in its Proof. Proof states what ran, at which head, what passed, and that the new test fails without the fix, when a receipt supports that. A Proof marked inconclusive or pin is stated as such.
- Public text carries no internal process vocabulary (gate, receipt, ledger, handoff, top-up, takeover, audit, rc=0, JUnit XML, pre-fix proof as a label, "submission" as a workspace word, internal log names, "premise run").
- Plain words and short sentences. No em dash and no en dash in anything you write.
- Lucene version claims: check against both the 9.x and 10.x lines wherever a draft names Lucene behavior, and say which line you checked.
- A moved tip, or a diff that differs from the receipt, is flagged, not silently adopted.
- Every finding: file and line, evidence (SHA and line, receipt line, or command output), and exact replacement wording. Mark FIX or NOTE. If you cannot check something, say so under "Not checked". Do not guess.

## Parts

Six subagents. Reports go to `reports/search-components-2-<part>.md`. Drafts go to `pr-drafts/search-components/`.

**Part h1: RealTimeGet family (SOLR-8009, 8767, 8954, 15018).** All four change `RealTimeGetComponent.java` or its request path. State the landing order, and check the hunks against each other. 8009's receipt already names the duplicate-result behavior it inherits from 8954's path. 8767's `/get` response-shape change needs an explicit owner decision and user-facing description, per its receipt. 15018 is a test-only strengthening on an earlier gated head; both are stated in its receipt. Draft the draftable ones; present 8767's shape change as the owner's decision.

**Part h2: ExpandComponent pair (SOLR-13568 and 13876).** Both change `ExpandComponent.java` and share `TestExpandComponent`. The two maxScore and caching behaviors must not contradict each other in the drafts. State which lands first. 13876's slice-local versus group-wide maxScore question is the owner's call, posed as the Choice with slice-local implemented. 13568 is a live PR (#5014): consistency pass only; flag description drift; no new PR text.

**Part h3: QueryComponent family (SOLR-8939, 17748, 17976).** 8939 changes the stored-fields request factory side of `QueryComponent.java`; 17748 changes `returnFields` null handling in the same file. Check for hunk overlap. 17976 changes `CombinedQueryComponent.java` and `QueryComponent.java` merge behavior; check whether its diff assumes the modernized NamedList behavior that SOLR-3044's parked work touched (already on main). Note the tie-order change and the new `ShardDoc` field that 17976's receipt names. 17748's behavior change must be stated plainly. Draft the draftable ones.

**Part h4: Gated handler singles (SOLR-6975, 7550, 8020, 18109).** 6975: `DistributedQueryComponentOptimizationTest` 10 of 10; the receipt names the Choices candidate and the Limits line. 7550: PeerSync counts 1 of 1 and 2 of 2; the proof is INCONCLUSIVE by construction, and the generic-500 trade-off is an owner-level decision to make explicit. 8020: `ComponentStageLimitsTest` 5 of 5; no Choices section is owed. 18109: test only, counts 7 of 7 and 3 of 3; proof skipped by construction, as recorded. Each draft's Proof must match its receipt exactly.

**Part h5: Rerank and consistency (SOLR-11470, 14451, 17539).** 11470 (`{!bool}` rerank of negative queries): the Proof must rest on the added `{!bool}` parser test, not the shipped Lucene test, per the receipt. 14451: counts 7 and 7; the option-semantics question stays the owner's call. 17539 is a live PR (#4998): gate green at `bce505f45ac`, with the live tip `f9d201a278b` one docs-only commit past it, and GitHub runs successful at both heads. Consistency pass only. Flag the head difference rather than treating either head as wrong. The round 9 review's four description-wording findings are a main-side item: note them, do not re-derive them. Note 11470's relation to SOLR-15479 and SOLR-11310 on the rerank path, without auditing those here.

**Part h6: Audit only (SOLR-3044, 6759, 9124, 10305, 17055).** 3044 is parked: its production hunks are on main via SOLR-18373, and its retarget question is the owner's call. Do not draft it. 6759 has no gate, premise unverified. 9124 and 10305 have no gate. 17055 has no gate. For 9124, 10305 and 17055 the assignment names a different head from the live one: audit the live head, and flag each move. Do not draft any of the five. For each, say what a premise run and a first gate would need to show.

## Deliverables

1. `reports/search-components-2-h1.md` through `-h6.md`. The lead writes `reports/search-components-2.md`, with per-ticket verdicts (draftable, held with reason, audit-only result, consistency pass result for the two live PRs), the interaction results, and the owner decisions.
2. Drafts in `pr-drafts/search-components/` for the draftable tickets, each naming its head.

Not in scope: opening PRs, posting comments, editing branches or live PR descriptions, builds, Gradle, and test runs.
