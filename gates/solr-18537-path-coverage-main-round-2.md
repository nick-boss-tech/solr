# Gate: encoded paths and ambiguous-path rejection, repair round 2

Status: OPEN. Intended host: capable Linux gate host with JDK 21 and the pinned Gradle toolchain, serialized under the host build lock.

- Parent assignment `assignments/solr-12613-adjacent-implementation-round-1.md`; repair claim `claims/solr-18537-path-coverage-repair-windows-round-2.md`.
- Supersedes `gates/solr-18537-path-coverage-main-round-1.md` for this repaired head. Its actual failed result remains at `receipts/SOLR-18537-path-coverage-main-round-1.md`; this is an investigated test-design repair, not a blind retry or an environment-only relaunch.
- Fork `nick-boss-tech/solr:solr-18537-path-coverage-followup`.
- Required head `0fad9847cc89973c751b1732aa22a259764610b2`, plain-pushed and separately read back from the GitHub ref.
- Previous failed head `59ad950e1ac14d628e711e82746ee37c489cf151`; baseline main `07ccc73cd24e26e59da169ca35b3fd5f64c35986`.
- Only changed file against main: `solr/core/src/test/org/apache/solr/servlet/NodeTargetProxyFilterTest.java`. Production and URI-compliance configuration are unchanged; this remains coverage-only work.
- Receipt target `receipts/SOLR-18537-path-coverage-main-round-2.md`; seed `5110A7C0DE`.

## Investigation and repair

Round 1's two new methods failed with Jetty 400 Ambiguous URI path encoding before the echo handler or assertions ran. The old double-encoding mutation failed the same way and proved no assertion. Baseline 14/14, the other candidate methods, neighbors, tidy and the verified-head root check passed. Preserve that distinction.

Jetty 12.1.14 is pinned by main. Its HttpURI contract marks any encoded percent character as ambiguous: https://github.com/jetty/jetty.project/blob/jetty-12.1.14/jetty-core/jetty-http/src/main/java/org/eclipse/jetty/http/HttpURI.java . Therefore `%25d` is not a safe replacement for `%2523`; both must stay out of successful forwarding paths. No compliance flags or server behavior are relaxed.

The two forwarding methods now use `/a%20b%23c`, matching the space/hash content of the pre-existing passing single-hop test. Both must still make a real replica hop and assert `_forwardedCount=1` before the final raw-URI equality assertion. Query marker `space # % +`, foreign-header preservation, and consumed header/query directives remain checked.

New `testAmbiguousEncodedPathsRemainRejected` uses raw Jetty requests for `/a%25b` and `/a%2523b` with header and query targeting separately (four requests). Each must return 400 with `Ambiguous URI` in the response body. These are deliberate rejection checks, not successful second-hop requests or mutation-proven assertions. They preserve the percent-path boundary that the original positive fixture incorrectly tried to cross.

The new mutation changes encoded `#` to encoded `$` only in HttpSolrCall's second-hop URI construction. The encoded path remains valid and the echo handler must respond. This targets path corruption without manufacturing another ambiguity rejection.

## Runner preflight and required positive legs

Use fresh full standalone checkouts suitable for root check, not the sparse Windows tree. Verify local and live fork HEAD equal the required SHA before running. Record parents/tree, JDK/Gradle, UTC times and fresh log/XML locations. Use the existing host wrapper/egress setup and serialize builds under the lock. Investigate stale-daemon Gradle-journal contention before retrying; the earlier runner defects are not patch failures. If root check uses a separate clone, explicitly fetch the candidate SHA into that clone and require `rev-parse HEAD` to match; a local clone can omit fetched-only commits.

At the main baseline run:

```sh
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.servlet.NodeTargetProxyFilterTest -Ptests.seed=5110A7C0DE
```

Require 14 methods, zero failures/errors/skips. At the candidate run:

```sh
./gradlew tidy
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.servlet.NodeTargetProxyFilterTest --tests org.apache.solr.servlet.NodeTargetProxyFilterAuthTest -Ptests.seed=5110A7C0DE
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.servlet.HttpSolrProxyTest --tests org.apache.solr.servlet.SolrRequestParserTest -Ptests.seed=5110A7C0DE
./gradlew check -x test
```

Require tidy-clean tracked tree and all positive legs passing without failure/error/skip/thread leak. Expected counts: 17 filter methods plus 4 auth; neighbor counts were 2+9 in round 1, but report actual fresh XML totals. Preserve XML before every cleanTest. Expected 400 requests inside the new rejection method are passing assertions, not positive-leg HTTP exceptions. Successful forwarding methods must receive actual echo responses. Record NODE_ENV, CA/relay truststore and any npm/Antora condition beside the result. Do not borrow round 1's positive/root results for the changed candidate.

## Required isolated mutation

In an isolated checkout of the candidate, keep all tests and API/handler setup unchanged. In `HttpSolrCall.sendRemoteProxy` replace only `new URI(null, null, path, null).getRawPath()` with `new URI(null, null, path, null).getRawPath().replace("%23", "%24")`. Record the full diff. Run:

```sh
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.servlet.NodeTargetProxyFilterTest.testEncodedPathPreservedAcrossReplicaHopWithHeader --tests org.apache.solr.servlet.NodeTargetProxyFilterTest.testEncodedPathPreservedAcrossReplicaHopWithQueryParam -Ptests.seed=5110A7C0DE
```

Require both methods to execute, obtain a successful echo response and pass the preceding forwarded-count assertion, then fail specifically at final requestURI equality (`/a%20b%23c` expected versus `/a%20b%24c` actual). HTTP rejection, compilation/setup errors, timeouts, skipped tests or failure of the forwarding-count assertion do not satisfy this mutation. Investigate the actual failure before any repair/retry. Keep mutations off the fork branch. The mutation proves this URI assertion for both targeting modes; query/header/rejection assertions without their own discriminating mutation remain positive coverage pins.

## Repairs and receipt

Any formatting/source/test repair must be a scoped ordinary Nick Shanin commit without PR references or attribution trailers, plain-pushed to the fork and independently read back. Publish a superseding exact-head job, rerun all required proof at that head, and notify the combined-input record that its source needs recomposition. No old-head proof transfer.

The receipt must state exact baseline/candidate/remote head, commands/seeds, toolchain/times, exits/counts, actual mutation diff/assertion messages, fresh logs/XML, verified root-clone HEAD and final tree. End with an unposted draft describing accepted-path preservation and explicit ambiguous-path rejection, without claiming all assertions mutation-proven. No new changelog is required for this test-only slice. Windows ran no Gradle, compiler, browser or tests. No PR, GitHub/Jira comment, contributor/upstream write or compliance-policy change is authorized. Status stays Implemented, Proof Pending.
