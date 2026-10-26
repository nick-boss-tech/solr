# Packaging BATS assertion review, round 1: assembly

Status: DONE (static review and assembly delivered; runtime controls/fixes are not part of this assignment).
Assignment: assignments/bats-scripts-assertion-review-round-1.md.
Claim: claims/bats-scripts-assertion-review-round-1-assembly-windows.md.
Completed: 2026-10-26, Windows Codex desktop; Nick explicitly authorized static review on this host.

## Result

All 30 packaging BATS files and their 128 tests were read at apache/solr main `0d6b2e32f598d772aed094038022a6a588fdfc5a`. The reports inventory 409 active assertion/control rows: 350 sound, 48 confirmed-defect, 11 suspected-defect. Of the 48 confirmed rows, one is the already-known PR 5225 reference. The new confirmed rows are 47 call sites/control rows, not 47 independent defects.

There are 12 new confirmed finding entries across the units, representing **nine distinct defect patterns** after consolidating the repeated PostTool count, PostLogsTool commit, and HealthcheckTool findings. There are four new suspected patterns. The two known PR 5225 defects and its reserved checkpoint suspicion remain separate reference entries below. No BATS, Gradle, Solr, Docker, or mutation ran here. These are source-established defects and named controls, not captured runtime false passes.

The highest-priority follow-ups are the attempted-post summaries, the failed-batch/empty-commit summary, broad TLS rejection alternatives, and health checks that accept degraded reports. This ranking is about test reliability; it asserts no production security vulnerability.

## Delivered unit reports

All three were present at board snapshot `c7db93c41c785b42c24163f25465782854c3db8a` before assembly was claimed. Posting verified each file byte-for-byte and each commit's reachability.

