# Skip record: SOLR-13242

- Ticket: SOLR-13242, RegexReplaceProcessorFactory not making accurate replacement.
- Verdict: keep-open.
- Draft branch: solr-13242-draft-open (verdict unchanged; this record replaces the earlier record on this branch).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm5 (pool Linux host), 2026-10-20 UTC. Redo of remediation batch 07, item 5 (`assignments/pool-aaa-skip-verdict-remediation-07.md`).

## Ticket reading

JIRA read 2026-10-20: Open, no resolution, created 2019-02-12, updated 2019-08-12, no fix versions, no links. The report: a `RegexReplaceProcessorFactory` with pattern `(\s*\r?\n)\{2,}` and replacement `<br><br>` gives the wrong number of replacements in the indexed `content` field. Ten comments (2019-02-12 to 2019-08-12). The reporter found that the pattern is the cause (the `\s` class includes newlines, so the match spans more than intended), and that `\h`, `[ \t]*`, or `\W` gave the expected result (2019-03-13, 2019-04-24). Two comments (2019-08-12, nppoly and gus) say the processor is a thin wrapper around `Pattern.matcher(...).replaceAll(...)` and that they could not reproduce a Solr-side fault.

## Code reading

Read at the pin:

- `solr/core/src/java/org/apache/solr/update/processor/RegexReplaceProcessorFactory.java:81`: the pattern is compiled with `Pattern.compile(patternParam.toString())`.
- `RegexReplaceProcessorFactory.java:124-126`: each CharSequence value is passed to `pattern.matcher(txt).replaceAll(replacement)`.
- The processor does not change the pattern or the match. The matching is `java.util.regex` behavior, and the reporter's observed output follows from the pattern's `\s` class.
- `solr/solr-ref-guide/modules/configuration-guide/pages/update-request-processors.adoc:403` lists the processor with a link to its Javadoc. The guide does not explain `\s`, newline matching, or the literal-replacement option in this context, so it does not answer the question.

## History

No commit in the pinned history names SOLR-13242. The processor's matching line has not changed in the history read in this pass; the history was not traced further.

## Reproduction attempts

Not run. This pass ran no builds or tests. The missing piece is a run through the pinned processor: an update with the ticket's pattern and replacement against a sample value containing `\r\n` and spaces, comparing the output with the `\h` or `[ \t]*` pattern. The commenter's unit test (2019-08-12) is not in the ticket's code.

## Gap analysis

- The code path is a direct pattern replacement; nothing in Solr changes the result for a given pattern, so the reported output is a regex-semantics question.
- Close-answered needs a reference-guide answer, and the guide does not give one. Close-cannot-reproduce needs a run at the pinned head, which has not been done.
- The reporter's working pattern is a configuration fix; the ticket does not record a Solr defect.

## Verdict justification

keep-open: the reading shows the processor is a direct regex wrapper and the reporter's own pattern change explains the output, but no run at the pinned head has confirmed that, and the reference guide does not answer the question. close-cannot-reproduce would hold after a run that matches the reporter's result with the expected pattern at the pin.

Missing for this verdict: a run of the pinned processor on the ticket's pattern and a sample value, showing the matched span, plus the same run with the `\h` pattern.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
