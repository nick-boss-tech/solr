# Query parsing review, sub-batch q3: child fields, MLT, math facets, range cache, grammar

Reviewer: Windows review host (claim `claims/query-parsing-review.md`), 2026-10-13.
Scope: `pr-drafts/query-parsing/` for SOLR-12871, 15615, 16267, 17280, 17311, 17796.

Method is the same as sub-batch q1. In addition, one read-only `gh pr view` was made for PR 1481 (17280). Lucene claims were checked against the 9.12.3 and 10.4.0 release sources where they are not in the receipt.

## Summary

| Ticket | Verdict | Edits in this tree |
|---|---|---|
| SOLR-12871 | SATISFIED | 2 |
| SOLR-15615 | SATISFIED | 2 |
| SOLR-16267 | SATISFIED | 3 |
| SOLR-17280 | SATISFIED | 3 |
| SOLR-17311 | SATISFIED | none |
| SOLR-17796 | OPEN (gate) | 1 |

## SOLR-12871: SATISFIED

- Head `c79a49320cdb0390a9ae7118ab70efbf080826d7` equals the live tip. Receipt: TestNestedDocsSort 12 of 12, one failure on base, gate 2026-10-07.
- Merge-base `9b3a84b1c460981eab09d8ffaef776acc4a184f8` confirmed. Base `SortSpecParsing.java` L112-126 and L157-164 match the draft's description of the generic 400 message.
- Head cites checked: `ChildFieldValueSourceParser.java` L195-203 (the REWRITEABLE check and its message), L204-205 (the log line), test L89-103, changelog L1.
- Edits:
  - Verification date added to the Proof line: 12 of 12 at `c79a49320cd`, verified 2026-10-07. The receipt's round 35 disposition is dated 2026-10-07.
  - Removed "The error metadata now names `SyntaxError` as the root error class. It named `UnsupportedOperationException` before." No test checks that field, so it was an unverified claim. The request line now reads "The request still returns a 400, as before."
- Not checked: the message text sent to the client is unchanged. The test checks the 400 code and the cause chain, not the exact message.

## SOLR-15615: SATISFIED

- Head `be77267bf5549ae6bdc91bc4122b5bf5256f41e1` equals the live tip. Receipt: CloudMLTQParserTest 15 of 15. Base has one failure, `testMLTQParserOnAliasOverMultipleCollections`. The earlier GATE INVALID head is superseded, which the draft does not need to mention.
- Diff against the merge-base `e2cdb2d7e8ae0be4cf6cf0606206c67d89e7278f`: `CloudMLTQParser.java` (+42), its test (+50), and the changelog. "Only the query parser changes" holds for production code.
- Head cites checked: `CloudMLTQParser.java` L118-152 (the lookup, the collection loop, the catch), L122 (the return), test L166-211, changelog L1.
- The receipt's 2026-10-12 fallback trace (vm1) found an error-handling gap in the draft:
  - The fallback catches `SolrServerException` and `IOException` only (L148-150).
  - An authorization refusal (403) from one collection is retried, surfaces as a caught `SolrServerException`, is logged, and is skipped. The request ends with the 400, not a 403.
  - An authentication failure (401) is not caught. It propagates out of `parse()`.
  - The draft said any failed lookup "is logged and skipped". That is too broad.
- Edits:
  - The failed-lookup bullet now says only server and I/O errors are skipped, and that other errors end the request.
  - The identity Limits sentence, which the trace supports, now adds that a refusal is skipped and the request ends with the 400, not a 403.
- Not checked: cost on long aliases (the draft says it is not measured, which is accurate).

## SOLR-16267: SATISFIED

- Head `8f4b0c6d2fb0c89c833512de502f39eb1bec560e` equals the live tip. Receipt: TestJsonFacets 30 of 30, TestFunctionQuery 23 of 23. The receipt says the branch was rebuilt on `14c7aac0d151402b00259e2fb9bf5eed7049ec5d` and force-pushed with a lease. The head reconciliation is recorded there.
- Head cites checked: `ValueSourceParser.java` L1688-1691 and L1743-1746 (the `exists()` overrides), test L2149-2174, changelog L1.
- The `def()` claim ("the same answer reaches `exists()` and `def()`") rests on the receipt's bytecode read of `DefFunction` in lucene-queries 9.12.3 and 10.4.0. Not re-run here.
- Coverage problem, fixed in Limits. The new assertions check `avg`, `countvals`, `missing`, `min` and `max` through facet stats (test L2151-2172). They do not call `def()`, and they do not test `percentile`. The draft's Limits said the new assertions "reach them only through the facet stats", which is wrong for `def()` and `percentile`.
- Edits:
  - Limits heading now says `exists()` is reached only through the facet stats, and `def()` and `percentile` have no assertion.
  - Limits body states what is and is not reached.
  - "We can submit it after this change" changed to "I can open that follow-up after this change."
