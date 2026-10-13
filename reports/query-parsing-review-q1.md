# Query parsing review, sub-batch q1: parser core and join family

Reviewer: Windows review host (claim `claims/query-parsing-review.md`), 2026-10-13.
Scope: `pr-drafts/query-parsing/` for SOLR-874, 4824, 6014, 8977, 9048, 9149.

Method: each draft was read against its receipt. Each live tip was checked with `git ls-remote` on the fork. Each GitHub citation was read at the SHA it names with `git show`. Merge-bases were checked with `git merge-base`. Lucene and branch claims were checked against upstream release lines and Lucene release sources. Nothing was built, no test was run, and nothing was written to GitHub.

## Summary

| Ticket | Verdict | Edits in this tree |
|---|---|---|
| SOLR-874 | SATISFIED | none |
| SOLR-4824 | SATISFIED | 2 |
| SOLR-6014 | OPEN (gate) | 2 |
| SOLR-8977 | SATISFIED | none |
| SOLR-9048 | SATISFIED | 1 |
| SOLR-9149 | SATISFIED | 1 |

## SOLR-874: SATISFIED

- Head `ac9ab33753751a6fef05b5f100f5c1c5683510b2` equals the live tip and the receipt. Counts match: SolrPluginUtilsTest 10 of 10, DisMaxRequestHandlerTest 4 of 4. Each class has one failing test on base, per the receipt.
- Merge-base `cabedd1d968059215188f4e7563fb303241899ed` confirmed. Base cite `SolrPluginUtils.java` L651-654 matches the draft's description of `stripIllegalOperators`.
- Head cites checked: `SolrPluginUtils.java` L658-663, `DisMaxQParser.java` L195, test lines L99 and L193, changelog L1-8.
- Operator behavior checked against the patterns at head (`SolrPluginUtils.java` L641-649). A trailing NOT is removed, a leading NOT is kept, and the matches are case-sensitive, so lowercase words stay plain terms. The draft says all of this correctly.
- Edits: none.
- Not checked: the Jira statements ("earlier patches on the ticket escaped the words", and the reported queries). They need the Jira packet, which this review did not open.

## SOLR-4824: SATISFIED

- Head `76c777e4661e9240ac66dd01247c0dd1286cacff` equals the live tip and the receipt. TestSolrQueryParser 38 of 38 matches.
- Cites checked: `LuceneQParser.java` L53-56, `SolrQueryParserBase.java` L677-683 (five-argument `FuzzyQuery`), test L87-118, changelog L1.
- The test runs the default check (L104, expects 50) before the 200 check (L105-107). So the draft's statement that the default check passes on base is consistent with the receipt, which records one mismatch (`'75'!='50'`).
- Lucene: `FuzzyQuery.defaultMaxExpansions` is 50 and the three-argument constructor passes 50 with transpositions `true` in lucene-core 9.12.3 and 10.4.0. Both were read from the Lucene release sources on 2026-10-13. The draft's version claim holds for both lines.
- Default for a query without the parameter: `SolrQueryParserBase.java` L143 and L356 show the default comes from `FuzzyQuery.defaultMaxExpansions`.
- Edits:
  - "We can open a follow-up" changed to "I can open a follow-up". ("We" would read as Nick plus the agent, which public text may not do.)
  - Removed "It is expected to fail with a server error instead of a 400 error." That was an unchecked expectation. The sentence "is not checked yet" stays.
- Not checked: the Jira statements about the hit count changing on a large index.

## SOLR-6014: OPEN (gate)

- Head `505849d2d4e7d8699a2fe34d410773b8ecf3a55a` equals the live tip and the receipt. TestExtendedDismaxParser 39 of 39 matches. The base failure (nested check, numFound 0 instead of 2) matches.
- Cites checked: `DisMaxQParser.java` L198-202 (the blank check), test L389-399, changelog L1-8.
- The 2026-10-13 test run recorded in the receipt was not in the draft's Limits. That was the finding the assignment asked to be stated plainly. Verdicts from the receipt, on this head:
  - filter query, stopword-only: fails at search time with `NullPointerException`, with or without `cache`.
  - `cache`, nested or top-level stopword-only: returns the whole corpus (numFound 2), not no documents.
  - `cost` with `AND`, nested stopword-only: fails at search time with `NullPointerException`.
  - Without local parameters, the nested clause returns 0, as the gated proof says.
