# Claim: PR 5241 core overview replication errors Linux gate, round 1 (vm4)

- Claimant host: vm4, matching `hosts/vm4.md`. Tags: gate, review.
- Started: 2026-10-27T02:52:48Z.
- Job: `gates/pr-5241-overview-errors-round-1.md` (OPEN, immutable, not edited). Ticket SOLR-16398.
- Fork: `nick-boss-tech/solr`, branch `solr-16398-submit`. Offered head `80ca9374f48f292af159c7e9600019b5947adc49` (verified by ls-remote at claim time). Original PR head and fail-before base `aa8272ef089c39df449c519551ef439557b876f8`.
- Implementation claim: `claims/pr-5241-overview-errors-windows-round-1.md` (Windows, implementation only).
- Host toolchain: Ubuntu 22.04 aarch64; JDK Temurin 21.0.12.1 at `/workspace/tools/jdk21`; Node v22.23.4; Gradle 9.7.0 through the repo wrapper.
- Browser: Chrome for Testing 156.0.8078.4, headless shell at `/opt/playwright-browsers/chromium_headless_shell-1248/chrome-headless-shell-linux-arm64/chrome-headless-shell`. Passed to the test JVM as `-Dtests.selenium.chrome.binary` and recorded in the receipt. Chromedriver through Selenium Manager (outbound network reachable).
- Build lock: `/workspace/tools/.gradle-vm4.lock` through `/workspace/tmp/pool-bin/sweep-run.sh`; one build at a time.
- Worktrees: positive `/workspace/worktrees/gate-pr5241` (detached at the offered head); fail-before `/workspace/worktrees/gate-pr5241-failbefore` (detached at `aa8272ef08`, overlaid with only the final test file).
- Logs: `/workspace/gates/logs/vm4/pr-5241-overview-round-1/`.
- Preflight at claim time: root `/` 38% used, 4.6G free; inodes 9%.
- Deliverables: `receipts/PR-5241-overview-errors.md` (new, gate host owns it); completion report `reports/pr-5241-overview-errors-gate-vm4-round-1.md`.

Status: CLAIMED until the receipt is pushed. If tidy changes files at the offered head, a formatting-only repair commit goes to the fork and a superseding gate is added under `gates/`.
