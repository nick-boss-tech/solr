# SolrJ and clients round 1, part S6: consistency and merged work (SOLR-10198, SOLR-15823 pair, SOLR-18129)

Consistency pass only. Read-only git (show, diff, merge-base, log, `ls-remote`, and `merge-tree` in its old form, which writes no refs) and `gh pr list` and `gh pr view` only. No builds, tests, drafts, edits, commits, pushes or posts.

## Live heads

`ls-remote origin` and `gh pr view` `headRefOid` agree on all four:
- `solr-10198-submit` `9448cda146f7e09f3389551c4ef883a63d415eeb` (PR 4965, MERGED)
- `solr-15823-submit` `1f60cd39baae5dddbefcf6127b25955dc3518578` (PR 5030, OPEN, base main)
- `solr-15823-levels-submit` `6fa54c4de8c9bbcc859cc57606ff7e4cb574dc81` (PR 5031, OPEN, base main)
- `solr-18129-submit` `657e443d866d5bdfd7b3d7f3448e074a2ae75714` (PR 5000, OPEN)

## Item 1: SOLR-10198, consistent with the receipt, one wording note

- `9448cda146f7` is not in main's history: `git merge-base --is-ancestor 9448cda146f7 upstream/main` returns 1 (`upstream/main` is `8e62c2686882`).
- Main carries the merged content under `e97c2a0884cbd68790fd7e7036fbb7800db27856`, the PR's merge commit per `gh`. That commit has one parent (`52fba69a4eb3`), so it is a squash commit committed by GitHub, not a two-parent merge.
- Content check: the five paths the branch changes (`changelog/unreleased/SOLR-10198.yml`, `EmbeddedSolrServer.java`, `DocsStreamer.java`, `TestEmbeddedSolrServerStreamingTypes.java`, `EmbeddedSolrNoSerializeTest.java`) are identical between `9448cda` and `e97c2a0884c` (`git diff` is empty). The full-diff patch-id is `ef955e5743ff` on both sides. No main commit after `e97c2a0884c` touches those paths.
- `gh pr view 4965`: state MERGED, `mergedAt` 2026-10-07T13:19:33Z, `mergedBy` dsmiley, `headRefOid` `9448cda146f7`. All match the receipt.
- The fork branch is still present: `ls-remote refs/heads/solr-10198-submit` is `9448cda146f7`.
- Receipt line 4 says "merge commit e97c2a0884cbd". Wording only: it is a squash. "Merged as-is" holds at the content level.

## Item 2: SOLR-15823 submit (PR 5030), gate move verified; one stale head name in the Proof; authorization model holds at the head

- The receipt's last gated head `85d8df82502` to the live head `1f60cd39baae` is exactly one added line: `configuring-logging.adoc` line 108 at the live head ("A node named in failedNodes is not guaranteed to be unchanged..."). This matches the receipt.
- `100ad2e0df69` to `85d8df82502` is two test files only (`NodeLoggingAPITest.java`, `NodeLoggingNodesSolrCloudTest.java`), with no production or UI code. This matches the description.
- Drift: the PR body's Proof says "Verified 2026-10-05 at head `85d8df82502`". The live head is `1f60cd39baae`, one docs commit later. The new sentence's substance is already in Limits bullet 3 (the change may already have applied on the nodes that responded).
- Authorization model, re-read at `1f60cd39baae`:
  - The receiving node checks once: `solr/core/src/java/org/apache/solr/handler/admin/api/NodeLogging.java` lines 91 to 92, `@PermissionName(CONFIG_EDIT_PERM)` on `modifyLocalLogLevel`.
  - Fan-out: `RemoteRequestProxy.java` lines 135 to 137 send through the default client. `HttpJettySolrClient.requestAsync` (`solr/solrj-jetty/.../HttpJettySolrClient.java` lines 377 to 389) builds and sends synchronously on the caller thread.
  - Identity: `PKIAuthenticationPlugin.java` lines 377 to 396 (`getUser`) return `NODE_IS_USER` ("$") when there is no `SolrRequestInfo` on the thread and the thread carries the server flag. The ADMIN action sets no `SolrRequestInfo` (`HttpSolrCall.java` lines 729 to 733, `handleAdminRequest`; `setRequestInfo` only at line 516, on the PROCESS path). The server flag is set by `SolrServlet.java` line 112. The ADMIN action is `V2HttpCall.java` lines 211 to 215.
  - Receiving side: `PKIAuthenticationPlugin.java` lines 157 to 160 map "$" to `CLUSTER_MEMBER_NODE`. `needsAuthorization` (lines 371 to 372) returns false for it. `HttpSolrCall.java` lines 608 to 619 (`shouldAuthorize`) and `solr/core/src/java/org/apache/solr/jersey/SolrRequestAuthorizer.java` lines 93 to 100 skip authorization for it.
  - Verdict: "the sending node's own identity; the receiving node does not re-authorize" holds at the head.
  - Dependency to note, no action: if the endpoint ever ran on the PROCESS path, which sets `SolrRequestInfo` with the caller's principal (`SolrRequestInfo.java` lines 154 and 164), the token would carry the caller, and the receiving node would re-authorize.
