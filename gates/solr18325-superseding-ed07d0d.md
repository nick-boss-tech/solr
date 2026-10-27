# Gate SOLR-18325 superseding job: exact head ed07d0dd67ec (vm2)

Status: OPEN

- Supersedes: `gates/solr-18325-solr10-feasibility-round-1.md` for the final proof. The incoming Windows head `aa8cc9ab29a` is not the proof head.
- Proof head: `ed07d0dd67ec31f8091abc41e92942bde53054da` on `nick-boss-tech/solr:solr-18325-submit`, four commits above base `f45b38d5f6c0`.
- Chain (each a fast-forward, Nick Shanin, no trailers):
  - `aa8cc9ab29a` incoming Windows prototype head.
  - `958a8411cfe` tidy formatting, six source/test files: two `CoreAdminHandlerTest` style wraps, one `SystemInfoResponse` join, one `JacksonContentWriterTest` wrap, one `TestManagedSchema` and one `TestCloudManagedSchema` wrap, one `CoreAdminCreateDiscoverTest` wrap, one shared annotation split in `JacksonContentWriterTest`. No behavior change.
  - `35a35d69900` dependency locks, 26 `gradle.lockfile` files: adds Jackson 3.2.1 core, databind and BOM; keeps Jackson 2.22.3 core and databind in the same lockfiles for the server and oracle graphs. No license or checksum metadata changed.
  - `ed07d0dd67e` Error Prone fix: `CoreAdminJacksonCompatibilityTest` line 35 compares the start time by epoch millis instead of `assertEquals(new Date(1234), ...)`, which Error Prone rejects under `-Werror`. The assertion is kept.
- Tidy at `ed07d0dd67e`: rc 0, tree clean.
- Compile at `ed07d0dd67e`: `:solr:solrj:compileTestJava :solr:core:compileTestJava` with `-Pvalidation.errorprone=true`, rc 0.
- Toolchain: JDK 21 (Gradle), JDK 17 installed at `/workspace/jdk/jdk17` for consumer execution.
- Seed for all named repository tests: `1832510C0FFEE`.

The remaining legs (focused suites, published artifacts, compatibility harness on Java 17 and 21, Solr 10.0.0 HTTP, root check) run at `ed07d0dd67e` and report here.
