# Core admin draft fidelity, slice s1

Assignment: draft fidelity review under the brief at `C:\Users\shaninna\AppData\Local\Temp\2\claude\C--Users-shaninna-dev-Solr-issues\1a4dfad7-722c-4e59-ab2e-961faf1441e1\scratchpad\brief-draft-fidelity.md`. Slice: the core-admin drafts SOLR-11939, SOLR-12007, SOLR-13246, SOLR-15024 and SOLR-15805 in `pr-drafts/core-admin/`.

Worktree: `C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-suggester`, HEAD `d627304e96b`, clean. The brief names the older claim commit `e84522fa5bc`. This review used `d627304e96b`, as the slice instructed.

Sources read: `reports/core-admin-round-1.md` and parts k1, k3, k4, k5 (k2 and k6 name none of the five tickets); `reports/core-admin-answers-round-1.md` and parts c1, c2; `material/core-admin-round-1-answers.md` (owner default of 2026-10-10: recorded recommendations adopted unless marked DISCUSS); `pr-formula.md`; `receipts/SOLR-<n>.md`; Jira packets in `research/jira-context/` (present for 11939, 12007, 13246, 15805; absent for 15024).

Head check, `git ls-remote origin refs/heads/solr-<n>-submit` (run 2026-10-11):

| Ticket | Live tip | Draft head | Match |
|---|---|---|---|
| SOLR-11939 | d4cff5e76430e6deb88115d6675db537b630948e | d4cff5e76430e6deb88115d6675db537b630948e | yes |
| SOLR-12007 | bdeba582fd63c812b6a0ecaa9874c42c42880e76 | bdeba582fd63c812b6a0ecaa9874c42c42880e76 | yes |
| SOLR-13246 | 6817c6c0c267d127e82e4a679245040f5ca9db64 | 6817c6c0c267d127e82e4a679245040f5ca9db64 | yes |
| SOLR-15024 | f95b5010b3fe61078e8730986e1e4b309ead4bbc | f95b5010b3fe61078e8730986e1e4b309ead4bbc | yes |
| SOLR-15805 | 3432f950f0aed8e2b461e9d7255d590ed7df9b20 | 3432f950f0aed8e2b461e9d7255d590ed7df9b20 | yes |

Base commits for pre-change citations (`git merge-base <head> 8e62c2686882`, local upstream main): 11939 `b6b2b8f10e9827e3b86e44649fb7966ce646c185`; 12007 `14c7aac0d151402b00259e2fb9bf5eed7049ec5d`; 13246 `97d973814336101e12475558d7419321c743de79`; 15024 and 15805 `b5c71bc5573c4e31b4cee5a7965d73587fc0ae58`. Every named SHA resolves with `cat-file -t`.

## Verdicts

| Draft | Head checked | Verdict |
|---|---|---|
| SOLR-11939 | d4cff5e76430 (live) | DRIFT (2 items) |
| SOLR-12007 | bdeba582fd63 (live) | DRIFT (2 items) |
| SOLR-13246 | 6817c6c0c267 (live) | DRIFT (3 items) |
| SOLR-15024 | f95b5010b3fe (live) | DRIFT (2 items) |
| SOLR-15805 | 3432f950f0ae (live) | DRIFT (1 item) |

Consistent in all five: the head SHA; the changelog link at the head, with the file present and its title matching the change; the Proof counts for 12007, 13246, 15024 and 15805 against the receipts; the Choice sections (12007 matches DISCUSS item 1 in the material; 15024 matches owner decision 10; 15805 matches owner decision 6; 13246 and 11939 have no Choice, as the receipts say); the AI header and footer; no em or en dashes.

## SOLR-11939

Verdict: DRIFT (2 items).

