# Review of the opened update-processing PRs: group 7

Group 7: SOLR-16356 (PR 5092), SOLR-16655 (PR 5093), SOLR-16673 (PR 5094), SOLR-16910 (PR 5095). Read-only: `gh pr view` and `git ls-remote` and `git fetch` of the four submit branches, no builds, no GitHub writes.

Shared item for all four: the PR titles are the Jira summaries, not the changelog title line. This is an owner call, described once per section below. The title rule is in `assignments/open-update-29-prs.md` (commit faa275ad1fd, "Titles" bullet). The openings report (`reports/open-update-29-prs.md`, line 3, commit e2cafde3a88) says titles use the Jira summary for all 28.

## SOLR-16356 (PR 5092)

**Verdict: OWNER CALL** (title only; no text fix needed).

1. Title. PR title is `SOLR-16356: DBQ is noisy during core close`. The changelog title at head 39c0585072f0 (`changelog/unreleased/SOLR-16356.yml`, line 1) is "A delete-by-query that runs while its core is closing no longer logs an error with a stack trace when the update log cannot open a new realtime searcher". Owner call: keep the Jira-summary title and drop the rule from the amendment, or retitle to `SOLR-16356: A delete-by-query that runs while its core is closing no longer logs an error with a stack trace when the update log cannot open a new realtime searcher`. A retitle edits a public PR title, so it needs explicit go-ahead.

Checks passed: head 39c0585072f0 equals the receipt and the PR head; head branch solr-16356-submit; base main; body equals the draft exactly; blob citations at head (`UpdateLog.java` L919-L925, L949-L952, L953-L955, L964-L971; `UpdateLogClosedCoreTest.java` L31-L42) land on the described code; proof numbers (1/1, 5/5, rc=0, 2026-10-04, head) match the receipt; formula sections, bold summaries, AI header and footer hold; diff is three files, no workspace files; GitHub file list matches the local diff; changelog YAML valid.

## SOLR-16655 (PR 5093)

**Verdict: FIX FIRST** (one sentence in Limits). Also carries the shared title owner call (item 2).

1. PR body, Limits, first bullet. Current text: "This change is confined to the child-document descent in `mutateDocument`." The diff does more than the descent. It moves the field loop out of `processAdd` into `mutateDocument` (`FieldMutatingUpdateProcessor.java` L82-L84, L101-L146) and adds the identity check (L90-L96). Corrected text: "This change adds the child-document descent and the identity check to `mutateDocument`, and moves the field loop there from `processAdd` with no change to what the loop does."

2. Title (owner call). PR title is `SOLR-16655: Indexing nested documents and URPs`. The changelog title (`changelog/unreleased/SOLR-16655-field-mutating-urps-nested-docs.yml`, line 1) is 260 characters, so the full PR title would be 272. GitHub may not accept a title that long (not checked). Owner call: keep the Jira-summary title, or retitle to `SOLR-16655: Field mutating update processors, such as the date, number and boolean parsers, trim, regex-replace and remove-blank, now also apply to child documents, both anonymous child documents and child documents given as field values, instead of only the root document`.

3. Cosmetic. PR body, Proof, second bullet. Current: "That is the commit before this change." The branch has six commits, so "this change" is ambiguous. Corrected: "That is the parent of the last commit on the branch, `5e2317443f41`, which adds the selector gate." The receipt's pre-fix head matches (`b201a57fb3e5`).

4. Cosmetic. PR body, What this change does, first bullet. Link label "gated descent" is internal wording. Corrected label: "child documents under selected fields".

5. Confirm. PR body, Proof, first bullet says "recorded 2026-10-09 at 08:03 MDT". The receipt has the date (2026-10-09) but no time. Confirm the time in the gate log, or drop it.

6. Note. The body is 4,639 characters, above the roughly 3,500 guide. Acceptable for a multi-part change; no action needed.

Checks passed: head 5e2317443f41 equals the receipt and the PR head; base main; body equals the draft exactly; `FieldMutatingUpdateProcessor.java` L90-L96, L128-L145, L148-L153 land on the described code; `FieldMutatingUpdateProcessorTest.java` L95-L113 and L115-L139 and `ParsingFieldUpdateProcessorsTest.java` L212-L238 match the text; test counts 36/36 and 44/44 match the receipt and the method counts at head; the pre-fix head is the parent of the head; cross-PR order holds (the body says 16655 lands first and SOLR-12705 rebases onto it; a read-only `git merge-tree` of 5e2317443f41 with the live SOLR-12705 head aa56b7b1be4 conflicts in `FieldMutatingUpdateProcessor.java`, as the body says); diff is four files, no workspace files; GitHub file list matches; changelog valid; the choice section is a real decision with a live alternative; Limits name the gaps with no Jira promise.

## SOLR-16673 (PR 5094)

**Verdict: FIX FIRST** (one sentence in What this change does). Also carries the shared title owner call (item 3).

