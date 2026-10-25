# Jira ticket draft: source tarball omits dev-docs/gradle-help, so check fails at allHelpFilesExit

Status: DRAFT, not filed. Filing needs the owner's go.

Summary: Source tarball omits dev-docs/gradle-help, so ./gradlew check from the tarball fails at allHelpFilesExit

Description:

Since PR #4551 (commit dd5dc65d45, 2026-07-04) added "/dev-docs export-ignore" to .gitattributes, git archive (used for the source tarball in solr/distribution/source-release.gradle) no longer includes dev-docs/. gradle/help.gradle lists 11 files under dev-docs/gradle-help/ and check.dependsOn allHelpFilesExit, which fails when any is missing. At releases/solr/10.0.0 and releases/solr/9.10.1 the archive included all 11 files.

Reproduce: unpack git archive HEAD of main into an empty directory with no .git, then run ./gradlew check. It fails at :allHelpFilesExit with "Help file missing: dev-docs/gradle-help/workflow.txt".

Suggested fix: un-export the help files with "dev-docs/gradle-help/** -export-ignore" in .gitattributes. They are plain text, which RAT does not scan, so no license-header obligation is created.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
