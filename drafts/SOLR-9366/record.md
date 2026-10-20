# Skip record: SOLR-9366

- Ticket: SOLR-9366, "replicas in LIR seem to still be participating in search requests". JIRA status read 2026-10-20T14:52:21Z (UTC): Open, resolution None, updated 2019-09-03. Linked: SOLR-3486 (Closed), SOLR-9367 (Closed), SOLR-13189 (Open), SOLR-9363 (Closed).
- Verdict: keep-open. This replaces the recorded close-obsolete: the recorded reason (LIR removed) names the mechanism, but the reported behavior (a replica recovering after an error still serving queries) depends on query routing, which this record did not trace.
- Draft branch: solr-9366-draft-open.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T14:52:21Z (UTC).

## Ticket reading

Replicas that are put into leader-initiated recovery (LIR) still take part in searches, and a commit they ignore while not active is never applied. The 2016 comments say the queries run before the down state propagates, and suggest such replicas answer with 503.

## Code reading (pinned head)

- `git grep -n -i "leaderInitiatedRecovery\|\bLIR\b" 498a09aea97aac86c2cc7ac114113b946d249942 -- solr/core/src/java/org/apache/solr/cloud`: empty. The LIR flag is gone as named.
- The equivalent step still exists: `solr/core/src/java/org/apache/solr/update/processor/DistributedZkUpdateProcessor.java:1327` adds a failing replica to `replicasShouldBeInLowerTerms`, which `:1360` acts on. So the leader still asks an erroring replica to recover, by term rather than by flag (the same finding as the SOLR-11776 record).
- Query routing (whether a recovering replica is excluded from search) was not traced in this pass. The `Slice.State.ACTIVE` check at `DistributedZkUpdateProcessor.java:1110` is in the update path, not the query path.

## History

- evidence.py at the pinned head: no commits naming SOLR-9366. SOLR-13189 (open) is linked by JIRA.

## Reproduction attempts

Not run. The reported behavior needs a SolrCloud run that puts a replica into recovery during queries and records the responses; the 2016 test (TestStressCloudBlindAtomicUpdates) is the reference.

## Gap analysis

The name of the mechanism is gone, but the step that asks an erroring replica to recover is still there. Whether a recovering replica still answers queries is the open question, and the query routing path decides it.

## Verdict justification

keep-open meets the rubric: attempts made are the grep for the LIR mechanism and the reading of the term-based recovery step; the missing piece is the query-routing trace or a SolrCloud run. close-obsolete is not supported: the mechanism is replaced, not removed. close-fixed is not supported: no fix commit is named.

## Limits

This record does not establish the query behavior. It does not check SOLR-13189.
