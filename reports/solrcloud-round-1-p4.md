# SolrCloud round 1, part p4 (collections, aliases, file store, recovery)

Result: five drafted (11288, 13369, 17680, 17733 ready; 14919 drafted, opening held for one owner call), 13239 held with its record verified, 18277 merged and retire confirmed.

Drafts: `C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-suggester\pr-drafts\solrcloud\SOLR-11288.md`, `SOLR-13369.md`, `SOLR-14919.md`, `SOLR-17680.md`, `SOLR-17733.md`. Each draft names its head in its Proof. Branch file paths below are relative to the repository root, and the heads are the live fork heads checked on 2026-10-10.

## Findings

1. **FIX** (branch changelog, owner). `changelog/unreleased/SOLR-11288-set-of-split-duplicates.yml` line 2, head `cd094c3c623`. A blank-only value now means the same as an omitted value. `ClusterStatus.java` lines 174-180 and 260-261: an empty set returns all shards. `BalanceReplicasCmd.java` lines 52-58, with `BalanceRequestImpl.create`: an empty set means all live data nodes. `MigrateReplicasCmd.java` lines 76-82: an empty `targetNodes` means all other live nodes. The title says only "ignore blanks around names." Replacement title: "CLUSTERSTATUS (shard), BALANCE_REPLICAS (nodes) and MIGRATE_REPLICAS (source and target nodes) no longer fail with an IllegalArgumentException when a comma separated value repeats a name, ignore blanks around names, and treat a blank-only value as an omitted one." The draft already states this.

2. **NOTE** (11288 receipt wording). The receipt says one run failed on the padded repeat and the other on the exact repeat. In the test as shipped, the exact repeat is asserted first (`TestCollectionAPI.java` line 321). On the base code `Set.of` throws there, so a base run cannot reach the padded case at line 323. The draft says what each case does on the base code and does not say which run failed. Check the failure lines in the gate log before posting. The log is not on disk. No draft change.

3. **FIX** (14919, owner decision 3). `RecoveryStrategy.java` lines 302-304 set `commit_end_point=true` on every replica recovery commit, in every cluster. `DistributedZkUpdateProcessor.java` line 189 builds the fan-out list as the leader of each shard. Lines 215-219 skip that fan-out when `commit_end_point` is set. So a recovery commit now commits the leader and its own shard's replicas only. No test covers this path; the receipt proof is for the processor alone. Replacement: the draft's "What this change does" and Limits already say this. The owner decides whether this hunk ships in this PR.

4. **FIX** (14919, before opening). Upstream commented out this marker in `75b183196798` (SOLR-12801 batch, whose message also lists "SOLR-12933: Fix SolrCloud distributed commit."). `upstream/main` `RecoveryStrategy.java` line 315 still has it commented out. The branch comment at line 303 gives no reason for re-enabling it. Replacement: before opening, confirm that the reason for the 2018 change does not apply. The draft's Choice asks this openly. SOLR-12933 itself was not read.

5. **NOTE** (14919 test comment). `IgnoreCommitOptimizeUpdateProcessorFactoryTest.java` lines 63-66 say the leader hop of a recovery commit is forwarded with the endpoint names. The recovery commit carries `true` (`RecoveryStrategy.java` line 304). `leaders` is set only on a leader's fan-out to other shard leaders (`DistributedZkUpdateProcessor.java` line 218), and `replicas` on the hop to its own replicas (line 235). Replacement for lines 63-66: "// The distributed update processor replaces the marker when it forwards a commit: "leaders" on a leader's fan-out to other shard leaders, and "replicas" on the hop to the leader's own replicas. Those internal commits must pass, and must not fail boolean parsing."

6. **NOTE** (14919, inherited trust gap, round-28 MEDIUM). `IgnoreCommitOptimizeUpdateProcessorFactory.java` lines 122-130 read the marker from the request. Any client can send `commit_end_point=true` on the base code too. The branch adds `leaders` and `replicas`. Draft Limits bullet 1 says so. No code change proposed.

7. **NOTE** (14919, behavior change). Lines 127 and 142: an unknown marker value makes `StrUtils.parseBool(value, false)` return false, so it takes the client path. The base code used `getBool` and returned 400 "invalid boolean value." Draft Limits bullet 3 says so.

8. **NOTE** (14919 against 7394, consistency). Trial merges of `origin/solr-14919-submit` and `origin/solr-7394-recovery` are clean in both orders and give the same result tree (`a319c15cd716`). The 7394 hunks change `recoveryFailed`, the retry limit, and add a test hook. The 14919 hunks change the import list and `commitOnLeader`. No line is shared. The 7394 head still has the recovery marker commented out, which is upstream's state. Nothing blocks either landing order.

9. **NOTE** (17680 branch changelog). `changelog/unreleased/SOLR-17680.yml` line 1 names only the v1 path. The receipt asks the draft to name v2, and the draft does. Optional branch title: "CREATEALIAS and the v2 aliases endpoint for a Dimensional Routed Alias, as in the reference guide example with per-dimension router.<i>.field parameters and no top-level router.field, no longer fail with "A routed alias requires these params"."

10. **NOTE** (17733, round-28 findings resolved at head). Round 28 flagged a wrong quote in the changelog and a testing handoff file. At head `636196b7954` the title reads "The path exist ZK, delete and retry", which matches `DistribFileStore.java` line 536. The diff against `upstream/main` has three files and no handoff. No change needed.

11. **NOTE** (17733, failure paths). `DistribFileStore.java` lines 162-168: `deleteFile` logs an IOException and returns, so the API still reports success. Lines 496-511: remote deletes use `requestAsync`, and their failures are not reported. Lines 489-490: the ZooKeeper entry is removed before the local delete. Draft Limits bullets 1 and 2 say so.

