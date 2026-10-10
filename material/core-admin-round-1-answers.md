# Core admin and collections API round 1: main-side answers (2026-10-10)

Report: `reports/core-admin-round-1.md`, with parts `reports/core-admin-round-1-k1.md` through `-k6.md`. Claim: `claims/core-admin-round-1.md`. Assignment: `assignments/core-admin-round-1.md`. Under the owner's standing decision practice, recorded recommendations are adopted below unless an item is marked DISCUSS; DISCUSS items carry the recommendation and the call is not taken. Gate state is settled by the receipt in `receipts/` at the exact live tip; where a report claim and a receipt differ, the receipt wins and the entry says so. All thirty-three tickets are answered here. Drafts are in `pr-drafts/core-admin/` (seventeen written). Nothing in this pass edits a draft, a branch, or a live PR; corrections are listed as owed.

## Gate states settled by the receipts

- Gated green at the live tip, per the receipts: SOLR-4989 (`5bac95376cd4`), SOLR-6438 (`8c77988d591`), SOLR-8275 (`e52e10fa50a`), SOLR-8576 (`4c46f95c785`, test-only pin), SOLR-9750 (`f97da6da14a`), SOLR-11431 (`968fad873c4`, live PR, consistency only), SOLR-12007 (`bdeba582fd6`), SOLR-13246 (`6817c6c0c267`), SOLR-15024 (`f95b5010b3f`, hardened), SOLR-15805 (`3432f950f0a`, hardened), SOLR-16108 (`70ad7371b31`, proof inconclusive by construction), SOLR-16725 (`be1838ef8cc`), SOLR-16849 (`d612b055da2`, test-only pin), SOLR-16887 (`42675f65d6f`, BATS counts), SOLR-17297 (`c0ab38fc0a8`), SOLR-17377 (`22b5f209a11`), SOLR-17708 (`10a7fa07a79`), SOLR-17731 (`f2b4ba164f5`), SOLR-18278 (`bf19c9fa449`, proof fails by construction).
- Reconciled green at the live tip (ledger table row, no per-branch gate log): SOLR-14098 (`dd4995515e8`).
- Gated at an older head; the live tip is not gated: SOLR-12849 (gated `ed73600d877`, tip `6b92223bc24f`, live PR), SOLR-13097 (gated `38e306c2cd5`, tip `f0e7395f58f3`, live PR), SOLR-15003 (gated `8f5b6f8360a`, tip `1004abee39ab`), SOLR-18010 (gated `fadc5ee31e3`, tip `c3685bb37d9d`).
- No gate recorded: SOLR-4502, SOLR-5011, SOLR-5262 (no pipeline record at all), SOLR-8554, SOLR-8628, SOLR-11939 (docs only; no test gate needed), SOLR-12916, SOLR-16499 (the GitHub corroboration run does not settle gate state), SOLR-18317 (banked head `ae918a03fa7b`, held).

## Per-ticket answers

### SOLR-4502 (audit only, not ready): DISCUSS on the guard placement

- Gate state: NO GATE per the receipt. The premise holds on main (the factory is null until `loadInternal()`, and the public `create` has no guard). Owed before any draft: remove `SOLR-4502-TESTING.md` from the branch, a premise run showing `create` on an unloaded container producing a core whose search fails with the NPE (or otherwise reproducing the ticket), and a first gate running the new test and the rest of `TestCoreContainer` (part k3).
- DISCUSS: keep the `create()` guard as implemented, or call `load()` from the constructor, as Alan Woodward proposed on the ticket. The guard covers `create()` only; `registerCore` is protected and `SyntheticSolrCore.createAndRegisterCore` is unguarded. Recommendation: keep the guard; it sits before `inFlightCreations` is touched, which is the right place, and loading in the constructor is a wider behavior change. The call is not taken, and whether a 500 is the right status for this misuse goes with it.

### SOLR-4989 (draftable, PR-ready)

