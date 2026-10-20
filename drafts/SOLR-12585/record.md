# Skip record: SOLR-12585

- Ticket: SOLR-12585, Solr fails even ZK quorum has majority.
- Verdict: keep-open.
- Draft branch: solr-12585-draft-open (verdict unchanged; this record replaces the earlier record on this branch).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm5 (pool Linux host), 2026-10-20 UTC. Redo of remediation batch 08, item 10 (`assignments/pool-aaa-skip-verdict-remediation-08.md`).

## Ticket reading

JIRA read 2026-10-20: Open, no resolution, created 2018-07-24, updated 2019-06-08, no fix versions, no links, no comments. The report: a Solr cloud cluster stops working when one ZooKeeper node becomes unreachable through a DNS failure, although the other two ZooKeeper nodes form a majority. The ticket links a mailing-list thread for detail; the thread is not in the ticket text.

## Code reading

Read at the pin:

- `gradle/libs.versions.toml:44`: `apache-zookeeper = "3.9.5"`, with the `zookeeper` client artifact at lines 268-269. The host resolution and connection-string handling that decides whether a DNS failure on one server blocks the client are in the ZooKeeper client library, not in this tree.
- The Solr-side ZooKeeper connection code was not traced for how it handles a connect string with an unresolvable host.
- No commit at the pin names SOLR-12585 (the evidence tool lists none).

## History

- The ZooKeeper version bump history was not traced in this pass.

## Reproduction attempts

Not run. This pass ran no builds or tests. The reproduction is a three-node ZooKeeper ensemble with one host name made unresolvable, a Solr cloud node pointed at the connect string, and the cluster's request behavior observed at the pin.

## Gap analysis

- The failure mechanism sits in the ZooKeeper client (3.9.5 at the pin); whether the reporter's version behaves the same is not established.
- Solr's own handling of the connect string was not traced.
- The mailing-list thread the ticket links is not available in the ticket text.

## Verdict justification

keep-open: the mechanism is in the ZooKeeper dependency and the Solr connection path has not been traced or run, so neither a close nor a fix can be recorded. close-obsolete and close-fixed do not fit (no removal or fix commit).

Missing for this verdict: a run with one unresolvable ZooKeeper host name at the pin (ZooKeeper 3.9.5), and the Solr-side connect handling trace, or a decision that the behavior is the ZooKeeper client's expected behavior.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
</content>