| Unit | Files / tests | sound | confirmed-defect | suspected-defect | Report commit |
| --- | --- | --- | --- | --- | --- |
| [1](https://github.com/nick-boss-tech/solr/blob/c7db93c41c785b42c24163f25465782854c3db8a/reports/bats-scripts-assertion-review-round-1-unit-1.md) | 10 / 39 | 88 | 11 | 1 | 7fbf130c58a82d37ed6d01ae84ea5fd1649eca39 |
| [2](https://github.com/nick-boss-tech/solr/blob/c7db93c41c785b42c24163f25465782854c3db8a/reports/bats-scripts-assertion-review-round-1-unit-2.md) | 10 / 41 | 107 | 28 | 1 | 391a2b181caf22793d2d9ca45f1a5349b8795df7 |
| [3](https://github.com/nick-boss-tech/solr/blob/c7db93c41c785b42c24163f25465782854c3db8a/reports/bats-scripts-assertion-review-round-1-unit-3.md) | 10 / 48 | 155 | 9 (includes REF-1) | 9 | c7db93c41c785b42c24163f25465782854c3db8a |

Each report includes every inventoried assertion's pinned source line, verdict, reason, coverage limits, producing paths, relevant history, and controls. Rows include direct endpoint assertions and polling/file prerequisites as well as captured-output assertions. A bare run with no assertion is not counted as verification. Sound is scoped to the explicitly checked observable and immediate assertion group, not to unstated behavior or runtime portability.

## Ranked new confirmed findings

All nine entries are confirmed-defect by source/control-flow evidence. Full quotes and exact producer citations are in the unit reports; source links below pin the reviewed revision.

| Rank | Priority | Pattern and finding ids | Affected checks and deciding evidence | Proposed follow-up control (not run) |
| --- | --- | --- | --- | --- |
| 1 | P1 | Attempt count accepted as successful indexing; U1-C1 / U2-C1 | basic_auth:74; post:62-96,105-124,146-147,159/162,209/211; extraction:115-116. [PostTool:757](https://github.com/apache/solr/blob/0d6b2e32f598d772aed094038022a6a588fdfc5a/solr/core/src/java/org/apache/solr/cli/PostTool.java#L757) increments after void postFile; line 1108 ignores postData's boolean. Warnings use WARNING at 990-991. The basic-auth test creates COLL_NAME but posts to monitors. | Reject a document add while permitting commit/optimize. Require unguarded tests to reject the attempted-file summary. Record that later numFound guards already protect the entire no-type/directory/extraction tests. |
| 2 | P1 | Empty commit accepted as log indexing; U1-C2 / U2-C2 | basic_auth:78; postlogs:47. [PostLogsTool:221](https://github.com/apache/solr/blob/0d6b2e32f598d772aed094038022a6a588fdfc5a/solr/core/src/java/org/apache/solr/cli/PostLogsTool.java#L221) catches batch errors and independently prints Committed after commit. | Fail only batch adds and allow commit. Require positive indexed-document evidence. Batch progress text is not success evidence. |
| 3 | P1 | Non-TLS refusal accepted as certificate rejection; U3-C1 | ssl:117,353,366,496. [HttpJettySolrClient:508](https://github.com/apache/solr/blob/0d6b2e32f598d772aed094038022a6a588fdfc5a/solr/solrj-jetty/src/java/org/apache/solr/client/solrj/jetty/HttpJettySolrClient.java#L508) emits Server refused connection for ConnectException, one of the allowed alternatives. | After the successful TLS precondition, refuse only the tested connection and require the certificate assertion to reject the non-TLS cause. |
| 4 | P1 | No lowercase error / exit zero accepted as healthy; U2-C3 / U3-C3 | healthcheck:36,39; rolling_upgrade:118,163. [HealthcheckTool:228](https://github.com/apache/solr/blob/0d6b2e32f598d772aed094038022a6a588fdfc5a/solr/core/src/java/org/apache/solr/cli/HealthcheckTool.java#L228) can print degraded without an error member; callTool returns zero at 264. A missing-collection ERROR can also lack lowercase error. | Construct degraded replicas while the collection responds, preserve health output/exit and three live nodes, and require the checker/poller to reject degraded state. |
| 5 | P2 | Failed ZK lookup accepted as deletion; U1-C3 | basic_auth:62-63; delete_collection:46,55,64,73,81. [bats_helper:74](https://github.com/apache/solr/blob/0d6b2e32f598d772aed094038022a6a588fdfc5a/solr/packaging/test/bats_helper.bash#L74) and 87 ignore listing failure and return absent, which refute accepts. | Keep the object present and fail only its post-delete lookup; require the negative helper assertion to distinguish lookup failure from absence. |
| 6 | P2 | Earlier export file accepted for a later export; U2-C4 | export:49 rechecks the same output.json established at 33/38 without removal or exit check. [test_export:48](https://github.com/apache/solr/blob/0d6b2e32f598d772aed094038022a6a588fdfc5a/solr/packaging/test/test_export.bats#L48). | Fail only the second export before touching output and show the existing artifact cannot satisfy its success check. |
| 7 | P2 | Assertion-result exit confused with tool-error exit; U1-C4 | assert:43,57 accepts any nonzero with --exitcode. [AssertTool:420](https://github.com/apache/solr/blob/0d6b2e32f598d772aed094038022a6a588fdfc5a/solr/core/src/java/org/apache/solr/cli/AssertTool.java#L420) distinguishes assertion counts from exception exit 100. | Cause an exception only in the --exitcode invocation and require the intended mode-failure result rather than any failure. |
| 8 | P2 | Complement of success output accepted as the intended outcome; U2-C5 | export:67 accepts any absence of Export complete as a mode rejection; post:180 accepts absence of success JSON as suppressed verbosity even for a failed post. [ToolBase:105](https://github.com/apache/solr/blob/0d6b2e32f598d772aed094038022a6a588fdfc5a/solr/core/src/java/org/apache/solr/cli/ToolBase.java#L105) provides unrelated ERROR/nonzero paths. | Inject a pre-mode connection error for export and an error only on the quiet args request for post. Require reason/success checks to reject both. |
| 9 | P2 | Generic ERROR accepted as authentication rejection; U3-C2 | stream:113/115. [StreamTool:312](https://github.com/apache/solr/blob/0d6b2e32f598d772aed094038022a6a588fdfc5a/solr/core/src/java/org/apache/solr/cli/StreamTool.java#L312) can fail opening the expression before connecting; ToolBase emits the same generic ERROR. | Make only the generated expression unreadable and require an auth-specific assertion to reject that file error. |

The optional controls above sharpen later fixes; none was run, scheduled, or claimed. Some confirmed weak assertions are compensated by later independent checks in the same test. Their presence is not counted as evidence of a complete-test false pass where a later query would fail.

## Ranked new suspected findings and required deciding controls

| Rank | Id | Priority | Location and plausible wrong pass | Deciding control |
| --- | --- | --- | --- | --- |
| S1 | U3-S1 | P2 | ssl:66,557,558,560,561,568,581,583 accepts bare nonzero without identifying the intended trust/certificate failure. | Inject an unrelated per-call failure after the positive TLS precondition. Capture exit/cause and complete the whole test. If a later check catches it, record non-reproduction for that case. |
| S2 | U2-S1 | P2 | postlogs:50 treats any output lacking the exact zero-count token as documents indexed. | Fail only the select request with a real HTTP/non-JSON response; capture status/body/curl exit and whether the test passes without a positive numFound. |
| S3 | U3-S2 | P2 | start_solr:40 treats no forcefully killing text as a successful graceful stop. | Leave both pre-asserted processes alive and fail the tested stop before shutdown. Preserve output/exit and live endpoint state; test must reject it. Later teardown cleanup is not evidence for the tested stop. |
| S4 | U1-S1 | P2 | compression:36 may accept empty silent-curl output as an uncompressed response. | After normal setup refuse just this request, run the individual test, and preserve curl exit/output. Check whether the missing header still passes. Later tests are independent. |

No suspected entry is promoted to confirmed by its plausibility. These controls are the requested deliverable; executing them is follow-up work on Nick's direction.

## PR 5225 reference entries and boundaries

1. **REF-1, confirmed known defect, P1 reference:** the main security_manager:54 alternation accepts the generic backup wrapper on Java 24+. This is the single inherited confirmed assertion row in the totals, not a newly discovered pattern.
2. **REF-2, confirmed historical candidate defect, P1 reference:** the follow-up candidate formerly placed the phrase before the denied path in its regex, contrary to the producer order. Existing packet report reports/pr-5225-jdk25-regex-fix-final-head-follow-up.md records its correction at fork head `35378004f1797c5b9ae3ba68d2503e4edb3840e8` and two JDK 25 parser passes. That head is separate from reviewed apache main; no pass is borrowed for these reports.
3. **REF-S1, reserved suspicion:** the candidate's whole-log checkpoint sensitivity belongs to control 2 of gates/pr-5225-bats-followup-round-1.md. It was not investigated or duplicated. At the assignment snapshot the gate is OPEN and its evidence report is incomplete. This review publishes no receipt and changes no gate state.

## Non-findings and coverage limits

The placement-plugin and deprecated-property log patterns match their producing code in both wording and argument order. Their logs live inside a home cleared before each test; a bare whole-file search here does not establish stale-log acceptance. test_start_solr's console file is truncated by launcher redirection. No additional producer-order mismatch was established.

Missing body parsing, actual shard/replica topology read-backs, exact client-version parsing, export file contents, snapshot contents, and document-survival assertions remain coverage limits, not defects in this review. The intentional BATS stub is recorded as a stub. The rolling-upgrade assert_success calls are redundant because their preceding direct commands already fail the body on nonzero; redundancy is not a false-pass finding. The stop-timeout prerequisite can skip when timeout is invoked without operands; it is a coverage/control-flow concern, not included in the defect totals.

## Verification and disposition

Static report validation checked all 30 file sections, all enumerated source assertion links, every pinned local source line against file bounds, and verdict/row totals: 100 + 136 + 173 = 409. The two additional extraction helper/readiness rows were explicitly included. No source compilation, formatting, test queue, runtime test, or gate ran.

No scripts were edited. No fix branch, PR, public comment, Jira write, or follow-up gate was created. The three unit claims are complete by their landed deliverables; this assembly claim completes when this report lands. The assignment/index files remain unchanged under the board's add-only rule. Fixes and deciding runs await Nick's direction, as the assignment requires.

