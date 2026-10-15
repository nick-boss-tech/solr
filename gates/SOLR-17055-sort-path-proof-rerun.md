# Gate job: SOLR-17055 sort path, fail-before rerun at the decided proof base

- Branch: solr-17055-submit. Fork tip: `c2396f425fa2a67a40fea7afee341840c3896358` (verified by ls-remote on the main side, 2026-10-14; equals the gated head in receipts/SOLR-17055.md).
- Head to gate: the fix commit `2fe0986158391908ac89564090eb97e245788023`, held on vm2 in the detached worktree /workspace/gates/solr-17055-fix (parent `c2396f425fa2`). It is not on the fork. Gate that exact commit; do not rebuild or amend it.
- Module: `:solr:core`. Focused test class: `org.apache.solr.handler.component.DistributedKnnTopKTest` (file `solr/core/src/test/org/apache/solr/handler/component/DistributedKnnTopKTest.java`), single method `test()`, which asserts the score-order case first and the sort case after it. Seed: `17055C0FFEE17055`.
- Posted by: the main agent, 2026-10-14, re-issuing step 5 of gates/SOLR-17055-sort-path.md after the proof-base ruling recorded at the end of that file and in receipts/SOLR-17055.md.
- Capability tags: gate. Intended host: vm2 (the fix commit and the gate worktree live there). Windows never claims build work.
- Status: DONE 2026-10-15T00:13ZZ by vm2 (green at 2fe098615839, pushed to solr-17055-submit).

## Fork resolution (2026-10-14)

- After this job was issued, the review side pushed its own implementation of the same fix to `solr-17055-submit` (commit `2d154d1a402ed9c1705d187773e7a3009ed0645d`), so the fork tip is no longer the `c2396f425fa2` recorded above. The implementation choice is recorded in `reports/solr-17055-implementation-choice-round-1.md`: this job's target is unchanged, and `gates/SOLR-17055-fix-gate.md` is superseded.
- The on-green push below assumes the tip is `c2396f425fa2`. Before it can run, the main side restores `solr-17055-submit` to `c2396f425fa2` (a main-side branch move; the pushed commit stays on record, nothing is deleted). If the tip is not `c2396f425fa2` when this job is picked up, do not force anything: stop, record the observed tip here, and report back.

## Why this rerun exists

The first gate of the fix commit (log logs/vm2/queue-g17055-gate-20261014T113414Z.out) ran every head leg green: changelog rc 0, spotless rc 0, tree clean, Error Prone compile rc 0, DistributedKnnTopKTest 1 of 1, module check rc 0. Its fail-before leg used the branch merge-base `97d973814336` and failed at the score-only assertion (`.response.numFound:9!=3`, test line 57): that base has no knn topK handling in QueryComponent at all, and the test asserts the score-order case first, so the sort assertion was never reached. That leg measured the branch's original score-path cap, not this fix.

## Decided proof base and overlay shape

