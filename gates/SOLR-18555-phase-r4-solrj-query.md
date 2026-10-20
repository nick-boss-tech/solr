# Gate job: SOLR-18555 (SolrJ HTTP client support for V2 QUERY requests)

Status: OPEN

- Branch: `solr-18555-query-client-support`. Head: `0c6aae3162f29a7ae2f62fb226cefdf9c365dd63` (fork tip, confirmed by ls-remote on 2026-10-20). If the tip has moved at claim time, stop and record the new tip here.
- Base: `7c859a4d0eeae897542e21046e059f6fca45a6e8`. Prior gated head: none recorded.
- Gate ID: `SOLR-18555-phase-r4-solrj-query`. Claim file: `claims/pool-<host>-gate-SOLR-18555-phase-r4-solrj-query.md`.
- Intended host: any capable Linux host. WORKFLOW.md lets any capable Linux host claim it if that host is busy.
- Capability tags: gate.
- Module: `:solr:solrj-jetty`. Focused class: `org.apache.solr.client.solrj.jetty.HttpJettySolrClientTest`. Seed: `18555C0FFEE18555`.
- Diff scope: 6 files, 101 insertions and 10 deletions against 7c859a4d0eeae897542e21046e059f6fca45a6e8: SolrRequest.java, HttpJdkSolrClient.java, HttpJettySolrClient.java, HttpSolrClientTestBase.java, HttpJettySolrClientTest.java, and highlighting.adoc.
- Why: This child branch extends the V2 QUERY feasibility work with body-bearing QUERY support in the JDK and Jetty SolrJ HTTP clients. The focused class tests the Jetty client and includes a direct JDK client case that checks the method, text/plain body, and query parameters. QUERY remains opted in by the V2 handler API spec and rejected on legacy routes.

## What to run

Use the host's standard gate runner. Do not run the build tool directly. Check the worktree out at the head first.

- Step 0: changelog check. The branch must add no changelog fragment; if it adds one, record that here and stop.
- Step 1: tidy. If tidy changes any file, apply the tidy output, record its patch and the tidied tree sha in this job, and continue from step 2. WORKFLOW.md, Gate repairs.
- Step 2: Error Prone compile: `:solr:solrj-jetty:compileJava :solr:solrj-jetty:compileTestJava -Pvalidation.errorprone=true`.
- Step 3, proof leg: none requested. Record that in the receipt.
- Step 4, focused head run: `:solr:solrj-jetty:cleanTest` first, then `:solr:solrj-jetty:test --tests org.apache.solr.client.solrj.jetty.HttpJettySolrClientTest -Ptests.seed=18555C0FFEE18555`. Count from the fresh JUnit XML. Nothing else runs on the worktree between the test run and the XML read.
- Step 5: module check `:solr:solrj-jetty:check -x test`.

Failures follow the rows below and WORKFLOW.md, Gate repairs. A failed retry hands the job back.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Tidy changes any file | decided: repair | Apply the tidy output on the gate worktree, record its patch and the tidied tree sha here, and continue from step 2. WORKFLOW.md, Gate repairs. | Nick Shanin (standing pool rule) |
| Failure in repair scope (compile, changelog, lint, or up to two failing tests) | decided: repair, then one retry | Make the repair as one commit and push it per WORKFLOW.md. Record the commit, its files, and the failing test or task here, then rerun from step 0 on the new head. Do not rerun an unchanged tree. | Nick Shanin (standing pool rule) |
| Failure outside repair scope, or any failure after the retry | decided: hand back | Set Status to HANDED BACK with the reason. Record the failing test or task, its failure text, the seed, the fresh XML counts, and both attempts. Do not relaunch. | Nick Shanin (standing pool rule) |
| Cluster fails to start before any assertion | default: run the same command once more | If the second run also fails before an assertion, record the first exception here and stop. This is harness setup, not a test result. | Nick Shanin (standing pool rule) |
| Proof leg result differs from the stated expectation | decided: record and stop | Record the test name and both failure texts here. Do not edit the tests. | Nick Shanin (standing pool rule) |
| Tip moved at claim time | decided: stop | Record the new tip here and hand back. | Nick Shanin (standing pool rule) |

No row may leave the choice to the claimant.

## On green

1. Write or refresh `receipts/SOLR-18555.md` with a new top section for gate ID `SOLR-18555-phase-r4-solrj-query`: head, gate date and host, log path, step results, the proof-leg result (test name and failure text, or none requested), and the focused counts from the fresh XML. Record every attempt, its repair commits and files, and the gated head.
2. No public text edits in this job unless a later job says so.
3. Mark this job DONE and `claims/pool-<host>-gate-SOLR-18555-phase-r4-solrj-query.md` DONE in the same push as the receipt.

If any step cannot be verified, record it here and stop.

## Checklist

- [ ] head confirmed by ls-remote
- [ ] diff scope confirmed
- [ ] step 0 changelog check
- [ ] step 1 tidy clean, or repaired and recorded
- [ ] step 2 Error Prone compile
- [ ] step 3 proof leg, if requested
- [ ] step 4 focused head run, counted from fresh XML
- [ ] step 5 module check
- [ ] receipt written
- [ ] job DONE, claim DONE

## Deliverable and limits

- One fast-forward push with the receipt and the DONE marks, plus any repair commits (WORKFLOW.md, Gate repairs).
- No PR opening or state change, no comment, no JIRA or email, no force push. Commit as Nick Shanin with no trailers. No em dashes.
