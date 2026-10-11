# Review confidence round 4, part B (slice RC4B)

- Reviewer: windows review agent (read-only subagent), 2026-10-11.
- Assignment: `assignments/pool-review-confidence-round-4.md`. Claim: `claims/pool-review-confidence-round-4.md` (not edited in this part).
- Worktree: `wt/pr-prepare-suggester` at `pr-prepare` 6a756b4e958.
- Scope: body fidelity for SOLR-12849 (PR 5011), SOLR-13568 (PR 5014), SOLR-13706 (PR 5015), SOLR-13097 (PR 5016), SOLR-18505 (PR 5028). No live body edit, comment, review, branch edit, draft edit, build, test or gate.

## Verdict counts

- CONSISTENT: 0 of 5.
- DRIFT: 5 of 5, 13 items in total.

| Ticket | PR | Verdict |
|---|---|---|
| SOLR-12849 | 5011 | DRIFT (3 items) |
| SOLR-13568 | 5014 | DRIFT (1 item) |
| SOLR-13706 | 5015 | DRIFT (2 items) |
| SOLR-13097 | 5016 | DRIFT (4 items) |
| SOLR-18505 | 5028 | DRIFT (3 items) |

SOLR-13097 (PR 5016) is judged on body fidelity only. Its CONFLICTING state and changes-requested review were not assessed, and this verdict does not mean the PR is ready. The API returned `mergeable` and `mergeStateStatus` as UNKNOWN for all five PRs at read time, so mergeability was not judged for any of them.

## Live state and head check (Method steps 1 and 2)

All five live bodies were read with `research/gh.ps1 pr view ... --json` (read only). All five fork tips were resolved with `git ls-remote origin`.

| Ticket | PR | State | Head ref | API head SHA | ls-remote | Receipt head | Lines up |
|---|---|---|---|---|---|---|---|
| SOLR-12849 | 5011 | OPEN, not draft | solr-12849-submit | 6b92223bc24f4ef5a6726ed986a240c6eacedf73 | same | 6b92223bc24f (gated) | yes |
| SOLR-13568 | 5014 | OPEN, not draft | solr-13568-submit | ac5d60c214cf8a1c6a24c79d89d9d22f44e9d721 | same | ac5d60c214cf (gated) | yes |
| SOLR-13706 | 5015 | OPEN, not draft | solr-13706-submit | 590dd5c24d97c5c1b8f2ed262037c0a9c4ba6f77 | same | 590dd5c24d97 (gated) | yes |
| SOLR-13097 | 5016 | OPEN, not draft | solr-13097-submit | f0e7395f58f3c8d8c9066cd88baa6931d8b6f425 | same | f0e7395f58f3 (gated) | yes |
| SOLR-18505 | 5028 | OPEN, not draft | solr-18505-submit | e28739b4069d0399c7e2aa11b7f515758ec5928a | same | e28739b4069d (gated) | yes |

The head SHAs in the bodies are prefixes of the API head SHAs. The only head-related mismatch is a date conflict, recorded under RC-1 below. Every commit the bodies cite as a verified head exists in the shared repository.

## SOLR-12849 (PR 5011): DRIFT (3 items)

