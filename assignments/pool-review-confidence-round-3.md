# Pool assignment: review confidence round 3 (live PRs beyond the update set, gate job files, near-opening drafts)

Capability tags: `review`. Staffing: up to 5. Claim path: claims/pool-review-confidence-round-3.md (slice-level claims fine).

Background: review rounds are stochastic; repeated independent passes compound the chance a defect is caught (round 2 of the confidence review and the flaky-fix rounds both found real defects after earlier passes). Round 2 covered 54 category drafts and the 28 update-processing PR bodies. This round covers what those did not: the other live PRs, the VM2 gate job files before VM2 spends gates on them, and the drafts nearest to opening. Read-only except the report and claim files. Verify any automated or reviewer comment against the code before reporting it as real.

Slice A: the live PRs outside the update set. For each: PR #4968 (SOLR-18178), #4997 and #5027 (SOLR-18391 pair), #4998 (SOLR-17539), #5000 (SOLR-18129), #5004 (SOLR-16130), #5009 (SOLR-18482), #5011 (SOLR-12849), #5012 (SOLR-13202), #5014 (SOLR-13568), #5015 (SOLR-13706), #5016 (SOLR-13097), #5028 (SOLR-18505), #5029 (SOLR-18506), #5030 and #5031 (SOLR-15823 pair), #5061 (SOLR-18119), #5062 (SOLR-18523). Check the body against its branch head and its receipt in receipts/: title accuracy, every Proof number traceable to the receipt at the head, citation links at the head SHA, Limits and Choice statements still true of the code, Lucene version mentions complete if any version is named, no internal process vocabulary. Verdict per PR: CONSISTENT or DRIFT with the exact line and fix.

Slice B: the VM2 gate job files. Audit every job in assignments/pool-vm2-gate-backlog-round-1.md and assignments/pool-vm2-gate-backlog-round-2.md and their gates/ files: the head SHA matches the receipt and the fork tip, the module and test class names match the branch tree (exact package), the premise instructions are runnable as written, and the expected outcomes match the ticket's TESTING.md or answers file. A wrong head or class name wastes a full gate; flag any mismatch as a job-file fix for the main side.

Slice C: near-opening drafts. SOLR-16630 (pr-drafts/flaky-fixes/SOLR-16630.md; its base-runs job is assignments/pool-solr-16630-base-runs.md), SOLR-12651 (draft in pr-drafts/, gated green at f3131d1ee846; Tomas Lobbe's restore-cleanup question must appear as a Choice), SOLR-17987 (Metrics round 1 draft; gated green at 38abf64231). Full formula check per draft: sections, bold summary lines, plain language, head-SHA citations, Proof numbers from the receipt only.

Deliverables: reports/review-confidence-round-3.md (roll-up) plus part reports per slice (review-confidence-round-3-a.md, -b.md, -c.md). Verdict counts up front. No PR edits, no branch edits, no builds.

Rules: WORKFLOW.md binds (claim before work, subagent cap of 6 to 7 across all hosts, mark the claim DONE in the same push as the deliverable).
