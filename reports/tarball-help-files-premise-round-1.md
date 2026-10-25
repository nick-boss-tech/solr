# Tarball help-file premise (round 1)

Verdict: the premise holds, and it is a regression. `/dev-docs export-ignore` (commit `dd5dc65d45`, PR #4551, 2026-07-04) removes the 11 help files that `./gradlew check` requires from the source tarball. Releases 10.0.0 and 9.10.1 predate that line, and their archives include the files.

## Verification (main at `07fdebdda5`)

1. **Code.** `gradle/help.gradle` lists 11 files under `dev-docs/gradle-help/`, and line 70 has `check.dependsOn allHelpFilesExit`. That task throws "Help file missing" for any absent file. The check dates from `27f4b02ab4` (2019-12-03).
2. **Release artifact.** `solr/distribution/source-release.gradle` builds the source tarball with `git archive --format tgz --prefix solr-${version}/ HEAD`. The `.gitattributes` export-ignore rules therefore apply to the official source tarball.
3. **Archive listing.** `git archive 07fdebdda5` has 10,034 entries, none under `dev-docs/`. None of the 11 required files is in it.
4. **History.** `git log -S'/dev-docs' -- .gitattributes` gives `dd5dc65d45` (2026-07-04, "Exclude some files from the source release", #4551). At tags `releases/solr/10.0.0` (`28c1bc1f9394`) and `releases/solr/9.10.1` (`a51f9879faa9`), `.gitattributes` has no dev-docs line, and the archive contains all 11 files.
5. **Published sources.** Mirrors of `solr-9.10.1-src.tgz` and `solr-10.0.0-src.tgz` list `dev-docs/` members, which matches step 4.

Note: `git check-attr export-ignore` reports `unspecified` for each dev-docs path, because the rule applies to the directory as a whole. The archive listing is the reliable check.

## Correction to earlier notes

The vm5 report (`reports/vm5-release-tarball-dev-tools-check-round-1.md`) calls the `:allHelpFilesExit` gap "pre-existing". It is not: it came in with #4551 on 2026-07-04. PR 5222 does not change it. PR 5222 only adds the `dev-tools` line.

## Search for prior notice

- Jira text search, "allHelpFilesExit": only LUCENE-9793 (Closed, unrelated).
- Jira text search, "Help file missing": generic hits from other projects.
- Jira text search, "gradle-help" with "tarball", and "help.gradle" with "release" or "export-ignore": no Solr ticket about this failure. Closed hits (for example SOLR-17563, "Cleanup and merge dev-docs") were not read.
- GitHub search, "allHelpFilesExit" and "gradle-help export-ignore" on apache/solr: PR 5222 only.
- Web search of the dev list archive for the failure text: no hit.

Conclusion: no ticket or thread found, so the defect appears untracked. A ticket draft is at `drafts/jira-ticket-tarball-help-files-draft.md`. Filing it is the owner's act.

## Fix shapes

A. **Un-ignore the help files.** Add `dev-docs/gradle-help/** -export-ignore` to `.gitattributes`, keeping `/dev-docs` ignored otherwise. Cost: 11 small `.txt` files ship in the source tarball. `gradle/validation/rat-sources.gradle` excludes `**/*.txt`, so this creates no license-header obligation. The build code is unchanged. This restores the 10.0.0 tarball state.

B. **Scope the check to checkouts.** Skip `allHelpFilesExit` when the help files are absent. Cost: the check no longer runs from a tarball, and the `:help*` tasks still fail there. One condition in `gradle/help.gradle`.

C. **Move the help files into a shipped tree** (for example `gradle/help/`) and point `help.gradle` at it. Cost: the largest change, with path updates in `dev-docs` docs.

## Recommendation: A

- It honours the intent of #4551. That rule keeps ASF license headers out of files that need them. Plain-text help files are not scanned by RAT, so shipping them does not reopen the problem.
- It is one line, and it restores the state of the 10.0.0 tarball, which passed the check.
- B leaves the help tasks broken in any tarball build.

## Open items

- Not verified: whether any release step runs `check` against the tarball. vm5's assignment says the release wizard and smoke tester run from a checkout (`dev-docs/releasing.adoc`), which would make the failure latent for releases.
- For maintainers: should the help text ship in the source tarball? The author of #4551 (David Smiley) is the person to ask.
