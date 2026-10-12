# Schema re-check, SOLR-14199 landing after the prescribed fix, round 1

Verdict: **the landed draft's heads, anchors, changelog and counts hold, and three unsupported sentences are corrected in the draft.** The corrections are checked against the receipt, and the TEST_HACK bullet is checked against the prior head's code. The draft's two owed Limits lines are present and unchanged in substance.

Scope: `f9057e454cc` (the prescribed fix, the receipt refresh and the draft update), checked against `receipts/SOLR-14199.md` (top section, re-gate 2026-10-12) and the new head `2c549d9fb384363f741ce62206c37f518744153f`. The claim is `claims/pool-recheck-14199-landing.md`. Two read-only subagents for the first pass and one for the fresh check.

## First pass

- **Heads.** `ls-remote origin refs/heads/solr-14199-submit` returns `2c549d9fb384363f741ce62206c37f518744153f`. No old head `e743c90da79` remains in the draft, and every link uses the new head.
- **Anchors.** The head object was fetched locally, and the changelog file exists there. The only change since the prior head is `SchemaVersionSpecificBehaviorTest.java`, which matches the receipt's test-only fix. The `PointField.checkSchemaField` range (L96 to L110) covers the rejection of `omitNorms="false"` on indexed fields.
- **Counts and dates.** BadIndexSchemaTest 26 of 26, PrimitiveFieldTypeTest 2 of 2, SchemaVersionSpecificBehaviorTest 1 of 1 and TestSolrQueryParser 37 of 37 all match the receipt's top section. The date 2026-10-12 matches the re-gate heading.
- **Placeholders.** No CONFIRM, HOLD, OWNER or TO FILL markers remain, and no internal block is left in the draft.
- **Dashes and process words.** None in the outbound text.
- **Limits, bullet (a).** "The new point check skips non-indexed fields ..." matches the code: the check runs only when `field.indexed()`, and base rejects the explicit form through `SchemaField.java` lines 376 to 382.
- **Limits, bullet (b), the TEST_HACK sentence.** The flag exists, and it guards only the `precisionStep` removal. The `omitNorms` rejection sits outside that guard, so a hand-written schema with `omitNorms="false"` on a point type still fails under the flag.

## Corrections made in the draft

- **Line 26.** "because base loads all three fixtures without error" became "and it is the only one of the 26 that does". The receipt records that exactly 1 of 26 fails on base.
- **Line 32.** "The numeric point type is randomized in tests, so the run covers one of the point or trie configurations." was removed. Neither the receipt nor the fresh check supports the claim about configurations, and the fresh check's first attempt at a reworded sentence failed the same test.
- **Line 48.** The sentence "No declaration in the tree combines a randomized point type with `omitNorms`, and the randomized suites pass" was removed. The tree search was not recorded in the receipt. The bullet now reads: "The checks do not exempt `TEST_HACK_IGNORE_USELESS_TRIEFIELD_ARGS`. A hand-written test schema that sets `omitNorms="false"` on a point type would now fail to load even under the flag. A follow-up for that case is possible on request."
- **Spacing.** A blank line was restored before the "A choice to check" heading, which the line-32 removal had dropped.

## Fresh check

- The three edits changed only lines 26, 32 and 48, and the fresh check confirmed lines 26 and 48 against the receipt and the prior head's code.
- The fresh check failed line 32 as worded. Line 32 was then removed, and that removal was checked by reading the diff, not by a further subagent.

## Notes for the main side

- Line 44 of the draft repeats the summary in the Limits body. This is the owed Limits line (a), which the schema round 2 record requires verbatim, so it was not changed.
- The existence-query and stored-only point claims in What this change does come from the earlier schema rounds, which converged on them. They were not re-opened here.

## Convergence line

The landed SOLR-14199 draft holds on heads, anchors, counts, dates and placeholders. The three unsupported sentences are removed, and the draft's Limits now rest on the receipt and the code the draft cites. The corrections are in the working tree and are committed with this report.
