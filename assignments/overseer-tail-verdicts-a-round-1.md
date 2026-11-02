# Assignment: Overseer tail verdicts, batch A (design and scale asks that removal retires), round 1

Status: DONE. Capability tags: records. Host: any capable pool host.

Trigger: Nick's Overseer tail census (2026-10-31) placed these seven tickets
in Group A: design and scale asks aimed at making the Overseer itself faster
or bigger. With decentralized processing the default (SOLR-17293) and the
overseerEnabled switch in place, the census proposal is that the removal path
answers them, not the ask. Posted by the phone agent overnight 2026-11-01, in
the same shape as the four Group D assignments posted 2026-10-31.

Rules for the whole batch, every ticket:

- One claim per ticket. A host claims a single ticket (a claims/ file naming
  the ticket) before starting it, and no host takes two tickets from this
  batch at once. Each ticket below is its own unit of work.
- Start by re-verifying against the current code and the ticket's later
  comments. The census dispositions are triage proposals from the ticket
  text, not verdicts. If the census premise fails for a ticket (the ask is
  still live, or removal does not retire it), the deliverable becomes a
  correction report saying so with citations, not a closure draft.
- Records and drafts only. Do NOT post on Jira and do NOT close anything;
  a closure draft waits for a maintainer's or Nick's explicit go.
- No build is needed for this batch: ticket read plus code read only.

## Claim 1: SOLR-5475

Ticket: https://issues.apache.org/jira/browse/SOLR-5475 (2013, assignee
shalin). Ask: scale the Overseer to multiple queues and threads.

Verdict draft: a closure draft citing SOLR-17293, the distributed default,
and the overseerEnabled switch, measured against this ticket's own scaling
goal. If multiple queues or threads already exist in some form in current
code, say exactly what exists and whether it changes the verdict.

Deliverable: reports/solr-5475-tail-verdict-a-round-1.md.

## Claim 2: SOLR-10265

Ticket: https://issues.apache.org/jira/browse/SOLR-10265 (2017, assignee
caomanhdat). Ask: the Overseer as a bottleneck in very large clusters; the
census notes this is the motivating report for parallel processing.

Verdict draft: a closure draft citing SOLR-17293, the distributed default,
and the overseerEnabled switch, measured against the bottleneck this report
describes. Check what decentralized processing changed since 2017 before
concluding the bottleneck is retired rather than moved.

Deliverable: reports/solr-10265-tail-verdict-a-round-1.md.

## Claim 3: SOLR-11465

Ticket: https://issues.apache.org/jira/browse/SOLR-11465 (2017, unassigned).
Ask: process independent Overseer messages in parallel. The census notes
this is the one Group A ticket linked to the removal umbrella SOLR-14927.

Verdict draft: a closure draft citing SOLR-17293, the distributed default,
and the overseerEnabled switch, and addressing the SOLR-14927 link directly:
if removal retires this ask, say so and note the link; if the link means
this ticket should stay open until removal lands, say that instead.

Deliverable: reports/solr-11465-tail-verdict-a-round-1.md.

## Claim 4: SOLR-6525

Ticket: https://issues.apache.org/jira/browse/SOLR-6525 (2014, unassigned).
Ask: investigate Overseer performance issues. The census notes this is the
evidence thread for SOLR-5872.

Verdict draft: a closure draft citing SOLR-17293, the distributed default,
and the overseerEnabled switch, and addressing the ticket's evidence role:
if the investigation question is answered by the removal path and the
SOLR-5872 supersession work, say so; if open evidence items remain, list
them instead of drafting closure.

Deliverable: reports/solr-6525-tail-verdict-a-round-1.md.

## Claim 5: SOLR-13007

Ticket: https://issues.apache.org/jira/browse/SOLR-13007 (2018, assignee
noble.paul). Ask: send Overseer messages in javabin instead of JSON.

Verdict draft: a closure draft citing SOLR-17293, the distributed default,
and the overseerEnabled switch, measured against this ticket's own goal.
Read the current message serialization first: if messages already travel in
javabin, the verdict is fixed-by-later-work, not retired-by-removal, and the
draft must say which.

Deliverable: reports/solr-13007-tail-verdict-a-round-1.md.

## Claim 6: SOLR-9811

Ticket: https://issues.apache.org/jira/browse/SOLR-9811 (2016, unassigned).
Ask: make it easier to manually execute Overseer commands.

Verdict draft: a closure draft citing SOLR-17293, the distributed default,
and the overseerEnabled switch, measured against this ticket's own goal.
Check what current admin tooling already offers for manual command
execution before concluding removal retires the ask.

Deliverable: reports/solr-9811-tail-verdict-a-round-1.md.

## Claim 7: SOLR-7985

Ticket: https://issues.apache.org/jira/browse/SOLR-7985 (2015, unassigned).
Ask: Overseer task management improvements, a grab-bag carried from
SOLR-7789.

Verdict draft: a closure draft citing SOLR-17293, the distributed default,
and the overseerEnabled switch. This ticket is a grab-bag: work through its
sub-asks one by one, and if some sub-asks are retired while others stay
live, the deliverable says exactly which are which instead of forcing a
single verdict.

Deliverable: reports/solr-7985-tail-verdict-a-round-1.md.
