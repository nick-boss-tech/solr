# Review confidence round 3, slice A1 (live PRs)

Read-only review of nine live apache/solr PRs against their fork branch tips, the pr-prepare receipts, and the code at each head. No PR body, comment, review, branch, or Jira item was changed. No builds, tests, or Gradle runs.

## Counts

- CONSISTENT: 2 (#4998, #5011)
- DRIFT: 7 (#4968, #4997, #5027, #5000, #5004, #5009, #5012)
- UNREAD: 0

## Verdict table

| PR | SOLR ticket | State | Head (equals fork tip) | Verdict |
|---|---|---|---|---|
| #4968 | SOLR-18178 | Open, ready | b75e7d4d3c4 | DRIFT |
| #4997 | SOLR-18391 | Open, draft; superseded by #5027 (per its comment) | 3000eeede7a | DRIFT |
| #5027 | SOLR-18391 | Open, ready, approved (dsmiley) | adcda10b501 | DRIFT |
| #4998 | SOLR-17539 | Open, ready | f9d201a278b | CONSISTENT |
| #5000 | SOLR-18129 | Open, ready, merge state CLEAN | 657e443d866 | DRIFT |
| #5004 | SOLR-16130 | Open, ready | 3c48dec4b79 | DRIFT |
| #5009 | SOLR-18482 | Open, ready | 49ca9099d8e | DRIFT |
| #5011 | SOLR-12849 | Open, ready | 6b92223bc24 | CONSISTENT |
| #5012 | SOLR-13202 | Merged (approved at c0aec3a7ae0) | c0aec3a7ae0 | DRIFT (main-side state noted) |

Checks run for every PR, in order: head against `git ls-remote` on the fork (all nine match; all head commits are in the local object store, so no fetch was needed); state; title; Proof numbers against `receipts/<TICKET>.md` on `origin/pr-prepare` (all eight receipts exist; the SOLR-18391 receipt covers the graceful-create branch #5027 only and says solr-18391-submit, which is #4997, is "not gated here"); citations against the head SHA; Limits and Choice against the code at head; Lucene versions; internal vocabulary; reviewers and automated comments; CI state.

## Findings

Each DRIFT lists the exact text, the fact it conflicts with, and the fix. NOTE items do not change the verdict.

### #4968 (SOLR-18178): DRIFT

1. Proof, first bullet: "`UploadConfigSetAPITest` 18/18, including `testSafeZipEntryPathRules`". The receipt records 20 of 20 at b75e7d4d3c4 at three seeds. No 18/18 run is on the tip. Fix: use the receipt's 20/20 at b75e7d4d3c4, or record the 18/18 run in the receipt first.
2. Proof, "Also at head": "`DownloadConfigSetAPITest` 5/5", "`TestFileSystemConfigSetService` 3/3", "`TestSchemaDesignerConfigSetHelper` 8/8". None is in the receipt. Fix: add these runs to the receipt with head and date, or drop the counts.
3. Proof, the two Windows bullets: "Windows run (2026-10-04, at c3b6ded623e): ... 3 tests, 0 failed", "2 of the 3 fail with backslash entry names", and "(3 tests, 1 failure)". The receipt says only that a Windows run of the class succeeded. It has no counts and no base comparison. Fix: record the Windows runs (head, date, counts) in the receipt, or drop the counts.
4. Proof head line: "Verified at head 61b8f767c34 on 2026-10-04". The PR head and the gated head are b75e7d4d3c4 (receipt and fork tip). Fix: name b75e7d4d3c4 for the count line; mark 61b8f767c34 as an earlier head wherever it is still used.
5. Internal vocabulary: "three runs at seeds 4968D15EA5E2, 4968D15EA5E3, and 7777777777771007"; "Seed 7777777777771007 is included because a GitHub run at the previous head failed"; "which is the rejection this round removes"; "fixed in this round by comparing entry names as strings". Seeds, run identifiers, and "round" are internal. Fix: remove the seed values and the word "seed" (for example "three repeated runs of the class"); replace "this round" with "in the later push" or the head SHA.
- NOTE (formula): Limits, "Pre-existing behavior; a separate ticket follows." States a commitment. The formula asks for an offer on request. Fix: "a follow-up ticket can be opened on request."
- Checked and consistent: title; the entry-name normalization and `/` directory exception (DownloadConfigSet.java and UploadConfigSet.java at head); the validation rules and the `uploadConfigSetFile` check; all named test methods exist at head; changelog file and type (fixed); the cleanup Limit (filesToDelete is built before the upload and deleteUnusedFiles runs after it); the unconditional backslash replacement; the child-of-root check in FileSystemConfigSetService.
- NOTE (formula): no file-citation links; length 6,904 characters.

### #4997 (SOLR-18391, draft): DRIFT

1. Proof: "Verified at head 3000eeede7a on 2026-10-04: the tree is tidy-clean, compiles with Error Prone enabled, and passes `:solr:core:check -x test`." No receipt covers this head, and the tip receipt says solr-18391-submit is "not gated here". Fix: cite the receipt for this head, or say the branch was not gated and drop the sentence.
- NOTE: the body does not say the PR is superseded by #5027. The author's comment of 2026-10-05 says so. Fix: add "Superseded by #5027." near the top.
- NOTE: "The production change is 17 lines in one file" is 17 lines added and 3 removed in PlacementPluginAssignStrategy.java. Fix: "17 lines added, 3 removed".
- Checked and consistent: the guard throws an AssignmentException that names the collection (PlacementPluginAssignStrategy.java lines 68 to 83 at head); the base catch in CreateCollectionCmd (AssignmentException leads to DeleteCollectionCmd cleanup and BAD_REQUEST) is unchanged at this head; the two test files were removed at the head commit, as the body says; title; changelog file exists.
- NOTE (formula): no file links, no bold summary lines.

### #5027 (SOLR-18391, graceful create): DRIFT

1. Citations (rule 4): all 42 file links point at `github.com/apache/solr/blob/e051032dd75847ce4ba2dca9f9e13407c8fec3c4/...`. The head is adcda10b501 on the fork (nick-boss-tech/solr). e051032 is an ancestor of the head. Since e051032 only CreateCollectionCmd.java has changed (6 lines added, 1 removed, starting near line 443), so every line from 448 on moved by +5. Example, the body's text: "[CreateCollectionCmd.java:515-516](https://github.com/apache/solr/blob/e051032dd75847ce4ba2dca9f9e13407c8fec3c4/...#L515-L516)". At head, lines 515-516 are the catch line and the interrupt check. The cleanup call is at 521. Fix: re-link every file citation to `github.com/nick-boss-tech/solr/blob/adcda10b501cbea9f92119bae8e91187da77d9da/<path>#L<a>-L<b>` with these head ranges:

   | Claim (as in body) | Body cites (e051032) | Head range (adcda10b501) |
   |---|---|---|
   | Catch for the whole method | 510 | 515 |
   | Cleanup call, guarded by stateWritten and interrupt | 515-516 | 520-521 |
   | Interrupt flag restored | 511-513 | 516-518 |
   | AssignmentException returns 400 | 521-524 | 526-528 |
   | "Could not create collection" message | 526 | 531 |
   | Bounded retry helper (ZooKeeperException only) | 537-565 | 542-570 |
   | Placement rejection response without delete output | 574-578 | 579-583 |
   | Cleanup failure logged at ERROR and suppressed | 583-587 | 588-592 |
   | Core failure message names first failure | 471-477 | 476-482 |
   | Alias write call | 500-507 | 505-512 |
   | Empty /collections node | 798-799 | 803-804 |
   | Auto-created configset copy | 721-724 | 726-729 |

   These keep their numbers at head (only the SHA and repo change): 103-104, 162, 211-212, 224, 233-236, 253-255, 259, 269-271, 399-402, 445. The citations to PlacementPluginAssignStrategy, DeleteCollectionCmd, CollectionHandlingUtils, and CreateShardCmd keep their numbers, because those files did not change after e051032.
2. Proof: "exactly two tests fail, in the ticket's shapes: testCleanupAfterUnexpectedPlacementFailure ... and testAssignForMissingCollection". The receipt's base rerun has three failing tests: CreateCollectionCleanupTest 2 of 5 (testCleanupAfterUnexpectedPlacementFailure, and testCreateDoesNotDeleteExistingCollectionOnStaleView, which gets 500 where the branch expects 400), plus PlacementPluginIntegrationTest.testAssignForMissingCollection. Fix: say "three tests fail" and name testCreateDoesNotDeleteExistingCollectionOnStaleView.
3. Proof, follow-up paragraph: "testCleanupAfterUnexpectedPlacementFailure is the one that fails." The receipt shows the stale-view test also fails at base. Fix: "testCleanupAfterUnexpectedPlacementFailure and testCreateDoesNotDeleteExistingCollectionOnStaleView fail; testCleanupAfterPlacementException passes."
4. Proof: "Verified locally 2026-10-05 at fca4235be87 (the PR head differs from it only by one later commit that removes an internal handoff document): CreateCollectionCleanupTest 4/4". Four commits separate fca4235be87 from adcda10b501: d129cc22d01 adds the stale-view test, 98dd6d06c5f trims the failure detail, e051032dd75 removes a 243-line gate handoff file, and adcda10b501 edits the changelog. CreateCollectionCleanupTest has 5 tests at head, and the receipt records 5 of 5 there. Fix: say the head is four commits past fca4235be87, drop "internal handoff document", and label 4/4 as an earlier-head count or drop it.
5. Proof: "Fork CI runs of all five suites on that same tree are also green." No receipt or run is cited. The PR's checks show gradle check, Crave tests, and the changelog check; no SolrJ run is listed. Fix: cite the runs, or drop "all five suites".
6. Internal vocabulary: heading "Round 29 follow-up, verified locally 2026-10-07 at adcda10b501"; "internal handoff document"; "in a scratch run". Fix: "Follow-up at adcda10b501"; drop the handoff phrase; "a local run with the check disabled".
- NOTE: the 2026-10-07 date for the follow-up is not in the receipt, which gives no gate date.
- Checked and consistent: the CreateCollectionCmd behavior described in What this change does and the Choices (stateWritten flag; one catch; cleanup failures logged at ERROR; interrupt guard; three attempts with a 200 ms pause; ZooKeeperException-only retry; 30 s and 120 s create timeouts; DeleteCollectionCmd refuses to delete a collection in an alias and waits up to 60 s; CreateShardCmd cleanup). Choice 2 is accurate: the PRS state write (211-212) throws before the flag is set, and the exists check (233-236) sits in the non-PRS branch. All named test methods exist at head. Changelog file exists.
- NOTE (formula): no bold one-line summary per section; length 18,892 characters (the big-PR allowance may cover part of it).

### #4998 (SOLR-17539): CONSISTENT (notes only)

- NOTE: the Proof counts (TestDocValuesIteratorCache 2 of 2, SolrCoreTest 9 of 9, TestConfigOverlay 2 of 2) match the receipt. The receipt records them at the gated head bce505f45acd. The PR head f9d201a278b is one docs commit later. The body names no head or date for these counts. Fix: name the head and date for the counts, or name the docs commit.
- NOTE: "Changelog: changelog entry under changelog/unreleased (added)" gives no file path. The file is changelog/unreleased/SOLR-17539.yml. Fix: give the path.
- NOTE (formula): no file-citation links.
- Checked: the query.enableDocValuesIteratorCache key and default (SolrConfig.java); both configsets set it (solr/server/solr/configsets/_default and sample_techproducts_configs); RealTimeGetComponent and SolrDocumentFetcher go through createDocValuesIteratorCache(); UpgradeCoreIndex.java line 173 builds a caching instance (the one-argument constructor defaults to caching); the four per-leaf arrays in FieldDocValuesSupplier; the ref guide pages changed. Not traced: the Config API reload path, and the getDocValues OOM statement from the ticket.

### #5000 (SOLR-18129): DRIFT

1. Proof: "`TestSolrConfigHandler`: 13 of 13 pass with this change." and "Re-verified at head 657e443d866 on 2026-10-06 ...: `TestUtils` 20 of 20 and `TestSolrConfigHandler` 13 of 13 pass". The receipt (round-29 gate at 657e443d866) records "TestSolrConfigHandler 33 tests SUCCESS". The class has 13 test methods at head. The body counts methods and the receipt counts 33 runs, and the two are not reconciled. Fix: state what each figure counts; keep the receipt's 33 with its meaning, or record the 13-method run in the receipt.
2. Proof: "`TestUtils` 20 of 20". The receipt has no TestUtils count. The class has 20 test methods at head. Fix: record the run in the receipt.
3. Title: "SOLR-18129: Keep multi-valued request-handler defaults on the Config API". The change covers defaults, appends, and invariants (the body's first paragraph; the receipt's changelog title). Fix: name all three sections in the title (a title edit is the author's call).
4. Internal vocabulary: "the array-form tests below were verified in a local gate re-run". Fix: "in a local re-run".
- NOTE: "The cost of that route is about 110 lines of new code in `CommandOperation`." The head adds 118 lines and removes 7 in CommandOperation.java. Fix: "about 120 lines added".
- NOTE: the first Proof line names head 72fd153d7bc (2026-10-04), an earlier head. The counts after it are at 657e443d866. Fix: name the current head with those counts.
- Checked and consistent: the accumulation is scoped to the three request-handler commands and the three sections; add-initparams and update-initparams stay last-value-wins; a repeated section stays last-one-wins; end-to-end tests post to "/config" only; the v2 config route goes through SolrConfigHandler and ApiBag.readCommands; the ref guide text on arrays matches the code.

### #5004 (SOLR-16130): DRIFT

1. Proof: "With the guard (already on main), `CrossCollectionJoinQueryTest` passes 10/10, the new test included." The class has 10 @Test methods at head, so the figure matches the code. The receipt for this branch records gate green and the fail-before pass, with no count. Fix: record the gate log count in the receipt, or cite the count the receipt holds.
2. Proof: "GitHub Actions corroboration run 37581056636 dispatched for the class at this head". This is a run identifier and an internal label. The receipt says the run "succeeded at this head". Fix: "The class also passed in a GitHub Actions run at this head."
3. Proof: "Fail-before, run on 2026-10-06 against `70c1a28995d`". The base SHA is correct (git confirms it is the parent of 56ec140e363), but the receipt does not record it. "Fail-before" is an internal label. The quoted NullPointerException text comes from a server log that is not on the tip. Fix: "Against the parent of the guard commit (70c1a28995d), run on 2026-10-06:"; add the base SHA and the log excerpt to the receipt.
- NOTE (formula): no changelog line. If a test-only change needs no changelog entry, this is fine; otherwise add changelog/unreleased/SOLR-16130.yml.
- Checked and consistent: the regression test (four matching documents, at least two leaves, ids to-0 through to-3); the ccjoin configset flag; only this class uses that configset; the #4953 references are to apache/solr and resolve; title accurate.

### #5009 (SOLR-18482): DRIFT

1. Proof header: "Verified on the fork's GitHub Actions test runner at head 8ef833b64cb on 2026-10-04, with Error Prone enabled". The PR head is 49ca9099d8e, a docs-only commit that removes one sentence. The receipt records the counts (TestJsonRangeFacets 10 of 10, TestJsonFacetErrors 5 of 5) as a confirmation run at 49ca9099d8e on 2026-10-06. Fix: "Verified at head 49ca9099d8e on 2026-10-06 (confirmation run)."
2. Proof: "On base code, its range-facet error cases fail" and "The same class also passes on base code". The receipt says the 2026-10-04 paired runs split, and "the per-run test class mapping is not recorded." The per-class base outcomes cannot be traced. Fix: drop the per-class base claims, or cite a base run by class.
3. Limits: "Only range facets are covered; other facet types still ignore parameters they do not support." The author's reply on the review thread (2026-10-06) says "I will open a follow-up ticket for the general check and take it on." The formula asks for the broader issue to be named in Limits with a follow-up offer. Fix: add: "Unknown or misspelled parameters are still ignored on every facet type. A general check is a follow-up; a ticket and PR can be opened on request."
- NOTE (plain language): "pin the accepted range-facet behavior" is an internal label. Fix: "keep the accepted range-facet behavior".
- Checked and consistent: FacetRangeParser at head (the seven names in the fixed order; message "<param> is not supported on range facets"; err() builds a BAD_REQUEST SolrException); other facet types unchanged; changelog file exists; the docs sentence removed as the review thread asked.
- NOTE (formula): no file-citation links; no bold summary lines; length 3,052 characters.

### #5011 (SOLR-12849): CONSISTENT (notes only)

- NOTE (plain language): "This is the discriminating test" and "end-behavior pins" (in the description and in the class javadoc) are internal labels. Fix: "This is the test that fails on the base code."
- NOTE: "That base comparison ran on the fork's GitHub Actions test runner" is not recorded in the receipt, which names the premise logs only. Add the runner to the receipt, or drop the detail.
- NOTE (formula): no file-citation links; no bold summary lines; length 4,500 characters.
- Checked and consistent: the HttpSolrCall change (a body-only collection parameter is kept and its aliases are resolved; the URL case is unchanged); SolrJ's DEFAULT_URL_PARAM_NAMES includes "collection"; V2HttpCall rejects a URL or path that resolves to several collections with BAD_REQUEST before addCollectionParamIfNeeded runs (lines 143 to 151 and 198); the core-based v2 path passes an empty list (getCollectionsList returns List.of() when collectionsList is null); the v1 call sits inside the handler branch; the receipt counts (AliasPostBodyTest 4 of 4, HttpSolrCallCollectionParamTest 2 of 2) and the base failure text match the body.

### #5012 (SOLR-13202): DRIFT (PR is merged)

1. What this change does: "`JoinQParserPlugin`, `ScoreJoinQParserPlugin`, and `CrossCollectionJoinQParser` now share one check, `ScoreJoinQParserPlugin.requireFromAndTo`". At head c0aec3a7ae0 the helper is also called from AuxIndexJoinQParserPlugin (line 411). The reviewer asked for this on 2026-10-06, and the author pushed it in c0aec3a7ae0. The old single SyntaxError naming both parameters is gone. Fix: name four parsers, and say auxIndexJoin now names the one missing parameter.
2. What this change does (missing scope): the reviewer also asked for GlobalOrdinalsJoinQParserPlugin. That plugin is unchanged; TestGlobalOrdinalsJoinQParser gains a missing-query case. Fix: add one line saying so.
3. Proof header: "Verified at head b3ded4da758 on 2026-10-04". The PR head is c0aec3a7ae0. The body's counts (TestJoin 5 of 5, CrossCollectionJoinQueryTest 10 of 10) match the receipt at b3ded4da758. The later scope extension is gated at c0aec3a7ae0 (the receipt names g13202ext-gate.log), but its per-class counts are not on the tip. Fix: add a line for c0aec3a7ae0 with counts recorded in the receipt, or state that the extension was checked separately.
- Main-side finding (not a body failure): the PR is MERGED (approved 2026-10-06 at c0aec3a7ae0). The receipt still says "awaiting merge". Update the receipt and the issue-log state.
- NOTE: the body lists three join methods for testJoinMissingFromToReturns400; the test also covers "score=none". Fix: add it, or leave as is.
- Checked and consistent: the helper messages and BAD_REQUEST; the missing `v` parameter is not checked by the change (true at head); changelog file exists; the receipt counts at b3ded4da758.

## Reviewer and automated comments

- #4968: epugh (issue comment, 2026-10-07) prefers failing the upload. The author replied that it is pushed: every entry name is validated before any write, and the tests, ref guide note, and changelog are updated. The body's Choice matches this. malliaridis (review, COMMENTED, on 61b8f767c34) asked for the same check in uploadConfigSetFile (the body says it is done, and the code has it), asked for a documentation note (the ref guide change is in the diff), and reports a Windows run that worked.
- #4997: dsmiley (review, 2026-10-02) asked for the mock-heavy tests to be dropped (done at 3000eeede7a). dsmiley's comment of 2026-10-04 asks for graceful collection-creation errors instead of a patch to the race; that is the scope of #5027. The author's 2026-10-05 comment says this draft is superseded (see the NOTE above).
- #5027: dsmiley APPROVED on 2026-10-07 at adcda10b501 ("The trade-offs/choices makes sense to me"). He plans to check test cost and may move tests to Nightly. No inline comments. No body change is needed for this.
- #4998, #5000, #5004, #5011: no reviews, no inline comments, no issue comments.
- #5009: janhoy (COMMENTED, 2026-10-05, at f09608f5, an older head). Inline: "Unnecessary line" (docs; removed in 49ca9099d8e, consistent with the receipt). Inline: a general request that unknown facet parameters return 400. The author replied that the general check is out of scope and offered a follow-up (the body does not name it; see Finding 3).
- #5012: mkhludnev (comment, 2026-10-06) asked for AuxIndexJoinQParserPlugin and GlobalOrdinalsJoinQParserPlugin in scope. The author pushed both (see Findings 1 and 2). mkhludnev APPROVED at c0aec3a7ae0. Later comments about milestones, 9.x, and 10.1 are main-side items, not body items.
- Automated comments: none found on any of the nine PRs. There was no bot finding to verify.

## CI state (statusCheckRollup at each head)

- All nine: "generate" (Generate Renovate Changelog) SKIPPED; "gradle check" SUCCESS; "labeler" SUCCESS; "Run Solr Tests using Crave.io resources" SUCCESS; "Check changelog entry" SUCCESS.
- #5000 also: "Run SolrJ Tests" SUCCESS.
- No FAILURE, PENDING, or action_required on any PR.
- mergeStateStatus is UNKNOWN on eight PRs (GitHub had not computed it), CLEAN on #5000, and not applicable on #5012 (merged).
- reviewDecision is APPROVED on #5027 and #5012; empty elsewhere.

## Formula shape (not verdict-driving)

- Bold one-line summary per section: none of the nine bodies opens a section with a bold summary line (presentation rule of 2026-10-08).
- File citations as links: only #5027 has links, and they are wrong (Finding 1). The other eight have no file links.
- Length (characters, live bodies): #4968 6,904; #4997 2,184; #5027 18,892; #4998 3,343; #5000 6,200; #5004 2,809; #5009 3,052; #5011 4,500; #5012 1,779. The guide is about 3,500.
- Changelog line: missing on #5004; #4998 gives no file path.
- Lucene versions: no body names a Lucene version. #5004 names a Lucene class inside a log excerpt only, so the Lucene rule is not triggered.

## Not checked

- Jira ticket text (not read in this run). Ticket-level claims, such as #4998's heap figures and getDocValues concern, were checked only against the code.
- No builds, tests, or Gradle runs. Windows and GitHub run results come from receipts and PR text; they were not re-run.
- #4998: the Config API reload path for the new attribute (EditableSolrConfigAttributes value 11).
- #5000: the claim that the JSON array form already works on base; the v2 Config route beyond SolrConfigHandler and ApiBag.readCommands.
- #5027: whether the system-collection guard and the alias-retry wiring have test coverage (checked for code presence only).
- Whether apache/solr resolves the fork-only SHA e051032 in the #5027 links.
