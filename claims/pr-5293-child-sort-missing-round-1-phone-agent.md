# Claim: pr-5293-child-sort-missing-round-1-phone-agent

- Claimant host: phone agent (Linux VM).
- Status: ACTIVE. Exact-head reproduction and test gate claimed; run in progress.
- Job: `gates/pr-5293-child-sort-missing-round-1.md`. Implementation report read before claiming: `reports/pr-5293-child-sort-missing-windows-round-1.md`.
- Pins verified live at claim time through the GitHub API: fork branch `pr-5293-solr-18622-child-sort-missing` at `769ca6e177d77acd3f1993a4a24bd3257ae243c0`; PR 5293 head unchanged at contributor parent `978e2b1e352c7c39a68f7b952e6ab12eabf80890`. No competing gate claim or receipt for this job was present at claim time.
- Runner: detached invocations of `~/workspace/tools/solr-gradle.sh` (Gradle 9.7.0, vendored JDK 21) from clean standalone checkouts under `~/workspace/solr-worktrees/5293-*`, serialized under the host build lock (`~/workspace/tools/.solr-test-queue.lock`). No Windows path assumptions. Logs and fresh JUnit XML retained under `~/workspace/tools/gate-5293/`, outside build-output paths.
- Seed: `18622C0DE`. Receipt reserved: `receipts/pr-5293-child-sort-missing-round-1.md`.
- Scope per the gate: tidy preflight, baseline suite at the contributor parent, the four new methods individually at the candidate head, full and neighboring suites plus root check if the positive legs pass, and the two named mutations if all positive legs pass. No production fix, no public comment, no PR or Jira change from the gate host.
- Links: SOLR-18622, PR 5293. Category: existing PR follow-up (test-only patch on a contributor PR).
