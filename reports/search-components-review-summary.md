# Search components full-category review: summary

Assignment: `ASSIGNMENT.md` on this branch (Search components, full-category review to SATISFIED).
Reviewer: Windows review host, claim `claims/search-components-review.md`, 2026-10-13.
Base: this branch at `790624c4e56` (the assignment commit). Drafts were read at their own heads. Sub-batch reports: `reports/search-components-review-1.md` to `-4.md`.

## Status

The assignment is not discharged. One draft is OPEN (gate): SOLR-6193. Its code fixes are written as gate requests (SC-G1 to SC-G3, below) for the gate coordinator to stock. Nothing is posted to `gates/` from this lane.

Everything else has a verdict:
- 47 drafts are SATISFIED and ready to open. Opening each PR needs Nick's approval, which this lane does not give.
- Wording fixes found in this pass are applied in this tree (ten drafts, twelve edits; see "Edits in this tree").
- Records that disagree with a draft are listed under "Record notes for the main side".

## In-scope drafts: one line each (48)

Each line gives the verdict and the one point that matters for it.

1. SOLR-4374: SATISFIED, ready to open. No edits.
2. SOLR-5394: SATISFIED, ready to open. No edits.
3. SOLR-6193: OPEN (gate). The ticket's own `facet.field` example is not covered, the changelog title overclaims local `facet.offset` and `facet.pivot.mincount`, and commit subjects carry process words. Requests SC-G1 to SC-G3; decision SC-D1.
4. SOLR-6207: SATISFIED, ready to open. A documentation pin with no fail-before by design, which the draft says.
5. SOLR-6831: SATISFIED, ready to open. No edits.
6. SOLR-6975: SATISFIED, ready to open. Three edits: "shard name" becomes "shard address", which is what the code breaks ties by. See the note on SOLR-17976.
7. SOLR-7390: SATISFIED, ready to open. No edits. The gate ran at a received head; the code-identity claim is the receipt's.
8. SOLR-7498: SATISFIED, ready to open. No edits.
9. SOLR-7520: SATISFIED, ready to open. No edits. The coordinator gap is named in Limits with an offer.
10. SOLR-7550: SATISFIED, ready to open. One edit: the narrower rule is a named follow-up (record-reserved choice; see SC-D6).
11. SOLR-8009: SATISFIED, ready to open. One edit: an unrecorded run claim about an assert is removed.
12. SOLR-8020: SATISFIED, ready to open. No edits.
13. SOLR-8240: SATISFIED, ready to open. No edits. Ordering with SOLR-10424 is consistent in both drafts.
14. SOLR-8939: SATISFIED, ready to open. No edits. Unit-level proof only, as the receipt says.
15. SOLR-9148: SATISFIED, ready to open. No edits. The tip differs from the proof head only by a 4-line guide edit.
16. SOLR-9396: SATISFIED, ready to open. No edits. One uncited parser claim is noted for the main side.
17. SOLR-9864: SATISFIED, ready to open. No edits. `SortClause` has final fields, as the draft says.
18. SOLR-10424: SATISFIED, ready to open. No edits. Ordering with SOLR-8240 is consistent.
19. SOLR-10492: SATISFIED, ready to open. One edit: the StoredFields citation is labelled as the base commit, since this change does not touch that file.
20. SOLR-10694: SATISFIED, ready to open. No edits.
21. SOLR-10844: SATISFIED, ready to open. No edits. The enum claim is verified: `EnumFieldType` reports `INTEGER`.
22. SOLR-11129: SATISFIED, ready to open. No edits. One uncited base-behavior sentence is noted.
23. SOLR-11153: SATISFIED, ready to open. No edits.
24. SOLR-11310: SATISFIED, ready to open. No edits. Option 1 is adopted; the record asks for confirmation before opening, and the draft poses it.
25. SOLR-11364: SATISFIED, ready to open. One edit: the description of the final commit now matches its title and its test addition. A rebase before opening is a record owner item.
26. SOLR-11470: SATISFIED, ready to open. No edits. The wrapper case is verified against `QueryUtils.makeQueryable`.
27. SOLR-12044: SATISFIED, ready to open. No edits. Option 1 is adopted, because option 2 keeps the intersection the ticket is about.
28. SOLR-12543: SATISFIED, ready to open. No edits. The draft says "partial", as the record asks.
29. SOLR-12556: SATISFIED, ready to open. No edits. The test count (12, plus one unannotated method) is verified.
30. SOLR-13245: SATISFIED, ready to open. One edit: the collection-wide version is a named follow-up (record-reserved choice; see SC-D2).
31. SOLR-13876: SATISFIED, ready to open. One edit: the group-wide maximum is a named follow-up (record-reserved choice; see SC-D4). Receipt row lacks a gate log and date.
32. SOLR-14381: SATISFIED, ready to open. No edits.
33. SOLR-14451: SATISFIED, ready to open. One edit: the other debug modes are a named follow-up (record-reserved choice; see SC-D5).
34. SOLR-14678: SATISFIED, ready to open. No edits. The per-document route is adopted (see SC-D3).
35. SOLR-14931: SATISFIED, ready to open. No edits.
36. SOLR-15018: SATISFIED, ready to open. No edits. The tlog defaults (100 records, 10 files) are verified.
37. SOLR-15041: SATISFIED, ready to open. No edits. No base count is given, as the record does not give one.
38. SOLR-15319: SATISFIED, ready to open after the SOLR-8051 landing. No edits. The landing dependency is in Limits, as the assignment asks; the receipt's premise verdict matches the draft.
39. SOLR-15331: SATISFIED, ready to open. No edits. The inconclusive proof is stated as such.
40. SOLR-15479: SATISFIED, ready to open. One edit: maxScore over all matches is a named follow-up (record-reserved choice; see SC-D7).
41. SOLR-15895: SATISFIED, ready to open. No edits. The proof is stated as inconclusive, as the record asks.
42. SOLR-16444: SATISFIED, ready to open. No edits.
43. SOLR-17051: SATISFIED, ready to open. One edit: the new checks are in `doStatsTemplated`, which `doStats` calls.
44. SOLR-17155: SATISFIED, ready to open. No edits. Reading docValues is adopted; the record's scope question stays a PR-text question.
45. SOLR-17748: SATISFIED, ready to open. No edits. One uncited sentence (the 503 path) is noted.
46. SOLR-17791: SATISFIED, ready to open. No edits. Both API options are named, as the receipt asks. "Four new tests" is verified.
47. SOLR-17976: SATISFIED, ready to open. No edits. Its tie-break interacts with SOLR-6975 (see Record notes).
48. SOLR-18109: SATISFIED, ready to open. No edits. Upstream outcome below.

