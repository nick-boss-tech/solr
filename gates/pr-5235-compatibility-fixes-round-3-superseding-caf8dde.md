# Gate: PR 5235 compatibility fixes, superseding round 3 (pinned `caf8dde9063f`)

Status: OPEN
Tags: gate, premise-run, review, records. Intended host: vm2, claim `claims/pr-5235-compatibility-fixes-round-3-vm2.md`.
Supersedes: `gates/pr-5235-compatibility-fixes-round-3.md` for the fixed head only, under its tidy-repair rule.

## Pinned inputs

- Fork: `nick-boss-tech/solr:solr-18351-submit`.
- Final fixed head F: `caf8dde9063fd2470c02d9938b5723e691c0e11e`. One formatting-only commit on `5f279c7aa44a993bd999be1d4dfc6ecab7942bc8`: a javadoc line wrap in `CommandOperation.java`, Nick Shanin, no trailers. Tidy at `5f279c7` changed only that line.
- Original production PR head O: `9ff44cdd0b818c6f1296d69651732f794acd76b7` (apache/solr PR 5235, live head unchanged at this check). O is an ancestor of F.
- Incremental O..F: six production files (ApiBag, EmbeddedSolrServer, SolrRequest, JavaBinRequestWriter, XMLRequestWriter, CommandOperation) plus three regression suites. `HttpJettySolrClientTest` unchanged relative to O.
- Seed `5235C0FFEE18351` on all focused and neighboring runs.

## Matched pair

Unchanged from the round 3 gate: F leg is the exact final head F; O leg is O with the three test files restored byte-for-byte from F. Test blob hashes are recorded at execution.

## Legs

Run sequentially under the host lock, one step at a time, each with separate shell arguments, `--no-daemon --no-build-cache`, and `cleanTest` where the gate specifies it. Steps:

1. Tidy at F, tree must be clean.
2. O focused: three commands from the gate (solrj command API, embedded, Jetty HTTP).
3. F focused: the same three commands.
4. Neighbors on F: `HttpJdkSolrClientTest` and `EmbeddedSolrServerTest` in solrj; `HttpJettySolrClientTest` in solrj-jetty; `TestEmbeddedSolrServerSchemaAPI` and `TestEmbeddedSolrServerResponseParser` in core.
5. Tidy at F again, tree must stay clean.
6. Root check on F: `env -u NODE_ENV ./gradlew --no-daemon check -x test --no-build-cache`, not a module check.

Expected O totals: 12 regression failures and 6 passing controls. Expected F totals: all 18 methods pass. Actual outcomes are recorded from fresh XML, not from these predictions.

Evidence: `/workspace/gates/logs/vm2/pr5235-r3/`. Snapshot at claim: `903d8365a88`, 2026-10-26T19:27:35Z.
