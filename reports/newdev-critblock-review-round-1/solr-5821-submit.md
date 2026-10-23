# Review round 1, unit 23: SOLR-5821 (branch solr-5821-submit), vm3 and phone-agent-2

## Pass 1 (vm3, 2026-10-21T08:38:13Z)

- Head reviewed: `327547d693e098447ff70c536b794aa364508670`. Base `14c7aac0d1`. Diff: one file, `common-query-parameters.adoc`, 3 insertions. Docs only.
- Ticket SOLR-5821 (Reopened, Critical): "Search inconsistency on SolrCloud replicas". The branch adds three sentences to the sort section: replicas can order tied documents differently when the sort is `score` or another non-unique criterion, so pages from different replicas can repeat or skip a document; `numFound` is unaffected when replicas are in sync.
- Surrounding code check (lesson): the paragraph sits after the existing tie-breaker sentence about Lucene document IDs and before the advice to add a unique sort field. The new text agrees with it, and the advice to add `id asc` still follows the new text. No conflicting statement elsewhere on the page was found in this pass.
- Finding, major (ladder rung 3): the ticket is Reopened, so its expected outcome is a fix to the replica inconsistency. The branch documents the behaviour and does not change it. A docs-only change cannot close a reopened Critical bug. Recorded; the unit's status is NEARLY, not DONE.
- Repairs in pass 1: none.
- Regate: NEARLY. No Java change; the blocking step is the reference-guide build, which needs npm. vm3's host file does not list the npm tag. Passes 2 and 3 wait for that host.

Pass 1 result: reviewed; NEARLY at `327547d693e098447ff70c536b794aa364508670` on the guide build.

### Pass 1 guide-build continuation (phone-agent-2, 2026-10-23T06:17:04.255186+00:00)

- Resumed only the named npm-capable guide-build step; reused vm3's completed pass 1 review. The prior NEARLY mark is historical and superseded by this GREEN continuation.
- Regate: GREEN at `327547d693e098447ff70c536b794aa364508670`. Runner `/workspace/tools/phone-agent-2-5821-docs-gate.py`; standalone checkout `/workspace/solr-5821-antora`; logs `/workspace/phone-agent-2-logs/solr-5821-review/pass-1/`.
- Tidy rc=0; forced Antora build rc=0; three added sentences each render once; obsolete same-searcher wording absent; existing unique-sort advice retained. Tracked tree clean.
- No repair or product commit. No Java tests, fail-before proof, or full checkSiteLinks run or claimed. The major ticket-fit finding remains; it does not prevent completing the prescribed three review/regate passes under the assignment's decision row.

Pass 1 completed result: GREEN.

## Pass 2 (phone-agent-2, 2026-10-23T06:17:04.288299+00:00)

