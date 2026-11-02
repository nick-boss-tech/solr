# Claim: SOLR-18613 published-head gate execution (phone agent)

- Claimant host: phone-agent, matching `hosts/phone-agent.md`.
- Status: CLAIMED.
- Job: `gates/SOLR-18613-published-head-round-1.md`.
- Started (UTC): 2026-11-02T16:33:39Z. Authorized by Nick in the main chat
  on 2026-11-02 ("Yes claim it").
- Pinned source head H: `c2d2c6259462a7e848008b065d65df69a1b82ebb` on fork
  branch `solr-lb-attempt-limits-pr1-submit` (ls-remote verified at claim
  time; equals the gate's pinned head). Expected complete tree:
  `4d09e238ca0a7890e125b5f36d2dd78ff90f7110`. Production base:
  `0d6b2e32f598d772aed094038022a6a588fdfc5a`.
- Checkout: full clone at `~/workspace/solr-clones/18613-gate` (a real .git
  directory, not a worktree), detached at H.
- Toolchain: vendored JDK 21 and Gradle 9.7.0 via
  `~/workspace/tools/solr-gradle.sh`. Build lock: the host test-queue flock
  (`~/workspace/tools/.solr-test-queue.lock`), one build at a time.
- Runner: `~/workspace/tools/18613-gate-runner.sh`, output
  `~/workspace/tools/18613-gate-runner.out`; per-step logs and archived
  JUnit XML under `~/workspace/tools/18613-gate-*.log` and
  `~/workspace/tools/18613-gate-xml/`.
- Deliverables: `receipts/SOLR-18613.md` and an add-only completion report,
  written by this host after the run.
- Constraints from the job: no source or changelog edits; no upstream PR,
  public comment, or Jira edit. Repairs, if any leg fails, only per the
  job's repair rules with a superseding exact-head job.
