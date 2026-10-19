# Skip record: SOLR-13572

- Ticket: SOLR-13572, NullPointerException leads to msg=null.. JIRA status as recorded in the B3 reading: Open, 2020-01-05, 2. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-13572-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the B3 reading (reports/2026-10-18-skip-repro/B3-committed-support-round-1.md, vm2, round 1, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: NullPointerException leads to msg=null.. B3 row type: skip note: NPE null-guarded. Skip note from the B3 reading: The skip note is not borne out: a null-message exception still yields msg=null.

## Code reading

solr/core/src/java/org/apache/solr/update/DocumentBuilder.java:286-296 generic catch concatenates ex.getMessage() without a null guard into "msg="; git grep 'Error adding field' at the pinned head finds only :279 and :291

## History

None cited as an ancestor in the B3 reading.

## Reproduction attempts

Not attempted. The B3 scope ran no builds or tests.

## Gap analysis

The B3 reading gives the missing piece; see the code reading and notes.

## Verdict justification

keep-open: the code path is still present at the pinned head, but the behavior has not been shown on a build.

Missing for this verdict: Reproduction on the pinned head, or the missing piece named in the B3 reading. B3 ran no builds.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
