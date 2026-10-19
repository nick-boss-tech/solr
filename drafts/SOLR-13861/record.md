# Skip record: SOLR-13861

- Ticket: SOLR-13861, SynonymGraphFilterFactory - with pattern tokenizer - not able to start. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-13861-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C4, slice 4, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: SynonymGraphFilterFactory - with pattern tokenizer - not able to start.

## Code reading

SynonymGraphFilterFactory is no longer in this repo (Lucene sources removed by 168df90d79, SOLR-14497) and comes from the external Lucene 10.4.0 jar (gradle/libs.versions.toml:39). The Solr test (solr/core/src/test-files/solr/collection1/conf/schema-synonym-tokenizer.xml:27) only uses StandardTokenizerFactory. Checking the failing tokenizerFactory.pattern handling needs the Lucene source.

## History

- `168df90d79`: cited in the C4 reading; ancestor of the pinned head: yes (checked).

## Reproduction attempts

Not attempted. The C scope ran no builds or tests.

## Gap analysis

The C reading gives the missing piece; see the evidence below.

## Verdict justification

keep-open: the code reading does not settle the question; the C reading names the piece that would settle it.

Missing for this verdict: The missing piece named in the C reading (see the evidence below).

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path where the subject does not cite the ticket.
