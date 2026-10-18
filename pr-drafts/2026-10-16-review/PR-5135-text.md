# PR 5135 (SOLR-6975): body draft, round 2 item 7

- Head: c666155ed058a0535f60e8c51a49c25f17821f98 (live body read 2026-10-18T04:33Z; branch head matches).
- Visible characters: 3831 (no limit set for this item).
- RE-DRAFT NEEDED if code round 1 item 3 changes the ordering behavior. That item has not landed (its default is escalate). The draft describes the comparator as it stands at c666155, `ShardFieldSortedHitQueue.lessThan` (lines 82 to 111) and the `DOC` comparator (lines 123 to 126 at 761aa629bb83, same code at c666155).
- Proof numbers come only from receipts/SOLR-6975.md: 10 of 10 at c666155 (2026-10-15); base e432df19c4a with the branch test file: testSortByDocId fails.
- Changes from the stored body: (1) the leading-key sentence is replaced. The stored sentence said that when `_docid_` is one of several keys, the other keys decide the order. For a leading `_docid_` that is wrong: documents from one shard keep shard order, documents from other shards are compared on the other keys, and those rules can contradict each other. (2) The grouping sentence in "What this change does" is limited to the single-key case, because with other keys the cross-shard comparison uses them.
- Not run: the reasoning about the comparator was read from code, not tested with a multi-key request.

---

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-6975

## What happens today

**A distributed search that sorts on `_docid_` fails while the coordinating node merges the shard responses.**

With `q=*:*`, `fl=id,test_sS`, `sort=_docid_ asc` and `rows=20` on a multi shard collection, the request throws `IndexOutOfBoundsException: Index: 0, Size: 0`. The exception comes from `NamedList.getName` in the shard comparator, under `QueryComponent.mergeIds` ([ticket with the stack trace](https://issues.apache.org/jira/browse/SOLR-6975)). The comparator file has not changed on main since this branch was cut, so the path is still there.

## What this change does

**A `_docid_` sort no longer reads a sort value from the shards. With `_docid_` as the only sort key, results from different shards are grouped by shard.**

Shards do not send `_docid_` values back, so the merge has nothing to compare for that key. The comparator for this key now reports a tie ([ShardFieldSortedHitQueue.java L123-L126](https://github.com/nick-boss-tech/solr/blob/761aa629bb83e241df2aefc114513797b7339522/solr/core/src/java/org/apache/solr/handler/component/ShardFieldSortedHitQueue.java#L123-L126)). Documents from one shard keep the order that shard returned, in both directions. With `_docid_` as the only sort key, documents from different shards are grouped by shard address, and the sort direction does not change that grouping ([lessThan, L82-L111](https://github.com/nick-boss-tech/solr/blob/761aa629bb83e241df2aefc114513797b7339522/solr/core/src/java/org/apache/solr/handler/component/ShardFieldSortedHitQueue.java#L82-L111)).

If `_docid_` is the first sort key, the merge ignores that key, and the other keys do not give one fixed order. Documents from one shard keep the order that shard returned. Documents from different shards are compared on the other keys. Those two rules can contradict each other, so the result depends on which pairs the merge compares. The shard address breaks only full ties.

## Proof

**The new test fails on the base commit and passes with this change.**

- [`DistributedQueryComponentOptimizationTest`](https://github.com/nick-boss-tech/solr/blob/c666155ed058a0535f60e8c51a49c25f17821f98/solr/core/src/test/org/apache/solr/handler/component/DistributedQueryComponentOptimizationTest.java#L350-L375) 10 of 10 passed at `c666155ed058`, verified 2026-10-15. That run includes the shard-group assertion added in `c666155ed058`.
- With `ShardFieldSortedHitQueue.java` reverted to the base commit `e432df19c4a` and the new test kept, `testSortByDocId` fails. On the server side the cause is the same `IndexOutOfBoundsException` from `NamedList.getName` under the shard comparator, during the shard merge. At the client the request fails with "No live SolrServers available".
- The test sends an ascending and a descending `_docid_` request. It checks that the number of returned documents equals `numFound` and is above zero, and that each shard's hits come back as one contiguous group. It does not check which shard comes first, because that order is not defined.

## A choice to check

**With `_docid_` as the only sort key, documents from different shards come back grouped by shard. The order across shards is not defined.**

Options considered:

1. Group by shard, as implemented. The request succeeds. Each shard keeps its own order, and the groups follow the shard address.
2. Reject a distributed `_docid_` sort with a clear error. The cost: a request whose hits all come from one shard works today, and it would fail too.

Was grouping by shard the right call, or should distributed `_docid_` sorts fail with a clear error?

## Limits

**Multi key `_docid_` sorts and CQC are not tested here, and a separate payload fetch failure is not fixed here.**

- The new test covers one `_docid_` key. The behavior with several keys is described above but not tested.
- `CombinedQueryComponent` (CQC) builds the same merge queue from the request's sort fields, so the same tie applies there. This PR does not test that path. A follow-up PR will cover it.
- A payload fetch failure in a distributed query also happens on the base commit. I did not look into it here, and this change does not touch it. I can open a follow-up ticket and PR for it on request.

Changelog: [changelog/unreleased/SOLR-6975-distributed-docid-sort.yml](https://github.com/nick-boss-tech/solr/blob/761aa629bb83e241df2aefc114513797b7339522/changelog/unreleased/SOLR-6975-distributed-docid-sort.yml)

### AI assistance

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.
