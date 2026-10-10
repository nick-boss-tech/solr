# Security and authentication round 1, part S2: SOLR-18368 (salvage draft, waiting on the owner's call)

## Result

- **Verdict:** salvage draft, waiting on the owner's call (whether a PR opens, and under which key).
- **Draft:** `pr-drafts/security/SOLR-18368.md` (new file; the `pr-drafts/security` directory did not exist and was created). Count: 5,574 characters by `LC_ALL=C.UTF-8 wc -m`. That `wc` counts each 4-byte emoji as 2, so the code-point count is 5,572 and the byte count is 5,578. Prose with link URLs removed: 3,380 characters.
- **Head:** `git ls-remote origin refs/heads/solr-18368-submit` returns `a7ec9a1b65c05b2e359e9a186fc2938fe7b7ca7d`, matching the expected `a7ec9a1b65c`. The local ref matches.
- **Branch diff** against the merge-base `14c7aac0d151402b00259e2fb9bf5eed7049ec5d`: one file, the ref-guide page `basic-authentication-plugin.adoc`, 3 insertions and 3 deletions. The pin test is not in the base-to-head diff, so it is absent at the head.
- **Base note:** the assignment names `8e62c2686882` as the merge-base. That is the `upstream/main` tip, not the merge-base. The merge-base is `14c7aac0d151`, which matches the receipt. This part used `14c7aac0d151`.
- **Upstream moved during the run.** `git fetch upstream refs/heads/main` updated the shared ref `upstream/main` from `8e62c2686882` to `3f5d4c5bf8ac` (four commits, none touching the page or the client sources). Both states show the same page lines and signatures below. No commit, push or post.

## The three claims against main

Base `14c7aac0d151` and live main `3f5d4c5bf8ac`.

**(a) The example calls a removed builder method: confirmed.** No `withInternalClientBuilder` remains under `solr/` Java on main. The removal is commit `3ef3d093c068` ("SOLR-18368: remove CloudSolrClient.Builder.withInternalClientBuilder (#4782)"), an ancestor of the base. The replacement is `solr/solrj/src/java/org/apache/solr/client/solrj/impl/CloudSolrClient.java` line 1550, `public Builder withHttpClientBuilder(HttpSolrClient.BuilderBase<?, ?> ...)`. The page shows the claim at base line 377: `.withInternalClientBuilder(httpClientBuilder).build();`. The page is the only non-changelog user of the name.

**(b) The example constructs `HttpJettySolrClient.Builder` without `new`: confirmed.** `solr/solrj-jetty/src/java/org/apache/solr/client/solrj/jetty/HttpJettySolrClient.java` line 944 is `public static class Builder extends BuilderBase<Builder, HttpJettySolrClient>`, with `public Builder()` at line 956. Neither `HttpJettySolrClient.java` nor `HttpSolrClient.java` declares a method named `Builder`. The page shows the claim at base line 375: `var httpClientBuilder = HttpJettySolrClient.Builder().withBasicAuthCredentials(userName, password);`.

**(c) The sentence names a builder class that no longer exists: confirmed.** No `Http2SolrClient` class or file exists on main. The only Java mentions are comments, for example `HttpJettySolrClient.java` line 96, "formerly known at Http2SolrClient". The page shows the claim at base line 371: "CloudSolrClient's Builder supports receiving an `Http2SolrClient.Builder` instance...". At the head, line 371 reads "an HTTP SolrClient builder".

## The corrected example against main signatures

- Line 375, `new HttpJettySolrClient.Builder()`: `HttpJettySolrClient.java` line 956 (public no-argument constructor). Matches.
- `.withBasicAuthCredentials(userName, password)`: `solr/solrj/src/java/org/apache/solr/client/solrj/impl/HttpSolrClient.java` line 526, `public B withBasicAuthCredentials(String user, String pass)` in `BuilderBase` (line 438). `B` is `HttpJettySolrClient.Builder`, so the chain returns the builder. Matches.
- Line 376, `new CloudSolrClient.Builder(zkHostList, chroot)`: `CloudSolrClient.java` line 1398, `public Builder(List<String> zkHosts, Optional<String> zkChroot)`. The example does not declare `zkHostList` or `chroot`, and `chroot` must be `Optional<String>`. The branch does not change those lines, so this is a pre-existing gap, named in the draft's Limits.
- Line 377, `.withHttpClientBuilder(httpClientBuilder).build()`: `CloudSolrClient.java` line 1550 (accepts `HttpSolrClient.BuilderBase<?, ?>`, which `HttpJettySolrClient.Builder` extends) and `CloudSolrClient.java` line 1582, `public CloudHttp2SolrClient build()`. Matches.
- The client source under `solr/solrj` and `solr/solrj-jetty` is unchanged from base to live main (the diff is empty over `solr/solrj/src/java` and `solr/solrj-jetty/src/java`). The three cited Java files are the same blobs on base, head and main.

## Verification statement (as the draft writes it)

