# Query parsing review, sub-batch q2: point fields, join method, grammar and wildcards

Reviewer: Windows review host (claim `claims/query-parsing-review.md`), 2026-10-13.
Scope: `pr-drafts/query-parsing/` for SOLR-10897, 11391, 11761, 12212, 12532, 12608.

Method is the same as sub-batch q1: receipts, live tips, citations read at their SHAs, merge-bases checked, Lucene claims checked against release sources. No builds, tests or GitHub writes.

## Summary

| Ticket | Verdict | Edits in this tree |
|---|---|---|
| SOLR-10897 | SATISFIED | none |
| SOLR-11391 | SATISFIED | 2 |
| SOLR-11761 | SATISFIED | none |
| SOLR-12212 | SATISFIED | 1 |
| SOLR-12532 | SATISFIED | 2 |
| SOLR-12608 | OPEN (gate) | 2 |

## SOLR-10897: SATISFIED

- Head `d9240d0750faf55d21fb125668ea66db0142be79` equals the live tip and the receipt. 20 focused tests, with TestSimpleQParserPlugin 18 of 18. Base has one failure, `testPointFieldQuery` (`'1'!='0'`). The receipt says the same.
- Merge-base `c3cdf7b46e8cfff3673f76d881f32cf8e7b00622` confirmed. The branch does not change `PointField.java`, so the draft's citation of `PointField.java` L241-247 at the merge-base is correct.
- Cites checked: `SimpleQParserPlugin.java` L184-217, test L580-596, schema L53-56, changelog L1-8.
- Code behavior checked against the cited lines:
  - Only `BAD_REQUEST` is swallowed (L197-203). Other errors are rethrown (L198-199).
  - Boosts apply to both the point path and the text path (L207-212).
  - The mixed `qf` case is in the test at L592-594.
- Edits: none.
- Not checked: other point types (the draft says only `IntPointField` is tested).

## SOLR-11391: SATISFIED

