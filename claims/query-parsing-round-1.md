# Claim: Query parsing round 1

Claimed 2026-10-09 by Claude Code (AI agent), working for Nick Shanin.

Scope: `assignments/query-parsing-round-1.md` (commit `9bfcb2ff1c1`). Twenty-five tickets: SOLR-874, 4824, 6014, 8977, 9048, 9149, 10897, 11391, 11761, 12212, 12532, 12608, 12871, 13202, 13838, 13903, 15615, 15906, 16130, 16267, 16570, 17280, 17311, 17796, 17882. Audit first, then drafts under `pr-drafts/query-parsing/` for the draftable tickets.

## Heads checked live on 2026-10-09

`git ls-remote`, then a read-only fetch. Named heads match the live heads for every ticket that names one.

| Ticket | Branch | Live head | Named in the assignment | Result |
|---|---|---|---|---|
| 874 | `solr-874-submit` | `ac9ab33753751a6fef05b5f100f5c1c5683510b2` | `ac9ab337537` | matches |
| 4824 | `solr-4824-submit` | `76c777e4661e9240ac66dd01247c0dd1286cacff` | `76c777e4661` | matches |
| 6014 | `solr-6014-submit` | `505849d2d4e7d8699a2fe34d410773b8ecf3a55a` | `505849d2d4e` | matches |
| 8977 | `solr-8977-submit` | `395b24964fd05f2908c0340ef79c1ee4273775a8` | `395b24964fd` | matches |
| 9048 | `solr-9048-submit` | `b145018563cbc2ddd29e43a0fa8619545415b8c6` | `b145018563c` | matches |
| 9149 | `solr-9149-submit` | `151dfed119ee0a6f5a922f3a914665e7b3281427` | `151dfed119e` | matches |
| 10897 | `solr-10897-submit` | `d9240d0750faf55d21fb125668ea66db0142be79` | `d9240d0750f` | matches |
| 11391 | `solr-11391-submit` | `b0f15a22856a887d4e369cd1542c0ce09a687059` | none (no gate) | the fetch moved it from `825d7f81d12`; audit only |
| 11761 | `solr-11761-submit` | `41893ee9ce6101224f46e0287a1f2f0bfe6a6974` | `41893ee9ce6` | matches |
| 12212 | `solr-12212-submit` | `876953fdc92714a1692615bb67685b22c3814483` | `876953fdc92` | matches |
| 12532 | `solr-12532-submit` | `69c06da446797cec5c3c8dd885e19a28381789d3` | `69c06da4467` | matches |
| 12608 | `solr-12608-submit` | `d1dd8a1f9f0aa17c6e34f8c5e40cb9768a83aea2` | `d1dd8a1f9f0` | matches |
| 12871 | `solr-12871-submit` | `c79a49320cdb0390a9ae7118ab70efbf080826d7` | `c79a49320cd` | matches |
| 13202 | `solr-13202-submit` | `c0aec3a7ae01aa629ba1e5e06249568054dc267f` | `c0aec3a7ae0` | matches; live PR, consistency pass |
| 13838 | `solr-13838-submit` | `4520c25abe28228bfa011102814b9b1063a4415a` | `4520c25abe2` | matches |
| 13903 | `solr-13903-submit` | `73ef411dd117cc826caf15baa1c71e50ff5d622e` | `73ef411dd11` | matches |
| 15615 | `solr-15615-submit` | `be77267bf5549ae6bdc91bc4122b5bf5256f41e1` | `be77267bf55` | matches |
| 15906 | `solr-15906-submit` | `50139a8e979cd33319a30317d9371ac0b9198e73` | `50139a8e979` | matches |
| 16130 | `solr-16130-test-followup` | `3c48dec4b79faed625c4fb79833d0975a55f3ed7` | `3c48dec4b79` | matches; live PR, consistency pass |
| 16267 | `solr-16267-submit` | `8f4b0c6d2fb0c89c833512de502f39eb1bec560e` | `8f4b0c6d2fb` | matches |
| 16570 | `solr-16570-submit` | `974c44f9608261adbdb6a2aa9d1f7d7250ccec43` | none (no gate) | the fetch moved it from `5676646936d`; audit only |
| 17280 | `solr-17280-submit` | `40817c5cb7ec96b6f4119a46bec3db8ae6a1f6de` | `40817c5cb7e` | matches |
| 17311 | `solr-17311-submit` | `9f7524582f9de4d6931779bc31811013c414a45f` | `9f7524582f9` | matches |
| 17796 | `solr-17796-submit` | `661165d2673de9211846e276c05daf1fd0e879d6` | `661165d2673` | matches |
| 17882 | `solr-17882-submit` | `d328eb392d9c9acf3d925bb20729798e0d17720f` | none (retire candidate) | the fetch moved it from `74d2726e29b`; audit only |

