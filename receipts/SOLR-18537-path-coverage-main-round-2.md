# Receipt: SOLR-18537 encoded paths and ambiguous-path rejection, round 2

Verdict: GATE GREEN at candidate `0fad9847cc89973c751b1732aa22a259764610b2`. Every leg of `gates/solr-18537-path-coverage-main-round-2.md` passed, including the redesigned mutation proof that round 1 could not produce. This receipt supersedes the round 1 result for the repaired head only; the round 1 receipt at `receipts/SOLR-18537-path-coverage-main-round-1.md` stands as the record for `59ad950e1ac1`.

## Identity

- Executor: phone agent (Linux VM), claimed after the owner noted the update in chat; claim `claims/solr-18537-path-coverage-main-round-2-phone-agent.md`. Repair report: `reports/solr-18537-path-coverage-repair-round-2.md`.
- Fork branch `nick-boss-tech/solr:solr-18537-path-coverage-followup`; required head `0fad9847cc89973c751b1732aa22a259764610b2` read live through the GitHub API before claiming and again after the final leg, unchanged. Baseline main `07ccc73cd24e26e59da169ca35b3fd5f64c35986`. Diff against main: one test file, `solr/core/src/test/org/apache/solr/servlet/NodeTargetProxyFilterTest.java`. Production unchanged.
- Host: this Linux VM, vendored JDK 21, runner `~/workspace/tools/solr-gradle.sh` (Gradle 9.7.0), serialized under the host build lock. Logs and fresh JUnit XML on this host under `~/workspace/tools/gate-18537-path-r2/`. Seed `5110A7C0DE` for every test leg.

## Preflight

- Detached tidy at the required head: rc 0, tracked tree clean, `git diff --check` clean. One environment repair preceded it: the first attempt died on the Gradle journal lock held by two stale daemons from the preceding SOLR-18624 gate; daemons killed, lock cleared, rerun clean. No test had executed.

## Positive legs (2026-11-05T05:57:16Z to 06:10:29Z)

- Baseline on main, `NodeTargetProxyFilterTest`: 14 tests, 0 failures, 0 errors, 0 skipped. rc 0.
- Candidate, `NodeTargetProxyFilterTest` plus `NodeTargetProxyFilterAuthTest`: 17 plus 4 tests, 0 failures, 0 errors, 0 skipped. rc 0. The 17 include both redesigned forwarding methods on `/a%20b%23c` (real replica hop, `_forwardedCount=1`, final raw-URI equality) and the new `testAmbiguousEncodedPathsRemainRejected`, whose four expected 400 responses are passing assertions inside the method.
- Neighbors, `HttpSolrProxyTest` plus `SolrRequestParserTest`: 2 plus 9 tests, all clean. rc 0.
- Candidate tree clean after the legs and after the mutation revert.

## Mutation (isolated, candidate checkout)

One production line in `HttpSolrCall.sendRemoteProxy` (line 650):

```diff
-      URI uri = URI.create(coreUrl + new URI(null, null, path, null).getRawPath() + queryStr);
+      URI uri = URI.create(coreUrl + new URI(null, null, path, null).getRawPath().replace("%23", "%24") + queryStr);
```

Both methods executed (2 tests, 2 failures, 0 errors, 0 skipped), received the echo response, and failed at the final requestURI equality, as required:

- `testEncodedPathPreservedAcrossReplicaHopWithHeader`: `ComparisonFailure: expected:<...lr/coll/echo/a%20b%2[3]c> but was:<...lr/coll/echo/a%20b%2[4]c>`
- `testEncodedPathPreservedAcrossReplicaHopWithQueryParam`: identical comparison failure.

No HTTP rejection, setup failure, or forwarding-count failure occurred; the corruption reached the handler and was caught by the URI assertion in both targeting modes. The mutation was reverted; it exists in no branch or retained checkout state.

## Root check

- `check -x test` at the candidate head in a normal git clone (layout disclosed per host rule: Antora cannot run in a worktree). The clone fetched the candidate SHA explicitly and recorded `git rev-parse HEAD` as `0fad9847cc89973c751b1732aa22a259764610b2` before building. BUILD SUCCESSFUL in 7m 39s, 556 actionable tasks, rc 0. Clone tree clean afterwards. The check leg ran with the host CA truststore for the egress relay, the same disclosed condition as prior gates on this host.

## Scope of proof, stated plainly

- Mutation-proven: the final raw-URI equality assertion in both forwarding methods (header and query-param targeting).
- Positive coverage, not mutation-proven: the ambiguous-path rejection method, the query marker, foreign-header preservation, and consumed-directive checks, consistent with the job's terms.
- This receipt is one of the five required inputs to `gates/solr-12613-scoped-integration-round-2.md`. The combined job still waits on the frontend-only baseline and selection-frontend receipts, which require a browser host; nothing about this result infers them.

## PR description draft (not posted)

Title: `SOLR-18537: Encoded path coverage and ambiguous-path rejection for node-target proxying`

Body:

🤖 *AI text below* 🤖 *(reviewed by Nick Shanin)*

Test-only coverage for the node-target proxy filter, following up the merged `X-Solr-targetNode` transport. No production code changes.

- Two forwarding tests drive an encoded path containing a space and a hash (`/a%20b%23c`) across a real replica hop, once with header targeting and once with query-parameter targeting, and assert the forwarded count and the exact raw request URI at the receiving node.
- A rejection test pins the platform boundary: paths carrying an encoded percent (`%25`, `%2523`) are ambiguous under Jetty's URI rules and must be answered 400, for both targeting modes, instead of being forwarded.
- The existing query marker, foreign-header, and consumed-directive checks stay in place.

Verification: at the offered head, the filter suite passes 17 of 17 and the auth suite 4 of 4 (seed 5110A7C0DE), against a 14 of 14 baseline on main; the proxy and parser neighbor suites pass; tidy is clean; the full `check -x test` build succeeded. A temporary mutation that rewrites the encoded hash to an encoded dollar in the second-hop URI construction, reverted after the run, makes both forwarding tests fail on the final URI equality, which proves those assertions observe the forwarded path.

Limits: only the URI-equality assertions are mutation-proven; the rejection and header checks are positive coverage. The original fixture's fuller encoded path is not used for the success cases because Jetty rejects encoded percents in paths as ambiguous; that behavior is now pinned by the rejection test instead.

*Nick Shanin directed the work and takes responsibility for this contribution.*
