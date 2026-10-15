# Premise job: SOLR-15106 claim verification, the 1200 queued messages figure

- Branch: solr-15106-submit. Head: `40b7e5d0efa70d61b74b9fa9ac04beb1ed397637` (fork tip, verified by `git ls-remote` on 2026-10-14 at filing, unchanged since receipts/SOLR-15106.md). Merge-base: `9b3a84b1c460981eab09d8ffaef776acc4a184f8`.
- Status: OPEN, unclaimed. Claimable by either Linux host (vm1 or vm2). Claim under `claims/` before running anything. Windows never claims build work.
- Capability tags: premise-run.
- Job type: verification and records only. No branch code change is authorized by this job, no push, no PR change. The only texts it edits are the draft and the receipt on pr-prepare, per the outcome below.
- Why owed: `pr-drafts/solrcloud/SOLR-15106.md` line 9 states: "The ticket reports more than 1200 queued messages during a ZooKeeper outage." The openings slates doc (reports/morning-openings-slates-2026-10-14.md) flags the owner check: the ticket's 1200-message claim has no packet on the branch. The draft's own Limits say the ZooKeeper outage in the ticket is not reproduced and the test ends the processor thread directly. Nothing on the branch currently supports or refutes the number, and the draft cannot go out with the figure resting on nothing.
- Module: `:solr:core`. Focused class: `org.apache.solr.cloud.OverseerProcessorExitTest` (confirm the package by grep before any run).
- Out of scope: the slates doc's second SOLR-15106 item, the draft length measure, is a separate owner item and is not settled by this job.

## Steps, in order

0. Claim this job under `claims/` (host, date UTC, head, runner path). Verify the starting point: `git ls-remote` on the fork shows `solr-15106-submit` at `40b7e5d0efa70d61b74b9fa9ac04beb1ed397637`. Stop and record here if the tip differs; do not run at any other head.
1. Read the branch's test code at the head (`OverseerProcessorExitTest`, and the Overseer queue handling it exercises). Establish whether the test sends or queues any Collection API messages at all, and if it does, how the count arises: a fixed set of requests, a literal loop bound, or a dynamic count that depends on timing or retries.
2. If the message count is exact and static, establish it by code reading alone: record the number and cite the file and line ranges at the head that fix it. No run is needed for this leg.
3. If the count is dynamic, make it observable and run the focused class once, under the blocking flock (`~/workspace/tools/.solr-test-queue.lock`; one Gradle build at a time per host), through the host's standard wrapper (`solr-gradle.sh` on vm1, or the host's own wrapper per its host file on vm2, with `gradle.properties` copied into the worktree). Use only observability the code or the test already exposes (test output, an existing counter or queue-size read); do not edit branch code to add instrumentation. Count from the fresh JUnit XML or the test's own recorded output, and record the observed number with the run date (UTC) and seed.
4. Settle exactly one outcome and apply it:
   - Verified: the established number supports the draft's figure. Record the number and its basis (step 2 citations, or step 3's run) in receipts/SOLR-15106.md; leave the draft's sentence as it stands.
   - Corrected: the branch establishes a different number, or establishes that the test queues no messages of the kind the figure describes. Correct the draft's sentence to the established fact (the number the branch shows, or what the test actually does), and record the correction and its basis in the receipt.
   - Unverified: the number cannot be established from the branch (for example it rests only on the ticket's outage report, which the branch does not reproduce). Record the claim as unverified in receipts/SOLR-15106.md with the reason, and correct the draft so the figure stands only as the ticket's report, attributed as such, or is dropped; the draft must not present it as branch evidence.
5. A run that executed tests and failed is a result, not a launch failure: record it here and in the receipt, and do not relaunch. A run that never executed tests (an environment refusal: disk guard, journal lock timeout, flock starvation) may be relaunched once; record the refusal either way.
6. Stop condition: one of the three outcomes in step 4 is recorded with its basis in the receipt, and the draft matches it. Then stop. Any further question the run raises is a new job, not an extension of this one.

## On completion

- Refresh `receipts/SOLR-15106.md` with the outcome, the established number or the unverified record, and its basis.
- Align `pr-drafts/solrcloud/SOLR-15106.md` with the outcome (sentence corrected, attributed, or dropped; Limits adjusted only if the outcome changes what they say).
- Mark this job DONE with the date and the outcome, and mark the claim DONE.

## Checklist

- [ ] claimed under claims/, fork tip verified at 40b7e5d0efa7
- [ ] test code read; count characterised as none, exact and static, or dynamic
- [ ] number established by cited code reading, or by one instrumented run counted from fresh output
- [ ] outcome settled (verified, corrected, or unverified) and applied to the draft and the receipt
- [ ] job and claim marked DONE
