# Critical and Blocker backlog, slice 04 (complete)

Assignment: `assignments/pool-csv-critical-blocker-round-1.md`. Claim: `claims/pool-csv-critblock-04.md`. Auditor: vm2. Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. JIRA read 2026-10-20 (UTC) for all 25 tickets (one JQL call). Code paths are at the pinned head.

All 25 tickets are classified. The four code fixes marked STILL-PRESENT have a chosen route and tradeoffs below, but no fix branch, gate, or receipt exists yet; each needs its own follow-up claim. No PRs are opened. No JIRA or GitHub posts.

## Summary

| Classification | Count | Tickets |
| --- | --- | --- |
| ALREADY-FIXED | 1 | SOLR-17840 |
| NOT-REPRODUCIBLE | 1 | SOLR-17844 |
| STILL-PRESENT (route chosen, fix not started) | 8 | SOLR-18552, 18446, 18398, 18297, 18458, 17252, 17433, 17375 |
| NEEDS-OWNER | 6 | SOLR-18486, 18240, 17988, 17842, 18501, 17767 |
| KEEP-OPEN (evidence inconclusive; missing piece named) | 9 | SOLR-17291, 17546, 17762, 18010, 17788, 17463, 17370, 17297, 17281 |

Totals: 1 + 1 + 8 + 6 + 9 = 25.

## Classified, with evidence and route

### ALREADY-FIXED

- **SOLR-17840** (Blocker, LTR after Lucene 10). Fix commit `efeee564663` ("SOLR-17840: Fixing LTR after Lucene 10 upgrade") is an ancestor of the pinned head. Its workaround is in `solr/modules/ltr/.../scoring/MultiFeaturesScorer.java:129-173`. The ticket's second item (timeouts not working) was not checked, so the record should not say the whole ticket is closed until that is read.

### NOT-REPRODUCIBLE

- **SOLR-17844** (Blocker, TestFieldCacheSortRandom failures). Test present at `solr/core/src/test/.../uninverting/TestFieldCacheSortRandom.java`, no ignore marker. Three runs of `:solr:core:test --tests TestFieldCacheSortRandom` at the pinned head with seeds 16D2BCA4BE25E927, 5A1B2C3D4E5F6071, and 9F8E7D6C5B4A3921: all exited 0, first run 2 of 2 tests passed. The ticket says the earlier fix "is likely incorrect" and does not name the commit. The attempts do not cover the STRING_VAL and STRING sort orders under other seeds. Route: no fix. Ask the owner whether to close, or keep open for a seed sweep.

### STILL-PRESENT (route chosen, fix not started)

