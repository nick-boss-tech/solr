# Jira notes: stale Blockers (18240, 17988, 12833, 16450 POSTED 2026-10-23; 18297 dropped)

## SOLR-18297: DROPPED 2026-10-23 at Nick's direction. Jan stated the status on the ticket himself today; the note added nothing.

## SOLR-18240 (Complete Major changes and Upgrade Notes in RefGuide for 9.11.0)

🤖 _AI text below_ 🤖 _(reviewed by Nick Shanin)_

Status check on this ticket: Solr 9.11.0 shipped, and its Ref Guide major changes and upgrade notes are published with the release. This looks complete; can it be closed?

## SOLR-17988 (Ensure Solr 10 Release Conveys Changes Users Need To Be Aware Of)

🤖 _AI text below_ 🤖 _(reviewed by Nick Shanin)_

Status check on this ticket: Solr 10.0.0 has been released, and the property name changes are covered in the Solr 10 major changes and upgrade notes. If specific property transitions are still missing from the docs, naming them here would let someone pick that up; otherwise this looks complete with the release.

## SOLR-12833 (Use timed-out lock in DistributedUpdateProcessor)

🤖 _AI text below_ 🤖 _(reviewed by Nick Shanin)_

Status check on this ticket, from its own history: it was closed in February 2019, then reopened by [~ab] in April 2019 for a specific regression: the change grew {{VersionBucket}} from about 24 to about 104 bytes, which put roughly 0.7 GB of extra heap on a 100-core node. The follow-up work happened here on the reopened ticket: the two-implementation bucket split (PR 641), a wait-timeout fix (PR 661), and a test fix (PR 663), and the thread ends with [~ab] confirming the content is present on the 7.x branches. That work shipped in 8.1 and 7.7.2, which is why those fixVersions were added after the reopen. Nothing has moved here since May 2019, and the ticket still sits Reopened with a Fixed resolution. It looks ready to close as Fixed.

## SOLR-16450 (Proper handling on watcher registration failure in ZkStateReader#waitForState)

🤖 _AI text below_ 🤖 _(reviewed by Nick Shanin)_

Answering [~gus]'s question from 2024: yes, this is complete, and the version is 9.1.0. The timeline on the ticket: the original fix resolved it for 9.1 on 2022-10-14; [~hiteshkhamesra] then flagged one more place in {{ZkStateReader}}, and [~ichattopadhyaya] reopened it for that follow-up on 2022-10-18. The follow-up fix ("waitForState to cleanly unregister watches if there is a failure") landed the same day on branch_9_1 as 74f09064947 and on main as 32e0f68a9bc, so both fixes are in the 9.1.0 release. The ticket's fixVersion was removed in January 2023 while it sat reopened; restoring 9.1.0 and closing as Fixed matches the record.
