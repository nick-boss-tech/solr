# Assignment: SOLR-17849 browser-leg check and screen render for PR #5257, round 1

Status: OPEN

Posted 2026-10-27 by the phone agent at Nick Shanin's direction. Intended hosts: a host with the `ui` and `gate` tags and the working software-renderer setup (vm4, vm5, or vm6). Claim before work. Twin of `assignments/solr-17914-browser-check-round-1.md`, for the second of the two Christos PRs whose verification stops at JVM tests.

## Purpose

PR #5257 (SOLR-17849, Index & Query section header) is open as a draft. Its body's Limits state the gap plainly: "The tests run on the JVM with a software renderer. Nothing was run in a browser." Close both halves of that gap: run the ui build's browser leg at the PR head, and render the header for a visual check against the design, the way SOLR-17887's screen check rendered the FileTree inside the app theme and compared it with the design frames.

## Work

1. Check out branch `solr-17849-header-submit` at exact head `cf35ab0329c41cdef11429906d1e669d38b9f3a9` in a clean worktree.
2. Run the full `./gradlew -p ui check` at that head, no build cache, with the host's established software-renderer setup. Report the browser (wasm) leg results from the fresh JUnit XML: total run, failures, errors, skips, and the named results for `IndexQueryHeaderTest` and `IndexQueryStateHolderTest` in the browser run specifically.
3. Screen render: render `IndexQueryHeader` inside the app theme using the pool's screen-check setup, in at least the populated state (collections loaded, a collection selected, its handlers listed). Capture PNG screenshots and post them to the board under `material/screen-checks/SOLR-17849/`. Compare against the Figma capture of node `137-12066`: field widths (collection 256 px, handler 384 px), the handler class shown in the closed field in monospace, and the general structure. State plainly what matches, what differs, and what the render cannot confirm, following the honesty pattern of the SOLR-17887 screen receipt (approximate colors from thumbnails are stated as approximate, not as matched).

## Deliverables

- `receipts/SOLR-17849-browser-check-round-1.md`: the command, the head, the browser-leg counts from the fresh XML, the screenshot paths, the comparison notes, and the verdict.
- If the browser leg is green: `drafts/pr-5257-limits-line-update.md` with the exact replacement text for the "Nothing was run in a browser" line in the PR #5257 body, ready for the phone agent to apply on the owner's word.
- If the browser leg fails: the receipt records the failures with test names and the first error text. Do not fix code under this assignment.

## Boundaries

- No code changes, no pushes to the branch, no PR or Jira posts or edits.
- One Gradle build at a time under the host's build lock, as usual.
