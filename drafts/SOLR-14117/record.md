# Skip record: SOLR-14117

- Ticket: SOLR-14117, remove java serialization from AnalyticsShardResponseParser.java. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2019-12-19, updated 2020-01-21, linked to SOLR-13931 (Resolved), LUCENE-9094 (Closed), SOLR-14095 (Closed); no comments.
- Verdict: close-obsolete.
- Draft branch: solr-14117-draft-close (the existing close draft; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Grep and removal commit checked at this head (detached worktree). Upstream sha not cited.
- Source: remediation redo of batch 05, item 4, under assignments/pool-aaa-skip-verdict-remediation-05.md. The earlier record (vm2, from the B2 reading) cited a grep that was not re-run; this record re-runs it.

## Ticket reading

The ticket asks to remove Java serialization from AnalyticsShardResponseParser.java, which serializes exceptions for shard responses, because Java serialization is unsafe.

## Grep at the pinned head

- `git grep -n "AnalyticsShardResponseParser" 498a09aea97aac86c2cc7ac114113b946d249942 -- '*.java' | wc -l` gives 0.
- `git grep -n "ObjectInputStream\|ObjectOutputStream" 498a09aea97aac86c2cc7ac114113b946d249942 -- 'solr/*/src/java/org/apache/solr/analytics*' 'solr/*/src/java/org/apache/solr/handler/component/Analytics*' | wc -l` gives 0. This pathspec is narrow, so the next check covers the whole tree.
- `git grep -l "org.apache.solr.analytics\|AnalyticsShardResponseParser" 498a09aea97aac86c2cc7ac114113b946d249942 | wc -l` gives 0. No file anywhere in the tree refers to the analytics package or the parser.

## History and code path

- The removal commit is 67afcc228d (2023-05-12), "SOLR-16661 Remove deprecated Analytics Component in 10.0 (#1622)". `git merge-base --is-ancestor 67afcc228d 498a09aea97aac86c2cc7ac114113b946d249942` succeeds, so the removal is on the pin.
- The parser class was added in d5963bebc4 (2017, SOLR-10123, the Analytics Component 2.0) and is gone from the tree by 67afcc228d.

## Reproduction attempts

Not attempted. The code the ticket names does not exist at the pin, so there is nothing to run.

## Gap analysis

The ticket's target is removed, with the whole analytics component. The general point (Java serialization of exceptions in other places) is not what the ticket's title names, and the record does not claim it was checked across the tree; the grep above covers the analytics code and any reference to it, not every use of ObjectInputStream in Solr.

## Verdict justification

close-obsolete: the code the ticket is about (AnalyticsShardResponseParser and the analytics package) has been removed by 67afcc228d, and a search of the pinned head finds no remaining reference (the empty results above). The record does not claim anything about other serialization sites in Solr.

## Limits

This record does not reproduce anything. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
