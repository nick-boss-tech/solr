# Gate job: SOLR-17297 module SPI orphaning, follow-up round 1 (vm5)

Status: PROBE CONFIRMED at c0ab38fc0a; FIX GATE pending

- Claim: `claims/pool-solr-17297-spi-followup.md`. Assignment: `assignments/pool-solr-17297-spi-orphaning-followup-round-1.md`.
- Base: `solr-17297-submit` at `c0ab38fc0a` (unchanged). Fix branch: `solr-17297-spi-followup`.
- Host: vm5. Logs: `/workspace/gates/logs/vm5/solr-17297-probe2/` (probe), `/workspace/gates/logs/vm5/solr-17297-fix/` (fix).

## Step 1: probe (rung 2), at c0ab38fc0a

Test `TestCoreContainer.testModuleSpiCanStillLoadItsClassesWhenSharedLibIsAdded`: a module jar holds a PostingsFormat SPI and the helper class it loads on first use; `lib/` holds one dummy jar, so the shared-lib step adds URLs and replaces the class loader. `PostingsFormat.forName` returns the SPI instance created in the module stage; `fieldsProducer` then loads the helper.

Result: FAILED. `expected UnsupportedOperationException but was IllegalStateException`, caused by `ClassNotFoundException: org.apache.solr.core.test17297.module.ModuleHelper`. The sibling test `testSharedLibSpiCanStillLoadItsClassesWhenModulesAreEnabled` passes in the same run.

Cause: `SolrResourceLoader.addURLsToClassLoader` closes the old `URLClassLoader` when it creates the replacement. Lucene's SPI registry is JVM-wide and keeps the instance it already has, so the module-stage SPI keeps the closed loader.

## Step 2: fix (rung 4, recorded before the change)

Choice: a replaced class loader stays open until `SolrResourceLoader.close()`; it is then closed with the current one. Reason: the instances from the old loader remain reachable through Lucene's SPI registry, and the replacement loader cannot serve their classes. Rejected: a single combined add of module and shared-lib URLs (would fix node startup only; `SolrConfig` also adds lib jars per core, so the same close can still happen later); and the leak-free alternative of closing on shutdown without a list (same thing, written differently). The cost is a few open jar handles per replacement until shutdown; replacements happen at startup and at core load only.

Changed files: `SolrResourceLoader.java` (list of replaced loaders; no close on replacement; close all on `close()`), `TestCoreContainer.java` (the probe test stays as the regression test).

## Gate (fix)

Pending, see the status file in the fix log directory.
