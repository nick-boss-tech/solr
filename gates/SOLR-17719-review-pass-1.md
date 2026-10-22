# Gate job: SOLR-17719 review pass 1 (TestManagedSchemaWithMultipleAdd timing related failures)

Status: DONE (GATE GREEN at 732d3fbcc4, vm4, 2026-10-22T17:30:00Z)

- Branch: solr-17719-semaphore-newsearcher. Head: `732d3fbcc47358dfa532cbdf77d6205fde6a0368` (ls-remote 2026-10-22T17:06:11Z, from the claim). Prior gated head: `732d3fbcc4` (none on this unit).
- Intended host: vm4. Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.schema.TestManagedSchemaWithMultipleAdd`. Seed: `17719C0FFEE17719`.
- Why: pass 1 of the review round for unit 35.

## Checks

- [x] head confirmed by ls-remote
- [x] diff scope confirmed
- [ ] step 0 changelog: not applicable, no changelog file on this branch (test-only change)
- [x] step 1 tidy, rc=0
- [x] step 2 Error Prone compile `:solr:core:compileTestJava -Pvalidation.errorprone=true`, rc=0 (17:20:57)
- [ ] step 3 proof leg: not run. Test-only timing change; no production file to revert, and the original failure is timing-dependent, so a base run is not a fail-before proof here.
- [x] step 4 focused head run, from fresh XML: TestManagedSchemaWithMultipleAdd tests=1, skipped=0, failures=0, errors=0 (ts 2026-10-22T17:25:46Z). rc=0.
- [x] step 5 module check `:solr:core:check -x test`, rc=0 (17:31).

Logs: `/workspace/gates/logs/vm4/17719/`.

## Deliverable

- Pass 1 result in `reports/newdev-critblock-review-round-1/solr-17719-semaphore-newsearcher.md`.
- Receipt section in `receipts/SOLR-17719.md`, top section.
