# SOLR-15144 — testing handoff for external reviewer

## Ticket
https://issues.apache.org/jira/browse/SOLR-15144 — '"timeAllowed" param with "numFound" having a count value but doc list is empty'
Reporter: Aleksandr Yaroslavskiy. Research note: `pipeline/research-notes/SOLR-15144.md`.
Note: the research note claims `firstPhaseElapsedTime` is never set outside its
declaration — that was inaccurate; the grouping path sets/uses it
(`SearchGroupShardResponseProcessor`, `TopGroupsShardRequestFactory`). This patch
extends that established pattern to the regular query path.

## What was changed
`solr/core/src/java/org/apache/solr/handler/component/QueryComponent.java` only (+20 lines):

- `mergeIds` now records the max phase-one shard elapsed time into
  `rb.firstPhaseElapsedTime` (same source the grouping response processor uses:
  `SolrResponse.getElapsedTime()` per shard response).
- `createRetrieveDocs` no longer copies the original `timeAllowed` verbatim onto
  the second-phase GET_FIELDS requests. It propagates only the remainder
  (`origTimeAllowed - rb.firstPhaseElapsedTime`); when the budget is exhausted
  (<= 1ms), it drops `timeAllowed` from the doc-fetch request entirely.

## Why
Phase one already honors the timeout and flags `partialResults=true`. Re-applying
the fully-consumed allowance to phase two made every doc-fetch request time out
immediately, so shards returned empty doc lists while `numFound` stayed positive.
Dropping the exhausted allowance lets already-merged ids materialize; the timeout
semantics remain visible via the phase-one partial-results flag.

## What was NOT done
- No Gradle build or test run (per pipeline Phase 2 protocol — intentionally
  uncompiled/untested).
- Behavior is unchanged when `timeAllowed` is absent (<= 0) or when `mergeIds`
  doesn't run (merge-strategy path, one-pass queries): `firstPhaseElapsedTime`
  stays 0 and the original value passes through as before.
- Grouping path untouched; it already implements this pattern and doesn't call
  `mergeIds`, so no interference.

## Suggested validation (for the reviewer with a build environment)
- Reproduce first on unpatched main: 2-shard distributed collection, artificially
  slow query, small `timeAllowed`; assert `numFound > 0`, `partialResults=true`,
  `docs` non-empty after the patch (empty before, per the ticket).
- Assert phase-one partial-results semantics are preserved (header flag, per-shard
  partial details) and that a generous `timeAllowed` still bounds phase-two
  requests via the remainder.
- Existing suites: `TestDistributedSearch`, timeAllowed-related tests
  (e.g. `TestSolrQueryParser`? no — search for `timeAllowed` in
  `solr/core/src/test`), plus `TestDistributedGrouping` for the grouping path.
- Run tidy/Error Prone on the module (`-Pvalidation.errorprone=true`).

## Reviewer watch-outs
- `getElapsedTime()` on shard responses is the shard-reported QTime; clock skew
  between coordinator and shards could make the remainder slightly off — same
  approximation the grouping path already accepts.
- Dropping (rather than clamping to 1ms) when exhausted is a deliberate choice:
  a 1ms allowance on a doc-fetch would still time out and reproduce the bug.
  If reviewers prefer strict total-budget enforcement, the alternative is skipping
  the fetch (grouping behavior) — but that preserves the reported empty-docs
  outcome.
- This branch must be rebased onto current `apache/solr` main and the handoff file
  removed before any upstream PR. No PR opened, no Jira comment posted.
