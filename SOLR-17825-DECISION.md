# SOLR-17825: decision needed (handoff, remove before any PR)

Branch head reviewed: `4b35e5a5165`. Nothing here was compiled or run.

## Status

Retire. The ticket is Resolved (fix version 10.1): the Kafka 4 upgrade (SOLR-18300, `8c750e25b11`) removed the
`kafka213` runtime dependency that pulled in `commons-beanutils` 1.9.4. On `upstream/main` the only remaining
`commons-beanutils` reference is `gradle.lockfile` (`ratDeps`, the RAT tool, build time only).

## Why not merge

`verifyCommonsBeanutilsFloor` throws when beanutils is absent from `runtimeClasspath`, so `check` would fail for
`cross-dc-manager` on current `main`. The branch also references `libs.apache.kafka.kafka213`, which no longer
exists upstream, so it does not merge cleanly.

## Decision

Retire the branch. Optionally link SOLR-18300 on the ticket.

Full review: `research/branch-reviews/round-6/SOLR-17825-review.md` in the workspace.
