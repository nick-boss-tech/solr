# SOLR-3498 - hypothetical reproduction (not run)

Nothing here was compiled or run. The change and test were guessed from reading `upstream/main`.

## JIRA context
`ContentStreamUpdateRequest` (SolrJ, used for CSV/Solr Cell uploads) did not honour `setCommitWithin()`. The audit
note said "obsolete: ContentStreamUpdateRequest removed".

## What the code shows on main
The class was replaced by `ContentWriterUpdateRequest` (extends `AbstractUpdateRequest`, has `addFile(Path, contentType)`,
so the content-type half, SOLR-3064, is fixed). `AbstractUpdateRequest.setCommitWithin` only stores a field. The only
readers of that field are `UpdateRequest`/`JavaBinUpdateRequestCodec`/`XMLRequestWriter`, which serialise documents into
the body. `ContentWriterUpdateRequest.getParams()` returns just `params`, and the content is opaque to SolrJ, so the
value never reaches the server: the same defect as the ticket, on the replacement class.

## Change
`ContentWriterUpdateRequest.setCommitWithin(int)` also sets (or, for a negative value, removes) the `commitWithin`
request parameter (`UpdateParams.COMMIT_WITHIN`), which the CSV/JSON/XML/Cell loaders read.
`TestContentWriterUpdateRequest` covers both cases.

## Guessed / verify first
- `AbstractUpdateRequest.setParams(...)` replaces the params and would drop the value; not handled.
- Not checked: that every loader behind `/update/extract` reads `commitWithin` (the standard `UpdateRequestProcessor`
  chain reads it from the params when building the `AddUpdateCommand`).
- Fail-before: the new assertion on `getParams()` fails on main (null).
