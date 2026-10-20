# Skip record: SOLR-11932

- Ticket: SOLR-11932, "ZkCmdExecutor: Retry ZkOperation on SessionExpired". JIRA status read 2026-10-20T06:42:10Z (UTC): Open, resolution None, no fix versions, updated 2021-08-13.
- Verdict: close-obsolete. This replaces the recorded keep-open: the class the ticket patches is no longer on main.
- Draft branch: solr-11932-draft-close.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T06:42:10Z (UTC).

## Ticket reading

The reporter wants `ZkCmdExecutor` to retry an operation after `SessionExpired` as it does after `ConnectionLoss`, with a patch that catches both. Shalin Mangar's comment says the command holds a `SolrZooKeeper` instance that is unusable after expiry, so the retry needs a new instance. The ticket names no test.

## Code reading (pinned head)

- The grep the verdict rests on: `git grep -l "ZkCmdExecutor" 498a09aea97aac86c2cc7ac114113b946d249942 -- solr`. Result: empty. No Java, test, or doc file under `solr/` names the class at the pinned head.
- The only remaining hits anywhere in the tree are historical entries in `CHANGELOG.md` and `changelog/archive.md`, which are release notes, not code.
- Session-expiry handling that exists now is in other classes: `solr/core/src/java/org/apache/solr/cloud/ShardLeaderElectionContext.java:295, 319, 436`, `LeaderElector.java:153`, `Overseer.java:302, 321, 379`. Each handles SessionExpired in its own context; none is the ZkCmdExecutor retry the ticket asks for.

## History

- evidence.py at the pinned head: no commits naming SOLR-11932.
- The commit that removed `ZkCmdExecutor` was not located in this pass. A path-scoped history search is the next step if the removal date is needed for the record.

## Reproduction attempts

Not attempted. The code the ticket is about is absent, so there is nothing at the pinned head to run against.

## Gap analysis

The retry the reporter proposed cannot be applied to code that no longer exists. Whether the same retry gap exists in the current session-expiry handlers listed above is a different question, and it is not this ticket.

## Verdict justification

close-obsolete meets the rubric: the class the ticket is about has been removed, and the search of main finds no remaining use in code (the empty `git grep` above, run at 498a09aea97aac86c2cc7ac114113b946d249942 under `solr/`). The rubric's requirement is the grep and its empty result, which is recorded here.

## Limits

This record does not name the commit that removed the class. It does not decide whether the current session-expiry handlers need the same retry. That would be a new ticket.
