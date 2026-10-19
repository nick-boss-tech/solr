# SOLR-1247 closeout record

Verdict: keep-open (draft branch `solr-1247-draft-open`).

- Pinned head: fork `origin/main` `498a09aea97aac86c2cc7ac114113b946d249942`. Upstream history is not cited.
- JIRA checked 2026-10-19: status Open, no resolution, no fix versions, created 2009-06-25, updated 2013-04-13. Description (one sentence) read in full. One comment (2013-04-13), a request to check whether it was addressed.
- Audit verdict on record: `confirmed-skip` (audit-3, 2026-10-06): "Stats on docValues now go through DocValuesStats (StatsField:439), not a top-level FieldCache; 1.4 behaviour is gone."

## Code paths read at the pinned head

- `solr/core/src/java/org/apache/solr/handler/component/StatsField.java:436-439`: multi-valued fields (non-point) use `DocValuesStats.getCounts`. The audit's line is right for this branch.
- `StatsField.java:441`: every other field (single-valued) goes to `computeLocalValueSourceStats`. The audit's "1.4 behaviour is gone" is therefore too broad.
- `StatsField.java:462-475`: `computeLocalValueSourceStats` walks `searcher.getIndexReader().leaves()` per segment context. It does not build a top-level FieldCache entry on this path. Blame puts these lines at commit `7ada403218` (2021-03-09), whose message is about SOLR-14749; that commit is not identified as a fix for this ticket.
- `StatsField.java:345`: the value source is still a `FieldCacheSource` for single-valued fields, so per-segment FieldCache entries are still created on that path.

## Attempts

- Read the audit's citation and the code around the cited line. Found the single-valued branch the audit did not mention.
- Searched history for the change that moved stats to per-segment reads (`git log -S`). No commit names SOLR-1247 or a fix for it.
- No memory measurement. The ticket describes a memory doubling in 2009-era code; nothing on the ticket gives a repro.

## What the verdict does not cover

- Whether per-segment FieldCache entries used for single-valued stats still double memory when the same field is also used for sorting or function queries. Not measured.
- The fixing commit, if one exists. Not identified.

## What discharges keep-open

A memory comparison on a current release (stats on one single-valued field, with and without a sort or function query on the same field), or identification of the commit that moved this path, then a closeout as close-fixed with that commit, or as close-cannot-reproduce if the measurement is flat.
