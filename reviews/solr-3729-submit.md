# solr-3729-submit

- Branch: origin/solr-3729-submit
- Head: 26258a2cbab0 (matches the listed head)
- Base: upstream/main at 9d7cc2884e8a (merge-base 97d973814336, 19 commits behind)
- Scope: 4 commits, 3 files (+136/-2). `ExtendedDismaxQParser.java` (+45/-2: `isStandaloneMatchAll` at lines 743-775, used for the colon-escape decision in `splitIntoClauses` at line 919), `TestExtendedDismaxParser.java` (+85), changelog `SOLR-3729-edismax-parenthesized-match-all.yml` (+8, type `fixed`)
- Verdict: Needs work (the colon-escape exemption can be extended past a boost into a field clause, which bypasses a restrictive `uf`. The fix is narrow.)
- Reviewer: claude-haiku-5-5 (Claude Code), 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. Every claim below rests on reading the diff, the code and the grammar at the listed head.

## Delta check against earlier disposition

- Bulk review `research/branch-reviews/round-28/SOLR-3729-review.md` (verdict Close) was written at snapshot `04dda9110fb`, an ancestor of the head. The delta (`git diff 04dda9110fb 26258a2cbab0`) changes the changelog title (`26258a2cbab`), removes whitespace handling from `isStandaloneMatchAll` and adds tests (`d134c668d57`).
- Round-28 F1 (the ticket's shapes with other clauses are untested): addressed. `testMatchAllColonEscaping` now covers `(*:* -fox)`, `( *:* -fox)`, `(*:* )` and `(foo *:*)`, plus the glued negatives.
- Round-28 F2 (whitespace handling is dead code): addressed. The leading and trailing whitespace skipping is gone. One whitespace check remains in the boost loop; see finding 4.
- Round-28 F3 (changelog names only `(*:*)`): addressed. The changelog now says "and signed or boosted forms".
- Round-28 F4 (malformed boosts fall through): test added (no throw). The cause is finding 1, which the round-28 review did not check.
- Round-28 open decision (keep the broader spellings, or narrow to the ticket): still open; see owner calls.
- Round-27 disposition: taken from the round-28 table; `round-27/SOLR-3729-review.md` was not re-read.

## Findings (ranked)

1. **HIGH, verified by reading the splitter and the grammar (not run). The colon-escape exemption extends past a boost, which bypasses a restrictive `uf`.**
   - `isStandaloneMatchAll` (lines 763-768) takes every character after `^` up to the next `)` or whitespace as the boost. So a `:`, `(` or `+` after a standalone `*:*^N` is inside the "boost", and the colon is not escaped (line 919).
   - `getFieldName` (line 938) looks only at the start of the clause. A clause that starts with `*` has no field, so `disallowUserField` stays true. The per-clause `uf` checks are `isAllowed` at lines 806 and 925 (line 149 is the `_query_` magic field). Neither runs for this clause.
   - The first parse is built from `clause.raw` (`rebuildUserQuery`, base lines 524-545), so the unescaped text reaches the Solr grammar.
   - In `QueryParser.jj`, after `^` the lexer enters the `Boost` state, where only `NUMBER` matches (line 162). The digits end the boost and the rest lexes as ordinary tokens, so a field prefix or a `(` after the digits starts a new clause. `Conjunction()` can be empty (line 177), so no operator is needed.
   - Impact: where `uf` is restricted (the default is `*`, the `UserFields` constructor at lines 1599-1600), a field clause placed after a boosted `*:*` runs against a field the user is not allowed to query. A `+` prefix on that clause makes it required, so the result set can show whether the restricted field matches. Base is safe: only the exact `*:*` is exempt, so `*:*^N` followed by anything is escaped.
   - Fix (text only): the boost run must be digits with at most one `.`, as `isBoostValue` does in SOLR-3962. Any other character should make the helper return false, so the colon stays escaped. Add a test under a restrictive `uf` that asserts a field clause glued after a boosted match-all is not parsed as a field query.
   - Caveat: this is a reading of the grammar and the splitter. A test run would settle it. It should be fixed before any PR.

2. **MEDIUM, verified by reading. The changelog and tests widen more than `(*:*)`.** `(foo *:*)` and `(*:* -fox)` now keep the colon, so a `*:*` inside a larger group is match-all. Under the default OR operator, `(foo *:*)` matches every document. The unparenthesised `foo *:*` already does that in base, because the exact `*:*` clause is exempt. The tests pin this behaviour (`testMatchAllColonEscaping`), so it is intended. The changelog should say that a `*:*` inside a larger group is now match-all too.

3. **LOW, verified. Signed and boosted forms change more than the title suggests.** At base, `-*:*` is escaped, because the raw clause includes the sign and the exact `*:*` test fails, so it became a wildcard term. At head it is MUST_NOT match-all, so `foo -*:*` returns nothing. That is the logical reading, and "signed or boosted forms" covers it. The changelog could say what the negated form returns.

4. **LOW, wording. The boost-loop comment is misleading.** The comment at lines 765-766 says whitespace "ends the boost too". The outer loop then returns false on whitespace, so the clause is escaped. The behaviour is the safe one; the comment should say so.

5. **LOW, hypothesis. Malformed boosts reach the grammar.** `*:*^abc` is accepted by the helper and fails in the grammar, which falls back to the escaped reparse. The new no-throw assertion is correct. Finding 1 is the same root cause, and the fix in finding 1 covers this too.

## Owner calls (not decided here)

- Keep the broader spellings (`+`, `-`, boost, unbalanced parentheses) in this branch, or narrow to the ticket and let SOLR-3962 own the others. Round 28 left this open. This review adds that the boost check should be digits-only whichever way it goes (finding 1).
- Is the `(foo *:*)` widening intended (finding 2)? The tests say yes; the changelog does not say so.

## Interactions with other branches

- SOLR-3962 has its own helper, `isMatchAllDocsClause` (line 364), for the same grammar, used only for pf. The accept sets differ. This helper accepts non-numeric boosts and unbalanced parentheses; 3962 requires a digit boost and wrapping parentheses. Both accept `(*:*^2)`. A clause accepted here but not by 3962 keeps its unescaped colon, and pf still builds a phrase from it. The two helpers should be consolidated or one kept, at PR time.
- SOLR-3243 and SOLR-6320 change other parts of the same file. No shared code with this branch's helper.

## Not checked

- Not compiled, formatted, or run. The finding 1 result comes from reading `QueryParser.jj` and `splitIntoClauses`, not from a run.
- Whether an existing test already covers colon escaping under a restricted `uf` for `*`-prefixed clauses (I did not search).
- The boost value after the first parse was not traced beyond the grammar.
- The round-27 file was not re-read; see the delta section.
- No GitHub or JIRA writes.
