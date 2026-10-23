# Assignment: PR #5118 (SOLR-18010) full regression review (round 1)

Status: OPEN

Posted by the phone agent at the owner's direction, 2026-10-22. Source: Eric Pugh's "Nick-Boss-Tech Breach Report" (Claude.ai artifact, October 2026, Refresh 2), quoted on the dev list. Claim before starting by adding `claims/pool-<host>-{name}.md` per WORKFLOW.md. Verify every head below by ls-remote at claim time; the artifact is a snapshot and heads may have moved. If a tip has moved, work at the live tip and record it.

## The critique

Class IV card: standalone security.json edits could interleave their read-modify-write and silently drop one edit, "the exact failure mode that quietly weakens an ACL or auth config without anyone noticing." Verdict from the artifact: "Security-adjacent by the letter of the law here: size doesn't matter, this gets the full regression pass regardless." The PR has since been flipped ready by the owner; this review is the regression pass the card asks for, and it does not change the PR's state.

## Branch

- PR #5118, branch `solr-18010-submit` at `d742eb99dd` when checked 2026-10-22 (includes vm3's Error Prone fix from the CI round; that round's report is on this branch). Ticket: SOLR-18010.

## Work

1. Read the whole diff against its base and the ticket. Reconstruct the concurrency claim precisely: which two operations could interleave on the base, what the branch serializes, and what it deliberately does not serialize (for example across processes or nodes). State the boundary in the report.
2. Check the atomic-write path: temp file location and sync behavior, rename semantics on the filesystems Solr supports, and what a crash at each step leaves behind. The owner's premise standard applies: a narrow fix for a demonstrated failure is in scope; hardening against a hypothetical crash is a finding to name, not to silently expand.
3. Run the focused security handler tests at the head, counted from fresh XML, and at least one leg against the base production code for the interleaving scenario, or state why the scenario cannot be reproduced in a test and what stands in for it.
4. Verdict: OPEN-READY, FIX FIRST (fixes specified), or WON'T DO, with the patch loop for in-scope defects.

## Deliverable

`reports/pool-pr5118-regression-review-round-1.md`: the concurrency boundary, the crash-at-each-step accounting, run counts, and the verdict.

## Appendix: what the existing evidence covers (2026-10-23)

The receipt already records base failures for the reader and overlapping-write tests, focused security-handler runs, mutation checks, and crash and disk-full probes. At the current head `d742eb99dd`, the failure-path test class passed 8/8 and Error Prone compilation passed. The module check was run at `46869c4d8b`; the later commits only changed tests. See [`receipts/SOLR-18010.md`](../receipts/SOLR-18010.md#L298-L317). Do not repeat the broad gate or the atomic-write and crash analysis unless this review finds a concrete reason.

The remaining question is narrower: does a pair of real edit requests lose one update? The existing `testConcurrentPersistConfLeavesOneWholeDocument` calls `persistConf` directly and asserts that the final file is one complete document. It does not assert that two request-level changes are both preserved. The current handler serializes `handleRequestBody` on one handler instance, so also state that it does not coordinate separate instances, processes, or nodes. See [`SecurityConfHandlerLocal.java`](https://github.com/nick-boss-tech/solr/blob/d742eb99dd28be761b5f60aea6fbfc832e1b7a50/solr/core/src/java/org/apache/solr/handler/admin/SecurityConfHandlerLocal.java#L63-L124) and [`SecurityConfHandlerTest.java`](https://github.com/nick-boss-tech/solr/blob/d742eb99dd28be761b5f60aea6fbfc832e1b7a50/solr/core/src/test/org/apache/solr/handler/admin/SecurityConfHandlerTest.java#L204-L256).

For this review, test only that remaining request-level question. If it can be reproduced through the supported request path, add a deterministic test showing whether both independent changes survive, run that focused test at the live head and against base production code, and report the fresh counts. If it cannot be reproduced through that path, explain the boundary with code evidence. No broad re-gate is needed unless source code changes; if it does, follow the normal gate rules. Do not change PR #5118's state or post comments. Edit its body only if this review changes a claim, as already allowed in Limits, and record that edit.

## Limits

Pushes to the branch are authorized (it is the owner's own open PR). No PR state change, no comments, no Jira edits. If the work changes the PR's claims, update the PR description to match and record the edit in the report; description edits on the owner's own PRs carry standing authorization. No em dashes. Commit as Nick Shanin with no trailers.
