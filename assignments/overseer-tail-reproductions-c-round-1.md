# Assignment: Overseer tail reproductions, batch C (old bug reports needing a live-or-dead verdict), round 1

Status: DONE. Capability tags: records, premise-run. Host: any capable pool host.

Trigger: Nick's Overseer tail census (2026-10-31) placed these seven tickets
in Group C: old bug reports needing a live-or-dead verdict, plus the two
Group E test-only items folded into this batch in the same verdict shape.
Posted by the phone agent overnight 2026-11-01, in the same shape as the
four Group D assignments posted 2026-10-31 and the batch A and B verdicts
posted the same night.

Rules for the whole batch, every ticket:

- One claim per ticket. A host claims a single ticket (a claims/ file naming
  the ticket) before starting it, and no host takes two tickets from this
  batch at once. Each ticket below is its own unit of work.
- Start by re-verifying against the current code and the ticket's later
  comments. The census dispositions are triage proposals from the ticket
  text, not verdicts. The verdict for each ticket is one of two outcomes:
  still live (promote toward a Group D style implementation assignment, with
  the mechanism as it stands today), or fixed by later work (a closure draft
  citing the change that fixed it). If neither outcome can be established
  from a code read or a reproduction attempt, the deliverable becomes a
  cannot-reproduce report saying exactly what was tried, not a forced
  verdict.
- Records and drafts only. Do NOT post on Jira and do NOT close anything;
  a closure draft waits for a maintainer's or Nick's explicit go.
- Verdict by reproduction attempt or code read against current main. A
  code-read verdict with file and line citations is acceptable where
  reproduction needs a live cluster or a race window that does not open on
  demand. Do not launch a full gate for a verdict; a focused test run is
  enough where a reproduction attempt fits in one.

## Claim 1: SOLR-10597

Ticket: https://issues.apache.org/jira/browse/SOLR-10597 (2017, reporter
Smiley). Report: election endless loop on NodeExists at
/overseer_elect/leader.

Verdict work: read the current election path and check whether the
NodeExists loop this report describes can still occur, and whether any
later election rework already closed the loop. Note the boundary with
SOLR-18301 (election may not converge after ZK disconnect, Curator
migration, owned and tracked only): if this report turns out to be the
same mechanism, say so and stop rather than duplicating owned work.

Deliverable: reports/solr-10597-tail-verdict-c-round-1.md.

## Claim 2: SOLR-13186

Ticket: https://issues.apache.org/jira/browse/SOLR-13186 (2019, Miller).
Report: race when a node wins the election can register an invalid leader.

Verdict work: read the current election win path and check whether the
invalid-leader registration window this report describes still exists, or
whether later election work closed it. If the window is still open, state
the mechanism as it stands today so the ticket can promote to an
implementation assignment.

Deliverable: reports/solr-13186-tail-verdict-c-round-1.md.

## Claim 3: SOLR-13038

Ticket: https://issues.apache.org/jira/browse/SOLR-13038 (2018, reporter
Gerlowski). Report: Overseer actions fail with NoHttpResponseException
right after a node restart.

Verdict work: read the current Overseer-to-node request path and the
restart handling around it, and check whether the post-restart failure
this report describes still occurs or whether later client or restart
work fixed it. If it is still live, name the exact failure point rather
than the symptom alone.

Deliverable: reports/solr-13038-tail-verdict-c-round-1.md.

## Claim 4: SOLR-13416

Ticket: https://issues.apache.org/jira/browse/SOLR-13416 (2019,
caomanhdat). Report: Overseer gets stuck; the report carries a LockTree
lock_is_leaked warning.

Verdict work: read the current Overseer task processing and LockTree use
and check whether the stuck state and the leaked lock this report
describes still occur. Note the boundary with SOLR-18548 (lock leak in
OverseerTaskProcessor, owned and active, mechanism fully specified on
that ticket): if this report turns out to be the same leak, say so and
stop rather than duplicating owned work; if it is a distinct leak, say
what distinguishes it.

Deliverable: reports/solr-13416-tail-verdict-c-round-1.md.

## Claim 5: SOLR-15997

Ticket: https://issues.apache.org/jira/browse/SOLR-15997 (2022, noble).
Report: sporadic ZK BadVersionException when perReplicaState flips to
true.

Verdict work: read the current perReplicaState flip path and check
whether the BadVersion race this report describes still exists, or
whether later per-replica state work (see SOLR-15052, the PRS delivery)
closed it. A sporadic race that does not reproduce on demand still gets
a code-read verdict: state the race window as it stands today, or cite
the change that closed it.

Deliverable: reports/solr-15997-tail-verdict-c-round-1.md.

## Claim 6: SOLR-5833

Ticket: https://issues.apache.org/jira/browse/SOLR-5833 (2014, Hossman).
Report: ZkController.forceOverseer does not actually stop the old
Overseer (test utility).

Verdict work: read the current forceOverseer implementation and check
whether the old Overseer still survives the call, or whether later
Overseer lifecycle work fixed it. This is a test utility, so the verdict
is about what the utility does today, not about production behavior.

Deliverable: reports/solr-5833-tail-verdict-c-round-1.md.

## Claim 7: SOLR-5919

Ticket: https://issues.apache.org/jira/browse/SOLR-5919 (2014, Hossman).
Report: test control server believes it is a cloud of one and takes the
Overseer role.

Verdict work: read the current test control server setup and check
whether it still claims the Overseer role in the single-node test shape
this report describes, or whether later test harness work fixed it. If
the behavior changed because the Overseer role itself changed (see
SOLR-17293, the distributed default), say which change retired it.

Deliverable: reports/solr-5919-tail-verdict-c-round-1.md.

## Claim 8: SOLR-5699 (Group E test item, folded in)

Ticket: https://issues.apache.org/jira/browse/SOLR-5699 (2014, Miller).
Ask: add a test assert that a cluster never runs two Overseers at once.

Verdict work: same shape as the Group C verdicts, applied to the test
ask. Check whether current test coverage already asserts single
Overseer leadership, and whether the Overseer role as it stands today
(the distributed default, SOLR-17293) still makes the assert meaningful.
If the assert is already covered, cite the test; if the ask is retired
by the role change, say so; if it is still open and meaningful, state
what the assert would check so it can promote to an implementation
assignment.

Deliverable: reports/solr-5699-tail-verdict-c-round-1.md.

## Claim 9: SOLR-4944 (Group E test item, folded in)

Ticket: https://issues.apache.org/jira/browse/SOLR-4944 (2013, Shalin).
Ask: ChaosMonkeyShardSplitTest does not actually kill the Overseer, so
it does not test what it claims.

Verdict work: same shape as the Group C verdicts, applied to the test
ask. Read the current test and check whether it kills the Overseer today
and whether the split path it exercises still runs through the Overseer
at all. If the test now does what it claims, cite the change; if it
still does not, state exactly what it does instead so the fix can
promote to an implementation assignment.

Deliverable: reports/solr-4944-tail-verdict-c-round-1.md.
