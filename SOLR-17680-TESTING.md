# SOLR-17680 — Testing handoff

**Status: UNCOMPILED AND UNTESTED.** Written without running Gradle
(no compile, no tests). A reviewer must compile and test before this goes
anywhere near a PR.

## What the patch does

Creating a Dimensional Routed Alias exactly per the ref-guide example
(`router.name=Dimensional[time,category]` with per-dimension `router.0.field`
/ `router.1.field`, no top-level `router.field`) was rejected with
BAD_REQUEST "A routed alias requires these params: [router.field,
router.name]...". Root cause: the minimal-params pre-check added by
SOLR-16393 ran BEFORE `RoutedAlias.fromProps` normalized the dimensional
params into a synthetic top-level `router.field` — so a valid, documented DRA
create could never pass validation.

Fix (in `CreateAliasCmd.callCreateRoutedAlias`): the early check now only
requires `router.name` (same BAD_REQUEST message as before, needed because
`fromProps` answers null without it). Round 4 review: the full-minimum check
that the first version repeated after normalization was redundant, because the
existing `getRequiredParams()` check after `RoutedAlias.fromProps` already
requires `router.name` and `router.field` for time, category and dimensional
aliases, and `fromProps` synthesizes `router.field` for dimensional aliases.
That second check was removed.

Behavior matrix after the patch:

- DRA per the guide (`router.name=Dimensional[...]`, `router.0.field`,
  `router.1.field`) → passes validation (previously wrongly rejected).
- Plain time routed alias missing `router.field` → still BAD_REQUEST, but the
  text now comes from the `TimeRoutedAlias` constructor ("A time routed alias
  requires these params: ..."), not from `CreateAliasCmd` (the first version of
  this handoff said "same message"; that was wrong).
- Any `router.*` request missing `router.name` → still BAD_REQUEST, same
  message.

File changed:
- `solr/core/src/java/org/apache/solr/cloud/api/collections/CreateAliasCmd.java`
  (`callCreateRoutedAlias`, check reordered/split)

## Recommended reviewer commands

```bash
cp ~/workspace/solr/gradle.properties .   # worktrees don't inherit it
~/workspace/tools/solr-gradle.sh :solr:core:compileJava -Pvalidation.errorprone=true
```

Tests added (round 4, not compiled or run), in `CreateRoutedAliasTest` (raw
`CREATEALIAS` URLs, because the SolrJ builder adds `router.field` itself and so
hid the bug):

1. `testDimensionalRoutedAliasWithoutTopLevelFieldV1`: the reference guide
   request succeeds and the alias metadata holds `router.name` and the
   per-dimension fields.
2. `testDimensionalRoutedAliasMissingDimensionFieldFails`: missing
   `router.1.field` returns 400 (message not asserted).
3. `testRoutedAliasMissingFieldOrNameFails`: time alias without `router.field`
   and request without `router.name` both return 400 with their messages.

## Patch limits and follow-ups

- **Not compiled or tested.**
- No changelog entry (repo convention: scaffold once a Jira/PR is assigned).
- Remove this file before opening the upstream PR.
