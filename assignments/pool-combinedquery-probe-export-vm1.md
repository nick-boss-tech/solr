# Pool assignment: export the CombinedQueryComponent probe and its run records to the repository (vm1)

Status: DONE

Capability tags: `review` (export and reporting only). Intended host: vm1, the main side's Linux host, where the probe source and run artefacts live. Staffing: one agent on vm1. Claim: a commit under `claims/` before the export, as the pool rules require.

## Why

The CombinedQueryComponent review has been held up by facts that exist only on vm1: the probe source, its JUnit XML, and its premise log. The Windows review host reads only this repository, so it cannot check those facts. That is why round 11 left open item A5 (whether cases M1 and M2 set `maxHitsTerminatedEarly`) and why the labels that depend on the probe could not be settled. This assignment puts the answers on the record. Nothing here needs a new run.

## What vm1 does

1. **Claim.** Commit `claims/pool-combinedquery-probe-export-vm1.md` with the host, the start time (UTC), and status CLAIMED.
2. **Copy the probe source, unchanged.** From the vm1 worktree `solr-worktrees/cqc-premise`, copy `solr/core/src/test/org/apache/solr/handler/component/CombinedQueryComponentMergeTotalsTest.java` to `material/combinedquery-probe-export/CombinedQueryComponentMergeTotalsTest.java`. Do not edit it. Record its git blob SHA in the summary.
3. **Copy the run records.** Copy the JUnit XML `gcqc-premise-TEST-org.apache.solr.handler.component.CombinedQueryComponentMergeTotalsTest.xml` and the premise log `gcqc-premise3.log` to `material/combinedquery-probe-export/`. If either file is missing on vm1, say so in the summary and stop that item. Do not re-run the probe to produce it. A new run is a separate job.
4. **Write the per-case summary.** Create `material/combinedquery-probe-export/PROBE-SUMMARY.md` with one row per case: P, P2, F, X1, X2, M1, M2. Each row gives:
   - the sub-query count, from the request builder, and the query strings passed;
   - the outcome from the XML: passed or failed, and the assertion message verbatim;
   - whether each shard response set `maxHitsTerminatedEarly`, and its value;
   - the merged `numFound`, `numFoundExact` and `approximateTotalHits` as the test read them;
   - the probe lines that set each of those values.
5. **Answer A5 in the summary.** State, for M1 and M2, whether any shard response set `maxHitsTerminatedEarly`, and whether the merged `approximateTotalHits` reached the response header. Quote the probe lines. This is the item the review is waiting on.
6. **Answer the open record items from the same export.** State whether the run counts (tests, failures, errors) and the seed `C0FFEE42C0FFEE42` appear in the XML, and quote the `<testsuite>` line.
7. **Commit and push.** Commit the export and the summary as Nick Shanin. Commit message: `CombinedQueryComponent probe export and per-case summary`. No trailers, no em dashes, no PR numbers in file names or messages. Push to `pr-prepare` as a fast-forward only. Do not force.
8. **Mark the claim DONE** in the same push, naming the summary path.

## Rules

- The probe is not re-run. Nothing is built, no Gradle, no test.
- Copy only. Do not change the probe source, the XML, or the log.
- Keep the summary factual: quote lines and values. Do not interpret the probe for the ticket. The review does that.
- No PR, comment, Jira write, or email. Nothing is posted publicly.
- If a fact cannot be read from the export, write `NOT IN EXPORT: <what is missing>`. Do not guess.

## Deliverable

`material/combinedquery-probe-export/` containing the probe source, the XML, the log, and `PROBE-SUMMARY.md`. The summary's first section answers A5 directly, with a verdict line for M1 and M2 each: FLAG SET, FLAG NOT SET, or NOT IN EXPORT.

## After this assignment

The Windows review reads the export and answers A5 and the labels from it. The review does not need vm1 again for this ticket. Any further probe question goes to vm1 as a new assignment, answered from the export.
