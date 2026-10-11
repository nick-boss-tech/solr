# Search components draft fidelity, slice s5 (edismax)

Assignment and claim ids were not given in the brief. Slice: the five search-components drafts SOLR-15319, SOLR-15331, SOLR-15479, SOLR-15895 and SOLR-16444 in `pr-drafts/search-components/`, checked in the worktree `C:\Users\shaninna\dev\Solr-issues\wt\pr-prepare-suggester`. Read only. No build, test, gate, commit, push or GitHub write.

Round reports used for Limits and choice content (the part whose sections cover each ticket):
- SOLR-15319: `reports/search-components-4-s1.md` (summary: `reports/search-components-4.md`)
- SOLR-15331: `reports/search-components-1-f5.md` (summary: `reports/search-components-1.md`)
- SOLR-15479: `reports/search-components-4-s3.md` (summary: `reports/search-components-4.md`)
- SOLR-15895 and SOLR-16444: `reports/search-components-4-s4.md` (summary: `reports/search-components-4.md`)

Receipts: `receipts/SOLR-<n>.md` exists for all five. Material: a grep of `material/` for the five ticket numbers returned no matches, and no `material/*answers*` file covers them.

Live fork heads, from `git ls-remote origin refs/heads/solr-<n>-submit` run on 2026-10-11. Each matches the head the draft names and the receipt names:

| Draft | Branch | Head named by draft | Live tip |
|---|---|---|---|
| SOLR-15319 | solr-15319-submit | 4bda91f46fc18142809c919d7c563129fdf7fb96 | 4bda91f46fc18142809c919d7c563129fdf7fb96 (match) |
| SOLR-15331 | solr-15331-submit | 6769b4cd8a12d8606a1616c6269c2fdb224d019b | 6769b4cd8a12d8606a1616c6269c2fdb224d019b (match) |
| SOLR-15479 | solr-15479-submit | 57bd53ce4d07a4a392456db5318d52d64f5680a2 | 57bd53ce4d07a4a392456db5318d52d64f5680a2 (match) |
| SOLR-15895 | solr-15895-submit | 02930909397ad52c5c7c61279152b07f2c4ae98d | 02930909397ad52c5c7c61279152b07f2c4ae98d (match) |
| SOLR-16444 | solr-16444-submit | 8ffee94a5e75b17f5b5f8be9f840862a1a56524e | 8ffee94a5e75b17f5b5f8be9f840862a1a56524e (match) |

All five head SHAs resolve with `git cat-file -t` (commit). Code citations were read with `git show <sha>:<path>` at the SHA the draft names. Base SHAs (b5c71bc5573 for 15319, 15331 and 15479; 22a8cfebbbd for 16444 is only named by the receipt) were read from history where the draft links them.

## Verdicts

| Draft | Head checked | Verdict |
|---|---|---|
| SOLR-15319 | 4bda91f46fc1 (live) | DRIFT (2 items) |
| SOLR-15331 | 6769b4cd8a12 (live) | DRIFT (1 item) |
| SOLR-15479 | 57bd53ce4d07 (live) | DRIFT (1 item) |
| SOLR-15895 | 02930909397 (live) | DRIFT (1 item) |
| SOLR-16444 | 8ffee94a5e75 (live) | CONSISTENT |

## SOLR-15319

Verdict: DRIFT (2 items).

Checked and consistent: the head; the Proof counts (TestDistribIDF 4 of 4, TestExactStatsCacheLegacyResponse 3 of 3, verified 2026-10-07, receipt lines 4 to 7); the single failing test on the old key (testLegacyResponsesWithSameShardNameKeyApart); the code citations (ExactStatsCache.java L228 `shard.collection` add, L319-L326 `perShardKey`, L120 exception check, L125 `perShardKey` call, all at 4bda91f46fc); the "Can't determine shard" warning is absent at head and present at base (b5c71bc5573, line 266); the Limits bullets on subclass tests (round 4-s1 finding 8), `StatsUtil.shardUrlToShard` with no callers (finding 11), and mixed-version coverage (`TestExactStatsCacheLegacyResponse` calls `perShardKey` directly); the choice section has a live alternative (keying by URL), with the owner question.

