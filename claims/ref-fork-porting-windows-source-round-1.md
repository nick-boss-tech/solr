# Claim: reference fork porting branches, Windows source phase

- Status: CLAIMED, source implementation and ranking only; Linux proof pending.
- Host: windows; tags implementation, review, records.
- Started: 2026-11-06; owner request in this chat: produce branches for the
  reference-fork porting ideas, rank contribution value, prioritize removal
  of Overseer.
- Board snapshot reviewed: `936322de124342b38c9b6673ca9b2ca21575bc08`.
- Source base: Apache main `c0c219e374611e0edc475c0f793c9bd688b13292`.
- Reference fork: `4ce82ee7bb425595365ef9e392179730ae4abc0d`, subject to live-tip
  confirmation.

Scope: source-only slices for unused Overseer queues/maps (SOLR-17946),
distributed command conflict safety, and bounded admission/failure evidence;
rank and document the resulting branches and any experiment that cannot yet
justify a production patch. Reuse existing SOLR-16542 implementation branch
`solr-16542-submit` at `85ce6f5475a259f61f786edcbf0058852a9f5db2` rather than
duplicate it. No existing SOLR-17946 implementation claim or fork branch was
found in this snapshot/read-back.

This is an owner-directed source phase. The existing SOLR-17946 assignment
names a Linux host; its runtime proof remains a separate Linux phase. No
Gradle, compiler, test, or benchmark runs will execute on Windows. Preserve
`/overseer/queue` for QUIT/leadership control and cluster-singleton lifecycle;
do not interpret the ticket literally as deleting that still-used queue.

Deliverables: ordinary commits pushed to distinct fork branches, local
`research/solr-reference-branch/contribution-ranking.md` and branch notes,
add-only implementation report and exact-head Linux gate jobs on this board.
No public PR/Jira writes. Decision ladder: obtain code/history evidence,
implement bounded changes, retain unproved performance questions as named
experiments rather than claim speedups.
