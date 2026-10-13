# Job: SOLR-18280 seed and reproduce line for the failing nightly head run (read-only)

- Branch: solr-18280-submit at `0da92abd9e96` (no branch change). Status: DONE 18:28Z by vm2 (gate backlog coordinator). Seed, failure and reproduce line recorded in receipts/SOLR-18280.md, section "Failing nightly head run: seed and reproduce line".
- Intended host: vm2, because the logs are there. Windows cannot read them.
- Not a gate run. Do not relaunch any test. The head rerun already ran and passed (2026-10-13, 14:11Z to 14:23Z, 25 of 25, seed `18280C0FFEE18280`).

## Read

- The failing nightly head run of 2026-10-13 at 12:55Z (gate `g-prdrift2-gate`, log `logs/vm2/queue-g-prdrift2-gate-20261013T122045Z.out`) and its fresh JUnit XML under `logs/vm2/prdrift-xml/18280-head-nightly/`.

## Record (in `gates/SOLR-18280-nightly-record-run.md` completion note and `receipts/SOLR-18280.md`)

- The seed, from the XML properties for that run.
- The failing test name: `doTestIndexAndConfigAliasReplication`.
- The failure text and line: "timed out waiting for collection1 startAt time to exceed", `TestReplicationHandler.java:1770`, 36 seconds.
- The reproduce line: `-Ptests.nightly=true -Ptests.seed=<seed>` with the focused class `org.apache.solr.handler.TestReplicationHandler`.
- The classification: clock-dependent timing flake, one failure in two valid head runs, unless the XML shows a different failure. If it does, record that and stop; do not relaunch.
