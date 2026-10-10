# Configsets round 1, part B: SOLR-15478, SOLR-17363, and the 17363 and 18129 interaction

Result: SOLR-15478 is draftable once the owner confirms the schema-sharing premise (draft written at head 0478bdf0ac5). SOLR-17363 has a draft with the stale-replica Limit, but it is held: its receipt says production code is untouched, which is false, and the owner must make the stale-replica scope call.

Heads checked against the claim: `origin/solr-15478-submit` = 0478bdf0ac5cd100d5020451a6732ca5cbaa1a52, `origin/solr-17363-submit` = b8e8e1c48461be964ac0ef9a0cc570c277e7420d. Both match. Bases used: SOLR-15478 = merge-base with `upstream/main` (local ref 8e62c268688), which is b5c71bc5573c; SOLR-17363 = 56ec140e3636 (merge-base, matches the receipt).

Drafts (not committed):
- `pr-drafts/configsets/SOLR-15478.md`
- `pr-drafts/configsets/SOLR-17363.md`

## Findings

1. FIX. The SOLR-17363 receipt says production code is untouched. It is not.
   - Evidence: `receipts/SOLR-17363.md` line 3 on `origin/pr-prepare`. `git diff --name-status 56ec140e3636 origin/solr-17363-submit` lists `M solr/core/src/java/org/apache/solr/handler/SolrConfigHandler.java` (69 lines changed, the replica wait and a new helper). Commit 3c513c782f4 adds 16 lines to that file; commit 9417e50fc4b changes 77 lines. The `src/main` check over the whole diff finds no file. Solr's production Java is under `solr/core/src/java`, and no `src/main` directory exists under `solr/` (the only `src/main` paths are 12 files under `build-tools/`). The inventory row at `branch-focus-inventory-2026-10-08.md` line 139 also names SolrConfigHandler.java.
   - Code: [SolrConfigHandler.java L909-L937](https://github.com/nick-boss-tech/solr/blob/b8e8e1c48461be964ac0ef9a0cc570c277e7420d/solr/core/src/java/org/apache/solr/handler/SolrConfigHandler.java#L909-L937) and [helper L964-L977](https://github.com/nick-boss-tech/solr/blob/b8e8e1c48461be964ac0ef9a0cc570c277e7420d/solr/core/src/java/org/apache/solr/handler/SolrConfigHandler.java#L964-L977).
   - Replacement for the receipt line 3: "Production change: solr/core/src/java/org/apache/solr/handler/SolrConfigHandler.java (replica wait; commits 3c513c782f4 and 9417e50fc4b). Test change: TestConfigWaitForReplicas.java (commits 9417e50fc4b and b8e8e1c4846). Head b8e8e1c4846 itself changes only the test file and deletes the testing note."
   - The draft does not repeat the receipt claim. It says the change touches the wait.

2. NOTE. SOLR-15478 premise depends on schema sharing, which is off by default. The Jira packet does not mention it.
   - Evidence: `NodeConfig.java` line 606 (`useSchemaCache = false`); `SolrXmlConfig.java` line 365 (`it.boolVal(false)`); `ZkConfigSetService.java` line 61 (`hasSchemaCache()`). The shipped `solr/server/solr/solr.xml` has no shareSchema element. `ConfigSetService.java` line 269 creates the cache only when `shareSchema` is true.
   - Replacement: none in the draft's wording. The draft's first paragraph states the condition. Owner: confirm the reporter ran with shareSchema on. If not, the draft's premise does not fit their case.

3. NOTE. SOLR-15478 changelog title is not wrong, but it does not name the condition.
   - Evidence: `changelog/unreleased/SOLR-15478.yml` line 1.
   - Optional replacement: "A schema cached for a configset in SolrCloud, with shareSchema on, is no longer served after the configset was deleted and uploaded again, for example when restoring a backup that reuses a configset name".

4. NOTE. SOLR-15478 "fails without the fix" rests on one receipt phrase.
   - Evidence: `receipts/SOLR-15478.md` line 7 says "the gate's pre-fix proof step passed at this head". The receipt does not name the failing assertion. Its log `g15478-harden.log` is not on disk. By reading the code, on the base both versions are 0, so the expected failure is test line 69 (`assertNotEquals("recreated config set gets a new version", ...)`). That is my reading, not a recorded run.
   - Replacement: none. The draft says only that the test fails on the base code. Owner: confirm the failing line from the log before posting.

5. NOTE. SOLR-15478 test does not exercise the schema cache.
   - Evidence: `ZkConfigSetServiceModificationVersionTest.java` line 53 builds `new ZkConfigSetService(zkServer.getZkClient())`, which calls `super(null, false)` at [ZkConfigSetService.java L67-L68](https://github.com/nick-boss-tech/solr/blob/0478bdf0ac5cd100d5020451a6732ca5cbaa1a52/solr/core/src/java/org/apache/solr/cloud/ZkConfigSetService.java#L67-L68). No cache exists in that path.
   - Replacement: the draft's Limits says the test checks the version value only. Owner: decide whether a cache-level test is needed.

6. NOTE. SOLR-17363 receipt scope sentence is missing one excused case.
   - Evidence: `receipts/SOLR-17363.md` line 8 lists removed and not-active replicas. The active filter also requires a live node: [SolrConfigHandler.java L993-L994](https://github.com/nick-boss-tech/solr/blob/b8e8e1c48461be964ac0ef9a0cc570c277e7420d/solr/core/src/java/org/apache/solr/handler/SolrConfigHandler.java#L993-L994).
   - Replacement for line 8: "removed replicas, and replicas that go down, start recovering, or sit on a node that is no longer live, are excused."

7. NOTE. Named SOLR-17363 gate logs are not on disk. The one on-disk run is older than the head.
   - Evidence: `g17363-gate.log` and `g17363-premise.log` (receipt line 5) are not on disk. A search of names containing 17363 under `research`, `env`, `wt`, `tools`, `worktrees`, and the top level found only `research/test-queue/results/SOLR-17363.json` and its two logs. The JSON shows SUCCESS, 4 tests, 0 failures, finished 2026-10-03 16:05 UTC. Head b8e8e1c4846 was committed 2026-10-05 07:39 UTC. The JSON records no SHA, so it cannot stand for the head.
   - Replacement: none. Owner: confirm the 2026-10-05 record before the draft's counts are posted.

8. NOTE. SOLR-17363 "Error Prone compile passes" is not confirmed on disk.
   - Evidence: receipt line 5. The on-disk 2026-10-03 log says "errorprone disabled ... pass -Pvalidation.errorprone=true". The draft does not repeat this claim.
   - Replacement: none. Do not repeat the claim without a log that shows it.

9. NOTE. SOLR-17363 and SOLR-18129 both touch the config request path, but there is no file overlap.
   - Evidence: `git diff --name-status 0d2a4649c79 origin/solr-18129-submit` lists CommandOperation.java, TestSolrConfigHandler.java, TestUtils.java, config-api.adoc, and a changelog. The 17363 diff does not list any of these. SolrConfigHandler.java has no duplicate-key code at base or head (grep for "dup" returns nothing). SolrConfigHandler.java calls `ApiBag.readCommands` at line 411 and handles the results as `List<CommandOperation>`, so the command parse path is shared.
   - Replacement: none. Owner: if 18129 lands first or together, re-run TestSolrConfigHandler (cited in the 17363 receipt line 6 as 8 of 8) on the combined tree, because 18129 edits that file.

10. NOTE. SOLR-15478 and SOLR-15674 pairing. Not audited.
    - Evidence: inventory line 288 lists 15674 with `ZkSolrResourceLoader.java`, `SolrConfig.java`, and `IndexSchemaFactory.java` (5 files, 3 shown). None of those three is in the 15478 diff. The 15478 change is in the cache key (ConfigSetService.java L304-L318 and ZkConfigSetService.java L101-L118), and 15674 is on the loader side, so both touch the stale-schema symptom at different layers.
    - Replacement: none. Owner: decide landing order and whether the two tests are read together.

11. NOTE. SOLR-17363 helper is described as extracted "for unit testing", but no unit test calls it.
    - Evidence: commit 9417e50fc4b message. `failedCoresStillActive` (L964) is called only from SolrConfigHandler.java L912. It is covered only through the end-to-end tests.
    - Replacement: none. Owner: decide whether a unit test is wanted.

12. NOTE. SOLR-17363 draft length is above the guide. It is about 4,900 characters with links, about 3,000 without them.
    - Replacement: none required. Trim the Limits section if the owner wants it shorter.

## Task results

B1. SOLR-15478 audit and draft. Verdict: draftable, with two owner checks before posting (Findings 2 and 4). The premise matches the code: the cache key uses the znode data version (ConfigSetService.java L304-L318; base ZkConfigSetService.java L117), which restarts at 0 for a new znode. The fix returns mzxid, which ZooKeeper does not reuse. The Jira packet describes a schema not visible after a configset is reused, which fits. The draft is at `pr-drafts/configsets/SOLR-15478.md`, written against 0478bdf0ac5. The draft states the schema-sharing condition and does not claim the standalone service is fixed. Pairing with SOLR-15674 is noted (Finding 10); not audited.

B2. SOLR-15478 proof and changelog check. Verdict: pass with NOTEs. Head 0478bdf0ac5 matches the receipt and the live tip. Count 1 of 1 matches: the test class has one @Test. The receipt test name `ZkConfigSetServiceModificationVersionTest` matches `solr/core/src/test/org/apache/solr/cloud/ZkConfigSetServiceModificationVersionTest.java` in the diff against base b5c71bc5573. Changelog `changelog/unreleased/SOLR-15478.yml`: type `fixed`, author `Nick Shanin`, no placeholder text, link to the Jira. The draft's Changelog line links to the same file at head. The draft says "fails on the base code" only because the receipt says the pre-fix step passed (Finding 4). The log is not on disk.

B3. SOLR-17363 audit and draft. Verdict: draft written at b8e8e1c4846, held before posting for Finding 1 (receipt error) and the owner scope call (Owner decision 1). The branch covers removed, down, recovering, and non-live-node replicas. An active replica that never reports still fails. The draft states that gap as a Limit and states the down or recovering excusal as a choice. "Production untouched" is false: one production file changed (Finding 1). The test count 4 of 4 matches the four @Test methods. Regression counts (TestSolrConfigHandler 8 of 8, TestReqParamsAPI 1 of 1, TestSetPropertyConfigApis 4 of 4) come from the receipt only; the three classes exist at head under `solr/core/src/test/org/apache/solr/core/` and `.../handler/`. Logs are not on disk (Finding 7).

B4. SOLR-17363 and SOLR-18129 interaction. Verdict: no code overlap; one ordering note. The 17363 diff touches SolrConfigHandler.java, but only in the replica wait (the check at L909-L937 and the helper at L959-L977). Replica selection at L865 is unchanged. It does not touch any duplicate-key path. SOLR-18129 is reachable locally at `origin/solr-18129-submit` = 657e443d866 (also `refs/heads/solr-18129-submit`); it was not fetched. Its diff changes CommandOperation.java and TestSolrConfigHandler.java, and neither is in the 17363 diff. The 17363 test payload is a single `set-user-property` command, a command type 18129's changelog does not name (it names request-handler defaults, appends, and invariants). Whether 18129's parse change alters that payload was not checked (Finding 9).

## Owner decisions

1. SOLR-17363 stale replica (an active replica that does not report the new version in time still fails the request, the ticket's second cause). Options, stated plainly:
   - a. Submit as drafted. The gap stays as a named Limit with a follow-up offer. Slow reloads still fail with the overlay error.
   - b. Hold the SOLR-17363 submission until the slow-reload case has its own change and tests. The branch has no change for it.
   - c. Submit as drafted and open a follow-up ticket only if someone asks (the standing rule is no new ticket unless asked).
   The receipt (Finding 1) must be corrected before any option is used.
2. SOLR-17363 down or recovering excusal. Keep the implemented route (the draft's choice section), or narrow the change to removed replicas only. Narrowing needs a code change and new tests.
3. SOLR-15478 schema-sharing premise. Confirm the reporter ran with shareSchema on (Finding 2). If not, the draft and the changelog need a different premise.
4. SOLR-15478 changelog title: keep, or add the schema-sharing condition (Finding 3).
5. Receipts: correct the SOLR-17363 production claim (Finding 1); name the failing line for SOLR-15478 from its log (Finding 4); confirm the 2026-10-05 SOLR-17363 record (Finding 7).
6. Landing order: SOLR-15478 with or before SOLR-15674 (Finding 10); SOLR-17363 with SOLR-18129 (re-run TestSolrConfigHandler on the combined tree, Finding 9).

## Not checked

- No build, Gradle, or test run (rule for this round). Counts are taken from the receipts only.
- No `gh` calls, no Jira calls beyond reading the local packet, no fetch.
- Receipt logs not on disk: `g15478-harden.log`, `g17363-gate.log`, `g17363-premise.log`, and the main side's takeover log and receipts ledger. Not searched beyond the locations listed in Finding 7.
- SOLR-15674 and SOLR-18129 were not audited. Only file names and the inventory line were compared.
- The 17363 timing-dependent tests (they edit ZooKeeper state while a wait runs) were not run, so flakiness is not checked.
- Error Prone results and the module check: not verified (Finding 8).
- Changelog YAML was read by eye, not parsed.
- The Jira packets do not state the reporter's shareSchema setting.
- The owner's scope call on the 17363 gap is deferred on the main side; its record was not visible here.
- The base for SOLR-15478 is the merge-base with the local `upstream/main`; newer upstream commits were not compared.
- Whether the 17363 regression classes pass: receipt-only.
