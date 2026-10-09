# Suggester round 4 report: audits

Round: `assignments/suggester-round-4.md` (commit `49bc048b3e9`). Claim: `claims/suggester-round-4.md` (commit `1426f97ed6f`). Audits: `audits/suggester/SOLR-<ticket>.md`, one per in-scope ticket.

Done 2026-10-09 by Claude Code (AI agent), working for Nick Shanin. One subagent wrote the seven audits. The lead agent checked the live tips, the merge conflict, the scope claims, and the file hashes, and wrote this report. Audits only: no gates, tests, builds, drafts, PRs, comments, or submit-branch edits.

## Summary

- Seven audits, none ready to draft. Four are held: SOLR-10937, SOLR-11844, SOLR-17215, and SOLR-17393. Three are "draft with named Limits or Choices": SOLR-9227, SOLR-9968, and SOLR-14171. Those three depend on a gate that has not run.
- No gate receipt exists for any of the seven at its live tip. The test-queue results and `queue.json` hold no suggester jobs. The receipts ledger (`test-receipts-63-branches.md`) is not in the workspace.
- Live fork tips matched the inventory heads and the claim for all seven. None differed, so each audit covers the tip the claim recorded.
- SOLR-9637 is out of scope and was not audited. The SOLR-17393 audit records the stack dependency only.

## Per-branch table

Verdicts are recommendations for the owner. Line references are to the live tips.

| Ticket | Live tip | Gate | Last review | Verdict | Blocker that matters most |
|---|---|---|---|---|---|
| SOLR-9227 | `3c23fc5cfa6cba5d6f932cae1806c4f44fbacba4` | None on record | Close (round 28, at tip) | Draft with named Limits or Choices | No focused proof. The ERROR-to-WARN choice for non-storing lookups. |
| SOLR-9968 | `d688e1efdf2e70493149a1c670b6ccd3aef5f229` | None on record | Close (round 28, at tip) | Draft with named Limits or Choices | The keyword claim is overstated: whitespace and query syntax still split it. |
| SOLR-10937 | `9d8151c43a815352edb5e160b58b4a762eb5facc` | None on record | None on record | Held | The NOTE lists three lookups, but Solr passes the tmpdir to five. Owner call: docs-only or code. |
| SOLR-11844 | `4e226462f3b070e2fac3e140c3ceafaad0a42e09` | None on record | None on record | Held | A public sentence contradicts the formula beside it (`suggester.adoc` line 402). The premise is unverified. |
| SOLR-14171 | `942acabd26ef224d4cb4636c6cf08c5aa32af247` | None on record | Close (round 28, at tip) | Draft with named Limits or Choices | A configured `true` splits the two paths. Pre-existing, not fixed here. |
| SOLR-17215 | `66be01719765fdc91d509aec698773cfb0995e87` | None on record | None on record | Held | Owner call: is a 503 the fix for a "Replication doesn't work" ticket. The ISE catch is too broad. |
| SOLR-17393 | `dd6c82924fff6b8f7cedaf020494c2f06ab502a2` | None on record | Not ready (round 28, at tip) | Held | HIGH: per-shard top-count truncation is still open. The reporter's query uses count = 1. |

## SOLR-9227 and SOLR-9968 interaction

Both edit `SuggestComponentContextFilterQueryTest.java`. A trial merge (`git merge-tree --write-tree`, read-only) conflicts there, in two hunks:
- the import block, where both add a `SolrSuggester` import that must be kept once;
- the insertion point, where both add a method before `testContextFilterParamIsIgnoredWhenContextIsNotImplemented`.

`SolrSuggester.java` auto-merges. No logic overlap was found by reading. The order is 9227 first, as the assignment asks. SOLR-9968 also conflicts with SOLR-14171 in the `SolrSuggester.java` constants, so 14171 has a third ordering question.

## SOLR-17393 stack note for SOLR-9637 (out of scope; metadata only)

