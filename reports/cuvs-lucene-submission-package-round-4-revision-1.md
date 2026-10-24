# cuVS-Lucene submission package, round 4 (step 02), revision 1

Status: drafts corrected, nothing posted. Claim `claims/cuvs-lucene-02-submission-package-round-4-revision-1-vm2.md`. Supersedes the target and wording in `reports/cuvs-lucene-submission-package-round-4.md`, `drafts/cuvs-lucene-first-contact-round-4.md` and `drafts/cuvs-lucene-technical-handoff-round-4.md`. Those records stay as published; they are add-only. Do not post them.

## What changed

The first revision recommended PR 2867 as the first-contact target. That was wrong. Checked live on 2026-10-24:

| Item | PR 2702 head `23004d0f0de` | PR 2867 head `1c6bc921126` |
| --- | --- | --- |
| Lucene module directories | `lucene-10.2`, `10.3`, `10.4`, `10.5` | `lucene-10.2` only |
| `compat/` modules | present (8 modules, including the two `TestLuceneCompat` bridges) | absent |
| Base | `efc7d30` (main) | main |
| Author's statement | comment by imotov (2026-10-07): splitting the PR into smaller PRs | description: "doesn't add any new Lucene versions and still releases only the Lucene 10.2 jar" |

The round-2 and round-3 evidence covers Lucene 10.2.2, 10.3.2, 10.4.0 and 10.5.1. Only PR 2702's head has those modules, so the step 01 patches apply to it unchanged. Running them on PR 2867 would test 10.2.2 alone and drop the 10.3 to 10.5 evidence.

The first revision also said PR 2867 "removes" the `compat/` modules. That was wrong: PR 2867 is based on main and never contained them.

## Decision ladder record

- Target choice. Rung 1: module layout at both heads, above. Rung 3: the roadmap (`material/cuvs-lucene-next-steps-round-4.md`) and the phone-agent decision (`reports/cuvs-lucene-round-4-decision-round-1.md`) both name PR 2702 as the production compatibility implementation. Settled at rung 3: the target is PR 2702. Recorded in the revision claim.
- Whether to port the tests to PR 2867 now. Rung 1 settles it: a port would test only Lucene 10.2.2 and would drop the multi-version evidence. Not done.
- Whether to revise the published drafts in place. Rung 3: the board is add-only and existing records are immutable. Revision is published as new files with a new claim.
- Whether to post. Rung 5: posting a comment is public, and the owner's approval is required. Set to NEEDS-OWNER below.

## Revision outputs

- `drafts/cuvs-lucene-first-contact-round-4-revision-1.md`, SHA-256 `a8fea52cf1fa33908f1462a87ae7e8473b2cd823c2ae40d305cac61e2b073912`. Target PR 2702. One question: whether the tests should be included in this PR. It notes that a port follows only if PR 2867 lands first.
- `drafts/cuvs-lucene-technical-handoff-round-4-revision-1.md`, SHA-256 `dfa9a790c02e48617f1b208ea235703f8cd0c1b2caaf5ee56f035b76102522c0`. Same evidence sections as before; identity and not-covered sections corrected.

The original step 02 manifest is unchanged and still lists PR 2867 as "recommended". Read this revision report as its correction.

## Owner decision packet (NEEDS-OWNER)

1. Target: PR 2702 (this revision, rung 3). Confirm or redirect.
2. Approve posting `drafts/cuvs-lucene-first-contact-round-4-revision-1.md` on PR 2702, after Nick reviews the text and the "reviewed by" header. The header stays proposed until then.
3. Patch identity: `combined-tests.patch`, SHA-256 `84688de57719d0600505e42f4a62bfbd8b49373486ee90f6c4d9efbeff73ee5c`, applies to PR 2702 head `23004d0f0de`.
4. Delivery method: none in the first contact. If imotov asks, choose between attaching the patch file to the comment and a fork branch for preservation. A fork branch is not a PR.

## Dependencies

- If PR 2867 lands before the tests are included, PR 2702 must be rebased onto it. The test files then need a port to the new layout and a new CPU run on the exact new head. That is a later step, not part of this approval.
- Step 03 readiness review must come from a host other than vm2, which prepared steps 01 and 02.
- The stale round-2 draft `drafts/cuvs-lucene-pr2702-cpu-review-round-2.md` must not be posted. It says the Linux proof is pending, and its footer says "reviewed by Nick Shanin" before any review.

## Public-text check

- Revised drafts: no em or en dashes, no host names, paths, or board terms.
- Counts and blobs are copied from the step 01 receipts and manifest. The PR 2867 blob (`f6701a14`) and the module layouts come from GitHub at the heads listed above.

## Limits

- No build or test was run in this revision. The revision changes no test bytes.
- Nick has not reviewed the drafts. The "reviewed by" wording stays proposed.
- No comment, PR, Jira post, or fork push was made.
