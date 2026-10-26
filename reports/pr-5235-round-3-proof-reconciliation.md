# PR 5235 round 3: proof reconciliation and offer decision

Status: FIX PACKAGE VERIFIED, NO FURTHER CODE CHANGE OR GATE REQUIRED

- Reviewed board snapshot: e9b9b8b1624515329d10a19b46cae8b25ea773a3.
- Read receipts/PR-5235-compatibility-fixes-round-3.md, reports/pr-5235-compatibility-fixes-round-3-outcome.md, gates/pr-5235-compatibility-fixes-round-3-superseding-caf8dde.md and claims/pr-5235-compatibility-fixes-round-3-vm2.md.
- Verified fork head: caf8dde9063fd2470c02d9938b5723e691c0e11e. Compared it with 5f279c7aa44a993bd999be1d4dfc6ecab7942bc8: only one deprecation-javadoc wrap changes. Nick Shanin authored the repair, without attribution trailers. No production logic or test changed.
- Verified F tree: 0221b9385b5f33479daccd63799746c262a2ce8c, matching the receipt's full-clone tree prefix. The receipt records the root check at caf8dde in that clone.
- Live PR remains open at 9ff44cdd0b818c6f1296d69651732f794acd76b7, an ancestor of F. No reviews or inline review comments. One Nick-authored conversation comment announces a forthcoming four-issue review; it does not duplicate the fix offer.

## Decision

The required matched pair, neighboring tests, clean tidy, and root check are complete at the offered head. No additional source/test patch or execution is needed. The Antora worktree failure is resolved by the full clone at the same commit/tree; disclose that condition and NODE_ENV being unset rather than describing the failed attempt as successful.

The self-review report and addendum described the head at their writing time, before the gate host's permitted tidy repair. Their current-head statements are now superseded by the caf8dde gate and receipt. Historical files remain intact under the add-only workflow. Current records and all adoption/diff links must use caf8dde.

Runtime evidence for the generic embedded case is SolrException "missing content stream" before the count assertion. BAD_REQUEST was inferred from the original source, not observed in the captured XML, and is not an execution-proof claim. The case is additional coverage of generic fallback, not a fifth independently established base-to-PR regression.

The receipt's sentence "three skips" disagrees with its own counts and named cases: solrj 83/1 skipped, Jetty 52/0 skipped, core 4/1 skipped. Those detailed figures enumerate two skipped tests. The outcome report has the same detailed counts. Use those counts explicitly in public text, with no assertion of a third skip. The gate host owns the receipt; this reconciliation does not rewrite it.

## Author-facing changes

Use a pinned SHA-to-SHA diff, replacing the gate draft's moving branch comparison. Use direct-URL fetch so adoption does not depend on an unused remote alias. Keep the pinned fast-forward-only merge and all three exact focused Gradle commands. Include the four behavior/fix explanations, observed results, two documented skip cases, and the root-check environment conditions. Do not claim a precompiled linkage test or observed HTTP status for the generic embedded failure.

Standing authorization: root workspace AGENTS.md, "PR review follow-ups after a clean gate" (Nick, 2026-10-26). The proof and branch state meet its conditions; no unresolved reviewer objection or public-response concern remains. This authorizes the factual PR fix offer only. No Jira write, PR body edit, approval, merge, new PR or contributor-branch write is included.
