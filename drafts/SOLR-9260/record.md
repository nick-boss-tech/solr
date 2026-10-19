# Skip record: SOLR-9260

- Ticket: SOLR-9260, facet.field.limit doesn't work in local params for distributed search. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-9260-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. The audit below was checked against upstream main c8ab71430f, not the fork pin. Not re-checked at the pinned head for this record.
- Source: the queue entry's audit field (round audit-6, 2026-10-17, audit verdict deferred).

## Ticket reading

The summary as recorded in the queue: facet.field.limit doesn't work in local params for distributed search. Skip note: same cause as SOLR-11129 (coordinator ignored facet local params); covered by solr-11129-submit.

## Code reading

Taken from the audit evidence, not re-read at the pinned head: The local-param coordinator issue remains in upstream/main c8ab714. The pushed origin/solr-11129-submit branch changes FacetComponent.FieldFacet.fillParams to read local facet.limit/mincount and adds distributed local-param tests, so it appears to cover the same seam. Keep linked and re-audit after that branch lands or is superseded; do not treat the unmerged branch as an upstream fix.

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