SOLR-9637's history contains all three SOLR-17393 commits plus two of its own. Both share base `c3cdf7b46e8`. Landing 17393 would move 9637's diff basis to the landed 17393 commit and shrink its diff to its two commits. A squash or rebase of 17393 would need a rebase step on 9637, not a fast-forward. The diff of 9637 was not read. The stacking call is not decided here.

## Owner decisions

1. **SOLR-9227.** WARN or ERROR for non-storing lookups. May the absolute store path reach request callers?
2. **SOLR-9968.** The public option name. Fix the keyword wording, or narrow the feature.
3. **SOLR-10937.** Docs-only, or a build-time path wrapper, for a Major "No space left" ticket.
4. **SOLR-11844.** Is a ref-guide note an acceptable resolution? Line 402 needs a fix first, after a check against Lucene's `BlendedInfixSuggester` formula, which is not in this checkout.
5. **SOLR-14171.** Fix the `Boolean.getBoolean` factory split here or separately. Keep the literal `true` fallback at `SolrSuggester.java:93`, or defer to Lucene's default. Confirm the newer default.
6. **SOLR-17215.** Is a clearer 503 the resolution? Narrow the catch, and accept 500 to 503.
7. **SOLR-17393.** The tie policy (text order as "closest"), the payload scope, the memory bound, and the per-shard fix. The HIGH finding is open until one of these is decided.
8. **SOLR-9637.** Stacking: wait, rebase after a SOLR-17393 landing, or rework.
9. **Gate order.** None of the seven has a gate receipt. The three "draft with named Limits or Choices" verdicts rest on drafts that would have no Proof. Decide whether to gate these seven before the draft round, and in what order.

## Gaps and discrepancies

- **Gates.** No receipt for any of the seven at its live tip. No suggester jobs in `research/test-queue/results` or `queue.json`. The receipts ledger is not in the workspace.
- **Reviews.** No review on record for SOLR-10937, SOLR-11844, or SOLR-17215. Only `research/branch-reviews/round-28/PARKED.md` mentions them.
- **Review base.** The round 28 reviews name `cabedd1d968` as the comparison base. The actual merge bases are `97d973` for 9227 and 9968, `9b3a84b` for 14171, and `c3cdf7b` for 17393.
- **SOLR-14171 review.** It says the fallback is Lucene's default. `SolrSuggester.java:93` hard-codes `true`. The audit records this.
- **SOLR-17393 review.** It says the regression is distributed, citing `DistributedSuggestComponentTest.java:65`. The branch changes no distributed test. The regression is `SuggestComponentMergeTest`, a unit test of `merge`. The audit records this, and the lead confirmed the file list at the tip (`SuggestComponent.java` 29 lines changed, plus the new unit test, changelog, and TESTING note).
- **SOLR-9968 inventory row.** It says 4 files. The tip has 5.
- **SOLR-10937 handoff.** It says WFST and TST do not use the tmpdir, but the call sites pass it. It also says the path wrapper belongs with 9227, but 9227 wraps only the store call.
- **SOLR-11844.** Line 402 contradicts the formula in the same paragraph. The Lucene check is still needed.

## Checks (lead)

- Claim `1426f97ed6f` pushed before the audits. Live tips matched for all seven.
- Audit files: seven present, uncommitted. Hashes match the subagent's report. Em dash count 0 in each (`LC_ALL=C grep`).
- Merge conflict in `SuggestComponentContextFilterQueryTest.java` confirmed with `git merge-tree --write-tree`, and `SolrSuggester.java` auto-merges.
- `SolrSuggester.java:93` at `942acabd26ef` hard-codes `defaultAllTermsRequired = true`.
- SOLR-17393 diff (`c3cdf7b46e8` to `dd6c82924fff`): 4 files, +135/-12, `SuggestComponent.java` among them.
- Not done: no gates, tests, builds, Gradle, PRs, comments, or submit-branch edits.
