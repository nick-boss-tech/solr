# SOLR-18368: is the ref-guide defect still on apache main?

**Verdict: STILL AN ISSUE.** All three defects the branch fixes are present on current apache/solr main. None was fixed upstream, so the split result is empty and the branch is not retired.

Reason: `basic-authentication-plugin.adoc` on current main still calls the removed `withInternalClientBuilder` (line 377), still omits `new` on `HttpJettySolrClient.Builder()` (line 375), and still names the removed `Http2SolrClient.Builder` (line 371).

## Inputs

- Current apache/solr main: `d76fc14b9857e77b830d7a82514b28c523a239c2`, committed 2026-10-10 21:13:16 -0400, "SOLR-16396: Replace the GET /config/{component} wildcards with explicit paths (#5097)". Fetched read-only into `upstream/main` in the Solr source checkout.
- Branch live tip `origin/solr-18368-submit`: `a7ec9a1b65c05b2e359e9a186fc2938fe7b7ca7d`. `ls-remote` and the fetch agree, and this matches the receipt.
- Branch base `14c7aac0d15` (2026-10-03 11:38 -0400) is an ancestor of current main, with 72 upstream commits since.
- Branch diff against base: one file, `solr/solr-ref-guide/modules/deployment-guide/pages/basic-authentication-plugin.adoc`, 3 insertions and 3 deletions. This matches the receipt.

## Per-defect evidence on current main

Page: `solr/solr-ref-guide/modules/deployment-guide/pages/basic-authentication-plugin.adoc`

1. **withInternalClientBuilder: PRESENT.** Line 377: `.withInternalClientBuilder(httpClientBuilder).build();`. The method itself is gone from Java. A grep of the whole main tree finds it only in changelog files and this page. The removal is upstream commit `3ef3d093c068` (2026-08-24, "SOLR-18368: remove CloudSolrClient.Builder.withInternalClientBuilder (#4782)"), which is an ancestor of current main. The page was never updated to follow it.
2. **Missing `new`: PRESENT.** Line 375: `var httpClientBuilder = HttpJettySolrClient.Builder().withBasicAuthCredentials(userName, password);`. `HttpJettySolrClient` has only constructors `public Builder()` (line 956) and `public Builder(String)` (line 975). There is no static `Builder()` factory on main, so the bare call does not resolve.
3. **Http2SolrClient.Builder sentence: PRESENT.** Line 371: "CloudSolrClient's Builder supports receiving an `Http2SolrClient.Builder` instance ...". There is no `Http2SolrClient` class under `solr/solrj/src/java` on main. Only `CloudHttp2SolrClient` exists, plus a comment in `LBAsyncSolrClient.java:39`.

Upstream history of the page:

- No upstream commit after the base has touched the page. The page is byte-identical between base and current main (`diff` of the page path between `14c7aac0d15` and `upstream/main` is empty).
- The last commit touching the page on main is `91cbb6a343e` (2026-10-02, SOLR-18469). It is older than the base and the receipt.
- So there is no partial upstream fix. The verdict shape is a clean STILL AN ISSUE, not a split.

## Branch against current main

`diff upstream/main origin/solr-18368-submit -- <page>` has exactly three hunks, and each one targets one defect:

- Line 371: "an `Http2SolrClient.Builder` instance" becomes "an HTTP SolrClient builder".
- Line 375: `HttpJettySolrClient.Builder()` becomes `new HttpJettySolrClient.Builder()`.
- Line 377: `withInternalClientBuilder(...)` becomes `withHttpClientBuilder(...)`.

The branch changes nothing else in the page. The three hunks apply cleanly to current main because the page is unchanged since base. The branch's changes are confined to this one file.

Reading check of the corrected example against main's API (read only, not compiled):

- `new HttpJettySolrClient.Builder()` is the public no-arg constructor (line 956).
- `withBasicAuthCredentials` is defined on `HttpSolrClient.BuilderBase` (line 526) and returns `B`, which is the Jetty builder type.
- `CloudSolrClient.Builder.withHttpClientBuilder(HttpSolrClient.BuilderBase<?, ?>)` exists at `solr/solrj/src/java/org/apache/solr/client/solrj/impl/CloudSolrClient.java:1550`.

The corrected snippet therefore uses APIs that exist on main. The verdict does not depend on a compile, so no run spec was appended to the assignment.

## Other mentions of the removed names in the ref guide

- `withInternalClientBuilder`: only the page, line 377.
- `Http2SolrClient.Builder` (dotted form): the page, line 371 (defect 3). Also `modules/upgrade-notes/pages/major-changes-in-solr-9.adoc:163`, a version-scoped 9.x note ("If Http2SolrClient.Builder.withHttpClient is used ..."). This is historical and is not one of the three defects. The branch does not touch it.
- Bare `Http2SolrClient`: `modules/configuration-guide/pages/solr-properties.adoc:107` describes a current property ("If Http2SolrClient should support cookies"). That wording is stale and is outside the page, the receipt, and the branch. The owner may want it in scope for the PR. The upgrade notes for 8, 9 and 10 mention the name historically, and `major-changes-in-solr-10.adoc:179` records the rename to `HttpJettySolrClient`.

## Consequence

The issue still exists on a ticket Jira shows resolved, so under Nick's rule a PR or a new ticket is the main side's call. The branch's three hunks still apply to current main, so the branch is not on the retire pile. The owner call recorded in the receipt (whether a PR opens, and under which key) is unchanged.

## Method and limits

- Read only. Used `ls-remote`, `fetch upstream main:refs/remotes/upstream/main`, `fetch origin solr-18368-submit:refs/remotes/origin/solr-18368-submit`, then `log`, `grep`, `show`, `diff`, `merge-base`, and `rev-parse` in the Solr source checkout. No branch edit, no PR, no Jira or GitHub write, no build or test.
- Not checked: whether `branch_10x` carries the same page fix or removal. Not needed for this verdict.
- Left uncommitted in the worktree: this report and the claim DONE mark. The caller commits. Nothing was pushed from the worktree.
