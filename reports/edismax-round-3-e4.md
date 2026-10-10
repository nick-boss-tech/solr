# eDisMax round 3, part e4: SOLR-12092 and SOLR-14913

Result: both tickets agree with the record and are draftable (drafts filed); 12092 needs three FIXes before a PR (Claude trailers on three commits, changelog wording, ref guide sentence), 14913 needs one changelog FIX, and 12092 conflicts textually with 2309 at one insertion point while 14913 merges cleanly with 2309, 3243, 6009 and 12092.

## Heads checked

| Ticket | Branch | Head used | Matches claim table | Base (merge-base with upstream/main) |
|---|---|---|---|---|
| 12092 | `origin/solr-12092-submit` | `ca9573dabd385a38711bd60633973ae46b7a552e` | yes | `14c7aac0d151` |
| 14913 | `origin/solr-14913-submit` | `b80221f46d3ce5889c510b97a97d8748a2ea4c7c` | yes | `b5c71bc5573` |

The local ref `refs/heads/solr-12092-submit` is at `3a54f4f3fdf`, three commits behind origin (`rev-list --left-right --count` gives 0 and 3). For 14913 the gated tree `51fe1087fe9` differs from the tip only in two comment hunks in `ExtendedDismaxQParser.java` and one comment hunk in `TestExtendedDismaxParser.java`; checked line by line with `git diff 51fe1087fe9 b80221f46d3`.

## Findings

**1. FIX (12092, commit history).** `git log --format=%B origin/solr-12092-submit --not upstream/main` shows `Co-Authored-By: Claude Sonnet 5.5 <noreply@anthropic.com>` on `cde23f0a9d7`, `b989934d4ea` and `3a54f4f3fdf`. Subjects of `3a54f4f3fdf` and `c171ae986d7` say "handoff", which is internal vocabulary. Rule: no Claude trailers on pushed commits, and no process words in public text. Replacement: squash the six branch commits into one commit with the subject `SOLR-12092: edismax stopwords=false also keeps ManagedStopFilterFactory words` and no trailer. This rewrites the fork branch, so it needs Nick's explicit go-ahead; the workspace rule is that push never forces.

**2. FIX (12092, changelog).** `changelog/unreleased/SOLR-12092-edismax-managed-stopwords.yml` lines 1-3 (folded title). Evidence: the index-side rule at head `ExtendedDismaxQParser.java` L1510-L1513 is a behavior change, and the title names only the query side. Replacement for lines 1-3:
```
title: >
  The edismax parameter stopwords=false now also keeps the words removed by a
  ManagedStopFilterFactory in the query analyzer. It is also ignored when the index analyzer has a ManagedStopFilterFactory, as it already was for a StopFilterFactory.
```

**3. FIX (12092, ref guide).** `solr/solr-ref-guide/modules/query-guide/pages/edismax-query-parser.adoc` line 110. Evidence: at head the index-side check returns early for any stop filter (L1510-L1513), so with a stop filter in the index analyzer the parameter does nothing. That was already true for `StopFilterFactory` on base (`branch_9x` shows the same early return); the branch extends it to the managed filter, and the guide does not say so. Replacement for line 110 (keep the sentence before it):
```
If this is set to `false`, then the stop filter in the query analyzer is ignored. If the index analyzer also has a stop filter, this parameter has no effect.
```

**4. NOTE (12092 with 2309, textual conflict and order).** `git merge-tree --write-tree origin/solr-12092-submit origin/solr-2309-submit` exits 1. The one conflict is in `ExtendedDismaxQParser.java`, merged lines 1499-1522: both branches add a method at base line 1490 (12092 `isStopFilter`, head L1492-L1494; 2309 `analysisYieldsNoTokens`, 2309 head L1501). Resolution: keep both methods, one after the other, and keep both import sets (2309: `java.io.IOException`, `org.apache.lucene.analysis.TokenStream`, `org.apache.solr.schema.TextField`; 12092: `org.apache.solr.rest.schema.analysis.ManagedStopFilterFactory`). Semantics: a query can reach both changes. `the~` on a field whose query analyzer has a managed stop filter, with stopwords=false, reaches 2309's FUZZY branch (2309 head L1481), and 2309's helper calls `noStopwordFilterAnalyzer` when `removeStopFilter` is set (2309 head L1507). So 2309 reads 12092's predicate. The non-fuzzy path reads the same method through `newFieldQuery`. Both paths use one analyzer, so the final behavior does not depend on the landing order. Suggested order: 12092 first, since it is the smaller change, then 2309 with the resolution above.

