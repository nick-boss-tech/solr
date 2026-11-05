# Gate: encoded paths across the replica proxy hop on main, round 1

Status: OPEN. Intended host: Linux gate host with JDK 21, the pinned Gradle toolchain and a serialized host build lock.

- Assignment `assignments/solr-12613-adjacent-implementation-round-1.md`; claim `claims/solr-18537-transport-main-windows-round-1.md`.
- Supersedes the withdrawn `gates/solr-12613-adjacent-path-coverage-round-1.md`. Scope separation record: `gates/solr-12613-adjacent-scope-standdown-round-1.md`. That record's withdrawal of the old inputs remains in force; this is a new main-based input.
- Fork `nick-boss-tech/solr:solr-18537-path-coverage-followup`.
- Required head `59ad950e1ac14d628e711e82746ee37c489cf151` (plain-pushed and independently read back).
- Baseline and sole parent: upstream main `07ccc73cd24e26e59da169ca35b3fd5f64c35986`. No picker integration or backend API expansion is inherited.
- Receipt `receipts/SOLR-18537-path-coverage-main-round-1.md`.
- Seed `5110A7C0DE`.
- Exact changed file: `solr/core/src/test/org/apache/solr/servlet/NodeTargetProxyFilterTest.java`; production is unchanged.

## Required proof

This is coverage-only work. The unchanged baseline suite and candidate suite must both pass. The two new methods cover header and query targeting separately, with space, fragment and literal-percent path content. They assert final raw URI, `_forwardedCount=1` (proving a real collection forwarding hop), query-value preservation, foreign-header preservation and consumed targeting directives.

Verify live fork equals the required head. Use full standalone clones/checkouts suitable for root checks, not the sparse Windows authoring tree. Record versions, parents, tree and timestamps. In the baseline checkout run:

```sh
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.servlet.NodeTargetProxyFilterTest -Ptests.seed=5110A7C0DE
```

Expected baseline count: 14 methods, no failures/errors/skips. At the candidate run:

```sh
./gradlew tidy
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.servlet.NodeTargetProxyFilterTest --tests org.apache.solr.servlet.NodeTargetProxyFilterAuthTest -Ptests.seed=5110A7C0DE
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.servlet.HttpSolrProxyTest --tests org.apache.solr.servlet.SolrRequestParserTest -Ptests.seed=5110A7C0DE
./gradlew check -x test
```

Require tidy-clean tracked tree and all positive legs passing with zero errors/skips. Expected focused counts: 16 filter and 4 auth methods. Preserve fresh XML before each cleanTest; report neighbor totals from the XML. Record any NODE_ENV/npm condition with the root check result.

## Required isolated mutation

Create an isolated candidate checkout, keep the new tests unchanged, and modify only HttpSolrCall.sendRemoteProxy's URI path expression from `new URI(null, null, path, null).getRawPath()` to `new URI(null, null, path, null).getRawPath().replace("%23", "%2523")`. This preserves compilation, the executable API/echo handler and valid URI syntax, while double-encoding the fragment character on the second hop. Record the full mutation diff and run:

```sh
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.servlet.NodeTargetProxyFilterTest.testEncodedPathPreservedAcrossReplicaHopWithHeader --tests org.apache.solr.servlet.NodeTargetProxyFilterTest.testEncodedPathPreservedAcrossReplicaHopWithQueryParam -Ptests.seed=5110A7C0DE
```

Require both methods to execute and fail specifically at the final requestURI equality assertion (`%2523` versus `%23`). Compile/setup failures, HTTP errors before the assertion, timeouts or skips are not mutation proof. This one mutation proves the second-hop URI assertion, not every other assertion in these tests. Keep the mutation off the public source branch. Preserve fresh logs/XML and the actual mutated source diff.

## Repairs and receipt

Investigate executed failures before retrying. Environment-only interruptions and assertion failures are separate outcomes. If formatting or a source/test repair changes the candidate, commit scoped repairs as Nick Shanin with no trailers/PR references, plain-push, independently verify the new full SHA and post a superseding gate; rerun all required legs at that head.

Receipt must state exact head/baseline, commands/seed, toolchain/times, exit codes/counts, mutation diff/assertion failures, fresh log/XML paths, final clean tree and live fork SHA. End with a short PR-description draft. No Windows Gradle ran, and no runtime outcome is claimed yet. No new changelog is needed for this test-only slice. No PR, GitHub comment, Jira action, contributor-branch write or upstream push is authorized. Status remains Implemented, Proof Pending until the full receipt is green.
