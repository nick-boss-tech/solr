# Pool assignment: SOLR-12505 re-gate check and remaining internal blocks, round 1

Capability tags: `review`. Intended host: windows. Staffing: one read-only subagent for SOLR-12505, and the review agent for the remaining internal blocks. Claim: `claims/pool-regate-12505-and-blocks-round-1.md`.

## Purpose

- vm2 claimed a gate at the new head for SOLR-12505 (`claims/pool-vm2-gate-backlog-12505.md`, head `5f20e1171bc8`, which equals the fork tip). The draft `pr-drafts/streaming/SOLR-12505.md` names an earlier head. Check the draft against the claim and the gate, and record the post-gate text.
- Seven drafts still carry owner-note blocks that the pattern list did not match. Under the convention that owner notes live in reports, move each block to `reports/internal-blocks-round-1.md` verbatim, then remove it.

## Checks

1. SOLR-12505: named heads in the draft against the claim head and the fork tip; links resolve at the claim head; the sentences the gate will change; internal blocks, quoted with line numbers.
2. Internal blocks in these drafts: `query-parsing/SOLR-11391.md` (line 1), `query-parsing/SOLR-12608.md` (line 1), `search-components/SOLR-15319.md` (lines 50 to 59), `search-components/SOLR-15331.md` (line 41), `search-components/SOLR-17051.md` (lines 56 to 63), `search-components/SOLR-17155.md` (line 21), `search-components/SOLR-6831.md` (lines 43 to 48). Copy each verbatim, check the boundary, remove.

## Hold rule

A draft whose named head will change at the re-gate is held at its current head.

## Rules

- Read-only subagent for the SOLR-12505 check. No builds, tests, Gradle, posting, PR writes, or gate-job edits.
- Block removals only where the verbatim copy is in the report first.
- No em dashes or en dashes in authored text. No PR numbers in file names or commit messages.

## Deliverable

`reports/regate-12505-and-blocks-round-1.md`, verdict first.