**5. NOTE (12092, local branch).** `refs/heads/solr-12092-submit` is behind origin by three commits. Do not build or push from it. Sync it with origin as a main-side step, after the decision in finding 1.

**6. NOTE (12092, shared test schema).** The three new field types at `solr/core/src/test-files/solr/collection1/conf/schema-rest.xml` L502-L540 are shared with other test classes (round 28 L2). The receipt names nine classes but not which ones, so this was not checked by reading. The draft's Limits names a full run as the remaining check.

**7. FIX (14913, changelog title).** `changelog/unreleased/SOLR-14913.yml` line 1. The title says the change avoids "failing the whole query". Evidence: base `ExtendedDismaxQParser.java` lines 416-428: the catch comment reads "ignore failure and reparse later after escaping reserved chars", and it sets `exceptions = false`. Base does not fail the query; it re-parses the escaped query. Replacement for line 1:
```
title: edismax field aliases now skip targets that are missing from the schema instead of re-parsing the query with escaped characters; an alias with no usable target matches nothing
```

**8. NOTE (14913, draft Proof).** The all-missing assertions in `testAliasingWithNonSchemaField` (head L1017-L1031, all `nor`) also return no documents on base. The test's own comment says so (L1017-L1020). The draft states this and does not claim them as proof.

**9. NOTE (14913, round 28 M2 still open).** No assertion in the new test fails on base for the all-missing change, so the compound-query change is not pinned. Proposed addition after head line 1031, not run:
```
    assertQ(
        req("defType", "edismax", "q", "myalias:Zapp AND name:Zapp", "f.myalias.qf", "nosuchfield"),
        nor);
```
The round 33 evidence figure in the receipt (1 document on base, 0 at head for an AND compound) is taken as stated. Adding it changes the tree and needs a new gate on the main side. Owner decision 3.

**10. NOTE (14913, nested all-missing alias untested, round 28 L2 still open).** Head L1411-L1413 keep an alias target that is itself an alias, so an outer alias keeps an inner alias whose targets are all missing. That branch returns a match-nothing query inside a DisjunctionMaxQuery, which changes the flat per-clause shape and where `mm` applies. No test. The draft's Limits names it.

**11. NOTE (14913 with 6009, semantics only).** 6009's `getRegexpQuery` (6009 head L1163) calls `getAliasedQuery()`, so a regexp on an all-missing alias now also gets `MatchNoDocsQuery`. No test covers that shape. No textual conflict.

**12. NOTE (14913, stale premise gone).** The first version of the new comment (commit `589d9aea39e`) said the old code fell back to querying the alias name. Round 28 read the old result as a dropped clause, and the receipt agrees. The tip (`b80221f46d3`) now says "an alias with no usable target matches nothing", and no head comment mentions the alias name (grep, no matches).

**13. NOTE (drafts, length).** `pr-drafts/edismax/SOLR-12092.md` is about 3,875 characters and `SOLR-14913.md` about 3,921, above the 3,500 guide. About a third of each is link URLs. Left as is; trim if the owner wants the guide strictly.

### Hunk map (base line numbers, ExtendedDismaxQParser.java)

- 12092: import at 55; new method before 1490; checks at 1506 and 1517.
- 14913: import at 41 (MatchNoDocsQuery); 1180 and 1223 (all-missing returns); 1399 and 1418 (target skips); 1431 (helpers, before `getQuery` at 1432).
- 2309: 1477 (FUZZY branch); 1490 (helper); imports inserted after base lines 18, 29 and 57.
- 3243: 1134 (`getRangeQuery`), no overlap with 14913 or 12092.
- 6009: 961 (`QType.REGEXP`); 1160 (`getRegexpQuery`, before `getFuzzyQuery` at 1162); 1476 (switch case in `getQuery`).

