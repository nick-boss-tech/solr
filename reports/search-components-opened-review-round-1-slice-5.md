# Search components opened review, round 1, slice 5 (PRs 5146 to 5149)

- Assignment: `assignments/pool-search-components-opened-review-round-1.md`. Claim: `claims/search-components-opened-review-round-1.md`.
- Reviewer: review agent, Windows host. Read-only review plus one description PATCH on PR 5146. All four PRs stay draft and open; heads unchanged. No builds, tests, pushes, commits, comments, ready flips or Jira edits. No gate jobs filed (no code finding).
- Scratch files, not in the worktree: `pr5146-before.md` and `pr5146-after.md`, and the saved live bodies for each PR.

## PR #5146 (SOLR-8939), head `a855a2d8965c`. SATISFIED after 2 edits.

- Diff is scoped (5 files, +134 and -3). The merge-base `97d973814336` is not cited in the body, and "What happens today" has no file citation, so there is nothing to re-point.
- Checks: `idToString` (L1453 to L1462), the plain path (L1443 to L1447) and the grouping path (L79) match. The receipt counts (2 of 2 and 2 of 2, gated 2026-10-06) match. The date test fails on base, the string test passes on both, and `QueryComponentIdFormatTest` cannot compile on base, as the receipt and body say. The ticket symptoms match the Jira packet.
- Edits:
  1. The `QueryComponentIdFormatTest` line gained the verification date the formula requires: "... 2 of 2 pass at this head, recorded 2026-10-06. ..."
  2. Limits gained the follow-up offer the receipt records: "A follow-up that adds a run with two or more shards can be opened on request."
- The live body now ends with one newline. The read-back matches the file sent, apart from that newline.

## PR #5147 (SOLR-9124), head `64b8256cde72`. SATISFIED, no edits.

- The fetch moved the branch from `17a279d4dce` to `64b8256cde7`. The only change between them is the changelog title. The title matches the diff.
- The body claims no grouped-versus-ungrouped equality the test does not check. The grouped check compares control grouped scores with shard grouped scores (`TestBaseStatsCache` L78 to L95).
- Judgment call: the reviewer read "name no cache" as "no cache claimed as fixed or covered". The Limits line "Only the exact cache class fails without the change" is the sentence to decide on if Nick reads it strictly. No edit was made.
- Counts 5 of 5 at `17a279d4dce1`, verified 2026-10-09, match the receipt's second re-gate. All eight blob citations are correct at head and base.

## PR #5148 (SOLR-9396), head `a5ab2eda4e6a`. SATISFIED, no edits.

- Diff is scoped (3 files). The scan regex and `getExtraRequestFields` (`SubQueryAugmenterFactory` L307 to L362) and the test `testRowFieldNotInFl` (L257 to L283) match the body.
- The receipt (17 of 17, with 11, 3, 1, 2 across the groups) and the base run (1 of 11 failed, the new test) match. The dates 2026-10-05 match.
- The Limits claim that `${row.` is never a row reference holds: the local-param dereference reads only `$name`.
- Commit-history finding, not edited: the branch has a "hypothetical-reproduction handoff doc" add and remove pair, and the subject `SOLR-9396: SOLR-9396:` is doubled. Both show on the PR's Commits tab. Fixing needs a history rewrite on the fork (Nick's call).

## PR #5149 (SOLR-9595), head `bf99cd0e89c2`. SATISFIED, no edits.

- Diff is scoped (3 files). The changelog title is double-quoted and valid.
- The body's date, 2026-10-10, matches the receipt's takeover-log line ("second re-gate finished 2026-10-10"). The receipt's gated-head line itself has no date, so the date rests on the takeover log, and the two agree.
- The doc-values limit holds: `NumericDocValues` (L177), `BinaryDocValues` (L183), `SortedNumeric` (L189) and `Norm` (L339) are rebuilt per call at head. The receipt shows 3 of 3 at head, and the new test fails on base at a caching `assertSame`, not at setup.
- Commit history: the same handoff-doc add and remove pair shows on the Commits tab. Fixing it needs a history rewrite (Nick's call).

## Length and history, for the main side

- Over the 3,500-character guide: 9124 (about 4,470), 9396 (about 4,410) and 9595 (about 4,880). Not edited.
- Commit subjects with the handoff doc on 9124, 9396 and 9595. A squash or rewrite is Nick's call. Each PR's Commits tab shows them.

## Formula checks, all four PRs

- Bold one-line opener on every section. No em dashes, no "we", no internal process vocabulary. The AI header and the AI assistance footer are exact. The Jira line and the changelog line are present, and the changelog line links the head. The fragments were parsed by reading, since no YAML library or Python is on this host, and all four are valid. All commits are authored by Nick Shanin, with no Claude trailers.
