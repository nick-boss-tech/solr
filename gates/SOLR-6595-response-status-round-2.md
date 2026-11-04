# Gate: SOLR-6595 collection sub-request status, round 2

Status: OPEN
Tags: gate, review, records.
Intended host: any capable Linux gate host with JDK 21, Gradle 9.7.0, Git, and the branch's pinned build dependencies. Run one Gradle build at a time under the host lock.
Supersedes: `gates/SOLR-6595-response-status-round-1.md` because the SolrJ integration assertion was corrected.

## Pinned inputs and scope

- Fork: `nick-boss-tech/solr`, branch `solr-6595-submit`.
- Fixed head F: `2b5a1d174f0471edb8280207ba877a4d7eb6e619`.
- Parent: upstream/main `ad26fdf2c6bee8168eae043b94833f05643d1995`.
- Implemented on Windows; no Gradle or tests were run there.
- The shared Collection Admin response path now recognizes a non-empty distributed `failure` value, sets response status 500, and retains `success` and `failure` payloads. The V1 writer carries the status override into the response header and HTTP status. The Jersey response filter maps non-zero `responseHeader.status` to the HTTP status for V2.
- SolrJ's current V2 response parser may return the parsed response object for a non-200 response that has no top-level `error` object. Therefore the distributed DeleteNode integration assertion checks `responseHeader.status` and the retained failure map; `PostRequestDecorationFilterTest` separately verifies that V2 HTTP status is set to 500.
- Changed files: `solr/core/src/java/org/apache/solr/core/SolrCore.java`, `solr/core/src/java/org/apache/solr/handler/admin/api/AdminAPIBase.java`, `solr/core/src/java/org/apache/solr/jersey/PostRequestDecorationFilter.java`, `solr/core/src/java/org/apache/solr/response/SolrQueryResponse.java`, `solr/core/src/java/org/apache/solr/servlet/HttpSolrCall.java`, `solr/core/src/test/org/apache/solr/core/SolrCoreResponseStatusTest.java`, `solr/core/src/test/org/apache/solr/handler/admin/api/DeleteNodeAPITest.java`, `solr/core/src/test/org/apache/solr/handler/admin/api/ReloadCollectionAPITest.java`, `solr/core/src/test/org/apache/solr/jersey/PostRequestDecorationFilterTest.java`, and `changelog/unreleased/SOLR-6595.yml`.
- No PR is open and no JIRA or upstream write is part of this gate.

## Required candidate proof

1. Refresh the live fork ref and confirm it still equals F. Check that this job has no live claim or receipt. Use the host lock, disk/inode guards, a clean detached worktree, the supported gate runner, fresh logs/XML, and JDK 21. Record host, UTC time, JDK path/version, runner, worktree, command lines, exit codes, test counts, and artifact paths in the claim and receipt.
2. Run the detached tidy preflight at F. Then run `./gradlew --no-daemon --no-build-cache tidy` and require a clean worktree. A formatting change invalidates F; commit and push a repair, then post a superseding gate before running more proof.
3. Compile changed production and test sources with Error Prone:

   `./gradlew --no-daemon --no-build-cache :solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`

4. Run all focused tests with seed `6595C0FFEE0001` and a fresh absolute work directory:

   `./gradlew --no-daemon --no-build-cache :solr:core:cleanTest :solr:core:test --tests org.apache.solr.core.SolrCoreResponseStatusTest --tests org.apache.solr.handler.admin.api.DeleteNodeAPITest --tests org.apache.solr.handler.admin.api.ReloadCollectionAPITest --tests org.apache.solr.jersey.PostRequestDecorationFilterTest -Ptests.seed=6595C0FFEE0001 -Ptests.workDir=/tmp/solr6595-response-status-tests`

   All four classes must execute and pass. In particular, `DeleteNodeAPITest.testDeleteNodeFailureSetsResponseStatus` must verify the real distributed V2 response header and retained failure map. Preserve fresh XML and report skipped tests separately.
5. Run the root check at pristine F:

   `./gradlew --no-daemon --no-build-cache check -x test`

   Record the full result and any environment condition. Do not count an up-to-date or cached focused test task as a fresh run.
6. Run three isolated synthetic mutations with tests unchanged. Each mutation must compile and fail on the named assertion, not on setup or compilation:
   - In a disposable F worktree, change only `AdminAPIBase.hasFailedSubResponses` to return `false`. Run `./gradlew --no-daemon --no-build-cache :solr:core:cleanTest :solr:core:test --tests org.apache.solr.handler.admin.api.ReloadCollectionAPITest.testFailedSubRequestReturnsServerErrorStatus -Ptests.seed=6595C0FFEE0001 -Ptests.workDir=/tmp/solr6595-status-detection-mutation`. The response-header and status-override assertions must fail.
   - In a separate disposable F worktree, remove only the `SolrCore.postDecorateResponse` branch that copies `getHttpStatusCodeOverride()` into the response header. Run `./gradlew --no-daemon --no-build-cache :solr:core:cleanTest :solr:core:test --tests org.apache.solr.core.SolrCoreResponseStatusTest.testHttpStatusOverrideIsAddedToResponseHeader -Ptests.seed=6595C0FFEE0001 -Ptests.workDir=/tmp/solr6595-status-header-mutation`. The expected status 500 assertion must fail.
   - In a separate disposable F worktree, change only the Jersey filter's `responseContext.setStatus(response.responseHeader.status)` call to set status 200. Run `./gradlew --no-daemon --no-build-cache :solr:core:cleanTest :solr:core:test --tests org.apache.solr.jersey.PostRequestDecorationFilterTest.testNonZeroResponseHeaderStatusIsUsedForHttpStatus -Ptests.seed=6595C0FFEE0001 -Ptests.workDir=/tmp/solr6595-jersey-status-mutation`. The expected HTTP status 500 assertion must fail.
   - In a separate disposable F worktree, change only `AdminAPIBase.hasFailedSubResponses` to return `false`. Run `./gradlew --no-daemon --no-build-cache :solr:core:cleanTest :solr:core:test --tests org.apache.solr.handler.admin.api.DeleteNodeAPITest.testDeleteNodeFailureSetsResponseStatus -Ptests.seed=6595C0FFEE0001 -Ptests.workDir=/tmp/solr6595-delete-node-mutation`. The expected response-header status 500 assertion must fail (the response status remains 0).

   Record each mutation diff, production/test blob hashes, exact result and fresh XML. Do not commit or push mutation worktrees.

## Deliverables and disposition

- Add a claim before execution and a report with the exact host evidence.
- Add `receipts/SOLR-6595-response-status-round-2.md` with the exact tested head, commands and exit codes, counts/skips, mutation results, tidy/compile/root-check results, log/XML paths, and limitations.
- The patch remains **Implemented, Proof Pending** until a green receipt covers F. No public comment, PR, JIRA update, or merge is authorized by this gate.
