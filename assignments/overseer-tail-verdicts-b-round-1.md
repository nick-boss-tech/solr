# Assignment: Overseer tail verdicts, batch B (superseded by later work), round 1

Status: DONE. Capability tags: records. Host: any capable pool host.

Trigger: Nick's Overseer tail census (2026-10-31) placed these five tickets
in Group B: asks that later work appears to have superseded. Posted by the
phone agent overnight 2026-11-01, in the same shape as the four Group D
assignments posted 2026-10-31 and the batch A verdicts posted the same night.

Rules for the whole batch, every ticket:

- One claim per ticket. A host claims a single ticket (a claims/ file naming
  the ticket) before starting it, and no host takes two tickets from this
  batch at once. Each ticket below is its own unit of work.
- Start by re-verifying against the current code and the ticket's later
  comments, and against the cited later work itself. The census dispositions
  are triage proposals from the ticket text, not verdicts. For each ticket,
  confirm the cited work actually landed, and that it covers this ticket's
  ask rather than a neighbouring one. If the supersession premise fails for
  a ticket (the cited work never landed, covers only part of the ask, or
  was itself reverted or replaced), the deliverable becomes a correction
  report saying so with citations, not a closure draft.
- Records and drafts only. Do NOT post on Jira and do NOT close anything;
  a closure draft waits for a maintainer's or Nick's explicit go.
- No build is needed for this batch: ticket read plus code read only.

## Claim 1: SOLR-4689

Ticket: https://issues.apache.org/jira/browse/SOLR-4689 (2013). Ask:
SPLITSHARD node-name delegation to the Overseer. The census notes SOLR-4655
landed in 2013 and the split API has been rewritten since.

Supersession check: read SOLR-4655 and the current split path, and confirm
whether node-name delegation still runs through the Overseer in any form.
If the delegation is gone because the split API was rewritten, the draft
says which change removed it. If any delegation path remains, the
deliverable is a correction report, not a closure draft.

Deliverable: reports/solr-4689-tail-verdict-b-round-1.md.

## Claim 2: SOLR-5872

Ticket: https://issues.apache.org/jira/browse/SOLR-5872 (2014, assignee
noble). Ask: "Eliminate overseer queue". The census notes its stated path,
per-collection state out of clusterstate.json, is what PRS delivered
(SOLR-15052).

Supersession check: read SOLR-15052 and the current per-replica state
handling, and confirm whether the overseer queue this ticket wanted gone
is actually gone for the cases the ticket named. Note the link to SOLR-6525
(batch A, the evidence thread for this ticket): if the batch A verdict on
6525 reaches a different conclusion on shared evidence, flag the
disagreement in the report instead of drafting closure over it.

Deliverable: reports/solr-5872-tail-verdict-b-round-1.md.

## Claim 3: SOLR-5928

Ticket: https://issues.apache.org/jira/browse/SOLR-5928 (2014). Ask: expose
Overseer status via JMX. The census notes SOLR-5749 implemented an
Overseer status API instead.

Supersession check: read SOLR-5749 and the current status surface, and
confirm whether the API it delivered covers what this ticket asked JMX to
expose. If the API covers the ask, the draft says so and names the
endpoint or surface. If JMX exposure was what the requester actually
needed and no equivalent exists, the deliverable is a correction report,
not a closure draft.

Deliverable: reports/solr-5928-tail-verdict-b-round-1.md.

## Claim 4: SOLR-6395

Ticket: https://issues.apache.org/jira/browse/SOLR-6395 (2014). Ask: the old
admin UI cloud tree hangs on a large queue. The census notes the old UI is
being replaced by the new UI.

Supersession check: confirm the old UI path this ticket reports on is
actually retired or replaced, and whether the new UI carries the same
cloud-tree behaviour. A hang that survives into the replacement UI is a
live bug, not a superseded ask, and the deliverable in that case is a
correction report naming where the hang now lives.

Deliverable: reports/solr-6395-tail-verdict-b-round-1.md.

## Claim 5: SOLR-7872

Ticket: https://issues.apache.org/jira/browse/SOLR-7872 (2015, Miller).
Ask: move Overseer classes into a cloud.overseer package. The census notes
this is a cosmetic move that removal makes pointless.

Supersession check: confirm the classes still sit outside a
cloud.overseer package, and weigh the move against the removal path
(SOLR-14927, SOLR-17293). If removal retires the move, the draft says so
plainly. If the package move would still help readers of code that stays
after removal, say that instead of forcing a closure verdict on a
cosmetic ask.

Deliverable: reports/solr-7872-tail-verdict-b-round-1.md.
