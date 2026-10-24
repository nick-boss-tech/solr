# SOLR-18556 documentation and status reconciliation, round 4

Assignment: `assignments/cuvs-lucene-07-solr-docs-round-4.md`. Claim: `claims/cuvs-lucene-07-solr-docs-round-4-vm2.md`. Drafts only: no Solr source edit, no pin, no guard, no Jira or GitHub write, no shared draft or index edit.

## Current state (checked 2026-10-24)

- Solr source: upstream main `13c693d805f`. The two sentences in question were added by merged PR #5182 (`1bd53870036`, "Document cuvs module breakage in Solr 10.1"). They are still present on main:
  - `solr/solr-ref-guide/modules/upgrade-notes/pages/major-changes-in-solr-10.adoc` line 104: "This is expected to be fixed in a subsequent Solr 10.x release."
  - `solr/solr-ref-guide/modules/query-guide/pages/dense-vector-search.adoc` line 1047: "...does not work in Solr 10.1 (see SOLR-18556), and is expected to be fixed in a subsequent Solr 10.x release."
- SOLR-18556 (Jira, live read): Open, reporter and assignee Rahul Goswami, no fix version, no comments, last updated 2026-10-23. The ticket carries no fix commitment, so the docs sentences promise more than the ticket does.
- cuVS side: latest cuvs-lucene 26.10.0 still calls `getSortedNodes`, and every release declares lucene-core 10.2.0 (evidence in `reports/solr-18556-round-1.md`, unchanged by this round). A compatibility fix for the library is being prepared upstream by the Windows implementation lane (`assignments/cuvs-lucene-lucene104-upstream-fix-round-1.md`). That fix is not released or in a Solr branch.
- The CPU checks (`receipts/CUVS-LUCENE-PR2702-CPU-ROUND-2.md`, `receipts/CUVS-LUCENE-BACKCOMPAT-ROUND-3.md`) cover the library's test files only. They do not validate GPU construction or search, native integration, or Solr integration. They do not reproduce or fix SOLR-18556 in Solr.
- Active Solr work: no open Solr branch or receipt for SOLR-18556 was found on the board. Open upstream PRs touching these two doc files were not checked against the GitHub API in this round, so that is an open check before any PR.

## Obsolete draft dispositions

- `jira/SOLR-18556-comment-draft-round-1.md` (vm6): superseded. Its options section is replaced by round 2, and its evidence is confirmed here.
- `jira/SOLR-18556-comment-draft-round-2.md` (phone agent): superseded by `jira/SOLR-18556-comment-draft-round-4.md`, but not withdrawn from the board. Two problems to fix before anyone posts from it:
  - Its footer says "(reviewed by Nick Shanin)". Nick has not reviewed it, so the footer is inaccurate until he does.
  - It says "I will put up a small PR correcting the two sentences". That is a commitment to public work, which needs Nick's decision, so round 4 presents the correction as a proposal.
- The round-2 draft's evidence bullets were checked against the sources above and remain accurate.

## Recommended route

1. Replace the two sentences with the proposed wording below. They say that no Solr 10.x fix is scheduled, and they drop the date-like "subsequent release" promise. They do not mention a release date, a pin, or a guard.
2. Post one Jira comment, the round-4 draft, after Nick reviews it and confirms the addressee. Do not post round 2 as well.
3. Keep the Solr-side guard deferred. The upstream compatibility route is still viable, so a guard would be a behavior change that needs a maintainer's request. If the upstream route is abandoned, that is a new owner decision with new evidence.
4. Ask Nick to decide on the docs change (below).

## Proposed wording (diff)

The patch is `material/solr-18556-docs-round-4/proposed-docs.patch`, against upstream main `13c693d805f`, SHA-256 `aa14cd92cdf676e2cb65a8cebf04ff8382e1c5baf40d8faa536d2be338958fb9`. It applies cleanly with `git apply --check --whitespace=error-all`, and the result matches the two edited files. It is docs-only: no code, pom, or test change.

## Owner choices

- A. Post the round-4 Jira comment: approve, after Nick's review and the addressee check. Addressee: Rahul Goswami is the reporter and assignee. Whether he is the committer sponsor under `comment-formula.md` is not verified from here, so Nick should choose the addressee.
- B. Docs change: approve a fork branch and a PR carrying `proposed-docs.patch`, or hold. Target: a fork branch from upstream main, then a PR after an open-PR check on these files. Proof for a docs-only change would be the Solr ref-guide formatting check, which has not been run here.
- C. Round 2 draft: withdraw it from the board, or leave it marked superseded. This round does not withdraw it.

## Limits

No Solr source edit, guard, or pin. No Gradle. No Jira or GitHub write. Public text has no dashes and no internal process vocabulary. Nick has not reviewed the drafts.
