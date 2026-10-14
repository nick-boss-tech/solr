# Owner rulings of 2026-10-13: effect on the review lane, round 1

Trigger: pr-prepare tip `4ea40aebba5`, from `db02335`. Three owner rulings were recorded in `5118ccff34f`, and the SOLR-16013 ruling in `4ea40aebba5`. Round claim: `claims/pool-pr-prepare-tip-db02335-round-1.md`'s successor, `claims/pool-pr-prepare-tip-4ea40ae-round-1.md`.

Scope: record housekeeping and a reading of the rulings against the review items. No build, test or gate. No PR, comment, live body or fork branch touched. No subagents were started: the only review-lane change is closing a superseded claim, so there was no reading or drafting to farm out.

## Effect on the review lane

1. SOLR-16013: the fixture patch is superseded. The owner ruled that the branch-caused classification governs, the fixture patch is not pushed, and the production route is taken: an interrupt-flag completion fix at local head `4215c8cee5`, which is gated by vm1 and not yet pushed. Our claim `claims/pool-review-16013-head-failure-fixture-patch.md` is closed as superseded. The patch file `gates/SOLR-16013-fixture-patch-round-1.patch` stays as history and is not to be applied.
2. SOLR-18010, permission copy: closed by the owner. The PR stays a draft until the other contributor's PR merges. The permission-copy disposition is settled in the post-merge rework. No code or draft change follows now. The Choice text held in `pr-drafts/core-admin/SOLR-18010.md` therefore stays as it is, and no new edit is made to it.
3. SOLR-18277: retired by the owner, and all three branches are left in place. No deletion. The receipt record is in `receipts/SOLR-18277.md`. Nothing for the review lane.
4. SOLR-18541, owner decision 1: the multi-query `numFoundExact` question stays posed as the PR's Choice. No reviewer change is added to any patch. The narrowing patch for SOLR-18539 (`gates/SOLR-18539-narrowing-round-1.patch`) is a different ticket and is unaffected.
5. SOLR-18541, owner decision 2: the missing per-query entry crash stays a named follow-up in Limits, with no ticket until the PR merges. This matches the drafts as they stand.

## Still open for the review lane

- The SOLR-18539 narrowing patch: its push route is still the owner's call (the push wrapper's branch rule), and the gate is vm2's.
- The SOLR-18541 fail-before decisions from `reports/narrowing-and-fail-before-round-1.md`: the re-run point and the WithoutHits expectation.
