# Gate: SOLR-17241 wt response format on non-hosting collection nodes, round 1

Status: OPEN. Implementation pushed; proof pending. Public comment HOLD.

- Tags: gate, review, records.
- Intended host: any capable Linux gate host with JDK 21; vm6 preferred because it holds the earlier reproduction, but this is not exclusive ownership. Follow hosts/pool-linux.md, disk/inode guards and the host build lock. One build per host.
- Owner direction: Nick requested the patch first, then a Linux gate receipt BEFORE commenting. No JIRA/GitHub comment, PR or mailing-list post is authorized by this job.
- Fork: nick-boss-tech/solr, branch solr-17241-submit.
- Candidate: bded8997e57e7cc0974aa766251735f93f289f87 (remote verified 2026-10-29).
- Parent / original production control: 9859d8bb2e8d63061116b8281639a4beb1be480c, upstream/main when implemented.
- Module: solr/core. Seed: 17241C0FFEE17241. Preserve the same seed across each before/after pair.
- Gate claimant: unclaimed. Record host, UTC time, JDK path/version, runner script, worktree, logs and idempotency key SOLR-17241-wt-format-round-1@bded8997e57e7cc0974aa766251735f93f289f87 in a new claim before execution.
- Suggested log directory: /workspace/gates/logs/<host>/solr17241-wt-format-round-1/. Preserve separate full stdout/stderr, command, exit status and fresh test XML for each leg; no grep pipelines as status evidence.
- Deliverables: receipts/SOLR-17241.md and reports/SOLR-17241-wt-format-linux-round-1.md, plus the gate completion claim. The gating host owns its receipt. Final receipt section: draft PR description, NOT POSTED.

## Scope and prior evidence

Read reports/SOLR-17241-wt-format-implementation-windows-round-1.md, receipts/solr-wt-non-hosting-review-round-1.md and receipts/solr-wt-non-hosting-validation-round-1.md. This is independent of SOLR-18600 and binary compatibility.

The filter now takes params from SOLR_QUERY_REQUEST first, falling back to the existing parsed SOLR_PARAMS property only when the request object is absent. No servlet reparse, routing change, request-body change or SOLR-16398 edit. V2HttpCall publishes SOLR_PARAMS even on the non-hosting path.

The new filter suite checks json/xml/javabin selection with a conflicting Accept header, existing request-param precedence, Accept fallback, missing and wildcard Accept, missing params, error and exempt-resource guards, and blank/unknown wt. The existing V2ApiIntegrationTest suite gains a one-replica collection in its two-node cluster. It explicitly targets the hosting node FIRST and the non-hosting node SECOND with a javabin parser, checks status zero and checks that reload replaces the hosted SolrCore. It cleans up the collection. Do not substitute a random-node probe or a JSON parser.

The earlier uploaded ProbeRoutingTest source is split-only, despite the receipt describing an expanded reload probe. This gate must use the COMMITTED assertion-based regression, not treat the old probe's rc 0 as proof. Existing SOLR-16398 JSON-parser tests do not prove the default binary path.

## Required candidate proof

1. Refresh the live fork head and competing claims/receipts. Verify it equals the candidate. Use an isolated FULL checkout, not the Windows sparse checkout or another worker's dirty probe tree. Record git status and hashes. Read the applicable AGENTS.md. Run the detached tidy preflight and then claim under the normal board rules.
2. Parse changelog/unreleased/SOLR-17241-wt-non-hosting-node.yml and run tidy. Require a clean tracked tree afterward. No tests/checks disabled. Formatting was done by hand on Windows; no build has run there.
3. Compile with Error Prone enabled (-Pvalidation.errorprone=true): :solr:api:compileJava, :solr:solrj:compileJava, :solr:core:compileJava and :solr:core:compileTestJava. A compile/setup failure is not behavioral proof.
4. Run the complete org.apache.solr.jersey.MediaTypeOverridingFilterTest and org.apache.solr.handler.V2ApiIntegrationTest suites via :solr:core:test with -Dtests.seed=17241C0FFEE17241. The filter suite has eight test methods; all must execute and pass, without a Mockito assumption skip. Derive actual integration counts from fresh XML. In particular testReloadHonorsWtOnNonHostingNode must execute, parse javabin and verify both reload effects. Preserve XML separately before another test invocation overwrites it.
5. Run :solr:core:check -x test and root check -x test. Record separate outcomes, environmental conditions and first failures. Do not call a failed root check a pass merely because a focused module check passed. Required runtime tests remain the focused suites above; this is not a request for a full root test run.

Use the repository's documented test filtering syntax. Typical commands are ./gradlew :solr:core:test --tests org.apache.solr.jersey.MediaTypeOverridingFilterTest -Dtests.seed=17241C0FFEE17241 and the corresponding integration class. Preserve exact executed commands rather than relying on these examples as a runner.

## Required original-production controls

In a separate disposable checkout derived from the candidate, replace ONLY solr/core/src/java/org/apache/solr/jersey/MediaTypeOverridingFilter.java with the file from parent 9859d8bb2e8d63061116b8281639a4beb1be480c. Retain the candidate tests unchanged. Record the complete diff and hashes showing that this is the sole production difference; do not commit or push this control tree.

Run two separate focused invocations with the same seed and fresh XML:

- MediaTypeOverridingFilterTest.testWtFromQueryParamsWithoutSolrQueryRequest: must fail because wt in SOLR_PARAMS is ignored and the negotiated text/plain header survives. This is an assertion failure, not a compile/mock/setup failure.
- V2ApiIntegrationTest.testReloadHonorsWtOnNonHostingNode: must pass the hosting-node request/control and fail at the non-hosting request with the expected SolrJ javabin-versus-application/json MIME mismatch. A missing route, authentication failure or any unrelated exception does not establish this regression. Retain a stack trace and the XML. The candidate pass must use the identical committed test source and seed.

Do not swallow exceptions or turn expected failing tests into a green Gradle summary. The before legs should have failing test exit status; record why that is expected. If either original-production control unexpectedly passes, investigate the topology/route/control identity before claiming PASS.

## Repair and acceptance

Tidy-only repair: commit and plain-push the formatting change to the fork with Nick Shanin identity, verify the remote SHA, and post a NEW superseding gate pinned to that head before running its proof. The current head's proof does not cover a changed head. No force push or edit of this job. Source/test failures need an add-only repair report naming the failing leg and exact error; do not weaken the regression, silently change the candidate or run unrelated ticket work here.

PASS requires all candidate steps and both original-production controls at the recorded identities, plus a final clean status and remote-head verification. The receipt must state the exact gated SHA, parent, host/JDK, seed, per-leg command/status, counts/failures/skips, log and fresh-XML paths, control diff/hash identity, and limitations. Otherwise write INCOMPLETE/FAIL with the missing or failed leg and repair needed. No old green receipt substitutes for this gate.

Keep SOLR-16398 branch/default-parser follow-up separate. This receipt must not claim split/move proof on upstream main or certify that branch. No released-version/backport claim is established by a main-only gate. On green, leave public communication HOLD for Nick's decision; provide a concrete, evidence-based draft rather than posting it.
