# SOLR-15945 — Testing handoff

**Status: UNCOMPILED AND UNTESTED.** Written without running Gradle
(no compile, no tests). A reviewer must compile and test before this goes
anywhere near a PR.

This patch also resolves SOLR-15403 ("Non-indexed DateRangeField throws
SolrException"), a duplicate report of the same `checkSchemaField()` bug —
the `if (field.indexed())` guard below fixes both tickets. Upstream PR time
should reference both.

## What the patch does

`AbstractSpatialPrefixTreeFieldType.checkSchemaField()` unconditionally
required `omitNorms()` and `indexOptions() == DOCS`. But `SchemaField`
strips OMIT_NORMS from non-indexed fields and reports `indexOptions() ==
NONE` for them, so any spatial field declared `indexed="false"` (stored-only
or docValues-only) could never pass validation — core load failed with
"incompatible with omitNorms=false; hardcoded behavior is omitNorms=true".
Norms cannot exist on a non-indexed field anyway, so both checks were false
positives. Affects `DateRangeField` and `SpatialRecursivePrefixTreeFieldType`
(the two subclasses inheriting the check).

Fix (`solr/core/src/java/org/apache/solr/schema/AbstractSpatialPrefixTreeFieldType.java`):
both validations are now skipped when `!field.indexed()` — they have no
meaning on a non-indexed field. Indexed-field behavior is unchanged.

## Recommended reviewer commands

```bash
cp ~/workspace/solr/gradle.properties .   # worktrees don't inherit it
~/workspace/tools/solr-gradle.sh :solr:core:compileJava -Pvalidation.errorprone=true
```

## Test ideas (not written)

- Extend `BadIndexSchemaTest` (which already asserts the omitNorms error for
  an *indexed* DateRangeField, ~lines 178-181): add a schema with
  `indexed="false" stored="true"` (plus a docValues variant) — the
  core/schema must load without error.
- Keep the existing indexed-field error assertions green to prove the guard
  doesn't weaken the intended validation.

## Patch limits and follow-ups

- **Not compiled or tested.**
- No changelog entry (repo convention: scaffold once a Jira/PR is assigned).
- Remove this file before opening the upstream PR.
