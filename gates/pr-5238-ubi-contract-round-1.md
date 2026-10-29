# Gate: listener UBI contract proof, round 1

Status: OPEN

- Required tags: gate, review, records.
- Intended host: a capable Linux Solr gate host with JDK 21, the pinned Gradle distribution and dependencies; one gate at a time under its host lock.
- Source: Nick's direct request for a patch/tests on the author's branch after reviewing the proposed UBI adapter split.
- Fork branch: `nick-boss-tech/solr:solr-11994-pr5238-ubi-submit`.
- Exact candidate: `de4d222bb12302eb0e10aeb5697bae307eb2cd22`.
- Parent: PR 5238 head `aa03c9544968f1d9de9b6b36e2823bcbe76268b9`.
- Implementation claim: `claims/pr-5238-ubi-contract-windows-round-1.md`.
- Implementation report: `reports/pr-5238-ubi-contract-windows-round-1.md`.
- Receipt target: `receipts/PR-5238-ubi-contract.md` (owned by the executing gate host).
- Scope: one added `SearchRequestListenerUbiTest.java`, nine methods and a test-only preparation component/recording listener. No production changes or new dependencies/configset fixtures.

## Preflight and ownership

Read current board instructions, check that the job has no live competing claim or completed receipt, verify the fork head, and inspect the parent-to-candidate diff before claiming. Confirm the production Java diff against the parent is empty. Use a clean detached checkout, the supported runner, the host build lock and JDK 21. Record commands, timestamps, seeds, logs and fresh XML. Unset NODE_ENV for the root check and disclose that execution condition.

Run clean tidy at the exact head. If formatting needs repair, commit/push only the scoped repair on the fork, publish an add-only superseding job/head record and obtain all required proof for the new head. A dirty formatting run does not prove the old head. Never change the contributor's branch or an older client-dispatch gate.

## Required positive proof

Run the complete new class with two seeds, preserving separate logs/XML for each:

```sh
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.handler.component.SearchRequestListenerUbiTest -Ptests.seed=5238AB1
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.handler.component.SearchRequestListenerUbiTest -Ptests.seed=5238AB2
```

Each run must execute all nine methods with zero failures, errors or skips. Confirm the local/distributed ID and query-ID assertions, coordinator callback counts, disabled/unconfigured cases, destination exception with a later callback, malformed/partial query cases and copied embedded payload after closure. Do not replace cloud requests with fabricated SearchRequestInfo snapshots or remove the sensitive assertions to obtain green.

Run the author's complete existing listener suite:

```sh
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.handler.component.SearchRequestListenerTest -Ptests.seed=5238AB1
```

Expected at the parent plus this patch: 19 methods. The CPU timing test must execute, not skip. Record actual counts and investigate any discrepancy.

Run affected neighboring classes:

```sh
./gradlew :solr:core:cleanTest :solr:core:test --tests org.apache.solr.handler.PingRequestHandlerTest --tests org.apache.solr.core.TestSolrConfigHandler --tests org.apache.solr.request.TestSolrRequestInfo --tests org.apache.solr.response.transform.TestSubQueryTransformerDistrib -Ptests.seed=5238AB1
```

Finally require clean tidy and a successful root `check -x test` at the same exact head, in a clean full clone as required by the host runner. Do not silently exclude tasks. Record any environmental failure and repair before treating the final run as proof.

## Interpretation and failure decisions

This is a positive consumer-contract demonstration. It is not a fail-before gate: the production hook is identical to the author parent. Do not call a missing-API compilation failure or an intentionally broken consumer a reproduced bug in the author implementation. No public claim of a hook defect is authorized by this job.

If compilation/setup/tests fail, preserve the first logs/XML, trace the relevant configuration or consumer behavior, and repair within scope before rerunning the same proof. If an actual hook limitation is demonstrated, report the precise failed contract and minimal necessary change rather than importing the unfinished UBI feature. A production change requires a new reviewed head and a superseding gate.

Green means only that this test-only component/listener pattern works on the tested head. It does not prove PR 2452 integration, persistent sinks, core reload/unload handling, asynchronous lifetime, grouped/ID-omitting result handling, redaction or production overhead.

## Finish

Write an add-only execution report and the receipt with exact tested/pushed heads, clean-tree confirmation, all commands/results/seeds, test XML counts, logs and any repairs. Verify the remote fork head again. Keep public comments/body edits/PR actions for their separate authorization rules. Completion is derived from the report and receipt; do not edit this job or the Windows claim.
