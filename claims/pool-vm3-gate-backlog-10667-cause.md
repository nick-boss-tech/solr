# Claim: pool-vm3-gate-backlog-10667-cause

Status: DONE 2026-10-18T08:19Z (NO BRANCH CHANGE)

- Claimant host: vm3, matching a file in `hosts/` (`hosts/vm3.md`).
- Job or assignment: `gates/SOLR-10667-dotfiles-cause.md` (head 9ce9ef1db5e9, ls-remote verified at claim time).
- Slice: the whole job. Takes over from `claims/pool-vm2-gate-backlog-10667-cause.md` (BLOCKED, STALE), which left the rule that writes the module example directory unlocated. The head's build text is re-checked first, since the vm2 record found none of the job's named exclude text at this head.
- Capability tags used: gate, premise-run.
- Started: 2026-10-18T07:42:08Z (UTC).

Heartbeat: 2026-10-18T07:42:08Z

Result: 2026-10-18T07:52Z: `:solr:packaging:installFullDist` at 9ce9ef1db5e9 (ls-remote verified), log `/workspace/gates/logs/vm3/10667-cause-dist.log`, rc=1, BUILD FAILED in 6m48s. No test executed. Failure is the non-branch environment pattern in the job file: `:solr:webapp:js-client:jsClientBuild` (npm run build) cannot find `@babel/plugin-syntax-dynamic-import` in `build/jsClientWorkspace`, although npm reported 178 packages added with the fresh `NPM_CONFIG_CACHE=/workspace/npm-cache/vm3-10667-cause`. Inconclusive for the cause trace (the module packaging output is not reached by this run's result, though assemblePackaging tasks ran). Step: one retry with a second fresh npm cache and the js-client build directory removed (git-ignored build output), recorded before the run; the job's stop rule applies only after a second identical environment failure.

Result: 2026-10-18T08:03Z: retry `:solr:packaging:installFullDist` at 9ce9ef1db5e9 with a second fresh cache (`/workspace/npm-cache/vm3-10667-cause-2`) and the js-client build directory removed, log `/workspace/gates/logs/vm3/10667-cause-dist-r2.log`, rc=1, BUILD FAILED in 4m36s, no test executed. Same failure: `Cannot find package '@babel/plugin-syntax-dynamic-import'`. The fresh cache did not clear it, so the job's stated remedy does not apply here.
Cause found (environment, not the branch): the shell sets `NODE_ENV=production`. `jsClientDownloadDeps` runs `npm install`, which under production omits devDependencies, and `solr/webapp/js-client/build/jsClientWorkspace/package-lock.json` marks `@babel/plugin-syntax-dynamic-import` as `dev: true` (221 of 334 locked packages are dev). The install left 91 top-level packages. The head's build text is not at fault.
Change before the next run: the same head, with `NODE_ENV` unset for the Gradle process only (`env -u NODE_ENV`), the same fresh npm cache (`/workspace/npm-cache/vm3-10667-cause-2`), and the js-client build directory removed. This is an environment change, so one rerun is allowed under the failed-runs rule. If it fails the same way, the environment block is recorded and the job stops.

Result: 2026-10-18T08:19Z: step 1 presence (run 10667-cause-dist-r3, rc=0): .gitignore present at source, absent at the ltr module packaging output and absent in the distribution. Mechanism: Gradle default excludes (Ant `**/.gitignore`) on the directory copy; the solrclient exclude is not the cause, and a single-file copy is excluded too. Step 1b: the distribution holds the liblinear probe link as a copied directory. Environment failures were NODE_ENV=production (see the gate file run record). No branch change.

DONE: 2026-10-18T08:19Z. Deliverables: `gates/SOLR-10667-dotfiles-cause.md` (run record and proposed draft wording), logs `/workspace/gates/logs/vm3/10667-cause-*.log`.
