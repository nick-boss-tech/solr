# SOLR-17708 - hypothetical-reproduction handoff

**Read this first: the fix and test on this branch were written without being compiled or run.** The audit pipeline has no Gradle access. Treat both as hypotheses. This change touches authorization, so it deserves a careful human review before anything is proposed upstream.

- JIRA: https://issues.apache.org/jira/browse/SOLR-17708 - "JAX-RS v2 APIs go through authorization twice" (Jason Gerlowski, 2025; he also sketched the fix in a comment: override `HttpSolrCall.shouldAuthorize` in `V2HttpCall`). The old skip note was "maintainer-filed; hold for coordination per protocol". Reopened in audit round audit-1 (Tier 2 batch 1) as a hypothetical branch only; nothing was posted.
- Branch: `solr-17708-submit` off `apache/solr` main `9b3a84b1c46`

## The bug, as understood
`HttpSolrCall.call()` authorizes every request (`shouldAuthorize()` and a non-proxy `action`). JAX-RS resources are authorized a second time by the `SolrRequestAuthorizer` Jersey filter, registered in `CoreContainerApp` (and inherited by `SolrCoreApp`) after a resource matched, before the method runs. Authorization (and the audit events it emits) therefore happens twice per JAX-RS request.

## Design choice and why
`V2HttpCall` cannot ask Jersey up front whether a resource will match, but its own routing already tells it: `handleAdmin` and `executeCoreRequest` send a request to Jersey exactly when `api == null`. So `shouldAuthorize()` is overridden to return `false` when `api == null` and the action is `ADMIN` or `PROCESS`. Everything else (native `Api`s, `ADMIN_OR_REMOTEPROXY`, the root path, static files) is unchanged.

- Equivalence of the second check: the filter uses the same `CoreContainer` authorization plugin, the same PKI shortcut (`isAlreadyAuthorizedByPKI` mirrors `shouldAuthorize`), the same collection list (`getAuthorizationCollectionsList`) and the resource method's `@PermissionName`.
- `ADMIN_OR_REMOTEPROXY` is deliberately left alone: there Jersey is only a probe and a miss becomes a remote proxy, so skipping the local check would change who authorizes. This leaves double authorization for those requests when Jersey matches; a follow-up could handle it.
- Trade-off to review: an `api == null` v2 request that Jersey cannot match (404) is no longer authorized locally; it returns 404 instead of 401/403. Authentication still happens earlier in the dispatch filter.

## What the branch changes
- `HttpSolrCall.shouldAuthorize()`: `private` → `protected`.
- `V2HttpCall`: override described above.
- Test: `org.apache.solr.security.JaxRsSingleAuthorizationTest` starts a one-node cluster with `MockAuthorizationPlugin`, counts authorizations whose resource ends with `/cluster/nodes` (a JAX-RS endpoint, `ListClusterNodesApi`) and asserts exactly one for a v2 GET.

## Guesses to verify first
1. `context.getResource()` for a v2 request ends with `/cluster/nodes` (it may carry the `/____v2` prefix; `endsWith` is used on purpose).
2. `MockAuthorizationPlugin.predicate` is package-private static (hence the test's package) and the plugin from `security.json` is the one `CoreContainer` uses in `MiniSolrCloudCluster`.
3. Existing tests that assert authorization failures for v2 JAX-RS endpoints (`BasicAuthIntegrationTest`, `TestRuleBasedAuthorizationPlugin`, v2 security tests) still see 401/403 from the Jersey filter rather than from `HttpSolrCall`; response bodies/headers could differ slightly (the filter aborts with `Response.status(code).entity(message)`).
4. Audit-logging tests (`AuditLoggerIntegrationTest`) may expect the previous number/shape of `AUTHORIZED` events for v2 calls.

## Verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.security.JaxRsSingleAuthorizationTest" --tests "org.apache.solr.security.AuditLoggerIntegrationTest" --tests "org.apache.solr.security.BasicAuthIntegrationTest"
```
Fail-before: remove the override in `V2HttpCall`; the new test should see 2 authorizations.

## Not done
No JIRA comment, no PR.
