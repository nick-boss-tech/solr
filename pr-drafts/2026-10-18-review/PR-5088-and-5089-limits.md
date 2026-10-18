# Limits text for PR 5088 and PR 5089 (for Nick to approve; not applied)

Status: DRAFT. Neither body is changed until Nick approves.

## PR 5088: replace the commitWithin promise in "## Limits"

Current text (to remove): "Every document is committed explicitly, so commitWithin is no longer exercised. A follow-up submission to restore that coverage is planned."

Replacement:

**commitWithin coverage is removed from the routed-alias tests on purpose.**

- `RoutedAliasUpdateProcessorTest.addDocsAndCommit` commits each collection explicitly, so the routed-alias tests no longer exercise commitWithin.
- This is the decision for the owner. The alternative is an honest commitWithin check in the re-enabled tests, which this PR does not add.
- The reason for dropping it: commitWithin is orthogonal to routed aliases. A commitWithin add and an explicit commit go through the same path to the searcher, so the routed-alias tests lose little coverage. The ticket discussion reached the same view in 2021.

## PR 5089: replace the six-test promise in "## Limits"

Current text (to remove): "A follow-up submission for those six tests is planned."

Replacement:

**The six remaining tests stay skipped until SOLR-13059 is resolved.**

- `testPreemptiveCreation` is one of the six. They keep the class-level `@AwaitsFix` in `TimeRoutedAliasUpdateProcessorTest`, so they do not run in normal mode.
- SOLR-13059 is open, and its test fix is under way on the 13696 branch. No separate follow-up is planned in this PR.
