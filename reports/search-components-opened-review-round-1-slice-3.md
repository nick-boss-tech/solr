# Search components opened review, round 1, slice 3 (PRs 5138 to 5141)

- Assignment: `assignments/pool-search-components-opened-review-round-1.md`. Claim: `claims/search-components-opened-review-round-1.md`.
- Reviewer: review agent, Windows host. All four PRs stay draft and open. No ready flips, no PR comments, no pushes, no commits, no Jira edits, no builds, no tests. No gate jobs filed (no finding needed a branch change).
- The formula is `pr-formula.md` at the root of the branch, not `material/pr-formula.md`.
- Full before and after text for the body edits: scratchpad `pr5138-body-before.md`, `pr5138-body-after.md`, `pr5140-body-before.md`, `pr5140-body-after.md`.

## PR #5138 (SOLR-7520), head `10b6931e1c05`. SATISFIED after 1 edit.

- Diff: the changelog, `CommandHandler.java` and `AnalyticsMergeStrategyTest`. Each `CommandHandler` execute path calls `searchWithTimeLimiter` once, so `complete()` runs once per search. The merge-base `14c7aac0d151` matches the body. The receipt counts (1, 1, 17, 2) and the gate date 2026-10-05 match.
- Edit: the symptom citations were bare text. They now link `CommandHandler.java#L254-L257` and `SolrIndexSearcher.java#L330-L335` at `14c7aac0d151`, with "at the same base commit" added.

## PR #5139 (SOLR-7550), head `687165651f92`. SATISFIED, no edit.

- Diff covers the changelog, `ShardResponse` (two setters now public), `PeerSync` (`handleResponse` package-visible, the 500 block at L423 to L433) and the new test class. The base and head links are correct.
- The test asserts 500 for versions gives true and 500 for updates gives false. "Inconclusive by construction" is correct: on base the test does not compile. The receipt counts and the INCONCLUSIVE verdict match.
- Notes, not edited: the Proof has no verification date (advisory under the owner's ruling). The changelog title names only the failed-core motive, while the body says every 500 counts. Accurate as written.

## PR #5140 (SOLR-8003), head `17c516a607a8`. SATISFIED after 3 edits.

- Diff covers the changelog, `SolrReturnFields`, `TextResponseWriter`, the transformers and the test. The receipt counts (3 of 3, 2026-10-09) match.
- Finding (claim error): the Proof said the test "sends the glob requests above". The shipped test sends `fl=id,su*:[json],link?:[json]`, `fl=au*:[xml]` and `fl=su*:[docid]`. No request uses `*_json` or `*:[json]`.
- Finding (formula): the "choice to check" section had no live alternative. The alternative needs field names the parser cannot know, as the section itself said.
- Edits:
  1. The Proof bullet now names the requests the test actually sends, and says the first request is rejected with a 400 on base.
  2. Limits: a new bullet says the `*_json` example used above and in the changelog is not in the test, and that `*:[json]` is not tested either.
  3. The "A choice to check" section was removed. Proof now follows "What this change does".
- Coverage gap: the `*_json` form is untested and is now disclosed. A test needs a fixture change, which is Nick's call. No gate was filed.
- Record disagreement: the receipt's premise names `fl=*_json:[json]`, but the shipped test does not exercise it. Flagged for the main side.

## PR #5141 (SOLR-8009), head `8795661ddc98`. SATISFIED, no edit.

- Diff covers the changelog, `RealTimeGetComponent` and the test. The base link L1065 to L1067 and the head links are correct. `ImplicitDocRouter.getTargetSlice` throws on an unknown `_route_`, so the null path is only the no-route case, and the changelog wording holds.
- The receipt counts (10, 4, 2, 1) match. The gated date 2026-10-05 matches the body. The receipt also records a 2026-10-12 reconciliation run with the same counts; the body cites only the gated date, which is correct.
- The duplicate-result Limit is the slate's owner item SOLR-8954, and it is named correctly.

## Formula checks, all four PRs

- Bold summaries open every section. The AI header and footer are present and unchanged. No em or en dashes, no "we", no gate, receipt or JUnit vocabulary. The changelog links the head SHA. The fragments were parsed by reading, since no YAML library is on this host, and all four are valid.
