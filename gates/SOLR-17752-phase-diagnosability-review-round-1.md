# Gate job: SOLR-17752 (corrupted-response diagnostics hardening)

Status: CLAIMED (claims/pool-phone-agent-2-gate-SOLR-17752-phase-diagnosability-review-round-1.md)

- Branch: `solr-17752-submit`. Head: `32c33350daf84bb3d540ba1f49badd1d6f9b4a4a` (fork tip, confirmed by ls-remote on 2026-10-23).
- Prior gated head: `041c5b0b2e44e903e7010a40530de3a24ea9bb06`. Merge-base with upstream/main: `56ec140e3636d5f4150fa87fbf7103529536ac99`.
- Intended host: phone-agent-2. Any capable Linux host may claim if this host is busy (WORKFLOW.md).
- Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.handler.component.TestShardResponseLogging`. Seed: `17752B0BCAFE`.
- Diff scope against `041c5b0b2e44e903e7010a40530de3a24ea9bb06`: `SolrResponseUtil.java` (+30/-19) and `TestShardResponseLogging.java` (+39/-7). No other product files changed.
- Why: the warning previously emitted untrusted response key names and the custom-shard URL fallback could include credentials or query data. The new change logs only the missing fixed section, response entry count, numeric purpose, and host/port when no node name exists. The new tests distinguish both exposures and cover the wrapped exception path.

Links: Ticket or parent: SOLR-17752, PR #5171. Category: Search and query components. Siblings: the SOLR-17752 receipt and PR #5171. Supersedes: the prior gate recorded in `receipts/SOLR-17752.md` at head `041c5b0b2e`.

## What to run

Use `/workspace/tools/phone-agent-2-hardening-gate-v2.py` with a fresh config and logs directory under `/workspace/phone-agent-2-logs`. Do not run Gradle directly.

- Step 0: parse `changelog/unreleased/SOLR-17752.yml` as YAML.
- Step 1: run tidy. If tidy changes a file, commit the tidy output as a gate repair, record its patch and tidied tree SHA here, and restart the gate at step 0.
- Step 2: Error Prone compile `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
- Step 3, proof leg: restore only `SolrResponseUtil.java` from `041c5b0b2e44e903e7010a40530de3a24ea9bb06`; keep the tests and other files at the head. Expected: the `TestShardResponseLogging` failure text includes `sensitive query/key value reached warning`. Any other failure shape stops and follows the gate repair rules.
- Step 4, focused head run: clean and run `TestShardResponseLogging`, `DistributedTermsComponentTest`, `DistributedExpandComponentTest`, `DistributedSpellCheckComponentTest`, and `org.apache.solr.TestDistributedGrouping` with seed `17752B0BCAFE`, one JVM. Count from fresh JUnit XML.
- Step 5: `:solr:core:check -x test`.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Proof base | decided: restore only `SolrResponseUtil.java` from the prior gated head and keep the added tests | none | Phone Agent 2 |
| Failure outside repair scope | default: stop and record the exact failure | if repair exceeds WORKFLOW.md Gate repairs, mark HANDED BACK and return the item to implementation | Nick |

## On green

1. Write a new top section in `receipts/SOLR-17752.md` with original head, any repair commits, gated head, host/date, runner/log paths, step results, proof failure text, and fresh XML counts.
2. Write `reports/pool-pr5171-diagnosability-round-1.md` with emitted fields, the leak-path review, diagnosability findings, test results, verdict, and any PR-description follow-up.
3. Mark this gate and its claim DONE in the same push as the receipt and report. Refresh `OPEN-WORK.md` and read all posted records back.

If any step cannot be verified, record it here and stop.

## Checklist

- [x] head confirmed by ls-remote
- [x] diff scope confirmed
- [ ] step 0 changelog YAML parse
- [ ] step 1 tidy clean, or repaired and recorded
- [ ] step 2 Error Prone compile
- [ ] step 3 proof leg, expected failure by content
- [ ] step 4 focused head run, counted from fresh XML
- [ ] step 5 module check
- [ ] receipt written
- [ ] report written
- [ ] public PR-description follow-up resolved with Nick
- [ ] gate DONE, claim DONE

## Deliverable and limits

- One fast-forward push with the receipt, report, and DONE marks, plus any allowed repair commits (WORKFLOW.md, Gate repairs).
- No new PR, PR state change, comment, Jira edit, or email. The assignment authorizes updating the existing PR body to match the changed branch; keep that edit separate from the code commit and read it back. Commit as Nick Shanin with no trailers. No em dashes.
