# Drafts for the round 4 newly gated branches (part report)

Assignment: `assignments/pool-drafts-newly-gated-round-4.md`. Claim: `claims/pool-drafts-newly-gated-round-4.md` (status ACTIVE, not edited here; the lead marks it DONE in the commit). Worktree at `ff97d2f0726`, clean at start. Drafts written: `pr-drafts/security/SOLR-13705.md` and `pr-drafts/solrcloud/SOLR-11479.md`. No commit, push, PR, comment, Jira write, build or test.

## Head check (live fork tip vs receipt)

- SOLR-13705: `ls-remote origin refs/heads/solr-13705-submit` = `73504e7a8dcb58e8f77c976d98db600af6fc5ced`. Matches the receipt head. Drafted at that head.
- SOLR-11479: `ls-remote origin refs/heads/solr-11479-submit` = `360dca60fcfade6937353d1d1f41e01152aaa3c7`. Matches the receipt head. Drafted at that head.
- Local objects: neither head was in the local object store. The local remote-tracking refs are stale (`origin/solr-13705-submit` at `5f141fb2af3`, `origin/solr-11479-submit` at `7e538e844c4`). I ran `git fetch origin <sha>` for the two head SHAs in the worktree. That only added objects and FETCH_HEAD; no branch ref was moved and no tracked file changed.
- Merge-bases checked as ancestors of the heads: `9b3a84b1c460981eab09d8ffaef776acc4a184f8` (13705, matches receipt line 10) and `cabedd1d968059215188f4e7563fb303241899ed` (11479, matches the assignment).

## Packaging delta (SOLR-13705)

The packaging commit `73504e7a` removed only `SOLR-13705-TESTING.md` (30 lines). The branch diff against the merge-base is three files: the changelog fragment, the one-line production change, and the new test file. Nothing about the testing note appears in the draft.

## Receipt numbers used

SOLR-13705 (`receipts/SOLR-13705.md`):
- line 3: full gate completed 2026-10-11T07:49:43Z. Used as the verification date.
- line 5: green at head `73504e7a8dcb...` on `solr-13705-submit`.
- line 6: packaging removed only the testing note.
- line 10: merge-base `9b3a84b1c460`, copied branch test only: 2 tests, 1 failure, 0 errors; `testConcurrentCurrentReturnsSingleInstance` passed; `testLazilyInitializedSingletonIsVolatile` failed on the volatile assertion. Used in Proof.
- line 11: focused class at head: 2 tests, 0 failures, 0 errors, 0 skipped. Used in Proof.
- line 19: the non-volatile site had no prior reproduction. Used in Limits ("No failure has been seen at this site in practice").
- Not used: seed, logs, module and tidy results (process material).

SOLR-11479 (`receipts/SOLR-11479.md`):
- lines 3-4: GREEN; head `360dca60...`; tip verified 2026-10-11T11:38:54Z. Used as the verification date for the whole Proof (the receipt gives no separate date per leg).
- line 5: merge-base `cabedd1d968...`.
- line 8: scope is the four gate-specified corrections; no rebase onto SOLR-15035. Used in Limits.
- line 17: merge-base proof leg: 2 tests, 2 expected failures, 0 errors; the multi-replica test got HTTP 500 instead of 400; the one-replica alias test failed with `ADDREPLICA failed to create replica`. Used in Proof.
- line 18: focused head: 26 tests, 0 failures, 0 errors, 1 conditional skip (`testCreateAndDeleteShard`, SOLR-13021 assumption); both SOLR-11479 tests passed. Used in Proof.
- Not used: seed and log paths.

## Title source

- SOLR-13705: `changelog/unreleased/SOLR-13705-ssl-configurations-factory-volatile.yml` at the head: "SSLConfigurationsFactory's lazily initialized singleton is now safely published (the double-checked locking field is volatile)." The draft's summary lines are phrased from this fragment and the diff.
- SOLR-11479: `changelog/unreleased/SOLR-11479-addreplica-property-corenodename.yml` at the head (the retitled fragment): "ADDREPLICA: accept property.coreNodeName as an alias of coreNodeName for one replica, and reject it when several replicas are requested." The summary line follows it. No pre-correction wording was used; the draft describes the head code.

