# SOLR-18623 failed-gate repair, round 3

Status: Implemented, Proof Pending. The H2 gate failed and is not green.

- Repair claim: `claims/pr-5301-reconnect-repair-windows-round-3.md`, verified board commit `ff9a4d99f8d526112366d8562c6e5862a853d097`.
- Input receipt: `receipts/PR-5301-reconnect-liveness-round-1.md` and execution report `reports/pr-5301-reconnect-gate-round-2.md`, read at `75bc158448ef0781d5ccbaf0f9abe35856a6b300`.
- Failed H2: `27a9cf105b25f1bf3d604534cf8bdd8674c470fc`; fork branch `solr-18623-reconnect-liveness`.
- Repaired H3: `dfde9bde0a348ad44e6f6ceec197d478ef11bfd0`; committed, plain-pushed and independently read back from the fork refs API at the same full SHA. Ordinary child of H2, authored and committed by Nick Shanin with no trailers. The worktree is clean.
- Contributor H0 remains `01e820f0539cddc4697d26176d216614a776a99a`, PR 5301, branch_9x.
- New gate: `gates/pr-5301-reconnect-liveness-round-3.md`; reserved new receipt `receipts/PR-5301-reconnect-liveness-round-3.md`. Preserve the failed H2 receipt.

## Evidence audit

The downloaded archive matches SHA-256 `80a8d934a581dfb6979c22943793795cfb7717044fd2691057582637f4c29e33`; all 40 manifest entries match their hashes. XML confirms the ordinary suite 5/5 passing, existing ConnectionManagerTest 2 passing plus one pre-existing ignored test, SolrZkClientTest 7/7 passing, exact-H0 production liveness negative 1/1 failing at its named bounded assertion, and the accept-all mutation 3/3 failing at intended connected-state assertions. These prove four discriminating methods; the in-order fifth method remains a coverage pin. The receipt draft's wording suggesting every one of five is negative-proven is too broad and should not be copied to public text.

Root check fails on the new suite's missing `assumeWorkingMockito()` call. Both stress invocations fail at session-expiration recovery, same seed/sub-seed and assertion. These are real H2 failures; no rerun has passed and no acceptance condition has been weakened.

## Repair 1: required Mockito guard

Add the static import from SolrTestCaseJ4 and invoke `assumeWorkingMockito()` from a static `@BeforeClass` method, matching the existing SolrTestCase-based SplitShardCmdCleanupTest. The suite continues to extend SolrTestCase. All five methods must still execute on the Linux gate; an assumption skip is not an acceptable substitute for their proof.

## Repair 2: retain the proxy listener while dropping client connections

The first stress log records a final proxy close/reopen at elapsed 60130/60131 ms, then the Expired callback at 60913 ms and repeated connection-refused responses on the replacement's SendThread for the remainder of the drain window. No proxy acceptance follows the final reopen. The independent rerun has the same shape after its final reopen at 46404 ms. The existing SocketProxy.reopen implementation catches creation/bind/start exceptions and logs only at debug; these failure logs do not contain that hidden exception. A particular bind exception is therefore not claimed as established.

The repair calls SocketProxy.halfClose() instead of close()+reopen() inside flap(). That documented API closes existing client receive connections and leaves the acceptor/listening port open. It exercises the intended ZooKeeper connection loss while removing listener rebinding from a test of callback ordering. The test still requires a fresh Disconnected callback on every flap, physical ZooKeeper reconnection, matching delayed/processed counters, all 25 iterations and five expirations, and the original 10-second settle / 30-second drain deadlines. No assertion, timeout, seed, expiration injection or production code is changed.

The adjacent FullSolrCloudDistribCmdsTest explicitly avoids halfClose for a different condition: forcing a subsequent HTTP operation to fail despite automatic retries. This stress test instead requires observation of a Disconnected event and subsequent successful reconnect, so that comment does not prevent this use. The halfClose pumps close their peer socket on teardown; they do not stop the listener.

This is a source-supported fixture repair hypothesis. Same-seed Linux comparisons are required to establish the runtime result. Do not claim it passed or that it repairs an additional production expiration bug before that proof.

## Scope and checks

Only two existing tests change: ConnectionManagerOrderingTest.java and TestConnectionManagerReconnectStress.java. The production fix and both changelog entries at H2 are retained unchanged. Local `git diff --check` passed. The scoped commit helper requires exactly those two outbound files, H2 as parent, Nick Shanin's author/committer identity and the expected fork branch. Windows ran no Gradle, compiler, formatter or tests.

Error Prone's FloatRangeFieldTest FloatingPointLiteralPrecision warning was independently reproduced at H0 in the Linux diagnostics. It is outside this diff and is not repaired here. The new job requires Error Prone diagnostics to remain enabled; it permits the build's documented `-Pjavac.failOnWarnings=false` only to complete compilation past this known baseline warning, requires no new diagnostics and records the warning-tolerance condition explicitly. The root check must pass; it is not waived. Changelog generation is required, with existing cross-branch bookkeeping errors kept separate from fragment validity.

The first two attempts to add this claim through the Contents API failed with HTTP 500, and the path was confirmed absent afterward. The board's documented temporary-index / normal fast-forward push route was used instead, through a task-scoped helper that can only add PR 5301 records. It never edits a shared index, overwrites a record, force-pushes or targets upstream, and verifies the pushed commit and blob against the fetched live tip.

No public PR comment, review, JIRA write or PR body edit has been made. The draft will pin H3 and retain the failed/pending validation limits. A new gate handoff is not a passing receipt.
