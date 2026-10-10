# eDisMax round 3, part e2 (SOLR-3243, 3729, 3962)

Result: all three drafts are filed in `pr-drafts/edismax/`. 3729 is the first to land. 3962 needs a reconcile with 3729 before it is final (FIX 2 and 3). 3243 needs its negated form stated and tested (FIX 1). Nothing was built, run, posted, or committed.

Heads checked against the claim table (origin remote-tracking refs, not re-fetched): 3243 `1db99c13662`, 3729 `0fe7e503945`, 3962 `e7d5f350503`. All three match.

## Findings

1. **FIX (3243): the negated form changes meaning, and nothing states it.**
   File: `solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java` L1137-L1139 at `1db99c13662`. Changelog: `changelog/unreleased/SOLR-3243-edismax-unfielded-range.yml`, title line.
   Evidence: the guard returns `MatchAllDocsQuery` for every unfielded inclusive open range. `splitIntoClauses` keeps the sign in `clause.raw` (L876 and L882 at `1db99c13662`), and `rebuildUserQuery` rebuilds from `raw`, so the grammar wraps the range in MUST_NOT. `foo -[* TO *]` therefore returns nothing. In base it returned `foo` documents with no qf value. The three new tests (`TestExtendedDismaxParser.java` L162-L234 at `1db99c13662`) use no negated form. The Oct 8 review (`wt/chan-A1/reviews/solr-3243-submit.md`, finding 1) reached the same result.
   Replacement, draft: done (Limits in `pr-drafts/edismax/SOLR-3243.md`). Replacement, branch: add a negated assertion to the test file, and change the changelog title to: "edismax treats an unfielded inclusive range [* TO *] as a match all docs query instead of expanding it over every qf field. Documents with no value in any qf field now match, and a negated -[* TO *] now excludes every document." Then re-run the focused proof. This is a branch change, not made here.

2. **FIX (3962, reconcile after 3729 lands): the helper misses spellings that 3729 treats as match all.**
   File: `ExtendedDismaxQParser.java` L309-L310 and L358-L427 at `e7d5f350503`. Test list: `TestExtendedDismaxParser.java` L189.
   Evidence: `isMatchAllDocsClause` strips one leading sign (L366-L368), then only outer parentheses. `(+*:*)` becomes `+*:*` and fails. 3729's `isStandaloneMatchAll` (L743-L798 at `0fe7e503945`) accepts any run of `(`, `+`, `-`. Spellings 3729 accepts and 3962 rejects: `(+*:*)`, `(-*:*)`, `-(+*:*)`, `+(-*:*)`, `+-*:*`, and unbalanced forms such as `(*:*`. After 3729 lands, `(+*:*)` is match all in the main query, but the phrase step still builds a phrase from the literal text, so the score bug stays for those spellings.
   Replacement, after 3729 lands: at L310 replace `if (isMatchAllDocsClause(clause)) continue;` with `if (isStandaloneMatchAll(clause.raw)) continue;`. Delete L358-L427 (`isMatchAllDocsClause`, `wrapsWholeClause`, `isBoostValue`, with their javadoc). At L189 add `"(+*:*)", "(-*:*)"` to the spelling list. This swap works only after 3729 lands, because only 3729 leaves a standalone colon unescaped. Before that, the helper must keep its unescape step. Re-gate needed.

3. **FIX (3962): a typed `*\:*` is skipped by the phrase step. The fix cannot be made inside 3962 alone.**
   File: `ExtendedDismaxQParser.java` L369-L370 at `e7d5f350503` (`s = s.replace("\\:", ":")`). This confirms finding 1 of the Oct 8 3962 review.
   Evidence: a typed `*\:*` keeps its backslash (splitter L853-L861 at `0fe7e503945`, the same code as base). A bare `*:*` gets the same escape (L954 at `e7d5f350503`, base L879, the same code). Both give the raw text `*\:*`, so 3962 cannot tell them apart. 3729 makes the bare standalone colon stay `*:*`, which removes the ambiguity.
   Replacement: none inside 3962 alone. Finding 2 removes L369-L370 when it lands. Until then the 3962 draft's Limits names the typed-escape case.

