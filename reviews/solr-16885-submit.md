# solr-16885-submit

- Branch: origin/solr-16885-submit
- Head: 2b9b80119a92 (matches the listed head)
- Base: upstream/main at 9d7cc2884e8a (merge-base cabedd1d968, 16 commits behind)
- Scope: 3 commits. `solr/core/src/java/org/apache/solr/highlight/UnifiedSolrHighlighter.java` (+10: `SolrExtendedUnifiedHighlighter.getOffsetSource` falls back to ANALYSIS for TERM_VECTORS fields without positions), `solr/core/src/test-files/solr/collection1/conf/schema-unifiedhighlight.xml` (+9: `text_tv_offsets` type and `text4` field), `solr/core/src/test/org/apache/solr/highlight/TestUnifiedSolrHighlighter.java` (+14: `testTermVectorOffsetsWithoutPositions`), the changelog fragment, and `SOLR-16885-TESTING.md` (kept in place).
- Verdict: Needs work
- Reviewer: review-agent A2 (claude-haiku-5-5, Claude Code), 2026-10-08

Nothing here was compiled, run, or tested. No Gradle. Every claim below was checked by reading code, except where marked as hypothesis. Patches: none.

## Premise check (hypothetical-reproduction handoff)

- VERIFIED: the code path the branch changes exists on base. `SolrExtendedUnifiedHighlighter.getOffsetSource` (`UnifiedSolrHighlighter.java:274`) returned `super.getOffsetSource(field)` unless `hl.offsetSource` was set.
- VERIFIED: the schema flag the guard reads is real. `SchemaField.storeTermPositions()` reads `STORE_TERMPOSITIONS` (`SchemaField.java:119-121`). Nothing in the schema code forces positions on when `termOffsets` is set, so `text_tv_offsets` (`termVectors` and `termOffsets`, no `termPositions`) has positions off. The test's `assertFalse` holds by reading.
- HYPOTHESIS: Lucene picks `TERM_VECTORS` for these fields and then throws `IndexOutOfBoundsException` in `DefaultPassageFormatter.append`. That is the ticket's claim and the basis for the test. Lucene's source is not in the workspace, so it was not traced. The TESTING doc says the test may pass on main as well.
- VERIFIED, from the JIRA packet (`research/jira-context/SOLR-16885.json`, the comment body): the maintainer comment says "Root cause is Lucene issue 12431 ... There is not likely a fix in Solr", and "I created this ticket mainly for tracking purposes and to assist Solr users in searching for this type of problem and how to fix if via schema changes". The branch adds a Solr-side workaround, which is the direction the ticket rules out.

## Findings (ranked)

MEDIUM (direction call, verified): The ticket's own stated resolution is a schema change and a reindex, not a Solr code change (see the JIRA comment above). The TESTING doc also names the schema change as "the real fix". This branch adds a fallback that changes highlight results and cost for those fields: highlighting re-analyzes the stored text instead of reading term vectors. Owner call: (a) ship the Solr-side fallback, (b) close the ticket with the schema-change guidance and no code, or (c) narrow the fallback further. Not decided here.

MEDIUM (hypothesis, not verified): The fail-before proof depends on Lucene behavior that is not checked here. If Lucene no longer throws for these fields, `testTermVectorOffsetsWithoutPositions` passes on main too, the Linux gate's fail-before verdict is `NOT_PROVEN`, and the change ships with no proof of the failure it fixes. The TESTING doc says this is a risky guess.

LOW (verified): The guard fires for every `TERM_VECTORS` choice on a field without positions (`UnifiedSolrHighlighter.java:280-287`). It does not check for offsets, so a field with term vectors but neither positions nor offsets also moves to ANALYSIS. The TESTING doc's second risky guess mentions this, but says the field "takes the analysis path only when positions are missing". That is true. The effect on those fields is still a behavior change, and it is not covered by the test.

LOW (verified): The guard reads the schema (`schema.getFieldOrNull(field).storeTermPositions()`), not the index. For an index written before a schema change, the decision follows the schema and not the stored positions. This is an edge case, and the schema is the right input for the TESTING doc's stated fix.

## Verified correct (by reading; not run)

- Compiles by reading. `IndexSchema` and `SchemaField` are imported (`UnifiedSolrHighlighter.java:49-50`). `schema` is a `protected final IndexSchema` field set in the constructor (`:247`, `:254`). `OffsetSource` is already used in the same method.
- The logic. The guard fires only when `super.getOffsetSource` returns `TERM_VECTORS` and the schema says no positions. An explicit `hl.offsetSource` returns before the guard (`:276-277`), so an explicit `term_vectors` is still honored, as the TESTING doc says.
- The test wiring. `TestUnifiedSolrHighlighter.java:34` loads `schema-unifiedhighlight.xml`, so `text_tv_offsets` and `text4` are present. Existing `text_offsets` (`storeOffsetsWithPositions="true"`) is unchanged.
- The changelog fragment matches the upstream format (`type: fixed`, ICLA author, JIRA link).
- All three commits are authored as the ICLA identity, with no Co-Authored-By trailers.

## Owner decisions posed (not decided, no patch made)

1. Ship a Solr-side fallback for positionless term-vector fields, or close the ticket with the schema-change guidance (see the first MEDIUM). The verdict becomes Close if the owner keeps the ticket's stated direction.
2. If the fallback ships: is the scope right (any TERM_VECTORS field without positions, including fields without offsets)? See the first LOW.

## Not checked

- Nothing was compiled or run.
- Lucene's `UnifiedHighlighter.getOffsetSource` and the `DefaultPassageFormatter` exception path were not traced. No Lucene source is in the workspace. The claims about Lucene are hypothesis.
- The expected highlight output of the new test (one snippet containing `<em>crappy</em>` and `<em>document</em>`) was not evaluated against the highlighter's passage selection.
- Other callers of `getOffsetSource`, and the other `hl.method` paths, were not enumerated.
- Upstream conflicts were not checked. The branch is 16 commits behind `upstream/main`.
