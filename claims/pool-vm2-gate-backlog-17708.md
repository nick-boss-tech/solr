# Claim: SOLR-17708 changelog correction and top-up gate

- Claimant host: vm3.
- Assignment: `assignments/pool-shared-gate-backlog-buffer.md`.
- Job: `gates/SOLR-17708.md`.
- Capability tags: `implementation`, `gate`.
- Started at: 2026-10-11T10:29:41Z (UTC).
- Slice: apply only the gate-prescribed changelog title replacement, preserving `type`, `authors`, and `links`; then complete the prescribed top-up gate at the new branch head. Do not edit source or open/update a PR.
- Branch: `solr-17708-submit`, live packaged starting head `10a7fa07a79ac4b4b52a83d307ee65349d015a23`, verified by fetch at 2026-10-11T10:28:43Z (UTC).
- Worktree: `/workspace/gates/solr-17708-build` (isolated task checkout; clean after tidy).
- Logs: `/workspace/gates/logs/vm3/17708-*.log`.
- Status: CLAIMED. The title correction `2eceb54730373475e9adfe55bfe555744fb3724a` is live; tidy and Error Prone compile passed, and the merge-base proof established the expected single failure; next is the focused head tests.
- Heartbeat: 2026-10-11T10:32:31Z (UTC), packaged head verified live; next step is changelog YAML parse.
- Heartbeat: 2026-10-11T10:33:58Z (UTC), changelog validator passed 315/315 fragments at live head `2eceb54730373475e9adfe55bfe555744fb3724a`, including the corrected SOLR-17708 entry; logs retained. Next: tidy and confirm clean tree before compile.
- Heartbeat: 2026-10-11T10:38:31Z (UTC), root tidy passed in 1m24s with 62 actionable tasks; source checkout is clean and no tests ran. Disk: 21 GB available; free inodes decreased from 54,721 to 52,130. Next: Error Prone compile.
- Heartbeat: 2026-10-11T10:45:22Z (UTC), `:solr:core:compileTestJava -Pvalidation.errorprone=true` passed in 4m39s (18 actionable tasks, 14 executed and 4 up-to-date); no tests ran. Disk: 21 GB available; free inodes decreased from 52,113 to 44,793. Checkout clean; post-run check found sleeping workers/daemons only. Next: proof at merge-base with branch test class kept.
- Heartbeat: 2026-10-11T10:50:55Z (UTC), merge-base `9b3a84b1c460981eab09d8ffaef776acc4a184f8` proof ran the copied branch test class: 2 tests, 1 expected failure (`testJaxRsApiIsAuthorizedOnce`, expected 1 but was 2), denial guard passed, 0 errors/skips. Fresh JUnit and output retained under `/workspace/gates/logs/vm3/17708-proof*`. Disk: 21 GB available; free inodes fell from 34,700 to 24,207. Next: focused head tests.
