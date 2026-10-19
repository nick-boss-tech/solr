# Skip record: SOLR-15521

- Ticket: SOLR-15521, Cleanup gitattributes and align to spotless config. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2021-07-06, updated 2021-09-28, no fix versions, no links, no comments.
- Verdict: close-fixed.
- Draft branch: solr-15521-draft-close (new; supersedes the keep-open draft solr-15521-draft-open, which is kept).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read at this head (detached worktree).
- Source: remediation redo of batch 03, item 5, under assignments/pool-aaa-skip-verdict-remediation-03.md. The earlier record (round audit-1, 2026-10-04, confirmed-skip) is background only.

## Ticket reading

The reporter found that solr/.gitattributes sets `* text=auto`, so on Windows checkouts Java files get CRLF endings, while spotless writes LF. Spotless then flags every Java file on a Windows checkout. The ticket asks for LF for Java files and, ideally, for all text files, with batch files excepted.

## Code reading

- solr/.gitattributes:31: `*.java     text eol=lf`. This is the rule the ticket asks for, on Java files. It is present at the pinned head.
- solr/.gitattributes:17: `*          text=auto` still applies to other text files. Spotless in this build checks Java only (gradle/validation/spotless.gradle:28-38 sets UNIX line endings and endWithNewline for Java), so the remaining text files are not checked by spotless.
- solr/.gitattributes:26-28: `*.bat`, `*.cmd`, `*.ps1` are `eol=crlf`, which is the exception the ticket names for batch files.

## History

- The fixing commit is 65e5f9c106 (2021-07-06), "Fix line endings and enforce LF for all java files". It changes solr/.gitattributes (3 insertions, 1 deletion). `git merge-base --is-ancestor 65e5f9c106 498a09aea97aac86c2cc7ac114113b946d249942` succeeds, so the fix is on the pinned head.
- The changed rule is still on the head: `git show 498a09aea97aac86c2cc7ac114113b946d249942:solr/.gitattributes` shows line 31.

## Reproduction attempts

Not run. A reproduction would need a Windows checkout with core.autocrlf and a spotless run, which this environment does not have. The fix is a line in a gitattributes file, and the code path shows it applies to the files the spotless check reads.

## Gap analysis

The reported problem is the Java violation on Windows checkouts. 65e5f9c106 fixes that for every `*.java` file, and the rule is still on main. Two parts of the ticket are not covered by that commit: (1) the broader request that all text files get LF (the root `text=auto` still applies to them), and (2) the request that people configure their git. Neither is a Java build failure, and the ticket has had no maintainer discussion since 2021. The record does not claim those parts are done.

## Verdict justification

close-fixed: a commit on main (65e5f9c106) fixes the reported Java problem, it is an ancestor of the pinned head, and the changed rule is present at the head (solr/.gitattributes:31). The broader text-file request is not covered by this verdict; if the maintainers want it, it is a new ticket or a PR, not part of this closeout.

Limits of the close-fixed check: the run that proves the Windows checkout behavior was not done.

## Limits

This record does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
