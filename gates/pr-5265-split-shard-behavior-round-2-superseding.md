# Gate: split-shard migration behavior repairs, round 2 (superseding, tidy repair)

Status: CLAIMED by phone agent (claim: `claims/pr-5265-split-shard-behavior-round-2-phone-agent.md`); running.

- Supersedes: `gates/pr-5265-split-shard-behavior-round-2.md` at candidate `b1f09ab79a3d3f1943e62898512447f836342b34`. That gate's tidy step reformatted three branch files (line rewraps only: `AdminAPIBase.java`, `SplitShardAPITest.java`, `SplitShardMessageMappingTest.java`; no semantic change), so per that job's own rule the repair was committed and proof restarts at the new head.
- New candidate: `009f2c60513636b4aba2c93f4a4137aa39ccea64` on `nick-boss-tech/solr:solr-16398-split-shard` (`b1f09ab79a` plus tidy commit `009f2c6051`, author Nick Shanin). Remote head and live PR #5265 head to be re-verified at the end of the run.
- Everything else follows the superseded job exactly: positive proof steps 1 to 6 (tidy clean at the head, Error Prone compile with OpenAPI regeneration, focused plus neighboring suites with seed `16398C0DE`, `:solr:core:check -x test`, `git diff --check`), then the three controlled mutation legs (timeout, timing, validation) against the new candidate, restoring each mutant and finishing with the positive focused suite at the unmodified candidate.
- Logs: `~/workspace/tools/gate-5265-r2/` on the phone agent host (phase files prefixed `r2b-`).
- Deliverables: `reports/pr-5265-split-shard-behavior-round-2-linux.md` and, only if every leg passes, `receipts/pr-5265-split-shard-behavior-round-2.md`. No PR body edit, GitHub comment/review, or JIRA write.
