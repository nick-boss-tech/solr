# Claim: SOLR-10667 dotfiles premise run (vm2, gate backlog coordinator)

- Claimed: 2026-10-15T02:04:00Z by vm2. Job: gates/SOLR-10667-dotfiles.md.
- Head at claim: 9ce9ef1db5e9d33312ac628d2e3f0b236a474c36 (solr-10667-submit, live tip per the job file). Merge-base with main: cabedd1d968059215188f4e7563fb303241899ed.
- Capability tags: gate.
- Runner: gates/g10667-dotfiles-gate.sh, worktree /workspace/gates/solr-10667-dotfiles (detached). Log: gates/logs/vm2/10667-dotfiles-run.out.
- Scope of this claim: the premise run only (job steps 1 and 1b, record only). No branch change is made under this claim. If the cause sits in the copy spec, the fix is a follow-up under a new claim, after the record is read.
- Fork push: none under this claim.
- Heartbeat: refresh hourly; staleness rule 2 hours.
- Run: started 2026-10-15T02:05:07Z, GATE END 2026-10-15T02:18:24Z. Log: gates/logs/vm2/queue-g10667-dotfiles-gate-20261015T020507Z.out.
- Result: not GREEN. Premise assemble rc=0; the `.gitignore` presence probe reported the file absent in the assembled distribution (rc=1, 0 present, 1 absent), the six other tracked example files present. The liblinear link probe ran with rc=0 and the probe was removed (rc=0). The run did not isolate the cause of the absent file; the trace step only captured Gradle output.
- Status: RUN ENDED, record incomplete. Cause not recorded. Left for the main agent; no fork push, no branch change under this claim. Not DONE.
