# Query parsing draft fidelity, slice 3

Assignment: `assignments/pool-draft-fidelity-configsets-query-schema.md`, slice A3. Claim: `claims/pool-draft-fidelity-configsets-query-schema.md` (slice A3: SOLR-12608, 12871, 15615, 16267, 17280). Slice drafts: `pr-drafts/query-parsing/SOLR-12608.md`, `SOLR-12871.md`, `SOLR-15615.md`, `SOLR-16267.md`, `SOLR-17280.md`. Worktree HEAD `d627304e96bb614fccb1b031b99ce4b4cc819869` as the lead instructed (the brief names `e84522fa5bc`, the earlier claim commit). No tracked changes in the worktree.

Category round report: `reports/query-parsing-round-1.md`, with parts `-q5` (12871, 15615), `-q6` (12608), and `-q7` (16267, 17280). Part `-q1` mentions 12608 only for merge checks. Answers material: a grep of `material/` for the five ticket numbers finds no files. Receipts present for all five: `receipts/SOLR-<n>.md`.

Head checked per draft (`git ls-remote origin refs/heads/solr-<n>-submit`; every named head resolves with `cat-file -t`):
- SOLR-12608: `solr-12608-submit` = `d1dd8a1f9f0aa17c6e34f8c5e40cb9768a83aea2`. Matches the draft.
- SOLR-12871: `solr-12871-submit` = `c79a49320cdb0390a9ae7118ab70efbf080826d7`. Matches.
- SOLR-15615: `solr-15615-submit` = `be77267bf5549ae6bdc91bc4122b5bf5256f41e1`. Matches.
- SOLR-16267: `solr-16267-submit` = `8f4b0c6d2fb0c89c833512de502f39eb1bec560e`. Matches.
- SOLR-17280: `solr-17280-submit` = `40817c5cb7ec96b6f4119a46bec3db8ae6a1f6de`. Matches.

Merge-bases with `upstream/main` (`8e62c268688`), used for symptom-code links: 12871 `9b3a84b1c460981eab09d8ffaef776acc4a184f8`; 15615 and 17280 `e2cdb2d7e8ae0be4cf6cf0606206c67d89e7278f`; 12608 and 16267 `14c7aac0d151402b00259e2fb9bf5eed7049ec5d`.

Method notes. The drafts have no separate title line, so each draft's bold summary is checked against its branch changelog title. Each receipt count was compared with the draft. Code lines were read with `git show <sha>:<path>`. Jira text came from the on-disk packets under `research/jira-context/`. No draft contains an em dash.

## Verdicts

| Draft | Head checked | Verdict |
|---|---|---|
| SOLR-12608 | `d1dd8a1f9f0a` (matches live tip) | DRIFT (5 items) |
| SOLR-12871 | `c79a49320cd` (matches live tip) | DRIFT (2 items) |
| SOLR-15615 | `be77267bf55` (matches live tip) | DRIFT (1 item) |
| SOLR-16267 | `8f4b0c6d2fb` (matches live tip) | DRIFT (2 items) |
| SOLR-17280 | `40817c5cb7e` (matches live tip) | DRIFT (1 item) |

Item counts include branch-side changelog titles (brief check 4). The fix for those lands on the branch, not in the draft. Any branch fix moves the head SHA, so every link in the draft must then be re-pointed.

## SOLR-12608

Verdict: DRIFT (5 items). Draft status: HOLD in the record (q6). Counts match the receipt (`receipts/SOLR-12608.md` line 6: 86 focused tests, 0 failures, TestSolrQueryParser 38). The code links are correct: `SolrQueryParserBase.java` L1271 (`collapseRepeatedWildcards`), L1305 (the call in `getWildcardQuery`), and `TestSolrQueryParser.java` L276-L291 at d1dd8a1.

1. Draft says: "At head `d1dd8a1f9f0aa17c6e34f8c5e40cb9768a83aea2`, the class passes 38 of 38, including this method."
   - Evidence: The Proof has no verification date. pr-formula.md section 3 requires one. The receipt gives the gate date as 2026-10-05 (line 8: "SOLR-12608 gate DONE, 2026-10-05"; line 4: pushed 2026-10-05).
   - Replacement: "At head `d1dd8a1f9f0aa17c6e34f8c5e40cb9768a83aea2`, verified 2026-10-05, the class passes 38 of 38, including this method."

