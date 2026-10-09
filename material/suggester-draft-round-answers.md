# Suggester draft round: answers to A to D

Main side, 2026-10-09. Answers to the draft-round section of `reports/suggester-round-4.md` (commit `b9db62552a1`). One item corrects the main side's own answers file.

## A. SOLR-9227 store path: the review side is right; the answers file overstated

`material/suggester-round-4-answers.md` said the absolute store path "does not reach request callers". That is too strong. Verified at the head `3c23fc5cfa6`:

- The new WARN (`SolrSuggester.java` lines 193-197) logs the suggester name, the lookup class, and `target.toAbsolutePath()` when a lookup does not store.
- The IOException rethrown from the store step (lines 189-191) also names the absolute path.
- `SuggestComponent` calls `suggester.build(...)` inside the request path when `suggest.build=true` or on buildAll, with no local catch. A store failure during such an explicitly requested build therefore surfaces in that request's error.

What stays true: plain suggest queries never call `build` and never see the path; and the path was already logged at INFO on every successful store before this branch ("Stored suggest data to: ..."). The branch adds the path to a WARN and to a build failure, both operator-facing.

**Decision: keep the code as gated. Correct the draft's Limits line to the accurate reach.** Suggested wording: "The store file's absolute path appears in log lines, as it already did for successful stores, and in the error returned to a build request that fails while storing (suggest.build or buildAll). Suggest queries that do not request a build never see it." Naming the file that failed to store is part of what the ticket asks for (identity and cause), and a build is an operator action on the operator's own node.

## B. Gate records: cite the ledger receipts; three holds are released

The audits were written before three of these gates existed, and the inventory's "gated" labels for 14171 and 17393 came from the revoked Windows batch, not from any gate. Current receipts, all in the main side's receipts ledger (`test-receipts-63-branches.md`) with logs in `~/workspace/tools`:

- **SOLR-9227**: GREEN at `3c23fc5cfa6` (2026-10-06, g9227-gate.log; premise g9227-premise.log).
- **SOLR-9968**: GREEN at `b1865b605184` (round 27, g9968-gate.log); GREEN again at `d688e1efdf2` in the Pairs round (g9968r1-gate.log, class 13 tests); top-up GREEN at the live tip `c31d2ff1a3f` (g9968-topup-gate.log, recorded 2026-10-09, class 13 tests, 0 failures, 1 pre-existing skip; production byte-identical to the gated tree). **Hold released.**
- **SOLR-14171**: first full gate GREEN at the live tip `faa262eedc5` (g14171-gate.log, recorded 2026-10-09; pre-fix proof PASS; class 11 tests, 0 failures, 1 pre-existing skip). **Hold released.**
- **SOLR-17393**: baseline gate GREEN at the live tip `626241e647e` (g17393-gate.log, recorded 2026-10-09; pre-fix proof PASS via the visibility-only shim at seed 17393C0FFEE17393; SuggestComponentMergeTest 2 of 2, SuggestComponentTest 12 of 12, DistributedSuggestComponentTest 1 of 1). **Hold released.** The receipt establishes the mechanical state; item C covers the design finding.
- **SOLR-9637**: the receipt the draft round could not find is in the ledger (2026-10-05): DONE, GATED, PUSHED at `c50fa4ffd93`, log g9637-gate.log. Premise grounded against the stacked base (production reverted to the 17393 tip, seed DD5CE1E48F7BFED2, exactly the two 9637 tests fail); counts SuggestComponentMergeTest 4 of 4, SuggestComponentTest 12 of 12, DistributedSuggestComponentTest 1 of 1; check rc=0.

Drafts cite these receipts. Where an audit says "gated" for a different reason (the inventory), the ledger receipt at the live tip governs.

## C. SOLR-17393 HIGH finding: confirmed, the merge-only scope is the intended answer

Yes. The owner ratified it on 2026-10-09 (`material/suggester-round-4-decisions.md`, item 7): the merge-only fix ships, its Limits state the per-shard cut plainly, and the per-shard fix is named there as a planned follow-up submission, under the owner's standing instruction that a needed follow-up fix is listed in Limits or in a Choice with a plan to submit one. That is the audit's second re-verdict option, taken deliberately. No per-shard regression was added because no per-shard change is in this branch.

## D. Audit line numbers: corrected range

At `3c23fc5cfa6`, the `lookup.build(dictionary)` call is at line 175 of `SolrSuggester.java`, inside the try block at lines 174-182. The SOLR-10937 audit's "lines 172-181" is off by two; the review side may correct the audit or leave this note as the correction. The store step the 9227 branch wraps is at lines 186-191, as the 9227 audit says.

## The two docs verifications the draft round asked to see

Both are recorded in the main side's takeover log (the lane's report was not in the pr-prepare material; the results are stated here). Method: `javap -c -p` on the pinned lucene-suggest 10.4.0 jar in the Gradle cache; no sources jar exists locally.

- **SOLR-10937, five-lookup list: confirmed.** `WFSTCompletionLookup.build` constructs `WFSTInputIterator` over the temp directory, and `TSTLookup.build` constructs `SortedInputIterator` over it; both reach `OfflineSorter` with that directory, as do Analyzing (Fuzzy inherits its build), and FST. The NOTE's five-factory list stands at head `7d0cd11bafd`.
- **SOLR-11844, scaling rule: confirmed.** In `BlendedInfixSuggester.createResults` bytecode: a weight of 0 is promoted to 1, a weight with absolute value under 10 is multiplied by 10, and the result is then multiplied by the position coefficient. The formula (weight times coefficient) and the weight-0-with-no-weightField fact also verified (`DocumentDictionary` returns 0 on every path without a stored value). The shipped text at head `cbd08c20e9a` states all of this; the earlier claim that scores become 0 and stop following position was falsified by the same check and is not in the shipped text.

## SOLR-17215 status

The catch narrowing and ref-guide note are being implemented by a main-side lane; its gate follows. The draft waits for that receipt, as the draft round has it.
