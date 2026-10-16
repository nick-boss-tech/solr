# Gate job: SOLR-8240 (test fix for the initParams rejection test)

Status: CLAIMED (claims/pool-vm1-solr-8240-head-failure-round-1.md)

- Branch: solr-8240-submit. Head: `78ecddf01f5c76736d415b9d031095f22aa38b17` (committed on vm1, not yet pushed; the fork tip stays at `97cd22159ba0d2dd2de9f8a5bdb52de93bd2bffa` until this gate is green and the push step runs).
- Prior gated head: `fad7a1dd8e2397330e0973e809aeba60490d2f08`. Merge-base with upstream/main: `e432df19c4a50b9d54d7fba545397b859cc54f98`.
- Intended host: vm1, the fix lane under assignments/pool-vm1-solr-8240-head-failure-round-1.md (main-agent decision of 2026-10-16T02:19Z in that file).
- Capability tags: gate.
- Module: `:solr:core`. Focused classes: `org.apache.solr.core.TestInitParams` and `org.apache.solr.handler.JsonLoaderTest`. Seed: `8240C0FFEE824001`.
- Diff scope against `97cd22159ba0d2dd2de9f8a5bdb52de93bd2bffa`: 1 file, `solr/core/src/test/org/apache/solr/core/TestInitParams.java`, 9 insertions, 8 deletions, test code only. Production is byte-identical to `97cd22159b`. Anything else in the tree is out of scope.
- Why: the gate at `97cd22159b` failed at the head (see gates/SOLR-8240.md). The vm1 investigation in that file's "Head-failure investigation" section established that production at `97cd22159b` is correct and the new test was defective in two independent parts: it wrapped `handler.handleRequest(...)` in `expectThrows`, but `RequestHandlerBase.handleRequest` records handler exceptions on the response instead of throwing, and its two mid-class `initCore` swaps leaked the replaced harnesses (147 unreleased objects at classMethod). This head fixes both defects in the test only: the named test asserts on the response exception, and the test closes each swapped-out harness with `deleteCore()` so every harness the class creates is accounted for at teardown. Both solrconfig variants stay exercised.

## What to run

Use the vm1 gate runner behind the blocking flock on the test queue (`~/workspace/tools/.solr-test-queue.lock`); builds go through `~/workspace/tools/solr-gradle.sh`, never `./gradlew`. Log: `~/workspace/tools/g8240-fix-gate.log` on vm1. Runner: `~/workspace/tools/g8240-fix-gate.sh`. Worktree: `~/workspace/solr-worktrees/solr-8240-fix`, checked out at the head above.

- Step 0: changelog YAML parse of `changelog/unreleased/SOLR-8240-map-unique-key-only-f-conflict.yml` (unchanged by this commit; confirms the head still parses).
- Step 1: tidy at the head. If tidy changes any file (after restoring `gradle/libs.versions.toml`), the run is GATE INVALID and stops.
- Step 2: Error Prone compile: `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
- Step 3, focused head run: clean the module test results, then `:solr:core:test --tests org.apache.solr.core.TestInitParams --tests org.apache.solr.handler.JsonLoaderTest -Ptests.seed=8240C0FFEE824001`. Count from the fresh JUnit XML files `TEST-org.apache.solr.core.TestInitParams.xml` and `TEST-org.apache.solr.handler.JsonLoaderTest.xml`. Nothing else runs on the worktree between the test run and the XML read. Expected: TestInitParams fully green, including the classMethod check, with no ObjectTracker leak; JsonLoaderTest 33 of 33. Record the actual counts.
- Step 4: module check: `:solr:core:check -x test`.

Proof shape: this gate has no reverted-production leg. The corrected assertion fails on the pre-fix test code by construction of the old assertion shape: `expectThrows` around `handler.handleRequest` can never observe the rejection, because the handler contract records the exception on the response; that is the recorded failure at both the merge-base and the old head (gates/SOLR-8240.md, investigation section). What this gate proves is exact and narrower: the fixed test class is green at the new head with production unchanged from `97cd22159b`, the response-exception assertion observes the 400 the head produces (the investigation's scratch probe passed with exactly this assertion shape and production untouched), and the class no longer leaks harnesses.

A gate that executed tests and failed is not relaunched by the claimant. Record the failure here and follow the failure-route row.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Seed | decided: `8240C0FFEE824001` for both focused classes | none | Gate runner |
| Tidy | decided: any file changed by tidy makes the run GATE INVALID | if tidy changes a file, record the diff here, restore the tree, and stop | Gate runner |
| Failed head run | default: a run that executed tests and failed is recorded here (test name, failure text, seed, reproduce line) and not relaunched | if the head run fails, record it here and in the claim, leave the branch unpushed, and stop | Main agent |
| Fork push | decided: on green only, push `78ecddf01f` to fork `solr-8240-submit` as a plain fast-forward and verify by ls-remote | if the gate is not green, no push | Main agent decision in the assignment |
| Public text | decided: this job makes no public edits; the main agent updates the PR body and draft after the receipt lands | none | Main agent |

No row may leave the choice to the claimant.

## On green

1. Push the fix commit to fork `solr-8240-submit` (plain fast-forward), verified by ls-remote.
2. Write `receipts/SOLR-8240.md` with a new top section: head `78ecddf01f`, gate date and host, log path, step results, the proof-shape statement above, and the focused counts from the fresh XML.
3. Mark this job DONE, mark assignments/pool-vm1-solr-8240-head-failure-round-1.md DONE, and mark both claims (claims/pool-vm1-solr-8240-head-failure-round-1.md and claims/pool-vm2-gate-backlog-8240.md) DONE naming the new head, in the same push sequence as the receipt.

If any step cannot be verified, record it here and stop.

## Checklist

- [ ] head confirmed: local commit `78ecddf01f` on top of `97cd22159b`, fork tip unchanged until the push step
- [ ] diff scope confirmed: 1 file, test only, against `97cd22159b`
- [ ] step 0 changelog parse
- [ ] step 1 tidy clean (GATE INVALID guard)
- [ ] step 2 Error Prone compile
- [ ] step 3 focused head run, counted from fresh XML
- [ ] step 4 module check
- [ ] branch pushed on green, verified by ls-remote
- [ ] receipt written
- [ ] job DONE, assignment DONE, both claims DONE

## Deliverable and limits

- The branch push on green, plus one fast-forward push of pr-prepare with the receipt and the DONE marks.
- No PR opening or state change, no comment, no JIRA or email, no force push. Commit as Nick Shanin with no trailers. No em dashes.
