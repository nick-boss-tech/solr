# Receipt refresh round 2 (SOLR-12651 and SOLR-17292): round roll-up

Claim: `claims/receipt-refresh-round-2.md` (commit `50734605cdf`). Trigger: `pr-prepare` moved to `a266ae9c509`, which refreshed `receipts/SOLR-12651.md` and `receipts/SOLR-17292.md` with no new assignment or material file. Parts: `reports/receipt-refresh-12651-17292-a.md` (SOLR-12651) and `reports/receipt-refresh-12651-17292-b.md` (SOLR-17292).

The claim names the files `receipt-refresh-round-2-a`, `-b` and this roll-up. Those names were already taken by the SolrCloud answers round's reports (`reports/receipt-refresh-round-2.md`, `-b1.md`, `-b2.md`), so this round's files carry the `12651-17292` slug instead.

Done 2026-10-10 by Claude Code (AI agent), working for Nick Shanin. Two subagents did the read-only checks and the draft edits, one per ticket, within the cap of six. No build, Gradle run, or test was run. No branch, receipt, live PR, JIRA item, or comment was touched. Nothing was posted.

## Mid-round correction

The claim's worktree had the old receipts when the subagents started, because the round's base was `27f34310ce0`. The lead rebased the unpushed claim onto `a266ae9c509`, which brought in the refreshed receipts, and sent both subagents a correction. Both then re-read the refreshed receipts. Both reports say all their findings are against the refreshed text. The drafts were not changed by the rebase.

## Heads

| Ticket | Branch | Live head | Receipt head | Result |
|---|---|---|---|---|
| SOLR-12651 | `solr-12651-submit` | `f3131d1ee846` | `f3131d1ee846` | matches |
| SOLR-17292 | `solr-17292-submit` | `f614a42fbc80` | `f614a42fbc80` | matches |

## Verdicts

- **SOLR-12651: draftable after edits.** The receipt matches the branch. The two commits since the older gate (`5fb9fb01717`, `f3131d1ee84`) are confirmed with `git log 90032e274b7..f3131d1ee846`. The draft is edited to the live head and to one claim, and the stale "run is owed" text is gone.
- **SOLR-17292: draftable after edits.** The in-branch remedy is in the code at `f614a42fbc80`. The draft is edited to the new head, the remedy is described, and the node-down behavior is stated as it now is.

## Draft edits

`pr-drafts/solrcloud/SOLR-12651.md` (by part a):
- The bold summary is narrowed to "A RESTORE that throws an error after it creates the new collection leaves that collection in place." The base code cleans up when replica creation reports a failure, so "fails" was too broad.
- The body sentence changed from "fails" to "throws an error" for the same reason, and the "copied data that cannot be read" phrase is gone.
- The Proof summary now states one claim: "The restore test fails on the base code and passes with this change." The counts stay in the bullet.
- The Proof bullet names `f3131d1ee846`, the 2026-10-10 date, and the one failure with `RestoreCmd.java` at the merge-base.
- The "owed" line and the old head are deleted.
- Length: 3,246 characters with links, under the guide. Dashes: none. Process words: none.

`pr-drafts/solrcloud/SOLR-17292.md` (by part b):
- All nine references to the old head `e43200b0fb6` now name `f614a42fbc80`.
- The Choice-like sentence on call sites now links the `PerReplicaStatesOps.persist` range at base and says callers that relied on the silent return now get the exception.
- The node-down bullet now cites `ZkController.java` lines 3058 to 3068. It says the node-down message is now sent after any persist KeeperException, where at base a lost connection skipped it. The first draft of this sentence said the outcome was unchanged, which was wrong. The agent corrected it before reporting.
- The Proof bullet names `f614a42fbc80` and the 2026-10-10 date, and adds the one-failure revert sentence. The "owed" note is gone.
- The Limits line on the duplicate ERROR now says which log line is which.
- Length: 4,808 characters with link URLs, 3,071 without. The guide is about 3,500, so the draft is over only when the URLs are counted. Not trimmed.
- Dashes: none. Process words: none.

## Owner decisions

1. **SOLR-17292: widened node-down behavior.** At `f614a42fbc80`, the node-down offer is sent after any persist KeeperException, for example a lost ZooKeeper connection. At base it was skipped. The draft states this openly. The question is whether that is the behavior you want, or whether the remedy should catch only the stale-state case. If the remedy changes, the branch moves and the draft is re-headed.
2. **SOLR-17292: length.** 4,808 characters with URLs. Trim, or accept for a complex ticket.
3. **SOLR-12651: changelog title overclaims (branch).** The title in `changelog/unreleased/SOLR-12651-restore-cleanup-on-failure.yml` says a RESTORE that "fails after the new collection has been created now deletes the new collection". At head, failures in `addReplicasToShards` and `restoringAlias` come after the catch and do not delete the collection. Suggested title (needs a branch commit): "A RESTORE collection operation that fails before its restored shards become active now deletes the new collection instead of leaving the partially restored collection behind." A title change moves the head, so the draft is re-headed.
4. **SOLR-12651: optional Limits sentence.** On the async path with a user async id, a shard copy failure is recorded and the restore appears to continue. This was read, not run, and it is unchanged from base. Add a Limits sentence, or leave it out.
5. **SOLR-17292 and SOLR-12651: base SHA.** Both reviews used the merge-base `14c7aac0d151`. upstream/main is now `8e62c2686882`. Confirm `14c7aac0d151` is still the intended base. The receipt for 12651 does not name its base.

## Main-side work owed (not ours to do here)

- **SOLR-12651:** the gate logs (`g12651-livetip-gate.log`, `g12651-gate2.log`, `g12651-premise.log`) are not on disk, and the JUnit XML is not either. Record the failing assertion in the receipt, because the copy-failure check comes before the property-upload check and the receipt does not say which one failed. Name the base SHA.
- **SOLR-17292:** the gate logs (`g17292-regate.log` and the VM-killed attempt log) are not on disk. Correct the receipt's "the duplicate ERROR log line stays": the node-down caller logs at WARN, so the second ERROR is the Overseer loop, not the same call site.

## Not done

- No build, Gradle run, or test. No `gh` write call. No commit to a submit branch. No live PR edit. No JIRA access.
- The changelog YAML for both tickets was read by eye and not parsed by a tool.
- The receipts were not edited. Receipt changes are the main side's.
