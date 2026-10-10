# Configsets and config API round 1: round roll-up

Claim: `claims/configsets-round-1.md` (commit `4c8c3614c87`). Assignment: `assignments/configsets-round-1.md` (commit `ecf98d6e72a`). Per-part reports: `reports/configsets-round-1-a.md`, `reports/configsets-round-1-b.md`, `reports/configsets-round-1-c.md`.

Done 2026-10-09 by Claude Code (AI agent), working for Nick Shanin. Three read-only subagents did the audit. No build, Gradle run, or test was run. No submit branch, live PR, JIRA item, or comment was touched. Nothing was posted.

Drafts in `pr-drafts/configsets/`:
- `SOLR-6960.md`, written at `9bef536fc12`. Held on one branch fix (below).
- `SOLR-7267.md`, written at `59f34a339b0`.
- `SOLR-15478.md`, written at `0478bdf0ac5`. Held on owner checks.
- `SOLR-17363.md`, written at `b8e8e1c4846`. Held on an owner scope call and a receipt correction.

## Heads

All seven branches match the heads the assignment names, except `solr-7323-submit`. That branch moved from `fb034dc5f87` to `1f5b0f2c82d` during this round. The move removes the testing note. The assignment named no head for SOLR-7323, so the audit covers `1f5b0f2c82d`.

## Per-ticket verdicts

**SOLR-6960: draftable, one branch fix first.**
- The top-up receipt landed during this round (`75570244670`). It records TestInitParams 9 of 9 and TestSolrConfigHandler 8 of 8 at `9bef536fc12`, dated 2026-10-09. I updated the draft's Proof to those counts.
- The new second test has no recorded run without the change. The draft says so and does not claim it fails without the change.
- **Branch FIX (owner).** The changelog title on `solr-6960-submit` says the whole requestHandler section now reports initParams. Built-in handlers are not covered, and the ticket's own example, `/update/json/docs`, is one of them. I checked this: `ImplicitPlugins.json` L28 lists the handler. Suggested title: "The /config API now reports the defaults, appends and invariants that initParams add to request handlers defined in solrconfig.xml." The draft's Limits already says built-in handlers are not covered.
- The round 28 review repeats the overstatement. That review file is on disk under `research/`, not on the branch.
- Also noted: `RequestHandlers.applyInitParams` is public, and only package code calls it. Owner decision whether to narrow it.

**SOLR-7267: draftable.**
- The live tip differs from the gated tree only by a removed note file. No code changed after the gate.
- The Choice wording comes from the receipt's one-line description. The review it names (`goal files/reviews-2026-10-06-round28-fresh-arrivals/7267.md`) is not on disk. **Owner check** of the Choice wording before any use.
- The Limits line that cites LUCENE-6366 comes from the ticket's one comment. Owner may keep or drop it.

**SOLR-7323: held.**
- No gate and no premise run exist. Main side runs the premise and the first gate at `1f5b0f2c82d` before any draft.
- The receipt's line 4 and the inventory row (`branch-focus-inventory-2026-10-08.md`, line 136) are stale. Both still say the testing note is on the branch. The row also lists 4 files; the branch changes 3.
- Message change, by reading: it names the configset, the base path, and `configSetBaseDir`. The new test should fail on base by reading. Not run.
- Owner decision: the error text now repeats the server path, because the old message already printed `configSetDirectory`.

**SOLR-15478: draftable, two owner checks before posting.**
- Premise, by reading: the cache key uses the znode data version, which restarts at 0 for a recreated node. The fix returns `mzxid`. The premise applies only when `shareSchema` is on, and that is off by default (`NodeConfig.java` L606). The Jira packet does not say the reporter had it on.
- The receipt says the pre-fix step passed, but does not name the failing assertion. The log is not on disk. The draft says only that the test fails on the base code. **Owner check:** confirm the failing line from the log, and confirm the reporter's `shareSchema` setting.
- The test does not reach the schema cache (it builds `ZkConfigSetService` with no cache). The Limits say the test checks the version value only.
- Pairing with SOLR-15674 is noted, not audited.

