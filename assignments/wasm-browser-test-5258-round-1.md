# Assignment: run the wasm browser test leg for PR #5258 (SOLR-17914), round 1

Status: OPEN
Capability tags: [ui]. Intended host: any host with the ui capability, a local Chrome or Chromium, and an npm route that can fetch from registry.yarnpkg.com. Posted by the phone agent at Nick's direction, 2026-10-30.

## Context

PR #5258 (SOLR-17914, create collection dialog, new design) is a draft on branch solr-17914-create-collection-dialog-new-design, current head d02ccdcdd8dd69129ebdacd2bd8bcc8917b5e79a. The 17913 details card was removed at that head and the removal is gated: spotless passes and the full :shared:jvmTest suite is green at d02ccdcdd8 (receipts/pr-5258-17913-removal.md). The one proof leg still open at this head is the wasm browser suite, which last passed at bb7a4f32eb, before the removal.

The phone agent's VM cannot run this leg: the Kotlin plugin's bundled yarn 1 cannot fetch npm tarballs through this VM's intercepting egress proxy (TLS socket closes inside the CONNECT tunnel; curl and Node fetch reach the same URLs fine). Full diagnosis in receipts/pr-5258-wasm-attempt-2026-10-30.md; do not repeat it on a host with the same egress shape.

## The ask

1. Check out the PR branch at exactly d02ccdcdd8dd69129ebdacd2bd8bcc8917b5e79a in a scratch worktree (copy gradle.properties and the wrapper jar in, per local convention).
2. Run the wasm browser suite for the shared module from the repo root: solr-gradle.sh -p ui :shared:wasmJsBrowserTest --no-daemon --console=plain, with CHROME_BIN set to the host's Chrome or Chromium. Karma runs the browser headless; if the host runs as root, apply whatever no-sandbox handling the host already uses for Karma.
3. Read the verdict from the fresh JUnit XML under ui/shared/build/test-results/wasmJsBrowserTest/, per suite and in total.

## Done

File a receipt under receipts/ naming the head tested, the browser and its version, the per-suite counts from the fresh XML, and the pass or fail verdict. Change nothing on GitHub and nothing on the branch; the phone agent updates the PR body from the receipt. If the leg cannot run on the claiming host either, the receipt states the exact failure point instead, so the blocker record stays current.
