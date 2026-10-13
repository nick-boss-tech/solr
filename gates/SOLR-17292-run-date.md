# Gate job: SOLR-17292 dated run for the record

- Branch: solr-17292-submit. Head: `f614a42fbc8046e47cb7ed216f6b52b8a01f594c` (receipts/SOLR-17292.md, GATE GREEN at the live tip).
- Status: OPEN, unclaimed. Claim before work: `claims/pool-vm2-gate-backlog-17292-run-date.md`.
- Capability tags: gate. Intended host: vm2, or vm1 if vm2 is busy. Windows never claims build work.
- Why: the receipt records the re-gate as finished 2026-10-10, taken from the main side's receipts ledger and takeover log. It gives no run date of its own. The batch B review (`reports/solrcloud-ready-review-batch-b-round-1.md`, SOLR-17292 section) held the ticket on that gap. The draft's Proof cannot cite a run date until a dated run is recorded.
- Job type: record only. No branch change, no fork push, no PR touched.

## Steps

1. Confirm the head: `ls-remote origin refs/heads/solr-17292-submit` must equal `f614a42fbc8046e47cb7ed216f6b52b8a01f594c`. If it differs, stop and record the new tip in this file.
2. Find the gate log `g17292-regate.log` on the host that ran the re-gate. Record its path. Check whether it holds a GATE START line and a GATE END line, each with a UTC time.
3. Outcome A, the log exists with both dated lines: copy the start time, the end time, the seed (17292C0FFEE17292) and the counts into a new section "Dated run of record" in `receipts/SOLR-17292.md`. Name the log path. Run nothing.
4. Outcome B, the log is gone or has no dates: run the focused class at the head, from fresh JUnit XML. Confirm the module and the package by grep at the head before the run. The WORKFLOW example names `:solr:solrj-zookeeper:test` and `org.apache.solr.common.cloud.TestPerReplicaStates`; use that only if the grep agrees. Record the counts, the seed, the run's own start and end time, and the log path in the same new receipt section. The revert proof is already recorded in the receipt and is not re-run.
5. Mark this job DONE with the outcome (A or B) and the receipt section name. Mark the claim DONE.

## Expected result

TestPerReplicaStates 4 of 4 at `f614a42fbc8`, with a dated run. A count other than 4 of 4 is recorded as is and the job stops there. The main side decides what follows.

## Step checklist

- [ ] head confirmed by ls-remote
- [ ] gate log located, with its path recorded
- [ ] outcome A (dated log copied) or outcome B (focused run with fresh JUnit XML)
- [ ] receipt section "Dated run of record" written in receipts/SOLR-17292.md
- [ ] job and claim DONE
