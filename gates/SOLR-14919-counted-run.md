# Gate job: SOLR-14919 counted focused run at the precision-gate head

- Branch: solr-14919-submit. Head: `483b3885f6439391a3128e6c61be34fc47122514` (fork tip, verified by `git ls-remote` on 2026-10-14 at filing). Parent: `8f1f4ef91e798f736eb91c9625223a654b59794a`; the delta is the changelog title and a test comment reflow only, no production file changes and no test logic changes.
Status: DONE
- Capability tags: gate.
- Job type: a run only. No push, no branch change, no code change. The head is already the fork tip and is classified GATE GREEN (gates/SOLR-14919-precision-round-1-gate.md, Classification; receipts/SOLR-14919.md, top section).
- Why owed: vm2's precision gate at this head (GATE END 2026-10-14T22:24:18Z) took no fresh JUnit count and recorded no seed for its head leg; its pass evidence is the head leg's rc 0. The receipt's top section states that gap plainly. This job closes it with a counted run at the exact head.
- Module: `:solr:core`. Focused class: `org.apache.solr.update.processor.IgnoreCommitOptimizeUpdateProcessorFactoryTest`.
- Seed: `14919C0FFEE14919` (the seed of the parent head's counted run, so the two counts compare directly).
- Expected count, from fresh JUnit XML: 1 test, 0 failures, 0 errors, 0 skipped, matching the counted run at the parent head 8f1f4ef91e (count run 2026-10-13T18:05:35Z to 18:13:49Z, same seed, counts from fresh JUnit XML).

## Steps, in order

0. Claim this job under `claims/` (host, date UTC, head, runner path). Verify the starting point: `git ls-remote` on the fork shows `solr-14919-submit` at `483b3885f6439391a3128e6c61be34fc47122514`. Stop and record here if the tip differs; do not run at any other head.
1. Check out the head in a detached worktree. Copy `gradle.properties` into the worktree (Solr worktrees do not inherit the gitignored file). Confirm the tree is the head's tree (compare the focused test file's hash against the head blob) before running.
2. Run the focused class once, under the blocking flock (`~/workspace/tools/.solr-test-queue.lock`; one Gradle build at a time per host), through the host's standard wrapper: `solr-gradle.sh` on vm1, or the host's own wrapper per its host file on vm2 (`./gradlew` with the vendored JDK and `GRADLE_USER_HOME` under /workspace). Clear the class's stale test results first (or use `cleanTest`), then run `:solr:core:test --tests "org.apache.solr.update.processor.IgnoreCommitOptimizeUpdateProcessorFactoryTest" -Ptests.seed=14919C0FFEE14919`.
3. Count from the fresh JUnit XML for that class: tests, failures, errors, skipped, plus the suite timestamp. Record the run dates (UTC) and the seed with the counts.
4. A run that executed tests and failed is a result, not a launch failure: record the counts and the failure content here and in the receipt, and do not relaunch. A run that never executed tests (an environment refusal: disk guard, journal lock timeout, flock starvation) may be relaunched once; record the refusal either way.

## On completion

- Refresh the top section of `receipts/SOLR-14919.md`: add the counted run at this head (run dates UTC, seed `14919C0FFEE14919`, counts from the fresh JUnit XML, suite timestamp), so the receipt no longer states the count-and-seed gap for this head.
- Mark this job DONE with the date and the counts, and mark the claim DONE.

## Checklist

- [ ] claimed under claims/, fork tip verified at 483b3885f643
- [ ] worktree at the head, gradle.properties copied, tree hash-checked
- [ ] focused run executed under the blocking flock at seed 14919C0FFEE14919
- [ ] counts taken from the fresh JUnit XML
- [ ] receipts/SOLR-14919.md top section refreshed with the count and seed
- [ ] job and claim DONE


## Result (vm2, 2026-10-15T00:39ZZ)

Focused run at 483b3885f6439391a3128e6c61be34fc47122514: tests 1, failures 0, errors 0, skipped 0 (fresh JUnit XML 2026-10-15T00:38:14Z). Receipt top section in receipts/SOLR-14919.md. DONE.
