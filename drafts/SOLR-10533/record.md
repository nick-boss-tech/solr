# Skip record: SOLR-10533

- Ticket: SOLR-10533, "Improve checks for which fields can be returned". JIRA status read 2026-10-20T12:28:53Z (UTC): Open, resolution None, updated 2019-11-21.
- Verdict: pr-needed. This replaces the recorded keep-open: the code path still rejects a docValues field that is not stored, and the change the ticket asks for is small.
- Draft branch: solr-10533-draft-pr (the earlier draft branch solr-10533-draft-open stays as the prior record).
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T12:28:53Z (UTC).

## Ticket reading

`DocBasedVersionConstraintsProcessorFactory` refuses a version field that is not stored, even when the field has docValues and useDocValuesAsStored is set (the default in the latest schema). The reporter asks for the check to accept that case, and says the same check may exist elsewhere, so the issue is kept broad. Comments from 2017 show a draft patch and a CSVResponseWriter test; no maintainer decision.

## Code reading (pinned head)

- `solr/core/src/java/org/apache/solr/update/processor/DocBasedVersionConstraintsProcessorFactory.java:192-199`: the condition `userVersionField == null || !userVersionField.stored() || userVersionField.multiValued()` throws "must be defined in schema, be stored, and be single valued."
- `solr/core/src/java/org/apache/solr/schema/SchemaField.java:107-109`: `stored()` tests only the STORED property, and `useDocValuesAsStored()` (line 148) is a separate property. So a docValues field with useDocValuesAsStored and stored=false fails the check.
- `git grep -n "must be defined in schema" 498a09aea97aac86c2cc7ac114113b946d249942 -- solr/core/src/java`: one hit, so this processor is the only place with that message.

## History

- evidence.py at the pinned head: no commits naming SOLR-10533.

## Reproduction attempts

Not run in this pass. The check is a single condition, so the failing input is clear from the code; the test the PR would add is a processor-factory case with a long field that has docValues and useDocValuesAsStored, not stored, expecting the factory to accept it.

## Gap analysis

Whether the processor reads the version value correctly from a docValues field (not from stored fields) was not verified. The PR would need that check. The ticket also asks for a broader audit of the same pattern; this record names only the one site found by grep.

## Verdict justification

pr-needed meets the rubric: the code-path argument with the line that causes the failure (DocBasedVersionConstraintsProcessorFactory.java:196 with the condition at 192-195), and the test the PR would add (the processor-factory case above). The PR itself is not opened here; opening it needs owner approval.

## Limits

This record does not run the new test. It does not check the processor's read path for docValues fields.
