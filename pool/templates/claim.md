# Claim: <slug>

- Claimant host: <host>, matching a file in `hosts/`.
- Status: <ACTIVE; mark DONE with the deliverable paths when the work lands>.
- Job or assignment: `<gates/... or assignments/... path>`.
- Slice: <the whole job, or the slice taken>.
- Capability tags used: <tags>.
- Started: <YYYY-MM-DDTHH:MM:SSZ> (UTC).
- Lease: live while heartbeats continue; stale 2 hours after the last heartbeat, and then takeable over under WORKFLOW.md, Claims. Attempts: <n, incremented on each takeover>. Idempotency key: <item>@<head sha or slice id>; a takeover resumes from this claim's recorded steps and reuses their results instead of redoing them.
- Links: Ticket or parent: <ticket or parent PR/branch>. Category: <one of the 14 in pool/work-map.md>. Siblings: <work on the same surface, or none known>. Supersedes: <claim or record, or none>.

Heartbeat: <YYYY-MM-DDTHH:MM:SSZ>

Append one heartbeat line at least once an hour while the claim is active, and push it.

Result: <YYYY-MM-DDTHH:MM:SSZ>: <result in one line, with the receipt or deliverable path>.

DONE: <YYYY-MM-DDTHH:MM:SSZ>. Deliverables: <paths>.
