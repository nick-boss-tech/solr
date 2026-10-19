# Gate job: SOLR-18503 (JSON facet parser options, after the tidy commit)

Status: OPEN

- Branch: `solr-18503-submit`. Head: `cc5b84dfcdd783ec2c2626aba4ba9931bb027893` (fork tip, confirmed by ls-remote on 2026-10-19). If the tip has moved at claim time, stop and record the new tip here.
- Base: `c8ab71430f6e7547c773da596101d680862310ff`. Prior gated head: none recorded.
- Gate ID: `SOLR-18503-phase-tidyfix`. Claim file: `claims/pool-<host>-gate-SOLR-18503-phase-tidyfix.md`.
- Intended host: any capable Linux host. WORKFLOW.md lets any capable Linux host claim it if that host is busy.
- Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.search.facet.TestJsonRangeFacets`. Seed: `18503C0FFEE18503`.
- Diff scope: 4 files against Base, 17 insertions and 5 deletions before the tidy commit; the tidy commit adds one file change, TestJsonRangeFacets.java, 1 insertion and 4 deletions
- Why: The earlier gate at dba48d69 stopped as GATE INVALID because tidy reformatted TestJsonRangeFacets.java. The tidy commit fixes that, so this head needs a new gate.

## What to run

Use the host's standard gate runner. Do not run the build tool directly. Check the worktree out at the head first.

- Step 0: changelog YAML parse of `changelog/unreleased/SOLR-18503-json-facet-parser-options.yml`.
- Step 1: tidy. If tidy changes any file, the run is GATE INVALID and stops.
- Step 2: Error Prone compile: `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
- Step 3, proof leg: Overlay only TestJsonRangeFacets.java from this head onto Base; expect the f-only range request to fail because base FacetRangeParser reads only field. Any other failure shape stops the job and follows the failure-route row.
- Step 4, focused head run: `:solr:core:cleanTest` first, then `:solr:core:test --tests org.apache.solr.search.facet.TestJsonRangeFacets -Ptests.seed=18503C0FFEE18503`. Count from the fresh JUnit XML. Nothing else runs on the worktree between the test run and the XML read.
- Step 5: module check `:solr:core:check -x test`.

A gate that executed tests and failed is not relaunched by the claimant. Record the failure here and follow the failure-route row.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Gate failure after tests executed | decided: record and stop | Record the failing test, its failure text, the seed and the XML counts here. Do not relaunch. | Nick Shanin (standing pool rule) |
| Tidy changes any file | decided: GATE INVALID | Record the tidy diff here and stop. Do not commit the tidy change. | Nick Shanin (standing pool rule) |
| Cluster fails to start before any assertion | default: run the same command once more | If the second run also fails before an assertion, record the first exception here and stop. This is harness setup, not a test result. | Nick Shanin (standing pool rule) |
| Proof leg result differs from the stated expectation | decided: record and stop | Record the test name and both failure texts here. Do not edit the tests. | Nick Shanin (standing pool rule) |
| Tip moved at claim time | decided: stop | Record the new tip here and hand back. | Nick Shanin (standing pool rule) |

No row may leave the choice to the claimant.

## On green

1. Write or refresh `receipts/SOLR-18503.md` with a new top section for gate ID `SOLR-18503-phase-tidyfix`: head, gate date and host, log path, step results, the proof-leg result (test name and failure text, or none requested), and the focused counts from the fresh XML.
2. No public text edits in this job unless a later job says so.
3. Mark this job DONE and `claims/pool-<host>-gate-SOLR-18503-phase-tidyfix.md` DONE in the same push as the receipt.

If any step cannot be verified, record it here and stop.

## Checklist

- [ ] head confirmed by ls-remote
- [ ] diff scope confirmed
- [ ] step 0 changelog check
- [ ] step 1 tidy clean
- [ ] step 2 Error Prone compile
- [ ] step 3 proof leg, if requested
- [ ] step 4 focused head run, counted from fresh XML
- [ ] step 5 module check
- [ ] receipt written
- [ ] job DONE, claim DONE

## Deliverable and limits

- One fast-forward push with the receipt and the DONE marks.
- No PR opening or state change, no comment, no JIRA or email, no force push. Commit as Nick Shanin with no trailers. No em dashes.
