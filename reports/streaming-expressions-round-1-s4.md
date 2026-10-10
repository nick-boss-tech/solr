# Streaming expressions round 1, part S4: SOLR-12657 and SOLR-12505 drafts

Only `pr-drafts/streaming/SOLR-12657.md` and `pr-drafts/streaming/SOLR-12505.md` were written. Nothing was built, run, committed, pushed, or posted.

## Results

**SOLR-12657: held for one ruling.** Path `pr-drafts/streaming/SOLR-12657.md`, 4,153 characters with links (2,911 without URLs). The formula guide is about 3,500, so the draft is over with links. The long links are the full-SHA blob URLs.

The draft is accurate as written. It can go out unchanged if you accept its Limits paragraph. The open point is a real ordering problem. The parallel rollup compares the ISO-8601 strings as text (`MinMetric.java` lines 105 to 112 and `MaxMetric.java` lines 104 to 111 at head `16a0a69e8398`). Solr writes dates with `Instant.toString` (`DatePointField.java` line 249 on main), which prints fractional digits only when needed. So `2018-03-01T10:00:00Z` sorts after `2018-03-01T10:00:00.250Z` as text, although it is the earlier time. A bucket whose values fall in the same second with different fractional-digit counts can return the wrong min or max. The branch's own comment says text order is chronological for same-format dates. That is narrower than what Solr emits.

The receipt's two tests use whole seconds only. A fix parses both values as `java.time.Instant` when both parse. That changes the head, needs a new gate, and changes the Proof. Options are in the owner list below.

**SOLR-12505: draftable at head `5f20e1171bc890509d76e4f5f7defcc4fc8c102c`.** Path `pr-drafts/streaming/SOLR-12505.md`, 4,128 characters with links (2,898 without). Also over the guide with links. The premise holds on main. `FetchStream.java` has no `defType` on main, and `QParser.java` lines 387 to 396 restrict local-params parsing to the lucene and func default parsers. The draft includes a short "A choice to check" section (`defType=lucene`, implemented, versus `defType=terms`). That is a judgment call. The lead can cut it, because the ticket thread agreed the route in 2018 (Bernstein comment 16519604, Smiley comment 16519637).

**Security point for SOLR-12505, not in the public draft.** The ticket thread (Smiley, comment 16519471) gives the reason local params are restricted to lucene and func: SOLR-11501, so end-user input cannot set parser options. The change now parses the batch prefix on handlers whose default is not lucene. The key values are escaped with `ClientUtils.escapeQueryChars`. But the `on=` field name is written into `{! df=<name> ...}` unescaped (`FetchStream.java` line 239 at head, `rightKey`). I confirmed that line by reading. Whether that field name can carry end-user text depends on deployment, and it was not assessed here.

## Self-check, both drafts

- Em and en dashes: zero in each file.
- Process words (gate, receipt, ledger, rc=0, JUnit, pre-fix, owed, round, re-gate, fresh, dispositioned, verdict): none as words in either file. A substring check hit "followed" in an early version, which was fixed.
- Head references: every head reference uses the full SHA. 12657 uses `16a0a69e8398079cceb4bec4bd7e0f6bcf02c469`, and 12505 uses `5f20e1171bc890509d76e4f5f7defcc4fc8c102c`. Both match `ls-remote` and the receipts. Base citations use the full merge-base `14c7aac0d151402b00259e2fb9bf5eed7049ec5d` and say "base". No abbreviated SHA appears.
- Counts: 12657 StreamExpressionTest 35 of 35 (33 on main plus 2 new, checked by counting `@Test`). 12505 StreamDecoratorTest 56 tests, 5 skipped, 0 failures, as the receipt states.

## Findings for the lead

