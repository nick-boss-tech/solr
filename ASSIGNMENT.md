# Assignment: paired review of the SOLR-18119 and SOLR-18523 branches and their PR descriptions

Review only: no patching, no comments anywhere, no pushes to the
branches under review. Claim first: before reading anything else,
push a claim commit on this branch adding
`claims/solr-18119-18523-pair.md` with your agent name, the date,
and the two heads you are about to review. Write the verdict to
`reviews/solr-18119-18523-pair.md` on this branch.

## Branches under review

- SOLR-18119 converter port: branch `solr-18119-jvm` at
  `3fa8af32707`, on main `9becf6c6a15`. Four commits: port the
  converter, add its tests, remove `changes2html.py`, add the
  changelog entry.
- SOLR-18523 DocLint follow-up: branch `solr-18523-submit` at
  `c0a3972d667`, on base `9becf6c6a15`. Four commits: crawl
  removal, CI gate on the documentation check, a comment-only fix
  scoping the gate to `:solr:documentation:check`, changelog type
  `other`.

Both PRs are drafts. They flip to ready only after this review
round passes. Verify both heads against the fork before starting;
if either head has moved, review the head you actually see and say
so in the verdict.

## Descriptions under review

`pr-body-18119.md` and `pr-body-18523.md` on this branch are copies
of the two live PR descriptions as of 2026-10-08. Check them
against the live descriptions if you can; if they differ, review
the live text and note the difference. The descriptions are part
of the review scope, not background: they were both rewritten
under a new presentation rule (a bold one-line summary opens every
section, the claim is stated once, the body carries evidence and
citations only, and every file citation is a blob link at the PR
head SHA).

## What changed since the last review of each

- SOLR-18119: this branch replaces an earlier full port of both
  Python scripts. The crawl half is gone (SOLR-18523 deletes the
  crawl, so this branch touches neither crawl file).
  `PythonCompat` is trimmed from 516 to 85 lines, keeping only
  the three helpers the converter calls. Test suite is 14 tests:
  ChangesToHtmlTest 11, PythonCompatTest 3. The one converter-path
  behavior fix carried over: LF output on every platform.
  Fragment-anchor validation is not code in either branch; it is
  a named follow-up promise in this branch's description Limits,
  sized by an audit recorded with the branch owner (185,623
  fragment links checked in the built site, 0 broken, compared
  the way browsers resolve anchors).
- SOLR-18523: this is the revised head after a previous review's
  Needs work verdict on the description (the Gradle wiring did
  not change in the revision). The `documentation.gradle` comment
  was scoped to `:solr:documentation:check`, the changelog type
  was fixed, two negative controls were run (a planted bad
  overview `{@link}` fails the CI documentation check at
  `renderSiteJavadoc`; a planted broken link in a docroot
  markdown page passes unchecked), one bullet naming the
  companion PR was added to What this change does after a paired
  read, and the description follows the presentation rule above.

## Review scope

1. Claim check, both descriptions: every claim checked against
   the tree at the head above. Flag any claim stated more broadly
   than its proof, any claim repeated across sections in widening
   form, and any citation that is not a working blob link at the
   head SHA with a line anchor.
2. Pair consistency: each description's statements about the
   other change checked against the other branch and the other
   description. In particular the pair framing (disjoint files,
   merge in either order), the silent-skip statements on both
   sides, and the anchor follow-up promise against SOLR-18523's
   stated trade on docroot coverage.
3. SOLR-18119 scope discipline: confirm the diff is
   converter-only (no crawl files, no unrelated build changes),
   `PythonCompat` contains nothing the converter does not call,
   `changes2html.py` is fully removed with no surviving
   references, and the changelog fragment parses and uses type
   `changed`.
4. SOLR-18523 at the revised head: crawl deletion completeness
   (tree-wide search for surviving `checkBrokenLinks` /
   `checkJavadocLinks` references), the `isCIBuild` gate and its
   comment's accuracy, and that the Limits wording on the removed
   task name states only what is checkable in-repo.
5. Pair interaction: a test merge of the two heads was clean and
   the merged tree passed the CI documentation check with
   `documentation` executed, `checkBrokenLinks` absent, tests
   14/14, and `Changes.html` at the recorded reference hash.
   Re-verify by reading, or re-run if the heads have moved; do
   not take the merged result on trust if either head differs.
6. Presentation: both descriptions follow the formula (bold
   one-line summary opens every section, the claim stated once,
   body carries evidence and citations only, citations are
   working links).

## Coordination hygiene

- Refer to the tickets as SOLR-18119 and SOLR-18523 only. No PR
  numbers in commit messages or file names.
- Write the review files as publishable documents: findings,
  evidence, and verdict, with no internal shorthand or process
  narration.
- Verdict per branch (Ready / Nearly / Needs work), plus a pair
  verdict, with each finding tied to a file and line at the
  reviewed head.
