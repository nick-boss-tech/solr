# Claim: Search components round 1, sub-batch 1 (facets)

Claimed 2026-10-09 by Claude Code (AI agent), working for Nick Shanin.

Scope: `assignments/search-components-1.md` (commit `e903baf1359`). Eleven tickets: SOLR-5394, 6193, 6831, 10492, 10844, 11129, 12556, 15331, 16290, 17051, 18482. Audit first; drafts under `pr-drafts/search-components/` for the draftable tickets.

## Heads checked live on 2026-10-09

`git ls-remote`, then a read-only fetch. Named heads match the live heads except where marked.

| Ticket | Branch | Live head | Named in the assignment | Result |
|---|---|---|---|---|
| 5394 | `solr-5394-submit` | `967445622f92` | `967445622f9` | matches |
| 6193 | `solr-6193-submit` | `ec94bf50c80c` | `ec94bf50c80` | matches; scope disputed |
| 6831 | `solr-6831-submit` | `96b33ba5f6f8` | `96b33ba5f6f` | matches |
| 10492 | `solr-10492-submit` | `ecf21e2192cb` | `ecf21e2192c` | matches |
| 10844 | `solr-10844-submit` | `e87515c56d04` | `e87515c56d0` | matches |
| 11129 | `solr-11129-submit` | `6c1356bdff70` | `6c1356bdff7` | matches |
| 12556 | `solr-12556-submit` | `033ec65a0e1b` | `033ec65a0e1` | matches |
| 15331 | `solr-15331-submit` | `6769b4cd8a12` | `6769b4cd8a1` | matches |
| 16290 | `solr-16290-submit` | `ff760120c817` | `ff760120c81` | matches; held pin, audit only |
| 17051 | `solr-17051-submit` | `148544e9ed54` | `148544e9ed5` | matches |
| 18482 | `solr-18482-submit` | `49ca9099d8eb` | `49ca9099d8e` (live PR #5009) | matches; consistency pass only |

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

Six subagents. Reports go to `reports/search-components-1-<part>.md`. Drafts go to `pr-drafts/search-components/`.

**Part f1: SOLR-5394 and 10844.** Both gated; both change `SimpleFacets.java` or its test class. 5394 has a Choice and Limits in its report draft. 10844 names the Choices candidate and the Limits lines the description owes, and its premise matrix is recorded. Check each draft against its receipt. Check whether the two diffs overlap in `SimpleFacets.java`, and state the landing order. Draft the draftable ones.

**Part f2: SOLR-10492 and 11129.** 10492's group.field fallback edge belongs in the description, per its receipt. 11129's receipt notes that the coordinator now honors the other per-field local params too, while the changelog names only `facet.mincount`. That scope note belongs in the draft. Check the two diffs against each other: both concern how the coordinator treats per-field facet params in distributed search, and 6193 (part f3) is on the same ground. Draft the draftable ones.

**Part f3: SOLR-6193 (scope audit first).** The gate passed, but the premise audit and the round 35 review say the branch covers distributed `facet.pivot` merging, while the ticket is about `facet.field` local params. Audit the scope question first. A draft follows the audit's answer, not the gate. Check the overlap with 11129's coordinator hunks, since both touch per-field facet params.

**Part f4: SOLR-6831 and 12556.** 6831 is gated (`TestQueryLimits` 4 of 4). 12556 is gated (`TestJsonFacetRefinement` 13 with 1 skipped). The receipt names the Limits for 12556 (buckets first seen during refinement) and a changelog sentence that is true only for buckets known before refinement. Check the changelog sentence against the code. Draft the draftable ones.

**Part f5: SOLR-17051, 15331, and 16290 (audit only).** 17051 is gated (`TestJsonFacets` 30 of 30, recorded as PASS with no failure shape in the record). 15331 is gated, with proof INCONCLUSIVE by construction. No draft may state a proof pass for it. 16290 is a held gated pin, audit only; its disposition (pin PR, fund the real fix, or bank) is the owner's call already on record. Do not draft 16290. Note the relation between 12556's refinement area and 16290's exclusion recompute, without auditing 16290 past its held state. Note where 12556, 17051 and 15331 sit on three different layers of the JSON facet path, and where one draft's claim depends on another layer.

**Part f6: SOLR-18482 (live PR consistency pass).** The live PR is apache/solr #5009 at `49ca9099d8e`. No full gate at the tip is recorded; a confirmation run at the tip is green. Check the branch, the receipt, and the live PR description against each other. Do not draft new PR text. Flag any description drift.

## Deliverables

1. `reports/search-components-1-f1.md` through `-f6.md`. The lead writes `reports/search-components-1.md`, with per-ticket verdicts, the interaction results, and the owner decisions.
2. Drafts in `pr-drafts/search-components/` for the draftable tickets, each naming its head.

Not in scope: opening PRs, posting comments, editing branches or live PR descriptions, builds, Gradle, and test runs.