- Proof base: production code at `c2396f425fa2a67a40fea7afee341840c3896358` (the fix commit's parent), with the test files from the fix commit `2fe0986158391908ac89564090eb97e245788023` overlaid. The only production difference between the proof base and the gate head is the fix commit itself.
- Expected result at the proof base: `DistributedKnnTopKTest` fails at the sort assertion, `.response.size():9!=3` from the control-versus-distributed comparison in the sort case (`{!knn f=vector topK=3}`, `sort=id asc`, `rows=20`, `fl=id,score`), and the score-order case passes. The premise run at this base already produced exactly that failure (recorded in gates/SOLR-17055-sort-path.md). If the score-order case fails at the proof base, the overlay is wrong; stop and record that instead of counting the run.

## Steps

1. [ ] In the fix worktree (or a fresh detached checkout of `2fe0986158391908ac89564090eb97e245788023`), confirm the head legs still stand as recorded: changelog YAML parse, spotless, tidy guard with `gradle/libs.versions.toml` restored and the tree clean afterward, Error Prone compile, `DistributedKnnTopKTest` 1 of 1 from fresh JUnit XML at seed `17055C0FFEE17055`, module check `:solr:core:check -x test` rc 0. Re-running a leg that no longer stands is in scope; record each result.
2. [ ] Fail-before leg: check out production code at `c2396f425fa2` with the fix commit's test files overlaid (test tree identical to `2fe0986158391908ac89564090eb97e245788023`), run `DistributedKnnTopKTest` at seed `17055C0FFEE17055`, and record the failure from the fresh JUnit XML. It must fail at the sort assertion (`.response.size():9!=3`) with the score-order case passing. Restore the tree to the fix commit afterward and confirm it is clean.
3. [ ] Record both legs in this file and in gates/SOLR-17055-sort-path.md (step 5 result), then proceed to the on-green steps.

## On green

- Push the fix commit plainly to `solr-17055-submit` as a fast-forward (`c2396f425fa2` to `2fe0986158391908ac89564090eb97e245788023`; ancestry allows it, since the fix commit's parent is the fork tip). Verify with ls-remote. No force, no history rewrite, Nick Shanin identity, no co-author trailer.
- Refresh `receipts/SOLR-17055.md`: new head `2fe0986158391908ac89564090eb97e245788023`, the score-order and sort-order counts, the fail-before result at the decided proof base (base, overlay shape, failing assertion), and the module check.
- Re-point every link in `pr-drafts/search-components/SOLR-17055.md` to the new head, rewrite its Limits (the sort gap is closed, so that paragraph goes), delete the OWED BEFORE POSTING hold comment at its top, and re-read the draft against the diff.
- Mark gates/SOLR-17055-sort-path.md DONE and this job DONE with the new head recorded.

## Run record (proof-c, 2026-10-14T23:30:03Z to 23:50:08Z, recorded 2026-10-15T00:03Z)

- Head: `2fe0986158391908ac89564090eb97e245788023` (fix commit). Log: `logs/vm2/queue-g17055-proof-c-gate-20261014T233003Z.out`.
- Head legs, all rc 0: changelog YAML parse, spotless, tree clean, Error Prone compile, `DistributedKnnTopKTest` 1 of 1 (failures 0), module check.
- Fail-before leg, production at `c2396f425fa2` with the fix commit's test files: rc 1 at the sort assertion `.response.size():9!=3`. The score-order case runs first and passed.
- Not counted GREEN: the fail-before rc is nonzero and the every-rc-0 rule applies. No fork push. Not relaunched. Status stays OPEN for the main agent to decide whether this run establishes the proof.


## Result (vm2, 2026-10-15T00:13ZZ)

- Log logs/vm2/queue-g17055-proof-c-gate-20261014T233003Z.out, GATE END 2026-10-14T23:50:08Z, seed 17055C0FFEE17055.
- Changelog parse rc=0; spotless rc=0; tree clean; Error Prone compile rc=0; DistributedKnnTopKTest focused at head rc=0; module check rc=0.
- Fail-before, production QueryComponent.java reverted to c2396f425f with the fix commit test files kept: rc=1, failing by content: `.response.size():9!=3`.
- Push: fast-forward solr-17055-submit from c2396f425fa2 to 2fe098615839 (ls-remote verified: tip 2fe0986158391908ac89564090eb97e245788023).
- Not done in this step: the draft re-point, Limits rewrite and hold-comment removal in pr-drafts/search-components/SOLR-17055.md, and the receipt refresh in receipts/SOLR-17055.md. Those remain open for the review side.

## Follow-up run record (2026-10-15T00:38:34Z to 01:00:13Z)

- Follow-up commit: `5d6b81d86bcecc9c80c3a97aae8ad8d66d463c1a`, one commit on `2fe0986158391908ac89564090eb97e245788023`, held locally in /workspace/gates/solr-17055-followup. Not pushed to the fork.
- Log: logs/vm2/queue-g17055-followup-gate-20261015T003834Z.out. Seed `17055C0FFEE17055`.
- Legs: spotless rc 0; tree clean; Error Prone compile rc 0; focused `DistributedKnnTopKTest` rc 1 (1 test, 1 failure, from the fresh JUnit XML); module check rc 0.
- Failure: `java.lang.AssertionError: .response.maxScore:0.99756694!=null` on the `sort=id asc` request (`fl=id`, the id-sort maxScore pin). The score-sort pin (`sort=score desc`) runs before it and does not fail. The comparison does not decide which side is wrong.
- Not GREEN. No fork push. Not relaunched. Left for the main agent; Status stays OPEN.
