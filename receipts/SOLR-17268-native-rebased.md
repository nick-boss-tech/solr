# SOLR-17268 gate receipt: native arrntv, rebased

## Native arrntv branch rebased onto main, vm2, 2026-10-26: GATE GREEN

- Branch: `solr-17268-native-rebased` on the fork (new branch, not a PR). Head `22d3f761554d`, base `upstream/main` `eb6a8e50b0e` (rebased from `beb7c420894`, which sat on `714d6ceff852`). The rebase applied cleanly. Commits: `6938a5313d` (native arrntv writer), `d4350fce3b` (changelog and imports), `22d3f76155` (legacy-output parity).
- Gate driver: `/workspace/gates/g17268-rebased-gate.sh` on the shared runner `/workspace/gates/lib/gate-run.sh`, under the vm2 host lock. Seed `17268C0FFEE17268`. Result file: `/workspace/gates/logs/vm2/17268-result.txt`.
- Scope against base: 5 files, 706 insertions, 4 deletions.

Steps at `22d3f761554d`:
- Changelog YAML parse: rc 0.
- Tidy: rc 0, tree clean afterward.
- Error Prone compile (`:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`): rc 0.
- Proof leg: production files at base `eb6a8e50b0e`, head tests. `JSONWriterTest` 22 tests, 10 failures. `JacksonJsonWriterArrntvCloudTest` 4 tests, 4 failures. All failures are `UnsupportedOperationException: arrntv namedListStyle must only be used with ArrayOfNameTypeValueJSONWriter`, plus assertion failures on nested named lists in the same cases. Production files restored; tree clean.
- Focused head run (fresh XML): `JSONWriterTest` tests 22, failures 0, errors 0, skipped 0. `JacksonJsonWriterArrntvCloudTest` tests 4, failures 0, errors 0, skipped 0.
- Core check (`:solr:core:check -x test`): rc 0.
- Final status: clean.

Earlier native gate at `05f6cd4caf` (pre-rebase) is archived with the vm2 logs under `17268-native-05f6cd4caf-archive`.