2. Draft says (line 1): "HOLD: not for posting. The Proof below says only what the record supports. The owner must decide whether to submit, and the changelog title must change first (see the q6 report, FIX 5)."
   - Evidence: The banner uses process vocabulary (HOLD, owner, q6 report), which brief check 7 flags. It must not reach a post.
   - Replacement: Delete line 1 and the blank line after it. The first line of the post is then the AI header.

3. Draft says (Limits): "- The changelog entry must not claim a memory benefit until one is shown."
   - Evidence: This is an internal to-do, not a limit. It also states a condition the branch does not yet meet (item 4). q6 FIX 5.
   - Replacement: "- The change makes no memory claim." Use it after item 4 lands on the branch.

4. Branch changelog, `changelog/unreleased/SOLR-12608-collapse-repeated-wildcards.yml` lines 1-3 at d1dd8a1, says: "...which avoids the excessive memory use seen with edismax queries made of thousands of * characters."
   - Evidence: The draft says the change makes no memory claim (Limits). No record shows a memory benefit. The research note says SKIP, and the 5000-star assertion checks only status 0 (q6 FIX 5; `research/branch-reviews/round-28/SOLR-12608-review.md`, Findings item 1).
   - Replacement (branch file, whole title block):
     ```
     title: >
       Runs of unescaped * in a wildcard query term are collapsed into a single *. The terms matched do not change.
     ```

5. Draft says: "It gives no other reproduction detail."
   - Evidence: `research/jira-context/SOLR-12608.json`, Description: "The file 'select_resp.json' describes the query request (faceting is active)." The ticket does give one more detail.
   - Replacement: "It also says that faceting is active in that request."

Optional notes, not blocking:
- "This change does not reproduce the error on the current code" rests on one status-0 assertion at this head (q6 NOTE 6). The premise log `g12608-premise.log` is not on disk. A safer wording: "The test run at this head returns status 0 for 5000 stars."
- "The focused run at this head had 86 tests" is test-gate jargon. Plain option: "The test run of this class at this head had 86 tests, and none failed."
- `collapseRepeatedWildcards` has one caller (`SolrQueryParserBase.java` L1305), so the "only wildcard terms" Limit holds.

## SOLR-12871

Verdict: DRIFT (2 items). Counts match the receipt (`receipts/SOLR-12871.md` lines 6-7: TestNestedDocsSort 12 of 12; base 12 tests with 1 failure). Verified date 2026-10-07 is present. The Limits follow q5 (rewrite question and client-message follow-up in Limits, no choice section, owner decision 2 still open). The changelog FIX from q5 is the open problem.

1. Draft says: "([parse loop](https://github.com/nick-boss-tech/solr/blob/c79a49320cdb0390a9ae7118ab70efbf080826d7/solr/core/src/java/org/apache/solr/search/SortSpecParsing.java#L112-L126), [message](https://github.com/nick-boss-tech/solr/blob/c79a49320cdb0390a9ae7118ab70efbf080826d7/solr/core/src/java/org/apache/solr/search/SortSpecParsing.java#L157-L164))"
   - Evidence: This is symptom code in "What happens today". `SortSpecParsing.java` is not in the diff from the merge-base `9b3a84b1c46` to c79a493 (the diff lists only the changelog, `ChildFieldValueSourceParser.java`, and `TestNestedDocsSort.java`). pr-formula.md says symptom links go to the merge-base, and the text says so. The line ranges are right at both commits: the try block is L113-L126 and the generic message is L157-L164.
   - Replacement: "([parse loop](https://github.com/nick-boss-tech/solr/blob/9b3a84b1c460981eab09d8ffaef776acc4a184f8/solr/core/src/java/org/apache/solr/search/SortSpecParsing.java#L112-L126), [message](https://github.com/nick-boss-tech/solr/blob/9b3a84b1c460981eab09d8ffaef776acc4a184f8/solr/core/src/java/org/apache/solr/search/SortSpecParsing.java#L157-L164); both links point to the merge-base commit, before this change)"

