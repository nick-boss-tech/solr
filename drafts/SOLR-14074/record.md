# Skip record: SOLR-14074

- Ticket: SOLR-14074, Make CollapsingQParserPlugin re-entrant. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2019-12-12, updated 2020-01-21, no fix versions, no links, no comments.
- Verdict: keep-open.
- Draft branch: solr-14074-draft-open (the existing keep-open branch; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read and the cited commit checked at this head (detached worktree).
- Source: remediation redo of batch 05, item 7, under assignments/pool-aaa-skip-verdict-remediation-05.md. The earlier record (vm2, from the C reading) cited the grouping check and the commit; both are confirmed here.

## Ticket reading

Grouping calls the same post filter twice in one query, and the collapse plugin's DocValues caches are not reset on the second call, so collapse cannot be used with grouping. The ticket asks that the plugin be made re-entrant.

## Code reading and history at the pinned head

- solr/core/src/java/org/apache/solr/search/CollapsingQParserPlugin.java:342-345, in the `CollapsingPostFilter` constructor: "Don't allow collapsing if grouping is being used." If `GroupParams.GROUP` is true on the request, the constructor throws BAD_REQUEST "Can not use collapse with Grouping enabled". So the current behavior is an explicit refusal, not a re-entrancy bug.
- The commit that added the refusal is 88b5b5a7cd, "SOLR-13970: disallow using collapse/expand with grouping". `git merge-base --is-ancestor 88b5b5a7cd 498a09aea97aac86c2cc7ac114113b946d249942` succeeds, so the refusal is on the pin.

## Grep and reproduction attempts

- The re-entrancy the ticket asks for is not in the code: the refusal at :342-345 means the combined path is never reached.
- Not attempted: a run with grouping and collapse. The request is refused by design at this head, so the run would show the refusal.

## Gap analysis

The ticket asks for the combination to work. The code refuses the combination on purpose (SOLR-13970). Making it work means reworking the post filter's caches for a second call and removing the refusal, which is a feature decision the maintainers have not made.

## Verdict justification

keep-open: the attempts made are the code read and the history check above at the pinned head, and the Jira read. The missing piece is the maintainer decision on whether collapse should be made to work with grouping (and the design for resetting the caches), which the refusal in SOLR-13970 leaves open. close-fixed and close-obsolete are not supported: the feature is not built and the refusal is current. pr-needed is not supported without that decision.

Missing for a closing or PR verdict: the decision on supporting grouping with collapse, and a design for the cache reset.

## Limits

This record does not run a grouping-and-collapse request. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
