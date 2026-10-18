# Claim: pool-vm3-gate-backlog-6320-edismax-p4

- Claimant host: vm3, matching `hosts/vm3.md`.
- Status: DONE 2026-10-18T09:21Z. Deliverables: `receipts/SOLR-6320.md` (new top section), `gates/SOLR-6320-edismax-p4.md` (Status: DONE).
- Job or assignment: `gates/SOLR-6320-edismax-p4.md`.
- Slice: the whole job (steps 0 to 5, receipt on green).
- Capability tags used: gate.
- Started: 2026-10-18T08:55:02Z (UTC).
- Head checked at claim time: `solr-6320-submit` at 3a35d800a2e5ea9e14632733110973d5caee70e8 (ls-remote matches the job's Head line).
- Disk at claim time on `/`: 4.6 GB free; inodes 91% free.

Heartbeat: 2026-10-18T08:55:02Z (claim).

Result: 2026-10-18T09:02Z: tidy (`:tidy`, rc=0, 2m 53s), tree clean afterwards, log `/workspace/gates/logs/vm3/g6320p4-step1-tidy.log`. Conclusive.
Result: 2026-10-18T09:09Z: Error Prone compile (`:solr:core:compileTestJava -Pvalidation.errorprone=true`, rc=0, 6m 17s), log `/workspace/gates/logs/vm3/g6320p4-step2-errorprone.log`. Conclusive.
Result: 2026-10-18T09:15Z: focused head run at 3a35d800a2e5ea9e14632733110973d5caee70e8 (`:solr:core:clean :solr:core:test --tests "org.apache.solr.search.TestExtendedDismaxParser" -Ptests.seed=6320C0FFEE6320`, rc=0, 4m 33s). Fresh JUnit XML: 44 tests, 0 failures, 0 errors, 0 skipped. Log `/workspace/gates/logs/vm3/g6320p4-step4-focused.log`, XML `/workspace/gates/logs/vm3/g6320p4-step4-focused.xml/`. Conclusive: green, count matches the expected 44.
Result: 2026-10-18T09:20Z: module check (`:solr:core:check -x test`, rc=0, 3m 49s), log `/workspace/gates/logs/vm3/g6320p4-step5-check.log`. Conclusive.
Heartbeat: 2026-10-18T09:21Z (receipt written, gate closed green).

DONE: 2026-10-18T09:21Z. Deliverables: `receipts/SOLR-6320.md`, `gates/SOLR-6320-edismax-p4.md`.
