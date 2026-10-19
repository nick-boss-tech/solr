# Skip record: SOLR-11269

- Ticket: SOLR-11269, Node gone after reconnecting to ZK. JIRA status as recorded in the B3 reading: Open, 2019-10-02, 3. JIRA was not re-read for this record.
- Verdict: keep-open.
- Draft branch: solr-11269-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, from the B3 reading (reports/2026-10-18-skip-repro/B3-committed-support-round-1.md, vm2, round 1, pinned head unchanged).

## Ticket reading

The summary as recorded in the queue: Node gone after reconnecting to ZK. B3 row type: skip note: addressed. Skip note from the B3 reading: The skip note is not borne out. The live-node creation path still lets the node go missing after reconnect.

## Code reading

solr/core/src/java/org/apache/solr/cloud/ZkController.java:1267-1288 createEphemeralLiveNode has no NodeExistsException catch; the OnReconnect caller at :1120 rethrows at :1126-1129; the only NodeExistsException catch is at :2676, which is for config resources

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
