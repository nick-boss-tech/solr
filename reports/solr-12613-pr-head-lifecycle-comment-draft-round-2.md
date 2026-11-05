# Conditional comment revision: repaired picker test isolation

Status: DRAFT ONLY, NOT POSTED, proof pending. Supersedes `reports/solr-12613-pr-head-lifecycle-comment-draft-round-1.md` for publication. Current fork F2 is `b0e096ae19ad637107227e1bc04e951b9023a680`; original contributor P is `6b5d830132b941faa85856347099ade8b190be0e`. The round-1 gate at F1 was NOT GREEN. Every passing statement below is hypothetical wording for a successful complete round-2 receipt, not an observed F2 result.

Before publication read `gates/solr-12613-pr-head-lifecycle-round-2.md` and its actual receipt. Require full F2 picker success and all four intended failures in the combined original-production run, all baseline/paired neighbors, tidy/Error Prone/exact-source client/root proof, no skips/errors/leaks and matching final remote head. Incorporate actual toolchain/environment/repair conditions; change counts/head/commands if needed. Preserve the round-1 failure rather than silently presenting it as passing.

Refresh PR head/state/discussion and contributor ancestry. PR was approved by Eric but remains unchanged by this fork. If merged, moved or already incorporating the patch, reconcile the delivery route and omit obsolete fast-forward instructions. Use reviewed-by-Nick disclosure only after actual review. Existing owner instructions/holds/standing clean-gate rules govern publication; this file grants none.

## Proposed public text only after the full round-2 job passes unchanged

🤖 *AI text below* 🤖 *(prepared for review by Nick Shanin)*

@iprithv @epugh, I have a tested core-menu follow-up on top of `6b5d830`: [two-file diff](https://github.com/nick-boss-tech/solr/compare/6b5d830132b941faa85856347099ade8b190be0e...b0e096ae19ad637107227e1bc04e951b9023a680).

It ignores older core responses after a newer refresh or node selection, applies restored selection before the initial core request, and retains a local core refresh when mode metadata fails. Four browser regressions cover these cases using the existing fixtures.

My three earlier P2 findings are addressed in the current PR source: the added backend helper was removed, and failed live-node discovery now continues to local core refresh. This patch handles the separate startup and response-ordering concerns.

<details>
<summary>Linux verification at b0e096ae19ad637107227e1bc04e951b9023a680</summary>

- Full picker suite: 8/8; original PR picker: 4/4, seed `5110C0AE`.
- The complete 17-method neighboring selection passed on both heads at seeds `5110C0AE` and `12613F00D`, including standalone Add Core, node screens, logging-level changes and authentication. No failures, errors, skips or thread leaks remained in these successful runs.
- All four regressions executed together with original production and unchanged repaired tests, failing at the intended stale-inventory, initial-target and fallback-refresh assertions.
- Root tidy, Error Prone test compilation, exact-source generated client and root `check -x test` passed with clean source.
- The first patch's full picker run failed after the controlled metadata-error test left shared app state behind. I repaired test setup to use the existing fresh-page helper; the complete suite above was rerun. The historical Windows AddCore timeout remains recorded as non-reproduced on these paired heads, with no claim about its original cause.

[Verification record](https://github.com/nick-boss-tech/solr/blob/coordination-ui/receipts/SOLR-12613-pr-head-lifecycle-round-2.md).

</details>

You can take the tested commits without rewriting history:

```sh
git switch admin-ui-node-picker
git fetch https://github.com/nick-boss-tech/solr.git pr-5110-core-menu-lifecycle
git merge --ff-only b0e096ae19ad637107227e1bc04e951b9023a680
```

The broader SOLR-18619 migration, generic transport follow-ups and legacy parameter deprecation remain separate. Would you like to include this small correctness patch here?
