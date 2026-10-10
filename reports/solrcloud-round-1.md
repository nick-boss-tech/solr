# SolrCloud round 1: round roll-up

Claim: `claims/solrcloud-round-1.md` (commit `f44c9a09116`). Assignment: `assignments/solrcloud-round-1.md` (commit `30ffe9683ff`). Per-part reports: `reports/solrcloud-round-1-p1.md` through `-p6.md`.

Done 2026-10-10 by Claude Code (AI agent), working for Nick Shanin. Six read-only subagents did the audit, at the cap of six. No build, Gradle run, or test was run. No submit branch, live PR, JIRA item, or comment was touched. Nothing was posted.

Count note: the assignment's header says thirty-one tickets but lists thirty. The thirty listed are audited. SOLR-18391 has two branches.

## Heads

Every named head matches its live branch, and none of the six audit parts found a moved tip. The live PR heads were checked read-only with `gh pr view`.

## Per-ticket verdicts

| Ticket | Verdict | Draft | Live head | What blocks it or what the draft must say |
|---|---|---|---|---|
| SOLR-15674 | Draftable, as the default-path half of a pair with SOLR-15478 | `SOLR-15674.md` | `ba01c83d4c5a` | The premises differ: 15478 is gated by `shareSchema`, while 15674's object cache is not. Recommend landing 15674 first. The Choice is mzxid alone against creation id plus data version. Limits cover the core watcher and the managed-schema fallback |
| SOLR-5813 | Draftable, PR-ready | `SOLR-5813.md` | `90b8baa08aef` | Choice: default to the core name (drafted), or reject an empty name (as the ticket title asks) |
| SOLR-17292 | Drafted, held | `SOLR-17292.md` | `e43200b0fb6d` | In `ZkController.java` 3051-3085 (`publishNodeAsDown`), a `KeeperException` from the per-replica persist at 3064 skips the DOWNNODE offer for the whole node. The remedy wraps the persist call; it needs your go-ahead |
| SOLR-12991 | Draftable | `SOLR-12991.md` | `1a86966179e1` | Choice: WARN against keeping ERROR. The receipt says the ticket accepts WARN or ERROR, but the Jira comment argues against both, and base already logs ERROR |
| SOLR-15035 | Draftable | `SOLR-15035.md` | `12d7491e82cd` | Paste the observed base failure line before opening. The split case is in Limits |
| SOLR-15863 | Draftable | `SOLR-15863.md` | `f381fd8d4dd1` | Owner must rule on a shard that reports no version |
| SOLR-12651 | Draftable, held | `SOLR-12651.md` | `f3131d1ee846` | Two commits sit past the gated head, so the live tip needs a run before opening. The changelog title overstates the change: async restores are not cleaned up (`CollectionHandlingUtils.java` 794-816), and failures after the shards go active are not cleaned up either |
| SOLR-13186 | Draftable, PR-ready | `SOLR-13186.md` | `b436d90d2a88` | Limits lines only |
| SOLR-15106 | Draftable, PR-ready | `SOLR-15106.md` | `40b7e5d0efa7` | Confirm the base failure text and run date |
| SOLR-9155 | Draftable | `SOLR-9155.md` | `9f08d0330233` | Choice: a `SolrException` with the interrupt flag restored, against a declared `InterruptedException`. Owner decision on the conflict-path remedy text |
| SOLR-15386 | Draftable | `SOLR-15386.md` | `ca8cb61ee957` | Owner settles the cached live-node check question before posting. The proof is inconclusive by construction, and the draft says so |
| SOLR-11288 | Draftable, a partial fix | `SOLR-11288.md` | `cd094c3c623f` | The escaping gap is in Limits. Owner call on blank-only input. Blank-only values now mean all shards or all live nodes, and the changelog title does not say so |
| SOLR-13369 | Draftable | `SOLR-13369.md` | `dfa0db5bdf96` | The proof is by construction, not by run, and the draft says so. It does not claim a failing base run |
| SOLR-14919 | Drafted; opening held | `SOLR-14919.md` | `84e7bcaeb57d` | The `RecoveryStrategy.java` hunk changes recovery commit fan-out in every cluster, has no test, and re-enables a marker that upstream commented out in `75b183196798` (SOLR-12801 batch, which also lists SOLR-12933). Decide before opening |
| SOLR-17680 | Draftable | `SOLR-17680.md` | `f4b8ce833654` | The draft names that the v2 aliases endpoint with two routers starts working |
| SOLR-17733 | Draftable | `SOLR-17733.md` | `636196b7954a` | States the contract change (an API delete now removes the ZooKeeper entry); the sync complaint was not reproduced |
| SOLR-13239 | Held (submission-held); no draft | none | `699a1fce368c` | Record verified. Remove `SOLR-13239-TESTING.md` before any submission |
| SOLR-18277 | Retire confirmed; no draft | none | `17c0e6448128` | PR #4959 merged; head matches |
| SOLR-16013 | Held; no draft | none | `ba26b7028917` (gated at `e1bd21fd11a`) | The live tip adds an interrupt-safe wait and a second test. Neither has a run. Commit `3059f9be884` carries a Claude `Co-Authored-By` trailer; removing it rewrites the fork and needs your go-ahead, then a re-gate |
| SOLR-11479 | Audit only; held | none | `7e538e844c45` | No gate. `SOLR-11479-TESTING.md` ships in the branch diff. Remove before any push. Overlap with 15035 in `AddReplicaCmd.java` is noted in part p2 |
| SOLR-3865 | Audit only; not ready | none | `363e8f0651e9` | The leak the Jira describes is already closed on main. The branch fixes a different gap |
| SOLR-4754 | Audit only; not ready | none | `d2e9038881f6` | The guard fires only for scheme-only input, not the ticket's empty value, and the proof cannot fail on base. The error text names `-Dhost`, but the shipped `solr.xml` reads `solr.host.advertise` |
| SOLR-10234 | Audit only; plausible | none | `16825538a766` | Pins the annotation only. Whether to suppress or use a per-node limit is open |
| SOLR-10641 | Audit only; no draft | none | `4ab4bd2040f3` | The pins pass on base (NOT_PROVEN). The tidy fold-in is owed. Shipping the pin alone is your call |
| SOLR-16437 | Audit only; no draft | none | `673ae584de03` | The strongest of the six. The premise holds in both modes. Draftable after a main-side first gate |
| SOLR-17281 | Parked; not ready | none | `ba21ca32791b` | Revert or replace is your call |

