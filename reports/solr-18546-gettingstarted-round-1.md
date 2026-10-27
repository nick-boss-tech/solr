# SOLR-18546 gettingstarted vs techproducts: code and Ref Guide read

Ticket: SOLR-18546, "Simplify our getting started tutorial by using the collection name techproducts instead of gettingstarted" (Open, Minor, reporter Eric Pugh, created 2026-10-21). One comment by Eric (2026-10-21): positive feedback from the dev list (thread linked in the comment).
Pinned main: `e59ce3c97ddb`.

## What uses the name

- Ref Guide: `gettingstarted` appears on 177 lines in 36 files (`git grep -c` over `solr/solr-ref-guide`). The largest are `indexing-guide/pages/post-command.adoc` (21), `deployment-guide/pages/solr-control-script-reference.adoc` (19), `deployment-guide/pages/solr-in-docker.adoc` (13), `indexing-guide/pages/indexing-with-tika.adoc` (12), and `query-guide/pages/stream-api.adoc` (11).
- CLI, which is user-visible behaviour:
  - `RunExampleTool.java:1188`: `String collectionName = "gettingstarted";` for `bin/solr start -e cloud`.
  - `RunExampleTool.java:607`: the schemaless example also maps to `gettingstarted`.
  - Help text and footers in `PostTool.java:95-101`, `HealthcheckTool.java:61`, and the `RunExampleTool.java:110` and `:327` usage strings.
- Tests: `solr/core/src/test/org/apache/solr/cli/SolrCloudExampleTest.java:57` (the cloud example's collection) and `solr/solrj/src/test/org/apache/solr/client/solrj/impl/CloudSolrClientCacheTest.java:69` and `:142`.

## Reading

- A docs-only change (the Ref Guide examples) does not change any running behaviour, and it can be checked with a grep.
- Changing the CLI default collection name for `bin/solr -e cloud` changes what users get after they run the command. Scripts that post to `gettingstarted` after `-e cloud` would break. That is a user-visible change, so it is a decision for the maintainers, and the dev-list feedback is the stated intent for the docs, not necessarily for the CLI default.
- The tests named above assert the name, so a CLI change needs them updated in the same change.

## Verdict

Concrete. The two parts differ in risk. The docs part is safe to propose. The CLI default is a decision for the owner. No edit is made here. The draft asks which part Eric means.

## Not checked

- Whether the dev-list thread (linked in Eric's comment) mentions the CLI default. The thread was not fetched.
- Whether `techproducts` as a collection name collides with the `techproducts` example configset in `RunExampleTool`.