Excluded from this category by the assignment (not drafted or reviewed here): SOLR-18541 (its own review assignment) and the mergeIds staging file (SOLR-18539).

## Verification notes the assignment asked for

- SOLR-18109, the upstream change: PR 4779, "SOLR-18109: move SolrPluginUtils debug methods to DebugComponent", is MERGED with `mergedAt` 2026-08-24T17:25:40Z (read-only `gh pr view`). `upstream/main` has `doStandardQueryDebug`, `doStandardResultsDebug` and `doSimpleQuery` in `DebugComponent`. The draft still has something of its own to submit: its two regression tests (`DebugComponentTest.testExplainOther` and `MoreLikeThisHandlerTest.testStandardDebugOutput`) are not on `upstream/main`. The branch base (`14c7aac0d151`) predates PR 4779, so a rebase is a main-side call before opening.
- SOLR-15319, the landing dependency: the production fix is on the SOLR-8051 branch and lands first. The draft's Limits say so, and say that this change is rebased onto that fix if the two `ExactStatsCache` edits need reconciling. Per the assignment this is a fact for the Limits, not a finding against the draft.

## Decisions (recorded under the decision default and the three-rung ladder)

Under the decision default, a recorded recommendation wins. None of the decisions below had one on record, so the harder, more thorough option applied where the ticket's symptom needed it. Where the record reserved a choice between two designs that neither dominates, the ladder's third rung applied: the alternative became a named follow-up in the draft's Limits, and the owner can reopen it when the PR is opened.

