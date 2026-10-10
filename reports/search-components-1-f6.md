# Report f6: SOLR-18482 live PR consistency pass (apache/solr #5009)

Result: Head matches at 49ca9099d8e (branch, receipt, live PR). Mergeable is MERGEABLE and CLEAN on the second read (UNKNOWN on the first). The code claims hold. The live description has five FIX drifts, the most important being the Proof head and date (it names 8ef833b64cb from 2026-10-04, not the live head).

## Findings

1. FIX. PR 5009, "Proof", first paragraph.
Evidence: The live Proof says "Verified on the fork's GitHub Actions test runner at head 8ef833b64cb on 2026-10-04". The live headRefOid is 49ca9099d8ebc2b43fb4482791fef736cc036d7a, and origin/solr-18482-submit is 49ca9099d8e. The receipt (receipts/SOLR-18482.md) records a confirmation run at this head on 2026-10-06 with the same counts (TestJsonRangeFacets 10 of 10, TestJsonFacetErrors 5 of 5). `git diff --stat 8ef833b64cb 49ca9099d8e` shows only the changelog (3 lines) and json-facet-api.adoc (one line deleted), so no Java source changed. The 2026-10-04 dispatch tree was origin/ci/solr-18482 = 1eed1270b17, which is 8ef833b64cb plus .github/workflows/fork-test-runner.yml (not in the PR). Error Prone is on by default when the CI variable is set (gradle/validation/error-prone.gradle line 24; gradle/globals.gradle line 175), and the dispatch gradle_args default is empty.
Replacement for the first paragraph of Proof (exact): "Verified at head 49ca9099d8e on 2026-10-06, and the counts match the earlier run. The Java sources have not changed since 8ef833b64cb, which the fork's GitHub Actions test runner used on 2026-10-04 with Error Prone enabled. That earlier run used 8ef833b64cb plus a workflow file that is not part of this PR. Since 8ef833b64cb only a changelog entry and one doc sentence have changed."

2. NOTE. PR 5009, "Proof", the two per-class bullets ("On base code, its range-facet error cases fail" and "The same class also passes on base code").
Evidence: The receipt says one proof-tree run failed at the focused test step, the other passed on base production, and "the per-run test class mapping is not recorded". The proof-tree ref origin/ci/solr-18482-proof = 09e09d20c29 is base production plus the branch's test files (its test tree matches the branch exactly). That makes the TestJsonFacetErrors base failure likely, but the record does not name the failing class or cases. The "none is thrown" failure shape follows from the expectThrows assertions in the test, not from a log.
Action: Before the wording stays, read the job log for the 2026-10-04 proof run and cite the failing class. If the log does not confirm that TestJsonRangeFacets passes on base, delete the sentence "The same class also passes on base code: those cases pin the accepted range-facet behavior, which this change does not alter."

