# Gate job: SOLR-16155 review pass 1 (document values out of update errors)

Status: DONE (GATE GREEN at 0881de1ed6, vm5, 2026-10-20T15:47Z; minor scope note recorded)

- Branch: solr-16155-submit. Head: `0881de1ed68b847c781abd7ccb56196cdbf15ecd` (ls-remote 2026-10-20T15:37Z).
- Prior gated head: `0881de1ed6` (receipt, hybrid gate). Base: `14c7aac0d1`.
- Intended host: vm5. Capability tags: gate.
- Module: `:solr:core`. Focused classes: `org.apache.solr.handler.loader.JavabinLoaderTest`, `org.apache.solr.update.DocumentBuilderTest`, `org.apache.solr.update.processor.TolerantUpdateProcessorTest`. Seed: default.
- Diff scope against `14c7aac0d1`: JavabinLoader, DocumentBuilder, three tests, one changelog.

## Checks

- [x] head confirmed by ls-remote
- [x] diff scope read; changelog text read (discloses id and field name only)
- [x] compile `:solr:core:compileTestJava` rc=0 (no reference to MAX_VALUES_AS_STRING_LENGTH remains)
- [x] `JavabinLoaderTest` 3/3, `DocumentBuilderTest` 17/17, `TolerantUpdateProcessorTest` 11/11 (fresh XML)
- [ ] tidy, Error Prone, module check: not run this round (receipt covers them)
- Note (minor): exception text dropped from the update error response, beyond the ticket's field-value scope; disclosed in the changelog.

## Deliverable

- Pass 1 result in `reports/newdev-critblock-review-round-1/solr-16155-submit.md`.
- Receipt section in `receipts/SOLR-16155.md`, top section.