- SC-D1 (SOLR-6193, scope): the f3 scope audit (`reports/search-components-1-f3.md`) lists options A (narrow), B (keep the ticket open and add a facet.field fix) and C (hold), and records no recommendation. The round 28 and round 36 reviews also record no recommendation. The ticket's own `facet.field` example is still broken on this branch, and the fix is effort, so the harder option applies: an in-branch facet.field fix, after an information run (SC-G1). The draft is accurate about its current scope and gets rewritten when the fix lands.
- SC-D2 (SOLR-13245, collection-wide visibility): the record reserves the node-level versus collection-wide choice. The two collection-wide designs (ZooKeeper registration, or a request to each replica) have different costs, and neither is more thorough than the other. Named follow-up in Limits.
- SC-D3 (SOLR-14678, per-document nest-path names): the record reserves the framing question. The request-global route leaks an ordinary root field's projection, which the draft's own Choice describes. The per-document route is adopted; the Choice stays as PR text.
- SC-D4 (SOLR-13876, slice-local maxScore): the record reserves the choice. The group-wide maximum needs scores for documents the page does not return, and its cost is not measured. Named follow-up in Limits.
- SC-D5 (SOLR-14451, debug modes): the record reserves the choice. The other debug modes are not part of the ticket. Named follow-up in Limits.
- SC-D6 (SOLR-7550, the 500 rule): the record calls it an owner-level decision to make explicit. The draft makes it explicit. The narrower rule needs error-text matching or a server marker, both fragile, and the ticket's reporter proposed the broad rule. Named follow-up in Limits.
- SC-D7 (SOLR-15479, maxScore scope): the record reserves "all matches after reranking" against "the collected result window". The draft implements the collected window and did not name the all-matches option. The all-matches option is now a named follow-up in Limits.
- Adopted implemented routes, where the alternative does not fix the symptom or goes beyond the ticket: SOLR-10424 (remove the id-only default), SOLR-11310 (boost every elevated document), SOLR-12044 (return the filter), SOLR-14381 (long return types), SOLR-15895 (ask the file system), SOLR-17155 (read docValues), SOLR-6207 (document, do not deprecate). Each draft keeps its Choice section as PR text for Nick.

## Gate requests (for the gate coordinator to stock; not posted here)

Gates live on `pr-prepare` under `gates/`. These are written up in the sub-batch 1 report (`reports/search-components-review-1.md`, SOLR-6193 section) and are restated here.

- SC-G1 (information only, no code change): run the ticket's example `facet.field={!key myblah facet.offset=10}blah&f.blah.facet.offset=20` on single-node and distributed search, at base `c3cdf7b46e8` and at head `ec94bf50c80`. Record each value. The ticket says single-node returns 10; a code reading says 20.
- SC-G2 (branch change, gated): on `solr-6193-submit`, narrow the changelog title to the local params the tests cover, and rewrite the four fork commits so no subject carries process words or a doubled prefix. Force-push on the fork submit branch is allowed under the standing direction, pinned with `push-fork-lease.ps1`. Gate at the rewritten head.
- SC-G3 (branch change, gated, after SC-G1): the facet.field fix, with a test that fails at base `c3cdf7b46e8`. The shape depends on SC-G1: if single-node returns 10, the distributed path matches single-node; if single-node returns 20, see the owner item.

## Owner items

- Conditional, SOLR-6193. If SC-G1 shows single-node returns 20 today, the ticket's expected value contradicts current single-node precedence. Making the fix would change single-node results, and SOLR-11129 shares the same precedence rule. That is a product behavior change, so it comes back to Nick. If SC-G1 shows 10, there is no owner item.
- Not an owner item, but reserved by the assignment: each PR opening needs Nick's approval. Forty-seven drafts are ready for that approval, and none is opened.

## No-draft receipts (24): one line each

Draft owed, gate green at the live tip (11, not reviewed here):
- SOLR-6759, SOLR-8003, SOLR-8088, SOLR-8767, SOLR-8954, SOLR-9124, SOLR-9595, SOLR-10305, SOLR-17055: gated green, draft owed.
- SOLR-8051: gated green (re-gate after an in-branch fix). Draft owed. Its fix is the landing dependency for SOLR-15319.
- SOLR-16570: gated green at `9d3466101a`. Draft owed. It is in the assignment's list and is a collapse pair with SOLR-17796 (query-parsing category).

Held or qualified (2):
- SOLR-13851: gated green as-is, submission held (a maintainer decision on the public `getFirstMatch` contract). Draft held.
- SOLR-15144: gate green in the hardening record, qualified by a later round 35 end-to-end finding. The receipt says not to present it as plainly PR-ready until that caveat is handled.

