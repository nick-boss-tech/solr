# SolrCloud answers pass, part a2: draft corrections for items 6 and 7

Result: all nine drafts in this part have a bold Limits opener. SOLR-17292 now describes the adopted node-down remedy (the Choice is removed, and the Proof says a run at the current head is owed). SOLR-15674 is 4,540 characters after the opener (4,419 before), left as is. No DISCUSS position changed.

Written from the subagent's hand-back text, which the subagent could not save as a file. The changes are listed as the subagent reported them.

## Changes

- `SOLR-5813.md`: added the opener "The SOLR-5811 path is untouched, and the core.properties effect has no test."
- `SOLR-11288.md`: added the opener "Escaping is not added, and only CLUSTERSTATUS has a direct test." Limits bullet 1: cut "This change does not add escaping." Bullet 2 changed from "The test covers CLUSTERSTATUS only. BALANCE_REPLICAS and MIGRATE_REPLICAS have no direct test..." to "BALANCE_REPLICAS and MIGRATE_REPLICAS have no direct test...". The blank-only DISCUSS wording is unchanged.
- `SOLR-12991.md`: added the opener "Only two catches change, each retry logs more text, and the test simulates the leader rather than stopping it."
- `SOLR-13369.md`: added the opener "The test cannot catch a routing bug that both the router and the test share." The bullet was shortened to end at "...search."
- `SOLR-14919.md`: added the opener "Any client can send these markers, and the distributed path has no cluster test." The Choice is untouched (DISCUSS).
- `SOLR-17292.md`:
  - The lead sentence now reads "now see the failure, at these call sites:".
  - The node-down bullet now reads "the failure is logged as a warning, and the node-down message is still sent".
  - The Proof reads "TestPerReplicaStates passed 4 of 4 at head e43200b0fb6 on 2026-10-04. That run is before the node-down change, so a run at the current head is owed before opening."
  - The "A choice to check" section is removed.
  - Limits opener: "The test covers persist alone, and the callers and sustained contention are not tested."
- `SOLR-17680.md`: added the opener "No cloud test covers the v2 endpoint, and clients that match the old error text need an update."
- `SOLR-17733.md`: added the opener "A failed remote or local delete still returns success, and the test covers only the normal path." The bullet "The test covers the normal path, not a node failure." is removed.
- `SOLR-15674.md`: added the opener "Three paths are outside this change: the configset cache, the core config watcher, and the managed-schema fallback." Length is 4,419 characters before and 4,540 after. Over the 3,500 guide. Not trimmed, since the answers do not direct a trim.

## Not done

1. The SOLR-17292 remedy is not in the branch. The head is still `e43200b0fb6`. The remedy needs a commit and a re-gate (main side). The draft must not open until then. After that commit, the draft's links, which are pinned to `e43200b0fb6`, need re-pinning.
2. The SOLR-17292 Proof needs a run at the post-remedy head (a gate).

## Notes, not changed

- SOLR-17680's Proof says "(10 before, 11 now)". The receipt says only "the pinned message count". 11 matches `CreateAliasAPITest.java` line 308 at `f4b8ce83365`. 10 is not in the receipt or any run. Left as drafted.
- SOLR-15674 says "ZooKeeper does not reuse this ID, so a recreated znode gets a new cache key". SOLR-15478 line 17 says "recreated configset". The version-reset sentence matches 15478 line 9. Not edited. The lead decides whether the pair must match exactly.
- Where a bullet repeated the opener word for word, it was cut. Other bullets are kept, so some overlap remains (5813, 12991, 14919, 17680, 15674).

## Checks

- No em or en dashes in the nine drafts.
- No process words (gate, receipt, ledger, handoff, top-up, takeover, audit, rc=0, JUnit XML, pre-fix, premise, hardened, hypothetical, round N, owner) after the edits.
- Proof counts match the receipts for all nine. Dates for 17292, 15674, 17680 and 17733 match the answers file. 5813, 11288, 12991, 13369 and 14919 match their receipts.
