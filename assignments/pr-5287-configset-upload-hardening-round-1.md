# Assignment: PR 5287 configset repository upload hardening, round 1

Status: OPEN

- Posted: 2026-11-04, by the Windows host at Nick Shanin's direct request.
- Capability tags: implementation, review, records; Linux proof requires gate, review, records.
- Intended host: Windows source implementation; any capable Linux gate host for exact-head proof.
- Category: existing PR follow-up.
- Target: [PR 5287](https://github.com/apache/solr/pull/5287), SOLR-18618, titled `ConfigSet upload from repository`.
- Contributor branch: `HoustonPutman:SOLR-18618-configset-upload-from-repository`.
- Reviewed contributor head: `19f7c83812a4c81227601ff1abadca84beb05cb9`.
- PR base: `ad26fdf2c6bee8168eae043b94833f05643d1995`; merge base: `1fdb455a524a7b8d334d5baea04e395ed4d2d7f8`.
- Board snapshot checked: `c8c2ab67967cd3c50b47fab32870d707e1454b6b`; no matching assignment, claim, gate, report, or receipt was present.
- Authority: Nick requested a scoped patch on a new branch of `nick-boss-tech/solr`, with a normal non-forced push, and a diff comment after exact-head Linux proof. Do not change Houston's branch, edit the live PR body, or post to JIRA.

## Items

1. **Enforce one source for V1 and V2.** Reject any non-empty request body when a repository source is selected, using stream reads rather than `InputStream.available()`. Add regression coverage for a stream whose `available()` is zero while data remains and for the V1 route.
2. **Validate local repository paths.** Reject symbolic-link entries while recursively importing a local repository and apply the allowed-path check to resolved local paths. Cover directory and single-file imports where relevant. This path is introduced by PR 5287; keep the change scoped to the repository upload path.
3. **Preserve dot-prefixed directories.** Import their files consistently with the ZIP upload path and add a focused cross-provider test.

## Decisions

| Decision | Answer or default | Condition and action | Owner |
| --- | --- | --- | --- |
| Include the local-path hardening | decided: yes, the repository importer is new in this PR | Reject symlink entries and validate resolved local paths in this upload path; do not broaden the change to unrelated backup consumers | Nick Shanin |
| Comment timing | decided: after Linux proof | Refresh PR state and check for an equivalent comment immediately before posting the tested diff | Nick Shanin |

## Deliverable

`reports/pr-5287-configset-upload-hardening-round-1.md`: findings addressed, exact file list, fork head, diffstat, Linux gate state, and remaining limits.

## Limits

- Use worktree `wt/PR-5287-configset-source-validation` from the verified contributor head and preserve contributor ancestry.
- Push only to `nick-boss-tech/solr:pr-5287-configset-source-validation`; never force-push or modify the contributor branch.
- Windows does not run Gradle or tests. Obtain exact-head Linux proof before treating the patch as ready for the PR comment.
- Keep the diff scoped to source validation, local repository path validation, dot-directory import, and their focused tests. Do not edit the PR body or JIRA.
- Commit as Nick Shanin with no trailers. Do not put `#5287` in the commit message.
