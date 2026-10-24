# SOLR-18295 routing implementation, round 1 outcome

Status: IMPLEMENTED, PROOF PENDING

- Claim: `claims/solr-18295-routing-implementation-round-1.md` (Windows implementation, explicit owner request in chat).
- Fork branch: [solr-18295-submit](https://github.com/nick-boss-tech/solr/tree/solr-18295-submit).
- Pushed head: `b273b245115beff0cc131448c277dc9bde909a0c`, independently confirmed with the fork refs API. Base: upstream/main `13c693d805f6541f7cec684cd27d28eef7b3b614`.
- Author and committer: Nick Shanin, `nick.boss.us@gmail.com`. No attribution trailers.
- Outbound file: `solr/core/src/test/org/apache/solr/cloud/ReplicationFactorTest.java` only (8 additions, 2 deletions). No changelog: test-only exemption.
- Linux handoff: `gates/SOLR-18295-routing-round-1.md`, posted and bytes/reachability verified at `075db4e32645b08b041af55165c1ed360db668c9`, then live tip. No earlier receipt covers this new branch.

## What changed

The test overrides the existing `createCloudClient(String)` hook (head lines 62-67), passes `true` for shardLeadersOnly to `createNewCloudSolrClient`, and removes its SOLR-18295 AwaitsFix import and annotation. The inherited factory otherwise randomizes that routing flag (`AbstractFullDistribZkTestBase.java`, base lines 368-370).

All adds and deletes through `cloudClient`, including inherited batch helpers, now prefer shard leaders. Only follower proxies are deliberately closed. No RF assertion, partition, retry helper, recovery wait, or explicit non-direct request changed. The 2x2 scenario still sends requests directly to a healthy shard1 leader and follower after shard2's follower is cut. Base-managed client lifecycle and timeouts are preserved.

This is leaders-first, not a claim that followers disappear from every failover list. The helper still randomizes directUpdatesToLeadersOnly and parallelUpdates independently (`SolrTestCaseJ4.java`, base lines 2517-2520). CloudSolrClient.sendRequest (base lines 902-925 and 999-1024) routes eligible updates directly and puts leaders first for the other updates. The test keeps its leaders available.

## Evidence and overlap

JIRA's two comments only reference mute commits (`c851aae5c8221e0a78c5b67efd4392bd0c8e5cd4` and branch_10x cherry-pick `58e15ff9a9272dbd1a662c6c0511688e20b2d77d`); both diffs were read. This is not an already-landed fix.

Jan Høydahl has open apache/solr [#4563](https://github.com/apache/solr/pull/4563), head `54f375b0bed805a08e1b456bd782e15dfce74723`, which adds single-document retries. Nick explicitly requested implementation of this fork branch. Coordinate with #4563 before any public submission; this work makes no public claim to replace it.

Related apache/solr [#5098](https://github.com/apache/solr/pull/5098), head `14868bc7a7f5ec5c4fa1ade5f894cbda9533307d`, was approved by Sanjay on October 24 with a reported Linux comparison of 16/100 before and 0/100 after, recovery still exercised. [His report](https://iamsanjay.github.io/solr-notes/RecoveryAfterSoftCommitTest.html) explains the cut-connection/request-commit race and names ReplicationFactorTest as the same pattern. Those runs were for RecoveryAfterSoftCommitTest, not SOLR-18295; do not reuse their numbers as proof here.

Decision ladder: rung 1 (current routing code, helper, test body, and mute history) and rung 3 (owner's direct implementation request). Test-local routing follows the test's achieved-RF purpose; no product replay policy changes are needed. No new security boundary, public API, dependency, or persisted behavior changes.

## Verification and remaining work

The ticket worktree is clean. `git diff --check <base> HEAD` passes. Fork-versus-upstream review confirms one scoped commit. Source review confirms the existing test body and RF assertions are unchanged. No Gradle, tests, tidy, Error Prone, or Windows test queue ran.

The OPEN Linux gate requests tidy/Spotless, Error Prone compilation, a whole-class smoke, 20 fresh-suite synthetic baseline runs forced to any-replica routing, 20 matched-seed exact-head runs, and core `check -x test`. It requires failure classification by cut endpoint and exception, raw logs/XML, honest baseline non-reproduction wording, and a receipt at the final SHA. Formatting/code repairs need a superseding exact-head job.

Implementation deliverables are complete. Proof and public submission remain pending. No public PR, body edit, GitHub comment, or JIRA write was made.
