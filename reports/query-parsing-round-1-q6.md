# q6 report: dismax pair (SOLR-874, 6014) and SOLR-12608

Result: None of the three is ready to post. 874 needs three FIX items in its own code and a focused run again. 6014 needs one FIX (a null guard in QParser.getQuery) and one owner call on filter queries. 12608 is held: its premise is not shown and its changelog overclaims.

Heads checked (origin refs, read only; these match the claim table): 874 `ac9ab33753751a6fef05b5f100f5c1c5683510b2`, 6014 `505849d2d4e7d8699a2fe34d410773b8ecf3a55a`, 12608 `d1dd8a1f9f0aa17c6e34f8c5e40cb9768a83aea2`. Merge bases: 874 `cabedd1d968`, 6014 `97d97381433`, 12608 `14c7aac0d15`. The local branch refs are older heads (874 `456f7134a7d`, 6014 `bc4a1d5e811`, 12608 `b8a63f5b68d`) and were not used.

Drafts: `pr-drafts/query-parsing/SOLR-874.md` (held for FIX 1 to 3), `pr-drafts/query-parsing/SOLR-6014.md` (held for FIX 4 and the owner call on NOTE 5), `pr-drafts/query-parsing/SOLR-12608-HOLD.md` (held; not for posting).

## Findings

**FIX 1. SOLR-874, `solr/core/src/java/org/apache/solr/util/SolrPluginUtils.java` lines 659-662 (`stripIllegalOperators`).** Mixed operator chains still throw. Evidence (traced from code, not run): for `ipod AND - OR`, line 660 (DANGLING_OP) does nothing, line 661 (DANGLING_BOOL) strips ` OR`, and the result `ipod AND -` is never cleaned again. For `ipod - AND` the result is `ipod -`. The grammar (`QueryParser.jj` lines 188-198 and 211-229) needs a term after a modifier, so a trailing `-` is a parse error. The existing docstring already says `chocolate chip -` is illegal. Replacement body:

```java
  public static CharSequence stripIllegalOperators(CharSequence s) {
    String temp = CONSECUTIVE_OP_PATTERN.matcher(s).replaceAll(" ");
    String previous;
    do {
      previous = temp;
      temp = DANGLING_OP_PATTERN.matcher(temp).replaceAll("");
      temp = DANGLING_BOOL_PATTERN.matcher(temp).replaceAll("");
    } while (!temp.equals(previous));
    return LEADING_BOOL_PATTERN.matcher(temp).replaceAll("");
  }
```

Add to `SolrPluginUtilsTest.testStripDanglingBooleanOperators` after line 106: `assertEquals("ipod", stripOp("ipod - AND"));`, `assertEquals("ipod", stripOp("ipod AND - OR"));`, `assertEquals("ipod", stripOp("ipod !"));`, `assertEquals("NOT ipod", stripOp("NOT ipod"));`. The last line pins the leading NOT rule the draft describes.

**FIX 2. SOLR-874, `SolrPluginUtils.java` line 647 (`DANGLING_BOOL_PATTERN`).** A trailing `!` is not stripped. Evidence: `QueryParser.jj` line 138 defines NOT as `("NOT" | "!")`, so `ipod !` leaves a modifier with no term. The branch's own operator list is incomplete. Replacement line 647:

```java
      Pattern.compile("(?<=\\S)(?:\\s+(?:AND|OR|NOT|!|&&|\\|\\|))+\\s*$");
```

**FIX 3. SOLR-874, `changelog/unreleased/SOLR-874-dismax-dangling-boolean-operator.yml` line 1.** The title says a leading NOT is dropped. The leading pattern (`SolrPluginUtils.java` lines 649-650) has no NOT, and a leading NOT is kept on purpose. Replacement line 1:

```yaml
title: "dismax: a trailing AND, OR, NOT, && or || and a leading AND, OR, && or || in the user query (for example q=ipod AND) is dropped instead of causing a parse error"
```

**FIX 4. SOLR-6014, `solr/core/src/java/org/apache/solr/search/QParser.java` line 216 (in `getQuery`), with the new check at `DisMaxQParser.java` lines 198-202.** A stopword only `{!dismax}` clause with a `cache` or `cost` local parameter is wrapped around a null query. Evidence (code, not run): `getQuery` calls `extendedQuery()` at lines 226-237 with no null check; `extendedQuery` (lines 244-251) wraps whatever `query` is, including null; `WrappedQuery.java` lines 30-32 accepts null. So the nested clause is not dropped, and at top level `QueryComponent.java` lines 191-195 no longer turns the null into a match-nothing query. The likely result is a failure at search time. Replacement line 216:

```java
      if (localParams != null && query != null) {
```

Add to `TestExtendedDismaxParser.testFocusQueryParser` after line 399:

```java
    // a stopword only dismax clause with cache or cost set must be dropped too
    assertQ(req("q", "{!dismax qf=text_sw cache=false v=the}"), nor);
    assertQ(
        req(
            "q",
            "_query_:\"{!dismax qf=text_sw cache=false v=$b}\" AND _query_:\"{!dismax qf=text_sw v=$a}\"",
            "a",
            "big",
            "b",
            "the"),
        twor);
```

Owner note: `QParser.java` is also changed by SOLR-15906 (q7). Line 216 is unchanged on the 15906 head, and `merge-tree` is clean for 6014 against 15906 as the branches stand. The one-line change has not been merge-checked against 15906 after it is applied. When FIX 4 lands, delete the cache and cost bullet from the 6014 draft Limits.

**NOTE 5. SOLR-6014, filter query path (not run).** `QueryUtils.parseFilterQueries` (lines 284-287) adds `fqp.getQuery()` with no null check. `SolrIndexSearcher.getProcessedFilter` (lines 1242-1265) has no null guard. So a stopword only `fq={!dismax ...}` probably reaches a null filter. The same path already exists today for a blank `{!dismax}` in `fq`, so this is older than the branch, but the branch adds stopword input to it. Test to run before posting: `fq={!dismax qf=text_sw v=the}` should return zero documents with no error. If it fails, the smallest fix is `filters.add(query == null ? new MatchNoDocsQuery() : query);` in `QueryUtils.parseFilterQueries`. That is outside the branch, so it is an owner call. Until then the 6014 draft keeps its filter query Limits bullet.

**FIX 5. SOLR-12608, `changelog/unreleased/SOLR-12608-collapse-repeated-wildcards.yml` lines 1-3.** The title says the change avoids excessive memory use. Nothing in the record shows that: the research note says SKIP and the root cause is unverified (`research/pipeline/research-notes/SOLR-12608.md` lines 1-2), and the round 28 review's MEDIUM finding says the 5000-star test asserts only status 0 (`research/branch-reviews/round-28/SOLR-12608-review.md`, Findings item 1). Replacement title:

```yaml
title: >
  Runs of unescaped * in a wildcard query term are collapsed into a single *. The terms matched do not change.
```

**NOTE 6. SOLR-12608, proof.** The new test is `TestSolrQueryParser.java` lines 276-291. Lines 279-283 are helper checks and cannot run on the base code, because the helper is absent there. Lines 285-290 assert only status 0 for 5000 stars. The receipt says the premise run "discriminated", but `g12608-premise.log` is not on disk, so I cannot read what the base run showed. The draft therefore claims no fail-before result. Any such claim needs a base run that is recorded on disk.

**NOTE 7. Claim premise is wrong about SOLR-11761.** The claim says 11761 "also extends" `TestExtendedDismaxParser`. It does not. `git grep "extends TestExtendedDismaxParser"` finds no class on `origin/solr-11761-submit`, `origin/solr-6014-submit`, or `upstream/main`. The 11761 branch changes only `SolrQueryParserBase.java`, `TestSolrQueryParser.java`, and its changelog. The dismax pair does not share a test class with 11761. The claim and the assignment should be corrected.

**NOTE 8. SOLR-874 round 28 report is not on disk.** I searched `research/branch-reviews/round-28/` (no SOLR-874 file), `reports/`, and `research/pipeline/`. The only 874 rows are `PARKED.md` line 31 (old head) and `queue.json` lines 15713-15728. So the two choices in the 874 draft are my proposal, not the report's text. The owner should confirm them.

**NOTE 9. Stale ledger rows (not edited).** `research/pipeline/queue.json` lines 15713-15728 record SOLR-874 at `456f7134a7d1` with the note "test unrun". `research/branch-reviews/round-28/PARKED.md` lines 31 and 33 list the old 874 and 6014 heads. `research/pipeline/HANDOFF.md` line 169 lists the old 12608 head `b8a63f5b68d`.

**NOTE 10. Interactions (`git merge-tree --write-tree`, read only).** No textual conflict for any of these pairs: 874 with 6014, 874 with 12608, 6014 with 12608, 874 with 15906, 6014 with 15906, and 12608 with 4824, 9149, 11761, 12532, 15906, or 12212. 874 and 6014 change no file that another query-parsing branch changes (checked against the 21 other branches in the round). 12608 shares `SolrQueryParserBase.java` and `TestSolrQueryParser.java` with the q1 cluster; semantic overlap is for q1. Landing order for my three, if all are submitted: 874 (after FIX 1 to 3), then 6014 (after FIX 4 and the owner call), then 12608 only if the owner submits it. No textual order is forced.