4. **NOTE (3962): repeated boosts are accepted by 3962 and rejected by 3729.**
   File: 3962 `ExtendedDismaxQParser.java` L377-L382 (`lastIndexOf('^')` loop). 3729 L763 (`!seenBoost`).
   Evidence: `*:*^2^3` strips to `*:*` in 3962 and is skipped by the phrase step. 3729 rejects the second `^`, so the main query reads the text literally. Result: a pf boost is dropped for literal text. Resolved by finding 2. Not run.

5. **NOTE (3962, if 3962 lands before 3729): the comment at L369 becomes wrong after 3729.**
   Current text: `// splitIntoClauses escapes the colon of every clause except a bare *:*; undo that here`. After 3729 the splitter also leaves `(*:*)` and other standalone forms unescaped. Replacement if this order is used: `// splitIntoClauses escapes the colon of every clause except a standalone match all query; undo that here`. Under the recommended order, finding 2 deletes the comment.

6. **NOTE (3729 javadoc, `ExtendedDismaxQParser.java` L737-L742 at `0fe7e503945`): the wording is narrower than the code.**
   The javadoc says "wrapped in any number of parentheses" with an optional sign. The code also accepts unbalanced parentheses and repeated signs (L746-L753). Replacement: `/** Whether a clause is a standalone "match all" query: a {@code *:*} term with any leading {@code (}, {@code +}, or {@code -} characters, then any closing {@code )} characters and an optional plain-number boost. Parentheses are not counted, so an unbalanced clause such as {@code (*:*} is accepted; the grammar then rejects it and the query falls back to the escaped parse. A {@code *:*} glued to other text, such as {@code foo(*:*)bar}, is not standalone. */`

7. **NOTE (3729 test comment, `TestExtendedDismaxParser.java` L207-L209 at `0fe7e503945`): the comment is wrong for `*:*^abc`.**
   Evidence: the helper returns false for `*:*^abc` (L790-L791, `if (!seenDigit) return false`), so the colon is escaped and the query parses as literal text. Only `*:*)))` reaches the grammar. The assertion (`assertNotNull`) still holds.
   Replacement comment: `// a malformed boost makes the helper reject the clause, so its colon is escaped; extra closing parens are accepted by the helper and then rejected by the grammar. Either way the query parses without throwing.`

8. **NOTE (3962, early return, L319-L320 at `e7d5f350503`): not proven neutral.**
   Base `addShingledPhraseQueries` with an empty list and word-gram 0 builds `""` and parses it (base L619-L650). That probably adds no clause, but nothing runs or pins it. The early return also changes fielded-only queries under pf with word-gram 0, not only `*:*`. Replacement: none to the code. The draft does not claim neutrality. Before filing, run or name this case.

9. **NOTE (3243 review count, `wt/chan-A1/reviews/solr-3243-submit.md`, Scope line): the test count is wrong.**
   The review says "+86: four tests and one helper". The diff adds three `@Test` methods (count goes from 14 to 17; test methods at L164, L189, L225 at `1db99c13662`) and one helper (L236). The receipt says "3 new or strengthened", which is right. Replacement: "three tests and one helper".

10. **NOTE (count arithmetic, receipts): the totals imply 39 base tests, the grep finds 38.**
    Receipts give 41, 42, and 43 totals for 3729, 3243, and 3962. Each equals a base of 39 plus its new methods. A grep for `public void test` in `97d973814336` finds 38. The receipts agree with each other, so this is not record drift. I did not find the 39th method. Not checked further.

## Task results

**SOLR-3243 (head `1db99c13662`): draftable, held until finding 1 is settled.** The receipt (42 of 42; base fails exactly the three new tests; verified 2026-10-07) matches the diff. The Oct 8 review matches the code and the owner decision, but it miscounts the tests (finding 9) and misses the negated form (finding 1). The draft is `pr-drafts/edismax/SOLR-3243.md`. Its Proof names the head, and its Limits states the negated form and the exclusive forms. Its Choice poses the widening as a live reading (see Owner decisions). In code, 3243 is independent of the other two: its only hunk is L1135-L1139, inside `getRangeQuery`.

