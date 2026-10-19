# Skip record: SOLR-6468

- Ticket: SOLR-6468, Regression: StopFilterFactory doesn't work properly without deprecated enablePositionIncrements="false". JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: close-obsolete.
- Draft branch: solr-6468-draft-close.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C2, slice 2, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: Regression: StopFilterFactory doesn't work properly without deprecated enablePositionIncrements="false".

## Code reading

gone | git grep finds no enablePositionIncrements in solr/core/src/java or solr/core/src/test, and there is no top-level lucene/ directory. Only commented-out lines remain in solr/core/src/test-files/solr/collection1/conf/schema-tagger.xml:170 and :178. Lucene sources were removed from main by 168df90d79 (SOLR-14497), and the option was dropped from test schemas in 513cefa5e9 (LUCENE-6044).

## History

- `168df90d79`: cited in the C2 reading; ancestor of the pinned head: yes (checked).
- `513cefa5e9`: cited in the C2 reading; ancestor of the pinned head: yes (checked).

## Reproduction attempts

Not attempted. The C scope ran no builds or tests.

## Gap analysis

The code reading shows the named code is gone at the pinned head, as described above.

## Verdict justification

close-obsolete: the feature or code the ticket names was removed, and the search at the pinned head finds no remaining use (cited in the code reading).

Missing for this verdict: The grep result is taken from the C reading; it was not re-run in this record.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path where the subject does not cite the ticket.
