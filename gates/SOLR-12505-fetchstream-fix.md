# Gate job: SOLR-12505 FetchStream join field name escaping fix, gate at the new head

- Branch: solr-12505-submit. Head: 5f20e1171bc890509d76e4f5f7defcc4fc8c102c (live tip, verified by ls-remote at posting, 2026-10-11; GATE GREEN at this head per receipts/SOLR-12505.md). Merge-base with main: 14c7aac0d151402b00259e2fb9bf5eed7049ec5d.
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Windows never claims build work.
- Status: DONE 2026-10-13T16:49Z by vm2 (gate backlog coordinator). GATE GREEN at 1a1875d95f3680c20447006efc3c949c57f5135f (GATE END 2026-10-13T16:48:06Z; logs/vm2/queue-g12505-full-gate-*.out). Route validate. Receipt: receipts/SOLR-12505.md, section 'Gate at 1a1875d95f'. Draft: pr-drafts/streaming/SOLR-12505.md at the new head. Claim: claims/pool-vm2-gate-backlog-12505.md. Previous status: CLAIMED at 1a1875d95f after the round 3 proof-leg classification as a test-fixture defect.
- Capability tags: gate.
- Job type: branch-side fix on a gated branch, then the full hardening gate at the new head. Claimable end to end by vm1 or vm2 under the lane coordinators' authorization.
- Spec: reports/streaming-convergence-round-1-response.md, item ST-R1-23 (verified there against the head code), with the register text in reports/streaming-convergence-round-1.md. The draft pr-drafts/streaming/SOLR-12505.md stays NOT CONVERGED until this job lands; the streaming convergence round 2 assignment re-checks the draft against the new head.
- The defect: in `solr/solrj-streaming/src/java/org/apache/solr/client/solrj/io/stream/FetchStream.java` at the head above, line 239 builds the fetch batch as `buf.append("{! df=").append(rightKey).append(" q.op=OR cache=false }")`. `rightKey` is the join field name taken from the expression's `on=` parameter, and it is appended unescaped, while the key values appended below it go through `ClientUtils.escapeQueryChars`. A join field name containing a space, a closing brace, or other local-params syntax breaks out of the `df=` value and corrupts the batch query.
- The fix (adopted recommendation, ST-R1-23): escape or validate the `on=` field name in FetchStream so it cannot break out of the `df=` value. The claimant picks one route (escape the field name the way the key values are escaped, or validate it as a field name and fail with a clear error), and records the choice and its reason in this file and in the receipt. Add a test to `org.apache.solr.client.solrj.io.stream.StreamDecoratorTest`, the class that carries this branch's fetch tests, showing that a join field name with such a character no longer corrupts the batch: the fetched fields come back (escape route) or the expression fails with the clear validation error (validate route). No other production change is part of this job; stop and record here if the fix seems to need one.
- Proof shape: with FetchStream.java reverted to the pre-fix head 5f20e1171bc (the branch tests, including the new one, kept), the new test must fail for the premise reason (the corrupted batch), checked by failure content, not count alone. The defType proof from the original gate stands as recorded in receipts/SOLR-12505.md and is not re-run.
- Modules: :solr:solrj-streaming. Focused class at the new head: org.apache.solr.client.solrj.io.stream.StreamDecoratorTest (the full class, counted from fresh JUnit XML; the receipt's counts at the pre-fix head are 56 tests, 5 skipped, 0 failures, and the new test adds to that count). The claimant records the seed used in this file.
- Gate steps: changelog YAML parse (`changelog/unreleased/SOLR-12505-fetch-stream-deftype.yml`, unchanged by this job, parsed as part of the gate); tidy with the GATE INVALID guard (restore gradle/libs.versions.toml afterward; re-run tidy on the final tree after the last edit, before committing); Error Prone compile for :solr:solrj-streaming with `-Pvalidation.errorprone=true`; proof leg as above; focused tests; module check `:solr:solrj-streaming:check -x test`.
- Push: the fix commits push plainly (fast-forward) to solr-12505-submit after the gate is green at the committed tree.
- On green: refresh receipts/SOLR-12505.md at the new head (the fix and the route chosen, the new counts from fresh JUnit XML, the proof result). Update pr-drafts/streaming/SOLR-12505.md to the new head: re-point the head citations (the FetchStream and StreamDecoratorTest links and the changelog link) to the new head with line numbers verified at the new head, update the Proof head and counts from the new run, and add a sentence to "What this change does" recording that the join field name is now escaped or validated, matching the route implemented, so the convergence round 2 re-check has the current text. Mark this job DONE and the claim DONE.

## Step checklist

- [x] fix implemented in FetchStream (escape or validate the `on=` field name; route and reason recorded here); test added to StreamDecoratorTest
- [x] tidy on the final tree; tree clean (GATE INVALID guard)
- [x] changelog parse
- [x] Error Prone compile (:solr:solrj-streaming)
- [x] proof leg: the new test on pre-fix head production (5f20e1171bc) fails for the premise reason, checked by content
- [x] focused tests at the new head: StreamDecoratorTest, counted from fresh JUnit XML
- [x] module check (:solr:solrj-streaming)
- [x] pushed plainly to solr-12505-submit; receipt refreshed; draft updated to the new head; job and claim DONE

## Amendment 1 record (vm2)

- Route: validate. FetchStream (commit d81a773117) rejects an `on=` join field name containing whitespace, braces, `!`, quotes or backslashes, and fails with an IOException naming the field. Reason: escaping would let the batch query accept a field name that is almost certainly not in the schema, while validating fails at construction with a message that says what is wrong.
- Construction error cause: StreamFactory.constructStream (solrj-streaming expr/StreamFactory.java, the ctor.newInstance wrap around lines 593 to 611) wraps a constructor's IOException in an InvocationTargetException with no message, so the outer message is "Unable to construct instance of ...FetchStream" and the validation text sits on the cause chain. The head run failed because the test checked only the outer message. The test now checks the cause chain (causeChainContains). StreamFactory is unchanged. This is production behaviour outside the FetchStream fix; it loses the reason for every stream constructor and is recorded here for the owner, not fixed in this job.
- Compile leg cause: the assertThrows lambda held several statements (AssertThrowsMultipleStatements). The stream setup and drain now sit in drainFetchStream, so each lambda is one call.
- Proof leg, seed, counts: recorded when the gate at ee67d37421 completes.

## Trigger input for the re-run proof leg (round 3, amendment 2)

- Test: StreamDecoratorTest.testFetchStreamRejectsJoinFieldNameThatBreaksLocalParams at ee67d374219275907ef1b78e3d2b455b6a15e43f (StreamDecoratorTest.java lines 1009 to 1030).
- Exact trigger: the expression `fetch(COLLECTION, search(COLLECTION, q=*:*, fl="id,a_s,a_i", sort="a_i asc"), on="id=a i", batchSize="2", fl="subject")`, so the join field name is `a i`, with a space between the two parts (on= value `id=a i`).
- Assertion: assertThrows(IOException) on drain, then causeChainContains(e, "invalid join field name [a i]").
- Proof seed: 12505C0FFEE12505. Runner: gates/g12505-proof-gate.sh (proof leg only; FetchStream.java reverted to 5f20e1171bc890509d76e4f5f7defcc4fc8c102c).

## Round 3 proof-leg record (vm2, 2026-10-13)

- Run: proof leg only, at ee67d374219275907ef1b78e3d2b455b6a15e43f, per amendment 2. FetchStream.java reverted to 5f20e1171bc890509d76e4f5f7defcc4fc8c102c, branch tests kept. Seed 12505C0FFEE12505. GATE START 2026-10-13T15:45:18Z, GATE END 2026-10-13T15:47:27Z. Runner gates/g12505-proof-gate.sh. Logs: logs/vm2/queue-g12505-proof-gate-20261013T154518Z.out and logs/vm2/12505-proof-amend2.log.
- Runner note: the runner's live-tip check failed (`fatal: 'origin' does not appear to be a git repository`), so the proof ran on the gated head it was given, without the live-tip read.
- Result: proof rc=1. Failing test StreamDecoratorTest.testFetchStreamRejectsJoinFieldNameThatBreaksLocalParams with "java.lang.AssertionError: expected java.io.IOException to be thrown, but nothing was thrown". This is the same failure content as the earlier ee67d37421 proof leg that amendment 2 rejected.
- Classification against amendment 2: matches neither Content 1 (a server parse or parser error with no validation text, failing the cause-chain assertion) nor Content 2 (a completed drain with corrupted fetched fields). It is the "nothing thrown" ending, which amendment 2 says cannot establish the premise.
- Premise note for the ruling: the gate test's trigger is on="id=a i" (join field name `a i`). The amendment 2 probe's breakout inputs (on=<id=a_i a_s> and the brace and bang inputs) each threw on pre-fix code. The gate test's exact input did not throw on pre-fix code. The difference between the test's trigger and the probe's inputs is the first thing to settle.
- Not done, per amendment 2 item 2: no fork push (solr-12505-submit untouched), no receipt refresh, no draft update. Job and claim are not DONE.
- Next step (owned by vm2 as gate coordinator, on its next idle pass): run a scratch probe at the pre-fix head 5f20e1171bc with the gate test's exact expression and fixture, and classify the result as Content 1 or Content 2. If the probe shows the test trigger does not reach the batch (a test-fixture defect), make the narrow trigger fix on the branch, confirm the fixed test fails at base for the premise reason, and re-claim the proof leg at the new head. Escalate to the main side only if the probe result still fits neither content. vm2 does not relaunch this proof leg until the probe is recorded here.

## Proof-leg re-run result (round 3, amendment 2)

- Run: GATE START 2026-10-13T15:45:18Z, log logs/vm2/queue-g12505-proof-gate-20261013T154518Z.out (Gradle log logs/vm2/12505-proof-amend2.log). Head ee67d374219275907ef1b78e3d2b455b6a15e43f, FetchStream.java reverted to 5f20e1171bc890509d76e4f5f7defcc4fc8c102c, branch tests kept, seed 12505C0FFEE12505.
- Result: the proof leg failed with "java.lang.AssertionError: expected java.io.IOException to be thrown, but nothing was thrown" at StreamDecoratorTest.java line 1026 (assertThrows). Counts: the one test failed, 0 passed for the test; no other test in the class ran.
- Classification: matches neither Content 1 (a server error without the validation text) nor Content 2 (a corrupted batch with a wrong tuple field). It is the same failure content as the earlier gate at ee67d37421, which amendment 2 says cannot prove the premise.
- Stopped per amendment 2 ("If it matches neither, record that and stop"). Not relaunched. No fork push. Gate not GREEN; receipt, draft and DONE marks not written.
- Runner note: the head check in g12505-proof-gate.sh failed (ls-remote origin is not valid inside the gate worktree), so the live head was not confirmed by that run; the head was checked before the run.
- Left for the main side: the premise check needs a trigger that throws on pre-fix FetchStream, or a different proof shape.

## Round 3 investigation and fixture fix (vm2, 2026-10-13)

- Classification: test-fixture defect. The new test testFetchStreamRejectsJoinFieldNameThatBreaksLocalParams indexed no documents (the class's @Before only deletes them), so the wrapped search returned zero tuples, no batch was sent, and the drain completed. That is the empty-batch ending from amendment 2, not a production result.
- Probe (scratch worktree at ee67d37421 with FetchStream reverted to 5f20e1171bc, not committed): the exact expression with on="id=a i" drained to tuples=[] with the wrapped search also empty (PROBE wrapped tuples=[]). Log: logs/vm2/queue-g12505-probe-20261013T162147Z.out; XML system-out in the probe worktree's module build dir.
- Fix (narrow, test-only): seed two documents (ids 0 and 1, with a_s and a_i) at the start of the new test, commit 1a1875d95f. No production change.
- Fork push: solr-12505-submit fast-forwarded from 5f20e1171b to 1a1875d95f, carrying the three branch commits d81a773117 (validate route), ee67d37421 (cause-chain test) and 1a1875d95f (fixture). Push done under step 2(a) of the coordinator run, before the full gate at the new head.
- Base check of the fixed test (FetchStream reverted to 5f20e1171bc, seed 12505C0FFEE12505, logs/vm2/queue-g12505-basecheck-20261013T162852Z.out): the test fails with the drain throwing java.io.IOException "--> ...solr: invalid query parser 'i' for query '{! df=a i q.op=OR cache=false } 0 1'", and the failing assertion is the cause-chain check for "invalid join field name [a i]". That is Content 1 of amendment 2: the pre-fix drain throws a server parser error without the validation text. The premise holds at base for the premise reason.
- Route and reason are unchanged from amendment 1 (validate). Next: full hardening gate at 1a1875d95f, with the proof leg as amendment 2 specifies (reverted FetchStream, branch tests kept).

## Full gate at 1a1875d95f3680c20447006efc3c949c57f5135f, 2026-10-13 (vm2)

- changelog YAML parse rc=0; tidy rc=0, status clean afterward; Error Prone compile rc=0; proof leg rc=1 (Content 1, fresh XML at 16:43:29Z); focused StreamDecoratorTest 57 tests, 5 skipped, 0 failures (fresh XML at 16:45:27Z, seed 12505C0FFEE12505); module check rc=0.
- Fork push: solr-12505-submit fast-forwarded from 5f20e1171b to 1a1875d95f3680c20447006efc3c949c57f5135f before the gate, under the coordinator's step 2(a) rule for a test-fixture defect. The gate ran at the pushed head.