## Citations and checks

SOLR-13705 (all read with `git show <sha>:<path>` or the diff at the stated SHA):
- Base `SSLConfigurationsFactory.java` line 23 (the declaration, the only changed line in the diff) and lines 32-33 (first check, unchanged by the branch).
- Head line 23 (`volatile`).
- Head `SSLConfigurationsFactoryTest.java` lines 33-39 (`testLazilyInitializedSingletonIsVolatile`) and 41-69 (`testConcurrentCurrentReturnsSingleInstance`, 8 threads). The file is new in the branch, so it has no base version.
- Base `SecurityConfHandler.java` lines 284 (`private Collection<Api> apis;`), 289-292 (unsynchronized first check, then synchronized recheck). Read at base; this is the adjacent site named in Limits. Not changed by the branch.
- Changelog path and link at the head: confirmed by `ls-tree` and the diff.

SOLR-11479 (same method):
- Base `AddReplicaCmd.java` line 352 (`coreNodeName` read only from `CORE_NODE_NAME`). Cited as the pre-change symptom, linked to the merge-base.
- Head `AddReplicaCmd.java` lines 141-149 (the several-replica check now covers `property.coreNodeName`; message names both) and lines 360-363 (fallback when `coreNodeName` is blank).
- Head `solrj/.../common/cloud/Slice.java` line 91 (`replicasCopy.put(modified.getName(), modified)`), plus the `Map<String, Replica>` field, as the basis for "keyed by name". Cited in the Choice section.
- Head `CollectionsAPISolrJTest.java` lines 497-515 and 517-533 (the two new tests). Test names are from these lines.
- Changelog link at the head: confirmed by `ls-tree` and the diff.
- Test-to-receipt mapping: the receipt says "one-replica alias test" and "multi-replica test". I matched them to `testAddReplicaWithCoreNodeNameProperty` and `testAddReplicaWithCoreNodeNamePropertyRejectsMultipleReplicas` by their bodies. The receipt does not name the methods.
- The Jira quote "coreNodeName user_assigned_node_name does not exist in shard shard1" is from the local Jira packet `research/jira-context/SOLR-11479.json` (description). Not re-fetched from Jira.
- The statement that SOLR-15035 also changes `AddReplicaCmd.java` was checked directly: its gated head `12d7491e82c` changes that file (one insertion against its merge-base). The branch is not rebased onto it (receipt line 8).

## Choice-section decisions

- SOLR-13705: no choice section. The branch is the one-keyword form of the standard fix. The other scope options in the build-docs audit (close as not reproduced; widen to `SecurityConfHandler`) are scope questions on a straightforward patch, which the formula says are not choices. The wider site is named in Limits with a follow-up offer. The receipt's gate and the assignment both direct the narrow draft.
- SOLR-11479: choice section included. Refusing several replicas, the implemented route, is a behavior restriction a maintainer could reasonably reject in favor of derived per-replica names. That is a live alternative with its own cost, which meets the formula's bar. It is not a narrow-scope call.

## Receipt gaps, discrepancies, and open items

- No receipt gives a separate verification date per leg. Both Proof sections use the receipt's own head or completion date (2026-10-11).
- No receipt names the test methods behind the proof counts (see the mapping note above).
- The base-leg HTTP 500 for the multi-replica test and the base failure text are taken from the receipt. I could not check them against the gate logs, which are not on disk here.
- Scope decision, not a hold: `reports/build-docs-misc-round-1-g5.md` (lines 27-37, 83) says "decide scope before any gate" for SOLR-13705. I found no recorded scope decision on disk among the files I read. The gate has since run on the narrow change, and the assignment directs the narrow draft. The lead may want to confirm the scope call before posting.
- The claim file stays ACTIVE in this worktree. Per the brief I did not edit it; the lead marks it DONE with the deliverables.

## Not verified

- The Java memory model statement in the SOLR-13705 draft is general background, not sourced to the receipt.
- Nothing was run: no build, test, gate, or test-queue command.
- The SecurityConfHandler site is supported by reading base code only; no runtime check.
- Character counts: SOLR-13705 about 3,520 characters; SOLR-11479 about 3,390 characters, both including links.