1. Draft says: "It is the case in [SOLR-8051](https://issues.apache.org/jira/browse/SOLR-8051)."
   Evidence: `reports/search-components-4-s1.md` finding 3 (the Jira case "is not reproduced on the traced paths"; a null body with no exception is the only route read, and "Nothing shows that happens") and owner decision 2 (fix and prove, or close as not reproduced). The report does not support equating the null-body path with the SOLR-8051 report.
   Replacement: "That path is unchanged, and this change does not cover it. A follow-up ticket and PR can be opened on request." (replaces the sentence above in the Limits bullet; the link to line 120 stays)

2. Draft says: "Changelog: `changelog/unreleased/SOLR-15319.yml`"
   Evidence: `pr-formula.md` lines 174-175 (the changelog line is a link to the fragment at the head SHA, main-side decision 2026-10-10). The file exists at 4bda91f46fc (`git show` succeeds).
   Replacement: "Changelog: [`changelog/unreleased/SOLR-15319.yml`](https://github.com/nick-boss-tech/solr/blob/4bda91f46fc18142809c919d7c563129fdf7fb96/changelog/unreleased/SOLR-15319.yml)"

Optional notes, not blocking:
- The choice section is the reviewer's proposal, not a receipt record (`reports/search-components-4-s1.md` owner decision 5). The owner should confirm it or cut it.
- The changelog title says "and its subclasses", and the subclass tests were not gated (round finding 8, owner decision 3). The draft Limits already discloses this, so the title and the Limits agree. Narrowing the title moves the head.
- Below the separator (draft lines 49 to 63) are reviewer notes marked "not for posting". They must be deleted before pasting. Nothing above the separator uses process vocabulary or dashes.
- Landing order with SOLR-8051 (finding 7) can move the head.
- Jargon: "collection!shard" and "bare shard name" are compressed; plain wording is optional.

## SOLR-15331

Verdict: DRIFT (1 item).

Checked and consistent: the head; the Proof (NestableJsonFacetTest 2 of 2 at the head, which matches the two `@Test` methods; inconclusive by construction, with no pass or fail claim on base, as the 1-f5 review found); the getMissingCount citations at 6769b4cd8a12 (BucketBasedJsonFacet.java L74-L75 stores the count, L145-L153 is the method, class comment lists `missing`, UNSET_FLAG default at field line 51); NestableJsonFacetTest.java L129-L152 (the test method); the changelog link and file (exists at the head; title matches the code); the Limits (SolrJ-only change, no server test, and the SOLR-17051 `mincount` dependency per 1-f5 finding 7); no choice section, which matches the round report.

1. Draft says: "([FacetFieldProcessor.java](https://github.com/nick-boss-tech/solr/blob/b5c71bc5573c4e31b4cee5a7965d73587fc0ae58/solr/core/src/java/org/apache/solr/search/facet/FacetFieldProcessor.java#L470))" and "([BucketBasedJsonFacet.java](https://github.com/nick-boss-tech/solr/blob/b5c71bc5573c4e31b4cee5a7965d73587fc0ae58/solr/solrj/src/java/org/apache/solr/client/solrj/response/json/BucketBasedJsonFacet.java#L73-L77))"
   Evidence: both links point at the merge-base commit b5c71bc5573 (the pre-change symptom). The lines hold the symptom: base FacetFieldProcessor.java L470 is `res.add("missing", missingBucket);`, and base BucketBasedJsonFacet.java L73-L77 is the ignoring `else` branch. The draft text does not say the links point at the merge-base commit. `pr-formula.md` lines 92-94 require that the text says so.
   Replacement: "([FacetFieldProcessor.java at the merge-base commit](https://github.com/nick-boss-tech/solr/blob/b5c71bc5573c4e31b4cee5a7965d73587fc0ae58/solr/core/src/java/org/apache/solr/search/facet/FacetFieldProcessor.java#L470))" and "([BucketBasedJsonFacet.java at the merge-base commit](https://github.com/nick-boss-tech/solr/blob/b5c71bc5573c4e31b4cee5a7965d73587fc0ae58/solr/solrj/src/java/org/apache/solr/client/solrj/response/json/BucketBasedJsonFacet.java#L73-L77))"

