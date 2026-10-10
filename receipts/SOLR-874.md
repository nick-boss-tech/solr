# SOLR-874 gate receipt

- Status: GATE GREEN at the live tip.
- Gated head: ac9ab33753751a6fef05b5f100f5c1c5683510b2 (branch solr-874-submit, pushed 2026-10-06).
- Gate log: g874-gate.log. Changelog YAML parses; tidy clean; Error Prone compile passes; module check passes.
- Counts: SolrPluginUtilsTest 10 of 10 and DisMaxRequestHandlerTest 4 of 4 at the head, from fresh JUnit XML.
- Proof: GROUNDED (log g874-premise.log, seed 874C0FFEE874). Base production with the branch test files: SolrPluginUtilsTest 10 tests with exactly 1 failure (testStripDanglingBooleanOperators) and DisMaxRequestHandlerTest 4 tests with exactly 1 failure (the request-level testDanglingBooleanOperator).
- Recorded in the main side's receipts ledger (SOLR-874 entry, 2026-10-06).
