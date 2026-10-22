# Review: SOLR-13182, branch solr-13182-child-null-parent (unit 28)

Head reviewed: 2cf82a0b946432393d6dc3cc8216e67eb443007d. Base: c8ab71430f. Ticket: SOLR-13182, "NullPointerException due to an invariant violation in org/apache/lucene ...".

## Pass 1

Review (2026-10-22, vm4):

- Ticket fit. A `{!child}` query with no clauses and no `of` parent filter reaches `noClausesQueryLegacy` and passes a null query on. The branch adds a guard that throws a `SyntaxError` (400) with a clear message in that case. That fits the ticket's NPE.
- Scope. Four lines in `BlockJoinChildQParser.java`, one test, one changelog file. Nothing else.
- Finding, minor: the regression test was changed in the head commit (`2cf82a0b94`) from `{!child q=""}` to `{!child of=""}`. The test name says "no 'of' parent filter", but it sets `of` to an empty string. An empty `of` and an absent `of` both parse to null, so the guard handles both. The test pins the empty-value case; the absent-parameter case is not pinned separately. Recorded, not repaired (a new test is outside the repair scope).
- Changelog: present, YAML parses (step 0), type `fixed`.
- Proof leg: on base the test fails with `NullPointerException: Query must not be null`, the ticket's failure. The proof is real.
- Repairs: none.

Regate: `gates/SOLR-13182-review-pass-1.md`, GATE GREEN at 2cf82a0b94 on vm4.

## Pass 2

Review (2026-10-22, vm4), same head 2cf82a0b94.

- Minor, unchanged from pass 1: the test pins `of=""`. The absent-`of` case is not run. In code, `subQuery` takes a null string through `getParser` too, but no test in the branch covers it. Not repaired.
- The guard message is clear and returns 400 (`SyntaxError` mapped to BAD_REQUEST by the test's `assertQEx`). Consistent with the other `{!child}` errors in the file.
- Repairs: none.

Regate: `gates/SOLR-13182-review-pass-2.md`, GATE GREEN at 2cf82a0b94 on vm4.

## Pass 3

Review (2026-10-22, vm4), same head 2cf82a0b94. No new findings. The pass 1 and pass 2 minor finding stands: the test pins `of=""`, and the absent-`of` case is not covered.

- Repairs: none.

Regate: `gates/SOLR-13182-review-pass-3.md`, GATE GREEN at 2cf82a0b94 on vm4.

Final head: 2cf82a0b946432393d6dc3cc8216e67eb443007d. Unit 28 DONE with the three pass sections.

