# Census follow-up, scheduled round 3 (six slices)

- Trigger: the two-hourly scheduled check. Recorded tip `3a6e8780413`; live tip at the check `db51d7d6bfc`. Claim: `claims/pool-census-followup-round-3-scheduled.md` (pushed at `ff86b414d88`).
- The new commits before the check were vm2's gate results: SOLR-12044 changelog type green at `de8461adfc`, SOLR-8554 changelog phrase green at `1e8a8a4b6b` (with a draft edit in the same commit), and SOLR-3865 connect timeout green at `1ca7bf9ad0`. No new assignment was posted.
- Worktree: `wt/pr-prepare-suggester`. Live fork tips checked by `git ls-remote origin` for every ticket. Each draft names the head its receipt names.
- Read only for branches: no build, test, Gradle or gate run; no commit or push on any `solr-*-submit` branch; no GitHub write; no receipt, gate file or claim edited by the review side.
- Written by the parent from the six subagent replies. The subagents could not write report `.md` files.

## Result

- 24 tickets. 17 SATISFIED, 6 HOLD with a gate job filed, 1 owner item.

## Slice S1: settlements

- SOLR-12044: SATISFIED. `pr-drafts/search-components/SOLR-12044.md` at gated head `de8461adfc` (live tip). S4 cleared: `changed` is listed in dev-docs, `optimized` is not. Proof 6 of 6 at `de8461adfc`, verified 2026-10-14; the premise run keeps its `c92bbf9349` date (2026-10-09).
- SOLR-8554: SATISFIED. `pr-drafts/core-admin/SOLR-8554.md` at gated head `1e8a8a4b6b` (live tip). The vm2 edit in `bd2387f8a05` is kept: the receipt supports the head, 3 of 3, dated 2026-10-14. The compile-level bullet now names base `cabedd1d968` and 2026-10-11. The Choice section is unchanged. The changelog phrase rests on the receipt; the object is not on this host.
- SOLR-3865: SATISFIED on the draft, gated head `1ca7bf9ad0` (live tip). `pr-drafts/solrcloud/SOLR-3865.md`: citations moved; Proof 2 of 2 at `1ca7bf9ad0`, verified 2026-10-14; base failures 1.585 s and 1.209 s. The module-check and tidy/parse/Error Prone sentences are removed from Proof. Owner decision 8 (commit-body cleanup) is still open, so the opening stays held. The test-class line anchor (L26 to L44) is computed from the one-line insert and is unverified, because `1ca7bf9ad0` is not on this host. The ticket link is kept with its disclosure (a recommendation, unruled).

## Slice S2: search components drafts owed

- SOLR-6759: SATISFIED. `pr-drafts/search-components/SOLR-6759.md`. Gate green per the receipt (2026-10-09). Named gap: the Proof checks `complete()` calls, not a lost group. The lost-group case is deferred as a Limit. Flag, not edited: the changelog's "block collapse" example applies only through `expand.fq`.
- SOLR-8003: SATISFIED. `pr-drafts/search-components/SOLR-8003.md`. Gate green (2026-10-09). The glob-versus-per-field choice stays as the Choice.
- SOLR-8051: SATISFIED. `pr-drafts/search-components/SOLR-8051.md`. Gate green (2026-10-09). No Choice section: the receipt records the skip as the ticket's intent. No assertion added; the skip is named as a Limit. Limits states the SOLR-15319 order in plain words.
- SOLR-8088: SATISFIED. `pr-drafts/search-components/SOLR-8088.md`. Gate green (2026-10-09). Named gap: the base error wording (`IllegalStateException` on docValues) comes from the branch comment and changelog, not from the receipt. The reject-versus-support choice is narrow scope, so Limits offers a follow-up.
- Visible lengths for the four: 2,066 to 2,829 characters.

## Slice S3: search components drafts owed

- SOLR-8767: SATISFIED. `pr-drafts/search-components/SOLR-8767.md`. Limits names that the receipt does not say which new test fails on base. Decision: the draft takes the implemented `/get` route, with the owner question in the Choice section. The subagent reports this overrides an earlier owner hold. **Owner to confirm**; Nick can redirect at opening.
- SOLR-9595: SATISFIED. `pr-drafts/search-components/SOLR-9595.md`. The gated line is undated; 2026-10-10 comes from the receipt's takeover-log line. The base failure has no per-test count, which Limits names. The doc-values scope goes to Limits with a follow-up offer. The eager live-docs construction is kept, and its cost is named.
- SOLR-8954: HOLD. The branch appears to break `FullSolrCloudDistribCmdsTest#testRTGCompositeRouterWithRouterField`. This was read from the code; the gate did not run it. The recorded recommendation (8009 carries the all-shards fix) is adopted, so no fix draft was written. Gate job `gates/SOLR-8954-fullsolrcloud-rtg.md` (OPEN).
- SOLR-9124: HOLD. The changelog title claims a grouped-versus-ungrouped equality that the test does not check, and it names LRU, which passes on base. Gate job `gates/SOLR-9124-changelog-title.md` (OPEN). The draft follows when the title lands.

## Slice S4: search components drafts owed

