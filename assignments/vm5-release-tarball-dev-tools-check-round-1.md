# Assignment: release tarball check for the dev-tools export-ignore question, round 1

Status: OPEN

- Intended host: vm5 (it holds the RC3 smoke loop and the release-check setup). Claim as `claims/vm5-release-tarball-dev-tools-check-round-1.md` before starting.
- Capability tags: `records`.
- Source: PR #5222 (SOLR-18119: Exclude dev-tools from RAT checks) and David Smiley's comment on it, 2026-10-24: https://github.com/apache/solr/pull/5222 (issue comment, 19:17:20Z).

## The question

`.gitattributes` export-ignores `/dev-docs`, which is what keeps dev-docs out of the source release tarball (`git archive`); its comment ties that to license header requirements. `/dev-tools` is not export-ignored, so dev-tools ships in the tarball today. Smiley asks: if `/dev-tools` were added to `.gitattributes`, would that impact the ability to do a release?

The owner-side trace says no: nothing in the build reads dev-tools (on main the only build reference outside dev-tools is rat-sources.gradle's own excludes), and the release wizard and smoke tester run from a checkout (dev-docs/releasing.adoc), not from the tarball. This assignment replaces the trace with a run.

## The tree to test

The proposed end state, built locally and never pushed:

1. Start from the fork branch `solr-18119-rat-exclude-dev-tools` at `58d9573ae0e20e492f9bbb490415e750aa9e38c3` (main plus the RAT exclusion from PR #5222).
2. On a scratch branch, add one local commit that adds the line `/dev-tools export-ignore` to `.gitattributes`, beside the existing `/dev-docs` line. This commit is test scaffolding. Do not push the scratch branch anywhere, and do not add the line to the PR branch.

## Method

1. `git archive` the scratch head into a tarball. List its contents and record: dev-tools absent, dev-docs absent, and the top-level layout otherwise unchanged versus an archive of the unmodified PR head (diff the two listings; the only differences should be the dev-tools tree and nothing else).
2. Unpack the tarball into a clean directory with no `.git`. This is the no-git case the RAT fallback scan exists for.
3. From the unpacked tree, using the same toolchain handling as the RC3 loop (record how the Gradle wrapper and properties were supplied, since the archive is not a checkout):
   - Run the RAT task. It must pass in fallback mode.
   - Run the build check the tree supports: `check -x test` for the root project if the host's budget allows, else `:solr:core:compileJava :solr:core:compileTestJava` plus `:solr:distribution:assembleBinary`, and say which was run.
4. If anything fails, determine whether the failure names a dev-tools path or a file that dev-tools would have supplied. A failure with no dev-tools connection is reported as unrelated, with its own evidence; do not fix either kind in this assignment.

## Acceptance

- The archive listing diff shows dev-tools as the only removed tree.
- RAT passes from the unpacked tree.
- The build check run passes, or its failure is classified per step 4 with evidence.
- The report's verdict line answers Smiley's question in one sentence: export-ignoring dev-tools does or does not affect building and checking the source release, on this evidence.

## Limits

- No pushes: not the scratch branch, not the PR branch, nothing to the fork or upstream.
- No GitHub, Jira, or dev-list posts. The result goes in the report; the owner decides what is said on the PR.
- Do not change PR #5222 or PR #5180 in any way.
- Use the host's normal build lock and disk discipline; the unpacked tree and tarball are scratch and may be deleted after the report is filed, but record their sizes and the archive listing in the report first.

## Deliverable

- `reports/vm5-release-tarball-dev-tools-check-round-1.md`: the two archive listings (or their diff), the exact commands, the RAT and build outcomes with log paths, the failure classification if any, and the one-sentence verdict.
