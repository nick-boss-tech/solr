# SOLR-9750 - hypothetical-reproduction handoff

**Read this first: nothing on this branch was compiled or run.** The research/implement pipeline has no Gradle access. The fix and test are best-guess; treat them as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-9750 - "The paramset for the /graph implicit RequestHandler is named _ADMIN_GRAPH but should be _GRAPH" (Steven Rowe). The ticket has a trivial patch; later comment says it is still relevant.
- Branch: `solr-9750-submit` off `apache/solr` main `14c7aac0d15`
- Commits: fix + test, changelog fragment, this file (drop the doc before a PR)

## The bug, as understood
`ImplicitPlugins.json` names `/graph`'s paramset `_ADMIN_GRAPH`, while the convention is endpoint name upper-cased with slashes replaced by underscores (`/stream` -> `_STREAM`, `/export` -> `_EXPORT`, `/admin/ping` -> `_ADMIN_PING`). `/graph` is not an admin endpoint. Still on main, also in the ref guide (`implicit-requesthandlers.adoc`).

## What the branch changes
- `ImplicitPlugins.json`: `"useParams":"_GRAPH"` for `/graph`.
- `implicit-requesthandlers.adoc`: paramset name updated.
- `TestImplicitPlugins.testGraphHandlerParamsetName` reads the JSON resource with `Utils.fromJSONResource` and asserts the value.

## What was guessed (verify these first)
1. `Utils.fromJSONResource(ClassLoader, String)` returns a `Map` with a top-level `requestHandler` map keyed by path (as the file looks); casts are unchecked.
2. Anyone who already created a custom `_ADMIN_GRAPH` paramset through the Config API will silently stop getting it applied; the ticket does not discuss compatibility. A changelog note could mention this.
3. The ticket's sibling SOLR-9749 (`/admin/plugins` missing a paramset) looks obsolete: the endpoint is not in `ImplicitPlugins.json` on main.

## How to verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.core.TestImplicitPlugins"
```
Fail-before: revert only `ImplicitPlugins.json`.

## Not done
No JIRA comment, no PR. Changelog author is `Nick Shanin` per the ICLA note.
