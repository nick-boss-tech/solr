# Claim: pool-vm3-jira-newdev-bugs-round-1, slice SOLR-13185 (item 4 only)

- Claimant host: vm3, matching `hosts/vm3.md`.
- Status: DONE. Slice: item 4 only, SOLR-13185. Other items are not claimed by vm3.
- Job or assignment: `assignments/pool-jira-newdev-bugs-round-1.md`, item 4.
- Capability tags used: implementation, gate.
- Started: 2026-10-19T17:05:00Z (UTC).

## Result (vm3, 2026-10-19)

- Branch: `solr-13185-query-parse-null`, head `84523828bb` (base `c8ab71430f`). Commits: `365392a484` adds the regression test, `84523828bb` adds the fix.
- Reproduction at base plus test: `defType=complexphrase` with no `q` fails with an NPE in `StringReader`, from `ComplexPhraseQueryParser.parse`. The ticket reproduces.
- Fix: `solr/core/src/java/org/apache/solr/search/ComplexPhraseQParserPlugin.java`, `ComplexPhraseQParser.parse` returns null for a null query string, as the Surround and Lucene parsers do.
- Verdict: reproduces. Fix and regression test pushed as a new branch. No PR opened; PR needs Nick's approval.
- Receipt: `receipts/SOLR-13185.md` on coordination.

Heartbeat: 2026-10-19T17:29:00Z

DONE: 2026-10-19. Deliverables: this claim file (result section), `receipts/SOLR-13185.md`, branch `solr-13185-query-parse-null` at `84523828bb`.
