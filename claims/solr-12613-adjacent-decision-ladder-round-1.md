# Claim addendum: logging scope and request-specific transport decision

- Parent claim `claims/solr-12613-adjacent-windows-round-1.md`; assignment `assignments/solr-12613-adjacent-implementation-round-1.md`.
- Owner steering: Nick directed use of the decision ladder and presentation of the logging scope choice in the follow-up PR, rather than another owner question. This record concerns a proposed behavior on a fork branch, not public posting authorization.

## Evidence and ladder

Rung 1: the picker PR's `logging.js` at `73b30956ff2c0f7b16f6af3bceacf95bda8271b9` reads the selected node and the index badge identifies that node. The parent main controller requires a selection for node screens. The earlier main implementation read local messages. History includes `0a03be42193` (generic proxy integration) and `73b30956ff2` (explicit selection/landing). These establish selected-node expectations in the proposed picker, not a settled cluster-aggregation contract.

Rung 2: execution can prove target routing and aggregation, but a probe cannot determine the preferable product default. No runtime proof is claimed on Windows; deterministic routing/failure tests belong to the Linux gate.

Rung 3: SOLR-18619 explicitly asks for listing live nodes and fetching one proxied log response per node in cloud mode, retaining a direct local standalone request. The picker PR separately promises selected-node screens. Neither specification alone decides their combined UI default. The owner wants this choice posed in the follow-up PR.

Rung 4: implement a reviewable proposal preserving selected-node messages by default, with an explicit cloud-only All nodes option that enumerates live nodes, fetches each through its own target header, labels event origins and retains successful results when a sibling fails. Standalone retains one untargeted local read. Hide the single-node badge while All nodes is selected. This makes both uses available without silently changing the picker's default scope. The default and control placement remain a maintainer-facing product choice because they reconcile two active public feature contracts; the PR draft should present the alternative of always aggregating cloud messages and explain the evidence above. Nick expressly requested that review choice. No escalation to rung 5 is needed.

Transport choice: use a small source-owned Angular $http GET helper that allocates a target header map per call. Reuse it for Metrics, Nodes system information and log messages. Angular's existing interceptor supplies authentication and ordinary HTTP failure handling. Keep the existing generated client for unaffected APIs; do not mutate its shared defaults or hand-edit generated output. The original pending-target plugin is not declared a proven race: immediate generated calls consume it synchronously, and the new concurrent fan-out does not use that shared slot.

The C4 baseline is `b45bae5da3a151c7b439ff4e42009c383b538fae`, which includes the already-gated browser prerequisites. All code/test changes must be pushed and exact-head gated before readiness. The final report and local PR draft will repeat the chosen behavior, alternatives, proof limits and open product choice. This addendum does not authorize a public PR/comment/Jira write.
