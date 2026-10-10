# eDisMax round 3, part e1: SOLR-2309, SOLR-2988, SOLR-4362

Result: 2309 and 2988 match their receipts and round 28 reviews, and both drafts are filed. 4362 matches its receipt on head, base and count, but its slop check misses a decimal slop and a boost placed before the slop, so its draft is HELD until a branch fix lands.

Method: read only. Heads were compared with the origin refs already in this checkout, not a fresh ls-remote. Receipts, round 28 reviews and the local JIRA packet were read. Test counts were taken from the test files at each head and at each base. Trial merges used `git merge-tree --write-tree --merge-base`, which wrote tree objects only and moved no refs. Nothing was built or run. No gh calls and nothing posted.

## Findings

1. FIX (SOLR-4362, blocks its draft). `isSlopClause` misses a decimal slop and a boost placed before the slop.
   - File and lines: `solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java` at e96a057439c, lines 296-331. The `startsWith` test is at line 299, the digit loop at line 303, the boost branch at line 314. The call is at line 350.
   - Evidence (read from the code, not run):
     - `splitIntoClauses` (lines 779-905) keeps `.` and `^` inside a clause value. So `~2.5` becomes `\~2.5`, and `^2~10` becomes `\^2\~10`.
     - Line 303 stops at the `.`, so `\~2.5` is not treated as slop. Line 299 rejects `\^2\~10`, because the value does not start with `\~`.
     - The grammar accepts both spellings. `solr/core/src/java/org/apache/solr/parser/QueryParser.jj` line 149 allows a decimal in `FUZZY_SLOP`. Lines 316-317 let a quoted term take `^N` then `~M`, or `~M` then `^N`. `SolrQueryParserBase.java` `handleQuotedTerm` (lines 865-872) sets the slop with `(int) Float.parseFloat`, so `~2.5` is slop 2. `QueryParser.jj` is identical on upstream/main and upstream/branch_9x. `handleQuotedTerm` has the same lines on both.
     - Result by reading: `"phrase query"~2.5 term` and `"phrase query"^2~10 term` both leave the slop number in a pf2 shingle (`5 term` and `10 term`).
     - Record drift: the round 28 review (`research/branch-reviews/round-28/SOLR-4362-review.md`, line 26) says `"phrase query"~2.5` "is not valid slop, so that is not a gap." The grammar says it is valid and reads it as slop 2. That sentence is wrong and should be struck.
   - Exact replacement:
     (a) After line 27 (`import java.util.Set;`), add `import java.util.regex.Pattern;`.
     (b) Replace lines 296-331 (the javadoc and the method) with:
```java
  private static final Pattern SLOP_CLAUSE =
      Pattern.compile("(\\\\\\^\\d+(\\.\\d+)?)?\\\\~\\d+(\\.\\d+)?(\\\\\\^\\d+(\\.\\d+)?)?");

  /**
   * True for the clause that splitIntoClauses leaves behind for a phrase's slop, such as ~10, ~2.5,
   * ~10^2 or ^2~10. The clause holds no term of its own.
   */
  private static boolean isSlopClause(Clause clause) {
    return clause.val != null && SLOP_CLAUSE.matcher(clause.val).matches();
  }
```
     By reading, the pattern matches `\~10`, `\~2.5`, `\~10\^2`, `\~10\^2.5` and `\^2\~10`. It does not match `\~`, `\^2`, `foo\~2` or `\~10x`.
     (c) In `testPf2DoesNotUseSlopOfPhraseAsTerm` (`solr/core/src/test/org/apache/solr/search/TestExtendedDismaxParser.java`, before the positive control, about line 298), add these two checks. They are suggestions and were not run:
```java
    // a decimal slop is read as its whole part (~2.5 is slop 2), and a boost may come before the slop
    assertQ(
        req(
            "defType", "edismax",
            "q", "\"phrase query\"~2.5 term",
            "qf", "phrase_sw",
            "pf2", "phrase_sw",
            "debugQuery", "true"),
        "//str[@name='parsedquery_toString'][not(contains(.,'5 term'))]");
    assertQ(
        req(
            "defType", "edismax",
            "q", "\"phrase query\"^2~10 term",
            "qf", "phrase_sw",
            "pf2", "phrase_sw",
            "debugQuery", "true"),
        "//str[@name='parsedquery_toString'][not(contains(.,'10 term'))]");
```
   - Next step: the branch edit and the test belong to the main side. Run the focused test at the new head, and confirm both new checks fail on the base code. The analyzed text was not run, so the two `not(contains())` strings are not confirmed.

