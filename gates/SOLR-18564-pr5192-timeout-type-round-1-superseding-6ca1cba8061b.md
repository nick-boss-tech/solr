# Gate: SOLR-18564 / PR 5192 typed executor timeout follow-up, superseding round 1 at 6ca1cba8061b

Status: OPEN, superseding the head-pinned gate `gates/SOLR-18564-pr5192-timeout-type-round-1.md` for the head below.

- Tags: gate, premise-run, review, records.
- Intended host: vm5 (claim `claims/solr-18564-timeout-type-round-1-vm5-repaired.md`).
- Fork: `nick-boss-tech/solr:corecontainer-shutdown-closes`, apache/solr PR 5192.
- Repaired head R: `6ca1cba8061b3c6298040634972afc4361f55add`, one commit above F `bd42a1d58b9fba4656a200536a007d9d79dc45b2`.
- Repair: the job's preflight `tidy` at F changed one line in `solr/solrj/src/test/org/apache/solr/common/util/ExecutorUtilTest.java` (a wrapped `assertTrue` at the poolName check). The repair commit "SOLR-18564: wrap ExecutorUtilTest assertion for tidy" is authored by Nick Shanin, no trailers, no PR number. F is not gated; no proof carries forward from F.
- Original head O / parent: `dd65b21b41dc1d008c51031b71f61964ee2ca169`. Delta O..R: the three files the F job names, plus the formatting-only test line.

Steps are the F job's, unchanged: changelog YAML parse, tidy at R (tree must stay clean), Error Prone compile for solrj and core, focused suites with seeds `18564ABC0001` and `18564ABC0002` (cleanTest before each), matched mutation proof for `ExecutorUtilTest.testExecutorUtilAwaitsTerminationWhenTaskIgnoresInterupt` with seed `18564ABC0001`, final tidy and `env -u NODE_ENV ./gradlew check -x test`.

Result: see the receipt `receipts/SOLR-18564-timeout-type-round-1.md` once written.
