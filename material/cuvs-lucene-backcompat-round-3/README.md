# CPU archived-index compatibility proof

This package adds one test to NVIDIA/cuvs PR 2702 at `23004d0f0de2014f11b09313f04fcf0de7643c3b`. It does not change production code or the round-2 filter patch.

The patch is `backcompat.patch`. Its SHA-256 is `56cb5cb10f210e216c36fd413821dd1ddd931e5dd4ed26fc288299c928a84238`. The only upstream path changed is `java/cuvs-lucene/src/test/java/com/nvidia/cuvs/lucene/TestBackCompatIndices.java`.

Use a fresh directory, Python 3, git, network access, and an absolute JDK 21 root in `CUVS_CPU_JDK`. On Windows `CUVS_CPU_GIT` may name the git executable. Run:

```sh
python3 reproduce.py --preflight-only
python3 reproduce.py
```

Preflight verifies pinned Git blobs, all 16 fixture archives, patch application, the patched test blob and google-java-format 1.27.0. The full run rebuilds the public cuvs-java API and all four cuvs-lucene source variants with direct javac --release 21. It then runs the original read/upgrade methods on every compatible fixture and the new deletion/sparse-vector method, with seed 18556C0D. No Maven, Gradle, native implementation, CUDA or GPU fixture generation is used. The original GPU fixture-generation method is never invoked.

The input scripts and source manifest come from immutable round-2 board commit `2ad555a87a22acdf617f679ca03f5831f246b4a8` and are checked by Git blob ID. The upstream head, before/after test blobs and patch digest are in `patch-manifest.json`.

The new case deletes 67 of 200 old documents, appends 50 documents of which 10 have no vectors, then checks the 183 live document IDs and 173 vector-bearing IDs and exact float values before and after a forced merge. It asserts that the pre-merge index retains deletions and multiple segments and that the final index has one segment and no deletions. The existing search/codec check is also applied afterward. Both fixture generators use Random(42); that established data contract supplies the expected old vector values.

`FixtureMappingControl.java` creates two separate valid CPU-written indexes with stored IDs cyclically shifted relative to their vectors. It does not modify a production class or any archived fixture. On Lucene 10.2 and 10.4 the two existing methods still pass but the new value assertion fails. This establishes sensitivity to incorrect ID/vector association, not a demonstrated production regression. Some other assertions remain behavior pins without separate mutations.

Expected results: 8 original and 4 new passing method executions; 7/10/13/16 compatible fixtures on the four modules, totaling 92 original read/upgrade and 46 new upgrade cases. Controls add four passing original-method executions and two expected new-method failures. The harness rejects assumptions and ignored tests.

The saved evidence is from Windows, Temurin 22.0.2, with source target 21. Independent Linux JDK 21 proof is pending. The compiled API jar excludes the Java 22/native implementation; the module jars are thin proof jars, not Maven release artifacts. Reading GPU-produced archives and CPU-merging them is not GPU writer validation, GPU search validation, Solr integration proof, or release packaging proof.

Reproduction is intended for a fresh directory. A control output directory is deliberately not overwritten on a second run. Preserve failed evidence and use a new directory for a repair run.
