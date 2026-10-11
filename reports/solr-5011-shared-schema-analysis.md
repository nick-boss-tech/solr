# SOLR-5011 shared-schema loader analysis

## Verdict: UNSAFE

With `shareSchema` on and two cores on one `configSet`, the surviving core keeps the cached `IndexSchema` built by the first core. That schema holds the first core's `SolrResourceLoader`. The branch's `doClose` now closes that loader's lib `URLClassLoader`, so a later schema-API `add-field-type` on the survivor that names a lib-only class the loader has not yet loaded fails with a class-not-found error.

Scope: read-only. Gated head `3fd85bedef646b5b73c26e26a86a6130b5fc209f` (branch `solr-5011-submit`), fetched read-only (FETCH_HEAD and the remote-tracking ref only; no local branch change). The branch's production diff against merge-base `cabedd1d968059215188f4e7563fb303241899ed` touches only `SolrCore.java` (+13) and `SolrResourceLoader.java` (+7). Paths below are under `solr/core/src/java/org/apache/solr/` at the gated head unless marked otherwise.

Answer to the question: yes, but through the shared schema, not through a shared loader instance. Two cores never share a `SolrResourceLoader` instance. The schema is the only shared object that carries a loader reference across cores, and it carries the loader of the core that built it.

## 1. What doClose closes, and what the close changes

- `SolrCore.java:1722-1733`: `close()` decrements `refCount`; `doClose()` runs only at zero. Nothing else (the schema cache, the config set service, the surviving core) holds a count on the unloaded core, so nothing delays this close.
- `SolrCore.java:1739-1906`: `doClose` closes, in order, the circuit breakers, metrics, close hooks (pre), request handlers and plugin bags, the update handler, the searcher executor and searcher, the directory factory, close hooks (post), and finally its own `resourceLoader` (`SolrCore.java:1892-1903`, the close at `1895-1896`).
- `SolrResourceLoader.java:912-914`: `close()` sets `closed = true` and calls `IOUtils.close(classLoader)`.
- `SolrResourceLoader.java:139-141` and `917-919`: the `closed` field and the package-private `isClosed()`. Nothing reads `closed` except `isClosed()`, which only the branch test calls (`CoreCloseResourceLoaderTest.java:40-49`). No lookup checks it, so the observable flag changes nothing by itself.
- The real effect is on `classLoader`: a `URLClassLoader` created at `SolrResourceLoader.java:211` with the container loader as parent, into which SolrConfig adds the lib jars (`SolrConfig.java:931-932`).
- JDK `URLClassLoader.close()` Javadoc (external to this repo): after close the loader cannot load new classes or resources it defines. Classes already loaded, and classes or resources delegated to the parent, remain accessible.
- Still works after close: config-dir and instance-dir files through `openResource` (`SolrResourceLoader.java:356-368`, file system first), and Solr's own classes through parent delegation.
- Broken after close: a lib-dir class that was not yet loaded (`findClass` and `newInstance`, `SolrResourceLoader.java:475-541`, `604-625`), and the lib-dir resource fallback (`SolrResourceLoader.java:373-375`).
- The parent is not closed by this. `URLClassLoader.close()` closes only its own jars (JDK Javadoc). The parent is the container loader passed at `FileSystemConfigSetService.java:80` and `ZkConfigSetService.java:86`.

## 2. How each core gets its loader: never shared

- `ConfigSetService.loadConfigSet` (`ConfigSetService.java:228-237`) calls `createCoreResourceLoader` (abstract at `:364`) on every call, so every config set load produces a new loader.
- File system: `FileSystemConfigSetService.java:77-82` builds a new `SolrResourceLoader` each call. ZooKeeper: `ZkConfigSetService.java:74-87` builds a new `ZkSolrResourceLoader` each call.
- Callers: `CoreContainer.java:1674`, `1741` (create), and `1964` (reload).
- `SolrCore` takes its loader from its own config set: `SolrCore.java:1049-1051` (`configSet.getSolrConfig().getResourceLoader()`, then `setSolrCore`).
- Result: a core's own loader (call it LB) is never shared with another core, and A's `doClose` never closes B's own loader. Sharing happens only through the schema (next section).

## 3. shareSchema: the cached schema carries the origin loader

