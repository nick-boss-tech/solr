# Security and build-docs draft fidelity, slice 3

Assignment: `assignments/pool-draft-fidelity-cli-security-builddocs-metrics.md`. Claim: `claims/pool-draft-fidelity-cli-security-builddocs-metrics.md`, slice 3 (Security SOLR-18368; Build-docs SOLR-3684, SOLR-5821, SOLR-16914).

Drafts: `pr-drafts/security/SOLR-18368.md`, `pr-drafts/build-docs/SOLR-3684.md`, `SOLR-5821.md`, `SOLR-16914.md`. Receipts: `receipts/<ticket>.md`. Round reports: `reports/security-round-1.md` and `-s2.md`; `reports/build-docs-misc-round-1.md` and `-g1.md`, `-g2.md`. A grep of `reports/` for the four numbers also hits `-g4.md` (an overlap note for SOLR-16914 only); `-g3`, `-g5`, `-g6` do not name them. A grep of `material/` for the four numbers found no answers material.

Heads checked (`git ls-remote origin refs/heads/<branch>`, run during this slice):
- `solr-18368-submit`: `a7ec9a1b65c05b2e359e9a186fc2938fe7b7ca7d` (draft names `a7ec9a1b65c0`). Match.
- `solr-3684-submit`: `663b8ce754b69fd81dcd1b5c1abe7e94ca8e6faa` (draft names `663b8ce754b6`). Match.
- `solr-5821-submit`: `b8af2d1ce2f5f963250feafccd9fc698ae268289` (draft names `b8af2d1ce2f5`). Match.
- `solr-16914-submit`: `cd878023d3d0e351700f5365f692423fbe97d972` (draft names `cd878023d3d0`). Match.

Also confirmed present in the worktree object store (`git cat-file -t`): base `14c7aac0d151`, removal commit `3ef3d093c068`, SOLR-18368 tree `1acaced80b2`.

## Verdicts

| Draft | Head checked | Verdict |
|---|---|---|
| SOLR-18368 | a7ec9a1b65c0 (match) | DRIFT (1 item) |
| SOLR-3684 | 663b8ce754b6 (match) | DRIFT (2 items) |
| SOLR-5821 | b8af2d1ce2f5 (match) | DRIFT (2 items) |
| SOLR-16914 | cd878023d3d0 (match) | DRIFT (3 items) |

## SOLR-18368

Verdict: DRIFT (1 item).

