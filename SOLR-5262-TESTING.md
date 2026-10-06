# SOLR-5262 - hypothetical reproduction (nothing was compiled or run)

JIRA (2013, Hoss): `CoreDescriptor.buildSubstitutableProperties` only exposes `solr.core.<prop>` for properties present in
`coreProperties`, so a config using `${solr.core.ulogDir}` fails at startup unless `ulogDir` is set explicitly in
`core.properties`. The audit note said "obsolete: coreProperties includes defaults (CoreDescriptor putAll(defaultProperties))".
True for `config`, `schema`, `configSetProperties`, `dataDir`, `loadOnStartup` (the `defaultProperties` map), but
`ulogDir`, the ticket's own example, has no entry there.

## What main does (read on `upstream/main`)
- `defaultProperties` = config, schema, configSetProperties, dataDir, loadOnStartup. `ulogDir` is absent; `getUlogDir()` returns
  null and `UpdateLog.resolveDataDir` / `SolrCore.deleteUnloadedCore` read null as "use dataDir".
- `buildSubstitutableProperties` iterates `coreProperties` only (plus `solr.core.instanceDir`), so `${solr.core.ulogDir}` stays
  unresolved (a missing-property error) for any core that does not set `ulogDir`.
- Other standard names without a default (`collection`, `shard`, `coreNodeName`, `configSet`, `properties`) have no derivable value
  and are not touched.

## Change
`buildSubstitutableProperties` adds `solr.core.ulogDir` with the core's `dataDir` value when `ulogDir` is not configured
(`putIfAbsent`). The stored `coreProperties` are unchanged (`getUlogDir()` still null), so the update log and unload cleanup behave as before.
New `TestCoreDescriptorImplicitProperties` (default, explicit value, stored property stays null).

## Guesses to verify first
- The "right" default is a pick: ulogDir is documented as the directory that holds `tlog/`, and with no `ulogDir` the tlog lives
  under `dataDir`, so `dataDir` (`data/`) is the closest value; Hoss's text mentions `tlog`.
- `PropertiesUtil` substitution happens through `getSubstitutableProperties()`; I did not trace every caller (SolrConfig,
  schema loading) beyond the accessor.
- `new CoreDescriptor(name, absoluteInstanceDir, Map, Properties, null)` works without a CoreContainer in a `SolrTestCase`.
- The ref guide mention Hoss asked for ("once resolved this page needs updating") was not changed.

## Fail-before
Expected: `testUlogDirDefaultsToDataDir` fails on `upstream/main` (`solr.core.ulogDir` is null).
