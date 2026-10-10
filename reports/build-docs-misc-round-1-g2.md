# Build, docs and misc round 1, part G2: SOLR-5821 and SOLR-16914

Audit pass only. Nothing was posted, pushed, checked out or built. Gate and build evidence is the receipts' record. Condensed by the lead from the subagent's final report. Both drafts are in `pr-drafts/build-docs/`.

Shared finding: `receipts/SOLR-5821.md:5` names `g5821-refguide.log` and `g5821-refguide2.log`, and `receipts/SOLR-16914.md:5` names `g16914-lightgate.log` and `g16914-antora.log`. A bounded search under `research/` and `env/` did not find them. Every Proof in these two drafts rests on the receipts' wording alone, not on a log the lead has read. This must be settled before either draft is posted.

## SOLR-5821 (`solr-5821-submit`, head `b8af2d1ce2f5`): draftable on the recorded state

Refs: head matches. Merge-base `14c7aac0d151` matches the receipt. Main read `3f5d4c5bf8ac`. Branch commits `52577bd7ff5`, `b8af2d1ce2f`. Jira JSON read.

Verdict: the three added sentences agree with the page's existing tie-breaker paragraph and with the guide's replica pages. The ticket's document-count half is not addressed and goes to Limits. Limits also name the per-replica term-statistics case, which changes scores rather than ties.

Evidence:
- One file, three insertions: `solr/solr-ref-guide/modules/query-guide/pages/common-query-parameters.adoc`, new text at head lines 74-76; the tie-breaker paragraph at lines 72-73 is unchanged.
- Claim 1 (tied sorts can order differently across replicas): supported by the page's own tie-breaker paragraph. Jira comment 14235725 says the same.
- Claim 2 (documents and consecutive pages can differ): `pagination-of-results.adoc` line 44 covers repeat and skip after index changes; lines 121-126 cover cursor requests across replicas, where term statistics vary per replica. The branch applies the tie case to plain start/rows paging; the guide does not state that case directly. Consistent by the same mechanism.
- Claim 3 (`numFound` unaffected when in sync): `solrcloud-update-consistency.adoc` line 73 (each replica opens its searcher independently) and line 74 (`shards.preference`). Consistent. The guide's own term is "opens its searcher independently"; the branch says "have the same searcher". Wording choice, see owner item 4.
- Main has no commits to this page since the base.
- The ticket's document-count half: Jira comment 13962715 (Markus Jelsma) says "Smells like SOLR-4260". Not addressed by the branch.

Changelog: none added, which matches the no-changelog position for ref-guide-only changes. `dev-docs/changelog.adoc` does not state this explicitly; its "other" row lists documentation as a use case and says most such changes are too small for an entry.

Receipt disagreements:
1. `receipts/SOLR-5821.md:6` records the documentation build on "main 14c7aac0d15". That is the branch base, not current main. The page is unchanged since the base, so the evidence holds for this page. A whole-site build on current main was not run.
2. `receipts/SOLR-5821.md:5` names the two logs; they were not found (see shared finding).

Owner decisions:
1. Changelog for ref-guide-only docs: (a) none, as the branch and the assignment's position; (b) an `other` fragment. Recommendation: (a).
2. Document-count half: (a) keep SOLR-5821 open for it and link SOLR-4260, with Limits saying so; (b) narrow SOLR-5821 to ordering and open a separate ticket for counts. Recommendation: (a) for now. Jira status and links are the owner's call.
3. Per-replica term statistics: (a) leave in Limits, as drafted (recommended); (b) add a sentence to the page, which needs a new head and a light re-check.
4. Wording "have the same searcher": (a) keep the branch wording; (b) "have the same committed documents", which is plainer and needs a new head. Recommendation: (b) if the owner wants plain words.

Not checked: the Antora build was not re-run and its logs were not located; the rendered HTML was not read; no `gh` call; branch_9x text not checked (the draft targets main); SOLR-4260 not read; other guide pages only at the cited lines.

## SOLR-16914 (`solr-16914-submit`, head `cd878023d3d0`): draftable, narrow claim only

