# Group 5 review of opened drafts (review-opened-28-g5)

Group 5: SOLR-11483 (PR 5083), SOLR-12245 (PR 5084), SOLR-12864 (PR 5086), SOLR-13265 (PR 5087).

Draft citations below are line numbers in `pr-drafts/update-processing/SOLR-<ticket>.md` on `origin/pr-prepare`. The PR body on each PR is byte-identical to its draft. Code citations are at the PR head SHA.

## SOLR-11483 (PR 5083)

**Verdict: FIX FIRST**

Findings:

1. PR title (live PR metadata, not a file). Current: `SOLR-11483: Keep more transaction log files when maxNumLogsToKeep is specified`. This is the Jira summary. The changelog title at `4431a250f665`, `changelog/unreleased/SOLR-11483-ulog-max-logs-default.yml` line 2, is "When updateLog numRecordsToKeep is set and maxNumLogsToKeep is not, ...". The current title also states the reverse condition, since the change applies when maxNumLogsToKeep is not set. Corrected title: `SOLR-11483: When updateLog numRecordsToKeep is set and maxNumLogsToKeep is not, maxNumLogsToKeep now defaults to 1000 instead of 10, so the configured number of records is not cut short by the log file limit.` (208 characters.)

2. Limits has no bold one-line summary (draft line 33, `## Limits`). Add directly under the heading: `**Disk cost, the key check and one test detail are not covered.**`

3. Cosmetic. Line 9 repeats the summary on line 7. Delete the last sentence of line 9: "A config that raises `numRecordsToKeep` but leaves the file limit at its default can therefore keep fewer records than it asks for."

Checks passed: head `4431a250f665` equals the receipt and the submit tip; headRefName `solr-11483-submit`, baseRefName `main`; body identical to draft; Proof matches the receipt (21 of 21, verified 2026-10-06, base run 21 tests with one failure, `expected:<1000> but was:<10>`); citations `UpdateLog.java` L764-L765, L386-L393, `commits-transaction-logs.adoc` L274, `TestRecovery.java` L404-L412 hold the described code; no shipped config under `solr/server` sets either key (git grep at head); diff is four files with no stray files; changelog parses; no em dashes; no first-person plural; the Choice section is a real decision.

## SOLR-12245 (PR 5084)

**Verdict: FIX FIRST**

Findings:

1. PR title (live PR metadata). Current: `SOLR-12245: DistributedUpdateProcessor doesn't set MDC in some errors`. This is the Jira summary. The change does not set MDC, and the Limits say so (draft line 40). The changelog title at `f325d5d0576e`, `changelog/unreleased/SOLR-12245-distrib-update-error-names-replica.yml` lines 2-3, describes replica naming. Corrected title: `SOLR-12245: The error reported when a distributed update fails asynchronously now names the replica URL, collection and shard the request was sent to.`

2. Limits, draft line 41. Current: "Null-guard coverage is not included in this change. No test covers a null guard." The second sentence is false. `testStatusCodeOnDistribError_NotSolrException` (`DistributedUpdateProcessorTest.java` L135-L152 at head) builds a SolrError with no `req`, so `describe()` returns at the `error.req == null` guard (`DistributedUpdateProcessor.java` L1246-L1248). Corrected: "Null-guard coverage is not included in this change. The new tests do not reach the null checks. The existing test testStatusCodeOnDistribError_NotSolrException covers only the missing-request check."

3. Limits has no bold one-line summary (draft line 38, `## Limits`). Add: `**The MDC ask and null-guard coverage are not in this change.**`

4. Cosmetic, draft line 15. Current: "It holds the replica URL, then `collection=` and `shard=`...". The URL is not used for a remote error (see next point). Corrected: "It holds the replica URL, or the core name for a remote error (next point), then `collection=` and `shard=`..."

5. Cosmetic, draft line 16. Current: "A remote error already names its server in its own message." The code tests whether the message contains the base URL (`DistributedUpdateProcessor.java` L1254-L1257), not the error type. Corrected: "When the server's own message already contains its base URL, the suffix uses the core name, so the host appears once."

6. Cosmetic, draft line 9. The last sentence repeats the summary on line 7. Delete: "The text does not name the replica, collection or shard that the request was sent to."

Note, not blocking: the draft is 3,792 characters, above the length guide of roughly 3,500. Trimming is optional.

Checks passed: head `f325d5d0576e` equals the receipt and the submit tip; headRefName and baseRefName correct; body identical to draft; Proof matches the receipt (6 of 6, verified 2026-10-09, pre-fix head `4a93167458b` fails on `testDistribErrorMessageNamesTheHostOnce`, which fix commit `f325d5d0576e` adds); citations `DistributedUpdateProcessor.java` L1243-L1269, L1254-L1261, L1299-L1307 and `DistributedUpdateProcessorTest.java` L155-L212, L215-L240 hold the described code; MDC and null-guard Limits present; diff is one changelog plus two Java files, no TESTING or handoff files; changelog folded title parses; the Choice section is a real decision (behavior change stated).

## SOLR-12864 (PR 5086)

