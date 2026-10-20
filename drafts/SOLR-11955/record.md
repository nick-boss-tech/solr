# Skip record: SOLR-11955

- Ticket: SOLR-11955, "Clean up usage of graph token filters in shipped schemas". JIRA status read 2026-10-20T06:30:34Z (UTC): Open, resolution None, no fix versions, updated 2019-06-08.
- Verdict: keep-open. This replaces the recorded close-obsolete: the fresh evidence contradicts it.
- Draft branch: solr-11955-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T06:30:34Z (UTC).

## Ticket reading

Token graph filters (SynonymGraphFilter, WordDelimiterGraphFilter) may not handle an input that is itself a graph from another graph filter. The reporter cites the shipped `text_en_splitting` type, which feeds SynonymGraph into WordDelimiterGraph, and asks for every shipped analysis chain to be checked. No comments.

## Code reading (pinned head)

- `solr/server/solr/configsets/_default/conf/managed-schema.xml`, fieldType `text_en_splitting`: the index analyzer has the synonymGraph filter commented out (lines 4-6 of the type). The **query analyzer** has `synonymGraph` active (line 21 of the type) followed by `stop` and then `wordDelimiterGraph` (line 26). So a graph filter feeds WordDelimiterGraph at query time in a shipped configset.
- `solr/server/solr/configsets/sample_techproducts_configs/conf/managed-schema.xml`: the same type with the same query chain (synonymGraph, stop, wordDelimiterGraph).
- The grep this pass used (`git grep WordDelimiterGraphFilter` under `solr/server`) finds hits only in comments and in these two schemas' filter elements. The earlier audit's statement that only test-file hits exist is not correct for the shipped configsets.

## History

- evidence.py at the pinned head: no commits naming SOLR-11955.
- The ticket's own reference is a mailing-list thread; no commit names the ticket.

## Reproduction attempts

Not attempted. The question is whether the query-time chain SynonymGraph → WordDelimiterGraph produces wrong results. A run would analyze a query with a multi-word synonym through `text_en_splitting` at the pinned head and check the token graph it yields.

## Gap analysis

The evidence shows the chain exists in shipped configsets, which is what the ticket asks to evaluate. It does not show the chain is wrong; that depends on how Lucene's WordDelimiterGraphFilter handles a graph input, which the pool has not tested here.

## Verdict justification

keep-open meets the rubric: the attempts made are the code-path reading of both shipped schemas, and the missing piece is the query-analysis run that shows whether the output is wrong. close-obsolete is not supported: the chain is present at the pinned head, so the grep the recorded verdict relied on does not come back empty. pr-needed is not chosen, since it needs that run first.

## Limits

This record does not establish a bug. It does not check the reference guide's description of the filters.
