# Pool assignment: SOLR-9396 distributed row-field test, round 1

Status: OPEN

- Capability tags: premise-run.
- Intended host: either Linux host (vm1 or vm2). Claim under `claims/` before starting (WORKFLOW.md). Windows does not run this.
- Posted by: main agent (Windows session), 2026-10-15, at the direction of Nick Shanin.

PR #5156 (SOLR-9396) carries a fix in `SubQueryAugmenterFactory`. Its own proof covers single-core subquery tests only. This round checks a distributed case: the outer `fl` names `depts:[subquery]` but not `dept_ss_dv`, which the subquery reads as `$row.dept_ss_dv`. The new test `testRowFieldNotInFlDistributed` is in `solr/core/src/test/org/apache/solr/response/transform/TestSubQueryTransformerDistrib.java`. It was added by commit `f62c06588f272834cae16f3f02c8383886b8f00d` on the fork branch `solr-9396-submit`.

Head for this round: `f62c06588f272834cae16f3f02c8383886b8f00d`. Base for the fail-before leg: `c3cdf7b46e8cfff3673f76d881f32cf8e7b00622`, the branch base that the gate receipt used.

This is a premise run: focused tests and one fail-before check. It writes no receipt. Nothing here changes code. A failing test is reported, not fixed.

## Items

1. **Head check.** In your own worktree, run `git ls-remote origin refs/heads/solr-9396-submit`. Confirm the tip is `f62c06588f272834cae16f3f02c8383886b8f00d`. If it has moved, stop and record the new tip in the deliverable. Start from: the fork branch. Output: section "Head" in the deliverable.

2. **Focused head run.** Check out the head at `f62c06588f272834cae16f3f02c8383886b8f00d` in your worktree. Run the focused test classes `org.apache.solr.response.transform.TestSubQueryTransformerDistrib` and `org.apache.solr.response.transform.TestSubQueryTransformer` with `gradlew :solr:core:test --tests "..."` once per class. Record the seed Gradle prints. Count from the fresh JUnit XML under `solr/core/build/test-results/test/`. Expected: TestSubQueryTransformerDistrib 2 of 2 (including `testRowFieldNotInFlDistributed`), and TestSubQueryTransformer 11 of 11. Start from: the head. Output: section "Head run" in the deliverable.

3. **Fail-before leg.** Create a detached worktree at `c3cdf7b46e8cfff3673f76d881f32cf8e7b00622`. Copy only `solr/core/src/test/org/apache/solr/response/transform/TestSubQueryTransformerDistrib.java` from the head over the base file. Copy nothing else from the head. Run `gradlew :solr:core:test --tests "org.apache.solr.response.transform.TestSubQueryTransformerDistrib"` with the seed from item 2. Expected: `testRowFieldNotInFlDistributed` fails, and the other test in the class passes. Record the failing test name and the first failure line, by content. Start from: the head. Output: section "Fail-before leg" in the deliverable.

4. **Format check.** At the head, run `gradlew :solr:core:spotlessJavaCheck`. This is a check only. Do not run `spotlessApply` on the fork branch. Record the result. If it fails, record the violating lines. Start from: the head. Output: section "Format" in the deliverable.

5. **Deliverable.** Write `reports/SOLR-9396-distrib-row-field-verify.md` with sections Head, Head run, Fail-before leg, Format, and Verdict. The verdict is one of PASS (head green, new test fails on base, format clean), NOT_PROVEN (new test passes on base), FAIL (head red, compile failure, or format failure), or INCONCLUSIVE (cluster startup noise). Commit the deliverable and this file's Status line set to DONE in one fast-forward push to `pr-prepare`. Read it back.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Head moved before item 2 | decided: stop and record the new tip | none | Main agent |
| Runner | decided: use the host's standard focused-test runner for the item 2 classes; if the host has none, run the item 2 gradlew command directly | none | Main agent |
| Seed | decided: reuse the seed from item 2 in item 3 | none | Main agent |
| New test fails at the head | decided: record the failure, verdict FAIL, do not relaunch; still run items 3 and 4 | none | Main agent investigates |
| New test passes on base | decided: record NOT_PROVEN; do not edit or weaken the test | none | Main agent decides the next step |
| Test file does not compile at the head | decided: record the compiler lines, verdict FAIL; skip items 2 and 3; still run item 4 | none | Main agent |
| Cluster startup fails before the assertions run | default: record it, verdict INCONCLUSIVE, do not relaunch | if it happens in item 2 or 3, skip the rest of that item and continue to item 4 | Main agent |
| Spotless check fails | decided: record the violating lines; do not apply formatting and do not commit it | none | Main agent applies any formatting fix |
| Any code or test change to get a pass | decided: none. This round changes no code and no tests | none | Main agent, with Nick |

## Deliverable

`reports/SOLR-9396-distrib-row-field-verify.md`: one section per item, with its status, the seed, the counts from the XML, the failure text by content, and the verdict. Commit as Nick Shanin with no trailers. Set this file's Status to DONE in the same push.

## Limits

- No PR, comment, JIRA or email. No force push. No receipt, and no change under `receipts/`.
- Do not edit main code or tests on the fork branch. The only commit is the deliverable on `pr-prepare`.
- Use your own worktree for this round. Do not use another session's checkout.
- Commit as Nick Shanin with no trailers. No em dashes.
