# PR 5235: compatibility fixes, Windows implementation, round 3

Status: IMPLEMENTED, PROOF PENDING

## Pinned package

- Source: Nick's direct request to implement the confirmed fixes and hand them to a Linux gate agent.
- Claim: claims/pr-5235-compatibility-fixes-windows-round-3.md, posted at b38140b45fe01e92b3367f2b94a06620552129aa after reviewing board 82eabc8bf96b31aeadc981af8d396bb471843b21. Refreshed ownership at d31ce26715d398be153dca339a84c0ce4b43c61c; no competing fix/gate claim.
- Fork: nick-boss-tech/solr, solr-18351-submit.
- Input test head: 1517b48953dd1a11037dadc28290bfb5ca05937a.
- Fix commit: 8f4a8f22674c86598e01cf3043fc884757e55fbc.
- Final pushed head: 5f279c7aa44a993bd999be1d4dfc6ecab7942bc8, verified through the remote ref API.
- During implementation, David advanced PR 5235 from 73fa87f1f41b45862c165a9571176c89e2ef4def to 9ff44cdd0b818c6f1296d69651732f794acd76b7, adding two tests in HttpJettySolrClientTest with no production changes. A normal merge incorporates that head without rewriting either history. The final fork head descends from the current PR head.
- New gate: gates/pr-5235-compatibility-fixes-round-3.md. Receipt to be written by the Linux host: receipts/PR-5235-compatibility-fixes-round-3.md.

## Changes

1. EmbeddedSolrServer restores direct ContentStreamUpdateRequest pass-through. Other requests without an explicit writer retain all legacy streams too. MultipartContentWriter handling and explicit writer precedence remain.
2. CommandOperation restores both original public readCommands signatures as deprecated methods, with the original JSON/javabin, singleton-command and error behavior. ApiBag delegates to this common implementation rather than maintaining two parser loops. SolrJ does not depend on core.
3. JavaBinRequestWriter and XMLRequestWriter restore their legacy getContentStreams fallback implementations, including the previous UpdateRequest handling.
4. SolrRequest's default getContentWriter returns null again, leaving legacy adaptation at the RequestWriter boundary, after virtual dispatch through custom writer overrides. Explicit writers retain priority.

The existing three regression suites remain in the offered branch. The embedded suite adds generic legacy streams and an explicit multipart-writer control that throws if the legacy hook is consulted. HTTP controls now cover both built-in writer types and assert that an explicit writer bypasses a custom stream hook. The raw servlet still reads bytes before any parameter parsing; all original exact body and document-count assertions remain.

Incremental implementation changes: six production files and two existing regression test files. The full offer relative to the live PR contains those six production files and the three regression suites. No changelog or unrelated source file is added; this preserves the PR's existing 10.x compatibility promise.

## Review and proof limits

- Read the exact base and PR code paths and reviewed the eight-file implementation diff. git diff --check passed before commit; final ancestry, diff scope and clean status are checked separately.
- New commits use Nick Shanin's ICLA identity and contain no attribution trailers. The author's incoming commit is preserved as authored.
- No new authentication, authorization, remote-fetch or file-access boundary is introduced. Payload parsing and streaming use the existing implementations; no new dependencies or production logging.
- No Gradle, Java compilation, local queue or tests ran on Windows. All expected new outcomes are predictions until the Linux gate reports them.
- Previous round-1 and round-2 receipts establish the original defects, not correctness of these production fixes. Leave them intact.
- PR head and discussion must be refreshed again before an author-facing offer. The current tests-only comment draft is superseded as a proposed handoff and must not be posted.

## Author handoff after green proof

Use the PR 5232 example from AGENTS.md. Link the exact live-PR-head-to-final-tested-head diff, summarize each fix and measured results, and provide pinned switch/fetch/fast-forward commands plus the focused Gradle commands from the new receipt. If tidy or another repair moves the head, use that final validated SHA everywhere and recheck ancestry. No public comment or JIRA write is part of this Windows implementation handoff.