- ADOPTED: the draft stands. No Choice is owed, per the receipt (all = index + fields + schema is the ticket's own suggestion). Limits lines are as drafted; the distributed path was read, not run, and the draft says so.
- ADOPTED: no rebase before opening. The branch trails main by about 40 commits, but the gate stands at the recorded head `5bac95376cd4` and the trial merge with SOLR-15024 is clean; a rebase would change the head and every Proof line for no recorded gain. This answers the rebase item in part k1's owner list for both LukeRequestHandler tickets.
- Recorded warning: the local `solr-4989-submit` branch is not a fast-forward of the live head and adds a handoff file (part k1, finding 2). It is never pushed; the PR head is `5bac95376cd4`.
- Gate state: green at the live tip per the receipt (LukeRequestHandlerTest 10 of 10; the new `testShowAllIncludesIndexFieldsAndSchema` fails on base). Landing order: 4989 before 15024 (part k1, finding 11).
- Draft correction owed: the Limits section opens with bullets; add the bold one-line summary opener (see the draft corrections list).

### SOLR-5011 (audit only, not ready)

- Gate state: NO GATE per the receipt; premise unverified in the record and confirmed by reading in the audit (no `resourceLoader` close in `SolrCore.java` on main; each reload gets a fresh loader).
- ADOPTED: the first gate's run list includes the shared-schema scenario from part k3, NOTE 12: two cores on one config set with `shareSchema` on, unloading one core leaves the other able to load a lazy lib class. The gate must cover that scenario or the close must be narrowed; which of the two follows from what the run shows, so no owner call is taken now. Also owed: the base failure at the first `assertTrue` of `CoreCloseResourceLoaderTest`, and removal of `SOLR-5011-TESTING.md` at packaging.
- Interaction: SOLR-5011 and SOLR-12007 change different parts of the same close method; the trial merge is clean (part k3).

### SOLR-5262 (audit only, not ready; no pipeline record)

- Gate state: NO GATE, and no pipeline record on the main side at all (the receipt says so explicitly; the branch exists on the fork at `ade8b80264ac` and that is the whole record). This answers the round's open question: there is nothing to reconcile against, so the premise run and first gate below are the first record this branch will have.
- ADOPTED: the premise holds by reading (part k5, finding 14), and dataDir is accepted as the default value for `solr.core.ulogDir`: `UpdateLog` already resolves a null ulogDir to the data dir, so the tlog location does not change (finding 15). The relative-value point goes in any draft's Limits (finding 16).
- Owed (main side): the premise run and first gate must show (a) a config using `${solr.core.ulogDir}` with no ulogDir in core.properties fails on base, (b) `TestCoreDescriptorImplicitProperties.testUlogDirDefaultsToDataDir` fails on base (the base value is null), and (c) both pass with the change. Remove the root `SOLR-5262-TESTING.md` at packaging.

### SOLR-6438 (draftable)

- ADOPTED: reject the combined request, as implemented; the draft's Choice poses the merge-both route to the maintainer, which is where that call belongs under the formula. The choice section stays.
- ADOPTED: the branch history is left as it is. Two commit subjects name a handoff doc (part k6, NOTE 1); the net diff is clean, and squash on merge is the maintainer's choice. No history rewrite.
- Gate state: green at the live tip per the receipt (MergeIndexesTest 4 of 4, CoreMergeIndexesAdminHandlerTest 1 of 1, CoreAdminOperationTest 44 of 44; on base the new test expects 400 and gets 500). The draft's Proof matches the receipt.
- Draft corrections owed: remove the INTERNAL comment block; add the Limits bold opener.

### SOLR-8275 (draftable, PR-ready)

- ADOPTED: the draft stands; no Choice is owed (the ticket thread settled on the single-message route, per the receipt). Limits lines are the receipt's three.
- Gate state: green at the live tip per the receipt (TestPrepRecovery 3 of 3; the new `testTimeoutMessageNamesLastSeenState` fails on base). The draft's Proof matches the receipt.
- Draft corrections owed: remove the INTERNAL comment block; add the Limits bold opener.

### SOLR-8554 (audit only, not ready): DISCUSS on shipping the narrowed change

- Gate state: NO GATE per the receipt. The audit's central finding stands: the branch does not do what the ticket asks. The ticket asks to move RebalanceLeader and ForceLeader into `OverseerCollectionMessageHandler`; the branch changes only the FORCELEADER wait loop, and its own note says the move is not attempted (part k5, finding 2). The new test does not compile on base, so a fail-before run is inconclusive by construction (finding 5).
- DISCUSS: ship the narrowed change now under the corrected title, or hold the branch until the Overseer move is attempted. Recommendation: ship the narrow change. Its title becomes "SOLR-8554: FORCELEADER returns an error when no active leader appears or the shard is removed", the changelog's NullPointerException phrase is corrected (finding 3), and Limits states that the move and the async option are not done and the ticket stays open for them, that a timed-out FORCELEADER still makes its shard term changes first, and that a timeout now returns a 500 where main returned success (findings 2, 4, 6). The call is not taken; no gate is owed until it is.

### SOLR-8576 (draftable after one fix)

- ADOPTED: apply the alias-assertion fix from part k6, FIX 1 in this branch before opening. The assertion at `CollectionsAPISolrJTest.java` line 1188 checks for a collection named like the alias, not that the alias still resolves; the replacement text is in FIX 1. The edit moves the head, so a run of `CollectionsAPISolrJTest` at the new head is main-side work owed, and the draft's head, counts, and links update from that run.
- ADOPTED: keep the test-only changelog fragment; the draft's Changelog line cites it. ADOPTED: the class counts (25 tests, 1 skipped) appear only in this draft; the SolrCloud round's SOLR-16437 text must not restate them (part k6, NOTE 12).
- Gate state: green at the live tip per the receipt for the head as it stands; the new test is a pin that passes on base too, and the draft says so plainly. The green does not carry to the fixed head.
- Draft corrections owed: remove the INTERNAL comment block; add the Limits bold opener.

### SOLR-8628 (audit only, held): DISCUSS on the route

- Gate state: NO GATE per the receipt. The audit's findings stand: the ticket's empty-directory case already works on main (`CachingDirectoryFactory.exists` returns true only when the directory has an entry), and the branch covers only a directory whose one file is `write.lock` (part k5, finding 17). The crash premise is unverified: whether Lucene 10.4.0 writes its first segments file on open with `OpenMode.CREATE` was not checked (finding 18).
- DISCUSS: pursue the Solr-side fix for the `write.lock`-only directory, or the Lucene-side route a 2017 ticket comment argues for. Recommendation: hold the branch. A premise run first shows whether the `write.lock`-only state is reachable at all; if it is, the branch drafts under the corrected title in finding 17 with a Choice naming the Lucene route. The call is not taken and no gate is owed until it is.

### SOLR-9750 (draftable)

- ADOPTED: the draft stands. Its Choice (plain rename against a one-release `_ADMIN_GRAPH` fallback) is a real decision with a live alternative and stays posed to the maintainer.
- Branch correction owed: the changelog fragment must state the upgrade step; the replacement text is in part k4, finding 5 (users who set `/graph` defaults under `_ADMIN_GRAPH` must rename that paramset to `_GRAPH`). The draft's "What this change does" already states it; the fragment catches up at packaging.
- ADOPTED: no history rewrite for the two handoff-doc commits (part k4, finding 6); the net diff at the head is clean. See the cross-cutting item.
- Gate state: green at the live tip per the receipt (11 focused tests; the new `testGraphHandlerParamsetName` fails on base). The draft's Proof matches the receipt, and its Limits note that the test reads the resource rather than sending a request.

### SOLR-11431 (live PR, consistency pass)

- Consistency result: consistent on gate state. The live PR head `968fad873c4b` matches the branch tip and the receipt (gate green at the live tip, TestCoreContainer 25 tests, 3 skipped; the base run fails with "expected:<503> but was:<500>"). No gate owed.
- Corrections owed (live PR text): the Limits sentence claiming CloudSolrClient "treats a 503 like a 404 in its stale-state retry" is not accurate by reading; a 503 enters the communication-error block and the 404 path is separate. The replacement text is in part k3, finding 1. Two Proof spots carry process wording ("in this round"); replacements are in finding 2. Optional, from findings 7 to 9: the bare "#5010" reference as a full link, file citations as links at the head SHA, and a trim toward the length guide. The read returned no maintainer reviews or comments on the PR, so the description edits are not blocked on maintainer activity; they are recorded here as owed, not applied in this pass.

### SOLR-11939 (drafted, docs only)

- ADOPTED: draftable as a review question, as the receipt frames it: a docs-only branch needs no test gate, and none is owed. The draft's account checks out against the code (part k5, findings 21 and 22).
- ADOPTED: the ADDREPLICA `property.name` behavior stays a Limits line in the draft with the follow-up offer, as drafted; the branch note's claim that ADDREPLICA gives no way to pick a name is wrong on main and is corrected in the internal record only.
- Branch correction owed: drop the root `SOLR-11939-TESTING.md` before opening.
- Draft check: sections open with bold summaries, the Proof states plainly that no test applies, and the draft names its head `d4cff5e76430`. No corrections owed to the draft itself.

### SOLR-12007 (drafted): DISCUSS on the design route

- DISCUSS: the synchronous-cleanup route the branch implements, against keeping a background cleanup with ordering guards so the factory close waits for it. The receipt records this as the branch's one open design question, and the draft poses it as a Choice. Recommendation: keep the synchronous route. It makes the order simple and provable, the reload path still uses the background thread, and the cost (close lasts as long as the cleanup) is stated in the draft's Limits. The call is not taken; the draft holds until the owner confirms the route.
- Gate state: green at the live tip per the receipt (SolrCoreCleanupOnCloseTest 1 of 1, SolrCoreTest 9 of 9, DirectoryFactoryTest 3 of 3, TestCoreContainer 25 tests, 3 skipped; the premise step fails on base as designed). The draft's Proof matches the receipt.
- Draft correction owed: the Limits section opens with bullets; add the bold one-line summary opener.

### SOLR-12849 (live PR, consistency pass: gate owed at the tip)

- Receipt wins: the gate covers `ed73600d877` (AliasPostBodyTest 3 of 3), and that head is not an ancestor of the live tip `6b92223bc24f`; the gated commits were rewritten. The live tip adds the only discriminating test class (`HttpSolrCallCollectionParamTest`, 2 tests) and a fourth `AliasPostBodyTest` test, and no receipt records a run of either at the tip.
- Owed (main side): a gate at `6b92223bc24f` (focused classes `HttpSolrCallCollectionParamTest` and `AliasPostBodyTest`, tidy, Error Prone compile, module check; part k2, finding 1). Until it lands, the PR body's Proof counts are not in any receipt and must not be relied on; after it lands, the Proof is corrected from the new receipt (a description edit, checking maintainer activity first).
- No draft, correctly. No owner call on behavior; the gap is proof only.

### SOLR-12916 (audit only, held): DISCUSS on the XML path change

- Gate state: NO GATE per the receipt. ADOPTED from the audit: the premise holds on main (the nested Config API form is still dropped; part k1, finding 9), and the branch note's three open guesses stay open until a run settles them.
- DISCUSS: the branch also changes the XML path (an even run of plain `<str>` values in a queries list now becomes a warming query instead of being logged and dropped), which is wider than the ticket's Config API framing. Recommendation: keep both paths, with the changelog title corrected to name both (replacement in part k1, finding 8) and the behavior stated openly in any draft. The call is not taken.
- Owed before any draft: remove `SOLR-12916-TESTING.md` from the branch, a premise run and first gate at a cleaned head, and a Config API round trip or a stated limit. Landing order: after SOLR-13246 (the QuerySenderListener hunks do not overlap; part k1, finding 12).

### SOLR-13097 (live PR, consistency pass: record corrected, gate owed)

- Record disagreement: the receipt says the PR "was closed out on 2026-10-06 and awaits the reviewer's re-review". The live check in part k2 shows the PR open, review decision CHANGES_REQUESTED (janhoy, 2026-10-06), and CONFLICTING with main. Gate state is the receipt's business; PR state is not, and on PR state the live check wins. A receipt correction is owed (replacement in part k2, finding 3).
- Receipt wins on gate state: gated (fixed, green) at `38e306c2cd5`, with the gate's fix pushed as `8cc61e00e60`. Neither is an ancestor of the live tip `f0e7395f58f3`; the same fix is in the live history as `e869956cdf4`. The tip's test class has 5 test methods, the gate covered 3, and the two added tests (`testV2RequestUsesCoreScopedRules`, `testWildcardRuleAppliesOnlyWhereNoScopedRuleGoverns`) have no run. The PR body's "5 of 5" and base failure counts are not in any receipt.
- Owed (main side): resolve the upgrade-notes conflict (delete the duplicate "Solr 10.2" heading and place the Security block as in part k2, finding 4; which section it belongs in follows the backport call in that finding), then a gate at the live tip, then correct the PR body's Proof and its one process-wording line (finding 6 replacement).
- DISCUSS: the PR's Limits states that a credential-free sub-request reaching a core with no governing permission "can return its documents" when `blockUnknown=false`, and calls that the plugin's designed behavior, "not a gap this PR opens". That probe is not on disk and the claim is unverified (part k2, finding 13). Recommendation: soften the statement to what is verified, or take it out, until the path is checked. The call is not taken. Whether the PR stays open for janhoy's re-review is the owner's standing business and is unchanged by this pass.

### SOLR-13246 (draftable, PR-ready)

- ADOPTED: the draft stands. No Choice is owed, per the receipt; Limits lines are as drafted, including the unchanged `SolrCore` searcher log line the audit names.
- Gate state: green at the live tip per the receipt (focused class 4 of 4; the gate fixed two received defects, recorded in the receipt). The draft's Proof states the base failure from the test's own last check; the per-test base failure line is not in the record, and the draft does not quote one.
- Recorded warning: the local `solr-13246-submit` branch (`065b36710adc`) holds the defective received version (the wrong `getName()`), is not a fast-forward of the live head, and carries a local handoff file (part k1, finding 1). It is never pushed; the PR head is `6817c6c0c267`.
- Draft correction owed: the Limits section opens with bullets; add the bold one-line summary opener.

### SOLR-14098 (held, no draft): DISCUSS on the symptom and the publish

- Gate state: reconciled green at the live tip per the receipt (unit test 5 of 5, SolrCloud end-to-end 2 of 2), with the receipt's own caveat standing: the record is a ledger table row with no per-branch gate log. No base run is recorded, so no draft may claim a failing base run (part k5, finding 26).
- DISCUSS: the fix covers only the BUFFERING case with an empty buffer. The handoff's symptom (a replica stuck in RECOVERING with the log already ACTIVE) is refused by the op and is not changed by this branch (finding 23), and the ticket text is not on disk to settle which symptom the ticket reports. Publishing ACTIVE for a RECOVERING replica with an empty buffer is also a live choice against fixing the recovery path (finding 27). Recommendation: hold. The branch becomes draftable when the owner confirms the BUFFERING case is the ticket's symptom and accepts the publish, with Limits naming the publish-failure divergence (finding 28). The call is not taken.
- Receipt correction owed: the ledger row date (2026-10-02) conflicts with the head's committer date (2026-10-03); the main side confirms the row date or records that the row was updated later (finding 25).

### SOLR-15003 (held, no draft)

- Receipt wins, with a correction: gated green at `8f5b6f8360a` (TestReplicationHandler 3 of 3, proof passed). The receipt says the tip moved twice; the audit counts three commits past the gated head, and the receipt understates it. The three are `afeab98ea82` (a production change in `IndexFetcher.java`: cleanup runs on the current core after a reload), `5c6e8753a39` and `1004abee39a` (test-only; the class has four new tests at the tip and the fourth, `testFullCopyWithReloadRemovesOldIndexDir`, has never run). A receipt correction is owed (replacement wording in part k6, FIX 8).
- Owed (main side): a gate at the live tip `1004abee39ab` before any opening (the class is nightly; any draft states that it runs only with nightly tests on). Not drafted, correctly.
- Cross-file, stated once: this round owns the ticket (also filed under Replication and backup). The branch's `SolrCore.java`, `IndexFetcher.java`, and `TestReplicationHandler.java` hunks do not overlap the named neighbors' hunks (part k6, NOTE 11).

### SOLR-15024 (draftable, one open check)

- ADOPTED: ship as gated. The branch stays scoped to `charFilters` only, as the gate narrowed it; the wider filters question is the draft's Limits line with the follow-up offer, and token filters are not added in this branch. The draft's Choice (ordered list against keys with an index) is a real decision and stays posed to the maintainer.
- ADOPTED: the two optional audit fixes are not taken (part k1, findings 5 and 6: the stale `lst` assertion in the test, and the changelog's client-effect claim). Each would move the head and force a re-gate; the assertion still passes and checks a zero count either way. They are recorded here if the branch is ever touched again.
- Owed before posting: confirm the ticket text describes duplicate char filter keys in the Luke output. No Jira packet for this ticket is on disk (part k1, finding 4); the draft's "What happens today" rests on the code. If the ticket describes only the Admin UI symptom, the summary line changes.
- Gate state: green (hardened) at the live tip per the receipt (LukeRequestHandlerTest 9 of 9, proof passed). No rebase before opening, per the SOLR-4989 item. Landing order: after 4989.
- Draft corrections owed: the Choice section and the Limits section both open without a bold one-line summary; add both openers.

### SOLR-15805 (draftable): DISCUSS on the history rewrite

- DISCUSS: approve a history rewrite of the fork branch that drops the `Co-Authored-By: Claude` trailer from commit `b2a463cf64f` (part k4, finding 1). Recommendation: approve the rewrite. Trailers of that kind are barred by the owner's standing rule, and the branch is unopened. Not executed here.
- ADOPTED: the draft stands otherwise. Its Choice (fail the context at startup against keeping it up with a 503) is a real decision and stays posed to the maintainer. The Limits line that the test accepts any `RuntimeException` stays (part k4, finding 9).
- Receipt correction owed: the receipt's proof PASS does not record the base failure text. The replacement line is in part k4, finding 10 (by code reading, the first assertion fails on base because no exception is thrown; the main side confirms from `g15805-harden.log`). Until that confirmation lands, the draft's Proof sentence about the base run rests on reading, and the draft-fix pass rewords it if the log does not confirm it.
- Gate state: green (hardened) at the live tip per the receipt (CoreContainerProviderTest 1 of 1). Interaction: SOLR-15805 and SOLR-16887 edit different hunks of `CoreContainerProvider.java`; a trial merge is clean and either order works (part k4, finding 11). 15805 is independent of the lifecycle cluster (finding 12).

### SOLR-16108 (held, no draft): DISCUSS on the disposition

- Gate state: green at the live tip per the receipt (SplitHandlerTest 5 of 5), with the proof inconclusive by construction, recorded as such: the added tests call the new production code, so they do not compile against unpatched code.
- The audit's central finding stands, and the round 28 review reached the same result: the branch does not fix the reported case. With one route value for every document, the splitter places all documents by one hash, so they land in one half by construction (part k4, finding 2). No PR text may claim the reporter's distribution is fixed.
- DISCUSS: hold the branch, narrow the ticket to distinct route values, or close it. Recommendation: hold and do not open. The change (split ranges built from route-field values) is real for the distinct-value case, but it does not fix the case the ticket reports, and the disposition is a ticket-posture call. Not taken.
- Conditional branch corrections, only if the branch is kept after the call: the changelog title replacement and the two Limits lines in part k4, findings 2 and 3 (one route value still lands in one half; `splitByPrefix` no longer reads `id_prefix` buckets when `router.field` is set), and removal of the `Co-Authored-By: Claude` trailer from the head commit `70ad7371b31`, which needs the same rewrite approval as SOLR-15805.

### SOLR-16499 (audit only, not ready): DISCUSS on the route

- Gate state: NO GATE per the receipt; the GitHub corroboration run of `ReplaceNodeAPITest` at the head does not settle gate state, and the receipt and the report agree on that.
- DISCUSS: plumb `parallel` and `timeout` through REPLACENODE (the branch's route), or remove the parameters from the guide and the code (the ticket's other route). Recommendation: plumb, as implemented; a 2022 comment from Noble Paul on the ticket supports it. The call is not taken.
- Owed regardless of the call's outcome on the implemented route: a first gate (main side); the reference guide's timeout default corrected (the guide says 300 seconds, the code uses 600; part k5, finding 8); the v2 request description corrected (the wait is not per replica move; finding 9); the behavior change stated in any draft (both parameters now take effect; finding 10 wording); and a check whether the changed v2 model needs generated OpenAPI or SolrJ artifacts regenerated (finding 12, unchecked). Packaging uses the origin tip `6a2ff7618d9a`, not the stale local branch (finding 13), and drops the root `SOLR-16499-TESTING.md`.

### SOLR-16725 (draftable)

- ADOPTED: strings, as implemented and as the draft's Choice poses the numbers route to the maintainer.
- ADOPTED: `maxShardsPerNode` stays a Limits line with the follow-up offer, exactly as the draft already carries it (part k6, FIX 5 is satisfied in the draft). It is not normalized in this PR.
- Branch corrections owed (author edits at packaging): a one-line comment in the test saying why it builds the source collection with a raw request instead of the SolrJ helper (part k6, NOTE 3), and the `ClusterStatus.java` javadoc rewording in NOTE 4 (the restored-collections half comes from the ticket, not from code). Either edit moves the head, so a top-up run follows at packaging.
- Gate state: green at the live tip per the receipt (LocalFSCloudIncrementalBackupTest 7 tests, 1 skipped; on base, `testCustomProperties` fails expecting String but was Long). The draft's Proof matches the receipt.
- Draft corrections owed: remove the INTERNAL comment block; add the Limits bold opener.

### SOLR-16849 (draftable as a regression test): DISCUSS on opening it

- DISCUSS: open the regression-test PR, or close the ticket as fixed by SOLR-18083. Recommendation: open the PR. The design record on the main side allows closing the ticket as fixed once the test is in a PR, the pin is the only coverage of the read-only segment path, and the draft's Proof states the pin honestly. The call is not taken.
- Gate state: green at the live tip per the receipt (SegmentsInfoRequestHandlerTest 7 of 7); the new test passes on unpatched main by design, and the draft says so. No changelog, test-only.
- Owed if the PR goes ahead: reword the head commit's subject and body (replacements in part k4, finding 13; a history rewrite, needing the owner's OK with the call), and the main side cancels or marks not-applicable the queued fail-before job for this ticket (finding 14), since the test passes on base by design and that stage would report NOT_PROVEN and stay queued.
- Draft check: no corrections owed to the draft itself.

### SOLR-16887 (held; the audit disagrees with the receipt on readiness)

- Receipt wins on gate state: green at the live tip (BATS counts from the BATS logs; the premise leg fails at the flag assertion as designed). The receipt calls the branch PR-ready. The audit disagrees on readiness, and this pass sides with the audit on readiness only: the branch drops `-XX:ErrorFile` from `bin/solr` and `solr.cmd`, which moves the native-crash file location for every JVM fatal error, the ticket does not ask for it, and neither the changelog nor the guide mentions it (part k6, FIX 6).
- ADOPTED: restore `-XX:ErrorFile` in both scripts and keep it in the guide example (replacement lines in FIX 6), and rename the BATS test so its name does not claim an OOM it never triggers (FIX 7 replacement). Neither is a close call, so no DISCUSS: the ticket asks for Exit instead of Crash on OOM, and nothing else.
- Owed (main side): after the fixes land, a BATS focused run at the new head; the current counts apply to the old branch only. No draft exists yet; a draft follows the fixes, and it includes the heap-dump Limits line from part k6, NOTE 8 (heap dump options are unchanged; checked by hand in the ticket, no test covers it). Interactions are in NOTE 9: the `bin/solr`, `solr.cmd`, and BATS hunks overlap no other branch's, and the `CoreContainerProvider.java` hunk is clear of SOLR-15805's.

### SOLR-17297 (drafted, held): DISCUSS on widening the fix

- DISCUSS: widen the fix to cover the reverse order, or ship the narrow fix with the reverse order as a named follow-up. The reverse order (a module SPI orphaned when sharedLib setup later closes the module classloader) was confirmed by run at this head, per the receipt; the probe was not committed. Recommendation: ship narrow, as drafted. The draft's Choice poses the widening to the maintainer and its Limits names the gap with the follow-up plan. The call is not taken; the draft stays held until the owner rules.
- ADOPTED: any PR text states the precedence flip the branch currently does not state (when a module and a shared lib jar provide the same class name, the module copy now wins; part k3, finding 6). The draft already states it; it stays.
- Gate state: green at the live tip per the receipt (TestCoreContainer 26 tests, 3 skipped, proof passed; a 2026-10-07 tidy audit re-checked the tip clean).
- Owed (main side): fill the draft's two Proof placeholders from the main-side record: the observed base failure line (the receipt records only PASS; check `g17297-harden.log`) and the reverse-order probe result (`g17297r35-probe.log`). If a line cannot be recovered, the Proof is reworded to the receipt's recorded outcome instead of quoting one. Optional at the same touch: the shorter changelog title in part k3, finding 17.
- Draft corrections owed: add the Limits bold opener; remove the duplicate Changelog line inside "What this change does". Landing order: 17297 before 17377 (see its entry).

### SOLR-17377 (drafted, draftable)

- ADOPTED: option 1 stands and is not reopened. The branch moves the clusterSingleton class check to the end of the `NodeConfig` constructor, after the loader completes; the draft states that route with no Choice section, as the assignment directs.
- Gate state: green at the live tip per the receipt (41 focused tests: TestSolrXml 35, NodeConfigClusterPluginsSourceTest 3, TestContainerPlugin 3; on base, TestSolrXml runs 35 with exactly 1 failure, the new `testClusterSingletonClassFromSharedLib`, with the ticket's shape).
- Owed (main side): fill the draft's Proof placeholder with the observed base failure line from `g17377-premise.log`; if it cannot be recovered, the draft's existing shape wording stands, since the receipt supports the count and the shape. ADOPTED: no module-backed test is added before opening; the draft's Limits already discloses that only the shared lib path is tested. The unverified `package:`-prefix question (part k3, finding 20) is recorded as open and is not a blocker.
- Landing order: after SOLR-17297. The two `NodeConfig.java` changes textually conflict; the resolution is the three-line order in part k3, finding 21 (`initModules();`, then `setupSharedLib();`, then `validateClusterSingletonClasses();`), and 17377 is rebased onto 17297 with that result when both move.
- Draft correction owed: add the Limits bold opener.

### SOLR-17708 (draftable)

- ADOPTED: ship with the remote-route exception in Limits, as drafted. On the open identification question (part k2, owner decision 1): the receipt names a deliberate structural exception for one API family, the round 28 review names the same `ADMIN_OR_REMOTEPROXY` route as the remaining duplicate check, and the draft's Limits names that route. The three accounts agree, so the Limits line stands. ADOPTED: the unmatched-v2-path behavior change (no plugin call; Jersey's not-found response) stands as stated in the draft.
- Branch correction owed: the changelog title overstates the change (the admin-remote route still checks twice); the replacement title is in part k2, finding 7, with the YAML re-parsed at the new head. The fix moves the head, so the draft's head references update and a top-up gate at the new head is main-side work owed.
- Gate state: green at the live tip per the receipt (JaxRsSingleAuthorizationTest 2 of 2, BasicAuthIntegrationTest 1 of 1, AuditLoggerIntegrationTest 9 of 9; on base, `testJaxRsApiIsAuthorizedOnce` fails, expected 1 authorization but was 2). The draft's Proof matches the receipt, and no Choice is owed: the implemented route is the ticket's suggested one.
- Interaction: no textual overlap with SOLR-12849 or SOLR-13097 in `HttpSolrCall.java`, and pairwise trial merges are clean; any landing order works. If 13097 lands with 17708, its v2 test case is re-run on the combined tip (part k2, finding 10).

### SOLR-17731 (draftable after three fixes; draft held): DISCUSS on the fix's shape

- DISCUSS: the open owner call on this branch is the fix's shape: the shared class-level path as implemented, against a router precedence change (the ApiBag/PathTrie redesign the design record names). Recommendation: keep the local fix as implemented; the draft's Choice poses the router route to the maintainer, and the general redesign is a separate project. The call is not taken.
- Gate state: green at the live tip per the receipt (V2ResourcePathOverlapTest 2 of 2; on base both tests fail with RemoteSolrException 405).
- Owed before opening: run `ListAliasesAPITest` at the head (it changed with the fix and has no recorded run; part k6, FIX 2, main side); fix the malformed license header line in the two new files (FIX 3); reword the two comments that state the Jersey routing rule as fact (FIX 4 replacements, and the test-comment trim in NOTE 6); correct the changelog title, which says "reachable again" for endpoints that never worked (NOTE 5 replacement). Then the draft's head and links update. Receipt note owed: the receipt says tidy clean, but the two header lines stand on the branch as FIX 3 shows; the receipt is corrected to note the defect.
- Draft corrections owed: remove the INTERNAL comment block (its hold list matches the items above); add the Limits bold opener.

### SOLR-18010 (held, no draft)

- Receipt wins: gated (hardened) at `fadc5ee31e3` (BasicAuthStandaloneTest 1 of 1, proof passed). The audit shows what that head is: it only strips the `"":{"v":0}` marker, and the corruption the ticket is about stays in place there. The live tip `c3685bb37d9d` adds the actual fix (the edit lock and the temp file with atomic move in `SecurityConfHandlerLocal.java`, plus 257 lines of tests including the two concurrent-edit tests), and none of it is gated.
- ADOPTED: the live tip is the change to evaluate; the marker-only head is not submitted. Owed (main side): a gate at `c3685bb37d9d`, and reading the settling run the receipt points to in the takeover record before any claim about concurrent `security.json` edits is drafted. The review side could not read that record; the main side can, so the claim stays unsettled until it does.
- Branch correction held with the ticket: the changelog title claims more than is settled; the narrower replacement is in part k2, finding 12, applied only after the settling run is read. No owner call is ripe until the gate and the settling run are in.

### SOLR-18278 (retire candidate): DISCUSS on retiring

- Gate state: green at the live tip per the receipt (TestHttpSolrClientProvider 2 of 2), and the proof fails by construction, recorded plainly in the receipt: this is a behavior-preserving refactor, so the focused tests pass on base too. Any draft, if the owner decides against retiring, states that instead of claiming a failing base run.
- DISCUSS: retire the branch, or open it as a refactor PR with that honest Proof. Recommendation: retire. The audit agrees with rounds 7 and 12: the ticket is resolved as not a bug, and with Jetty on the classpath the diff changes no behavior (the provider still checks `instanceof HttpJettySolrClient.Builder` and casts; part k4, finding 16). The call is not taken; retiring a banked branch is the owner's.
- If the branch is kept against the recommendation: delete the comment at `HttpSolrClientProvider.java` lines 49 to 50, which is wrong given the cast at line 63 (finding 16). The round 7 conflict claim is stale and is not repeated: the upstream rewrite it cited is already in the branch base, and a trial merge onto main is clean (finding 17).

### SOLR-18317 (held, banked server variant only)

- Banked state confirmed. Gate state: NO GATE at the banked head `ae918a03fa7b`, per the receipt, and the branch is HELD. The combined branch was narrowed at a reviewer's request; the Admin UI half shipped on the submit branch and its PR merged; this server-side half returns later as its own PR only if a reviewer wants it. No draft, correctly, and no gate is owed while it stays banked.
- ADOPTED: keep it banked on those terms.
- Recorded for any future server PR (not owed now): compare the branch against its own base `0d2a4649c79`, not current main (against that base: 8 commits, 16 files). A future PR drops the three UI files and the UI changelog text, which already shipped, resolves the add/add conflict in `changelog/unreleased/SOLR-18317.yml`, and uses the server-only title in part k4, finding 19. Receipt correction owed: the earlier passing run (2026-09-27, 19 tests) covers an earlier seven-file package, not this head; the replacement receipt line is in finding 20. The receipt's "no gate at this head" stands.

## Cross-cutting adoptions

- Landing orders: LukeRequestHandler pair 4989, then 15024. QuerySenderListener pair 13246, then 12916 when it is ever drafted. Request-path trio (12849, 13097, 17708): any order merges; re-run 13097's v2 case on a combined tip. Lifecycle cluster: 11431's text fixes first (no code change), then 4502 once its TESTING file is out and its gate lands, then 5011 and 12007 in either order after their gates and calls, then 17297 after the owner's ruling, then 17377 rebased onto it with the three-line `NodeConfig` result. 15805 is independent of the cluster, and its `CoreContainerProvider.java` hunk does not collide with 16887's.
- ADOPTED: no rebase of the gated branches. They trail main with clean trial merges; the gates stand at the recorded heads. Audit-only branches are rebased only as part of their pre-gate packaging, when that work is directed.
- ADOPTED: no history rewrites for commit subjects that name handoff documents (SOLR-6438, 8576, 9750, 16887, 17731). The net diffs are clean and squash on merge is the maintainer's choice. Trailer removals are different, because the standing rule bars them: SOLR-15805 is a DISCUSS item, SOLR-16108's and SOLR-16849's are conditional on their calls.
- ADOPTED: every branch that still carries a `SOLR-<ticket>-TESTING.md` file gets it removed as part of packaging, before any PR (4502, 5011, 5262, 8554, 8628, 11939, 12916, 16499, and any root TESTING.md the audit found).
- ADOPTED: local branch refs on the review side's machine are stale or, for 13246 and 4989, defective or non-fast-forward. Nothing is ever pushed from them; PR heads are the live heads in the claim table.
- ADOPTED: the live PR bodies (SOLR-11431, 12849, 13097) are not restyled; only the factual and process-wording corrections listed per ticket are owed, and each edit checks maintainer activity first.
- Checked across all seventeen drafts: no draft names a Lucene version, so the cross-version rule is not triggered; and the SOLR-15003 cross-file with Replication and backup is stated once, in its entry.

## Draft corrections owed (draft-fix pass; not done in this pass)

1. Remove the `<!-- INTERNAL ... -->` blocks before posting: SOLR-6438, SOLR-8275, SOLR-8576, SOLR-16725, SOLR-17731.
2. Add the bold one-line summary opener where a section opens with bullets or plain text: Limits in SOLR-4989, SOLR-13246, SOLR-12007, SOLR-17297, SOLR-17377, SOLR-6438, SOLR-8275, SOLR-8576, SOLR-16725, SOLR-17731; the Choice and Limits sections in SOLR-15024.
3. SOLR-17297 and SOLR-17377: fill the Proof placeholders with the observed base failure line from the main-side record, or reword to the recorded outcome (see their entries). SOLR-17297 also has a second placeholder for the reverse-order probe result.
4. SOLR-15805: the Proof's base-failure sentence is confirmed from the gate log or reworded as a code reading (see its entry).
5. SOLR-8576: after the alias-assertion fix and its run, update the head, counts, and links. SOLR-17731: after its three fixes and the `ListAliasesAPITest` run, update the head and links. SOLR-17708: after the changelog title fix, update the head references.
6. SOLR-17297: remove the duplicate Changelog line inside "What this change does" (the Changelog line also closes the draft).
7. SOLR-15024: confirm the ticket text before posting (see its entry); if it describes only the Admin UI symptom, the summary line changes.
8. Checked and clean: titles (the drafts carry no separate title lines; the suggested titles in part k1 for 4989, 15024, and 13246 are accurate); no internal process vocabulary in any draft's PR text (the only match for a banned term is the test class name `AuditLoggerIntegrationTest` in SOLR-17708's Proof, which is a class name, not process wording); no em dashes; every Proof's numbers match its receipt (4989 10 of 10, 15024 9 of 9, 13246 4 of 4, 17708 2 of 2 / 1 of 1 / 9 of 9, 12007 1/9/3/25 with 3 skipped, 17297 26 with 3 skipped, 17377 41 across three classes, 9750 11 across three classes, 15805 1 of 1, 16849 7 of 7 as an honest pin, 6438 4 of 4 / 1 of 1 / 44 of 44, 8275 3 of 3, 8576 25 with 1 skipped as an honest pin, 16725 7 with 1 skipped, 17731 2 of 2); pins and inconclusive proofs are stated as such in 8576, 16849, and 11939 (docs only, no test applies); every draft names the head it was written against.

