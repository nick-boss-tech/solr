# Gate job: SOLR-12505 FetchStream join field name escaping fix, gate at the new head

- Branch: solr-12505-submit. Head: 5f20e1171bc890509d76e4f5f7defcc4fc8c102c (live tip, verified by ls-remote at posting, 2026-10-11; GATE GREEN at this head per receipts/SOLR-12505.md). Merge-base with main: 14c7aac0d151402b00259e2fb9bf5eed7049ec5d.
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Windows never claims build work.
- Status: OPEN, unclaimed.
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

- [ ] fix implemented in FetchStream (escape or validate the `on=` field name; route and reason recorded here); test added to StreamDecoratorTest
- [ ] tidy on the final tree; tree clean (GATE INVALID guard)
- [ ] changelog parse
- [ ] Error Prone compile (:solr:solrj-streaming)
- [ ] proof leg: the new test on pre-fix head production (5f20e1171bc) fails for the premise reason, checked by content
- [ ] focused tests at the new head: StreamDecoratorTest, counted from fresh JUnit XML
- [ ] module check (:solr:solrj-streaming)
- [ ] pushed plainly to solr-12505-submit; receipt refreshed; draft updated to the new head; job and claim DONE
