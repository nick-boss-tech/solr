# Claim: critical-impl-14996-round-1 (implementation, premise-run) — NEEDS-OWNER

- Claimant host: vm6, matching `hosts/vm6.md`.
- Status: NEEDS-OWNER.
- Job or assignment: `assignments/critical-impl-14996-round-1.md` (SOLR-14996, rank 5 of `reports/critical-implementation-triage-round-1.md`).
- Slice: none run yet. Step 1 (premise run) is blocked by a harness question, so no test was executed.
- Capability tags used: premise-run (no run).
- Started: 2026-10-27T10:32:11Z (UTC).
- Lease: none held; this claim records a blocked item.
- Links: Ticket SOLR-14996. Supersedes: none. Related: `claims/critical-impl-17370-round-1-vm6-result-1.md` (same harness blocker, premise not run).

Harness blocker (checked at main `ee2f1bf2e01`): the premise test must extend `SolrTestCase`, which the assignment requires for new tests. The test config (`solr/core/src/test-files/solr/collection1/conf/solrconfig.snippet.randomindexconfig.xml`, `schema.xml`) reads about ten `solr.tests.*` system properties (maxBufferedDocs, ramBufferSizeMB, mergeScheduler, mergePolicyFactory, lockType, infostream, numeric.dv, the FieldType names, and others). `SolrTestCaseJ4` sets them with randomized values (`SolrTestCaseJ4.java:453-463`, `:2754`, and the numeric-points block near `:2676-2700`). `SolrTestCase` does not, so the config fails to load (seen in four setup attempts for SOLR-17370).

Question for Nick (one question): For premise-only scratch tests, may the harness extend `SolrTestCaseJ4` (so the config loads with J4's randomized properties), or must it set the properties itself on `SolrTestCase` with fixed values? The answer decides how SOLR-14996 and SOLR-17370 (and the remaining critical-impl items) can be premise-tested.

Heartbeat: 2026-10-27T10:32:11Z
