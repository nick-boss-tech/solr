# Merged-PR follow-up promise audit: SOLR-18119 pair (PR 5222 and PR 5180)

Posted by: phone agent, 2026-10-27, under the standing rule (audit our merged PR descriptions and comments for follow-up promises whenever another of our PRs merges). Both PRs merged 2026-10-27: 5222 at 15:28Z, 5180 at 16:25Z. Sources: the live PR bodies and comment threads via the GitHub API, and apache main's `.gitattributes` read after the merges. Filing a ticket or opening a PR from the open list needs the owner's go; this report files nothing.

## Discharged

- **Archive check promised on 5222.** Nick's comment (2026-10-24) told Smiley the `.gitattributes` question would be checked with a real archive and the result reported on the PR. Reported 2026-10-25 with the archive listings and the unpacked-tree runs.
- **Smiley's "okay lets add it then" (5222).** `/dev-tools export-ignore` is on apache main, `.gitattributes` line 11, beside `/dev-docs`.
- **The 5180 RAT-exclude drop.** Nick's comment (2026-10-25) said that once 5222 landed, 5180 could drop its two changes-to-html excludes in `rat-sources.gradle`. Done before the merge: the two RAT commits were stripped in the 2026-10-27 rebase, and the merged head carries no `rat-sources.gradle` change.

## Open

1. **Needless-headers guard for dev-docs.** On 5222, Nick offered Pugh two branches. Smiley answered for the guard (2026-10-25): "If it's a small/easy to change to the build to fail if there are needless headers, then okay. Otherwise, it's not worth automating IMO." The guard branch `solr-dev-docs-no-license-headers` exists on the fork at `445938b919`, unopened. State: a qualified maintainer yes, conditional on the change being small and easy. Next step is determining that on the branch; any PR opening still needs the owner's go.
2. **Header removal in export-ignored files.** Nick asked Pugh (2026-10-25) whether the ASF headers on 35 export-ignored files (25 under `dev-tools/`, 10 under `solr/monitoring/dev/`) should be removed for one standard, offering to do it as a follow-up. No answer from Pugh. Ball is in his court; nothing is owed by us unless he answers.
3. **Tidy license-headers branch.** `solr-tidy-license-headers` (fork, `fcb9c84f54`) was offered to Pugh in the same comment as item 2. No pick made. Same state.
4. **Jenkins smoke jobs (external).** The 5180 body warned that the `Solr-Smoketest-*` snapshot jobs need a step running `changes2html.py` before `assembleRelease`, or they fail; Nick repeated it to Smiley at merge time ("Please don't forget the Jenkins changes"). The job definitions are not in this repository, so this sits with whoever maintains those jobs. Nothing for us to file. If the snapshot smoke jobs fail after this merge, this warning is the first pointer.

## Not a promise

- The 5180 body's Limits lines (a direct `assembleRelease` without the generated file succeeds silently; no automated test runs `changes2html.py`) are stated limits with no follow-up offered.
- The `:allHelpFilesExit` tarball finding from the 5222 archive check is already pool work (`assignments/tarball-help-files-premise-round-1.md`), not a commitment made on the PR.
