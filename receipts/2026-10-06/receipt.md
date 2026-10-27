# Jira day scan receipt: 2026-10-06 (UTC)

- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). This is the next backward date before the oldest normal scan folder, 2026-10-07. The later out-of-sequence Oct 25 and Oct 26 folders do not change the backward frontier.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-10-06%22+AND+updated+%3C+%222026-10-07%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-10-06" AND updated < "2026-10-07"`. Search pagination: two pages at `maxResults=100` (100 + 10); all 110 returned `updated` timestamps fall on 2026-10-06 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`. All 110 histories were fetched serially, with at least 0.20 seconds between request starts and no parallel fetching.
- Scope totals: 110 SOLR tickets updated on the day; 881 comments across their complete histories; 66 commenter accounts. There are 95 fresh watch-list comments: 84 identical David Smiley notes saying “Closing after the 9.11.0 release” and 11 other comments from Eric Pugh. Nick Shanin’s four historical comments across three tickets remain in the aggregate only and are excluded from candidate highlights.

## Commenter table

Counts cover all comments in the histories of the 110 tickets, including older comments. `jira-bot` is marked as automation, not a person. Nick Shanin is labeled self and included for aggregate accounting only.

| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| ASF subversion and git services | `jira-bot` | 386 | 92 | Automation |
| David Smiley | `dsmiley` | 180 | 91 | Person |
| Eric Pugh | `epugh` | 64 | 29 | Person |
| Jan Høydahl | `janhoy` | 38 | 14 | Person |
| Jason Gerlowski | `gerlowskija` | 18 | 8 | Person |
| Chris M. Hostetter | `hossman` | 13 | 10 | Person |
| Arup Chauhan | `arupchauhan` | 8 | 2 | Person |
| Sonu Sharma | `ercsonu` | 8 | 3 | Person |
| Alessandro Benedetti | `abenedetti` | 7 | 2 | Person |
| Andrzej Bialecki | `ab` | 7 | 6 | Person |
| Isabelle Giguere | `igiguere` | 7 | 3 | Person |
| Lucas Kot-Zaniewski | `lkot` | 7 | 2 | Person |
| Jalaz Kumar | `jaykay12` | 6 | 3 | Person |
| Pierre Salagnac | `psalagnac` | 6 | 2 | Person |
| Shawn Heisey | `elyograg` | 6 | 2 | Person |
| Mark Miller | `markrmiller@gmail.com` | 5 | 1 | Person |
| Rahul Goswami | `rahulgoswami` | 5 | 3 | Person |
| Renato Haeberli | `renato` | 5 | 1 | Person |
| Vamsee K. Yarlagadda | `vamsee` | 5 | 1 | Person |
| Vishnu Priya | `vchandsekar` | 5 | 2 | Person |
| Christine Poerschke | `cpoerschke` | 4 | 2 | Person |
| Gaurav Tuli | `gmt425` | 4 | 1 | Person |
| Gus Heck | `gus` | 4 | 2 | Person |
| Khush Jain | `khushjain` | 4 | 2 | Person |
| Marc Byrd | `cloudmarc` | 4 | 2 | Person |
| Nick Shanin | `nickshanin` | 4 | 3 | Self, aggregate only |
| Serhiy Bzhezytskyy | `serhiybzhezytskyy` | 4 | 4 | Person |
| Anshum Gupta | `anshum` | 3 | 3 | Person |
| Hendrik Haddorp | `HendrikH` | 3 | 1 | Person |
| Matthew Biscocho | `mlbiscoc` | 3 | 3 | Person |
| Puneet Ahuja | `puneet22` | 3 | 1 | Person |
| Shrey Narayan | `nextbrick23` | 3 | 3 | Person |
| Ufuk Yilmaz | `uyilmaz` | 3 | 1 | Person |
| Yash Goswmai | `yashgoswami` | 3 | 1 | Person |
| Aaron Dockter | `aarondr` | 2 | 1 | Person |
| Abhishek Umarjikar | `aumarjikar` | 2 | 1 | Person |
| Alexander Veit | `veita` | 2 | 2 | Person |
| Bartosz Fidrysiak | `bfidrysiak` | 2 | 1 | Person |
| Cassandra Targett | `ctargett` | 2 | 1 | Person |
| Clemens Fuchslocher | `abcdefghijklmnopqrstuvwxyz` | 2 | 1 | Person |
| Dhoka Pramod | `dpramod` | 2 | 2 | Person |
| Idan Tepper | `idantepper` | 2 | 1 | Person |
| Ilan Ginzburg | `ilan` | 2 | 1 | Person |
| Julian Chandra Sutadi | `juliancs` | 2 | 2 | Person |
| Mikhail Khludnev | `mkhl` | 2 | 1 | Person |
| Sanjay Kumar Yadav | `yaduvanshi` | 2 | 1 | Person |
| Stefan Matheis | `steffkes` | 2 | 1 | Person |
| Sujeet Hinge | `sujeet-hinge` | 2 | 1 | Person |
| Alan Woodward | `romseygeek` | 1 | 1 | Person |
| Alex K | `tar` | 1 | 1 | Person |
| Christos Malliaridis | `malliaridis` | 1 | 1 | Person |
| Erick Erickson | `erickerickson` | 1 | 1 | Person |
| Facundo Mateo | `fmateo` | 1 | 1 | Person |
| Franck Valentin | `franckval` | 1 | 1 | Person |
| Francois Huaulme | `fhuaulme` | 1 | 1 | Person |
| Houston Putman | `houston` | 1 | 1 | Person |
| James Dyer | `jdyer` | 1 | 1 | Person |
| Jeremy Branham | `jdbranham` | 1 | 1 | Person |
| Kevin Risden | `krisden` | 1 | 1 | Person |
| Mark Robert Miller | `markrmiller` | 1 | 1 | Person |
| Markus Schuch | `schuch` | 1 | 1 | Person |
| Piotr Karwasz | `pkarwasz` | 1 | 1 | Person |
| Upayavira | `upayavira` | 1 | 1 | Person |
| Uwe Schindler | `uschindler` | 1 | 1 | Person |
| Zay Mowbray | `zaylinux` | 1 | 1 | Person |
| ZhenyuLi | `zhenyuli` | 1 | 1 | Person |

