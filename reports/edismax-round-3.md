# eDisMax round 3: round roll-up

Claim: `claims/edismax-round-3.md` (commit `c87876a40f2`). Assignment: `assignments/edismax-round-3.md` (commit `5a50bac2cdf`). Per-part reports: `reports/edismax-round-3-e1.md` through `-e5.md`.

Done 2026-10-09 by Claude Code (AI agent), working for Nick Shanin. Five read-only subagents did the consistency passes and first audits. Trial merges used `git merge-tree --write-tree`, which writes tree objects only and moves no ref. No build, Gradle run, or test was run. No submit branch, live PR, JIRA item, or comment was touched. Nothing was posted.

## Heads

All live heads named in the assignment match. For 3923, 7120 and 14638 the fetch moved or set the tips; the claim records them. The local `solr-3923-submit` and `solr-14638-submit` refs are stale and were not used.

## Per-ticket verdicts

| Ticket | Verdict | Draft | Head | What blocks it |
|---|---|---|---|---|
| SOLR-2309 | Draftable | `SOLR-2309.md` | `06f5a1c4a87` | Textual conflict with 2988 and 12092 at one insertion point; keep both blocks |
| SOLR-2988 | Draftable | `SOLR-2988.md` | `d2d144dfd9f` | Confirm the base named for the "premise run" before filing |
| SOLR-3243 | Draftable, held for a fix | `SOLR-3243.md` | `1db99c13662` | The negated form `-[* TO *]` now excludes every document; unstated and untested. Add a test and change the changelog title |
| SOLR-3729 | Draftable, lands first | `SOLR-3729.md` | `0fe7e503945` | Owner call (broad or narrow match-all) is the draft's Choice |
| SOLR-3923 | Not ready for a draft | none | `723f61ee4ab` | Premise run still owed; the guard misses signed lone parens such as `+(` |
| SOLR-3962 | Draftable, held for a reconcile | `SOLR-3962.md` | `e7d5f350503` | Must adopt 3729's helper after 3729 lands, then re-issue. Misses sign-in-parentheses spellings such as `(+*:*)` |
| SOLR-4362 | Held | `SOLR-4362.md` | `e96a057439c` | The slop check misses a decimal slop (`~2.5`) and a boost before the slop (`^2~10`); needs a branch edit and a focused re-run. Commit messages use internal words |
| SOLR-6009 | Draftable, two fixes first | `SOLR-6009.md` | `a41bb034a1f` | Changelog says "(or errored)" with no evidence; two commit subjects carry internal vocabulary |
| SOLR-6320 | Draftable, two fixes first | `SOLR-6320.md` | `cb710c96353` | A test comment overclaims that a change to the mm rule shows in the test strings; a TODO names a future fix that would not flip the expected string |
| SOLR-7120 | Sound in shape; first audit | none | `6a434a7fc3d` | A committed handoff note (`SOLR-7120-TESTING.md`) must be deleted first; the 500 to 400 status change must be stated; premise run still owed |
| SOLR-12092 | Draftable, hold the PR for a history fix | `SOLR-12092.md` | `ca9573dabd3` | Three commits carry Claude `Co-Authored-By` trailers and two subjects say "handoff"; squashing rewrites the fork branch. Changelog and ref guide must state the index-side rule |
| SOLR-14638 | Parked; audit only | none | `44cf1c8fc80` | Matches the ticket and the park note; a committed `OPEN-QUESTIONS-SOLR-14638.md` must be deleted; global default change is the owner's call |
| SOLR-14913 | Draftable after one changelog fix | `SOLR-14913.md` | `b80221f46d3` | Changelog says "failing the whole query", which base does not do |

## Landing order

- **Family order:** 3729, then 3962, then 3243 (part e2). 3729 first, because 3962 reconciles to its helper.
- **Pairs and textual conflicts:**
  - 2309 and 2988 conflict at one insertion point; keep both blocks.
  - 12092 and 2309 conflict at one insertion point; keep both methods.
  - 2988 and 4362, and 2309 and 4362, merge clean (part e1).
  - 14913 merges cleanly with 2309, 3243, 6009 and 12092 (part e4).
  - 6320 and 6009 touch separate methods. The parser hunks auto-merge. The test file conflicts at one anchor in both orders; land 6009 first (part e3).
  - 3923 and 3962 edit the same loop, and the merged result reads coherently (part e5).
- All trial merges were text-clean. Clean text merges do not prove the code compiles.

## Important findings

- **SOLR-4362, decimal slop.** `isSlopClause` in `ExtendedDismaxQParser.java` misses a decimal slop and a boost before the slop. Both leak the number into the pf2 shingles. Round 28 said `~2.5` is not valid slop. Solr's grammar accepts it and truncates to 2. Replacement regex and checks are in part e1, finding 1.
- **SOLR-3243, negated range.** The guard returns `MatchAllDocsQuery` for every unfielded inclusive open range. `foo -[* TO *]` therefore returns nothing, where base returned `foo` documents. The new tests use no negated form.
- **SOLR-3962, missing spellings.** The helper misses spellings that 3729 treats as match all, such as `(+*:*)`. After 3729 lands, the score bug survives for those. Fix: use `isStandaloneMatchAll(clause.raw)` and add the spellings to the test.
- **SOLR-3923, signed parens.** The new guard sits inside `if (clause.isBareWord())`. A signed lone paren has `must` set, so it skips the guard. Its escaped paren still enters the pf phrase text.
- **SOLR-6320, test comment.** The javadoc at `TestExtendedDismaxParser.java` lines 395 to 402 says a change to the mm rule shows in the test strings. It cannot: `setMinShouldMatch` sets mm only with SHOULD clauses. Replacement text is in part e3, finding 1.
- **SOLR-7120 and SOLR-14638, committed notes.** Both branches carry handoff notes that must be deleted before any PR or branch move.

## Owner decisions

1. **Recorded, not re-posed:** 3729 broad or narrow match-all (the draft's Choice). 6320 finding 1 remedy (a mixed-case `And` or `Or` neighbour). 6320 demotion rule (a standing call; the compatibility effect is stated). 14913 `MatchNoDocsQuery` for the all-invalid alias behavior (on record: keep it).
2. **SOLR-12092, history.** Squash the six commits to one clean commit (a force push to the fork). This needs your go-ahead.
3. **SOLR-12092, index-side rule.** Keep the rule that a managed stop filter in the index analyzer also turns `stopwords=false` off, or narrow it. The changelog and ref guide must state whichever is kept.
4. **SOLR-4362 and SOLR-6009 history.** Squash commit subjects that carry internal words. Needs your direction.
5. **SOLR-14638.** The global default change stays yours. Round 11 recommends documentation only.

## Corrections to the record

- The claim's live-head table and the round 33 and round 36 reports for 3729, 6320 and 12092 are not on disk. The drafts were written fresh from the record, so the round 36 and round 33 text was not adopted.
- Gate logs named in the receipts are not on disk. Every count in the drafts is receipt-only.

## Not done

No build, test, Gradle run, `gh` call, fetch, commit, or post. Live JIRA was not read. The 3923 premise run and the 7120 premise run are owed to the main side. Lucene was not checked in this round; no draft names Lucene behavior.