- CI at the head: all completed checks SUCCESS (gradle check, Admin UI browser tests, Solr Tests via Crave.io, New UI tests, Validate Changelog); generate SKIPPED. `mergeStateStatus` UNKNOWN.

## Item 3: SOLR-15823 levels (PR 5031), stacking is stated loosely; drift

- `git merge-base --is-ancestor 1f60cd39baae 6fa54c4de8c9` returns 1, so the levels head does not contain the live submit head. Neither does `85d8df82502` (also 1).
- The levels branch's base for the submit line is `100ad2e0df69` (the merge-base of the two heads). Levels adds `8927d37b394`, `caf3dbf4d8d` and `6fa54c4de8c` on top of it.
- Receipt: "stacked on the superseded #5030 head and pending its restack". The description says "it is stacked on that PR's branch and is meant to land after it." It does not name the base, say the base is superseded, or say the restack is pending. The 5030 body also says "a separate follow-up PR stacked on this one" in the present tense (Choice 3).
- `baseRefName` is main. Until 5030 merges, the 5031 "Files changed" view shows all ten 5030 files. The description does not say so.
- Code citations spot-checked at `6fa54c4de8c9`: `NodeLogging.java` lines 73 to 74 (CONFIG_READ_PERM on the GET), `logging.js` line 129 (`listAllLoggersAndLevels({}, function ...)`), `RemoteRequestProxy.java` lines 135 to 137, `PKIAuthenticationPlugin.java` lines 371 to 372. Consistent.

**Restack simulation** (`git merge-tree`, read-only; levels onto `1f60cd39baae`, base `100ad2e0df69`): three conflict hunks, all in `NodeLoggingNodesSolrCloudTest.java` (the `getLogLevels` helper signature, its `ModifiableSolrParams` argument, and the matching call-site argument). `NodeLoggingAPITest.java` and `configuring-logging.adoc` are changed on both sides and merge cleanly in the simulation. The restack is main-side work; this is information only.

## Item 4: shared files of the 15823 pair, overlap confirmed

- Against each head's merge-base with main (`e432df19c4a5`), both heads change the same ten files: `changelog/unreleased/SOLR-15823.yml`; `solr/api` `NodeLoggingApis.java` and `LoggingResponse.java`; `solr/core` `LoggingHandler.java`, `NodeLogging.java`, `NodeLoggingAPITest.java` and `NodeLoggingNodesSolrCloudTest.java`; `solr-ref-guide` `configuring-logging.adoc`; and `solr/webapp` `logging.js` and `services.js`. Overlap 10 of 10. `NodeLoggingApis`, `LoggingResponse` and `LoggingHandler` are all in it.
- Within the pair (base `100ad2e0df69`): levels edits seven of those files (`NodeLoggingApis`, `LoggingHandler`, `NodeLogging`, both NodeLogging tests, `configuring-logging.adoc`, `logging.js`). Submit edits three after `100ad2e0df69` (`NodeLoggingAPITest.java`, `NodeLoggingNodesSolrCloudTest.java`, `configuring-logging.adoc`). Those three are the true shared-edit set. Only `NodeLoggingNodesSolrCloudTest.java` conflicts in the simulation. `LoggingResponse.java`, the changelog and `services.js` reach levels only through the inherited submit commits.

## Item 5: SOLR-18129 (PR 5000), head consistent with the receipt; the receipt omits the head's last commit

