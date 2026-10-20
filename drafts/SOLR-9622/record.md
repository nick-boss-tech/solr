# Skip record: SOLR-9622

- Ticket: SOLR-9622, "ExplainAugmenterFactory does not work on grouped queries". JIRA status read 2026-10-20T12:49:23Z (UTC): Open, resolution None, updated 2020-08-01. JIRA marks it a duplicate of SOLR-14693 (Resolved).
- Verdict: keep-open (kept; re-read at the pinned head).
- Draft branch: solr-9622-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T12:49:23Z (UTC).

## Ticket reading

`fl=*,[explain]` works without grouping but the explanation disappears when grouping (group field or group facet) is on. A 2019 comment says the explain transformer needs the query object, and links the code. JIRA lists SOLR-14693 as a duplicate, and that issue is Resolved.

## Code reading (pinned head)

- `solr/core/src/java/org/apache/solr/response/transform/ExplainAugmenterFactory.java:102-103`: the explanation is computed only when `context.getQuery()` is non-null; `context.getSearcher().explain(context.getQuery(), docid)` is the call.
- Whether `context.getQuery()` is null on a grouped request was not traced in this pass.

## History

- evidence.py at the pinned head: no commits naming SOLR-9622.
- `git log --grep=SOLR-14693` over the full history: no commit names SOLR-14693, so the duplicate's fix is not identifiable by its ticket number here.

## Reproduction attempts

Not run. A run would send a grouped query with `fl=*,[explain]` against a small index at the pinned head and check whether the explanation is present.

## Gap analysis

The mechanism the 2019 comment names (the query object in the context) is present in the code, and whether it is null on grouped requests decides the question. The JIRA duplicate points at a fix that this history does not name.

## Verdict justification

keep-open meets the rubric: attempts made are the code-path reading (ExplainAugmenterFactory.java:102-103) and the history search; the missing piece is the grouped-query run, or the commit behind SOLR-14693. close-fixed is not supported: no fix commit is named. close-obsolete is not supported: the transformer is present.

## Limits

This record does not establish the current grouped-query behavior. It does not verify the duplicate link beyond the JIRA field.
