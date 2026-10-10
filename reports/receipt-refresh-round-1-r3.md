# Receipt refresh round 1, part r3 (SOLR-3923 and SOLR-7120, edismax)

Result: SOLR-3923 stays on hold with no draft, because the guard still misses signed lone parens at 723f61ee4ab (FIX 1); SOLR-7120 is draftable, with a draft at `pr-drafts/edismax/SOLR-7120.md`, and its changelog title needs one FIX on the branch (FIX 5).

## Heads

Checked live with `git ls-remote` on origin (read only, no fetch). Both match the claim table and the refreshed receipts:

- `solr-3923-submit` = `723f61ee4ab7438220193db0c39dad32601dfe07`
- `solr-7120-submit` = `4664014a919cc30448b784a5d8998c6536f69d55`

The local origin refs match these heads. The local branch refs `solr-3923-submit` (059804fcec8) and `solr-7120-submit` (6a434a7fc3d) are older and were not used.

## Findings

1. **FIX (SOLR-3923). Guard misses signed lone parens.** File `solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java`, lines 310-317 at `723f61ee4ab`.
   Evidence: the guard at line 315 (`if (isOnlyParens(clause.raw)) continue;`) sits inside `if (clause.isBareWord())` (line 310). `isBareWord()` is `must == 0 && !isPhrase` (`Clause`, lines 733-735). `splitIntoClauses` sets `must` for a leading `+` or `-` (lines 770-773). For the input `+(`, the clause has raw `+(`, val `\(`, and `must` set, so the guard is skipped. The val then enters the phrase text through `userPhraseQuery.append(clauses.get(i + j).val)` (line 650). The same holds for `-)`. Read from the code only; not run.
   Replacement for lines 310-317:
   ```java
           if (clause.isBareWord()) {
             String s = clause.val;
             // avoid putting explicit operators in the phrase query
             if ("OR".equals(s) || "AND".equals(s) || "NOT".equals(s) || "TO".equals(s)) continue;
           }
           // a lone paren, with or without a leading + or -, analyzes to nothing and leaves a hole
           if (isOnlyParens(clause.raw.replaceFirst("^[+-]", ""))) continue;
           normalClauses.add(clause);
   ```
   (Indent to the file's existing style: `if (clause.isBareWord())` sits at 8 spaces, as in the current file.) After this fix, the focused run must be repeated at the new head, so the receipt counts change.

2. **NOTE (SOLR-3923). The receipt does not show which assertion failed.** Receipt `receipts/SOLR-3923.md`, line 7.
   Evidence: the new block (lines 1295-1306 at `723f61ee4ab`) sits inside `testPfPs` (starts at line 1197; the next method, `testWhitespaceCharacters`, starts at line 1424). So "one failure, testPfPs" shows that the new block fails on base. The receipt does not name the failing assertion (the `phrase_sw:"zzzz xxxx"` check or the `not(contains(.,'?'))` check) or its message. The gate log is not on disk. The hole mechanism is therefore an inference on disk.
   Replacement for the Proof sentence on line 7, after the owner reads the gate log: "Proof: on base production with the branch test in place, the class runs 39 tests with exactly 1 failure, in testPfPs, at assertion <n> (`<message>`)." Do not write any draft that says the holes exist until this line is filled.

3. **NOTE (SOLR-3923). Receipt wording is wrong on the scope of the change.** Receipt line 8.
   Evidence: fielded clauses never reach the guard (`clause.field != null` skips them at line 308). The change skips lone paren clauses only.
   Replacement for the phrase "so a spaced fielded clause no longer puts placeholder terms into the pf, pf2 and pf3 phrase text": "so a lone paren clause, such as the `(` before `cat:foo` in `( cat:foo ) bar`, no longer puts placeholder terms into the pf, pf2 and pf3 phrase text."

4. **NOTE (SOLR-3923). Changelog title asserts holes and a `"? ?"` string that no run on disk shows.** File `changelog/unreleased/SOLR-3923-edismax-pf-lone-parens.yml`, line 2, at `723f61ee4ab`.
   Evidence: the `"? ?"` string comes from the ticket's comment as the earlier audit noted, not from a run. Keep line 2 only after Finding 2 is filled in from the gate log. If the failing assertion does not show holes, replace line 2 with: `  edismax no longer puts a lone "(" or ")" clause into the pf, pf2 and pf3 phrase text.`

5. **FIX (SOLR-7120). The changelog title says "bare RuntimeException", which is not what the base throws.** File `changelog/unreleased/SOLR-7120-edismax-no-qf-df-bad-request.yml`, line 2, at `4664014a919`.
   Evidence: the base line 1733 (`cabedd1d968`, and the removed line in the diff) is `throw new RuntimeException(e);`. `RuntimeException(Throwable)` takes the cause's text as its message. `solr/core/src/java/org/apache/solr/servlet/ResponseUtils.java` lines 94-100 write `causedBy.getMessage()` into the error response for a non-Solr exception, and line 70 sets the default code to 500. So the message text is present, and "bare" is wrong.
   Replacement for line 2: `  edismax answers 400 with "Neither qf nor df are present." instead of a 500 when a request has neither qf nor df.`
   The draft does not quote the title, so the draft does not depend on this fix. The branch needs it before any PR.

6. **NOTE (SOLR-7120). Receipt line 8 understates the scope.** Receipt `receipts/SOLR-7120.md`, line 8.
   Evidence: `QParser.getParser` calls `qplug.createParser` (`solr/core/src/java/org/apache/solr/search/QParser.java` line 456), and `ExtendedDismaxQParserPlugin.java` line 33 builds `new ExtendedDismaxQParser(...)`. Local parameters such as `{!edismax}` use the same path. The constructor (`ExtendedDismaxQParser.java` line 114) calls `createConfiguration` (line 360), which reads `qf` and `df` at line 1731. So the change applies to any edismax parser built without `qf` or `df`.
   Replacement for "an edismax request with neither qf nor df answers 400": "an edismax parser built with neither qf nor df, at top level or in local parameters, answers 400."

7. **NOTE (SOLR-7120). Placement differs from DisMax.** `ExtendedDismaxQParser.java` lines 114 and 1731 (edismax reads `qf` and `df` while building the parser). `DisMaxQParser.java` line 88 (DisMax reads them in `parse()`, which starts at line 83).
   Evidence: the draft's Limits section names this placement and the ticket's remark on it (`research/jira-context/SOLR-7120.json`, comment 14503066). The branch keeps the placement. No replacement needed; the draft already carries the offer.

8. **NOTE (SOLR-7120). The count matches the file.** Test file `solr/core/src/test/org/apache/solr/search/TestExtendedDismaxParser.java` at `4664014a919`, lines 994-1002.
   Evidence: at base there are 38 `public void test*` methods plus one `@Test` method with another name (`killInfiniteRecursionParse`, line 3329), so 39 tests. The branch adds `testNeitherQfNorDfIsBadRequest` (line 994, no annotation), so 40. The receipt's "40 of 40" agrees with the file. This is a count from the source, not a run. No replacement.

9. **NOTE (earlier audit). The e5 audit used the wrong description.** `reports/edismax-round-3-e5.md`, Finding 6 (line 33), says the bare RuntimeException "hides" the text "Neither qf nor df are present." Evidence: see FIX 5. The text is in the base response, under the wrong status and class. The lead should not carry "hides" into any round summary.

## Task results

**SOLR-3923 (edismax pf lone parens): HOLD. No draft written.** The live head matches (`723f61ee4ab`). The handoff note is gone at this head (`SOLR-3923-TESTING.md` is absent). The receipt's guard claim is true only for unsigned lone parens. Signed lone parens such as `+(` and `-)` still reach the phrase text (FIX 1), so the condition for a draft in the claim is not met. The premise is partly settled: the new block fails on base in `testPfPs`, which shows that the test discriminates. The receipt does not name the failing assertion, so the position-hole mechanism is not shown on disk (NOTE 2). Remaining work: FIX 1 on the branch, a new gate run at the new head, then the receipt wording and changelog title (NOTES 2-4) before any draft.

**SOLR-7120 (edismax no qf or df): DRAFTABLE. Draft at `pr-drafts/edismax/SOLR-7120.md`.** The live head matches (`4664014a919`). The handoff note is gone at this head (`SOLR-7120-TESTING.md` is absent). The one-line change at line 1733 is present (wraps the error as `SolrException` with `BAD_REQUEST`), and the message is the same text as `DisMaxQParser.java` lines 60-61 (`Neither qf nor df are present.`). The test count agrees with the file (NOTE 8). The changelog fragment has the logchange shape (`title`, `type: fixed`, which is a listed key, `authors`, `links`). The draft states the status change openly and the wider scope (local parameters), and it names the placement difference in Limits. It has no "A choice to check" section, as the earlier audit advised. Before any PR, the branch title needs FIX 5. The draft's Proof names head `4664014a919cc30448b782a5d8998c6536f69d55` and the counts from the receipt. The receipt's counts were not re-run.

## Owner decisions

- SOLR-3923: no choice for the owner. FIX 1 is a guard correction. The owner should decide when the branch gets the fix and a new gate run. The receipt counts will change after that run.
- SOLR-7120: none needed. The 500 to 400 change is stated in the draft. The owner would change the draft only if the 500 should stay.
- SOLR-7120: keep or drop the offer of a follow-up for the constructor placement (draft Limits, last sentence).

## Not checked

- No build, compile, Error Prone, Spotless, Gradle, or test run. The test code (try-with-resources, `expectThrows`, the lambda) follows patterns already in the file (lines 154-155 and 3014-3023), but nothing was compiled.
- Gate logs, JUnit XML, and the failing messages are not on disk. The 40 and 39 counts were checked against the source files, not a run.
- Lucene analysis: whether `text_sw` (`solr/core/src/test-files/solr/collection1/conf/schema12.xml` lines 508-530; `phrase_sw` maps to it through `*_sw` at line 708) leaves a position gap for a `(` token. Lucene source is not in this tree.
- The `+(` path to the phrase text was traced by reading only.
- HTTP status: the 500 default and the 400 path were traced in `HttpSolrCall.sendError` and `ResponseUtils.getErrorInfo`, not run.
- The changelog YAML was not parsed with Solr's changelog tool. The shape was compared with `changelog/logchange-config.yml` and a sample fragment.
- Live JIRA was not re-read. `research/jira-context/SOLR-7120.json` was used; its last update is 2015-04-20. It quotes the older `throw new RuntimeException();` without a cause.
- The DisMax 400 path was not re-traced, and the draft does not use it.
- Other callers: a grep found the plugin (line 33), `QParser.getParser` (line 456), and the test's direct constructor calls (`TestExtendedDismaxParser.java` lines 1543-1588). Those test calls still get a `RuntimeException` subclass, so `expectThrows(RuntimeException...)` style checks still pass.
- Nothing was committed, pushed, posted, or checked out. No `gh` calls.