## Branch corrections owed (not drafts; for the lanes that touch the branches)

1. SOLR-17708: changelog title replacement (part k2, finding 7).
2. SOLR-9750: changelog upgrade-step wording (part k4, finding 5).
3. SOLR-17731: changelog title (part k6, NOTE 5), license headers (FIX 3), comment rewordings (FIX 4, NOTE 6).
4. SOLR-16887: restore `-XX:ErrorFile` in `bin/solr` and `solr.cmd` and the guide example; rename the BATS test (part k6, FIX 6 and 7).
5. SOLR-8576: alias assertion replacement (part k6, FIX 1).
6. SOLR-16725: test comment and `ClusterStatus.java` javadoc (part k6, NOTE 3 and 4).
7. SOLR-12916: changelog title naming both input paths (part k1, finding 8); TESTING.md removal.
8. SOLR-8554, if its DISCUSS call goes to ship: corrected title, changelog phrase, and Limits lines (part k5, findings 2 to 4 and 6).
9. SOLR-8628, if it ever drafts: retitle to the `write.lock`-only case (part k5, finding 17).
10. SOLR-16499: guide default and v2 description (part k5, findings 8 and 9).
11. SOLR-16108 and SOLR-16849: the conditional items in their entries.
12. SOLR-17297: shorter changelog title (part k3, finding 17), optional, at the next touch.
13. SOLR-15024: findings 5 and 6 not adopted; recorded in its entry if the branch is touched again.

