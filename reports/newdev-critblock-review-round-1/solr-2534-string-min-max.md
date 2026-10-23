# Review: SOLR-2534, branch solr-2534-string-min-max (unit 43)

Head reviewed: a75b012a20630acc706eb0427a1d31638b2e0c22. Base: c8ab71430f. Ticket: SOLR-2534, "Add min() and max() on strings".

## Pass 1

- Ticket asks for min() and max() to handle strings such as "a" and "zz". The change adds lexicographic min and max over single-valued string fields (`StringMinMaxValueSource`, wired in `ValueSourceParser.minMaxValueSource`). Numeric min and max are unchanged.
- Scope: the diff touches only `ValueSourceParser.java`, the new `StringMinMaxValueSource.java`, `TestStringMinMaxFunction.java` and the changelog entry. No other files.
- Test coverage: five tests cover lexicographic max and min, missing values, a null value omitted from the response, and a multi-valued field rejected. The proof leg (base `ValueSourceParser.java`) is recorded in the step list.
- Minor, not repaired: `testMultiValuedStringFieldIsRejected` uses `expectThrows(Exception.class, ...)`, which would pass on any exception and does not check the multi-valued message. No test covers mixing string and numeric arguments, which throws a SyntaxError in the new code.
- Repairs: none.

Regate: `gates/SOLR-2534-review-pass-1.md`, GATE GREEN at a75b012a20 on vm4.

## Pass 2

Review (2026-10-23, vm4), same head a75b012a20. - Re-read the diff at the same head a75b012a20 against the ticket. No new findings in pass 2.
- Pass 1 minor test-strength items stand, not repaired: `testMultiValuedStringFieldIsRejected` uses `expectThrows(Exception.class, ...)`, and no test covers mixing string and numeric arguments.
- Repairs: none.

Regate: `gates/SOLR-2534-review-pass-2.md`, GATE GREEN at a75b012a20 on vm4.
