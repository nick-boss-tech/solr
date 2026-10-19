# Skip record: SOLR-5941

- Ticket: SOLR-5941, "CommitTracker should use the default UpdateProcessingChain for autocommit". JIRA status Open, resolution none, fix versions listed as 4.9 and 6.0, created 2014-04-01, last updated 2026-10-09, checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-5941-draft-open.
- Pinned head: fork origin/main `498a09aea97aac86c2cc7ac114113b946d249942`.
- Checked by: vm5, 2026-10-19 UTC.

## Ticket reading

Autocommit calls the update handler directly, so a custom update processor with a commit action is not run when autocommit fires; it only runs on an explicit client commit. The ticket asks that autocommit use the default update processing chain. Comments: lboutros (2014-04-01) attaches a small starting patch and offers help (2014-04-14); uschindler (2014-04-16) moves the ticket to 4.9. The fix versions are a bulk move, not a fix.

## Code reading (at `498a09aea97aac86c2cc7ac114113b946d249942`)

- `solr/core/src/java/org/apache/solr/update/CommitTracker.java:256-282`: `run()` builds a `CommitUpdateCommand` and calls `core.getUpdateHandler().commit(command)` at line 280. It does not go through the update processor chain.
- `solr/core/src/java/org/apache/solr/update/processor/DistributedUpdateProcessor.java:1157-1166`: `processCommit` and `doLocalCommit` are the chain's commit path. A commit that comes from a request runs through the chain.
- `solr/core/src/java/org/apache/solr/update/processor/DistributedZkUpdateProcessor.java:157`: in SolrCloud the chain's `processCommit` forwards the commit to the other replicas.

So the reported gap is present at the pinned head. Routing the autocommit through the chain would make the autocommit run the chain's commit action, and in SolrCloud it would also forward each autocommit to the replicas through `DistributedZkUpdateProcessor`. That second effect is a behavior change for every SolrCloud autocommit, and the ticket does not decide it.

## History

- The ticket's patch (2014-04-01) is not on main (no commit names SOLR-5941; `git log --grep` at the pinned head returns nothing).
- The fix versions 4.9 and 6.0 came from a bulk move (2014-04-16), not from a fix.

## Reproduction attempts

Not attempted. The behavior is visible in the code path at `CommitTracker.java:280` and does not need a run to show the gap. A reproduction of the processor not being run on autocommit would be a test with a custom processor that counts commit actions and a short autocommit interval; that test is the first piece of the PR, not part of this closeout.

## Gap analysis

The gap is real at the pinned head. The fix choice is not: (a) route autocommit through the default chain everywhere, which changes SolrCloud autocommit to forward commits to replicas; (b) route it through the chain only on standalone cores; (c) add a new option so operators choose. Each is a behavior decision for the maintainers, and the audit's note (2026-10-06) says the same.

## Verdict justification

keep-open: the code path is shown with file and line, and the missing piece is the design decision on how autocommit should interact with the chain in SolrCloud (options a, b, c above). pr-needed needs that decision made; the ticket and the code do not make it. close-fixed does not apply (the gap is still there). close-obsolete does not apply (the class is still used). The keep-open minimum is met.

## Limits

This record does not change behavior and does not run a test. It does not read the 2014 patch attachment. It does not check the replica-forwarding path beyond the processor's `processCommit` override.
