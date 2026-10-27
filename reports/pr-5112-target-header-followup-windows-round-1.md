# PR 5112 target header follow-up: Windows implementation, round 1

Status: Implemented, Proof Pending

Nick requested an adoptable review fix on October 27 after David asked for clarification of the header-loss finding. The current PR head and discussion were captured at 2026-10-27T16:22:51Z and refreshed before committing; the head remained `b4fa46f1ecb11a60cc2cfd4783492639bb4e3a85`, with four conversation comments and sixteen inline entries. No competing board assignment, implementation claim, gate or receipt matched this slice at board snapshot `f1ee7180bcd24d0dfd0054f7f7201d7627c3ad0e`.

## Delivered branch

- Fork: `nick-boss-tech/solr:solr-18537-submit`.
- Pushed head: `3d762be30f6b8c2805c3edcfc48446488265a806`, independently verified through the fork refs API.
- Parent: original PR head `b4fa46f1ecb11a60cc2cfd4783492639bb4e3a85`; one additive commit, no history rewrite.
- Author: Nick Shanin, `nick.boss.us@gmail.com`, no attribution trailers.
- Claim: `claims/pr-5112-followup-windows-round-1.md`, posted at board commit `6820711fbeb2dfbb25990d405b393873f3c39fcd`.
- Gate: `gates/pr-5112-target-header-followup-round-1.md`.
- Receipt target: `receipts/PR-5112-target-header-followup.md`.
- Pinned adoption diff: https://github.com/nick-boss-tech/solr/compare/b4fa46f1ecb11a60cc2cfd4783492639bb4e3a85...3d762be30f6b8c2805c3edcfc48446488265a806

## Behavior and scope

The source at original head lines 79-87 parses any GET query containing the target-name text, then assigns the parsed target unconditionally. For `foo=X-Solr-targetNode`, lookup of the actual targeting parameter is null, clearing the already valid header. It then executes the node-level endpoint on the receiving node. The fix nests assignment and query removal under the actual parameter's presence, preserving true header/query rejection.

Three outbound files only, 42 additions and 9 deletions:

- `solr/core/src/java/org/apache/solr/servlet/NodeTargetProxyFilter.java`: guarded assignment at lines 81-90; Javadoc's full-node colon rule corrected.
- `solr/core/src/test/org/apache/solr/servlet/NodeTargetProxyFilterTest.java`: four tests at lines 111-141. Two assert the other live node responds when unrelated query text contains the header name; one pins local behavior without a directive; one pins rejection of actual header plus parameter, even with matching values.
- `solr/solr-ref-guide/modules/deployment-guide/pages/solrcloud-distributed-requests.adoc`: lines 308-309 now describe the implemented colon rule and agreed 400 error.

The prior review's request to retain header/query rejection is now backed by an explicit test; the original routing suite had no such test. Existing query-only coverage remains. The unchanged original changelog credits David Smiley; this follow-up repairs his unmerged feature rather than creating a second release entry or rewriting his attribution.

Decision ladder rung 1 settles F1 from the explicit assignment/data-flow defect and F2 from code/tests plus the already agreed public 400 contract. A runtime reproduction has not run on Windows. The requested Linux negative leg must distinguish the original code from the fix before the finding is described as runtime-proven.

## Validation and limits

Inspected the complete follow-up diff, expected file list and branch ancestry. `git diff --check` before committing and `git show --check` afterward passed. Fourteen routing test methods and four Basic Auth methods are present. No Gradle, compilation, formatter, HTTP request or tests ran on Windows.

Linux proof requires those 18 methods, neighboring `HttpSolrProxyTest` and `SolrRequestParserTest`, tidy and `check -x test`. The two new routing regressions must fail at node identity assertions on the original production code with only the new test file overlaid. The gate pins all commands, seed `5112C0DE`, evidence and failure criteria.

The previously acknowledged certificate/caller-identity concern remains unresolved and separate from this routing patch. No authentication or transport design changed. Basic Auth success cannot settle certificate identity. This branch is not a claim that the entire PR is ready.

## Handoff and later public response

Nick will bring back the Linux result before the comment is drafted. Follow `comment-formula.md` and the PR 5232 example: answer David with a concrete request example, the verified before/after node identity, pinned original-to-tested-head diff, actual proof results and limits, and adoption commands. Refresh PR head/discussion and verify ancestry before offering `git merge --ff-only <tested-head>`; a moved PR head requires reconciliation.

At today's unchanged PR head the future adoption shape is `git switch SOLR-18537-proxyRequestsByHeader`, `git fetch https://github.com/nick-boss-tech/solr.git solr-18537-submit`, then merge the exact tested SHA with `--ff-only`. The final SHA must come from the completed receipt if formatting repairs move the branch.

No public comment drafted or posted, no live PR body edited, no Jira action, and no contributor branch or upstream branch changed. The fork branch is the deliverable for adoption.

Local tooling note: `research/branch-work.ps1` still loaded the superseded PAT path. Its loader now reads the current workspace PAT path, supports raw or `GH_TOKEN=` format, and refreshes inherited credentials in memory. No token was echoed or copied; this local helper change is outside the Solr branch.
