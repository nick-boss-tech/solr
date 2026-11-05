# Assignment: SOLR-12613 adjacent follow-up scope and branch plan, round 1

Status: OPEN (planning and source review only; implementation held by Nick).

- Posted: 2026-11-04 Eastern, at Nick Shanin's direct request.
- Intended host: any host with review and draft capabilities; records capability is needed to publish the deliverable.
- Required tags: review, draft, records. No implementation, gate, premise-run or ui build capability is needed for this planning slice.
- Deliverable: `reports/solr-12613-adjacent-followup-plan-round-1.md`, added to coordination-ui by the claiming host.
- Related: SOLR-12613 / PR 5110, SOLR-18537 / PR 5112, SOLR-18619 and SOLR-18549.
- Scope authority: this is a direct owner assignment for follow-up planning. The older pool-ui-tickets-round-1 exclusion of SOLR-12613 does not exclude this specific planning slice.

## Owner direction and contribution route

Nick asked for adjacent code opportunities around the node-picker PR, then selected an independent follow-up branch based on main and that PR rather than an offer of commits for the original author to merge. He subsequently withdrew implementation for now and requested plan/workflow refinement, then explicitly requested this board assignment.

Carry out the planning and source-review slice. Implementation remains held until Nick explicitly directs it to resume. Posting this assignment does not release that hold. Do not create or patch a source branch, run reproduction/build/test jobs, or post an implementation gate under this assignment. The future branch and Linux proof instructions below are planning requirements.

No source branch, patch, implementation commit, gate or runtime reproduction was created during the initial assessment. The candidates are not runtime-confirmed defects.

## Starting evidence and existing work

The following are historical source snapshots, not instructions to use stale revisions. Refresh live state before drawing conclusions.

| Item | Initial assessment snapshot |
| --- | --- |
| PR 5110, SOLR-12613 | OPEN, head `73b30956ff2c0f7b16f6af3bceacf95bda8271b9` |
| PR 5110 API base | `82abb86bb5b7326fe48c117e4de69981a43e4251` |
| PR 5110 merge base with then-current main | `ad26fdf2c6bee8168eae043b94833f05643d1995` |
| PR 5112, SOLR-18537 | MERGED November 2; head `46bc7e428b5be519a497ecf65dcd0256b65fa84c` |
| PR 5112 merge commit | `439b2e5096f7bd591d893fd9971e4ad9f9f15770` |
| Main used for adjacent reads | `07ccc73cd24e26e59da169ca35b3fd5f64c35986` |
| Board used for this assignment | `42dfa1a4f81f5d18b7f53052e95d5875fc966801` |
| SOLR-18549 fork branch read-only lookup | `b89664d164db46cdac432668e2d9a07ce95fbd37`; source overlap not yet assessed |

