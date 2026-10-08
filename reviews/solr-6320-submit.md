# solr-6320-submit

- Branch: origin/solr-6320-submit
- Head: 2789d8020105 (matches the listed head)
- Base: upstream/main at 9d7cc2884e8a (merge-base 97d973814336, 19 commits behind)
- Scope: 3 commits, 3 files (+167/-3). `ExtendedDismaxQParser.java` (+25/-3: `foundOperators` at lines 498-516 now counts a lowercase `or` only through `isPromotedOperatorWord` (line 509, helper at lines 524-529); `rebuildUserQuery` promotes through the same helper (line 546); `isExplicitOperator` at line 560), `TestExtendedDismaxParser.java` (+133, four tests), changelog `SOLR-6320-edismax-lowercase-operator-term.yml` (+8, type `fixed`)
- Verdict: Needs work (the neighbour rule is case-sensitive while promotion is not, so a mixed-case or chained operator still reaches the fallback. Two owner calls are open.)
- Reviewer: claude-haiku-5-5 (Claude Code), 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. Every claim below rests on reading the diff and the code at the listed head.

## Delta check against earlier disposition

- Bulk review `research/branch-reviews/round-28/SOLR-6320-review.md` (verdict Needs work) was written at snapshot `ac1f936855b`, an ancestor of the head. The delta (`git diff ac1f936855b 2789d8020105`) adds `2789d802010` (the demoted `or` is not an operator for mm either) and its test matrix.
- Round-28 F1 (demotion rule is a semantic choice nobody has endorsed): still open. An owner call; see below.
- Round-28 F2 (`foundOperators` still counts a demoted `or`): addressed in code. `foundOperators` (line 509) uses the same helper as `rebuildUserQuery`. Its discriminating power is weak; see finding 4.
- Round-28 F3 (case-exact neighbour test, case-insensitive promotion): still open and verified; see finding 1.
- Round-28 F4 (the pin test freezes a known-bad fallback): addressed. The test carries a TODO and says the shape is out of scope.
- Round-28 F5 (no proof recorded): still open; see finding 5.
- Round-27 disposition: taken from the round-28 table; `round-27/SOLR-6320-review.md` was not re-read.

## Findings (ranked)

1. **MEDIUM, verified by reading. The neighbour rule is case-sensitive; promotion is not.** `isExplicitOperator` (line 560) accepts only the exact `AND`, `OR` and `NOT`. `rebuildUserQuery` promotes with `equalsIgnoreCase` (line 546). For `Zapp And and Brannigan`, the neighbour `And` is not explicit, so both `And` and `and` are promoted. The rebuilt query is `Zapp AND AND Brannigan`, which does not parse, and the escaped fallback runs. That is the failure the ticket describes, reached through a mixed-case operator. Two adjacent lowercase operator words behave the same way (`x and or y` becomes `x AND OR y`). The branch pins `Zapp and and Brannigan` to the fallback (`testLowercaseOperatorBoundaryPins`). The rule as written is inconsistent with the promotion it guards. Options (owner call): treat a neighbour that will be promoted as explicit for the chain, so the chained word is demoted; or stop promoting any word that touches another operator word.

2. **MEDIUM, owner call (round-28 F1, still open). The demotion rule changes parses for opt-in users.** `Zapp NOT and Brannigan` now parses as a normal query. Base fell back to the escaped form, where NOT was a required term and nothing matched (`testLowercaseOperatorNextToExplicitOperatorResults`). The JIRA comment from Shawn Heisey says `x AND and AND y` is expected to fail with `lowercaseOperators=true`. This branch overrides that. The changelog says a query that used to fall back now parses. The PR should state that compatibility effect. Not decided here.

3. **LOW, hypothesis. Boundary words are neither promoted nor counted.** `isPromotedOperatorWord` requires `i > 0 && i + 1 < size`, and both `rebuildUserQuery` and `foundOperators` now use it. A lowercase `or` at either end is no longer counted for mm; base counted it. I could not find a configuration where this changes the result under the default mm rules: under `q.op=AND` the clauses are required, and under `q.op=OR` the derived mm is 0% either way. This is a hypothesis. No test pins a boundary `or`. Add a boundary pin, or a comment saying the boundary is intended.

4. **LOW, proof. The demoted-`or` matrix does not discriminate the mm change.** `testLowercaseOperatorDemotedOrShapes` says in its own Javadoc that the parsed query is "the same with and without that alignment". So the `foundOperators` change (finding 2 of round 28, line 509) is not covered by any test that would fail without it. A test that observes mm on a shape where mm matters would close this.

5. **LOW, proof (round-28 F5). No queue result and no fail-before verdict.** By reading, the exact-string cases fail on base, because the doubled operator makes the rebuilt query invalid and the fallback produces a different string. This has not been run.

6. **LOW, resolved (round-28 F4).** The chained-operator pin carries a TODO, and the comment says the shape is out of scope.

## Owner calls (not decided here)

- Accept the demotion rule (finding 2), or close the ticket as expected behaviour, as the JIRA comment suggests.
- Chained and mixed-case neighbours (finding 1): pick the rule.
- Keep `foundOperators` aligned with the demotion rule. The code now aligns; whether to keep it is the owner's call.

## Interactions with other branches

- SOLR-4362: the pf code (`addPhraseFieldQueries`, base line 313) still skips only exact uppercase `AND|OR|NOT|TO` by `clause.val`. A demoted lowercase `or` therefore stays a pf term. That matches the main query and is base behaviour. The slop rule in 4362 does not read operator words, so the two branches do not interact in code. The handoff ties the 6320 demotion rule to 4362 at PR time; the only shared place is that pf skip.
- SOLR-3729: both branches change the first-parse text (3729 the colon escape, 6320 the operator words). No shared code.

## Not checked

- Not compiled, formatted, or run. The exact-string expectations are taken from the test file and from reading `rebuildUserQuery` and `isExplicitOperator`.
- The mm path (`SolrPluginUtils.setMinShouldMatch`) was not traced for the boundary case beyond the default-mm reasoning in finding 3.
- The round-27 file was not re-read; see the delta section.
- No GitHub or JIRA writes.