- Edits:
  - The Change line now says "Without local parameters, a top-level dismax query with only stopwords still matches nothing". Before, the sentence was unqualified, and the `cache` case contradicts it.
  - Limits replaced with the four verdicts above, plus a line that the same run was not made on the merge-base.
- Open, code side. The cases the change does not cover are the same stopword-only `{!dismax}` clause the ticket is about. Two follow-ups are needed:
  1. Whether the `NullPointerException` exists before this change. The base was not run, so the information is missing. Rung one of the ladder applies: get the information first. See gate request G1 in the summary.
  2. The `cache` case returns the whole corpus, not the result of the first clause. Under the decision default (decision D1 in the summary), the harder option applies, so an in-branch fix is requested after G1.
- Not checked: the probe itself is not committed to the branch, so it cannot be re-run from the branch.

## SOLR-8977: SATISFIED

- Head `395b24964fd05f2908c0340ef79c1ee4273775a8` equals the live tip and the receipt. Counts total 38: GraphQueryTest 3, TestScoreJoinQPScore 13 with one skip, TestScoreJoinQPNoScore 4, BJQParserTest 18.
- Merge-base `14c7aac0d151402b00259e2fb9bf5eed7049ec5d` confirmed.
- Cites checked: `QParser.java` L114-115 at the merge-base (`onOrAfter(LUCENE_10_2_0)`), `GraphQuery.java` L227-228 at the merge-base, `GraphQueryParser.java` L45-48 at head, `GraphQueryParser.java` L40 at the merge-base (the root query is not repaired), test L157-188 at head, changelog.
- Round 2 proof matches the receipt: `expected:<2> but was:<1>` is the assertion at test L183.
- Cross-version, the central claim: "The 9.x line and the 10.0 line have no repair in the query parser for any luceneMatchVersion."
  - `upstream/branch_9x` and `upstream/branch_10_0`: `QParser.java` has no `autoFixPureNegative` and no `LUCENE_10_2_0` reference. `upstream/branch_10x` and `upstream/main` have both.
  - The repair in main sits in `SolrQueryParser.java`, which calls `QueryUtils.makeQueryable`. That file is absent from the search directory on both 9.x and 10.0. The claim holds.
- Edits: none.
- Not checked:
  - The draft says the `doGraph` assertion "also passes on the base code". The receipt does not record a base run of that assertion. NOTE for the main side.
  - "The root query has the same gap" is consistent with `GraphQueryParser.java` L40 but was not run.

## SOLR-9048: SATISFIED

- Head `b145018563cbc2ddd29e43a0fa8619545415b8c6` equals the live tip and the receipt. Counts: BJQParserTest 19, BlockJoinNestedVectorsQParserTest 17, TestMmBoolQParserPlugin 10, TestFiltersQueryCaching 2. Total 48, as recorded.
- Base failure matches the receipt: NullPointerException "Query must not be null" in `testSubQueryWithoutAnyTermsAfterAnalysis`.
- Cites checked: `FiltersQParser.java` L62-67 at head, test L169-185 at head, changelog.
- `BlockJoinParentQParser.java` L283-284: the draft cited this at the head. The branch does not change that file (its diff against the merge-base touches only `FiltersQParser.java`, `BJQParserTest.java` and the changelog). The base has the same lines (L283-284). Edit: the citation now links the merge-base and says so.
- Edits: the L283-284 citation moved to the merge-base commit, with the statement that the branch does not change the file.
- Not checked:
  - "An empty `v` takes the same path today." No citation supports this. NOTE for the main side: verify it or remove it.
  - "{!child} returns all children outside the parents." `noClausesQueryLegacy` (base L296) returns the legacy parent filter. The wording is consistent with that. Not run.

## SOLR-9149: SATISFIED

- Head `151dfed119ee0a6f5a922f3a914665e7b3281427` equals the live tip and the receipt. TestSolrQueryParser 37 of 37 matches. The base failure is the one in the receipt (`testNestedQueryModifiers`, no `~2` in the debug query).
- Cites checked: `SolrQueryParserBase.java` L545-547 at head (the `subQParser` reset), test L298-301 at head, `ExtendedDismaxQParser.java` L1067-1074 at the merge-base (the override does not call the base method).
- Edits: "We can open a follow-up for it on request." changed to "I can open a follow-up for it on request."
- Not checked: none beyond the receipt. The receipt notes that a test fix landed at `ffde13713ce` during the gate. The draft does not need to mention it.
