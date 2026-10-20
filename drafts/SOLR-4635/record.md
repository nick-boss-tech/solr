# Skip record: SOLR-4635

- Ticket: SOLR-4635, "shardHandlerFactory loading in solr.xml hides class loading exceptions". JIRA status read 2026-10-20T15:34:56Z (UTC): Open, resolution None, updated 2013-03-22.
- Verdict: keep-open. This replaces the recorded close-fixed: the message now carries the underlying text and chains the cause, but the commit that added that is not identified (the line blames to a formatting commit), so close-fixed cannot name its fix.
- Draft branch: solr-4635-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T15:34:56Z (UTC).

## Ticket reading

A custom shardHandlerFactory class named in solr.xml that does not load gives a SolrException with no information about the cause. The reporter filed it in 2013; no comments.

## Code reading (pinned head)

- `solr/core/src/java/org/apache/solr/handler/component/ShardHandlerFactory.java:52-68` (`newInstance`): a failure to load or construct the class is wrapped in a `SolrException` whose message includes the class name and `e.getMessage()`, with `e` attached as the cause. So the cause text and the chained exception now reach the caller.
- `git blame` on the error-message line (around line 62) attributes it to `c99af207c7` (SOLR-14920, formatting only, 2022-03-04). That commit moved the code; it did not add the message. The commit that added the cause text was not found, so no fix commit can be named.

## History

- evidence.py at the pinned head: no commits naming SOLR-4635.

## Reproduction attempts

Not run. A run would set a missing shardHandlerFactory class in solr.xml, start the node, and read the error text.

## Gap analysis

The reported behavior is no longer the case in code. The fixing change is not identified, and a run would show the exact message, which is the basis for a close.

## Verdict justification

keep-open meets the rubric: attempts made are the reading of newInstance and the blame; the missing pieces are the fixing commit (for close-fixed) or the run that shows the message. close-fixed is not supported without a named commit; close-obsolete is not supported since the code is present.

## Limits

This record does not run the failing load. It does not find the commit that added the cause text.
