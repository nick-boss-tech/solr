# Claim: SOLR-10667 dotfiles cause trace, step 1 and 1b (vm2, 2026-10-16T05:39Z)

- Claimed by vm2 under claims/pool-vm2-open-work-list-round-1.md (open-work item 4). Job: gates/SOLR-10667-dotfiles-cause.md, head 9ce9ef1db5e9d33312ac628d2e3f0b236a474c36 (ls-remote at claim time).
- Capability tags: gate, premise-run. Runner /workspace/gates/g10667-cause.sh; worktree /workspace/gates/solr-10667-cause (detached); seed 10667C0FFEE10667.
- Scope: the cause trace and the liblinear link record. No branch change unless step 3 of the job says so; if it does, the change is re-claimed under this claim before commit.
- Status: CLAIMED, runner queued.
- Status: BLOCKED (2026-10-16T05:59Z). Steps 1 and 1b run at 9ce9ef1db5e9. The file is absent from the module packaging output and the distribution, but the rule that feeds the module's example directory was not located; no branch change. Record in gates/SOLR-10667-dotfiles-cause.md.
