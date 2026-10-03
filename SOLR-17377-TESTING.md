# SOLR-17377 — Testing handoff

**Status: UNCOMPILED AND UNTESTED.** Written without running Gradle
(no compile, no tests). A reviewer must compile and test before this goes
anywhere near a PR.

## Round 4 review outcome (read this first)

See `research/branch-reviews/round-4/SOLR-17377-review.md`. No code was changed
in this round: the fix needs a design decision first.

- **The patch breaks an existing test.**
  `TestSolrXml.testFailAtConfigParseTimeWhenClusterSingletonClassNotFound`
  expects a `SolrException` (" Error loading class 'class.not.found.Class'") at
  parse time; the patch skips unloadable classes, so that test fails. Running
  `TestSolrXml` is queued to confirm. Either keep fail-fast for genuinely
  unknown classes, or change the test deliberately and say why.
- **The deferred check is not a check.** The `instanceof ClusterSingleton`
  guard in `ClusterSingletons.added` silently ignores a non-singleton; it does
  not raise the "must implement the interface" error. A loadable but wrong-typed
  module class would be silently ignored.
- **A typo'd class no longer stops startup.** In `ContainerPluginsRegistry`
  a plugin class that fails to load is added to `errs` and the entry is skipped
  with `log.error(...)`; startup continues without the singleton. The "log
  warn" and "fails loudly either way" statements below are wrong.
- Candidate designs: check the interface where the full class loader exists
  (plugin registration) and fail there; or keep the early check but only for
  classes the parse-time loader can see.

No test, changelog or code change was added, because each depends on the
decision above.

## What the patch does

Since SOLR-17096, custom cluster singletons can be declared via a
`<clusterSingleton>` section in solr.xml. `SolrXmlConfig.getClusterSingletonPluginInfos()`
ran an early interface check (`loader.findClass(p.className,
ClusterSingleton.class)`, catching only `ClassCastException`) — but at
solr.xml parse time the `SolrResourceLoader` does not yet have the extended
class loader covering modules/plugins, so any module-provided class failed
config parsing with `SolrException: Error loading class ...` caused by
`ClassNotFoundException`.

Fix: per-plugin try/catch around the early check. A class that loads but does
not implement `ClusterSingleton` still fails fast with the same "must
implement the interface" error. A class that cannot be loaded at all
(`SolrException` caused by `ClassNotFoundException`) now skips the early
check with a debug log, deferring validation to the existing `instanceof
ClusterSingleton` guard in `ClusterSingletons` when the plugin is actually
instantiated from the full class loader. Other `SolrException`s are
re-thrown unchanged.

File changed:
- `solr/core/src/java/org/apache/solr/core/SolrXmlConfig.java`
  (`getClusterSingletonPluginInfos`, forEach → loop with per-plugin catch)

## Recommended reviewer commands

```bash
cp ~/workspace/solr/gradle.properties .   # worktrees don't inherit it
~/workspace/tools/solr-gradle.sh :solr:core:compileJava -Pvalidation.errorprone=true
```

Suggested tests (not written):

1. Parse a solr.xml (via `SolrXmlConfig.fromFile`) containing a
   `clusterSingleton` naming a class that only exists in a module jar →
   expect no error at config-parse time.
2. A loadable-but-wrong class (does not implement `ClusterSingleton`) →
   still raises the "must implement the interface" `SolrException`.
3. End-to-end: module-provided singleton declared in solr.xml starts (the
   reporter's own verification was commenting out the check).

## Patch limits and follow-ups

- **Not compiled or tested.**
- A mis-named (typo'd) class now fails later — at plugin instantiation in
  `ClusterSingletons` (log warn) rather than at config-parse time. Fails
  loudly either way, but the timing/message differ.
- No changelog entry (repo convention: scaffold once a Jira/PR is assigned).
- Remove this file before opening the upstream PR.
