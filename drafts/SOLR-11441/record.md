# Skip record: SOLR-11441

- Ticket: SOLR-11441, windows shell splits args on "=" so we should consider updating our docs to always quote args like -Dfoo=bar or improve bin/solr.cmd to account for this. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-11441-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C3, slice 3, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: windows shell splits args on "=" so we should consider updating our docs to always quote args like -Dfoo=bar or improve bin/solr.cmd to account for this.

## Code reading

solr/bin/solr.cmd:678-707 set_passthru already rejoins a '-Dkey' arg split from its value (44ea44d1fb Fix argument parsing for -D options with multiple values on Windows), but needs a Windows PowerShell test; docs still unquoted at solr/solr-ref-guide/modules/query-guide/pages/learning-to-rank.adoc:282

## History

- `44ea44d1fb`: cited in the C3 reading; ancestor of the pinned head: yes (checked).

## Reproduction attempts

Not attempted. The C scope ran no builds or tests.

## Gap analysis

The C reading gives the missing piece; see the evidence below.

## Verdict justification

keep-open: the code reading does not settle the question; the C reading names the piece that would settle it.

Missing for this verdict: The missing piece named in the C reading (see the evidence below).

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path where the subject does not cite the ticket.
