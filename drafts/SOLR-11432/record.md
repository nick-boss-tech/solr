# Skip record: SOLR-11432

- Ticket: SOLR-11432, Query Elevation breaks collection aliasing under certain conditions. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-11432-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. The audit below was checked against upstream main c8ab71430f, not the fork pin. Not re-checked at the pinned head for this record.
- Source: the queue entry's audit field (round audit-3, 2026-10-05, audit verdict confirmed-skip).

## Ticket reading

The summary as recorded in the queue: Query Elevation breaks collection aliasing under certain conditions. Skip note: design/complex: QueryElevation over a multi-collection alias where only some shards return elevation sort values breaks mergeIds/unmarshalSortValues; needs shard-level sort-value contract design, no repro data..

## Code reading

Taken from the audit evidence, not re-read at the pinned head: audit-3 Tier 1 pass: ticket packet and note re-read (JIRA status Open, 0 comments). Class: no repro or too vague to test. Reason holds: design/complex: QueryElevation over a multi-collection alias where only some shards return elevation sort values breaks mergeIds/unmarshalSortValues; needs shard-level sort-value contract design, no repro data. Basis is ticket text, not a fresh code repro; no deterministic test could be written from it.

## History

No commit history was used for this record.

## Reproduction attempts

Not attempted in this record. The audit did not record a reproduction.

## Gap analysis

The audit (confirmed-skip) classified the ticket as not a current, reproducible defect. Nothing in the audit shows a closing fix or an obsolete feature, and no reproduction was recorded. Whether to close it is a maintainer decision, or a reproduction is needed first.

## Verdict justification

keep-open: the audit verdict (confirmed-skip) is recorded, but the rubric's close verdicts need evidence at the pinned head that this record does not have. Precedent on this queue: the other tickets with the same audit verdict carry keep-open (254 confirmed-skip, 41 deferred), with the audit in the queue entry.

Missing for a closing verdict: a check at the pinned head of the code path the audit names, or a reproduction of the reported behavior.

## Limits

This record does not establish a reproduction, and it does not check the fork pin. It does not post anything to JIRA or GitHub.
