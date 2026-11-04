# Gate job: PR 5287 configset repository upload hardening, round 2

Status: OPEN. Supersedes `gates/pr-5287-configset-upload-hardening-round-1.md` (round 1 verdict: GATE FAILED at `04e570d342`, receipt `receipts/PR-5287-configset-upload-hardening-round-1.md`).

- Branch: `pr-5287-configset-source-validation`. Head: `d0cf2471f37ae973ca06390ec195cf51f8fccbb3` (fork tip confirmed through the GitHub API on 2026-11-04). Stop if the fork tip differs and reconcile before running.
- Contributor parent: `19f7c83812a4c81227601ff1abadca84beb05cb9`. PR base: `ad26fdf2c6bee8168eae043b94833f05643d1995`.
- What changed since round 1: commit `8899ebd9d7d8a9985aef5d3c29b5f9b5189d5ee9` applies the tidy formatting round 1 required; commit `d0cf2471f37ae973ca06390ec195cf51f8fccbb3` makes the two symlink tests runnable under the test security manager (the test policy in `gradle/testing/randomization/policies/solr-tests.policy` gains LinkPermission "symbolic" next to the existing "hard" grant, and the LocalFS test creates its link with a default-filesystem target path). Diff scope against the contributor parent is now 9 files: the round 1 seven, plus `gradle/testing/randomization/policies/solr-tests.policy` and the tidy and test fixes inside two of the original seven.
- Focused classes and seed `5287C0DE` are unchanged from round 1. Steps 0 to 6 are the round 1 steps at the new head.
- Known environment limit, carried from round 1: the S3 suite's mock is a Docker testcontainer and skips itself without Docker; no pool host has Docker. The S3 legs are recorded as environment-blocked, and the shared dot-directory and exclusivity test code is proven under the LocalFS and GCS providers. A fully green S3 leg needs a Docker-capable host or upstream CI.
- Receipt reserved: `receipts/PR-5287-configset-upload-hardening-round-2.md`, written by the executing gate host.
- No public comment, no PR body edit, no JIRA as part of this job. Commit board records as Nick Shanin with no trailers. Use no em dashes.
