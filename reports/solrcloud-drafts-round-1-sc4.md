# SolrCloud drafts round 1, part SC4 (windows draft agent, 2026-10-11)

Slice: SOLR-17733, SOLR-13239, SOLR-18277, SOLR-16013, SOLR-11479.

Files changed: `pr-drafts/solrcloud/SOLR-17733.md` (corrected in place); this report (new). No other files, no commits, no pushes, no builds, no tests, no gate runs. The only remote call beyond `ls-remote` was one read-only `gh pr list` for SOLR-18277.

## Live fork tips against the receipts

| Ticket | Receipt head | Live fork tip (`ls-remote origin`) | Result |
|---|---|---|---|
| SOLR-17733 | 636196b7954 (gated) | 636196b7954 | match |
| SOLR-13239 | 699a1fce368 (gated) | 699a1fce368 | match |
| SOLR-18277 | 17c0e644812 (gated) | 17c0e644812 | match; upstream PR #4959 MERGED 2026-10-05 at the same head |
| SOLR-16013 | e1bd21fd11a (gated) | ba26b702891 | DIFFERS: gate is at an older head (answers agree) |
| SOLR-11479 | none (no gate) | 7e538e844c4 | match with the recorded code commit |

## Per ticket

**SOLR-17733: corrected.** Draft is at the gated head 636196b7954, which is the receipt head and the live tip.
- Fix: the "What happens today" citations pointed at the head. They describe pre-change behavior, so they now link the merge-base commit 56ec140e363 with the merge-base line range (DistribFileStore.java L513-L518; ClusterFileStore.java L283-L299, unchanged by the branch). One sentence added saying the links in that section point to the code before the change. This follows the formula's merge-base rule.
- Checked, no change needed: every head citation (L488-L511, L514-L530, L496-L511, L325-L327, test L201-L219) and the L162-L168 range were read at the head and match. Proof counts (TestDistribFileStore 1 of 1, base result expected 200 but was 500) and the "recorded 2026-10-04" date match the receipt and the answers. The Limits bold opener (answers draft correction 6) and the Choice opener and question are in place. The changelog title fits the change. No owner-notes header, no em dashes, no internal process vocabulary.
- Length: 4,205 bytes of raw markdown, about 2,775 bytes of visible text with link targets removed. Not trimmed; no answer asked for it.
- Judgment: the Limits links (remote delete loop, local `deleteFile`) stay at the head. The delete loop sits in the `delete()` method this branch edits, and both ranges were verified at the head.

**SOLR-13239: not drafted (submission-held).** Receipt: gated green as-is at the live tip (ZkStateReaderTest 17 tests, 1 skipped, 0 failures; TestCloudCollectionsListeners 2 of 2). The receipt records no separate Proof. Answers: record verified; the hold is the owner's standing decision and is unchanged. Owed only if the hold is lifted: `SOLR-13239-TESTING.md` is in the branch diff against upstream/main (confirmed), and it comes out first. Branch not touched.

**SOLR-18277: not drafted (merged, retire item).** Upstream PR #4959 is MERGED (2026-10-05), head 17c0e644812, the same as the receipt and the fork tip. Receipt Proof: INCONCLUSIVE, recorded as such. DISCUSS 6 (owner's call, not executed): delete `solr-18277-submit` and the two CI branches. All three still exist on the fork: `solr-18277-submit` (17c0e644812), `ci/solr-18277` (35c9e9952285), `ci/solr-18277-proof` (44c35a3deafd).

**SOLR-16013: not drafted (held, DISCUSS 4: history rewrite).** Receipt: gated at e1bd21fd11a (OverseerCloseOrderingTest 1 of 1; Proof PASS with the rewritten pinning test). The live tip ba26b7028917 is past the gate: it adds the interrupt-safe wait and a second test, and neither has a run. No Proof can be stated honestly at the live tip, so no draft. Owed: the owner's call on the rewrite that drops the Co-Authored-By trailer from 3059f9be884 (recommendation: approve, no squash); the changelog title replacement goes with that rewrite (answers branch correction 5); a re-gate at the rewritten head; landing last in the overseer cluster (adopted).

**SOLR-11479: not drafted (audit only, held for branch correction).** Receipt: NO GATE. The fork tip 7e538e844c4 is the code commit. Branch corrections owed before the first gate, checked on the tip:
1. `SOLR-11479-TESTING.md` is in the branch diff against upstream/main. Delete it.
2. The changelog fragment is `changelog/unreleased/SOLR-11479-addreplica-property-corenodename.yml`. Its title ("ADDREPLICA: accept property.coreNodeName as an alias of coreNodeName instead of failing core creation") describes the alias without the several-replica rejection. Replace it.
3. The 400 message in AddReplicaCmd.java names only `coreNodeName`, including for the property form. Wording fix.
4. No test for the several-replica rejection exists on the tip. CollectionsAPISolrJTest has only the alias test (testAddReplicaWithCoreNodeNameProperty). Add the rejection test before the first gate (adopted: part p2 finding 4 and decision 7).
Also owed: the first gate (main-side work), landing after SOLR-15035 (adopted).

## Gaps and holds

- Receipt gaps: none for SOLR-17733. The SOLR-13239 receipt has no Proof beyond the gate itself (the answers accept this). SOLR-11479 has no gate.
- Head discrepancy: SOLR-16013 only (gated e1bd21fd11a, live tip ba26b7028917). Receipt and answers agree.
- Holds: SOLR-13239 (owner's submission hold); SOLR-18277 (DISCUSS 6, branch deletion, owner's call); SOLR-16013 (DISCUSS 4, history rewrite, owner's call); SOLR-11479 (branch corrections and first gate owed; no DISCUSS item).
- Not verified: no gate or proof re-run (not allowed in this slice). The SOLR-16013 changelog title was not checked, since the answers give no replacement text and there is no draft.