- The head `657e443d866d` matches the receipt, `ls-remote` and `gh` `headRefOid`. The PR is OPEN, with `headRefName` `solr-18129-submit`.
- `changelog/unreleased/SOLR-18129.yml` line 1 matches the receipt's quoted title exactly.
- Gap: the head's last commit, `657e443d866` "SOLR-18129: shorten the changelog title" (committed 2026-10-07 03:40:23 +0000, which is 21:40 on 2026-10-06 at -0600), changes that title line. Its parent `f5715964a09` "quote the changelog title so it parses as YAML" (2026-10-05 11:46 -0600) is the commit the receipt describes. Code is unchanged between them: `git diff --stat f5715964a09 657e443d866d` shows one file, one line changed.
- The receipt says the head "has not moved since the round 29 re-review" (line 4) and that the re-review gate "ran green at the PR head" (line 5). Git cannot settle whether the gate ran before or after `657e443d866`. The round 29 log is not under `research\`.
- CI at the head: gradle check, Run SolrJ Tests, Solr Tests via Crave.io and Validate Changelog are SUCCESS, with runs started 2026-10-07 20:06 to 22:22 UTC (after the head commit). generate SKIPPED.
- The local ref `solr-18129-submit` is `3ddacabe7e0`, five commits behind the live head. This is stale local state, not the PR head.
- The PR title "SOLR-18129: Keep multi-valued request-handler defaults on the Config API" reads differently from the changelog title. Noted only.

## Receipt disagreements (exact wording)

1. **SOLR-10198 receipt, line 4:** "merge commit e97c2a0884cbd". Git shows a single-parent squash commit. The content is unaffected.
2. **SOLR-15823 receipt, line 5:** "stacked on the superseded #5030 head and pending its restack". Accurate, but it does not name the superseded SHA, which is `100ad2e0df69`. This is a gap, not a conflict.
3. **SOLR-18129 receipt, line 4:** "the head has not moved since the round 29 re-review", and line 5: "fixed by quoting the changelog title, which renders identically". The head has a later commit (`657e443d866`, which changes the title text, not only the quoting) that the receipt does not mention. The timing is unsettled here.

## Owner decisions (each public act is the owner's)

1. **Delete the merged fork branch** `solr-10198-submit` (ref at `9448cda146f7`)? The content is on main as `e97c2a0884c`. The local ref `solr-10198-submit` points at `7b1bf343465`, a separate stale ref.
2. **Restack `solr-15823-levels-submit` onto `solr-15823-submit`** (`1f60cd39baae`). Option A: restack and push, which rewrites the levels head (an owner-authorized force push), with a fresh gate at the new head (owed, not run here) and the 5031 Proof refreshed. Option B: keep the stack on `100ad2e0df69` and say so in the 5031 description. Recommendation: A, since the simulated conflict is confined to one test file.
3. **The 5031 base:** keep main (the description then says its diff includes 5030 until 5030 merges), or retarget to `solr-15823-submit`. This is a public change to the PR base.
4. **Public text:** the 5031 stacking sentence should name the base SHA and the restack status, or be updated at the restack. The 5030 Proof should name `1f60cd39baae`, or say the last change is one docs sentence.
5. **SOLR-18129:** confirm on the main side the round 29 gate head (`657e443d866` or `f5715964a09`). If the gate predates `657e443`, either accept the one-line YAML delta (code identical, CI green at the head) or re-gate on an explicit verify request.
6. **Optional:** align the PR title with the changelog wording (a public edit).

## Not checked

- The gate logs (`g18129r29-gate.log`, `g15823-gate.log`, `g15823-r24-gate.log`, `g15823-r24-regate.log`, `g15823levels-gate.log`, `g15823levels-r24-gate.log`) are not under `research\`. Name and seed searches (`18129C0FFEE001`) came up empty. The test counts come from the receipts and PR bodies, which agree with each other.
- The main-side takeover log and the round entries (rounds 24, 29 and 31 dispositions) are not in this worktree.
- Jira: the local snapshot `research\jira-context\SOLR-15823.json` is from 2021-12 with zero comments. The 5030 and 5031 claims about the 2026-10-05 ticket comment and the "acceptance criteria" could not be checked. No JIRA call was made.
- The Selenium counts in the 5030 and 5031 Proof sections are not verified. The CI Admin UI jobs are SUCCESS at both heads.
- Authorization was checked by reading the code at the head. No authenticated cluster was run, so the descriptions' own "untested with auth enabled" caveat stands.
- The levels-only GET fan-out (`NodeLogging.java` at `6fa54c4de8c9`, lines 72 to 100) was spot-checked, not fully reviewed.
- The restack result is a textual `merge-tree` simulation, not a resolved merge.
