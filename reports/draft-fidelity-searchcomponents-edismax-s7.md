# Search components draft fidelity, slice edismax-s7

Slice: `pr-drafts/search-components/` drafts SOLR-18109, SOLR-4374, SOLR-5394, SOLR-6193 and SOLR-6207, checked in worktree `wt/pr-prepare-suggester` (tree d627304e96b). Round reports used: `reports/search-components-1.md` (f1, f3), `reports/search-components-2.md` (h4), `reports/search-components-3.md` (w1), `reports/search-components-4.md` (s5). Receipts used: `receipts/SOLR-<n>.md` (all five present). Material grep: `material/` has no answers file for these tickets (one unrelated hit, `update-processing-final-round-3.md`, which does not name them). The assignment and claim files were not named in the spawn prompt and were not read.

Heads: `git ls-remote origin refs/heads/solr-<n>-submit` run for each ticket. Merge-bases were computed against the local `upstream/main` ref. No build, Gradle, test, or test-queue command was run. No write to git, GitHub or Jira was made. The one GitHub call was a read of apache/solr PR 4779.

## Verdicts

| Draft | Head checked (live `ls-remote` tip) | Verdict |
|---|---|---|
| SOLR-18109 | b19395e1f60acddb17cbd4ae3e3d0e75e0844c5f (matches draft) | CONSISTENT |
| SOLR-4374 | 801c62290f79483e714e88f907a6564e504dfca5 (matches draft) | DRIFT (1 item) |
| SOLR-5394 | 967445622f9294e26e425ec494d37e5ca13d4fe4 (matches draft) | DRIFT (2 items) |
| SOLR-6193 | ec94bf50c80ce75b44eaf78452e60e43972bc284 (matches draft) | DRIFT (4 items) |
| SOLR-6207 | b099a9f1be5a3fb529767ca625f42e7860e62f13 (matches draft) | DRIFT (1 item) |

Merge-bases with upstream main (used as "base commit" below): SOLR-4374 and SOLR-6207 cabedd1d968059215188f4e7563fb303241899ed; SOLR-5394 b6b2b8f10e9827e3b86e44649fb7966ce646c185; SOLR-6193 c3cdf7b46e8cfff3673f76d881f32cf8e7b00622.

Common checks that passed for all five: head SHA named in the draft matches the live tip; Proof counts and dates match the receipts (18109 7 of 7 and 3 of 3, verified 2026-10-04; 4374 16 of 16, verified 2026-10-07; 5394 49 tests, 0 failures, 0 errors, 1 skipped, verified 2026-10-07; 6193 focused set 30 of 30 plus 1 of 1, verified 2026-10-05; 6207 1 of 1, verified 2026-10-07); the 5394 new-test expected value (`expected:<1> but was:<-1>`) matches the receipt; every "Changelog" link resolves at the named head and the fragment exists (4374 lines 1-8, 5394, 6193, 6207); Choice and Limits sections match the round reports; no em dash (U+2014) and no internal process vocabulary (gate, receipt, claim, seed, handoff, round, subagent, submission) in the draft text; each draft has a verification date; AI header and footer present.

## SOLR-18109

Verdict: CONSISTENT.

Checked: the two test links resolve at b19395e1f60 and cover the stated lines (`DebugComponentTest.testExplainOther` L279-L287; `MoreLikeThisHandlerTest.testStandardDebugOutput` L255-L287, with the `finally` block that deletes both documents and commits). On upstream main, `DebugComponentTest` has no `explainOther` check and `MoreLikeThisHandlerTest` has no `rawquerystring`, `querystring` or `parsedquery` check, so the "What happens today" claim holds. PR 4779 (`gh pr view 4779 --repo apache/solr --json 'mergedAt,state,title'`) is MERGED at 2026-08-24T17:25:40Z, title "SOLR-18109: move SolrPluginUtils debug methods to DebugComponent", matching the draft. `doStandardQueryDebug`, `doStandardResultsDebug` and `doSimpleQuery` are public static at head, and the Jira packet names them (SOLR-18109.json, comment bodies).

