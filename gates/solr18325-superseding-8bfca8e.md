# Gate SOLR-18325 superseding job: exact head 8bfca8edd30 (vm2 round 2)

Status: OPEN

- Supersedes the round 1 proof at `ed07d0dd67ec31f8091abc41e92942bde53054da`, which failed the root check (`validateSourcePatterns`, diamond with `var`, `JacksonBehavior.java:48`).
- Proof head: `8bfca8edd30acf11bdf9d6a733312e97f99ec78a` on `solr-18325-submit`, chain `f45b38d5f6c0` -> `958a8411cfe` -> `35a35d69900` -> `ed07d0dd67e` -> `00be45f507` -> `8bfca8edd30`.
- The last two commits: explicit type for the one flagged declaration, then google-java-format of the same file. The format pass also reflows 11 pre-existing non-conforming lines in that harness file; no behavior change intended.
- Seed for named repository tests: `1832510C0FFEE`. Both harness lanes run on Java 21 and Java 17 toolchains.
- Pending legs at this head: tidy, Error Prone compile, both focused sets, mavenToLocalFolder, harness on Java 17 and 21, Solr 10.0.0 standalone HTTP, root check in a clean full clone.
- Known inherited failures carried from round 1 and not attributed to the prototype: `CoreAdminHandlerTest.testDeleteInstanceDirAfterCreateFailure` fails on the base `f45b38d5f6c0` too (same message, same seed). `binaryHttp` breaks because released 10.0.0 has a top-level `HttpSolrClientBuilderBase` and the base nests it as `HttpSolrClient.BuilderBase`.
