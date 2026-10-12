# Claim: SOLR-17612 multi-filter threshold fix and re-gate (shared buffer, vm2 coordinator)

Claimant host: vm2. Queue: assignments/pool-shared-gate-backlog-buffer.md (vm2 claims end to end). Job: gates/SOLR-17612.md.
Capability tags: implementation, gate. Staffing: 1.
Started: 2026-10-12T03:02Z (UTC, claim pushed before any code change; the first draft of this line said 03:15Z, which was wrong).
Branch: solr-17612-submit at dd2708fbb7e516fb8b6a593f96082a443e840b50 (live tip, verified by ls-remote at claim time).
Decision: material/spellcheck-round-5-answers.md, decision 10 (recommendation: fix in this branch).

Heartbeat: 2026-10-12T03:02Z. Code read next (SpellCheckComponent maxResultsByFilters path), then the implementation commit.

## 2026-10-12T03:12Z heartbeat (vm2 coordinator)

Implementation and test pushed to the fork as a fast-forward: solr-17612-submit at 6481b5865e932eed413d22337216d71abc249929 (from dd2708fbb7e). Tidy applied in the same commit. Gate next: changelog parse, tidy, compile, proof at baseline (runner /workspace/gates/g17612-gate.sh, log /workspace/gates/logs/vm2/17612-*.log).

## 2026-10-12T03:32Z FAILED at 6481b5865e (vm2 coordinator)

Gate ended 03:30:03Z: FAILED, not green. The focused head run (rc 1) and the baseline proof (rc 1) both fail with "Control data did not return any suggestions." in the control leg, so the new test does not reach the threshold assertion. Classification and evidence are in gates/SOLR-17612.md under "Gate run at 6481b5865e". Claim stays open, not DONE, and parked: no relaunch until the fixture change is decided and the investigation is recorded; the fixture choice is escalated to the main agent.

## 2026-10-12T04:22:56Z heartbeat (vm2 coordinator)

Fixture patch pushed at 3a456c27dd (see gates/SOLR-17612.md, "Patch at 3a456c27dd"). Re-gate queued after SOLR-8430 and SOLR-6711. Claim open.
