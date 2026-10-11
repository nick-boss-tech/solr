# Pool assignment: flaky root-cause reading, GCSInstallShardTest failure on the SOLR-18506 branch

Capability tags: `review`. Staffing: up to 2. Claim path: claims/pool-flaky-rca-gcs-install-shard.md.

Background: the Solr Tests run 37384974639 on the SOLR-18506 branch (head 77c019e1ff0, live PR by this author) failed in GCSInstallShardTest's classMethod, not in the test the branch changes. The failure is recorded in the takeover log (2026-10-10 ~15:50 MDT entry) and in the goal's CI failure record. Classification on record: pre-existing status unverified, three-run check owed. This assignment is the reading half of that check; the runs themselves stay on the Linux hosts.

Work: read GCSInstallShardTest and the harness code its classMethod runs (setup, cluster boot, GCS fixture), on current main and at the branch head; identify what classMethod does before any test method, what shared state or external dependency it touches (credentials, network, ports, timing), and whether the branch's diff plausibly touches any of it. Rank the hypotheses (branch-caused, pre-existing flake, infrastructure) with the code evidence for each. Then write the exact three-run spec the Linux lane will execute: the failing test with its Crave seed (from the run record; if the seed cannot be recovered from the record, say so and spec a fixed seed) on pre-merge main, current main, and the PR head, with the pass condition for each classification.

Deliverable: reports/flaky-rca-gcs-install-shard.md with the analysis, the ranked hypotheses, and the run spec. No builds or test runs under this assignment. Rules: WORKFLOW.md binds (claim before work, mark the claim DONE in the same push as the deliverable).