1. PR body, What this change does, second paragraph. Current text ends: "The value is then kept." The body does not say that this change reaches Schema Designer. The Long and Double changes sit in `parsePossibleLong` and `parsePossibleDouble`, which `DefaultSchemaSuggester` calls (`solr/core/src/java/org/apache/solr/handler/designer/DefaultSchemaSuggester.java` L235-L236, L300, L343). Corrected: append to that paragraph: "The Long and Double changes also reach Schema Designer's type guess, in [DefaultSchemaSuggester.isIntOrLong](https://github.com/nick-boss-tech/solr/blob/d7170b12f312b364cb280b943a042707e553ea64/solr/core/src/java/org/apache/solr/handler/designer/DefaultSchemaSuggester.java#L338) and the float and double check. In that path an empty value no longer throws. It counts as not a number, and the guess moves on to the other type checks."

2. Confirm before flip (no text change if confirmed). The Proof values "passes 43 of 43", "Tidy returns 0", "Error Prone compile returns 0", "fail-before check reports PASS" and "`:solr:core:check -x test` returns 0" are not in `receipts/SOLR-16673.md`. The receipt names only the head, the 2026-10-09 top-up and the takeover log. `ParsingFieldUpdateProcessorsTest.java` has 43 test methods at head d7170b12f312, and the top-up changed only the changelog, so "43 of 43" is consistent. Check the other results in the takeover log entry.

3. Title (owner call). PR title is `SOLR-16673: Strange error when using Schema Designer`. The labelled Title line in the body (line 5) and the changelog title (`changelog/unreleased/SOLR-16673.yml`, line 1) are the same text, so the body side of the amendment holds. The PR title does not. Owner call: keep the Jira-summary title, or retitle to `SOLR-16673: The Parse{Int,Long,Float,Double}FieldUpdateProcessorFactory classes no longer fail with a NullPointerException on an empty string value`. If the title changes, the labelled Title line repeats it. Removing that line changes the body away from the draft, so it would need a draft edit too.

4. Cosmetic. PR body, Limits, first bullet. Current: "where [DefaultSchemaSuggester.isIntOrLong] feeds the Long parser". Corrected: "where [DefaultSchemaSuggester.isIntOrLong] calls the Long parser". The code calls the parser; it does not feed it.

Checks passed: head d7170b12f312 equals the refreshed receipt head and the PR head; base main; body equals the draft exactly; citations at head land on the described code (`ParseInt...` L93 and L101, `ParseLong...` L104, `ParseFloat...` L96, `ParseDouble...` L107, `ParsingFieldUpdateProcessorsTest.java` L499-L514, `DefaultSchemaSuggester.java` L338); the unparseable paths return the skip marker, so the value is kept; diff is six files, no stray files; GitHub file list matches; changelog valid; the body is 3,671 characters, slightly over the guide.

## SOLR-16910 (PR 5095)

**Verdict: FIX FIRST** (one bold summary). Also carries the shared title owner call (item 2).

1. PR body, What this change does, bold summary. Current: "**The INFO summary and the slow-update WARN now use the same message, and the request details are cleared only after both.**" The code builds the message and clears `rsp.getToLog()` before either log call (`LogUpdateProcessorFactory.java` L211-L212, then L214-L219). The clear comes before the logs, not after. The body's next sentence already says so. Corrected: "**The INFO summary and the slow-update WARN now use the same message, built before the request details are cleared.**"

2. Title (owner call). PR title is `SOLR-16910: Adjusting LogUpdateProcessorFactory's log level has side effects on SolrCore logging and changes "slowUpdateThresholdMillis" formatting`. The changelog title (`changelog/unreleased/SOLR-16910-log-update-processor-slow-format.yml`, folded `title: >`, lines 1-3) is "LogUpdateProcessorFactory now logs the same request details in the slow-update warning as in the INFO summary instead of clearing them after the INFO message." Owner call: keep the Jira-summary title, or retitle to `SOLR-16910: LogUpdateProcessorFactory now logs the same request details in the slow-update warning as in the INFO summary instead of clearing them after the INFO message.`

3. Note, not a finding for the net diff. The submit branch history contains `8eabf2b30cd` ("SOLR-16910: add hypothetical-reproduction handoff doc"), which adds `SOLR-16910-TESTING.md`. A later commit deletes it, and the PR's file list and the merge-base diff are clean. A clean commit list would need a history rewrite (force-push), which needs explicit direction. A squash merge removes it.

Checks passed: head 9fce3e9a7058 equals the receipt and the PR head; base main; body equals the draft exactly; `LogUpdateProcessorFactory.java` L201-L220 and L223 and the three test ranges (L56-L79, L87-L105, L113-L132) match the text; proof matches the receipt (3/3 at head, 2026-10-07, module check rc=0, base run of 3 tests with exactly 1 failure in `testInfoAndSlowWarnBothContainRequestToLog`); changelog folded scalar is valid YAML; diff is three files; GitHub file list matches; Limits name the SolrCore item with an offer and no Jira promise.