1. Draft says (Proof): "Written against head `d4cff5e76430e6deb88115d6675db537b630948e`. I checked the behavior by reading the code at that head." followed by links to `CreateCollectionCmd.java#L306-L311` and `Assign.java#L182-L192`, both at the head.
   - Evidence: the Proof has no verification date. Round 1 dates the audit 2026-10-10 (`reports/core-admin-round-1.md` line 5); this review read the cited lines on 2026-10-11. The four Java files have no diff between `b6b2b8f10e98` and the head (`git diff --stat` is empty). The lines match: `CreateCollectionCmd.java` 306-310 is the `Assign.buildSolrCoreName` call; `Assign.java` 182-192 holds the `%s_%s_replica_%s%s` format. These are pre-change citations, so per `pr-formula.md` they link the base commit and the text says so. The guide note at `collection-management.adoc` L264-L265 (head) matches the "What this change does" text.
   - Replacement (whole Proof paragraph): "Written against head `d4cff5e76430e6deb88115d6675db537b630948e`. I checked the behavior by reading the code on 2026-10-11. The code below is not changed by this branch, so its links use the branch's base commit, `b6b2b8f10e9827e3b86e44649fb7966ce646c185`. [CreateCollectionCmd.java](https://github.com/nick-boss-tech/solr/blob/b6b2b8f10e9827e3b86e44649fb7966ce646c185/solr/core/src/java/org/apache/solr/cloud/api/collections/CreateCollectionCmd.java#L306-L311) builds each core name with `Assign.buildSolrCoreName`. [Assign.java](https://github.com/nick-boss-tech/solr/blob/b6b2b8f10e9827e3b86e44649fb7966ce646c185/solr/core/src/java/org/apache/solr/cloud/api/collections/Assign.java#L182-L192) formats that name as collection, shard, `_replica_`, the replica type letter, and the number. The example in the note follows that format. The guide was not built for this change."

2. Draft says (Limits): "ADDREPLICA copies `property.*` parameters into its message ([CreateReplica.java](https://github.com/nick-boss-tech/solr/blob/d4cff5e76430e6deb88115d6675db537b630948e/solr/core/src/java/org/apache/solr/handler/admin/api/CreateReplica.java#L128) and [L157-L158](https://github.com/nick-boss-tech/solr/blob/d4cff5e76430e6deb88115d6675db537b630948e/solr/core/src/java/org/apache/solr/handler/admin/api/CreateReplica.java#L157-L158)). It uses `property.name` as the core name when `name` is blank ([AddReplicaCmd.java](https://github.com/nick-boss-tech/solr/blob/d4cff5e76430e6deb88115d6675db537b630948e/solr/core/src/java/org/apache/solr/cloud/api/collections/AddReplicaCmd.java#L355-L357))."
   - Evidence: the code is unchanged by the branch (empty diff for `CreateReplica.java` and `AddReplicaCmd.java`). The lines match: `CreateReplica.java` L128 (`remoteMessage.put(PROPERTY_PREFIX ...)`) and L157-L158 (`copyPrefixedPropertiesWithoutPrefix`); `AddReplicaCmd.java` L355-L357 (blank `coreName` falls back to `property.name`). Same pre-change rule as item 1.
   - Replacement (whole Limits paragraph): "ADDREPLICA copies `property.*` parameters into its message ([CreateReplica.java](https://github.com/nick-boss-tech/solr/blob/b6b2b8f10e9827e3b86e44649fb7966ce646c185/solr/core/src/java/org/apache/solr/handler/admin/api/CreateReplica.java#L128) and [L157-L158](https://github.com/nick-boss-tech/solr/blob/b6b2b8f10e9827e3b86e44649fb7966ce646c185/solr/core/src/java/org/apache/solr/handler/admin/api/CreateReplica.java#L157-L158)). It uses `property.name` as the core name when `name` is blank ([AddReplicaCmd.java](https://github.com/nick-boss-tech/solr/blob/b6b2b8f10e9827e3b86e44649fb7966ce646c185/solr/core/src/java/org/apache/solr/cloud/api/collections/AddReplicaCmd.java#L355-L357)). This branch does not change that code, so the links use its base commit. Documenting that path is a separate change. I can open it as a follow-up on request."

Limits and scope otherwise match k5 finding 21 and the material (ADDREPLICA stays a Limits line with the follow-up offer). No Choice is owed, and none is drafted.

Optional notes (not blocking):
- Branch correction, not draft text: drop the root `SOLR-11939-TESTING.md` before opening (k5 finding 1; material "Branch correction owed").
- The word "draft" appears in the Proof ("this draft"); item 1 replaces it with "this change".
- The changelog link uses the branch file name `SOLR-11939-property-name-docs.yml`, not the template's `SOLR-<ticket>.yml`. The link matches the file at the head, so this is not drift.
- The ticket claims (`carmen_test`, four shards) are consistent with `research/jira-context/SOLR-11939.json` (checked by grep).

## SOLR-12007

Verdict: DRIFT (2 items).

