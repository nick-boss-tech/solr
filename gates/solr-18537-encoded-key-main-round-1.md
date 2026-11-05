# Gate: encoded node-target query keys on main, round 1

Status: OPEN. Intended host: capable Linux gate host with JDK 21 and the pinned Gradle toolchain.

- Assignment `assignments/solr-12613-adjacent-implementation-round-1.md`; claim `claims/solr-18537-transport-main-windows-round-1.md`.
- Supersedes the withdrawn `gates/solr-12613-adjacent-encoded-key-round-1.md`. Scope separation record: `gates/solr-12613-adjacent-scope-standdown-round-1.md`. That record's withdrawal of the old inputs remains in force; this is a new main-based input.
- Fork branch `nick-boss-tech/solr:solr-18537-encoded-key-followup`.
- Required head `7b83425fe7ce704ee781f273cd099b4425b795af`, remote read-back verified after plain push.
- Baseline and sole parent: upstream main `07ccc73cd24e26e59da169ca35b3fd5f64c35986`. No picker integration or backend API expansion is inherited. Tests call existing v1/v2 endpoints.
- Receipt target `receipts/SOLR-18537-encoded-key-main-round-1.md`.
- Seed `5110EAC0DE`. Run serially under the host build lock. Use fresh full standalone clones/checkouts suitable for root check, not the sparse Windows authoring tree.

## Behavior and files

The filter parses GET queries when the literal targeting name or percent escapes are present, so encoded keys resolve and are consumed. It retains header/query conflict checks, POST query non-routing, and the old unrelated-query header-preservation fix. Query parse errors are sent as their actual status inside the filter so AuthenticationFilter cannot reclassify them.

Only these files differ from the main baseline:

- `solr/core/src/java/org/apache/solr/servlet/NodeTargetProxyFilter.java`.
- `solr/core/src/test/org/apache/solr/servlet/NodeTargetProxyFilterTest.java`.
- `solr/core/src/test/org/apache/solr/servlet/NodeTargetProxyFilterAuthTest.java`.

No runtime proof was executed on Windows. No changelog fragment is included: a separate public issue/PR identity for this adjacent defect remains undecided. SOLR-18537 in the branch/commit identifies the generic transport provenance, not an approved public submission scope. Do not create a public issue or PR.

## Required positive legs

Verify live fork head equals the required SHA and record Git parents/tree and toolchain versions before execution. Run:

```sh
./gradlew tidy
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.servlet.NodeTargetProxyFilterTest --tests org.apache.solr.servlet.NodeTargetProxyFilterAuthTest -Ptests.seed=5110EAC0DE
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.servlet.HttpSolrProxyTest --tests org.apache.solr.servlet.SolrRequestParserTest -Ptests.seed=5110EAC0DE
./gradlew check -x test
```

Require tidy-clean tracked tree, successful tests with no errors/skips, and successful root check. Expected focused method counts: 19 NodeTargetProxyFilterTest and 6 NodeTargetProxyFilterAuthTest (25 total). Preserve XML before the next cleanTest. Neighbor counts must be reported from fresh XML, not assumed. Record NODE_ENV and any required Antora/npm environment condition next to the check result.

New positive coverage: encoded keys via v1/v2 and lower/uppercase hex spelling; encoded-key/header conflict; encoded unrelated value retaining the header; POST non-routing; encoded directive consumed across the collection forwarding hop; authenticated encoded-key routing; authenticated malformed-UTF8 query returning 400. Raw Jetty paths must reach Solr with the encoded key intact. If the client normalizes the key first, repair the test and regate a newly pushed head; do not claim encoding proof from a normalized request.

## Required fail-before leg

In an isolated checkout of main baseline `07ccc73cd24e26e59da169ca35b3fd5f64c35986`, overlay only the two new test files from the required head, leaving production code unchanged. Record the overlay diff. Run:

```sh
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.servlet.NodeTargetProxyFilterTest.testEncodedQueryParamProxiesGet --tests org.apache.solr.servlet.NodeTargetProxyFilterTest.testEncodedQueryParamAndHeaderAreRejected --tests org.apache.solr.servlet.NodeTargetProxyFilterTest.testEncodedQueryDirectiveConsumedWhenForwardingToReplica --tests org.apache.solr.servlet.NodeTargetProxyFilterAuthTest.testEncodedQueryParamProxiesWhenAuthenticated -Ptests.seed=5110EAC0DE
```

Require 4 selected methods to execute and fail at the intended routing/status/consumption assertions: local node instead of target, 200 instead of conflict 400, retained directive instead of absent, and authenticated local node instead of target. Compilation failures, setup errors, timeouts and skips do not satisfy fail-before. The GET method loops over path/key variants and may stop at its first baseline assertion; report that boundary rather than claiming every loop variant was independently proven negative. Other new tests are positive contract coverage unless separately proved.

## Repair and finishing rules

Record and investigate any executed failure before retrying under WORKFLOW.md. Separate host setup failure from test assertion failure. If tidy or any source/test repair changes tracked content, commit the scoped repair as Nick Shanin without trailers/PR references, plain-push to this fork branch, verify the full new SHA and post a superseding gate. All required proof must then run at that new head; this gate's old-head results do not transfer.

Receipt must name exact tested head/baseline, commands/seeds, timestamps/toolchain, exit codes and method totals, fresh log/XML paths, final tree cleanliness and live remote SHA. Status is Implemented, Proof Pending until every required leg is satisfied. No PR creation, GitHub comment, Jira write, contributor-branch change or upstream push is authorized. End with a concise PR-description draft for later owner review.
