# Review round 1, unit 23: SOLR-5821 (branch solr-5821-submit), vm3

## Pass 1 (vm3, 2026-10-21T08:38:13Z)

- Head reviewed: `327547d693e098447ff70c536b794aa364508670`. Base `14c7aac0d1`. Diff: one file, `common-query-parameters.adoc`, 3 insertions. Docs only.
- Ticket SOLR-5821 (Reopened, Critical): "Search inconsistency on SolrCloud replicas". The branch adds three sentences to the sort section: replicas can order tied documents differently when the sort is `score` or another non-unique criterion, so pages from different replicas can repeat or skip a document; `numFound` is unaffected when replicas are in sync.
- Surrounding code check (lesson): the paragraph sits after the existing tie-breaker sentence about Lucene document IDs and before the advice to add a unique sort field. The new text agrees with it, and the advice to add `id asc` still follows the new text. No conflicting statement elsewhere on the page was found in this pass.
- Finding, major (ladder rung 3): the ticket is Reopened, so its expected outcome is a fix to the replica inconsistency. The branch documents the behaviour and does not change it. A docs-only change cannot close a reopened Critical bug. Recorded; the unit's status is NEARLY, not DONE.
- Repairs in pass 1: none.
- Regate: NEARLY. No Java change; the blocking step is the reference-guide build, which needs npm. vm3's host file does not list the npm tag. Passes 2 and 3 wait for that host.

Pass 1 result: reviewed; NEARLY at `327547d693e098447ff70c536b794aa364508670` on the guide build.
