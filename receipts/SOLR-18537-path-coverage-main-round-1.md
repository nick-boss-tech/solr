# Receipt: SOLR-18537 path-coverage gate, round 1 (phone agent)

Status: GATE FAILED at the candidate head. Both new coverage methods fail before reaching their assertions; the required mutation leg is not satisfied. This is a factual failing receipt; no repair was attempted by the gate host, because the failure is in the test design's interaction with Jetty, not in formatting or setup.

- Job: `gates/solr-18537-path-coverage-main-round-1.md`. Claim: `claims/solr-18537-path-coverage-main-round-1-phone-agent.md`. Executed 2026-11-05T04:14Z to 04:22Z, with the root check leg completed 2026-11-05T04:57Z after two void attempts (below). Seed `5110A7C0DE`, JDK 21, Gradle 9.7.0 via `~/workspace/tools/solr-gradle.sh`, one build at a time under the host build lock. Test legs in clean worktrees; root `check -x test` in a normal clone whose checkout was verified by recorded `rev-parse HEAD`.
- Candidate head: `59ad950e1ac14d628e711e82746ee37c489cf151` on `solr-18537-path-coverage-followup`; test-only change to `NodeTargetProxyFilterTest.java`, production unchanged. Baseline: upstream main `07ccc73cd24e26e59da169ca35b3fd5f64c35986`. Live fork head re-verified through the GitHub API after the run: unchanged.

## Checks, independently

- Baseline leg (unmodified main, `NodeTargetProxyFilterTest`): PASS, 14 of 14, zero failures, errors, or skips, as the job expects.
- Tidy preflight at the candidate head: rc 0, tracked tree clean.
- Candidate focused suites: `NodeTargetProxyFilterAuthTest` 4 of 4 pass. `NodeTargetProxyFilterTest` 16 tests with 2 failures, both in the new methods:
  - `testEncodedPathPreservedAcrossReplicaHopWithHeader` and `testEncodedPathPreservedAcrossReplicaHopWithQueryParam` each fail with a `RemoteSolrException` wrapping the receiving node's response: HTTP 400, "Ambiguous URI path encoding" (Jetty). The forwarded path as sent is `/solr/coll/echo/a%20b%23c%25d%2523e` plus the test's query string. Jetty's URI compliance rejects the encoded path before the echo handler runs, so neither method reaches its final requestURI, forwarded-count, or preservation assertions. The remaining 14 methods pass.
  - Reading: the production forwarding code is unchanged by this slice, and the pre-existing suite passes at both baseline and candidate. What fails is the new coverage's chosen path content: a literal `%23` sequence inside a path segment is exactly the ambiguous encoding Jetty refuses. As written, the two methods cannot pass against a Jetty endpoint with this compliance behavior; proving second-hop preservation needs path content Jetty accepts, or an endpoint configuration that admits it. That redesign belongs to the implementation side.
- Candidate neighbors: `HttpSolrProxyTest` 2 of 2 and `SolrRequestParserTest` 9 of 9 pass.
- Mutation leg (isolated candidate checkout; `HttpSolrCall.sendRemoteProxy` path expression given `.replace("%23", "%2523")`, diff retained): both new methods executed and failed, but with the same 400 "Ambiguous URI path encoding" exception as the positive leg, not at the final requestURI equality assertion (`%2523` versus `%23`). Under the job's terms, an HTTP error before the assertion is not mutation proof. The leg is NOT satisfied, and cannot be evaluated until the positive leg's path content reaches the handler.
- Root `check -x test` in the clone at verified head `59ad950e1a`: BUILD SUCCESSFUL in 11m 5s, rc 0, tracked tree clean afterward. Two earlier attempts are void and claimed for no head: the first died on the Gradle journal lock held by a stale daemon, and the second built the clone's default head because the candidate checkout had silently failed (a local clone does not carry commits that exist in the source repo only as fetched objects). The counted run fetched the SHA inside the clone and recorded `rev-parse HEAD` before building. The combined Hatch CA truststore was supplied to the Gradle JVM for downloads through the local relay, per the host's recorded egress CA condition.

## Consequence

The encoded-key slice (round 2, GREEN) is unaffected. This slice's receipt is one of the four prerequisites for `gates/solr-12613-scoped-integration-round-1.md`; that integration gate remains unexecutable until this slice is redesigned and re-gated green. No source change, push, PR, or public comment was made by the gate host.

## Draft PR description (for later owner review, once the slice is repaired; no PR is authorized by this gate)

Test coverage for encoded paths across the replica proxy hop: two methods in NodeTargetProxyFilterTest drive header-targeted and query-targeted requests whose paths contain space, fragment, and literal-percent content across a real collection forwarding hop, asserting the final raw URI, a forwarded count of one, query-value preservation, foreign-header preservation, and consumption of the targeting directives. (Status: the methods as written are rejected by Jetty with 400 Ambiguous URI path encoding and must be redesigned before this description applies.)

## Evidence on the gate host

- `~/workspace/tools/gate-18537-path-coverage/` (master.log, per-leg logs, fresh JUnit XML per leg, mutation diff, root-check logs for the void attempts and the counted rerun, recorded check-clone HEAD).