[PR 5110](https://github.com/apache/solr/pull/5110) adds the legacy Angular Admin UI's node picker. [PR 5112](https://github.com/apache/solr/pull/5112) supplies the generic targeting transport. This concerns `solr/webapp/web`, not Christos's Kotlin/Compose `ui/` build.

This workspace previously reviewed both PRs. Nick's targeting-header-preservation fix and guide corrections were incorporated into PR 5112's merged head; the [public offer already exists](https://github.com/apache/solr/pull/5112#issuecomment-6450355228). Do not count those as new opportunities or prepare another adoption offer.

PR 5110's newer typed read helper already copies `failedNodes` and preserves `SolrException`. Its live-node lookup error branch now settles targeting state and refreshes core navigation. Ordinary selection, session restoration and navigation have browser coverage. Recheck current source rather than repeating the old F1-F3 findings as unchanged bugs.

SOLR-18619 is the existing migration ticket for Metrics, Nodes system information and cloud log messages. Read `reports/solr-18619-ui-target-node-assessment-round-1.md` and `receipts/SOLR-18619-selenium-failure-fixes-round-1.md`. The receipt at `35b5c45ec6ddb8ec8d54b89bbf9ca97368d2a8ca` covers Selenium thread filtering and duplicate property values only. It neither implements nor proves the header migration. Do not duplicate those repairs.

## Work to complete

1. Refresh the board, live PR heads/merge state/discussion, upstream main, relevant Jira context and competing branches. Read the actual SOLR-18549 changes before deciding whether C1 is available. Use immutable source revisions, not a dirty source checkout.
2. Reassess the candidates below across the actual request path and neighboring consumers. Cover correctness, architecture, maintenance/reuse and verification separately. Distinguish introduced behavior, inherited behavior, feature work, hypotheses and coverage-only proposals.
3. Recommend one small first contribution, with exact source/test file scope, user-visible invariant and a concrete Linux proof plan. C1 is the provisional preference, subject to overlap and current-code checks. Reject or defer candidates contradicted by current evidence.
4. Specify the independent follow-up branch baseline and review boundary for both possible PR states. Include how to distinguish our delta from the original PR's changes.
5. Publish the self-contained planning report on this board. Record which cases remain unexecuted and why. Do not rely on Windows-local research files being available on another host.

## Candidates and evidence

### C1: Preserve validation status in the existing system-info proxy

Provisional first slice: inherited production behavior, source-supported and unexecuted.

[GetNodeSystemInfo.java:70-92](https://github.com/apache/solr/blob/07ccc73cd24e26e59da169ca35b3fd5f64c35986/solr/core/src/java/org/apache/solr/handler/admin/api/GetNodeSystemInfo.java#L70-L92) catches every exception from its typed proxy and wraps it as SERVER_ERROR. [RemoteRequestProxy.java:185-196](https://github.com/apache/solr/blob/07ccc73cd24e26e59da169ca35b3fd5f64c35986/solr/core/src/java/org/apache/solr/handler/admin/proxy/RemoteRequestProxy.java#L185-L196) deliberately raises BAD_REQUEST for malformed/non-live cloud node names. A cloud request to `/api/node/system?nodes=no.such.host:1234_solr` therefore has a source path that rewrites 400 into 500.

PR 5110 preserves these errors in its new helper, but the older system-info consumer remains separate. This older sibling was already noted in the October review. Inspect SOLR-18549's current branch before deciding this cloud-validation slice is separate from its standalone handling.

Expected source scope if confirmed: `solr/core/src/java/org/apache/solr/handler/admin/api/GetNodeSystemInfo.java` and `solr/core/src/test/org/apache/solr/handler/admin/api/GetNodeSystemInfoSolrCloudTest.java`, plus an appropriate changelog after the follow-up has its own valid issue/PR identity. Reuse the existing cloud test fixture, which currently checks a valid node. Do not relabel an unrelated inherited defect as SOLR-12613 or create a public issue under this assignment.

Planned proof: separate malformed and non-live inputs, asserting actual HTTP 400; require baseline failure specifically at 500-versus-400 assertions and candidate success. Preserve the valid-node case and include `GetNodeSystemInfoTest` as the standalone neighbor.

### C2: Recognize an encoded targeting parameter name

Independent filter candidate, source-supported and unexecuted.

[NodeTargetProxyFilter.java:76-89](https://github.com/apache/solr/blob/07ccc73cd24e26e59da169ca35b3fd5f64c35986/solr/core/src/java/org/apache/solr/servlet/NodeTargetProxyFilter.java#L76-L89) only parses a GET query if the raw text contains literal `X-Solr-targetNode`. A key spelled `%58-Solr-targetNode` misses this guard, while [the normal parser decodes parameter names](https://github.com/apache/solr/blob/07ccc73cd24e26e59da169ca35b3fd5f64c35986/solr/core/src/java/org/apache/solr/servlet/SolrRequestParsers.java#L330-L351). The candidate symptom is local execution despite an encoded query-only directive.

Plan a raw-wire test in existing `NodeTargetProxyFilterTest` that preserves the encoded key, checks responding-node identity and checks the encoded-key/header conflict. Preserve ordinary routes, the merged unrelated-query regressions and POST behavior. Establish the accepted encoding contract before selecting the fix. This is distinct from Nick's already merged header-loss fix.

### C3: Reject late core-status responses after node selection changes

UI timing hypothesis requiring deterministic reproduction.

[MainController's core refresh](https://github.com/apache/solr/blob/73b30956ff2c0f7b16f6af3bceacf95bda8271b9/solr/webapp/web/js/angular/app.js#L537-L567) updates the persistent core menu without checking a request generation/selection captured when the request began. [Selection reloads the route](https://github.com/apache/solr/blob/73b30956ff2c0f7b16f6af3bceacf95bda8271b9/solr/webapp/web/js/angular/app.js#L772-L789).

Plan the sequence: select A and delay its core-status response; select B and complete B's response; release A's response; assert B's menu remains correct. Use distinguishable core inventories and reuse `AdminUiNodePickerTest`. Child controllers may be destroyed on route reload, so do not assume their late callbacks repaint a new view; the persistent parent menu is the concrete starting point. Reconcile the active author's work and preserve remote mutation guards.

### C4: Bind target headers to each request across migration consumers

Planned feature/design work under SOLR-18619, not a proven race.

PR 5110's `NodeTarget.withNodes` sets one pending target consumed by the next generated-client plugin invocation ([setter](https://github.com/apache/solr/blob/73b30956ff2c0f7b16f6af3bceacf95bda8271b9/solr/webapp/web/js/angular/services.js#L124-L131), [consumer](https://github.com/apache/solr/blob/73b30956ff2c0f7b16f6af3bceacf95bda8271b9/solr/webapp/web/js/angular/services.js#L218-L233)). Its immediate call path may be synchronous; a shared slot alone is not evidence of a race.

Examine two real client families before proposing a helper: Metrics uses Angular $resource, whereas system-info/log-message calls use the generated client. Nodes system info still uses `nodes` at the recorded PR-head `cloud.js:394`; Metrics uses `node` at line 450 onward. Concurrent per-node calls need a target owned by each request, without mutating shared default headers.

Plan acceptance for distinguishable concurrent responses, local/standalone requests, visible per-node failures and retained successful siblings. Resolve how selected-node log reads in PR 5110 relate to cluster log aggregation in SOLR-18619. Keep log-level broadcast, parameter deprecation and system-info schema redesign separate. Do not hand-edit generated build output or introduce a framework without a concrete need.

### C5: Combine encoded paths with a second proxy hop

Previously noted coverage opportunity, with no observed production failure.

`NodeTargetProxyFilterTest.testEncodedPathPreserved` serves directly on the destination; `testDirectiveConsumedWhenTargetForwardsToReplica` exercises a further hop with a plain path. Plan their combination using the existing collection/echo handler and assert final URI plus directive consumption. The second hop has its own [decode/re-encode path](https://github.com/apache/solr/blob/07ccc73cd24e26e59da169ca35b3fd5f64c35986/solr/core/src/java/org/apache/solr/servlet/HttpSolrCall.java#L641-L654).

Both unchanged and added-test suites are expected to pass unless a defect is established. For discriminating coverage, plan an isolated mutation preserving a functioning API scaffold and require an assertion failure, not a compile/startup failure.

## Follow-up branch and proof workflow to specify

- If PR 5110 has merged when implementation is later authorized, start from refreshed main containing the merged result. Account for squash merges rather than requiring the old PR head to be an ancestor.
- If it is still open and Nick still wants both inputs, pin main and PR head, integrate with an ordinary merge on an isolated follow-up branch, and record both inputs, conflict repairs and the resulting integration baseline before adding the fix.
- C1 may apply directly to main. Including the picker is the chosen integration baseline, not proof of a source dependency.
- Review the follow-up as `integration-baseline..candidate`. Before the original PR lands, a comparison against main includes the original PR; label that difference and keep conflict repairs distinct from our fix.
- Plan fail-before proof on the chosen baseline with the new tests overlaid, then positive proof on the exact candidate. A direct-main comparison may establish inherited behavior but cannot replace integration-baseline proof.
- The future Linux gate must name exact head/baseline, fully qualified classes, fixed seed, tidy, required root checks and fresh logs/XML. Windows runs no Gradle or local test queue.
- Once implementation is explicitly authorized again, scoped source work must be committed and plain-pushed to Nick's fork in the same session, independently verified by remote SHA, and accompanied by the add-only implementation report and exact-head Linux gate. Status stays Implemented, Proof Pending until the receipt satisfies the gate.
- If the upstream merge changes the source integration, reconcile and gate the new head. Force-pushing an open public PR requires Nick's direction.
- Opening the follow-up PR, creating/commenting on Jira, or offering commits to the original author remain separate public actions. This planning assignment authorizes none of them.

## Report acceptance

The report must contain refreshed revision/state and overlap tables, one recommended slice, exact proposed outbound files, the baseline decision, an executable future Linux proof specification, and explicit unassessed/unexecuted limits. Keep source findings, feature design and coverage distinct. Existing SOLR-18615 asset caching work, SOLR-18161 response-schema work and PR 5112's separately acknowledged certificate-identity discussion are not new discoveries assigned here.

Claim the planning slice under WORKFLOW.md and publish the report as an add-only record. Do not edit this assignment or generated indexes. Completion means the planning deliverable exists and is readable on the board; it does not release the implementation hold.
