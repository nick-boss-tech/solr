# Schema, analysis and field types round 1: round roll-up

Claim: `claims/schema-analysis-round-1.md` (commit `c87876a40f2`). Assignment: `assignments/schema-analysis-round-1.md` (commit `44bae5cc588`). Per-part reports: `reports/schema-analysis-round-1-s1.md` through `-s4.md`.

Done 2026-10-09 by Claude Code (AI agent), working for Nick Shanin. Four read-only subagents did the audit, split by ticket cluster. No build, Gradle run, or test was run. No submit branch, live PR, JIRA item, or comment was touched. Nothing was posted. Drafts are in `pr-drafts/schema-analysis/`.

## Heads

All eleven heads match the claim's table. The fetch moved four unnamed tips: 10131, 10403, 15712 (see below), and the audit-only tickets. 10403 moved from `1e285cd730c` to `d8e03755cb5`, which removes its handoff note. The 10403 receipt is stale. 15712 moved from `555f9cab6b7` to `cb988a2ff4c`.

## Per-ticket verdicts

| Ticket | Verdict | Draft | Head | What blocks it |
|---|---|---|---|---|
| SOLR-9349 | Draftable after a history fix | `SOLR-9349.md` | `1e79bb42123` | Commit messages name process and internal files; squashing rewrites the fork branch |
| SOLR-14199 | Held | `SOLR-14199.md` (HOLD and CONFIRM markers) | `e743c90da79` | Probable test failure in `SchemaVersionSpecificBehaviorTest`; unstated existence-query change; receipt count 37 does not match 36 test methods; ledger Limits lines not on disk |
| SOLR-15357 | Held | `SOLR-15357.md` | `d717899b873` | Test names are wrong, so the `/get` asserts cannot fail; changelog title claims a real-time get effect the branch does not produce; the receipt's Proof wording is wrong |
| SOLR-15358 | Draftable after one fix | `SOLR-15358.md` | `cbb345f2e7d` | The base fail-before run is a placeholder; land after 15357 |
| SOLR-15712 | Held (gate now green) | none | `cb988a2ff4c` | The receipt was updated on origin after the claim (`d48d2595e99`): gate green at this head, one base failure. Still held: the core guard fails hard at every `luceneMatchVersion` (the ICU sibling warns below 9.0 and fails from 9.0), and it may duplicate SOLR-15777 |
| SOLR-15945 | Draftable after two branch fixes | `SOLR-15945.md` | `adb0fd450c0` | Changelog title is wider than the fix; a test-schema comment is misleading |
| SOLR-10131 | Held for the owner's scope call (gate now green) | none | `fd495167cd0` | The receipt was updated on origin after the claim (`cbe3598c151`): gate green at this head, one base failure (the new test). Still held: the ticket's 500 symptom is already fixed by SOLR-10653 (`c1cfaec00d3`); the branch fixes a different gap; query paths change to 400 |
| SOLR-10403 | Audit only, no gate | none | `d8e03755cb5` | Receipt stale; no premise run; only query-time `convertAmount` changes, so no indexed values change |
| SOLR-16977 | Draftable | `SOLR-16977.md` | `1142f9563abe` | Owner decides whether the stored-vector half goes in this PR or a follow-up |
| SOLR-17047 | Draftable after owner calls and two fixes | `SOLR-17047.md` | `1ba7e33bfe7b` | Changelog title leaves out the quantized change; a javadoc sentence is wrong about cuVS; owner calls on quantized fields and cuVS settings |
| SOLR-18134 | Held (draftable after fixes) | `SOLR-18134.md` | `c5a0bdb21e86` | Upgrade note and filters reference say every parser builds a phrase; only the field query parser and quoted text do; base overlay not confirmed |

## Important findings

- **SOLR-14199, probable test failure (code reading, not run).** `SchemaVersionSpecificBehaviorTest` expects the old `omitNorms` default for point types on schema 1.0 to 1.4. The branch sets it for all versions. The gate did not run that class. The part s1 report gives the exact replacement assertion.
- **SOLR-14199, stored-only point fields.** A stored-only point field now returns 400 for existence queries, where base returned zero matches. The change is not stated anywhere. The part s1 report gives two options (keep and state it, or restore base behavior for stored-only points).
- **SOLR-15357, vacuous `/get` asserts.** `CopyFieldSubFieldsTest` reads `price_c_l_pl` and `price_c_s_c`. The currency type writes `price_c___l_pl` and `price_c___s_c`, because the separator is three underscores. The asserts pass and can never fail. Fix the names and rerun.
- **SOLR-15357 and SOLR-15358, real-time get (owner call).** Real-time get and `fl=*` still return the docValues sub-fields that 15358 writes. 15357 does not change that path. The part s2 report gives the options. Land 15357 before 15358. A combined run is needed either way.
- **SOLR-15712, compatibility.** The branch's core guard fails hard at every `luceneMatchVersion`. An old schema with `useDocValuesAsStored="true"` on a `CollationField` would stop loading. The ICU type's rule (warn below 9.0, fail from 9.0) is the precedent. The part s3 report gives the replacement code.
- **SOLR-10131, scope.** The ticket title is the 500 symptom. SOLR-10653 already returns 400 for that value. The branch fixes a separate gap: 36-character values with non-hex characters were accepted. That needs a scope decision before any draft.
- **SOLR-18134, phrase mechanism.** The upgrade note (`major-changes-in-solr-10.adoc` around lines 379 and 383) and `filters.adoc` line 3884 say every parser builds a phrase. Only the field query parser and quoted text do. The shipped 1.7 configsets build a SHOULD query for unquoted standard-parser text. Replacement wording is in the part s4 report, findings 14 to 16. The base proof overlay is not confirmed (finding 17).