1. Draft says: "**The example on the Basic Authentication Plugin page cannot compile against main.**" (the opening of "What happens today"). The ticket's position appears only in bullet 2 of that section, so the opening does not state it.
   - Evidence: `receipts/SOLR-18368.md` line 7: the ticket is RESOLVED/Fixed, and the removal landed upstream as `3ef3d093c06` (#4782). Checked: `3ef3d093c068` is an ancestor of base `14c7aac0d151`, and `changelog/unreleased/SOLR-18368-remove-withinternalclientbuilder.yml` exists on upstream/main. `reports/security-round-1-s2.md` disagreement 4 says the draft does not state the ticket status.
   - Replacement (replaces the bold line under "## What happens today"): "**The removal that SOLR-18368 asks for is already on main, in [#4782](https://github.com/apache/solr/pull/4782). The Basic Authentication Plugin example still calls that removed method, so it cannot compile against main.**"

Verified, no drift:
- Citations, each read at the SHA the draft names: base `14c7aac0d151` lines 371 (names `Http2SolrClient.Builder`), 375 (`HttpJettySolrClient.Builder()` without `new`), 377 (`withInternalClientBuilder`). The page is unchanged between base and upstream/main. Head `a7ec9a1b65c0`: `HttpJettySolrClient.java` 96 (comment "formerly known at Http2SolrClient"), 956 (public `Builder()`); `CloudSolrClient.java` 1398, 1550; `HttpSolrClient.java` 438, 526. Each holds what the text says. The client source is unchanged on base, head and upstream/main.
- Proof: `buildLocalSite` BUILD SUCCESSFUL dated 2026-10-04 and the snippet compile match `receipts/SOLR-18368.md` line 8. The head differs from `1acaced80b2` by one test file (22 deletions) and the page is unchanged. The draft cites no count, so the receipt's unconfirmed "3 of 3" does not reach it.
- Limits (zkHostList and chroot, the upgrade note offered on request) match `reports/security-round-1.md`. No choice section, and none is needed.
- Changelog "none" with the removal's own entry on main: verified.
- No em dashes. No internal vocabulary in the body.

Optional notes, not blocking:
- Line 1 is a bracketed WAITING note that names owner options. Delete it once the owner decides. Option (a) also deletes the Jira line.
- Jira status conflict: the receipt says RESOLVED/Fixed (checked 2026-10-05); `research/jira-context/SOLR-18368.json` says Open (updated 2026-08-18). JIRA was not called. The replacement above avoids a status word. Confirm the status before option (b).
- The snippet compile has no date of its own in the receipt. The `buildLocalSite` date is present.
- No PR title line (the build-docs drafts have "Title:"). Supply one when a PR opens.

## SOLR-3684

Verdict: DRIFT (2 items).

1. Draft says: "Title: Add JettyConfig.withMaxThreads for the test framework's embedded Jetty pool"
   - Evidence: `changelog/unreleased/SOLR-3684.yml` at `663b8ce754b6` has the title `SOLR-3684: JettyConfig gains a withMaxThreads option for embedded Jetty thread pools`. Brief check 5 requires the draft title to match the branch fragment.
   - Replacement: "Title: SOLR-3684: JettyConfig gains a withMaxThreads option for embedded Jetty thread pools"
   - If the owner wants the wording in `reports/build-docs-misc-round-1-g1.md` (drop the prefix, say the default is unchanged), the branch fragment must change first, in a branch commit by the branch owner. The title then follows it.

2. Draft says: "JettySolrRunner always uses the fixed constant `THREAD_POOL_MAX_THREADS = 10000` ([JettySolrRunner.java#L207](https://github.com/nick-boss-tech/solr/blob/663b8ce754b69fd81dcd1b5c1abe7e94ca8e6faa/solr/test-framework/src/java/org/apache/solr/embedded/JettySolrRunner.java#L207)), and JettyConfig has no setting for it."
   - Evidence: at `663b8ce` line 207 reads `qtp.setMaxThreads(config.maxThreads != null ? config.maxThreads : THREAD_POOL_MAX_THREADS);`. That is code the change produces, so it cannot illustrate "always". At base `14c7aac0d151` line 207 reads `qtp.setMaxThreads(THREAD_POOL_MAX_THREADS);`. `pr-formula.md` requires the pre-change symptom to link the merge-base and say so.
   - Replacement: "JettySolrRunner always uses the fixed constant `THREAD_POOL_MAX_THREADS = 10000` ([JettySolrRunner.java, line 207 at the base commit](https://github.com/apache/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/test-framework/src/java/org/apache/solr/embedded/JettySolrRunner.java#L207)), and JettyConfig has no setting for it."

Verified, no drift:
- Head `663b8ce754b6` matches the live tip. Shipped diff base to head: 4 files (changelog, `JettyConfig.java`, `JettySolrRunner.java`, `TestJettySolrRunner.java`), +54 -1. `jetty.xml` is not touched.
- Proof: TestJettySolrRunner 4 of 4, tidy, Error Prone compile and module check, dated 2026-10-03 (`receipts/SOLR-3684.md` lines 5-6). The test names and lines 34-63 match the head file. The inconclusive-by-construction statement matches receipt line 7.
- `JettyConfig.java` 110-113 (`withMaxThreads`) at head. `JettySolrRunner.java` 207 at head. `getConfiguredMaxThreads` at 718-721 is package-private.
- `jetty.xml` line 34 (`solr.jetty.threads.max`, default 10000) is the same at base and head.
- The changelog link resolves at head.
- Choice: a real live alternative (a lower default). The "Jetty's own default" and Robert Muir (2012-08-07) wording match `research/jira-context/SOLR-3684.json`. The draft's "probably ... to prevent a deadlock" is hedged, as the ticket is.
- Limits match `reports/build-docs-misc-round-1-g1.md`. The per-field analyzer reuse is in the ticket text.

Optional notes, not blocking:
- The bracketed HOLD line 1 names the round 12 review, owner decisions and the design record. Delete it before posting.
- The Choice wording is the review's wording. `design-decisions-open.md` was not found (`reports/build-docs-misc-round-1-g1.md` item 4). Paste the exact record text before posting, as the hold note says.
- "about 1000 indexing threads" and "full GC" are attributed to the ticket. `research/jira-context/SOLR-3684.json` supports both. The receipt is silent. This is not a Proof number.
- Date: the Proof date 2026-10-03 matches `receipts/SOLR-3684.md` line 5. The head commit is dated 2026-10-04 01:24 +0000 (`git log`). This is probably local time. Confirm before posting, as the round report asks.
- `g3684-harden2.log` is not on disk.

## SOLR-5821

Verdict: DRIFT (2 items).

1. Draft says: "The existing advice to add a unique sort field stays. See [common-query-parameters.adoc lines 74-76](https://github.com/nick-boss-tech/solr/blob/b8af2d1ce2f5f963250feafccd9fc698ae268289/solr/solr-ref-guide/modules/query-guide/pages/common-query-parameters.adoc#L74-L76)."
   - Evidence: at `b8af2d1ce2f5`, lines 74-76 are the three new sentences. The unique-sort advice ("Users looking to avoid this behavior can define an additional sort criteria on a unique or rarely-shared field such as `id`...") is line 77. The cited range misses it.
   - Replacement: "The existing advice to add a unique sort field stays. See [common-query-parameters.adoc line 77](https://github.com/nick-boss-tech/solr/blob/b8af2d1ce2f5f963250feafccd9fc698ae268289/solr/solr-ref-guide/modules/query-guide/pages/common-query-parameters.adoc#L77)."

2. Draft says: "The [tie-breaker paragraph](https://github.com/nick-boss-tech/solr/blob/b8af2d1ce2f5f963250feafccd9fc698ae268289/solr/solr-ref-guide/modules/query-guide/pages/common-query-parameters.adoc#L72-L73) explains the internal ID but does not say that replicas can disagree, or what that does to paging."
   - Evidence: the tie-breaker paragraph is the pre-change text in "What happens today". `pr-formula.md` requires a pre-change symptom to link the merge-base and say so. Lines 72-73 are unchanged by the branch, so only the link and label change.
   - Replacement: "The [tie-breaker paragraph](https://github.com/apache/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/solr-ref-guide/modules/query-guide/pages/common-query-parameters.adoc#L72-L73), at the base commit, explains the internal ID but does not say that replicas can disagree, or what that does to paging."

Verified, no drift:
- Head `b8af2d1ce2f5` matches the live tip. One file, three insertions (`git diff` base to head). The three sentences match the draft's "What this change does" items 1 to 3.
- Proof: Antora site build with no errors or warnings, the rendered page contains the sentences, dated 2026-10-03 (`receipts/SOLR-5821.md` lines 5-6, 9).
- Pagination lines 121-126 and `solrcloud-update-consistency.adoc` line 73 at head say what the draft says.
- Limits match `reports/build-docs-misc-round-1-g2.md`: document counts (the Jira comment "Smells like SOLR-4260", `research/jira-context/SOLR-5821.json`), per-replica term statistics, and the base-tree build. `common-query-parameters.adoc` has not changed on upstream/main since the base.
- Changelog "none": consistent with the round position.
- No em dashes. No internal vocabulary.

Optional notes, not blocking:
- SOLR-4260 is named without a link. Round report owner item 2 asks for a link, which is pending the owner.
- "have the same searcher" is the branch wording. Owner item 4 in `reports/build-docs-misc-round-1-g2.md` offers "have the same committed documents" for plainer words. Pending the owner.
- The two Antora logs named by the receipt are not on disk. The Proof rests on the receipt wording.
- No branch fragment, so there is no title to match. The "Title:" line is the PR title.

## SOLR-16914

Verdict: DRIFT (3 items).

1. Draft says: "**The Japanese tokenizer section does not say which tokens the compound option keeps, or what that does to a Synonym Graph Filter placed after the tokenizer.**" and "The section says that search mode keeps compound terms "as synonyms", but it does not name the option that controls this. Its option table gives `discardCompoundToken` no default."
   - Evidence: at base `14c7aac0d151`, line 2292 (the description) names no option, so "does not name the option" is true of that paragraph only. The option table (lines 2346-2350) names `discardCompoundToken`, says "Set to `false` to keep original compound tokens with the `search` mode, `true` to discard.", and shows "Default: none". So the section does say which tokens the option keeps. The option table does not mention a Synonym Graph Filter, so that half stands.
   - Replacement for the bold line: "**The Japanese tokenizer description does not name the option that keeps compound tokens. The option table does not say what that option does to a Synonym Graph Filter placed after the tokenizer.**"
   - Replacement for the second sentence pair: "The description says that search mode keeps compound terms "as synonyms", but it does not name the option that controls this. The option table names `discardCompoundToken` and says what it keeps, but gives it no default."

2. Draft says (Proof paragraph, no date): "This is a documentation build and a content check, not a test run. The reference guide's Antora site build finished without errors at this head (cd878023d3d0e351700f5365f692423fbe97d972). The repository's tidy check (whitespace and formatting) passed on the file at this head."
   - Evidence: `receipts/SOLR-16914.md` lines 3-6: VERIFIED at the live tip by a light gate finished 2026-10-08 (tidy, Antora rc=0). The receipt gives no separate Antora date, so the light-gate date is the verification date. Brief check 2 requires one, and the Proof has none.
   - Replacement: append " Verified 2026-10-08 at this head." to the end of the Proof paragraph (applied with item 3).

3. Draft says: "The content check compared parameter names, defaults and the default mode with the Lucene 10.4.0 Kuromoji factory and tokenizer source." (and "a documentation build and a content check" in the first sentence).
   - Evidence: `receipts/SOLR-16914.md` records a tidy light gate and an Antora check. It records no source comparison. The comparison is the review round's own read (`reports/build-docs-misc-round-1-g2.md` lines 39-47). The Lucene pin is confirmed at `cd878023d3d0` in `gradle/libs.versions.toml` line 39 (`apache-lucene = "10.4.0"`). The receipt does not support the Proof claim, so it is DRIFT under the brief.
   - Replacement: apply items 2 and 3 together. The Proof paragraph then reads exactly: "This is a documentation build, not a test run. The reference guide's Antora site build finished without errors at this head (cd878023d3d0e351700f5365f692423fbe97d972). The repository's tidy check (whitespace and formatting) passed on the file at this head. Verified 2026-10-08."
   - To restore the content-check sentence, record it in the receipt first.

Verified, no drift:
- Head `cd878023d3d0` matches the live tip. One file, `language-analysis.adoc` (base to head: 7 insertions, 3 deletions). Head lines 2292, 2315-2317, 2351 and 2354 hold what the draft says.
- `filters.adoc` line 3131 (head) holds the NOTE "it cannot consume an input token graph correctly". The branch does not change `filters.adoc`.
- Limits match `reports/build-docs-misc-round-1-g2.md`: the narrow claim, the mode row (line 2302, "Default: none"), the token-stream check not run, and branch_9x not checked.
- Changelog "none": consistent.
- No em dashes. No internal vocabulary in the body.

Optional notes, not blocking:
- Line 1 is a bracketed DRAFTABLE note that names owner item 3. Delete it before posting.
- The token-stream check (`reports/build-docs-misc-round-1-g2.md` owner item 3) is still recommended before posting. The Limits line covers the non-linear statement if it is posted without that check.
- "The defaults match the Lucene Kuromoji factory" in "What this change does" rests on the review's source read and the repo pin, not on the receipt. Keep it only if the main side accepts that, or record the check in the receipt.
- The Antora log and the light-gate log named by the receipt are not on disk.
- No title line. The branch has no fragment.
- Plain-language note (optional): "discardCompoundToken", "token graph" and "linear stream" are compressed jargon.

## Not done

- No JIRA call (read-only). The Jira status for SOLR-18368 is unresolved: the receipt says Resolved, the local snapshot says Open.
- No build, Gradle, test, Antora, tidy or gate run. The logs the receipts name (`g18368-gate.log`, `g3684-harden2.log`, `g5821-refguide*.log`, `g16914-*.log`) are not on disk and were not opened.
- No `gh` call. No live PR was checked for these branches.
- Links were not opened in a browser. Citations were checked by reading the file at the named SHA and confirming the objects exist. GitHub rendering was not checked.
- `design-decisions-open.md` (the SOLR-3684 Choice source) was not found. The Choice was compared with the Jira snapshot and the receipt, not the design record.
- No drift beyond the cited files was checked on upstream/main.
