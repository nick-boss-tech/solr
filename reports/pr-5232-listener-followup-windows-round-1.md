# PR 5232 listener correctness follow-up: Windows implementation

Status: IMPLEMENTED, PROOF PENDING

- Source: Nick's October 26 request to produce a branch above PR 5232 fixing both review findings.
- Claim: `claims/pr-5232-followup-windows-round-1.md`, posted at `dd21b62616c4ddf24c3e7a20c85b5b1ca11875a8`.
- Original PR / exact parent: `https://github.com/apache/solr/pull/5232`, `2c9243b111b38367677d7e0c9b30b8e72c554d2c`.
- New fork branch: `https://github.com/nick-boss-tech/solr/tree/solr-11994-submit`.
- Pushed fix: `b3a11484f13feaab55fe4f27504bfce3d1bdd2ef` (one commit directly above the PR).
- Commit subject: `SOLR-11994: correct listener CPU accounting and exclude nested searches`.
- Commit author: Nick Shanin, `nick.boss.us@gmail.com`, no attribution trailers. The contributor's two parent commits remain intact.
- Local worktree: `C:\Users\shaninna\dev\Solr-issues\wt\SOLR-11994`, branch `wt-solr-11994-submit`.
- Gate: `gates/pr-5232-listener-followup-round-1.md`.
- Receipt target: `receipts/PR-5232-listener-followup.md`.

## Changes

F1: `SearchRequestInfo.cpuTimeMs` now checks that a shard CPU total was actually published before adding local CPU. Distributed topology alone does not establish that the response header contains shard CPU: after an early parse failure, `RequestHandlerBase` fills that header with local CPU. This keeps the fallback from being counted twice while preserving the latest aggregate on successful or partial distributed responses.

F2: `SolrRequestInfo.isNestedRequest` exposes whether another request is below the current request on its thread's existing stack. `SearchHandler` skips notifications for nested requests, including `[subquery]` searches run through `EmbeddedSolrServer` during shard response writing. No client parameter or new request flag is required. Top-level embedded requests remain eligible. The listener Javadoc now names the exclusion.

The existing changelog retains Amrit Sarkar and adds Nick Shanin as a follow-up contributor. No unrelated source or research file enters the implementation branch.

## Exact follow-up file list

Seven files, 167 additions and three deletions, relative to the original PR head:

- `changelog/unreleased/SOLR-11994-search-request-listener.yml`
- `solr/core/src/java/org/apache/solr/handler/component/SearchHandler.java`
- `solr/core/src/java/org/apache/solr/handler/component/SearchRequestInfo.java`
- `solr/core/src/java/org/apache/solr/handler/component/SearchRequestListener.java`
- `solr/core/src/java/org/apache/solr/request/SolrRequestInfo.java`
- `solr/core/src/test/org/apache/solr/handler/component/SearchRequestInfoTest.java`
- `solr/core/src/test/org/apache/solr/handler/component/SearchRequestListenerTest.java`

## Regression coverage prepared

The new lightweight `SearchRequestInfoTest` uses deterministic nonzero CPU snapshots. It asserts that a distributed failure reports 7 ms rather than 14 ms, that the latest shard aggregate is used (11 + 7 = 18 ms), and that local-only and unavailable values are preserved.

The cloud listener suite adds local and distributed document subquery cases. Both validate the returned child documents before asserting that only the original coordinator emits an event. A third new method preserves top-level embedded behavior. The existing real CPU test also exercises a malformed two-shard query and checks its local-only fallback. The full focused set is seventeen methods.

The negative gate overlays only the two test files onto the original PR code. Its three required discriminators are the nonzero CPU fallback and the local/distributed subquery event-count tests. No fail-before result is claimed until that leg executes.

## Verification and limitations

- `git diff --check` and committed `git show --check` passed.
- The worktree was clean after the commit.
- A plain push created `nick-boss-tech/solr:solr-11994-submit`; a separate GitHub ref read returned the exact full fix SHA above.
- `review-branch.ps1 SOLR-11994 -StatOnly` fetched and confirmed that head. Its three commits are the contributor's two PR commits plus Nick's one follow-up.
- No Gradle, formatter, compiler, test, or retired Windows test queue ran on this host. The changes remain **Implemented, Proof Pending**.
- No new endpoint or authentication/authorization boundary was introduced. The changes affect which trusted in-process listener callbacks fire and which CPU value they receive.

The gate requests clean tidy, full focused tests, neighboring request-stack and distributed-transformer tests, root `check -x test`, and the matched negative leg on a capable Linux host. If formatting or test repair changes the branch head, the gate host must publish a superseding job before treating results as proof.

## Public state

No PR was created and no comment or review was posted. The original contributor's branch and PR head were not changed. The derived branch is ready for inspection at `https://github.com/nick-boss-tech/solr/compare/2c9243b111b38367677d7e0c9b30b8e72c554d2c...b3a11484f13feaab55fe4f27504bfce3d1bdd2ef`.

This report and the gate are the deliverables of the immutable Windows implementation claim. Linux proof is a separate claim.
