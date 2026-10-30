# SOLR-18600 round-2 behavioral closure: reconcile the vm2 additive candidate

Status: ADDENDUM to assignments/solr-18600-dual-release-behavior-closure-round-2.md. Not a separate runnable job. One shared claim for the remaining Linux work.

## Live reconciliation

Reviewed coordination-ui snapshot 238336be0a9f6d934e1ebef19c000b08daf1bc44 after Nick supplied both gate-agent updates.

- The repeated update at 0f19ef310d02b47cc5da72be21462c3728ebf7a0 is vm6's completed round-1 diagnostic/design work. Its claim is DONE; it did not execute the round-2 follow-up.
- The round-2 follow-up was posted at aea9655a8d424f93352be6b853e0278cd02394e1 (2026-10-30 00:30:13 UTC). At the reviewed snapshot it remains OPEN, with no matching claim, behavioral report or receipt. Its source pin must now be updated by this addendum, not executed from the stale c40665a prototype.
- Independently, vm2 built an additive source repair on c40665af8d6b. Its first implementation commit b5261d29d171 is dated 2026-10-29 23:20:59 UTC; repairs 37228af35b31 and 6f61b6dbaf96 followed. Its reported exact-head gate ran 2026-10-30 00:02-00:24 UTC, BEFORE the round-2 follow-up was posted. It therefore was not execution of that follow-up.
- vm2 posted receipts/SOLR-18600-additive-round-4.md and assignments/vm2-solr-18600-additive-review-round-4.md at 99b929343cf (2026-10-30 00:32:58 UTC). That review assignment remains OPEN.
- No separate vm2 additive implementation/gate claim or gate job was found under claims/, gates/ or hosts/ at the reviewed snapshot. This is a coordination record gap, not proof about which private instruction vm2 received. The records show overlapping design and implementation work, not two completed executions of the same round-2 assignment.

Preserve both agents' results. Do not repeat completed inventory, compilation or existing suites solely because they were produced on different hosts. Do not equate the differently scoped inventories or cohort counts.

## Candidate and ownership override for round 2

Use nick-boss-tech/solr:solr-18600-additive at **6f61b6dbaf963627fb4e9d4f479c9e5f6d9979d3**, separately verified against the live fork ref. This candidate descends from the original c40665af8d6bd23c85fef565f93de5004a7b2ca4; it is now the input for round-2 behavioral closure. The round-1 prototype is comparative design evidence only. This overrides the original round-2 candidate pin and its instruction to apply vm6's prototype diff; do not overlay that different implementation blindly onto vm2's repair.

Intended executor: any capable Linux host, vm2 preferred because its proof sources and logs are currently host-local. First valid board claim owns ALL remaining round-2 Linux legs; no second host runs an independent repair/proof track. Claim the original assignment plus this addendum with idempotency key solr-18600-dual-release-behavior-closure-round-2@6f61b6dbaf963627fb4e9d4f479c9e5f6d9979d3. Refresh competing claims and the live candidate before beginning. Preserve the existing round-2 output paths. This is not a claim on behalf of vm2 or vm6.

## What the new receipt does and does not close

The additive receipt reports tidy, Error Prone, three SolrJ module checks, focused SolrJ/Jetty suites and core check passing on 6f61b6dbaf96. Reuse those results for the unchanged candidate with their exact commands, full logs and XML. It is a build/test gate PASS for those named legs, not acceptance of its residuals or proof of every promised behavior.

The two-cohort matrix in that receipt ran on b5261d29d171, before the two annotation/test-fix commits. The receipt states no descriptors changed; that is useful evidence but is not a recorded execution of both cohorts on the final artifacts. Run the SAME already-compiled, hashed cohorts against jars built from 6f61b6dbaf96 and publish their class/jar hashes, exact runtime dependencies, exit status and full outputs. Do not recompile consumers against the candidate. Reuse positive/negative controls when identity is verified. If code changes during behavioral repair, run the final matrix on the repaired head as part of its fresh gate instead of needlessly doing it twice.

Before another host works, publish portable vm2 proof sources/runners, expected outcomes, inventories, source/class/jar hashes and relevant outputs. They currently live under gates/logs/vm2/solr18600-additive/proof/; a bare local directory reference does not let another host reuse them. vm6's portable artifacts remain available, but they are a different fixture set and must not be silently substituted while retaining vm2's counts.

## Remaining discriminating work

Retain round-2 legs A-D, but assess each against the additive source and skip only legs demonstrably satisfied by matching evidence. In particular:

1. connect is still a residual in vm2's receipt, not an implemented no-op or a settled decision. Investigate the original round-2 behavior-preserving provider delegation, timeout/retry/interruption and closed-provider checks. No-op restoration has not been approved by Nick.
2. Custom-client rejection is concrete in this candidate: HttpSolrClient.asHttpSolrClient at lines 103-108 rejects a non-HttpSolrClient HttpSolrClientBase with IllegalArgumentException. Legacy ConcurrentUpdate builder constructors call it at lines 759 and 789; LB's legacy constructor calls it at 194. Test a valid 10.0-compiled custom base subclass and its release control. Merely replacing ClassCastException with an explicit argument rejection does not preserve that old constructor's behavior. Do not accept a new exception as a compatibility pass.
3. URL dispatch still reads serverBaseUrl in HttpSolrClientBase.getRequestUrl at lines 121-123, and there is no getRequestUrl override in the additive HttpSolrClient. The virtual getBaseURL behavior question remains. Require actual-destination assertions using the release-compiled subclass controls described in round 2.
4. The additive receipt explicitly limits requests to a closed-port exception and ownership to constructor/getter checks. Successful request and borrowed-client closure behavior remain unproved by that matrix. ZooKeeper descriptor coverage is not a runtime cohort.
5. Reconcile protected residuals by exact member descriptor and fields, including overridden-method dispatch. vm2's lister explicitly omits protected fields. Its 'six' review categories include Jetty overrides; do not treat a category count as the complete ABI member list or automatically accept the excluded extension surface.

The round-4 source/scope review remains separate from Linux execution: assignments/vm2-solr-18600-additive-review-round-4.md asks about residual acceptance, unrelated removals, target release line, permanent proof and upgrade-note ownership. This addendum makes no upstream acceptance or release-placement decision. A passing build cannot settle those questions, nor does it restore descriptors missing due to branch_10x drift from published 10.1.0. Any port must name its exact new base/head and obtain matching proof.

Next production step after behavioral closure and scope review: implement the reviewed repair, push it to the fork and gate its exact new head. Preserve valid 6f61b6dbaf96 evidence, but do not reuse it as proof for changed source. No changes to another worker's tree, no fork force-push, no public comment, JIRA update, PR or mailing-list action. Overall submission status remains HOLD.