- Head `b0f15a22856a887d4e369cd1542c0ce09a687059` equals the live tip. The receipt is GATE GREEN at that head (gate finished 2026-10-09). TestScoreJoinQPNoScore 5 of 5 matches. Base has one failure (`testUnknownJoinMethodIsBadRequest`, 500 where 400 is expected).
- Record disagreement: the round 1 report and the claim said NO GATE at this branch. The receipt on this tree now records a gate at the live tip. This review treats the receipt as current. The main side should confirm the record.
- Merge-base `cabedd1d968059215188f4e7563fb303241899ed` confirmed. Base `JoinQParserPlugin.java` L221 has `Method.valueOf(...)`, the unhandled call the draft describes.
- Head cites checked: L220 (`parseMethodString`), helper L268-274 (`BAD_REQUEST`, message `"Provided join method '<name>' not supported"`), L255 (the other entry point, which also uses the helper, so the draft's statement holds), test L61-68, changelog L1-8.
- Edits:
  - The Jira link line had a placeholder, `SOLR-<key to be chosen>`. It now reads `SOLR-11391`.
  - "it does not change join speed on non-point fields" was not measured. Replaced with "No timing was measured."
- Not checked: the Jira text.

## SOLR-11761: SATISFIED

- Head `41893ee9ce6101224f46e0287a1f2f0bfe6a6974` equals the live tip. Counts match the receipt: 38 + 39 + 7 + 5 = 89.
- Cites checked:
  - base `LuceneQParser.java` L42 (a new `SolrQueryParser` per query, which is why the normal request path never reuses a parser)
  - base `QueryParserTokenManager.java` L25 (`commentNestingDepth`) and L1582-1592 (`ReInit(CharStream)` does not reset it)
  - head `SolrQueryParserBase.java` L238-245 (the new `ReInit(QueryParserTokenManager)`)
  - head test L545-559 (`testCommentsAfterSyntaxError`)
  - changelog
- Claim "No code under solr/core makes that call outside the parser itself": grep at head. `ReInit(CharStream)` callers are the generated parser, the token manager's own overloads, and two test stubs. The new `ReInit(QueryParserTokenManager)` is called only from `SolrQueryParserBase.java` L283. The claim holds.
- Edits: none.
- NOTE for the main side: the receipt does not name the test that discriminated. The draft names `testCommentsAfterSyntaxError`, and that test is the one at head L545-559. The receipt's own note says the shipped test's setup was fixed in the hygiene commit, so the name should be confirmed against the gate log.

## SOLR-12212: SATISFIED

- Head `876953fdc92714a1692615bb67685b22c3814483` equals the live tip. The receipt records 80 focused tests (TestSolrQueryParser 39, TestExtendedDismaxParser 39, TestStandardQParsers 2), with no failures, errors or skips.
- The draft's Proof had no run counts or verification date, which the formula requires. Edit: counts and date added (verified 2026-10-05, head `876953fdc92`).
- Grammar pair check (12212 and 17796 both change the `Query` production):
  - `QueryParser.jj` L237-244 at head. The production returns a lone pure-negative required clause unchanged.
  - Generated `QueryParser.java` L252-259 at head. The same logic, in JavaCC's generated return form. The generated file matches the grammar for this production.
  - The landing order for the pair is left to the category summary. Both change the same production, so the order matters.
- Cites checked: base `QParser.java` L113-118 (the `luceneMatchVersion` check), tests L571-584 and L586-616, base `SolrQueryParser.java` L32-35, changelog L1-9.
- Boundary claim: values below 10.2 include all of 9.x, 10.0 and 10.1. Consistent with the `onOrAfter(LUCENE_10_2_0)` check at base `QParser.java` L115. The 9.x and 10.0 release lines have no autoFix (checked in sub-batch q1).
- Not checked: the ticket's own releases (6.6.2, 7.3, 8.0). The draft says they were not run.

## SOLR-12532: SATISFIED

- Head `69c06da446797cec5c3c8dd885e19a28381789d3` equals the live tip. Receipt: TestSolrQueryParser 38 of 38, no skips, and the base failure `'1'!='0'` on `testQueryStringSlopOnGraphPhrase`. Gate pushed 2026-10-04, matching the draft's verification date.
- Merge-base `14c7aac0d151402b00259e2fb9bf5eed7049ec5d` confirmed.
- Base behavior checked: base `SolrQueryParserBase.java` L552-566 sets slop only on a top-level `PhraseQuery` and `MultiPhraseQuery` (under `subQParser == null`). The draft says so correctly.
- Head cites checked: `SolrQueryParserBase.java` L563-586 (`applySlop`, including the `BooleanQuery` walk at L577-583), test L718-732, changelog L1-9. Base `ExtendedDismaxQParser.java` L1067-1074 (eDismax has its own slop path).
- Lucene, checked against the release sources of 9.12.3 and 10.4.0 on 2026-10-13: `QueryBuilder.analyzeGraphPhrase` builds a `BooleanQuery` with one SHOULD clause per path. Multi-token paths become phrase queries. A single-token path stays a `TermQuery`, and a synonym position becomes a `SynonymQuery`. The draft said "BooleanQuery of phrase queries" and "checked in both class files". Neither is quite right. No class files were read.
- Edits:
  - The Lucene sentence now states the per-path clause, the single-token case, and that the check was made in the release source.
  - "We can open a follow-up" changed to "I can open a follow-up".
- Not checked: other token graph sources, such as multi-word synonyms. The draft already says they may take the same path and were not tested.

## SOLR-12608: OPEN (gate)

- Head `d1dd8a1f9f0aa17c6e34f8c5e40cb9768a83aea2` equals the live tip. Receipt: 86 focused tests, none failed, TestSolrQueryParser 38 of 38. The receipt also records that the gated head is one commit past the received fork head, and that the extra commit removes a handoff file only.
- Cites checked: `SolrQueryParserBase.java` L1271 (`collapseRepeatedWildcards`), L1305 (the call inside `getWildcardQuery`; the header is not cited), test L276-291, changelog L1-9.
- Premise: the receipt says a scratch copy of the test, with the four helper checks removed, discriminated on base with the ticket's symptom. The draft's Proof did not say so.
  - Edit: one sentence added to Proof. A scratch copy with the four helper checks removed failed on base in the way the ticket describes. It is not committed to the branch.
  - The observed failure line is in the premise log (`g12608-premise.log`), not in the receipt. The main side should quote it. NOTE.
- Limits: the bullet "The changelog entry must not claim a memory benefit until one is shown." is an internal instruction in public text. Replaced with "This change does not claim a memory benefit."
- Branch item, not a draft item: the changelog title on the branch says the change "avoids the excessive memory use seen with edismax queries". That claims a memory benefit the review cannot find evidence for. The draft's Limits now says the change does not claim one, so the title and the draft disagree. Decision D3 in the summary: correct the title. Gate request G2 carries the replacement wording.
- Not checked: the ticket's report (Solr 7.2.1 and 7.4, faceting) is in Jira.
