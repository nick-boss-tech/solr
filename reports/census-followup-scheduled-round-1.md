# Census follow-up, scheduled round 1 (six slices, four tickets each)

- Trigger: the two-hourly scheduled check. Recorded tip `38ba628d1f7`; live tip at the check `029377d2361`. Claim: `claims/pool-census-followup-round-1-scheduled.md` (pushed at `240b882fe31`).
- Census: `reports/openings-census-round-1.md`. Follow-up assignments: `assignments/pool-census-followup-*-round-1.md`.
- Worktree: `wt/pr-prepare-suggester`. Live fork tips checked by `git ls-remote origin` for every ticket in this round. Each draft names the head its receipt names, and every named head matched its live tip, except where a line below says otherwise.
- Read only for branches: no build, test, Gradle or gate run; no commit or push on any `solr-*-submit` branch; no GitHub write; no receipt or claim edited by the review side.
- Written by the parent from the six subagent replies. The subagents could not write report `.md` files.

## Result

- 24 tickets. 15 READY TO OPEN on the draft and the gate record. 4 SATISFIED on the draft, with opening held on a named gate job. 5 HOLD, each with a named step.

## Slice A: search components wording (`pool-census-followup-search-components-wording-round-1`)

- SOLR-15319: SATISFIED. `pr-drafts/search-components/SOLR-15319.md`. Bold summary on Limits; the SOLR-8051 dependency now stated in plain words; landing vocabulary removed. Length left as is (advisory).
- SOLR-14381: HOLD on `gates/SOLR-14381-title-fix.md`. Step S4: the changelog title is cut off mid-sentence. Bold summaries added to A choice, Proof and Limits. A complete replacement title (321 characters) is in the gate job and is not applied.
- SOLR-5394: SATISFIED. `pr-drafts/search-components/SOLR-5394.md`. Bold summaries on Proof, A choice and Limits. Advisory, unchanged: dev-docs defines "changed" as modifying existing behavior, so the fragment type "fixed" may be wrong. Owner to confirm.
- SOLR-7390: SATISFIED. `pr-drafts/search-components/SOLR-7390.md`. Bold summary on Limits. The "upstream main" link pointed at `8e62c2686882`, a later commit; it now points at the merge-base `0cc328310f8f` and is relabeled. Cited lines 232 to 239 match at both.

## Slice B: search components wording

- SOLR-7520: SATISFIED. Bold summaries on Proof and Limits. Tip `10b6931e1c05`.
- SOLR-8009: SATISFIED. Bold summary on Limits. Tip `8795661ddc98`. Correction to the round 1 ready list: the receipt's gated line does carry `2026-10-05`, so the date was not missing. Proof reads "Verified 2026-10-05". Owner to confirm.
- SOLR-8240: SATISFIED. Bold summary on Limits. Tip `fad7a1dd8e23`.
- SOLR-8939: SATISFIED. Bold summary on Limits. Tip `a855a2d8965c`.

## Slice C: query parsing (`pool-census-followup-query-parsing-round-1`)

- SOLR-874: HOLD on `gates/SOLR-874-title-fix.md`. Step S4: the live title says a leading NOT is dropped, but the code drops only leading AND, OR, && and ||. The replacement title is in the gate job for head `ac9ab3375375`, not applied. Also corrected: one choice sentence said nothing was left after cleanup, but a lone operator stays in place. Limits bold summary added; the claim now cites `DisMaxQParser` line 195.
- SOLR-12212: SATISFIED. `pr-drafts/query-parsing/SOLR-12212.md`. Bold summary on Limits.
- SOLR-15615: SATISFIED. `pr-drafts/query-parsing/SOLR-15615.md`. The bold summary sits on its own line under A choice to check. The Proof date is advisory and was not applied.
- SOLR-13838: HOLD, no draft. The receipt says RECONCILED GREEN, but no gate log, date or ledger row is on record, and the pre-fix outcome is NaN with no log. Owed: a fail-before run at `4520c25abe2` and the ticket text. No gate job was filed for this one; the parent will file it if the owner wants it run.

## Slice D: core admin drafts owed (`pool-census-followup-core-admin-round-1`)

- SOLR-5262: SATISFIED. `pr-drafts/core-admin/SOLR-5262.md`. Gate GREEN at live tip `616efd008b05` (2026-10-11). No Choice section (the dataDir default was already adopted in the answers).
- SOLR-8554: SATISFIED on the draft. `pr-drafts/core-admin/SOLR-8554.md`. Gate GREEN at `654c1a7c3570`. Opening held on the changelog phrase fix, filed as `gates/SOLR-8554-changelog-phrase.md` (OPEN). The fix moves the head.
- SOLR-8628: SATISFIED on the draft. `pr-drafts/core-admin/SOLR-8628.md`. Gate GREEN at `5b606dc779d0`. Opening held on the root TESTING file removal, filed as `gates/SOLR-8628-packaging.md` (OPEN). The fix moves the head.
- SOLR-14098: HOLD, no draft. The receipt is RECONCILED GREEN from a ledger row, not a gate run, and no base run is recorded. The ledger date `2026-10-02` precedes the head's committer date `2026-10-03`. Escalated to the main side. The publish-ACTIVE Choice stays open under the recorded hold.

