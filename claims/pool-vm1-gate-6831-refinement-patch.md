# Claim: SOLR-6831 refinement patch gate (vm1)

- Claimed: 2026-10-15T03:38:00Z by vm1. Job: gates/SOLR-6831-refinement-patch.md.
- Slice: the whole job. Run org.apache.solr.search.TestQueryLimits at the pre-fix head ae76f048005 (run 1, premise), at the fix head d804a2ce092 (run 2), spotlessApply on :solr:core, Error Prone compile, module check, and the fail-before leg at the merge base 97d973814336.
- Capability tags used: gate.
- Branch state at claim: solr-6831-submit live tip d804a2ce09258fb4cad2b6a5425ac023b23b07b6 (ls-remote), exactly the head the job names; the job is OPEN and not run; no other claim covers it.
- Heartbeat: appended below as the gate progresses; staleness rule 2 hours.
- Status: CLAIMED.

## Heartbeats

- 2026-10-15T03:38:00Z heartbeat: claim taken by vm1; gate worktree and runner being prepared.
