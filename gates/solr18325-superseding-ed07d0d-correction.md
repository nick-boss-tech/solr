# SOLR-18325 correction to the superseding gate for ed07d0dd67ec

The file-level breakdown in `gates/solr18325-superseding-ed07d0d.md` was wrong. The verified mapping of commit `958a8411cfe` against its parent (formatting only, verified by `git diff` per file):

- `solr/core/src/test/.../handler/admin/CoreAdminCreateDiscoverTest.java`: one `ObjectMapper().convertValue(...)` wrap.
- `solr/core/src/test/.../handler/admin/CoreAdminHandlerTest.java`: one `ObjectMapper().convertValue(...)` wrap.
- `solr/core/src/test/.../schema/TestCloudManagedSchema.java`: one `ObjectMapper().convertValue(...)` wrap.
- `solr/core/src/test/.../schema/TestManagedSchema.java`: one `ObjectMapper().convertValue(...)` wrap.
- `solr/solrj/src/java/.../response/SystemInfoResponse.java`: two `JacksonContentWriter.convertValue(removeHeader(response), NodeSystemResponse.class)` joins.
- `solr/solrj/src/test/.../request/json/JacksonContentWriterTest.java`: one `expectThrows(IOException.class, ...)` wrap and one `@JsonProperty("alias")` annotation split onto its own line.

No behavior change. The lockfile commit and the Error Prone fix are unaffected by this correction.
