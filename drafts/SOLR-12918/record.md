# Skip record: SOLR-12918

- Ticket: SOLR-12918, Reproducing test failure in TestHighFrequencyDictionaryFactory.
- Verdict: keep-open.
- Draft branch: solr-12918-draft-open (verdict unchanged; this record replaces the earlier record on this branch).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm5 (pool Linux host), 2026-10-20 UTC. Redo of remediation batch 07, item 22 (`assignments/pool-aaa-skip-verdict-remediation-07.md`).

## Ticket reading

JIRA read 2026-10-20: Open, no resolution, created 2018-10-24, updated 2019-06-08, no fix versions, no comments. The report: `ant test -Dtestcase=TestHighFrequencyDictionaryFactory -Dtests.method=testDefault -Dtests.seed=9F7E1447BD84930F -Dtests.slow=true -Dtests.timezone=Europe/Oslo` fails, found while backporting to 5x and reproduced on master. The ticket's error text is cut off after the first few characters (`ERROR 0.00s | Tes...`), so the failing assertion is not in the ticket.

## Code reading

Read at the pin:

- `solr/core/src/test/org/apache/solr/spelling/suggest/TestHighFrequencyDictionaryFactory.java:49-70` (`testDefault`): the test asserts that for query `chagn` with `suggest.count=3` the suggester returns `numFound` 2, with `chance` first and `change` second. The assertion order is the one that a tie in term frequency would change.
- The test has no `@AwaitsFix` or `@BadApple` annotation at the pin, and no commit in the file's history names the ticket.
- The suggester implementation is `solr/core/src/java/org/apache/solr/spelling/suggest/DictionaryFactory.java` (which builds the `HighFrequencyDictionary`), and the dictionary's ordering for equal frequencies was not traced.

## History

- No commit at the pin names SOLR-12918 (the evidence tool lists none).
- The test file's history shows no change tied to the ticket.

## Reproduction attempts

Not run. This pass ran no builds or tests. The ticket's own command is the reproduction: the test method with the listed seed, on the pinned head, with the failing assertion and the test XML recorded.

## Gap analysis

- The failing assertion is not in the ticket (the error text is truncated), so it cannot be matched to a line without a run.
- The ticket asserts a seed-specific failure; a run with the same seed at the pin is the first check.
- The tie-order question in the suggester is a plausible cause, but it is not traced.

## Verdict justification

keep-open: the test and its expected order are present at the pin, the failure is seed-specific, and the ticket's failing assertion is missing. close-fixed, close-obsolete, and close-cannot-reproduce do not fit without a run at the pin.

Missing for this verdict: a run of `TestHighFrequencyDictionaryFactory.testDefault` with the ticket's seed at the pin, with the failing assertion and the test XML.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
</content>
