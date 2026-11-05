# Conditional comment: SOLR-12613 tested lifecycle patch offer

Status: DRAFT ONLY, NOT POSTED. Prepared at F `f02bb7bb06decce976c3ed2fdfbf5907764c2183` on P `6b5d830132b941faa85856347099ade8b190be0e`. The patch is pushed; verification is pending. Owner asked for a comment assuming the gate works. Every passing statement below is conditional future wording, not an observed result. Replace it from the actual receipt if any head/count/seed/condition differs, and do not publish while proof is missing or a concern remains.

Read `gates/solr-12613-pr-head-lifecycle-round-1.md` and its actual receipt before using this text. Required checks include old-seed neighbor reconciliation, all four intended negative assertions, root tidy/Error Prone/root check, no skips/errors/leaks and final exact-head read-back. Refresh live PR ancestry and conversation; omit an obsolete offer if already incorporated. If the contributor head moved, reconcile source and commands before proposing a merge. A new tested repair head needs a new pinned diff/commands and draft revision.

Before publication, incorporate the execution host's actual AddCore investigation conclusion and any material NODE_ENV, downloaded-artifact or fixture conditions into the verification block. Fresh passing runs do not establish the historical timeout's cause. The linked verification record must actually exist and meet every requirement first.

The reviewed-by-Nick disclosure may be used only after actual review. Current prepared-for-review header is truthful. Publication follows existing owner instructions, any actual draft hold and the standing clean-gate policy; this document itself grants no posting permission.

## Proposed public text, assuming the full named job passes unchanged

🤖 *AI text below* 🤖 *(prepared for review by Nick Shanin)*

@iprithv @epugh, I have a tested core-menu follow-up on top of `6b5d830`: [two-file diff](https://github.com/nick-boss-tech/solr/compare/6b5d830132b941faa85856347099ade8b190be0e...f02bb7bb06decce976c3ed2fdfbf5907764c2183).

It prevents an older core response from replacing newer results, including two refreshes of the same node. It also waits for restored node selection before the first core request and preserves a local core refresh when system metadata fails. Four regression tests cover these cases using the existing browser fixtures.

My three earlier P2 findings are addressed in the current PR source: the added backend helper was removed, and failed live-node discovery now continues to local core refresh. The patch above addresses the separate startup and response-ordering concerns.

<details>
<summary>Linux verification at f02bb7bb06decce976c3ed2fdfbf5907764c2183</summary>

- The picker suite passed 8/8 at seed `5110C0AE`; the original PR's 4 picker methods also passed on its unchanged head.
- The full 17-method neighboring selection passed on both heads at seeds `5110C0AE` and `12613F00D`, including standalone Add Core, node screens, log-level changes, standalone logging and authentication. No failures, errors, skips or thread leaks remained in these successful runs.
- With original production restored and the new tests unchanged, all four regressions failed at their intended assertions: obsolete inventories, an incorrectly targeted initial request, and a missing fallback core refresh.
- Root tidy, Error Prone test compilation and root `check -x test` passed with clean source. The generated client came from the tested checkout's native API inputs.
- The earlier Windows neighbor timeout remains recorded as a historical failure; the new full-selection runs establish the results above, without claiming to explain that old timeout.

[Verification record](https://github.com/nick-boss-tech/solr/blob/coordination-ui/receipts/SOLR-12613-pr-head-lifecycle-round-1.md).

</details>

You can take the tested commit without rewriting history:

```sh
git switch admin-ui-node-picker
git fetch https://github.com/nick-boss-tech/solr.git pr-5110-core-menu-lifecycle
git merge --ff-only f02bb7bb06decce976c3ed2fdfbf5907764c2183
```

I’m keeping SOLR-18619's broader UI request migration, the generic transport follow-ups and legacy parameter deprecation separate. Would you like to include this small correctness patch here?
