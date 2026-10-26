# Claim: PR 5108 deterministic reverse shard merge regression

Status: CLAIMED. Host: windows. Tags: implementation, review, records.
Started: 2026-10-26T19:32:11Z.

Owner-directed assignment in the Windows Codex chat: "Implement the new test then hand off the gate" following Houston Putman's multi-shard reverse merge question on apache/solr#5108.

Board snapshot reviewed: `dc64c75f92c4e63022f697d07b65fe719965d6ea`. Existing round-2 work is complete at `fde070281383f113a331a9ec5ccde173b66fac3c`; its receipt does not prove a new head. No live claim for this new regression was found.

Slice: add a deterministic two-shard regression to `DistribCursorPagingTest`, verify placement and exact global selection, cursor boundaries and navigation, including ties across shards. Push a normal descendant to `nick-boss-tech/solr:solr-17786-reverse-cursor-submit`. Production code is outside this slice. No Windows Gradle or tests.

Deliverables: a pushed test commit; `gates/pr-5108-reverse-merge-round-3.md` naming the exact head and coordinator-only comparator mutation; `reports/pr-5108-reverse-merge-regression-round-3.md` recording the implementation and proof-pending status.

The Linux gate is a separate open pool job for a capable Linux gate host, serialized under its host build lock. No public PR or JIRA write is part of this claim.
