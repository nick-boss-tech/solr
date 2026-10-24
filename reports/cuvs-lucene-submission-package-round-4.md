# cuVS-Lucene submission package, round 4 (step 02)

Status: drafts done, nothing posted. Claim `claims/cuvs-lucene-02-submission-package-round-4-vm2.md`. Upstream state read live on 2026-10-24 through the GitHub REST API. No upstream write, PR, comment, or fork push was made.

## Result

Four deliverables: a first-contact draft, a technical-notes draft, a submission manifest, and this report. Two live facts change the plan that the step 02 assignment assumed.

1. The step 01 test files target PR 2702's `compat/` layout. PR 2867, which imotov opened from the same work, is the live vehicle and has two approvals (2026-10-22). It is based on main and uses per-version Lucene modules instead of `compat/`. The step 01 patch does not apply to PR 2867's head.
2. The step 01 file `TestBackCompatIndices.java` shares a path with a different file in PR 2867 (blob `f6701a14` against our final `ee3cd673`). Both cannot land unchanged.

So the test contribution is not ready to offer as a patch against the live head. It needs a port and a new CPU run first. The first-contact draft says this, asks one question, and sends no patch.

## Verified upstream state (2026-10-24)

| Item | State |
| --- | --- |
| PR 2702 | open draft, base `efc7d30`, head `23004d0f0de` (the step 01 base). Has `compat/` modules. |
| PR 2867 | open, not draft, head `1c6bc921126`. Based on main. No `compat/` tree. Approvals: paul-aiyedun (2026-10-22 20:42Z), cjnolet (2026-10-22 21:46Z). Merge state blocked. |
| PR 2769, 2771 | merged. Split from 2702 by imotov (comment on 2702, 2026-10-07). |
| `TestCompatKnnFloatVectorQuery.java` | in 2702 only. Absent from 2867. |
| `TestBackCompatIndices.java` | in both, different content. |

## Policy check

- Contributing: the repository's CONTRIBUTING.md points to the NVIDIA developer guide. That guide asks for unit tests with changes and requires benchmarks only for performance-critical paths. These tests do not touch such a path.
- DCO: no DCO workflow or sign-off wording at base `23004d0f0de` or head `1c6bc921126` (PR template, `.github/workflows/pr.yaml`). No conflict with the no-trailers rule, so no owner decision is needed on DCO. No sign-off is added.
- Commit identity: Nick Shanin, no trailers. Proposed subjects are in the manifest, scoped to the 2702 base only.
- AI disclosure: GitHub comment header only; PR description header and one-line footer; Jira footer only. The "reviewed by Nick Shanin" wording is proposed in every draft until Nick reviews the text.
- Stale draft: `drafts/cuvs-lucene-pr2702-cpu-review-round-2.md` says the Linux proof is pending. It also says all three new cases fail on each mutation, but the mutation ran on Lucene 10.2.2 and 10.4.0 only. Its footer says "reviewed by Nick Shanin" before any review. The new first-contact draft replaces it. The old draft is not withdrawn (board records are add-only); it should not be posted.

## Recommended route

Option A (recommended): post the first-contact comment on PR 2867 after Nick reviews it. The comment leads with the evidence, names the port need, and asks one question (include or keep separate). It asks for no review. If imotov wants the tests, a port to 2867's head starts as a new assignment, with a new CPU run on the exact ported head. Reason: 2867 is the approved, active PR. 2702 is the draft it was split from, so a comment there would reach a PR that is no longer the working one.

Option B: post on PR 2702 as the step's original target. Lower value; the author has moved to 2867.

Option C: hold until the port is done, then post with a patch. Delays the offer with no evidence gain.

## Owner decision packet

1. Target: PR 2867 (recommended) or PR 2702.
2. Comment: `drafts/cuvs-lucene-first-contact-round-4.md`, SHA-256 `cd5847f7d4c75d7d02e60ebacdca024c1781c8ad4500045f35e60cba8d39d0b8`. Header is proposed until Nick reviews.
3. Patch identity: `combined-tests.patch`, SHA-256 `84688de57719d0600505e42f4a62bfbd8b49373486ee90f6c4d9efbeff73ee5c`, against base `23004d0f0de`. Not applicable to 2867's head without a port.
4. Delivery method: none in the first contact. If imotov asks, choose between attaching the patch file to the comment and pushing a fork branch for preservation. A fork branch is not a PR.
5. Readiness dependencies: (a) port to 2867's layout if 2867 is the target; (b) a new CPU run on the exact ported head; (c) step 03 review by a host other than vm2, which prepared steps 01 and 02.
6. The one approval needed: approve posting the first-contact comment on the chosen PR, after Nick reviews its text and header.

Technical notes (`drafts/cuvs-lucene-technical-handoff-round-4.md`, SHA-256 `05672c525f229df423a2f2c18f38fccce3b83b31f2b13e42af0d30abe41dd457`) are for an engaged maintainer only. They are not to be attached or linked before Nick's review.

## Evidence scope

- Check A (query acceptance masks): 16 positive method executions over four modules, 0 failures. Mutation on 10.2.2 and 10.4.0: six expected failures.
- Check B (archived-index upgrades): 12 positive method executions across 138 fixture cases. Data-fault control on 10.2.2 and 10.4.0: two expected failures.
- The checks are not combined into one invocation count. The receipts are `receipts/CUVS-LUCENE-PR2702-CPU-ROUND-2.md` and `receipts/CUVS-LUCENE-BACKCOMPAT-ROUND-3.md`. Seed `18556C0D` is confirmed in both evidence sets.
- Not covered: GPU, native build, Solr, release packaging, and any run on PR 2867's head.

## Public-text check

- Public drafts contain no em or en dashes. Scan hits only in owner notes and the AI header line.
- Public drafts contain no host names, local paths, or board terms.
- Counts and digests were copied from the step 01 receipts and manifest, and the PR 2867 blob from the GitHub contents API at its head. No count was taken from memory.

## Limits

- No build or test was run in this step, per the assignment. The port to PR 2867 is not compiled or checked.
- Nick has not reviewed the drafts. The "reviewed by" wording stays proposed.
- No target is chosen: the recommendation is an owner decision.
