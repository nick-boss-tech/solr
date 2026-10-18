# Retire pack: SOLR-18119-submit (branch solr-18119-submit, PR #4999)

Verdict: RETIRE CLEAN, CONDITIONAL. Retire-clean only while the shipped pair lands or is accepted. The condition is not yet met, so do not close PR #4999 or delete the branch yet.
Round item: reports/retire-candidates-review-round-rr2.md, item 2. Checked 2026-10-15.

## Premise state

receipts/SOLR-18119.md, section "solr-18119-submit (superseded)": "Status: RETIRE CANDIDATE, superseded by solr-18119-jvm on 2026-10-08. No gate at its live tip in this line." It adds that the PR "is still open; closing it is a public action and an owner call."

## Head checked

- ls-remote origin refs/heads/solr-18119-submit: 723022d35fcede32de2a884c29e5aa0ff08e3f1f (2026-10-15).
- PR #4999 (apache/solr, GitHub API, 2026-10-15): state OPEN, headRefOid 723022d35fcede32de2a884c29e5aa0ff08e3f1f, reviewDecision CHANGES_REQUESTED.

## What the branch holds, and whether main has it

- One file against merge-base 0d2a4649c79: gradle/documentation/changes-to-html.gradle (+10/-9). It resolves python3 through project.externalTool, fails with a GradleException when python3 is missing, and honours -Ppython3.exe.
- Live main still has the base Python route. `git-source.ps1 grep -n python3 upstream/main -- gradle/documentation` shows changes-to-html.gradle lines 77 and 110 (2026-10-15). The branch's hunk is not on main (rr2).

## The pair

- solr-18119-jvm, head 660faedd026ddc77f8736a4d1ab5cb89a4253a4a, PR #5061 (apache/solr, OPEN, no review decision yet, checked 2026-10-15). It ports the changelog-to-HTML converter to Java (ChangesToHtml.java, ChangesToHtmlTask.java), deletes changes2html.py, and has no python reference in gradle/documentation/changes-to-html.gradle (`git-source.ps1 grep -n python origin/solr-18119-jvm -- gradle/documentation/changes-to-html.gradle` returns nothing).
- solr-18523-submit, head 26678c3737ca6baa353609846b2f58749ab1448d, PR #5062 (apache/solr, OPEN, checked 2026-10-15). It does not remove the Python route. Its changes-to-html.gradle still holds the base Python code (rr2).
- The g6 hold, PR #5034 (graalpy python fallback, based on #4999), is CLOSED unmerged on 2026-10-09T04:46:01Z (GitHub API). That hold is no longer live.

## Gainable and taken

- Gainable: nothing, while #5061 lands or is accepted. The Java port replaces the route this hunk edits.
- Taken: nothing.

## Owner steps, in order

1. Close PR #4999 on apache/solr. This is a public action for the owner only. Its reviewDecision is CHANGES_REQUESTED (API, 2026-10-15). The review date, 2026-10-05T12:13:45Z, and the absence of a later review come from reports/retire-packs-round-1.md, not from a fresh read. The requested changes were applied at 723022d35fc (receipts/SOLR-18119.md). Draft closing comment, NOT POSTED: "Closing this in favour of #5061 (solr-18119-jvm). It ports the changelog-to-HTML converter to Java and removes the python3 route this PR edits."
2. Retire the branch. Under the owner's current rule (2026-10-14: a retired branch is labelled retired in the records and left in place, never deleted), label solr-18119-submit retired and leave it in place. Only if the owner chooses to delete instead: `git -C C:\Users\shaninna\dev\Solr-issues\source push origin --delete solr-18119-submit`. Here origin is the fork (nick-boss-tech/solr).
3. Jira: none for the submit branch. The jvm PR carries the ticket's single submission comment. Nothing is drafted.

## Condition and how the owner checks it

The verdict depends on the python3 route being removed from main by PR #5061. Check it before steps 1 and 2:

- `gh.ps1 pr view 5061 --repo apache/solr --json 'state,mergedAt,reviewDecision,headRefOid'`. Proceed when state is MERGED, or reviewDecision is APPROVED (accepted). Confirm headRefOid is still 660faedd026ddc77f8736a4d1ab5cb89a4253a4a.
- After it lands, refresh upstream/main, then `git-source.ps1 grep -n python3 upstream/main -- gradle/documentation`. The result must show no python3 call in changes-to-html.gradle. Today it shows lines 77 and 110.
- If #5061 is closed without landing, this hunk becomes the only Python fallback. Do not close #4999 or delete the branch. Revisit the verdict first. Also note that #5062 still carries the base Python route.

The round's wording says "the shipped pair." The reading here is that #5061 alone removes the route, and #5062 does not. The owner should confirm which reading governs.
