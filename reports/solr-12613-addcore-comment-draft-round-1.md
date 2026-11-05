# Conditional combined lifecycle/Add Core offer

Status: DRAFT ONLY, NOT POSTED. G `75a18ecd07925a26a869dac8af8eed9ab4176bb9` is pushed, proof pending. This is the current combined candidate on a NEW branch; F2 branch remains unchanged and failed. Earlier conditional lifecycle drafts refer to failed heads and must not be presented as successful offers.

All passing statements below are hypothetical wording for success of `gates/solr-12613-addcore-initial-status-round-1.md`, not observed G results. Before use, read its actual receipt and verify every named leg, counts, head, conditions, original-selection result and negative assertion. Include actual execution conditions and any remaining uncertainty. Refresh live PR state/head/discussion and ancestry. If merged/moved/incorporated, change delivery route and omit obsolete pull-in commands. Never claim Nick reviewed text before actual review. Existing authorization/holds/clean-gate rules govern publication; this file grants no permission.

## Proposed public text only after all required G proof succeeds

🤖 *AI text below* 🤖 *(prepared for review by Nick Shanin)*

@iprithv @epugh, I have a tested follow-up on top of `6b5d830`: [four-file diff](https://github.com/nick-boss-tech/solr/compare/6b5d830132b941faa85856347099ade8b190be0e...75a18ecd07925a26a869dac8af8eed9ab4176bb9).

It guards obsolete core-menu responses, applies restored selection before the first core request, and retains local refresh after metadata failure. It also prevents a late initial Core Admin response from redirecting away from an open Add Core form; ordinary default-core navigation stays intact when Add is closed.

My three earlier P2 findings are addressed in the current PR source: the backend helper was removed and failed live-node discovery continues to local core refresh. These changes address the separate remaining lifecycle concerns.

<details>
<summary>Linux verification at 75a18ecd07925a26a869dac8af8eed9ab4176bb9</summary>

- Picker 8/8 and standalone Core Admin 7/7 passed. Expanded 19-method neighbors and the original 17-method selection passed at seeds `5110C0AE` and `12613F00D`, with no errors, skips or thread leaks.
- All four original-production lifecycle regressions reached their intended assertions together. The Add Core regression caught default-core navigation closing an open form on the previous controller, while the normal-navigation control passed there. A temporary navigation-removal mutation failed that compatibility assertion.
- Root tidy, Error Prone test compilation, exact-input generated client and root `check -x test` passed with clean source.
- Earlier runs exposed a picker test-isolation defect, then an Add Core timeout. The repaired setup uses the existing fresh-page helper. The controlled Add case demonstrates the late-navigation race; the original failure screenshot did not capture its event ordering. The failed runs remain recorded rather than presented as passing.

[Verification record](https://github.com/nick-boss-tech/solr/blob/coordination-ui/receipts/SOLR-12613-addcore-initial-status-round-1.md).

</details>

You can take the tested commits without rewriting history:

```sh
git switch admin-ui-node-picker
git fetch https://github.com/nick-boss-tech/solr.git solr-12613-addcore-initial-status
git merge --ff-only 75a18ecd07925a26a869dac8af8eed9ab4176bb9
```

The broader UI migration, generic transport changes and legacy parameter deprecation stay separate. Would you like to include these correctness fixes here?
