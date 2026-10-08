# solr-12608-submit

- Branch: origin/solr-12608-submit
- Head: d1dd8a1f9f0a (the listed head; matches the fork tip, `d1dd8a1f9f0aa17c6e34f8c5e40cb9768a83aea2`, after fetch 2026-10-08)
- Base: upstream/main at 9d7cc2884e8a (merge-base 14c7aac0d151, 45 commits behind, 5 commits ahead)
- Scope: 5 commits, 3 files. `SolrQueryParserBase.java` (new `public static collapseRepeatedWildcards(String)`, `:1271`, called from `getWildcardQuery` at `:1305`), `TestSolrQueryParser.java` (+19, `testRepeatedWildcardsAreCollapsed`), changelog `SOLR-12608-collapse-repeated-wildcards.yml` (`type: fixed`). The tip commit (`d1dd8a1f9f0`) removes the testing handoff file, so no `SOLR-12608-TESTING.md` is on the tip.
- Verdict: Not ready (the premise is unproven on this tree, and the only new test does not discriminate the change)
- Reviewer: claude-haiku-5-5 (Claude Code), round 36 Group B review, 2026-10-08

Nothing here was compiled, formatted, or run. No Gradle, no `drain`, no test runs. Every claim rests on reading the diff and the code at the listed head.

## Head check (handoff near miss)

The bulk review header gives its snapshot as `d1dd8a1f0aa17c6e34f8c5e40cb9768a83aea2`. That object is not in this clone (`git cat-file -t d1dd8a1f0aa1` fails). The tip is `d1dd8a1f9f0aa17c6e34f8c5e40cb9768a83aea2`. The bulk string is the tip with `9f` dropped, so it reads as a transcription error. Two checks point to the same tree: the bulk review's line anchors (`SolrQueryParserBase.java:1271-1299,1302-1305`, `TestSolrQueryParser.java:276-289`) match this tip's diff, and the test assertion it cites is the one at the tip. This review is on the listed head, `d1dd8a1f9f0a`.

## Delta check against the bulk review

Bulk review `research/branch-reviews/round-28/SOLR-12608-review.md` (verdict Not ready) on the same tree.

- Bulk F1 (MEDIUM, the new query test does not establish the premise): **confirmed.** See finding 1.

## Findings (ranked)

1. **MEDIUM, verified. The new test asserts status 0 only and does not show the change matters.** `testRepeatedWildcardsAreCollapsed` (`TestSolrQueryParser.java`, diff lines 79-95) checks the helper's string results, then runs `assertQ` for 5,000 stars under `lucene` and `edismax`, asserting only `responseHeader/status = 0`. It does not show that the unmodified parser fails, times out, or exceeds a bounded resource. So the test would pass on base as well, which means it does not prove the normalization fixes the reported OOM. The changelog's OOM claim ("avoids the excessive memory use") is therefore not backed by a test on this tree.

2. **LOW, hypothesis. The edismax half of the test may not reach the helper.** A query made only of `*` may take a match-all branch in the edismax parser before `getWildcardQuery` runs. If so, the edismax assertion does not exercise `collapseRepeatedWildcards` at all. Not checked; reading `ExtendedDismaxQParser` would settle it.

3. **Not a finding (checked by reading).** The helper keeps an escaped star (`a\***b` becomes `a\**b`), collapses unescaped runs (`a***b` becomes `a*b`), keeps a lone `?`, and keeps a trailing backslash. The collapse runs before the `"*".equals(termStr)` check in `getWildcardQuery`, so the match-all path sees the collapsed term. Semantically, a run of `*` matches the same strings as one `*`, so the normalization is sound if the premise holds.

4. **Premise (carried from the bulk review, not re-read here).** The bulk review cites `research/pipeline/research-notes/SOLR-12608.md:1-9`: the JIRA is from Solr 7.2/7.4, has little reproduction detail, and is not known to reproduce on current main because Lucene bounds wildcard determinization. The same note recommends SKIP absent a current reproduction.

## Owner calls (not decided here)

1. **Pursue the normalization without a current reproduction?** The research note recommends SKIP. If the owner keeps the branch, the premise has to be shown first (finding 1). The choice between "reproduce and fix" and "drop the OOM rationale and keep the normalization as hygiene" is the owner's.

## Proposed fixes (not applied; the owner decides)

- Finding 1: run the 5,000-star query against the unmodified parser on the relevant Lucene and Solr version and record the failure (or the bound that is hit). Then make the test assert the failure mode on base and success on the branch. If no failure reproduces, the changelog's OOM sentence should go.
- Finding 2: confirm the edismax path reaches `getWildcardQuery`; if not, add a case that does.

## Interactions with other branches

- None found in the bulk round notes for this ticket.

## Not checked

- Not compiled, formatted, or run. No Gradle, no `drain`, no test runs.
- The edismax match-all path (finding 2).
- The research note (`research/pipeline/research-notes/SOLR-12608.md`) was not re-read; its content is carried from the bulk review.
- The queue result (none, per the bulk review) was not re-read.
- No GitHub or JIRA writes were made.
