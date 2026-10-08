# solr-8536-submit

- Branch: origin/solr-8536-submit
- Head: 28552ddc26eb (matches the listed head; fork tip unchanged after fetch 2026-10-08)
- Base: upstream/main at 9d7cc2884e8a (merge-base 14c7aac0d151, 45 commits behind, 5 commits ahead)
- Scope: 5 commits, 3 files. `ExecutorUtil.java` (`:346-350`, the thread-name context string: `replaceAll("\\p{Cntrl}", " ")` added after the MDC loop), `ExecutorUtilTest.java` (+19, `testMdcControlCharactersNotInThreadName`), changelog `SOLR-8536-mdc-thread-name-control-chars.yml` (`type: fixed`). No `SOLR-8536-TESTING.md` on the tip.
- Verdict: Needs work (the change removes control characters only; the non-String MDC cast and the key-filtering part of the ticket are untouched)
- Reviewer: claude-haiku-5-5 (Claude Code), round 36 Group B review, 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. Every claim rests on reading the diff and the code at the listed head.

## Delta check against the bulk review

Bulk review `research/branch-reviews/round-28/SOLR-8536-review.md` (verdict Needs work) was written at the same head (28552ddc26eb). No delta.

- Bulk F1 (P1, non-String MDC value still throws before sanitization): **the code path is confirmed; reachability is a hypothesis.** See finding 1.
- Bulk F2 (P2, all application MDC values still appended; no Solr-key filter): **confirmed.** See finding 2 and owner call 1.

## Findings (ranked)

1. **MEDIUM (bulk P1), verified in the code; reachability is a hypothesis. The cast to `String` runs before the new replacement.** In `ExecutorUtil.execute` (`:336-342`), `MDC.getCopyOfContextMap()` is typed `Map<String, String>`, and `for (String value : values)` appends each value. The new `replaceAll("\\p{Cntrl}", " ")` (`:350`) runs on the joined string after that loop. So a non-String value in the MDC copy throws `ClassCastException` inside `execute`, before the task is submitted, and the sanitizer never sees it. This is the failure the JIRA comment describes (`research/jira-context/SOLR-8536.json`, the comment around `:30`). Whether the current logging stack can place a non-String value into this map was not verified here; the slf4j `MDC.put` signature takes a String, so the path needs a different `MDCAdapter` or a raw put. Before a fix is chosen, the reachability should be shown with a real MDC adapter. The test does not cover this case (see finding 3).
   - Proposed fix (not applied): iterate `Map<String, ?>`, convert each value with `String.valueOf`, and sanitize per value, so no cast can fail. Add a test that puts a non-String value through the same adapter the JIRA report used.

2. **MEDIUM, verified. Application values still reach the thread name.** The loop (`:336-342`) appends every value in the copy, with no key filter. The JIRA request is that only Solr-owned MDC keys go into the thread name. The branch does not add that filter, and the changelog (`:7-9`) describes control-character removal only. This part of the ticket is not addressed. See owner call 1.

3. **LOW, verified. Coverage is narrow.** `ExecutorUtilTest.testMdcControlCharactersNotInThreadName` covers a String with `\n` and `\t`. It does not cover a non-String value (finding 1), key filtering (finding 2), or other line separators. `\p{Cntrl}` matches only `\x00-\x1F` and `\x7F` (Unicode category Cc), so U+2028 and U+2029 (category Zl/Zp) pass through unchanged into the thread name. Add those code points to the test, or use an explicit character class.

## Owner calls (not decided here)

1. **Key filtering.** Is the JIRA's Solr-owned-key filter required for this ticket, or is the scope narrowed to control characters? If the filter is required, finding 2 is needed. If the scope is narrowed, the ticket text and the changelog should say so, and the residual exposure (application and session identifiers in thread names and logs) should be recorded.

## Proposed fixes (not applied; the owner decides)

- Finding 1: convert values with `String.valueOf` in the loop, sanitize per value, and add a test with a non-String MDC value.
- Finding 2: filter to Solr-owned keys, if owner call 1 says so.
- Finding 3: add U+2028 and U+2029 to the test and to the character class.

## Interactions with other branches

- None found in the bulk round notes for this ticket.

## Not checked

- Not compiled, formatted, or run. No Gradle, no `drain`, no test runs.
- The reachability of a non-String MDC value with this project's logging stack (finding 1) was not shown.
- The JIRA ticket text and the comment thread were taken from the packet and the bulk review, not re-read in full.
- The history note on the removed hypothetical-reproduction doc was not checked against the commit log beyond the bulk review's description.
- No GitHub or JIRA writes were made.
