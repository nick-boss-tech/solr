# Receipt: PR 5112 target header routing follow-up

## Gate pr-5112-target-header-followup-round-1-superseding-46bc7e4 (vm5, 2026-10-27): GREEN, bounded routing proof; certificate-identity question not covered

- Branch: `nick-boss-tech/solr:solr-18537-submit`. Head: `46bc7e428b5be519a497ecf65dcd0256b65fa84c` (fetched and confirmed by ls-remote before the run). Base: `b4fa46f1ecb11a60cc2cfd4783492639bb4e3a85` (original PR 5112 head, apache/solr PR 5112, `dsmiley/solr:SOLR-18537-proxyRequestsByHeader`).
- Links: Ticket or parent: SOLR-18537 / PR 5112. Category: review. Siblings: `claims/pr-5112-followup-windows-round-1.md` (implementation, Windows). Supersedes: the round-1 gate at `3d762be30f6b`, whose tidy changed one Javadoc paragraph; its focused run was stopped before any result and is not carried forward.
- Repair commit: `46bc7e428b5be519a497ecf65dcd0256b65fa84c` ("SOLR-18537: wrap NodeTargetProxyFilter Javadoc for tidy"), one commit above `3d762be30f6b`, Javadoc wrap only in `solr/core/src/java/org/apache/solr/servlet/NodeTargetProxyFilter.java`, author Nick Shanin, no trailers, no PR number. Pushed as a fast-forward; no force.
- Runner: vm5 (Linux aarch64, 4 CPUs, 8 GB), detached full checkouts at `/workspace/gates/pr-5112/head` (`46bc7e4`) and `/workspace/gates/pr-5112/base` (`b4fa46f`), under the host lock `/workspace/tools/.gradle-vm5.lock`. Toolchain: JDK 21.0.12.1+1 at `/workspace/jdk/jdk-21.0.12.1+1`, `GRADLE_USER_HOME=/workspace/gradle-home`, Gradle wrapper, Node v22.18.0 from the Gradle node plugin.
- Log and XML root: `/workspace/gates/logs/vm5/pr5112-linux/`.

### Steps

- Step 0, tidy at `46bc7e4`: rc=0, `pr5112-head-46bc7e4/tidy.log`, tree clean afterwards (`git status --porcelain` empty).
- Step 1, focused suites (`:solr:core:cleanTest :solr:core:test --tests ...NodeTargetProxyFilterTest --tests ...NodeTargetProxyFilterAuthTest -Ptests.seed=5112C0DE`): rc=0. `NodeTargetProxyFilterTest` 14 tests, 0 failures, 0 errors, 0 skipped. `NodeTargetProxyFilterAuthTest` 4 tests, 0 failures, 0 errors, 0 skipped. Fresh JUnit XML: `pr5112-head-46bc7e4/focused.xml/`.
- Step 2, neighbor suites (`HttpSolrProxyTest`, `SolrRequestParserTest`, seed `5112C0DE`): rc=0. `HttpSolrProxyTest` 2 tests, `SolrRequestParserTest` 9 tests, 0 failures, 0 errors, 0 skipped. XML: `pr5112-head-46bc7e4/neighbors.xml/`.
- Step 3, `./gradlew check -x test` at `46bc7e4`: rc=0 in the final run (`pr5112-head-46bc7e4-check-env2/check.log`). Two earlier runs failed on an environment cause, recorded below.
- Step 4, fail-before proof on the base `b4fa46f1ec` with only `NodeTargetProxyFilterTest.java` overlaid from `46bc7e4` (overlay sha256 prefix `6af5dbcb611bd15a`, identical to the head's copy; all production files at the PR revision): `NodeTargetProxyFilterTest.testHeaderPreservedWhenTargetNameIsQueryValue` and `testHeaderPreservedWhenTargetNameIsPartOfQueryParamName` both FAILED, rc=1 as expected. Each is `org.junit.ComparisonFailure` at the responding-node assertion: expected `127.0.0.1:41507_solr`, was `127.0.0.1:41933_solr`. Both XML and log are in `pr5112-base-negative/negfail.xml/` and `negfail.log`. Not a compile or startup failure; both methods ran.

### Environment conditions and repairs

- The first `check -x test` at `46bc7e4` (`pr5112-head-46bc7e4/check.log`, 17:09 UTC, rc=1) failed in `:solr:solr-ref-guide:buildLocalAntoraSite`: Antora could not find `@asciidoctor/tabs`. Cause: the shell carried `NODE_ENV=production`, so npm skipped the devDependency `@asciidoctor/tabs`. A rerun with the same setting failed the same way; the task was UP-TO-DATE from the failed run.
- Repair: ran `:solr:solr-ref-guide:downloadAsciidoctorTabsExtension --rerun` with `NODE_ENV` unset (rc=0; package now at `solr/solr-ref-guide/.gradle/node/node_modules/@asciidoctor/tabs`), then `check -x test` with `NODE_ENV` unset (rc=0). No source, build script or test was changed for this repair.
- The base checkout has a local overlay of the test file only; `git status` there lists only that file.
- The tidy-changed focused run on `3d762be30f6b` was stopped on purpose; its partial output is kept in `pr5112-head/` for the record and is not evidence.

### Result summary for the comment draft

- Head `46bc7e428b5be519a497ecf65dcd0256b65fa84c`: tidy clean; focused 18 of 18 green (14 routing, 4 Basic Auth); neighbors 11 of 11 green; `check -x test` green after the docs-tool environment repair; `git diff --check` clean over the range from `3d762be30f6b`.
- Fail-before: the two discriminating methods fail on the original production code with the new test. The other two new methods (`testUnrelatedQueryValueWithoutHeaderIsHandledLocally`, `testHeaderAndQueryParamAreRejected`) were not part of the negative run and are covered by the positive run only.
- Bounded claim: this proves header-target preservation with unrelated query text and the header/query conflict behavior at the routing layer. It does not prove certificate identity. The Basic Auth tests passing is not certificate-identity proof, and the PR's certificate-identity question remains unresolved and outside this change.
- No public comment, PR, or Jira write was made. The comment draft waits for Nick.
