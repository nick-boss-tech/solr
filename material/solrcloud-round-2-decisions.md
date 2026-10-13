# SolrCloud round 2: decisions adopted (2026-10-13)

Nick said: go with the recommendations. Each decision below is the one the review side recommended. It is recorded here in the same push that acts on it.

## SOLR-9155: route and ops logging

1. **Route: declared InterruptedException (adopted).** The branch head `dbd88c84ff2` implements the declared route. Its receipt calls that head PR-ready. The 2026-10-09 answers file adopted wrapping. That adoption is superseded for this head. The draft's Choice still puts the route to maintainers, with the wrapping route and its cost stated.
2. **Ops logging: keep the base code's ERROR line (adopted).** The generic catch in `getLeader` had logged at ERROR on the base code. The head dropped that line. Branch commit `29bf3738289` restores it, as `log.error("Error getting leader from zk", e);`. The changelog title no longer says logging was removed. Gate pending: `gates/SOLR-9155-logging-regate.md`.

## SOLR-14919: replication scope

3. **Ship processor-only now (adopted).** The processor change fixes forwarded commits. The replication failure (recovery commits with no marker) is not fixed here. It needs its own ticket and test. The draft's Limits says so. A draft of that ticket is in `material/solr-14919-replication-ticket-draft.md` for Nick to file. Nothing has been filed.

## SOLR-16437: changelog wording

4. **Changelog says "not in that shard" (adopted).** The 400 path covers a collection or shard that does not exist, and a replica that is not in the named shard. The old title said the replica "does not exist", which is wrong for a replica on another shard. Branch commit `a23f770e4b9` rewords the title. The wrong-shard test stays a Limits line in the draft. Gate pending: `gates/SOLR-16437-changelog-regate.md`.

## Consequences

- Two branch heads changed (`29bf3738289`, `a23f770e4b9`). Their receipts are behind until the gates land. The drafts cite the new heads, and their Proofs stay bracketed until then.
- No JIRA write, PR, comment or email was made.
