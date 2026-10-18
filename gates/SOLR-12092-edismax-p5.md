# Gate job: SOLR-12092 (edismax stopwords and ManagedStopFilterFactory, patch P5)

Status: DONE 2026-10-18 (vm2, GATE GREEN at `e309a2ddefab`, nine classes 82 of 82; receipts/SOLR-12092.md). The round 33 list was recovered by vm1 (reports/SOLR-12092-round-33-focused-list.md). Trailers removed by a history rewrite with the user's approval.

- Branch: `solr-12092-submit`. Head: `4243924c34faa28d9964d7c214d82a8fd175b822` (fork tip after the vm2 push under `assignments/pool-edismax-patches-apply-round-1.md`, 2026-10-18). If the tip has moved at claim time, stop and record the new tip here.
- Prior gated head: `ca9573dabd385a38711bd60633973ae46b7a552e` (receipts/SOLR-12092.md, round 33). Merge-base with upstream/main: `14c7aac0d151`.
- Intended host: any capable Linux host (vm2 authored the patch; vm1 may run it). Any capable Linux host may claim if the intended host is busy.
- Capability tags: gate.
- Module: `:solr:core` and `:solr:solr-ref-guide` where the sweep runner names it for the `.adoc` change. Focused classes: `org.apache.solr.search.TestManagedStopFilterFactory` (expect 5 of 5), `org.apache.solr.search.TestExtendedDismaxParser` (expect 39 of 39), and the other seven classes of the focused run recorded in `receipts/SOLR-12092.md`. Seed: `12092C0FFEE12092`.
- Diff scope against `ca9573dabd38`: `changelog/unreleased/SOLR-12092-edismax-managed-stopwords.yml` (one line added to the folded title); `solr/solr-ref-guide/modules/query-guide/pages/edismax-query-parser.adoc` (one sentence added after line 110). Anything else in the tree is out of scope.
- Why: the title and the guide now state that `stopwords=false` has no effect when the index analyzer has a stop filter (D10 and D4). The `.adoc` change is not docs-only under the 2026-10-12 rule, so the full gate applies. No gate has run on this head yet.
- History note: the three commits above `upstream/main` still carry `Co-Authored-By` trailers (`cde23f0a9d7`, `b989934d4ea`, `3a54f4f3fdf`). They were not removed: removing them rewrites the branch and needs a force push and Nick's go-ahead (owner item O3).

## What to run

Use the host's sweep runner (`pool/bin/sweep-run.sh`) from a worktree at the head.

- Step 0: changelog YAML parse of `changelog/unreleased/SOLR-12092-edismax-managed-stopwords.yml`.
- Step 1: tidy. If tidy changes any file, the run is GATE INVALID and stops.
- Step 2: Error Prone compile: `:solr:core:compileTestJava -Pvalidation.errorprone=true`.
- Step 3: no proof leg named for this patch.
- Step 4, focused head run: clean first, then `:solr:core:test` on each focused class with `-Ptests.seed=12092C0FFEE12092`. Count each class from its fresh JUnit XML. The other seven class names come from the run recorded in `receipts/SOLR-12092.md`; if that list cannot be recovered from the gate log `g12092r33-gate.log` or the receipt, record it here and stop.
- Step 5: module check `:solr:core:check -x test`.

A gate that executed tests and failed is not relaunched by the claimant. Record the failure here and follow the failure-route row.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Tip moved | default: stop | if the head differs from the Head line, record the new tip and stop | main side |
| Other seven classes | default: use the round 33 run | if the list cannot be recovered from the gate log or the receipt, record it here and stop; do not guess a list | main side |
| Failed gate run | default: record, do not relaunch | a gate that executed tests and failed is recorded here and escalated, not retried | main side |
| Trailer removal on the three commits | decided: not performed | removing trailers is a history rewrite and needs a force push; O3 stays open with Nick | Nick |
| Pending draft review flag (`pr-drafts/edismax/SOLR-12092.md`) | default: stays flagged | the flagged sentence becomes true at this head; the flag is removed only in the draft that lives on the pr-prepare branch, after this gate | main side |

## On green

1. Write `receipts/SOLR-12092.md` with a new top section: head, gate date and host, log path, step results, and the focused counts from the fresh XML for each class.
2. Mark this job DONE and the claim DONE in the same push as the receipt.

If any step cannot be verified, record it here and stop.

## Checklist

- [ ] head confirmed by ls-remote
- [ ] diff scope confirmed
- [ ] step 0 changelog parse
- [ ] step 1 tidy clean
- [ ] step 2 Error Prone compile
- [ ] step 4 focused head runs, counted from fresh XML, all nine classes
- [ ] step 5 module check
- [ ] receipt written
- [ ] job DONE, claim DONE

## Deliverable and limits

- One fast-forward push with the receipt and the DONE marks.
- No PR opening or state change, no comment, no JIRA or email, no force push. Commit as Nick Shanin with no trailers. No em dashes.

## Round 33 focused list, recovered in part (vm2, 2026-10-18)

- Confirmed from the Fork test runner dispatches of round 33 (runs 37588170948, 37594429145, 37594491891, 37594544056, 37594598556, 37594677017, 37594742467; `prepare` job logs): TestManagedStopFilterFactory, TestBulkSchemaAPI, TestRestManager, TestSolrConfigHandler, TestManagedSynonymGraphFilterFactory, TestExtendedDismaxParser. Six of nine.
- Still unidentified: three classes. The receipt's 82-test count and nine-class total cannot be matched from the records on this host, the coordination history, or the pr-prepare archive. The round 33 local log g12092r33-gate.log is not on this host.
- Status stays STOPPED under the row "Other seven classes". No list has been guessed.