2. Branch changelog, `changelog/unreleased/SOLR-12871-childfield-sort-rewriteable.yml` line 2 at c79a493, says: "...is now rejected with a clear 400 error instead of a Lucene UnsupportedOperationException."
   - Evidence: On base and branch the client gets the same 400 and the same generic message (`SortSpecParsing.java` L157-L164). The only client-visible change is `metadata.root-error-class` (`solr/core/src/java/org/apache/solr/servlet/ResponseUtils.java` L77-L79). The draft's "What this change does" already says the status and message are unchanged. q5 finding 1.
   - Replacement (branch file, line 2 only):
     ```
       Sorting by childfield() on a field type whose sort needs rewriting (such as CurrencyFieldType) is now rejected before the unsupported sort is built, and the server log names the field and its type.
     ```

Optional notes, not blocking:
- "On main the sort is still built." Plainer: "On upstream main the sort is still built."
- The log line at `ChildFieldValueSourceParser.java` L204-L205 is the existing catch, not new code. Linking it at the head is acceptable.
- Lucene 9.x was not checked (q5 "Not checked"). The draft names the exception only through the ticket.

## SOLR-15615

Verdict: DRIFT (1 item). Counts match the receipt (`receipts/SOLR-15615.md` lines 6-7: CloudMLTQParserTest 15 of 15; base has exactly the one failing test). Verified 2026-10-07 is present. Code links are correct at be77267: `CloudMLTQParser.java` L118-L152 (local get, then the fallback loop, which runs L131-L153), and `CloudMLTQParserTest.java` L166-L211 (the new test). The Choice section has a live alternative (parallel lookups, or a local lookup), so it meets pr-formula.md section 4. Owner decision 3 (keep or drop) is still open.

1. Draft Limits has no sentence on the fallback lookup's authorization. Draft says: "## Limits ... - The test covers two collections. The cost on aliases with many collections is not measured. I can measure it and add a bound on request."
   - Evidence: q5 owner decision 5 and finding 15: confirm the fallback carries the caller's identity, then add one Limits sentence. The fallback calls `coreContainer.getZkController().getSolrClient().getById(collection, id)` (`CloudMLTQParser.java` L143-L144 at be77267). `git diff e2cdb2d7e8ae be77267` adds no authorization or principal code. The check is still open, so the sentence says so.
   - Replacement (add as the last Limits bullet): "- The fallback lookup uses the node's own Solr client to read the other collections. This change adds no authorization check for them, and whether the lookup carries the caller's identity is not yet confirmed." Replace "not yet confirmed" once the owner has checked it.

Optional notes, not blocking:
- Branch-side open item, not a draft DRIFT: `CloudMLTQParserTest.java` L208 at be77267 reads "similar docs from the *other* collection (13, 14, ...)", but docs 13 and 14 are indexed into COLLECTION (q5 FIX 2). The draft's test link covers that line, so re-point it after the branch fix.
- The cost is stated in both the Choice section and Limits. pr-formula.md warns against restating a claim across sections. Keeping it in Limits only would be enough.

## SOLR-16267

Verdict: DRIFT (2 items). Counts match the receipt (`receipts/SOLR-16267.md` lines 6-7: TestJsonFacets 30 of 30, TestFunctionQuery 23 of 23). Verified 2026-10-04 is present. Code links are correct at 8f4b0c6: `ValueSourceParser.java` L1688-L1691 (single-argument `exists()`) and L1743-L1746 (two-argument `exists()`), and `TestJsonFacets.java` L2149-L2174 (the new block). The facet-stats claim holds: `CountValsAgg.java` L76, `MissingAgg.java` L74, `MinMaxAgg.java` L257/289/354, and `PercentileAgg.java` L150 all skip documents without a value. The Limits match q7 finding 14.

1. Draft says: "This is wider than the average in the ticket. The same answer reaches exists() and def(), as the changelog says."
   - Evidence: `def()` is Lucene's `DefFunction`. In `lucene-queries-10.4.0.jar` (read with `javap -c -p`), `DefFunction$1.exists(int)` returns true when any argument exists, and `get(int)` returns the first argument that exists. So `def()` changes through its arguments: it skips a sparse argument and uses the next one. q7 finding 15 left this unconfirmed.
   - Replacement: "This is wider than the average in the ticket. The same answer reaches `exists()`. `def()` skips an argument with no value and uses the first one that has a value, so its results change too."

