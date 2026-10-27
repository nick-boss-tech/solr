# Jira day scan receipt: 2026-10-26 (UTC)

> **Out of sequence, owner-directed closeout.** This receipt closes the existing Oct 26 claim at Nick's direction on 2026-10-27: “Close Oct 26 with an out-of-sequence receipt, then scan Oct 13.” Oct 26 is not part of the backward walk. The oldest normal date folder is 2026-10-14, so the next allowed date is 2026-10-13.

- Store: `assignments/work-store-jira-day-scan-round-1.md`. Host: `phone-agent-2` (`records`, `review`). The prior out-of-sequence note remains at `scope-deviation.md`.
- Scope source: [anonymous Jira REST day query](https://issues.apache.org/jira/rest/api/2/search?jql=project+%3D+SOLR+AND+updated+%3E%3D+%222026-10-26%22+AND+updated+%3C+%222026-10-27%22&fields=key%2Cupdated%2Csummary&maxResults=100&startAt=0). JQL: `project = SOLR AND updated >= "2026-10-26" AND updated < "2026-10-27"`. Search pagination: 2 pages, 133 of 133 tickets. Every returned `updated` timestamp is on 2026-10-26 UTC.
- Comment source: anonymous `GET https://issues.apache.org/jira/rest/api/2/issue/<key>/comment`, paginated at `maxResults=100`. All 133 histories were fetched serially, with at least 0.20 seconds between request starts and no parallel fetching.
- Totals: 133 tickets, 624 comments across their complete histories, and 101 commenter accounts. The `nickshanin` account contributed 113 comments across 113 tickets; those comments remain in the required totals and are excluded from candidate highlights.

## Commenter table

Counts cover all comments in the histories of the 133 tickets, including older comments. `jira-bot` is automation. Nick Shanin is labeled self and retained for aggregate accounting only.

| Commenter | Jira username | Comments | Tickets | Type |
| --- | --- | ---: | ---: | --- |
| Nick Shanin | `nickshanin` | 113 | 113 | Self, aggregate only |
| Mark Miller | `markrmiller@gmail.com` | 58 | 18 | Person |
| Chris M. Hostetter | `hossman` | 47 | 23 | Person |
| ASF subversion and git services | `jira-bot` | 35 | 13 | Automation |
| Yonik Seeley | `yseeley@gmail.com` | 31 | 18 | Person |
| David Smiley | `dsmiley` | 30 | 13 | Person |
| Varun Thacker | `varun` | 21 | 7 | Person |
| Jan Høydahl | `janhoy` | 19 | 6 | Person |
| Ludovic Boutros | `lboutros` | 18 | 2 | Person |
| Shawn Heisey | `elyograg` | 17 | 7 | Person |
| Amrit Sarkar | `sarkaramrit2@gmail.com` | 13 | 3 | Person |
| Erick Erickson | `erickerickson` | 12 | 7 | Person |
| Noble Paul | `noble.paul` | 12 | 5 | Person |
| Steven Rowe | `sarowe` | 11 | 5 | Person |
| Ramkumar Aiyengar | `andyetitmoves` | 7 | 1 | Person |
| Mikhail Khludnev | `mkhl` | 6 | 3 | Person |
| Paolo Cappuccini | `cappuccini` | 6 | 2 | Person |
| Robert Muir | `rcmuir` | 6 | 3 | Person |
| Shalin Shekhar Mangar | `shalin` | 6 | 4 | Person |
| Cao Manh Dat | `caomanhdat` | 5 | 3 | Person |
| Christos Malliaridis | `malliaridis` | 5 | 3 | Person |
| Eric Pugh | `epugh` | 5 | 3 | Person |
| Joel Bernstein | `jbernste` | 5 | 3 | Person |
| Per Steffensen | `steff1193` | 5 | 1 | Person |
| Eyyub ÇİL | `ecil` | 4 | 1 | Person |
| Ishan Chattopadhyaya | `ichattopadhyaya` | 4 | 3 | Person |
| Markus Jelsma | `markus17` | 4 | 2 | Person |
| Timo Hund | `timo.schmidt` | 4 | 1 | Person |
| Tomas Eduardo Fernandez Lobbe | `tflobbe` | 4 | 2 | Person |
| Yevhen Tienkaiev | `Hronom` | 4 | 2 | Person |
| Alan Woodward | `romseygeek` | 3 | 3 | Person |
| Anshum Gupta | `anshum` | 3 | 2 | Person |
| Christine Poerschke | `cpoerschke` | 3 | 2 | Person |
| Jack Krupansky | `jkrupan` | 3 | 2 | Person |
| Jalaz Kumar | `jaykay12` | 3 | 2 | Person |
| Jason Gerlowski | `gerlowskija` | 3 | 3 | Person |
| Khalid Galal | `kgalal` | 3 | 1 | Person |
| Mike Drob | `mdrob` | 3 | 2 | Person |
| Roland Deck | `rdeck` | 3 | 1 | Person |
| Uwe Schindler | `uschindler` | 3 | 3 | Person |
| Alexandre Rafalovitch | `arafalov` | 2 | 2 | Person |
| Bernhard Frauendienst | `obeliks` | 2 | 1 | Person |
| Brent Pearson | `Brent212` | 2 | 1 | Person |
| Chris de Kok | `cdekok` | 2 | 2 | Person |
| David Fennessey | `david_fennessey` | 2 | 1 | Person |
| Issei Nishigata | `issei2029` | 2 | 1 | Person |
| Jane Sandberg | `sandbergja` | 2 | 1 | Person |
| Keith Laban | `k317h` | 2 | 1 | Person |
| Manoj Lawrence | `razen` | 2 | 1 | Person |
| Marc Portier | `mportier` | 2 | 1 | Person |
| Pierre Salagnac | `psalagnac` | 2 | 2 | Person |
| Sandro Mario Zbinden | `sandrozbinden` | 2 | 1 | Person |
| Senthuran Sivananthan | `senthuran` | 2 | 1 | Person |
| Serhiy Bzhezytskyy | `serhiybzhezytskyy` | 2 | 2 | Person |
| Vassil Velichkov | `vassil.velichkov` | 2 | 1 | Person |
| vinay pothnis | `vinaypothnis` | 2 | 1 | Person |
| Masoud Kiaeeha | `maxxkia` | 1 | 1 | Person |
| Ahmet Arslan | `iorixxx` | 1 | 1 | Person |
| Alexey Serba | `alexey.serba` | 1 | 1 | Person |
| Andrzej Bialecki | `ab` | 1 | 1 | Person |
| Arcadius Ahouansou | `arcadius` | 1 | 1 | Person |
| Artem Karpenko | `artem.karpenko` | 1 | 1 | Person |
| Boyi Dai | `boyidai` | 1 | 1 | Person |
| Carlos Maroto | `cmaroto` | 1 | 1 | Person |
| Cassandra Targett | `ctargett` | 1 | 1 | Person |
| Chris Toomey | `ctoomey` | 1 | 1 | Person |
| Christian Winkler | `cwinkler` | 1 | 1 | Person |
| David Boychuck | `dboychuck` | 1 | 1 | Person |
| Forest Soup | `forest_soup` | 1 | 1 | Person |
| Frank Kelly | `kellyfj` | 1 | 1 | Person |
| Furkan Kamaci | `kamaci` | 1 | 1 | Person |
| Gregg Donovan | `greggny3` | 1 | 1 | Person |
| Gregory Chanan | `gchanan` | 1 | 1 | Person |
| Gus Heck | `gus` | 1 | 1 | Person |
| Henrik | `hg@impact.dk` | 1 | 1 | Person |
| Hrishikesh Gadre | `hgadre` | 1 | 1 | Person |
| Jack Reed | `mejackreed` | 1 | 1 | Person |
| James Dyer | `jdyer` | 1 | 1 | Person |
| John Wang | `john.wang@gmail.com` | 1 | 1 | Person |
| Ken Ip | `jugnooken` | 1 | 1 | Person |
| Lance Norskog | `lancenorskog` | 1 | 1 | Person |
| Luca Cavanna | `lucacavanna` | 1 | 1 | Person |
| Mark S | `marks1900` | 1 | 1 | Person |
| Martin Grotzke | `martin.grotzke` | 1 | 1 | Person |
| Martin Ruckli | `mruckli` | 1 | 1 | Person |
| Mihaly Toth | `mihaly.toth` | 1 | 1 | Person |
| Munendra S N | `munendrasn` | 1 | 1 | Person |
| Paul McArthur | `pmcarthur` | 1 | 1 | Person |
| Peter Davis | `davispw` | 1 | 1 | Person |
| Puneet Ahuja | `puneet22` | 1 | 1 | Person |
| Pushkar Raste | `praste` | 1 | 1 | Person |
| Ramsey Haddad | `rwhaddad` | 1 | 1 | Person |
| Ryan Ernst | `rjernst` | 1 | 1 | Person |
| Sachin Divekar | `ssd532` | 1 | 1 | Person |
| Scott Blum | `dragonsinth` | 1 | 1 | Person |
| scott hobson | `bkscott` | 1 | 1 | Person |
| Shishir Choudhary | `shishircc@gmail.com` | 1 | 1 | Person |
| Stephan Lagraulet | `stephlag` | 1 | 1 | Person |
| Tom Burton-West | `tburtonwest` | 1 | 1 | Person |
| Vasili Logvinov | `v.logvinov` | 1 | 1 | Person |
| Vitaliy Zhovtyuk | `vzhovtiuk` | 1 | 1 | Person |

## Fresh watch-list comments

Only comments created on 2026-10-26 UTC are included here. Nick-authored comments are not candidate highlights.

| Person | Ticket and time | Comment |
| --- | --- | --- |
| David Smiley | [SOLR-18538](https://issues.apache.org/jira/browse/SOLR-18538?focusedCommentId=18132449), `2026-10-26T14:20:05.163+0000` | “Any reason why 9.x Milestone wasn't chosen on the PR?” |

## Candidate work from the fresh comment

- **SOLR-18538, David Smiley:** He asked why the 9.x milestone was not chosen on the PR. Candidate only, not claimed: inspect the PR milestone and linked issue context to establish whether a 9.x target is intended; then prepare a staged question about whether the milestone should be corrected. No PR or Jira response was posted, and no source investigation was performed during this scan.

## Per-ticket dispositions

“Comments” counts the entire retrieved history for that ticket. The single fresh watch-list comment is on SOLR-18538; every other ticket had no fresh watch-list comment.

| Ticket | Comments | Fresh watch-list activity | Disposition |
| --- | ---: | --- | --- |
| [SOLR-18592](https://issues.apache.org/jira/browse/SOLR-18592) | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18591](https://issues.apache.org/jira/browse/SOLR-18591) | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18590](https://issues.apache.org/jira/browse/SOLR-18590) | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18587](https://issues.apache.org/jira/browse/SOLR-18587) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18586](https://issues.apache.org/jira/browse/SOLR-18586) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18585](https://issues.apache.org/jira/browse/SOLR-18585) | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18569](https://issues.apache.org/jira/browse/SOLR-18569) | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18566](https://issues.apache.org/jira/browse/SOLR-18566) | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18565](https://issues.apache.org/jira/browse/SOLR-18565) | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18554](https://issues.apache.org/jira/browse/SOLR-18554) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18550](https://issues.apache.org/jira/browse/SOLR-18550) | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18548](https://issues.apache.org/jira/browse/SOLR-18548) | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18538](https://issues.apache.org/jira/browse/SOLR-18538) | 4 | David Smiley, 1 | Candidate only: inspect PR milestone context before preparing a staged question; not claimed. |
| [SOLR-18535](https://issues.apache.org/jira/browse/SOLR-18535) | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18521](https://issues.apache.org/jira/browse/SOLR-18521) | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18500](https://issues.apache.org/jira/browse/SOLR-18500) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18351](https://issues.apache.org/jira/browse/SOLR-18351) | 4 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18297](https://issues.apache.org/jira/browse/SOLR-18297) | 24 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-18234](https://issues.apache.org/jira/browse/SOLR-18234) | 10 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-17988](https://issues.apache.org/jira/browse/SOLR-17988) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-17914](https://issues.apache.org/jira/browse/SOLR-17914) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-17913](https://issues.apache.org/jira/browse/SOLR-17913) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-17911](https://issues.apache.org/jira/browse/SOLR-17911) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-17909](https://issues.apache.org/jira/browse/SOLR-17909) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-17895](https://issues.apache.org/jira/browse/SOLR-17895) | 8 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-17887](https://issues.apache.org/jira/browse/SOLR-17887) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-17861](https://issues.apache.org/jira/browse/SOLR-17861) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-17850](https://issues.apache.org/jira/browse/SOLR-17850) | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-17849](https://issues.apache.org/jira/browse/SOLR-17849) | 5 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-17845](https://issues.apache.org/jira/browse/SOLR-17845) | 5 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-17786](https://issues.apache.org/jira/browse/SOLR-17786) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-17660](https://issues.apache.org/jira/browse/SOLR-17660) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-17328](https://issues.apache.org/jira/browse/SOLR-17328) | 0 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-17268](https://issues.apache.org/jira/browse/SOLR-17268) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-16548](https://issues.apache.org/jira/browse/SOLR-16548) | 6 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-16398](https://issues.apache.org/jira/browse/SOLR-16398) | 8 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-11994](https://issues.apache.org/jira/browse/SOLR-11994) | 4 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-11927](https://issues.apache.org/jira/browse/SOLR-11927) | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-11712](https://issues.apache.org/jira/browse/SOLR-11712) | 11 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-11583](https://issues.apache.org/jira/browse/SOLR-11583) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-11577](https://issues.apache.org/jira/browse/SOLR-11577) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-11495](https://issues.apache.org/jira/browse/SOLR-11495) | 11 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-11364](https://issues.apache.org/jira/browse/SOLR-11364) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-11353](https://issues.apache.org/jira/browse/SOLR-11353) | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-11292](https://issues.apache.org/jira/browse/SOLR-11292) | 6 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-11083](https://issues.apache.org/jira/browse/SOLR-11083) | 4 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-10952](https://issues.apache.org/jira/browse/SOLR-10952) | 5 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-10928](https://issues.apache.org/jira/browse/SOLR-10928) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-10751](https://issues.apache.org/jira/browse/SOLR-10751) | 7 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-10726](https://issues.apache.org/jira/browse/SOLR-10726) | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-10363](https://issues.apache.org/jira/browse/SOLR-10363) | 8 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-10332](https://issues.apache.org/jira/browse/SOLR-10332) | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-10112](https://issues.apache.org/jira/browse/SOLR-10112) | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-10110](https://issues.apache.org/jira/browse/SOLR-10110) | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-10030](https://issues.apache.org/jira/browse/SOLR-10030) | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-9978](https://issues.apache.org/jira/browse/SOLR-9978) | 11 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-9841](https://issues.apache.org/jira/browse/SOLR-9841) | 10 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-9765](https://issues.apache.org/jira/browse/SOLR-9765) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-9664](https://issues.apache.org/jira/browse/SOLR-9664) | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-9648](https://issues.apache.org/jira/browse/SOLR-9648) | 5 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-9622](https://issues.apache.org/jira/browse/SOLR-9622) | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-9550](https://issues.apache.org/jira/browse/SOLR-9550) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-9514](https://issues.apache.org/jira/browse/SOLR-9514) | 6 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-9477](https://issues.apache.org/jira/browse/SOLR-9477) | 7 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-9369](https://issues.apache.org/jira/browse/SOLR-9369) | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-9361](https://issues.apache.org/jira/browse/SOLR-9361) | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-9352](https://issues.apache.org/jira/browse/SOLR-9352) | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-9230](https://issues.apache.org/jira/browse/SOLR-9230) | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-9226](https://issues.apache.org/jira/browse/SOLR-9226) | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-9223](https://issues.apache.org/jira/browse/SOLR-9223) | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-9206](https://issues.apache.org/jira/browse/SOLR-9206) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-9174](https://issues.apache.org/jira/browse/SOLR-9174) | 13 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-8893](https://issues.apache.org/jira/browse/SOLR-8893) | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-8879](https://issues.apache.org/jira/browse/SOLR-8879) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-8862](https://issues.apache.org/jira/browse/SOLR-8862) | 11 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-8841](https://issues.apache.org/jira/browse/SOLR-8841) | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-8733](https://issues.apache.org/jira/browse/SOLR-8733) | 5 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-8709](https://issues.apache.org/jira/browse/SOLR-8709) | 5 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-8634](https://issues.apache.org/jira/browse/SOLR-8634) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-8629](https://issues.apache.org/jira/browse/SOLR-8629) | 5 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-8553](https://issues.apache.org/jira/browse/SOLR-8553) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-8543](https://issues.apache.org/jira/browse/SOLR-8543) | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-8368](https://issues.apache.org/jira/browse/SOLR-8368) | 23 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-8311](https://issues.apache.org/jira/browse/SOLR-8311) | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-8067](https://issues.apache.org/jira/browse/SOLR-8067) | 7 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-8030](https://issues.apache.org/jira/browse/SOLR-8030) | 43 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-7932](https://issues.apache.org/jira/browse/SOLR-7932) | 18 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-7521](https://issues.apache.org/jira/browse/SOLR-7521) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-7511](https://issues.apache.org/jira/browse/SOLR-7511) | 7 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-7347](https://issues.apache.org/jira/browse/SOLR-7347) | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-7257](https://issues.apache.org/jira/browse/SOLR-7257) | 5 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-7247](https://issues.apache.org/jira/browse/SOLR-7247) | 16 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-7071](https://issues.apache.org/jira/browse/SOLR-7071) | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-7070](https://issues.apache.org/jira/browse/SOLR-7070) | 9 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-7069](https://issues.apache.org/jira/browse/SOLR-7069) | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-7035](https://issues.apache.org/jira/browse/SOLR-7035) | 4 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-6922](https://issues.apache.org/jira/browse/SOLR-6922) | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-6837](https://issues.apache.org/jira/browse/SOLR-6837) | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-6785](https://issues.apache.org/jira/browse/SOLR-6785) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-6719](https://issues.apache.org/jira/browse/SOLR-6719) | 6 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-6714](https://issues.apache.org/jira/browse/SOLR-6714) | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-6429](https://issues.apache.org/jira/browse/SOLR-6429) | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-6364](https://issues.apache.org/jira/browse/SOLR-6364) | 4 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-6099](https://issues.apache.org/jira/browse/SOLR-6099) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-6004](https://issues.apache.org/jira/browse/SOLR-6004) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-5962](https://issues.apache.org/jira/browse/SOLR-5962) | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-5376](https://issues.apache.org/jira/browse/SOLR-5376) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-5235](https://issues.apache.org/jira/browse/SOLR-5235) | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-5095](https://issues.apache.org/jira/browse/SOLR-5095) | 5 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-5046](https://issues.apache.org/jira/browse/SOLR-5046) | 8 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-4996](https://issues.apache.org/jira/browse/SOLR-4996) | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-4983](https://issues.apache.org/jira/browse/SOLR-4983) | 9 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-4859](https://issues.apache.org/jira/browse/SOLR-4859) | 10 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-4733](https://issues.apache.org/jira/browse/SOLR-4733) | 14 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-4669](https://issues.apache.org/jira/browse/SOLR-4669) | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-4558](https://issues.apache.org/jira/browse/SOLR-4558) | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-4486](https://issues.apache.org/jira/browse/SOLR-4486) | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-4277](https://issues.apache.org/jira/browse/SOLR-4277) | 4 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-4130](https://issues.apache.org/jira/browse/SOLR-4130) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-4100](https://issues.apache.org/jira/browse/SOLR-4100) | 12 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-4023](https://issues.apache.org/jira/browse/SOLR-4023) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-4021](https://issues.apache.org/jira/browse/SOLR-4021) | 11 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-3945](https://issues.apache.org/jira/browse/SOLR-3945) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-3721](https://issues.apache.org/jira/browse/SOLR-3721) | 19 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-3539](https://issues.apache.org/jira/browse/SOLR-3539) | 11 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-3473](https://issues.apache.org/jira/browse/SOLR-3473) | 16 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-3407](https://issues.apache.org/jira/browse/SOLR-3407) | 7 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-3008](https://issues.apache.org/jira/browse/SOLR-3008) | 1 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-2855](https://issues.apache.org/jira/browse/SOLR-2855) | 5 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-2516](https://issues.apache.org/jira/browse/SOLR-2516) | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-2420](https://issues.apache.org/jira/browse/SOLR-2420) | 3 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-1780](https://issues.apache.org/jira/browse/SOLR-1780) | 2 | None | No fresh watch-list comment; no candidate from this scan. |
| [SOLR-1557](https://issues.apache.org/jira/browse/SOLR-1557) | 10 | None | No fresh watch-list comment; no candidate from this scan. |

No Jira or GitHub comments were posted, no PRs were opened, and no follow-up claims were created. The broad BATS assertion-hardening gate remains OPEN/INCOMPLETE; this read-only scan does not change its state.
