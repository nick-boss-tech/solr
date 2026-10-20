# Tickets to open: the registry

Owner direction, 2026-10-19: one inventory of the tickets that do not exist yet, each tied to the existing work that parents it. A row states the proposed ticket, its parent work, its category (from `pool/work-map.md`), the event that triggers filing, and who files. The standing rule governs every row: a follow-up ticket is filed only after its parent PR merges, unless a maintainer or the owner directs otherwise, and the owner files tickets himself. Agents maintain this registry; agents do not file Jira tickets.

Graduated off this registry: the three side-finding tickets the owner filed on 2026-10-12 (SOLR-18539 mergeIds, SOLR-18540 gradlew Git Bash, SOLR-18541 CombinedQueryComponent). Rows leave the registry the same way, by recording the filed number.

Security findings never enter this registry. They stay on the owner's private track.

| # | Proposed ticket | Parent work | Category | Trigger | State |
|---|---|---|---|---|---|
| 1 | SOLR-4841 follow-up: the language-detection test work gets its own ticket | SOLR-4841; Jan Høydahl directed a new ticket on the parent ticket | Suggester, spellcheck, and language detection | The owner files it; the ticket text was drafted and given to him 2026-10-14 | Awaiting the owner's filing. No branch is ready; the prepared work opens under the new number once it exists. |
| 2 | SOLR-18541 follow-up: the union-count crash follow-up | PR #5117 (SOLR-18541); named in the PR, and the ticket's reporter offered to file it | Search and query components | PR #5117 merges | Waiting on the merge. Do not file early. |
| 3 | Fragment anchor validation, the follow-up promised in PR #5061 | PR #5061, whose converter piece is superseded by PR #5180 (SOLR-18119) | Build, scripts, and release tooling | The owner decides the vehicle when PR #5061 closes | Promise recorded in #5061's description; with #5061 closing unmerged, the follow-up needs a new home or the owner's ruling that it lapses. |
| 4 | Native arrntv support in the Jackson writer | SOLR-17268, whose fix delegates to the legacy writer | Search and query components (response writers) | A maintainer orders the native work | Contingent only. The delegation fix is the submission; this row exists so the option is not lost, and it is offered to maintainers as their choice, never pre-filed. |
| 5 | The SOLR-18317 server-side coverage work | SOLR-18317 (closed; the related server branch cannot open under it) | Permissions and security declarations | A verdict lands on the coverage question, then the owner's call | Awaiting the verdict. If the work proceeds, it proceeds under a new ticket from this row. |

Maintenance: a row is added in the same push that records the promise or the direction creating it, citing the record it came from. A row whose trigger fires is updated the same day, and the filed ticket number replaces the row's state.
