# Skip record: SOLR-10896

- Ticket: SOLR-10896, RawQParserPlugin doesn't work with PointFields. JIRA read in this pass: Open, resolution none, fix versions none, 0 comments, description empty; updated 2019-06-08.
- Verdict: keep-open (unchanged status; this record replaces the earlier record on this branch, whose cross-audit reading named missing items J and C).
- Draft branch: solr-10896-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (checked at this head for this record).
- Source: the queue entry (audit verdict confirmed-skip, audit date 2026-10-19) is background only; the reading below is fresh at the pin.

## Ticket reading

The ticket title says the raw query parser (q={!raw f=... v=...}) does not work with PointFields. The description is empty and there are no comments, so the reported symptom and the steps are not recorded.

## Code reading

- solr/core/src/java/org/apache/solr/search/RawQParserPlugin.java:45: the parser returns new TermQuery(new Term(f, v)) for the given field and value. There is no branch on the field's type (the file has no FieldType or PointField reference; grep returns none).
- A point field (for example IntPoint or LongPoint) is not indexed as a term, so a TermQuery on the field name and the raw value does not match point values. The parser therefore does not handle point fields by design of its code at the pin.
- The file's last change at the pin is c99af207c7 (SOLR-14920, formatting only), so the parser's behavior has been the same since the ticket's time.
- No test in solr/core/src/test names RawQParserPlugin together with point fields (grep for raw and point returned only unrelated matches, e.g. QueryEqualityTest.java:408, which covers raw query equality with a string field).

So the code path the title describes is present at the pin: the raw parser builds a term query for any field type.

## History

- No commit at the pin names SOLR-10896 (git log --grep at the pin: none).
- c99af207c7 is formatting only.

## Reproduction attempts

Not run. A reproduction needs a core with an IntPointField (or LongPointField) and a query {!raw f=field v=value} with a value that is indexed in the point field, recording whether the query returns the matching documents. Not built or run in this pass.

## Gap analysis

The ticket has no description and no steps, so the exact failing query is not known. The code does not handle point fields, which matches the title. Whether that is a defect to fix (a raw query is meant to be a term query, so point fields may be out of scope) is a maintainer decision.

## Verdict justification

keep-open: the title's symptom matches the code at the pin, so close-fixed and close-obsolete are not supported; no commit fixes it. close-answered would need a ref-guide statement that raw queries are term-only, which was not checked in this pass. close-cannot-reproduce needs a run attempt, and none was made.

Missing for a closing verdict: the reporter's failing query (the description is empty), a run at the pin on an IntPointField, and a maintainer decision on whether the raw parser should support point fields or document that it is term-only.

## Limits

This record does not run a query, does not check the ref guide for the raw parser, and does not post anything to JIRA or GitHub.
