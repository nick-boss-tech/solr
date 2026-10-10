# Claim: eDisMax round 3

Claimed 2026-10-09 by Claude Code (AI agent), working for Nick Shanin.

Scope: `assignments/edismax-round-3.md` (commit `5a50bac2cdf`). Thirteen tickets: SOLR-2309, 2988, 3243, 3729, 3923, 3962, 4362, 6009, 6320, 7120, 12092, 14638, 14913. Consistency pass on the settled family, first audits for 3923 and 7120, and an audit for the parked 14638. Drafts under `pr-drafts/edismax/` where the record leaves drafting to do.

## Heads checked live on 2026-10-09

`git ls-remote`, then a read-only fetch. Named heads match the live heads for every ticket that names one. The inventory check the assignment cites was not repeated.

| Ticket | Branch | Live head | Named in the assignment | Result |
|---|---|---|---|---|
| 2309 | `solr-2309-submit` | `06f5a1c4a87eef0fed17435e8d30bbc422079119` | `06f5a1c4a87` | matches |
| 2988 | `solr-2988-submit` | `d2d144dfd9f4cd8208c856a7b955e6eb997da146` | `d2d144dfd9f` | matches |
| 3243 | `solr-3243-submit` | `1db99c13662d8f827511a5d3a51763d7e1e2ee4c` | `1db99c13662` | matches |
| 3729 | `solr-3729-submit` | `0fe7e503945c1a169f76a019392d68bdf4261eb7` | `0fe7e503945` | matches |
| 3923 | `solr-3923-submit` | `723f61ee4ab7438220193db0c39dad32601dfe07` | none (no gate) | the fetch moved it from `059804fcec8`; first audit flags it |
| 3962 | `solr-3962-submit` | `e7d5f350503516dc5a01211cb28917835af64a0b` | `e7d5f350503` | matches |
| 4362 | `solr-4362-submit` | `e96a057439c879427bf07195fe6e18bed08a2c42` | `e96a057439c` | matches |
| 6009 | `solr-6009-submit` | `a41bb034a1f47c08a8f8ea6ebbe46e6d443cbb6f` | `a41bb034a1f` | matches |
| 6320 | `solr-6320-submit` | `cb710c963531c4f32166e6e0b28c45e54b164a3f` | `cb710c96353` | matches |
| 7120 | `solr-7120-submit` | `6a434a7fc3dacf754689855dfecacf689edfcdf7` | none (no gate) | first audit; no earlier tip on record |
| 12092 | `solr-12092-submit` | `ca9573dabd385a38711bd60633973ae46b7a552e` | `ca9573dabd3` | matches |
| 14638 | `solr-14638-submit` | `44cf1c8fc805e629108f12d1b67702f88d155b34` | none (parked) | the fetch moved it from `c5aa8bb7dff`; audit only |
| 14913 | `solr-14913-submit` | `b80221f46d3ce5889c510b97a97d8748a2ea4c7c` | `b80221f46d3` | matches (the gated tree differs by one comment-only commit) |

## Settled, not re-audited

Rounds 27 to 36 settled the six round-27 members, 4362, 6009, 12092, 14913, and the 3729 and 6320 delta findings. The verdicts stand. Per-ticket points the drafts must reflect, not relitigate:
- 3243: the unfielded `[* TO *]` widening is intended, and the draft says so.
- 3962: the boosted and parenthesized match-all spellings are handled.
- 4362: the F5 shape (`term~2` or `term^2` beside a phrase) stays a Limits line.
- 6009: coverage was closed with test additions only.
- 14913: the core premise and its all-invalid pin shapes are proven.

Where an earlier report already carries a draft for the current head (3729 and 6320 in the round 36 reports, 12092 in the round 33 report), check that draft against the formula and the plain-language rule, and adopt or correct it into `pr-drafts/edismax/`. Do not rewrite from scratch without a reason.

## Owner decisions already on record

