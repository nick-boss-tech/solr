# SOLR-874 gate receipt

## Re-gate at a283e05090 (title correction, 2026-10-14T05:58:37Z)

- Status: GATE GREEN at the new head a283e050904a5d323bfd5a6016c799f434d229e4 (branch solr-874-submit, plain fast-forward from ac9ab33753751a6fef05b5f100f5c1c5683510b2).
- Delta from ac9ab33753751a6fef05b5f100f5c1c5683510b2: `changelog/unreleased/SOLR-874-dismax-dangling-boolean-operator.yml` (the title line only). No production or test code changed, so the proof above carries over.
- Gate log: logs/vm2/queue-g874-gate-20261014T052652Z.out. Changelog YAML parses; tidy clean (tree empty after tidy); Error Prone compile of :solr:core passes; module check passes (rc lines 0).
- Focused counts at the new head, from fresh JUnit XML, seed 874C0FFEE874: SolrPluginUtilsTest 10 tests, 0 failures, 0 errors, 0 skipped; DisMaxRequestHandlerTest 4 tests, 0 failures, 0 errors, 0 skipped.

- Status: GATE GREEN at the live tip.
- Gated head: ac9ab33753751a6fef05b5f100f5c1c5683510b2 (branch solr-874-submit, pushed 2026-10-06).
- Gate log: g874-gate.log. Changelog YAML parses; tidy clean; Error Prone compile passes; module check passes.
- Counts: SolrPluginUtilsTest 10 of 10 and DisMaxRequestHandlerTest 4 of 4 at the head, from fresh JUnit XML.
- Proof: GROUNDED (log g874-premise.log, seed 874C0FFEE874). Base production with the branch test files: SolrPluginUtilsTest 10 tests with exactly 1 failure (testStripDanglingBooleanOperators) and DisMaxRequestHandlerTest 4 tests with exactly 1 failure (the request-level testDanglingBooleanOperator).
- Recorded in the main side's receipts ledger (SOLR-874 entry, 2026-10-06).
