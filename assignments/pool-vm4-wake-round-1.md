# Pool assignment: vm4 wakes the other pool hosts and points them at the owner complaints

Status: DONE (vm4, 2026-10-19T16:07Z): all three hosts active; see reports/vm4-wake-round-1.md

- Capability tags: records, coordination.
- Intended host: vm4 only. Claim under `claims/` before starting (WORKFLOW.md).
- Posted by: phone agent (vm1), 2026-10-19, at the direction of Nick.

The owner posted `pool/owner-complaints-2026-10-19.md` this morning and pointed both `pool/START-HERE.md` and `pool/cron-prompt.md` at it. Since then vm4 is the only host visibly acting on it (the pool-aab batch 01 claim at 2026-10-19T15:13Z). vm2 has produced nothing today, and vm3 and vm5 are well below vm4's pace. The hosts have an MCP channel for messaging each other; this assignment uses it. Hosts are supposed to wake themselves on their own ticks. Some of them appear to be failing at that, so vm4 keeps waking them until they move.

## Items

1. **Message every other pool host now.** Start from: `pool/owner-complaints-2026-10-19.md` and `pool/START-HERE.md`. Over the MCP channel, send vm2, vm3, and vm5 each a message that says, in substance: the owner has posted complaints at `pool/owner-complaints-2026-10-19.md`; go to `pool/START-HERE.md` on the coordination branch, read the complaints file before claiming anything, then claim work that answers them. Name the complaints that concern that host: vm2 gets complaint 3 (the CSV assignment, `assignments/pool-post-issues-csv-vm2.md`: claim it and post the file, or mark it BLOCKED with the search record) and complaint 1 (cross-audit batches); vm3 and vm5 get complaints 1, 2, and 5 (take a pool-aab audit batch, re-audit remediations so cleared moves, spread the load). Output: the sent messages logged in the deliverable report.
2. **Keep waking them until they move.** On every one of your ticks while this claim is open, check the branch for each host: any claim, closeout, or heartbeat from that host since the complaints were posted (2026-10-19T15:13Z). Re-message every host that still shows nothing, each tick, with the same direction. Stop messaging a host once it has any activity of its own on the branch. Output: the per-tick wake log in the deliverable report.
3. **Close the round out.** When every other host has shown activity, or at 2026-10-20T06:00Z, whichever comes first, finish the report: which hosts woke, when, after how many messages; which hosts never moved; and for a host that never moved, state plainly that its tick appears stopped, since no peer message can restart a stopped cron. Mark the claim DONE in the same push. Output: `reports/vm4-wake-round-1.md`.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Message channel | decided: the MCP messaging channel between hosts | if the channel errors or a host is unreachable on it, record that in the report and keep retrying on later ticks; do not substitute branch files for the messages, the branch is already carrying the complaints and it was not enough | Nick |
| Wake cadence | decided: once per vm4 tick per silent host | if a host shows any branch activity of its own, stop messaging it that round | Nick |
| Round end | decided: all hosts active, or 2026-10-20T06:00Z | at the deadline, file the report naming any host that never moved and mark the claim DONE either way | Nick |
| vm4's own queue work | default: this assignment runs alongside it | if a tick forces a choice, the wake messages go first; they are short | Nick |

No row may leave the choice to the claimant.

## Deliverable

`reports/vm4-wake-round-1.md`: one section per host (vm2, vm3, vm5), with the messages sent (time and substance), the ticks on which wakes were repeated, the host's first activity of its own on the branch, and the round-end verdict per host. The claim `claims/pool-vm4-wake-round-1.md` marked DONE in the same push as the final report.

## Limits

- No PR, comment, JIRA or email. No force push.
- Messages go only to the pool hosts named here, and say only what item 1 says. No other content, no credentials, nothing about any host's local state beyond what this branch shows.
- Do not claim, edit, or close another host's assignments or claims while waking it. Waking is messaging and recording, not doing their work for them.
- Commit as Nick Shanin with no trailers. No em dashes.
