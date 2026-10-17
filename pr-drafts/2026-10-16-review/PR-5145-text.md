# PR #5145 draft: SOLR-8240 overstated sentence, default recommendation, trim (review item 5)

- Written against head: 78ecddf01f5c76736d415b9d031095f22aa38b17 (live branch solr-8240-submit, read through the API 2026-10-17).
- Visible characters: 3,461 (body; markdown syntax and link URLs removed). Limit 3,500.
- Proof numbers: the body's Proof section, which cites JsonLoaderTest 33 of 33 and TestInitParams 8 of 8 at 78ecddf01f5c, verified 2026-10-15. Confirmed against receipts/SOLR-8240.md (fresh JUnit XML: JsonLoaderTest 33, TestInitParams 8). This draft did not re-run them.
- Guide facts checked at 78ecddf01f5c: the "Setting JSON Defaults" section recommends `mapUniqueKeyOnly` as a default (page line 856 onward). The `f=txt:/**` examples are in three tabs (lines 586, 613, 640). The sample's `_UPDATE_JSON_DOCS` defaults in params.json apply to `/update/json/docs` only, so only the V1 tab changes; the V2 tabs post to `/update/json`.
- Not applied to the live PR. Drafts only, per the assignment.

=== TITLE ===

(unchanged) SOLR-8240: A request that sends `f` with `mapUniqueKeyOnly=true` now fails with a 400 error

=== BODY ===

🤖 *AI text below* 🤖 *(posted on behalf of Nick Shanin)*

https://issues.apache.org/jira/browse/SOLR-8240

## What happens today

**A request to `/update/json/docs` that sets `mapUniqueKeyOnly=true` and also sends field mappings (`f`) succeeds, and the names in `f` are dropped.**

With `mapUniqueKeyOnly=true`, each record keeps only its uniqueKey and the `srcField` copy. Every other mapped value goes into the default search field (`df`) with no field name ([pre-change code at the merge-base](https://github.com/nick-boss-tech/solr/blob/e432df19c4a50b9d54d7fba545397b859cc54f98/solr/core/src/java/org/apache/solr/handler/loader/JsonLoader.java#L295-L327)). A request with `f=title:/title` puts the title text into `df` and returns success. The name `title` never reaches the index.

## What this change does

**A request that sends `f` with `mapUniqueKeyOnly=true` now fails with a 400 error.**

The check runs first in `handleSplitMode`, before any record is read ([check](https://github.com/nick-boss-tech/solr/blob/78ecddf01f5c76736d415b9d031095f22aa38b17/solr/core/src/java/org/apache/solr/handler/loader/JsonLoader.java#L249-L255)). The error text tells the caller to send `mapUniqueKeyOnly=false` to map fields. This change adds one sentence to the reference guide's `mapUniqueKeyOnly` section ([guide](https://github.com/nick-boss-tech/solr/blob/78ecddf01f5c76736d415b9d031095f22aa38b17/solr/solr-ref-guide/modules/indexing-guide/pages/transforming-and-indexing-custom-json.adoc#L68)).

The check reads the effective value, so an inherited value counts. A params set or `initParams` that sets `mapUniqueKeyOnly=true` makes each request that sends `f` fail, unless the request sends `mapUniqueKeyOnly=false`. The techproducts sample sets it for `/update/json/docs` ([params.json](https://github.com/nick-boss-tech/solr/blob/78ecddf01f5c76736d415b9d031095f22aa38b17/solr/server/solr/configsets/sample_techproducts_configs/conf/params.json#L3-L5)). Until [SOLR-10424](https://issues.apache.org/jira/browse/SOLR-10424) removes that default, `f` requests to the sample fail with this error. The sample needs SOLR-10424 first, so this change should land after it, or with it.

## Proof

**JsonLoaderTest passes 33 of 33 and TestInitParams passes 8 of 8 at `78ecddf01f5c`. The new tests fail on the code before this change.**

- `JsonLoaderTest.testFieldMappingWithMapUniqueKeyOnlyIsRejected` ([test](https://github.com/nick-boss-tech/solr/blob/78ecddf01f5c76736d415b9d031095f22aa38b17/solr/core/src/test/org/apache/solr/handler/JsonLoaderTest.java#L192-L214)) sends `f=title:/title` with `mapUniqueKeyOnly=true`. It expects a 400 whose message names `mapUniqueKeyOnly`.
- `TestInitParams.testUpdateJsonDocsInitParamsRejectsFieldMappings` ([test](https://github.com/nick-boss-tech/solr/blob/78ecddf01f5c76736d415b9d031095f22aa38b17/solr/core/src/test/org/apache/solr/core/TestInitParams.java#L137-L158)) covers the inherited case. With `mapUniqueKeyOnly=true` set in `initParams`, a request that sends `f` fails with the same 400. A request that sends `mapUniqueKeyOnly=false` succeeds with the fields mapped.
- On the code before this change, the test fails with "Expected exception SolrException but no exception was thrown".

## A choice to check

**The request now fails, where the old code let it succeed and drop the names.**

The options are:

1. Return a 400 when `f` and `mapUniqueKeyOnly=true` both apply. This is the change here. The ticket proposes this error.
2. Accept the request and report the unused `f` in the response or the log. Clients keep working, but the names are still dropped.

Option 1 costs a new error for clients that send both, or inherit the value. Option 2 keeps those requests working, but the mismatch stays a warning.

Was the 400 the right call?

## Limits

- The tests cover a request that sets the value itself, an inherited `initParams` default, and a request that overrides that default with `mapUniqueKeyOnly=false`.
- The "Setting JSON Defaults" section of the same guide page still recommends `mapUniqueKeyOnly` as a default. Do not use that default for any request that sends `f` until SOLR-10424 lands. This change does not edit that section.
- Only the V1 `/update/json/docs` example in the wildcard `f=txt:/**` section changes to a 400 on the sample. The two V2 examples post to `/update/json`, which the sample does not set, so they are unchanged.

Changelog: [`changelog/unreleased/SOLR-8240-map-unique-key-only-f-conflict.yml`](https://github.com/nick-boss-tech/solr/blob/78ecddf01f5c76736d415b9d031095f22aa38b17/changelog/unreleased/SOLR-8240-map-unique-key-only-f-conflict.yml)

### AI assistance

AI agents assisted with research, implementation, review, and drafting. Nick Shanin directed the work and takes responsibility for this contribution.
