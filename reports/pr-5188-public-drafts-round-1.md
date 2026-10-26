# PR 5188 follow-up: proof review and public drafts, round 1

Status: DONE, drafts only. Claim: claims/pr-5188-public-drafts-windows-round-1.md (f5ab5305d31570be6cc44a12976fd14dbc883e97). Owner asked to check round-3 proof and draft a Jira ticket and PR body if it is sound. No Jira issue, PR or public comment was created.

## Independent review

Accepted for the scope requested in the round-3 superseding job. Fork head read back as 555e1bf2398c08b1018ed44c056274c727bd6454; current upstream PR 5188 is OPEN at db867d80365510790bb6a04ce33105cd196cf34c, the production base used for the matched pre-fix tests. The final commit changes only BasicFunctionalityTest formatting; production is unchanged from f00caeb5e83ac375672ae27fb43ee1546795e98b. The local follow-up worktree was fast-forwarded to the verified head without source edits.

Evidence read from immutable board snapshot e9b9b8b1624515329d10a19b46cae8b25ea773a3, including receipts/PR-5188-glob-field-loading-round-3.md and reports/pr-5188-glob-field-loading-round-3-evidence/. Recomputed all 66 manifest payload hashes, with no missing or extra payloads (67 files including the manifest). Manifest SHA-256: 32974bdd33b39e34086cbdf008d7b8c4a089f5d0492ea64812e85df2a5c79544. Parsed all 13 fresh XML files from actual test legs, checked testcase counts/names, failures/errors/skips and raw statuses/log build markers. The driver cleans test results per leg, uses seed 5188C0DE, and names all required filters. XML copies under tidy/compile/check were not counted as additional test runs.

- Four independent pre-fix failures: unrelated_s remains lazy (RTG); original glob source_s prefetch; title remains lazy (unmatched glob); missing prefetched document at source_* / 50 / non-shard. Earlier explicit controls pass on the two prefetch legs.
- Positive 24-row snippets pass before and after the fix; original and unified have unconditional positive assertions. Source loops checked against the final BasicFunctionalityTest hash bbbd61eead2610a458a1b4cb1c1b5bed51d89e6f21813398d90bdea4085d8552.
- Six focused methods, zero failures/errors/skips. Six full classes: BasicFunctionalityTest 28, TestPseudoReturnFields 35, TestRetrieveFieldsOptimizer 1, ReturnFieldsTest 16, TestUnifiedSolrHighlighter 33, LargeFieldTest 1. Total 114, zero failures/errors/skips.
- Final tidy, executed core Java and test Java compilation with Error Prone, and :solr:core:check -x test pass.

No full core test suite, repository-wide build, physical I/O measurement or latency benchmark is claimed. No Gradle, tests, compiler or formatter ran on Windows during this review. Scope remains five incremental files against db867d8 (three production files, two test classes), 390 insertions and 43 deletions.

## Drafting assessment

The Jira proposal is a Bug about stored-field selection and prefetch. Returned field lists and snippets can already be correct, so the text makes no output corruption, disclosure, or measured speedup claim. Version fields remain unset because this proof is on the PR branch, not a released-version reproduction.

Searched live Jira summaries for glob, prefetch and stored field access, and descriptions for glob + RTG/real time or stored-field prefetch. Reviewed SOLR-9612 in full: it concerns avoiding stored-field seeks for docValues-only finite lists, rather than these partial-glob RTG and query-prefetch paths. SOLR-15450 concerns LTR FieldValueFeature prefetch; SOLR-10321 concerns empty highlight arrays from hl.fl globs; SOLR-1702 proposes a generic component prefetch registration mechanism. No exact duplicate was identified in the returned matches; this is a focused duplicate check, not a claim to have read every Jira issue.

Both drafts credit David Smiley's PR 5188. The fork branch currently includes that open PR's changes. Before opening a separate PR against apache/solr main, settle the parent/base dependency (or wait for PR 5188 to merge) and inspect the resulting diff. A changed Java/test tree needs fresh exact-head proof. This report does not authorize a rebase, history rewrite, publication or changes to PR 5188.

Replace the SOLR-<pending> title and Jira-link placeholder after an issue is created. A ticket-scoped changelog fragment with author name Nick Shanin is still needed before submission; no nonexistent changelog link was added to the draft. Disclosure lines are prepared for Nick's review before publication. No live PR body was edited.

## Jira ticket draft

Summary: Honor glob field selection in real-time get and lazy document prefetch
Project: SOLR
Issue type: Bug
Affects version / fix version: unset.

Description (Jira wiki markup):

Partial-glob {{fl}} requests can load unrelated stored values in index-backed real-time get, and skip the lazy prefetch used by normal queries. The returned field list can still be correct; the gap is in stored-field loading.