- Not checked: `missing` as a facet stat name, and the `percentile` behavior the Change line describes. The Change line still lists `percentile` as affected. Limits now says it has no assertion, so the two are consistent.

## SOLR-17280: SATISFIED

- Head `40817c5cb7ec96b6f4119a46bec3db8ae6a1f6de` equals the live tip. Receipt: focused class 5 of 5, and base has one failure, `testNestedRangeQueryNotPutInFilterCache`.
- Head cites checked: `SolrRangeQuery.java` L482-489 (the removed `put`, with the code comment naming SOLR-17280), test `TestFiltering.java` L109-137, changelog L1.
- PR 1481 (read-only `gh pr view`): CLOSED, not merged. Title "SOLR-16707: Avoid "Recursive update" ISE in non-async cache computeIfAbsent()". The draft's "closed without merging" is right, and the SOLR-16707 relation is supported by the title.
- The receipt's design note says the tradeoff (a nested range's DocSet is no longer cached independently) is Limits material. The draft had it in Change and Choice only. Added to Limits.
- Edits:
  - Limits: a nested range query has no cache entry of its own, so each run builds its DocSet again.
  - "we can open a follow-up for it" changed to "I can open a follow-up for it" (in the Limits paragraph).
  - "If the maintainers prefer that route, we can open a follow-up for it." changed to "I can open..."
- Not checked (Jira): the maintainer comment that links this ticket to SOLR-16707, and the ticket discussion the Choice says favors PR 1481. The claim that the exception needs a particular hash bin collision is also not in the receipt. These need the Jira packet.
- Not checked (code): "a range query used directly as a filter is still cached by the searcher, unless it has `cache=false`". The code comment at L484-488 says this. The `cache=false` clause was not run.

## SOLR-17311: SATISFIED

- Head `9f7524582f9de4d6931779bc31811013c414a45f` equals the live tip. Receipt: TestNestedDocsSort 12 of 12, pre-fix proof passed at this head.
- Head cites checked: `ChildFieldValueSourceParser.java` L63-67 (`setTopValue`, inside the `BytesToStringComparator` class), test L107-145 (six parents, with the odd-numbered parents having no child value, as the draft says), changelog L1.
- Lucene claim, checked on 2026-10-13 against the release sources of 9.12.3 and 10.4.0: `TermOrdValComparator.setTopValue` stores a null without a check, and no exception is thrown. The leaf comparator then uses `missingOrd` when the top value is null. The draft's statement, "treats a null top value as the missing value", holds for both lines.
- Edits: none.
- Not checked: "Only string child fields use this comparator." The draft is accurate about the class where `setTopValue` lives, but the numeric path was not traced. NOTE for the main side.

## SOLR-17796: OPEN (gate)

- Head `661165d2673de9211846e276c05daf1fd0e879d6` equals the live tip. Receipt: TestCollapseQParserPlugin 21 of 21, 0 skipped, 0 failures. Base failure: `testCollapseFilterIsNotWrappedWhenRequired` (the `UnsupportedOperationException` from `CollapsingPostFilter.createWeight`).
- The draft quotes the ticket body (where AND fails), not the title, which the receipt says names OR. That matches the receipt's note.
- Merge-base `56ec140e3636d5f4150fa87fbf7103529536ac99` confirmed. `CollapsingQParserPlugin.java` L269 is the `CollapsingPostFilter` declaration the draft cites.
- Head cites checked: `QueryParser.jj` L232-245 (the lone `PostFilter` return), `QueryParser.java` L247-260 (the generated file, which has the same branch in JavaCC's return form), tests L287-305 and L307-328, changelog L1-7.
- Grammar check: the checked-in generated `QueryParser.java` matches `QueryParser.jj` for this production. The receipt records that the file was regenerated from the grammar.
- Limits, fixed: the draft said a negated collapse "may still" throw. The receipt records a check on this head that showed it does. The Limits now states that plainly, and says the check is not committed to the branch.
- Edits: the negated-collapse Limits bullet.
- Branch items, not draft items (see the summary):
  - The changelog title on the branch says a filter query "made only of a collapse or other post filter ... no longer fails with an UnsupportedOperationException when q.op is AND". A negated collapse is also a filter made only of a post filter, and it still fails. The title overstates the fix. Gate request G3 carries replacement wording.
  - Decision D2: the negated case is a scope question. The decision default takes the harder option, so an in-branch fix for the negated case is requested, with a gate. G3 carries it.
