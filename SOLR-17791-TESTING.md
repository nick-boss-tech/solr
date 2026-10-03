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

Tests added (round 4, not compiled or run), in `TestManagedFeatureStore`:
child id used as default store (list body, `_DEFAULT_` stays empty); explicit
body `store` still wins; POST with a single feature map and a `managedList`
envelope; no child id still uses `_DEFAULT_`. They call `doPut`/`doPost`
directly with a null endpoint, not through the REST layer.

Still untested: a real `PUT /schema/feature-store/<name>` request, and that a
`ManagedResource` subclass overriding the 2-arg `doPost` is still reached via
REST (see below).

## Round 4 review changes

See `research/branch-reviews/round-4/SOLR-17791-review.md`.

- The default 3-arg `ManagedResource.doPost(endpoint, json, childId)` now
  delegates to the 2-arg `doPost`, not straight to `doPut`, so resources that
  override the documented 2-arg extension point keep working.
  `ManagedFeatureStore` overrides the 3-arg `doPost` to reach its 3-arg `doPut`.
- Added `changelog/unreleased/SOLR-17791.yml`.
- Not changed (needs a decision): whether a behaviour change for callers that
  relied on `_DEFAULT_` needs an upgrade note in `major-changes-in-solr-X.adoc`;
  whether a smaller design (a child-id getter on `ManagedEndpoint`) is
  preferred over the two new public overloads.

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
