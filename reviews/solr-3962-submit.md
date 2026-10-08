# solr-3962-submit

- Branch: origin/solr-3962-submit
- Head: e7d5f3505035 (matches the listed head)
- Base: upstream/main at 9d7cc2884e8a (merge-base 97d973814336, 19 commits behind)
- Scope: 3 commits, 4 files (+196). `ExtendedDismaxQParser.java` (+75: `addPhraseFieldQueries` skips match-all clauses at line 310 and returns early at line 320; new `isMatchAllDocsClause` at line 364, `wrapsWholeClause`, `isBoostValue`), `TestExtendedDismaxParser.java` (+105, four tests), `schema12.xml` (+8: `text_chars` type and `*_chars` dynamic field), changelog `SOLR-3962-edismax-pf-match-all.yml` (+8, type `fixed`)
- Verdict: Close (no defect found; the string-based detection and the duplicated helper are owner calls, and proof is outstanding)
- Reviewer: claude-haiku-5-5 (Claude Code), 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. Every claim below rests on reading the diff and the code at the listed head.

## Delta check against earlier disposition

- Bulk review `research/branch-reviews/round-28/SOLR-3962-review.md` (verdict Close) was written at snapshot `1f75be3780f`, an ancestor of the head. The delta (`git diff 1f75be3780f e7d5f3505035`) rewrites `isMatchAllDocsClause` as a loop and extends the tests.
- Round-28 F1 (a boost inside the parentheses is not recognised): addressed. The loop at lines 374-391 strips a trailing boost and wrapping parentheses until neither applies. So `(*:*^2)` becomes `*:*^2`, then `*:*`. `testMatchAllDocsSpellingsWithPhraseFields` covers `(*:*^2)` and `+(*:*^2)`, across `pf`, `pf2`, `pf3` and both `sow` values.
- Round-28 F2 (`\:` unescaping matches a user-typed literal): still present; see finding 1.
- Round-28 F3 (pf2 and pf3 guards only): addressed. `testMatchAllDocsShingleWithPhraseFields` uses two-clause queries (`*:* foo`, `foo *:*`), and the pf2 case discriminates.
- Round-28 F4 (`schema12.xml` growth): confirmed. `schema12.xml` at `upstream/main` has no existing `_chars` field, so the new `*_chars` dynamic field does not collide.
- Round-28 F5 (early return on an empty list): still open; see finding 3.
- Round-27 disposition: taken from the round-28 table; `round-27/SOLR-3962-review.md` was not re-read.

## Findings (ranked)

1. **LOW, verified. A user-typed escaped `*\:*` is treated as match-all.** `isMatchAllDocsClause` replaces `\:` with `:` (line 370) before it compares with `*:*`. A user who types the escaped literal gets the same clause text, so pf drops that clause's boost. The main query is not affected; `*\:*` still parses as a literal wildcard term. Round-28 F2 is still open. Options: check the parsed query type (`MatchAllDocsQuery`) instead of the text, or accept the rare case and say so.

2. **LOW, verified. The boost-in-parentheses case is handled at head.** The round-28 finding was against an older snapshot. The loop is the fix. Tests cover `(*:*^2)` and `+(*:*^2)` in the spelling matrix (`testMatchAllDocsSpellingsWithPhraseFields`).

3. **LOW, hypothesis (round-28 F5). The early return on an empty list is most likely neutral.** Line 320 returns when `normalClauses` is empty. For the pf case (zero word-grams), base would call `addShingledPhraseQueries` with an empty list, which builds `""` and parses it. I expect that to add no clause, so the early return changes nothing. Not run.

4. **LOW, verified. Skipping a clause joins its neighbours for shingling.** A skipped `*:*` is removed from `normalClauses`, so `foo *:* bar` produces a `foo bar` shingle. Base already does the same for the `AND`, `OR`, `NOT` and `TO` words (base line 313). This is consistent with base, not new. It is noted here for the record.

5. **Proof.** The score test (`testMatchAllDocsScoreWithPhraseFields`) assumes the 1-gram `text_chars` tokeniser splits `*:*` into single characters, so the base-fails claim is a hypothesis. Not run.

## Owner calls (not decided here)

- String-based detection (this branch) or a check on the parsed query type (finding 1, round-28 F2).
- Consolidate with SOLR-3729's helper at PR time (round-28 cross-branch note). The two helpers accept different spellings; see the 3729 review.

## Interactions with other branches

- SOLR-3729: two helpers for the same grammar; see the 3729 review. This helper unescapes `\:` itself, so it works with or without 3729's escape change.
- SOLR-3243: the `[* TO *]` text still reaches the pf analyzer. Not a 3962 defect, but the same class.
- SOLR-6009: the regex clause text still reaches the pf analyzer. Same class; this branch does not change it.

## Not checked

- Not compiled, formatted, or run. Score and shingle outcomes are hypotheses.
- How the `text_chars` NGram analyzer tokenises `*:*` was not traced beyond the test's own comment.
- The round-27 file was not re-read; see the delta section.
- Spotless and Error Prone were not run.
- No GitHub or JIRA writes.
