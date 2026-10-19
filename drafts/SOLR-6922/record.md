# Skip record: SOLR-6922

- Ticket: SOLR-6922, Spatial example in Solr. JIRA status not recorded in the C reading. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-6922-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the C reading (reports/2026-10-18-skip-repro/C2, slice 2, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: Spatial example in Solr.

## Code reading

unclear | The RPT type exists in solr/server/solr/configsets/sample_techproducts_configs/conf/managed-schema.xml:711 and *_srpt in solr/server/solr/configsets/_default/conf/managed-schema.xml:160. Techproducts' location field is LatLonPoint (line 147), and no geonames dataset is under solr/example. Next step: Nick decides whether these meet the request.

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