**SOLR-17363: held.**
- **Receipt FIX, verified by me.** Receipt line 3 says "production code is untouched". It is not. `solr/core/src/java/org/apache/solr/handler/SolrConfigHandler.java` changed by 69 lines, against the merge-base `56ec140e3636` with upstream main. Production Java is under `solr/core/src/java`, so the earlier `src/main` check missed it. Replacement for line 3 is in the part B report, finding 1.
- **Owner decision, stale replica.** An active replica that never reports the new version in time still fails the request. The options are: (a) submit as drafted, with the gap as a named Limit; (b) hold until the slow-reload case has its own change and tests; (c) submit as drafted and open a follow-up only if asked.
- **Owner decision, down or recovering replicas.** The draft's choice section excuses them too. Keep that, or narrow the change to removed replicas only (which needs code and tests).
- Receipt line 8 should also name replicas on a node that is no longer live. The draft's scope matches the code.
- The named gate logs for 17363 are not on disk. The only on-disk run (2026-10-03) predates the head, so the counts are receipt-only. The receipt's Error Prone claim has no log.

**SOLR-13706 (live PR, consistency pass): drift and conflict, owner action on the live PR.**
- Head matches `590dd5c24d9`.
- **Verified by me, read only:** `gh pr view` shows the PR open, not a draft, `mergeable: CONFLICTING`, `mergeStateStatus: DIRTY`. The part C simulation found one conflict, in the upgrade-notes `.adoc`, where both sides add a section at the same spot. The assignment calls the PR ready. GitHub does not.
- The live Proof cites a GitHub run at `b062f3b1ffd`. `PluginInfo.java` changed after that head. The receipt says the run was at the live head. Owner must confirm.
- The live body's base-code counts (8 and 4 failing executions) are not in the receipt.
- The live body says `TestSolrConfigHandler` navigates children by type. It does not, by the part C reading.
- The live body says "a separate ticket follows", which conflicts with the offer rule. Owner decides the wording.

**SOLR-18178 (live PR, consistency pass): drift, owner action on the live PR.**
- Head matches `b75e7d4d3c4`. The PR is `CLEAN` and `MERGEABLE`.
- The live Proof header cites `61b8f767c34`, and production code changed after it. The receipt should name the run head.
- The round 2 review record for this ticket is not in `reports/` or `claims/`. The assignment says round 2 is clean at this head. Owner confirms where the record lives.
- The live body says "a separate ticket follows", the same conflict with the offer rule.

## Interactions

- **SOLR-6960 and SOLR-13706 in `SolrConfig.java`:** one textual conflict in either order. The hunks are adjacent. The part C simulation found no other shared file. Suggested order: 13706 first, then 6960. Note that 13706 already has a conflict on GitHub.
- **SOLR-7323 and SOLR-18178 in `FileSystemConfigSetService.java`:** no overlap. The hunks are 28 lines apart. Simulated three-way merges were clean. Land 18178 first, then rebase 7323.
- **SOLR-15478 and SOLR-15674:** not audited. The two change different layers of the same stale-schema symptom. Owner decides the landing order.
- **SOLR-17363 and SOLR-18129:** no file overlap. Both touch the config request path. 18129 edits `TestSolrConfigHandler.java`, which the 17363 receipt cites. If both land, re-run `TestSolrConfigHandler` on the combined tree. Whether 18129's parse change alters the 17363 test payload was not checked.

## Owner decisions

1. SOLR-6960: correct the branch changelog title before the draft is used.
2. SOLR-6960: run the second test without the change, or accept its Proof as pending.
3. SOLR-7267: check the Choice wording against the review file.
4. SOLR-7323: main side runs the premise and the first gate, and corrects the receipt and inventory row.
5. SOLR-15478: confirm `shareSchema` for the reporter, and the failing line from the log.
6. SOLR-17363: correct receipt line 3, then choose options (a), (b), or (c) for the stale replica, and keep or narrow the down-or-recovering excusal.
7. SOLR-13706: resolve the live PR's conflict, confirm the proof run's head, and fix the body's claims. This is for you to do on GitHub; nothing was changed.
8. SOLR-18178: fix the live Proof header head, locate the round 2 record, and fix the "separate ticket" wording. Also for you on GitHub.
9. Landing orders: 13706 before 6960; 18178 before 7323; 15478 with the 15674 owner decision; 17363 with a re-run if 18129 lands.

## Not done

No PR, comment, or live PR body was changed. No submit branch was edited. No build, Gradle run, or test was run. The gate, premise, and top-up logs named in the receipts are not on disk, so their counts are receipt-only. The SOLR-7267 review file and the SOLR-18178 round 2 record are not on disk. The JIRA packets for 6960 and 7267 date from 2015 and 2016 and may be stale.

## Left for the next round

Commits that arrived during this round and have not been read as a round: `6e623c34949` (SOLR-11483 draft), `75570244670` (the 6960 top-up receipt, used above), `44bae5cc588`, `5a50bac2cdf`, and `9bfcb2ff1c1`. Those add three assignments (schema and analysis round 1, eDisMax round 3, query parsing round 1) and about fifty receipts.
