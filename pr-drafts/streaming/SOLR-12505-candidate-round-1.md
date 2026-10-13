# Staging notes (not part of the PR text, not for posting)

- Candidate revision of `pr-drafts/streaming/SOLR-12505.md`, from the strong review round for the pr-prepare tip a9a8c85d074 (reviewer section kept in the round's scratchpad; summary in `reports/`). The live draft is unchanged in this round. It still describes the defType route at the older head and does not mention the validate route or the new test.
- The fork head is `1a1875d95f3680c20447006efc3c949c57f5135f` (vm2 fixture fix on top of the validate route). The full hardening gate at this head has not run. The brackets below mark everything that waits for it.
- Owner items that change the text: (1) the Unicode whitespace gap (widen the check with `\p{javaWhitespace}`, or reword to "ASCII whitespace"); (2) the Choice section (validate versus escape, or defType versus the terms parser); (3) the StreamFactory cause text (Limits line or its own ticket); (4) the changelog title, which describes only defType; (5) quote and backslash names at the base; (6) JIRA attributions; (7) the merge-base recount; (8) the fork branch `ci/12505-stream` (b673677a4af), not in any record.
- Do not post until the gate lands and the owner items are settled.

---

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-12505

## What happens today

**fetch() returns the tuples without the fetched fields when the target handler's default query parser is not lucene.**

fetch() sends its batch as `{! df=<join field> q.op=OR cache=false }` followed by the escaped key values, and it sends no `defType`. The handler's default parser reads the batch. Solr reads the `{!` prefix only when that default is lucene or func ([QParser.java lines 387 to 396, merge-base](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/search/QParser.java#L387-L396)). With another default, the fetched fields are missing. The batch is built at [FetchStream.java lines 239 to 246, merge-base](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/solrj-streaming/src/java/org/apache/solr/client/solrj/io/stream/FetchStream.java#L239-L246). The join field name from `on=` goes into that prefix with no check ([FetchStream.java lines 145 to 151, merge-base](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/solrj-streaming/src/java/org/apache/solr/client/solrj/io/stream/FetchStream.java#L145-L151)). [With a lucene default, a join name with a space, such as `on="id=a i"`, fails with `invalid query parser 'i'`. Confirm after the full run.]

## What this change does

**fetch() sends `defType=lucene` with every batch, and rejects a join field name that would break the batch.**

[FetchStream.java lines 255 to 257](https://github.com/nick-boss-tech/solr/blob/1a1875d95f3680c20447006efc3c949c57f5135f/solr/solrj-streaming/src/java/org/apache/solr/client/solrj/io/stream/FetchStream.java#L255-L257) set `defType=lucene`, so the prefix is read whatever the handler's default is. Behavior change: a `defType` default on the handler no longer applies to fetch batches. A handler with the lucene default sends the same batch as before.

The join field name is checked when the stream is built. A name with ASCII whitespace, braces, `!`, quotes or a backslash fails with "fetch on parameter has an invalid join field name [...]" ([FetchStream.java lines 153 to 159](https://github.com/nick-boss-tech/solr/blob/1a1875d95f3680c20447006efc3c949c57f5135f/solr/solrj-streaming/src/java/org/apache/solr/client/solrj/io/stream/FetchStream.java#L153-L159)). The check runs in both constructors. Through `StreamFactory`, the message sits one level down the cause chain, because the factory reports only "Unable to construct instance of FetchStream" ([StreamFactory.java lines 593 to 611](https://github.com/nick-boss-tech/solr/blob/1a1875d95f3680c20447006efc3c949c57f5135f/solr/solrj-streaming/src/java/org/apache/solr/client/solrj/io/stream/expr/StreamFactory.java#L593-L611)).

## Proof

**The new test fails before the check and passes with it.**

[testFetchStreamRejectsJoinFieldNameThatBreaksLocalParams](https://github.com/nick-boss-tech/solr/blob/1a1875d95f3680c20447006efc3c949c57f5135f/solr/solrj-streaming/src/test/org/apache/solr/client/solrj/io/stream/StreamDecoratorTest.java#L1009-L1036) joins on `a i`. On the parent commit 5f20e1171bc, which has no check, the drain throws `invalid query parser 'i'` and the cause-chain assertion fails. With the check, the expression fails with the validation message. [StreamDecoratorTest N tests, S skipped, F failures; verified DATE at 1a1875d95f.]

[testFetchStreamWithNonLuceneDefaultDefType](https://github.com/nick-boss-tech/solr/blob/1a1875d95f3680c20447006efc3c949c57f5135f/solr/solrj-streaming/src/test/org/apache/solr/client/solrj/io/stream/StreamDecoratorTest.java#L946-L1006) fails on the merge-base FetchStream with `expected:<alpha> but was:<null>` and passes with the defType line.

## A choice to check

[Owner decides. Keep the terms-parser question, or replace it with the validate-or-escape question.]

## Limits

- Covered: fetch batches on a handler with a non-lucene default. The test uses edismax only.
- Not covered: a handler that sets `defType` as an invariant (not tested). Names with quotes or backslashes are not characterized at the base [owner item 5].
- The check looks at ASCII whitespace only [delete this line if the check is widened].
- Only the batch request and the join name check change. The search side is untouched.
- Base: merge-base 14c7aac0d15, [N] commits behind upstream main.
- StreamFactory reports constructor failures without their cause text. That is outside this change. I can open a follow-up ticket and PR for it on request.

Changelog: [changelog/unreleased/SOLR-12505-fetch-stream-deftype.yml](https://github.com/nick-boss-tech/solr/blob/1a1875d95f3680c20447006efc3c949c57f5135f/changelog/unreleased/SOLR-12505-fetch-stream-deftype.yml)

### AI assistance

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.