2. NOTE (SOLR-4362, owner call, adjacent). A boost with no slop, as in `"phrase query"^2 term`, leaves a `\^2` clause after the phrase. By reading, it enters the pf2 shingles the same way, with the number `2` becoming a word. The Finding 1 pattern does not match it. Either extend the pattern to accept a boost-only clause (and rename the method to match), or keep the second Limits bullet in the draft. The draft keeps the bullet.

3. FIX (SOLR-4362, public text, needs owner direction). Commit messages on the branch use internal vocabulary, and a PR's commit list shows them:
   - `52abce333db`, body: "Hypothetical, unrun regression test; see SOLR-4362-TESTING.md." That file is not in the branch's net diff.
   - `6631400a9f4`, subject: "SOLR-4362: add hypothetical-reproduction handoff doc".
   - `f0f3d1f004a`, body: "The hypothetical-reproduction handoff doc is removed; the gate record lives in the takeover log."
   - Exact replacement: squash `52abce333db` through `e96a057439c` into one commit, with subject `SOLR-4362: keep a phrase slop out of the edismax pf, pf2 and pf3 shingles` and no body. A squash rewrites the fork branch, so it needs explicit direction.

4. NOTE (SOLR-2309 and SOLR-2988, landing). They conflict in one place. `git merge-tree --write-tree --merge-base 97d973814336 06f5a1c4a87 d2d144dfd9f4` exits 1 with one conflict, in `ExtendedDismaxQParser.java`. Both branches add a method at the same spot, between the end of `getQuery()` (line 1496 at both heads) and `noStopwordFilterAnalyzer` (line 1520 at 2309). Resolution: keep both. Put the 2309 method `analysisYieldsNoTokens` with its javadoc (2309 lines 1498-1517) first, then the 2988 helper `containsWhitespace` (2988 lines 1498-1500). No other conflict. Whichever lands second makes that edit.
   - Other pairs: 2988 with 4362 merges clean. 2309 with 4362 merges clean. 2309 with 3243 (head 1db99c13662, base 97d973814336) merges clean. 3243's parser change is in `getRangeQuery` (base line 1132), about 340 lines from the 2309 FUZZY hunk, and its tests are separate methods. So 2309 and 3243 are separate hunks, as the record says.
   - 2988 and 4362 share a call path. 4362 changes which clauses reach `addShingledPhraseQueries` (its `normalClauses` list, 4362 line 386). 2988 changes how that method builds its text (2988 lines 637-647). The hunks do not overlap, and either can land first.

5. NOTE (SOLR-2988 receipt). The receipt says "the premise run fails with exactly 3 failures" and does not name the code state. This report and the draft read it as the base code with the new test file, which is how the receipt's wording is used elsewhere. Confirm before filing. The round 32 gate log and goal-file reviews it cites are not on disk.

6. NOTE (SOLR-2988 changelog, optional wording). `changelog/unreleased/SOLR-2988-edismax-pf-string-field.yml` at d2d144dfd9f, title block. The sentence "The phrase text built for pf, pf2 and pf3 is also changed on its own: words are now joined with a single space instead of appending a trailing space after each word." could read: "The phrase text built for pf, pf2 and pf3 also changes: words are joined by one space, with no trailing space."

7. NOTE (code comments, public text).
   - `TestExtendedDismaxParser.java` line 275 (4362 head): "tokenises" should be "tokenizes", the Solr spelling.
   - `ExtendedDismaxQParser.java` line 1473 (2988 head): "so it does match >1 words" should read "so the term can hold more than one word."

8. NOTE (round 28 items, status at the current heads).
   - 2309: F1 (undefined field) is pinned by `testFuzzyOnUndefinedField` (lines 638-647). F2 (mixed qf) is covered (lines 386-458). F3 (proof not recorded) is now recorded. Closed.
   - 2988: F1 (changelog names the separator change) is done. F2 (heuristic) has a comment now, and the heuristic is kept and named in the draft Limits. F3 (score comparison) is done. F4 (proof) is recorded. Closed.
   - 4362: F1 `~N^boost` is handled at e96a057. F2 (pf2 positive control) is added. F4 (handoff doc in history) is still open, see Finding 3. F5 (bare word with a suffix) stays a Limits line, as the record says.
   - 4362 F1 decimal and boost-first forms are not closed, see Finding 1.

