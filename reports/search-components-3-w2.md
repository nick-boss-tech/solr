# Search components round 1, sub-batch 3, part w2 (SOLR-9396, SOLR-14678)

Result: SOLR-9396 is draftable after two FIXes; SOLR-14678 is draftable but held (the /get gap, a fail-before claim the receipt cannot support, and an owner choice).

Heads: SOLR-9396 `a5ab2eda4e6a9bcad5668653e80c0d0b510e6388`, base `c3cdf7b46e8`. SOLR-14678 `5d94e6cf3981bd8a141a2063992148893e6d1bbb`, merge base with `upstream/main` `b5c71bc5573`. Both match the claim table and a live `git ls-remote` on this run. Drafts: `pr-drafts/search-components/SOLR-9396.md` and `pr-drafts/search-components/SOLR-14678.md`. Nothing was committed, posted, built, or tested.

## Findings

1. **FIX (SOLR-14678, public scope claim): the /get path does not get the fix.**
   - Files: `solr/core/src/java/org/apache/solr/handler/component/RealTimeGetComponent.java` L366-L368 and L375; `solr/core/src/java/org/apache/solr/response/JSONWriter.java` L97; `XMLWriter.java` L182; `CSVResponseWriter.java` L278; `solr/core/src/java/org/apache/solr/response/transform/ChildDocTransformer.java` L153; `solr/core/src/java/org/apache/solr/search/SolrReturnFields.java` L558.
   - Evidence (head 5d94e6cf398, by reading, not run): /get loops over the ids, calls `transformer.transform(doc, ...)` at L366, and adds the document at L368. The list is written only after the loop (L375, `addDocListToResponse` L1173-L1186, `rsp.addResponse` L1185). `transform` clears the name set on every call (ChildDocTransformer L153), so at write time the set holds only the last document's names. The writers call `wantsField`, which reads that set. DocsStreamer (L117-L125) transforms one document and returns it, so search responses are fine. The loss needs two or more documents in one /get request, where an earlier document has nested children. No test in TestChildDocTransformerHierarchy sends a /get request (grep finds no `/get` path, no `ids` parameter, and no RealTime use; the one `ids` match is a comment).
   - Replacement: the draft's Limits bullet, already written. Owner decision 1 is whether to fix it here instead.

2. **FIX (SOLR-14678, Proof): the recorded premise run cannot be the base code.**
   - Evidence: the receipt says the premise leg ran 18 tests with exactly 1 failure, `testExtraResponseFieldsDoNotLeakAcrossDocuments`, and does not name the production tree. At base `b5c71bc5573`, `SolrReturnFields.wantsField` (L554-L565) returns false for any name that is neither in `fl` nor matched by a glob. So on base, `testNestedChildrenRetainedWhenFlOmitsNestPathsJSON` (head L155-L198, `fl=id,[child]`) and its XML variant (L200-L218) should also fail on the `toppings` and `lonely` paths. A single failure fits the earlier request-wide variant (before `e639b19e933`) with the head tests. That is an inference; nothing was run.
   - Replacement: the draft's Proof gives only "18 of 18 at head". Do not write "fails without the fix" for 14678 until the owner confirms the tree. If the owner confirms the earlier request-wide variant, add: "On the earlier variant that kept one set for the whole response, testExtraResponseFieldsDoNotLeakAcrossDocuments fails; it passes at this head."

3. **FIX (SOLR-14678, changelog title overclaims).** `changelog/unreleased/SOLR-14678.yml` L1. Current: "The [child] doc transformer now keeps nested child documents in the response when the field list omits the nest-path fields, at any nesting depth". Evidence: finding 1 (/get is not covered), and the tests cover two levels below the root (`toppings/ingredients` and `lonely/lonelyGrandChild`, head L155-L198), not "any depth".
   - Replacement: "The [child] doc transformer now keeps nested child documents in search results when the field list omits the nest-path fields, including children two levels below the root."

4. **FIX (SOLR-9396, commit history carries internal wording).** Commits `f0e03ca25da` ("SOLR-9396: add hypothetical-reproduction handoff doc") and `a5ab2eda4e6` ("SOLR-9396: remove hypothetical-reproduction handoff doc"). The net diff is clean: `git diff --stat c3cdf7b46e8 origin/solr-9396-submit` shows 3 files, 67 insertions, and no handoff file. A PR shows its commit list, so both subjects would be public.
   - Replacement: squash the four commits into one before any PR, with subject "SOLR-9396: subquery transformer requests the fields it references as row params". Owner decision 3. The branch was not edited in this round.

