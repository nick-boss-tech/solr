# eDisMax round 3, part e3 (SOLR-6009, SOLR-6320): consistency pass and drafts

Result: both tickets match the record at their live heads and are draftable. SOLR-6009 (head a41bb034a1f): draft filed, two FIX items before the PR opens. SOLR-6320 (head cb710c96353): draft filed from the record, two FIX items in test comments (they move the head), one NOTE on a commit subject and one on receipt wording.

## Findings

1. **FIX (SOLR-6320): a test comment overclaims and narrates an internal re-check.**
   File: [TestExtendedDismaxParser.java lines 395-402](https://github.com/nick-boss-tech/solr/blob/cb710c963531c4f32166e6e0b28c45e54b164a3f/solr/core/src/test/org/apache/solr/search/TestExtendedDismaxParser.java#L395-L402), javadoc of `testLowercaseOperatorDemotedOrShapes`.
   Evidence: lines 399-402 say a change to either rule "shows up here", then say "no test can observe the alignment on its own", and narrate "a later re-check" and "a scratch tree". The mm code explains why: the BooleanQuery.Builder overload of `setMinShouldMatch` sets mm only when there are optional (SHOULD) clauses, and `parseMinShouldMatch` derives 100% under AND and 0% under OR when no mm param is set (`solr/core/src/java/org/apache/solr/util/SolrPluginUtils.java` lines 499-503 and 465-490 at this head). So the alignment cannot change these strings.
   Replacement: replace lines 395-402 with these four lines (3 leading spaces, as in the rest of the javadoc):
   ```
      * foundOperators must agree with the demotion rule and not count the word as an operator, or it
      * would switch off the configured mm for a query that contains no OR. These strings pin the
      * demotion rule. The mm check does not change them: under q.op=AND every clause is required, so
      * mm has no optional clause to act on, and under q.op=OR the derived mm is already 0%.
   ```

2. **FIX (SOLR-6320): a TODO says a future fix would flip a case whose expected string does not change.**
   File: [TestExtendedDismaxParser.java lines 336-342](https://github.com/nick-boss-tech/solr/blob/cb710c963531c4f32166e6e0b28c45e54b164a3f/solr/core/src/test/org/apache/solr/search/TestExtendedDismaxParser.java#L336-L342).
   Evidence: line 343 says each case in the table is "identical before and after the fix". The chained case `Zapp and and Brannigan` has one expected string, and it is the escaped-path output. Nothing in the test would change if the chained words were read as terms, so "a future fix should flip" is wrong. (Not run: whether a term-only parse of that input gives the same string.)
   Replacement for lines 336-338: replace with
   ```
      * promotion candidate. In "Zapp and and Brannigan" the chained lowercase operators have no
      * explicit operator neighbor. Both still promote, the rebuilt query is invalid, and the escaped
      * form is used; that shape is outside this ticket's scope.
   ```
   Delete lines 341-342 (the two `// TODO` and `// a known out-of-scope case` comment lines).

3. **FIX (SOLR-6009): the changelog says "(or errored)" with no evidence.**
   File: [changelog/unreleased/SOLR-6009-edismax-regexp.yml lines 2-5](https://github.com/nick-boss-tech/solr/blob/a41bb034a1f47c08a8f8ea6ebbe46e6d443cbb6f/changelog/unreleased/SOLR-6009-edismax-regexp.yml#L2-L5).
   Evidence: "matched nothing (or errored)" comes from the wording of the round 28 review (`research/branch-reviews/round-28/SOLR-6009-review.md`, F3), which did not test an error. The ticket data says zero hits: `research/jira-context/SOLR-6009.json` reads "numFound=0 for both of these".
   Replacement for lines 4-5 (keep line 3 as it is):
   ```
     regular expression clause previously matched no documents; it now matches the same way
     a fielded regular expression clause does.
   ```

4. **FIX (SOLR-6009, before the PR opens): two commit subjects carry internal vocabulary.**
   Commits: `8b17fca972a` "SOLR-6009: add hypothetical-reproduction handoff doc" and `b2976bdc7dc` "SOLR-6009: remove the hypothetical-reproduction handoff doc before submission". The final tree is clean (three files, `git diff --stat 14c7aac0d15 origin/solr-6009-submit`), so only the history shows the words.
   Replacement: squash those two commits away (they net to nothing), or drop them, and keep the same final tree. Check it with `git diff --quiet origin/solr-6009-submit <new-branch>` (exit 0 means the trees match). A new branch tip means a new head SHA, so the receipt and the draft's Proof must name it. This round does not edit branches; the owner or the main side does this.

5. **NOTE (SOLR-6320): a commit subject uses a process word.**
   Commit `cb710c963531` "SOLR-6320: pin boundary lowercase or; record mm alignment re-check" uses "re-check". If history is cleaned, the replacement subject is "SOLR-6320: add tests for lowercase or shapes the rule leaves alone".

6. **NOTE (family landing): SOLR-6009 and SOLR-6320 conflict in the test file, not in the parser.**
   Evidence: `git merge-tree --write-tree` gives tree `08e70f095078` for 6009 then 6320, and `3d08f57bfe4b` for 6320 then 6009. Both report one content conflict in `TestExtendedDismaxParser.java`, at the same anchor (base lines 259-261, just before `testCharFilter`). The conflict markers in the 6009-first tree sit at lines 262-497. `ExtendedDismaxQParser.java` auto-merges in both orders. The parser hunks do not overlap: 6009 changes the inner class `ExtendedSolrQueryParser` (enum `QType` at lines 957-965, `getRegexpQuery` at 1162-1169, dispatch at 1487-1488, all at the 6009 head), and 6320 changes outer-class methods (`foundOperators` 498-517, `isPromotedOperatorWord` 524-529, `rebuildUserQuery` 539-557, `isExplicitOperator` 560-564, at the 6320 head). So `rebuildUserQuery` (6320) and the regexp handling in `getQuery` (6009) are separate methods, as the record says.
   Reconcile: keep both test blocks at the anchor, the 6009 block first and the 6320 block after it, and keep the 6320 import (`import static org.hamcrest.Matchers.not;`, line 29 at the 6320 head). Test method names do not collide.
   Recommended order: 6009 lands first. The 6320 head still has an owner call open (finding 1 remedy, which could change its parser and tests), so 6320 should absorb the reconcile.

7. **NOTE (receipts): two statements in `receipts/SOLR-6320.md` need restating.**
   Line 6 says "a companion run with the parser class reverted also ran 44 of 44". Read as passes, that contradicts the three base failures on line 7. Read as a count of tests run, it proves nothing. The draft does not cite it. Ask the main side to restate it with passed and failed counts and what was reverted.
   Line 7 says "the boundary pin test passes on base" (singular). The branch has two such tests (`testLowercaseOperatorBoundaryPins` and `testLowercaseOperatorBoundaryOrPin`). The failure list excludes both, so both pass on base.

8. **NOTE (local branch refs are stale).**
   Local `solr-6009-submit` is at `8b17fca972a` and local `solr-6320-submit` is at `1e15fc26623`. The live heads are `origin/solr-6009-submit` at `a41bb034a1f` and `origin/solr-6320-submit` at `cb710c96353` (checked with `git rev-parse`). Review against the origin refs. Not changed here.

## Task results

**SOLR-6009 (head `a41bb034a1f47c08a8f8ea6ebbe46e6d443cbb6f`): consistent with the record; draftable; draft filed.** The live head matches the claim table. The receipt says gate green at that head, 41 of 41, with the base at `14c7aac0d151` showing 2 failures. The count agrees with the branch: 40 methods named `test...` plus `killInfiniteRecursionParse`, which carries `@Test`, makes 41. The round 28 review's F1 (coverage) is closed at this head by the multi-field, fielded, `sow=false`, unknown-field and dynamic-field tests. Its F2 (fan-out cost) is in the draft's Limits. Its F3 (behavior change) is in the changelog, except the "(or errored)" wording (finding 3). No draft exists in the record, so the draft is written from the record: `pr-drafts/edismax/SOLR-6009.md`. Verdict: draftable. The PR should wait for findings 3 and 4.

**SOLR-6320 (head `cb710c963531c4f32166e6e0b28c45e54b164a3f`): consistent with the record; draftable; draft filed from the record.** The live head matches the claim table. The receipt says gate green at that head, 44 of 44, with base `97d973814336` showing 3 failures. The count agrees: 43 methods named `test...` plus `killInfiniteRecursionParse` makes 44. The round 28 F2 (`foundOperators` counting a demoted `or`) is fixed at this head by commit `2789d802010`. The round 28 F3 (mixed-case neighbors) is the owner call, now the Choice in the draft. The round 28 F4 (pin test) is only partly fixed: the TODO remains (finding 2). The round 36 draft text is not on disk, so no adoption check was possible. The draft is written from the receipt, the branch diff, the round 27 and 28 reviews, and the Jira packet: `pr-drafts/edismax/SOLR-6320.md`. Verdict: draftable. Findings 1 and 2 change the head, so the Proof must name the new head if they land.

## Owner decisions

Already on record (stated in the drafts as recorded, not posed as new questions):
- SOLR-6320 finding 1 remedy, mixed-case neighbors: the Choice in `SOLR-6320.md`. The options are the implemented exact-uppercase rule, or counting mixed-case And, Or and NOT as explicit neighbors.
- SOLR-6320 demotion rule (standing call): stated under "What this change does", with its compatibility effect (a query that used to fall back now parses) and the 2014 comment that expected `x AND and AND y` to fail.
- SOLR-6009: no owner decision on record, so no Choice section.

Not on record (questions for the lead, not settled):
- Do findings 1 and 2 (comment-only changes to the 6320 branch) need a re-check at a new head, or are they recorded as a comment-only difference, as 14913 is?
- Should the 6009 history be squashed before the PR opens (finding 4)? That gives a new head SHA.
- Landing order: 6009 first, 6320 second, as recommended in finding 6.

## Not checked

- Gate logs `g6009r32-gate.log` and `g6320r36-gate.log`, and the GitHub run `37606261377` cited in the 6009 receipt, were not checked. The logs are not on disk, and no `gh` call was made. Receipts were used.
- The round 36 reports and the goal file `reviews-2026-10-07-round36-findings/6320.md` are not on disk in this worktree or in the main checkout (searched `research/`, `wt/` and the main checkout). So the round 36 draft text was not checked for adoption.
- The Jira text came from the local packets `research/jira-context/SOLR-6009.json` and `SOLR-6320.json` (2014 snapshot). It was not read live.
- Live heads come from the claim table and the local `origin/` refs. No `ls-remote` or fetch was run.
- No compile and no tests. The `merge-tree` results show textual merges only. The merged trees were not built.
- Lucene: neither draft names Lucene behavior, so no 9.x or 10.x check was needed.
- Base failure messages for the new tests were not checked. The receipts name the tests only. The 6009 draft's description of base behavior (U+FFFC in the parsed query, zero hits) comes from the code path and the ticket, not from a base run.
- That `AND OR` and `AND AND` do not parse was taken from the ticket and the branch's own test comments. Not run.
- Whether a term-only parse of `Zapp and and Brannigan` gives the same string as the escaped form (finding 2). Not run.
- The changelog YAML parse was not re-run. The receipts say it parses at their heads.
- The local `upstream/main` ref (`8e62c268688`) was not fetched. The parser and test files have no commits between either base and that ref (`git log` count 0), so no upstream conflict was seen.
- The mm analysis in findings 1 and 2 is read from source (`SolrPluginUtils.java`). It was not run.
