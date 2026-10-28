# Gate: PR 5112 target header routing follow-up, round 1

Status: DONE

Completed 2026-10-28 (cleanup slice, phone agent): superseded by `gates/pr-5112-target-header-followup-round-1-superseding-46bc7e4.md` before execution; the superseding job carries the proof.

- Tags: gate, premise-run, review.
- Intended host: any capable Linux gate host with the branch's JDK/toolchain, serialized behind its host build lock.
- Source: Nick's October 27 request to implement the review and return proof before drafting a public reply.
- Fork branch: `nick-boss-tech/solr:solr-18537-submit`.
- Exact implementation head: `3d762be30f6b8c2805c3edcfc48446488265a806`.
- Original PR / negative proof base: `b4fa46f1ecb11a60cc2cfd4783492639bb4e3a85`, apache/solr PR 5112, `dsmiley/solr:SOLR-18537-proxyRequestsByHeader`.
- Implementation claim: `claims/pr-5112-followup-windows-round-1.md`.
- Implementation report: `reports/pr-5112-target-header-followup-windows-round-1.md`.
- Receipt target: `receipts/PR-5112-target-header-followup.md`.
- State: Implemented, Proof Pending. No Gradle, compilation, HTTP reproduction, or tests ran on Windows. The source delta passes `git show --check`.

## Scope and preflight

Read live WORKFLOW.md and START-HERE.md, competing claims and the actual fork head. Follow detached tidy preflight and claim protocol before execution. Record host, UTC start, JDK/toolchain, host build lock, full detached checkout, runner, log and result paths. Preserve the full source tree and fresh raw evidence.

Require the implementation head to be one commit directly above the original PR head. The follow-up changes only three files, 42 additions and 9 deletions:

- `solr/core/src/java/org/apache/solr/servlet/NodeTargetProxyFilter.java`
- `solr/core/src/test/org/apache/solr/servlet/NodeTargetProxyFilterTest.java`
- `solr/solr-ref-guide/modules/deployment-guide/pages/solrcloud-distributed-requests.adoc`

The filter now assigns the parsed query target only when that parameter exists. An unrelated value or longer parameter name containing `X-Solr-targetNode` therefore cannot clear a real header target. A real header/query conflict still returns 400. The guide now says 400 for an unknown node and excludes colon-containing full node names from hostname shorthand; Javadoc matches.

This is bounded routing correctness proof. The PR's separately acknowledged certificate-identity question is unresolved and outside this change; Basic Auth test success must not be reported as certificate-identity proof. No upstream, contributor-fork, PR, or Jira writes are authorized. Nick will bring back the results before a comment is drafted.

## Formatting and positive proof

Run `./gradlew tidy` at the pinned head and require a clean tree. If it changes files, commit scoped formatting repairs with Nick Shanin's ICLA identity, no attribution trailer, and no PR number in the commit message. Push normally to the fork branch, add a superseding gate pinned to the resulting head, and prove that new exact head. Distinguish inherited PR formatting from this follow-up. Never edit this immutable job or carry proof forward to a changed head.

Run the complete affected suites from fresh output:

```sh
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.servlet.NodeTargetProxyFilterTest --tests org.apache.solr.servlet.NodeTargetProxyFilterAuthTest -Ptests.seed=5112C0DE
```

Require 18 tests executed (14 routing, 4 Basic Auth), zero failures/errors/skips. Preserve both JUnit XML files and stdout before any later cleanTest. Check these four new methods and retain their outcomes:

- `testHeaderPreservedWhenTargetNameIsQueryValue`: GET `foo=X-Solr-targetNode`, header points to the other live node; response names that target.
- `testHeaderPreservedWhenTargetNameIsPartOfQueryParamName`: GET `X-Solr-targetNodeSuffix=value`, header points to the other node; response names that target.
- `testUnrelatedQueryValueWithoutHeaderIsHandledLocally`: same unrelated value, no targeting directive; response names the receiving node.
- `testHeaderAndQueryParamAreRejected`: actual target supplied in both places, even with matching values; returns 400.

Existing query-only routing, POST behavior, v1/v2 headers, self-targeting, unknown-node errors, directive consumption on a subsequent proxy hop, encoded paths/query characters, shorthand, and Basic Auth 200/400/401/403 must remain green.

Run the neighboring proxy/parser suites, retaining their evidence separately:

```sh
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.servlet.HttpSolrProxyTest --tests org.apache.solr.servlet.SolrRequestParserTest -Ptests.seed=5112C0DE
```

Require zero failures/errors/skips and record actual counts. Run `./gradlew check -x test` as required by this branch's AGENTS.md, with no silent exclusions. Record environment repairs and their causes. Finish with `git diff --check` and a clean worktree at the exact verified head.

## Negative proof: original production code with the new tests

Use a separate clean detached checkout of `b4fa46f1ecb11a60cc2cfd4783492639bb4e3a85`. Overlay ONLY `solr/core/src/test/org/apache/solr/servlet/NodeTargetProxyFilterTest.java` from the implementation head (or its superseding formatting head). Keep every production file at the original PR revision. Record both revisions, overlaid test hash, synthetic tree and diff.

```sh
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.servlet.NodeTargetProxyFilterTest.testHeaderPreservedWhenTargetNameIsQueryValue --tests org.apache.solr.servlet.NodeTargetProxyFilterTest.testHeaderPreservedWhenTargetNameIsPartOfQueryParamName -Ptests.seed=5112C0DE
```

Require exactly these two executed methods to fail at the responding-node equality assertion: expected the target node, actual the receiving node. Retain the actual distinct node names, assertion messages, logs and fresh XML. A startup/compilation failure, missing test, skip or passing original is not fail-before proof. Investigate and correct the premise if either method passes.

## Completion

GREEN requires both discriminating negative assertions, exact-head positive suites and neighbors, clean tidy, required checks, clean tree and retained raw evidence. Diagnose executed-test failures as repair work; do not retry blindly or replace this with a fresh unrelated gate. Follow WORKFLOW.md for environment failures before tests execute.

The receipt must name the final verified fork head, original proof base, commands/seeds, class/method counts and skips, negative assertion details, toolchain, environment conditions and runner/raw evidence paths. Its final section should summarize the result for Nick's later comment draft, with no unverified readiness/security claim. Do not post a public comment.