## Receipt corrections owed (main side)

1. SOLR-15003: the moved-tip wording (three commits, one production, one unrun test; part k6, FIX 8 replacement).
2. SOLR-13097: the "closed out" line (part k2, finding 3 replacement), the fix-SHA mapping (`8cc61e00e60` in the record, `e869956cdf4` in the live history), and the 5-test gate owed at the tip (finding 5 replacement).
3. SOLR-12849: a gate-owed line at the live tip `6b92223bc24f` (part k2, finding 1 replacement).
4. SOLR-14098: the ledger row date against the head date (part k5, finding 25).
5. SOLR-15805: the base failure text line (part k4, finding 10 replacement).
6. SOLR-17731: note the malformed license header defect against the tidy record (part k6, FIX 3).
7. SOLR-18317: the earlier-run line (part k4, finding 20 replacement).

## Main-side work owed (gates first)

1. SOLR-12849: gate at the live tip `6b92223bc24f` (focused classes `HttpSolrCallCollectionParamTest` and `AliasPostBodyTest`).
2. SOLR-13097: gate at the live tip `f0e7395f58f3` (`CoreScopedAuthStandaloneTest`, 5 tests), after the upgrade-notes conflict is resolved in-branch.
3. SOLR-15003: gate at the live tip `1004abee39ab` (`TestReplicationHandler`, nightly tests on).
4. SOLR-18010: gate at the live tip `c3685bb37d9d`, and read the settling run from the takeover record.
5. SOLR-8576: run `CollectionsAPISolrJTest` at the new head after the alias-assertion fix.
6. SOLR-17731: run `ListAliasesAPITest` at the head, after its header and comment fixes.
7. SOLR-16887: BATS focused run at the new head after the ErrorFile restore and test rename.
8. SOLR-17708: top-up gate at the new head after the changelog title fix.
9. First gates, each with its premise run: SOLR-4502 (spec in its entry), SOLR-5011 (with the shared-schema scenario), SOLR-5262 (spec in its entry), SOLR-12916, SOLR-16499. SOLR-8554 and SOLR-8628 gates only after their DISCUSS calls.
10. Recover-or-reword checks: the base failure lines for SOLR-17297 (`g17297-harden.log`) and SOLR-17377 (`g17377-premise.log`), the reverse-order probe result for SOLR-17297 (`g17297r35-probe.log`), and the base failure text for SOLR-15805 (`g15805-harden.log`).
11. SOLR-16849: cancel or mark not-applicable the queued fail-before job (part k4, finding 14).
12. No gate owed: SOLR-11431 (gated at the live tip, live PR consistent), SOLR-11939 (docs only), SOLR-14098 (reconciled green; held on scope), SOLR-18278 (gated; retire call pending), SOLR-18317 (banked).

