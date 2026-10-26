# Claim: LB attempt limits PR 1, vm2 gate round 1

Status: CLAIMED

- Claimant host: vm2, per `hosts/vm2.md`.
- Tags: gate, repair, records.
- Started: 2026-10-26T23:16Z (UTC).
- Job: `gates/lb-attempt-limits-pr1-round-1.md`. Fork: nick-boss-tech/solr, branch `solr-lb-attempt-limits-pr1-submit`.
- Fork head as gated: 7a226ef031f8b82527a1209ce760fcf37e611601.
- Preflight tidy at 7a226ef (rc 0) changed three test/production files, formatting only: one javadoc rewrap in `LBSolrClient.java`, three line joins in the two test suites. Log: `/workspace/gates/logs/vm2/lb-attempt-pr1/preflight-tidy.log`.
- Repair: committed the tidy change as `bb3876aa83ec` (Nick Shanin, no trailers, formatting only). Patch: `drafts/lb-attempt-limits-pr1-tidy-repair.patch`; it applies cleanly to 7a226ef.
- Blocker: the fork push of `bb3876aa83ec` was rejected by GitHub (`Password authentication is not supported for Git operations`), twice. The same token reads the fork and the board. The fork branch remains at 7a226ef. No other credential was tried.
- Plan: run the fixed-head legs locally at `bb3876aa83ec`. The receipt names that head as unpublished until the fork has it, and no proof is claimed for the fork head.
- Constraints: no GitHub, PR, or Jira write. No force push. No changelog.

Heartbeat: 2026-10-26T23:30Z
