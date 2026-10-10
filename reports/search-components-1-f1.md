# Search components round 1, sub-batch 1, part f1 (SOLR-5394, SOLR-10844)

Result: both tickets are draftable. SOLR-5394 is ready as drafted. SOLR-10844 needs two text FIXes (a code comment and the changelog title) before its branch text is used. The two diffs do not overlap, and either landing order works.

Heads read (origin remote-tracking refs, matching the claim table): solr-5394-submit 967445622f9294e26e425ec494d37e5ca13d4fe4 (base b6b2b8f10e98, the merge-base with upstream/main), solr-10844-submit e87515c56d048f96850d4586d6e0508dc177e4c7 (base 22a8cfebbbdb). Drafts: `pr-drafts/search-components/SOLR-5394.md` and `pr-drafts/search-components/SOLR-10844.md`.

## Findings

1. FIX. `solr/core/src/java/org/apache/solr/request/SimpleFacets.java` lines 799-804 at e87515c56d04 (the comment above the guard). Two problems. (a) The comment says the collector "needs SORTED docValues on both fields". Lucene 10.4.0 disassembly (javap -c -p on `TermGroupFacetCollector$MV`) shows `DocValues.getSorted` for the group field and `DocValues.getSortedSet` for the facet field, so a multi-valued facet field needs SORTED_SET. `$SV` uses getSorted for both. (b) "used to return empty counts" appears in no record: the 10844 receipt shows only "expected 400 was 500" and the IllegalStateException, and the Jira packet shows only the IllegalStateException. Replacement for lines 799-804:
   ```
       // SOLR-10844: the grouped facet collector cannot read numeric doc values or points fields, and
       // multi-valued numeric fields are not supported here. Reject these with a clear 400.
   ```
   Lines 805-818 stay as they are.

2. FIX. `changelog/unreleased/SOLR-10844-group-facet-numeric-bad-request.yml` line 2 (the folded title). "instead of empty counts or an internal error" is not shown by any receipt. The receipt shows a 500 only for Points and Trie with docValues. Replacement title text:
   ```
     Grouped faceting (group.facet) on an unsupported numeric facet or group field (points based, docValues, or multi-valued) now fails with a clear 400 error.
   ```

3. FIX (before any push). The local branches are not the live heads. Local `solr-10844-submit` is at e7709d685f2, which is three commits on 22a8cfebbbd (8eb22d118af, ab928362d6f, e7709d685f2). Its last commit adds `SOLR-10844-TESTING.md` (46 lines), and its code differs from origin (an older guard). Local `solr-5394-submit` is at 576e8b441c0. Its code matches origin 967445622f9 exactly; its only extra commit adds `SOLR-5394-TESTING.md` (28 lines). The inventory (`branch-focus-inventory-2026-10-08.md` line 598) records 576e8b441c0 as the pre-move 5394 head. Action: none in the text. Do not push or run `branch.ps1 push` or `finish` from either local branch until the owner picks the line. The reviewed heads are the origin ones.

4. NOTE. `solr/core/src/test/org/apache/solr/request/SimpleFacetsTest.java` lines 609-667 (the new test). The test stops at its first failing assertion. On base, only the numeric facet field block (lines 636-650) ran. The numeric group field block (lines 652-667) has no recorded base result. The draft's Proof does not claim it failed on base.

5. NOTE. Guard coverage (SimpleFacets.java lines 805-818). The guard also rejects enum fields, because `solr/core/src/java/org/apache/solr/schema/EnumFieldType.java` lines 495-497 return `NumberType.INTEGER`, and enum docValues are numeric (imports at lines 33-34). Multi-valued fields are rejected too. Neither case is in the test or a receipt. The draft's Limits names both.

6. NOTE. Guard PointField clause (SimpleFacets.java line 811). A Points group field without docValues is now rejected. Base behavior for that case is not recorded. A Points facet field without docValues already returned 400 before the group path (lines 471-474, "Can't facet on a PointField without docValues"), so the new clause matters only for the group field. The draft's Limits names it. See owner decision 4.

7. NOTE. Facet.threads on the fcs path (SimpleFacets.java in 5394 head). Line 208-211 reads only the local `threads` param. Line 539 starts `case FCS:`, and line 593 has `threads == 0 ? directExecutor : facetExecutor`. Line 885 reads the global `facet.threads`, which sets parallelism between fields only. So a request with `facet.threads=0` and `facet.method=fcs` still uses the facet executor after this change. The reporter's Jira case is not fixed by the default change. This is the 5394 Choice, and the draft's Limits says it.

8. NOTE. Lucene check. The "unexpected docvalues type" message from `DocValues` is present in lucene-core 10.4.0 and 9.12.3 jars (local Gradle cache, extracted to scratchpad). The branch pins Lucene 10.4.0 (`gradle/libs.versions.toml`, apache-lucene = "10.4.0"). The 9.x lucene-grouping jar is not cached, so the 9.x collector call sites are not checked. The drafts say only that the collector "does not accept numeric doc values", which the Jira stack trace and the 10.4.0 call sites support.