5. **FIX (SOLR-9396, regex branch that reads nothing).** `solr/core/src/java/org/apache/solr/response/transform/SubQueryAugmenterFactory.java` L307: `Pattern.compile("\\$\\{?row\\.([\\w.\\-]*\\w)")`.
   - Evidence (base `solr/core/src/java/org/apache/solr/search/QueryParsing.java`, read by code, not run): L117-L122 treat `$` as a dereference only at the start of a local-parameter value. L144-L148 then look up the rest of the unquoted value as a parameter name. For `${row.x}` the name is `{row.x`, which is never a row parameter. So the `\{?` branch requests a field that no subquery reads. The pattern also matches a `$row.` that appears outside local parameters, which this code does not dereference. Both cases cost one extra read and nothing else. The receipt's Limits line names `${row.` references, so that wording must not reach the draft (the draft does not use it).
   - Replacement: `Pattern.compile("\\$row\\.([\\w.\\-]*\\w)")`. This is a code change that needs a new focused run. This review ran nothing. Owner decision 4.

6. **NOTE (SOLR-9396, null elements).** `SubQueryAugmenterFactory.java` L343-L344 loop over the parameter values and call `ROW_REFERENCE.matcher(value)` with no null check. A null element would throw while the transformer is built. Not reproduced. Likelihood from HTTP input looks low.
   - Optional replacement: `if (value == null) continue;` as the first line of the loop at L343.

7. **NOTE (SOLR-9396, /get path).** The row fields come from the parse path in `SolrReturnFields.add` (base L376-L383), which runs for every request, /get included (`RealTimeGetComponent.java` L112-L113). No test covers /get, so the draft's Limits names it. The two-argument constructor (base `SolrReturnFields.java` L147-L153) reads only the top-level transformer, but `RealTimeGetComponent.java` L855-L870 passes only the child transformer there, so no subquery is lost. Replacement: none; the Limits bullet is in the draft.

8. **NOTE (interaction: SOLR-9396 and SOLR-14678).** No file overlap. 9396 touches `SubQueryAugmenterFactory.java`, `TestSubQueryTransformer.java`, and its changelog. 14678 touches `ChildDocTransformer.java`, `DocTransformer.java`, `DocTransformers.java`, `SolrReturnFields.java`, `TestChildDocTransformerHierarchy.java`, and its changelog. 9396's assumptions still hold at 14678's head:
   - (a) `getExtraRequestFields()` is unchanged. Base `DocTransformer.java` L115 is the only definition on that path; 14678 adds only `getExtraResponseFields()` (DocTransformer.java L138, DocTransformers.java L59).
   - (b) The parse-time collection in `SolrReturnFields.add` (base L376-L383) is unchanged. 14678 changes only `wantsField` (L554-L560) and adds `isTransformerAddedField` (L574-L588).
   - (c) The subquery reports no response names, so its helper field stays out of responses. `SubQueryAugmenterFactory` does not override `getExtraResponseFields`, and the base returns null.
   - The 14678 javadoc (DocTransformer.java L129-L134) describes the same request-versus-response split that 9396's test depends on.
   - Landing order: either order. There is no textual conflict. The gap is testing: 9396's focused run includes `TestChildDocTransformer` (2 tests) at 9396's head, which has no 14678 change, and no receipt runs `TestChildDocTransformer` at 14678's head. Owner decision 5.

9. **NOTE (SOLR-14678, aggregation untested).** `DocTransformers.java` L58-L67 collects names from several transformers. Every `"fl"` value in TestChildDocTransformerHierarchy was checked, and none combines two transformers. Replacement: the draft's Limits bullet.

10. **NOTE (SOLR-14678, javadoc contract).** `DocTransformer.java` L129-L131 says callers "should consult this method when writing each document rather than caching it once per request". The /get path in finding 1 breaks that for multi-document responses. If the per-document scope stays, add: "A caller that transforms several documents before writing any of them sees only the last document's names." Owner decision 2.

11. **NOTE (SOLR-14678, round-28 review).** `research/branch-reviews/round-28/SOLR-14678-review.md` rated `6e91b843fcf` "Needs work" for the request-wide name set (its P1). `e639b19e933` scopes the set. No review file exists for the current head; this report is the review for `5d94e6cf398`.

