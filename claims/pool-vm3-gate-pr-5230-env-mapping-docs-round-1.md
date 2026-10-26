# Claim: PR 5230 environment-mapping docs gate, round 1

- Claimant host: vm3, matching `hosts/vm3.md`.
- Status: ACTIVE. Mark DONE with the receipt and report paths when the gate lands.
- Job or assignment: `gates/pr-5230-env-mapping-docs-round-1.md`.
- Slice: the whole gate job (exact-head tidy preflight, three Ref Guide tasks, EnvUtils behavior probes at the Solr 10.0.0 baseline, and startup-script module checks).
- Capability tags used: gate, premise-run.
- Started: 2026-10-26T01:44:02Z (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat, and then takeable over under WORKFLOW.md, Claims. Attempts: 1. Idempotency key: pr-5230-env-mapping-docs-round-1@bf285189f74b775165fc53ba305d35dccf880a47.
- Pinned documentation head: `bf285189f74b775165fc53ba305d35dccf880a47` on fork branch `solr-17988-docs` (checked by `git ls-remote` at claim time).
- Behavior baseline: Solr 10.0.0 release `6c6c48a6f78486130682ea9c1f7a2723af5a87be`.
- Runner and lock: sequential commands run in the foreground inside this session, serialized with `flock /workspace/tools/.vm3-gradle.lock`.
- Standalone clone: `/workspace/pr5230-docs-standalone` (own `.git` directory, not a linked worktree).
- Log directory: `/workspace/gates/logs/vm3/pr-5230-env-mapping-docs-round-1/`.
- Receipt: `receipts/SOLR-17988.md` (none existed on the board at claim time).
- Links: Ticket or parent: SOLR-17988 / https://github.com/apache/solr/pull/5230. Category: documentation (`pool/work-map.md` is not on `coordination-ui`; no category file to cite). Siblings: `claims/solr-17988-docs-refguide-build-round-3-done.md` (round 3 at `16d3005293d3`, now superseded for this head), `claims/pr-5230-env-mapping-windows-round-1.md` (the Windows correction that produced this head). Supersedes: none.

Heartbeat: 2026-10-26T01:44:02Z

Heartbeat: 2026-10-26T02:06:00Z

Attempt 1 at `bf285189f74b775165fc53ba305d35dccf880a47`, standalone clone `/workspace/pr5230-docs-standalone`:
- Tidy preflight (`./gradlew tidy`): exit 0, `git diff --exit-code` exit 0, clean status. Logs: `tidy.log`, `tidy-diff.txt`, `tidy-status.txt` in the log directory.
- `:solr:solr-ref-guide:buildLocalSite`: exit 1. `:solr:solr-ref-guide:checkSiteLinks`: exit 1. Both fail in Antora with `Cannot find module '@asciidoctor/tabs'`.
- `:solr:solr-ref-guide:checkCliDocsUpToDate`: exit 0 (does not run Antora).
- Investigation: the task `downloadAsciidoctorTabsExtension` runs `npm install -D @asciidoctor/tabs@1.0.0-beta.6` and npm reports "changed 1 package", but the module is absent from `.gradle/node/node_modules`. The host shell has `NODE_ENV=production`, under which npm omits devDependencies, and the tabs package is a devDependency. The failure is in the host environment. The PR head and the repository are unchanged, and no repair to tracked files is needed.
- Repair: rerun the three tasks at the same head with `NODE_ENV` unset for the Gradle run only. Failed logs are kept in `attempt-1-NODE_ENV-production/`. Tidy stays valid for this head and is not repeated.

Attempt 2 (`NODE_ENV` unset, daemon reused from attempt 1): tidy not repeated. `buildLocalSite` exit 1 and `checkSiteLinks` exit 1 again with `Cannot find module '@asciidoctor/tabs'`. `checkCliDocsUpToDate` exit 0. Investigation: the log shows `downloadAsciidoctorTabsExtension UP-TO-DATE`. Gradle kept attempt 1's up-to-date record for a task whose output directory was never created, so npm did not run again. Repair: attempt 3 runs `downloadAsciidoctorTabsExtension --rerun --no-daemon`, checks that `node_modules/@asciidoctor/tabs` exists, then runs the three tasks with `--no-daemon` so the Gradle process does not inherit the older environment. Attempt 2 logs are kept in `attempt-2-tabs-up-to-date/`.

Attempt 3 (`--rerun` for the tabs task, `--no-daemon` for all Gradle runs): `buildLocalSite` exit 0, `checkSiteLinks` exit 0 (no errors, no warnings), `checkCliDocsUpToDate` exit 0, tracked tree clean after the run. Logs in the log directory: `buildLocalSite.log`, `checkSiteLinks.log`, `checkCliDocsUpToDate.log`, `steps-attempt-3.tsv`.

Heartbeat: 2026-10-26T02:30:00Z: probes, launcher checks and receipt written.

Result: 2026-10-26T02:30:00Z: receipt `receipts/SOLR-17988.md` holds all step results, the probe and launcher evidence, and the corrected PR proof paragraph.

DONE: 2026-10-26T02:30:00Z. Deliverables: `receipts/SOLR-17988.md`, `gates/pr-5230-env-mapping-docs-round-1.md` (Status DONE), and this claim. Log directory: `/workspace/gates/logs/vm3/pr-5230-env-mapping-docs-round-1/`.
