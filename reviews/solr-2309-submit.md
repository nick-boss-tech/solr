# solr-2309-submit

- Branch: origin/solr-2309-submit
- Head: 06f5a1c4a87e
- Base: upstream/main at 9d7cc2884e8a (merge-base 97d973814336, 19 commits behind)
- Scope: 3 commits. `ExtendedDismaxQParser.java` (+28: fuzzy case returns `null` when the analyzer yields no tokens; new helper `analysisYieldsNoTokens`; imports), `TestExtendedDismaxParser.java` (+82: fuzzy-stopword assertions and `testFuzzyOnUndefinedField`), changelog `SOLR-2309-edismax-fuzzy-stopword.yml` (+8, type `fixed`)
- Verdict: Close (the round 28 meaning: the fix is in place and only proof and hygiene items remain)
- Reviewer: claude-haiku-5-5 (Claude Code), 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. Every claim below rests on reading the diff and the code at the listed head.

## Delta check against earlier disposition

- Handoff: round 32 eDisMax disposition at this head. Head unchanged: `origin/solr-2309-submit` is at `06f5a1c4a87e`, as listed.
- Bulk review (round 28, `research/branch-reviews/round-28/SOLR-2309-review.md`): Close, at snapshot `5b73e2afdeb`. Its open items were F1 (a test for a fuzzy term on an undefined field), F2 (a test for a mixed `qf`), and F3 (no proof recorded).
- Delta since the snapshot: `git diff 5b73e2afdeb 06f5a1c4a87e` changes only `TestExtendedDismaxParser.java` (+19 lines). The main source is unchanged. The added tests cover the round-28 F1 and F2 requests, so those two are addressed. F3 (proof) is not addressed by anything in the tree; no queue result was checked in this review.

## Verified code facts

- `removeStopFilter` is a parser field (`ExtendedDismaxQParser.java:992`) set by `setRemoveStopFilter(!config.stopwords)` (`parseOriginalQuery`). `analysisYieldsNoTokens` chooses its analyzer the same way as `newFieldQuery` (`removeStopFilter ? noStopwordFilterAnalyzer : qa`, lines 1113-1119 and 1507).
- All-stopword re-parse: `parseOriginalQuery` re-parses with `setRemoveStopFilter(true)` when the first parse is empty and `stopwords` is on and `alwaysStopwords` is off. The fuzzy helper follows that second parse, so `q="the~0.5"` (all stopwords) keeps `text_sw:the~`, as the test expects. Traced, not run.
- The fuzzy case sits inside the enclosing `try`, whose `catch (Exception e)` already returns `null` for incompatible field queries. Callers therefore already handle `null` from this method, so the new `return null` is not a new kind of result.
- `IOException`, `TokenStream`, and `TextField` imports are added and used.

## Findings (ranked)

1. **LOW, verified. Per-term analyzer is rebuilt, not cached.** `newFieldQuery` caches the no-stopword analyzer in `nonStopFilterAnalyzerPerField` (lines 1113-1119). `analysisYieldsNoTokens` calls `noStopwordFilterAnalyzer(fieldName)` directly on every fuzzy term. `noStopwordFilterAnalyzer` builds a new `TokenizerChain` each time it is called. So a query with many fuzzy terms builds many analyzers. A small cost, but it is avoidable by reusing the cache. Suggested change (not applied): route through the same cache.

2. **LOW, verified. Analysis failures are swallowed into a silent drop.** `analysisYieldsNoTokens` declares `IOException`, and it runs inside the enclosing `try`/`catch (Exception e)`, which returns `null`. A failure from the analyzer therefore drops the fuzzy clause without a log line, where base would have built the fuzzy query. The existing catch is documented for field-type incompatibility, not analyzer I/O. Owner may want analysis errors to propagate or log.

3. **LOW, verified. Helper duplicates the analyzer-selection rule.** The `removeStopFilter ? noStopwordFilterAnalyzer : getQueryAnalyzer` choice now appears in two places. A later change to one would need the same change in the other. Consider a shared helper used by both `newFieldQuery` and `analysisYieldsNoTokens`.

4. **Positive, verified by reading (not run). The discriminating assertions.** On base, `the~0.5` builds a fuzzy clause on `text_sw`, so `//str[@name='parsedquery'][not(contains(.,'text_sw:the'))]` fails. The per-field assertion (`qf="text_sw name"`) and the `stopwords=false` assertions also depend on the drop being per field and per analyzer, which the helper does. `testFuzzyOnUndefinedField` pins the fallback for an undefined field: the clause never reaches the per-field fuzzy path, so the helper is not reached.

5. **LOW, hypothesis. Drain loop comment.** The helper drains the stream after the first token before `end()`, citing graph filters. This matches the Lucene contract (`end()` must follow exhaustion). No issue found; noted because the comment makes a specific claim about synonym graphs that was not verified.

## Owner calls

None. Consistency with the plain-term path is the stated rule, and the branch follows it.

## Not checked

- Not compiled, formatted (spotless), or run. Pass and fail on base and head is a hypothesis (finding 4).
- Whether the shared `nonStopFilterAnalyzerPerField` cache is keyed the same way in every entry point (only the `newFieldQuery` use was read).
- Error Prone was not run.
- Nothing pushed or posted to GitHub or JIRA.
