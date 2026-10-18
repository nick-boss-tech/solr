# PR draft: highlight query terms inside any function query that wraps a query (for Nick to approve; not opened)

Status: DRAFT. Not opened. Opening needs Nick's approval and the ticket decision in ticket-draft.md. Replace SOLR-XXXXX with the ticket key.
Head: solr-2681-nested-submit at cec42ef4f0 (gate: receipts/SOLR-2681-nested.md once the gate closes). Base: apache/solr main c8ab71430f.
Relation: separate from #5104. This PR does not change #5104.

Title: SOLR-XXXXX: Highlight query terms inside any function query that wraps a query

---

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-XXXXX

## What happens today

**Terms inside a nested function query are not highlighted.**

With a dismax query in `$v1`, the request `{!func}product($v1,$v2)` highlights nothing. The highlighter's extractor passes a `FunctionQuery` to Lucene, and Lucene does not look inside it. The same happens for `sub(query($v1),1)` and `product(sum(query($v1),0),$v2)`. The extractor is in [DefaultSolrHighlighter.java at c8ab71430f](https://github.com/apache/solr/blob/c8ab71430f/solr/core/src/java/org/apache/solr/highlight/DefaultSolrHighlighter.java#L297-L298).

## What this change does

**It walks the function tree and extracts the queries it finds.**

The extractor now collects the `QueryValueSource` queries under any `MultiFloatFunction` or `DualFloatFunction`, recursively, and extracts their terms. The walk is in [DefaultSolrHighlighter.java](https://github.com/nick-boss-tech/solr/blob/cec42ef4f061ff7b81a4610a91ee76aa83b806ff/solr/core/src/java/org/apache/solr/highlight/DefaultSolrHighlighter.java#L304-L351). The new test is in [HighlighterTest.java](https://github.com/nick-boss-tech/solr/blob/cec42ef4f061ff7b81a4610a91ee76aa83b806ff/solr/core/src/test/org/apache/solr/highlight/HighlighterTest.java#L91-L141).

Behavior change: a function query that wraps a query now highlights that query's terms. Before, it highlighted nothing.

## Proof

**The new test fails on main and passes with the change.**

- Base (apache/solr main c8ab71430f with the new test): `testHighlightQueryInsideNestedFunctions` fails at the `product` assertion, `REQUEST FAILED`, expecting `<em>lorem</em> ipsum`. Run on 2026-10-18, JDK 21, seed 2681C0FFEE2681. The fail-before rerun is in the receipt.
- Head cec42ef4f0, 2026-10-18: `HighlighterTest` tests=36, failures=0, errors=0, skipped=0. Tidy clean; Error Prone compile passes; `:solr:core:check -x test` result in the receipt.

## A choice to check

**The walk reads two protected Lucene fields through reflection.**

`MultiFloatFunction.sources` and `DualFloatFunction.a` and `.b` are protected, so the highlighter cannot read them directly. The change reads them through `VarHandle`s opened with `MethodHandles.privateLookupIn`. The other route is a public accessor in Lucene, which needs a Lucene change first.

Is the reflection acceptable here, or should the change wait for a Lucene accessor?

## Limits

**Only Lucene's product, sum and sub style functions are walked.**

- Other value sources that wrap a query are not walked. Examples are `scale`, `if`, `def`, and Solr-specific functions.
- The FastVector highlighter is not changed and not tested.
- #5104 covers only the top-level `query(...)` form. This PR is separate.

Changelog: [changelog/unreleased/SOLR-2681-highlight-nested-functions.yml](https://github.com/nick-boss-tech/solr/blob/cec42ef4f061ff7b81a4610a91ee76aa83b806ff/changelog/unreleased/SOLR-2681-highlight-nested-functions.yml) (type: fixed).

### AI assistance

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.
