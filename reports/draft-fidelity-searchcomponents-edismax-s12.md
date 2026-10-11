# Search components and eDisMax draft fidelity, slice s12 (claim C12)

Assignment: `assignments/pool-draft-fidelity-searchcomponents-edismax.md`. Claim: `claims/pool-draft-fidelity-searchcomponents-edismax.md`, slice C12. Read-only review. This report is the only file written. No commit, push, comment, build, or test.

Slice drafts: `pr-drafts/edismax/SOLR-3729.md`, `SOLR-3962.md`, `SOLR-4362.md`, `SOLR-6009.md`, `SOLR-6320.md`, `SOLR-7120.md`.

Inputs: receipts `receipts/SOLR-<n>.md` (all six present). Category round reports `reports/edismax-round-3.md` and `reports/edismax-round-3-e1.md` to `-e5.md`. `material/` grep for the six ticket numbers and for "edismax" returned no hits, and no edismax answers file exists there. Code was read with `git show <sha>:<path>` and `git grep` at each head and base. The worktree HEAD is d627304e96b.

Local upstream: the worktree's `upstream/main` resolves to `3f5d4c5bf8ac`, not the `8e62c2686882` that round 3 recorded. Merge-bases below were computed against the local ref.

## Verdicts

| Draft | Head checked (`git ls-remote origin refs/heads/solr-<n>-submit`) | Verdict |
|---|---|---|
| SOLR-3729 | `0fe7e503945c1a169f76a019392d68bdf4261eb7` (match) | CONSISTENT |
| SOLR-3962 | `e7d5f350503516dc5a01211cb28917835af64a0b` (match) | DRIFT (1 item) |
| SOLR-4362 | `e96a057439c879427bf07195fe6e18bed08a2c42` (match) | DRIFT (3 items) |
| SOLR-6009 | `a41bb034a1f47c08a8f8ea6ebbe46e6d443cbb6f` (match) | DRIFT (2 items) |
| SOLR-6320 | `cb710c963531c4f32166e6e0b28c45e54b164a3f` (match) | DRIFT (1 item) |
| SOLR-7120 | `4664014a919cc30448b784a5d8998c6536f69d55` (match) | DRIFT (1 item) |

Checked for all six: each Proof count matches its receipt. Each named test method exists at the head. Each verification date is present (3729 2026-10-08, 3962 2026-10-07, 4362 2026-10-07, 6009 2026-10-07, 6320 2026-10-08, 7120 2026-10-09). Each changelog fragment exists at the named head. No em dash appears in any of the six drafts. No internal process vocabulary appears in public text, except the drafter-status block in SOLR-4362. The drafts have no title line, so the changelog title was compared with the draft summary instead.

---

## SOLR-3729

Verdict: CONSISTENT.

Checked:
- Proof: 41 of 41 at `0fe7e503945`, verified 2026-10-08 (receipt line 6, gate round 36 on 2026-10-08). Base `97d973814336` fails exactly `testFocusQueryParser` and `testMatchAllColonEscaping` (receipt line 7). `testMatchAllBoostGluedFieldClauseRespectsUserFields` passes on base, as the draft says. Test names at head: L164, L218, L363. Lines 210 hold the `*:*^abc` and `*:*)))` loop.
- Citations: `isStandaloneMatchAll` is at `ExtendedDismaxQParser.java` L737-L798 (javadoc 737-742, method 743-798). Its call is at L940-L943. Both are at `0fe7e503945`. The base escape is at `97d973814336` L878-L879 (`if (!"*:*".equals(clause.raw))`), which matches the draft's "every clause except exactly `*:*`" claim.
- The helper accepts any run of `(`, `+`, `-`, then `*:*`, then `)` characters and one `^` boost of digits with at most one dot. It rejects `foo(*:*)bar` and `(*:*)foo`, as the draft says. The splitter ends a token at whitespace, so `(foo *:*)` gives a standalone `*:*)` clause, as the draft says.
- Changelog: `changelog/unreleased/SOLR-3729-edismax-parenthesized-match-all.yml` exists at `0fe7e503945`, `type: fixed`. Its title matches the draft's summary.
- Choice: the owner call on record (receipt line 9; round 3 e2 "Recorded"). The narrow alternative (only `(*:*)`) is a real route a maintainer could pick. Pass.
- Limits: match round 3 e2. The pf gap is named and points to SOLR-3962.

Optional notes, not blocking:
- Round 3 e2 findings 6 and 7 are branch-side comment and javadoc fixes (javadoc wording at L737-L742; wrong test comment at L207-L209 for `*:*^abc`). They do not change behavior. If they land, the head moves and the Proof must name the new head.
- The draft has no title line. The PR title will come from the changelog title, which is consistent.
- Plain language: "splitter", "grammar", and "pf" are compressed. "the query splitter" and "the query grammar" read more easily.

