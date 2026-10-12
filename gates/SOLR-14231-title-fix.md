# Gate job: SOLR-14231 changelog title correction, re-gate at the new head

- Branch: solr-14231-submit. Head: 2f73d00a3996da1623156ccef649b59be0b59d45 (live tip, verified by ls-remote at posting, 2026-10-11; GATE GREEN at this head per receipts/SOLR-14231.md).
- Intended host: any capable Linux host, under the WORKFLOW.md claim rules. Windows never claims build work.
- Status: DONE. Title fix pushed at 593463f398 (fast-forward from 2f73d00a399); re-gate green, see receipts/SOLR-14231.md.
- Capability tags: gate.
- Job type: branch-side changelog title correction on a gated branch, then a re-gate at the new head. Claimable end to end by vm1 or vm2 under the lane coordinators' authorization.
- Spec: reports/streaming-convergence-round-1-response.md, item ST-R1-33 (verified there: the title at the head above claims the single-node and no-node failure, and no base run exists, so that failure claim is unshown).
- The fix: in `changelog/unreleased/SOLR-14231.yml` at the head above, the title says the scoreNodes streaming expression "no longer fails when it receives a single node or no nodes, and sends its /terms request as a POST". Replace the title with exactly: "scoreNodes streaming expression now sets its field and collection from every node, returns no tuples when there are no nodes, and sends its /terms request as a POST." Keep the fragment's YAML shape (a plain scalar title, as it stands); the parse step below verifies it. No other file changes: verify with `git diff` against 2f73d00a399 that the delta is this one fragment only, and stop and record here if anything else differs.
- Proof shape: none owed. The delta touches no production or test code, so the gate evidence at 2f73d00a399 carries over the title-only commit (the SOLR-16630 precedent: re-gate green at the new head, delta changelog-only). The focused runs below still re-run at the new head and are counted from fresh JUnit XML.
- Modules: :solr:solrj-streaming. Focused classes at the new head: org.apache.solr.client.solrj.io.graph.GraphExpressionTest and org.apache.solr.client.solrj.io.stream.ScoreNodesStreamTest (packages read from the branch's test files by grep, per the FQCN rule; the receipt's counts at the gated head are GraphExpressionTest 8 of 8 and ScoreNodesStreamTest 1 of 1).
- Gate steps: changelog YAML parse of the edited fragment with a real YAML parser; tidy with the GATE INVALID guard (restore gradle/libs.versions.toml afterward; any tidy change to another file invalidates the delta check above); Error Prone compile for :solr:solrj-streaming with `-Pvalidation.errorprone=true`; focused tests; module check `:solr:solrj-streaming:check -x test`.
- Push: the title commit pushes plainly (fast-forward) to solr-14231-submit after the gate is green at the committed tree.
- On green: refresh receipts/SOLR-14231.md with the new head and a line recording that the delta from 2f73d00a399 is the ST-R1-33 title correction only, with the focused counts from fresh JUnit XML. Re-point the changelog link in pr-drafts/streaming/SOLR-14231.md to the new head. Mark this job DONE and the claim DONE.

## Step checklist

- [ ] title replaced verbatim in SOLR-14231.yml; delta against 2f73d00a399 is that fragment only
- [ ] changelog YAML parse
- [ ] tidy on the final tree; tree clean (GATE INVALID guard)
- [ ] Error Prone compile (:solr:solrj-streaming)
- [ ] focused tests at the new head: GraphExpressionTest and ScoreNodesStreamTest, counted from fresh JUnit XML
- [ ] module check (:solr:solrj-streaming)
- [ ] pushed plainly to solr-14231-submit; receipt refreshed; draft changelog link re-pointed; job and claim DONE
