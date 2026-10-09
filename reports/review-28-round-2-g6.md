# Round 2 review, group 6: SOLR-13696, SOLR-13943, SOLR-14262, SOLR-14718

Read only. Final drafts from `origin/pr-prepare` (`pr-drafts/update-processing/`), receipts from `origin/pr-prepare` (`receipts/`), live state from `gh pr view` at review time. Nothing was edited, committed, pushed, or posted. No builds or tests were run.

Checks common to all four:

- Em dashes: 0 in each draft.
- Internal vocabulary (gate, receipt, ledger, JUnit XML, rc=, takeover, live tip, pre-fix proof as a label, tidy, Error Prone, seed): none in any of the four drafts.
- First-person plural and singular: none in authored text.
- Bold one-line summary present under every Limits section.
- Head of each live PR equals the gated head in its receipt.
- Live body equals the final draft byte for byte (`cmp`). Sizes: 7,541, 5,514, 3,221, and 5,814 bytes, trailing newline included.

## PR 5088, SOLR-13696

**Verdict: body ready. One remaining item: the title (owner call under O1).**

Part 1

- Title (round 1: change to the changelog title): not applied. The live title is the Jira summary, "DimensionalRoutedAliasUpdateProcessorTest / RoutedAliasUpdateProcessorTest failures due commitWithin/openSearcher delays". It names only the commitWithin cause. The draft also carries the create-alias and time-route production fixes, so the title is incomplete under the O1 accuracy rule. Proposed replacement (owner ratification): `SOLR-13696: Category and Dimensional routed-alias test suites run again, with a create-alias fix and a time-route fix`
- Proof, tidy and Error Prone (round 1): applied. Replaced by "The module checks pass.", which matches "module check rc=0" in the receipt.
- Proof, pre-fix items (round 1, plus ruling 6.3): applied as ruled. Seeds removed. The three pre-fix heads (`98ad9d3fcc33`, `a4e0da422327`, `08f9384e47c0`) stay. They are not in the receipt; the ruling accepts them from the round 3 and round 8 records.
- Create-alias fail-before (round 1): applied as specified. Moved from Limits into Proof; seed and quoted message removed.
- Time-route choice (round 1): removed. Create-alias choice heading (round 1): applied in the form the corrections report records ("The create-alias fix rides along with the test repair..."). The alternative names a cost, so the choice stays.
- Optional, not a round 1 item: the Proof gives the head but no verification date (formula section 3). The receipt date is 2026-10-09.

Part 2

- Pre-fix heads: landed as ruled.

Body and head

- Live body: byte-identical to the final draft.
- Head `da4fa6df11784ce5a83bc7e74ec7b6aa78f689b9`: matches the receipt.

## PR 5089, SOLR-13943

**Verdict: FIX FIRST. One added Proof sentence goes beyond the correction and the record. One owner call on Limits follow-up.**

Part 1

- Stack up front (round 1): applied. First bullet of What this change does. "Touches tests only" is replaced by "The commits added on top of SOLR-13696 change two test files only, and no production code." Checked: the diff from `1d0b8a0a73cd` to `cc155cf68e1d` touches two test files.
- "3 of 5 runs" (round 1): removed. Applied.
- Tidy and Error Prone (round 1): removed. Replaced by "The module checks pass." Applied.
- Title (round 1): applied differently. The live title is now "SOLR-13943: Move testDateMathInStart into its own test class with a polling wait". It is accurate and no longer names the old location. Keep.
- Proof sentence not in the correction: "This failure is pre-existing, and this change does not cause it." The receipt records the pre-existing timing race, but no base-code run of this class is on record, so "does not cause it" is unsupported. "The change does not touch that test" holds: the diff has no hunk at `testPreemptiveCreation`.
  - Proposed replacement (in the paragraph beginning "With the `awaitsfix` group enabled"): delete "This failure is pre-existing, and this change does not cause it. " and delete " This change does not fix that test." The result reads "...fails. The change does not touch that test. The failure is the pre-existing timing race that the method's comment describes (assertion)."
- Owner call (O2): the Limits name gaps (six tests still skipped under SOLR-13059; no base run) with no planned follow-up. SOLR-13943 is not among the 22 ratified drafts. Proposed sentence for ratification, appended to the first Limits bullet: "A follow-up submission for those six tests is planned."
- Optional: "These counts are from runs at this head." has no date. The receipt date is 2026-10-09.

Part 2

- "All in normal mode": removed. Normal mode is claimed for `TimeRoutedAliasDateMathInStartTest` 1 of 1 only, which the receipt supports.

Body and head

- Live body: byte-identical to the final draft.
- Head `cc155cf68e1d8e79f1bcecd2e4c25ada864d8f53`: matches the receipt.

## PR 5090, SOLR-14262

**Verdict: READY TO FLIP on the body. Title is an optional owner call under O1.**

Part 1

- Title (round 1): not applied under the O1 ruling. "Ignored during replay" is narrower than the change, which covers any non-ACTIVE state. The test covers BUFFERING. Optional replacement (changelog title, accurate): `SOLR-14262: A commit that is skipped because the update log is not ACTIVE now reports commitIgnored in the response header instead of looking successful.`
- Choice question blank line (round 1): applied.
- Cloud bullet (round 1): applied under O2. Now "A follow-up submission for the cloud case is planned." This supersedes the round 1 wording.
- Autocommit overlap (not traced in round 1): now verified. At SOLR-5941 head `a4df7bfd214b`, CommitTracker sends autocommits through the default chain's `processCommit` with a new response object, so the header goes nowhere. The claim holds.
- Proof: matches the receipt (1 of 1, failure at the `commitIgnored` assertion, 2026-10-07, module check rc=0).

Part 2

- Internal vocabulary: clean.
- Follow-up items: the cloud follow-up is planned. The autocommit gap stays a stated limitation, as ratified.

Body and head

- Live body: byte-identical to the final draft.
- Head `1e8d2b0075d7fded72ac19a049db27123a46ec10`: matches the receipt.

## PR 5091, SOLR-14718

**Verdict: body ready. One remaining item: the title "Multiple flaws" describes the ticket, not the change (owner call under O1).**

Part 1

- Title (round 1): not applied under O1. The live title says "Multiple flaws in tracking which UpdateCommand is associated...", but the change fixes one flaw and the draft says the second is a planned follow-up. Proposed replacement: `SOLR-14718: Failed distributed adds keep their own copy of the add command, so the error names the document`
- Citation (round 1, SolrCmdDistributor L241-L264): applied. Now at base `9b3a84b1c460`. The `Req.toString` link is also at base, L417-L421, which matches the base lines.
- SOLR-5939 links (round 1): applied. The sentence about the second and third bullets is in the first Limits bullet.
- Ruling R3: passes. Neither forbidden phrase appears.
- Proof wording (round 1): applied. "Method `test()`, which now calls a new check", L547-L575.
- "Premise step" (round 1 cosmetic): the sentence is removed. The fact survives in "With the copy removed... exactly 1 test fails." Applied.

Part 2

- Internal vocabulary: clean.
- Follow-up: second flaw planned. "Two paths still name one request" stays a stated limitation, as ratified.

Body and head

- Live body: byte-identical to the final draft.
- Head `29c09959791aea2ee46f7b697e1590d19a48ef1f`: matches the receipt.
