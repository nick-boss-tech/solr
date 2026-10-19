# Skip record: SOLR-10256

- Ticket: SOLR-10256, Parentheses in SpellCheckCollator. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-10256-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. The audit below was checked against upstream main c8ab71430f, not the fork pin. Not re-checked at the pinned head for this record.
- Source: the queue entry's audit field (round audit-5, 2026-10-17, audit verdict confirmed-skip).

## Ticket reading

The summary as recorded in the queue: Parentheses in SpellCheckCollator. Skip note: feature (configurable parentheses).

## Code reading

Taken from the audit evidence, not re-read at the pinned head: This asks for a new switch controlling multiword spell-check collation. Current upstream/main SpellCheckCollator.getCollation inserts AND and parentheses for multiword boolean-query corrections to preserve their boolean meaning; the JIRA discussion disputes adding a flag and identifies extended results as a workaround. No incorrect result is demonstrated.

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
