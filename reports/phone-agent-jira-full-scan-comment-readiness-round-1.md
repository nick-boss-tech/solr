# Report: full-scan Jira comment readiness, remaining leads, round 1

By the phone agent, 2026-10-24. Live Jira state re-read on 2026-10-24 for all seven tickets; code checked on origin/main where the lead required it. One draft resulted (SOLR-18549). Nothing was posted.

## SOLR-18551: IndexFetcher can leak mmaps on TLOG/PULL non-leaders

- Live state: Open, unassigned, 7 comments, last updated 2026-10-19. Jason Gerlowski is driving the thread; the owner already has two comments on it (2026-10-17: the leak measurement on Lucene 10.4.0 and 9.12.3, and the Directory-variant benchmark).
- Lead check: Jason's comment 18128711 (2026-10-19) answers how the issue was found (a customer's TLOG/PULL switch, mmap failures within hours, Lucene issue 15068 via Houston Putman). A second user corroborated with a users-list thread and a workaround. Code on current main matches Jason's description: `IndexFetcher` opens downloaded files with `IOContext.READONCE` (line 1276), the direction the ticket proposes; one check path still opens with `IOContext.DEFAULT` (line 1339).
- Recommendation: no comment now. The owner's evidence is already on the ticket, the origin question is answered, and the remaining step is the fix, which is Jason's to drive. A further comment would repeat the thread.
- Missing evidence: none for a comment decision. If the line 1339 check path is ever questioned, the owner's 2026-10-17 measurement (openInput with IOContext.DEFAULT retains mappings while a reader is open) is the relevant evidence and is already posted.

## SOLR-18266: Migrate data corpuses to ASF infrastructure

- Live state: Open, unassigned, 9 comments, last updated 2026-10-20 (Jan Høydahl's POC note, solr-orbit PR 130).
- Lead check: no sponsor for the AWS storage and egress costs is confirmed anywhere in the live thread. The thread records INFRA-27982, Infra's funding questions (PMC funds or AWS credits, with a requirements write-up Infra asked for and does not yet have), OpenSource Connections' June offer of a corner of its public S3 bucket, and Jan's note that the AWS Open Data Sponsorship Program is a possibility for data sets that are not the project's own.
- Recommendation: no comment now. The sponsor gap is stated plainly on the ticket itself, and the open answers belong to Infra and the PMC, not to a contributor comment. The owner cannot sponsor or commit funds, and asking whether a sponsor has been found repeats the ticket's own record.
- Missing evidence: a sponsor commitment or the requirements write-up Infra requested; both are other parties' deliverables.

## SOLR-18284: Fix load-average and memory circuit breakers

- Live state: Open, unassigned, 4 comments, last updated 2026-10-20 by Jason Gerlowski's comment 18129173 asking Mark Miller whether the ticket should be closed.
- Lead check: Mark has not answered; there is no later activity. The sponsor question the scan identified is already asked and standing.
- Recommendation: no comment. The assignment bars repeating Jason's question, and a second voice asking the same thing adds nothing. This is a sponsor ticket-state decision.
- Missing evidence: Mark Miller's answer, which only he can give.

## SOLR-18549: Standalone node throws NPE for nodes or node parameters in proxy-capable admin endpoints

- Live state: Open, unassigned, reporter Nick Shanin, 1 comment: Eric Pugh (2026-10-21) noting he tends to think in SolrCloud terms and that standalone needs more thought for these APIs. Eric is the engaged maintainer on the owner's own ticket.
- Lead check: the endpoint behavior was verified in the October gate work recorded in `material/decision-ladder.md`: before SOLR-16458 (commit c74142675e) wrapped every exception, the same case returned 400, and v1 still returns 400; the ticket's stated expectation is that the broadcast endpoints are answered by the node itself and `/admin/metrics` returns 400. A fork branch `solr-18549-submit` carries the fix; no PR is open.
- Recommendation: a comment is warranted. It is the owner's own ticket, an engaged maintainer has commented, and verified evidence exists. Shape: staged ask addressed to Eric, presenting the verified behavior and the branch, and asking whether the shape matches his expectation before the PR opens.
- Draft: `jira/jira-comment-SOLR-18549-draft.md` (DRAFT, not posted), and the ticket is added to `jira/COMMENTS.md`.

## SOLR-18568: Unload/delete-replica can wait indefinitely in SolrCore.closeAndWait

- Live state: Open, unassigned, 1 comment, last updated 2026-10-22. The description is detailed (thread dump, `SolrCore.closeAndWait` at line 1627 with no timeout, roughly 36,000 log lines in an hour). Links SOLR-18560 as a reference.
- Lead check: Jason's comment 18130390 already answers the scan's question. The hangs his team found (SOLR-18244) were different paths with the same end result, and he poses the choice himself: inspect the shutdown logic for all such hang-ups, or pursue the timeout idea. The examples do not share one failure path, on the only first-hand evidence available.
- Recommendation: no comment now. Without a reproduction, a comment from the owner would restate Jason's either/or. The useful contribution here is evidence, not a note.
- Missing evidence: a reproduction or thread dumps tying a specific hang to the missing timeout in `closeAndWait`, as opposed to the other shutdown paths Jason named.

## SOLR-14306: Refactor coordination code into separate module

- Live state: Open, unassigned, 8 comments, last updated 2026-10-23 by David Smiley's comment 18130678: he converted sub-task SOLR-16116 to a normal issue, linked it as superseding this one, and is inclined to close this issue as won't-do.
- Lead check: SOLR-16116 is Closed, resolution Fixed (closed after the 10.0.0 release). The superseding work Smiley points at is therefore done and shipped, and the closure inclination is stated by a committer who can execute it himself.
- Recommendation: no comment. The sponsor conversation the scan envisaged has already happened in Smiley's comment; there is no question left for the owner to ask.
- Missing evidence: none.

## SOLR-18571: Query Insights, opt-in top-N expensive queries per node

- Live state: Open, assigned to Eric Pugh, 4 comments, last updated 2026-10-23. Amrit Sarkar is implementing, starting with SOLR-11994.
- Lead check: the open question is precise and already joined. Eric (comments 18130653 and 18130654) wants the feature on by default unless it proves too memory intensive or intrusive, which he says would defeat its purpose. Amrit (18130688) gives three reasons for starting off (query text becomes visible to accounts holding only metrics-read; it runs on every search and he wants numbers first; off-to-on later is easy, on-to-off after a release is not) and says he is happy to switch if Eric still feels it should be on. The default question therefore sits with Eric, with the one missing input being the footprint numbers Eric's condition names.
- Recommendation: no comment now. A comment from the owner would be an unsupported vote in a design thread between the two engaged parties; the comment formula's staged ask leads with evidence, and the evidence this thread needs is a measurement, not an opinion.
- Missing evidence: the memory and intrusiveness footprint of the per-node top-N structures under load. If the owner wants this lead pursued, that measurement is a benchmark work-store item (misc box shape); its result would then support a staged ask.
