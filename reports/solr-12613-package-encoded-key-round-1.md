# Draft package: encoded node-target query keys

Status: DRAFT DELIVERED; publication and ticket identity pending. Prepared 2026-11-05 by Windows Codex desktop, not yet reviewed by Nick. No public write.

Ticket disposition: proposed NEW linked bug, `<JIRA-KEY>`; do not reopen resolved SOLR-18537. Jira summary search and GitHub search found no matching standalone submission; refresh before filing. SOLR-18537 is provenance, not the proposed issue key. No verified released-version range; scope is the tested main snapshot.

Source: `nick-boss-tech/solr:solr-18537-encoded-key-followup`, head `a676e7a392244e7cf4a35719fc64383b65cd947f`, based on main `07ccc73cd24e26e59da169ca35b3fd5f64c35986`. [Scoped diff](https://github.com/nick-boss-tech/solr/compare/07ccc73cd24e26e59da169ca35b3fd5f64c35986...a676e7a392244e7cf4a35719fc64383b65cd947f).

Outbound files: `solr/core/src/java/org/apache/solr/servlet/NodeTargetProxyFilter.java`, `solr/core/src/test/org/apache/solr/servlet/NodeTargetProxyFilterTest.java`, `solr/core/src/test/org/apache/solr/servlet/NodeTargetProxyFilterAuthTest.java`. No changelog exists yet. Arrange a ticket-scoped changelog after ticket approval, then obtain proof for the resulting head. No source changed during drafting. Independent of PR 5110. If main advances or the two transport patches are combined, reconcile the test-file overlap and verify the new head.

## PR title

`<JIRA-KEY>: Recognize percent-encoded node-target query parameter names`

## PR body

🤖 *AI text below* 🤖 *(prepared for review by Nick Shanin)*

https://issues.apache.org/jira/browse/<JIRA-KEY>

## Description

**An encoded targeting key can leave a GET request on the receiving node.** `%58-Solr-targetNode` decodes to `X-Solr-targetNode`, but the filter's raw-string check can skip parsing it. A simultaneous header also misses the intended conflict rejection.

## Solution

**Parse potentially encoded keys before selecting a target.** The [filter change](https://github.com/nick-boss-tech/solr/blob/a676e7a392244e7cf4a35719fc64383b65cd947f/solr/core/src/java/org/apache/solr/servlet/NodeTargetProxyFilter.java#L76-L100) retains GET-only query targeting, removes the directive before forwarding, and returns query-parse errors with their original status. It adds no endpoint parameter or response shape.

## Tests

<details>
<summary>Linux verification at a676e7a392244e7cf4a35719fc64383b65cd947f</summary>

- Seed `5110EAC0DE`, JDK 21, Gradle 9.7.0: filter 19/19, authenticated filter 6/6, proxy 2/2 and parser 9/9; no failures, errors or skips.
- Four named tests fail on the original production code: encoded-key routing, header conflict, directive consumption on a replica hop, and authenticated routing. The GET loop stops at its first failing baseline assertion; other variants have positive coverage.
- Tidy and root `check -x test` passed with a clean tree. The check used a normal clone at this SHA and the host CA truststore for download access.
- [Verification record](https://github.com/nick-boss-tech/solr/blob/357ab091ff6e93a0d0ffa1b9129f482828b51844/receipts/SOLR-18537-encoded-key-main-round-2.md).

</details>

**The authenticated coverage uses BasicAuth.** It does not establish behavior for every authentication plugin. A ticket-scoped changelog and verification of the final submission head remain pending.

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

## Jira title

Recognize percent-encoded X-Solr-targetNode query parameter names

## Jira body (new-ticket draft; Jira wiki markup)

The node-target proxy can miss a GET targeting directive when the parameter name is percent-encoded. For example, {{%58-Solr-targetNode}} decodes to {{X-Solr-targetNode}} but does not contain that literal raw string. With two live SolrCloud nodes, send {{GET /solr/admin/info/system?%58-Solr-targetNode=<other-live-node>}} to the first node. The original tested code answers from the receiving node rather than the requested target. Supplying both that encoded key and the target header can also avoid the intended HTTP 400 conflict check.

Expected: recognize equivalent encoded parameter names, reject header/query conflicts, consume the directive before a subsequent replica hop, and preserve malformed-query HTTP status. Query-based targeting remains GET-only.

Acceptance criteria:
 * Encoded keys work on v1 and v2, including lowercase and uppercase hex escapes.
 * Authenticated routing, conflict rejection and directive consumption are covered.
 * An encoded unrelated value does not replace a header target; POST query parameters do not enable routing.
 * Malformed UTF-8 returns HTTP 400.

Observed on main snapshot {{07ccc73cd24e26e59da169ca35b3fd5f64c35986}}; no released-version range is established. A candidate at {{a676e7a392244e7cf4a35719fc64383b65cd947f}} passed 36 focused/neighboring tests, tidy and root checks. Four named regressions fail against original production; other variants have positive coverage. Changelog and final submission checks remain pending. Related: [SOLR-18537|https://issues.apache.org/jira/browse/SOLR-18537].

🤖 _AI text above_ 🤖 _(prepared for review by Nick Shanin)_