- **SOLR-18552** (Blocker, sharedArenaMaxPermits=64). Absent from `solr/bin/solr` and `solr/packaging`. Route: add `-Dorg.apache.lucene.store.MMapDirectory.sharedArenaMaxPermits=64` to the startup options for 9.x only. Tradeoff: only changes the launcher; embedded or custom-start users are not covered. A 10.x default is not part of this route.
- **SOLR-18446** (Blocker, remove "collection" routing). `solr/core/.../servlet/HttpSolrCall.java:757` still sets the "collection" parameter from alias-resolved collections. Route: keep the parameter only as a core-name disambiguation hint, with a deprecation warning, and do not remove it in this round. Tradeoff: removal is the ticket's first ask but breaks clients that send it; the ticket itself allows the hint. This is a public behavior change, so mark it as a maintainer question in the PR.
- **SOLR-18398** (Blocker, NamedList to LinkedHashMap for responseHeader). `solr/core/.../response/SolrQueryResponse.java:94` still builds `values` as `SimpleOrderedMap`. Route: change only the top-level container to a LinkedHashMap, keep the NamedList for nested values, and add a getter for existing callers. Tradeoff: writers that call NamedList methods on the top level must be found and changed; the scope is wider than the ticket's one-line picture.
- **SOLR-18297** (Blocker, Jetty 12.1.14). `gradle/libs.versions.toml:84` pins `eclipse-jetty = "12.1.12"`. Route: bump the version in the version catalog and fix the breaking changes listed in the Jetty 12.0 to 12.1 migration guide. Tradeoff: the bump alone may not compile or start; the gate must run the full server test set. The CSV marks a PR available; that was not checked.
- **SOLR-18458** (Blocker, drop debugQuery=true). Still honored: `solr/core/.../handler/component/DebugComponent.java:166,176` and `handler/MoreLikeThisHandler.java:232`. Route: deprecate `debugQuery` now (warn when set), move the internal DebugComponent uses to `debug`, and remove in a later release. Tradeoff: removing it now breaks users who send it; the ticket's "drop" is a release-notes decision. Linked SOLR-17931 was not read.
- **SOLR-17252** (Critical, release wizard hangs in nano). `dev-tools/scripts/releaseWizard.py:196` still treats nano as a supported editor. Route: skip the editor for CHANGES.txt when the editor is a known full-screen editor that cannot be driven from a script, and print the file path instead. Tradeoff: the release manager opens the file by hand; a more precise fix would need a pty test that the pool cannot run here.
- **SOLR-17433** (Critical, SolrStream 60-second client). `solr/solrj-streaming/.../io/SolrClientCache.java:39-45` floors both connection and socket timeouts at 60000 ms, and the system properties can only raise the floor (`max(property, 60000)`). Route: document the existing `PROP_SO_TIMEOUT` and `PROP_CONNECTION_TIMEOUT` overrides for streaming in the reference guide, and do not change the default in this round. Tradeoff: raising the default changes every client in the process; documentation leaves the default for operators to change. Maintainer question: is 60 seconds the intended floor?
- **SOLR-17375** (Critical, close IndexReader async on commit). No asynchronous close found at the pinned head (grep for closeAsync, asyncClose, closeReader in `solr/core` found only the synchronous `closeReader` flag in `SolrIndexSearcher.java:158,389,426`). Route: close the previous reader on a single bounded background executor after the searcher swap. Tradeoff: old index files and file handles stay open a little longer, and deletion is delayed; a performance measurement is required before merge.

### NEEDS-OWNER

- **SOLR-18486** (Blocker, reference guide before a 10.1.0 announcement). Release-process gate. The owner or release manager decides.
- **SOLR-18240** (Blocker, 9.11.0 major changes and upgrade notes). Release documentation. Owner decides.
- **SOLR-17988** (Blocker, Solr 10 property names conveyed to users). Release documentation. Owner decides.
- **SOLR-17842** (Blocker, publish benchmarks). Scope and resources are the owner's call.
- **SOLR-18501** (Blocker, solr-mcp IP clearance). Legal and CLA. Owner only.
- **SOLR-17767** (Critical, SIP-24 umbrella for replacing the Java Security Manager). A design proposal umbrella, not a defect. Owner decides which children to open.

### KEEP-OPEN (inconclusive; the missing piece is named)

- **SOLR-17291** (Critical, "indexDeletionPolicy must not be null" when a replica is created). `SolrCore.java:593-615` (`initDeletionPolicy`) always returns a wrapper, falling back to `new SolrDeletionPolicy()`, so the null path is not reachable from SolrCore at the pinned head. The fixing commit is not identified (the `git log -S` hits only the file-move commit db22b4d1f71). Missing: the commit that made the null path unreachable, and a reproduction at the reported version.
- **SOLR-17546** (Blocker, reference guide removed pages). `playbook.template.yml:28` sets `redirect_facility` from a build variable, and `.htaccess` is produced only when it is `httpd`. Missing: the facility the published site uses, and the list of removed pages without a redirect entry.
- **SOLR-17762** (Critical, Solr 9.8.1 install on two VMs). A support question about a VM setup with no logs or config given. Missing: the server logs and the ZooKeeper config. Not closeable as answered without a docs link.
- **SOLR-18010** (Critical, adding a role corrupts security.json). The ticket gives exact steps for Solr 9.8.0. Missing: a reproduction run on the pinned head, which was not attempted.
- **SOLR-17788** (Critical, cores hang during full index bootstrap in leader/replica mode). Missing: a thread dump or a reproduction.
- **SOLR-17463** (Blocker, Solr hangs in SolrCore.getSearcher). Thread dumps are in the ticket; the path was not traced against the pinned head. Missing: the trace from the dump to a line at the pinned head.
- **SOLR-17370** (Critical, IllegalAccessException in facets with numeric strings). Missing: a reproduction with the schema the ticket gives.
- **SOLR-17297** (Critical, classloading with plugin jar and modules). Missing: a reproduction with the plugin jar and `modules` in solr.xml.
- **SOLR-17281** (Critical, leader elected a down replica after shard move). Missing: the logs for the move and a reproduction.

