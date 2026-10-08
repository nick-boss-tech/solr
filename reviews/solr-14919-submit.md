# solr-14919-submit

- Branch: origin/solr-14919-submit
- Head: 84e7bcaeb57 (matches the listed head; fork tip unchanged after fetch 2026-10-08)
- Base: upstream/main at 9d7cc2884e8a (merge-base b5c71bc5573c, 46 commits behind, 2 commits ahead)
- Scope: 2 commits, 4 files. `RecoveryStrategy.java` (`commitOnLeader` now sets `COMMIT_END_POINT=true` on the recovery commit; the line was commented out before), `IgnoreCommitOptimizeUpdateProcessorFactory.java` (new `isInternalEndpointCommit`, used by `processCommit`: accepts a boolean-true prefix or the exact values `leaders` and `replicas`), `IgnoreCommitOptimizeUpdateProcessorFactoryTest.java` (processor-level tests), changelog `SOLR-14919.yml` (`type: fixed`).
- Verdict: Nearly (unchanged from the bulk verdict). The recovery fix and the forwarded-endpoint handling match what the internal code emits. Whether the marker is an enforcement boundary is an owner call, and today a client can set it, so that call decides whether this is ready.
- Reviewer: claude-haiku-5-5 (Claude Code), round 36 Group B review, 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. Every claim rests on reading the diff and the code at the listed head. The JIRA packet was not available locally (the bulk review notes the connector returned only a summary), so the intended behavior comes from the code and the changelog.

## Delta check against the bulk review

Bulk review `research/branch-reviews/round-28/SOLR-14919-review.md` (Nearly) reviewed this head (`84e7bcaeb57`). No delta.

- Bulk F1 (MEDIUM, caller-supplied endpoint strings are not proof of an internal request): **confirmed.** The processor reads only `cmd.getReq().getParams()`. Refined: the `true` form was already accepted from clients before this branch. The branch adds the exact values `leaders` and `replicas` as new bypass values, and switches to a lenient parse. See finding 1.
- Bulk note (the trust weakness is inherited, so not a new boundary): **partly confirmed.** The `true` bypass is inherited. The two new spellings are not. The bulk's "not a new boundary" statement does not cover them. See finding 1.
- Bulk test point (the test proves processor acceptance, not the public update-handler boundary): **confirmed.** See finding 5.

## Findings (ranked)

1. **MEDIUM, verified (code path). A client can set the internal marker and bypass the configured reject.** `isInternalEndpointCommit` (`solr/core/src/java/org/apache/solr/update/processor/IgnoreCommitOptimizeUpdateProcessorFactory.java`, added in `processCommit`'s helper block) returns true for `StrUtils.parseBool(endPoint, false)` or for `leaders` or `replicas`. `processCommit` (`:133`) then passes the commit to `next` and skips the configured `errorCode` reject (`:142-146` at head). Nothing ties the parameter to an internal request. The distributed processor does set these values on forwarded commits (`DistributedZkUpdateProcessor.java:215-218`, `:235`), and the recovery commit now sets `true` (`RecoveryStrategy.java:304`). Both are settable from an HTTP commit request as well.
   - Inherited: `commit_end_point=true` (and the `on…` and `yes…` prefixes). The base used the strict `SolrParams.getBool`, which accepts the same prefixes (`StrUtils.parseBool(String)`, `StrUtils.java:267-277`).
   - New in this branch: exact `leaders` and `replicas`, and the lenient `parseBool(String, boolean)` (`StrUtils.java:286-297`), which matches by prefix. A value such as `onboard` or `yesterday` also passes.
   - Verified by reading. Not exercised through an HTTP request.
   - Proposed fix (not applied; owner call 1): a provenance mechanism that client parameters cannot set. For example, a request-context attribute set only by the internal forwarder and the recovery path, and checked here. Until then, document the marker as a convenience and not an enforcement control.

2. **LOW, verified. Unknown values no longer return 400.** The base `getBool` called the strict `StrUtils.parseBool(String)`, which throws `BAD_REQUEST "invalid boolean value"` for an unknown marker (`StrUtils.java:277`). The branch's helper returns false for an unknown value, so the commit goes to the configured reject path instead. The comment at `IgnoreCommitOptimizeUpdateProcessorFactory.java` describes the `leaders` and `replicas` 400 this avoids, but the unknown-value behavior change is not in the changelog. Minimal fix: keep the strict parse for anything that is not `leaders` or `replicas` (see proposed fixes).

3. **LOW, verified (checked, no functional issue). The new spellings are the ones the code emits.** `DistributedZkUpdateProcessor` sets `leaders` when it forwards a commit to the leader (`:215-218`) and `replicas` for the replica-side forward (`:202`, `:235`). Accepting exactly those two strings is what the forwarding path needs. Fine as a functional fix. The issue is the trust boundary (finding 1), not the values.

4. **LOW, verified (checked, no issue). The recovery commit restores the marker.** `RecoveryStrategy.commitOnLeader` now sets `COMMIT_END_POINT=true` (`:304`), replacing a commented-out line. This is the change that lets the leader's processor pass the recovery commit through. Read, not run.

5. **LOW, verified. Test coverage stops at the processor.** `IgnoreCommitOptimizeUpdateProcessorFactoryTest` sets the marker on the request object directly (`:55-68`, and the helper at `:100`). Nothing sends an external commit with `commit_end_point=leaders` through an update handler. That is the case finding 1 is about, so it is the one the test should cover.

## Owner calls (not decided here)

1. **Is the commit marker a boundary?** The chain is named `ignore-commit-from-client-403`, which suggests the intent is to refuse client commits. If so, the marker is not a boundary today: `true` was already client-settable, and this branch adds two more values. The branch then needs a provenance mechanism (finding 1) before it ships. If the marker is only a convenience so recovery commits are not rejected, document that, keep the change, and accept that clients can set it. The review does not choose between these.
2. **Which values should be accepted at all?** If the owner keeps the parameter-based marker, decide whether `leaders` and `replicas` should be accepted from clients. Under option 1 that is a no.

## Proposed fixes (not applied; the owner decides)

- Finding 1: add a request-context marker that only the internal forwarder and the recovery path set, and have `processCommit` check that instead of the parameter. Add a test that an external commit with `commit_end_point=leaders` (or `true`) is rejected by the configured chain. If option 1 is "convenience only", write that in the changelog and in the processor's Javadoc.
- Finding 2: keep strict parsing for every value other than `leaders` and `replicas`. For example:

  ```java
  String endPoint = params.get(DistributedUpdateProcessor.COMMIT_END_POINT);
  if ("leaders".equals(endPoint) || "replicas".equals(endPoint)) return true;
  return params.getBool(DistributedUpdateProcessor.COMMIT_END_POINT, false);
  ```

  This keeps the base's 400 for unknown values, and it still avoids the 400 for forwarded `leaders` and `replicas`.
- Finding 5: an update-handler-level test for an external commit with each marker value.

## Not checked

- Nothing compiled, formatted, or run.
- Whether any layer upstream of the processor strips or overrides `commit_end_point` on client requests was not traced. Finding 1 assumes it does not, based on the processor reading the request params directly.
- Whether the Solr reference guide documents this processor as a security control was not checked.
- The JIRA packet was not available beyond the summary; the intended behavior comes from the code and the changelog.
- The recovery flow that depends on the leader accepting the `true` marker was read only as far as the processor.
