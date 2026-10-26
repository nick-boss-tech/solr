# Claim: vm2 for interrupted-close fix proof, round 1 (new branch, no PR)

- Claimant host: vm2. Started: 2026-10-26T03:32:28Z.
- Input: review round 2 (`reports/teardown-client-close-source-review-round-2.md`, commit 3733d701c4f) and the external-executor result (`reports/teardown-client-close-external-executor-round-1.md`, board commit caadee98de8).
- Source pin: apache/solr main `d298cd0b42602247bee7bf9e2bcf302e725f6690`.
- Scope: one fix in `HttpJettySolrClient.close()` (preserve the pre-existing interrupt across the Jetty stop, clear it only for the stop, restore it in finally) and focused tests that fail before and pass after. Covers owned and caller-owned executors, interrupt preservation, and a borrowed client.
- Branch: a new fork branch `solr-teardown-interrupt-close-fix` off the pin. No PR, no comment, no Jira. Owner approval needed before any PR.
- Runner: `pool/bin/sweep-run.sh` under `/workspace/tools/.gradle-vm2.lock`.
- Decision ladder: rung 4 (a choice among fix shapes), recorded in the report.
