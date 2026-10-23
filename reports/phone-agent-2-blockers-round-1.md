# Phone Agent 2: Blocker round 1

Status: DONE, with explicit partial implementation delivery. All 13 classifications are complete; one narrow test fix is fully gated, two candidates are handed back, and deferred/release/process work remains open as described.

Claim: `claims/phone-agent-2-blockers-round-1.md`. Assignment: `assignments/phone-agent-2-blockers-round-1.md`. All 13 tickets received fresh anonymous public Jira reads on 2026-10-22, including description, status, comments, issue links and available remote PR/commit links. The CSV was used only to identify the tickets. Source baseline is Apache main `b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb`, fetched in this pass. Source links below are pinned to that commit unless a release branch is explicitly named.

## Summary

| Ticket | Classification at pinned upstream | Outcome of this round |
| --- | --- | --- |
| SOLR-12833 | ALREADY-FIXED | Configurable lock timeout and compatibility setting are present; no duplicate implementation. |
| SOLR-16450 | ALREADY-FIXED | Initial fix and the post-reopen second watcher-path fix are present. |
| SOLR-17463 | NOT-REPRODUCIBLE | Missing full thread dump and triggering sequence prevent a faithful reproduction; no hypothetical hardening. |
| SOLR-17546 | NOT-REPRODUCIBLE in sampled deployed URLs | Sampled old URLs return 302 then 200; complete removed-page redirect coverage is not established. |
| SOLR-17840 | STILL-PRESENT, reproduced timeout defect | Two runtime loss/failure boundaries proved. Candidate passed compile and pre-fix proof, but failed the full focused class; HANDED BACK. |
| SOLR-17844 | STILL-PRESENT, test-coverage defect | Narrow test repair GATE GREEN at 08ecc90ff7, including discriminating mutation and 2 passing focused tests. |
| SOLR-17988 | ALREADY-FIXED for named property transitions | Published Solr 10 guidance lists replacements; no issue-specific additional omissions were supplied. |
| SOLR-18240 | ALREADY-FIXED for guide content | Solr 9.11 major changes are on the target branch and published. |
| SOLR-18297 | ALREADY-FIXED | Merged upstream PR #5059 is an ancestor; prior completed fork work was read, not duplicated. |
| SOLR-18398 | STILL-PRESENT, deferred migration | Jira explicitly says not to begin the broad 11.0 migration until closer to release. No premature conversion. |
| SOLR-18446 | STILL-PRESENT | Path/parameter precedence matches the report. Candidate pushed; permitted gate retry failed compilation. Missing import corrected afterward; new head remains unverified and handed back. |
| SOLR-18486 | STILL-PRESENT, release gate | Populated beta guide is not release-owner sign-off. |
| SOLR-18501 | STILL-PRESENT, IP-clearance process | Draft paperwork is not official filing and approval. |


### Implementation heads and next actions

| Ticket | Fork branch/head | Verified state / next action |
| --- | --- | --- |
| SOLR-17844 | `solr-17844-restore-sort-assertions-phone-agent-2` at `08ecc90ff7b5505e576985c8dcb514393314ce2b` | Full module gate green; owner may review the test-only repair. No PR was opened. |
| SOLR-17840 | `solr-17840-preserve-partial-results-phone-agent-2` at `3999ff0be34bd192c2325b9138b8aa8a7b5bc24d` | HANDED BACK. Preserve the unchanged regression; address the demonstrated doc-values fetch/response-streaming timeout, then validate the completed implementation. |
| SOLR-18446 | `solr-18446-path-routing-phone-agent-2` at `8bb60e6f61abc9827fb58dcae1863cabb33f9796` | HANDED BACK. The compiler-named import is corrected, but the new head is unverified; full proof, focused tests and module validation remain. No third gate attempt was made. |

Five duplicate implementation slots are refused and cleared below; the two evidence-limited reports are not declared fixed. SOLR-18398 stays deferred by its own timing instruction, while SOLR-18486 and SOLR-18501 remain release/PMC-owned work. Completion of this round does not mean all 13 upstream issues are resolved.

These classifications do not change Jira status. A closed implementation item in this round does not mean the upstream tracker or a release gate was resolved. Existing completed or live work is not rebuilt solely to make a fresh receipt.

## SOLR-12833: Use timed-out lock in DistributedUpdateProcessor

