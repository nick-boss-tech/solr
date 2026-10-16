# Gate job: SOLR-9595 (live-head gate at defce50943)

Status: DONE

- Branch: solr-9595-submit. Head: `defce50943f21e1abcfa4bf6d3fe8e225eefd0ee` (fork tip, confirmed by ls-remote on 2026-10-16). If the tip has moved at claim time, stop and record the new tip here.
- Prior gated head: `e18a8063e6afcb85b67d2adc66a862104bedb258` (tree-identical to the gated head bf99cd0e89c255889b3d801e2ec863813dad78a7; see receipts/SOLR-9595.md). That evidence does not cover the one commit on top of it, defce50943, which this job gates. Merge-base with upstream/main: `cabedd1d968059215188f4e7563fb303241899ed`.
- Intended host: vm1 (Nick directed this run on 2026-10-15 evening).
- Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.index.TestSlowCompositeReaderWrapper` (package copied from the file on the branch). Seed: `9595C0FFEE9595`.
- Diff scope of defce50943 against e18a8063e6af: 2 files, +14/-10. `changelog/unreleased/SOLR-9595-cache-slow-reader-terms.yml` (the nick key under the author name dropped; the author name itself stays) and `solr/core/src/test/org/apache/solr/index/TestSlowCompositeReaderWrapper.java` (the caching test's segment layout pinned: a plain IndexWriter with auto flush disabled so only commits flush, DirectoryReader.open(w) in place of the random writer's getReader(), an exact two-leaf assertion, and one added live-docs assertion). No production file changes in this commit; the branch's production change (SlowCompositeReaderWrapper caching the merged Terms per field and building the live-docs Bits once in the constructor) is unchanged since the gated tree. Anything else in the tree is out of scope.
- Why: the head commit is ungated. This branch's first two gates failed at the test's own setup precondition before either caching assertion ran, and the correction that finally gated green rested on a setup assumption about segment layout (see the receipt's history). This commit replaces that setup with a deterministic layout, which is exactly the kind of change that needs its own run. Competition check: at pr-prepare tip da2dbfa8cb4e (2026-10-16), no file in claims/, assignments/ or gates/ names defce50943, no gate file names 9595, and the claims and assignments that mention 9595 are closed review and audit rounds that claim no gate and name no host for one. This claim is the first.
- Runner and logs on vm1: ~/workspace/tools/g9595-live-gate.sh, ~/workspace/tools/g9595-live-gate.log (head worktree ~/workspace/solr-worktrees/solr-9595-gate); the proof leg runs inside the same runner and log (proof worktree ~/workspace/solr-worktrees/solr-9595-proof). JUnit XML archived at ~/workspace/tools/g9595-live-proof-xml/ and ~/workspace/tools/g9595-live-head-xml/.
- Author context (relayed by the owner, 2026-10-16): the author of defce50943 never compiled or ran the commit. The test change uses IndexWriter and IndexWriterConfig calls the author had not built, and the author recorded that if one of them is wrong, the preflight fails before any test runs. A compile failure at step 2 is therefore a live possibility and, if it happens, is recorded as a defect in the new commit (failing step, exact error, file and line), not as an environment refusal and not as GATE INVALID.

## What to run

Use the host's standard gate runner (~/workspace/tools/solr-gradle.sh behind the blocking flock on ~/workspace/tools/.solr-test-queue.lock). Do not run the build tool directly. Check the worktree out at the head first.

- Step 0: changelog YAML parse of `changelog/unreleased/SOLR-9595-cache-slow-reader-terms.yml`.
- Step 1: tidy (`:solr:core:tidy`; every changed Java file on the branch is in :solr:core). If tidy changes any file (post-tidy status, with gradle/libs.versions.toml restored), the run is GATE INVALID and stops.
- Step 2: Error Prone compile: `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
- Step 3, proof leg: scratch worktree at the merge-base `cabedd1d9680` (base production) with the branch's test file at defce50943 copied in; run the focused class at the seed; count and read failures from the fresh JUnit XML. Expected, by content: exactly one failure in the class, `testTermsAndLiveDocsAreCached` failing at a caching assertSame (terms or liveDocs). Stated from the code before the run: at the merge-base, terms() returns `MultiTerms.getTerms(in, field)` new on every call (SlowCompositeReaderWrapper.java lines 155 to 157) and getLiveDocs() returns `MultiBits.getLiveDocs(in)` new on every call (lines 353 to 355), so two calls never return the same object. The pinned setup does not depend on the production change, so the exact two-leaf assertion passes on base, and the other two tests pass on base because the behavior they check (the core listener and the cached ordinal maps) predates the branch. Any other failure shape stops the job and follows the failure-route row.
- Step 4, focused head run: cleanTest first, then the focused class at the seed in the head worktree. Count from the fresh JUnit XML (expected 3 of 3). Nothing else runs on the worktree between the test run and the XML read.
- Step 5: module check `:solr:core:check -x test`.