- SOLR-10305: HOLD. The branch changelog and the test javadoc still name the wrong NPE site. Gate job `gates/SOLR-10305-text.md` (OPEN). Owner decision 3 is settled at the third rung: the standalone 403 stays, named in Limits. The gated line is undated; 2026-10-09 comes from the history line.
- SOLR-16155: SATISFIED on the text. `pr-drafts/search-components/SOLR-16155.md`. The opening stays with Nick, because an existing PR covers the same change more widely. The count lines are undated; 2026-10-04 comes from the takeover log.
- SOLR-17055: HOLD. The branch has a defect: an explicit non-score sort can list more than topK documents while numFound is capped. Gate job `gates/SOLR-17055-sort-path.md` (OPEN; premise run first, then the fix, per the decision default). The gated line is undated; 2026-10-10 is the date used.
- SOLR-17841: SATISFIED on the text. `pr-drafts/search-components/SOLR-17841.md`. The opening is held by Nick's retire call. The receipt names no base SHA; the subagent read it as `14c7aac0d151`.
- Correction for the main side: `reports/search-components-4-s5.md` finding 8 is wrong. Git shows the change is already in base `14c7aac0d151`, so the SOLR-17841 base run is post-fix.

## Slice S5: core admin

- SOLR-12916: SATISFIED. `pr-drafts/core-admin/SOLR-12916.md` refreshed. CA-R1-34 has landed: the fragment title matches the replacement. Code citations move to `d62ddfc246`. Proof is split into base (2026-10-10 at `50b1f4bcab03`) and head (2026-10-13 at `d62ddfc246`, 2 of 2, 0 failures).
- SOLR-15003: HOLD, no draft. The receipt says "5 tests", but the class has 28 `@Test` methods, four of them new. The receipt names no tests and records fail-before only at `8f5b6f8360a`. Gate job `gates/SOLR-15003-proof-recount.md` (OPEN).
- SOLR-16887: HOLD, no draft. The fixes adopted in the round 2 record are not on the branch at `42675f65d6f`: `-XX:ErrorFile` was dropped from the scripts and the guide, and the BATS test name claims an OOM it never causes. Gate job `gates/SOLR-16887-errorfile-bats-name.md` (OPEN).
- SOLR-18278: OWNER ITEM, no draft. The recorded recommendation is to retire. The ticket is Resolved and the reviewer calls it invalid. The retire call is Nick's. If Nick keeps the branch, an honest draft can be written.

## Slice S6: SolrJ confirmations

- SOLR-4424: SATISFIED. `pr-drafts/solrj/SOLR-4424.md`, gated head `2e947b7f622c`. No run date in Proof: the receipt gives only a takeover-log date (draft-date-flags rule). Visible length about 4,116 characters.
- SOLR-5220: SATISFIED. `pr-drafts/solrj/SOLR-5220.md`, head `e415c409044c`. No Choice section.
- SOLR-7709: SATISFIED. `pr-drafts/solrj/SOLR-7709.md`, head `501078f22203`.
- SOLR-12094: SATISFIED. `pr-drafts/solrj/SOLR-12094.md`, head `8d957a73f4fb`. The error-versus-buffering point stays in Limits. No Choice section. Visible length about 4,390 characters.
- SOLR-14967: SATISFIED. `pr-drafts/solrj/SOLR-14967.md`, head `ee76643f5b3f`.
- Receipt wording errors, not edited: the SOLR-4424 receipt puts the ticket's check at XML response parsing, but the ticket text says solrconfig parsing, so the draft is right. The SOLR-14967 receipt says the retry sent the caller value "instead of the fresh one", but the test asserts no value on that retry, so the draft is right.
- Premise conflict for the main side: round 1 holds verdicts for all five (SOLR-4424 and 7709 converged with notes; 5220, 12094 and 14967 not converged, then fixed in round 1). The round 2 response has none. These drafts are judged against the receipts.

## Gate jobs filed this round (all OPEN, none run)

- `gates/SOLR-8954-fullsolrcloud-rtg.md`
- `gates/SOLR-9124-changelog-title.md`
- `gates/SOLR-10305-text.md`
- `gates/SOLR-17055-sort-path.md`
- `gates/SOLR-15003-proof-recount.md`
- `gates/SOLR-16887-errorfile-bats-name.md`

## Owner items raised by this round

1. SOLR-8767: confirm the `/get` route, which the subagent reports overrides an earlier owner hold.
2. SOLR-18278: retire or keep (Nick's call).
3. SOLR-3865: owner decision 8 (commit-body cleanup) before opening.
4. Length: SOLR-4424 (about 4,116) and SOLR-12094 (about 4,390) are over the 3,850 limit. You allowed one exception, so these two need a trim or a waiver.
5. SOLR-16155 and SOLR-17841: openings are Nick's (an existing PR covers 16155; 17841 is held by the retire call).
6. SolrJ premise conflict: the round 1 verdicts for SOLR-4424, 5220, 7709, 12094 and 14967 are not carried into round 2. Main side to reconcile.
7. Correction: `reports/search-components-4-s5.md` finding 8 (SOLR-17841 base) is wrong; see S4.

## Still open from earlier rounds

- SOLR-14231: vm2 to restate the counts and add the fail-before line.
- SOLR-4754: HOLD from round 1; gate `gates/SOLR-4754-text-fixes-and-premise.md` is OPEN.
- SOLR-8628 and SOLR-10234: opening held on gates `gates/SOLR-8628-packaging.md` (OPEN) and decision 8.
- SOLR-13838: a fail-before run at `4520c25abe2` and the ticket text are owed.
- SOLR-14098: the ledger date conflict is with the main side.

## Not started (remaining census follow-up)

- Query parsing: drafts owed SOLR-15906 and SOLR-16570.
- Core admin: refresh SOLR-8576.
- SolrCloud: SOLR-5813, 10641, 11288, 12991, 13186, 13369, 15674, 15863 and the SOLR-17292 receipt reconcile.
- Spellcheck, suggester, streaming-schema-configsets, build-docs-cli-metrics, and the edismax leftover: not started.
