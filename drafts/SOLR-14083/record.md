# Skip record: SOLR-14083

- Ticket: SOLR-14083, Solr GUI access through Internet Explorer. JIRA status as recorded in the B3 reading: Open, 2020-01-21, 2. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-14083-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the B3 reading (reports/2026-10-18-skip-repro/B3-committed-support-round-1.md, vm2, round 1, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: Solr GUI access through Internet Explorer. B3 row type: support. Skip note from the B3 reading: The IE question is not answered by the docs.

## Code reading

no "internet explorer" or IE11 hits in solr/solr-ref-guide; solr/webapp/web/js/angular/app.js:18 (IE11 polyfill, SOLR-14120); basic-authentication-plugin.adoc:152-163 (browser prompt, not IE-specific)

## History

None cited as an ancestor in the B3 reading.

## Reproduction attempts

Not attempted. The B3 scope ran no builds or tests.

## Gap analysis

The B3 reading gives the missing piece; see the code reading and notes.

## Verdict justification

keep-open: the code reading does not settle the question; the B3 reading names the missing piece below.

Missing for this verdict: Reproduction on the pinned head, or the missing piece named in the B3 reading. B3 ran no builds.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