12. **NOTE** (13369 handoff file). The round-28 MEDIUM finding (`SOLR-13369-TESTING.md`) is resolved at head `dfa0db5bdf96`. The diff has two files, the changelog and the test. The receipt says the proof was not obtained by a run. The draft says so.

13. **FIX** (13239, before any submission; held). The branch still commits `SOLR-13239-TESTING.md` (commit `09725b66e6b`, which says "UNCOMPILED / UNTESTED"). `research/branch-reviews/SOLR-13239-review.md` (reviewed 2026-10-02 at head `2fa91ce4dd7`) calls it a BLOCKER. Head `699a1fce368` is one commit after that review and still has the file. Replacement: delete the file from the ticket branch before any submission. Not done here.

14. **NOTE** (13239 record). The head matches the live fork head, and the receipt says green at that tip. The later commit's message says it re-arms watches and adds a test. That commit was not reviewed beyond the record.

15. **NOTE** (18277, retire confirmed). PR #4959 (`solr-18277-submit`) merged 2026-10-05T10:46:07Z. Its head `17c0e644812` equals the live fork head. Merge commit `0cc328310f8` is an ancestor of `upstream/main`. The four changed files are identical between the merge commit and the branch head. The branch has six commits that `upstream/main` does not reach, because the merge was a squash. `origin/ci/solr-18277` and `origin/ci/solr-18277-proof` also exist. Nothing deleted.

16. **NOTE** (heads). All seven live heads match the claim table and `git ls-remote` on 2026-10-10. The local branch `wt-solr-17680-submit` (`f9f1c579f39`) is not the live head. It was not compared.

17. **NOTE** (no open PRs). `gh pr list --author nick-boss-tech --state all` shows no apache/solr PR for 11288, 13239, 13369, 14919, 17680, or 17733. Only 18277 has one, and it is merged. Nothing was opened or posted.

## Task results

**SOLR-11288** (draftable, partial fix). Head `cd094c3c623` matches live. The record says TestCollectionAPI 4 of 4. Draft: `SOLR-11288.md`. The draft states the blank-only behavior and a Choice on it. Owner decision 1 is needed before posting. The escaping gap and the two untested parsers are named in Limits.

**SOLR-13239** (held, submission-held per Nick). Record verified: head `699a1fce368` matches live, and the receipt says green at that tip. No draft. Finding 13 is the handoff file that must go before any submission.

**SOLR-13369** (draftable, proof by construction). Head `dfa0db5bdf96` matches live. Test only, with two files in the diff. Draft: `SOLR-13369.md`. Proof says no run has shown the old failure. The Choice asks whether the test or the router should change.

**SOLR-14919** (drafted, opening held). Head `84e7bcaeb57` matches live. Draft: `SOLR-14919.md`. The processor half is covered by a unit test. The recovery half is not covered and changes commit fan-out in every cluster. Owner decision 3 and finding 4 need an answer before opening. Trial merge with 7394 is clean.

**SOLR-17680** (draftable). Head `f4b8ce83365` matches live. Draft: `SOLR-17680.md`. It names the v2 endpoint as the draft must. The Choice asks about moving the field check. Limits say there is no cloud test through v2.

**SOLR-17733** (draftable). Head `636196b7954` matches live. Draft: `SOLR-17733.md`. It states the contract change, that local-only delete still refuses, and that the sync complaint was not reproduced. The Choice asks whether DELETE should own the cleanup. The round-28 changelog finding is resolved at head.

**SOLR-18277** (retire confirmed, no draft). PR #4959 is merged. Its head matches the live fork head, and the merged files match the branch. Owner decision 6 covers deletion.

## Owner decisions

1. SOLR-11288: keep blank-only input meaning "all" (all shards, all live nodes, all other live nodes), or keep the failure for blank-only input. Either way, update the branch changelog title (finding 1).
2. SOLR-13369: keep the test-side rule (draft Choice), or change the router.
3. SOLR-14919: ship the `RecoveryStrategy` marker in this PR, or ship the processor change alone and move the marker to its own ticket with its own test.
4. SOLR-14919: confirm the 2018 reason for removing the marker (finding 4) before opening.
5. SOLR-17680: keep the moved field check (draft Choice), or keep the up-front check and fill `router.field` in the create path. Optionally update the branch changelog title (finding 9).
6. SOLR-17733: DELETE owns the shared cleanup (draft Choice), or return a 400 that tells the caller to delete the entry first.
7. SOLR-18277: delete the retired fork branch `solr-18277-submit`, and decide on the two CI branches.
8. SOLR-13239: remove `SOLR-13239-TESTING.md` before any submission (held per Nick).

## Not checked

- No builds, tests, or gate logs. The gate logs are not on disk, so every proof number comes from the receipts.
- Live JIRA was not queried. The apache-jira MCP tool is not available in this session, so Jira text comes from `research/jira-context/` packets. There is no local packet for SOLR-14919, and round 28 could not read its body.
- SOLR-12933 was not read, so the reason for the 2018 marker change is unknown.
- The SOLR-11288 receipt's run wording (finding 2) was not checked against a log.
- Overlaps with branches outside this part were not checked, for example other rounds that touch `ClusterStatus`, `DistribFileStore`, or `CreateAlias`.
- Only the seven named live heads were compared with the fork. Other local worktree branches were not compared.
- The `gh` calls were read-only: `pr list` and `pr view 4959`.
- Other parts' drafts already in `pr-drafts/solrcloud/` were not read or changed.
- No draft names a Lucene version, so the Lucene version rule does not apply.
- Draft length is about 2,700 to 4,400 characters. Link URLs count toward that, so the visible text is shorter.
