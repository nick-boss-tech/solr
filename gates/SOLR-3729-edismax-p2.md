# Gate job: SOLR-3729 (edismax match-all helper comments, patch P2)

Status: DONE 2026-10-18 at gated head `ec4173da5dbb9333dcb69257d10c66b0b7138ee6` (GREEN; receipts/SOLR-3729.md; claim claims/pool-vm3-gate-backlog-3729.md)

- Branch: `solr-3729-submit`. Head: `ec4173da5dbb9333dcb69257d10c66b0b7138ee6` (fork tip after the vm2 push under `assignments/pool-edismax-patches-apply-round-1.md`, 2026-10-18). If the tip has moved at claim time, stop and record the new tip here.
- Prior gated head: `0fe7e503945c1a169f76a019392d68bdf4261eb7` (receipts/SOLR-3729.md, round 36). Merge-base with upstream/main: `97d973814336`.
- Intended host: any capable Linux host (vm2 authored the patch; vm1 may run it). Any capable Linux host may claim if the intended host is busy.
- Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.search.TestExtendedDismaxParser`. Seed: `3729C0FFEE3729`.
- Diff scope against `0fe7e503945c`: `solr/core/src/java/org/apache/solr/search/ExtendedDismaxQParser.java` (javadoc of `isStandaloneMatchAll`, 9 lines changed, comment only); `solr/core/src/test/org/apache/solr/search/TestExtendedDismaxParser.java` (comment lines above the `*:*^abc` loop, 6 lines changed, comment only). Anything else in the tree is out of scope.
- Why: two comment defects in the branch's own text (D9): the javadoc described a narrower helper than the code, and the test comment was wrong for `*:*^abc`. Comment-only, so no proof leg. No gate has run on this head yet.

## What to run

Use the host's sweep runner (`pool/bin/sweep-run.sh`) from a worktree at the head.

- Step 0: changelog YAML parse of the changelog file already on the branch (`changelog/unreleased/SOLR-3729-*.yml`), unchanged by this patch.
- Step 1: tidy. If tidy changes any file, the run is GATE INVALID and stops.
- Step 2: Error Prone compile: `:solr:core:compileTestJava -Pvalidation.errorprone=true`.
- Step 3: no proof leg (comment-only change).
- Step 4, focused head run: clean first, then `:solr:core:test --tests "org.apache.solr.search.TestExtendedDismaxParser" -Ptests.seed=3729C0FFEE3729`. Expect 41 of 41. Count from the fresh JUnit XML. Nothing else runs on the worktree between the test run and the XML read.
- Step 5: module check `:solr:core:check -x test`.

A gate that executed tests and failed is not relaunched by the claimant. Record the failure here and follow the failure-route row.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Tip moved | default: stop | if the head differs from the Head line, record the new tip and stop; do not apply or gate a different head | main side |
| Failed gate run | default: record, do not relaunch | a gate that executed tests and failed is recorded here and escalated, not retried | main side |
| Owner item O1 (broad or narrow match-all set) | decided: not settled here | the javadoc describes the code as it is; the O1 choice stays with the draft and is not changed by this job | Nick |

## On green

1. Write `receipts/SOLR-3729.md` with a new top section: head, gate date and host, log path, step results, and the focused counts from the fresh XML.
2. Mark this job DONE and the claim DONE in the same push as the receipt.

If any step cannot be verified, record it here and stop.

## Checklist

- [x] head confirmed by ls-remote
- [x] diff scope confirmed (comment-only)
- [x] step 0 changelog parse
- [x] step 1 tidy clean
- [x] step 2 Error Prone compile
- [x] step 4 focused head run, counted from fresh XML
- [x] step 5 module check
- [x] receipt written
- [x] job DONE, claim DONE

## Deliverable and limits

- One fast-forward push with the receipt and the DONE marks.
- No PR opening or state change, no comment, no JIRA or email, no force push. Commit as Nick Shanin with no trailers. No em dashes.
