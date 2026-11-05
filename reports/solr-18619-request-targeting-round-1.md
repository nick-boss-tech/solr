# Request-specific Admin UI targeting implementation, round 1

Status: Implemented, Proof Pending.

- Fork `pr-5110-followup-request-targeting`, exact live head `4d8c0a5a94528514aea41c429bb5009fd82a9ffa`. Ordinary implementation `7e2076f391bf084e0e9964b8bfbcef3879712e37` plus an assertion-strengthening commit; plain-pushed and separately read back.
- UI baseline `b45bae5da3a151c7b439ff4e42009c383b538fae`: main `07ccc73cd24e26e59da169ca35b3fd5f64c35986`, picker `73b30956ff2c0f7b16f6af3bceacf95bda8271b9`, previously gated Selenium/property repairs `35b5c45ec6ddb8ec8d54b89bbf9ca97368d2a8ca`, with no conflicts. The lifecycle fix is a separate branch, not included here.
- Gate `gates/solr-18619-request-targeting-round-1.md`; receipt `receipts/SOLR-18619-request-targeting-round-1.md`.
- Decision record `claims/solr-12613-adjacent-decision-ladder-round-1.md` follows the owner's explicit direction to use the ladder and present the product choice in the follow-up PR draft.

## Implemented behavior

Metrics and Nodes system information use a source-owned Angular $http helper with a new header map for each request. Metrics drops the node query parameter and preserves its promise-facing consumer plus raw local endpoint behavior. Nodes makes one native-response read per node and shows per-node system/metrics errors without losing healthy rows. Angular $q.all keeps merged metric updates within digest processing.

Log messages default to the selected node, with an explicit cloud-only All nodes option. That option lists live nodes and fetches messages separately through per-call headers; messages are chronologically merged with origin labels. Failures are named while healthy messages remain; recovery clears errors, and an all-node failure clears old events. Standalone uses one untargeted local call. The selected-node badge is hidden for aggregate viewing. Polls are scheduled after settlement and old/destroyed-view responses are discarded. Log-level writes retain their broadcast behavior.

The choice combines SOLR-18619's aggregation intent with the picker's selected-node intent. A runtime probe cannot settle that product default. At ladder rung 4, this is a reviewable proposal, with the maintainer-facing alternative of always aggregating cloud messages. Nick specifically requested that choice be posed in the PR, so it remains in the draft below rather than being escalated back to him.

## Source scope and verification limits

12 changed files: SOLR-18619-target-node.yml; configuring-logging.adoc; AdminUiTestBase, AdminUiNodeScreensTest, AdminUiLoggingScreenTest, AdminUiLoggingStandaloneTest; index.html; services.js; controllers/cloud.js and logging.js; partials/cloud.html and logging.html. The gate lists their full paths. Changelog author is Nick Shanin, no nick line. No generated output or backend API schema was changed.

Six new test methods exercise actual browser request headers and returned system node identity, log aggregation targets, controlled partial failure/recovery and origin rendering, and the untargeted standalone path. The reusable XHR capture helper collects only method/URL/target, not authentication headers. Failure payload tests use controlled service results and are not claims of real node outage reproduction. The gate specifies five isolated runtime mutations with executable APIs and unchanged tests; assertions without mutation proof remain pins.

Scoped source review and diff --check passed. No runtime, browser, compilation or Gradle proof ran on Windows. The initial assessment remains source-derived until the exact-head Linux receipt arrives. The existing prerequisite receipt proves only those inherited repairs. No public PR, comment, Jira change, contributor-branch write or upstream push was made.

## Follow-up PR draft (not posted)

Title: SOLR-18619: use request-specific node targets in the Admin UI

🤖 AI-assisted draft, reviewed for scope by Nick's directed workflow; final human review and Linux results are pending.

The Admin UI's per-node Metrics and Nodes reads still use the older node/nodes proxy parameters. This follow-up sends a separate X-Solr-targetNode header with each request, preserving native single-node responses and showing which node failed while retaining healthy results. Log messages use the same request-specific path; standalone reads remain local.

The branch is based on main plus the node-picker change and the already-gated browser prerequisites. Its own delta is [the UI baseline to the candidate](https://github.com/nick-boss-tech/solr/compare/b45bae5da3a151c7b439ff4e42009c383b538fae...4d8c0a5a94528514aea41c429bb5009fd82a9ffa). Reconcile against the actual upstream merge before opening an independent follow-up PR.

### Choice for review

I kept selected-node messages as the default to preserve the node picker's scope, and added Show messages from all nodes for cluster viewing. The aggregate view labels each message's node and names failed reads. SOLR-18619 originally describes cloud aggregation, so always aggregating in cloud mode is also a defensible default. Would you prefer that default, or this explicit opt-in? Log-level changes continue to broadcast in either case.

### Verification

Pending the exact-head Linux gate. Six new browser methods cover routing, node identity, aggregation, partial failure/recovery and standalone handling; the job also requires transport/browser neighbors, tidy, root checks and targeted mutations. Do not replace this paragraph with success claims until the actual receipt has been read and matched to the offered head.

AI agents assisted with research, implementation, source review and drafting under Nick Shanin's direction. Nick retains responsibility for any eventual contribution.
