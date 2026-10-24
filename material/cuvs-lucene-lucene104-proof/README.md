# Reproducing the Java-only evidence

This directory accompanies `reports/cuvs-lucene-lucene104-upstream-fix-round-1.md`.
It is not a release or a complete Lucene 10.4 compatibility fix.

On Windows with Python and the workspace's documented git executable, copy this
directory to a writable location and run:

```powershell
python reproduce.py
```

The scripts download pinned source files, Maven Central artifacts and portable
Temurin JDK 22.0.2+9. They verify Git blob IDs, artifact digests, and the JDK's
SHA-256. The JDK is extracted inside this directory, without changing system Java.
No Gradle, Maven build, native build, Solr build or GPU operation runs.

Expected results:

- Full production-source compile passes on Lucene 10.2.0 and fails with six errors
  on 10.4.0 in unchanged query/reader classes.
- All five affected/dependency source files compile on both versions.
- The actual private helper passes 526 comparisons per compiled variant against
  the original Lucene 10.2 method, plus a Lucene 10.4 smoke invocation.
- The original 26.10.0 jar has four incompatible references on 10.4. The patched
  shim removes one and leaves three. The original jar on 10.2 has zero.
- The original 25.12.0 jar has three incompatible references on 10.4.
- Auditor mutation fixtures detect descriptor, staticness, owner-kind and missing
  class/member changes. Baseline fixture checks pass.
- The patch applies to the pinned release tree and inspected main snapshot. Only
  the intended outer class changes in the rebuilt proof jar.

The linker inventory is JSON, with every reference, descriptor, bytecode use and
failure retained. `results/compile-results.json` and
`results/semantics-results.json` preserve the exact originally executed argument
arrays. Those paths identify the original host; rerunning generates local paths.

The jar and portable JDK are intentionally excluded from the board package.
`artifact-manifest.json` lists artifact URLs and SHA-256 digests;
`release-metadata.json` pins the source revision and Git blob IDs. The unified
patch is in the parent material directory as `cuvs-lucene-lucene104-fix.patch`.

The proof scripts are research tools. Constant-pool checks do not validate
reflection-generated names, native dependencies, the on-disk format or GPU
execution. The report explains the remaining abstract/override source errors and
why this package should feed the existing upstream compatibility work.
