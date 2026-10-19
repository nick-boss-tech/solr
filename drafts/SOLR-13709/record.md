# Skip record: SOLR-13709

- Ticket: SOLR-13709, Race condition on core reload while core is still loading?. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2019-08-22, updated 2020-12-31, no fix versions, no links; 26 comments, the last on 2020-12-31 (the investigator says they will stop working on it).
- Verdict: keep-open.
- Draft branch: solr-13709-draft-open (the existing keep-open branch; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Greps and history run at this head (detached worktree).
- Source: remediation redo of batch 05, item 24, under assignments/pool-aaa-skip-verdict-remediation-05.md. The earlier record (round audit-1, 2026-10-04, confirmed-skip) cited the thread from audit evidence; the thread and the pin are read here.

## Ticket reading

A test failure suggested that reloading a core while it is still loading can leave it unusable, and a null core descriptor was seen in that path. The thread splits: one investigator (erickerickson) concluded the failures came from a test that deletes a collection before earlier operations finish, and called that a test artifact; another (hossman, 2019-10-07) said there is a real bug, that deleting a collection while core reloads are in progress causes errors. The ticket was not closed.

## Grep and history at the pinned head

- `git log --format='%h %ad %s' --date=short --grep='SOLR-13709' 498a09aea97aac86c2cc7ac114113b946d249942` gives 83cd54f801 (2019-09-03, "SOLR-13709: Fixed distributed grouping when multiple 'fl' params are specified") and d1a4d13525 (2019-09-03, "CHANGES fixup: SOLR-13709 -> SOLR-13717"). The commit 83cd54f801 fixes a distributed grouping bug, not the reload race. The ticket number was reused for it, so it does not close this ticket.
- `git grep -n "getCoreDescriptor" 498a09aea97aac86c2cc7ac114113b946d249942 -- solr/core/src/java/org/apache/solr/core/CoreContainer.java` shows the lookups used during reload (for example CoreContainer.java:1532 and :2002, `reloadCoreDescriptor(core.getCoreDescriptor())`). The reload path still reads the descriptor of a core that may be loading; the thread's null-descriptor question was not settled here.

## Reproduction attempts

Not run. The investigators' reproduction was a beasting run (about 2 failures in 1000 runs), which this pass did not repeat.

## Gap analysis

The thread has not agreed on whether this is a test artifact or a code race, and no fix is designed. The reload path in code still reads the descriptor during load; whether a reload can race a load is not settled by reading.

## Verdict justification

keep-open: the attempts made are the thread read, the grep and history check above at the pinned head, and the Jira read. The missing piece is a reproduction that separates the explanations: a test that deletes a collection during a reload, with a check of whether the core stays usable, and a decision on whether the reload should wait for loading to finish. close-fixed is not supported (no fix commit for the reload race; 83cd54f801 is a different bug). close-obsolete and close-answered are not supported.

Missing for a closing or PR verdict: the separating reproduction, and the maintainer decision.

## Limits

This record does not run the beasting test. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
