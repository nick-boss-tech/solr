# Skip record: SOLR-10601

- Ticket: SOLR-10601, "StreamExpressionParser should handle white space around = in named parameters". JIRA status read 2026-10-20T12:04:47Z (UTC): Open, resolution None, updated 2021-08-14.
- Verdict: close-fixed. This replaces the recorded keep-open.
- Draft branch: solr-10601-draft-close.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T12:04:47Z (UTC).

## Code reading (pinned head)

- Fix commit `17563ce81f` (2017-05-03, SOLR-10601) changes `wordToken(name)` to `wordToken(name.trim())`; it is an ancestor of the pinned head (`git merge-base --is-ancestor`).
- The line is present at `solr/solrj-streaming/src/java/org/apache/solr/client/solrj/io/stream/expr/StreamExpressionParser.java:187`.
- Changelog follow-up `df6da67204`; squash merge `95968c69fd` also contains the commit.

## Reproduction attempts

Not run; the changed line and its commit establish the fix at the pinned head.

## Verdict justification

close-fixed meets the rubric: fix commit named, ancestor of the pinned head, changed line present.

## Limits

This record does not check other whitespace paths in the parser or run the test.