---

## SOLR-3962

Verdict: DRIFT (1 item).

1. Draft says: "The check accepts a leading `+` or `-`, grouping parentheses, and a plain-number boost, in any combination"
   - Evidence: `ExtendedDismaxQParser.java` at `e7d5f350503` L364-L393 (`isMatchAllDocsClause`). The method strips one leading sign only (L365-L368). It then loops over a trailing boost and outer parentheses (L374-L391). So `(+*:*)` reduces to `+*:*` and returns false. The same happens to `(-*:*)`, `-(+*:*)`, `+(-*:*)`, and `+-*:*`. Round 3 part e2 finding 2 reports the same gap. It is closed only when the helper is swapped to `isStandaloneMatchAll` after SOLR-3729 lands.
   - Replacement: "The check accepts one leading `+` or `-`, then grouping parentheses and a plain-number boost around the match all query, in any order. A sign inside the parentheses, such as `(+*:*)`, is not accepted yet."

Optional notes, not blocking:
- Round 3 e2 finding 2 is a branch FIX. Swap to `isStandaloneMatchAll` after SOLR-3729 lands, and add `(+*:*)` and `(-*:*)` to the spelling list at L189. Then re-issue this draft at the new head. If the gap is still open after the swap, name it in Limits as well.
- Round 3 e2 finding 4 (`*:*^2^3`) and finding 8 (the early return at L319-L320 is not proven neutral) are not claimed in the draft. Keep it that way.
- The typed `*\:*` case is named in Limits, as round 3 e2 finding 3 requires. `[* TO *]` is named too.
- Citations checked: call site L309-L310, early return L319-L320, helpers L358-L427, all at `e7d5f350503`. The score test field `text_chars` is at `solr/core/src/test-files/solr/collection1/conf/schema12.xml` L506-L511 at the same head. The four test names are at L164, L184, L210, L233.
- Changelog `SOLR-3962-edismax-pf-match-all.yml` exists at the head, and its title matches the draft's summary.

---

## SOLR-4362

Verdict: DRIFT (3 items).

1. Draft says: "A decimal slop such as `~2.5` counts too. So does a boost on either side of the slop, such as `~10^2` or `^2~10`."
   - Evidence: `ExtendedDismaxQParser.java` at `e96a057439c` L296-L331 (`isSlopClause`). Line 299 requires the clause value to start with `\~`. The splitter writes `^2~10` as `\^2\~10`, so that form returns false. The digit loop (L303-L305) stops at the `.` in `\~2.5`. Line 314 then needs `\^` after the digits, so `\~2.5` also returns false. The skip at L350 never fires for these forms, so the slop number stays in a pf2 shingle. Round 3 part e1 finding 1 reached the same result. The draft's test, `testPf2DoesNotUseSlopOfPhraseAsTerm` (L261), covers integer slop only.
   - Replacement: "A clause of the form `~N` directly after a phrase is treated as the phrase's slop, such as `~10`. A boost after the slop, such as `~10^2`, counts too ([slop check](https://github.com/nick-boss-tech/solr/blob/e96a057439c879427bf07195fe6e18bed08a2c42/solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java#L296-L331))."

2. Draft says: "**A bare word with a suffix next to a phrase is not covered.**" (the Limits summary). The Limits section omits the two forms in item 1.
   - Evidence: the same code as item 1. Limits must name every form the change does not cover (pr-formula.md, section 5). Round 3 e1 finding 1 gives the branch fix. If that fix lands first, the What text of item 1 is correct as drafted, and this bullet is removed. Re-run the Proof at the new head in that case.
   - Replacement: add this bullet at the top of the Limits list: "- A decimal slop, such as `~2.5`, and a boost before the slop, such as `^2~10`, are not covered by this change. The slop number can still reach a pf2 shingle for those forms. If maintainers want them covered, we will open a follow-up ticket and PR."

3. Draft says: "**Drafter status, not PR text. Delete this block before filing.** HELD. Do not file yet." (draft lines 39-46, after the AI assistance footer)
   - Evidence: this block is internal process text in the draft file. It uses the words HELD, "Finding 1", "Finding 2", "Finding 3", and "Squash". The block says to delete it before filing, so it must not reach the PR.
   - Replacement: delete from the horizontal rule on draft line 39 to the end of the file. No replacement text.