3. FIX. PR 5009, "What this change does" (`FacetRangeParser`), "Proof" (`TestJsonFacetErrors`), and the "Changelog:" line. Bare file citations. The formula requires each file citation to link to the blob at the PR head.
Evidence: Tip anchors checked: FacetRangeParser.java lines 30-31 (list) and 49-53 (check); TestJsonFacetErrors.java lines 386-443 (new test block); changelog file has 7 lines.
Replacements (exact):
- "What this change does": replace `FacetRangeParser` with [`FacetRangeParser`](https://github.com/nick-boss-tech/solr/blob/49ca9099d8ebc2b43fb4482791fef736cc036d7a/solr/core/src/java/org/apache/solr/search/facet/FacetRangeParser.java#L30-L53)
- "Proof", first bullet: replace `TestJsonFacetErrors` with [`TestJsonFacetErrors`](https://github.com/nick-boss-tech/solr/blob/49ca9099d8ebc2b43fb4482791fef736cc036d7a/solr/core/src/test/org/apache/solr/search/facet/TestJsonFacetErrors.java#L386-L443)
- "Changelog:" line: replace the backticked path with [`changelog/unreleased/SOLR-18482-range-facet-unsupported-params.yml`](https://github.com/nick-boss-tech/solr/blob/49ca9099d8ebc2b43fb4482791fef736cc036d7a/changelog/unreleased/SOLR-18482-range-facet-unsupported-params.yml)

4. FIX. PR 5009, "A choice to check", last sentence. The formula requires the section to end with a pointed question. The live text ends "If maintainers prefer warn-first, or holding the rejection for a major release, that change fits in this PR." with no question.
Replacement (exact): keep the existing sentence and append: " Was rejecting these parameters with a 400 now the right call?"

5. FIX. PR 5009, "What happens today", first paragraph. The phrase "the full, unsorted result" is wrong for start, end and gap ranges, which come back in ascending range order (ref guide json-facet-api.adoc line 411 at tip; FacetRangeProcessor.java line 118 comment at base).
Replacement (exact): replace "gets back the full, unsorted result with no signal" with "gets back the full result with no signal".

6. FIX. PR 5009, "Limits". The formula requires an adjacent gap named in Limits with an offer to open a follow-up ticket and PR on request. The live Limits names the other facet types but makes no offer.
Replacement (exact): append to the end of the Limits paragraph: " I can open a follow-up ticket and PR for the other facet types on request."

7. NOTE. PR 5009, "Limits" or "A choice to check". The PR does not say why mincount is not on the list. Range facets read mincount (FacetRangeParser.java line 63 at tip; line 50 at base), so it has an effect and rightly stays off the list. The receipt says the round 29 re-review "resolved the mincount question with no change". Optional sentence: "mincount is not on the list because range facets read it."

8. NOTE. PR 5009, structure. The body predates the 2026-10-08 presentation rule: no section opens with a bold one-line summary. The "Upgrade note" section is not in the formula template and restates the behavior change a third time (What this change does, A choice to check, Upgrade note). Not drafted here. Description edits are Muse's, and a maintainer who is actively commenting means checking with Nick first. Comments were not fetched in this round.

9. NOTE. Upgrade note placement. Round 8 review (research/branch-reviews/round-8/SOLR-18482-review.md, 2026-10-04) raised the changelog type and the author block. Both are now closed on the branch: type is changed (commit 4e781931d89) and the author block reads "Nick Shanin" (commit f09608f5446). The changelog guide (dev-docs/changelog.adoc) has no upgrade-note key, so the upgrade note exists only in the PR body. Whether it also belongs on a ref guide upgrade-notes page is an owner call; no such page changed on this branch.

10. NOTE. Ref guide edit not described. The branch adds two sentences to solr/solr-ref-guide/modules/query-guide/pages/json-facet-api.adoc (tip lines 410-411): the seven-parameter list and the bucket order. The PR body does not mention them. Optional sentence for "What this change does": "The Range Facet section of the ref guide now lists the parameters that do not apply."

11. NOTE. Check run head SHAs. `gh pr checks 5009` shows changelog check pass, "Run Solr Tests using Crave.io resources" pass (run 37390531043), gradle check pass (run 37390531495), labeler pass, and generate skipping. The output does not show head SHAs, and no receipt records a full gate at the tip. Before these count as tip evidence, confirm the head_sha of runs 37390531043 and 37390531495 is 49ca9099d8e (outside this round's allowed calls).

12. NOTE (info). Round 8 finding 2 (only the facet test classes ran) still holds as history; the tip has the same test set. The round 8 claim that the seven keys are unread by the range parser is confirmed (see Task results).

## Task results

Head check: the branch origin/solr-18482-submit, the receipt head, and the live headRefOid all match 49ca9099d8e (the tip commit is docs only, one deleted sentence). Mergeable: the first `pr view` read was UNKNOWN and UNKNOWN; the second was MERGEABLE and CLEAN, state OPEN. The code claims hold: the seven keys are read only by the terms parser (FacetParser.java base lines 552-577), and the range parser's common-params call reads only excludeTags and domain (base FacetParser.java lines 205 onward); the check order matches UNSUPPORTED_PARAMS and the test that names limit before sort; the test counts match the code (TestJsonRangeFacets has 10 test methods including the Distrib variants, TestJsonFacetErrors has 5); the legacy facet.range translation (LegacyFacet.addRangeFacet, tip lines 134-156) emits only field, start, end, gap, other, include, mincount and hardend, so it is not affected. The changelog type and author match the guide and the ICLA name. The live body has no internal process words (checked for gate, receipt, ledger, handoff, takeover, audit, JUnit, pre-fix, submission, premise). The PR names no Lucene behavior, so no Lucene check was needed. Verdict: consistency pass with drift. Branch, receipt and live PR agree on head, scope and counts. The description needs the five FIX edits above; the rest are owner calls.

## Owner decisions

1. Confirm from the 2026-10-04 job log which class fails on base, or drop the TestJsonRangeFacets base-code sentence (finding 2).
2. Whether to add the mincount sentence (finding 7).
3. Whether to bring the body up to the 2026-10-08 presentation rule and drop the repeated behavior-change statements, and who edits it, given the check-with-Nick rule (finding 8).
4. Whether the upgrade note also goes on a ref guide upgrade-notes page (finding 9).
5. Whether to add the ref guide sentence to the PR body (finding 10).

## Not checked

- The SOLR-18482 Jira text. Not available to this agent and no hydrated packet exists in the worktree, so "which is what the ticket proposes" in the PR is unverified.
- The job logs behind the 2026-10-04 and 2026-10-06 runs. The receipt does not say whether the 2026-10-06 confirmation ran on GitHub or locally, so the PR's "GitHub Actions" wording for that run cannot be checked.
- head_sha and conclusions for each check run (finding 11).
- PR comments and reviews (not fetched under this round's rules).
- Spotless and formatting at the tip. No record exists. New Java lines in FacetRangeParser.java and the new TestJsonFacetErrors block are within 100 columns. Five lines over 100 in TestJsonFacetErrors.java (176, 188, 619, 636, 648) sit outside the new block and predate the branch.
- The ascending bucket order was checked against code comments only, not by running anything.
- The "other facet types still ignore parameters" statement in Limits was not checked type by type.
- upstream/main has moved past the merge base 91cbb6a343e (local upstream/main is 8e62c2686882). The PR's CLEAN state was taken as given.
- No builds, tests, posts, or commits were made. The worktree was clean at e4a5dafa8f4 at the start.
