# Jira day scan receipt: 2026-10-23 (UTC)

- Store: `assignments/work-store-jira-day-scan-round-1.md`. Source run: the owner's smoke scan (script jira-commenters-smoke.py on the phone agent host; anonymous REST reads, 0.15s pacing).
- Scope: SOLR tickets updated on 2026-10-23 (UTC). Tickets updated: 38. Comments scanned (all comments on those tickets): 149. Unique commenters: 31.

## Commenter table for the day's tickets

| Commenter | Username | Comments | Tickets |
| --- | --- | --- | --- |
| ASF subversion and git services (automation) | jira-bot | 45 | 7 |
| David Smiley | dsmiley | 20 | 8 |
| Jan Høydahl | janhoy | 19 | 7 |
| Amrit Sarkar | sarkaramrit2@gmail.com | 10 | 6 |
| Mark Miller | markrmiller@gmail.com | 6 | 2 |
| Eric Pugh | epugh | 5 | 3 |
| Nick Shanin | nickshanin | 5 | 4 |
| Houston Putman | houston | 4 | 2 |
| Forest Soup | forest_soup | 3 | 1 |
| Jason Gerlowski | gerlowskija | 3 | 3 |
| Kevin Risden | krisden | 3 | 1 |
| Kevin Liang | liangkaiwen | 3 | 1 |
| Jerry Chung | jerry-cherry | 2 | 1 |
| Tomas Eduardo Fernandez Lobbe | tflobbe | 2 | 1 |
| Uwe Schindler | uschindler | 2 | 2 |
| Vassil Velichkov | vassil.velichkov | 2 | 1 |
| Andrzej Bialecki | ab | 1 | 1 |
| Anshum Gupta | anshum | 1 | 1 |
| Dawid Weiss | dweiss | 1 | 1 |
| Erick Erickson | erickerickson | 1 | 1 |
| Ishan Chattopadhyaya | ichattopadhyaya | 1 | 1 |
| Jalaz Kumar | jaykay12 | 1 | 1 |
| Lucene/Solr QA | lucenesolrqa | 1 | 1 |
| Marcus Eagan | marcussorealheis | 1 | 1 |
| Mike Drob | mdrob | 1 | 1 |
| Paul McArthur | pmcarthur | 1 | 1 |
| Puneet Ahuja | puneet22 | 1 | 1 |
| Serhiy Bzhezytskyy | serhiybzhezytskyy | 1 | 1 |
| Shalin Shekhar Mangar | shalin | 1 | 1 |
| Alex Deparvu | stillalex | 1 | 1 |
| Zamir Paltiel | zamirp | 1 | 1 |

## Fresh watch-list comments (created on the scan day)

- David Smiley on SOLR-14306 at 2026-10-23T03:57:09.431+0000: "I converted the sub-task SOLR-16116 to be a normal issue, and linked it as something that has superseded this issue. I'm inclined to close this issue as won't-do. I see us using Curator recipes directly." (https://issues.apache.org/jira/browse/SOLR-14306?focusedCommentId=18130678)
- Eric Pugh on SOLR-18571 at 2026-10-23T00:53:32.793+0000: "we don't actually really use the "assign" at all. It's ifne just to put a comment saying "im working on this".." (https://issues.apache.org/jira/browse/SOLR-18571?focusedCommentId=18130653)
- Eric Pugh on SOLR-18571 at 2026-10-23T00:54:43.288+0000: "in my world, I'd love to see it on by default... if it's too memory intensive or otherwise intrusive to be on by default, well, then that kind of defeats the prupose. I think it's great to be able to disable it, but we don't awant to make everythign opt in because maybe someone doesn't know they nee" (https://issues.apache.org/jira/browse/SOLR-18571?focusedCommentId=18130654)
- David Smiley on SOLR-17895 at 2026-10-23T04:09:57.782+0000: "[~sarkaramrit2@gmail.com] What do you think of my comment RE Curator (it's _recipes_)? SOLR-17704 Solr's ZK code per-dated Curator. Only in Solr 10 have we finally made the initial prerequisite step of using Curator for connection management but the biggest value is in its recipes (higher level abst" (https://issues.apache.org/jira/browse/SOLR-17895?focusedCommentId=18130679)
- Jason Gerlowski on SOLR-18569 at 2026-10-23T16:00:46.787+0000: "It looks like the "removeEphemeralLiveNode" method actually does clean up node_role entries on "main" and "branch_10x"...it's just branch_9x missing the fix. (It looks like this was something Houston caught as a part of his curator work in SOLR-16116, which couldn't be backported as far as 9.x I gue" (https://issues.apache.org/jira/browse/SOLR-18569?focusedCommentId=18131023)
- Jan Høydahl on SOLR-18297 at 2026-10-23T20:32:05.046+0000: "It did not make the 10.1 train so sits ready for 10.2. Release roadmap is not planned ahead of time." (https://issues.apache.org/jira/browse/SOLR-18297?focusedCommentId=18131158)

Context, all comment dates on the same tickets: Eric Pugh 5, Christos Malliaridis 0, Jan Høydahl 19, Jason Gerlowski 3, David Smiley 20.

## Candidate work from the fresh comments

- SOLR-18571: Eric Pugh is actively discussing whether the feature should be on by default; an engaged owner on a live question. Ask candidate under the staged ask.
- SOLR-18569: Jason Gerlowski's note (the fix is on main and branch_10x, missing on branch_9x) ties directly to PR 5211, whose test review is already in flight on this board; no new unit, the receipt confirms the thread is live.
- SOLR-14306: David Smiley is inclined to close as won't-do and has converted sub-task SOLR-16116; recorded only. Any follow-up starts as a sponsor conversation, not a build.
- SOLR-17895: David Smiley's design question to Amrit Sarkar about the Curator recipes; watch, no candidate.
- SOLR-18297: Jan Høydahl stated the status himself (missed 10.1, ready for 10.2). Settled; no candidate.
