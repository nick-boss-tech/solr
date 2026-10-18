# Gate job: SOLR-3243 (edismax unfielded inclusive range, patch P1)

Status: DONE 2026-10-18 (vm2 run and vm5 round 2 both GREEN at `a07674262eee`; receipts/SOLR-3243.md). Proof leg: 4 expected failures, see the receipt.

- Branch: `solr-3243-submit`. Head: `a07674262eee472a8faf1bfebc0a95e8e611b362` (fork tip after the vm2 push under `assignments/pool-edismax-patches-apply-round-1.md`, 2026-10-18). If the tip has moved at claim time, stop and record the new tip here.
- Prior gated head: `1db99c13662d8f827511a5d3a51763d7e1e2ee4c` (receipts/SOLR-3243.md, round 32). Merge-base with upstream/main: `97d973814336`.
- Intended host: any capable Linux host (vm2 authored the patch; vm1 may run it). Any capable Linux host may claim if the intended host is busy.
- Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.search.TestExtendedDismaxParser`. Seed: `3243C0FFEE3243`.
- Diff scope against `1db99c13662d`: `changelog/unreleased/SOLR-3243-edismax-unfielded-range.yml` (title line, 1 line replaced); `solr/core/src/test/org/apache/solr/search/TestExtendedDismaxParser.java` (+12, new test `testNegatedUnfieldedOpenRangeExcludesEveryDocument`). Anything else in the tree is out of scope.
- Why: the title and the new negated-form test pin the decided behaviour (D1 keep the widening, D2 negated form excludes every document). No gate has run on this head yet.

## What to run

Use the host's sweep runner (`pool/bin/sweep-run.sh`) from a worktree at the head.

- Step 0: changelog YAML parse of `changelog/unreleased/SOLR-3243-edismax-unfielded-range.yml`.
- Step 1: tidy. If tidy changes any file, the run is GATE INVALID and stops.
- Step 2: Error Prone compile: `:solr:core:compileTestJava -Pvalidation.errorprone=true`.
- Step 3, proof leg: revert `solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java` to `97d973814336` (the base parser) and keep the head test file. Expected: the new test `testNegatedUnfieldedOpenRangeExcludesEveryDocument` fails with `numFound` above 0 (doc 46 has neither a name nor a title). Any other failure shape stops the job and follows the failure-route decision. Restore the parser to the head before step 4.
- Step 4, focused head run: clean first, then `:solr:core:test --tests "org.apache.solr.search.TestExtendedDismaxParser" -Ptests.seed=3243C0FFEE3243`. Expect 43 of 43. Count from the fresh JUnit XML. Nothing else runs on the worktree between the test run and the XML read.
- Step 5: module check `:solr:core:check -x test`.

A gate that executed tests and failed is not relaunched by the claimant. Record the failure here and follow the failure-route row.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Tip moved | default: stop | if the head differs from the Head line, record the new tip and stop; do not apply or gate a different head | main side |
| Proof-leg failure shape | default: stop | if the proof leg fails in any way other than the new test with `numFound` above 0, record and stop | main side |
| Failed gate run | default: record, do not relaunch | a gate that executed tests and failed is recorded here and escalated, not retried | main side |
| Owner item O1 (SOLR-3729 match-all set) | not in this job | nothing in this job depends on it | Nick |

## On green

1. Write `receipts/SOLR-3243.md` with a new top section: head, gate date and host, log path, step results, the proof-leg result (test name and failure text), and the focused counts from the fresh XML.
2. Mark this job DONE and the claim DONE in the same push as the receipt.

If any step cannot be verified, record it here and stop.

## Checklist

- [x] head confirmed by ls-remote
- [x] diff scope confirmed
- [x] step 0 changelog parse
- [x] step 1 tidy clean
- [x] step 2 Error Prone compile
- [ ] step 3 proof leg, expected failure by content (FAILED, four failures, see the claim)
- [ ] step 4 focused head run, counted from fresh XML
- [ ] step 5 module check
- [ ] receipt written
- [ ] job DONE, claim DONE

## Deliverable and limits

- One fast-forward push with the receipt and the DONE marks.
- No PR opening or state change, no comment, no JIRA or email, no force push. Commit as Nick Shanin with no trailers. No em dashes.
