# Gate job: SOLR-13369 dated base-code run for the draft's base sentence

- Branch: solr-13369-submit. Head: `dfa0db5bdf96d119d985a8b895ff90c4d783ad47` (live tip, ls-remote 2026-10-14; matches receipts/SOLR-13369.md).
- Status: OPEN. Posted by windows (review agent), for Nick Shanin, 2026-10-14. Claim: `claims/pool-census-followup-round-4-scheduled.md` (slice S4).
- Capability tags: gate. Intended host: any capable Linux host (vm1 or vm2), under the WORKFLOW.md claim rules. Windows never claims build work.
- Job type: one dated focused run on the base code. No code change, no push.
- Why: `pr-drafts/solrcloud/SOLR-13369.md` says the same test also passed 1 of 1 on the base code. That count carried only a takeover-log date (2026-10-07), which is not a verification date. A dated base run settles it. The round 5 decisions (`reports/solrcloud-decisions-round-5.md`) hold SOLR-13369 for this run (step 4 of `gates/SOLR-13369-dated-run.md`).

## Steps

1. Confirm the head: `ls-remote origin refs/heads/solr-13369-submit` must equal `dfa0db5bdf96`. If it differs, stop and record the new tip here.
2. Compute the base: `git merge-base upstream/main dfa0db5bdf96d119d985a8b895ff90c4d783ad47`. Expected `c3cdf7b46e8cfff3673f76d881f32cf8e7b00622`. If it differs, stop and record it.
3. Grep the branch for the exact package of `TriLevelCompositeIdRoutingTest` before composing the run spec. The path is `solr/core/src/test/org/apache/solr/cloud/TriLevelCompositeIdRoutingTest.java`; confirm the package by grep.
4. Run on the base production code with the head's `TriLevelCompositeIdRoutingTest.java` overlaid. The base copy of the test carries `@AwaitsFix`, so only the head copy runs the test. Use a throwaway worktree at the base, not the ticket worktree. One focused run with JUnit XML, under the standing queue rules.
5. Record the counts (tests, failures, errors, skipped), the seed, and the run date (UTC) in a base-run section of `receipts/SOLR-13369.md`, with the base sha and the head sha. Mark this job DONE.

Expected result: 1 test, 0 failures, 0 errors. If the base run fails, stop: record the failing test, seed and reproduce line in this file and in the receipt, and do not relaunch. The review lane then removes the base sentence from the draft, since the draft cannot show a base pass that did not happen.

## Checklist

- [ ] head confirmed by ls-remote
- [ ] base confirmed by merge-base
- [ ] package grepped
- [ ] base run with the head test overlaid: JUnit XML counts, seed and date recorded
- [ ] receipt base-run section added; job DONE
