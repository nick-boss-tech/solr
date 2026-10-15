# Retire pack: SOLR-17372 (branch solr-17372-submit)

Verdict: RETIRED (owner ruling 2026-10-14). The premise is NOT SUPPORTED. The branch is labelled retired and left in place. Salvage is closed.
Round item: reports/retire-candidates-review-round-rr1.md, item 3 (SALVAGE FIRST, written before the premise run and describing that run as specified, not started). Where that part report and the receipt differ, the receipt governs.

## Premise state

receipts/SOLR-17372.md, section "Premise verdict at the ticket seed (2026-10-12, vm2)":

- "Verdict: NOT SUPPORTED. Leg A (current main d76fc14b9857, no tolerance hunk) passes at the ticket's own seed, so the reported percentile failure does not reproduce there."
- Leg A: org.apache.solr.handler.component.StatsComponentTest.testPercentiles, 1 test, 0 skipped, 0 failures, 0 errors, seed 2074D8EC40F42163 (the seed the ticket reports).
- Leg B (branch head, hunk present): not run to a result (worktree delete error). The receipt says it is not required for NOT SUPPORTED.
- Leg C (merge-base e2cdb2d7e8ae): not run.
- Owner ruling 2026-10-14: "RETIRED, branch left in place." Option (i) is closed; options (ii) and (iii) are not taken.

gates/SOLR-17372-premise.md agrees: "DONE by vm2 (coordinator). Verdict NOT SUPPORTED: leg A passes at seed 2074D8EC40F42163 on current main."

Why the receipt governs: the round's part report predates the premise run. It describes a run that had not started, and it notes that the receipt's base runs used seed 17372C0FFEE17372, not the Jira seed. The 2026-10-12 leg A run is the one at the Jira seed, so it closes that gap.

## Head checked

- ls-remote origin refs/heads/solr-17372-submit: 048862fda8ab76f36f259c6849ec5c5a8cfafabe (2026-10-15). It equals the receipt's gated head.
- Merge-base e2cdb2d7e8ae. Three commits above it: 3257831180f (the tolerance hunk), 2869441fb32 (a TESTING handoff doc), 048862fda8a (removes that doc).
- Net diff: solr/core/src/test/org/apache/solr/handler/component/StatsComponentTest.java only, +6/-1.

## What the branch holds, and whether main has it

- The hunk changes the percentile assert's tolerance from fixed 1.0D to Math.max(1.0D, expected * 0.10D).
- Live main still has the fixed tolerance: upstream/main StatsComponentTest.java line 2298, `assertEquals(Double.parseDouble(p), pVal, 1.0D);`. The hunk is not on main. The test file did not change between d76fc14b9857 and live main 714d6ceff852 (checked 2026-10-15).
- Corroboration only: GitHub run 37639175484 (ci/17372-statscomponent, head 933a41eb35f, success, 2026-10-07; API read 2026-10-15). It ran with the hunk, so it does not test the premise.

## Why the premise failing closes the salvage

The hunk is the ticket's own suggested fix (rr1 cites Jira comment 17866876; the Jira packet was not re-read here). Its only reason to exist is an assertion failure at the reported seed. At that seed, on current main, the assertion passes without the hunk. So the hunk would change a passing assertion and fix no failure. A fail-before proof needs a failure without the fix, and the record shows none at the Jira seed. The salvage was triggered only by leg A, and leg A passed.

## Gainable and taken

- Gainable: nothing.
- Taken: nothing.
- No PR exists under apache/solr with that head, and no PR draft exists under pr-drafts/ (receipt).

## Owner step

- Done: the receipt carries the RETIRED label (2026-10-14). Nothing else is required.
- Keep solr-17372-submit at 048862fda8ab76f36f259c6849ec5c5a8cfafabe. Under the owner's rule it is not deleted and not renamed.
- Deletion step, NOT taken and NOT authorized under the 2026-10-14 ruling. It would apply only if the owner reverses that ruling: `git -C C:\Users\shaninna\dev\Solr-issues\source push origin --delete solr-17372-submit`. Here origin is the fork (nick-boss-tech/solr).
- Jira note, optional, owner only, NOT POSTED: "Premise check on current main (d76fc14b9857): StatsComponentTest.testPercentiles passes at the reported seed 2074D8EC40F42163 without the tolerance change, so the reported failure does not reproduce there. The fork branch solr-17372-submit is retired. No PR was opened."

## Condition

The retire verdict rests on one leg A run at one seed on current main. It holds while the testPercentiles assert and its t-digest feed path stay unchanged on main. Reopen the premise if testPercentiles fails on main without the hunk at seed 2074D8EC40F42163, or if a CI failure shows the 2024 pattern (expected 60.0, actual near 58.3). In that case rerun leg A first. Leg C is not needed for the retire decision, because main is the target branch.

## Gaps

- Leg B and leg C were not run. Leg B is not required (receipt).
- The premise log is cited as /workspace/gates/logs/vm2/17372-main.log on the VM. It is not in this worktree, so the leg A counts are as recorded.
- The Jira packet (comment 17866876) was not re-read here.