## Limits

No JIRA or GitHub posts or comments. No PRs. No force push. No branches, gates, or receipts were made. The probe runs used the pinned head in a detached worktree and were removed.

## Fix branches (route taken, gate, state)

Routes are the ones recorded above. Each branch is on the fork. None is opened as a PR; pool hosts do not open PRs, and opening is the owner's call.

| Ticket | Branch (fork) | Head | Route taken | Gate run | Open |
| --- | --- | --- | --- | --- | --- |
| SOLR-17252 | solr-17252-release-wizard-editor | f2d0e2e3ac9 | Fail fast when EDITOR is a terminal editor. Changed from the report's first route ("print the path"): the wizard already warns the editor will not work, so stopping early matches that warning. Tradeoff: the release manager must restart the wizard with a windowed editor. | `get_editor()` checked with the wizard's requirements: nano exits with a message, a windowed editor is accepted. | Needs a release-manager look. |
| SOLR-18552 | solr-18552-sharedarena-9x | (branch on 9.x line, upstream branch_9 f4f2507) | Default `-Dorg.apache.lucene.store.MMapDirectory.sharedArenaMaxPermits=64` in the launcher, 9.x only. Tradeoff: embedded and custom-start users keep the Lucene default. | Launcher gate: a node started with the branch's `bin/solr` has the property on its JVM; the control launcher has none. Run on an 11.0 distribution copy because the 9.x distribution build cannot run on this host. | The 9.x distribution build and the bats launcher tests need a pool gate host. |
| SOLR-17433 | solr-17433-stream-timeout-doc | 42b90588cc2 | Document the `socketTimeout` and `connTimeout` system properties and the 60-second floor, in the streaming expressions guide. Default left unchanged. Tradeoff: raising the default would change every client in the process; documentation leaves that to operators. | Documentation only. The reference guide build was not run. | Maintainer question: is 60 seconds the intended floor? |
| SOLR-18458 | solr-18458-debugquery-deprecation | (head of branch) | Deprecation warning through `DeprecationLog` in `SearchHandler`, once per process. Parameter still honored. Removal is a later release. Tradeoff: the warning covers the main entry point only; `DebugComponent`, `MoreLikeThisHandler`, `ReRankQParserPlugin`, and the SolrJ `setShowDebugInfo` still use the parameter. | `:solr:core:test --tests org.apache.solr.BasicFunctionalityTest`: 23 of 23 pass. | Full core suite not run; the warning text is not tested. |
| SOLR-18297 | solr-18297-jetty-12-1-14 | 5cdc6574702 | Version bump only (12.1.12 to 12.1.14), lock files and license entries updated. No code changes: the compile gate passed without migration edits. Tradeoff: a compile is not a behavior check. | `:solr:core:compileTestJava :solr:server:compileJava` pass; `StatusToolTest` 1 of 1 (Jetty-backed). | Full core and server test suites need a pool gate host. Migration guide items not reviewed. |

## Deferred design items (routes recorded, no branch)

- **SOLR-18446** (remove "collection" routing in HttpSolrCall). The route is to keep the parameter as a core-name disambiguation hint and deprecate it, not remove it this round. Tradeoff: removal is the ticket's first ask but breaks clients that send it. Public behavior change: maintainer question. Needs its own claim and design PR text.
- **SOLR-18398** (responseHeader from NamedList to LinkedHashMap). The route is to change only the top-level container in `SolrQueryResponse` and keep a getter for existing callers. Tradeoff: callers that treat the top level as a NamedList must be found; the scope is wider than the ticket suggests. Needs its own claim.
- **SOLR-17375** (close IndexReader asynchronously on commit). The route is one bounded background executor after the searcher swap. Tradeoff: old files and handles stay open longer. Needs a performance measurement before any merge.

## Claim status

The claim `claims/pool-csv-critblock-04.md` is DONE for classification. The fix branches above are follow-ups; their gates beyond the ones listed are open.