This follows [David Smiley's PR 5188|https://github.com/apache/solr/pull/5188]. The behavior was reproduced on its {{db867d80365510790bb6a04ce33105cd196cf34c}} production code using the same regression tests as the fix.

h3. Reproduction

Enable lazy field loading and the document cache. Index a document with stored {{id=7781}}, {{title=keyword}}, {{source_s=source}}, and {{unrelated_s=unrelated}}, then commit.

 * Request {{/get?id=7781&fl=tit*}}. The partial glob has no finite field-name set, so RTG passes {{null}} to the stored-field fetcher. Its document conversion also materializes fields already present in a cached Lucene document. Unrelated values are loaded even though they are excluded from the response. An unmatched glob such as {{fl=missing_*}} also returns an empty document while loading unrelated values.
 * Query {{/select?q=title:keyword&fl=source_*&hl=true&hl.fl=title}} with at most 50 results on a non-shard request. Lazy prefetch skips the glob request because {{getLuceneFieldNames()}} returns {{null}}. The equivalent explicit {{fl=source_s}} request prefetches response fields, highlight fields and the unique key.

These loading states require inspection of the cached document before response serialization; response fields and snippets alone do not expose the problem.

h3. Proposed change

Share the existing stored-field selector with RTG and prefetch. Resolve glob matches and required transformer inputs before fetching. Filter RTG's cached fields before reading their values. When highlighting is enabled, add highlight fields and the unique key to the resolved prefetch selection. Preserve the existing non-shard and 50-result limits.

h3. Verification

On Linux with seed {{5188C0DE}}, four loading regression methods fail on the pre-fix production code at the expected assertions. All six focused methods pass at {{555e1bf2398c08b1018ed44c056274c727bd6454}}. The 24-row positive snippet matrix passes before and after the fix, including original and unified highlighting.

The six full classes have 114 tests, zero failures, errors or skips: {{BasicFunctionalityTest}} 28, {{TestPseudoReturnFields}} 35, {{TestRetrieveFieldsOptimizer}} 1, {{ReturnFieldsTest}} 16, {{TestUnifiedSolrHighlighter}} 33 and {{LargeFieldTest}} 1. Tidy, Error Prone compilation and {{:solr:core:check -x test}} pass.

Coverage includes unmatched globs, transformer inputs, alias collisions, reuse of cached documents, uncommitted RTG output, {{fl=*}}, pseudo-fields and 50/51-result and shard boundaries. No full core test run, whole-repository validation, disk-I/O measurement or latency benchmark was performed. Partial-glob term-vector behavior and docValues decoration are outside this change.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_

## PR draft

Title: SOLR-<pending>: honor glob field selection in RTG and prefetch

Body:

🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

JIRA: <add the new ticket link after filing>

Follow-up to [David Smiley's PR 5188](https://github.com/apache/solr/pull/5188), using its current `loadsField` API at `db867d8`.

**Partial-glob field lists leave two stored-field loading gaps.**

For `fl=tit*`, [RTG on the base](https://github.com/apache/solr/blob/db867d80365510790bb6a04ce33105cd196cf34c/solr/core/src/java/org/apache/solr/handler/component/RealTimeGetComponent.java#L348-L353) passes the null finite-name sentinel to the stored-field fetcher and materializes unrelated cached values during conversion. An unmatched glob also loads values despite returning an empty document. [Lazy prefetch on the base](https://github.com/apache/solr/blob/db867d80365510790bb6a04ce33105cd196cf34c/solr/core/src/java/org/apache/solr/util/SolrPluginUtils.java#L229-L253) skips partial globs, including the combined selection of response fields, highlight fields and the unique key.

**RTG and prefetch now share the existing stored-field selector.**

[The selector](https://github.com/nick-boss-tech/solr/blob/555e1bf2398c08b1018ed44c056274c727bd6454/solr/core/src/java/org/apache/solr/search/SolrDocumentFetcher.java#L772-L811) resolves glob matches and required transformer inputs. RTG filters cached fields before reading their values; prefetch adds highlight fields and the unique key when highlighting is enabled. The existing lazy-loading prerequisite, non-shard restriction and 50-result limit remain.

**Four loading regressions fail before the fix; all six focused methods pass afterward.**

Verified in [Linux test results](https://github.com/nick-boss-tech/solr/tree/e9b9b8b1624515329d10a19b46cae8b25ea773a3/reports/pr-5188-glob-field-loading-round-3-evidence/post-suites-xml) on 2026-10-26 at `555e1bf239`, seed `5188C0DE`. The pre-fix tree uses `db867d8` production with the final tests. RTG fails on `unrelated_s should remain lazy`; prefetch on `source_* did not prefetch source_s`; the unmatched-glob test on `title should remain lazy`; and the boundary test on a missing prefetched document at 50 results, non-shard. Explicit controls pass before the prefetch failures.

The 24-row positive snippet matrix passes on both trees, including original and unified highlighting. Full classes, with zero failures, errors or skips:

- `BasicFunctionalityTest`: 28 passed.
- `TestPseudoReturnFields`: 35 passed.
- `TestRetrieveFieldsOptimizer`: 1 passed.
- `ReturnFieldsTest`: 16 passed.
- `TestUnifiedSolrHighlighter`: 33 passed.
- `LargeFieldTest`: 1 passed.

Tidy, Error Prone compilation and `:solr:core:check -x test` pass. The new tests also cover transformer inputs, alias collisions, cache reuse, uncommitted RTG output, all-fields and pseudo-field selection, and shard/50/51-result boundaries.

**Validation covers the named core classes and the core module check.**

The full core test suite and repository checks were not run. Lazy-state assertions do not measure disk I/O or latency. This follow-up leaves docValues decoration and partial-glob term-vector handling unchanged.

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*