**NOTE 11. Scope of the dismax change.** `stripIllegalOperators` is called only from `DisMaxQParser.java` line 195 (grep at the 874 head). `ExtendedDismaxQParser` extends `QParser` (line 66), not `DisMaxQParser`, so both 874 and 6014 leave edismax alone. The 874 draft says so.

## Task results

**SOLR-874: HOLD.** Not ready at `ac9ab33753751a6fef05b5f100f5c1c5683510b2`. FIX 1 to 3 are defects in the branch's own code, so they belong in the branch, followed by a focused run that I have not run. The Proof counts in the draft (SolrPluginUtilsTest 10 of 10, DisMaxRequestHandlerTest 4 of 4, base-code failures in both named tests) come from the receipt at the old head and do not carry to a new head. The base-code run output and the gate log are not on disk. The draft is written against the live head and names it. Its two choices (drop or keep the word, such as "Portland, OR"; operator-only query as error or as empty) are proposals, because the round 28 report is not on disk. Jira shows the escape approach in earlier patches and the "Portland, OR" case in a 2010 comment, so both choices have a basis in the ticket.

**SOLR-6014: HOLD for FIX 4 and the NOTE 5 call.** Verified: the head matches the claim, the new checks sit at `TestExtendedDismaxParser.java` lines 389-399 in `testFocusQueryParser` (the receipt's method name is right), and the scope is dismax only. The 39 of 39 count and the revert result come from the receipt. The CI run number in the draft is from the receipt and was not re-checked, since I made no `gh` calls. I did not write a Choice section. The Jira reporter proposed the same approach ("Maybe one should just remove empty term lists?"), so there is no live alternative that a maintainer would plausibly pick.

**SOLR-12608: HOLD.** The premise is unproven. The research note recommends SKIP, the round 28 review says "Not ready" (MEDIUM), and the premise log is not on disk. The draft (`SOLR-12608-HOLD.md`) claims only what the run covers: the helper checks, and a 5000-star status 0 check at the head, with no fail-before claim and no memory claim. The changelog title must change (FIX 5) before any posting. No draft names a Lucene behavior, so no 9.x or 10.x check was drafted. The "determinizeWorkLimit" root cause in the research note is unverified and is kept out of the draft.

## Owner decisions

1. Approve FIX 1 to 3 for SOLR-874, then a focused run (not by me). Confirm whether a three-line test addition to `SolrPluginUtilsTest` is acceptable.
2. SOLR-874 choice 1: keep dropping a trailing OR (so "Portland, OR" becomes "Portland,"), or keep the word as a plain term.
3. SOLR-874 choice 2: keep the parse error for an operator-only query (`q=AND`), or treat it as an empty query.
4. SOLR-6014 FIX 4: approve the one-line `QParser.getQuery` guard and its test. It touches `QParser.java`, which 15906 also changes, so a merge check is needed after the change.
5. SOLR-6014 NOTE 5: run the filter query check. Decide whether to fix it in `QueryUtils.parseFilterQueries`, which is outside the branch, or to keep the Limits bullet.
6. SOLR-12608: hold, retire, or submit as a narrow wildcard normalization with no memory claim. Any fail-before claim needs a recorded base run.
7. Ledger: refresh the stale rows in NOTE 9 (not edited here).

## Not checked

- No builds, tests, Gradle, `gh` calls, posting, or commits. Git use was read only (`show`, `grep`, `diff`, `merge-base`, `rev-parse`). `merge-tree --write-tree` writes tree objects to the object store but moves no refs.
- Gate and premise logs are not on disk: `g874-gate.log`, `g874-premise.log`, `g6014-gate.log`, `g12608-gate.log`, `g12608-premise.log`. Every count and pass or fail result in the drafts comes from a receipt only.
- The FIX 1 and FIX 2 behavior, the FIX 4 null path, and the NOTE 5 filter path are traced from code and grammar, not run.
- The edismax parser's own handling of `ipod AND` was not checked.
- Jira text comes from the local hydrated packets under `research/jira-context/`. Apache JIRA was not fetched again.
- The 6014 CI run (37639870647) and the 874 and 12608 receipt dates were not checked on GitHub.
- SOLR-15906's `QParser.autoFixPureNegative` change and its effect on the leading NOT rule in 874 were not checked. That is q7's part.
- The round 28 report for 874 was not found, so its two choices are unverified.
- Textual merge checks are not semantic checks. Semantic overlap with the q1 cluster is left to q1.
