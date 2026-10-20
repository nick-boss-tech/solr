# Skip record: SOLR-11764

- Ticket: SOLR-11764, "preanalyzed field with highlight option throws exception". JIRA status read 2026-10-20T08:06:30Z (UTC): Open, resolution None, updated 2019-06-08.
- Verdict: close-answered (kept; the recorded verdict is re-checked against the docs at the pinned head).
- Draft branch: solr-11764-draft-close.
- Pinned head: 498a09aea97aac86c2cc7ac114113b946d249942 (fork origin/main).
- Checked by: vm2, 2026-10-20T08:06:30Z (UTC).

## Ticket reading

A PreAnalyzedField with highlighting enabled throws "Invalid JSON type java.lang.String, expected Map" during highlighting. The reporter's comment (2018-07-06) says that setting `storeOffsetsWithPositions="true"` on the field fixed it. The reporter confirmed the fix. Other comments discuss whether PreAnalyzedField should be an update processor, which is a design topic, not this question.

## Docs (pinned head)

- `solr/solr-ref-guide/modules/query-guide/pages/highlighting.adoc:338`: under the Postings option, "Set `storeOffsetsWithPositions` to `true`." The same page (line 326-340) explains that the Unified Highlighter can read offsets from postings, and that analysis is the default.
- The page answers the question: the highlighting option that avoids analyzing the stored text needs offsets stored with positions. The reporter's fix is that option.

## Code reading (pinned head)

Not needed for the answer. The exception path is the pre-analyzed parser on the analysis route, which the docs direct away from by the postings option.

## History

- evidence.py at the pinned head: no commits naming SOLR-11764.

## Reproduction attempts

Not run. The reporter's fix is confirmed in the ticket, and the docs give the option.

## Gap analysis

None for the answer. Whether the pre-analyzed parser should give a clearer error when the highlight method is set without offsets is a separate question, not raised here.

## Verdict justification

close-answered meets the rubric: a support question that the reference guide answers, with the page linked (highlighting.adoc:338 and the Postings description at 326-340), and the reporter confirmed the option in the ticket.

## Limits

This record does not verify the behavior on the pinned head with a run. It does not change the pre-analyzed parser's error text.