Optional notes, not blocking:
- Round 3 e1 finding 3 is branch-side, not draft text. Commits `52abce333db` ("Hypothetical, unrun regression test; see SOLR-4362-TESTING.md."), `6631400a9f4` ("SOLR-4362: add hypothetical-reproduction handoff doc"), and `f0f3d1f004a` show internal words in the PR commit list. Squashing rewrites the fork branch, so it needs Nick's direction first.
- Round 3 e1 finding 7 is branch-side: the test comment at `TestExtendedDismaxParser.java` L275 says "tokenises". Use "tokenizes".
- Round 3 e1 finding 2 (a boost-only clause `"phrase query"^2 term`): the draft keeps the second Limits bullet. That matches the round.
- Plain language: "shingle" and "slop" are not explained in the draft. A short gloss would help.

---

## SOLR-6009

Verdict: DRIFT (2 items).

1. Draft says: "Changelog: [changelog/unreleased/SOLR-6009-edismax-regexp.yml](https://github.com/nick-boss-tech/solr/blob/a41bb034a1f47c08a8f8ea6ebbe46e6d443cbb6f/changelog/unreleased/SOLR-6009-edismax-regexp.yml)". The link resolves, but the fragment's title does not match the evidence.
   - Evidence: the fragment at `a41bb034` lines 4-5 say "regular expression clause previously matched nothing (or errored) in this configuration; it now matches documents the same way a fielded regular expression clause does." Nothing in the record shows an error case. `research/jira-context/SOLR-6009.json` line 4 says "numFound=0 for both of these". Round 3 e3 finding 3 reached the same result. Brief check 4 requires the title to match the code and evidence.
   - Replacement (a branch edit to the fragment, not to the draft). Replace lines 4-5 of the fragment with these two lines, keeping line 3:
     ```
       regular expression clause previously matched no documents; it now matches the same way
       a fielded regular expression clause does.
     ```
     The head moves after this edit, so the draft's head, Proof and links must be re-issued at the new head.

2. Draft says: "Edismax has no regular expression override, so the clause goes straight to [`SolrQueryParserBase.getRegexpQuery`](https://github.com/nick-boss-tech/solr/blob/a41bb034a1f47c08a8f8ea6ebbe46e6d443cbb6f/solr/core/src/java/org/apache/solr/parser/SolrQueryParserBase.java#L1333) with the placeholder field."
   - Evidence: this is pre-change symptom code. This branch does not change `SolrQueryParserBase.java` (`git diff --stat 14c7aac0d151 a41bb034` lists only the changelog, the parser, and the test). Pr-formula.md says symptom links use the merge-base and the text says so. The merge-base of `a41bb034` with the local `upstream/main` is `14c7aac0d151402b00259e2fb9bf5eed7049ec5d`. Line 1333 is `protected Query getRegexpQuery(String field, String termStr)` at both SHAs.
   - Replacement: "Edismax has no regular expression override, so the clause goes straight to [`SolrQueryParserBase.getRegexpQuery`](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/parser/SolrQueryParserBase.java#L1333), the merge-base commit before this change, with the placeholder field."

Verified: the What-section links to L962, L1162-L1169, and L1487-L1488 are at `a41bb034` and match the changed code. The Limits claim about the catch at L1496-L1500 matches. The Proof counts match receipt line 6. The base `14c7aac0d151` failures are the two regex methods at L261 and L312. The Choice section is correctly absent, since round 3 records no owner decision for this ticket.

Optional notes, not blocking:
- The placeholder is three U+FFFC characters: `IMPOSSIBLE_FIELD_NAME = "￼￼￼"` at `ExtendedDismaxQParser.java` L72 (`a41bb034`). The draft's "the placeholder character U+FFFC" could read "three U+FFFC characters".
- Branch-side, not draft text: commits `8b17fca972a` ("SOLR-6009: add hypothetical-reproduction handoff doc") and `b2976bdc7dc` ("... remove the hypothetical-reproduction handoff doc before submission") show internal words in the PR commit list. Round 3 e3 finding 4 says to squash them away. That moves the head.
- Plain language: "U+FFFC" and "dispatch case" are compressed. Optional.

---

## SOLR-6320

Verdict: DRIFT (1 item).

1. Draft says: "Edismax then uses the escaped form, which quotes the explicit AND and searches it as a term ([escaping](https://github.com/nick-boss-tech/solr/blob/cb710c963531c4f32166e6e0b28c45e54b164a3f/solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java#L462-L495))."
   - Evidence: `escapeUserQuery` (L462-L495) is pre-change code. The branch's parser hunks start at base line 499 (`git diff -U0 97d973814336 cb710c96`), so L462-L495 are unchanged from `97d973814336`. Under pr-formula.md the symptom link goes to the merge-base, and the text says so. The links in "What this change does" (`isPromotedOperatorWord` L524-L529, `rebuildUserQuery` L546, `foundOperators` L509-L511) cover changed code, so they keep the head SHA.
   - Replacement: "Edismax then uses the escaped form, which quotes the explicit AND and searches it as a term ([escaping](https://github.com/nick-boss-tech/solr/blob/97d973814336101e12475558d7419321c743de79/solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java#L462-L495), the code before this change)."

