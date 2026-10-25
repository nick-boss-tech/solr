# Teardown failure sweep round 1: Windows intake refusal

Status: WON'T DO (Windows host only; no ownership acquired)
Host: windows
Started UTC: 2026-10-25T23:18:36Z
Assignment: assignments/teardown-failure-sweep-round-1.md
Reviewed board snapshot: 46518a3123ea8e23474e70cbcd5ece200db6c595
Tags assessed: premise-run, records, review, sweep
Tags held here: implementation, review, draft, records, selenium, windows-check

The owner asked this Windows session to try to take the assignment. Intake found
the assignment OPEN, with no matching claim or delivered report at the reviewed
snapshot. It explicitly names a pool Linux host and says "not Windows (no Gradle
work if a probe is later approved)." hosts/windows.md also excludes premise-run
and sweep work. This host lacks those two required tags.

Decision: WORKFLOW.md Claims rule 8, structurally unable to finish under the
assignment's host requirements. Decision ladder rung 3: the assignment's stated
host restriction settles intake. Record this terminal refusal without taking an
active claim. The static-only scope and prohibition on running probes do not
remove the explicit Windows exclusion.

The assignment remains available to a capable pool Linux host. hosts/vm6.md lists
all four required tags; availability and ownership must be checked there before
any Linux claim. This record does not claim for vm6 or direct another host.

No census, source investigation, reproduction, probe, fix, branch work, Jira text,
GitHub comment, or PR action was performed. No assignment or shared index changed.
No other assignment was taken because this request names this assignment only.

Deliverable: this terminal intake record only. The assignment's report remains
unfulfilled and is not a deliverable of this refusal.
