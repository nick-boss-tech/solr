# SolrCloud drafts: Nick's decisions and verdicts, round 5

Verdict count after this round: 8 READY, 9 HOLD, 2 NOT READY. Nothing has been opened, posted or merged.

## Nick's decisions and what was done

1. **SOLR-11479, history rewrite.** Done. The tip commit's author and committer changed from the GitHub noreply address to `nick.boss.us@gmail.com`. The tree, parent, message and dates are unchanged. `solr-11479-submit` moved from `360dca60fcfa` to `6f739cfbbd3` with a lease push. The draft's links point at the new head. A carry-over job, `gates/SOLR-11479-identity-carry.md`, asks vm2 to confirm the trees match and to refresh the receipt.
2. **SOLR-16437, nightly run.** Assigned to vm2. `gates/SOLR-16437-nightly-run.md` is marked assigned by Nick. vm2 claims it under the WORKFLOW.md rules.
3. **SOLR-9155, wording.** Agents write it, in plain language. The ops-logging Limits bullet is rewritten. The branch already restores the base ERROR line (`29bf3738289`).
4. **SOLR-17680, more information.** Found by code reading. The v2 endpoint with two routers sends the top-level router name and reaches the same checks as v1. A category dimension needs `maxCardinality`, as a single category alias does. No test runs the two-router v2 path through the command. The draft now says this, and Limits names the gap. vm2 is asked to add the two cases and run them (`gates/SOLR-17680-v2-two-router-test.md`).
5. **SOLR-5813, vague sentence.** Found by code reading and the ticket. The branch changes one line, so an empty `collection` falls back to the core name. CoreAdmin STATUS shows the core name in `cloud.collection`, and replica registration uses it. The rejection route is described as the alternative. The vague sentence is gone.
6. **JIRA token.** The read helper now uses `dev/jira_token.env`. A read test passed.

## Verdict changes

- **SOLR-5813: HOLD** (was READY in round 3). Its Proof still carries the takeover-log date 2026-10-07. The dated run is in `gates/solrcloud-dated-runs-round-2.md`, item 1.
- **SOLR-12991: HOLD** (was READY in round 3). Its Proof carries the ledger date 2026-10-06. The dated run is in the same batch, item 2.
- **SOLR-17680: HOLD** on the vm2 test. Text is final; the test would add a Proof line and may tighten Limits.
- **SOLR-11479: READY** after the identity rewrite. The carry-over check is queued to vm2.

## All verdicts

| Verdict | Tickets |
|---|---|
| READY | 11479, 13186, 15106, 15386, 15863, 16013, 17292, 17733 |
| HOLD | 5813, 11288, 12651, 12991, 13369, 15035, 15674, 16437, 17680 |
| NOT READY | 9155, 14919 |

## Blockers, by gate

- SOLR-5813 and SOLR-12991: dated runs (batch job).
- SOLR-11288: base run (`gates/SOLR-11288-base-run.md`).
- SOLR-12651: history scrub (`gates/SOLR-12651-history-scrub.md`). The base run is now in the gate file and cited in the Proof.
- SOLR-13369: base-code run, which the Proof's base sentence needs (`gates/SOLR-13369-dated-run.md`, step 4).
- SOLR-15035: base-code run and banked-patch run (`gates/SOLR-15035-base-run.md`).
- SOLR-15674: scrub and re-gate (`gates/SOLR-15674-history-scrub.md`).
- SOLR-16437: changelog re-gate (`gates/SOLR-16437-changelog-regate.md`); nightly run assigned to vm2.
- SOLR-17680: v2 two-router test (`gates/SOLR-17680-v2-two-router-test.md`).
- SOLR-9155: re-gate at `29bf3738289` (`gates/SOLR-9155-logging-regate.md`).
- SOLR-14919: gate at `8f1f4ef91e79` stopped at the focused-head leg (`gates/SOLR-14919-processor-only-gate.md`).

## Not done

- No PR, comment, JIRA write, or email.
- No build, test, or gate run on this host.