Live PR text, not in this draft set (4): SOLR-13568 (apache/solr #5014), SOLR-17539 (#4998), SOLR-18482 (#5009), SOLR-18506 (#5029). Their PR text is a live-PR review, not a draft review.

Not owed (7):
- SOLR-18196: already merged (PR #4995). Nothing to gate or open.
- SOLR-18356: retired as obsolete (2026-10-04).
- SOLR-3044: parked, no gate; the disposition is the owner's.
- SOLR-17841: retire candidate; the retire call is the owner's.
- SOLR-16155: excluded by the assignment (no PR of its own).
- SOLR-16290: excluded by the assignment (held test pin).
- SOLR-17372: excluded by the assignment (premise not supported; gate failed on the premise; retire pending the owner's word).

## Edits in this tree

Ten drafts, twelve edits:
- SOLR-10492: StoredFields citation labelled as the base commit; the change does not touch that file (1).
- SOLR-17051: the new checks sit in `doStatsTemplated`, which `doStats` calls (1).
- SOLR-6975: "shard name" to "shard address" in three places (3).
- SOLR-8009: the unrecorded run claim about an assert is removed; the Limits line says only that a get by one id on two shards is not tested (1).
- SOLR-14451: other debug modes as a named follow-up (1).
- SOLR-13876: group-wide maximum as a named follow-up (1).
- SOLR-7550: narrower rule as a named follow-up (1).
- SOLR-11364: the description of the final commit now matches its title and its test addition (1).
- SOLR-13245: collection-wide version as a named follow-up (1).
- SOLR-15479: all-matches maxScore as a named follow-up (1).

No branch changed. No commit was made on any submit branch, and nothing was pushed to a fork submit branch.

## Record notes for the main side

- SOLR-6975 and SOLR-17976: both change the cross-shard tie-break. SOLR-17976 says the shard address varies per request, and SOLR-6975 orders different shards by that address. The 6975 draft's "order across shards is not defined" covers it, but the landing order should be decided.
- SOLR-15319: the SOLR-8051 draft is owed (gated green), and its landing must come first.
- SOLR-13876: the receipt row names no gate log and no explicit gate date. The draft cites the dated round 35 logs, so the draft is fine; the receipt needs the log name and date.
- SOLR-7390: the receipt has no separate head line, and it records the gate at a received head (`72caa363e29`) whose code it says is identical to the shipped head (`7463dd006a7`). That identity was not re-verified here.
- SOLR-9148, SOLR-15319, SOLR-15479 and SOLR-16444 receipts have no separate head line (format note only). Each receipt's "Gated head" bullet names the head, and it matches the live tip.
- SOLR-11364: the branch was 45 commits behind `upstream/main` at the spot-check. The record says a rebase before the PR is an owner item.
- SOLR-10424: the goal file's header still says GATE PENDING; the receipt says the header is stale.
- SOLR-18109: the branch base predates PR 4779; rebase before opening is a main-side call.
- SOLR-17155: the record's non-stored unique-key scope question is still open; the draft's route is adopted here.
- SOLR-16444: the GitHub run at its head has no recorded conclusion; the draft claims none.
- SOLR-13568 (PR #5014) is open. The SOLR-13876 draft says it "does not change scores or the documents returned"; that was not checked against its diff.

Uncited sentences, not run (for the main side to verify or drop):
- SOLR-10492: a `group.field` given only as a local parameter still returns BAD_REQUEST.
- SOLR-11129: facet.zeros=false "did not do that before" (base behavior).
- SOLR-6831: memAllowed is included; the check runs at every level.
- SOLR-17051: sub-facets are computed before the bucket is dropped.
- SOLR-17748: a shard with no live replica without `shards.tolerant` still fails with a 503.
- SOLR-13876: for the relevance sort the slice maximum equals the group maximum.
- SOLR-14381: an older coordinator reads `totalHitCount` and `matches` with `(Integer)` casts.
- SOLR-15479: a `rows=0` request without a result window reports the pre-rerank score.
- SOLR-10424: no chain in the sample solrconfig adds unknown fields.
- SOLR-11364: an alias `fl=a3:a1` can return the raw `a3` value.
- SOLR-13245: a daemon left running after its collection is deleted stays listed.
- SOLR-9148: the JDBC driver adds no filter queries.
- SOLR-9396: the query parser never reads the `${row.` spelling as a row reference. The Limits depend on it.
- SOLR-15041: "CRLF stays CRLF" (supported by the test's JSON assertion).
- SOLR-11470: the 9.x branch has no such check in its query parser. This is consistent with the query-parsing review's check of `upstream/branch_9x` on `assignment-query-parsing-review` (`reports/query-parsing-review-q1.md`), and was not re-run here.

## Not checked

- Jira text for every ticket. No Jira packet was opened, so the statements that rest on tickets (examples, reporter proposals, affected-version lists) are unverified.
- Gate logs. They are not on disk here, so counts are taken as the receipts record them.
- Full test bodies. Only the cited blocks were read.
- No builds, no tests, no gate runs. GitHub was touched only through read-only `gh pr view` (PR 4779 and PR 5014), with no writes.
