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

Fix (in `CreateAliasCmd.callCreateRoutedAlias`): validate after
normalization. The full `MINIMAL_REQUIRED_PARAMS` check now runs on the
`props` map AFTER `RoutedAlias.fromProps` has synthesized `router.field`
from the per-dimension entries; a valid DRA passes it, and the existing
post-normalization `getRequiredParams()` check is unchanged. To avoid
regressing the error for requests missing `router.name` entirely (which
`fromProps` answers with null), a small up-front check still requires
`router.name` with the same BAD_REQUEST message as before.

Behavior matrix after the patch:

- DRA per the guide (`router.name=Dimensional[...]`, `router.0.field`,
  `router.1.field`) → passes validation (previously wrongly rejected).
- Plain routed alias missing `router.field` → still BAD_REQUEST, same
  message (the SOLR-16393 validation keeps working).
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

Suggested tests (not written):

1. Drive the create-alias command path with the guide's DRA params
   (`router.name=Dimensional[time,category]`, `router.0.field`,
   `router.1.field`, `create-collection.*` params) → expect the routed-alias
   creation path instead of BAD_REQUEST. Existing DRA tests:
   `DimensionalRoutedAliasUpdateProcessorTest`, `CreateAliasAPITest`.
2. Regression: plain routed alias without `router.field` → still BAD_REQUEST.

## Patch limits and follow-ups

- **Not compiled or tested.**
- No changelog entry (repo convention: scaffold once a Jira/PR is assigned).
- Remove this file before opening the upstream PR.
