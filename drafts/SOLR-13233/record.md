# Skip record: SOLR-13233

- Ticket: SOLR-13233, SpellCheckCollator ignores stacked tokens.
- Verdict: keep-open.
- Draft branch: solr-13233-draft-open (verdict unchanged; this record replaces the audit-derived record on this branch).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Upstream sha not cited.
- Checked by: vm5 (pool Linux host), 2026-10-20 UTC. Redo of remediation batch 07, item 7 (`assignments/pool-aaa-skip-verdict-remediation-07.md`).

## Ticket reading

JIRA read 2026-10-20: Open, no resolution, created 2019-02-08, updated 2019-06-08, no fix versions, no links. The report: `SpellCheckCollator` skips every token with a position increment of 0, on the assumption that such tokens were injected and carry offsets that do not match the original query. A stacked token from a filter such as WordDelimiterGraphFilter, which keeps the original text, is skipped too, so the collation misses a valid correction. Three comments (2019-02-08 to 2019-02-10): romseygeek is unsure of the fix (emit the original token first, or check the other token filters); sokolov suggests skipping all stacked tokens, original and injected. No agreed fix.

## Code reading

Read at the pin:

- `solr/core/src/java/org/apache/solr/spelling/SpellCheckCollator.java:210-217`: `offset` is set, and the loop over the tokens does `if (tok.positionIncrement() == 0) continue;` on line 217. The skip is present as the ticket describes.
- `SpellCheckCollator.java:255-258`: the replacement uses `tok.startOffset()` and `tok.endOffset()` against the unparsed query, so a stacked token's offsets are used directly; the skip exists because those offsets can be wrong for injected terms.
- The skip has not changed in the history read in this pass (`git log -S 'getPositionIncrement'` on the file returned no commit that names the ticket).

## History

No commit at the pin names SOLR-13233 (the evidence tool lists none). The history of the skip line was not traced beyond the search above.

## Reproduction attempts

Not run. This pass ran no builds or tests. The missing piece is a run: a spellcheck collation request on a field whose analyzer emits a stacked (position increment 0) token with the original text, showing the collation that is not produced.

## Gap analysis

- The code path is present and matches the ticket's description.
- The ticket records no agreed fix: one option (emit the original token first in the filter) is a filter change, and the other (skip all stacked tokens) changes collation for synonyms. Choosing between them is a maintainer decision.
- No fix commit exists, so close-fixed does not fit; the feature is present, so close-obsolete does not fit.

## Verdict justification

keep-open: the code path is confirmed at the pin, the reproduction has not been run, and the fix choice is open on the ticket. pr-needed needs the reproduction and the fix choice.

Missing for this verdict: a run of a stacked-token collation at the pin, and the maintainer choice between the two fix shapes.

## Limits

This record does not establish a reproduction. It does not post anything to JIRA or GitHub. Commit linkage is by commit title or code path, as stated above.
</content>
