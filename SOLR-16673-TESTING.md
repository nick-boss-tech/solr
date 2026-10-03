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

One-line-class fix in four files (the last two were added in the round-3 patch
pass: `ParseIntFieldUpdateProcessorFactory` and
`ParseFloatFieldUpdateProcessorFactory` parse inline with the same guard and the
same NPE, e.g. for an empty string in a schemaless update chain):

- `solr/core/src/java/org/apache/solr/update/processor/ParseLongFieldUpdateProcessorFactory.java`
- `solr/core/src/java/org/apache/solr/update/processor/ParseDoubleFieldUpdateProcessorFactory.java`
- `solr/core/src/java/org/apache/solr/update/processor/ParseIntFieldUpdateProcessorFactory.java`
- `solr/core/src/java/org/apache/solr/update/processor/ParseFloatFieldUpdateProcessorFactory.java`

The guard is now `if (number == null || pos.getIndex() != stringVal.length())`,
returning `null` (value not mutated) instead of NPEing. Callers
(`DefaultSchemaSuggester`'s type-guess helpers) already handle a `null`
return correctly. This matches the maintainer-endorsed direction in the
ticket comments (Houston Putman: "mitigable by checking for empty string /
null parse result and returning null").

Out of scope (separate threads, no maintainer-endorsed fix) — so the PR should
say it fixes the NPE only, not "fixes SOLR-16673":
- `SchemaDesigner` assuming uniqueField is a string field
  (`(String) d.getFieldValue(uniqueKeyField)` still on `main`).
- The `"_root_"` / uniqueKey fieldType mismatch on child docs.

Behavior to state: one empty string among otherwise numeric sample values makes
the Schema Designer suggest a string/text type for that field, like any other
unparseable value.

## Recommended reviewer commands

```bash
cp ~/workspace/solr/gradle.properties .   # worktrees don't inherit it
~/workspace/tools/solr-gradle.sh :solr:core:compileJava -Pvalidation.errorprone=true
```

Test added (round-3 patch pass, **not compiled or run**):

- `ParsingFieldUpdateProcessorsTest#testEmptyStringIsNotParsedAsNumber`: runs a
  document with an empty `not_in_schema` value through the existing
  `parse-{int,long,float,double}-no-run-processor` chains; each must return the
  document with the value left as the string `""` (before the fix: a
  NullPointerException from the processor).

Not added: a `DefaultSchemaSuggester` / Schema Designer test with an empty sample
value, and direct `parsePossibleLong`/`parsePossibleDouble` calls (the existing
round-trip tests cover the non-empty regression cases).

Queued for the verification run:
`org.apache.solr.update.processor.ParsingFieldUpdateProcessorsTest`, with
Spotless.

## Patch limits and follow-ups

- **Not compiled or tested.**
- Changelog fragment added: `changelog/unreleased/SOLR-16673.yml`.
- Remove this file before opening the upstream PR.
