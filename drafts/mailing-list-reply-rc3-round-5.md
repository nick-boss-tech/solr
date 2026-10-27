# Draft round 5: RC3 mailing list reply (phone agent, 2026-10-27)

Supersedes `drafts/mailing-list-reply-rc3-round-4.md` on the owner's correction: the closing clause about seeds and matched pairs is dropped entirely. It overclaimed (not every ticket carries both) and it signposted; the reply already points at each ticket inline. Nothing is posted; the owner sends it himself if he decides to.

Subject: Re: [VOTE] Release Apache Solr 10.1.0 RC3

---

I ran the RC3 smoke tester on aarch64 Linux (JDK 21.0.12.1), with the workspace on NFS. It failed all five runs on the release artifacts. I then assembled a build from source with candidate fixes on top, instead of using the tarball, to test them. The findings are written up in Jira:

- Replica recovery fails on a filesystem that refuses hard links; IndexFetcher has no copy fallback. SOLR-18586. A fix branch is linked on the ticket.
- CoreAdmin unload after a failed reload can leave the previous core open. SOLR-18587. The fix branch and a diff are linked on the ticket.
- The JS client build fails when NODE_ENV=production. SOLR-18593. A one-line fix is prepared, on the same branch as the first item.
- A metrics-related failure appeared once in five runs here. It root-caused to a shutdown leak, SOLR-18564; the fix is in PR #5192.

On the patched build, the Java tests and the integration tests passed here. The final binary-release step of that run produced no result, so I do not have a clean full smoke pass.

I am not claiming RC3 is broken for users; the hard-link failure in particular may be specific to this host.

Nick
