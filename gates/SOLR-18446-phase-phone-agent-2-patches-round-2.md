# SOLR-18446: owner-directed validation follow-up

Status: CLAIMED (claims/phone-agent-2-blocker-patches-round-2.md)

Host phone-agent-2. Branch `solr-18446-path-routing-phone-agent-2`; fork head verified `8bb60e6f61abc9827fb58dcae1863cabb33f9796`. Base `b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb`. Supersedes the round-1 handback for new implementation validation only; prior failed attempts remain historical. Ticket SOLR-18446, category SolrCloud and ZooKeeper, secondary Search and query components; sibling SOLR-18470 excluded.

The owner explicitly requested follow-up patches. The missing HashSet import is already committed. This phase validates that new implementation head, not a retry of the terminal gate.

Diff scope: HttpSolrCall.java, HttpSolrCallCloudTest.java, AliasIntegrationTest.java, and the existing changelog fragment. Contract: path supplies candidates; collection may narrow within a multi-destination path, never redirect a singular path to another collection. No new 400 behavior.

Runner `/workspace/tools/phone-agent-2-hardening-gate.py`; config `/workspace/phone-agent-2-logs/gate-18446-patches-round-2.json`; logs `/workspace/phone-agent-2-logs/gate-18446-patches-round-2`. Shared host build lock; disk and inode guards before each Gradle invocation. JDK 21.

## Steps

- [ ] Parse changelog YAML.
- [ ] Root tidy with clean committed tree.
- [ ] Error Prone core production and test compilation.
- [ ] Pre-fix proof: retain new regression, restore only HttpSolrCall.java to base. Run exact testCollectionPathGovernsRouting method at seed 18446 and require expected collection1 versus actual collection2 assertion.
- [ ] Restore head and run full HttpSolrCallCloudTest at seed 18446; archive fresh XML.
- [ ] Core check excluding tests.
- [ ] Record receipt and final phase outcome.

Gate repairs follow WORKFLOW: one scoped tree-changing repair and full retry; otherwise hand back. Do not weaken tests, edit prior terminal claim, post comments, open PRs or flip ready status.
