# SOLR-12613: reconciliation of Nick's three P2 findings

Status: TARGETED SOURCE REVIEW COMPLETE. All three quoted findings are addressed in the reviewed source. This is not a full PR approval or new runtime proof.

Owner asked for a brief review against his exact earlier comments and an assignment addition for the deprecation follow-up. Claim: `claims/solr-12613-three-findings-assignment-windows.md`. Discovery board snapshot: `50ef548e1fb1a7ce5488bd98f71cb6bcf8523511`. Windows Codex desktop, 2026-11-05.

## Reviewed revision

| Item | Snapshot |
| --- | --- |
| PR | https://github.com/apache/solr/pull/5110, OPEN |
| Fetched and live head | `6b5d830132b941faa85856347099ade8b190be0e` |
| Earlier head before backend revert | `73b30956ff2c0f7b16f6af3bceacf95bda8271b9` |
| Common-main comparison base | `ad26fdf2c6bee8168eae043b94833f05643d1995` |
| API/core comparison | Empty delta at the reviewed head (`git diff --exit-code` returned 0) |
| New discussion | David replied to Eric at 2026-11-05T17:18:02Z: https://github.com/apache/solr/pull/5110#discussion_r4483637626 |
| Local evidence | `research/branch-reviews/2026-11-05-pr-5110-three-findings/evidence/PR-5110/`; collector verified stable source head and metadata |

## Exact findings

| Earlier finding | Current status | Evidence and limit |
| --- | --- | --- |
| Silent remote failures: added helper drops failedNodes | Addressed by removing the added helper and its endpoint callers | Added `proxyToNodes` helper is absent from V2SolrRequestBasedProxy. All `solr/api/` and `solr/core/` files match the common-main base. No claim that every other remote-failure path has been tested |
| Wrong error status: helper converts intended 400 to 500 | Addressed by removing that catch-all helper path | Same removal, not a new error-status patch in PR 5110. The generic transport is existing independent functionality |
| Permission-dependent navigation: failed live-node lookup skips core refresh | Addressed in the current callback | Failure calls `setLiveNodes([])`, then continues to `loadCoresForSelectedNode(pageType)`; that state setter falls back to the known local node. No fresh permission-specific browser run was executed here |

The backend removal is the latest commit's change. The live-node fallback already exists at the current head; this review does not attribute every finding's resolution to that single commit.

For the third finding, inspect [app.js lines 618-637](https://github.com/apache/solr/blob/6b5d830132b941faa85856347099ade8b190be0e/solr/webapp/web/js/angular/app.js#L618-L637). The error branch handles the error and empties the node list, with no early return before line 635. [services.js lines 181-197](https://github.com/apache/solr/blob/6b5d830132b941faa85856347099ade8b190be0e/solr/webapp/web/js/angular/services.js#L181-L197) shows that an empty list selects the previously established local identity, publishes it and marks readiness. Thus the remote-core-page redirect guard does not suppress the fallback refresh. This addresses the quoted denied-node-discovery scenario after successful mode/local identity discovery; it does not claim that a denied core-read operation should succeed.

## Separate remaining concerns

Do not carry the three quoted findings forward as unresolved blockers. Do not use their closure as evidence that the rest of the PR is ready.

- Core-menu responses still lack request-generation/captured-node checks at [app.js lines 537-567](https://github.com/apache/solr/blob/6b5d830132b941faa85856347099ade8b190be0e/solr/webapp/web/js/angular/app.js#L537-L567).
- Initial core loading precedes mode/local-identity readiness at [app.js lines 570-605](https://github.com/apache/solr/blob/6b5d830132b941faa85856347099ade8b190be0e/solr/webapp/web/js/angular/app.js#L570-L605).
- A mode-metadata failure after cloud mode was known clears selection and returns without a new local core refresh. This is distinct from a live-node-list failure and remains in the prepared lifecycle slice.

These source concerns are unchanged from the preceding targeted review; no fresh browser reproduction ran. The lifecycle branch needs adaptation to the real contributor head and exact-head proof before a tested offer. The existing original review's statement that wrong-node mutations/session restoration had fixes is not re-certified by this narrow review; the first core-request boundary is separately identified above.

## Architecture, maintenance and verification

Architecture: removal restores frontend-only integration through the generic transport, with native endpoint responses and no new v2 node parameters. Properties/threads are existing consumers of the same header hook. Existing APIs and clients outside this PR are not being removed by this commit.

Maintenance/reuse: deleting the duplicate endpoint helper removes its two reported defects at their source. No new helper, fixture or dependency is introduced by the update. Follow-up coverage continues to reuse the existing picker and transport fixtures.

Verification: Windows performed pinned source/diff inspection and captured the live PR evidence. No Gradle, compilation, formatting, Selenium or retired test queue ran. Earlier CI success and separate transport receipts are not proof of the prepared UI follow-up heads. The earlier baseline neighbor failure remains a separate unresolved verification item.

## Assignment consequence

New addendum: `assignments/solr-followup-deprecation-pr-package-round-3.md`. It extends the six prepared packages with one planned backend deprecation candidate after consumer migration. No deprecation implementation or new submission draft is falsely counted as already complete.

Important consumer gap: [logging.js lines 145-159](https://github.com/apache/solr/blob/6b5d830132b941faa85856347099ade8b190be0e/solr/webapp/web/js/angular/controllers/logging.js#L145-L159) still sends `nodes=all` for cloud log-level writes. The prepared SOLR-18619 read migration preserves those writes. Consumer migration must account for them before backend retirement, with explicit per-node outcomes. Do not equate All nodes log-message viewing with migrating log-level writes.

David's new reply confirms the old functionality is deprecated material that should not be newly enabled in v2. His earlier direction under SOLR-18537 is to migrate UI callers first, deprecate for removal in Solr 11, and let clients send parallel requests per node. Addendum requires deduplication, a consumer audit, full PR/Jira draft text and release/compatibility planning before implementation or publication.

Disposition: the three original P2 findings can be acknowledged as addressed in source, with the different remaining lifecycle concerns named separately. No public PR comment, review, Jira write, source patch or contributor-branch change was made.
