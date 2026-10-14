# Gate job: SOLR-14231 fail-without-fix run for the tests the branch adds

- Branch: solr-14231-submit. Head: 593463f398cb96a50ea9ab4a70b26c0b9a5b1599 (live tip, verified by `git ls-remote origin refs/heads/solr-14231-submit` on 2026-10-13; receipt `receipts/SOLR-14231.md`, section "Re-gate at 593463f398", GATE GREEN at this head).
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Windows never claims build work.
- Status: OPEN, unclaimed. Posted 2026-10-13 by the review coordinator (W2 streaming, `reports/streaming-convergence-round-2-response.md`).
- Capability tags: gate.
- Job type: fail-before proof, a run only. No push, no branch change, no receipt "green" line. The gate records each new test's outcome on the proof base.
- Why owed: the branch adds tests, and WORKFLOW's fail-before rule asks that a new test fail without its fix. `pr-drafts/streaming/SOLR-14231.md` states "Fail without the fix: not run for these tests." That sentence stays true until this job reports.
- Modules: `:solr:solrj-streaming`.
- Focused classes at the head (packages read from the branch's test paths):
  - `org.apache.solr.client.solrj.io.graph.GraphExpressionTest`: the two new tests are `testScoreNodesNoNodes` (line 845) and `testScoreNodesStreamSingleNode` (line 854). The class runs whole; record all 8 outcomes.
  - `org.apache.solr.client.solrj.io.stream.ScoreNodesStreamTest`: the new test is `testSingleNodeTermsRequest` (line 46).
- Seed: `14231C0FFEE14231`.
- Proof base: merge-base of the head with `upstream/main`, `b5c71bc5573c4e31b4cee5a7965d73587fc0ae58` (verified with `git merge-base` on 2026-10-13). The production change on the branch is `solr/solrj-streaming/src/java/org/apache/solr/client/solrj/io/stream/ScoreNodesStream.java` only (`git diff --stat b5c71bc5573 593463f398` shows that file, the two focused test files and the changelog fragment).
- Proof shape: revert `ScoreNodesStream.java` to `b5c71bc5573` in a throwaway worktree, keep the branch's two focused test files and the changelog as they are at the head, run the two focused classes with the seed, and count from fresh JUnit XML. Each new test is recorded as one of:
  - FAILS on the base: the failure line and message are quoted in this file. This proves the test catches the fix.
  - PASSES on the base: recorded as a regression guard, not as proof of the fix. The review coordinator then rewrites the draft's Proof sentence to say so.
- Expected from the draft's reading of the base code (to be confirmed or corrected by the run, not assumed): `testSingleNodeTermsRequest` fails on the base, because the base sets the collection only from the second node and sends a GET.
- Gate steps:
  1. Confirm the head with `git ls-remote` and the proof base with `git merge-base`; record both here.
  2. Throwaway worktree at the head. Revert only `ScoreNodesStream.java` to the proof base. `git diff --stat` against the head must show that file alone.
  3. Focused run of `GraphExpressionTest` and `ScoreNodesStreamTest` with `-Ptests.seed=14231C0FFEE14231`.
  4. Counted results from fresh JUnit XML: tests, failures, errors, skipped for each class, and the per-test outcome for the three new tests.
  5. Record the failure line and message for each failing new test, and whether the failure is the one the title describes (no node, single node, or GET).
  6. Discard the throwaway worktree. The branch is not changed.
- On completion: this file gets the results and a DONE mark. The gating host adds a line to `receipts/SOLR-14231.md` naming the proof base, the seed, the per-test outcomes and the date (UTC and MDT). The review coordinator then updates the Proof sentence in `pr-drafts/streaming/SOLR-14231.md` from the recorded outcomes, and the owner decides opening.

## Step checklist

- [ ] head and proof base confirmed by ls-remote and git merge-base, recorded here
- [ ] throwaway worktree; only `ScoreNodesStream.java` reverted; diff checked
- [ ] focused run: GraphExpressionTest and ScoreNodesStreamTest, seed 14231C0FFEE14231
- [ ] per-test outcomes counted from fresh JUnit XML; failure lines quoted for failing new tests
- [ ] receipt line added to receipts/SOLR-14231.md; draft Proof sentence updated from the outcomes
- [ ] throwaway worktree removed; branch unchanged; job DONE