9. NOTE (upstream drift). upstream/main is at 8e62c2686882, 40 commits past the base 97d973814336 that round 28 used (round 28 recorded cabedd1d968). None of those commits touched `ExtendedDismaxQParser.java`, `TestExtendedDismaxParser.java` or `schema12.xml`, and none touched them after 14c7aac0d151 either. All three branches still merge against current main on those files.

## Task results

SOLR-2309 (head 06f5a1c4a87eef0fed17435e8d30bbc422079119): consistent, draftable. Draft filed at `pr-drafts/edismax/SOLR-2309.md`. The head matches the origin ref. The receipt's 40 of 40 matches the file: 39 methods named test*, plus `killInfiniteRecursionParse` (base: 38 plus 1). The one fail-before failure, in `testFocusQueryParser`, matches where the new checks sit (lines 386-458, inside a method that starts at line 274). The changelog is `fixed`, by Nick Shanin, with a link. Its one conflict is with 2988 (Finding 4).

SOLR-2988 (head d2d144dfd9f4cd8208c856a7b955e6eb997da146): consistent, draftable. Draft filed at `pr-drafts/edismax/SOLR-2988.md`. The head matches the origin ref. The receipt's 42 of 42 matches the file: base 39 plus the three new tests. The three failures named in the receipt are the three new test names. The receipt's wording gap is Finding 5. The changelog and the round 28 items are in order.

SOLR-4362 (head e96a057439c879427bf07195fe6e18bed08a2c42): the record is consistent, but the branch is not draftable yet. Draft HELD at `pr-drafts/edismax/SOLR-4362.md`. Head, base (14c7aac0d151) and count (40 of 40, base 39) match the receipt and the file, and the one named failure is `testPf2DoesNotUseSlopOfPhraseAsTerm`. But the slop check misses decimal and boost-first slop, so the round 28 statement that this is not a gap is wrong (Finding 1). Finding 3 must also be fixed before filing. Both 2309 and 2988 merge cleanly with it.

Suggested order for these three, from the merge results only: 2309, then 2988 (keep both blocks at the one conflict), then 4362 after its fix. The family order is the lead's call.

## Owner decisions

- Already on record: the 3729 match-all choice, the two 6320 calls and the 14913 alias call. None applies to 2309, 2988 or 4362, so they are not restated here.
- Round 28 open items for these three: none remain open at the current heads, except the 4362 decimal and boost-first gap (Finding 1). That is a fix, not a decision.
- New candidates, not on record and not drafted as Choice sections:
  1. 2988: keep the whitespace check (the draft says so in Limits), or move to a field type check.
  2. 4362 Finding 2: fix the boost-only clause in this PR, or keep the Limits bullet as drafted.
  3. 4362 Finding 3: direction to squash the fork branch history. This rewrites the fork branch.

## Not checked

- The round 32 gate logs (`g2309r32-gate.log`, `g2988r32-gate.log`, `g4362r32-gate.log`), the round 32 goal-file reviews and the takeover log named in the receipts are not on disk. Receipt counts were taken as stated, and they matched the test files.
- No build, test or focused run. The replacement code and the suggested checks in Finding 1 were not compiled or run. The leaks in Findings 1 and 2 were read from the code.
- Heads were compared with the local origin refs (fetched on the claim day), not a fresh `git ls-remote`.
- JIRA text was read from the local packet `research/jira-context/SOLR-*.json`. Live JIRA was not checked.
- GitHub Actions run 37607125537, named in the 2988 receipt, was not checked. No gh calls.
- Lucene: the drafts name no Lucene behavior. Solr's own grammar was checked on upstream/main and upstream/branch_9x, the 10.x and 9.x lines. The 2309 code comment about draining a TokenStream before `end()` was not checked against Lucene 9.x or 10.x, since no Lucene source is in this checkout.
- Trial merges used `git merge-tree --write-tree`, which writes tree objects to the object store. No refs, branches or stashes were changed.
- The Proof lines in the drafts reuse the receipt's verification date, 2026-10-07. They were not re-run.
