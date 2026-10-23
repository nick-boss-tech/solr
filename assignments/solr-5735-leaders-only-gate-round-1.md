# Pool assignment: SOLR-5735, repeat the leaders-only safe-leader runs on another host

Status: DONE

- Capability tags: gate, premise-run.
- Category: Solr core, SolrCloud tests.
- Intended host: any pool Linux host with a local (non-network) disk. Builds go through `pool/bin/sweep-run.sh`.
- Posted by: vm2, 2026-10-23, at the direction of Nick.

## Why this is posted

On `b73f03976fa17b857bc0a81bc8af2c6b1c31e0bb`, `ChaosMonkeySafeLeaderWithPullReplicasTest` with seed `E4EFD2B86C0B81D4` fails with `expected:<0> but was:<1>`. The failed request was a delete sent straight to a pull replica, which the monkey then stopped. The test builds its cloud client with a random choice between "updates to shard leaders only" and "updates to any replica", and the safe-leader tests only spare leaders. Branch `solr-5735-safeleader-leaders-only` (head `60eb0e7fd0521b8d866443a7b72dd623bacd884d`) makes both safe-leader tests use the leaders-only client. vm2 has run it on its own disk; this repeats it elsewhere, since the failure depends on timing.

## Items

1. On the branch head, run `ChaosMonkeySafeLeaderWithPullReplicasTest` with `-Ptests.nightly=true -Dtests.seed=E4EFD2B86C0B81D4` three times. Record pass or fail for each.
2. On the branch head, run `ChaosMonkeySafeLeaderWithPullReplicasTest` and `ChaosMonkeySafeLeaderTest` with `-Ptests.nightly=true` and no seed, three times each. Record the seed of each run and the result.
3. For any failure, record the failing assertion and the URL the failed request was sent to (grep `REQUEST FAILED` and the `talking to server at` line in the test output).

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Fix the test or the product | decided: none in this assignment | findings only; no code change | Nick |
| Post to Jira or GitHub | decided: no | do not post | Nick |
| Disk | decided: local disk only | if the host's workspace is a network file system, run in a local temporary directory; the pull-replica copy fails with "Too many links" on a network file system | Nick |

No row may leave the choice to the claimant.

## Deliverable

`reports/solr-5735-leaders-only-gate.md` with a table of run, seed, result, and the failing request URL for each failure. Mark the claim DONE in the same push.

## Limits

- No Jira or GitHub posts or comments. No product code change. No force push.
- Commit as Nick Shanin with no trailers. No em dashes in anything authored.
