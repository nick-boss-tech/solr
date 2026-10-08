# solr-3243-submit

- Branch: origin/solr-3243-submit
- Head: 1db99c13662d (matches the listed head)
- Base: upstream/main at 9d7cc2884e8a (merge-base 97d973814336, 19 commits behind)
- Scope: 4 commits, 3 files (+99). `ExtendedDismaxQParser.java` (+5: the `getRangeQuery` guard at lines 1137-1139 returns `MatchAllDocsQuery` for an unfielded, inclusive `[* TO *]`), `TestExtendedDismaxParser.java` (+86: four tests and one helper), changelog `SOLR-3243-edismax-unfielded-range.yml` (+8, type `fixed`)
- Verdict: Nearly (the code is small and matches the stated decision; the match-all semantics are an owner call, and the negated form has to be part of that call)
- Reviewer: claude-haiku-5-5 (Claude Code), 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. Every claim below rests on reading the diff and the code at the listed head.

## Delta check against earlier disposition

- Bulk review `research/branch-reviews/round-28/SOLR-3243-review.md` (verdict Needs work) was written at snapshot `c43e00159a2`, which is an ancestor of the head. The delta (`git diff c43e00159a2 1db99c13662d`) touches only the pf test (`1d663bb7a76`) and the changelog wording (`1db99c13662`). The code is unchanged since the snapshot.
- Round-28 F1 (owner decision; the changelog must say that documents with no qf value now match): the changelog now says so (`1db99c13662`). The decision itself is still open.
- Round-28 F2 (exact-string pf assertion): addressed. `testUnfieldedOpenRangeWithPhraseFields` now asserts the clause shape only (`assertOnlyRequiredClauseIsMatchAll`).
- Round-28 F3 (exclusive forms not handled; title reads broader): addressed in the changelog, which now says "inclusive". The exclusive forms still expand per field; see owner calls.
- Round-28 F4 (the null-endpoint comment relies on the grammar): verified. `solr/core/src/java/org/apache/solr/parser/QueryParser.jj`, lines 298-312, map an open end (`*`) to `null` on both ends, so `a == null && b == null` matches `[* TO *]`.
- Round-27 disposition: taken from the round-28 table; `round-27/SOLR-3243-review.md` was not re-read.

## Verified code facts

- Bare `*` already means match-all when no field is given: `SolrQueryParserBase.java:1279` (`"*".equals(field) || getExplicitField() == null`) and the eDisMax override at base `ExtendedDismaxQParser.java:1148`. The new guard follows that precedent.
- Base `getRangeQuery` (base line 1132) delegates to `getAliasedQuery()`, which expands an unfielded `[* TO *]` over every qf field. In base, `[* TO *]` therefore means "has a value in some qf field".
- A negated `-[* TO *]` reaches the grammar as a MUST_NOT clause. `splitIntoClauses` keeps the sign in `clause.raw`, and `rebuildUserQuery` builds the first parse from `raw`, so the grammar produces `MUST_NOT(getRangeQuery(...))`.
- `getQuery()` unwraps a lone `MatchAllDocsQuery`, which is why `isA(MatchAllDocsQuery.class)` holds for `[* TO *]`.
- The early return skips the `this.type`, `this.field` and `this.val` writes. Every entry point that reaches `getQuery()` sets those first, so nothing reads stale state. The draft hypothesis on this is resolved: benign.

## Findings (ranked)

1. **MEDIUM, verified by reading (not run). A negated match-all range changes meaning.** In base, `foo -[* TO *]` excludes documents that have a value in any qf field, so it returns foo documents with no qf value. At head the MUST_NOT clause is `-MatchAllDocsQuery`, so `foo -[* TO *]` returns nothing. The changelog says only that the positive form matches more documents, and no test covers the negated form. A bare `-*` already returns nothing in base, which is the precedent the test Javadoc cites, so the branch is consistent with `*`. It still changes `-[* TO *]`, and the change is not stated. Proposal (text only): say the negated form in the changelog and add a test that pins whichever behaviour the owner chooses, or restrict the rewrite to non-negated clauses.

2. **LOW, verified. Exclusive open ranges still expand per field.** `{* TO *}`, `[* TO *}` and `{* TO *]` keep the base expansion, because the guard requires `startInclusive && endInclusive` (line 1137). The changelog now says "inclusive", so this is documented.

3. **LOW, verified. The pf path still sees the range text.** `addPhraseFieldQueries` skips only `*:*` spellings (the SOLR-3962 change). The `[*` and `*]` clauses remain and build an optional pf phrase. The test documents that the phrase is only a boost and does not pin its text. This is the SOLR-3962 class of problem.

4. **LOW, wording. Helper name.** `assertOnlyRequiredClauseIsMatchAll` checks MUST clauses only, so a SHOULD clause carrying any query passes. That is what the test intends. The name should say "required".

## Owner calls (not decided here)

- Is an unfielded inclusive `[* TO *]` "every document" (this branch) or "has a value in a qf field" (base)? The branch takes the first reading and calls it a compatibility decision. Round 28 recorded that Jan Hoydahl's original direction was literal tokens, and no maintainer has endorsed match-all for the range form.
- If match-all stays, decide the negated form (finding 1) and state it in the changelog.
- Keep the inclusive-only scope (finding 2), or treat the exclusive forms the same way.

## Interactions with other branches

- SOLR-3962: `[* TO *]` is not a `*:*` spelling, so the 3962 helper does not skip it. If 3962 lands, the range text still reaches the pf analyzer (finding 3).
- SOLR-3729 and SOLR-3962 share the `*:*` grammar. This branch uses neither helper and does not touch the colon-escape decision in `splitIntoClauses`.

## Not checked

- Not compiled, formatted, or run. Pass and fail on base and on head are hypotheses.
- `sow=true` and `sow=false` are traced from the test code only, not through the stopword re-parse.
- The round-27 file was not re-read; see the delta section.
- Changelog tooling (parse, tidy) and Error Prone were not run.
- No GitHub or JIRA writes.
