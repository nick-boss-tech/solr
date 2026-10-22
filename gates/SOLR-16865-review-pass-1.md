# Gate job: SOLR-16865 review pass 1 (Solr should warn about low disk space upon startup)

Status: DONE (GATE GREEN at 606eca38e9, vm4, 2026-10-22T16:23:46Z)

- Branch: solr-16865-low-disk-warning. Head: `606eca38e9594ada4ed16e2a376ba30b0c21273a` (ls-remote 2026-10-22T15:53:23Z, from the claim). Prior gated head: `606eca38e9` (none on this unit).
- Intended host: vm4. Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.core.LowDataDirSpaceThresholdTest`. Seed: `16865C0FFEE16865`.
- Why: pass 1 of the review round for unit 34.

## Checks

- [x] head confirmed by ls-remote
- [x] diff scope confirmed
- [x] step 0 changelog YAML parse, rc=0 (`changelog/unreleased/SOLR-16865-low-data-dir-space-warning.yml`)
- [x] step 1 tidy, rc=0, status file empty
- [x] step 2 Error Prone compile `:solr:core:compileTestJava -Pvalidation.errorprone=true`, rc=0 (16:09:47)
- [ ] step 3 proof leg: NOT A BEHAVIOUR PROOF. With `SolrCore.java` at base, the test does not compile: `SolrCore.isBelowFreeSpaceThreshold` is missing (`LowDataDirSpaceThresholdTest.java:26`). rc=1, inconclusive by construction, because the test calls the new helper directly. Log `pass1-step3-proofleg.log`.
- [x] step 4 focused head run, from fresh XML: LowDataDirSpaceThresholdTest tests=3, skipped=0, failures=0, errors=0 (ts 2026-10-22T16:20:14Z). rc=0.
- [x] step 5 module check `:solr:core:check -x test`, rc=0 (16:23:46).

Logs: `/workspace/gates/logs/vm4/16865/`.

## Deliverable

- Pass 1 result in `reports/newdev-critblock-review-round-1/solr-16865-low-disk-warning.md`.
- Receipt section in `receipts/SOLR-16865.md`, top section.