**ALREADY-FIXED.** [Jira](https://issues.apache.org/jira/browse/SOLR-12833) is Reopened with resolution Fixed and fix versions 7.7, 7.7.2, 8.0 and 8.1. Its 69 comments and [lucene-solr PR #463](https://github.com/apache/lucene-solr/pull/463) show the configurable lock timeout was committed as [`b9a966e5f7`](https://github.com/apache/solr/commit/b9a966e5f75b77944f61e88a30c002dbc22c504e), followed by memory optimization `70e090717d`. Both are ancestors of the pinned main. Later reopen/test/backport activity is not evidence that the requested feature is missing.

Current `solr/core/src/java/org/apache/solr/update/UpdateLocks.java:41-43,137-161` stores `docLockTimeoutMs` and uses timed `tryLock` for a nonzero timeout, raising a server error when it expires. `UpdateLog.java:392-400` retains `versionBucketLockTimeoutMs` as the compatibility fallback. Zero deliberately means unlimited wait. Existing timeout coverage is in `DistributedUpdateProcessorTest.java:188-225`.

The ticket describes operational contention but supplies no deterministic schema/input sequence. No new contention test was invented for this already-implemented feature. Implementation disposition: **WON'T DO, advisory shape 1**; the current round's implementation slot is cleared by this evidence. The prior `reports/csv-critblock-02.md:30` classification is corroborated, not redone as a patch.

Owner-only closure draft: "The configured update-lock timeout is present in main, originally implemented in b9a966e5f7 and retained through docLockTimeoutMs with the legacy versionBucketLockTimeoutMs fallback. A nonzero timeout fails lock acquisition rather than waiting indefinitely. No new missing code path was identified."

## SOLR-16450: watcher registration failure

**ALREADY-FIXED, despite Reopened/unresolved Jira state.** [Jira](https://issues.apache.org/jira/browse/SOLR-16450) and its 12 comments show [PR #1058](https://github.com/apache/solr/pull/1058) first moved doc-collection watcher registration inside the cleanup `try`. The explicit October 2022 reopen identified the collection-state watcher variant; [`32e0f68a9b`](https://github.com/apache/solr/commit/32e0f68a9bc380905746eec0e7d392594ec132fb) fixed that variant after the reopen. Initial commits `7998c973c7`, `ed1533e391` and the follow-up are ancestors of main. The 2024 comment asks which version contains the fix; it reports no new failing scenario.

`solr/solrj-zookeeper/src/java/org/apache/solr/common/cloud/ZkStateReader.java:1694-1711` and `1730-1775` now register both watcher types inside `try` and remove watchers/latches in `finally`. Registration exceptions therefore enter cleanup. There is no supplied runnable Jira reproduction, and no current missing branch was found. Implementation disposition: **WON'T DO, advisory shape 1**, cleared without a duplicate branch.

Owner-only closure draft: "PR #1058's initial fix and the post-reopen companion fix 32e0f68a9b are both present. Both current waitForState overloads register within try/finally cleanup. Please reconcile the Reopened status and fix version; no additional failing case was supplied."

## SOLR-17463: hanging at SolrCore.getSearcher

**NOT-REPRODUCIBLE from the available report, not ALREADY-FIXED.** [Jira](https://issues.apache.org/jira/browse/SOLR-17463) is Open, affects 9.6.1, and includes a partial interrupted metrics-to-searcher stack. The two comments matter: the maintainer requests the complete thread dump, and the reporter says it is unavailable and that not all threads were checked. No exact metrics request/rate, concurrent core operation, config/index snapshot or runnable sequence is supplied. The related [SOLR-17060](https://issues.apache.org/jira/browse/SOLR-17060) and [PR #2101](https://github.com/apache/solr/pull/2101) were read; the reporter explicitly says that earlier mitigation did not eliminate the similar incident.

At main, `SolrCore.java:2516-2536` waits for searcher lifecycle coordination; `:527-533` is the segment-count/searcher path. The SOLR-17060 mitigation at `:1370-1376` samples the segment gauge only when `isReady()` (`:1906-1907`); readiness is set after searcher opening at `:2750`. `SolrCoreTest.java:332-394` contains a best-effort SOLR-17060 test, not proof for this distinct incident. No faithful production reproduction could be constructed from the supplied material. The generic wait is not itself a defect premise.

No patch or closure-as-fixed is justified. Owner-only evidence-request draft: "Please provide a complete thread dump, including searcherExecutor and request/metrics threads, the exact build, metrics polling configuration, concurrent core/searcher operation and core configuration. The available partial stack does not distinguish the remaining hang from expected searcher coordination."

## SOLR-17546: removed Ref Guide pages

**NOT-REPRODUCIBLE for the sampled missing-page outcome; full redirect coverage remains unverified.** [Jira](https://issues.apache.org/jira/browse/SOLR-17546) is Open, targets 10.0, and names Hadoop authentication and rule-based authorization pages. Its one comment proposes sending removed 10.x pages to the latest 9.x content. No PR, commit or linked issue is recorded.

The Hadoop source page is absent from main; the rule-based authorization page is still present. `dev-tools/scripts/releaseWizard.yaml:1380-1395` separates guide completeness from generated `.htaccess` work. Generated deployed redirects are not a checked-in source file here.

A fresh `curl -sSIL --max-time 30` probe of `/guide/solr/10_0/deployment-guide/hadoop-authentication-plugin.html` and `/guide/solr/10_0/deployment-guide/rule-based-authorization-plugin.html` returned **302 to the matching `/latest/` path, then 200**. The latest Hadoop path returned 200 directly, and the fetched content identifies the older 9.10 page. Raw headers are preserved at `/workspace/phone-agent-2-logs/solr-17546-headers.txt`. These samples do not establish all removed-page mappings or the exact intended latest-9.x destination policy. No fixing commit was identified, and no speculative `.htaccess` patch was made. The prior KEEP-OPEN finding in `reports/csv-critblock-04.md:52` is not silently converted to fixed.

Owner-only evidence-request draft: "The two reported 10.0 URLs currently redirect to working pages. Please identify any remaining removed URL, its observed result, and the intended target; the sampled success does not establish complete redirect coverage."

## SOLR-17840: LTR after Lucene 10

**STILL-PRESENT: reproduced timeout-result loss; the fork candidate is incomplete and HANDED BACK.** [Jira](https://issues.apache.org/jira/browse/SOLR-17840) remains Open/unresolved for 10.0. All four comments were read: commits `4415d9e602` and `efeee56466`, followed by questions about the ignored timeout regression and missing changelog. Related SOLR-17631 is Closed/Fixed. Both compatibility commits are ancestors: `LTRScoringQuery.java:568` uses `DisiPriorityQueue.ofMaxSize`, and `scoring/MultiFeaturesScorer.java:118-209` supplies the replacement disjunction iterator. Those API changes do not settle the remaining timeout regression.

At the pinned base, `solr/modules/ltr/src/test/org/apache/solr/ltr/TestLTRQParserPlugin.java:145-179` ignores the existing expensive-feature timeout method. It sets `timeAllowed=800` against a 1000 ms feature, expecting all four original documents, order and scores after reranking aborts. The round enabled only that method in a disposable upstream worktree and ran `:solr:modules:ltr:test --tests org.apache.solr.ltr.TestLTRQParserPlugin.ltr_expensiveFeatureRescoring_shouldTimeOutAndReturnPartialResults -Ptests.seed=1BB02D3762B56611 -Ptests.jvms=1`. Fresh XML: **1 test, 0 skipped, 1 failure, 0 errors**, `mismatch: '4'!='0' @ response/numFound`. The response reports the LTR-specific time-limit breach, QTime 1098, but no hits. Evidence: `/workspace/phone-agent-2-logs/ltr-premise/test-results/test/TEST-org.apache.solr.ltr.TestLTRQParserPlugin.xml` and `test.log`.

A separate instrumentation-only repeat at the same seed localized the loss. `ReRankCollector.java:131-163` has 4 initial hits and restores all 4 after the timeout. `SolrIndexSearcher.java:1905-1934` receives 4 and builds a DocList with size=4/matches=4. At `QueryComponent.java:1861-1869`, the post-query limit check returns before `rsp.addResponse(ctx)`: the probe records size=4/matches=4 but `responsePresent=false`. Diagnostic log/XML: `/workspace/phone-agent-2-logs/ltr-diagnostic/`. This satisfies the narrow premise; no scorer hypothesis or changed expectation is needed.

The fork fix is [3999ff0be3](https://github.com/nick-boss-tech/solr/commit/3999ff0be34bd192c2325b9138b8aa8a7b5bc24d) on `solr-17840-preserve-partial-results-phone-agent-2`. It moves the two existing response-exposure lines before the limit early return, enables the unchanged regression, and adds a valid changelog. No diagnostic prints, timeout changes, scoring rewrites or fixture changes are included. Its gate passed YAML parse, clean tidy, Error Prone compile, and a discriminating pre-fix proof: 1 executed test, 0 skipped, 1 intended numFound 4-versus-0 failure, 0 errors. At the restored head, the full class executed **15 tests, 0 skipped, 1 failure, 0 errors**. The timeout method now returns `ExitableDirectoryReader$ExitingReaderException` during doc-values fetching in `SolrDocumentFetcher`/`DocsStreamer` and JSON response writing, so `/response/numFound` is absent. The candidate is not green and must not be merged as a completed fix. Repairing that remaining fetch/streaming timeout boundary is outside this gate's declared Diff scope; the item is **HANDED BACK** without weakening the expectation or disabling limits. Module check was not run. [Receipt](../receipts/SOLR-17840.md) preserves both runtime boundaries, the failed head, and the exact follow-up. Local response output: `/workspace/phone-agent-2-logs/gate-17840-attempt-1/head-response-output.txt`. The prior broad ALREADY-FIXED row in `reports/csv-critblock-04.md` omitted this unresolved part of the ticket.

## SOLR-17844: TestFieldCacheSortRandom

**STILL-PRESENT as a concrete test-coverage defect.** [Jira](https://issues.apache.org/jira/browse/SOLR-17844) is Open/unresolved for 10.0. Its description asks whether two workaround commits are sound, rather than merely whether the resulting test exits green. Both comments and both commits were read: `3a58c85b9a12` and `403069e7b4`, both ancestors. The historical failure seed is `1BB02D3762B56611`; the later commit says it stopped checking order for both string sort types.

`solr/core/src/test/org/apache/solr/uninverting/TestFieldCacheSortRandom.java:202-231` computes expected values, but `:268-289` only checks each result's runtime type and unconditionally skips exact ordering comparisons. It does not assert expected count there either. The older three-seed green sample in `reports/csv-critblock-04.md:25-27` therefore cannot close this review concern.

The initial candidate `0a7ffa07a40c123bd541df3068548247f40055d7` restored count, per-hit docID/order and value assertions. Its discriminating mutation failed as intended, but the restored head failed a count assertion: 42 expected versus 14 returned. The attempt was honestly handed back, not counted green. Lucene 10.4.0 partition dispatch independently explains the oracle bug: it invokes the full-leaf supplier once per partition, recording duplicate global docIDs. Implementation rework deduplicates only those observations and asserts their values agree. It does not take expectations from actual hits or weaken assertions.

The current fork head [08ecc90ff7](https://github.com/nick-boss-tech/solr/commit/08ecc90ff7b5505e576985c8dcb514393314ce2b), branch `solr-17844-restore-sort-assertions-phone-agent-2`, received a new complete gate from step 0. **GATE GREEN:** test-only changelog exemption verified; tidy unchanged; Error Prone compile passed; inverted actual sort direction produced 2 executed expected order-assertion failures with 0 skips/errors; restored-head full class at historical seed produced **2 tests, 0 skipped, 0 failures, 0 errors**; `:solr:core:check -x test` passed. The fork tip was read back at the gated head and the worktree is clean. [Receipt](../receipts/SOLR-17844.md) preserves both the failed candidate and green rework. No production sorting change was inferred.

## SOLR-17988: Solr 10 property-change guidance

**ALREADY-FIXED for the concrete named transitions, with no claim of an exhaustive configuration audit.** [Jira](https://issues.apache.org/jira/browse/SOLR-17988) is Open, assigned to Eric Pugh, and broadly requests property-name/configuration-location guidance. It has no comments, links or issue-specific fix. Current `solr/solr-ref-guide/modules/upgrade-notes/pages/major-changes-in-solr-10.adoc:169-175` and the [published 10.0 guide](https://solr.apache.org/guide/solr/10_0/upgrade-notes/major-changes-in-solr-10.html) explicitly identify `SOLR_JETTY_HOST`/`solr.jetty.host` to `SOLR_HOST_BIND`/`solr.host.bind`, advertised-host renaming, and `jetty.port` to `solr.port.listen`.

The paragraph originates in [`7f635a9f31`](https://github.com/apache/solr/commit/7f635a9f3114fe380d8eb736d4d2b3b727e49a16), SOLR-15442 / PR #3814, dated before this Jira was created. That timing is stated rather than presenting it as an issue-specific fixing commit. The current guide is completion evidence for those transitions, not evidence that every possible property omission has been audited. No reproduction applies to this communication task. Implementation disposition: **WON'T DO, advisory shape 1** for duplicating the existing guidance.

Owner-only closure draft: "The published Solr 10 major-changes guide documents the named binding, advertised-host and listener-port property changes. Please itemize any additional omissions before further edits; this finding does not claim an exhaustive property audit."

## SOLR-18240: Solr 9.11 Ref Guide

**ALREADY-FIXED for the requested guide content.** [Jira](https://issues.apache.org/jira/browse/SOLR-18240) is Open, assigned to David Smiley, and created by the release workflow. No comments, PRs or issue links are recorded. The version is marked released on 2026-10-04. The [published 9.11 guide](https://solr.apache.org/guide/solr/9_11/upgrade-notes/major-changes-in-solr-9.html) and [target branch_9x source](https://github.com/apache/solr/blob/branch_9x/solr/solr-ref-guide/modules/upgrade-notes/pages/major-changes-in-solr-9.adoc#L70-L138) contain the Solr 9.11 section; main's corresponding file has it at `:70-138`.

[`ce063d9877`](https://github.com/apache/solr/commit/ce063d9877), "Ref guide, major-changes-in-solr-9.adoc: 9.11", added the section on 2026-10-07. The notes were added after the Jira release date; no claim is made that pre-announcement sign-off occurred. There is no runtime reproduction for this release-documentation task. Implementation disposition: **WON'T DO, advisory shape 1**, without another speculative guide edit.

Owner-only closure draft: "The 9.11 major-changes section added by ce063d9877 is present on branch_9x and in the published guide. The requested guide content is available; historical release sign-off was not reconstructed."

## SOLR-18297: Jetty 12.1.14

**ALREADY-FIXED.** Existing claims, review reports, gate receipts and the completed owner-items repair were read before checking the upstream PR. [Jira](https://issues.apache.org/jira/browse/SOLR-18297) remains Open and targets 10.2. Its comments and remote links identify [PR #5059](https://github.com/apache/solr/pull/5059), merged on 2026-10-10 as [`eaf9c86369`](https://github.com/apache/solr/commit/eaf9c86369daebd82d1abe92e5af7b7200e40d2e), plus branch_10x backport `4507807d28`. The merge is an ancestor of the pinned main; the version catalog and Jetty-resolving lockfiles now use 12.1.14.

Existing fork evidence is `claims/pool-owner-items-solr-18297.md` (DONE), `reports/owner-items-scrub-2026-10-22.md`, and `receipts/SOLR-18297.md`, including the repaired 14 module lockfiles and recorded green owner-items head `73f269e99a0`. Earlier stood-down/duplicate review claims are preserved rather than revived. Those historical test counts are not represented as this host's new run. Implementation disposition: **WON'T DO, advisory shape 1**. Its current-round slot is cleared by the merged-upstream and completed-record pointers, not by editing terminal claims or making a duplicate upgrade branch.

Owner-only closure draft: "Jetty 12.1.14 was merged in PR #5059 and is included in current main, with a branch_10x backport. Another dependency upgrade is not needed for this ticket."

## SOLR-18398: response containers to LinkedHashMap

**STILL-PRESENT, explicitly deferred 11.0 API migration.** [Jira](https://issues.apache.org/jira/browse/SOLR-18398) is Open for main (11.0), with one comment and no links or fixing PR. That comment explicitly says the many-file migration should not start until closer to the 11.0 release. This is not a reported runtime failure.

At `solr/core/src/java/org/apache/solr/response/SolrQueryResponse.java:94,129-140`, top-level values remain `NamedList<Object>`/`SimpleOrderedMap`. Response-header methods at `:260-274` still use `SimpleOrderedMap`, not `LinkedHashMap`; related helpers are at `:159-227`. Historical PR #4809 / `2da4736779` narrowed header accessor types, but did not complete this migration.

No runtime reproduction applies. The evidence establishes remaining work but not an urgent narrow defect fix. No premature cross-tree API conversion was made in conflict with the ticket's timing instruction. This item is a documented partial delivery, not WON'T DO, not ALREADY-FIXED, and not a new request to the owner to settle a decision Jira already states. Future implementation must preserve ordering/serialization and cover unique-key behavior; unrelated logging and HTTP-header containers are outside the stated scope.

## SOLR-18446: path-authoritative collection routing

**STILL-PRESENT at the pinned upstream base.** [Jira](https://issues.apache.org/jira/browse/SOLR-18446) is Open for main (11.0). Description/comments discuss path authority, aliases/multi-destination paths and limited permissible disambiguation. Related SOLR-18470 was also read and is a broader request-scope change, not this patch's scope. No PR, commit or remote link is recorded.

`solr/core/src/java/org/apache/solr/servlet/HttpSolrCall.java:253-277` parses the path/core; `:279-284` explicitly gives `collection` parameter precedence when constructing the collection list; `:286-305` selects local or remote routing from it. The named precedence is present and narrow enough to test. Existing `HttpSolrCallCloudTest.java` covers core selection but not the conflicting path/parameter scenario.

The narrow candidate `23db47ef150dd87198718085a9d28cffdf4ada4c` was pushed on `solr-18446-path-routing-phone-agent-2`, without implementing SOLR-18470's broader handler-scope contract. Attempt 1 parsed the changelog and ran tidy, which reformatted the new test. Exact tidy output was committed and pushed as `5dcdf0d2bce40a9e83b9d19567beb6785844335f`; the one permitted full retry at that head passed tidy but failed compile because HashSet was not imported. No proof or head tests executed. The gate is HANDED BACK, not green. After handback, one-line import correction `8bb60e6f61abc9827fb58dcae1863cabb33f9796` was committed and pushed, without a third gate attempt. The corrected current head remains unverified; `receipts/SOLR-18446.md` preserves both attempts and the outstanding full-validation requirement.

## SOLR-18486: Solr 10.1 guide announcement gate

**STILL-PRESENT as release-owned process work.** [Jira](https://issues.apache.org/jira/browse/SOLR-18486) is Open, assigned to Andrzej Bialecki, affects unreleased 10.1, and explicitly prohibits announcing before the guide is complete. There are no comments or fixing links. The [target branch_10_1 guide](https://github.com/apache/solr/blob/branch_10_1/solr/solr-ref-guide/modules/upgrade-notes/pages/major-changes-in-solr-10.adoc), observed at `749712b71940c9f96d824c533f32f5c56dcd3924`, and the [published 10.1-beta guide](https://solr.apache.org/guide/solr/10_1/upgrade-notes/major-changes-in-solr-10.html) have populated notes. Main has the 10.1 section at `major-changes-in-solr-10.adoc:53-138`.

`dev-tools/scripts/releaseWizard.yaml:704-710,1380-1395` distinguishes RC/voting from announcement and requires current guide content plus blocker resolution before announcement. Populated beta content is not release-owner sign-off. No runtime reproduction or code patch applies; release readiness remains with the release owner. This is a finished non-code classification, not a claim that release work is complete.

## SOLR-18501: Solr MCP IP clearance

**STILL-PRESENT as legal/process work.** [Jira](https://issues.apache.org/jira/browse/SOLR-18501) is Open, assigned to Eric Pugh, has no comments or issue links, and cites legal paperwork/CLA and SOLR-18259 as precedent. The precedent was read; its completed form and subsequent filing/list steps do not establish completion of this separate item.

The [solr-mcp draft form](https://github.com/apache/solr-mcp/blob/main/solr-mcp-ip-clearance.xml) has populated project/license facts but still has `???` vote-thread/result placeholders. [Preparation PR #248](https://github.com/apache/solr-mcp/pull/248) was closed after its one-time license rows were committed; it is not a clearance approval. The official [Incubator clearance procedure and list](https://incubator.apache.org/ip-clearance/) require PMC-owned filing and approval; Solr MCP was absent from the published list in this pass. No Solr-core runtime reproduction or code fix applies. Authorized PMC/release participants must finish and verify the paperwork; this report does not submit legal records or assert clearance.

## Clearing and limits

The current-round duplicate implementation slots for SOLR-12833, SOLR-16450, SOLR-17988, SOLR-18240 and SOLR-18297 are refused under advisory shape 1 and recorded closed in the claim in the same final push. Existing terminal claims are not edited. SOLR-17463 is evidence-limited, not a repeat structural-refusal claim: it has a code pointer but no faithful triggering sequence. SOLR-18398 remains deferred rather than being cleared as done. Release/process classifications do not manufacture patches, Jira resolutions or owner sign-off.

No Jira or GitHub issue/PR posts or comments, PR openings, ready flips or force pushes were made. Ticket-branch commits and coordination writes use Nick Shanin's identity without attribution trailers. Builds are serialized under `/workspace/tools/.gradle-$(hostname -s).lock`, with the disk guard before execution. This report, the claim's DONE mark, assignment completion, and per-ticket clearing records are delivered in one atomic coordination commit. No tracker status is changed.
