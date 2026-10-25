# Assignment: warm-ticket evidence units, round 3 (each ending in a comment draft or a verdict)

Status: OPEN
Tags: premise-run, draft, records

Two units from the 2026-10-24 evening warm scan (tickets updated since 18:00 UTC; most of that slice was the owner's own RC3 filings or githubbot worklog entries). One claim per unit. Drafts follow `comment-formula.md`; nothing posts; every text needs the owner's explicit go.

## Unit 1: SOLR-18548 Overseer lock leak premise run

The ticket (reporter boyidai) says OverseerTaskProcessor leaks the collection lock when `runningMap.put` fails after the lock is taken. psalagnac asked on 2026-10-16 whether it was reproduced in a real cluster or found by code analysis; the reporter answered (2026-10-19) that it is code analysis only. The open question is a reproduction. Work: trace the lock and running-map path in `OverseerTaskProcessor`, then attempt a focused reproduction (a test that forces the put failure after the lock is taken and observes the lock state). Deliverables: `reports/solr-18548-lock-leak-premise-round-1.md` (trace, reproduction attempt, result either way) and, whichever way the attempt lands, `jira/jira-comment-SOLR-18548-draft.md` answering the thread's question with the evidence: reproduced with the mechanism, or not reproduced with what was tried.

## Unit 2: SOLR-17895 persistent ZNode leak trace

The ticket says distributed collection commands leak persistent ZNodes. The live thread (dsmiley and Amrit, 2026-10-23) is discussing whether a switch to Curator recipes addresses it, against the background of Solr 10's Curator changes. Work: trace the distributed command runner's lock-node lifecycle on current main and establish, with citations, whether the leak the ticket describes still exists after those changes, and which nodes persist. Deliverables: `reports/solr-17895-znode-leak-trace-round-1.md` and a draft at `jira/jira-comment-SOLR-17895-draft.md` ONLY if the trace settles a point the thread has open; if the thread has already covered the ground, the report says so and no draft is written. No routed review ask to anyone; the draft informs the ticket.
