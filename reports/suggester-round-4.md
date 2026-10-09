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

## Draft round (2026-10-09)

Claim: `claims/suggester-round-4-drafts.md` (commit `95b62c33450`), made before any draft. Source: `material/suggester-round-4-decisions.md` and `material/suggester-round-4-answers.md` at `81339a26ef6`. Live tips checked with `git ls-remote` on 2026-10-09. Two subagents drafted and checked; the lead checked their output and made the commits.

Drafted (1), `pr-drafts/suggester/SOLR-9227.md`. The head is `3c23fc5cfa6`, the gate receipt's head and the live tip. The changelog path and the test name exist at that head. The draft has one bracketed HOLD line in Limits, for owner decision A.

Held (7), no draft:

| Ticket | Live tip | Audit pin | Hold reason |
|---|---|---|---|
| SOLR-9968 | `c31d2ff1a3f` | `d688e1efdf2` | Wording narrowing sits on the gated tip; its top-up gate is pending. |
| SOLR-14171 | `faa262eedc5` | `942acabd26ef` | Packaged; first full gate is running. |
| SOLR-17393 | `626241e647e` | `dd6c82924fff` | Packaged; baseline gate is running. |
| SOLR-17215 | `66be01719765` | same | Catch narrowing and ref-guide note have not landed. |
| SOLR-10937 | `7d0cd11bafd` | `9d8151c43a81` | Docs lane. New commits `363dd367b6e` and `7d0cd11bafd`. No lane report in the material. |
| SOLR-11844 | `cbd08c20e9a` | `4e226462f3b0` | Docs lane, conditional on the Lucene check. New commits `97a97efc995` and `cbd08c20e9a`. |
| SOLR-9637 | `c50fa4ffd93` | none | Its own PR after SOLR-17393 lands, rebased onto that tip. |

Inputs recorded for the later drafts:

- SOLR-9968: the setting controls how context values are tokenized. The keyword tokenizer keeps characters such as `#` inside one term. A value is still parsed as a query, so whitespace separates terms. The round 27 Choice is the configurable tokenizer as implemented, against the context field's schema analyzer.
- SOLR-14171: the `Boolean.getBoolean` split is named in Limits, not fixed. The literal `true` stays in `SolrSuggester`, and the Choice is the literal against the lookup's own default. The unconfigured default stays `true`.
- SOLR-17393: ship the merge-only fix. Limits say the merge orders what the shards return, a shard's own top-count cut happens first, so at count 1 with equal weights the symptom can persist, and the per-shard fix is a planned follow-up. Payload scope is the stable order over distinct suggestion text. Memory bound is count times shard count.
- SOLR-17215: keep the clearer 503 message. Narrow the catch so `AlreadyClosedException` is not reported as "not built". The 500 to 503 change is the Choice. Add the ref-guide note that dictionaries are not replicated.
- SOLR-10937: docs only. Keep the five-lookup list only if the Lucene check confirms WFST and TST sort in `java.io.tmpdir`; otherwise drop it.
- SOLR-11844: docs only, conditional on the Lucene check of the weight formula and the weight-0 premise. The new tip text also asserts a scaling rule (weight 0 counts as 1; weights below 10 scaled by 10). The decisions file does not name that rule, so check it too.

Owner decisions needed:

- **A. SOLR-9227 store path (blocks the draft).** The material says the absolute store path appears only in the IOException rethrown from `build`, and does not reach request callers. At `3c23fc5cfa6` the WARN in `SolrSuggester.java` (L197) also logs the absolute path. `SuggestComponent.java` (L196-L199) calls `build` on `suggest.build=true` with no catch, so the IOException reaches that request. Not checked: whether the message reaches the client. Decide whether the Limits line keeps the path claim with corrected facts, or the message changes.
- **B. Gate records disagree.** The SOLR-9968 audit pins `d688e1efdf2` and calls its gate owed. The material says that commit was gated green in the Pairs round (`g9968r1-gate.log`). The SOLR-14171 and SOLR-17393 audit headers say "gated". The material says neither was ever gated on the main side. The material's receipt list names only 9227 and 9968, so SOLR-9637's green gate at `c50fa4ffd93` has no receipt on record. Decide which record the drafts cite.
- **C. SOLR-17393 HIGH finding.** The merge-only scope with its Limits line answers the finding only under the audit's second re-verdict option. No per-shard regression was added. Confirm that this is the intended answer.
- **D. Audit line numbers.** The SOLR-9227 audit gives `lookup.build` at lines 172-181. At `3c23fc5cfa6` it is lines 174-182. Correct the audit, or note it.

Not done: no gates, tests, builds, Gradle, PRs, comments, or submit-branch edits. Nothing was posted.