12. **NOTE (SOLR-14678, tidy commit).** `5d94e6cf398` changes only the `DocTransformer.java` javadoc (4 lines, per `git show --stat`). It is formatting only, as the receipt says. No replacement.

13. **NOTE (dates in the drafts).** The drafts say "verified" with dates taken from the record, not from a run log. 9396 uses 2026-10-05 (the takeover-log entry and the branch commit dates). 14678 uses 2026-10-07 (the round 35 entry and the head commit date). The receipts give no run dates. Owner decision 6.

14. **NOTE (local refs).** In this repo the local branch `solr-9396-submit` points at `f0e03ca25da`, one commit behind `origin/solr-9396-submit` (`a5ab2eda4e6`). This review used the origin refs. Do not push from the local branch.

## Task results

**SOLR-9396: draftable.** The draft is `pr-drafts/search-components/SOLR-9396.md`, written against `a5ab2eda4e6a9bcad5668653e80c0d0b510e6388`. It is ready after FIX 4 (squash) and FIX 5 (regex) are decided. The code matches the record: 3 files, 67 insertions, one new test (`testRowFieldNotInFl`, TestSubQueryTransformer.java L257-L283). The counts come from the receipt: 17 of 17 at head, and the base run had 1 failure of 11 (the new test). The Jira packet (`research/jira-context/SOLR-9396.json`: Open, Major, created 2016-08-09) matches the draft's symptom. The changelog author is Nick Shanin and there are no trailers. The draft names no Lucene behavior, so no 9.x or 10.x check applies.

**SOLR-14678: draftable, held.** The draft is `pr-drafts/search-components/SOLR-14678.md`, written against `5d94e6cf3981bd8a141a2063992148893e6d1bbb`. It is held until the owner decides FIX 1 (/get in this change or a follow-up), confirms the premise tree (FIX 2), and approves the narrowed changelog title (FIX 3). The per-document versus request-wide framing is the draft's "A choice to check" section (finding 10, owner decision 2). The Proof gives only the receipt's count, 18 of 18 at head, with no fail-before sentence. The six commits are all ticket-scoped, authored by Nick Shanin, with no trailers.

## Owner decisions

1. SOLR-14678: fix the /get multi-id path in this change, or name it as a follow-up in Limits. The draft assumes the follow-up, and the follow-up plan needs your confirmation.
2. SOLR-14678: keep per-document scope (implemented; no cross-document leak; /get gap) or switch to one request-wide set (fixes /get; reopens the round-28 leak). The draft presents this as the choice.
3. SOLR-9396: squash or reword the two handoff commits before any PR. The draft assumes it; the branch was not edited.
4. SOLR-9396: apply the regex fix (finding 5) or keep the `\{?` branch. The draft does not mention `${row.` either way.
5. Both: whether to enqueue a focused `TestChildDocTransformer` run at 14678's head before its PR, and a combined run once the second of the two lands. Nothing was run here.
6. Both: confirm the "verified" dates in the drafts (finding 13).
7. SOLR-14678: confirm which production tree the premise run used (finding 2). If it was the earlier request-wide variant, add the sentence from finding 2; otherwise leave the fail-before claim out.
8. SOLR-14678: confirm the Jira ticket text before posting the "What happens today" section (see Not checked).

## Not checked

- No builds, no tests, no test runs. Every behavior statement comes from reading code at the named SHAs. Findings 1, 5, 6, and 7 are by reading only.
- Gate logs (`g9396-gate.log`, `g9396-premise.log`, `g14678r35-gate-fix.log`) are not on disk. Counts come from the receipts. The `research/test-queue` JSON files have no rows for either ticket.
- The GitHub Actions runs cited in the receipts (37306343107 for 9396, 37695975655 for 14678) were not checked. No `gh` calls were made. The inventory shows no live PR for either ticket.
- Live heads: `git ls-remote` matched both heads on this run. No fetch was done.
- SOLR-14678 Jira text: there is no packet under `research/jira-context` and no row in the CSV export. The draft's symptom rests on the code and the inventory summary.
- Distributed search and SolrCloud /get were not traced for either ticket. The 9396 receipt includes `TestSubQueryTransformerDistrib` (1 of 1), which I did not read.
- Unknown field names that come only from `$row.` references were not traced through `SolrDocumentFetcher`.
- Writers other than JSON, XML, CSV, and javabin, including GeoJSON, were not checked.
- The failure text from the 9396 base run is not in the receipt, so the draft does not quote it.
- Lucene: neither draft names Lucene behavior, so no 9.x or 10.x check was made.
