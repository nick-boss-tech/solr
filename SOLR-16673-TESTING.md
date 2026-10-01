# SOLR-16673 — Testing handoff

**Status: UNCOMPILED AND UNTESTED.** Written without running Gradle
(no compile, no tests). A reviewer must compile and test before this goes
anywhere near a PR.

## What the patch does

Using the Schema Designer with documents containing empty or unparseable
string values threw an NPE. Root cause: `NumberFormat.parse(String,
ParsePosition)` returns `null` when nothing can be parsed, and both
`parsePossibleLong` and `parsePossibleDouble` only guarded on
`pos.getIndex() != stringVal.length()`. For an empty string,
`pos.getIndex()==0` equals `stringVal.length()==0`, so the guard passed and
`number.longValue()` / `number.doubleValue()` NPEd. The stack flowed through
`DefaultSchemaSuggester.isIntOrLong` → `guessFieldType` →
`SchemaDesignerAPI.analyzeInputDocs`, killing the whole analysis.

One-line-class fix in two files:

- `solr/core/src/java/org/apache/solr/update/processor/ParseLongFieldUpdateProcessorFactory.java`
- `solr/core/src/java/org/apache/solr/update/processor/ParseDoubleFieldUpdateProcessorFactory.java`

The guard is now `if (number == null || pos.getIndex() != stringVal.length())`,
returning `null` (value not mutated) instead of NPEing. Callers
(`DefaultSchemaSuggester`'s type-guess helpers) already handle a `null`
return correctly. This matches the maintainer-endorsed direction in the
ticket comments (Houston Putman: "mitigable by checking for empty string /
null parse result and returning null").

Out of scope (separate threads, no maintainer-endorsed fix):
- `SchemaDesignerAPI` assuming uniqueField is a string field.
- The `"_root_"` / uniqueKey fieldType mismatch on child docs.

## Recommended reviewer commands

```bash
cp ~/workspace/solr/gradle.properties .   # worktrees don't inherit it
~/workspace/tools/solr-gradle.sh :solr:core:compileJava -Pvalidation.errorprone=true
```

Suggested tests (not written):

1. Direct: `parsePossibleLong("", format)`, `parsePossibleLong("abc",
   format)`, `parsePossibleDouble("", format)` → `null`, not NPE.
2. Regression: `parsePossibleLong("42", format)` → `42L`;
   `parsePossibleDouble("4.2", format)` → `4.2` (guard against
   over-rejection).
3. Existing update-processor test suites for these factories.

## Patch limits and follow-ups

- **Not compiled or tested.**
- No changelog entry (repo convention: scaffold once a Jira/PR is assigned).
- Remove this file before opening the upstream PR.
