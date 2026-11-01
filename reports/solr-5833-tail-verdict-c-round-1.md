# Verdict: SOLR-5833, ZkController.forceOverseer does not actually stop the old Overseer

Verdict: FIXED BY ELIMINATION (the ticket's own second option: "make
forceOverSeer() work safely, or eliminate it"). The named method is gone
from current main, and the path that replaced it stops the old Overseer
explicitly instead of hoping its loops notice. The next step is a closure
draft, not an implementation assignment. Nothing closes without a
maintainer's or Nick's go.

Ticket: https://issues.apache.org/jira/browse/SOLR-5833 (2014, reporter
Chris Hostetter, Open, Major, last updated 2018-01-11). The description
says that while working on SOLR-5823, Hossman tried to use
ZkController.forceOverSeer() to move the Overseer from one node to
another, and both nodes' Overseer objects ended up active. Miller's
2014-03-07 comments say the old one should stop only eventually, when
its loops check ZK and find it is no longer the registered leader, with
no promise when and lots of room for overlap, and that counting on that
is not sound. Noble's 2014-03-08 comment says two Overseers likely run
but do not process the queue simultaneously, with a small chance of
overlap if a message takes over 1500+100 ms, and that the feature was
then used only when an Overseer designate is added by ADDROLE for the
first time. No later comment, fix version, or link beyond the SOLR-5823
reference appears on the ticket. SOLR-5823 itself stands Resolved
Incomplete, so the motivating test need never landed either.

## Code read (current main as checked)

Checked against the local main checkout at e55d2e7bcb (2026-09-29). A
whole-tree grep for forceOverseer (any file type, excluding .git)
returns nothing: no method, no caller, no test utility by that name.
The assignment's premise ("read the current forceOverseer
implementation") fails at the first step because there is no current
implementation.

What replaced it, and why the old hazard does not carry over:

- The overseerop action no longer forces a leader; it asks a node to
  rejoin the election. CoreAdminOperation OVERSEEROP_OP
  (solr/core/src/java/org/apache/solr/handler/admin/CoreAdminOperation.java:213-225)
  calls zkController.rejoinOverseerElection(electionNode,
  "rejoinAtHead".equals(op)); the v2 API
  (solr/core/src/java/org/apache/solr/handler/admin/api/OverseerOperationAPI.java:61-66)
  only forwards to that action, and its class doc says it is about
  leader election.
- ZkController.rejoinOverseerElection
  (solr/core/src/java/org/apache/solr/cloud/ZkController.java:2533-2563)
  retries the election with a fresh OverseerElectionContext rather than
  overwriting a leader registration.
- Losing the election now closes the Overseer object explicitly:
  OverseerElectionContext.joinedElectionFired
  (solr/core/src/java/org/apache/solr/cloud/OverseerElectionContext.java:91-92)
  and checkIfIamLeaderFired (96-98) both call overseer.close(), and
  cancelElection (74-76) does the same. This is the explicit stop the
  2014 code lacked.
- The designate path also tells the old leader to quit:
  OverseerNodePrioritizer.prioritizeOverseerNodes
  (solr/core/src/java/org/apache/solr/cloud/OverseerNodePrioritizer.java:64-113)
  asks the designate to rejoin at head, asks the old first-in-line node
  to rejoin behind, then sends QUIT to the current leader via
  Overseer.sendQuitToOverseer
  (solr/core/src/java/org/apache/solr/cloud/Overseer.java:1071). The
  QUIT case in the ClusterStateUpdater loop (Overseer.java:568-576)
  closes the old leader's config-set processor and its updater when the
  id matches.
- The only test-tree hits for overseerop are param-mapping tests
  (V2NodeAPIMappingTest, CoreAdminOperationTest); no test forces an
  Overseer switch through a forceOverseer-style utility.

## Scope note

This verdict is about the named utility, not about the general
two-Overseers hazard. That hazard's test ask lives in SOLR-5699, which
this overnight program already verdict-recorded as still live. The
distributed default (SOLR-17293) further narrows Overseer-mode
processing, but the verdict does not rest on it: even in Overseer mode,
the forced-switch path is gone and its replacement stops the old leader
explicitly.

## Relation to the earlier skip record

A 2026-10-19 pool record (fork branch solr-5833-draft-close, commit
7e49176e54fcf9c15bd3b9f227670067697cce88) already reached
close-obsolete for this ticket from a code reading, with a draft Jira
comment on that branch, but it states Jira was not re-read for it and
its grep was taken from an earlier reading. This record re-read Jira
and re-ran the grep on current main, and agrees with its conclusion
while adding the successor-mechanism evidence. Nothing from that draft
has been posted to Jira; this record posts nothing to Jira either.

## What was not done

No test run: the verdict rests on a code read, which settles it, since
the named method is absent from the whole tree. No removal commit is
cited: the local checkout is shallow (363 commits, back to June 2026),
so the commit that removed forceOverseer cannot be pinned from here,
and no Jira comment or fix version names it. If a closure draft wants
the removal commit, it needs a full-clone history search. No Jira post,
no closure, no PR. This record is a verdict only.

## Next step

Closure draft, not an implementation assignment. The draft should cite
the elimination (no forceOverseer on current main at e55d2e7bcb), the
successor rejoin and QUIT mechanism above, and the ticket's own "or
eliminate it" wording, and it waits for a maintainer's or Nick's go
before anything posts.

Posted by the phone agent overnight 2026-11-01, as item 3 of the
overnight backlog (direct verdict record, batch C, smallest ticket
first after SOLR-5699). Source assignment:
assignments/overseer-tail-reproductions-c-round-1.md, claim 6.