9. NOTE. Overlap and landing order. No hunk overlap. 5394 changes SimpleFacets.java line 183 and the test at lines 2737-2746. 10844 changes line 82 (import), lines 799-818, and the test at lines 609-667. Trial merges with `git merge-tree --write-tree` are clean in both orders (rc=0). Each head also merges cleanly onto upstream/main (rc=0). SimpleFacets.java and SimpleFacetsTest.java are unchanged between each base and upstream/main, so both diffs apply to current text. Bases differ: 5394 is 39 commits behind upstream/main, 10844 is 42 behind. Landing order: either works. Suggested order: 5394 first, since its diff is one line plus a test and does not depend on the 10844 guard. Hunk-level overlap with 10492 (part f2) is not checked here, but 10492 merges cleanly with both heads (rc=0).

10. NOTE. Line anchors in `research/branch-reviews/round-28/SOLR-10844-review.md` say the test is at 611-665 and the guard at 799-817. At head e87515c56d04 they are 609-667 and 799-818. Line drift only.

## Task results

SOLR-5394. Draftable. The draft names head 967445622f9. The receipt is at the live tip, and the moved head in the inventory (576e8b441c0 to 967445622f9) is settled by it. The diff is three files: the changelog, SimpleFacets.java line 183 (`int threads = 1`), and the new test. The changelog title is accurate: `nThreads <= 0` means no limit (PerSegmentSingleValuedFaceting.java line 128), so the old default of -1 meant one task per segment. Proof: the new test fails on base b6b2b8f10e98 with `expected:<1> but was:<-1>` (receipt line 7). The draft says the suite runs 49 tests, 0 failures, 0 errors, 1 skipped at this head. The method count matches: 49 `public void test` methods at head, 48 at base. The draft's Choice is live: the reporter expected `facet.threads=0` to stop the threads. The draft does not say that fcs stops using threads, which would be wrong (Finding 7 explains why). The draft is about 3,860 characters with link URLs, slightly over the guide.

SOLR-10844. Draftable after Findings 1 and 2. The draft names head e87515c56d04. The receipt (gated 2026-10-05) matches the live origin head. The diff is three files. Proof: the new test fails on base in the Points and Trie-with-docValues configurations ("expected 400 was 500", receipt line 7). The default and Trie-without-docValues configurations pass on base, so the proof is not a fail-before in every configuration, and the draft says so. The Choice is live (numeric support in the grouped collector). The draft's Limits covers multi-valued, enum, Points group field without docValues (Findings 2, 4, 5 and 6), and distributed requests. The premise matrix logs are not on disk, so the matrix counts come from the receipt only.

## Owner decisions

1. SOLR-5394 Choice: ship the default of 1 as built, or let `facet.threads` control the fcs path (the reporter's expectation). The draft asks this as its pointed question.
2. SOLR-5394: no timing comparison is on record. Decide whether to run one before submission, since the default now serializes segments on this path.
3. SOLR-10844 Choice: reject unsupported numeric grouping with a 400 in this change, or add numeric support now.
4. SOLR-10844 scope: keep the multi-valued, enum, and Points-group-field rejections in this PR (none tested), or narrow the guard to the configurations the matrix covered.
5. Landing order: no conflict either way. Suggested: 5394 first.
6. Local branches: pick which line is kept (origin or the local rewrite with TESTING.md commits). Nothing pushes from local until decided.

## Not checked

- Live Jira. I read the hydrated packets `research/jira-context/SOLR-5394.json` and `SOLR-10844.json` in the main workspace. They record no fetch date.
- Gate and premise logs (g5394-gate.log, g10844-gate.log, g10844-gate2.log, g10844-premise-*.log), the takeover ledger entries, and the round-28 goal file are not on disk. All counts and fail-before results come from the receipts.
- GitHub corroboration runs cited in the receipts (37624116487, 37410344938). No gh calls were made. The inventory and the round-28 review say neither ticket has a live PR.
- The identity of the one skipped test. The likely one is `testSimpleGroupedFacets`, which assumes false when docValues or points is on (test lines 507-510), but the JUnit XML is not on disk.
- Lucene 9.x grouping dispatch (see Finding 8). Lucene 10.4.0 was checked by disassembly only.
- Changelog YAML parse and the Solr changelog validator were not run. The 5394 receipt says the parse passed.
- upstream/main is the local ref 8e62c2686882 (2026-10-09 11:00 -04:00). It was not re-fetched.
- Hunk-level overlap with 10492 (part f2).
- Distributed (sharded) grouped faceting behavior.
- Enum docValues on base (no record).
- Nothing was built, no tests ran, no gh write calls, nothing posted, nothing committed.