1. **12657 ordering edge case (the hold).** Detail above. Options are in the owner list.
2. **12657 public API change.** `Metric.getValue()` changed from `Number` to `Object` (`Metric.java` line 75 at head; line 71 at base). The draft states it. The receipt says the core and `modules/sql` consumers compile. Other round branches were not checked for `Metric.getValue()` callers that treat the result as a `Number`.
3. **12505 security boundary.** See the security point above. Decide whether it needs a Limits sentence, an escape or validation of the field name, or a check before opening.
4. **12505 Choice section** is optional. See the verdict.
5. **Base drift.** Both branches fork at `14c7aac0d151`, which is 66 commits behind `upstream/main` `8e62c2686882`. The files these branches touch are unchanged on main. `solr/solrj-streaming` differs from the merge-base only in `gradle.lockfile`, and `QParser.java` is identical between head and main. So the premise holds on main. But a PR opened now merges against the older base. Rebasing would change the head and need a new gate.
6. **`fetch()` sweep on main.** Only StreamDecoratorTest calls `fetch()`: `testFetchStream` (starts line 945; fetch at lines 974, 1009 and 1048) and `testParallelFetchStream` (starts line 1068; fetch at lines 1103 and 1137). StreamExpressionTest and StreamingTest have no `fetch()` call. The streaming test configset sets only `df=text` on `/select`, and no `defType` appears in `solr/solrj-streaming/src/test-files` on main. So these tests run on the lucene default, and the new parameter changes nothing for them. A repo-wide grep of test sources on main found no other `fetch(` expression. None of the other eight round branches with local refs (9852, 10322, 10882, 11922, 13524, 14200, 14231, 15326) nor 17433 adds a `fetch(` call, a `FetchStream` line, or a `defType` in its diff against its merge-base. These are the local refs, which were not re-fetched.
7. **Shared suites.** 12657 adds two tests to StreamExpressionTest, and 12505 adds one to StreamDecoratorTest. Overlap with 10322 and 11922 is covered by part S6's landing-order check.
8. **Minor, test only.** The 12505 test creates its collection before its try block, so a failed create or wait skips the cleanup. The draft does not depend on it.
9. **Receipt claims checked against the diffs.** All match: date to ISO string, string min and max, null for an empty bucket, the 33 plus 2 count, premise A (both new tests fail with ClassCastException on base), premise B (`1.7976931348623157E308` is the base `MinMetric` default `Double.MAX_VALUE`, consistent with the receipt), and the `SolrClientCache` close in a finally block.
10. **No Lucene version is named** in either draft, so the Lucene version rule does not apply.

## Owner decisions

- **SOLR-12657:** (a) ship with the Limits paragraph as written; or (b) fix the min and max comparison to parse both values as `Instant` when both parse, then re-gate and redraft the Proof. Recommendation: (b). A known wrong minimum or maximum in a shipped feature is worse than a re-gate, and the Limits paragraph would describe a defect rather than a scope.
- **SOLR-12505:** decide on the `on=` field name before opening. Options: (a) check or escape the name; (b) a Limits sentence that says what the name may contain; (c) a check of deployment exposure first. Recommendation: (a), with a test.
- **SOLR-12505 Choice section:** keep or cut.
- **Base drift:** rebase both branches onto `upstream/main` before opening (changes the heads and needs new gates), or open on the older base with the drift stated.
- **`Metric.getValue()` change:** confirm the other callers of `Metric.getValue()` on the branches treat the result as an `Object`, or are unaffected.

## Not checked

- No builds, Gradle, tests, `gh` calls, or posting. All gate evidence (tidy, Error Prone, Spotless, the GitHub Actions run `37651985013`, the test counts) comes from the receipts and was not re-run.
- The mixed-precision case is shown by reading code (`DatePointField.java` line 249, `TextWriter.java` line 194, and the `MinMetric` and `MaxMetric` comparisons). The JSON and javabin transports of the parallel path were not traced, and no case was run. The edge case is established by code reading only.
- `ParallelMetricsRollup` was not traced to confirm it receives only strings on every transport. The receipt's parallel test covers the common path.
- Other round branches were checked only for `fetch(` and `defType` in the local refs. Their shared-suite edits and any `Metric.getValue()` callers were not checked.
- The JIRA packets `SOLR-12657.json` and `SOLR-12505.json` were read (read only, in the main checkout). The 12505 comments are the source for the Choice section. The 12657 ticket text matches the ISO-8601 output.
