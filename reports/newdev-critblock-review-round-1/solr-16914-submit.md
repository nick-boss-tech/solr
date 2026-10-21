# Review round 1, unit 15: SOLR-16914 (branch solr-16914-submit), vm3

## Pass 1 (vm3, 2026-10-21T03:25:27Z)

- Head reviewed: `cd878023d3d0e351700f5365f692423fbe97d972`. Base `14c7aac0d1`. Diff: one file, `language-analysis.adoc` (7 insertions, 3 deletions). Four commits: b647d03036, 5e8fe46708, ed1a00202e, cd878023d3.
- Ticket SOLR-16914 (Open, Critical): "Japanese Synonyms Search not workinh". The reporter's index analyzer is `JapaneseTokenizerFactory mode="search"`, then `JapaneseBaseFormFilter`, stop filters, `CJKWidthFilter`, `SynonymGraphFilter expand="true"`, `FlattenGraphFilter`, `JapaneseKatakanaStemFilter`, `LowerCaseFilter`, `WordDelimiterGraphFilter`, `EdgeNGramFilter`. The ticket says synonym search does not work with it.
- What the branch does: documents the `discardCompoundToken` behaviour in the tokenizer section, changes the documented default of `discardCompoundToken` from none to `true`, and adds a note that with the default the `search` and `extended` modes produce a linear stream, while `discardCompoundToken="false"` produces a graph that `SynonymGraphFilter` cannot consume correctly.
- Finding, major (ticket fit): the branch documents the behaviour but does not reproduce the reporter's failure. The docs say that with the default, no mode switch is needed before synonyms, which is the reporter's setup, yet the ticket reports that setup as failing. Nothing in the receipt or the branch shows that the reporter's config passes or fails at the head. A docs-only change cannot close the Critical bug unless the owner accepts a documentation fix as the resolution. Recorded, not repaired (a scope choice for the owner).
- Finding, minor (commit message): `5e8fe46708` says "drop the Arabic paragraph (SOLR-11330)". The net diff against base does not touch the Arabic section, and the sentence that commit removes is not in base either. The message does not match the net change. Recorded; a docs commit message is not a repair target.
- Surrounding code check: `filters.adoc` defines the Synonym Graph Filter section that `xref:filters.adoc#synonym-graph-filter[...]` targets. The anchor could not be built here (no guide build on this host). Unverified until the guide build.
- The `discardCompoundToken` default lives in Lucene's Kuromoji module, not in this checkout (no match in `solr/` or `lucene/` at the head). The default is verified only by the Lucene 10.4.0 fact recorded in receipts/SOLR-16914.md. Not re-verified in this pass.
- Repairs in pass 1: none.
- Regate: NEARLY. The changelog and compile steps do not apply to a docs-only head (no changelog entry in the branch; no Java change). The blocking step is the reference-guide build, which needs npm; vm3's host file does not list the npm tag. Passes 2 and 3 need the same regate and are not run here.

Pass 1 result: NEARLY at `cd878023d3d0e351700f5365f692423fbe97d972`, blocked on the guide build.
