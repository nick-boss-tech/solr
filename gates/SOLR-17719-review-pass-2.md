# Gate job: SOLR-17719 review pass 2 (TestManagedSchemaWithMultipleAdd timing related failures)

Status: DONE (GATE GREEN at 732d3fbcc4, vm4, 2026-10-22T17:47:11Z)

- Branch: solr-17719-semaphore-newsearcher. Head: `732d3fbcc47358dfa532cbdf77d6205fde6a0368` (unchanged since pass 1). Prior gated head: `732d3fbcc4` (pass 1, vm4, GATE GREEN).
- Intended host: vm4. Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.schema.TestManagedSchemaWithMultipleAdd`. Seed: `17719C0FFEE17719`.
- Why: pass 2 of the review round for unit 35, regate on the head pass 1 reviewed.

## Checks

- [x] head confirmed by ls-remote
- [x] diff scope unchanged
- [ ] step 0 changelog: not applicable, no changelog file on this branch (test-only change)
- [x] step 1 tidy, rc=0
- [x] step 2 Error Prone compile, rc=0
- [ ] step 3 proof leg: not run (test-only timing change; no fail-before evidence, same as pass 1).
- [x] step 4 focused head run, from fresh XML: TestManagedSchemaWithMultipleAdd tests=1, skipped=0, failures=0, errors=0 (ts 2026-10-22T17:44:59.444Z). rc=0.
- [x] step 5 module check `:solr:core:check -x test`, rc=0 (17:47:11).

Logs: `/workspace/gates/logs/vm4/17719/`.

## Deliverable

- Pass 2 result in `reports/newdev-critblock-review-round-1/solr-17719-semaphore-newsearcher.md`.
- Receipt section in `receipts/SOLR-17719.md`, top section.
