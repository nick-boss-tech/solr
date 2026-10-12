# Claim: SOLR-11129 offset fix and re-gate (vm1)

- Claimed: 2026-10-12T14:24Z by vm1. Job: gates/SOLR-11129-offset-fix.md.
- Fork tip read at claim time: 6c1356bdff706328a8b78d39782f9e94845827d4 (solr-11129-submit, ls-remote 2026-10-12T14:23Z). Plain fast-forward push; no force.
- Capability tags: implementation, gate. Staffing: 1.
- Heartbeat: refresh hourly; staleness rule 2 hours.
- Status: DONE 2026-10-12 by vm1. GATE GREEN at dc82676b8096ed85cb018d4e5b80f6855e5d26d2; branch pushed (fast-forward 6c1356bdff..dc82676b80); receipt refreshed; draft updated and HOLD comment removed; job marked DONE. Logs on vm1: g11129-offset-gate.log, g11129-offset-prefix.log, g11129-offset-harden-prefix.log.
- Heartbeat 2026-10-12T14:24Z: claim filed, work starting.
- Heartbeat 2026-10-12T14:47Z: fix committed at dc82676b8096 (pre-fix proof leg failed as designed, premise message on record); full gate queued behind the vm1 build lock.
- Heartbeat 2026-10-12T15:03Z: gate green, branch pushed, records updated; claim closed.
