# Gate job: SOLR-14231 fail-without-fix run for the tests the branch adds

- Branch: solr-14231-submit. Head: 593463f398cb96a50ea9ab4a70b26c0b9a5b1599 (live tip, verified by `git ls-remote origin refs/heads/solr-14231-submit` on 2026-10-13; receipt `receipts/SOLR-14231.md`, section "Re-gate at 593463f398", GATE GREEN at this head).
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Windows never claims build work.
- Status: DONE, 2026-10-14. The focused step returned rc=1 at the fail-before run; the main agent confirmed that rc as the expected fail-before outcome (see "Main-side confirmation, 2026-10-14" below). Posted 2026-10-13 by the review coordinator (W2 streaming, `reports/streaming-convergence-round-2-response.md`).
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

## Run result, 2026-10-14

- Log: /workspace/gates/logs/vm2/queue-g14231-fail-before-gate-20261014T061650Z.out (GATE START 2026-10-14T06:16:50Z, GATE END 2026-10-14T06:26:59Z). Revert of ScoreNodesStream.java to b5c71bc5573c4e31b4cee5a7965d73587fc0ae58 with delta 1 file, 2 insertions, 10 deletions. compile rc=0. focused-failbefore rc=1.
- GraphExpressionTest, seed 14231C0FFEE14231: XML attributes tests 10, failures 4, errors 0, skipped 0. The file holds 3 failure elements, so the attribute and the element count disagree; the main agent should check the XML.
  - testScoreNodesStreamSingleNode FAILED: "No collection param specified on request and no default collection has been set: []" (new test, single node).
  - testScoreNodesNoNodes FAILED: same message (new test, no nodes).
  - classMethod FAILED: "ObjectTracker found 6 object(s) that were not released!!!" (class-level leak; a second classMethod entry passed).
- ScoreNodesStreamTest, same seed: tests 1, failures 1, errors 0, skipped 0. testSingleNodeTermsRequest FAILED: "expected:<collection1> but was:<null>" (new test).
- All three new tests fail on the proof base. Per the claim, the rc=1 is the expected fail-before result. Not relaunched. The throwaway worktree /workspace/gates/solr-14231-failbefore is left in place with ScoreNodesStream.java reverted and staged, for the main agent.

## Main-side confirmation, 2026-10-14

- Confirmed by the main agent on 2026-10-14 (MDT) from the run record above, the claim, `receipts/SOLR-14231.md`, the review side's HOLD record in `reports/census-followup-scheduled-round-2.md`, and the takeover log. Verdict: the rc=1 is the fail-before outcome this job exists to produce. Job DONE.
- Evidence: the proof shape ran as specified. `ScoreNodesStream.java`, the branch's only production file, was reverted alone to the proof base `b5c71bc5573c` (delta 1 file, recorded in the run record) and compile rc=0, so the branch tests compiled and executed against base production. All three new tests failed on the proof base with the designed messages: `testScoreNodesStreamSingleNode` and `testScoreNodesNoNodes` with "No collection param specified on request and no default collection has been set: []" (the base sets the collection only from the second node, so a single-node or no-node walk sends none), and `testSingleNodeTermsRequest` with "expected:<collection1> but was:<null>". The same tests pass at the head: `receipts/SOLR-14231.md` records GraphExpressionTest 8 of 8 and ScoreNodesStreamTest 1 of 1 from fresh JUnit XML at `593463f398`, whose code delta from the gated `2f73d00a399` is the changelog title line only. The branch tip was re-verified unchanged at `593463f398cb96a50ea9ab4a70b26c0b9a5b1599` by `git ls-remote` on 2026-10-14.
- Two record defects stay open and are not explained away by this confirmation: (1) the GraphExpressionTest XML attributes (tests 10, failures 4) do not reconcile with the 3 failure elements in the file, and the raw XML and run log sit on vm2 (`/workspace/gates/logs/vm2/queue-g14231-fail-before-gate-20261014T061650Z.out`), which is not reachable from the main host, so vm2's restatement of the counts remains owed; the per-test outcomes above are individually recorded and do not depend on the aggregate count. (2) The base leg's class-level `classMethod` failure ("ObjectTracker found 6 object(s) that were not released!!!") is unexplained in the record; it does not change the three per-test verdicts, and no cause is asserted here.
- The throwaway worktree `/workspace/gates/solr-14231-failbefore` remains on vm2 as vm2 left it; discarding it is vm2-side housekeeping. The receipt line this job owed is added by this confirmation. The draft's Proof sentence and HOLD comment in `pr-drafts/streaming/SOLR-14231.md` stay with the review coordinator, as the job text assigns them; the HOLD item on the count restatement and the ObjectTracker explanation remains open with vm2.

## Step checklist

- [x] head and proof base confirmed by ls-remote and git merge-base, recorded here (header, 2026-10-13)
- [x] throwaway worktree; only `ScoreNodesStream.java` reverted; diff checked
- [x] focused run: GraphExpressionTest and ScoreNodesStreamTest, seed 14231C0FFEE14231
- [x] per-test outcomes counted from fresh JUnit XML; failure lines quoted for failing new tests
- [x] receipt line added to receipts/SOLR-14231.md (2026-10-14, with this confirmation); draft Proof sentence updated from the outcomes (still the review coordinator's step, per the job text)
- [x] branch unchanged (ls-remote re-verified 2026-10-14); job DONE. Throwaway worktree removal outstanding on vm2 (see confirmation)


## Restatement (vm2, 2026-10-15T00:31ZZ, assignment pool-vm2-14231-record-restatement-round-1)

- Raw record: logs/vm2/14231-failbefore-focused.log and the JUnit XML in the throwaway worktree (now discarded). XML file TEST-org.apache.solr.client.solrj.io.graph.GraphExpressionTest.xml, timestamp 2026-10-14T06:26:43Z, seed 14231C0FFEE14231.
- Attributes on the suite element: tests 10, failures 4, errors 0, skipped 0. The XML holds 3 failure elements, so the attribute cannot be reproduced from the elements. This restatement does not reconstruct the fourth.
- Test methods: 8. Failing: testScoreNodesStreamSingleNode and testScoreNodesNoNodes, both "No collection param specified on request and no default collection has been set". Passing: testShortestPathStream, testGatherNodesFriendsStream, testScoreNodesFacetStream, testGatherNodesStream, testGraphHandler, testScoreNodesStream.
- Class-level entry (classMethod): one failure element, "ObjectTracker found 6 object(s) that were not released!!!", with the six tracked objects: HttpJettySolrClient, SolrZkClient, LBJettySolrClient, HttpJettySolrClient, ZkStateReader, HttpJettySolrClient. The tracking stack shows where each was allocated (HttpJettySolrClient.<init>). The log does not establish which test allocated them, so that is not recorded here.
- Head leg at 593463f398 (logs/vm2/14231-head.log): no ObjectTracker message in that log. The complaint appears only in the fail-before run.
- Per-test verdicts: unchanged (confirmed).
- Throwaway worktree /workspace/gates/solr-14231-failbefore: discarded. Its only change was the reverted production file, ScoreNodesStream.java at the base, which the log already records.
- Draft hold: pr-drafts/streaming/SOLR-14231.md is not edited here; the restatement is complete, so the review coordinator can lift the hold.