Checked and consistent: test counts (AliasPostBodyTest 4 at head, HttpSolrCallCollectionParamTest 2 at head, matching the receipt); the base-failure text (`expected:<[bodycoll]> but was:<[emptycollection,doccollection]>` in the receipt, matching the test's intent); the v2 rule (V2HttpCall rejects a multi-collection request with 400 before the method runs); `COLLECTION` is in SolrJ's `DEFAULT_URL_PARAM_NAMES`; the AliasPostBodyTest class javadoc labels the cases as end-behavior pins.

1. Presentation rule. Live: none of the four section openings (What happens today, What this change does, Proof, Limits) has a bold one-line summary. Replacement: add one bold line directly under each heading:
   - What happens today: `**A collection parameter sent in a POST form body is overwritten when the URL path names two or more collections.**`
   - What this change does: `**A collection parameter that arrives only in the POST body keeps its value, with its aliases resolved; URL parameters behave as before.**`
   - Proof: `**The method-level test class fails on base code and passes with this change; the end-to-end tests also pass on base code.**`
   - Limits: `**No end-to-end test covers a body-only collection parameter, and the v2 path cannot reach the multi-collection overwrite.**`
2. Changelog line not linked. Live: ``Changelog: `changelog/unreleased/SOLR-12849.yml` ``. Evidence: the file exists at head (type fixed); pr-formula requires the changelog line to link the head SHA. Replacement: `Changelog: [changelog/unreleased/SOLR-12849.yml](https://github.com/nick-boss-tech/solr/blob/6b92223bc24f4ef5a6726ed986a240c6eacedf73/changelog/unreleased/SOLR-12849.yml)`
3. Plain language (pr-formula simple-language rule, "discriminating" and "pin" are compressed jargon). Three replacements:
   - Live (Proof): "This is the discriminating test:" Replacement: "This is the test that shows the defect:"
   - Live (Proof, AliasPostBodyTest bullet): "The end-to-end cases pin the end behavior but do not discriminate; the method-level test above is the one that fails on base." Replacement: "The end-to-end cases check the end behavior but do not show the defect; the method-level test above is the one that fails on base."
   - Live (Limits): "The discriminating evidence is a method-level test; the end-to-end cases pass on base code as well, for the reason given under Proof." Replacement: "The test that shows the defect is a method-level test. The end-to-end cases also pass on base code, for the reason given under Proof."

Unconfirmed by the receipt (no change proposed; main side to confirm from the premise logs or remove):
- "That base comparison ran on the fork's GitHub Actions test runner". The receipt names the premise logs and no venue.
- "local runs on 2026-10-04 and again on 2026-10-06" for AliasPostBodyTest on base. The receipt gives no run dates.
- "Verified at head cd7bd799c91 on 2026-10-04". The receipt does not list that head. Branch commit 6b3a2829fe5 (2026-10-04) mentions "verified base behavior", so the claim has some support in history.

## SOLR-13568 (PR 5014): DRIFT (1 item)

Checked and consistent: TestExpandComponent has 9 test methods at head (receipt 9 of 9); the premise text `expected:<3> but was:<5>` and "9 tests, one failure" match the receipt; the ExpandComponent diff matches the body (WrappedQuery with setCache(false) on the per-page group query only); the Limits (uncached recompute, not benchmarked) match the receipt's round 29 note; every section has a bold summary.

1. Changelog line not linked. Live: ``Changelog: `changelog/unreleased/SOLR-13568.yml` (type fixed)`` Replacement: `Changelog: [changelog/unreleased/SOLR-13568.yml](https://github.com/nick-boss-tech/solr/blob/ac5d60c214cf8a1c6a24c79d89d9d22f44e9d721/changelog/unreleased/SOLR-13568.yml) (type fixed)`

Unconfirmed: none beyond RC-1.

## SOLR-13706 (PR 5015): DRIFT (2 items)

Checked and consistent: PluginInfoTest 11 test methods, TestConfigHighlightOutput 1, TestSolrConfigHandler 8 (not touched by the branch), all matching the receipt; the SolrConfig TODO workaround is removed at head; the changelog type is `changed` as the Limits say; the Solr 11 upgrade note exists at head; the `containsKey("formatter")` assertion matches the test source.

1. Summary contradicts the Proof section. Live summary: "**The affected suites pass at this head, and the new cases fail on base code with the same NullPointerException.**" The Proof says only testUnnamedChildIsWrittenUnderItsType and TestConfigHighlightOutput fail with the NullPointerException, and testChildrenGroupedByTypeInSerializedOutput fails on its key assertion. Replacement: `**The affected suites pass at this head. On base code the new tests fail: two with the NullPointerException from the null key, and one on a key-name assertion.**`
2. Changelog line not linked. Live: ``Changelog: `changelog/unreleased/SOLR-13706.yml` `` Replacement: `Changelog: [changelog/unreleased/SOLR-13706.yml](https://github.com/nick-boss-tech/solr/blob/590dd5c24d97c5c1b8f2ed262037c0a9c4ba6f77/changelog/unreleased/SOLR-13706.yml)`

Unconfirmed by the receipt (no change proposed): the base counts "8 failing test executions under randomization" (PluginInfoTest) and "4 failing test executions" (TestConfigHighlightOutput). The receipt records only that the pre-fix proof step passed. The Limits claim that no code reads serialized children by name was not re-searched in full in this pass.

## SOLR-13097 (PR 5016): DRIFT (4 items), body fidelity only

Checked and consistent: CoreScopedAuthStandaloneTest 5 test methods at head (receipt 5 of 5, verified 2026-10-07, matching the commit date of f0e7395f58f); the HttpSolrCall change matches the body (standalone context is the serving core's name, cloud unchanged); the rule-based guide and Solr 10.2 upgrade-note text match the body; the probe head 4c08aa5751e is an ancestor of the live head, and no production file changed after it (only the test and one guide line), so the probe result applies to the live code; the 4 failing executions for testScopedRuleDeniesOtherRolesOnTheCore match the receipt.

1. Changelog line not linked. Live: ``Changelog: `changelog/unreleased/SOLR-13097.yml` `` Replacement: `Changelog: [changelog/unreleased/SOLR-13097.yml](https://github.com/nick-boss-tech/solr/blob/f0e7395f58f3c8d8c9066cd88baa6931d8b6f425/changelog/unreleased/SOLR-13097.yml)`
2. File citation not linked. Live: "The change is noted in the Solr 10.2 upgrade notes (`major-changes-in-solr-10.adoc`)," Replacement: "The change is noted in the Solr 10.2 upgrade notes ([major-changes-in-solr-10.adoc](https://github.com/nick-boss-tech/solr/blob/f0e7395f58f3c8d8c9066cd88baa6931d8b6f425/solr/solr-ref-guide/modules/upgrade-notes/pages/major-changes-in-solr-10.adoc)),"
3. Failing-case count does not reconcile with the named cases. Live: "Against base production code the v2 and wildcard cases fail for the reason this PR fixes (3 of 5 methods fail on base under the extended configuration)." The receipt records three failures on base: the v2 case, the wildcard case, and the allow case under the extended configuration, each for the ticket's reason. Replacement: "Against base production code, three of the five methods fail under the extended configuration: the v2 case, the wildcard case, and the allow case. Each fails for the reason this PR fixes."
4. Plain language. Live: "pinning that a scoped rule governs its core on its own and the wildcard decides only for cores no scoped rule governs." Replacement: "checking that a scoped rule governs its core on its own and that the wildcard decides only for cores no scoped rule governs."

Unconfirmed by the receipt (no change proposed): the Limits statements that under JWT the caller's token is forwarded and that BasicAuth sub-requests carry no credentials. The receipt records three blockUnknown configurations and does not name the authentication scheme. The statement that requests with no serving core are unchanged was not re-run.

## SOLR-18505 (PR 5028): DRIFT (3 items)

Checked and consistent: the diff touches only DirectUpdateHandlerTest.java, so "test-only, no changelog" is right; the head adds one commit over 8ca33200e37 and the diff is comment-only, as the body says; DirectUpdateHandlerTest has 7 test methods at head (receipt 7/7); the FilterMergePolicy wrapper, the restore in finally, the try-with-resources samples and the setMergePolicy call before the first add all match the code; the round 21 and battery figures (6 of 6 failing re-executions, 5 forced plus 2 random-seed runs at 7/7, neighbors 3/3 and 1/1) match the receipt. The base-line citation is correct: line 494 of 22a8cfebbbdb is the first-sample assertion.

1. Internal term. Live: "This failure fired in a Crave run on an unrelated PR and is what prompted the investigation on SOLR-18505." Replacement: "This failure also showed up in a test run on an unrelated PR, which prompted the investigation on SOLR-18505." The receipt does not record that run; the main side should confirm it before keeping the sentence.
2. Internal term. Live: "At 1c5a948d3e4 the same battery passed" Replacement: "At 1c5a948d3e4 the same set of repeated runs passed"
3. Citation to the pre-change symptom not linked and not identified as the base commit. Live: "five times at the line 494 assertion" Replacement: "five times at the [line 494](https://github.com/nick-boss-tech/solr/blob/22a8cfebbbdb026437d10b0aa47bb66ffdb23a9a/solr/core/src/test/org/apache/solr/update/DirectUpdateHandlerTest.java#L494) assertion of the base commit (22a8cfebbbdb)". pr-formula requires a pre-change citation to link the merge-base commit and say so in the text.

Unconfirmed by the receipt (no change proposed): tidy and Error Prone passing at 8ca33200e37 and at e28739b4069 (the receipt records focused tests and the module check only at the head); the split "five times at line 494 and once at the second sample" (the receipt gives 6 of 6 only); "the merge lost the race" later the same day (narrative, not in the receipt); "merge policy behavior has its own coverage elsewhere" (not checked); the Lucene setter range of 20 to 50 (external to this tree, not checked).

## Record conflicts flagged (no replacement chosen)

RC-1. Head commit dates versus recorded dates. Four head commits carry author and committer timestamps on 2026-10-07 UTC (+0000), but the receipt and the body record gate or verification at those heads on 2026-10-06:
- 6b92223bc24 (SOLR-12849): 2026-10-07 04:20:44. Receipt: round 29 gate finished 2026-10-06. Body: "re-verified at head 6b92223bc24 on 2026-10-06", and the second test case "added on 2026-10-06" (that case is in this commit).
- ac5d60c214c (SOLR-13568): 2026-10-07 03:54:32. Receipt and body: 2026-10-06.
- 590dd5c24d9 (SOLR-13706): 2026-10-07 03:25:43. Receipt and body: 2026-10-06.
- e28739b4069 (SOLR-18505): 2026-10-07 04:38:52. Receipt: "Date: 2026-10-06". Body: "Update 2026-10-06".

SOLR-13097 (f0e7395f58f, 2026-10-07 07:21:31) agrees with its receipt and body. A timezone convention could explain the gap, but the tree does not settle it. The bodies are left as written; the main side should confirm the date convention for these gates.

## Checks performed and limits

- GitHub: read only, `pr view` JSON for PRs 5011, 5014, 5015, 5016, 5028. No writes. No token was printed.
- Git: `ls-remote` for the five submit branches; `diff --stat` against `upstream/main...<head>`; `show` and `grep` at each head; commit ancestry and timestamps; base-commit line check. No fetch, checkout or branch change.
- Not done: no build, test or gate; no Lucene source check; no full Admin UI search for by-name readers; no mergeability judgement.
- Files written: this report only. The claim file was not edited. The main side marks the RC4B slice DONE when this report lands.
