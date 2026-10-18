# Gate job: SOLR-6320 (edismax lowercase operator test comments, patch P4)

Status: OPEN

- Branch: `solr-6320-submit`. Head: `3a35d800a2e5ea9e14632733110973d5caee70e8` (fork tip after the vm2 push under `assignments/pool-edismax-patches-apply-round-1.md`, 2026-10-18). If the tip has moved at claim time, stop and record the new tip here.
- Prior gated head: `cb710c963531c4f32166e6e0b28c45e54b164a3f` (receipts/SOLR-6320.md, round 36). Merge-base with upstream/main: `97d973814336`.
- Intended host: any capable Linux host (vm2 authored the patch; vm1 may run it). Any capable Linux host may claim if the intended host is busy.
- Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.search.TestExtendedDismaxParser`. Seed: `6320C0FFEE6320`.
- Diff scope against `cb710c963531`: `solr/core/src/test/org/apache/solr/search/TestExtendedDismaxParser.java` (javadoc of `testLowercaseOperatorBoundaryPins` and `testLowercaseOperatorDemotedOrShapes`, and the two-line TODO comment removed; 5 insertions, 11 deletions, comment-only). Anything else in the tree is out of scope.
- Why: the comments described a finding that the owner has not settled (O2), and the TODO pinned an out-of-scope shape. The patch rewrites the comments to state what the strings pin and nothing more. No gate has run on this head yet.

## What to run

Use the host's sweep runner (`pool/bin/sweep-run.sh`) from a worktree at the head.

- Step 0: no changelog change in this patch; skip.
- Step 1: tidy. If tidy changes any file, the run is GATE INVALID and stops.
- Step 2: Error Prone compile: `:solr:core:compileTestJava -Pvalidation.errorprone=true`.
- Step 3: no proof leg (comment-only change).
- Step 4, focused head run: clean first, then `:solr:core:test --tests "org.apache.solr.search.TestExtendedDismaxParser" -Ptests.seed=6320C0FFEE6320`. Expect 44 of 44, the same count as before the patch. Count from the fresh JUnit XML.
- Step 5: module check `:solr:core:check -x test`.

A gate that executed tests and failed is not relaunched by the claimant. Record the failure here and follow the failure-route row.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Tip moved | default: stop | if the head differs from the Head line, record the new tip and stop | main side |
| Test count differs from 44 | default: record and stop | if the focused run counts other than 44 of 44, record the count here and escalate; do not change the count expectation | main side |
| Failed gate run | default: record, do not relaunch | a gate that executed tests and failed is recorded here and escalated, not retried | main side |
| Owner item O2 (mixed-case neighbours, finding 1) | decided: not settled here | the comments state the implemented rule only; the choice stays with the draft | Nick |
| Owner item O5 (history subject) | decided: not performed here | history rewrites need Nick's go-ahead | Nick |

## On green

1. Write `receipts/SOLR-6320.md` with a new top section: head, gate date and host, log path, step results, and the focused counts from the fresh XML.
2. Mark this job DONE and the claim DONE in the same push as the receipt.

If any step cannot be verified, record it here and stop.

## Checklist

- [ ] head confirmed by ls-remote
- [ ] diff scope confirmed (comment-only)
- [ ] step 1 tidy clean
- [ ] step 2 Error Prone compile
- [ ] step 4 focused head run, counted from fresh XML (44 of 44)
- [ ] step 5 module check
- [ ] receipt written
- [ ] job DONE, claim DONE

## Deliverable and limits

- One fast-forward push with the receipt and the DONE marks.
- No PR opening or state change, no comment, no JIRA or email, no force push. Commit as Nick Shanin with no trailers. No em dashes.
