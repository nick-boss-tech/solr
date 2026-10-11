# Search components draft fidelity, slice s9

Assignment: `assignments/pool-draft-fidelity-searchcomponents-edismax.md`. Claim: `claims/pool-draft-fidelity-searchcomponents-edismax.md`, slice C9 (SOLR-7550, 8009, 8020, 8240, 8939). Worktree `wt/pr-prepare-suggester` at HEAD d627304e96b (confirmed with `cat-file -t`). Read-only: this file is the only write. No build, test, gh or Jira call, no push, no commit.

Sources: drafts `pr-drafts/search-components/SOLR-<n>.md`; receipts `receipts/SOLR-<n>.md` (all five present). Round 1 reports: `reports/search-components-2-h4.md` (SOLR-7550), `reports/search-components-2-h1.md` (SOLR-8009), `reports/search-components-2-h3.md` (SOLR-8939), `reports/search-components-2.md` (summary rows for 7550, 8009, 8020, 8939), `reports/search-components-3-w3.md` (SOLR-8240), `reports/search-components-3.md` (summary row for 8240). The `search-components-1*` and `search-components-4*` reports have no section for these five tickets (grep). Answers material: a grep of `material/` for the five numbers returned no hits. Local Jira packets in `research/jira-context/` were used for spot checks of problem statements only. Drafts carry no separate title line, so the changelog fragment title was compared with the draft text and the code.

Head checked per draft (`git ls-remote origin refs/heads/solr-<n>-submit`, 2026-10-11):

| Draft | Head named in draft | Live tip on origin | Match |
|---|---|---|---|
| SOLR-7550 | 687165651f9210e333a03c566a27f59f40527174 | 687165651f9210e333a03c566a27f59f40527174 | yes |
| SOLR-8009 | 8795661ddc98c8bffdb8db49a7dd5f293216e929 | 8795661ddc98c8bffdb8db49a7dd5f293216e929 | yes |
| SOLR-8020 | 79f790523d2fcbc91670e5dab0d4b5940dc11c41 | 79f790523d2fcbc91670e5dab0d4b5940dc11c41 | yes |
| SOLR-8240 | fad7a1dd8e2397330e0973e809aeba60490d2f08 | fad7a1dd8e2397330e0973e809aeba60490d2f08 | yes |
| SOLR-8939 | a855a2d8965c8eae5df171ad9b5148b7e95f7425 | a855a2d8965c8eae5df171ad9b5148b7e95f7425 | yes |

Other checks that passed for all five: every head and base SHA resolves. Merge-bases with the worktree `upstream/main` (3f5d4c5bf8a): 14c7aac0d15 for 7550 and 8009, 22a8cfebbbdb for 8020, e432df19c4a for 8240, 97d973814336 for 8939. Every Proof count matches its receipt. The SOLR-8009 count also conflicts with the head file (SOLR-8009 item 1). Each verification date is present. Each changelog fragment exists at its head. No em dash, no HTML comment, and no internal vocabulary in visible text (searched: gate, receipt, ledger, seed, claim, pool, assignment, subagent, submission, handoff, pipeline, queue, drain, worktree, round). AI header and footer present.

## Verdicts

| Draft | Head checked | Verdict |
|---|---|---|
| SOLR-7550 | 687165651f9 (match) | DRIFT (2 items) |
| SOLR-8009 | 8795661ddc98 (match) | DRIFT (2 items) |
| SOLR-8020 | 79f790523d2 (match) | CONSISTENT |
| SOLR-8240 | fad7a1dd8e23 (match) | DRIFT (2 items) |
| SOLR-8939 | a855a2d8965c (match) | DRIFT (1 item) |

## SOLR-7550

Verdict: DRIFT (2 items).

1. Draft says: "PeerSync already counts a 503 or a 404 from that request as success when `cantReachIsSuccess` is set ([PeerSync.java L399-L421](https://github.com/nick-boss-tech/solr/blob/687165651f9210e333a03c566a27f59f40527174/solr/core/src/java/org/apache/solr/update/PeerSync.java#L399-L421))."
   Evidence: the 503 and 404 blocks are pre-change code. At the merge-base 14c7aac0d15 they sit at `PeerSync.java` L398-L420. The head is one line lower because of the `@VisibleForTesting` line added at L367. The formula (`pr-formula.md`, section 1) says pre-change symptom code links the merge-base and the text says so.
   Replacement: "PeerSync already counts a 503 or a 404 from that request as success when `cantReachIsSuccess` is set ([PeerSync.java L398-L420 at the base commit](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/update/PeerSync.java#L398-L420)). A 500 had no such case."

