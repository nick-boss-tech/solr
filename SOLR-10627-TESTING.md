# SOLR-10627 - hypothetical-reproduction handoff

**Read this first: nothing on this branch was compiled or run.** The research/implement pipeline has no Gradle access. The fix and test are best-guess; treat them as hypotheses.

- JIRA: https://issues.apache.org/jira/browse/SOLR-10627 - "Security API should not let users create permission with collection:null for per collection permissions" (Noble Paul). Users post `{"collection":null,"name":"update","role":"x"}` expecting it to secure updates; it matches nothing.
- Branch: `solr-10627-submit` off `apache/solr` main `14c7aac0d15`
- Commits: fix + test, changelog fragment, this file (drop the doc before a PR)

## The bug, as understood
In `Permission.load`, an explicit `"collection": null` becomes `singleton(null)` (the sentinel for core/collection admin requests). For well-known per-collection permissions (`read`, `update`, `schema-read`, `schema-edit`) the rule can then never match a collection request, so the permission silently protects nothing. Still true on main.

## What the branch changes
- `Permission.validateOnEdit` (called by `AutorizationEditOperation` right after `Permission.load`, so stored configs still load): for a well-known permission, if `collection` is present with a JSON null and the permission's default `collName` does not contain null, throw `BAD_REQUEST` ("collection cannot be null for the per-collection permission: <name>"). `AutorizationEditOperation` already turns that exception into a command error. Permissions whose `collName` includes null (admin, `config-*`, `metrics-read`, `health`, `all`) and custom path permissions are untouched, so `multi_auth_plugin_security.json` (custom `k8s-probe-0` with `collection: null`) still loads.
- New `BaseTestRuleBasedAuthorizationPlugin.testNullCollectionRejectedForPerCollectionPermission` (inherited by the concrete subclasses).

## What was guessed (verify these first)
1. `Perms.runCmd(cmd, false)` expecting an error and `true` expecting none, as used by `testEditRules`.
2. That the JSON-ish command string `collection: null` parses to a real null (not the string "null") in `CommandOperation.parse`.
3. The check is edit-only on purpose: existing security.json files with null on `read`/`update` keep loading (and keep protecting nothing); a load-time warning could be added separately. `update-permission` goes through the same code path as `set-permission` (assumed).
4. Line formatting: run spotless.

## How to verify (Gradle required; not run here)
```
.\gradlew :solr:core:spotlessApply
.\gradlew :solr:core:test --tests "org.apache.solr.security.TestRuleBasedAuthorizationPlugin" --tests "org.apache.solr.security.TestExternalRoleRuleBasedAuthorizationPlugin"
```
Fail-before: revert only `Permission.java` and `AutorizationEditOperation.java`; the new test's first two commands should not error.

## Not done
No JIRA comment, no PR. Changelog author is `Nick Shanin` per the ICLA note.