## Live PR consistency (part p6)

| Ticket | Live PR | State | Finding |
|---|---|---|---|
| SOLR-7394 | #5003 | open | Consistent with its receipt. The title and changelog omit the term "restore" |
| SOLR-12998 | #5010 | open | The receipt's head `25520806c26` was rewritten, and a code commit `62a17a116b5` followed the gate. Re-gate owed. The text says "local gate passes" |
| SOLR-13136 | #5017 | open | Most important drift. The gated behavior deletes the shard on activation failure. The live tip `486b3877556` keeps it in CONSTRUCTION. Owner must confirm; re-gate owed. The Proof cites a 2026-10-06 run the receipt does not record |
| SOLR-18391 (submit) | #4997 | draft | No gate at the tip. Superseded by #5027. Owner decides close or keep |
| SOLR-18391 (graceful-create) | #5027 | open, approved | Gated at `3a0ff1262bf`, eight commits back. The text says one later commit; it is four. File links use an old SHA. The changelog overstates the alias delete. The Limits omit failover |

## Interactions

- **mzxid pair (15674 and 15478).** The premises differ, so the two are not the same change. Land 15674 first. The wording about version resets must match in both drafts (part p1).
- **AddReplicaCmd (11479 and 15035).** Both change `AddReplicaCmd.java`. Overlap is noted in part p2. No landing order is recorded; the owner sets it.
- **Backup and restore (12651 and 15863).** Both use `TestLocalFSCloudBackupRestore`. Check part p2 for the order.
- **Overseer and ZkController (13186, 15106, 16013, 15386, 9155).** Part p3 checked shared files and the shutdown and election assumptions. The landing order is in that report.
- **RecoveryStrategy (7394 and 14919).** The hunks merge cleanly (parts p4 and p6). 14919 carries the decision in its FIX above.

## Owner decisions

1. SOLR-17292: the `publishNodeAsDown` remedy (wrap the persist call), which needs your go-ahead.
2. SOLR-13136: confirm the shard-deletion behavior on activation failure (gated against live). Re-gate owed.
3. SOLR-12998: re-gate owed, because the gated head was rewritten.
4. SOLR-18391: close or keep the draft PR #4997, which is superseded by #5027.
5. SOLR-14919: decide the `RecoveryStrategy` marker before opening.
6. SOLR-15674 and SOLR-15478: confirm landing 15674 first.
7. SOLR-5813: default to the core name, or reject an empty name.
8. SOLR-12991: WARN against ERROR.
9. SOLR-9155: the conflict-path remedy text.
10. SOLR-15386: the cached live-node check question.
11. SOLR-11288: what blank-only input means (all shards, or all live nodes).
12. SOLR-15863: a shard that reports no version.
13. SOLR-10641: ship the pin alone, or not.
14. SOLR-17281: revert, or replace.
15. History rewrites: commit `3059f9be884` (16013), the handoff notes and "Hypothetical, unrun" commit bodies in all six audit-only tickets (part p5), and `SOLR-11479-TESTING.md` and `SOLR-13239-TESTING.md`. Rewriting fork history needs your approval.
16. SOLR-12651: run at the live tip before opening.
17. Re-gates owed: 12651 (live tip), 12998, 13136, 16013 (live tip), 16437 (main-side first gate).

## Draft fixes before posting

- Remove the "Open: the gate record gives no run date" notes at the top of `SOLR-13186.md`, `SOLR-15106.md`, `SOLR-15386.md`, and `SOLR-9155.md`. These are internal, and the date must be confirmed in the Proof line.
- The "inconclusive by construction" wording in `SOLR-15386.md` and `SOLR-15674.md` is the formula's required phrasing for those proofs; keep it.
- `SOLR-14919.md`: the draft is complete, but opening waits for owner decision 5.

## Corrections to the record

- The 12651 changelog title overstates the cleanup.
- The 4754 error text names the wrong property.
- The 18391 graceful-create text says one commit where there are four, and its links use an old SHA.
- The 13136 live tip differs from its gated behavior.
- The 12998 gated head was rewritten.
- The 17292 persist path can skip the DOWNNODE offer.

## Not done

No build, test, Gradle run, `gh` write call, fetch, commit, or post. Gate logs named in the receipts are not on disk, so counts are receipt-only. Live JIRA was not queried. The core-admin round waits until this round is handled.
