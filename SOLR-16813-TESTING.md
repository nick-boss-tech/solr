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
persisted with its metadata companion). The fetch lives in a private
`Version.fetchManifest()`. If the manifest still is not a file afterwards, a
warning is logged and the package version loads anyway.

Round-3 review change: the first version threw
`"Cannot load package: manifest.json is not available in filestore: ..."`.
`SolrPackage.updateVersions` catches an exception from the `Version`
constructor and skips the version, so that would have stopped the whole package
version from loading on a node (cold start with no other node live, an install
whose manifest was never distributed, a missing file) although the JARs do not
need the manifest. The ticket leaves open whether the manifest is "mandatory";
this keeps loading unchanged and only adds the fetch. If the maintainers want it
mandatory, that is a deliberate design decision for the PR discussion.

Notes for the reviewer:

- The manifest is deliberately NOT added to `version.files`:
  `validateFiles` demands trusted-keys signatures and the manifest is
  uploaded with a null signature (`RepositoryManager`), so it needs the
  fetch without the signature gate. Integrity is still covered by the
  sha512 check in `fetchFileFromNodeAndPersist`.
- `version.manifest == null` is tolerated (nothing to fetch); a non-null but
  unresolvable manifest path only logs a warning.
- The remote fetch runs inside `SolrPackage.updateVersions` (`synchronized`);
  the JAR fetches already happen there, so this adds one small fetch.
- Alternative not taken: fetching lazily on read in `ClusterFileStore.getFile`
  would cover any lazily distributed file but is a wider change.
- Standalone mode is unaffected: `getType` short-circuits on local
  existence before attempting any fetch.

Files changed:
- `solr/core/src/java/org/apache/solr/pkg/SolrPackageLoader.java`

## Recommended reviewer commands

```bash
cp ~/workspace/solr/gradle.properties .   # worktrees don't inherit it
~/workspace/tools/solr-gradle.sh :solr:core:compileJava -Pvalidation.errorprone=true
```

Test added (round-3 patch pass, **not compiled or run**):

- `TestPackages#testPackageLoadsWhenManifestIsNotInFileStore`: registers a
  package version whose `manifest` path was never uploaded to the file store,
  then creates a collection whose config uses a plugin from the package
  (`verifyComponent`, as in `testCoreReloadingPlugin`). The version must still
  load on every node. It fails with the first version of this patch.

Not covered (needs a dedicated multi-node fixture): a node that joins late
actually fetching an existing manifest and its `.manifest.json.json` companion.
Queued for the verification run: `org.apache.solr.pkg.TestPackages` and
`org.apache.solr.filestore.TestDistribFileStore`, with Spotless.

## Patch limits and follow-ups

- **Not compiled or tested.** The new test assumes the Package API `add`
  command does not itself reject a manifest path that is not in the file store.
- Changelog fragment added: `changelog/unreleased/SOLR-16813.yml`.
- Remove this file before opening the upstream PR.
