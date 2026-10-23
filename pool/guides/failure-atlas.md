# Guide: failure atlas

Red builds get classified before they get retried. Each entry is one failure class the pool has already paid for: its signature from the log alone, the first checks in order, the rule that decides the classification, what to record and where, and the instance it was learned from. A class with no instance on the record has no entry. Read [traps-build-and-test.md](traps-build-and-test.md) for the rules that do not need a classification.

Status of this atlas: round 1, written by vm5 on 2026-10-23. Classes 1 and 6 are not yet entered (see the end). Every entry needs a second host to confirm its cited instance before the assignment is DONE.

## 2. Run-shape failure

- **Signature.** A check fails in the same way at the branch head and at its base. In the ref guide, `:solr:solr-ref-guide:check -x test` fails in `buildLocalAntoraSite` with `Local content source must be a git repository: <path>`, and `checkSiteLinks` does not run because it depends on the site build.
- **First checks, in order.** (1) Is the checkout a linked git worktree (`git rev-parse --git-dir` differs from `--git-common-dir`)? (2) Run the same task at the base commit in a second detached worktree. (3) Compare the two failure messages word for word.
- **Classification rule.** Identical failure at head and base is the run's shape, not the change. Record it as a pre-existing run-shape failure of that check and name the cause. Do not classify it as a code failure of the branch.
- **What to record.** In the gate or check record: the failing task, the message, both commit SHAs, and the base-run log path. Say that the check was not verified at the head, if it was not.
- **Instance.** SOLR-18555, `receipts/SOLR-18555.md`, section "Module checks, SolrJ and ref guide, head b99259cba3" (item 4). The head and base runs both failed in `buildLocalAntoraSite` with the same Antora message, in linked worktrees at `b99259cba3` and base `c8ab71430f`. The same record also notes that `checkSiteLinks` did not run.

## 3. Environment refusal

- **Signature.** The run stops before any test executes. The log has a Gradle lock timeout, a disk or cache refusal, or the run is queued behind another build and no test result file is written. No fresh JUnit XML exists for the classes.
- **First checks, in order.** (1) Look for fresh XML for the target classes. None means no test ran. (2) Check the host's Gradle lock holder and the disk usage. (3) Check whether a stale daemon holds the lock.
- **Classification rule.** An environment refusal is recorded as a refusal, not as a test result. It may be relaunched once, after the cause is cleared. A second refusal goes to the claim as ENDED with the reason, not as a failing test.
- **What to record.** In the claim's step list: the refusal text, the lock or disk state, and the relaunch outcome. Never write a count of tests for a run with no XML.
- **Instance.** `pool/guides/traps-build-and-test.md`, "One Gradle build at a time per host" (2026-10-01 and 2026-10-03): a Gradle build failed with a lock timeout while another build held the cache lock, and a stale daemon held the lock. The fix was to clear the stale daemons and lock files and re-run serially. The gate rule for relaunch is in `gates/SOLR-16290-fix.md` (the note that a run which never executed tests may be relaunched). This instance is the guide's record; the run-level log for it is not on the branch.

## 4. Wrong-name dispatch

- **Signature.** A focused run reports `No tests found` for a class that exists. The test filter names a package or a Gradle project path that was typed from memory, such as `core` where the project is `:solr:core`, or a package built from the feature name.
- **First checks, in order.** (1) Grep the branch for the test file to get its package. (2) Copy the full Gradle project path from the module's build file or the task list. (3) Re-dispatch with the exact package and path.
- **Classification rule.** `No tests found` is a dispatch error until a grep proves the package is right. It is never a test failure, and it is not counted as a pass.
- **What to record.** The dispatched name and the grep result that gives the right one, in the gate log, before the re-run.
- **Instance.** `pool/guides/traps-build-and-test.md`, "Get a test class's exact package before dispatching": four dispatches on 2026-10-05 failed with `No tests found` on guessed packages and were read as test failures until reproduced locally.

## 5. Seed-dependent test defect

- **Signature.** A test fails under some random seeds and passes under others on the same head. The failure is an assertion about a name or count that the test builds from a fixed string or from its own assumption, not from the state it created. Passing seeds show nothing about the defect; they only show it did not fire.
- **First checks, in order.** (1) Fix the failing seed and reproduce it; a seed that fails once fails again under the same command. (2) Find the thread or path that produces the state the assertion reads. (3) Compare the assertion's expected value with what that path really uses.
- **Classification rule.** If the production path behaves as specified and the test's expected value does not match the state the path actually creates, the defect is the test's synchronization or expectation. Production code is not changed on this evidence. Test-only fixes need a mutant that fails at the head without the fix.
- **What to record.** The failing seeds, the passing seeds with the note that they say nothing about the bug, the root cause in one sentence, and the mutant results.
- **Instance.** SOLR-13059, `receipts/SOLR-13059.md`, "Root cause of the gate-2 failures (fixed in 6914eae1c2)". The preemptive checks matched the literal `"myalias" + TRA`, but the thread for `alias2` runs as `myalias2`, so the asynchronous creation was checked for one alias only. Seeds 13059C0FFEE13059, 13059C0FFEE13060, 13059C0FFEE13061 and 13059C0FFEE13062 failed; 13059C0FFEE13063 and 13059C0FFEE13064 passed by timing. The fix changed only `TimeRoutedAliasUpdateProcessorTest.java`.

## 7. Tidy stop

- **Signature.** A gate reports `GATE INVALID` at step 1 (tidy). Tidy exited 0 but the tree changed; the change is formatting only, with no test or expectation touched.
- **First checks, in order.** (1) Read the tidy diff recorded by the gate. (2) Confirm it is formatting only. (3) Check that nothing after tidy ran.
- **Classification rule.** On our own prepared branches, a formatting-only tidy diff is a gate repair: commit the tidy output, then re-run the gate from step 0. Any change beyond formatting stops for a human read. Earlier steps' results do not carry over to the new head.
- **What to record.** The tidy diff summary (files, insertions, deletions), the commit that applies it, and the new head.
- **Instance.** SOLR-18555 phase r2, `gates/SOLR-18555-phase-r2.md` line 76 (2026-10-20): tidy changed one file in `HighlightRequestHandlerHttpTest.java`, one insertion and two deletions, which joined a two-line statement onto one line. The job was GATE INVALID and the author applied tidy and pushed a new head. The owner's complaint list (`pool/owner-complaints-2026-10-19.md`, item 7) names SOLR-18503 with the same pattern, GATE INVALID for tidy.

## Not entered yet

- **1. Step miscount.** The guide's "Gate reading" rule describes it, and the SOLR-6045 instance is cited, but the round 2 summary where the miscount was corrected is not on the branch. The receipt `receipts/SOLR-6045.md` labels the pre-fix proof "PASS" next to "6 failures" on the same line. That is a labelling problem, but it is not the miscount instance. Enter this class when the summary is found.
- **6. Harness hang.** The assignment's instance is "the settled GCS harness classification on PR #5029". The record on the branch, `reports/flaky-rca-gcs-install-shard.md` and `reports/flaky-rca-cluster-shutdown-hang.md`, classifies the hang as a pre-existing flake in production shutdown code (close order in `HttpShardHandlerFactory`, or Jetty), not as a harness hang. The entry's rule ("the remedy is a re-run, not a code change") does not match that classification. Entered only after the owner or a second host says which record settles the class.
