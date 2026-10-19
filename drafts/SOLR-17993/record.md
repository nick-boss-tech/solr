# SOLR-17993 closeout record

Verdict: close-obsolete (draft branch `solr-17993-draft-close`).

- Pinned head: fork `origin/main` `498a09aea97aac86c2cc7ac114113b946d249942`. Upstream history is not cited.
- JIRA checked 2026-10-19: status Open, no resolution, no fix versions, created and updated 2025-11-17. Description read in full (about 350 characters). No comments.
- Audit verdict on record: `confirmed-skip` (audit-3, 2026-10-06): no `CLOUD_CORE_PATTERN` or shard or replica core-name regex left in `solr/core/src/java` on upstream main. The audit note says the metric class was removed in the OpenTelemetry metrics refactor.

## Obsolescence check at the pinned head

- `git grep -n 'CLOUD_CORE_PATTERN' 498a09aea9 -- solr/core/src/java`: empty.
- `git ls-tree -r --name-only 498a09aea9 -- solr/core/src/java/org/apache/solr/metrics/prometheus`: empty. The package named in the ticket's link does not exist at the pinned head.
- `git grep -n 'SolrCoreMetric' 498a09aea9 -- solr/`: only `SolrCoreMetricManager` references, none to the removed class.
- A search for a shard or replica core-name regex with `[_m]+` or a `core` pattern in `solr/core/src/java`: empty.

## Attempts

- The ticket names the class and a line range in a release branch, not in main. The class and line range are gone at the pinned head, so there is no code path left to reproduce.
- No test run. The ticket describes a regex in a removed class; no test in the tree can reach it.

## What the verdict does not cover

- Whether a Prometheus-format metrics response on current main still reports split-shard cores under a different name. That would be a new ticket, not this one. Not checked.

## Reopen condition

Output from a current release that still shows the split-shard naming problem, with the request. A new ticket is the right place for it.