2. Changelog fragment title (the draft's changelog link target) says: "PeerSync during leader election now treats a 500 response to the versions request from a replica as unreachable, like a 503, so a replica whose core failed to load no longer blocks the election". The draft says the rule "counts as success" (`PeerSync.java` L423-L433 at head, `return true` at L432, log text "counting as success").
   Evidence: `changelog/unreleased/SOLR-7550-peersync-500.yml` line 1 at 687165651f9. Round report `search-components-2-h4.md` Finding 1, summary item 4 and owner decision 3 record the same mismatch.
   Replacement (changelog title line, a change to the branch, not to the draft): "title: PeerSync during leader election now counts a 500 response to the versions request from a replica as success, like a 503, so a replica whose core failed to load no longer blocks the election". Changing the fragment creates a new head, so every SHA link in this draft then moves to that commit. The owner must approve the change first (h4 owner decision 3).

Choice and Limits: consistent with `search-components-2-h4.md` and the summary row. The choice has a live alternative (count only the failed-core case) and states the cost of the broad rule. The pointed question is present. Owner decisions 1 and 2 in h4 (keep the broad rule; keep the two public `ShardResponse` setters) are still open, so the draft should not be posted before they are settled.

Optional notes, not blocking: about 4,600 bytes, above the roughly 3,500 character guide. "versions request" and `cantReachIsSuccess` are code terms; a short gloss would help a reader.

## SOLR-8009

Verdict: DRIFT (2 items).

1. Draft says: "Verified 2026-10-05 at head `8795661ddc98`: FullSolrCloudDistribCmdsTest 10 of 10; TestRealTimeGet 4 of 4; TestRandomFlRTGCloud 2 of 2; TestAddFieldRealTimeGet 1 of 1. All pass."
   Evidence: `receipts/SOLR-8009.md` line 6 says "FullSolrCloudDistribCmdsTest 10 tests", so the draft matches its receipt. The head file does not match it: `solr/core/src/test/org/apache/solr/cloud/FullSolrCloudDistribCmdsTest.java` at 8795661ddc98 has 9 `public void test` methods (lines 98 to 845). The base 14c7aac0d15 has 8, and the new test makes 9. The class extends `SolrCloudTestCase`, which has no test methods. Round report `search-components-2-h1.md` Finding 2 and summary owner item 9 raise the same conflict. The gate log `g8009-gate.log` is not on disk, so the 10 cannot be confirmed here.
   Replacement, to paste once the gate log confirms 9 (if the log shows 10, explain the extra test before posting): "Verified 2026-10-05 at head `8795661ddc98`: FullSolrCloudDistribCmdsTest 9 of 9; TestRealTimeGet 4 of 4; TestRandomFlRTGCloud 2 of 2; TestAddFieldRealTimeGet 1 of 1. All pass."

2. Draft says: "- They go out in one more request to all shards. That is the same request the code already builds when `shards` is given ([RealTimeGetComponent.java](https://github.com/nick-boss-tech/solr/blob/8795661ddc98c8bffdb8db49a7dd5f293216e929/solr/core/src/java/org/apache/solr/handler/component/RealTimeGetComponent.java#L1097-L1103))."
   Evidence: head L1097-L1103 is the new fan-out block (`if (!idsForAllShards.isEmpty())`). The request the code already builds when `shards` is given is the `else` branch, which is at the merge-base L1087-L1093 (`createShardRequest(rb, reqIds.allIds)` with `sreq.shards = null; // ALL`) and at head L1104-L1110. The cited range does not hold the code the sentence describes.
   Replacement: "- They go out in one more request to all shards ([RealTimeGetComponent.java](https://github.com/nick-boss-tech/solr/blob/8795661ddc98c8bffdb8db49a7dd5f293216e929/solr/core/src/java/org/apache/solr/handler/component/RealTimeGetComponent.java#L1097-L1103)). That is the same request the code already builds when `shards` is given ([RealTimeGetComponent.java at the base commit](https://github.com/nick-boss-tech/solr/blob/14c7aac0d151402b00259e2fb9bf5eed7049ec5d/solr/core/src/java/org/apache/solr/handler/component/RealTimeGetComponent.java#L1088-L1093))."

Verified and consistent: the base link L1065-L1067 (merge-base, text says "base code"), the head range L1067-L1076, the test range L157-L184 (first `assertNotNull` at L181), the changelog link and its title, the Choice (fan-out versus 400, with the cost stated), and the Limits, which match `search-components-2-h1.md` Findings 1 and 3 and the formula's follow-up offer.

Optional notes, not blocking:
- The Proof sentence says the test "checks a get by one id and a get by two ids with no route". The test checks single-id gets for ids 1 and 4 and then a two-id get. Clearer: "checks a get by each of two ids, one on each shard, and a get by both ids together, with no route".
- The Limits section opens with a bullet, not the bold one-line summary that the presentation rule asks for.
- About 4,000 bytes, a little over the guide.

## SOLR-8020

Verdict: CONSISTENT.

Checked and matching: the base link `SearchHandler.java` L794-L798 at the merge-base 22a8cfebbbdb (the text says "base code"); the head guard at L794-L800; the response-list guard at L784; the test at `ComponentStageLimitsTest.java` L207-L240; the Proof count 5 of 5 and the verification date 2026-10-05 (receipt); the base run failing 1 of 5 with `expected:<1> but was:<2>` (receipt). The changelog title matches the code. The Limits match `search-components-2-h4.md` Note 12 and the summary row ("No Choice owed"). The "What happens today" section opens with a bold summary.

Optional notes, not blocking:
- The draft says "The ticket calls this an overwrite". The attribution is accurate. `search-components-2-h4.md` Note 5 asks that "overwrites" not be repeated in the PR. If you want to follow it literally, replace that sentence with: "The ticket reports that the empty section replaces the real one ([ticket](https://issues.apache.org/jira/browse/SOLR-8020))."
- "JSON clients may keep only one of them" is a hedge. Client behavior was not checked (h4 Not checked).

## SOLR-8240

Verdict: DRIFT (2 items).

1. Draft says: "Every other mapped value goes into the default search field (`df`) with no field name ([mapping code](https://github.com/nick-boss-tech/solr/blob/fad7a1dd8e2397330e0973e809aeba60490d2f08/solr/core/src/java/org/apache/solr/handler/loader/JsonLoader.java#L301-L332))."
   Evidence: this is the pre-change symptom (`getDocMap`, which copies `f` values into `df`). It should link the merge-base e432df19c4a50b9d54d7fba545397b859cc54f98, where `getDocMap` is at L295-L327 (at head it is L301-L333). The formula (section 1) requires the merge-base link and the text saying so.
   Replacement: "Every other mapped value goes into the default search field (`df`) with no field name ([mapping code at the base commit](https://github.com/nick-boss-tech/solr/blob/e432df19c4a50b9d54d7fba545397b859cc54f98/solr/core/src/java/org/apache/solr/handler/loader/JsonLoader.java#L295-L327))."

2. Draft says: "- The \"Setting JSON Defaults\" section of the same reference guide page still shows `mapUniqueKeyOnly` as a default. A request that uses that default and sends `f` gets the 400. This change does not edit that section. Updating it is a planned follow-up pull request."
   Evidence: the section does show the default (adoc head L856-L876), so the first two sentences are right. `search-components-3-w3.md` owner decision 3 says the phrase "planned follow-up pull request" needs the owner's agreement, and no agreement is on record. The formula (section 4) says a broader issue is named in Limits, with an offer to open a follow-up ticket and PR on request.
   Replacement: "- The \"Setting JSON Defaults\" section of the same reference guide page still shows `mapUniqueKeyOnly` as a default. A request that uses that default and sends `f` gets the 400. This change does not edit that section. I can open a follow-up ticket and PR for it on request."

Verified and consistent: head checks at `JsonLoader.java` L249-L255; the test at `JsonLoaderTest.java` L192-L214; the adoc line 68 at head; `params.json` L3-L5 at head; the changelog link and title; Proof counts JsonLoaderTest 32 of 32 and TestInitParams 7 of 7 (receipt), date 2026-10-05. The Choice (400 versus accepting with a warning) has a live alternative and a stated cost, consistent with `search-components-3-w3.md`. The landing-order sentence ("Until SOLR-10424 removes that default") matches w3 FIX 3, so no change is needed.

Optional notes, not blocking:
- The Proof says the test "fails on the code before this change" without naming the base. `search-components-3-w3.md` Finding 7 and owner decision 8 ask to confirm the premise run used e432df19c4a. Name it once confirmed.
- The Proof says the test "sends `f=title:/title` with `mapUniqueKeyOnly=true`". The test also sends `df=_catch_all` (`JsonLoaderTest.java` L207). The receipt (line 7) records that correction, and the stated base failure depends on it. Suggested: "sends `f=title:/title` with `mapUniqueKeyOnly=true` and `df=_catch_all`".
- Branch doc, `transforming-and-indexing-custom-json.adoc` L68, says a request "that supplies both is rejected", which misses inherited values (w3 Finding 2a). The draft says the guide "documents the rule". Owner decision 3 in w3 (fix the line on the branch, or keep the Limits follow-up) decides this.
- The Limits section opens with a bullet, not the bold summary line. About 4,100 bytes, over the guide.

## SOLR-8939

Verdict: DRIFT (1 item).

1. Draft says: "Changelog: `changelog/unreleased/SOLR-8939-date-unique-key-millis.yml`"
   Evidence: the formula (`pr-formula.md`, "Rules for filling it in") says the changelog line is a link to the fragment at the head SHA. The fragment exists at a855a2d8965c8eae5df171ad9b5148b7e95f7425, and its title ("Distributed queries keep the millisecond resolution of a date uniqueKey when fetching stored fields") matches the draft. The suffixed file name is accepted (h3 Finding 15).
   Replacement: "Changelog: [changelog/unreleased/SOLR-8939-date-unique-key-millis.yml](https://github.com/nick-boss-tech/solr/blob/a855a2d8965c8eae5df171ad9b5148b7e95f7425/changelog/unreleased/SOLR-8939-date-unique-key-millis.yml)"

Verified and consistent: `QueryComponent.java` L1443-L1447 (plain path, the helper call at L1445), L1453-L1462 (the `idToString` helper; base L1445 shows `shardDoc.id.toString()`), `StoredFieldsShardRequestFactory.java` L79. The counts 2 of 2 and 2 of 2 match the receipt, and the test names exist at head (`testDateUniqueKeyIsSentAsIsoWithMilliseconds`, `testDateKeyKeepsMilliseconds`). The "no fail-before result" wording for `QueryComponentIdFormatTest` matches h3 Finding 2. The Limits match h3: unit-level only, public helper for the other package, zero-millisecond dates sent without a fraction. No Choice section is owed.

Optional notes, not blocking:
- "recorded 2026-10-06" should read "verified", to match the template ("verified <date> at this head"). Suggested: "StoredFieldsShardRequestFactoryTest 2 of 2 pass at `a855a2d8965c8eae5df171ad9b5148b7e95f7425`, verified 2026-10-06."
- `constructRequests` calls `assumeWorkingMockito()`, so a run without Mockito support shows as a skip, not a failure. Confirm both are runs in the gate log (h3 Finding 14; the log is not on disk).
- Code note, not a draft change: the Javadoc at `QueryComponent.java` L1453-L1456 says dates "must be written in ISO-8601 (with milliseconds)", which overstates the zero-millisecond case the Limits describe (h3 Finding 13).
- The local `solr-8939-submit` differs from origin (h3 Finding 5). This check used origin only.
- The Limits section opens with bullets, not the bold summary line.

## Not done

- Live Jira was not queried. Problem statements were spot-checked against local packets only (7550 "500" and `recovery_failed` text, 8009 NullPointerException, 8020 "overwrites", 8240 proposal, 8939 summary). The full "What happens today" narratives were not checked.
- Gate logs (for example `g8009-gate.log`) are not on disk, so Proof counts were checked against receipts. The SOLR-8009 count conflict stays open until the log is read.
- Code was read with `git show <sha>:<path>` at the named SHAs. Nothing was built or run.
- Whether a remote HTTP 500 reaches PeerSync as a `SolrException` was not traced. The 7550 draft does not claim it.
- The length guide was assessed in bytes, not characters.