## Fresh watch-list comments

All 95 comments created on 2026-10-06 UTC are listed below, with the scanner’s short excerpt and focused Jira comment link. Nick Shanin’s comments are retained only in full-history totals and are not candidates.

| Person | Ticket and time | Comment excerpt |
| --- | --- | --- |
| Eric Pugh | [SOLR-17422](https://issues.apache.org/jira/browse/SOLR-17422?focusedCommentId=18123909), `2026-10-06T16:54:39.564+0000` | “Fixed with the merge of https://github.com/apache/solr/pull/4952” |
| Eric Pugh | [SOLR-18425](https://issues.apache.org/jira/browse/SOLR-18425?focusedCommentId=18123910), `2026-10-06T16:55:38.173+0000` | “With the merge of [https://github.com/apache/solr/pull/4952] we are done!” |
| Eric Pugh | [SOLR-18425](https://issues.apache.org/jira/browse/SOLR-18425?focusedCommentId=18123916), `2026-10-06T17:05:03.041+0000` | “There is a discussion needed about if the other subtasks actually are going to be executed on, if we actually will make those happen.. if so, great, but if not, then let's close those JIRAs. We have lots of APIS to migrate, don't need more.” |
| Eric Pugh | [SOLR-18448](https://issues.apache.org/jira/browse/SOLR-18448?focusedCommentId=18123917), `2026-10-06T17:06:22.927+0000` | “THe work in SOLR-18450 actually delivers this!” |
| Eric Pugh | [SOLR-15752](https://issues.apache.org/jira/browse/SOLR-15752?focusedCommentId=18123929), `2026-10-06T17:27:45.758+0000` | “We are moving right along... I'll post an update on where we are.” |
| Eric Pugh | [SOLR-14265](https://issues.apache.org/jira/browse/SOLR-14265?focusedCommentId=18123930), `2026-10-06T17:29:22.272+0000` | “This effort is very much in progress, between revamped ref guide, new v2 apis, using them in the Solr CLI and Solr Admin UI. That work is in many other JIRAs, so I'd like to close this as a duplicate.” |
| David Smiley | [SOLR-18346](https://issues.apache.org/jira/browse/SOLR-18346?focusedCommentId=18124046), `2026-10-06T19:54:01.136+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-17208](https://issues.apache.org/jira/browse/SOLR-17208?focusedCommentId=18124047), `2026-10-06T19:54:01.215+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-13309](https://issues.apache.org/jira/browse/SOLR-13309?focusedCommentId=18124048), `2026-10-06T19:54:01.412+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18195](https://issues.apache.org/jira/browse/SOLR-18195?focusedCommentId=18124049), `2026-10-06T19:54:01.586+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18220](https://issues.apache.org/jira/browse/SOLR-18220?focusedCommentId=18124051), `2026-10-06T19:54:01.790+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-17755](https://issues.apache.org/jira/browse/SOLR-17755?focusedCommentId=18124052), `2026-10-06T19:54:01.903+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18406](https://issues.apache.org/jira/browse/SOLR-18406?focusedCommentId=18124053), `2026-10-06T19:54:02.014+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18056](https://issues.apache.org/jira/browse/SOLR-18056?focusedCommentId=18124055), `2026-10-06T19:54:02.298+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18219](https://issues.apache.org/jira/browse/SOLR-18219?focusedCommentId=18124056), `2026-10-06T19:54:02.413+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18408](https://issues.apache.org/jira/browse/SOLR-18408?focusedCommentId=18124057), `2026-10-06T19:54:02.510+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18335](https://issues.apache.org/jira/browse/SOLR-18335?focusedCommentId=18124058), `2026-10-06T19:54:02.615+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18144](https://issues.apache.org/jira/browse/SOLR-18144?focusedCommentId=18124059), `2026-10-06T19:54:02.707+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-15601](https://issues.apache.org/jira/browse/SOLR-15601?focusedCommentId=18124060), `2026-10-06T19:54:02.815+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18073](https://issues.apache.org/jira/browse/SOLR-18073?focusedCommentId=18124063), `2026-10-06T19:54:03.139+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-17841](https://issues.apache.org/jira/browse/SOLR-17841?focusedCommentId=18124064), `2026-10-06T19:54:03.248+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-17787](https://issues.apache.org/jira/browse/SOLR-17787?focusedCommentId=18124065), `2026-10-06T19:54:03.376+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-7177](https://issues.apache.org/jira/browse/SOLR-7177?focusedCommentId=18124066), `2026-10-06T19:54:03.492+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18271](https://issues.apache.org/jira/browse/SOLR-18271?focusedCommentId=18124067), `2026-10-06T19:54:03.692+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18114](https://issues.apache.org/jira/browse/SOLR-18114?focusedCommentId=18124068), `2026-10-06T19:54:03.801+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-17901](https://issues.apache.org/jira/browse/SOLR-17901?focusedCommentId=18124069), `2026-10-06T19:54:03.905+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18198](https://issues.apache.org/jira/browse/SOLR-18198?focusedCommentId=18124070), `2026-10-06T19:54:04.004+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-17899](https://issues.apache.org/jira/browse/SOLR-17899?focusedCommentId=18124072), `2026-10-06T19:54:04.207+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18181](https://issues.apache.org/jira/browse/SOLR-18181?focusedCommentId=18124073), `2026-10-06T19:54:04.302+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-17870](https://issues.apache.org/jira/browse/SOLR-17870?focusedCommentId=18124074), `2026-10-06T19:54:04.401+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18285](https://issues.apache.org/jira/browse/SOLR-18285?focusedCommentId=18124075), `2026-10-06T19:54:04.532+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-15352](https://issues.apache.org/jira/browse/SOLR-15352?focusedCommentId=18124076), `2026-10-06T19:54:04.626+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18183](https://issues.apache.org/jira/browse/SOLR-18183?focusedCommentId=18124077), `2026-10-06T19:54:04.698+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18237](https://issues.apache.org/jira/browse/SOLR-18237?focusedCommentId=18124078), `2026-10-06T19:54:04.795+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18029](https://issues.apache.org/jira/browse/SOLR-18029?focusedCommentId=18124079), `2026-10-06T19:54:04.897+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18221](https://issues.apache.org/jira/browse/SOLR-18221?focusedCommentId=18124080), `2026-10-06T19:54:04.991+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18089](https://issues.apache.org/jira/browse/SOLR-18089?focusedCommentId=18124082), `2026-10-06T19:54:05.199+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18334](https://issues.apache.org/jira/browse/SOLR-18334?focusedCommentId=18124083), `2026-10-06T19:54:05.293+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18008](https://issues.apache.org/jira/browse/SOLR-18008?focusedCommentId=18124084), `2026-10-06T19:54:05.374+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18194](https://issues.apache.org/jira/browse/SOLR-18194?focusedCommentId=18124085), `2026-10-06T19:54:05.550+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18157](https://issues.apache.org/jira/browse/SOLR-18157?focusedCommentId=18124086), `2026-10-06T19:54:05.685+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18239](https://issues.apache.org/jira/browse/SOLR-18239?focusedCommentId=18124087), `2026-10-06T19:54:05.784+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-17319](https://issues.apache.org/jira/browse/SOLR-17319?focusedCommentId=18124088), `2026-10-06T19:54:05.901+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18013](https://issues.apache.org/jira/browse/SOLR-18013?focusedCommentId=18124089), `2026-10-06T19:54:06.630+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18039](https://issues.apache.org/jira/browse/SOLR-18039?focusedCommentId=18124090), `2026-10-06T19:54:06.734+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18074](https://issues.apache.org/jira/browse/SOLR-18074?focusedCommentId=18124091), `2026-10-06T19:54:06.836+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-12224](https://issues.apache.org/jira/browse/SOLR-12224?focusedCommentId=18124092), `2026-10-06T19:54:06.939+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-17982](https://issues.apache.org/jira/browse/SOLR-17982?focusedCommentId=18124093), `2026-10-06T19:54:07.113+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18304](https://issues.apache.org/jira/browse/SOLR-18304?focusedCommentId=18124094), `2026-10-06T19:54:07.215+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18176](https://issues.apache.org/jira/browse/SOLR-18176?focusedCommentId=18124095), `2026-10-06T19:54:07.346+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18413](https://issues.apache.org/jira/browse/SOLR-18413?focusedCommentId=18124096), `2026-10-06T19:54:07.449+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18107](https://issues.apache.org/jira/browse/SOLR-18107?focusedCommentId=18124097), `2026-10-06T19:54:07.541+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-17903](https://issues.apache.org/jira/browse/SOLR-17903?focusedCommentId=18124100), `2026-10-06T19:54:07.812+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-14687](https://issues.apache.org/jira/browse/SOLR-14687?focusedCommentId=18124101), `2026-10-06T19:54:08.057+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18344](https://issues.apache.org/jira/browse/SOLR-18344?focusedCommentId=18124102), `2026-10-06T19:54:08.167+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-17810](https://issues.apache.org/jira/browse/SOLR-17810?focusedCommentId=18124103), `2026-10-06T19:54:08.266+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18202](https://issues.apache.org/jira/browse/SOLR-18202?focusedCommentId=18124104), `2026-10-06T19:54:08.363+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18093](https://issues.apache.org/jira/browse/SOLR-18093?focusedCommentId=18124105), `2026-10-06T19:54:08.444+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-17715](https://issues.apache.org/jira/browse/SOLR-17715?focusedCommentId=18124106), `2026-10-06T19:54:08.554+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18071](https://issues.apache.org/jira/browse/SOLR-18071?focusedCommentId=18124108), `2026-10-06T19:54:08.778+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18350](https://issues.apache.org/jira/browse/SOLR-18350?focusedCommentId=18124109), `2026-10-06T19:54:08.931+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18189](https://issues.apache.org/jira/browse/SOLR-18189?focusedCommentId=18124110), `2026-10-06T19:54:09.015+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18327](https://issues.apache.org/jira/browse/SOLR-18327?focusedCommentId=18124111), `2026-10-06T19:54:09.131+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18032](https://issues.apache.org/jira/browse/SOLR-18032?focusedCommentId=18124112), `2026-10-06T19:54:09.209+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18142](https://issues.apache.org/jira/browse/SOLR-18142?focusedCommentId=18124113), `2026-10-06T19:54:09.335+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18293](https://issues.apache.org/jira/browse/SOLR-18293?focusedCommentId=18124115), `2026-10-06T19:54:09.496+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18215](https://issues.apache.org/jira/browse/SOLR-18215?focusedCommentId=18124116), `2026-10-06T19:54:09.580+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18051](https://issues.apache.org/jira/browse/SOLR-18051?focusedCommentId=18124117), `2026-10-06T19:54:09.669+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-17707](https://issues.apache.org/jira/browse/SOLR-17707?focusedCommentId=18124118), `2026-10-06T19:54:09.755+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18418](https://issues.apache.org/jira/browse/SOLR-18418?focusedCommentId=18124119), `2026-10-06T19:54:09.861+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18197](https://issues.apache.org/jira/browse/SOLR-18197?focusedCommentId=18124120), `2026-10-06T19:54:09.934+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18328](https://issues.apache.org/jira/browse/SOLR-18328?focusedCommentId=18124121), `2026-10-06T19:54:10.066+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18312](https://issues.apache.org/jira/browse/SOLR-18312?focusedCommentId=18124122), `2026-10-06T19:54:10.164+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18290](https://issues.apache.org/jira/browse/SOLR-18290?focusedCommentId=18124123), `2026-10-06T19:54:10.279+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18155](https://issues.apache.org/jira/browse/SOLR-18155?focusedCommentId=18124124), `2026-10-06T19:54:10.390+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18244](https://issues.apache.org/jira/browse/SOLR-18244?focusedCommentId=18124126), `2026-10-06T19:54:10.574+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-17833](https://issues.apache.org/jira/browse/SOLR-17833?focusedCommentId=18124127), `2026-10-06T19:54:10.670+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18096](https://issues.apache.org/jira/browse/SOLR-18096?focusedCommentId=18124128), `2026-10-06T19:54:10.782+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18345](https://issues.apache.org/jira/browse/SOLR-18345?focusedCommentId=18124129), `2026-10-06T19:54:10.880+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18329](https://issues.apache.org/jira/browse/SOLR-18329?focusedCommentId=18124130), `2026-10-06T19:54:10.965+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-17353](https://issues.apache.org/jira/browse/SOLR-17353?focusedCommentId=18124131), `2026-10-06T19:54:11.051+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18174](https://issues.apache.org/jira/browse/SOLR-18174?focusedCommentId=18124132), `2026-10-06T19:54:11.179+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-17922](https://issues.apache.org/jira/browse/SOLR-17922?focusedCommentId=18124133), `2026-10-06T19:54:11.367+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-17973](https://issues.apache.org/jira/browse/SOLR-17973?focusedCommentId=18124135), `2026-10-06T19:54:11.549+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-17170](https://issues.apache.org/jira/browse/SOLR-17170?focusedCommentId=18124136), `2026-10-06T19:54:11.638+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18201](https://issues.apache.org/jira/browse/SOLR-18201?focusedCommentId=18124137), `2026-10-06T19:54:11.725+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18216](https://issues.apache.org/jira/browse/SOLR-18216?focusedCommentId=18124138), `2026-10-06T19:54:11.854+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-17972](https://issues.apache.org/jira/browse/SOLR-17972?focusedCommentId=18124140), `2026-10-06T19:54:12.057+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-18136](https://issues.apache.org/jira/browse/SOLR-18136?focusedCommentId=18124141), `2026-10-06T19:54:12.153+0000` | “Closing after the 9.11.0 release” |
| David Smiley | [SOLR-8127](https://issues.apache.org/jira/browse/SOLR-8127?focusedCommentId=18124142), `2026-10-06T19:54:12.263+0000` | “Closing after the 9.11.0 release” |
| Eric Pugh | [SOLR-15752](https://issues.apache.org/jira/browse/SOLR-15752?focusedCommentId=18124145), `2026-10-06T20:03:57.445+0000` | “Fully v1, no v2 equivalent planned: - Metrics (admin/metrics, wt=prometheus) — Plugins/Stats, cloud node metrics - Core Info (:core/admin/info) — Core Overview - Replication (:core/replication) — Replication screen + core overview summary - Update/Documents (:core/:handler, defaults to update) — Documents screen, file uploads - Query (:core/:handler) — Query, Stream, SQL screens (arbitrary handler forwarding) - File…” |
| Eric Pugh | [SOLR-5044](https://issues.apache.org/jira/browse/SOLR-5044?focusedCommentId=18124146), `2026-10-06T20:04:36.639+0000` | “Please speak up if this remains a issue!” |
| Eric Pugh | [SOLR-18102](https://issues.apache.org/jira/browse/SOLR-18102?focusedCommentId=18124147), `2026-10-06T20:05:09.235+0000` | “This was fixed back in the 10 release window” |
| Eric Pugh | [SOLR-13161](https://issues.apache.org/jira/browse/SOLR-13161?focusedCommentId=18124149), `2026-10-06T20:07:55.637+0000` | “We are prioritzing the new Solr UI. As. commented on the PR, if you want this in Solr 10x, then please reopen against github.com/apache/solr repo and I'll work to get it added.” |
| Eric Pugh | [SOLR-11797](https://issues.apache.org/jira/browse/SOLR-11797?focusedCommentId=18124154), `2026-10-06T20:17:26.297+0000` | “So, with the jax-rs apis, we go away from introspection but DO have openapi... There is various ideas around how to host hte openapi spec for poeple to play with it...” |

## Candidate work from fresh comments

- **Candidate only - [SOLR-18425](https://issues.apache.org/jira/browse/SOLR-18425?focusedCommentId=18123916), Eric Pugh:** PR #4952 was merged and the parent work was described as done, but Eric says the remaining subtasks need a decision. Ask whether those subtasks are still intended; if not, close them. No task is claimed.
- **Candidate only - [SOLR-5044](https://issues.apache.org/jira/browse/SOLR-5044?focusedCommentId=18124146), Eric Pugh:** Eric invites a response if the Admin UI directory note remains an issue. Ask the ticket owner whether it is still relevant before taking any follow-up. No task is claimed.
- **Candidate only - [SOLR-13161](https://issues.apache.org/jira/browse/SOLR-13161?focusedCommentId=18124149), Eric Pugh:** the new Solr UI is the priority; if inclusion in Solr 10.x is wanted, Eric says to reopen against the Apache Solr repository. Confirm whether that release target is desired before any reopen. No action is taken.
- **Possible design candidate - [SOLR-11797](https://issues.apache.org/jira/browse/SOLR-11797?focusedCommentId=18124154), Eric Pugh:** JAX-RS moves away from introspection but exposes OpenAPI; the hosting approach for people to try the spec remains unsettled. Clarify whether choosing an OpenAPI hosting/discovery path is wanted before design work. No task is claimed.

Other fresh comments report completed, covered, duplicate, or release-close status and are recorded as settled/no candidate in the per-ticket table. SOLR-15752 is a progress/inventory note: Eric says work is progressing and lists several fully v1 APIs with no v2 equivalent planned; it does not request a follow-up.

## Per-ticket dispositions

“Comments” counts each ticket’s complete retrieved history. Tickets without fresh watch-list activity have no candidate from this scan.

| Ticket | Updated UTC | Comments | Fresh activity | Disposition |
| --- | --- | ---: | --- | --- |
| [SOLR-18497](https://issues.apache.org/jira/browse/SOLR-18497) - Concurrent RELOAD of a core that failed to load creates the core once per request | `2026-10-06T10:19:14.000+0000` | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18496](https://issues.apache.org/jira/browse/SOLR-18496) - Single-pass distributed query fails with an NPE when the unique key is renamed in fl | `2026-10-06T10:21:19.000+0000` | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18495](https://issues.apache.org/jira/browse/SOLR-18495) - Make placeholder name in URL clearer.  /mycollection/blah --> /{collectionName}/blah | `2026-10-06T12:20:37.000+0000` | 5 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18491](https://issues.apache.org/jira/browse/SOLR-18491) - Ref Guide: show a v2 form wherever only v1 is shown | `2026-10-06T10:45:19.000+0000` | 6 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18487](https://issues.apache.org/jira/browse/SOLR-18487) - V2 CollectionApiCommands NPE (AdminCmdContext) is a new-to-10.1 regression, not the pre-existing SOLR-18324 — introduced by SOLR-18072, exposed by SOLR-15752 | `2026-10-06T20:13:09.000+0000` | 7 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18482](https://issues.apache.org/jira/browse/SOLR-18482) - JSON range facet silently ignores `limit`, `offset`, `sort` and the refine parameters | `2026-10-06T00:11:30.000+0000` | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18469](https://issues.apache.org/jira/browse/SOLR-18469) - Introduce tabs for v1 and v2 api examples everywhere | `2026-10-06T10:45:19.000+0000` | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18463](https://issues.apache.org/jira/browse/SOLR-18463) - Reference our cli tools as "Solr CLI"....  looking at you Post Tool | `2026-10-06T10:45:19.000+0000` | 4 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18460](https://issues.apache.org/jira/browse/SOLR-18460) - Ensure all curl commands follow the same pattern | `2026-10-06T12:20:37.000+0000` | 7 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18455](https://issues.apache.org/jira/browse/SOLR-18455) - <metrics enabled=false> is too shallow | `2026-10-06T12:20:38.000+0000` | 8 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18448](https://issues.apache.org/jira/browse/SOLR-18448) - Create model/POJO types for CLUSTERSTATUS output | `2026-10-06T17:06:22.000+0000` | 3 | Eric Pugh | Eric Pugh says SOLR-18450 delivers this; covered, no candidate. |
| [SOLR-18425](https://issues.apache.org/jira/browse/SOLR-18425) - Break up CLUSTERSTATUS V2 API | `2026-10-06T17:05:03.000+0000` | 5 | Eric Pugh | Eric Pugh says PR #4952 is merged and parent work is done; candidate only to decide whether remaining subtasks proceed or close. |
| [SOLR-18418](https://issues.apache.org/jira/browse/SOLR-18418) - complement() and intersect() silently return wrong results when on= maps different field names | `2026-10-06T19:54:09.000+0000` | 4 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18413](https://issues.apache.org/jira/browse/SOLR-18413) - Deleting a MIGRATE target collection can leave source shard replicas inconsistent | `2026-10-06T19:54:07.000+0000` | 5 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18408](https://issues.apache.org/jira/browse/SOLR-18408) - CrossDC Consumer: incorrect nextOffset commits | `2026-10-06T19:54:02.000+0000` | 3 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18406](https://issues.apache.org/jira/browse/SOLR-18406) - Replication filecontent returns HTTP 500 when the commit reservation expires | `2026-10-06T19:54:02.000+0000` | 6 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18350](https://issues.apache.org/jira/browse/SOLR-18350) - Many callers of org.apache.solr.core.SolrCore#getLatestSchema should use alternatives | `2026-10-06T19:54:08.000+0000` | 5 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18347](https://issues.apache.org/jira/browse/SOLR-18347) - Possible AdminUI bugs found by Selenium | `2026-10-06T14:58:21.000+0000` | 6 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18346](https://issues.apache.org/jira/browse/SOLR-18346) - Leader-to-replica update retry depends on which exception the client reports outermost | `2026-10-06T19:54:01.000+0000` | 5 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18345](https://issues.apache.org/jira/browse/SOLR-18345) - ClientUtils.encodeLocalParamVal() can produces lossy/invalid encodings | `2026-10-06T19:54:10.000+0000` | 12 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18344](https://issues.apache.org/jira/browse/SOLR-18344) - Backup status is not reported on /replication?command=details until the backup completes | `2026-10-06T19:54:08.000+0000` | 11 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18335](https://issues.apache.org/jira/browse/SOLR-18335) - SPLITSHARD fails when router.field field lacks terms index | `2026-10-06T19:54:02.000+0000` | 4 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18334](https://issues.apache.org/jira/browse/SOLR-18334) - PRS collection creation returns too early; can cause restore failure | `2026-10-06T19:54:05.000+0000` | 4 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18329](https://issues.apache.org/jira/browse/SOLR-18329) - DenseVectorField should support existence queries (field:* / [* TO *]) | `2026-10-06T19:54:10.000+0000` | 4 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18328](https://issues.apache.org/jira/browse/SOLR-18328) - Support stddev in rollup function for streaming expressions | `2026-10-06T19:54:10.000+0000` | 5 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18327](https://issues.apache.org/jira/browse/SOLR-18327) - Balance replicas API does not balance duplicate replicas on same node | `2026-10-06T19:54:09.000+0000` | 6 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18324](https://issues.apache.org/jira/browse/SOLR-18324) - V2 (JAX-RS) requests can throw a cascading NPE / RTimer assertion failure when a `SolrQueryRequest` isn't attached to the request context | `2026-10-06T10:45:18.000+0000` | 6 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18321](https://issues.apache.org/jira/browse/SOLR-18321) - bin/solr delete should not use ZK if ZK host not provided | `2026-10-06T12:20:40.000+0000` | 5 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18312](https://issues.apache.org/jira/browse/SOLR-18312) - Thread-Pool Starvation in HttpJdkSolrClient for large Queries. | `2026-10-06T19:54:10.000+0000` | 6 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18304](https://issues.apache.org/jira/browse/SOLR-18304) - Fix collapse on String performance regression due to Lucene upgrade | `2026-10-06T19:54:07.000+0000` | 9 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18293](https://issues.apache.org/jira/browse/SOLR-18293) - AllowListUrlChecker uses inconsistent URL scheme detection compared to shard URL fetching | `2026-10-06T19:54:09.000+0000` | 10 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18290](https://issues.apache.org/jira/browse/SOLR-18290) - Support configurable fusion candidate pool in Combined Query | `2026-10-06T19:54:10.000+0000` | 4 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18285](https://issues.apache.org/jira/browse/SOLR-18285) - change JettySolrRunner's use of GracefulHandler to be opt-in | `2026-10-06T19:54:04.000+0000` | 6 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18271](https://issues.apache.org/jira/browse/SOLR-18271) - Support for Query Elevation in Combined Query | `2026-10-06T19:54:03.000+0000` | 5 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18244](https://issues.apache.org/jira/browse/SOLR-18244) - Fix concurrency bugs in HttpShardHandler / ParallelHttpShardHandler that can hang searches or crash coordinator | `2026-10-06T19:54:10.000+0000` | 5 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18239](https://issues.apache.org/jira/browse/SOLR-18239) - `ObjectSizeEstimator.primitiveEstimate()` calls `obj.getClass()` without checking if `obj` is null | `2026-10-06T19:54:05.000+0000` | 20 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18237](https://issues.apache.org/jira/browse/SOLR-18237) - Collection state deserialization duplicates content for no good reason | `2026-10-06T19:54:04.000+0000` | 4 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18221](https://issues.apache.org/jira/browse/SOLR-18221) - Support percentiles in rollup function for streaming expressions | `2026-10-06T19:54:04.000+0000` | 6 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18220](https://issues.apache.org/jira/browse/SOLR-18220) - Support countDist in rollup function for streaming expressions | `2026-10-06T19:54:01.000+0000` | 9 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18219](https://issues.apache.org/jira/browse/SOLR-18219) - Rename CoreAdmin UPGRADECOREINDEX action to UPGRADEINDEX | `2026-10-06T19:54:02.000+0000` | 9 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18216](https://issues.apache.org/jira/browse/SOLR-18216) - Fix documentation bug for jwt-auth blockUnkonwn | `2026-10-06T19:54:11.000+0000` | 5 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18215](https://issues.apache.org/jira/browse/SOLR-18215) - Change default of blockUnknown to true for jwt-auth | `2026-10-06T19:54:09.000+0000` | 6 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18202](https://issues.apache.org/jira/browse/SOLR-18202) - Scary uptick in failures from SolrIndexFingerprintTest (OOM risk) | `2026-10-06T19:54:08.000+0000` | 8 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18201](https://issues.apache.org/jira/browse/SOLR-18201) - "Range Field" types should have docValues support and associated optimizations | `2026-10-06T19:54:11.000+0000` | 6 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18198](https://issues.apache.org/jira/browse/SOLR-18198) - Support "missing" count in rollup function for streaming expressions | `2026-10-06T19:54:04.000+0000` | 9 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18197](https://issues.apache.org/jira/browse/SOLR-18197) - Query _nest_path_ (NestPathField) ease-of-use | `2026-10-06T19:54:09.000+0000` | 13 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18195](https://issues.apache.org/jira/browse/SOLR-18195) - Support for Collapse Results in Combined Query Component | `2026-10-06T19:54:01.000+0000` | 6 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18194](https://issues.apache.org/jira/browse/SOLR-18194) - Handle Updated Documents in UpgradeCoreIndex Child Doc Check | `2026-10-06T19:54:05.000+0000` | 6 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18189](https://issues.apache.org/jira/browse/SOLR-18189) - Improve tracking of duplicate Solr docs with content hash | `2026-10-06T19:54:09.000+0000` | 6 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18183](https://issues.apache.org/jira/browse/SOLR-18183) - All EphemeralDirectoryFactory should permit LockType.NONE | `2026-10-06T19:54:04.000+0000` | 6 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18181](https://issues.apache.org/jira/browse/SOLR-18181) - ValueSourceAugmenter NPE when rows exceed 1000 and scores requested | `2026-10-06T19:54:04.000+0000` | 6 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18176](https://issues.apache.org/jira/browse/SOLR-18176) - HttpShardHandler query throughput bottleneck from ZooKeeper | `2026-10-06T19:54:07.000+0000` | 6 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18174](https://issues.apache.org/jira/browse/SOLR-18174) - AsyncTracker Semaphore leak causes deadlock | `2026-10-06T19:54:11.000+0000` | 14 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18157](https://issues.apache.org/jira/browse/SOLR-18157) - Optimize buffer allocation in JavaBinCodec | `2026-10-06T19:54:05.000+0000` | 4 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18155](https://issues.apache.org/jira/browse/SOLR-18155) - CollectionsAPISolrJTest seed reliably leaks unclosed ZkShardTerms | `2026-10-06T19:54:10.000+0000` | 7 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18144](https://issues.apache.org/jira/browse/SOLR-18144) - Regression in Schema Designer and .system collection | `2026-10-06T19:54:02.000+0000` | 9 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18142](https://issues.apache.org/jira/browse/SOLR-18142) - Flaky: CloudSolrClientCacheTest.testStaleStateRetryWaitsAfterSkipFailure | `2026-10-06T19:54:09.000+0000` | 5 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18136](https://issues.apache.org/jira/browse/SOLR-18136) - ArrayStoreException in MultiThreadedSearcher when combining rerank with sort and parallel segment search | `2026-10-06T19:54:12.000+0000` | 5 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18114](https://issues.apache.org/jira/browse/SOLR-18114) - CloudSolrClient fails deleteById with directUpdatesToLeadersOnly but no route passed | `2026-10-06T19:54:03.000+0000` | 7 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18107](https://issues.apache.org/jira/browse/SOLR-18107) - LoggingHandlerTest flaky after Log4j 2.25.3 upgrade | `2026-10-06T19:54:07.000+0000` | 9 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18102](https://issues.apache.org/jira/browse/SOLR-18102) - Admin UI 401s when basic-auth enabled | `2026-10-06T20:05:09.000+0000` | 4 | Eric Pugh | Eric Pugh says this was fixed during the Solr 10 release window; settled, no candidate. |
| [SOLR-18096](https://issues.apache.org/jira/browse/SOLR-18096) - Introduce a UPRADECOREINDEX CoreAdmin API to support index upgrade | `2026-10-06T19:54:10.000+0000` | 7 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18093](https://issues.apache.org/jira/browse/SOLR-18093) - Add top-level "queries" support to JsonQueryRequest in SolrJ | `2026-10-06T19:54:08.000+0000` | 4 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18089](https://issues.apache.org/jira/browse/SOLR-18089) - embeddedZkServer warning RE maxCnxns is not configured | `2026-10-06T19:54:05.000+0000` | 5 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18074](https://issues.apache.org/jira/browse/SOLR-18074) - MultiValued dense vectors support (through inner nested vectors) | `2026-10-06T19:54:06.000+0000` | 5 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18073](https://issues.apache.org/jira/browse/SOLR-18073) - JWTAuthPlugin claimsMatch fails with MalformedClaimException for non-string claims (e.g., booleans) | `2026-10-06T19:54:03.000+0000` | 7 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18071](https://issues.apache.org/jira/browse/SOLR-18071) - Export Writer should optionally support stored-only fields | `2026-10-06T19:54:08.000+0000` | 6 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18056](https://issues.apache.org/jira/browse/SOLR-18056) - HttpClusterStateProvider; use urlScheme from initial quorum | `2026-10-06T19:54:02.000+0000` | 12 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18051](https://issues.apache.org/jira/browse/SOLR-18051) - HttpJettySolrClient async requests double register with the phaser/semaphore | `2026-10-06T19:54:09.000+0000` | 6 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18039](https://issues.apache.org/jira/browse/SOLR-18039) - knn QParser should support multiple parents.preFilter params | `2026-10-06T19:54:06.000+0000` | 13 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18032](https://issues.apache.org/jira/browse/SOLR-18032) - Make our gradle docker tasks support multi platform | `2026-10-06T19:54:09.000+0000` | 18 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18029](https://issues.apache.org/jira/browse/SOLR-18029) - PropertiesInputStream does not override bulk read method read(byte[] b, int off, int len) | `2026-10-06T19:54:04.000+0000` | 5 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18013](https://issues.apache.org/jira/browse/SOLR-18013) - Upgrade lz4 jar to fix CVE-2025-12183 and CVE-2025-66566 | `2026-10-06T19:54:06.000+0000` | 4 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-18008](https://issues.apache.org/jira/browse/SOLR-18008) - Allow Core Creation when remnant files are still on disk | `2026-10-06T19:54:05.000+0000` | 17 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-17982](https://issues.apache.org/jira/browse/SOLR-17982) - Provide a means to force SearchHandler's distributed logic with 1 shard | `2026-10-06T19:54:07.000+0000` | 5 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-17973](https://issues.apache.org/jira/browse/SOLR-17973) - Cross Collection Join not respecting shards routing preference | `2026-10-06T19:54:11.000+0000` | 10 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-17972](https://issues.apache.org/jira/browse/SOLR-17972) - DistributedMultiLock can fail to release some locks if ZK connection loss occurs | `2026-10-06T19:54:12.000+0000` | 10 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-17922](https://issues.apache.org/jira/browse/SOLR-17922) - Upgrade netty jar to fix CVE-2025-58057 , CVE-2025-58056 , CVE-2025-24970 | `2026-10-06T19:54:11.000+0000` | 5 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-17903](https://issues.apache.org/jira/browse/SOLR-17903) - CVE-2025-31672: vulnerability in poi-ooxml 5.2.2 dependency | `2026-10-06T19:54:07.000+0000` | 6 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-17901](https://issues.apache.org/jira/browse/SOLR-17901) - CVE-2024-21742: vulnerability in apache-mime4j-core 0.8.4 dependency | `2026-10-06T19:54:03.000+0000` | 14 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-17899](https://issues.apache.org/jira/browse/SOLR-17899) - CVE-2024-30171, CVE-2024-30172, CVE-2024-29857, CVE-2023-33201 : vulnerabilities in Bouncy Castle provider 1.70 dependency | `2026-10-06T19:54:04.000+0000` | 4 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-17870](https://issues.apache.org/jira/browse/SOLR-17870) - UnsupportedOperationException in SpellCheckComponent when Sorting Collations | `2026-10-06T19:54:04.000+0000` | 6 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-17841](https://issues.apache.org/jira/browse/SOLR-17841) - Investigate multithreaded search performance bottlenecks | `2026-10-06T19:54:03.000+0000` | 24 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-17833](https://issues.apache.org/jira/browse/SOLR-17833) - Assess expoitability of CVE-2024-7254 in dependency `protobuf-java` | `2026-10-06T19:54:10.000+0000` | 4 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-17810](https://issues.apache.org/jira/browse/SOLR-17810) - RateLimiting doesn't work for non-Apache Http SolrClients | `2026-10-06T19:54:08.000+0000` | 17 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-17787](https://issues.apache.org/jira/browse/SOLR-17787) - CborResponseWriter should use content-type: application/cbor | `2026-10-06T19:54:03.000+0000` | 8 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-17755](https://issues.apache.org/jira/browse/SOLR-17755) - Official Docker Images with a horrible number of security vulnerabilities | `2026-10-06T19:54:01.000+0000` | 7 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-17715](https://issues.apache.org/jira/browse/SOLR-17715) - SolrJ / QueryRequest - stop sending "qt" param | `2026-10-06T19:54:08.000+0000` | 24 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-17707](https://issues.apache.org/jira/browse/SOLR-17707) - HttpJdkSolrClient threads stuck on exceptions | `2026-10-06T19:54:09.000+0000` | 12 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-17495](https://issues.apache.org/jira/browse/SOLR-17495) - Change CLI DeleteTool deleting of configs to opt in instead of default behavior | `2026-10-06T12:20:40.000+0000` | 4 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-17422](https://issues.apache.org/jira/browse/SOLR-17422) - Remove CLUSTERSTATE from v2; redundant | `2026-10-06T16:54:53.000+0000` | 2 | Eric Pugh | Eric Pugh says fixed by merged PR #4952; settled, no candidate. |
| [SOLR-17353](https://issues.apache.org/jira/browse/SOLR-17353) - gosu in Dockerfile cause of most solr docker image CVEs | `2026-10-06T19:54:11.000+0000` | 10 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-17319](https://issues.apache.org/jira/browse/SOLR-17319) - Introduce support for Reciprocal Rank Fusion (combining queries) | `2026-10-06T19:54:05.000+0000` | 35 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-17208](https://issues.apache.org/jira/browse/SOLR-17208) - Restore should fetch index files in parallel | `2026-10-06T19:54:01.000+0000` | 6 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-17170](https://issues.apache.org/jira/browse/SOLR-17170) - Support Blocks in Index Sorting | `2026-10-06T19:54:11.000+0000` | 8 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-16458](https://issues.apache.org/jira/browse/SOLR-16458) - Cosmetic improvements and migration to JAX-RS ("node" APIs) | `2026-10-06T10:44:17.000+0000` | 30 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-15752](https://issues.apache.org/jira/browse/SOLR-15752) - Switch Admin UI to use v2 API | `2026-10-06T20:03:57.000+0000` | 6 | Eric Pugh | Eric Pugh reports progress and provides a v1-only/no-v2-equivalent inventory; status information, no candidate. |
| [SOLR-15601](https://issues.apache.org/jira/browse/SOLR-15601) - Remove blank rat exclusions and add proper licenses to certain files | `2026-10-06T19:54:02.000+0000` | 5 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-15352](https://issues.apache.org/jira/browse/SOLR-15352) - Querying multiple collection performance issue | `2026-10-06T19:54:04.000+0000` | 4 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-14687](https://issues.apache.org/jira/browse/SOLR-14687) - Make child/parent query parsers natively aware of _nest_path_ | `2026-10-06T19:54:08.000+0000` | 12 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-14265](https://issues.apache.org/jira/browse/SOLR-14265) - Move collections admin API to v2 completely | `2026-10-06T17:29:22.000+0000` | 10 | Eric Pugh | Eric Pugh says the work is covered across other JIRAs and proposes closing as a duplicate; settled direction, no candidate. |
| [SOLR-13309](https://issues.apache.org/jira/browse/SOLR-13309) - Add FieldTypes for Lucene IntRange, LongRange, FloatRange, DoubleRange | `2026-10-06T19:54:01.000+0000` | 24 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-13217](https://issues.apache.org/jira/browse/SOLR-13217) - collapse parser with /export request handler throws NPE | `2026-10-06T10:45:19.000+0000` | 4 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-13161](https://issues.apache.org/jira/browse/SOLR-13161) - Drag and drop replica move - admin ui | `2026-10-06T20:07:57.000+0000` | 2 | Eric Pugh | Eric Pugh says the new Solr UI is prioritized; only proceed toward Solr 10.x if the requester wants it reopened in the repo. |
| [SOLR-13097](https://issues.apache.org/jira/browse/SOLR-13097) - RuleBasedAuthorizationPlugin is not fully fonctionnal in Solr standalone mode | `2026-10-06T21:58:41.000+0000` | 4 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-12224](https://issues.apache.org/jira/browse/SOLR-12224) - there is no API to read collection properties | `2026-10-06T19:54:06.000+0000` | 23 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-11797](https://issues.apache.org/jira/browse/SOLR-11797) - Create an admin page to show the V2 API commands through introspection | `2026-10-06T20:17:26.000+0000` | 3 | Eric Pugh | Eric Pugh says JAX-RS removes introspection but OpenAPI exists; candidate only to decide how the spec should be hosted/discovered. |
| [SOLR-8127](https://issues.apache.org/jira/browse/SOLR-8127) - Luke request handler does not know about dynamic fields on other shards fast enough | `2026-10-06T19:54:12.000+0000` | 19 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-7177](https://issues.apache.org/jira/browse/SOLR-7177) - ConcurrentUpdateSolrClient should log connection information on http failures | `2026-10-06T19:54:03.000+0000` | 15 | David Smiley | David Smiley marks this for closure after the 9.11.0 release; settled status, no candidate. |
| [SOLR-5044](https://issues.apache.org/jira/browse/SOLR-5044) - Admin UI - Note on Core-Admin about directories while creating core | `2026-10-06T20:04:36.000+0000` | 6 | Eric Pugh | Eric Pugh invites confirmation if the issue remains; candidate only to check relevance with the ticket owner. |

No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.
