# Draft package: SOLR-18619 request targeting and logging

Status: DRAFT DELIVERED; current-contributor base and exact-head proof pending. Prepared 2026-11-05, not yet reviewed by Nick. No public write.

Ticket disposition: EXISTING SOLR-18619; title/body below are proposed wording for review, not authorization to overwrite the live issue. Its current description requires metrics headers, per-node system-info reads, and cloud log reads with a direct standalone path. Proposed addition: selected-node default plus explicit All nodes mode, pending the maintainer choice below. Preserve the existing transport-migration scope. Do not fold in resolved SOLR-15823 / PR 5031 logging-level work, status-preservation SOLR-18549, parameter deprecation, or a backend API expansion.

Branch `solr-18619-ui-request-targeting-followup`, head `be369c41c633d639ad66a7291a4d82891ea9836c`, slice base `4e8aa913fce4a40b529fe89d8b5dd7930c29a86e`. [Scoped diff](https://github.com/nick-boss-tech/solr/compare/4e8aa913fce4a40b529fe89d8b5dd7930c29a86e...be369c41c633d639ad66a7291a4d82891ea9836c). PR 5110 is now OPEN at `6b5d830132b941faa85856347099ade8b190be0e`; reconcile the UI delta there or after merge. Do not submit this synthetic-base branch wholesale against main. No source change here.

Outbound files relative to the synthetic base:

- `changelog/unreleased/SOLR-18619-target-node.yml`
- `solr/solr-ref-guide/modules/deployment-guide/pages/configuring-logging.adoc`
- `solr/webapp/src/test/org/apache/solr/webapp/AdminUiLoggingScreenTest.java`
- `solr/webapp/src/test/org/apache/solr/webapp/AdminUiLoggingStandaloneTest.java`
- `solr/webapp/src/test/org/apache/solr/webapp/AdminUiNodeScreensTest.java`
- `solr/webapp/src/test/org/apache/solr/webapp/AdminUiTestBase.java`
- `solr/webapp/web/index.html`
- `solr/webapp/web/js/angular/controllers/cloud.js`
- `solr/webapp/web/js/angular/controllers/logging.js`
- `solr/webapp/web/js/angular/services.js`
- `solr/webapp/web/partials/cloud.html`
- `solr/webapp/web/partials/logging.html`

## PR title

`SOLR-18619: Use request-scoped node targeting for Admin UI reads`

## PR body

🤖 *AI text below* 🤖 *(prepared for review by Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-18619

## Description

**Metrics, Nodes and logging reads move to the generic node-target transport.** Metrics uses `X-Solr-targetNode` instead of `node`; the Nodes view requests system info separately for each live node. A request helper keeps each target in that request's headers, and asynchronous results are guarded against obsolete selections. [Scoped implementation](https://github.com/nick-boss-tech/solr/compare/4e8aa913fce4a40b529fe89d8b5dd7930c29a86e...be369c41c633d639ad66a7291a4d82891ea9836c).

## Solution

**Log messages follow the selected node, with an explicit All nodes option.** Cloud aggregation retains origin labels, reports partial failures and clears stale results when every request fails. Standalone keeps a direct local read without node discovery.

**Choice to confirm: keep the selected node as the default for log viewing?** First, a selected Node screen should identify the source of its data. Second, cluster troubleshooting still needs combined logs, which All nodes supplies with per-node failure visibility. Always aggregating would preserve a cluster-wide view but make the picker ineffective on this screen. I recommend the implemented selected-node default with explicit aggregation. Does that fit the intended navigation?

## Tests

<details>
<summary>Verification pending at be369c41c633d639ad66a7291a4d82891ea9836c</summary>

Seven browser methods were added for node reads, cloud logs and standalone behavior. No completed exact-head execution result is recorded for this branch. The baseline's partial 7/7 contract/picker result does not prove these changes; its neighboring run still has one unresolved timeout. Linux formatting, core checks, browser tests, documentation checks and root check remain pending.

</details>

**This proposal depends on the revised picker work in [PR 5110](https://github.com/apache/solr/pull/5110).** The current branch uses an earlier synthetic base and needs reconciliation and fresh proof before submission. Backend v2 APIs, log-level broadcast semantics and removal of legacy parameters are outside this change.

Changelog: [SOLR-18619-target-node.yml](https://github.com/nick-boss-tech/solr/blob/be369c41c633d639ad66a7291a4d82891ea9836c/changelog/unreleased/SOLR-18619-target-node.yml).

## Checklist

- [ ] I have reviewed the contribution guidelines and this final submission.
- [ ] The Jira identity and PR title are confirmed for submission.
- [ ] Maintainer access to the fork branch is confirmed.
- [ ] The final submission base is main, or its dependency is explicit.
- [ ] The final head has completed the required checks described above.
- [x] Tests for this change are included in the prepared slice.
- [ ] Reference Guide documentation is included where required.
- [ ] Changelog treatment is confirmed for the final submission.

*AI-generated description. Nick Shanin directed the work and takes responsibility for this contribution.*

## Jira title (proposed existing-ticket wording)

Shift the Admin UI to the X-Solr-targetNode mechanism for per-node admin calls

## Jira body (proposed existing-ticket wording; Jira wiki markup)

Follow-up to [SOLR-18537|https://issues.apache.org/jira/browse/SOLR-18537]. Move the legacy Admin UI's per-node reads to {{X-Solr-targetNode}} without adding backend API parameters or changing native response shapes.

Acceptance criteria:
 * Metrics uses a per-request target header instead of {{node}}.
 * Nodes requests system info once per live node rather than using {{nodes}} fan-out.
 * Cloud log reads support selected-node viewing and explicit All nodes aggregation; combined results identify their node and show partial failures. Clear stale content if all reads fail.
 * Obsolete responses do not replace the current selection or update a destroyed view.
 * Standalone uses the direct local read, with no cloud discovery or target header.
 * Document the logging behavior and preserve existing log-level change semantics.

Proposed addition to the current ticket: make log messages follow the picker by default, with All nodes available for cluster troubleshooting. Always aggregating is the alternative; this UI choice remains for maintainer confirmation in the PR. The existing per-node transport migration remains the ticket's core scope.

Candidate {{be369c41c633d639ad66a7291a4d82891ea9836c}} is prepared on a synthetic frontend-only picker baseline. Reconcile it with [PR 5110|https://github.com/apache/solr/pull/5110] before submission. Seven added browser methods have no completed exact-head execution result yet. No released-version claim is made. Deprecating legacy parameters, backend API additions and SOLR-15823 logging-level work remain separate.

🤖 _AI text above_ 🤖 _(prepared for review by Nick Shanin)_
