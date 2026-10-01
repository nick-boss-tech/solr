# SOLR-17791 — Testing handoff

**Status: UNCOMPILED AND UNTESTED.** Written without running Gradle
(no compile, no tests). A reviewer must compile and test before this goes
anywhere near a PR.

## What the patch does

`PUT /schema/feature-store/store1` with a feature body lacking a `"store"`
attribute silently wrote the features into the `_DEFAULT_` store instead of
`store1`: `RestManager.ManagedEndpoint` extracted the child id from the URL
for GET/DELETE but dropped it on the PUT/POST path, and
`ManagedFeatureStore.applyUpdatesToManagedData` only read the store name from
the body.

The patch:

- `RestManager.java` — `delegateRequestToManagedResource` now passes `childId`
  to new child-aware `doPut`/`doPost` overloads.
- `ManagedResource.java` — adds `doPut(endpoint, json, childId)` /
  `doPost(endpoint, json, childId)` overloads whose defaults delegate to the
  existing 2-arg forms, so every other managed resource is behaviorally
  untouched (existing 2-arg overrides keep working).
- `ManagedFeatureStore.java` — overrides the 3-arg `doPut` to default the
  `"store"` attribute to the URL child id (via `putIfAbsent`, so an explicit
  body `"store"` keeps precedence) for each feature map, recursing into
  `initArgs`-envelope payloads (`managedList`/`managedMap`). Doc note added in
  `learning-to-rank.adoc`.

Files changed:
- `solr/core/src/java/org/apache/solr/rest/RestManager.java`
- `solr/core/src/java/org/apache/solr/rest/ManagedResource.java`
- `solr/modules/ltr/src/java/org/apache/solr/ltr/store/rest/ManagedFeatureStore.java`
- `solr/solr-ref-guide/modules/query-guide/pages/learning-to-rank.adoc`

## Recommended reviewer commands

```bash
cp ~/workspace/solr/gradle.properties .   # worktrees don't inherit it
~/workspace/tools/solr-gradle.sh :solr:modules:ltr:spotlessApply :solr:core:spotlessApply
~/workspace/tools/solr-gradle.sh :solr:core:compileJava :solr:modules:ltr:compileJava -Pvalidation.errorprone=true
```

Suggested tests (not written):

1. `ManagedFeatureStore`: PUT-style update with childId `store1` and a body
   lacking `"store"` → feature retrievable via `getFeatureStore("store1")`,
   `_DEFAULT_` empty.
2. Regression: body `"store": "other"` still wins over the childId.
3. List payload and `{"managedList": [...]}` envelope payload with childId.
4. Other managed resources (e.g. schema, stopwords): PUT/POST behavior
   unchanged via the default overloads.

## Patch limits, risks, open questions

- **Not compiled or tested.**
- `ManagedModelStore` (`PUT /schema/model-store/<name>`) was NOT changed —
  check whether it has the analogous child-id drop; out of scope for this
  ticket but worth a follow-up look.
- The `withDefaultStore` helper mutates the parsed request-body maps in place
  (they are freshly parsed per request, so no aliasing concern), adding an
  explicit `"store"` key. This also means the persisted managed data carries
  the resolved store name, consistent with body-specified stores on reload via
  `onManagedDataLoadedFromStorage`.
- No changelog entry (repo convention: scaffold once a Jira/PR is assigned).
- Remove this file before opening the upstream PR.
