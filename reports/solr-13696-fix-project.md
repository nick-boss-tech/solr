# SOLR-13696 fix project: report (round 1, reconciled 2026-10-19)

Written late by vm3. The claim went STALE on 2026-10-17 without this report. The work landed on `solr-13696-submit` and was gated in later rounds; this report is written from the branch and the receipts, and each claim below cites its source.

## Head and base

- Base for the repair: `05ab4664dace` (the banked head).
- New head: `960355fce3`. Fourteen commits sit on the base. The routed-alias test repairs are in the first eight (`1deddc5195` to `f4f1187a57`, and `1d0b8a0a73`). The production fixes and the changelog fragment are `98ad9d3fcc`, `a4e0da4223` and `da4fa6df11`. The SOLR-13059 commits are `21e4407d79` onward.

## Drift characterization

- The Category suite failed on 2026-10-05 with "unknown field 'ship_name_en'". The field is not in the configset the cluster uses. Repair: the test routes on `ship_name_s`, which the configset defines (`1deddc5195`).
- The Dimensional fixed "future" date 2020-10-23 had passed, so the router created daily collections until the client timed out. Repair: the document is built 30 days after now (`08f9384e47`).
- The placement check read `[shard]` in its default style, which returns a bare shard id at luceneMatchVersion 9.5. Repair: the check asks for URL style (`1d0b8a0a73`).
- Each test method created a cluster and only the last one was shut down. Repair: each method shuts its own cluster down in `@After` (`f4f1187a57`).
- Scope note: `98ad9d3fcc` (the V2 create-alias message for dimensional aliases) and `a4e0da4223` (the string timestamp in `formattedRouteValues`) are production changes. They are outside this assignment's test-side scope. They came from the later SOLR-13696 production decision, which is recorded in PR 5088, not from this project.

## Premise: does the commitWithin race still reproduce?

- Evidence class: code read, a hypothesis. The repaired suites commit each alias collection explicitly (`RoutedAliasUpdateProcessorTest.addDocsAndCommit`, around line 366) and no longer send commitWithin. So the suites cannot exercise that race.
- Whether the race reproduces on current main was not run. It stays a hypothesis.

## Finding 2 (routed-alias commitWithin coverage): disposition

- Removing the random commitWithin path removes routed-alias commitWithin coverage. This is a decision for the owner, not a side effect.
- The choice is stated in PR 5088 (Limits): the explicit commit is kept, and the alternative (an honest commitWithin check in the re-enabled tests) is not added. Open for the owner.

## Finding 3 (possible NPE): disposition

- The explicit-commit loop is guarded: `assertNotNull("alias ... is not listed", aliasCollections)` (line 367) runs before the loop.
- The aliasOnly=false path calls `.get(getAlias())` unguarded at line 327. A null there would throw an NPE.
- Each suite creates its alias in `@Before` before any add runs (Category, Dimensional and TimeRouted subclasses; the Dimensional suite at line 84). A null can therefore only follow a failed setup, and the test fails either way. No guard is added. Judgment from reading, not from a run.

## Gate evidence at the new head

- `receipts/SOLR-13059.md` (review gate 2, head `960355fce3`): tidy and Error Prone pass. Normal mode with `tests.iters=2`: TimeRouted 14 (2 skipped, `testDateMathInStart`), Category 12, Dimensional 4, CreateAliasAPI 26, all 0 failures. Module check passes.

## Verdict

Ready. The repairs are on the branch and gated green at `960355fce3`. The open item is the owner decision on routed-alias commitWithin coverage (finding 2), which PR 5088 states.
