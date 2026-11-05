# SOLR-18314 eDisMax empty phrase implementation, round 1

Status: **Implemented, Proof Pending**. Owner-directed adjacent preparation; this is not a new public PR competing with PR 4652.

| Revision | Full SHA |
| --- | --- |
| Unchanged PR 5294 / parent P | `0af4f5b3f2784eba3693acdc33dc49f6c2802b6a` |
| Pushed bounded candidate E | `6a8eb075ad7e906a547225845f8ac13420179e29` |
| Ian Springer's PR 4652 reviewed head | `c122384ba230eecd0a6f2ba13a15c02908c9f0fc` |

Branch `nick-boss-tech/solr:solr-18314-edismax-empty-phrase` was plain-pushed and independently read back through the refs API at E. Exact files: ExtendedDismaxQParser.java, TestExtendedDismaxParser.java and changelog/unreleased/SOLR-18314-edismax-empty-phrase.yml. Ordinary commit authored by Nick Shanin; 3 files, 65 additions and 2 deletions. Changelog credits Ian Springer and Nick Shanin.

## Decision ladder and reuse

Rung 1: empty optional BooleanQuery clauses cannot contribute a score but block the existing one-clause MatchAllDocsQuery unwrapping. Reuse the two eDisMax production edits already present in Ian's PR: reject zero eligible raw clauses and use the existing isEmpty helper for the analyzed phrase result. Rung 3: Smiley's November 3 comment explicitly supports this cleanup while objecting to the combined classic DisMax scoring change: https://github.com/apache/solr/pull/4652#issuecomment-6608422285 . Stop here on scope; there is no reason to select the disputed behavior or introduce an opt-out in this slice.

Classic DisMax single-term pf boosts can affect ranking when qf/pf fields or weights differ. Removing them would alter relevance, so no DisMax production or guide/upgrade file is changed. No performance magnitude is claimed without a benchmark. The early empty-list guard avoids unnecessary parsing; the later empty-query filter is the correctness protection.

Tests adapt Ian's wi-fi/usa analyzer examples in the existing schema rather than adding another analyzer fixture. They add fielded-query and both sow modes, verify no empty boost clause, require direct MatchAllDocsQuery for *:* with pf configured, and preserve meaningful phrases from one raw token. Existing isEmpty, parser request helpers and query matchers are reused; the only new import is the standard not matcher. This covers correctness, architectural fit and maintenance separately from runtime proof.

## Verification and follow-up

Windows source review and diff --check passed. No Gradle, compilation, formatter or tests ran. All new assertions are pins until Linux executes and discriminates them. `gates/solr-18314-edismax-empty-phrase-round-1.md` requires existing eDisMax, DisMax and synonym suites, unchanged new tests on original production, an isolated over-aggressive one-token mutation, YAML/tidy/Error Prone/root check and exact-head remote read-back. Receipt: `receipts/SOLR-18314-edismax-empty-phrase-round-1.md`.

After proof, reconcile live PR 4652 before preparing any offer, so incorporated work is not offered twice. This record does not authorize a PR/comment or edit Ian's branch. PR 5294 remains at P. Claim: `claims/solr-18314-edismax-empty-phrase-round-1-windows.md`.