## Shared rules for every part

- Read only, except the report file and the drafts for your part. No commit, push, checkout, reset, merge, stash, or fetch of anything new. `git show`, `git log`, `git diff`, `git merge-tree`, `git rev-parse`, `git cat-file -e`, `git grep`, `git ls-tree`, and `git merge-base` are fine.
- No builds, no Gradle, no tests, no test runs of any kind. No `gh` calls.
- Post nothing anywhere. No PR, no comment, no JIRA. No new Jira ticket unless someone asks.
- Proof numbers come only from the receipt named for the ticket. Gate logs are often not on disk; say so.
- Drafts follow `pr-formula.md`: the AI header as line 1, the Jira link line, a bold one-line summary in each section, "What happens today", "What this change does", "Proof", "A choice to check" (only with a live alternative and a pointed question), "Limits", the Changelog line with a link, and the AI assistance footer. Length guide about 3,500 characters. Each draft names the head it was written against in its Proof. Proof states what ran, at which head, what passed, and that the new test fails without the fix. If a receipt records a test that does not fail without the fix, the draft says so and does not claim otherwise.
- A needed follow-up is named in Limits with a stated plan to submit it. A defect in a branch's own new code is fixed in that branch, not deferred to a follow-up.
- Public text carries no internal process vocabulary (gate, receipt, ledger, handoff, top-up, takeover, audit, rc=0, JUnit XML, pre-fix proof as a label, "submission" as a workspace word, internal log names, "premise run").
- Plain words and short sentences. No em dash and no en dash in anything you write.
- Lucene version claims: check against both the 9.x and 10.x lines wherever a PR description names Lucene behavior. The 8977 receipt turns on a `luceneMatchVersion` boundary; 4824's fix was verified against the Lucene 10.4.0 constructor delegation. Both need the cross-version check before any version claim is drafted.
- Every finding: file and line, evidence (SHA and line, receipt line, or command output), and exact replacement wording. Mark FIX or NOTE. If you cannot check something, say so under "Not checked". Do not guess.

## Parts

Eight subagents. Reports go to `reports/query-parsing-round-1-<part>.md`. Drafts go to `pr-drafts/query-parsing/`.

**Part q1: SolrQueryParserBase cluster (SOLR-4824, 9149, 11761, 12532).** All four change `SolrQueryParserBase.java`, and 4824, 9149, 11761, and 12532 all extend `TestSolrQueryParser`. State the landing order inside this cluster. For each pair, say whether it conflicts in production code, only in the shared test class, or not at all. Consistency pass and draft for each of the four, as their receipts allow. 4824 carries a parameter-name Choice for the draft. 9149 and 11761: 11761's shipped test had a setup defect fixed at the gate; say so only if the draft needs it.

**Part q2: Grammar pair (SOLR-12212, 17796) and audit of 17882.** 12212 and 17796 both change `QueryParser.java` and `QueryParser.jj`, and both carry a checked-in generated parser. Check that each generated file matches its grammar, and state the landing order. 17796's draft quotes the ticket body, not its title (see its receipt). 12212: the auto-fix trap is handled in the premise; see the receipt, and do not claim more than the run shows. 17882 is premise-dead and a retire candidate; the retire-or-re-aim call is the owner's and still pending. Audit only, no draft.

