# Gate job: SOLR-18552 launcher leg (bats, main line)

Status: DONE (vm1, 2026-10-22; launcher leg PASS both directions; bats 21 tests, 14 pass, 7 environmental root-refusal failures; receipt top section in receipts/SOLR-18552.md)

- Branch: solr-18552-submit. Head: `cb32d0d966159edb237d581318af9bf96071c0d1` (fork tip, ls-remote 2026-10-20T14:08Z). If the tip has moved at claim time, stop and record the new tip here.
- Prior gated head: `a5406c9fe531161df7b424b2a55143489b95166e` (earlier PR head). Merge-base with upstream branch_9x: `3511867c3e`.
- Intended host: vm1 (the only host with the bats tag, hosts/vm1.md). Any capable host with bats may claim if vm1 is busy.
- Capability tags: bats, gate.
- Why: the launcher leg of the review round (assignments/pool-newdev-critblock-review-round-1.md, unit 4). The vm5 pass 1 partial regate named this leg (gates/SOLR-18552-review-pass-1.md).
- Diff scope against `3511867c3e`: `solr/bin/solr`, `solr/bin/solr.cmd`, `changelog/unreleased/SOLR-18552-mmap-arena-permits.yml`. Nothing else.

## What to run

1. Assemble a distribution from the head (the 9.x distribution build needs the npm step; see the receipt). If it cannot be assembled on the host, record the step and stop.
2. Start a node with the branch's `bin/solr` in the distribution. Read the node JVM's command line (for example `ps` or the start log) and confirm `-Dorg.apache.lucene.store.MMapDirectory.sharedArenaMaxPermits=64` is present.
3. Start a control node with the same distribution but without the property: use the launcher from `3511867c3e` (or `SOLR_OPTS` cleared) and confirm the property is absent.
4. Run the bats launcher tests for `solr/bin` on the head.
5. Record the command lines, the JVM argument lines (the grep result), and the bats counts from the output.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Launcher check passes | decided: the property is on the JVM of the branch node and absent from the control | if either is wrong, record the output and stop; no repair (the change is a launcher default, not a test fix) | vm5, review pass 1 (rung 1) |
| Distribution cannot be built | default: record and stop as NEARLY | the leg waits for a host with the npm step | WORKFLOW.md |
| Head moved | default: record the new tip and review it in a new pass | no reset | WORKFLOW.md |

## On green

Append a receipt top section to `receipts/SOLR-18552.md` with the head, the two JVM lines, and the bats counts. Mark this job DONE and the unit's pass 1 leg complete in the claim.

## Limits

- No PR, comment, JIRA or email. No force push. Commit as Nick Shanin with no trailers. No em dashes.
