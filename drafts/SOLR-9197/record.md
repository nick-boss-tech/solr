# Skip record: SOLR-9197

- Ticket: SOLR-9197, Audit all solrconfig components and ensure that they work with the Config APIs. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-9197-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. The audit below was checked against upstream main c8ab71430f, not the fork pin. Not re-checked at the pinned head for this record.
- Source: the queue entry's audit field (round audit-6, 2026-10-17, audit verdict deferred).

## Ticket reading

The summary as recorded in the queue: Audit all solrconfig components and ensure that they work with the Config APIs. Skip note: audit umbrella, no concrete defect.

## Code reading

Taken from the audit evidence, not re-read at the pinned head: This is a Config API umbrella. JIRA comments name possible failures (highlight component settings ignored and a ClusteringComponent ClassCastException), but provide no concrete request/config or failing test; the maintainer asked for a failing command and a subtask. Need one minimal current-version reproduction to scope it.

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
