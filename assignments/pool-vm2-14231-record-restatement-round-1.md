# Assignment: SOLR-14231 record restatement, round 1

Status: DONE

Capability tags: [records]. Intended host: vm2. Posted by the main side, 2026-10-14, at Nick's direction: anything the main side needs from vm2 gets posted as an assignment on pr-prepare.

Claim first: before starting, file a claim under `claims/` naming this assignment (for example `claims/pool-vm2-14231-record-restatement-round-1.md`), per WORKFLOW.md. Heartbeat and staleness follow the pool rules.

This is a records task over an existing run log. No builds and no test runs are asked for. Do not relaunch anything.

## Background

The SOLR-14231 fail-before job is finished. The main side confirmed the run on 2026-10-14: `gates/SOLR-14231-fail-before.md` and its claim stand DONE, and the confirmation sections there and in `receipts/SOLR-14231.md` record the per-test verdicts (all three added tests failed on the proof base with the designed messages, and the same tests pass at the head). That confirmation is settled and does not reopen here.

Two record defects were left open in that confirmation because the evidence lives only on vm2: the raw run log at `/workspace/gates/logs/vm2/queue-g14231-fail-before-gate-20261014T061650Z.out` (run 2026-10-14T06:16:50Z to 06:26:59Z, seed 14231C0FFEE14231, proof base b5c71bc5573c). This assignment closes both defects from that log, plus one housekeeping item.

## Item 1: restate the base leg counts

The base leg's GraphExpressionTest JUnit XML attributes say tests 10, failures 4, but the XML carries only 3 failure elements, so the aggregate and the per-test verdicts on record do not agree. From the raw XML and the log on vm2:

1. State the correct test and failure counts for that base run, and name the testcases the 3 failure elements belong to.
2. Correct the numbers in `gates/SOLR-14231-fail-before.md` and `receipts/SOLR-14231.md` so the aggregate counts and the per-test verdicts agree. The per-test verdicts themselves are confirmed and do not change; only the aggregate restatement moves.

## Item 2: record the ObjectTracker complaint

The same base leg carries a class-level classMethod failure: "ObjectTracker found 6 object(s) that were not released!!!". From the log:

1. If the log establishes what leaked (the object types, or the test that allocated them), record that in `gates/SOLR-14231-fail-before.md` and `receipts/SOLR-14231.md`. If the log does not establish a cause, record that plainly instead. Do not speculate.
2. Note in the same record whether the head leg at 593463f398 shows the same complaint. The receipt's head counts are green; the point is only whether the record should carry the observation.

## Item 3: discard the throwaway worktree

The throwaway worktree `/workspace/gates/solr-14231-failbefore` remains on vm2. Read items 1 and 2 from it first if they need it; then discard it. If it holds nothing the log lacks, discard it without further reading and say so in the gate file.

## If the log is gone

If the run log no longer exists on vm2, that fact is the answer: record it plainly in `gates/SOLR-14231-fail-before.md` and `receipts/SOLR-14231.md` (the restatement could not be made because the raw record is gone), leave the per-test verdicts as confirmed, and still do item 3. Do not reconstruct counts from memory or from secondary summaries.

## On completion

1. `gates/SOLR-14231-fail-before.md` and `receipts/SOLR-14231.md` carry the restated counts, the ObjectTracker record, and the worktree disposition, dated 2026-10-14 or the completion date.
2. The draft `pr-drafts/streaming/SOLR-14231.md` carries a HOLD item for this restatement. The draft edit itself belongs to the review coordinator under the original job text, so do not edit the draft from this assignment; note in the gate file that the restatement is complete, so the coordinator can lift the hold.
3. Mark the claim for this assignment DONE.
4. Append one line to the takeover log (`files/remaining-branches-takeover-log.md` in the goal records) recording the restatement and the two files corrected.
