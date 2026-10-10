🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-874

## What happens today

**A trailing or leading AND, OR, NOT, && or || makes the dismax parser fail with a parse error.**

With `defType=dismax`, the query `q=ipod AND` fails to parse. The same happens for `q=ipod &&`, `q=OR ipod` and `q=AND cool`. The ticket reports the first case, and a comment on the ticket reports a query that starts with OR. The ticket expects dismax not to raise parse errors for user input.

The cleanup step in [SolrPluginUtils.java lines 658-663](https://github.com/nick-boss-tech/solr/blob/ac9ab33753751a6fef05b5f100f5c1c5683510b2/solr/core/src/java/org/apache/solr/util/SolrPluginUtils.java#L658-L663) removes dangling `+` and `-` signs. It passes boolean words through to the query grammar. The grammar rejects a boolean word that has no term on one side.

## What this change does

**Trailing AND, OR, NOT, && and || are removed. Leading AND, OR, && and || are removed.**

The change is in [`stripIllegalOperators`](https://github.com/nick-boss-tech/solr/blob/ac9ab33753751a6fef05b5f100f5c1c5683510b2/solr/core/src/java/org/apache/solr/util/SolrPluginUtils.java#L658-L663). [`DisMaxQParser`](https://github.com/nick-boss-tech/solr/blob/ac9ab33753751a6fef05b5f100f5c1c5683510b2/solr/core/src/java/org/apache/solr/search/DisMaxQParser.java#L195) calls it on the user query. Operators between two terms stay for the parser, so `ipod AND nano` is unchanged. Lowercase `and` and `or` are plain words.

A leading NOT is kept, because `NOT ipod` is a valid query. A trailing NOT is removed, because `ipod NOT` is not.

Effect: `q=ipod AND` returns the same results as `q=ipod`.

## Proof

**Both test classes fail on the base code and pass with this change.**

- [SolrPluginUtilsTest](https://github.com/nick-boss-tech/solr/blob/ac9ab33753751a6fef05b5f100f5c1c5683510b2/solr/core/src/test/org/apache/solr/util/SolrPluginUtilsTest.java#L99): 10 of 10 pass at head `ac9ab33753751a6fef05b5f100f5c1c5683510b2`. `testStripDanglingBooleanOperators` fails on the base code, and the other nine pass there.
- [DisMaxRequestHandlerTest](https://github.com/nick-boss-tech/solr/blob/ac9ab33753751a6fef05b5f100f5c1c5683510b2/solr/core/src/test/org/apache/solr/DisMaxRequestHandlerTest.java#L193): 4 of 4 pass at the same head. `testDanglingBooleanOperator` fails on the base code.

The base-code run used the new test files on top of the base source. The counts are from a focused run recorded 2026-10-06.

## A choice to check

**Two choices came up. Each has a live alternative.**

1. Drop the word, or search for it as a plain term. This change drops it. Earlier patches on the ticket escaped the words so the parser read them as terms. Cost of dropping: `Portland, OR` becomes `Portland,`, and the OR is lost. Cost of keeping the word: `ipod AND` searches for the word AND and usually matches nothing. Was dropping the right call?
2. A query that is only a boolean word, such as `q=AND`, still returns a parse error, because nothing is left after the cleanup. The other option is to treat it as an empty query, the way dismax already treats blank input. Should an operator-only query be treated as empty?

## Limits

- Operator-only queries, such as `q=AND`, still return a parse error (see the second choice above).
- Only the dismax parser changes. The edismax parser does not call this code.

Changelog: [changelog/unreleased/SOLR-874-dismax-dangling-boolean-operator.yml](https://github.com/nick-boss-tech/solr/blob/ac9ab33753751a6fef05b5f100f5c1c5683510b2/changelog/unreleased/SOLR-874-dismax-dangling-boolean-operator.yml#L1-L8)

### AI assistance

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.
