# Assignment: prepare the cuvs-lucene upstream submission package, round 4

Status: HANDED (waiting for step 1's verified contribution package)

- Intended host: any review/draft host.
- Capability tags: review, draft, records.
- Claim: `claims/cuvs-lucene-02-submission-package-round-4-<host>.md`, after step 1 delivers.
- Dependency: `assignments/cuvs-lucene-01-consolidate-round-4.md` and its resulting immutable package.
- Shared context: `material/cuvs-lucene-next-steps-round-4.md`.

## Objective

Prepare everything Nick and a reviewer need to decide on a small test contribution to the existing NVIDIA/cuvs PR 2702. Nothing is posted publicly. Replace stale proof-pending prose with a new draft that accurately cites both independent Linux results; do not edit the old drafts or reports.

## Work

1. Resolve step 1's published package, base, four changed files and manifest. Verify both GREEN receipts and their underlying logs/results. If step 1 adapted source, require its matching replacement receipt before claiming READY. Read current upstream PR state and discussion to avoid proposing work already adopted or rejected.
2. Read comment-formula.md and, if proposing a PR body, pr-formula.md. Prepare a short first-contact GitHub comment offering the CPU coverage and asking whether the author wants it included. The production compatibility work belongs to imotov's PR. Do not ask for a review commitment before engagement or open a competing implementation PR. Default to a concise evidence offer; keep patch delivery as a separate, concrete proposed method rather than inventing a public branch URL.
3. Prepare a longer technical handoff for an engaged maintainer: exact source/patch identity, four-file summary, commands/toolchain, test counts, mutation versus data-fault controls, and what remains GPU/native/package/Solr work. Link public-facing evidence only where its content and disclosure have been reviewed. Remove local paths, host names and internal board/process terminology from proposed public text.
4. State results by scope: round 2 has 16 positive method executions plus two mask-drop controls; round 3 has 12 positive method executions over 138 fixture cases plus two data-fault controls. Do not combine these into a claimed 28-test invocation. The compact round-3 compile matrix came from stdout; check it against the log without rerunning compilation just to change report format.
5. Recheck upstream CONTRIBUTING and commit requirements at the intended submission revision. Propose Nick Shanin commit subjects and author identity without trailers. If a mandatory DCO requirement actually conflicts, cite it and record one owner decision; never add Signed-off-by automatically. AI disclosure must follow current workspace policy. Label reviewed-by-Nick wording as proposed until Nick actually reviews it.
6. Provide a complete owner decision packet: exact target URL, proposed comment file and digest, patch/package identity, delivery method, readiness dependencies and the one approval needed. A request to post a comment is not permission to create/push a public source branch or open a PR.

## Deliverables and acceptance

- `drafts/cuvs-lucene-first-contact-round-4.md`.
- `drafts/cuvs-lucene-technical-handoff-round-4.md`.
- `material/cuvs-lucene-contribution-round-4/submission-manifest.json`, added without changing step 1's files.
- `reports/cuvs-lucene-submission-package-round-4.md`, including the policy check and recommended route.

Accept when all claims trace to exact evidence, the proposed first contact is concise, public text contains no em/en dashes or internal process vocabulary, no unfounded fix date/GPU claim is made, and step 3 has concrete files and hashes to review. Keep a proposed PR body under about 3,500 visible characters if one is genuinely needed; no checklist. Do not claim the contribution is Nick-reviewed yet.

## Limits

Read-only source work and add-only board drafts. No build/test rerun, upstream write, source push, PR creation, Jira comment or live-body edit. Public approval is step 4, after readiness review.
