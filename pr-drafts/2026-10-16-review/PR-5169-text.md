# PR #5169 draft: SOLR-12505 body, trimmed (review item 3)

- Written against head: cc3b9d681e9d3caa63bb405baf5b19c66e64cd04 (live branch solr-12505-submit, read through the API 2026-10-17).
- Visible characters: 3,460 (body; markdown syntax and link URLs removed). Limit 3,500.
- Proof numbers: receipts/SOLR-12505.md.
  - Head counts (StreamDecoratorTest 60 tests, 0 skipped, 0 failures, 0 errors): section "Review fix round 2 at cc3b9d681e", fresh JUnit XML.
  - Merge-base proof (3 failures): section "Gate at 248a9715b1c3", the proof leg with FetchStream.java reverted to 14c7aac0d151. The same proof is recorded at that head, not at cc3b9d681e.
  - The "$" rejection delta (1 failure at the pre-fix head): section "Review fix round 1 at bd6c16d916".
- Gap, not claimed in the body: the closing-brace and leading-quote test (added at cc3b9d681e) has no fail-before run on record. The body says so.
- Trailers: three commits on the branch carry "Co-Authored-By: Claude" trailers (68d4b0b668, 1249998133, d0e8385b9b). Removing them rewrites history on an open PR head. That needs Nick's go-ahead, and this draft does not depend on it.
- Not applied to the live PR. Drafts only, per the assignment.

=== TITLE ===

(unchanged) SOLR-12505: fetch() returns the fetched fields when the target handler's default query parser is not lucene

=== BODY ===

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-12505

## What happens today

**fetch() returns the tuples without the fetched fields when the target handler's default query parser is not lucene.**

fetch() sends its batch as `{! df=<join field> q.op=OR cache=false }` followed by the escaped key values, and sends no `defType`. Solr reads the `{!` prefix only when the default parser is lucene or func ([QParser.java lines 387 to 396, merge-base](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/search/QParser.java#L387-L396)). With another default, the batch is read as plain query text and the fetched fields are missing.

## What this change does

**fetch() sends `defType=lucene` with every batch, so the prefix is read whatever the handler's default is.** The parameter is added in [FetchStream.java lines 273 to 276](https://github.com/nick-boss-tech/solr/blob/cc3b9d681e9d3caa63bb405baf5b19c66e64cd04/solr/solrj-streaming/src/java/org/apache/solr/client/solrj/io/stream/FetchStream.java#L273-L276). A handler with the lucene default sends the same batch as before. A `defType` default on the handler no longer applies to fetch batches.

**fetch() also rejects a join field name that would break its batch query.** The check runs when the expression is built and names the field in the error ([FetchStream.java lines 153 to 187](https://github.com/nick-boss-tech/solr/blob/cc3b9d681e9d3caa63bb405baf5b19c66e64cd04/solr/solrj-streaming/src/java/org/apache/solr/client/solrj/io/stream/FetchStream.java#L153-L187)). A name with whitespace or `}` anywhere is rejected, and so is a leading quote or `$`. The name is rejected rather than escaped, because escaping would let the batch accept a name that is not in the schema.

## Proof

**The new tests fail on the merge base, as recorded at 248a9715b1.**

- With FetchStream.java reverted to `14c7aac0d151` and the branch tests in place, three tests fail: `testFetchStreamWithNonLuceneDefaultDefType` (ComparisonFailure, expected alpha but was null), and both join-name rejection tests (no exception thrown).
- At head `cc3b9d681e`: StreamDecoratorTest 60 tests, 0 skipped, 0 failures, 0 errors, from fresh JUnit XML.
- The leading-`$` rejection fails on the pre-fix head `bd6c16d916` with one failure: a server "Missing param a_i" error, because the name was read as a parameter reference.
- The closing-brace and leading-quote test was added at `cc3b9d681e`. Its fail-before run is not on record.

The defType test uses a dedicated copy of the configset, so its edismax default does not reach the other tests.

## A choice to check

**The batch sets `defType=lucene`. The ticket thread also named the terms query parser.**

Joel Bernstein proposed setting the parameter, and David Smiley agreed ([his comment](https://issues.apache.org/jira/browse/SOLR-12505?focusedCommentId=16519637)). Smiley also suggested the terms parser. That alternative sets `defType=terms` with the keys as the query. It changes the query text of every batch, and the keys would need the terms separator instead of the escaping the batch uses now.

The batch ORs one clause per key, so a large batch can hit `maxBooleanClauses`; the terms parser has no such limit.

Was setting `defType` on the batch the right call, or should the batch use the terms parser?

## Limits

- The batch no longer depends on a handler's default `defType`. The test uses edismax only.
- A handler that pins `defType` under `invariants` still overrides the batch's `defType=lucene`, because request parameters do not override invariants. That configuration is not tested.
- The realtime get endpoint is not covered.
- The join-name message sits on the cause chain, because StreamFactory reports a generic message.
- The change is written against merge-base `14c7aac0d151` and has not been rebased.

Changelog: [changelog/unreleased/SOLR-12505-fetch-stream-deftype.yml](https://github.com/nick-boss-tech/solr/blob/cc3b9d681e9d3caa63bb405baf5b19c66e64cd04/changelog/unreleased/SOLR-12505-fetch-stream-deftype.yml)

### AI assistance

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.
