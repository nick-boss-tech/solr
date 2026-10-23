# PR #5140 (SOLR-8003) Ref Guide update, round 1

- Host: vm5. Claim: `claims/pool-vm5-pr5140-refguide-round-1.md`. Assignment: `assignments/pool-pr5140-refguide-round-1.md`.
- Branch: `solr-8003-submit` on the fork. Head at claim: `a24414bf3d9`. Final head: `874766b7625` (docs commit, one commit on top).

## Pages changed

- `solr/solr-ref-guide/modules/query-guide/pages/document-transformers.adoc`, section `[json] / [xml]`. Added the glob form (`fl=id,*_json:[json]&wt=json`) and five limits: unmatched fields are written as usual; `fl=*:[json]` is a 400; only `[json]` and `[xml]` can follow a glob; the glob is matched against returned names, so a renamed field is raw only under `f=`; a glob applies only when the transformer's `wt` matches the request's `wt`; `/export` ignores the raw mark.

## Where the text came from (rung 1)

- `SolrReturnFields` glob branch: a bare `*` is a 400, and a non-raw factory is a 400, so only raw factories can follow a glob.
- Registered raw factories are `json` and `xml` only (`TransformerFactory.java` lines 117-118), so the "only `[json]` and `[xml]`" line is the full set.
- `RawValueTransformerFactory.createForGlob` returns null when the writer does not match, so `*_json:[xml]&wt=json` returns plain values.
- `TextResponseWriter.shouldWriteRaw` matches the glob against the returned field name.
- Export behavior is the one stated in the PR body and tested in `TestExportWriter`.

## Renamed-field choice (rung 4, recorded)

The PR body already records option A (a glob matches the returned name) with a recommendation for A, and this page documents A. If the owner picks B, this page and the example in it change with it.

## Verification

- Syntax: rendered the page with `npx -p @asciidoctor/cli asciidoctor` (ad hoc, outside the guide's Antora build). It rendered with no warnings, and the new list, the code blocks and the `fl=*:[json]` text came out as intended.
- Full Ref Guide build: not run. The Antora build needs the guide's npm setup and is not practical for a docs-only change on this host. Tracked as a gap for a host that runs it.
- Tests: not re-run. The commit touches only the `.adoc` file, and the code proof at `a24414bf3d9` (TestRawTransformer 17/17, TestExportWriter 22/22) is unchanged.
- No em dashes or en dashes in the added lines.

## PR description

- The body had no documentation claim that needed correcting. Added a Ref Guide line that links the page at `874766b7625`. Edit made through the GitHub API, read back, and no dashes in the body.
- The PR head on the fork is now `874766b7625`. The body's code links still point to `a24414bf3d9`, which is the tested code head.

## Result

Ref Guide page for the raw glob written and pushed. Deliverable is this report; the full guide build is the one check not run.
