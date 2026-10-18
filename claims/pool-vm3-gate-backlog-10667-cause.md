# Claim: pool-vm3-gate-backlog-10667-cause

Status: ACTIVE; mark DONE with the deliverable paths when the work lands.

- Claimant host: vm3, matching a file in `hosts/` (`hosts/vm3.md`).
- Job or assignment: `gates/SOLR-10667-dotfiles-cause.md` (head 9ce9ef1db5e9, ls-remote verified at claim time).
- Slice: the whole job. Takes over from `claims/pool-vm2-gate-backlog-10667-cause.md` (BLOCKED, STALE), which left the rule that writes the module example directory unlocated. The head's build text is re-checked first, since the vm2 record found none of the job's named exclude text at this head.
- Capability tags used: gate, premise-run.
- Started: 2026-10-18T07:42:08Z (UTC).

Heartbeat: 2026-10-18T07:42:08Z

Result: 2026-10-18T07:52Z: `:solr:packaging:installFullDist` at 9ce9ef1db5e9 (ls-remote verified), log `/workspace/gates/logs/vm3/10667-cause-dist.log`, rc=1, BUILD FAILED in 6m48s. No test executed. Failure is the non-branch environment pattern in the job file: `:solr:webapp:js-client:jsClientBuild` (npm run build) cannot find `@babel/plugin-syntax-dynamic-import` in `build/jsClientWorkspace`, although npm reported 178 packages added with the fresh `NPM_CONFIG_CACHE=/workspace/npm-cache/vm3-10667-cause`. Inconclusive for the cause trace (the module packaging output is not reached by this run's result, though assemblePackaging tasks ran). Step: one retry with a second fresh npm cache and the js-client build directory removed (git-ignored build output), recorded before the run; the job's stop rule applies only after a second identical environment failure.