A gate that executed tests and failed is not relaunched by the claimant. Record the failure here and follow the failure-route row.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Production change in the gated commit | decided: none; the diff was read in full (test file and changelog fragment only), so the proof leg is the receipt's original premise leg, run with the pinned test | none | main side brief, 2026-10-15 |
| Branch push | decided: none. The head is already the fork tip; do not edit the branch | none | main side brief, 2026-10-15 |
| Failure route | default: record the failing test, seed and reproduce line here and in the claim, mark the job accordingly, and stop | if tests executed and failed, no relaunch of the unchanged gate | WORKFLOW.md |

No row may leave the choice to the claimant.

## On green

1. Write `receipts/SOLR-9595.md` with a new top section: head defce50943, gate date and host, log path, step results, the proof-leg result (test name and failure text), and the focused counts from the fresh XML. Keep the earlier sections as history.
2. Append one line to the takeover log (main host file files/remaining-branches-takeover-log.md).
3. Mark this job DONE and the claim DONE in the same push as the receipt.

If any step cannot be verified, record it here and stop.

## Checklist

- [x] head confirmed by ls-remote
- [x] diff scope confirmed
- [x] step 0 changelog parse
- [x] step 1 tidy clean
- [x] step 2 Error Prone compile
- [x] step 3 proof leg, expected failure by content
- [x] step 4 focused head run, counted from fresh XML
- [x] step 5 module check
- [x] receipt written
- [x] job DONE, claim DONE

## Outcome (vm1, 2026-10-16): DONE, GATE GREEN at defce50943

- Log: ~/workspace/tools/g9595-live-gate.log on vm1 (GATE RESULT: GREEN, GATE RUNNER DONE). JUnit XML archived at ~/workspace/tools/g9595-live-head-xml/ and ~/workspace/tools/g9595-live-proof-xml/. Seed 9595C0FFEE9595.
- Steps: changelog YAML parse OK rc 0; tidy rc 0, post-tidy tree unchanged; Error Prone compile rc 0 (the author's never-compiled IndexWriter and IndexWriterConfig calls compile clean; the step 2 defect possibility recorded in this job did not materialize); focused head run from fresh JUnit XML: TestSlowCompositeReaderWrapper 3 of 3, 0 failures, 0 errors, 0 skipped; module check :solr:core:check -x test rc 0; post-gate tree clean.
- Proof leg at merge-base cabedd1d9680 production with the branch's pinned test file in place: 3 tests, exactly 1 failure, testTermsAndLiveDocsAreCached at the terms assertSame (TestSlowCompositeReaderWrapper.java:164), "expected same:<org.apache.lucene.index.MultiTerms@28fc6bff> was not:<org.apache.lucene.index.MultiTerms@42173c8a>". The exact two-leaf setup assertion passed on base and the other two tests passed, matching the expectation recorded in this job before the run; the premise discriminates with the pinned test.
- Receipt: receipts/SOLR-9595.md, new top section at defce50943. No branch push was needed; the head was already the fork tip and the branch was not edited.

## Deliverable and limits

- One fast-forward push with the receipt and the DONE marks.
- No PR opening or state change, no comment, no JIRA or email, no force push. Commit as Nick Shanin with no trailers. No em dashes.