Optional notes, not blocking:
- Blocker on the branch, not in the draft: the owner-note trailer `Co-Authored-By: Claude` on a9f5ecf9d0c (round 1-f5 FIX 1). The draft says it is not postable until this is fixed. The rewrite moves the head, so the Proof must name the new SHA.
- The Limits sentence about SOLR-17051 is conditional (1-f5 owner decision 5). The draft's wording "UNSET_FLAG is returned if the user did not request the bucket" differs slightly from 1-f5 NOTE 7 ("did not request it"). Either is accurate.
- The date 2026-10-03 is the receipt's hardening and push date, not a run time (1-f5 Not checked).
- The owner notes below the `---` separator ("not for posting") must be removed before pasting.

## SOLR-15479

Verdict: DRIFT (1 item).

Checked and consistent: the head; the Proof (TestReRankQParserPlugin 14 of 14 at 57bd53ce4d07, checked 2026-10-07; base b5c71bc5573 gives 3 failures, which are the three new tests, matching receipt lines 6 and 7); the test names at head (lines 209-210, 251-252, 321-322) and the multi-threaded loop in each test (`for (boolean multiThreaded : ...)`), which supports "the tests run both"; the code citations (base SolrIndexSearcher.java L1911-L1914 is the pre-rerank maxScore read; head L1827-L1846 is `rescoredMaxScore`, whose body reads `topDocs`, and the call sites at L1933 and L1950 (getDocListNC) and L2057 and L2073 (getDocListAndSetNC) cover both single and multi-threaded paths); the "at least the result window size" claim (supersetMaxDoc at head L1613-L1618); the rows=0 Limits (4-s3 finding 10); the choice section (options 1 and 2 with a cost each, and the owner question, matching owner decision 2); the changelog link and file (exists at 57bd53ce4d07).

1. Draft says (no title line; the changelog line at draft line 42 links `changelog/unreleased/SOLR-15479.yml` at the head, whose title reads "maxScore of a response that uses a rerank query (rq) now reflects the rescored documents instead of the maximum seen before rescoring")
   Evidence: `reports/search-components-4-s3.md` finding 7. The new tests contradict "reflects the rescored documents": in `testRerankMaxScoreOutsideWindowMatch` (TestReRankQParserPlugin.java L252-L319, assertions at L286-L287), doc 1 keeps its untouched score of 5.0 and sets maxScore. The draft's own bullet "A document outside the rerank window keeps its original score. It can set maxScore" says the same thing.
   Replacement (branch changelog fragment, title line; a change moves the head): "title: maxScore of a response that uses a rerank query (rq) now uses the final scores after reranking, not the scores seen before reranking"

Optional notes, not blocking:
- Branch blockers the report names, which the draft cannot fix: the history rewrite (`Co-Authored-By` trailer on 4a6ffa87358; handoff commits b21e1b940fa and 81602df492f), which moves the head and then the Proof must name the new SHA (finding 5 and 6). The javadoc at SolrIndexSearcher.java L1829-L1830 says "among the returned docs", inside the cited range L1827-L1846. The code body matches the draft text, but the javadoc must be fixed before opening (finding 8). The comment at TestReRankQParserPlugin.java L263-L265 contradicts L290-L294 (finding 9).
- The example scores in "What happens today" (1.4050193 and 3.9578552) are attributed to the ticket's example. They are not in the receipt. They do appear in `research/jira-context/SOLR-15479.json` (grep found them there), so they are sourced from the ticket.
- The change covers every RankQuery, including LTR and export (finding 11). The draft says "a request that uses a rank query", which covers that. Naming the LTR and export paths would be more exact.
- Jargon: "rerank window", "collected result window", "superset" are compressed. Plain wording is optional.
- The Limits section opens without a bold one-line summary (`pr-formula.md` presentation rule, lines 81-87). Optional.

## SOLR-15895

Verdict: DRIFT (1 item).

