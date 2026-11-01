# Gate for analysis handler extensibility tests round 1

Status: OPEN.

## Identity and ownership

- Intended host: any available capable Linux gate host with gate, implementation, review and records capabilities, JDK 21, the branch's pinned Gradle toolchain and dependencies.
- Serialize under the host build lock. Claim before proof, using a clean task-specific worktree. Claim records host, UTC start, build lock, JDK, runner, worktree and log directory.
- PR: https://github.com/apache/solr/pull/5279; no JIRA ticket.
- Fork repository: https://github.com/nick-boss-tech/solr.git.
- Fork branch: pr-5279-analysis-extensibility-tests.
- Exact head H: 49ce5c0c10acb02cdfd828f8ad0f9588aa9d832e.
- Contributor head and patch parent P: fd033eb55a4eb7c4a5d8c626ad099dd8137098fb.
- Implementation claim: claims/pr-5279-analysis-extensibility-tests-windows-round-1.md.
- Implementation report: reports/pr-5279-analysis-extensibility-tests-round-1.md.
- Deliverable: receipts/PR-5279-analysis-extensibility-tests.md, owned by the claiming gate host. Include exact final head, commands, exit codes, seeds, test counts, fresh XML and log locations, mutations and limits.
- Windows has not claimed Linux execution and has run no builds/tests. Old upstream CI at P does not cover H.

## Preflight and repair

Fetch the live branch, verify H and contributor ancestry, and read this job and its related records from one board snapshot. Check claims before taking it. If the source head moved, inspect the delta and reconcile rather than running stale proof.

Run tidy on the clean head. If formatting or compilation needs repair, make only scoped changes to the five outbound files named in the report. Commit using Nick Shanin <nick.boss.us@gmail.com>, with no attribution trailer or PR-number reference in the message. Plain-push to this fork branch, separately verify the remote head, and post a new add-only repair report and superseding gate pinned to that head before positive proof. Never force, push to apache or the contributor's fork, or edit this immutable job. A formatting change invalidates proof for the earlier head.

## Positive proof

Use seed 5279C0DE. Keep individual command logs and fresh JUnit XML, archived before another cleanTest overwrites it. No skipped required test is a pass. Do not exclude tasks except the expressly named -x test for the root check.

1. `./gradlew tidy` must exit zero and leave the final pinned head clean. Also run `git diff --check`.
2. `./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.handler.FieldAnalysisRequestHandlerTest --tests org.apache.solr.handler.DocumentAnalysisRequestHandlerTest -Ptests.seed=5279C0DE`
   Require both complete suites to execute with no failures, errors or skips. Explicitly identify all four new methods in the fresh XML: testCustomAnalyzers and testCustomAnalyzersShowMatch in each class. This also compiles the subclass fixture in org.apache.solr.handler.custom and its super.analyzeValues call across the package boundary.
3. `./gradlew check -x test` must succeed. Record setup conditions and any real failure; a module-only check does not satisfy this leg.
4. Finish with a clean source tree, git diff --check, and a separate remote branch read matching the tested full SHA.

## Discriminating mutation proof

This is a tests/Javadoc follow-up over an existing extension API. The contributor head P is expected to pass these tests. Do not describe a failure to compile on the pre-PR base as runtime fail-before evidence.

Use separate clean detached negative worktrees at the final positive head. Retain all tests, helper signatures, hook declarations and visibility. Record each mutation diff and source/test hashes. Never commit or push mutations. Change only the stated production call sites in each leg. Run the following four-method filter separately for each mutation, retaining fresh XML per leg:

`./gradlew :solr:core:cleanTest :solr:core:test --tests 'org.apache.solr.handler.FieldAnalysisRequestHandlerTest.testCustomAnalyzers*' --tests 'org.apache.solr.handler.DocumentAnalysisRequestHandlerTest.testCustomAnalyzers*' -Ptests.seed=5279C0DE`

1. **Index hook bypass:** in FieldAnalysisRequestHandler.analyzeValues and DocumentAnalysisRequestHandler.handleAnalysisRequest, replace only the getIndexAnalyzer(fieldType) calls with fieldType.getIndexAnalyzer(). Leave the base hook bodies unchanged. Expect all four new tests to fail because index tokens remain uppercase rather than the expected lowercase tokens.
2. **Query output hook bypass:** in those same handlers, replace only the getQueryAnalyzer(fieldType) calls used to build the query AnalysisContext with fieldType.getQueryAnalyzer(). Keep getQueryTokenSet calls routed through the hook. Expect all four new tests to fail: the multiword query splits into two tokens and the one-word query remains uppercase.
3. **Matching hook bypass:** replace only the getQueryAnalyzer(fieldType) calls passed to getQueryTokenSet with fieldType.getQueryAnalyzer(). Keep index and query output routed through the hooks. Expect exactly the two testCustomAnalyzersShowMatch methods to fail for a missing match=true marker. The two multiword testCustomAnalyzers methods must still pass because neither split index token matches the whole query.

Four tests must execute in every mutation leg. Build errors, missing classes, startup failures, skips or unrelated assertions do not satisfy mutation proof. If expected outcomes differ, investigate and record the reason before retrying or changing acceptance. A leg that executed and failed outside its intended assertions follows the board's failure/repair rules; do not blindly rerun it.

## Completion

GREEN requires all positive legs at the exact final pushed head, all three expected mutation outcomes, clean status and retained logs/XML. The receipt must distinguish the positive head from synthetic negative trees, explain any repairs or execution conditions, and include a concise draft follow-up with the pinned contributor-to-tested-head diff. Public communication is handled separately under the standing workspace rules.