2. Branch changelog, `changelog/unreleased/SOLR-16267.yml` line 1 at 8f4b0c6, says: "The exists() function and def() likewise see no value for such documents."
   - Evidence: Same as item 1 (q7 finding 15).
   - Replacement (branch file, line 1 final sentence only): "The exists() function likewise sees no value for such documents, and def() skips an argument that has no value."

Optional notes, not blocking:
- Receipt head versus queue head: `research/test-queue/results/SOLR-16267.json` has headSha `67ffcb2b63f` (q7 finding 13). The three changed files are identical at both commits. The owner should confirm the receipt head before posting.
- The "fail on the base code" claim rests on the receipt's pre-fix step (receipt line 7). The on-disk `SOLR-16267.failbefore.json` at `67ffcb2b63f` shows the failures (q7 finding 13). That file was not reopened here.
- "We can submit it after this change." Plainer: "We can open a follow-up pull request after this change."

## SOLR-17280

Verdict: DRIFT (1 item). The draft's Choice section, Limits and Proof are consistent with q7. Counts match the receipt (`receipts/SOLR-17280.md` lines 6-7: 5 tests, 0 failures; base 1 failure). Verified 2026-10-07 is present. Code links are correct at 40817c5: `SolrRangeQuery.java` L482-L489 (the removed `put`, with the new comment), and `TestFiltering.java` L109-L137 (the new test and its Javadoc). The class has five test methods, as the draft says.

1. Branch changelog, `changelog/unreleased/SOLR-17280-range-query-recursive-cache-update.yml` lines 1-4 at 40817c5, says: "A range query nested inside another filter is no longer cached on its own as a side effect."
   - Draft says (What this change does): "A range query that is not a filter, such as a clause of `q` or of a larger query, no longer gets a cache entry of its own as a side effect."
   - Evidence: The `put` is removed for every caller in `getSegState` (`SolrRangeQuery.java` L482-L489). The threshold is 16 (L372). Only the filter path caches through `getAndCacheDocSet` (`SolrIndexSearcher.java` L1007-L1008). So the changelog is too narrow, and the draft is right. q7 FIX 10.
   - Replacement (branch file, lines 1-4, whole title block):
     ```
     title: >
       SolrRangeQuery no longer puts its DocSet into the filterCache from inside another cache computation, which could
       fail with "IllegalStateException: Recursive update". A range query that is not used directly as a filter, such as a
       clause of q, is no longer cached on its own as a side effect. A range query used directly as a filter is still cached
       by the searcher unless it has cache=false.
     ```

Choice and Limits checks:
- The Choice section's option 2 (keep the `put`, detect recursion, as in PR 1481) is a live alternative. `gh pr view 1481 --repo apache/solr` returns state CLOSED, mergedAt null, closedAt 2025-02-10. The draft's "closed without merging" is correct. The ticket comment in `research/jira-context/SOLR-17280.json` (Michael Gibney) names PR 1481 as the runtime-detection fix and says "I think it's the right thing to do" to merge it, so "the ticket discussion favors" it holds.
- The Limits sentence "A maintainer comment on this ticket says it is the same issue as SOLR-16707" matches the ticket comment (Michael Gibney, in the packet). I did not check his maintainer status.

Optional notes, not blocking:
- "The focused run covers the whole class." Plainer: "The class has five test methods, and all five run."
- Commit subjects with handoff wording (q7 finding 8) are not draft text. The owner decides on squashing.

## Not done

- No build, Gradle, test, test-queue, gate, commit, push, PR, comment, review, or Jira action. One read-only `gh pr view 1481` call (PR state only).
- Lucene check: read-only `javap` on `lucene-queries-10.4.0.jar` for `DefFunction`. Lucene 9.x was not checked.
- Gate logs, premise logs, and the timestamped 16267 logs are not on disk (per the round). Counts are receipt-only. The 16267 receipt-head versus queue-head question (q7 finding 13) is left for the owner.
- 12871 client metadata (`root-error-class`) was checked by reading `ResponseUtils.java` L77-L79, not by a request.
- 15615 fallback authorization: the diff shows no auth code, but the end-to-end path was not traced (q5 finding 15).
- Changelog YAML parse, tidy, Spotless, and Error Prone were not checked. The receipts say they pass.
- Jira text is from the on-disk packets, not live. The 16267 claim that plain `avg(field)` is correct comes from the packet text and was not checked separately.
