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

## Pass 1 continuation (phone-agent-2, 2026-10-23)
- Reused vm3's completed review at unchanged head `cd878023d3d0e351700f5365f692423fbe97d972`; no repair.
- Tidy passed, rc=0, no tracked changes. Antora initially refused the linked worktree as a local content source before page processing. One environment relaunch used a standalone clone at the same commit and reused the completed tidy evidence.
- `:solr:solr-ref-guide:buildLocalAntoraSite --rerun` passed, rc=0, BUILD SUCCESSFUL in 59s. An optional BeautifulSoup import then failed in the extra verification script, not the build. Replaced it with standard-library HTMLParser and resumed only page verification, without another build.
- Rendered guidance is present and `filters.html#synonym-graph-filter` resolves to an existing file and anchor. Tree clean. Pass 1 regate GREEN.
- Logs: `/workspace/phone-agent-2-logs/solr-16914-review/pass-1/` (tidy and environment refusal), `/workspace/phone-agent-2-logs/solr-16914-review/pass-1-relaunch/` (successful Antora), `/workspace/phone-agent-2-logs/solr-16914-review/pass-1-final/` (page check and GREEN state).
- Gate: `gates/SOLR-16914-review-pass-1.md`. Java compile, proof tests and module check N/A per the existing docs-only receipt. No tests claimed.

## Pass 2 (phone-agent-2, 2026-10-23)
- Started after pass 1 GREEN. Head `cd878023d3d0e351700f5365f692423fbe97d972`; base `14c7aac0d151`. Re-read net diff: one page, 7 insertions and 3 deletions; `git diff --check` passes. Live fork head unchanged.
- Ticket re-read from `https://issues.apache.org/jira/rest/api/2/issue/SOLR-16914`: Open, Critical; original analyzer uses search mode, synonym graph filters, index-only FlattenGraph and EdgeNGram. Reported version 8.4.1; current Lucene defaults are not asserted to describe that historical version.
- Finding, major, ticket-fit limit (`language-analysis.adoc:2315-2317`): guidance does not prove the reporter's exact synonym resource works. Later decision `reports/owner-items-scrub-2026-10-22.md:20` and passing existing probe `gates/SOLR-16914-owner-items.md:10-20` settle the disposition as docs-only. The probe used two sample synonym entries, not the reporter attachment. Keep public claims narrow. No new production fix or repeated probe is required in this round.
- Finding, minor, historical commit message (`5e8fe46708`): vm3's finding remains; net diff does not touch Arabic. No history rewrite.
- Surrounding docs (`filters.adoc:3123-3131`) agree that SynonymGraph cannot consume input graphs. Fresh rendered anchor is verified. No new blocker or in-scope repair.
- Changelog and fail-before Java test N/A, docs-only. Lucene default evidence reused from receipt, not represented as a fresh runtime probe.
- Repairs: none. Pass 2 regate is next in the standalone clone, with a fresh forced Antora task.
- Pass 2 result: GREEN. Fresh tidy rc=0 (1m 1s), forced Antora rc=0 (46s), rendered guidance and target anchor verified, tracked tree clean. Logs: `/workspace/phone-agent-2-logs/solr-16914-review/pass-2/`; gate `gates/SOLR-16914-review-pass-2.md`.

## Pass 3 (phone-agent-2, 2026-10-23)
- Started after pass 2 GREEN. Re-read the diff against `14c7aac0d151` and rendered Japanese guidance; live fork tip remains `cd878023d3d0e351700f5365f692423fbe97d972`. One file, 7 insertions, 3 deletions, no whitespace errors and clean tracked tree.
- Finding, major, inherited evidence limit (`language-analysis.adoc:2315-2317`): documentation is configuration guidance, not a demonstrated fix for the exact 8.4.1 report or attached synonym file. The existing probe and recorded docs-only decision settle branch scope; no owner question is reopened. Public reply remains unperformed and requires authorization.
- Finding, minor, inherited commit-message mismatch (`5e8fe46708`): unchanged, not repaired. No new blocker, major defect or in-scope repair found.
- Reviewed `language-analysis.adoc:2292,2315-2317,2347-2354` for consistent default and mode wording, and `filters.adoc:3131` for the graph-input warning. The rendered pass 2 page includes the complete paragraph and a valid `filters.html#synonym-graph-filter` link.
- Changelog, Java fail-before proof and module tests N/A under the docs-only evidence shape. No production code change or new runtime test claim.
- Repairs: none. Pass 3 regate is next, with fresh tidy and forced Antora.

- Pass 3 result: GREEN at `cd878023d3d0e351700f5365f692423fbe97d972`. Fresh tidy and forced Antora rc=0, rendered text and link anchor verified, tracked tree clean. Logs: `/workspace/phone-agent-2-logs/solr-16914-review/pass-3/`; gate `gates/SOLR-16914-review-pass-3.md`.

## Final result
- All three pass regates GREEN at unchanged head `cd878023d3d0e351700f5365f692423fbe97d972`. Repairs: none. No product commit or branch push.
- Skip convention checked at live coordination tip `efedec1587`: the source README (`solr/solr-ref-guide/README.adoc:29`) documents `-Prefguide.include=false` or `SOLR_REF_GUIDE_INCLUDE=false` for normal assemble/check. That excludes automatic guide dependencies, not an explicit guide-build task. This unit specifically required guide regates in its named handoff and prior receipt; no waiver was found, so they ran.
- Existing probe and docs-only decision reused; reporter attachment and historical-version behavior remain unverified. No public action performed.
- The report, receipt, three gates, DONE claim and host heartbeat are published on coordination. No product branch push or public PR/comment/email action.
