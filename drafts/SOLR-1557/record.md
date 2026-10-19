# SOLR-1557 closeout record

Verdict: keep-open (draft branch `solr-1557-draft-open`).

- Pinned head: fork `origin/main` `498a09aea97aac86c2cc7ac114113b946d249942`. Upstream history is not cited.
- JIRA checked 2026-10-19: status Open, no resolution, no fix versions (removed 2012-06-14). Created 2009-11-12, updated 2012-06-14. Description read in full. Nine comments read in full: a 2009 design exchange (whitespace, quoting, multi-valued shards) between two developers, then bulk fix-version edits and the 2012 removal of the fix version.
- Audit verdict on record: `confirmed-skip` (audit-3, 2026-10-05): design decision, needs a maintainer call; no deterministic test could be written from the ticket text.

## Code paths read at the pinned head

- `solr/core/src/java/org/apache/solr/handler/component/HttpShardHandler.java:473`: the `shards` parameter is read with `params.get(ShardParams.SHARDS)`. The split into individual shard URLs happens after this read; that step was not traced for this record.

## Attempts

- Read the ticket and its comments. The 2009 exchange is a design argument with no agreed outcome: one side wants whitespace allowed and multi-valued shards, the other wants the existing split kept for compatibility.
- No reproduction. The ticket asks for a behaviour change (accept whitespace, accept multi-valued shards), not a failure.

## What the verdict does not cover

- Whether the current split still uses the quoting and escape handling the 2009 comments criticise. Not traced.
- Whether a multi-valued shards parameter would be accepted today. Not tested.

## What discharges keep-open

A maintainer decision on the parsing contract (keep comma-split for compatibility, or accept whitespace and multi-valued shards). Then a PR with a focused test, or a closeout as won't-do with the decision linked. Either is the owner's call.