- Gating: sharing needs `configSet=` in `core.properties` (`CoreDescriptor.java:60`, `366-373`, used at `ConfigSetService.java:303`) and `shareSchema` on. `shareSchema` defaults to false (`SolrXmlConfig.java:364-365`). `ConfigSetService.java:267-270` creates the schema cache only when shareSchema is on. Test configs: `solr/core/src/test-files/solr/solr.xml:26` is `${shareSchema:false}`; `solr-50-all.xml:27` is `${shareSchema:true}`; `TestCoreContainer.java:99` sets it to true.
- `ConfigSetService.createIndexSchema` (`:292-330`): if `configSet != null && schemaCache != null` (`:304`), the key is `configSet/schemaName/modVersion/luceneMatchVersion` (`:309-316`), and `schemaCache.get(key, create)` (`:317-319`). The cache is Caffeine `weakValues()` (`:269`), so an entry lives while a `ConfigSet` or core strongly holds the schema. The miss path builds with the calling core's `SolrConfig` (`:329`).
- Who builds the schema: `ManagedIndexSchemaFactory.java:275-283` (default factory, see `IndexSchemaFactory.java:73-77`) constructs `ManagedIndexSchema(config, ...)`. `ManagedIndexSchema.java:110-115` passes `solrConfig.getResourceLoader()` to `IndexSchema`. `IndexSchema.java:127` stores it as `loader`, and `IndexSchema.java:203-204` sets `solrClassLoader = loader.getSchemaLoader() == null ? loader : loader.getSchemaLoader()`. At schema build time `getSchemaLoader()` returns null because the loader has no `CoreContainer` yet (`SolrResourceLoader.java:161-166`, `977-982`; `setSolrCore` comes later at `SolrCore.java:1051`). So the schema's class loads go to the origin loader itself. Even if it were the `PackageListeningClassLoader`, its non-package path falls back to the same loader (`PackageListeningClassLoader.java:76-77`, `149-150`).
- Timing: `ConfigSet` builds its schema eagerly (`ConfigSet.java:46`, getters at `61-68`). `SolrCore` reads it at `SolrCore.java:1052` and sets it at `1077`.
- Which loader: the loader of the core whose config built the cache entry, normally the first core of that config set created on the node. That loader stays referenced by the schema (`IndexSchema.java:127`) for as long as any survivor holds the schema, even after its origin core is gone.
- The cache does not hold the core. Only the core's refCount drives its close, so the schema can outlive the origin's loader close for as long as a survivor's `ConfigSet` holds the schema.

## 4. Later operations by the survivor that reach the retained loader

Unsafe path, each link read at the gated head:

