# Owner directions, round 5: new branches, the #5106 gate, and the #5106 description

## Done

1. **Pushing new branches.** The wrapper `research\push-fork-ff.ps1` now pushes `HEAD:refs/heads/<Branch>`, so it can create a new fork branch. The name check and the no-force rule are unchanged.
2. **SOLR-18541 fix on a new branch.** Pushed `fcd92f23ba6` to `solr-18541-combined-query-merge-totals`. That is the first gate target on the new branch, with its gate job in `gates/SOLR-18541-exactness-gate.md`. PR #5117 is untouched, and it still points at `combinedquerycomponent-merge-totals`. Opening a PR from the new branch needs your go-ahead.
3. **vm2 assignment for #5106.** `assignments/pool-vm2-gate-16630-latches-round-1.md`, pushed. It points at the gate job for `a31707acfdb`, and it states the revert rule for the PULL-timing doubt.
4. **#5106 description, after the assignment.** The live body now says the fixed sleep before the stop is gone, and that the six passing runs were at the earlier head `9bea597`. It also says `a31707acfdb` is pending its gate on vm2. The live body was read back and matches the edited text.

## Not done

- No PR opened, closed, marked ready, or rebased. No comment posted anywhere.
- The #5118 description still carries the Limits bullet about owner-only permissions. That bullet is out of date now that `2b5ad80` keeps the existing permissions. Update it after its gate, or tell me to update it now.
- No build, test or gate run on this host.

## Gates queued for vm2

- `gates/SOLR-16630-latches-regate.md` (head `a31707acfdb`), assigned in `assignments/pool-vm2-gate-16630-latches-round-1.md`
- `gates/SOLR-18541-exactness-gate.md` (head `fcd92f23ba6`)
- `gates/SOLR-18010-perms-regate.md` (head `2b5ad801c9e`), already claimed by vm2
