# solr-2988-submit

- Branch: origin/solr-2988-submit
- Head: d2d144dfd9f4
- Base: upstream/main at 9d7cc2884e8a (merge-base 97d973814336, 19 commits behind)
- Scope: 3 commits. `ExtendedDismaxQParser.java` (+13/-1: `TermQuery` branch in the `PHRASE` case, `containsWhitespace` helper, `TermQuery` import, and the pf phrase text now joins words with a single space), `TestExtendedDismaxParser.java` (+109: three tests), changelog `SOLR-2988-edismax-pf-string-field.yml` (+12, type `fixed`)
- Verdict: Close (same meaning as the round 28 review: no defect found; wording and proof items remain)
- Reviewer: claude-haiku-5-5 (Claude Code), 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. Every claim below rests on reading the diff and the code at the listed head.

## Delta check against earlier disposition

- Handoff: round 32 eDisMax disposition at this head. Head unchanged: `origin/solr-2988-submit` is at `d2d144dfd9f4`, as listed.
- Bulk review (round 28, `research/branch-reviews/round-28/SOLR-2988-review.md`): Close, at snapshot `c4806a934ac`. Its open items were F1 (name the separator change and the ranking drift in the changelog), F3 (compare scores explicitly rather than rely on docid order), and F4 (proof).
- Delta since the snapshot: the changelog now names the separator change, and the ranking note is present ("so ranking may change for such configurations"). The score test now compares scores explicitly. So round-28 F1 and F3 are addressed. F4 (proof) is not addressed by anything in the tree; no queue result was checked in this review.

## Verified code facts

- The new `TermQuery` branch sits in the `PHRASE` case, after the `SpanQuery` case and before `else if (minClauseSize > 1) return null`. With `minClauseSize <= 1` the code falls through to `return query`, so the branch returns the same value the fall-through would. The only behaviour change is when `minClauseSize > 1`.
- `minClauseSize` is set to 2 only in the pf-shingle parser (`ExtendedDismaxQParser.java:677`). Its default is 0 (line 987). So the change affects pf, pf2 and pf3 only, not the main query parse.
- On a tokenised field, the query analyzer yields a `PhraseQuery` (or `BooleanQuery`), so the new branch is not reached. It applies to `StrField` and keyword-tokenised `TextField`, where the whole phrase is one term.
- The `userPhraseQuery` change (words joined by a single space, no trailing space) is needed for the whole-value match. With the old trailing space, a phrase such as `"hard drive "` would not equal a stored `hard drive`. This is correct and separately described in the changelog.
- `containsWhitespace` uses `Character::isWhitespace`, so tabs and newlines also count.

## Findings (ranked)

1. **LOW, verified. Ranking change is intended and named.** Configurations that already list a non-tokenised field in `pf`, `pf2` or `pf3` will now get boosts they did not get before. The changelog says "so ranking may change for such configurations". No existing deployment was checked.

2. **LOW, verified. The changelog title is three sentences on one folded line.** The first sentence says "as dismax does". That claim about dismax was not checked against dismax code in this pass. Shorten or confirm it.

3. **Positive, verified by reading (not run). Discriminating tests.**
   - `testPfOnNonTokenizedField` asserts `id:hard drive` in the parsed query. On base, the `StrField` TermQuery is dropped by `minClauseSize > 1`, so the assertion fails there.
   - `testPfBoostScoresOnNonTokenizedField` has a control document indexed first with identical subject content. Without the boost the control sorts first by docid. The second query boosts the matching document, and the test compares scores explicitly rather than relying on docid order. Good.
   - `testPf2Pf3OnNonTokenizedField` covers the shingle sizes 2 and 3 at parse level and at score level, and the control again sorts first on base.

4. **LOW, hypothesis. Test fixture dependencies.** The tests rely on `id` being a `StrField` and on `name` and `subject` being in the test schema with the expected types. Not checked against `schema12.xml`.

5. **LOW, verified. Tests use `fq` on a marker field and delete it in `finally`.** This keeps the shared test index clean for later tests. Good practice; noted.

## Owner calls

None blocking. Finding 1 is a release-note wording decision the owner may want to make.

## Not checked

- Not compiled, formatted (spotless), or run. Pass and fail on base and head is a hypothesis (finding 3).
- The test schema field types for `id`, `name`, and `subject` (finding 4).
- Dismax behaviour cited in the changelog title (finding 2).
- Error Prone was not run.
- Nothing pushed or posted to GitHub or JIRA.
