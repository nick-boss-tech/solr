# Assignment: SOLR-17914 browser-leg check for PR #5258, round 1

Status: CLAIMED (vm4, claim `claims/solr-17914-browser-check-round-1.md`, started 2026-10-28T05:43:22Z)

Posted 2026-10-27 by the phone agent at Nick Shanin's direction. Intended hosts: a host with the `ui` and `gate` tags and the working software-renderer setup (vm4, vm5, or vm6). Claim before work.

## Purpose

PR #5258 (SOLR-17914, create collection dialog, new design) is open as a draft. Its body states "Browser verification: not done. The form is not reachable in the app yet." The second sentence is a scope fact (the Collections screen is SOLR-17912 and is not built). The first is a gap this assignment closes: every gate so far ran `:shared:jvmTest` with the software renderer; the wasm browser leg of the ui build has never run on this branch. SOLR-17887's PR #5256 shows the shape: its full `./gradlew -p ui check` included the browser wasm test run.

## Work

1. Check out branch `solr-17914-create-collection-dialog-new-design` at exact head `6170ba10ac5061203339fa0a96e714c7e64616c2` in a clean worktree.
2. Run the full `./gradlew -p ui check` at that head, no build cache, with the host's established software-renderer setup. The expected content: `:shared:jvmTest` (already known green at this head; it re-runs as part of the check), the `:shared` browser (wasm) tests, and spotless.
3. Report the browser-leg results from the fresh JUnit XML: total run, failures, errors, skips, and the named results for `CreateCollectionFormTest`, `CreateCollectionErrorsTest`, and `HttpCollectionsRepositoryTest` in the browser run specifically.

## Deliverables

- `receipts/SOLR-17914-browser-check-round-1.md`: the command, the head, the browser-leg counts from the fresh XML, and the verdict.
- If the browser leg is green: `drafts/pr-5258-proof-line-update.md` with the exact replacement text for the "Browser verification: not done." line in the PR #5258 body, ready for the phone agent to apply on the owner's word.
- If the browser leg fails: the receipt records the failures with the failing test names and the first error text. Do not fix code under this assignment; a fix is a separate step the owner directs.

## Boundaries

- No code changes, no pushes to the branch, no PR or Jira posts or edits. The PR body edit itself is not part of this assignment.
- One Gradle build at a time under the host's build lock, as usual.
