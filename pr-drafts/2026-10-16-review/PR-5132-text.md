# PR 5132 (SOLR-6207): body draft, round 2 item 3

- Head: 05cc2776d0c126e77854915f21f35b7854cf9213 (live body read 2026-10-18T04:33Z; branch head matches).
- Visible characters: 3480 (limit 3,500).
- Proof numbers come only from receipts/SOLR-6207.md: SolrQueryRequestBaseTest 1 of 1 at b099a9f1be5a (gate 2026-10-07); the premise run with production reverted to base also passes 1 of 1; the head 05cc2776d0c1 was checked 2026-10-15 for changelog parse, tidy and Error Prone compile only.
- The test is a documentation pin, and the draft says so in one sentence. The corroboration run cited in the receipt is not added.
- Changes from the stored body: trimmed under 3,500 visible characters (two link sentences shortened, one sentence dropped); the Proof section now states the receipt's counts and checks by head.

---

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-6207

## What happens today

**After `setParams` is called, `getParamString()` still shows the original request parameters.**

[`SolrQueryRequestBase.getParamString()`](https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/core/src/java/org/apache/solr/request/SolrQueryRequestBase.java#L171-L174) returns `origParams`. [`setParams`](https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/core/src/java/org/apache/solr/request/SolrQueryRequestBase.java#L92-L95) replaces only the current parameters. A caller that reads a new `q` through `getParams()` gets the new value. A log line built from `getParamString()` still shows the old one. The request log uses this string ([SolrCore.java line 2927](https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/core/src/java/org/apache/solr/core/SolrCore.java#L2927)). The interface javadoc says only that the string shows "all the important parameters," so it does not say which set it shows. Links point at the base commit, before this change.

## What this change does

**The javadoc now states what the method returns and what it leaves out.**

The change is to the interface javadoc of [`SolrQueryRequest.getParamString()`](https://github.com/nick-boss-tech/solr/blob/05cc2776d0c126e77854915f21f35b7854cf9213/solr/core/src/java/org/apache/solr/request/SolrQueryRequest.java#L128-L136). The only production edit is javadoc. On the request-handler path, defaults, appends and invariants are merged in with `setParams` ([RequestUtil.java line 182 at the merge-base](https://github.com/nick-boss-tech/solr/blob/cabedd1d968059215188f4e7563fb303241899ed/solr/core/src/java/org/apache/solr/request/json/RequestUtil.java#L182)), so they are not part of the original parameters. The javadoc now scopes that to requests built from HTTP request parameters. It names a cross-core join sub-request, which is built from already merged parameters, as the exception.

## Proof

**This is a documentation pin. It records existing behavior, so it does not fail on the base code, and it is not meant to.**

[SolrQueryRequestBaseTest](https://github.com/nick-boss-tech/solr/blob/05cc2776d0c126e77854915f21f35b7854cf9213/solr/core/src/test/org/apache/solr/request/SolrQueryRequestBaseTest.java#L25-L39) checks that `getParamString()` still returns the original parameters after `setParams`. The ticket treats that behavior as intended. The test also passes with the production edit reverted to base, because that edit is javadoc only.

At `b099a9f1be5a3fb529767ca625f42e7860e62f13`, SolrQueryRequestBaseTest passes 1 of 1 (gate, 2026-10-07, fresh JUnit XML). The current head, `05cc2776d0c`, changes only javadoc and changelog text: no code and no test logic, so that result carries to this head. At this head the changelog fragment parses, the formatting check leaves the tree unchanged, and the core main and test sources compile with the error checks CI uses (checked 2026-10-15).

## A choice to check

**The change documents the method and keeps it. It does not deprecate it.**

The options are to document what the method returns, as this change does, or to deprecate the method. Chris M. Hostetter's 2014 comment on the ticket says the behavior is intentional, and that "we should probably make the javadocs more clear." He warns that reading `params` instead of `origParams` would make logging "ridiculously absurd." The same comment suggests deprecating and removing the method, because readers must check the javadoc to learn which parameters it returns. Deprecation would change the API for code that calls the method. Was documenting the method the right call, or should it be deprecated?

## Limits

**The javadoc covers requests built from HTTP parameters, and the method stays as it is.**

- The javadoc describes requests built from HTTP request parameters. A cross-core join sub-request in `JoinQParserPlugin` is built from merged parameters, so it can show defaults, and the javadoc says so. Other code that builds a request from merged parameters was not checked.
- The method is not deprecated or removed. See the choice above.

Changelog: [changelog/unreleased/SOLR-6207-getparamstring-javadoc.yml](https://github.com/nick-boss-tech/solr/blob/05cc2776d0c126e77854915f21f35b7854cf9213/changelog/unreleased/SOLR-6207-getparamstring-javadoc.yml)

### AI assistance

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.