**Verdict: FIX FIRST**

Findings:

1. PR title (live PR metadata). Current: `SOLR-12864: Custom JSON parser's echo parameter does not show values`. This is the Jira summary. The body says the symptom does not reproduce on base (draft line 9) and that the PR is coverage only (line 13). The branch adds no changelog fragment, so the changelog rule has nothing to match. The title still must not assert a bug the PR does not fix. Corrected title: `SOLR-12864: Add a test for echo with mapUniqueKeyOnly on the JSON update path`.

2. "A choice to check" (draft lines 27-32) must be removed. The branch changes no production code. The only alternative is whether to submit a passing test at all, which is not a design decision with a live alternative under the formula. Delete lines 27-32 so that "## Limits" (line 33) follows the Proof section. Not a finding: whether a coverage-only pin should be submitted is the owner's call, and this review does not make it.

3. Cosmetic, draft line 25. Current: "so this draft does not claim one." Corrected: "so this PR does not claim one." A public PR body should not refer to a draft.

4. Limits has no bold one-line summary (draft line 33). Add: `**The test covers one combination, and each run takes one random branch.**`

5. Cosmetic, draft line 9. The final sentence "The combination has no test." repeats the summary on line 7. Delete it.

Checks passed: head `b9c6c1e71ffa` equals the receipt and the submit tip; headRefName and baseRefName correct; body identical to draft; Proof matches the receipt (32 of 32, verified 2026-10-08, fail-before inconclusive by construction and stated as such); citations `JsonLoaderTest.java` L401-L436 and `JsonLoader.java` L295-L326 hold the described code; SOLR-16811 (`8c83faadfa3`) is in base; diff is one file; no changelog needed under `dev-docs/changelog.adoc` ("other" covers test infrastructure).

## SOLR-13265 (PR 5087)

**Verdict: FIX FIRST**

Findings:

1. PR title (live PR metadata). Current: `SOLR-13265: TLOG replica, updateHandler errors in metrics, no logs`. This is the Jira summary. The changelog title at `c134b34aa27f`, `changelog/unreleased/SOLR-13265-tlog-follower-update-errors.yml` line 2, is "Updates applied only to the transaction log (TLOG replica followers) are no longer counted in the update handler error metric." Corrected title: `SOLR-13265: Updates applied only to the transaction log (TLOG replica followers) are no longer counted in the update handler error metric.`

2. What this change does, draft line 15. The bullet credits this change with flagging TLOG follower adds. The branch does not touch `DistributedUpdateProcessor.java`. `git diff --name-status` against merge base `9b3a84b1c460` lists only `DirectUpdateHandler2.java`, `SolrIndexMetricsTest.java` and the changelog. The flag is already set in base at `DistributedUpdateProcessor.java` L509-L513. Corrected bullet: "- The TLOG follower's add is already flagged to skip the IndexWriter in base ([DistributedUpdateProcessor.java#L509-L513](same link as now)). This change does not touch that code."

3. Proof, draft lines 22, 24 and 25. The text says the base run fails "at its own assertion ... not in setup" and that this shows the datapoint is present and nonzero on base. The receipt (`receipts/SOLR-13265.md`) records only that this test is the one failure. The gate log it names, `g13265r35-gate.log`, was not found under the workspace (bounded search), so the failure site could not be checked. Owner: confirm the failure site from that log before flipping. If it cannot be confirmed, make these changes: line 22 becomes "**The focused test passes at this head and fails on the base code. Verified 2026-10-07 at this head.**"; delete the clause "It fails at its assertion ... not in setup." from line 24; on line 25, delete "The base failure does." and the clause "so the datapoint is present and nonzero on base".

4. Limits has no bold one-line summary (draft line 27, `## Limits`). Add: `**The test calls the update handler directly, not a TLOG follower in SolrCloud.**`

5. Cosmetic, draft line 13. The second sentence of the bold summary repeats line 17. Corrected: `**Bypassed adds no longer count as update errors.**`

Note, not blocking: the draft is 3,778 characters, above the length guide of roughly 3,500.

Checks passed: head `c134b34aa27f` equals the receipt and the submit tip; headRefName and baseRefName correct; body identical to draft; Proof counts match the receipt (3 of 3 at head, 1 of 3 fails on base, verified 2026-10-07); citations `DirectUpdateHandler2.java` L298-L300, L467-L470, L501-L507 and `SolrIndexMetricsTest.java` L209-L235 hold the described code (L231 is the start of the assertion, its message is on L232); delete paths return before the error counter (`DirectUpdateHandler2.java` L611-L614, L691-L695, L742-L746); diff is one changelog plus two Java files, no TESTING or handoff files; changelog folded title parses; no em dashes; no first-person plural.

## Cross-PR notes (group 5)

- All four PR titles equal the Jira summary, not the changelog title. The roll-up should check the other 24 titles for the same pattern.
- SOLR-11483: the Proof matches the 2026-10-06 receipt (21 of 21, base one failure).
- SOLR-12245: Limits carry the MDC ask and the null-guard line as the owner's record requires. The null-guard sentence needs correction (finding 2).