- Head reviewed: `327547d693e098447ff70c536b794aa364508670`; base `14c7aac0d151`. Previous pass regate is GREEN. Re-read the unchanged three-line diff and surrounding sort/pagination guidance; no production or test code changed.
- Ticket: [SOLR-5821](https://issues.apache.org/jira/browse/SOLR-5821), Reopened, Critical. REST ticket and all eight comments read; comment 8 distinguishes unstable tie ordering from unexpected differing counts after replicas have exposed the same corpus. Saved input: `/workspace/phone-agent-2-logs/solr-5821-review/ticket.json`.
- Finding, major, `solr/solr-ref-guide/modules/query-guide/pages/common-query-parameters.adoc:74-76`: ticket-fit limitation retained from pass 1. This is accurate guidance about tied sorting, not a repair for the reopened replica-consistency bug. It does not synchronize replicas or address persistent document-count divergence. No implication that this branch closes the bug.
- Finding, minor, same file line 77 (inherited on base, outside the three-line diff): an additional rarely-shared field reduces ties but cannot guarantee prevention. A unique sortable field is required to guarantee a tie-break among otherwise equal sort values. The existing `price desc,id asc` example is sound when `id` is unique. Recorded only; unrelated inherited wording is not repaired in this bounded gate round. A unique secondary key does not itself cure replica score/statistics differences, which `pagination-of-results.adoc:121-126` already explains.
- Added sentences at lines 74-76 accurately describe possible replica-local Lucene-ID tie order, offset-page repeats/skips, and the unchanged matching count under their stated in-sync/same-committed-documents condition. No new anchor or link was added. Surrounding unique-sort advice remains in place.
- Changelog: none, consistent with the existing docs-only receipt convention. No Java regression or fail-before test applies to a prose-only change. Rendered-page checks are the evidence shape, not claimed runtime proof.
- Repairs: none. No new blocker or major implementation defect beyond the retained ticket-fit limitation. Regate result recorded below.

### Pass 2 regate result

- GREEN at `327547d693e098447ff70c536b794aa364508670`. Tidy rc=0; forced Antora build rc=0; fresh rendered-page verification rc=0. Each added sentence appears once, obsolete wording is absent, and unique-sort advice remains. Tracked worktree clean.
- Runner: `/workspace/tools/phone-agent-2-5821-docs-gate.py`. Logs and durable checklist: `/workspace/phone-agent-2-logs/solr-5821-review/pass-2/state.json`. Rendered evidence: `/workspace/phone-agent-2-logs/solr-5821-review/pass-2/rendered-common-query-parameters.html`.
- No repair, product commit, Java tests, full checkSiteLinks, or runtime bug-fix claim.

Pass 2 completed result: GREEN.

## Pass 3 (phone-agent-2, 2026-10-23T06:19:35.063240+00:00)

- Head reviewed: `327547d693e098447ff70c536b794aa364508670`; base `14c7aac0d151`. Previous pass regate is GREEN. Re-read the unchanged three-line diff and surrounding sort/pagination guidance; no production or test code changed.
- Ticket: [SOLR-5821](https://issues.apache.org/jira/browse/SOLR-5821), Reopened, Critical. REST ticket and all eight comments read; comment 8 distinguishes unstable tie ordering from unexpected differing counts after replicas have exposed the same corpus. Saved input: `/workspace/phone-agent-2-logs/solr-5821-review/ticket.json`.
- Finding, major, `solr/solr-ref-guide/modules/query-guide/pages/common-query-parameters.adoc:74-76`: ticket-fit limitation retained from pass 1. This is accurate guidance about tied sorting, not a repair for the reopened replica-consistency bug. It does not synchronize replicas or address persistent document-count divergence. No implication that this branch closes the bug.
- Finding, minor, same file line 77 (inherited on base, outside the three-line diff): an additional rarely-shared field reduces ties but cannot guarantee prevention. A unique sortable field is required to guarantee a tie-break among otherwise equal sort values. The existing `price desc,id asc` example is sound when `id` is unique. Recorded only; unrelated inherited wording is not repaired in this bounded gate round. A unique secondary key does not itself cure replica score/statistics differences, which `pagination-of-results.adoc:121-126` already explains.
- Added sentences at lines 74-76 accurately describe possible replica-local Lucene-ID tie order, offset-page repeats/skips, and the unchanged matching count under their stated in-sync/same-committed-documents condition. No new anchor or link was added. Surrounding unique-sort advice remains in place.
- Changelog: none, consistent with the existing docs-only receipt convention. No Java regression or fail-before test applies to a prose-only change. Rendered-page checks are the evidence shape, not claimed runtime proof.
- Repairs: none. No new blocker or major implementation defect beyond the retained ticket-fit limitation. Regate result recorded below.

### Pass 3 regate result

- GREEN at `327547d693e098447ff70c536b794aa364508670`. Tidy rc=0; forced Antora build rc=0; fresh rendered-page verification rc=0. Each added sentence appears once, obsolete wording is absent, and unique-sort advice remains. Tracked worktree clean.
- Runner: `/workspace/tools/phone-agent-2-5821-docs-gate.py`. Logs and durable checklist: `/workspace/phone-agent-2-logs/solr-5821-review/pass-3/state.json`. Rendered evidence: `/workspace/phone-agent-2-logs/solr-5821-review/pass-3/rendered-common-query-parameters.html`.
- No repair, product commit, Java tests, full checkSiteLinks, or runtime bug-fix claim.

Pass 3 completed result: GREEN.

## Final disposition

DONE for this review unit at `327547d693e098447ff70c536b794aa364508670`: all three ordered regates GREEN, no product repair or commit. This is not a bug-resolution or PR-readiness verdict; the major docs-versus-reopened-bug finding remains. Historical NEARLY text in the original pass 1 section is superseded by the guide-build continuation above. No prohibited public action was taken.
