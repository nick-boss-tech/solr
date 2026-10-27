# SOLR-17849 header field follows the Figma design (round 3)

Claim: `claims/christos-reply-17849-header-field-design-round-3-vm6.md`. Design capture: `material/figma/render/137-12066.png` (node `137-12066`, Index & Query - /select, already captured in `reports/figma-node-index-round-1.md`).

## Result

- Branch `solr-17849-header-submit` on `nick-boss-tech/solr`, head `cf35ab0329c41cdef11429906d1e669d38b9f3a9`, fast-forward from `14ff707865`. Remote SHA verified.
- Gate at the exact head, job `header-17849-cf35ab0329-224344`: spotless rc=0; `IndexQueryHeaderTest` 6 of 6 and `IndexQueryStateHolderTest` 3 of 3 pass, 0 failures, 0 errors. Logs: `/workspace/gates/logs/vm6/pool-ui-header-17849/header-17849-cf35ab0329-224344/`.
- Mutation check (rung 2): the new test fails on the previous head `14ff707865`, 1 of 6 failed, `AssertionError` at `IndexQueryHeaderTest.kt:142`. Run `header-17849-mutation-retry-224215`. The first attempt, `header-17849-mutation-224043`, failed all six tests with `UnsatisfiedLinkError` because the libEGL stub had been removed from `/tmp`; it was rebuilt before the retry.
- PR draft `drafts/pr-solr-17849-header.md` updated to the new head and counts.

## Change

- The closed Request Handler field shows the selected endpoint and, on the same line, its class in monospace, at the right, before the chevron. The design draws the same arrangement.
- The handler field is 384 px wide and the collection field 256 px, the widths in the design at 1920 px.
- Menu entries are unchanged.

## Options (rung 4)

1. Class in the closed field's trailing slot (chosen; matches the design's position).
2. A two-line closed field (rejected: the design draws one line).
3. Name only in the closed field, class in the menu only (rejected: the design draws the class in the closed field).

## Not verified

- The screen was not rendered or screenshotted. The check is the Figma capture against the code and the JVM tests.
- The design does not draw the loading, empty or disabled states, so those rest on the ticket's wording.
