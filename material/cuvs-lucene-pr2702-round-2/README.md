# CPU regression patch and proof for NVIDIA/cuvs PR 2702

Base revision: `23004d0f0de2014f11b09313f04fcf0de7643c3b`.
Patch: `cpu-regression.patch`, SHA-256
`d9ad46585c65846cf9b0d4f0ccffbf882e79be593f15897d8775115de810b154`.

The patch adds three CPU query-adapter tests and two test-only API bridges. The
existing PR already fixes the original linkage failures. This package adds
discriminating coverage; it does not claim another production bug was found.

For a fresh Linux CPU proof, use Python 3, git, and a JDK 21+ installation. Set
`CUVS_CPU_JDK` to the JDK root, then run:

```bash
python3 reproduce.py
```

On Windows, use the documented Python executable and set `CUVS_CPU_JDK` to an
installed JDK root; `CUVS_CPU_GIT` can override the documented git executable.
No JDK is downloaded or installed by this package. All generated inputs and
outputs stay under the proof directory.

The scripts fetch source files at the pinned commit and verify their Git blob
IDs. They apply the patch and verify the three resulting source blob IDs. The
native cuVS implementation is excluded: only its real Java 21 API is compiled
from source. Lucene and test dependencies come from Maven Central with digests.
The formatter has a pinned SHA-256.

Expected results:

- All four production module source sets compile with `--release 21`.
- All four whole-jar Lucene inventories have zero linkage failures.
- Four JUnit tests pass on each module: 16 positive tests in total.
- The original test passes both dropped-mask mutations.
- The patched suite rejects each mutation with exactly the three added test
  failures. Those failures are required evidence, not a failed final build.
- The Java formatting check passes without changing the patch.

`reproduce.py` runs these checks sequentially and exits nonzero on an unexpected
result. It never runs Gradle, Maven, CUDA tools, a native build or Solr. Ignore
the automatic Gradle reproduction snippets in Lucene's mutation-failure logs;
the exact executed JVM commands are recorded in `results/cpu-tests.json`.

The retained local results used Temurin 22.0.2+9 on Windows. Paths in the saved
command arrays identify that run; a reproduction regenerates local paths. The
independent Linux gate is separate proof and remains pending until its receipt.

Downloaded binaries, source snapshots, generated jars and class directories are
excluded from this board package. This is CPU compilation, static linkage and
query-adapter semantics evidence, not a GPU or file-format validation claim.
