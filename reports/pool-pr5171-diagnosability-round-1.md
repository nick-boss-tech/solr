# PR #5171 (SOLR-17752) corrupted-response warning review

Status: DONE

Reviewed PR #5171 at `041c5b0b2e44e903e7010a40530de3a24ea9bb06`, then applied and gated the leak fix on `solr-17752-submit` at `310e1d02ef15e136016bb66f09aaaef8f836dc1b`. Verdict: OPEN-READY.

## Warning fields and leak audit

The warning now reports:

- The responding node name, when present. `HttpShardHandler` copies the internal `ShardRequest.nodeName` onto the response ([HttpShardHandler.java#L219-L225](https://github.com/nick-boss-tech/solr/blob/310e1d02ef15e136016bb66f09aaaef8f836dc1b/solr/core/src/java/org/apache/solr/handler/component/HttpShardHandler.java#L219-L225)). If it is null, `getShardIdentifierForLogging` parses the shard address and returns only its host and port, or `unknown` ([SolrResponseUtil.java#L88-L107](https://github.com/nick-boss-tech/solr/blob/310e1d02ef15e136016bb66f09aaaef8f836dc1b/solr/core/src/java/org/apache/solr/util/SolrResponseUtil.java#L88-L107)). User-info, path and query are discarded by that fallback.
- The expected response-section name, response entry count, and integer shard-request purpose ([SolrResponseUtil.java#L56-L70](https://github.com/nick-boss-tech/solr/blob/310e1d02ef15e136016bb66f09aaaef8f836dc1b/solr/core/src/java/org/apache/solr/util/SolrResponseUtil.java#L56-L70)). The warning does not print response keys or values.

The production callsites use fixed section names or code-defined labels. The only generated suffix is `sort_values_<queryIndex>`, where the index is an internal integer loop counter ([CombinedQueryComponent.java#L328-L329](https://github.com/nick-boss-tech/solr/blob/310e1d02ef15e136016bb66f09aaaef8f836dc1b/solr/core/src/java/org/apache/solr/handler/component/CombinedQueryComponent.java#L328-L329), [#L392-L393](https://github.com/nick-boss-tech/solr/blob/310e1d02ef15e136016bb66f09aaaef8f836dc1b/solr/core/src/java/org/apache/solr/handler/component/CombinedQueryComponent.java#L392-L393)). Highlighting uses the code-defined `highlighting` label ([HighlightComponent.java#L253-L255](https://github.com/nick-boss-tech/solr/blob/310e1d02ef15e136016bb66f09aaaef8f836dc1b/solr/core/src/java/org/apache/solr/handler/component/HighlightComponent.java#L253-L255)). I found no request parameter or response key/value interpolated into the warning fields.

There is no fallback warning that logs the old full request or response. The exception path names the same section and safe shard identifier; its wrapper and cause are checked by the new test. The sensitive response-key and custom-URL cases both failed against the previous production code, with JUnit output showing the secret marker in the captured warning.

## Diagnostic value and loss

From the warning alone, an operator can identify the node or host and port, which expected section was absent, how many top-level entries arrived, and the shard-request purpose. That is enough to locate the affected component and response path without exposing request or response contents.

The warning no longer reveals which other top-level keys were present, their nesting or any values. Those details may have no other safe log source. This is a real diagnostic tradeoff: a response key can itself carry customer data, so retaining arbitrary names would keep the leak. The warning keeps the entry count and the known missing-section name instead.

The tests construct `ShardResponse` objects directly. They verify the warning and wrapper for a query string in request/header data, a sensitive response key, and credentials/query text in a custom shard URL, including a null-node fallback ([TestShardResponseLogging.java#L34-L80](https://github.com/nick-boss-tech/solr/blob/310e1d02ef15e136016bb66f09aaaef8f836dc1b/solr/core/src/test/org/apache/solr/handler/component/TestShardResponseLogging.java#L34-L80), [#L83-L147](https://github.com/nick-boss-tech/solr/blob/310e1d02ef15e136016bb66f09aaaef8f836dc1b/solr/core/src/test/org/apache/solr/handler/component/TestShardResponseLogging.java#L83-L147)). They do not run a live cluster with `echoParams` enabled. Other request logging, such as `SuggestComponent` at INFO, remains out of scope ([SuggestComponent.java#L181](https://github.com/nick-boss-tech/solr/blob/56ec140e3636d5f4150fa87fbf7103529536ac99/solr/core/src/java/org/apache/solr/handler/component/SuggestComponent.java#L181)).

## Test evidence

Gate runner: `/workspace/tools/phone-agent-2-hardening-gate-v2.py`. Retry logs: `/workspace/phone-agent-2-logs/gate-17752-diagnosability-round-1-retry`. The first attempt stopped after tidy requested formatting-only changes; those were committed as `310e1d02ef15e136016bb66f09aaaef8f836dc1b`, with repaired tree `e4b07086da31a5224314a12dedd87e0d57ad7335`. The full retry then passed on that pushed head.

- Changelog YAML parse: passed.
- Tidy: passed clean on the repaired head.
- Error Prone compile (`:solr:core:compileJava :solr:core:compileTestJava -Pvalidation.errorprone=true`): passed.
- Base proof: restored only `SolrResponseUtil.java` from `041c5b0b2e44e903e7010a40530de3a24ea9bb06`, kept the new tests, and ran `TestShardResponseLogging`: 2 tests, 0 skipped, 2 failures, 0 errors. The JUnit failures included `sensitive query/key value reached warning` and `sensitive shard URL reached warning`, with captured warning text containing the response key in one case and URL credentials/query in the other.
- Fixed head: `TestShardResponseLogging` 2/2; `DistributedTermsComponentTest` 1/1; `DistributedExpandComponentTest` 1/1; `DistributedSpellCheckComponentTest` 1/1; `TestDistributedGrouping` 2/2. Total 7 tests, 0 skipped, 0 failures, 0 errors, counted from fresh JUnit XML.
- `:solr:core:check -x test`: passed.

## PR update and verdict

Updated the existing [PR #5171 description](https://github.com/apache/solr/pull/5171) on 2026-10-23 to match the current behavior, exact base proof, head, test counts and limits. Its title and open state were unchanged. No comment or Jira entry was posted.

Verdict: OPEN-READY at `310e1d02ef15e136016bb66f09aaaef8f836dc1b`. The branch gate is green and the description now matches the tested code. The remaining diagnostic loss is stated plainly for maintainers to weigh.