Refs: head matches. Merge-base `14c7aac0d151` matches the receipt. Main read `3f5d4c5bf8ac`. Branch commits `b647d03036d`, `5e8fe46708a`, `ed1a00202ea`, `cd878023d3d`. Jira JSON read (status Open; the ticket title has a typo, "workinh").

Verdict: parameter names, the `discardCompoundToken` default, and the default mode check out against Lucene 10.4.0 source. The Synonym Graph Filter note the branch links to exists at head. **One statement is not confirmed by the source read**: that `discardCompoundToken="false"` yields a non-linear token graph. The draft's Limits say a token stream check was not run. Owner item 3 recommends that check before posting.

Evidence:
- One file, 7 insertions and 3 deletions: `solr/solr-ref-guide/modules/indexing-guide/pages/language-analysis.adoc`. Head hunks at lines 2292, 2315-2317, 2351 and 2354. No changelog, no Java.
- Parameter names match Lucene 10.4.0 `JapaneseTokenizerFactory` (`mode`, `userDictionary`, `userDictionaryEncoding`, `discardPunctuation`, `discardCompoundToken`).
- `discardCompoundToken` defaults to `true` (`getBoolean(args, DISCARD_COMPOUND_TOKEN, true)`). `discardPunctuation` also defaults to `true`, matching the table.
- `JapaneseTokenizer.DEFAULT_MODE = Mode.SEARCH`, so "search mode (the default)" at head line 2292 is correct. **The mode row still reads "Default: none" at head line 2302**, which the branch does not touch and which contradicts that sentence.
- The flag applies "when tokenization mode is not NORMAL", matching "search and extended".
- `filters.adoc` line 3131 (same at head): "it cannot consume an input token graph correctly". The xref matches the section at line 3123. The rendered anchor was not checked.
- The reporter's chain (Jira description) uses `mode="search"` with no `discardCompoundToken` attribute. With the default `true`, that chain emits parts only, so the graph case in head line 2316 is not the reporter's path. This supports the Limits line.

Changelog: none, matching the ref-guide-only position (same note as SOLR-5821).

Receipt disagreements:
1. `receipts/SOLR-16914.md:5` names the two logs; not found (see shared finding). The "tidy clean" and "rc=0" wording is from the receipt and was not re-read.
2. `receipts/SOLR-16914.md:7` (round 13 fix on 2026-10-05, light gate at tip) agrees with the branch: the last commit is the head.

Owner decisions:
1. Claim scope: (a) the narrow claim as drafted (the guidance is accurate; the original report is not shown to be resolved); (b) a claim that the reported failure is fixed, which needs a reproduction of the reporter's chain, not run here. Recommendation: (a). Jira comment 17749940 (Shawn Heisey) says Jira is not a support portal; how to answer on Jira is the owner's call.
2. The mode row "Default: none" at head line 2302: (a) a one-cell fix on this branch, which needs a new head and a light re-check; (b) leave it and name it in Limits, as drafted. Recommendation: (a) if the branch is still unposted; otherwise (b).
3. Token stream check before posting the non-linear claim: (a) the main side runs a small stream dump with `discardCompoundToken=false` in search mode before posting; (b) post with the Limits line as drafted. Recommendation: (a).
4. Scope to main only: the draft says branch_9x (Lucene 9.12.3) was not checked. If the owner wants the same text on branch_9x, check it against 9.12.3 first.

Interaction with SOLR-17356 (shared file `language-analysis.adoc`): both branch diffs start from blob `211f816d5a9`, and main has no commit to the file since `14c7aac`, so both apply to main as-is. No overlapping sections: SOLR-16914 at head lines 2292 to 2354 (Japanese), SOLR-17356 at lines 3466 to 3511 (Ukrainian). Landing order: SOLR-16914 first, then SOLR-17356 rebases. An owner-approved mode-row fix would shift lines from 2302 onward, not the Ukrainian lines.

Not checked: Antora build and tidy not re-run; logs not located. Lucene position-length behavior with the flag false: no token stream dump. The rendered anchor for the Synonym Graph Filter xref. branch_9x defaults. The reporter's chain was not reproduced.