State these in the drafts. Do not re-pose them as new questions.
- 3729: the implemented set is the broader match-all spellings (signed, boosted, parenthesis forms, and a `*:*` inside a larger group). The alternative is to narrow to the ticket's `(*:*)` alone. The draft poses this in its Choice.
- 6320 finding 1: a mixed-case `And` or `Or` neighbour is not counted as an explicit operator, so both words promote and the query falls back. The remedy is the owner's call, posed as the Choice in the round 36 draft.
- 6320 demotion rule: a standing owner call. The draft states its compatibility effect plainly (a query that used to fall back now parses) and names the contrary expectation recorded on the ticket.
- 14913: ratify the all-invalid alias behavior (`MatchNoDocsQuery` at the head, against the base's silently dropped clause). The recommendation on record is to keep `MatchNoDocsQuery`.

## Cluster parts

Five subagents. Reports go to `reports/edismax-round-3-<part>.md`. Drafts go to `pr-drafts/edismax/`.

**Part e1: SOLR-2309, 2988, and 4362.** Consistency pass on 2309 and 2988 (settled) and 4362 (settled). Pairwise check: 2988 (shingle joining in `addShingledPhraseQueries`) against 4362 (pf2 slop clauses). Both work the phrase-field machinery; check for hunk overlap and changed context. 2309 (the inner-class `getQuery` FUZZY path) against the 3243 and 6009 hunks is handled by parts e2 and e3; confirm 2309's hunks are separate from 3243's.

**Part e2: SOLR-3243, 3729, and 3962.** Consistency pass on all three. Pairwise check: 3729 (`splitIntoClauses` standalone match-all) and 3962 (the pf skip in `addPhraseFieldQueries`). 3962's `isMatchAllDocsClause` mirrors 3729's standalone shape, including the plain-number boost rule. Use a trial merge with `git merge-tree`. State which lands first, and what the second must reconcile.

**Part e3: SOLR-6009 and 6320.** Consistency pass on both. Confirm that 6320 (`rebuildUserQuery`) and 6009 (regexp handling in `getQuery`) are separate methods, as the record says.

**Part e4: SOLR-12092 and 14913.** Consistency pass. 12092 (`noStopwordFilterAnalyzer`) and 2309 both turn on stopword handling in different methods. State whether a query can reach both changes, and whether the order matters. 14913 (alias handling in `getQueries` and `getMultiTermQueries`) fans clauses out over qf fields like several siblings; check its hunks against 3243 and 6009.

**Part e5: SOLR-3923, 7120, and 14638.** 3923 and 7120 have no gate and are unaudited. Do the full first audit for each: what the branch claims, what its testing note claims, whether the diff matches the ticket, and what a premise run would need to show. Place their hunks on the same map as the other eleven branches. Do not draft them. 14638 is parked: audit only. Confirm the branch still matches the ticket and the park note, and say what would have to happen before it could move. Do not draft it.

## Deliverables

1. `reports/edismax-round-3-e1.md` through `-e5.md`. The lead writes `reports/edismax-round-3.md`, with per-ticket verdicts, the family landing order, and any disagreement with the receipts.
2. Drafts in `pr-drafts/edismax/` for the draftable tickets, each naming its head.

All thirteen branches change `ExtendedDismaxQParser.java` and share `TestExtendedDismaxParser` and its `schema12.xml` fixtures. Landing order matters more here than in other categories. Use trial merges where two branches touch the same method.

## Shared rules for every part

- Read only, except the report file and the drafts for your part. No commit, push, checkout, reset, merge, stash, or fetch of anything new. `git show`, `git log`, `git diff`, `git merge-tree`, `git rev-parse`, `git cat-file -e`, `git grep`, `git ls-tree`, and `git merge-base` are fine.
- No builds, no Gradle, no tests, no test runs of any kind. No `gh` calls.
- Post nothing anywhere. No PR, no comment, no JIRA. No new Jira ticket unless someone asks.
- Proof numbers come only from the receipt named for the ticket, or from the latest per-ticket report it names. Gate logs are often not on disk; say so.
- Drafts follow `pr-formula.md`: the AI header as line 1, the Jira link line, a bold one-line summary in each section, "What happens today", "What this change does", "Proof", "A choice to check" (only with a live alternative and a pointed question), "Limits", the Changelog line with a link, and the AI assistance footer. Length guide about 3,500 characters. Each draft names the head it was written against in its Proof. Proof states what ran, at which head, what passed, and that the new test fails without the fix.
- A needed follow-up is named in Limits with a stated plan to submit it. A defect in a branch's own new code is fixed in that branch, not deferred to a follow-up.
- Public text carries no internal process vocabulary (gate, receipt, ledger, handoff, top-up, takeover, audit, rc=0, JUnit XML, pre-fix proof as a label, "submission" as a workspace word, internal log names).
- Plain words and short sentences. No em dash and no en dash in anything you write.
- Lucene version claims: check against both the 9.x and 10.x lines wherever a PR description names Lucene behavior, and say which line you checked.
- Every finding: file and line, evidence (SHA and line, receipt line, or command output), and exact replacement wording. Mark FIX or NOTE. If you cannot check something, say so under "Not checked". Do not guess.

Not in scope: opening PRs, posting comments, editing branches or live PR descriptions, builds, Gradle, and test runs. Owner decisions go in each part's report as a short list at the end. The decisions already on record above go in as recorded, not as new questions.
