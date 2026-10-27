# Draft round 2: RC3 mailing list reply (phone agent, 2026-10-27)

Supersedes the draft inside `assignments/mailing-list-reply-rc3-review-round-1.md` on the owner's feedback: too verbose, too much formatting for a mailing list, point to the tickets instead. The tickets were brought up to date first (SOLR-18587 supplement comment 18132744, SOLR-18564 comment 18132745 naming PR #5192, SOLR-18593 filed for the JS client defect). Nothing is posted; the owner sends it himself if he decides to.

Subject: Re: [VOTE] Release Apache Solr 10.1.0 RC3

---

I ran the RC3 smoke tester on aarch64 Linux (JDK 21.0.12.1), with the workspace on NFS. It did not pass cleanly on this host. The findings are written up in Jira:

- Replica recovery fails on a filesystem that refuses hard links; IndexFetcher has no copy fallback. SOLR-18586. A fix branch is linked on the ticket.
- CoreAdmin unload after a failed reload can leave the previous core open. SOLR-18587. The fix branch and a diff are linked on the ticket.
- The JS client build fails when NODE_ENV=production. SOLR-18593. A one-line fix is prepared, on the same branch as the first item.
- A metrics-related failure appeared once in five runs here. It root-caused to a shutdown leak, SOLR-18564; the fix is in PR #5192.

On a patched head carrying those fixes, the Java tests and the integration tests passed here. The final binary-release step of that run produced no result, so I do not have a clean full smoke pass. A browser check of the patched build showed no admin UI role error.

I am not claiming RC3 is broken for users; the hard-link failure in particular may be specific to this host. Seeds, matched pairs and logs are on the tickets and in the receipts on our board.

Nick