Verified: the head is `cb710c96353`. The Proof is 44 of 44, verified 2026-10-08 (receipt lines 4 and 6). Base `97d973814336` fails exactly the three named tests (receipt line 7; test names at L270, L310, L404). The Choice matches the code. `isExplicitOperator` (L559-L564) counts only exact "AND", "OR", "NOT". In `Zapp And or Brannigan`, neither "And" nor "or" has an exact operator neighbor, so both promote and the query falls back, as the draft says. The owner call is recorded in receipt line 8 and round 3 e3. The Limits match round 3 e3 finding 1 and the test shapes. The 2014 comment is in `research/jira-context/SOLR-6320.json`, created 2014-08-07, and it expects `x AND and AND y` to fail. The changelog title matches the draft's summary.

Optional notes, not blocking:
- Round 3 e3 findings 1 and 2 are branch-side test-comment fixes (javadoc at `TestExtendedDismaxParser.java` L395-L402; TODO at L341-L342). The draft's Limits sentence already matches the corrected javadoc. Applying the fixes moves the head.
- Branch-side: commit `cb710c963531` ("... record mm alignment re-check") uses "re-check" (round 3 e3 finding 5).
- Not verified: the ticket claim that the long `AND and AND` query "then searches the default field". The query text at line 4 of the Jira packet matches.
- Plain language: "promote", "demoted", and "neighbor" are compressed. A plain gloss, such as "a lowercase word that the rule turns into an operator", would help.

---

## SOLR-7120

Verdict: DRIFT (1 item).

1. Draft says: "Before this change, the code wrapped that error in a plain `RuntimeException` on [line 1733](https://github.com/nick-boss-tech/solr/blob/4664014a919cc30448b784a5d8998c6536f69d55/solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java#L1733)."
   - Evidence: at `4664014a` L1733 is the new `throw new SolrException(SolrException.ErrorCode.BAD_REQUEST, e.getMessage(), e);`, so this link shows the post-change code. The pre-change line is `throw new RuntimeException(e);`, at `cabedd1d968059215188f4e7563fb303241899ed` L1733. That is the merge-base of `4664014a` with the local `upstream/main`. Pr-formula.md requires the symptom link to use the merge-base, with the text saying so. The "What this change does" link to L1733 at the head is correct and stays.
   - Replacement: "Before this change, the code wrapped that error in a plain `RuntimeException` on [line 1733](https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java#L1733), the merge-base commit. The error text survived only as part of the wrapper's message."

Verified: the head is `4664014a919`. The Proof is 40 of 40, verified 2026-10-09 (receipt lines 6-7). Base with the branch test fails exactly `testNeitherQfNorDfIsBadRequest`, which is at L994-L1002. The test checks BAD_REQUEST and the message. `DisMaxQParser.java` L88 at the head calls `parseQueryFields` in `parse()`, as the Limits say. Edismax builds its configuration in the parser constructor (L114, then `createConfiguration` at L358-L360), so the claim that `{!edismax}` local parameters are covered holds. The handoff note `SOLR-7120-TESTING.md` is absent at `4664014a` (`git cat-file -e` fails), so round 3 e5 finding 5 is closed at this head. The changelog fragment exists and its title matches the draft's summary. No Choice section is needed. Round 3 e5 records no owner decision for this ticket.

Optional notes, not blocking:
- Round 3 e5 said the premise run was still owed. The receipt now records the base result (one failure, the new test), so the Proof is supported as written.
- Plain language: "SyntaxError" and "RuntimeException" are Solr and Java names. Solr developers will read them fine.

---

## Not done

- Live JIRA was not read. Jira claims were spot-checked against the local packets in `research/jira-context/` (6320 comment and date, 6009 numFound=0, 3962 scores and debug string, 3729 report string, 4362 2018 comment). Other report quotations were not checked.
- No build, Gradle run, test, test-queue command, gate run, fetch, or `gh` call. Head checks used `git ls-remote` only.
- The gate logs and the round 36 goal files named in the receipts are not on disk. Receipt counts were taken as stated. Test names were confirmed at each head.
- Lucene behavior was not checked. None of the six drafts names a Lucene version.
- Test bodies were checked only for names, the cited strings, and the 4362 slop cases. The 3962 spelling list at L189 and the 3729 test bodies were not read in full.
