# Claim: vm2 open items handed to the review agent, round 1

- Claimed by: windows (review coordinator), Claude Code on this host, working for Nick Shanin
- Capability tags: review, implementation
- Assignment: `assignments/pool-vm2-open-items-handoff-round-1.md` (posted 2026-10-14 in commit `eb39b9b75c2`). Deliverable: `reports/vm2-open-items-handoff-round-1.md`.
- Trigger: the two-hourly scheduled check. Recorded tip `63a4d1e4d2a`; live tip at the check `bb6b287d339`. The new commits are this session's own review outputs, the assignments posted by the main side (the vm2 open-items handoff, SOLR-18317 coverage check, SOLR-4841 follow-up), the overlap check, and the SOLR-16630 close-out. The SOLR-18317 and SOLR-4841 assignments are not claimed here; the owner said they can wait.
- Fork tips at the check: `solr-17055-submit` `c2396f425fa2`; `solr-15906-submit` `50139a8e979c`; `solr-11356-submit` `8b2e826154af`; `solr-16725-submit` `be1838ef8ccf`; `solr-8628-submit` `5b606dc779d0`; `solr-14919-submit` `483b3885f643`; `solr-17733-submit` `cf1b28327264`.
- Stale handoff references: the assignment's item 5 names `8f1f4ef91e` and `636196b7954a` as the starting heads. The live tips are the landed heads recorded in round 1 (`483b3885f64` and `cf1b283272`). The subagent must verify whether each patch is already on the live tip before it pushes anything.
- Scope this round (six items, three subagents):
  - S1: item 1, the SOLR-17055 sort-path fix on `solr-17055-submit`, the sort case in `DistributedKnnTopKTest`, and its gate job.
  - S2: item 2, the SOLR-15906 sort-paren fix on `solr-15906-submit` and its gate job; item 3, the SOLR-11356 header-copy test, its patch applied, and the fork fast-forward push.
  - S3: item 4, the SOLR-16725 branch-edit wording; item 5, verify the SOLR-14919 and SOLR-17733 landed heads; item 6, the SOLR-8628 stale-claim read (read only).
- Gate jobs: every change to a branch head needs a gate job for vm2 at the new head. Windows runs no builds or tests.
- Limits: no PR opened, closed, flipped or commented on; no live PR body edited; no Jira or email; no force-push; fork pushes only where the assignment names one, as plain fast-forwards through `push-fork-ff.ps1`; nothing posted.
- Staffing: three subagents. Each may write only the files its items name, and gate job files under `gates/`.
- Heartbeat: 2026-10-14T20:37Z (claim taken).
- Status: IN PROGRESS.