Optional notes, not blocking:
- The draft has no title line. When the PR opens, use the h4 title "SOLR-18109: add tests for the debug helpers moved to DebugComponent". The Jira summary says "Fix", so the PR title and body must not.
- The draft has no changelog line ("not needed for a test only change"). The formula template lists one. Lead decides whether to keep the reason.
- Plain language: "skipped by construction" (Proof) is compressed. It could read "This change is test only, so no fail-before run applies."

## SOLR-4374

Verdict: DRIFT (1 item).

1. Draft says: "[getFieldName](https://github.com/nick-boss-tech/solr/blob/801c62290f79483e714e88f907a6564e504dfca5/solr/core/src/java/org/apache/solr/search/SolrReturnFields.java#L219-L238) accepts a name only when its first character can start a Java identifier."
   Evidence: `getFieldName` is at lines 219-238 with identical text at the base commit cabedd1d968 and at head 801c62290f7 (git show of both). The branch changes only lines 240-264 and 286-289. The paragraph describes pre-change symptom code, so the link must point at the base commit and the text must say so (pr-formula.md, section 1 and the presentation rule).
   Replacement: "Before this change, [getFieldName](https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/core/src/java/org/apache/solr/search/SolrReturnFields.java#L219-L238) accepted a name only when its first character can start a Java identifier. A name such as `1001` fails that test and falls through to the function parser, which reads the number as a constant. The ticket shows the result: `fl=1001` returns the number 1001 under the key `1001`, not the value of a field named `1001`."

Other citations checked and correct at head 801c62290f7: helper L240-L264 (the `getDigitLeadingFieldName` javadoc starts at 240 and the method ends at 264); call site L286-L289 (`getFieldName` call and the digit-leading fallback); test `testDigitLeadingFieldName` L399-L406; changelog `changelog/unreleased/SOLR-4374-digit-leading-field-in-fl.yml` L1-L8.

Optional notes, not blocking:
- Changelog file name is descriptive, not `SOLR-4374.yml` as the brief and the template literally say. The file exists at the head and its title matches the code. The same applies to SOLR-5394, SOLR-6193 and SOLR-6207. Renaming would move the head. Lead decides. Round s5 item 12 accepted this for 6207.
- Limits and Choice match round w1 (findings 3, 4, 8) and owner decisions 3 to 5.
- Length is about 3,600 characters, close to the 3,500 guide.
- Plain language: "digit-leading", "catch-all dynamic field", "function parser", "identifier check" are compressed. Optional.

## SOLR-5394

Verdict: DRIFT (2 items).

