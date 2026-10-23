# SOLR-18446: owner-directed validation follow-up

Status: DONE (GREEN at 8bb60e6f61abc9827fb58dcae1863cabb33f9796)

Host phone-agent-2. Branch `solr-18446-path-routing-phone-agent-2`; fork head verified `8bb60e6f61abc9827fb58dcae1863cabb33f9796`. Base `b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb`. Supersedes the round-1 handback for new implementation validation only; prior failed attempts remain historical. Ticket SOLR-18446, category SolrCloud and ZooKeeper, secondary Search and query components; sibling SOLR-18470 excluded.

The owner explicitly requested follow-up patches. The missing HashSet import is already committed. This phase validates that new implementation head, not a retry of the terminal gate.

Diff scope: HttpSolrCall.java, HttpSolrCallCloudTest.java, AliasIntegrationTest.java, and the existing changelog fragment. Contract: path supplies candidates; collection may narrow within a multi-destination path, never redirect a singular path to another collection. No new 400 behavior.

Runner `/workspace/tools/phone-agent-2-hardening-gate.py`; config `/workspace/phone-agent-2-logs/gate-18446-patches-round-2.json`; logs `/workspace/phone-agent-2-logs/gate-18446-patches-round-2`. Shared host build lock; disk and inode guards before each Gradle invocation. JDK 21.

## Steps

- [x] Parse changelog YAML.
- [x] Root tidy with clean committed tree.
- [x] Error Prone core production and test compilation.
- [x] Pre-fix proof: retain new regression, restore only HttpSolrCall.java to base. Run exact testCollectionPathGovernsRouting method at seed 18446 and require expected collection1 versus actual collection2 assertion.
- [x] Restore head and run full HttpSolrCallCloudTest at seed 18446; archive fresh XML.
- [x] Core check excluding tests.
- [x] Record receipt and final phase outcome.

Gate repairs follow WORKFLOW: one scoped tree-changing repair and full retry; otherwise hand back. Do not weaken tests, edit prior terminal claim, post comments, open PRs or flip ready status.

## Outcome

All steps complete at the recorded head. Focused head leg includes both HttpSolrCallCloudTest (4) and AliasIntegrationTest (9): 13 tests, 0 skipped, 0 failures, 0 errors. Parsed proof XML establishes the intended single-path precedence failure; runner display-format matcher corrected and continued without repeating completed builds. Full details and preserved prior failed attempts are in receipts/SOLR-18446.md.
