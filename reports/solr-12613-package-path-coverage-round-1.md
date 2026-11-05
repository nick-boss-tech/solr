# Draft package: encoded path transport coverage

Status: DRAFT DELIVERED; publication and ticket identity pending. Prepared 2026-11-05, not yet reviewed by Nick. No public write.

Ticket disposition: proposed NEW linked test task, `<JIRA-KEY>`, rather than reopening resolved SOLR-18537 or claiming a new production defect. Searches found no matching standalone task/PR; repeat before filing. No release fixVersion is inferred from test coverage.

Source: `nick-boss-tech/solr:solr-18537-path-coverage-followup`, head `0fad9847cc89973c751b1732aa22a259764610b2`, based on main `07ccc73cd24e26e59da169ca35b3fd5f64c35986`. [Scoped diff](https://github.com/nick-boss-tech/solr/compare/07ccc73cd24e26e59da169ca35b3fd5f64c35986...0fad9847cc89973c751b1732aa22a259764610b2).

Outbound file: `solr/core/src/test/org/apache/solr/servlet/NodeTargetProxyFilterTest.java` only. No production or changelog change; confirm the repository's test-only changelog treatment before submission. Independent of the picker. Can be a separate small test PR; shares this test class with the encoded-key slice, so a new merged/rebased head needs fresh proof. No branch changed here.

## PR title

`<JIRA-KEY>: Cover encoded paths across node-target replica forwarding`

## PR body

🤖 *AI text below* 🤖 *(prepared for review by Nick Shanin)*

https://issues.apache.org/jira/browse/<JIRA-KEY>

## Description

**The tests now observe the raw path after an actual collection-replica hop.** Header and query targeting each send `/a%20b%23c`, require `_forwardedCount=1`, and compare the URI returned by the receiving handler. [Test changes](https://github.com/nick-boss-tech/solr/blob/0fad9847cc89973c751b1732aa22a259764610b2/solr/core/src/test/org/apache/solr/servlet/NodeTargetProxyFilterTest.java).

## Solution

**Ambiguous encoded-percent paths keep their existing rejection.** Separate cases require HTTP 400 for `/a%25b` and `/a%2523b` in both targeting modes. Query markers, foreign headers and consumed targeting directives are checked alongside the successful forwarding cases. Production code is unchanged.

## Tests

<details>
<summary>Linux verification at 0fad9847cc89973c751b1732aa22a259764610b2</summary>

- Seed `5110A7C0DE`, JDK 21, Gradle 9.7.0: original filter suite 14/14; candidate filter/auth suites 17/17 and 4/4; proxy/parser neighbors 2/2 and 9/9. No failures, errors or skips.
- Original and candidate suites are expected to pass. A temporary production mutation changed `%23` to `%24` during the replica hop. Both forwarding tests reached the handler and failed at the exact URI comparison. The mutation was reverted.
- Tidy and root `check -x test` passed, trees clean. The check used a normal clone at this SHA and the host CA truststore for download access.
- [Verification record](https://github.com/nick-boss-tech/solr/blob/357ab091ff6e93a0d0ffa1b9129f482828b51844/receipts/SOLR-18537-path-coverage-main-round-2.md).

</details>

**Only the two URI comparisons have discriminating mutation evidence.** Rejection, header and query assertions are positive coverage. This PR does not relax Jetty URI compliance or establish a production bug.

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

Cover encoded paths and ambiguous-path rejection in node-target replica forwarding

## Jira body (new test-task draft; Jira wiki markup)

The generic node-target transport needs coverage that observes the encoded request path after a collection request is forwarded to its replica. A successful response from the first target alone does not establish that a second hop preserves the URI.

Add tests using the existing two-node cluster and echo handler:
 * Send {{/a%20b%23c}} with header targeting and query-parameter targeting.
 * Require {{_forwardedCount=1}} before checking the exact raw URI at the handler.
 * Preserve the query marker and foreign header, and consume the targeting directive.
 * Require HTTP 400 for {{/a%25b}} and {{/a%2523b}} in both modes under the existing Jetty URI rules.

This is coverage-only work. Original production with the added tests should pass. A compiling temporary corruption of the forwarded encoded hash must reach the handler and fail the URI assertion in each mode; rejection and other assertions are positive coverage unless separately demonstrated.

Candidate {{0fad9847cc89973c751b1732aa22a259764610b2}} passed 21 focused and 11 neighboring tests, tidy and root checks. Both URI comparisons caught the temporary corruption. Tested against main {{07ccc73cd24e26e59da169ca35b3fd5f64c35986}}; no released-version defect or compatibility relaxation is proposed. Related: [SOLR-18537|https://issues.apache.org/jira/browse/SOLR-18537].

🤖 _AI text above_ 🤖 _(prepared for review by Nick Shanin)_
