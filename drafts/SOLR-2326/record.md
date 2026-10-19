# SOLR-2326 closeout record

Verdict: keep-open (draft branch `solr-2326-draft-open`).

- Pinned head: fork `origin/main` `498a09aea97aac86c2cc7ac114113b946d249942`. Upstream history is not cited.
- JIRA checked 2026-10-19: status Open, no resolution, fix versions 4.9 and 6.0. Created 2011-01-20, updated 2016-05-09. Description read in full. Twenty-eight comments read in full.
- Audit verdict on record: `confirmed-skip` (audit-9, 2026-10-18): current ReplicationHandler returns indexversion from the latest commit and returns zero only when replication is disabled or no commit point exists; the audit cites a later reporter who traced one zero to a missing `-Dsolr.enable.master=true`.

## What the JIRA says about the cause

- 2011-01-24 epugh: the zero appears after a MERGE command, and after a new document and commit it still returns zero.
- 2011-07-14 jcustenborder: the zero is in the `CMD_INDEX_VERSION` branch of `ReplicationHandler` (commit point null while replication is enabled).
- 2011-08-10 ykats: after a core RELOAD, `indexversion` returns zero, while `details` still shows the generation. The reporter links it to SOLR-2705, a different ticket.
- 2012-02-29 janhoy: a misconfiguration on their side (Jetty started without `-Dsolr.enable.master=true`), with a new ticket SOLR-3176 opened for better logging.

## Code paths read at the pinned head

- `solr/solr-ref-guide`: no page documents `solr.enable.master`. The property is not found anywhere in `solr/` at the pin.
- `solr/core/src/test/org/apache/solr/handler/TestReplicationHandler.java`: the audit says this test exercises the indexversion command. The MERGE and RELOAD cases from the ticket are not covered by name in the replicateAfter tests read for SOLR-1542.

## Attempts

- Read the JIRA comments and grepped the guide and code for the configuration property. The property no longer exists, so the 2012 misconfiguration cannot recur in that form.
- No reproduction. The MERGE and RELOAD cases are the ones that could still return zero on main, and neither was reproduced.

## What the verdict does not cover

- Whether the MERGE and RELOAD cases still return zero on main. Not run.
- The status of SOLR-2705 and SOLR-3176. Not checked in this pass.

## What discharges keep-open

A replication test (or a manual run) on main: merge two indexes, then commit, then query indexversion; and reload the core after replicateAfter=startup and query indexversion. If both return a non-zero commit version, close-cannot-reproduce with the test name. If either returns zero, the ticket becomes pr-needed.
