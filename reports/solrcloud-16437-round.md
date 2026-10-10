# SOLR-16437: first gate, receipt check and draft (round roll-up)

Claim: `claims/solrcloud-16437-draft.md` (commit `5444148ed32`). Trigger: `pr-prepare` moved to `9125af019ec`, which refreshed `receipts/SOLR-16437.md` from NO GATE to GATE GREEN at the packaged head `aa2a6b8afb6f`. Parts: `reports/solrcloud-16437-receipt-check.md` (part a) and `reports/solrcloud-16437-draft.md` (part b).

Done 2026-10-10 by Claude Code (AI agent), working for Nick Shanin. Two subagents, one per part, within the cap of six. No build, Gradle run, or test was run. No branch, receipt, live PR, JIRA item, or comment was touched. Nothing was posted.

## Verdicts

- **Gate: usable for drafting. Not cleared for push or PR open.** The gated head matches the live branch. The only packaging change is deleting `SOLR-16437-TESTING.md`. The premise holds by reading. The trial merge with `upstream/main` is clean. The receipt's counts and Proof are recorded as the receipt's numbers, because the gate log is not on disk.
- **Draft: written** at `pr-drafts/solrcloud/SOLR-16437.md`, naming head `aa2a6b8afb6f`.

## The draft

- Scope: ADDREPLICAPROP only. DELETEREPLICAPROP and the other APIs the ticket names are in Limits, with a follow-up offer.
- The behavior change is stated: a valid replica name given with the wrong shard is now rejected with HTTP 400. Before, the call returned success and did not set the property on that replica. For preferredleader, it also cleared the property on the replicas of the named shard.
- Proof names `testAddReplicaPropRejectsUnknownReplica` and one failure with the change reverted. The class counts (25 tests, 1 skipped) are not in this draft, as the adopted answer requires.
- Checks: no dashes, no process words, no old head references, every head reference names `aa2a6b8afb6`, and the section order follows `pr-formula.md`.
- Length: 5,035 characters with link URLs (3,218 without). The guide is about 3,500. Not trimmed.

## Owner decisions

1. **Commit `b7d642d4caa` body (blocks the push).** Its body says "Hypothetical, unrun regression test; see SOLR-16437-TESTING.md." The adopted packaging rule says to remove it, and the packaging commit did not. Removing it rewrites a fork branch and moves the head, but not the tree. If you approve, the receipt head and any commit-based links in the draft are refreshed after the rewrite. The gate stands on the tree.
2. **Subject lines with process words.** `4fdcad1dc58` ("add hypothetical-reproduction handoff doc") and `aa2a6b8afb6` ("remove the handoff note"). The adopted rule keeps subjects. Say if you want them changed. That also needs the rewrite in item 1.
3. **Choice section** (draft lines 27 to 31). It asks whether rejecting the wrong shard is right, against keeping the lookup across shards. Keep it, or delete those lines.
4. **Cached-state limit.** The check reads this node's cached cluster state, so a replica added moments earlier may be invisible and a valid request could get 400. Not tested. Add the optional Limits line from part b, or leave it out.
5. **Length.** The draft is 5,035 characters with URLs. The largest cut is the Overseer and distributed-mode citation sentences in What happens today (about 470 characters).
6. **Wording.** Line 37 says "I can open a follow-up ticket and PR ... on request". Other drafts say "A follow-up ticket and PR can be opened on request." Line 38 says "were not checked", which is process wording in public text.

## Owed on the main side

- The gate log `g16437-gate.log` is not on disk. The receipt does not name the tidied commit (`673ae584de0` is the only candidate).
- The source of "1 skipped" is not identified. The file has no `@Ignore`.
- Other `ADDREPLICAPROP` callers were not in the gate run: `TestCollectionAPI`, `TestReplicaProperties`, `TestRebalanceLeaders`, `TestPullReplica`, `TestTlogReplica`, and `AddReplicaPropertyAPITest`. The sampled calls name replicas inside the shard they give, so the new check should not reject them. A run of those classes at the head is needed before opening.
- Per-replica-state collections were not read.

## Owed in earlier files (next round's claim)

- `material/solrcloud-round-1-answers.md` line 168 and `reports/solrcloud-round-1-p5.md` line 79 say a wrong-shard request "changed the replica in the other shard". That is wrong on base. The draft's wording is right. These two lines need correcting under a claim of their own.

## Not done

- No build, Gradle run, or test. No `gh` write call. No commit to a submit branch. No live PR edit. No JIRA access.
- The changelog YAML was read by eye and not parsed by a tool.
- No history rewrite. Item 1 is waiting for your decision.
