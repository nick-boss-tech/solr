# Gate job: SOLR-6009 (edismax regexp changelog title, patch P3)

Status: CLAIMED (claims/pool-vm2-gate-backlog-6009-edismax-p3.md, vm2, 2026-10-18T08:40Z)

- Branch: `solr-6009-submit`. Head: `b7695a518cccc0158a6374c61af9760884d1cb80` (fork tip after the vm2 push under `assignments/pool-edismax-patches-apply-round-1.md`, 2026-10-18). If the tip has moved at claim time, stop and record the new tip here.
- Prior gated head: `a41bb034a1f47c08a8f8ea6ebbe46e6d443cbb6f` (receipts/SOLR-6009.md, round 32). Merge-base with upstream/main: `14c7aac0d151`.
- Intended host: any capable Linux host (vm2 authored the patch; vm1 may run it). Any capable Linux host may claim if the intended host is busy.
- Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.search.TestExtendedDismaxParser`. Seed: `6009C0FFEE6009`.
- Diff scope against `a41bb034a1f4`: `changelog/unreleased/SOLR-6009-edismax-regexp.yml` (folded title, 2 lines replaced). Anything else in the tree is out of scope.
- Why: the title drops the unverified "(or errored)" case (D11, round 3 part e3). Title-only changelog fix. No gate has run on this head yet.

## What to run

Use the host's sweep runner (`pool/bin/sweep-run.sh`) from a worktree at the head.

- Step 0: changelog YAML parse of `changelog/unreleased/SOLR-6009-edismax-regexp.yml`.
- Step 1: tidy. No Java files change, so tidy must report nothing to change. If tidy changes any file, the run is GATE INVALID and stops.
- Step 2: no Error Prone compile is named for this patch (changelog-only).
- Step 3: no proof leg (title-only).
- Step 4, focused head run: clean first, then `:solr:core:test --tests "org.apache.solr.search.TestExtendedDismaxParser" -Ptests.seed=6009C0FFEE6009`. Expect 41 of 41. Count from the fresh JUnit XML.
- Step 5: module check `:solr:core:check -x test`.

A gate that executed tests and failed is not relaunched by the claimant. Record the failure here and follow the failure-route row.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Tip moved | default: stop | if the head differs from the Head line, record the new tip and stop | main side |
| Failed gate run | default: record, do not relaunch | a gate that executed tests and failed is recorded here and escalated, not retried | main side |
| Owner item O4 (SOLR-6009 history: the two handoff-doc commits) | decided: not performed here | history rewrites need Nick's go-ahead and are not part of this job | Nick |

## On green

1. Write `receipts/SOLR-6009.md` with a new top section: head, gate date and host, log path, step results, and the focused counts from the fresh XML.
2. Mark this job DONE and the claim DONE in the same push as the receipt.

If any step cannot be verified, record it here and stop.

## Checklist

- [ ] head confirmed by ls-remote
- [ ] diff scope confirmed (changelog only)
- [ ] step 0 changelog parse
- [ ] step 1 tidy clean
- [ ] step 4 focused head run, counted from fresh XML
- [ ] step 5 module check
- [ ] receipt written
- [ ] job DONE, claim DONE

## Deliverable and limits

- One fast-forward push with the receipt and the DONE marks.
- No PR opening or state change, no comment, no JIRA or email, no force push. Commit as Nick Shanin with no trailers. No em dashes.
