# Skip record: SOLR-15970

- Ticket: SOLR-15970, "Release Wizard must clean up staging artifacts after a failed RC". JIRA status Open, resolution none, fix versions none, created 2022-02-02, updated 2022-05-12. Checked 2026-10-19 UTC.
- Verdict: keep-open (changed from close-fixed; this redo supersedes the recorded verdict).
- Draft branch: solr-15970-draft-open (the earlier close-fixed branch solr-15970-draft-close is not updated).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-15970-vm2.md).

## Ticket reading

When the release wizard aborts an RC ("Abort RCX and start a new RCY"), the RC's artifacts stay in the dist staging area (dist.apache.org dev). The ticket asks for a way to clean them up on abort, for example by tagging TODO steps as cleanup so the abort logic offers to run them. It notes that the same applies to Lucene and Solr-Operator. The report has no comments and names no test class.

## Code reading

- dev-tools/scripts/releaseWizard.py:481-482 (origin/main 498a09aea97): new_rc() asks "Are you sure? This will abort current RC" and then calls maybe_remove_rc_from_svn().
- dev-tools/scripts/releaseWizard.py:269-283: maybe_remove_rc_from_svn() removes the RC folder (`svn rm`) only if the import_svn TODO is done (line 270-271). If the upload was not done, nothing is removed.
- dev-tools/scripts/releaseWizard.py:468, 999 and 1146: the same function is called from other paths (clear and TODO-state handling), so the cleanup is not limited to one menu.
- dev-tools/scripts/releaseWizard.yaml:1105: the maven and docker artifacts are removed in a separate, later step (`svn rm ... maven`). That step is not run on abort; the abort path removes only the RC folder.
- dev-tools/scripts/releaseWizard.yaml:1126: "Clean up the RC folder" is in the release-move path, not the abort path.

Commands at the pinned head:
- git grep -n 'maybe_remove_rc_from_svn' origin/main -- dev-tools/scripts/releaseWizard.py: definition at 269 and four call sites.
- git log --format='%h %ad %s' --date=short -S 'maybe_remove_rc_from_svn' origin/main: 87c131baa73 (2019-06-20, LUCENE-8852 ReleaseWizard tool), an ancestor of the pinned head.
- git grep -n 'svn rm' origin/main -- dev-tools/scripts/releaseWizard.yaml: lines 1105 (maven and docker), 1126 (RC folder, release path), 1878 (old releases).

## History

- 87c131baa73 (2019-06-20): the wizard's initial commit, with the abort-path cleanup. It predates the ticket (filed 2022-02-02). No later commit names SOLR-15970 or changes the abort path.

## Reproduction attempts

Not attempted. The wizard needs an SVN dist server and credentials to run the steps, so the behaviour cannot be exercised in this container. The read is from the code.

## Gap analysis

The abort path removes the RC folder only when the upload step (import_svn) is done. An RC aborted before the upload leaves no folder to clean up, so the case the ticket describes is covered only for uploaded RCs. The maven and docker artifacts from the staging step are not cleaned on abort. The ticket's proposed mechanism (TODO tags that the abort logic offers to run) is not in the code. The cleanup that does exist predates the ticket, so the reporter's case is either one of the uncovered ones or from an older release; the report does not say which.

## Verdict justification

keep-open meets the rubric: the pinned head sha is cited, the Jira status and date are read in this pass, code paths are cited with file and line, and the attempt and the missing piece are listed. close-fixed does not hold: the commit that adds the abort-path cleanup (87c131baa73) predates the ticket, so no fix commit follows the report. The missing pieces are (1) the case that leaves artifacts after an abort (an RC aborted before upload, or the maven and docker artifacts), confirmed with the release manager, and (2) a decision on whether the abort path should also remove the staging artifacts. A close verdict needs a fix commit after the report, or a documented answer; a pr-needed verdict needs the case and a change.

## Limits

This record does not run the wizard. It does not check the Lucene or Solr-Operator wizards the ticket mentions. It does not confirm which failure the reporter hit.