## Interactions and landing order

- **9349, 14199, 15357.** All three apply cleanly to current main, and each pair commutes in trial merges. Landing order: 9349, then 14199, then 15357. 15357 widens the copy-target check that 9349 relies on, so it lands last and carries a note about 9349.
- **15357 and 15358.** Land 15357 first, because the atomic update path needs 15357's copy-target skip. The two sub-field sets are the same. The merge is clean in both orders.
- **16977 and 17047.** Both change `DenseVectorField.java` in separate hunks. Clean in both orders. 17047's eager construction does not change the assumption in 16977's zero-vector check. No landing dependency.
- **15945 and 18134.** No shared files. Clean merge.
- **10403 and the 15357/15358 pair.** The two share `CurrencyFieldTypeTest.java` with separate hunks. Clean merge. No effect on indexed sub-field values.

## Corrections to the record

- The receipt for 10403 is stale: it names `1e285cd730c`, and the live tip is `d8e03755cb5`.
- The receipts for 10131 and 15712 were refreshed on origin after the claim. Both now record gate green at the reviewed heads. The holds above rest on scope and compatibility, not on the gate.
- The 14199 receipt's count (37) does not match the 36 test methods at the head. Confirm from the JUnit XML or correct the receipt.
- The round-2 review for 15357 says "independent files; no conflict expected". That is right for files and wrong for behavior.

## Draft fixes before posting

- `SOLR-14199.md`: remove the bracketed markers after the owner decisions and the gate run; the count placeholder must be resolved.
- `SOLR-15357.md` and `SOLR-15358.md`: replace "Gate run" with a plain description of the run and its head. Both drafts name a head that will move after the test-name fix.
- `SOLR-15358.md`: fill the base fail-before placeholder from a base run.
- `SOLR-9349.md`, `SOLR-15945.md`, `SOLR-16977.md`, `SOLR-17047.md`, `SOLR-18134.md`: apply the branch-text fixes in the part reports before any draft is posted.

## Owner decisions

1. SOLR-9349: approve squashing the fork branch to one commit (a force push to the fork).
2. SOLR-14199: keep the stored-only point existence change, or restore base behavior for stored-only points.
3. SOLR-14199: approve a fix commit for `SchemaVersionSpecificBehaviorTest`, then a gate run before any PR.
4. SOLR-14199: supply the two ledger Limits lines (the file is not on disk).
5. SOLR-15357: widen the change to skip copy targets in the real-time get and `fl=*` paths, or keep it narrow and list the gap in Limits.
6. SOLR-15358: keep the default for docValues as stored on the new sub-fields, or turn it off for those types.
7. SOLR-15712: duplicate of SOLR-15777, or a separate change. Keep the hard fail, or use the ICU gate (recommended).
8. SOLR-15945: load stored-only spatial fields (the current draft's Choice), or keep refusing them with a clearer error. Also decide whether SOLR-15403 goes in the changelog links.
9. SOLR-15945: rebase. The branch is 66 commits behind upstream main, and a rebase changes the head SHA the draft names.
10. SOLR-10131: submit the non-hex check under this ticket with a description that says the 500 is already fixed, re-scope the ticket, or hold. Accept the new 400 for queries with 36-character non-hex values, or limit the check to writes.
11. SOLR-16977: the stored-vector half in a follow-up ticket, or in this PR.
12. SOLR-17047: quantized fields under a non-Schema codec fail at load (the current draft's Choice), or the check drops quantization. cuVS settings under `SchemaCodecFactory` stay a Limit, or get their own failure.
13. SOLR-18134: ship the Solr filter now (the recorded direction, kept as the Choice). Approve the phrase-scope wording. Confirm the base run overlaid `src/test-files`.
14. Landing order: 9349, 14199, 15357, 15358; 16977 and 17047 in either order.

## Not done

No build, test, Gradle run, `gh` call, fetch, commit, or post. Gate logs and JUnit XML named in receipts are not on disk, so the base failures and counts are receipt-only. Lucene 9.x and 10.x were checked for the 18134 path only, by reading class files. The JIRA text comes from local packets; no live JIRA read was done.
