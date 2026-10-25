# SOLR-3479 PR 5126 highlight-query fix, repair round 1

Status: REPAIR APPLIED AND GATED. The first fix head `23c159d06bd86180e0bbd4b474517b60b693f926` failed the Error Prone compile leg. The repair commit `8a685825b6202f45eb981d3b8e7f05b01a5c1829` passes every required leg (see `reports/pr-5126-highlight-query-fix-gate-round-1.md`).

## Failure

- Leg: `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true` at `23c159d06b`. Exit 1.
- Check: Error Prone `ReferenceEquality`, a warning promoted to an error by `-Werror`.
- Sites: `solr/core/src/java/org/apache/solr/response/transform/HighlightAugmenterFactory.java:195` (`rb.req != context.getRequest()`) and `:196` (`rb.getQuery() != context.getQuery()`), inside `getMainHighlightQuery`.
- Log: `/workspace/gates/logs/vm4/pr-5126-fix/leg4b-compile.log`. Excerpt: `evidence/superseded-pre-repair-23c159-compile-errorprone.txt`.
- No test ran in this leg. The runtime tests at `23c159d06b` (leg 2 and leg 3) passed, but they are superseded by the final head.

## Decision

Both comparisons are identity checks on purpose. The helper must use the search parser's highlight query only when the result context belongs to the same request and the same parsed query. Value equality would be looser: an equal-but-different query object from another request could pass the check.

Options considered:

- `Objects.equals` or `.equals` on `Query` or `SolrQueryRequest`: changes the semantics; rejected.
- Restructuring the check to avoid `!=`: no equivalent without changing behavior; rejected.
- `@SuppressWarnings("ReferenceEquality")` on the one method, with a reason: matches the pattern Solr already uses for intentional identity checks (for example `solr/core/src/java/org/apache/solr/cloud/LockTree.java:218`, `ReplicateFromLeader.java:68`, `cli/ExportTool.java:730`).

Chosen: the suppression on `getMainHighlightQuery` only.

## Change

- Commit `8a685825b6202f45eb981d3b8e7f05b01a5c1829` on `solr-3479-submit`. Author Nick Shanin, no trailers.
- Message: "SOLR-3479: suppress ReferenceEquality on the intentional identity check".
- Diff: two added lines above `getMainHighlightQuery` (one comment, one annotation). No logic change.
- Pushed as a fast-forward from `23c159d06b`; ls-remote read back `8a685825b6202f45eb981d3b8e7f05b01a5c1829`.
- Tidy before the commit changed nothing beyond the edit (`/workspace/gates/logs/vm4/pr-5126-fix/repair-tidy.log`, rc 0).

## Superseded and kept

- Pre-repair legs at `23c159d06b`: leg 2 (11 tests, 0 failures), leg 3 (33 tests, 0 failures), tidy (rc 0), compile (rc 1), module check (not run to completion). These are not proof for the final head.
- Leg 1 (fail before, original production plus the fix test file) is not invalidated: the fail tree is the original production, and the test file is unchanged between `23c159d06b` and `8a685825b6`.

## Reruns on the final head

Legs 2, 3, 4a, 4b and 4c were rerun on `8a685825b6`, all exit 0. See the gate report for counts and times.
