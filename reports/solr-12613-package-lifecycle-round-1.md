# Draft package: picker and core-menu lifecycle

Status: DRAFT DELIVERED; contributor reconciliation, final branch and proof pending. Prepared 2026-11-05, not yet reviewed by Nick. No public write.

Ticket disposition: prefer an author-review offer under SOLR-12613 if the remaining delta is wanted in PR 5110. If kept separate, the following is a NEW linked bug draft with `<JIRA-KEY>`. Do not file both or rewrite SOLR-12613's navigation scope. No matching separate ticket was found in the targeted search; refresh before filing.

Current fork branch `solr-12613-selection-lifecycle-followup`, head `7f8d251d6c27200f3804f8b4e1298b9646f6cab4`. Scoped base is synthetic frontend baseline `4e8aa913fce4a40b529fe89d8b5dd7930c29a86e`. [Slice diff](https://github.com/nick-boss-tech/solr/compare/4e8aa913fce4a40b529fe89d8b5dd7930c29a86e...7f8d251d6c27200f3804f8b4e1298b9646f6cab4).

Outbound files relative to that base: `solr/webapp/web/js/angular/app.js`, `solr/webapp/src/test/org/apache/solr/webapp/AdminUiNodePickerTest.java`. The entire branch must NOT be submitted against main. Actual PR 5110 is OPEN at `6b5d830132b941faa85856347099ade8b190be0e`, which removes backend additions and incorporates the callback-only controller fixes. Its new revision is not the tested synthetic baseline. Prepare only the still-missing lifecycle delta on the reconciled contributor/merged head, preserve contributor ancestry/credit, and obtain exact-head proof. No source was rearranged here. A separate behavior-fix PR needs a ticket-scoped changelog decision.

## PR title (if submitted separately)

`<JIRA-KEY>: Keep the core menu consistent with node selection and refreshes`

## PR body

🤖 *AI text below* 🤖 *(prepared for review by Nick Shanin)*

https://issues.apache.org/jira/browse/<JIRA-KEY>

## Description

**A delayed core-status response can replace newer node-selection results.** A same-node refresh can also finish out of order. Restored node selection must settle before the initial core request, and failed mode metadata must still allow the local core-menu fallback.

## Solution

**Core-menu updates now check both request generation and selected node.** The change defers the initial core request until mode handling completes, clears target state consistently, and retains a core refresh after metadata failure. Four added browser cases cover cross-node and same-node stale responses, session restoration, and the failure fallback. [Scoped changes](https://github.com/nick-boss-tech/solr/compare/4e8aa913fce4a40b529fe89d8b5dd7930c29a86e...7f8d251d6c27200f3804f8b4e1298b9646f6cab4).

**This is a dependent follow-up to [PR 5110](https://github.com/apache/solr/pull/5110).** The prepared implementation uses an earlier synthetic frontend-only base. Its remaining delta must be reconciled with the author's current revision before submission; that prerequisite has no runtime result yet.

## Tests

<details>
<summary>Verification pending for lifecycle candidate 7f8d251d6c27200f3804f8b4e1298b9646f6cab4</summary>

No completed exact-head browser/build result is recorded for this lifecycle branch. The planned picker suite has eight methods; that is an inventory, not a passing count.

On the different baseline head `4e8aa913fce4a40b529fe89d8b5dd7930c29a86e`, contract/picker tests passed 7/7 and three callback mutations failed at the intended assertions. Its neighboring selection was 16/17, with `testAddCoreViaUi` timing out. An isolated rerun passed but did not clear that suite failure. Linux formatting, core proxy/auth and root checks remain pending. [Partial baseline verification](https://github.com/nick-boss-tech/solr/blob/357ab091ff6e93a0d0ffa1b9129f482828b51844/receipts/SOLR-12613-browser-verification-round-1.md).

</details>

**The change is limited to the legacy Angular UI's core-menu lifecycle.** It does not implement new backend APIs or change core mutation scope.

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

## Jira title (if kept separate)

Keep the Admin UI core menu consistent with restored and changing node selection

## Jira body (new linked bug draft; Jira wiki markup)

Follow-up to [SOLR-12613|https://issues.apache.org/jira/browse/SOLR-12613] and [PR 5110|https://github.com/apache/solr/pull/5110]. The core menu must reflect the latest selected node and latest refresh. A delayed older response must not replace newer results. On reload, restored remote selection must be applied before the initial core request. A failed mode-metadata request must still allow the local core-menu fallback.

Acceptance criteria:
 * Delay a core response for node A, switch to B, then release A; the menu retains B's cores.
 * Complete two requests for the same node out of order; the later request wins.
 * Restore a persisted target and observe the first core request's target.
 * Fail mode metadata and verify a local core refresh still occurs.
 * Preserve standalone behavior and the picker/core navigation boundary.

Prepared slice {{7f8d251d6c27200f3804f8b4e1298b9646f6cab4}} changes the main controller and existing picker test. It is based on an earlier synthetic frontend baseline and needs reconciliation with the revised contributor PR before submission. No released-version range or completed runtime verification is claimed. Partial baseline browser results do not prove this follow-up. If the author incorporates the slice in SOLR-12613, this separate issue is unnecessary.

🤖 _AI text above_ 🤖 _(prepared for review by Nick Shanin)_