1. Draft says: "`parseParams` starts the per-field thread count at -1 ([SimpleFacets.java#L183](https://github.com/nick-boss-tech/solr/blob/967445622f9294e26e425ec494d37e5ca13d4fe4/solr/core/src/java/org/apache/solr/request/SimpleFacets.java#L183))."
   Evidence: at head 967445622f9 line 183 reads `int threads = 1; // a negative value would mean one thread per segment for facet.method=fcs`. The -1 is at the base commit b6b2b8f10e98, line 183 (`int threads = -1;`). The cited line contradicts the text. Round f1 finding 9 and the receipt agree on the base value.
   Replacement (this whole paragraph replaces the draft's "What happens today" paragraph that starts with "`parseParams` starts"; it also resolves item 2): "`parseParams` starts the per-field thread count at -1 before this change ([SimpleFacets.java#L183](https://github.com/nick-boss-tech/solr/blob/b6b2b8f10e9827e3b86e44649fb7966ce646c185/solr/core/src/java/org/apache/solr/request/SimpleFacets.java#L183)). A value of 0 or less means no limit in `PerSegmentSingleValuedFaceting` ([L128](https://github.com/nick-boss-tech/solr/blob/b6b2b8f10e9827e3b86e44649fb7966ce646c185/solr/core/src/java/org/apache/solr/request/PerSegmentSingleValuedFaceting.java#L128)), so each segment gets a task on the facet executor. The ticket reports about 46 facet executor threads from two fields. The global `facet.threads` parameter does not reach this path ([SimpleFacets.java#L885](https://github.com/nick-boss-tech/solr/blob/b6b2b8f10e9827e3b86e44649fb7966ce646c185/solr/core/src/java/org/apache/solr/request/SimpleFacets.java#L885)). The links in this paragraph point at the base commit, before this change."

2. Draft says: "A value of 0 or less means no limit in `PerSegmentSingleValuedFaceting` ([L128](https://github.com/nick-boss-tech/solr/blob/967445622f9294e26e425ec494d37e5ca13d4fe4/...#L128)), ... ([SimpleFacets.java#L885](https://github.com/nick-boss-tech/solr/blob/967445622f9294e26e425ec494d37e5ca13d4fe4/solr/core/src/java/org/apache/solr/request/SimpleFacets.java#L885))."
   Evidence: `PerSegmentSingleValuedFaceting.java` line 128 (`int threads = nThreads <= 0 ? Integer.MAX_VALUE : nThreads;`) and `SimpleFacets.java` line 885 (`int maxThreads = req.getParams().getInt(FacetParams.FACET_THREADS, 0);`) are identical at the base commit and at head. Both describe pre-change behavior, so they link the base commit and the text says so.
   Replacement: use the paragraph given in item 1 (same text, same links).

Other checks passed: L208-L211 (local `threads` read) and L183 at head for "What this change does"; L593 `threads == 0 ? directExecutor : facetExecutor` supports "threads=0 still runs on the calling thread"; the threads value is used only in the fcs branch (grep), so the Limits claim "Only the fcs branch reads the per-field thread count" holds; the new test at L2737-L2746 holds the four checks the draft lists; the changelog title (`changelog/unreleased/SOLR-5394-fcs-default-threads.yml` at head) matches the code. The reporter's "46, I think" and the 2014 comment (Vitaliy Zhovtyuk, 2014-03-19, proposes threads=1) match the Jira packet.

Optional notes, not blocking:
- Presentation rule (formula, not a brief check): the Proof, "A choice to check" and Limits sections open without a bold one-line summary. The other sections have one. A bold summary line for each would match the rule.
- Length is 3,858 bytes against the guide of about 3,500 (round f1 notes the same).
- "pool threads" in the Implemented bullet is the Java executor sense, not workspace vocabulary. "executor threads" reads more plainly.
- Timing comparison is still an open owner call (f1 owner decision 2). The Limits line already says so.

## SOLR-6193

Verdict: DRIFT (4 items). The draft is held (round f3: do not post until the owner picks option A and the FIX items are applied). The DRIFT items are the ones that must be fixed before any post.

1. Draft says: "The merge reads `facet.limit`, `facet.sort`, `facet.offset` and `facet.pivot.mincount` only from the request parameters, in [PivotFacetField.java](https://github.com/apache/solr/blob/c3cdf7b46e8cfff3673f76d881f32cf8e7b00622/solr/core/src/java/org/apache/solr/handler/component/PivotFacetField.java#L64-L67)."
   Evidence: at the base commit c3cdf7b46e8, `PivotFacetField.java` line 64 reads `facet.pivot.mincount`, 65 `facet.offset`, 66 `facet.limit`, and 69 `facetFieldSort = parameters.getFieldParam(field, FacetParams.FACET_SORT, defaultSort)`. The range L64-L67 misses the `facet.sort` read at L69. The link is correct, but the text does not say it is the base commit, before this change.
   Replacement: "Before this change, the merge reads `facet.limit`, `facet.sort`, `facet.offset` and `facet.pivot.mincount` only from the request parameters, in [PivotFacetField.java](https://github.com/apache/solr/blob/c3cdf7b46e8cfff3673f76d881f32cf8e7b00622/solr/core/src/java/org/apache/solr/handler/component/PivotFacetField.java#L64-L69)."

2. Draft says: "The top field of each pivot now reads its settings from the local params, layered over the request parameters ([PivotFacetField.java](https://github.com/nick-boss-tech/solr/blob/ec94bf50c80ce75b44eaf78452e60e43972bc284/solr/core/src/java/org/apache/solr/handler/component/PivotFacetField.java#L68-L74))."
   Evidence: at head ec94bf50c80, line 67 starts the `parameters =` statement, 70 holds `: SolrParams.wrapDefaults(localParams, rb.req.getParams());`, and 76 reads `facet.sort`. The range L68-L74 misses L67 and L76, and the text says "its settings".
   Replacement: "The top field of each pivot now reads its settings from the local params, layered over the request parameters ([PivotFacetField.java](https://github.com/nick-boss-tech/solr/blob/ec94bf50c80ce75b44eaf78452e60e43972bc284/solr/core/src/java/org/apache/solr/handler/component/PivotFacetField.java#L67-L76)). Nested fields reuse the settings of their top field. The coordinator passes the local params in [PivotFacet.java](https://github.com/nick-boss-tech/solr/blob/ec94bf50c80ce75b44eaf78452e60e43972bc284/solr/core/src/java/org/apache/solr/handler/component/PivotFacet.java#L154)."

3. Draft's changelog link (`changelog/unreleased/SOLR-6193-pivot-facet-local-params.yml` at ec94bf50c80, line 2) does not match the draft's Limits or the code. The title says the fragment's changes "honor facet.limit, facet.sort, facet.offset and facet.pivot.mincount given as local params". Round f3 finding 2: no test covers a local `facet.offset` or `facet.pivot.mincount`. The draft's own Limits say those two are untested and may be wrong. Replacement for the YAML title line (a branch edit, not a draft edit; the commit moves the head, so every `ec94bf50c80` link must then be re-pointed): `Distributed pivot facets now use facet.limit and facet.sort given as local params of facet.pivot when merging shard responses.`

4. Draft says: "**The cases that were commented out in the test now run, and they fail on the base code and pass with this change.**"
   Evidence: `receipts/SOLR-6193.md` line 7 (Proof): on base c3cdf7b46e8, `DistributedFacetPivotLargeTest` "fails at the first re-enabled case". The test stops at its first failing assertion, so the record does not show that every re-enabled case fails on base. Round f3 item 3 also notes that the premise log is not on disk.
   Replacement: "**The cases that were commented out in the test now run. The test fails on the base code at the first of them and passes with this change.**"

Other checks passed: the scope sentence and the "not covered" wording match round f3 item 1 in substance (the draft says "in the ticket", f3 says "in SOLR-6193"; either is fine). The precedence Choice matches f3 item 5 and the summary: the current order is `SimpleFacets.java` L191 (`SolrParams.wrapDefaults(localParams, global)`), and the ticket example gives 20 under the current order by reading. The Limits match f3 items 3 and 4: `FacetComponent.java` L637-L645 at head is unchanged from the base, and the shard request is sized from the request-level `facet.limit` and `facet.offset` (`originalParams`, `requestedLimit`, offset read). `PivotFacet.java` L154 at head passes `localParams`. The Proof counts match the receipt (1, 1, 1, 1, 1, 11, 10, 3, 1 = 30).

Optional notes, not blocking:
- Held: do not post until the owner picks option A (f3 owner decision 1) and the FIX items are applied (f3 items 3, 4, 6 are branch and receipt edits, not draft text).
- Length is 4,173 bytes against the guide of about 3,500. The ticket is complex, so trimming is optional.
- "A single-node search of the same request returns 4" (What happens today) is by reading. It is not in the receipt. Round f3 item 4 says the same.
- Limits links to `FacetComponent.java` (L637-L645) and the Choice link to `SimpleFacets.java` L191 point at head. Those lines are unchanged from the base, so the content is the same. Optional: link the base commit for consistency with the symptom rule.
- The changelog descriptive-name note applies here too (see SOLR-4374).

## SOLR-6207

Verdict: DRIFT (1 item).

1. Draft says (What happens today, second paragraph): "[`SolrQueryRequestBase.getParamString()`](https://github.com/nick-boss-tech/solr/blob/b099a9f1be5a3fb529767ca625f42e7860e62f13/solr/core/src/java/org/apache/solr/request/SolrQueryRequestBase.java#L171-L174) returns `origParams`", with the matching links for `setParams` (L92-L95) and the request log (`SolrCore.java` L2927) at b099a9f1be5.
   Evidence: `SolrQueryRequestBase.java` L92-L95 (`setParams`) and L171-L174 (`getParamString`), and `SolrCore.java` L2927 (`rsp.addToLog("params", "{" + req.getParamString() + "}");`), are identical at the base commit cabedd1d968 and at head b099a9f1be5. The branch does not change these files (its diff is the `SolrQueryRequest.java` javadoc, the test, and the changelog). This is pre-change symptom code, so the links must point at the base commit and the text must say so.
   Replacement (whole paragraph; the bold summary line above it stays):
   "[`SolrQueryRequestBase.getParamString()`](https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/core/src/java/org/apache/solr/request/SolrQueryRequestBase.java#L171-L174) returns `origParams`. [`setParams`](https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/core/src/java/org/apache/solr/request/SolrQueryRequestBase.java#L92-L95) replaces only the current parameters. A caller that reads a new `q` through `getParams()` gets the new value. A log line built from `getParamString()` still shows the old one. The request log uses this string ([SolrCore.java line 2927](https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/core/src/java/org/apache/solr/core/SolrCore.java#L2927)). The interface javadoc says only that the string shows "all the important parameters," so it does not say which set it shows. The ticket reports this. The links in this paragraph point at the base commit, before this change."

Other checks passed: the javadoc change `SolrQueryRequest.java` L128-L134 at head is the change and reads as the draft says (the base has the one-line "all the important parameters" javadoc at L128); `RequestUtil.java` L182 (`req.setParams(newParams)`) holds the claim; `SolrQueryRequestBaseTest.java` L25-L39 at head holds the test body; the changelog `changelog/unreleased/SOLR-6207-getparamstring-javadoc.yml` exists at head and its title matches the code; type `other` is valid in `changelog/logchange-config.yml`. The Choice matches round s5 item 13 and the 2014 comment (Chris M. Hostetter, 2014-06-27: "deprecate and remove this method ... to figure out which set of params it returns"). The Limits match s5.

Optional notes, not blocking:
- Limits: "The javadoc describes the request-handler path." The javadoc states a general rule. The check covers the request-handler path only (s5 item 13). Optional wording: "The javadoc's rule is checked on the request-handler path only."
- `RequestUtil.java` L182 in "What this change does" is pre-change code, used as evidence. Its content is identical at base, so no change is needed.
- Changelog: the descriptive-name note applies. Round s5 item 12 says the owner may drop the entry.

## Not done

- Not read: the assignment and claim files for this slice (not named in the spawn prompt). The slice was taken from the spawn prompt.
- Not run: no build, Gradle, test, test-queue, or changelog YAML parse. Receipts say the YAML parses for 4374, 5394 and 6207.
- Not checked live: Jira (local packets and the packet comment bodies were read; `research/jira-context/` has no fetch date). Gate logs are not on disk, so base-failure statements rest on the receipts.
- GitHub: only the read-only `pr view` of apache/solr PR 4779. No other PR, comment or branch was checked.
- Not checked: the 6193 single-node "returns 4" claim beyond reading the code; the base-file behavior of the other re-enabled 6193 cases (not in any record); Lucene claims (none in these drafts).
