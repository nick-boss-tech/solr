# Gate job: SOLR-17752 (corrupted-response diagnostics hardening)

Status: DONE (gate green at 310e1d02ef15e136016bb66f09aaaef8f836dc1b; see receipt and report)

- Branch: `solr-17752-submit`. Head: `310e1d02ef15e136016bb66f09aaaef8f836dc1b` (fork tip, confirmed by ls-remote on 2026-10-23). First attempt started at `32c33350daf84bb3d540ba1f49badd1d6f9b4a4a`.
- Prior gated head: `041c5b0b2e44e903e7010a40530de3a24ea9bb06`. Merge-base with upstream/main: `56ec140e3636d5f4150fa87fbf7103529536ac99`.
- Intended host: phone-agent-2. Any capable Linux host may claim if this host is busy (WORKFLOW.md).
- Capability tags: gate.
- Module: `:solr:core`. Focused class: `org.apache.solr.handler.component.TestShardResponseLogging`. Seed: `17752B0BCAFE`.
- Diff scope against `041c5b0b2e44e903e7010a40530de3a24ea9bb06`: `SolrResponseUtil.java` (+30/-19) and `TestShardResponseLogging.java` (+38/-7). No other product files changed.
- Why: the warning previously emitted untrusted response key names and the custom-shard URL fallback could include credentials or query data. The new change logs only the missing fixed section, response entry count, numeric purpose, and host/port when no node name exists. The new tests distinguish both exposures and cover the wrapped exception path.

Links: Ticket or parent: SOLR-17752, PR #5171. Category: Search and query components. Siblings: the SOLR-17752 receipt and PR #5171. Supersedes: the prior gate recorded in `receipts/SOLR-17752.md` at head `041c5b0b2e`.

## What to run

Use `/workspace/tools/phone-agent-2-hardening-gate-v2.py` with a fresh config and logs directory `/workspace/phone-agent-2-logs/gate-17752-diagnosability-round-1-retry`. Do not run Gradle directly.

- Step 0: parse `changelog/unreleased/SOLR-17752.yml` as YAML.
- Step 1: run tidy. If tidy changes a file, commit the tidy output as a gate repair, record its patch and tidied tree SHA here, and restart the gate at step 0.
- Step 2: Error Prone compile `:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`.
- Step 3, proof leg: restore only `SolrResponseUtil.java` from `041c5b0b2e44e903e7010a40530de3a24ea9bb06`; keep the tests and other files at the head. Expected: the `TestShardResponseLogging` failure text includes `sensitive query/key value reached warning`. Any other failure shape stops and follows the gate repair rules.
- Step 4, focused head run: clean and run `TestShardResponseLogging`, `DistributedTermsComponentTest`, `DistributedExpandComponentTest`, `DistributedSpellCheckComponentTest`, and `org.apache.solr.TestDistributedGrouping` with seed `17752B0BCAFE`, one JVM. Count from fresh JUnit XML.
- Step 5: `:solr:core:check -x test`.

## Attempt 1 and repair

At head `32c33350daf84bb3d540ba1f49badd1d6f9b4a4a`, step 0 changelog parsing passed and step 1 tidy completed successfully, but tidy changed only formatting in `TestShardResponseLogging.java`. The runner stopped before Error Prone or tests. The exact patch is `/workspace/phone-agent-2-logs/gate-17752-diagnosability-round-1/tidy.patch`.

The one allowed repair is commit `310e1d02ef15e136016bb66f09aaaef8f836dc1b`, applying that test-formatting patch only. Repaired tree: `e4b07086da31a5224314a12dedd87e0d57ad7335`.

## Retry result

The full retry completed at head `310e1d02ef15e136016bb66f09aaaef8f836dc1b`. YAML parse, tidy, Error Prone compile, and `:solr:core:check -x test` passed. In the proof leg, the two new `TestShardResponseLogging` tests failed on the prior production source with the expected sensitive response-key and shard-URL markers. On the fixed head, all five focused classes passed: `TestShardResponseLogging` 2/2, `DistributedTermsComponentTest` 1/1, `DistributedExpandComponentTest` 1/1, `DistributedSpellCheckComponentTest` 1/1, and `TestDistributedGrouping` 2/2. Fresh XML total: 7 tests, 0 skipped, 0 failures, 0 errors. Gate result: GREEN. Logs: `/workspace/phone-agent-2-logs/gate-17752-diagnosability-round-1-retry`.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Proof base | decided: restore only `SolrResponseUtil.java` from the prior gated head and keep the added tests | none | Phone Agent 2 |
| Failure outside repair scope | default: stop and record the exact failure | if repair exceeds WORKFLOW.md Gate repairs, mark HANDED BACK and return the item to implementation | Nick |

## Completed

1. Added the green retry to `receipts/SOLR-17752.md`, including the tidy repair, exact proof failures, fresh XML counts, and the PR draft.
2. Added `reports/pool-pr5171-diagnosability-round-1.md` with the emitted-field audit, leak paths, diagnostic tradeoff, test results, and OPEN-READY verdict.
3. Updated the existing PR description to match the tested head. Its title and open state remain unchanged; no comment or Jira entry was posted.
4. Marked this gate, its claim, and the review assignment and claim DONE in the same coordination push. Refreshed `OPEN-WORK.md`.

If any step cannot be verified, record it here and stop.

## Checklist

- [x] head confirmed by ls-remote
- [x] diff scope confirmed
- [x] step 0 changelog YAML parse
- [x] step 1 tidy clean after the recorded repair
- [x] step 2 Error Prone compile
- [x] step 3 proof leg failed by the two expected leakage markers
- [x] step 4 focused head run, counted from fresh XML
- [x] step 5 module check
- [x] receipt and report written
- [x] existing PR description updated and read back
- [x] gate DONE, claim DONE

## Deliverable and limits

- One fast-forward push with the receipt, report, and DONE marks, plus any allowed repair commits (WORKFLOW.md, Gate repairs).
- No new PR, PR state change, comment, Jira edit, or email. The assignment authorizes updating the existing PR body to match the changed branch; keep that edit separate from the code commit and read it back. Commit as Nick Shanin with no trailers. No em dashes.