1. Draft says: "Before this change, closing a core started the old index directory cleanup on a new background thread ([thread start](https://github.com/nick-boss-tech/solr/blob/bdeba582fd63c812b6a0ecaa9874c42c42880e76/solr/core/src/java/org/apache/solr/core/SolrCore.java#L3519-L3524)). The same close then closed the `DirectoryFactory` ([close](https://github.com/nick-boss-tech/solr/blob/bdeba582fd63c812b6a0ecaa9874c42c42880e76/solr/core/src/java/org/apache/solr/core/SolrCore.java#L1871))."
   - Evidence: these are pre-change symptom citations, so they belong at the base commit `14c7aac0d151`. At the head, L3519-L3524 is the new `if (async)` branch, which the close path no longer takes (the close passes `async` false at L1840-L1841, and L3525 runs `cleanup.run()`). The pre-change thread start is at base L3502-L3517 (`Thread cleanupThread = new Thread(...)` through `cleanupThread.start();`). The pre-change close call is at base L1870 (`directoryFactory.close();`; head L1871).
   - Replacement (first two sentences of the paragraph; the sentence "The cleanup can need that factory to be open, so the two can race. The Jira ticket describes this race." stays): "Before this change, closing a core started the old index directory cleanup on a new background thread ([thread start](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/core/SolrCore.java#L3502-L3517)). The same close then closed the `DirectoryFactory` ([close](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/core/SolrCore.java#L1870))."

2. Draft says (Proof): "- Verified 2026-10-08 at head `bdeba582fd63`."
   - Evidence: `receipts/SOLR-12007.md` line 9 says "Recorded in the main side's takeover log (round 38, 2026-10-08)." That is a record date, not a run date. Round k3 finding 16 raises the same point. The receipt supports "recorded", not "verified".
   - Replacement: "- Recorded 2026-10-08 at head `bdeba582fd63`."

Checked without drift: the head `SolrCore.java` L1840-L1841 (`async` false), L3497-L3498 (public method), and L3501-L3525 (overload); the test at L104-L151 (one test; `RecordingDirectoryFactory extends MockDirectoryFactory`, so "mock factory" holds); the reload caller is `SolrCore.java` L889, so the Limits statement that reload keeps the background thread holds; the receipt counts (1 of 1, 9 of 9, 3 of 3, 25 tests with 3 skipped and 0 failures). The changelog at the head exists and its title matches.

Choice: a live alternative (background cleanup with a close-side guard), matching the material's DISCUSS item 1 and round owner decision 1. The owner has not confirmed the route (material: "The call is not taken").

