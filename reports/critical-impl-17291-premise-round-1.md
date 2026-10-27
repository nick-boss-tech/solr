# SOLR-17291 premise run, round 1 (vm2)

Verdict: PREMISE NOT REPRODUCED ON MAIN (killed by code reading, rung 1). No probe run, no implementation, no branch, no gate request.

- Claim: `claims/critical-impl-17291-round-1-vm2.md`. Assignment: `assignments/critical-impl-17291-round-1.md` (step 1 only).
- Base for reading: apache/solr `upstream/main` `ee2f1bf2e01`.
- Ticket: SOLR-17291, Open, affects 9.0. Reported message: "Error opening new searcher / Caused by indexDeletionPolicy must not be null", when a new replica was created.

## Why the premise fails on main

- `solr/core/src/java/org/apache/solr/core/SolrCore.java` lines 597 to 616: the deletion policy for a core is built from the `IndexDeletionPolicy` plugin when one is configured. When none is configured, the code uses `new SolrDeletionPolicy()`. It then returns `new IndexDeletionPolicyWrapper(delPolicy, snapshotMgr)`. So a configuration with the policy unset gives a non-null wrapper, and the premise "openNewSearcher receives a null policy" cannot happen on this path.
- The text "indexDeletionPolicy must not be null" does not appear in `solr/core/src/java` on main. It most likely comes from Lucene's `IndexWriterConfig` when given a null policy, which Solr no longer passes on this path.

## Limits

- No probe was run. The verdict rests on reading main's code, not on a reproduction.
- The 9.0 code path was not checked. The ticket's stack is from 9.0.0 (`solr-core-9.0.0.jar`, commit `a4eb7aa123dc`). If the bug exists in 9.0 only, it is already changed on main, and a fix on this path would need a separate check.
- No fix or gate work follows from this verdict.

## Not done

- No step 2 implementation, no branch `solr-17291-submit`, no gate request.
- No PR, Jira, or GitHub write.
