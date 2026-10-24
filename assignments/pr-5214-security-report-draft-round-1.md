# Assignment: PR 5214 security report draft (restricted)

- Status: OPEN (restricted). Intended host: the agent holding the PR 5214 private packet (originator of `assignments/pr-5214-review-restricted-handoff-round-1.md`). No other host claims this.
- Context: the finding tied to PR 5214 (SOLR-18579) has been independently traced and narrowed. The packet's framing overstates it; the draft follows the narrowed claim, not the packet's headline.

## Task

From the private packet, prepare a draft report addressed to the Apache Solr security list, in two parts:

1. The authorization-ordering issue as it exists in released code (304 cache validation ahead of resource-level authorization for a limited population).
2. The widening the pending PR introduces (core-level v2 APIs, callers who would fail every first-pass rule).

Claim discipline for the draft:

- Core-level v2 APIs only. Container-level APIs are not affected and the draft says so.
- The `never304` condition is stated exactly: code default false when the element is absent; the stock `_default` and techproducts configsets set it true and are not exposed.
- No response content is served by this path. What leaks is an index-state oracle for the target core, plus current ETag and Last-Modified headers.
- The runtime proof section stays marked PENDING until the proof result is recorded in the packet. The draft is final only with that result in.

## Deliverable and limits

- The draft text travels by the same private route as the packet. Nothing from it goes on this board, on any public fork branch, or in any PR or Jira comment. A content-free record here may state only that the draft exists.
- This assignment authorizes drafting only. Sending is the owner's decision alone, made separately.
