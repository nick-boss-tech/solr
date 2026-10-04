# SOLR-9349 - hypothetical-reproduction handoff

**Read this first: nothing on this branch was compiled or run.** The research/implement pipeline has no Gradle access. The fix and test are best-guess; treat them as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-9349 - "Schema API should never delete fields used elsewhere in the schema" (Shawn Heisey). A user deleted the `id` field (the uniqueKey) through the Schema API and the core then failed to load. Steven Rowe noted that copyField sources/destinations are already covered in `ManagedIndexSchema.deleteFields`.
- Branch: `solr-9349-submit` off `apache/solr` main `14c7aac0d15`
- Commits: fix + test, changelog fragment, this file (drop the doc before a PR)

## The bug, as understood
`ManagedIndexSchema.deleteFields` rejects fields used by copyField directives but has no check for the uniqueKey field, so `delete-field` on it succeeds and the persisted schema no longer loads. Still true on main (no uniqueKey check in `ManagedIndexSchema`/`SchemaManager`).

## What the branch changes
- `ManagedIndexSchema.deleteFields`: throw `BAD_REQUEST` ("Can't delete field '<name>' because it is the uniqueKey field.") when the name equals `uniqueKeyFieldName`.
- New `TestBulkSchemaAPI.testDeleteUniqueKeyFieldRefused`: posts `delete-field` for `id` and asserts the error and that the field still exists.

## What was guessed (verify these first)
1. `uniqueKeyFieldName` on the shallow copy is populated (copied in `shallowCopy`, checked on main at ~L1432).
2. The error is reported under `error` in the `/schema` response like the existing copyField tests (`"Can't delete field 'NewField1' because it's referred to by ..."`).
3. `schema-rest.xml` (used by this test class) has `<uniqueKey>id</uniqueKey>` and no other test deletes `id`.
4. Other dependencies (e.g. fields used by `similarity`, default search field, update processors) are not covered; only the uniqueKey case from the ticket is.

## How to verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.rest.schema.TestBulkSchemaAPI"
```
Fail-before: revert only `ManagedIndexSchema.java`; the delete succeeds (and later tests in the class may fail because `id` is gone).

## Not done
No JIRA comment, no PR. Changelog author is `Nick Shanin` per the ICLA note.
