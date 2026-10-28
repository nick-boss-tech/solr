# SOLR-18589 / PR 5255 follow-up implementation

Status: IMPLEMENTED, PROOF PENDING

- Host: windows; source implementation and static review only.
- Source: Nick's request to implement both review findings, add tests, and hand off Linux proof.
- Claim: `claims/pr-5255-followup-windows-round-1.md`.
- Fork branch: `nick-boss-tech/solr:solr-18589-submit`.
- Pushed and remote-verified head: `fd49245a38ba779f78840477dbf6db34b92c5f6b`.
- Direct parent / negative proof base: contributor's PR 5255 head `1edd6a113b2a9e1b029f509c9279f4fb58683bba`.
- Delta: one follow-up commit, 19 files, 330 additions and 11 deletions. The original contributor's commit is retained unchanged as the parent.
- Commit: `SOLR-18589: Reject changed alias targets and preserve lock errors`.
- Diff: https://github.com/nick-boss-tech/solr/compare/1edd6a113b2a9e1b029f509c9279f4fb58683bba...fd49245a38ba779f78840477dbf6db34b92c5f6b
- Gate: `gates/pr-5255-alias-lock-followup-round-1.md`.
- Receipt target: `receipts/PR-5255-alias-lock-followup.md`, owned by the executing Linux host.

## Repairs

F1: AdminCmdContext retains the collection selected when the collection API lock was acquired. The distributed runner carries its resolved lock key; the Overseer lock wrapper carries the key from lockTask through processMessage and delegates unlock/id/subpath validation unchanged. Commands check their actual source collection immediately after alias resolution, before acting on it. A mismatch returns CONFLICT (409), with an instruction to retry, instead of executing against an unlocked collection. The message retains the external alias and followAliases flag so DELETE/RENAME bookkeeping is preserved.

The checks cover AddReplica, Backup, CreateShard, CreateSnapshot, DeleteCollection, DeleteReplica, DeleteShard, DeleteSnapshot, Migrate's source, MoveReplica, Rename, and SplitShard. The count-based DeleteReplica path also verifies its actual collection, so it cannot act under another collection's lock. This does not add count-based alias support from SOLR-18602; a mismatch is refused. LockLevel.NONE commands do not receive a claimed collection lock, and nested subrequest contexts do not inherit the parent's collection expectation. REINDEXCOLLECTION's separate locking and two-collection target locking remain outside this change.

F2: OverseerTaskProcessor serializes lock-acquisition SolrExceptions through CollectionHandlingUtils.addExceptionToNamedList. Both synchronous and stored asynchronous failures retain the structured message and response code consumed by SolrResponse.getException.

The existing changelog now describes the conflict and error behavior and retains Amrit Sarkar while adding Nick Shanin as an author.

## Regression tests

There are five new tests, all using existing production APIs so their test files can be overlaid unchanged on the parent for fail-before proof:

1. AliasCollectionApiLockTest.testAliasRetargetedWhileWaitingDoesNotDeleteUnlockedCollection: distributed mode, collection locks held on A and B, alias X initially A, async DELETE queued behind A, alias changed to B, then A released while B remains locked. Require FAILED/409, both collections and the alias preserved, B's lock still acquired. After release, a fresh request must successfully delete B and X while retaining A.
2. AliasCollectionApiOverseerTest.testAliasRetargetedAfterLockDoesNotDeleteUnlockedCollection: a real Overseer handler locks A, then X is retargeted to B before processMessage. Require 409, both collections and X -> B preserved. This is a controlled handler handoff test, not a queued scheduling test.
3. AliasCollectionApiOverseerTest.testMultiCollectionAliasReturnsBadRequest: synchronous endpoint request to a multi-collection alias must return 400 with the alias validation message and preserve both collections.
4. AliasCollectionApiOverseerTest.testAsyncMultiCollectionAliasPreservesErrorDetails: asynchronous endpoint request must reach FAILED and return a structured NamedList error with code 400 and the validation message.
5. AliasCollectionApiOverseerTest.testStableAliasStillDeletesCollectionAndAlias: ordinary followed-alias deletion still removes the collection and alias and preserves the other collection.

Together with the inherited distributed lock test, the two focused classes contain six tests. The gate must prove all six pass, and the first four tests above fail for their intended assertions against original production code. The two stability controls must pass on the original code as well.

## Validation and handoff state

The implementation worktree was clean after committing; git diff --check and git show --check passed. The remote fork ref was separately read and matched the full commit SHA above. No Gradle, tidy, compilation, test, or reproduction ran on Windows. Test counts here describe requested methods, not execution results.

The ticket remains Implemented, Proof Pending until the exact-head Linux gate supplies positive, negative, neighboring-suite, tidy and required-check evidence. If formatting or repair changes the source head, the gate must push that repair and add a superseding exact-head gate before proving it. No result from an older head may be carried forward.

No contributor branch, live PR head/body, GitHub discussion, JIRA issue, approval, or merge was changed. The public PR remains at the original contributor head; this derived fork branch is an offer pending proof. No gate result is claimed by this report.
