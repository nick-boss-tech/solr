# Skip record: SOLR-10726

- Ticket: SOLR-10726, SolrCloud opens multiple searchers on replica creation/startup. JIRA read in this pass: Open, resolution none, fix versions none, 1 comment (2017-07-06, hossman on testing waitSearcher in SolrCloud); updated 2019-06-08.
- Verdict: keep-open (unchanged status; this record replaces the earlier record on this branch, whose cross-audit reading named missing items J, C and L).
- Draft branch: solr-10726-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (checked at this head for this record).
- Source: the queue entry (audit verdict confirmed-skip, audit date 2026-10-19) is background only; the reading below is fresh at the pin.

## Ticket reading

The ticket: adding a replica, or restarting a node, opens two to four searchers, each firing first-searcher or new-searcher listeners and autowarming on indexes that are then discarded. The reporter's four steps are: the core opens a searcher on an empty index, a searcher is opened after replication, another after buffered updates are replayed, and (on restart) one more for log replay.

## Code reading

- solr/core/src/java/org/apache/solr/update/UpdateLog.java:940-955 (openRealtimeSearcher): calls uhandler.core.openNewSearcher(true, true) and clears the update-log maps. This is the realtime searcher open the ticket's step 4 and the replay steps depend on.
- UpdateLog.java:958-968 (deleteAll): also opens a new searcher, for the test-only deleteAll path.
- The searcher opens during replica creation and replay are made from several call sites (core load, replication, and the log replay path); the full set of call sites that the ticket's count depends on was not traced in this pass.

So the calls that open the extra searchers are present at the pin. Whether the count of opens per replica start still matches the four steps in the ticket is not shown by reading.

## History

- No commit at the pin names SOLR-10726 (git log --grep at the pin: none).
- The 2017 comment (hossman's test attempt for waitSearcher in SolrCloud) is not linked to a fix.

## Reproduction attempts

Not run. A reproduction needs a SolrCloud collection at the pin with a firstSearcher and newSearcher listener that counts invocations, then adding a replica and restarting a node, recording the listener counts.

## Gap analysis

The mechanism is present (several openNewSearcher call sites on the replica and replay paths), and the ticket's count of opens is a runtime fact that a listener count run would settle. No fix commit exists.

## Verdict justification

keep-open: the searcher-open paths the ticket names are present at the pin, and no fix commit names the ticket. close-fixed and close-obsolete are not supported. close-cannot-reproduce needs a run attempt, and none was made. Whether to reduce the opens is a design question.

Missing for a closing verdict: a listener-count run at the pin for the replica-add and restart scenarios, recording the number of searcher opens; a count of one per start would support close-fixed on a named change, and the current count would confirm the open issue.

## Limits

This record does not run a SolrCloud cluster, does not trace every openNewSearcher call site, and does not post anything to JIRA or GitHub.
