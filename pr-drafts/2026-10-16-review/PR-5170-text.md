# PR #5170 draft: SOLR-18368 key in the title (review item 7)

- Written against head: 15dd2aa74b08044cb919a079e7c87044923e9934 (live branch solr-18368-submit, read through the API 2026-10-17).
- Visible characters: 2,716 (body; markdown syntax and link URLs removed). Limit 3,500.
- Overlap check: the round-1 SOLR-18368 fix (assignments/pool-vm1-solr-18368-review-fix-round-1.md, DONE at 15dd2aa74b) changes only the prose line in basic-authentication-plugin.adoc. Its Direction says the title's "so it compiles" wording is a main-side call. It does not change the title, so this item is drafted, not skipped.
- Body edit: the first body sentence says "This PR carries no ticket of its own." With the key in the title, that sentence is now wrong, so it is reworded below.
- Not applied to the live PR. Drafts only, per the assignment.

=== TITLE ===

SOLR-18368: Fix stale names in the Basic Authentication Plugin example

=== BODY ===

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-18368

The ticket key is in the title. This PR covers the part of SOLR-18368 that still exists on main, and the commits carry that key.

## What happens today

**The example on the Basic Authentication Plugin page cannot compile against main.**

Three lines of the page use a name or syntax that main does not have. These links point to the base commit, where the page is the same as on main:

- The sentence above the example names `Http2SolrClient.Builder` ([line 371](https://github.com/apache/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/solr-ref-guide/modules/deployment-guide/pages/basic-authentication-plugin.adoc#L371), base). Main has no class by that name. The client is named `HttpJettySolrClient`, and [a source comment](https://github.com/nick-boss-tech/solr/blob/15dd2aa74b08044cb919a079e7c87044923e9934/solr/solrj-jetty/src/java/org/apache/solr/client/solrj/jetty/HttpJettySolrClient.java#L96) records the old name.
- [Line 375](https://github.com/apache/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/solr-ref-guide/modules/deployment-guide/pages/basic-authentication-plugin.adoc#L375), base, calls `HttpJettySolrClient.Builder()` without `new`. `Builder` is a [nested class with a public constructor](https://github.com/nick-boss-tech/solr/blob/15dd2aa74b08044cb919a079e7c87044923e9934/solr/solrj-jetty/src/java/org/apache/solr/client/solrj/jetty/HttpJettySolrClient.java#L956), and no method of that name exists.
- [Line 377](https://github.com/apache/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/solr-ref-guide/modules/deployment-guide/pages/basic-authentication-plugin.adoc#L377), base, calls `withInternalClientBuilder`. [#4782](https://github.com/apache/solr/pull/4782) removed that method from `CloudSolrClient.Builder` ([commit 3ef3d093c06](https://github.com/apache/solr/commit/3ef3d093c068b46431b6c7190d4706f5f599eaf7)), and that commit is already in the base. Apart from changelog entries, the page is the only place that still uses the name.

## What this change does

**The branch corrects those three lines and changes nothing else in the page or the code.**

- The sentence now names an [`HttpSolrClient.BuilderBase`](https://github.com/nick-boss-tech/solr/blob/15dd2aa74b08044cb919a079e7c87044923e9934/solr/solrj/src/java/org/apache/solr/client/solrj/impl/HttpSolrClient.java#L438) instance, the type that `withHttpClientBuilder` accepts ([CloudSolrClient.java, line 1550](https://github.com/nick-boss-tech/solr/blob/15dd2aa74b08044cb919a079e7c87044923e9934/solr/solrj/src/java/org/apache/solr/client/solrj/impl/CloudSolrClient.java#L1550)).
- The `httpClientBuilder` line adds `new`, as the [public constructor](https://github.com/nick-boss-tech/solr/blob/15dd2aa74b08044cb919a079e7c87044923e9934/solr/solrj-jetty/src/java/org/apache/solr/client/solrj/jetty/HttpJettySolrClient.java#L956) requires. The chained [`withBasicAuthCredentials`](https://github.com/nick-boss-tech/solr/blob/15dd2aa74b08044cb919a079e7c87044923e9934/solr/solrj/src/java/org/apache/solr/client/solrj/impl/HttpSolrClient.java#L526) returns that builder.
- The last line calls `withHttpClientBuilder`. The example's constructor call, [`new CloudSolrClient.Builder(zkHostList, chroot)`](https://github.com/nick-boss-tech/solr/blob/15dd2aa74b08044cb919a079e7c87044923e9934/solr/solrj/src/java/org/apache/solr/client/solrj/impl/CloudSolrClient.java#L1398), already matches main.

## Proof

**This change has no test. It is a documentation fix, checked by building the Ref Guide page.**

At the head, [15dd2aa74b](https://github.com/nick-boss-tech/solr/commit/15dd2aa74b08044cb919a079e7c87044923e9934), the Ref Guide site build (`buildLocalSite`) finished with BUILD SUCCESSFUL on 2026-10-16, and the generated page carries the corrected example. A snippet that mirrors the corrected example also compiled against the built `solrj` and `solrj-jetty` client classes; that check ran at commit 1acaced80b2, and the example's code lines are unchanged since.

## Limits

- No test covers the example text, so a later rename could leave the page wrong again.
- The example does not declare `zkHostList` or `chroot`. The [constructor it calls](https://github.com/nick-boss-tech/solr/blob/15dd2aa74b08044cb919a079e7c87044923e9934/solr/solrj/src/java/org/apache/solr/client/solrj/impl/CloudSolrClient.java#L1398) takes the chroot as an `Optional<String>`. This change does not touch those lines.
- No one-line upgrade note is added about the removal, for users who hit the old compile error.

Changelog: none. This branch has no changelog fragment. The removal's own entry is already in `changelog/unreleased` on main.

### AI assistance

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.
