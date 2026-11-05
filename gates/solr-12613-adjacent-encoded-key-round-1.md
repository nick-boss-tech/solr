# Gate: encoded node-target query keys, adjacent follow-up round 1

Status: OPEN. Intended host: capable Linux gate host with JDK 21 and the pinned Gradle toolchain.

- Assignment: `assignments/solr-12613-adjacent-implementation-round-1.md`.
- Implementation claim: `claims/solr-12613-adjacent-windows-round-1.md`.
- Fork branch: `nick-boss-tech/solr:pr-5110-followup-encoded-key`.
- Required head: `bd05d1ed39a005317f0a6655e4cd8fed078531b8`, remote read-back verified.
- Integration baseline: `6ec2e527ad716bde74404b2037bb1a1e6c52f79c`, ordinary conflict-free merge of main `07ccc73cd24e26e59da169ca35b3fd5f64c35986` and contributor head `73b30956ff2c0f7b16f6af3bceacf95bda8271b9`.
- Receipt target: `receipts/PR-5110-adjacent-encoded-key-round-1.md`.
- Seed: `5110EAC0DE`. Run serially under the host build lock. Use fresh full standalone clones/checkouts suitable for root check, not the sparse Windows authoring tree.

## Behavior and files

The filter parses GET queries when the literal targeting name or percent escapes are present, so encoded keys resolve and are consumed. It retains header/query conflict checks, POST query non-routing, and the old unrelated-query header-preservation fix. Query parse errors are sent as their actual status inside the filter so AuthenticationFilter cannot reclassify them.

Only these files differ from the integration baseline:

- `solr/core/src/java/org/apache/solr/servlet/NodeTargetProxyFilter.java`.
- `solr/core/src/test/org/apache/solr/servlet/NodeTargetProxyFilterTest.java`.
- `solr/core/src/test/org/apache/solr/servlet/NodeTargetProxyFilterAuthTest.java`.

No runtime proof was executed on Windows. No new changelog fragment was created because this adjacent bug does not yet have its own Jira/PR identity; the original PR's fragment belongs to the integration baseline. Public packaging remains pending, and no public issue or PR should be created by the runner.

## Required positive legs

Verify live fork head equals the required SHA and record Git parents/tree and toolchain versions before execution. Run:

```sh
./gradlew tidy
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.servlet.NodeTargetProxyFilterTest --tests org.apache.solr.servlet.NodeTargetProxyFilterAuthTest -Ptests.seed=5110EAC0DE
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.servlet.HttpSolrProxyTest --tests org.apache.solr.servlet.SolrRequestParserTest --tests org.apache.solr.handler.admin.api.NodeAdminProxySolrCloudTest -Ptests.seed=5110EAC0DE
./gradlew check -x test
```

Require tidy-clean tracked tree, successful tests with no errors/skips, and successful root check. Expected focused method counts: 19 NodeTargetProxyFilterTest and 6 NodeTargetProxyFilterAuthTest (25 total). Preserve XML before the next cleanTest. Neighbor counts must be reported from fresh XML, not assumed. Record NODE_ENV and any required Antora/npm environment condition next to the check result.

New positive coverage: encoded keys via v1/v2 and lower/uppercase hex spelling; encoded-key/header conflict; encoded unrelated value retaining the header; POST non-routing; encoded directive consumed across the collection forwarding hop; authenticated encoded-key routing; authenticated malformed-UTF8 query returning 400. Raw Jetty paths must reach Solr with the encoded key intact. If the client normalizes the key first, repair the test and regate a newly pushed head; do not claim encoding proof from a normalized request.

## Required fail-before leg

In an isolated checkout of integration baseline `6ec2e527ad716bde74404b2037bb1a1e6c52f79c`, overlay only the two new test files from the required head, leaving production code unchanged. Record the overlay diff. Run:

```sh
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.servlet.NodeTargetProxyFilterTest.testEncodedQueryParamProxiesGet --tests org.apache.solr.servlet.NodeTargetProxyFilterTest.testEncodedQueryParamAndHeaderAreRejected --tests org.apache.solr.servlet.NodeTargetProxyFilterTest.testEncodedQueryDirectiveConsumedWhenForwardingToReplica --tests org.apache.solr.servlet.NodeTargetProxyFilterAuthTest.testEncodedQueryParamProxiesWhenAuthenticated -Ptests.seed=5110EAC0DE
```

Require 4 selected methods to execute and fail at the intended routing/status/consumption assertions: local node instead of target, 200 instead of conflict 400, retained directive instead of absent, and authenticated local node instead of target. Compilation failures, setup errors, timeouts and skips do not satisfy fail-before. The GET method loops over path/key variants and may stop at its first baseline assertion; report that boundary rather than claiming every loop variant was independently proven negative. Other new tests are positive contract coverage unless separately proved.

## Repair and finishing rules

Record and investigate any executed failure before retrying under WORKFLOW.md. Separate host setup failure from test assertion failure. If tidy or any source/test repair changes tracked content, commit the scoped repair as Nick Shanin without trailers/PR references, plain-push to this fork branch, verify the full new SHA and post a superseding gate. All required proof must then run at that new head; this gate's old-head results do not transfer.

Receipt must name exact tested head/baseline, commands/seeds, timestamps/toolchain, exit codes and method totals, fresh log/XML paths, final tree cleanliness and live remote SHA. Status is Implemented, Proof Pending until every required leg is satisfied. No PR creation, GitHub comment, Jira write, contributor-branch change or upstream push is authorized. End with a concise PR-description draft for later owner review.
