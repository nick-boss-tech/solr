# Critical comment coverage — batch 01

- Claim: `claims/critical-comments-round-1-batch-01.md` (`phone-agent-2`), under the directed assignment.
- Census: `material/critical-tickets-census-2026-10-24.md`.
- Jira: full descriptions and comment histories read for all ten issues; the public Jira pages were also checked where cited.
- Solr code reference: upstream `main` at `13c693d805f6541f7cec684cd27d28eef7b3b614`.
- Result: ten individual Jira-wiki comment drafts below `jira/`; none were posted. No ticket in this batch was flagged security-sensitive.

The three closure-ready drafts are listed first in the closure index: SOLR-6683, SOLR-7517, and SOLR-8417. Their own ticket evidence plus a fix present in a shipped Solr line supports a closure recommendation. All other tickets remain open for evidence, scope, or reproduction; none is closed by this report.

## SOLR-6683 — configurable PeerSync/UpdateLog retention

- **Current state:** Bug, Open, Critical; affects 4.7; last updated 2015-08-23; seven comments.
- **Existing owner work:** No pre-existing ticket-specific owner report, receipt, branch, or PR; this ticket is covered by the current batch-01 claim. The frozen archive's CSV report classified it as already fixed; that is historical context, not a live claim.
- **Disposition:** Closure-ready draft at `jira/jira-comment-SOLR-6683-draft.md`; staged ask to [~elyograg], the final commenter.
- **Evidence:** The reporter says applying SOLR-6359 on 4.7 with `numRecordsToKeep=10000` and `maxNumLogsToKeep=100` worked, subject to a stated tlog-overlap condition. The official [Solr 5.1.0 release notes](https://solr.apache.org/docs/5_1_0/changes/Changes.html) and [6.0.0 release notes](https://solr.apache.org/docs/6_0_0/changes/Changes.html) list SOLR-6359 as making UpdateLog record/log retention configurable. Current `UpdateLog.init` reads both settings ([source](https://github.com/apache/solr/blob/13c693d805f6541f7cec684cd27d28eef7b3b614/solr/core/src/java/org/apache/solr/update/UpdateLog.java#L381-L407)); PeerSync uses the configured record count ([source](https://github.com/apache/solr/blob/13c693d805f6541f7cec684cd27d28eef7b3b614/solr/core/src/java/org/apache/solr/cloud/RecoveryStrategy.java#L574-L580)). The later question about full-snapshot recovery leaving a tlog is kept distinct from the original configurability request.
- **Implementation triage:** `not-tractable` — the requested retention controls are already implemented and shipped; any separate tlog-cleanup defect needs its own report.

## SOLR-7517 — core.properties persistence failures

- **Current state:** Bug, Open, Critical; no affects/fix version; no comments; last updated 2015-05-14.
- **Existing owner work:** Fork branch `solr-7517-draft-close` at `a4aefcb5ff24363ae1e20d29617b738b9cecb47a` contains an unposted generic close-fixed comment; no PR. Its evidence is sharpened here.
- **Disposition:** Closure-ready draft at `jira/jira-comment-SOLR-7517-draft.md`; status note to [~markrmiller], the reporter and assignee.
- **Evidence:** Current `CorePropertiesLocator.writePropertiesFile` logs an `IOException` and throws `SolrException(BAD_REQUEST)` ([source](https://github.com/apache/solr/blob/13c693d805f6541f7cec684cd27d28eef7b3b614/solr/core/src/java/org/apache/solr/core/CorePropertiesLocator.java#L93-L109)). This behavior was added by commit [`412e4ae2c1`](https://github.com/apache/solr/commit/412e4ae2c192a5b444dd63220e5918f4e7fd47be) for [SOLR-10719](https://issues.apache.org/jira/browse/SOLR-10719), not by the older SOLR-4914 persistence refactor; SOLR-10719 appears in the official [6.6.1 release notes](https://solr.apache.org/docs/6_6_1/changes/Changes.html). No unwritable-filesystem repro was run; no current test specifically exercises this failure path was found.
- **Implementation triage:** `not-tractable` — no new implementation candidate remains for the stated silent-failure behavior; it is fixed in a shipped line.

## SOLR-8417 — repeated remote forwarding when no core loads

- **Current state:** Bug, Open, Critical; affects 5.3.1; one reporter comment; last updated 2015-12-15.
- **Existing owner work:** No pre-existing ticket-specific owner report, receipt, branch, or PR; this ticket is covered by the current batch-01 claim. The frozen archive's CSV report classified it as already fixed; that is not a live claim.
- **Disposition:** Closure-ready draft at `jira/jira-comment-SOLR-8417-draft.md`; status note to [~ichattopadhyaya], maintainer of the forwarding-limit change.
- **Evidence:** Current `HttpSolrCall` tracks remote forwarding and throws `INVALID_STATE` with “No active replicas found” after the forwarding count exceeds the replica count ([source](https://github.com/apache/solr/blob/13c693d805f6541f7cec684cd27d28eef7b3b614/solr/core/src/java/org/apache/solr/servlet/HttpSolrCall.java#L950-L955)). The change is [SOLR-13793](https://issues.apache.org/jira/browse/SOLR-13793); its replica-count limit is listed in the official [7.7.3](https://solr.apache.org/docs/7_7_3/changes/Changes.html) and [8.3.0](https://solr.apache.org/docs/8_3_0/changes/Changes.html) release notes. The exact unreadable-core setup from the ticket was not reproduced; the focused down-collection test for the current bounded-forwarding guard passed (see the focused test run below).
- **Implementation triage:** `not-tractable` — the infinite-forwarding behavior is already guarded in shipped releases; only a later regression would create implementation work.

## SOLR-4180 — Jetty StatisticsHandler on multicore

- **Current state:** Wish, Open, Critical; affects 3.6; no comments; last updated 2012-12-13; reporter is inactive.
- **Existing owner work:** No pre-existing ticket-specific owner report, receipt, branch, or PR; this ticket is covered by the current batch-01 claim.
- **Disposition:** Scoping draft at `jira/jira-comment-SOLR-4180-draft.md`; addressed to [~janhoy] based on recent Solr metrics-handling activity.
- **Evidence:** The ticket asks for the built-in Jetty `StatisticsHandler` in a multicore setup. Current Solr metrics include node/core request-handler counts and timing ([metrics guide](https://solr.apache.org/guide/solr/latest/deployment-guide/metrics-reporting.html)), but this does not establish equivalence to the Jetty server statistics in the request. No current `StatisticsHandler` integration was found. The draft asks which measures remain missing before suggesting implementation.
- **Implementation triage:** `design-first` — identify the distinct measurements and consumer, if any, not covered by the current metrics API.

## SOLR-6420 — ADDREPLICA reports green without replicating

- **Current state:** Bug, Open, Critical; affects 4.10; last updated 2014-10-14; one committer comment from [~shalin].
- **Existing owner work:** No pre-existing ticket-specific owner report, receipt, branch, or PR; this ticket is covered by the current batch-01 claim. The frozen archive classified this as not reproducible, but that remains historical context.
- **Disposition:** Staged evidence-ask draft at `jira/jira-comment-SOLR-6420-draft.md`, addressed to the last commenter.
- **Evidence:** The report is from `branch_4x` and depends on a 4.9-built index that was readable but not writable; the ticket has no attachments. Shalin states that exact deployed bits and index files are needed to verify the claimed false-green replica. Those inputs are not available, so a faithful current-main reproduction was not possible.
- **Implementation triage:** `not-tractable` — the required historical build and index are missing; a synthetic current-main run would not establish the reported 4.9/branch_4x behavior.

## SOLR-5821 — search inconsistency on SolrCloud replicas

- **Current state:** Bug, Reopened, Critical; affects 4.6.1/4.7.1; eight comments; last updated 2014-12-05 by committer Erick Erickson.
- **Existing owner work:** Fork branch `solr-5821-submit` at `327547d693e098447ff70c536b794aa364508670`, one documentation change; no PR. The frozen archive's review found it describes tie-order behavior but does not fix or close the reopened inconsistency report.
- **Disposition:** Staged status draft at `jira/jira-comment-SOLR-5821-draft.md`; it discloses the existing docs-only change because the ticket has not been told about it.
- **Evidence:** The final ticket comment distinguishes replica-local tie ordering from different `numFound` after indexing has stopped and all replicas have committed/opened searchers; the latter would be a bug. The branch clarifies tied-sort pagination only. No current-main multi-replica reproduction was run. Prior archive references: frozen `reports/csv-critblock-01.md` and `reports/newdev-critblock-review-round-1/solr-5821-submit.md` / `receipts/SOLR-5821.md` on `archive/coordination-2026-10-23`.
- **Implementation triage:** `reproduce-first` — a current cluster test must separate expected tie-order variance from persistent count divergence.

## SOLR-7351 — MoreLikeThis does not work with dates

- **Current state:** Bug, Open, Critical; affects 5.0; two comments; last updated 2015-04-07 by reporter Bogdan Marinescu.
- **Existing owner work:** No pre-existing ticket-specific owner report, receipt, branch, or PR; this ticket is covered by the current batch-01 claim. SOLR-4860 is related, not evidence that this ticket is a duplicate.
- **Disposition:** Stale-check draft at `jira/jira-comment-SOLR-7351-draft.md`, addressed to [~erickerickson], who discussed whether MLT should support dates/numeric fields or reject them clearly.
- **Evidence:** The ticket's TrieDate example fails with `Invalid Date String:'-670816800000'`. Current `AbstractMLTQParser` supplies the selected fields to Lucene MLT with Solr's index analyzer ([source](https://github.com/apache/solr/blob/13c693d805f6541f7cec684cd27d28eef7b3b614/solr/core/src/java/org/apache/solr/search/mlt/AbstractMLTQParser.java#L104-L132)); current `DatePointField` parses readable date strings and stores a numeric long ([source](https://github.com/apache/solr/blob/13c693d805f6541f7cec684cd27d28eef7b3b614/solr/core/src/java/org/apache/solr/schema/DatePointField.java#L192-L229)). This is source-level evidence only. The prior archived report records a date repro, but its `MltDateReproTest.java` and run log are absent from this workspace; no current-main reproduction was run.
- **Implementation triage:** `reproduce-first` — current-main behavior must be established before choosing support for dates versus a clear unsupported-field error.

## SOLR-5850 — ConcurrentUpdateSolrServer queue-full race

- **Current state:** Bug, Open, Critical; affects 4.6; 11 comments; last updated 2015-05-30 by reporter Vijay Sekhri.
- **Existing owner work:** Fork branch `solr-5850-draft-close` at `dbd7daf5b4cfcecaa0b7ce83a2ada629b4e38f19` contains an unposted close-obsolete comment; no PR. That conclusion is not safe from removal of the old class alone.
- **Disposition:** Stale-check draft at `jira/jira-comment-SOLR-5850-draft.md`, addressed to [~thelabdude], the assignee and original patch-discussion participant.
- **Evidence:** `ConcurrentUpdateSolrServer` was removed in SOLR-6895, but current `ConcurrentUpdateJettySolrClient` can requeue via `queue.add(upd)` when params/destination change ([source](https://github.com/apache/solr/blob/13c693d805f6541f7cec684cd27d28eef7b3b614/solr/solrj-jetty/src/java/org/apache/solr/client/solrj/jetty/ConcurrentUpdateJettySolrClient.java#L79-L91)); the base queue throws `IllegalStateException("Queue is full")` when full ([source](https://github.com/apache/solr/blob/13c693d805f6541f7cec684cd27d28eef7b3b614/solr/solrj/src/java/org/apache/solr/client/solrj/impl/ConcurrentUpdateBaseSolrClient.java#L156-L163)). This is a related current path, not a reproduction or proof it is the same old HTTP-client failure. SOLR-7571 is related, not a duplicate.
- **Implementation triage:** `reproduce-first` — test the exact current SolrJ client, queue/thread settings, and requeue scenario before closing or proposing a fix.

## SOLR-8331 — CorruptIndexException after auto commit

- **Current state:** Bug, Open, Critical; affects 4.10.4; two comments; last updated 2015-11-26 by the reporter.
- **Existing owner work:** No pre-existing ticket-specific owner report, receipt, branch, or PR; this ticket is covered by the current batch-01 claim.
- **Disposition:** Stale-check draft at `jira/jira-comment-SOLR-8331-draft.md`, addressed to [~uschindler], who requested JVM details in the ticket.
- **Evidence:** The reporter says Solr 4.10.4 ran on Tomcat 7.0.55 with Java 1.7.0_67 and supplied JVM arguments. No later reproduction or supported-runtime confirmation appears in the record. No current-main reproduction was run.
- **Implementation triage:** `not-tractable` — the only demonstrated environment is an obsolete Solr/JVM pair, with no current reproducer or index fixture to test.

## SOLR-7926 — EdgeNGram highlighting

- **Current state:** Bug, Reopened, Critical; affects 5.1/5.2.1; eight comments; last updated 2016-02-05 by reporter Bjørn Hjelle.
- **Existing owner work:** No pre-existing ticket-specific owner report, receipt, branch, or PR; this ticket is covered by the current batch-01 claim. LUCENE-7016 remains open and concerns a later FastVectorHighlighter exception in a workaround, not necessarily this ticket's original symptom.
- **Disposition:** Stale-check draft at `jira/jira-comment-SOLR-7926-draft.md`, addressed to [~mikemccand], who discussed the EdgeNGram token-graph behavior.
- **Evidence:** The original report says EdgeNGram highlights the whole word. Later comments describe a separate NGram/FastVectorHighlighter workaround failure and open LUCENE-7016. The current ticket does not establish that the original symptom is fixed, nor that LUCENE-7016 duplicates it. No current Solr/Lucene reproduction was run.
- **Implementation triage:** `reproduce-first` — reproduce the original EdgeNGram case separately from the later FastVectorHighlighter workaround.

## Historical-record boundary

The classifications in `archive/coordination-2026-10-23` are frozen history only, not live claims. The live `coordination-ui` check found no batch-01 ticket claim/report/receipt before this claim; no Apache PR was found for these ten keys. The only ticket-named fork branches with owner work were `solr-5821-submit`, `solr-5850-draft-close`, and `solr-7517-draft-close`, all described above. This batch adds drafts only; no Jira comment was posted.


## Focused current-main tests

At the verified upstream-main SHA `13c693d805f6541f7cec684cd27d28eef7b3b614`, ran `./gradlew :solr:core:test --tests 'org.apache.solr.search.TestRecovery.testRemoveOldLogs' --tests 'org.apache.solr.cloud.TestQueryingOnDownCollection' --no-daemon --console=plain`. Gradle reported `:solr:core:test (SUCCESS): 2 test(s)` and `BUILD SUCCESSFUL`. `TestRecovery.testRemoveOldLogs` passed, exercising retention by configured log/record limits; `TestQueryingOnDownCollection.testQueryToDownCollectionShouldFailFast` passed, exercising the bounded-forwarding guard. The run emitted a Java CLDR locale-provider warning but completed successfully. These tests do not recreate the exact historical reports; no current test specifically covering SOLR-7517's unwritable `core.properties` path was found. Test log: `/tmp/critical-batch01-focused-tests-main.log`.