## DISCUSS list (recommendations recorded; calls not taken)

1. SOLR-12007: synchronous cleanup on close (implemented), or a background cleanup with ordering guards. Recommendation: keep the synchronous route.
2. SOLR-17297: widen the fix to the reverse order, or ship narrow with the follow-up named. Recommendation: ship narrow, as drafted.
3. SOLR-18278: retire the branch, or open it as a refactor PR with an honest pin Proof. Recommendation: retire.
4. SOLR-16849: open the regression-test PR, or close the ticket as fixed by SOLR-18083. Recommendation: open the PR.
5. SOLR-15805: approve the history rewrite dropping the co-author trailer from `b2a463cf64f`. Recommendation: approve.
6. SOLR-16108: hold, narrow the ticket to distinct route values, or close. Recommendation: hold; do not open.
7. SOLR-8554: ship the narrowed FORCELEADER change under the corrected title, or hold for the Overseer move. Recommendation: ship narrow.
8. SOLR-8628: Solr-side fix for the `write.lock`-only directory, or the Lucene-side route. Recommendation: hold pending the premise run.
9. SOLR-12916: keep the XML path change with the corrected title, or limit the change to the Config API. Recommendation: keep both paths.
10. SOLR-16499: plumb `parallel` and `timeout` (implemented), or remove the parameters. Recommendation: plumb.
11. SOLR-17731: shared class-level path (implemented), or a router precedence change. Recommendation: keep the local fix.
12. SOLR-13097: the `blockUnknown=false` Limits statement stands as written, or is softened until verified. Recommendation: soften or take it out until the path is checked.
13. SOLR-4502: the `create()` guard (implemented), or `load()` from the constructor. Recommendation: keep the guard.

## Openings slate (for the owner's per-opening approval)

Draftable once the draft corrections above land: SOLR-4989, SOLR-13246, SOLR-8275, SOLR-9750, SOLR-11939, SOLR-16725, SOLR-17377 (after its Proof line is filled), SOLR-17708 (after the changelog title fix and top-up), SOLR-15024 (after the ticket-text check). Draftable subject to their DISCUSS calls: SOLR-12007, SOLR-17297, SOLR-16849, SOLR-15805 (rewrite approval), SOLR-17731 (shape call plus its three fixes), SOLR-8576 (after the assertion fix and its run). Held: SOLR-12916, SOLR-14098, SOLR-15003, SOLR-16108, SOLR-16887 (until its fixes and BATS run), SOLR-18010, and the audit-only tickets SOLR-4502, SOLR-5011, SOLR-5262, SOLR-8554, SOLR-8628, SOLR-16499 pending their first gates. Live PRs: SOLR-11431 consistent with text corrections owed; SOLR-12849 and SOLR-13097 each owe a gate at the live tip before their Proof text can be relied on.
