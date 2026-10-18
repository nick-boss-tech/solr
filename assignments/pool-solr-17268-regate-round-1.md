# Pool assignment: SOLR-17268 full re-gate on corrected test head

Status: DONE 2026-10-18 at f888c1cea8 (GREEN; receipts/SOLR-17268.md, claims/pool-vm3-solr-17268-regate-round-2.md).

- Capability tags: gate.
- Intended host: vm2 (Linux). Any capable Linux host may claim if vm2 is busy or unavailable. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: Windows research lane, 2026-10-17, at the direction of Nick Shanin.

## Items

1. **Run the full hardening gate on the corrected test head.** Run Steps 0 through 5 in `gates/SOLR-17268.md`, including both proof legs, using the exact branch head below. The main agent investigated the prior test failures and pushed the test setup fixes in this commit: numeric fixture IDs for the JSON writer tests and collection-qualified routing for the SolrCloud test requests. This is an authorized fresh gate on the new head. Start from: `nick-boss-tech/solr`, branch `solr-17268-submit`, commit `9993394741edbd331e1f38576ec039b1c957b4b1`. Use seed `17268C0FFEE17268`. Output: `reports/SOLR-17268-regate-9993394.md`; update the existing `gates/SOLR-17268.md` with this run while preserving its prior outcome, and refresh `receipts/SOLR-17268.md` only if the gate is green.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Fork tip at claim time | decided: require `9993394741edbd331e1f38576ec039b1c957b4b1` | if the tip differs, do not run tests; record the actual tip in the report and stop | Nick Shanin |
| Tidy changes files | decided: stop as GATE INVALID | record the tidy diff in the gate file and report; do not commit tidy output or continue | Nick Shanin |
| Proof leg results | decided: require the content-based outcomes in the existing gate plan | if any expected failure or control differs, record the test and failure content, then stop without changing source or tests | Nick Shanin |
| SolrCloud startup before assertions | default: retry the same command once | if the second attempt also fails before an assertion, record the first exception as harness setup and stop | Nick Shanin |
| Focused head tests | decided: require 20 `JSONWriterTest` tests and 4 `JacksonJsonWriterArrntvCloudTest` tests, all passing with zero failures, errors, or skips | if any count or result differs, record fresh XML counts and failure details, then stop without relaunching | Nick Shanin |
| Gate completion | decided: report every step and preserve the prior gate history | if all steps pass, mark the existing gate and this assignment DONE, write the new receipt section, and mark the claim DONE in the same push; otherwise record the failure in the gate and report, then close the claim with the hand-back | Nick Shanin |

## Deliverable

`reports/SOLR-17268-regate-9993394.md`: exact branch head, runner and log paths, result of every gate step, proof-leg test names and failure content, focused counts from fresh JUnit XML, final status, and pool commit. Update `gates/SOLR-17268.md` in the same push. On green, add a new top section to `receipts/SOLR-17268.md` with the head, host, date, log path, step results, both proof-leg results, and fresh focused counts.

## Limits

- Do not modify or push the ticket source branch. Report any source or test issue for the main agent to handle.
- If a test failure occurs after tests execute, do not relaunch. Record the result and stop.
- No PR, comment, JIRA or email. No force push.
- Commit pool records and the report as Nick Shanin with no trailers. No em dashes.
