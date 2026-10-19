# Skip record: SOLR-16792

- Ticket: SOLR-16792, Solr CoreAdmin API status throws a null-pointer exception when the index is empty/corrupted.. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-16792-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. The audit below was checked against upstream main c8ab71430f, not the fork pin. Not re-checked at the pinned head for this record.
- Source: the queue entry's audit field (round audit-3, 2026-10-05, audit verdict deferred).

## Ticket reading

The summary as recorded in the queue: Solr CoreAdmin API status throws a null-pointer exception when the index is empty/corrupted.. Skip note: assigned to maintainer Eric Pugh (awaiting repro since Nov 2025); NPE stack no longer matches current CoreStatus code — likely fixed by refactor; no repro.

## Code reading

Taken from the audit evidence, not re-read at the pinned head: Re-read CoreStatus.getCoreStatus on main (api/CoreStatus.java l.112-180): a failed-init core is not in getAllCoreNames and a missing descriptor yields an empty SingleCoreData, not an NPE; the only unguarded derefs (l.146-158 getCloudDescriptor().getLastPublished(), getSolrConfig().getZnodeVersion()) need a loaded core. Ticket stack is truncated at CoreAdminOperation (pre-refactor 9.x code), reporter never replied to the repro request. Cannot reproduce from code; leave open, candidate for a close-as-cannot-reproduce comment (not posted).

## History

No commit history was used for this record.

## Reproduction attempts

Not attempted in this record. The audit did not record a reproduction.

## Gap analysis

The audit (deferred) classified the ticket as not a current, reproducible defect. Nothing in the audit shows a closing fix or an obsolete feature, and no reproduction was recorded. Whether to close it is a maintainer decision, or a reproduction is needed first.

## Verdict justification

keep-open: the audit verdict (deferred) is recorded, but the rubric's close verdicts need evidence at the pinned head that this record does not have. Precedent on this queue: the other tickets with the same audit verdict carry keep-open (254 confirmed-skip, 41 deferred), with the audit in the queue entry.

Missing for a closing verdict: a check at the pinned head of the code path the audit names, or a reproduction of the reported behavior.

## Limits

This record does not establish a reproduction, and it does not check the fork pin. It does not post anything to JIRA or GitHub.