14913 and 12092 do not overlap any of 3243, 6009 or 2309 in a shared hunk. Trial merges (`git merge-tree --write-tree`) of 14913 with 3243, 6009, 2309 and 12092 are clean, and 12092 with 3243 and 6009 is clean. The only conflict among the pairs checked is 12092 with 2309 (finding 4).

## Task results

**SOLR-12092: consistent and draftable. Hold the PR until finding 1 is done.** The head `ca9573dabd3` matches the receipt. The round 28 findings M1 (ref guide, now updated in the branch) and L1 (index-side case, now test 3) are resolved at this head. Counts agree with the tree: `TestManagedStopFilterFactory` has five `@Test` methods at head, two on base, and `TestExtendedDismaxParser` has 39 JUnit tests at head and on base, which is 38 `void test` methods plus `killInfiniteRecursionParse`. The round 33 report carries a draft for this head, but it is not on disk, so the draft filed at `pr-drafts/edismax/SOLR-12092.md` is written from the record, not adopted. The draft's changelog and ref guide sentences describe the state after findings 2 and 3; re-check its links after those fixes and the squash, since the head will change.

**SOLR-14913: consistent and draftable. Fix the changelog title (finding 7) before a PR.** The tip `b80221f46d3` adds only comment changes to the gated tree `51fe1087fe9`. The receipt count of 40 of 40 matches: 39 JUnit tests on base plus `testAliasingWithNonSchemaField`. The recorded owner decision is still pending (owner decision 1). The draft is at `pr-drafts/edismax/SOLR-14913.md`, written against `b80221f46d3`, with the Proof stating that the all-missing assertions guard and do not prove the compound change.

## Owner decisions

1. **Already on record (SOLR-14913).** Ratify the all-missing alias behavior: `MatchNoDocsQuery` at the head, against the base's silently dropped clause. Recommendation on record: keep `MatchNoDocsQuery`. The draft poses it as the Choice. Reading the code again confirms the compound-query effect and does not change the recommendation.
2. **New, not on record (SOLR-12092).** Keep the index-side rule in this PR, or narrow the change to the query-side check only. The draft poses it as the Choice. If the rule stays, findings 2 and 3 stand.
3. **New (SOLR-14913).** Add the compound-query assertion from finding 9 (needs a new gate on the main side), or ship the Proof as drafted with the all-missing caveat.
4. **Authorization needed (SOLR-12092).** Approve the squash in finding 1, which rewrites the fork branch.

## Not checked

- No builds, tests, `gh` calls, Jira reads, pushes or posts. Git calls were read-only. `git merge-tree --write-tree` wrote tree objects only; no refs changed. Scratch copies went to the session scratchpad.
- Round 33 reports and the goal-file reviews for 12092 and 14913 (`reviews-2026-10-07-round33-reviews/`) are not on disk, so the round 33 draft for 12092 was not checked or adopted.
- Gate logs (`g12092r33-gate.log`, `g12092r33-check2.log`, `gate-14913.log`, `g14913r33-evidence.log`) and the GitHub runs (37594429145, 37588170948, 37594742467, 37599059000) are not on disk. Receipt statements are taken as stated. The receipt names the failing 14913 assertion on base only as "parsedquery".
- The nine test classes in the 12092 run are not named in the receipt.
- Jira text for 14913: only a truncated snippet on disk (`research/pipeline/candidates.csv` row 174, cut at "bracke"). No hydrated JSON. The Jira MCP was not used.
- Compilation of the 12092 and 2309 resolution, and of any merged tree, was not checked.
- Lucene: the 10.x line is `apache-lucene 10.4.0` in `upstream/main` `gradle/libs.versions.toml` (line 39). For 9.x, `upstream/branch_9x` has no `libs.versions.toml`; I checked only that `solr/core` on `branch_9x` imports `org.apache.lucene.search.MatchNoDocsQuery`. The 9.x Lucene pin was not read. The drafts make no Lucene version claim beyond that class.
- Pairs beyond those in the e4 scope were not checked.
- Remote heads were taken from the local `origin/` refs fetched by the claim; no `ls-remote` was run in this part.
- The other drafts in `pr-drafts/edismax/` (3243, 3729, 3962, 6009, 6320) belong to other parts and were not touched.
