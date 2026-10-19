# Skip record: SOLR-10806

- Ticket: SOLR-10806, Solr Replica goes down with NumberFormatException: Invalid shift value (64) in prefixCoded bytes (is encoded value really an INT?). JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-10806-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C3, slice 3, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: Solr Replica goes down with NumberFormatException: Invalid shift value (64) in prefixCoded bytes (is encoded value really an INT?).

## Code reading

Message still produced by LegacyNumericUtils.java:165,180 for Trie fields; default configset now uses Point types (managed-schema.xml:217) and AddSchemaFields retries on SchemaChangedInZk (AddSchemaFieldsUpdateProcessorFactory.java:525); cross-shard type-guess theory never reproduced

## History

- None cited in the C reading.

## Reproduction attempts

Not attempted. The C scope ran no builds or tests.

## Gap analysis

The C reading gives the missing piece; see the evidence below.

## Verdict justification

keep-open: the code reading does not settle the question; the C reading names the piece that would settle it.

Missing for this verdict: The missing piece named in the C reading (see the evidence below).

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path where the subject does not cite the ticket.