1. Survivor B and origin A share `configSet` with shareSchema on. B's latest schema S is A's cached schema, with `S.loader` = LA (A's loader).
2. A is unloaded: `SolrCore.java:1895-1896` calls `resourceLoader.close()`, which is `SolrResourceLoader.java:912-914`. LA's lib `URLClassLoader` is closed. B is still live.
3. B gets `POST /schema` with `add-field-type` (standalone). `SchemaManager.performOperations` (`SchemaManager.java:98-104`) accepts it because B's latest schema is a mutable `ManagedIndexSchema` (mutable defaults to true, `ManagedIndexSchemaFactory.java:61`, `85`). In standalone mode `getFreshManagedSchema` returns the latest schema (`SchemaManager.java:555-556`), so the op runs on S. `ADD_FIELD_TYPE` calls `mgr.managedIndexSchema.newFieldType(...)` (`SchemaManager.java:245-246`).
4. `ManagedIndexSchema.newFieldType` (`ManagedIndexSchema.java:1365-1385`) builds a `FieldTypePluginLoader` on S and calls `loadSingle(solrClassLoader, ...)` (`:1383-1385`). That `solrClassLoader` is S's field, which is LA (`IndexSchema.java:204`).
5. `FieldTypePluginLoader.create` calls `loader.newInstance(className, FieldType.class)` (`FieldTypePluginLoader.java:71-74`). Analyzer, tokenizer and filter classes use `schema.getSolrClassLoader()` (`:181`) and then `loader.findClass` (`:211`) or `loader.newInstance` (`:267`, `331`, `402`).
6. `SolrResourceLoader.newInstance` and `findClass` (`SolrResourceLoader.java:475-541`, `604-625`) call `Class.forName(name, true, classLoader)` (`:541`, `:551`) against the closed LA.classLoader. The class is not found, and `findClass` throws `SolrException` "Error loading class" (`:557-558`).
7. `SchemaManager` turns the exception into an op error (`SchemaManager.java:249-251`). The request returns an error, and it does not persist or reload (`SchemaManager.java:131`).

Scope of the failure: it hits only a class that LA has not already loaded, and only one that is not on Solr's parent classpath. Built-in `solr.*` classes still resolve through the parent. A lib class that the origin already loaded still resolves, because `loadClass` returns the cached `Class` first. The jar may still be visible to B's own loader LB, which is the point: the schema API uses the origin's loader.

This is a regression introduced by the branch. Before the branch, LA.classLoader stayed open.

Other paths reaching LA after the origin closes (same root, weaker):

- Reload of the origin with an unchanged schema file. `CoreContainer.reload` loads a new config set (`CoreContainer.java:1964`), builds the new core (`:1971`, `SolrCore.java:775-795`), then `registerCore` puts it in place and closes the old core (`CoreContainer.java:1456-1472`, close at `:1472`). The cache hit (`ConfigSetService.java:306-319`, same key) gives the new core S with LA. The reloaded core is then a survivor holding a closed-loader schema. If the schema file changed, the key changes and the new core gets a fresh schema with its own loader, so this case does not apply.
- A fresh core on the same config set after the origin closed: the same cache hit gives S with LA (`ConfigSetService.java:317-319`).
- `OpenExchangeRatesOrgProvider` with a local `ratesFileLocation`: `getExchangeRate` (`OpenExchangeRatesOrgProvider.java:93`) calls `reloadIfExpired` (`:115-119`), then `reload` (`:148`), then `resourceLoader.openResource` (`:155`). The schema's `resourceLoader` is LA, and the lib fallback at `SolrResourceLoader.java:373-375` is the only part that touches the closed loader. This fails only when the file is found in a lib jar and not in conf or the instance dir. Refresh default is 1440 minutes, minimum 60 (`OpenExchangeRatesOrgProvider.java:62`, `:181-185`).

Checked and not on the request path:

- `GetSchemaField.java:234` (`insertPackageInfo`) only reads `PackageListeningClassLoader.getPackageVersion`, a map lookup with no class load.
- Init-time schema loads: `CurrencyFieldType.java:124-128`, `EnumFieldType.java:72` and `123-126`, `IndexSchema.java:523`, `533`, `1074`. They run once while a schema is built. They matter only when a new field type of such a class is added, which is the path above.

## 5. Lifecycle: nothing delays the origin's close

- Close needs only refCount zero (`SolrCore.java:1722-1733`). The cached schema is weak in the cache (`ConfigSetService.java:269`), and the config set service holds no refCount on any core. Survivor B does not hold A's refCount.
- So A's loader closes while B is live, as soon as A's last reference is released, whether or not B still uses the schema.

## 6. Reload and unload ordering

- Unload of the origin while a survivor holds its schema: unsafe (section 4).
- Reload of the origin with an unchanged schema file: the reloaded core is unsafe (section 4, reload bullet). The old core closes inside `registerCore` (`CoreContainer.java:1472`), after the new core already holds the cached schema.
- Can the survivor get a fresh loader after the origin closes? Its own core loader always: each config set load builds a new loader (section 2), and that loader is open. Its schema only on a cache miss (modVersion change, luceneMatchVersion change, or an evicted weak entry with no holder). A miss builds the schema with the survivor's own `SolrConfig` loader (`ConfigSetService.java:329`), which is open. A hit returns the closed-loader schema.
- The unsafe case requires that the unloaded core is the origin of the cached schema. If the other core is unloaded instead, the survivor's schema keeps the open origin loader, and the survivor is safe on this path.

## Verdict and failure path

UNSAFE. Origin A unloads, `SolrCore.doClose` closes LA (`SolrCore.java:1895-1896`, `SolrResourceLoader.java:912-914`). Survivor B's shared schema S still uses LA.classLoader for schema-scoped loads (`IndexSchema.java:127`, `203-204`). B's `POST /schema add-field-type` naming a lib-only class not yet loaded fails (`SchemaManager.java:245-251`, `ManagedIndexSchema.java:1385`, `FieldTypePluginLoader.java:74`, `181`, `211`, `SolrResourceLoader.java:541`, `551`, `557`).

## Recommended narrowing

Smallest change that keeps the SOLR-5011 leak fix for the default config:

- In `SolrCore.doClose` (`SolrCore.java:1892-1903`), skip `resourceLoader.close()` when the node uses a schema cache. Add a method to `ConfigSetService` that returns `schemaCache != null` (`ConfigSetService.java:259`, `267-270`), and check it with a null guard on `coreContainer.getConfigSetService()`.
- Trade-off: with shareSchema on, the loader leaks again. That is the pre-branch behaviour for that config, and it should be recorded as a residual in the gate. With shareSchema off (the default, `SolrXmlConfig.java:364-365`), the close stays, and `CoreCloseResourceLoaderTest` (single core, default config) still exercises the same close.
- A complete fix that closes the loader when the last core using it goes needs a per-loader count in `ConfigSetService`. That is a larger change and is not the smallest narrowing.

Settling and narrowing test (not run; this is the test that would prove the narrowing):

- Setup: `solr.xml` with `shareSchema=true` (or `-DshareSchema=true` as `TestCoreContainer.java:99` does). Two `core.properties` with `configSet=S`. S uses a managed schema, and its solrconfig has `<lib dir=...>` pointing to a test jar that holds a custom FieldType or TokenFilterFactory class which the schema does not reference.
- Create core A first (origin), then core B (cache hit). Assert that B's latest schema is A's schema and that its loader is A's loader.
- Unload A through the container API while B stays open. Assert B still answers a query.
- Send `POST /schema` `add-field-type` on B naming the jar class.
  - Gated head expected result: the op fails with an error whose text carries the class-not-found load failure. Assert on that failure content, and assert that A's loader `isClosed()` is true.
  - Narrowed branch expected result: the op succeeds, and A's loader `isClosed()` is false while shareSchema is on.
- Reload case: with B live and the schema file unchanged, reload A. Gated head: the reloaded core's schema loader is closed and the add-field-type fails. Narrowed branch: the loader stays open and the op succeeds.
- Control: the same setup with shareSchema off. No sharing, so the loader closes on both heads and the add-field-type on B succeeds on both.

## Paths checked that did not change the verdict

- Core-own loader LB: never shared, never closed by A (section 2).
- Container (parent) loader: not closed by `URLClassLoader.close()` (`SolrResourceLoader.java:211`, `FileSystemConfigSetService.java:80`, `ZkConfigSetService.java:86`).
- Package-aware schema loader (`SolrResourceLoader.java:977-1000`, `PackageListeningClassLoader.java:73-84`, `147-157`): not closed. Its non-package path falls back to LA. No protection, and no new hazard.
- Package-listener reload callback (`SolrResourceLoader.java:991-997`): calls `coreContainer.getCore(coreName, coreId)`, which returns null for the closed origin, so it is a no-op for the survivor.
- ZooKeeper schema reader (`ManagedIndexSchemaFactory.java:540-547`, `ZkIndexSchemaReader.java:51-61`): the reader is tied to the origin and its close hook removes the listener. Cloud only. Not traced beyond this.
- Jersey app handler (`SolrCore.java:1110-1121`, `CoreContainer.java:211`): cached per config set id, shared across cores regardless of shareSchema. Its endpoint classes were loaded by the origin. I did not trace request-time lazy loads through it. The schema path alone settles the verdict.
- Lucene SPI (`SolrConfig.java:931-932`, `reloadLuceneSPI`): global maps populated at load. Not a per-core lazy path after close.
- Existing tests: the branch test is single-core (`CoreCloseResourceLoaderTest.java:36-56`). `TestCoreContainer` sets shareSchema=true (`:99`), but I did not check whether any of its cases unloads the origin and then loads a lib class. The gate's green run is not evidence either way.

## Limits

- Read-only. No build, test, or gate job. No branch edit, no commit, no push. One file written (this report).
- The claim `claims/pool-solr-5011-shared-schema-analysis.md` is not edited, because the write limit allows one file. The caller should mark it DONE with the report.
- The closed-`URLClassLoader` behaviour is from the JDK Javadoc, not from repo code. The narrowing test above would confirm it at runtime.
- Not traced: cloud ZooKeeper paths beyond the close hook, Jersey request-time loads, and the bodies of the collation and spatial init paths (`CollationField.java:86`, `161`; `AbstractSpatialFieldType.java:157`; `AbstractSpatialPrefixTreeFieldType.java:132`), which I only saw in a grep.
