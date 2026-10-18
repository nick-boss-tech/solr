# Gate job: SOLR-14913 (edismax alias targets and compound query assertion, patch P6)

Status: CLAIMED (vm5, claims/pool-vm5-gate-backlog-14913-edismax-p6-r2.md, 2026-10-18T20:14:57ZZ; new proof base recorded in the claim). Previous status: HANDED (vm5 stopped at the proof leg, 2026-10-18T08:21:36Z; the proof leg fails at line 960, the multi-term assertion from `589d9aea39`, not at the new assertion at line 1031. Main side decides the proof design. Pool hosts skip this file until then. Claim: claims/pool-vm5-gate-backlog-14913-edismax-p6.md.)

- Branch: `solr-14913-submit`. Head: `137dbace0170d05f8655c212cac9b27562bcfb87` (fork tip after the vm2 push under `assignments/pool-edismax-patches-apply-round-1.md`, 2026-10-18). If the tip has moved at claim time, stop and record the new tip here.
- Prior gated head: `b80221f46d3c` (receipts/SOLR-14913.md; the gated tree was `51fe1087fe9a`, and the live tip differed by a comment-only commit). Merge-base with upstream/main: `b5c71bc5573`.
- Intended host: any capable Linux host (vm2 authored the patch; vm1 may run it). Any capable Linux host may claim if the intended host is busy.
- Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.search.TestExtendedDismaxParser` (expect 40 of 40 at the new head). Seed: `14913C0FFEE14913`.
- Diff scope against `b80221f46d3c`: `changelog/unreleased/SOLR-14913.yml` (title line replaced); `solr/core/src/test/org/apache/solr/search/TestExtendedDismaxParser.java` (3 lines added to `testAliasingWithNonSchemaField`: the `myalias:Zapp AND name:Zapp` compound query with `f.myalias.qf` set to `nosuchfield` must match nothing). Anything else in the tree is out of scope.
- Why: the title states the decided behaviour (D3: an all-missing alias matches nothing), and the new assertion pins the compound-query case (D5, round 28 item M2). No gate has run on this head yet.

## What to run

Use the host's sweep runner (`pool/bin/sweep-run.sh`) from a worktree at the head.

- Step 0: changelog YAML parse of `changelog/unreleased/SOLR-14913.yml`.
- Step 1: tidy. If tidy changes any file, the run is GATE INVALID and stops.
- Step 2: Error Prone compile: `:solr:core:compileTestJava -Pvalidation.errorprone=true`.
- Step 3, proof leg: revert `solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java` to `b5c71bc5573` (the base parser) and keep the head test file. Expected: `testAliasingWithNonSchemaField` fails at the new compound-query assertion, because the base parser returns the `name:Zapp` document (one document, where the assertion expects none). Any other failure shape stops the job and follows the failure-route decision. Restore the parser to the head before step 4.
- Step 4, focused head run: clean first, then `:solr:core:test --tests "org.apache.solr.search.TestExtendedDismaxParser" -Ptests.seed=14913C0FFEE14913`. Expect 40 of 40. Count from the fresh JUnit XML. Nothing else runs on the worktree between the test run and the XML read.
- Step 5: module check `:solr:core:check -x test`.

A gate that executed tests and failed is not relaunched by the claimant. Record the failure here and follow the failure-route row.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Tip moved | default: stop | if the head differs from the Head line, record the new tip and stop | main side |
| Proof-leg failure shape | default: stop | if the proof leg fails in any way other than the new assertion, record and stop | main side |
| Failed gate run | default: record, do not relaunch | a gate that executed tests and failed is recorded here and escalated, not retried | main side |
| All-missing alias matches nothing (D3) | decided: keep | the title and the assertion state it; no owner ratification is needed for the draft | main side |

## On green

1. Write `receipts/SOLR-14913.md` with a new top section: head, gate date and host, log path, step results, the proof-leg result (test name and failure text), and the focused counts from the fresh XML.
2. Mark this job DONE and the claim DONE in the same push as the receipt.

If any step cannot be verified, record it here and stop.

## Checklist

- [ ] head confirmed by ls-remote
- [ ] diff scope confirmed
- [ ] step 0 changelog parse
- [ ] step 1 tidy clean
- [ ] step 2 Error Prone compile
- [ ] step 3 proof leg, expected failure by content
- [ ] step 4 focused head run, counted from fresh XML (40 of 40)
- [ ] step 5 module check
- [ ] receipt written
- [ ] job DONE, claim DONE

## Deliverable and limits

- One fast-forward push with the receipt and the DONE marks.
- No PR opening or state change, no comment, no JIRA or email, no force push. Commit as Nick Shanin with no trailers. No em dashes.