- **Receipt:** a snippet mirroring the corrected example compiled clean against the built solrj and solrj-jetty client classes, and the Ref Guide page build (`buildLocalSite`) finished with BUILD SUCCESSFUL on 2026-10-04. Both are at tree `1acaced80b2eb8ace290ce12fcefffed5e4b359f`. The draft says this, names the tree, dates only the page build (the receipt gives no separate date for the snippet), and says it is not a test run.
- **Still true at the live tip, by construction and not re-run:** `git diff 1acaced80b2 a7ec9a1b65c0` touches only `CloudHttp2SolrClientMultiConstructorTest.java` (22 deletions). The page is byte-identical in both. The client sources are unchanged, so the compiled classes and the page content still describe the head.

## Owner options

- **(a)** Open the docs fix with no key. Cite `#4782` and commit `3ef3d093c068` in the body (the draft already does). Under (a), delete the Jira line.
- **(b)** Open as a follow-up reference to SOLR-18368. Keep the Jira line.
- **(c)** Do not open. Fold the three lines into the next docs pass.

The one-line upgrade note is a second option for the same list. It is not in the branch. The upgrade notes on main do not mention the removal. The draft names it in Limits and offers it on request.

**Recommendation: (a).** The removal is already on main (#4782, before the base), and the remaining defect is only in the page. A docs-only PR that cites #4782 needs no key to be understood. A follow-up reference adds a link to a closed ticket without adding anything a reviewer needs. The receipt's reasoning does not point to (b) or (c).

## Self-check

- Em and en dashes in the draft: zero. The report has none.
- Process words in the draft: none. The only match for "drafting" is in the AI footer, which the formula template requires. No gate, receipt, round, owed, salvage, claim, audit or tree.
- Header: the formula template's header line is kept as written, including its two emoji. This report uses none.
- Head references: the full head SHA `a7ec9a1b65c05b2e359e9a186fc2938fe7b7ca7d` appears nine times, in fork blob and commit links. The short form `a7ec9a1b65c0` appears once in prose. The gated tree `1acaced80b2` is named in full in its link.
- Citations: head citations are fork blob links at the full head SHA. Base citations are `apache/solr` blob links at `14c7aac0d151`, labeled "base". The removal is cited by its `apache/solr` commit and PR links.
- Worktree: the only change is the new `pr-drafts/security/` directory with `SOLR-18368.md` (untracked).

## Receipt disagreements (exact wording)

1. **Receipt line 5:** "Gate log: `g18368-gate.log` (gate finished 2026-10-04): ... `CloudHttp2SolrClientMultiConstructorTest` 3 of 3". The file is not on disk. A name search of `C:\Users\shaninna\dev` found nothing. The only on-disk SOLR-18368 result is `research/test-queue/results/SOLR-18368.json`: SUCCESS, finished 2026-08-19, one test (`testWithInternalClientBuilderRemoved`), an earlier run. The "3 of 3" count is not confirmed, and that earlier run was not treated as gate evidence.
2. **Receipt line 7:** "commit `3ef3d093c06` on main, and the same removal on `branch_10x`". Main is confirmed. On `upstream/branch_10x` the same subject is commit `62886913954`, not `3ef3d093c06`. The content matches (no `withInternalClientBuilder` in its solrj source), but it is a different commit.
3. **Receipt line 7:** "a CloudSolrClient retry-gating hunk ... is superseded by SOLR-18341." SOLR-18341 has no commit on `upstream/main`. Its work is on the fork branch `origin/solr-18341-submit` (`458b098719d`, "SOLR-18341: apply spotless formatting to the retry tests"). The docs-only tip does not carry the hunk (confirmed). "Superseded" holds on the fork only.
4. **Receipt line 7:** "Jira checked 2026-10-05: RESOLVED/Fixed". The local snapshot `research/jira-context/SOLR-18368.json` says "Status": "Open", Updated 2026-08-18. JIRA was not called. The draft does not state the ticket status.

## Owner decisions

1. Whether a PR opens, and under (a), (b) or (c). Recommendation: (a).
2. Whether to add the one-line upgrade note, in this PR or a follow-up.
3. If (b) is chosen, confirm the SOLR-18368 status in JIRA first. The receipt says Resolved and Fixed; the local snapshot says Open.
4. Whether to re-run the snippet compile and the page build at `a7ec9a1b65c0` before posting. The receipt's checks are at `1acaced80b2`; by construction they still apply.
5. Whether to keep the `zkHostList` and `chroot` line in Limits, or change the example to declare them. That is a small edit outside the three lines, so it is a scope call.

## Not checked

- Live JIRA: not called, per the rules. The status rests on the receipt and the local snapshot, which disagree.
- The receipt's gate log `g18368-gate.log` and its "3 of 3" count: not found on disk (see disagreement 1).
- No build, compile, Gradle run or test. The compile and build statements come from the receipt, and the draft says so.
- Whether the receipt's mirror snippet declared `zkHostList` and `chroot`: the receipt is silent.
- The assignment's interaction check (whether the page contradicts SOLR-12161's test or SOLR-10627's permission behavior): outside this part's task, not checked.
- SOLR-18341's content: not verified beyond its existence on the fork.
- The main-side takeover log entries (2026-10-05) named by the receipt: not in the worktree, not checked.
- Links were not opened in a browser. They were built from SHAs confirmed in the local clone and on origin.
