# Gate job: SOLR-18555 (Integrated supplied-text highlighting with V2 QUERY)

Status: OPEN

- Branch: `solr-18555-query-client-support`. Head: `0c6aae3162f29a7ae2f62fb226cefdf9c365dd63` (fork tip, confirmed by ls-remote on 2026-10-20). If the tip has moved at claim time, stop and record the new tip here.
- Base: `7c859a4d0eeae897542e21046e059f6fca45a6e8`. Prior gated head: none recorded.
- Gate ID: `SOLR-18555-phase-r4-solrj-query`. Claim file: `claims/pool-<host>-gate-SOLR-18555-phase-r4-solrj-query.md`.
- Intended host: any capable Linux gate host with the Solr reference-guide npm dependencies available.
- Capability tags: gate, npm.
- Modules: `:solr:core`, `:solr:solrj-jetty`, and `:solr:solr-ref-guide`.
- Focused classes: `org.apache.solr.handler.HighlightRequestHandlerTest`, `org.apache.solr.handler.HighlightRequestHandlerHttpTest`, `org.apache.solr.handler.HighlightRequestHandlerCloudHttpTest`, and `org.apache.solr.client.solrj.jetty.HttpJettySolrClientTest`. The SolrJ class includes a direct JDK-client case.
- Seed: `18555C0FFEE18555`.
- Diff scope: 6 files, 101 insertions and 10 deletions against `7c859a4d0eeae897542e21046e059f6fca45a6e8`: `SolrRequest.java`, `HttpJdkSolrClient.java`, `HttpJettySolrClient.java`, `HttpSolrClientTestBase.java`, `HttpJettySolrClientTest.java`, and `highlighting.adoc`. The three core handler tests are inherited from the feature branch and run at this exact integrated head.
- Why: One gate now validates the full feature stack at a single head: highlighting supplied text, the V2-only QUERY route and legacy rejection, QUERY body transmission by both SolrJ HTTP clients, and the guide text. The two parent-head phase-r4 jobs are paused; this replaces the SolrJ-only phase-r4 scope. The existing phase-r3 claim is NEARLY on `7c859a4`; do not edit or transfer that claim. It may finish its already-started guide check independently.

## What to run

Use the host's standard gate runner. Do not run the build tool directly. Check the worktree out at the head first.

- Step 0: changelog check. This branch adds no changelog fragment. If a new fragment appears, record it here and stop.
- Step 1: tidy. If tidy changes any file, apply the tidy output, record its patch and the tidied tree sha here, and continue from step 2. WORKFLOW.md, Gate repairs.
- Step 2: Error Prone compile: `:solr:core:compileJava :solr:core:compileTestJava :solr:solrj-jetty:compileJava :solr:solrj-jetty:compileTestJava -Pvalidation.errorprone=true`.
- Step 3, proof leg: none requested. Record that in the receipt.
- Step 4a, core focused run: `:solr:core:cleanTest`, then `:solr:core:test --tests org.apache.solr.handler.HighlightRequestHandlerTest --tests org.apache.solr.handler.HighlightRequestHandlerHttpTest --tests org.apache.solr.handler.HighlightRequestHandlerCloudHttpTest -Ptests.seed=18555C0FFEE18555`. Read the fresh JUnit XML immediately after this run, before running another task.
- Step 4b, SolrJ focused run: `:solr:solrj-jetty:cleanTest`, then `:solr:solrj-jetty:test --tests org.apache.solr.client.solrj.jetty.HttpJettySolrClientTest -Ptests.seed=18555C0FFEE18555`. Read the fresh JUnit XML immediately after this run.
- Step 5, module checks: `:solr:core:check -x test`, `:solr:solrj-jetty:check -x test`, and `:solr:solr-ref-guide:check -x test`. The guide check must include `checkSiteLinks` at this branch head.

Failures follow the rows below and WORKFLOW.md, Gate repairs. A failed retry hands the job back.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Tidy changes any file | decided: repair | Apply the tidy output on the gate worktree, record its patch and the tidied tree sha here, and continue from step 2. WORKFLOW.md, Gate repairs. | Nick Shanin (standing pool rule) |
| Failure in repair scope (compile, changelog, lint, or up to two failing tests) | decided: repair, then one retry | Make the repair as one commit and push it per WORKFLOW.md. Record the commit, its files, and the failing test or task here, then rerun from step 0 on the new head. Do not rerun an unchanged tree. | Nick Shanin (standing pool rule) |
| Failure outside repair scope, or any failure after the retry | decided: hand back | Set Status to HANDED BACK with the reason. Record the failing test or task, its failure text, the seed, the fresh XML counts, and both attempts. Do not relaunch. | Nick Shanin (standing pool rule) |
| Cluster fails to start before any assertion | default: run the same command once more | If the second run also fails before an assertion, record the first exception here and stop. This is harness setup, not a test result. | Nick Shanin (standing pool rule) |
| Guide dependencies are unavailable | default: stop and record the environment issue | Do not report the guide check green without `checkSiteLinks`; hand back the exact dependency and setup failure so a capable npm host can resume. | Nick Shanin (standing pool rule) |
| Proof leg result differs from the stated expectation | decided: record and stop | Record the test name and both failure texts. Do not edit the tests. | Nick Shanin (standing pool rule) |
| Tip moved at claim time | decided: stop | Record the new tip here and hand back. | Nick Shanin (standing pool rule) |

No row may leave the choice to the claimant.

## On green

1. Write or refresh `receipts/SOLR-18555.md` with a new top section for gate ID `SOLR-18555-phase-r4-solrj-query`: head, gate date and host, log path, each step result, the proof-leg result (none requested), and fresh XML counts for each focused class. Record every attempt, its repair commits and files, and the gated head.
2. No public text edits in this job unless a later job says so.
3. Mark this job DONE and `claims/pool-<host>-gate-SOLR-18555-phase-r4-solrj-query.md` DONE in the same push as the receipt.

If any step cannot be verified, record it here and stop.

## Checklist

- [ ] head confirmed by ls-remote
- [ ] diff scope confirmed
- [ ] step 0 changelog check
- [ ] step 1 tidy clean, or repaired and recorded
- [ ] step 2 Error Prone compile
- [ ] step 3 proof leg
- [ ] step 4a core focused tests counted from fresh XML
- [ ] step 4b SolrJ focused test counted from fresh XML
- [ ] step 5 core, SolrJ, and reference-guide module checks
- [ ] receipt written
- [ ] job DONE, claim DONE

## Deliverable and limits

- One fast-forward push with the receipt and the DONE marks, plus any repair commits (WORKFLOW.md, Gate repairs).
- No PR opening or state change, no comment, no JIRA or email, no force push. Commit as Nick Shanin with no trailers. No em dashes.
