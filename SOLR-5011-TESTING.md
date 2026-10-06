# SOLR-5011 - hypothetical reproduction (nothing was compiled or run)

JIRA (Uwe): close all `SolrResourceLoader`s when cores are unloaded or reloaded, so the class loader and the jar files
of `<lib>` directories are released (needed on Windows to delete jars). Audit note said "fix version set 4.9/6.0",
which is Uwe's bulk version move. On `upstream/main`: `SolrResourceLoader` is `Closeable` and `close()` closes its
`URLClassLoader`, but the only callers are `CoreContainer` (the node loader) and tests. `SolrCore.doClose()` never
closes `resourceLoader`.

## Change
`SolrCore.doClose()` closes `resourceLoader` after the post-close hooks (null-guarded, errors logged like the other
steps). Why it is safe: `ConfigSetService.loadConfigSet` builds a fresh core loader per load
(`createCoreResourceLoader`), including on reload; the loader's class loader is always its own `URLClassLoader` whose
parent (the container loader) is not closed by it.
`SolrResourceLoader` gets a `closed` flag and a package-private `isClosed()` for the test.

## Test (guessed)
`CoreCloseResourceLoaderTest`: `h.reload()` keeps the old loader open while the test still holds a core reference,
closes it when that reference is released, and the reloaded core has a different, open loader.

## Guesses to verify first
- Nothing keeps using the old core's loader after the core is closed (late lazy class loading from a lib jar would fail
  with the closed `URLClassLoader`; classes from the parent still load).
- `ConfigSet` is not shared between two live cores (tests that build a `SolrCore` directly from one `ConfigSet` twice).
- `TestHarness.reload()` keeps the old core open while an extra reference is held (the standard refcount contract).
- Cores that fail in their constructor keep their loader open (not handled, same as before).

## Fail-before
Without the change `isClosed()` stays false after `oldCore.close()`, so the test fails on the first `assertTrue`.
