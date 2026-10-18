# Claim: pool-vm2-gate-backlog-6009-edismax-p3

- Claimant host: vm2, matching a file in `hosts/` (`hosts/pool-linux.md`, host label vm2).
- Status: DONE 2026-10-18T08:57Z (GATE GREEN at b7695a518cccc0158a6374c61af9760884d1cb80). Gate job for SOLR-6009.
- Job or assignment: `gates/SOLR-6009-edismax-p3.md`.
- Slice: the whole gate job (step 0 changelog parse, step 1 tidy, step 4 focused run, step 5 module check).
- Capability tags used: gate.
- Started: 2026-10-18T08:40:27Z (UTC).

Setup: 2026-10-18T08:40Z: `~/.pool-host` and `~/.pool-env` were missing on this VM. `~/.pool-host` was written with `vm2`, matching `/workspace/.pool-host` and AGENTS.md. `~/.pool-env` sets JAVA_HOME=/workspace/jdk/jdk-21.0.12.1+1 (Temurin 21.0.12.1, verified) and GRADLE_USER_HOME=/workspace/gradle-home.

Head check: 2026-10-18T08:40Z: `origin/solr-6009-submit` is b7695a518cccc0158a6374c61af9760884d1cb80, matching the job's Head line.

Heartbeat: 2026-10-18T08:40:27Z

Result: 2026-10-18T08:43Z: step 0 changelog YAML parse of `changelog/unreleased/SOLR-6009-edismax-regexp.yml` at b7695a518c: rc 0. Diff vs a41bb034a1f4 is that one file, 2 insertions and 2 deletions.

Result: 2026-10-18T08:46:41Z: step 1 tidy at b7695a518c, `sweep-run.sh ... tidy tidy --no-daemon`: rc 0, BUILD SUCCESSFUL, tree clean afterward. Log `/workspace/gates/logs/vm2/6009/tidy.log`. Conclusive.

Result: 2026-10-18T08:52:31Z: step 4 focused head run at b7695a518c, `:solr:core:cleanTest :solr:core:test --tests org.apache.solr.search.TestExtendedDismaxParser -Ptests.seed=6009C0FFEE6009`: rc 0, PASS. Fresh XML: tests 41, skipped 0, failures 0, errors 0. Log `/workspace/gates/logs/vm2/6009/focused.log`, XML `focused.xml/`. Conclusive.

Result: 2026-10-18T08:57:02Z: step 5 module check at b7695a518c, `:solr:core:check -x test`: rc 0, BUILD SUCCESSFUL. Log `/workspace/gates/logs/vm2/6009/check.log`. Conclusive. Gate GREEN.

Deliverable: `receipts/SOLR-6009.md` (new top section), `gates/SOLR-6009-edismax-p3.md` (Status DONE).

DONE: 2026-10-18T08:57Z. Deliverables: `receipts/SOLR-6009.md`, `gates/SOLR-6009-edismax-p3.md` (GATE GREEN at b7695a518cccc0158a6374c61af9760884d1cb80).
