# Receipt: interrupted-close fix, final validation round 1 (vm2)

Owned by vm2. Gate file: `gates/teardown-interrupt-close-fix-final-validation-round-1.md`. Claim: `claims/teardown-interrupt-final-gate-round-1-vm2.md`. Assignment: `assignments/teardown-interrupt-close-fix-final-validation-round-1.md`.

## Heads

- Starting head H: `8787c6605ad5a5d322e9bca253235cd73142f101` (formatting commit, reviewed in the Windows review round 1).
- Final head F: `5ec461c4012036faf3fd14363202e82c4686fb07`, commit "Import HttpClient in the new interrupt-close tests" by Nick Shanin, no trailers. Only `solr/solrj-jetty/src/test/org/apache/solr/client/solrj/jetty/HttpJettySolrClientTest.java` changed (import plus three declarations shortened).
- Verified remote SHA of `nick-boss-tech/solr:solr-teardown-interrupt-close-fix`: `5ec461c4012036faf3fd14363202e82c4686fb07` (checked with `git ls-remote` after the gate).
- Base M: `d298cd0b42602247bee7bf9e2bcf302e725f6690`.
- Formatter check (`/workspace/tools/format-check.sh`) across `d298cd0b..F`: no new formatting violations.

## Commands, exits and tree state

Driver `gates/gates-vm2/run-final-gate-r1.sh`, one driver, lock `/workspace/tools/.teardown-final-gate.lock`, each leg through `pool/bin/sweep-run.sh` under `/workspace/tools/.gradle-vm2.lock`, watchdog 30 min per leg. Worktree `/workspace/worktrees/teardown-final-r1` clean at F before the driver started. Driver started 2026-10-26T05:18:10Z, finished 05:32:06Z.

| Leg | Command | Exit | Evidence |
| --- | --- | --- | --- |
| tidy | `./gradlew --no-daemon --max-workers=2 tidy` | 0 | tree unchanged afterwards |
| Error Prone compile | `--no-build-cache :solr:solrj-jetty:clean :solr:solrj-jetty:compileJava :solr:solrj-jetty:compileTestJava -Pvalidation.errorprone=true` | 0 | `compileJava` and `compileTestJava` executed, not up-to-date; no `errorProneSkipped` task in the log |
| module check | `:solr:solrj-jetty:check -x test -Pvalidation.errorprone=true` | 0 | BUILD SUCCESSFUL in 2m 16s; no `errorProneSkipped` |
| module suite (once) | `:solr:solrj-jetty:cleanTest :solr:solrj-jetty:test -Ptests.seed=FFF5A3C3AAC69084 -Ptests.jvms=1 -Ptests.useSecurityManager=true -Ptests.file.encoding=ISO-8859-1` | 0 | BUILD SUCCESSFUL in 4m 42s |

Tree after all legs: 0 changes.

## Module-suite XML, parsed by class (fresh XML, 7 classes)

| Class | tests | failures | errors | skipped |
| --- | ---: | ---: | ---: | ---: |
| ConcurrentUpdateJettySolrClientBadInputTest | 1 | 0 | 0 | 0 |
| ConcurrentUpdateJettySolrClientMultiCollectionTest | 1 | 0 | 0 | 0 |
| ConcurrentUpdateJettySolrClientTest | 11 | 0 | 0 | 0 |
| HttpJettySolrClientCompatibilityTest | 4 | 0 | 0 | 0 |
| HttpJettySolrClientProxyTest | 2 | 0 | 0 | 0 |
| HttpJettySolrClientTest | 53 | 0 | 0 | 0 |
| PreemptiveBasicAuthClientCustomizerTest | 4 | 0 | 0 | 0 |
| **Total** | **76** | **0** | **0** | **0** |

The three new methods ran and passed, in `HttpJettySolrClientTest`: `testCloseWithInterruptedThreadReleasesOwnedClient` (0.006 s), `testCloseWithInterruptedThreadKeepsCallerExecutorUsable` (0.007 s), `testCloseBorrowedClientWithInterruptLeavesParentRunning` (0.006 s). No skips in the suite. The seven classes match the source's seven concrete test classes.

Module check is a `solrj-jetty` module check, not a root repository check.

## Proof archive pointers

- Before, after and final-validation logs and XML from the earlier runs: `board-archive/fix-proof-r1/` (manifest `SHA256SUMS.txt`, commit fc381b9268c). Parsed from the archived XML: before (commit d1d5e963c70) 4 tests with 3 failures, after (423390023a6) 53/0/0/0, final (8787c6605ad) 53/0/0/0. The before failures are owned client and supplied executor throwing `Exception on closing client`, plus the tracker check that the two failing tests cause. The borrowed-client test passed on the base as a guard.
- Final-gate logs, XML and status: `board-archive/final-gate-r1/` (manifest `SHA256SUMS.txt`, 24 files, commit 41d9e152c46).

## Limits

- Only an interrupt set before close is handled. An interrupt delivered during the Jetty stop is not covered and remains a separate case.
- The module check and module suite are `solrj-jetty` checks, not a repository-wide check. Other modules were not run.
- No changelog is added. The changelog waits for a PR or Jira number, per AGENTS.md.
- The formatting-only commit `8787c6605ad` and the import cleanup `5ec461c4012` came after the earlier full-class runs. The final module suite ran on F, so the proof covers F.
- No PR was opened, and nothing was posted to GitHub or Jira. Opening a PR needs Nick's decision.

## Result

All four legs exited 0 on F. The gate is green for F under the limits above.