**Part q3: Join family (SOLR-8977, 9048, and 11391 audit only).** 8977 changes GraphQueryParser and BJQParserTest. 9048 changes FiltersQParser and BJQParserTest. 11391 changes JoinQParserPlugin, has no gate, and is audit only. The 8977 receipt turns on a `luceneMatchVersion` boundary, and 8977 is gated green at 38 of 38. The honest framing is in the receipt: the symptom is already fixed on current main for cores at LUCENE_10_2_0 or later, and the branch closes the configuration-dependent gap. 9048's all-stopword semantics is a genuine Choice for the draft. Check the shared helper `ScoreJoinQParserPlugin.requireFromAndTo` and the shared test class. State an order where they overlap. Do not audit 13202 or 16130 here (part q4).

**Part q4: Live-PR consistency and ledger rows (SOLR-13202, 16130, 13838, 13903).** 13202 is already a live PR, APPROVED and awaiting merge. Consistency pass only. The owner has already decided not to touch an approved PR for tidies. 16130 is already a live PR, test-only, gate green at `3c48dec4b79`. Consistency pass only. Read the live PR descriptions with read-only `gh` calls (`C:\Users\shaninna\dev\Solr-issues\research\gh.ps1 pr view <number> --repo apache/solr --json 'title,body,headRefOid,mergeable,mergeStateStatus'`). The PR numbers are 5012 for 13202 and 5004 for 16130. Nothing else with `gh`. 13838 and 13903 are reconciled green from a ledger table row with no gate log. Flag, do not fill in, any gap that matters. Draft nothing for the two live PRs.

**Part q5: Childfield pair (SOLR-12871, 17311) and 15615.** 12871 and 17311 both change `ChildFieldValueSourceParser.java` and both extend `TestNestedDocsSort`. This is the likeliest real conflict in the category. Check the hunks against each other directly. 15615 (CloudMLTQParserTest 15 of 15): an earlier GATE INVALID at a pre-fix head is superseded; the receipt names the final gate. Say so only as the receipt does. Drafts for the draftable ones.

**Part q6: Dismax pair (SOLR-874, 6014) and 12608.** 874 changes `SolrPluginUtils` (called from `DisMaxQParser`). 6014 changes `DisMaxQParser` itself and extends `TestExtendedDismaxParser`, which 11761 also extends (part q1). Note the coverage split the 6014 receipt records: dismax is covered, a nested `{!edismax}` clause is not. 874's round 28 report owes the description two Choice items on dropping dangling operators. 12608's premise used a scratch-adapted test; the draft's Proof must not claim more than that run shows.

**Part q7: QParser auto-fix thread (SOLR-15906) and 16267, 17280.** 15906 changes `QParser.java` itself. The `QParser.autoFixPureNegative` behavior shapes the premises of 8977 and 12212. If 15906 lands, say whether those two branches' tests or framings change. The 15906 shipped head fixes a regression an earlier head carried; the receipt tells that story. 16267 (TestJsonFacets 30 of 30, TestFunctionQuery 23 of 23; hardened). 17280 (focused class 5 of 5; the filterCache tradeoff kept in the branch is Limits material). Drafts for the draftable ones.

**Part q8: Audit only (SOLR-10897 draft, 16570 audit).** 10897 is gated green (20 focused tests). Audit and draft. 16570 has no gate and is awaiting the pipeline. Audit only, do not draft. Note the collapse pairing with 17796's tests (`TestCollapseQParserPlugin`) without auditing 16570's fix.

## Deliverables

1. `reports/query-parsing-round-1-q1.md` through `-q8.md`. The lead writes `reports/query-parsing-round-1.md`, with per-ticket verdicts, the interaction results, and the owner decisions.
2. Drafts in `pr-drafts/query-parsing/` for the draftable tickets, each naming its head.

Not in scope: opening PRs, posting comments, editing branches or live PR descriptions, builds, Gradle, and test runs. Owner decisions go in each part's report as a short list at the end.

---
Status: DONE. Marked 2026-10-10 by vm1 (main agent) at Nick's direction, because the completed claim had not been marked. Completion verified from the deliverable on this branch: reports/query-parsing-round-1.md. The work was performed by the original claimant; this mark is a record correction, not a new claim.