Checked and consistent: the head; the Proof counts (TestManagedFileStorage 2 of 2 and TestRestManager 5 of 5, both matching the `@Test` recount at head, verified 2026-10-03, tidy and Error Prone passed, receipt lines 4 to 7); the "inconclusive by construction" statement; the code citations (FileStorageIO validateStoredResourceId at ManagedResourceStorage.java L195-L215 at 02930909397; RestManager.java L474 calls the check before `storeManagedData`; the interface default at L82-L86 accepts every id); the Limits (no endpoint test; existing resources not repaired; DELETE unchanged; schema-declared handles not checked, round 4-s4 findings 7 and 8); the platform behavior on Linux and macOS (finding 9); the choice section (file system check versus fixed list, with the owner question, matching owner decision 5); the changelog link and file (title matches the code); no em or en dashes; no process vocabulary above the line (no separator or owner notes in this draft).

1. Draft says: "The Windows branch of the test is the one that covers this bug."
   Evidence: `reports/search-components-4-s4.md` finding 5 and owner decision 4: the record does not say which branch of the test ran (`Constants.WINDOWS`), so the owner must confirm the Windows branch ran at 02930909397 or cut the sentence. Receipt lines 6 and 7 give the counts and the inconclusive status, not which branch ran. The test code (TestManagedFileStorage.java L67-L77) confirms that the Windows branch is the one that exercises the bug, but the draft's wording reads as a run claim.
   Replacement: "The Windows branch of the test is the one that covers this bug. The counts above do not show which branch ran."

Optional notes, not blocking:
- Branch blocker: the head commit 02930909397 has a Claude `Co-Authored-By` trailer (round 4-s4 finding 1). Removing it moves the head, and the Proof must then name the new SHA.
- The "What happens today" facts (the resource id, HTTP 500, InvalidPathException) come from the Jira report and were not checked against the Jira packet (outside checks 1 to 8).

## SOLR-16444

Verdict: CONSISTENT.

Checked and consistent: the head; the Proof (TestRestManager 5 of 5 and TestManagedStopFilterFactory 3 of 3 at 8ffee94a5e75, verified 2026-10-07; the base failure matches receipt line 7 and the round report); the code citation (RestManager.java L230-L242 at the head, the late-observer call `existing.notifyObserversDuringInit(existing.managedInitArgs, List.of(observer))`, so only the new observer is notified); the test citations (TestRestManager.java L152-L180 is the javadoc and the `testLateObserverIsNotified` method; TestManagedStopFilterFactory.java L199-L242 is the javadoc and the `testLateRegisteredStopFilterFactoryIsInitialized` method); the Limits (reporter path not confirmed, one path only, per round 4-s4 finding 10 and owner decision 6); no choice section, which matches finding 11; the changelog link and file (`SOLR-16444-late-managed-resource-observer.yml` exists at the head; title matches the code); no dashes; no process vocabulary above the line.

Optional notes, not blocking:
- The Limits say "It does not go through a schema reload." The regression test calls `restTestHarness.reload()` (TestManagedStopFilterFactory.java L215, a core reload) before it builds the late factory by hand. The sentence is accurate for the late registration, but "does not go through a schema reload" could be read as "no reload at all". Optional clarification: "The late factory is built by hand after a core reload."
- The changelog title says "for example through a schema update". The record does not show that path for the reporter (Limits say so). Owner decision 6 covers the wording.

## Not done

- No build, Gradle, test, gate, test-queue or drain run (per brief). No `gh` call and no Jira access. GitHub run conclusions named in the receipts (37683158242, 37683278146, 37585441072, 37688766357, 37586157900, 37706665083) were not checked. None of the five drafts cites them.
- Gate and premise logs named in the receipts are not on disk (round reports say so). Counts were checked against the receipts and against `@Test` or test-name recounts at head, not against JUnit XML.
- Jira "What happens today" facts were not checked against the Jira packets, except the SOLR-15479 example numbers, which were located by grep.
- Upstream main was not re-fetched. Base SHAs were read only where the draft links them.
- Plain-language notes are optional and partial. Presentation-rule checks were made per section only as far as noted.