**SOLR-3729 (head `0fe7e503945`): draftable, first to land.** The receipt (41 of 41; base fails exactly `testFocusQueryParser` and `testMatchAllColonEscaping`; the user-fields test passes on base) matches the diff. The receipt names a round 36 report with a draft for this head. That report is not on disk, so nothing could be adopted. The draft `pr-drafts/edismax/SOLR-3729.md` was written from the receipt, the diff, and the Jira packet. Its Proof says the user-fields test passes on base, so the draft does not claim that every new test fails. Findings 6 and 7 are comment and javadoc fixes, with no behavior change. The owner call is posed as its Choice.

**SOLR-3962 (head `e7d5f350503`): draftable as written, held for the reconcile.** The receipt (43 of 43; base fails exactly the four pf tests) matches the diff (four new `@Test` methods, plus the schema field). The draft `pr-drafts/edismax/SOLR-3962.md` names that head. Findings 2 and 3 mean it must be re-issued after 3729 lands and the helper is swapped. It has no Choice section: string checking against a parsed-query check is an implementation detail, not a behavior a maintainer would pick.

**Pairwise and landing order.** `ExtendedDismaxQParser.java` merges with no conflict in every pair and for all three (`git merge-file` exit 0, `git merge-tree` shows no conflicting hunk). `TestExtendedDismaxParser.java` conflicts in every pair at one anchor: each branch adds its tests right after `testMatchAllDocs` (base L151-L161). Resolution is to keep both sides. A simulated union of all three keeps 23 `@Test` methods (14 base, plus 2, 4, and 3) and no duplicate method names. `schema12.xml` changes only in 3962 (L506-L511, L716), so it does not conflict.

Shared boost rule: 3729 (L790-L791) and 3962 (`isBoostValue`, L413-L427) accept the same plain numbers, digits with at most one dot and at least one digit. They differ on signs inside parentheses, unbalanced parentheses, repeated boosts, and the `\:` unescape. Those differences are findings 2, 4, and 6.

**Recommended family order for this part:**
1. 3729, once the owner call is made. It changes the escape decision, which 3962 cannot work around (finding 3), and its helper is the one to share.
2. 3962, with the finding 2 swap and the two added spellings. Its test file merge keeps both sides.
3. 3243, at any point. Its code hunk does not overlap 3729 (L737-L798, L940-L943) or 3962 (L309-L320, L358-L427). Its tests join the same anchor as the other two.

If 3962 has to go first, it keeps its own helper and its Limits names the typed escape. 3729 then changes only the 3962 comment at L369 (finding 5).

Interactions to note in the drafts: `[* TO *]` text still reaches the phrase analyzers after 3243 and 3962. Both Limits name it. `[* TO *]` is not a standalone match-all form, so 3729 does not touch it.

## Owner decisions

- **SOLR-3729, broader or narrower match all set: already on record, not re-posed as new.** The receipt calls the owner call "still open". The claim lists it under decisions on record. Both agree that the draft poses it as the Choice, and the draft does that. Nothing else needs a ruling before the draft is used.
- **SOLR-3243, unfielded `[* TO *]` means every document: already on record as intended.** The draft states it in What this change does. I also added a short Choice, because the widening is a compatibility change with a live alternative (the literal-token reading of the first 2012 patch). This is my drafting call. If the owner wants only the Limits line, drop the Choice section.
- **SOLR-3962: no owner decision on record.** No Choice section.
- **Landing order (recommendation, needs acceptance): 3729, then 3962 with the finding 2 swap, then 3243.** The 3962 swap is a branch change and needs a focused re-run before the draft is final.

## Not checked

- Nothing was built, compiled, formatted, or run. The combined file was checked only by trial merge. Grammar results for the malformed inputs (`*:*)))`, `(*:*`, `*:*^2^3`) were read from code, not run.
- The round 36 report for 3729, the round 32 reviews, and the gate logs named in the receipts are not on disk in this worktree or the main checkout. Receipt numbers are used as recorded.
- Lucene 9.x and 10.x: the drafts name no Lucene behavior, so no check was made. The 3962 score sentence repeats the Jira expectation (every hit scores 1.0) and does not cite Lucene code.
- Live heads were not re-fetched (read-only rule). The origin remote-tracking refs match the claim table. The local `solr-*-submit` branches are stale and were not used.
- Jira text was read from `research/jira-context/` JSON on disk, not from live JIRA.
- The 3243 and 3962 Oct 8 reviews were read as the latest on-disk per-ticket reports. Their round-28 dispositions were not re-read.
- Finding 10 (test count) is not resolved.