Optional notes (not blocking):
- Round k3 owner decision 2 asks Nick to confirm the inline route before posting. The draft is ready to post only after that confirmation.
- The Choice opener runs to two sentences, and the Limits bullets repeat the opener (answers roll-up, Left for you, items 2 and 5; owner's call).

## SOLR-13246

Verdict: DRIFT (3 items).

1. Draft says: "[`QuerySenderListener.newSearcher`](https://github.com/nick-boss-tech/solr/blob/97d973814336101e12475558d7419321c743de79/solr/core/src/java/org/apache/solr/core/QuerySenderListener.java#L48) logs the searcher object itself."
   - Evidence: the SHA and lines are right. Base L48 is `log.debug("QuerySenderListener sending requests to {}", newSearcher);`. The text does not say the citation is pre-change, which `pr-formula.md` requires for merge-base links.
   - Replacement: "Before this change, [`QuerySenderListener.newSearcher`](https://github.com/nick-boss-tech/solr/blob/97d973814336101e12475558d7419321c743de79/solr/core/src/java/org/apache/solr/core/QuerySenderListener.java#L48) logged the searcher object itself. Its `toString()` appends the reader description. The ticket reports a single message above 20 KB, against Solr 7.7."

2. Draft says (Limits): "Other log lines that print a searcher object keep their output, for example the error line in [`SolrCore`](https://github.com/nick-boss-tech/solr/blob/6817c6c0c267d127e82e4a679245040f5ca9db64/solr/core/src/java/org/apache/solr/core/SolrCore.java#L2823)."
   - Evidence: head `SolrCore.java` L2816 opens a `/*` block and L2826 closes it, so L2823 (`log.error("Ignoring searcher register on closed core:{}", newSearcher);`) is commented-out code, not a live error path. Round k1 finding 18 says the line is live; that is wrong. The live `SolrCore` searcher log (L2831) logs `getWarmupTime()`, not the searcher object.
   - Replacement: "- Only the `QuerySenderListener` debug line changes. Other log lines that print a searcher object keep their output."

3. Draft says (Proof): "`TestQuerySenderNoQuery`: 4 of 4 at `6817c6c0c267`, checked 2026-10-06."
   - Evidence: `receipts/SOLR-13246.md` line 9 says "Recorded in the main side's receipts ledger (round 27, 2026-10-06)." That is a record date. "checked" overstates it.
   - Replacement: "`TestQuerySenderNoQuery`: 4 of 4 at `6817c6c0c267`, recorded 2026-10-06."

Checked without drift: the debug change at head L50-L52 (`isDebugEnabled` at L50, call at L51); `getSearcherName()` at head L562-L568; the test at L60-L78 (four test methods, matching the receipt's 4 of 4); the changelog at head exists; the ticket claim (over 20 KB for one message, Solr 7.7) is in `research/jira-context/SOLR-13246.json`. No Choice section, which matches the receipt ("No Choice is owed").

Optional notes (not blocking):
- The base failure line for the new test is not on disk (the receipt names `g13246-premise.log`, which is absent). "Fails on the base code" rests on the receipt's "discriminating" verdict and the test's last check, as round k1 says.
- "searcher" and "reader description" are Lucene terms; "index reader" would be plainer.
- The Limits bullets repeat the opener (answers roll-up, Left for you, item 2).

## SOLR-15024

Verdict: DRIFT (2 items).

1. Draft says (line 11, under "What happens today"): "[OWED BEFORE POSTING: check that the Jira ticket describes duplicate char filter keys in the Luke output. The ticket text is not on hand, so this section rests on the code. If the ticket describes only the Admin UI symptom, the summary line changes.]"
   - Evidence: an internal process note in public text (brief check 7; answers roll-up Checks and c2 item 7; the material's owed-before-posting list). No Jira packet for SOLR-15024 is on disk, and this session has no Jira tool, so the ticket check is not done here. The bold claim in that section is about the code, so deleting the note leaves no unsupported ticket claim. The ticket check stays on the owner's pre-posting list, outside the draft.
   - Replacement: none. Delete line 11 and the blank line after it.

2. Draft says: "In [`getAnalyzerInfo` on main](https://github.com/nick-boss-tech/solr/blob/b5c71bc5573c4e31b4cee5a7965d73587fc0ae58/solr/core/src/java/org/apache/solr/handler/admin/LukeRequestHandler.java#L1008-L1017), the char filters go into a map keyed by simple class name."
   - Evidence: `b5c71bc5573c` is the branch base, 67 commits behind upstream main `8e62c2686882` (round k1 finding 13), so the link is not "main". The lines match: base L1008-L1017 is the class-name-keyed `SimpleOrderedMap`. This is a pre-change citation, so the text must say so.
   - Replacement: "Before this change, in [`getAnalyzerInfo`](https://github.com/nick-boss-tech/solr/blob/b5c71bc5573c4e31b4cee5a7965d73587fc0ae58/solr/core/src/java/org/apache/solr/handler/admin/LukeRequestHandler.java#L1008-L1017), the char filters went into a map keyed by simple class name."

Checked without drift: head `f95b5010b3fe`; the list change at head L1008-L1019 (`List`, `className` and `args` entries, `aninfo.add("charFilters", cfilters)` at L1019); token filters unchanged at head L1030-L1038; the test at L256-L284 (`testDuplicateCharFilters`; the receipt's 9 of 9); the fixture `solr/core/src/test-files/solr/collection1/conf/schema-dup-charfilters-analyzer.xml` exists at the head; `solr/solrj` has no `charFilters` read in the Luke response (the hits are Schema API classes only), so the Choice's SolrJ claim holds; the Admin UI loop at `schema.js` L648-L670 is a `for...in` over component data. The Choice (keep keys with an index, or an ordered list) matches owner decision 10 and the material. The Proof date "recorded 2026-10-03" matches `receipts/SOLR-15024.md` line 8. The changelog at the head exists.

Optional notes (not blocking):
- The changelog title is public text: "...shows every entry instead of only the last one". Round k1 finding 6 says that base JSON output was not read, and the material does not adopt it. The owner should decide whether the changelog keeps that phrase. The draft does not repeat it.
- "Two values under one key cannot both survive" is standard JSON parsing behavior, not a run result.
- "Luke" and "char filter" are not glossed for general reviewers.
- The Limits bullets repeat the opener (answers roll-up, item 2).

## SOLR-15805

Verdict: DRIFT (1 item).

1. Draft says: "[`CoreContainerProvider`](https://github.com/nick-boss-tech/solr/blob/b5c71bc5573c4e31b4cee5a7965d73587fc0ae58/solr/core/src/java/org/apache/solr/servlet/CoreContainerProvider.java#L186-L193) catches every `Throwable` during startup and logs "Could not start Solr". It rethrows only an `Error`, so any other exception is swallowed. The container stays null. Every later request fails in [`checkReady`](https://github.com/nick-boss-tech/solr/blob/b5c71bc5573c4e31b4cee5a7965d73587fc0ae58/solr/core/src/java/org/apache/solr/servlet/CoreContainerProvider.java#L100-L110) with `UnavailableException`, and the process keeps running until something stops it. The ticket names `SolrDispatchFilter.init()`. That class is no longer in main, and the startup code is in `CoreContainerProvider`."
   - Evidence: the SHAs and lines are right (base L186-L193 is the `catch (Throwable t)` block that rethrows only `Error`; base L100-L110 is `checkReady`). The paragraph describes pre-change behavior and links the base commit, but it does not say so. The head changes L186-L194 (`init` now rethrows `RuntimeException` and wraps others), so a reader could take the base link as current code.
   - Replacement (whole paragraph): "Before this change, [`CoreContainerProvider`](https://github.com/nick-boss-tech/solr/blob/b5c71bc5573c4e31b4cee5a7965d73587fc0ae58/solr/core/src/java/org/apache/solr/servlet/CoreContainerProvider.java#L186-L193) caught every `Throwable` during startup and logged "Could not start Solr". It rethrew only an `Error`, so any other exception was swallowed. The container stayed null. Every later request failed in [`checkReady`](https://github.com/nick-boss-tech/solr/blob/b5c71bc5573c4e31b4cee5a7965d73587fc0ae58/solr/core/src/java/org/apache/solr/servlet/CoreContainerProvider.java#L100-L110) with `UnavailableException`, and the process kept running until something stopped it. The ticket names `SolrDispatchFilter.init()`. That class is no longer in main, and the startup code is in `CoreContainerProvider`."

Checked without drift: head `3432f950f0ae`; head `init` L186-L194 (`RuntimeException` rethrown, other `Throwable`s wrapped in `SolrException(SERVER_ERROR)`); `contextInitialized` (head L82-L85) sets the attribute only after `init` returns, so the Choice and Proof claims about the attribute hold; the test at L44-L62 (one test; `expectThrows(RuntimeException.class, ...)` at L56-L58; `never()` on setAttribute at L60; `UnavailableException` at L61); no `SolrDispatchFilter` class in main (only a javadoc mention in `SolrServlet.java` L40); the changelog at the head exists and its title matches the bold summary; the Proof count 1 of 1 matches `receipts/SOLR-15805.md` line 6. The Choice (fail the context at startup, or keep it up and answer 503) matches owner decision 6 (round k4) and the material. Limits (any `RuntimeException` accepted; mocked context; no other startup path changed) match k4 findings 9 and 12.

Optional notes (not blocking):
- The receipt records "PASS (pre-fix proof step in the hardening run)" with no failure text. The Proof's "By code reading, it fails on the base code" follows k4 finding 10 and c2 item 4. The main side should confirm it from `g15805-harden.log` (not on disk).
- The draft has no title line. The changelog title is "Solr no longer stays up without a CoreContainer when startup throws a non-Error exception; the servlet context initialization now fails instead". The draft's bold summary matches it.
- "Servlet context" and "CoreContainer" are not glossed.

## Not done

- No build, Gradle run, or test. Test counts come from `receipts/`, not re-run. Gate logs, probe logs, and base failure lines are not on disk, so base-failure statements were checked by reading only.
- SOLR-15024 Jira ticket text not checked (no packet on disk; no Jira tool in this session). The packets for 11939, 12007, 13246 and 15805 were grepped for ticket claims only, not read in full.
- No GitHub call, commit, push, or post. Only `git ls-remote`, `cat-file`, `merge-base`, `show`, `diff --stat` and `grep` were run, all read-only.
- The semantic behavior of each change was not traced beyond the drafts' claims; checks were on cited lines, file presence, and the receipts.
