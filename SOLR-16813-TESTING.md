# SOLR-16813 — Testing handoff

**Status: UNCOMPILED AND UNTESTED.** Written without running Gradle
(no compile, no tests). A reviewer must compile and test before this goes
anywhere near a PR.

## What the patch does

When a new SolrCloud node joined a cluster with packages installed, the
package JARs were lazily fetched from other nodes, but `manifest.json` never
was: it is not in `version.files`, so `SolrPackageLoader.Version`'s
`FileStoreUtils.validateFiles` never touched it. Later,
`bin/solr package list-installed` → `PackageUtils.fetchManifest` →
`ClusterFileStore.getFile` threw `NOT_FOUND: not found in filestore`.

After the existing `validateFiles` block in the `Version` constructor, the
patch resolves `version.manifest` through
`coreContainer.getFileStore().getType(version.manifest, true)` — the same
lazy-fetch machinery the JARs use (fetched from a live node, sha512-verified,
persisted with its metadata companion). If the manifest still is not a file
afterwards, package load fails fast with
`"Cannot load package: manifest.json is not available in filestore: ..."`.

Notes for the reviewer:

- The manifest is deliberately NOT added to `version.files`:
  `validateFiles` demands trusted-keys signatures and the manifest is
  uploaded with a null signature (`RepositoryManager`), so it needs the
  fetch without the signature gate. Integrity is still covered by the
  sha512 check in `fetchFileFromNodeAndPersist`.
- `version.manifest == null` is tolerated (nothing to fetch); only a
  non-null but unresolvable manifest path fails the load.
- Standalone mode is unaffected: `getType` short-circuits on local
  existence before attempting any fetch.

Files changed:
- `solr/core/src/java/org/apache/solr/pkg/SolrPackageLoader.java`

## Recommended reviewer commands

```bash
cp ~/workspace/solr/gradle.properties .   # worktrees don't inherit it
~/workspace/tools/solr-gradle.sh :solr:core:compileJava -Pvalidation.errorprone=true
```

Suggested tests (not written):

1. SolrCloudTestCase: install a package on node1, start a fresh node2,
   trigger package load on node2, then GET
   `/package/<pkg>/<ver>/manifest.json` against node2 via the filestore API
   → 200 with content whose sha512 matches `manifestSHA512` in
   packages.json; the `.manifest.json.json` companion should exist too.
2. Negative: with no node holding the manifest, package load surfaces the
   clear "manifest.json is not available in filestore" error.
3. Existing package-manager tests (`TestPackages`, package-loader suites).

## Patch limits and follow-ups

- **Not compiled or tested.**
- No changelog entry (repo convention: scaffold once a Jira/PR is assigned).
- Remove this file before opening the upstream PR.
