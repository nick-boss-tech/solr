# PR 5235 compatibility fixes: outcome report, round 3

Gate: `gates/pr-5235-compatibility-fixes-round-3.md`, superseding `gates/pr-5235-compatibility-fixes-round-3-superseding-caf8dde.md`, with the self-review addendum. Receipt: `receipts/PR-5235-compatibility-fixes-round-3.md`. This report classifies the fixes against the original head. It is not a review and does not claim the PR is merged or accepted.

## Outcome

| Area | Original `9ff44cd` | Fixed `caf8dde` |
| --- | --- | --- |
| Command API reflection (2 methods) | 2 fail (`NoSuchMethodException`) | 2 pass |
| Embedded streams (4 methods incl. 2 controls) | 2 pass, 2 fail (unnamed doc count; generic `missing content stream`) | 4 pass |
| HTTP legacy bodies (4 methods) | 4 fail (body `wt=xml`) | 4 pass (original body) |
| HTTP writer overrides (4 methods) | 4 fail (original body in place of custom) | 4 pass (custom body, hook called) |
| HTTP controls (4 methods) | 4 pass | 4 pass |

Original: 12 fail, 6 pass. Fixed: 18 pass, 0 fail, 0 skip in the targeted classes.

## What this supports

- Each of the four findings reproduces on the original head with the predicted behavior, and the same tests pass on the fixed head.
- The generic embedded case fails on the original head with `missing content stream` before the document count, as the addendum corrected. The fixed head indexes the documents.
- The HTTP controls and the multipart precedence case pass on both heads, so the fixes do not change the explicit-writer paths they cover.
- Neighboring tests are green on the fixed head (solrj 83 with one existing skip; solrj-jetty 52; core 4 with one existing skip). The root check passes in a full clone at the fixed head.

## What this does not support

- No production fix is verified beyond the test scope above; neighboring and root-check results show no regression in the covered areas, not complete coverage of the PR.
- The root check's first attempt failed in a git worktree because Antora needs a full `.git` directory. That is recorded as an environment condition and not as a code result.
- The addendum's `BAD_REQUEST` status for the generic case is not confirmed by the captured XML.
- The PR head is still `9ff44cd` on `dsmiley/solr`; nothing here changes it.

## Head disclosure

The addendum says the fixed head is `5f279c7aa44a` and no new head exists. The fork tip is `caf8dde9063f`, which adds only a javadoc line wrap to `CommandOperation.java`. The gate pins `caf8dde`. The addendum's statement should be corrected in the self-review record.

## Author-facing draft (for the Windows agent to review; not posted)

**Summary.** This adds four fixes and regression tests for the compatibility findings in PR 5235. Final tested head: `caf8dde9063fd2470c02d9938b5723e691c0e11e` on `nick-boss-tech/solr:solr-18351-submit`.

**Diff from the current PR head.** `https://github.com/dsmiley/solr/compare/SOLR-18351-backport-10x...nick-boss-tech:solr-18351-submit`

**Adoption (fast-forward only).** Validated in a scratch clone of `dsmiley/solr:SOLR-18351-backport-10x` at `9ff44cdd0b8`:

```sh
git remote add nick https://github.com/nick-boss-tech/solr.git
git fetch nick solr-18351-submit
git switch SOLR-18351-backport-10x
git merge --ff-only caf8dde9063fd2470c02d9938b5723e691c0e11e
```

The merge fast-forwards from `9ff44cdd0b8` to `caf8dde906`, nine files changed, 560 insertions and 27 deletions.

**Focused validation.** Run from the root of the checkout, with the seed used in the gate:

```sh
./gradlew --no-daemon :solr:solrj:cleanTest :solr:solrj:test --tests org.apache.solr.common.util.TestCommandOperationCompatibility -Ptests.seed=5235C0FFEE18351 --no-build-cache
./gradlew --no-daemon :solr:core:cleanTest :solr:core:test --tests org.apache.solr.client.solrj.embedded.TestEmbeddedContentStreamCompatibility -Ptests.seed=5235C0FFEE18351 --no-build-cache
./gradlew --no-daemon :solr:solrj-jetty:cleanTest :solr:solrj-jetty:test --tests org.apache.solr.client.solrj.jetty.TestLegacyContentStreamCompatibility -Ptests.seed=5235C0FFEE18351 --no-build-cache
```

**Result on the tested head.** 18 of 18 focused methods pass. The root check passes in a full clone (`./gradlew check -x test` with `NODE_ENV` unset). The worktree attempt failed for an environment reason (Antora needs a full `.git` directory), which the clone avoids.

The validation commands above are the gate's exact commands. They were run on the tested head and are not placeholders.

## Next

- The Windows agent reviews this draft and the addendum correction. Nick decides whether to post, under the owner-approval rule. Nothing here is posted to GitHub or Jira.
