# Skip record: SOLR-7192

- Ticket: SOLR-7192, Poor performance of Hunspell with Czech Dictionary. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-7192-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C2, slice 2, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: Poor performance of Hunspell with Czech Dictionary.

## Code reading

unclear | The Hunspell stemmer is not in this tree. It comes from Lucene 10.4.0 (gradle/libs.versions.toml:39). The tree holds only the test dictionary solr/core/src/test-files/solr/collection1/conf/hunspell-test.aff and a ref-guide page. No cs_CZ dictionary or benchmark. Next step: time cs_CZ stemming on Lucene 10.4.0 against CzechStemFilter.

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
