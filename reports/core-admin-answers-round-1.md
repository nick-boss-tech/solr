# Core admin answers pass into drafts: round roll-up

Claim: `claims/core-admin-answers-round-1.md` (commit `eeadb9a01c0`). Answers: `material/core-admin-round-1-answers.md` (commit `9968fe293f6`). Parts: `reports/core-admin-answers-round-1-c1.md` (items 1 and 2) and `reports/core-admin-answers-round-1-c2.md` (items 3 to 8).

Done 2026-10-10 by Claude Code (AI agent), working for Nick Shanin. Two subagents did the draft-fix pass. No build, Gradle run, or test was run. No branch, receipt, live PR, or comment was touched. Nothing was posted. No DISCUSS item was decided.

## Changes made

Items 1 and 2 (part c1), in eleven drafts:
- The `<!-- INTERNAL -->` blocks are removed from `SOLR-6438`, `SOLR-8275`, `SOLR-8576`, `SOLR-16725` and `SOLR-17731`.
- A bold one-line opener is added to the Limits section of `SOLR-4989`, `SOLR-13246`, `SOLR-12007`, `SOLR-17297`, `SOLR-17377`, `SOLR-6438`, `SOLR-8275`, `SOLR-8576`, `SOLR-16725` and `SOLR-17731`. `SOLR-15024` gets an opener on its Choice and Limits sections.

Items 3 to 8 (part c2):
- `SOLR-17297`: the Proof placeholders are reworded to the recorded outcome, since the base-failure log is not on disk. The duplicate Changelog line inside "What this change does" is removed.
- `SOLR-17377`: the Proof placeholder is reworded to the recorded outcome.
- `SOLR-15805`: the Proof's base-failure sentence is reworded as a code reading. The bold summary line is also reworded; this is a judgment call that you may revert.
- `SOLR-8576`, `SOLR-17731` and `SOLR-17708`: a bracketed owed-note is added. The head and links are unchanged, because the branch fixes and runs have not happened yet.
- `SOLR-15024`: a bracketed owed-note asks for the ticket text to be checked. The summary line is unchanged.

## Checks after both passes

- No em or en dashes in any draft.
- No internal process words, except bracketed owed-notes and the word "allowed" or "swallowed", which contain "owed" and are not process wording.
- Every draft still names its head.

## Left for you

1. **Bracketed OWED notes.** `SOLR-15024`, `SOLR-17708`, `SOLR-17731` and `SOLR-8576` carry `[OWED BEFORE POSTING ...]` notes. They must come out before any posting, and each has a branch fix or check behind it.
2. **Limits bullets repeat their openers.** Part c1 did not trim them, because the formula wants each claim stated once. Some bullets in the eleven drafts now repeat their opener word for word. You decide whether to trim them.
3. **`SOLR-17377` duplicate changelog bullet.** Part c2 found the same duplicate that item 6 removed from `SOLR-17297`. Item 6 names only `SOLR-17297`, so the `SOLR-17377` bullet is left. Remove it, or confirm that it stays.
4. **`SOLR-15805` summary reworded.** The bold summary was changed as a judgment call. Revert it if you prefer the earlier wording.
5. **Two opener sentences run long.** The Choice openers in `SOLR-12007` and `SOLR-17297` run to two sentences. They are left as they are.

## Not done

No DISCUSS item was decided. No branch fix, run, or gate was done, so the heads and counts in the drafts that depend on them stay as recorded. No commit or post has been made outside this round's files.
