# PR 5126 RTG examples, round 1 (slice R)

Verdict: RTG WORKS for the assigned no-query case. With no query, the
`[hl]` transformer passes through whatever real-time get returns,
including uncommitted text, in both the replace and the insert phase.
David's Jira comment 18131608 ("`[hl]` cannot support RTG because it
must read from the index") holds for query highlighting, which needs an
index read; it does not hold for the no-query fallback, which reads
nothing. The PR description's real-time-get bullet therefore holds for
this case. Query-term highlighting over uncommitted text was NOT tested
here and remains unproven; that is a separate question, not a caveat on
this verdict.

Claim: `claims/pr-5126-rtg-examples-phone-agent.md`.
Assignment: `assignments/pr-5126-linux-examples-round-1.md`, slice R only
(slice Q DONE by vm4). Ticket and PR: SOLR-3479, apache/solr PR 5126.

## Baseline and diagnostic heads

- Baseline (production source): `74b09fb443f060caac3f8ec9701909cec276cfb4`
  (the assignment's pinned head).
- PR merged 2026-10-27 at head `591b99c02339ab9fa90b07ce7c58af1a3bc24334`
  (merge commit `c7c23508840a4c828283c76716902e5b1b6f9d2e`), after this
  assignment was written. Per the assignment's advance rule both heads
  are recorded here. The pinned-to-merge production diff for
  HighlightAugmenterFactory is only the highlight-query selection change
  (the query-slice fix, merged into the PR before merge); the RTG
  fallback path is unchanged, so this pinned-head diagnostic transfers
  to the merged code for the no-query case.
- Diagnostic commit: `78d54654101d4d9ad75c60f5d25a65adcc122e66`, branch
  `solr-3479-pr5126-rtg-examples-r1` on `nick-boss-tech/solr`, author
  Nick Shanin, no trailers, verified by ls-remote read-back.
- One file changed:
  `solr/core/src/test/org/apache/solr/response/transform/TestHighlightAugmenterFactory.java`,
  +128 lines, 0 deletions. Production source is byte-identical to the
  pinned head (production diff 0 lines).
- Patch: `reports/pr-5126-rtg-examples-round-1-evidence/test-patch-78d5465410.patch`.

## Environment and runs

- Host phone agent, Linux, vendored JDK 21.0.12.1+1, Gradle 9.7.0 via
  `~/workspace/tools/solr-gradle.sh`, one build at a time through the
  test-queue flock. Seed `34795126` on every test leg. Runner:
  `~/workspace/tools/5126-rtg-runner.sh`.

| Leg | Task form | Result |
| --- | --- | --- |
| 1 tidy | root `tidy` | rc=0, 0 non-test tracked files changed |
| 2 compile | `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true` | BUILD SUCCESSFUL, 5m 11s |
| 3 focused | `:solr:core:cleanTest :solr:core:test --tests ...TestHighlightAugmenterFactory.testReview5126RtgExamples` | BUILD SUCCESSFUL, 2m 7s, 1 test, 0 failures, fresh XML 2026-11-02T05:52:23Z |
| 4 whole class | `:solr:core:cleanTest :solr:core:test --tests ...TestHighlightAugmenterFactory` | BUILD SUCCESSFUL, 51s, 9 tests, 0 failures, fresh XML 2026-11-02T05:53:18Z |

Raw XML: `reports/pr-5126-rtg-examples-round-1-evidence/focused-TEST.xml`,
`.../class-TEST.xml`. Run logs (VM paths, retained with the runner):
`~/workspace/tools/5126-rtg-tidy.log`, `5126-rtg-compile.log`,
`5126-rtg-focused.log`, `5126-rtg-class.log`,
`5126-rtg-runner.out`.

## Method

New method `testReview5126RtgExamples` in the existing SolrTestCase
class, using the existing embedded client and fixture collection. Fresh
IDs, distinct from fixture IDs 1 and 2. Every phase compares plain
`/get` (`fl=id,body_t`) with transformed `/get`
(`fl=id,body_t:[hl]`), with no query anywhere. Each phase prints its
plain, transformed, and `/select` observations before asserting. The
`/select` control proves the phase is genuinely uncommitted for the
active searcher; had it shown the new data, the phase would have been
labeled non-discriminating instead of asserted. Cleanup in `finally`
deletes both fresh IDs and commits, so the whole class still passes.

## Observations (exact, from the focused run's XML system-out)

Phase 1, committed control. Added `rtg-existing-5126`, body
`Original stored text.`, committed. Plain and transformed both return
`Original stored text.`

Phase 2, replace without commit. Replaced the same ID with body
`Fresh update text.`, no commit. `/select` for the ID still returns
`Original stored text.` (discriminating: the searcher has not seen the
replacement). Plain get returns `Fresh update text.`. Transformed get
returns `Fresh update text.` (not the old text, not omitted, no throw).

Phase 3, insert without commit. Added `rtg-new-5126`, body
`Uncommitted insert text.`, no commit. `/select` finds nothing
(discriminating). Plain get returns `Uncommitted insert text.`.
Transformed get returns `Uncommitted insert text.` (not omitted, no
throw).

Phase 4, post-commit control. After commit, plain and transformed get
return `Fresh update text.` for the replaced ID and
`Uncommitted insert text.` for the inserted ID, as in the existing
`testRealTimeGet` committed control.

## Limits

- No-query only, by assignment design. A `[hl]` request that must
  highlight a query term against uncommitted text still needs an index
  read the searcher does not have; that case is untested and is where
  the Jira comment's limitation would bite.
- Single-node embedded client, default configset, one seed. No
  distributed, export, or alias behavior was exercised.
- The reply draft (`reports/pr-5126-reply-with-examples-round-1.md`)
  deliberately excluded the RTG question, and the PR has since merged;
  whether this finding belongs in any follow-up is Nick's call. No
  public comment is drafted or posted here.