## Slice E: SolrJ (`pool-census-followup-solrj-round-1`)

- SOLR-3498: SATISFIED. New draft `pr-drafts/solrj/SOLR-3498.md`. Head `2ca0b3743427` is the live tip. Base is `cabedd1d968`, the upstream merge-base; the receipt's base `e044bf20` has the identical file. The receipt's scope note and its QueryResponseTest and SimpleOrderedMapTest counts come from the stale base, so they are left out of Proof. The setParams-after-setCommitWithin gap is named in Limits with an offer.
- SOLR-2018: SATISFIED, no edit. `pr-drafts/solrj/SOLR-2018.md`. Head `213457aa91fd` matches.
- SOLR-3722: SATISFIED after fixes. `pr-drafts/solrj/SOLR-3722.md`. Head `f9d3dda1d3de` matches. The SimpleOrderedMap claim is re-cited (the javadoc cited is NamedList's). Proof date `2026-10-06` and base count 1 of 6 added from the receipt.
- SOLR-4422: SATISFIED after fixes. `pr-drafts/solrj/SOLR-4422.md`. Head `3e5b7afecddb` matches. Proof date `2026-10-06` and base count 1 of 6 added from the receipt.
- Owed to vm2, not edited: the receipt's scope note and focused list for SOLR-3498; the premise head SHA is not named; the gate file's push time (`2026-10-14 03:07Z`) differs from the census (2026-10-13).

## Slice F: SolrCloud (`pool-census-followup-solrcloud-round-1`)

- SOLR-16013: SATISFIED. `pr-drafts/solrcloud/SOLR-16013.md` refreshed to `37b01337eb`. Proof is 3 of 3 at both recorded seeds (2026-10-13); the pre-fix run shows the 2 premise failures, with the third passing on base; the test restart is carried. The fixture patch is not reapplied (superseded). Conflict for the main side: `reports/owner-rulings-recheck-round-1.md` item 1 (`4ea40aebba5`, 00:12Z) predates the owner reversal in `receipts/SOLR-16013.md` (`ae5e520ba53`, 00:48Z). The draft follows the reversal; the ruling file needs correcting.
- SOLR-3865: SATISFIED on the draft. New `pr-drafts/solrcloud/SOLR-3865.md`. Opening held on `gates/SOLR-3865-connect-timeout.md` (OPEN). Recorded answer adopted: the link is kept with disclosure.
- SOLR-4754: HOLD. New `pr-drafts/solrcloud/SOLR-4754.md`, with a HOLD comment at the top. Gap: the error text names `-Dhost`; the changelog title overstates; there is no merge-base run of an `http://` node. Gate job `gates/SOLR-4754-text-fixes-and-premise.md` (OPEN).
- SOLR-10234: SATISFIED on the draft. New `pr-drafts/solrcloud/SOLR-10234.md`. Opening held on `gates/SOLR-10234-drop-changelog.md` (OPEN). Recorded answer adopted: the suppression stays and the changelog fragment is dropped.
- Owner decision 8 (commit-body cleanup) is still open for SOLR-3865, SOLR-4754 and SOLR-10234.

## Gate jobs filed this round (all OPEN, none run)

- `gates/SOLR-14381-title-fix.md`, `gates/SOLR-874-title-fix.md`
- `gates/SOLR-8554-changelog-phrase.md`, `gates/SOLR-8628-packaging.md`
- `gates/SOLR-3865-connect-timeout.md`, `gates/SOLR-4754-text-fixes-and-premise.md`, `gates/SOLR-10234-drop-changelog.md`

## Owner items raised by this round

1. SOLR-8009 date: confirm `2026-10-05` (the round 1 ready list was wrong).
2. SOLR-5394 fragment type: `fixed` or `changed`.
3. SOLR-14098 date mismatch (ledger `2026-10-02` against committer `2026-10-03`), for the main side.
4. The owner ruling file for SOLR-16013 needs correcting to the reversal.
5. Decision 8 (commit-body cleanup) for SOLR-3865, SOLR-4754 and SOLR-10234.
6. SOLR-13838: run a fail-before at `4520c25abe2`, and supply the ticket text.

## Not started (remaining census follow-up, for later rounds)

- Search components wording: 20 of the 28 HOLD drafts not in this round.
- Search components drafts owed: all 12 (SOLR-6759, 8003, 8051, 8088, 8767, 8954, 9124, 9595, 10305, 16155, 17055, 17841).
- Query parsing: drafts owed SOLR-15906 and SOLR-16570 (SOLR-13838 held as above).
- Core admin: drafts owed SOLR-15003, 16887 and 18278; refreshes for SOLR-12916 and SOLR-8576.
- SolrJ: confirmations for SOLR-4424, 5220, 7709, 12094 and 14967.
- SolrCloud: SOLR-5813, 10641, 11288, 12991, 13186, 13369, 15674, 15863 and the SOLR-17292 receipt reconcile.
- Spellcheck, suggester, streaming-schema-configsets, build-docs-cli-metrics, and the edismax leftover: not started.
