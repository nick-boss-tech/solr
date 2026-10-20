# Critical and Blocker backlog, slice 04 (partial)

Assignment: `assignments/pool-csv-critical-blocker-round-1.md`. Claim: `claims/pool-csv-critblock-04.md` (released as partial at 2026-10-20T02:11:27Z). Auditor: vm2. Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. JIRA read 2026-10-20 (UTC): status, resolution, fix version, comment count, and description for all 25 tickets (one JQL call).

Status of the slice: 12 of 25 classified below. 13 are not classified: no reproduction or code read was done for them in this pass, and a classification without evidence does not count under the assignment. Another host can claim the slice's remaining items as a new claim.

## Classified

| Ticket | Priority | Classification | Evidence (pinned head unless stated) |
| --- | --- | --- | --- |
| SOLR-17840 | Blocker | ALREADY-FIXED | Fix commit efeee564663 "SOLR-17840: Fixing LTR after Lucene 10 upgrade" is an ancestor of the pinned head. The workaround is in solr/modules/ltr/.../scoring/MultiFeaturesScorer.java:129-173 (mimics DisjunctionDISIApproximation). The timeout part of the ticket was not checked. |
| SOLR-18552 | Blocker | STILL-PRESENT | No sharedArenaMaxPermits in solr/bin/solr or solr/packaging. The default is not set. Fix is a one-line bin/solr change; no branch or gate made in this pass. |
| SOLR-18446 | Blocker | STILL-PRESENT | solr/core/.../servlet/HttpSolrCall.java:757 still sets the "collection" parameter from the alias-resolved collections. Fix is a routing change, not a one-liner; linked SOLR-18470 not read. |
| SOLR-18398 | Blocker | STILL-PRESENT | solr/core/.../response/SolrQueryResponse.java:94 still initializes values as a NamedList (SimpleOrderedMap). The ticket asks for a LinkedHashMap; this is a design change. |
| SOLR-18297 | Blocker | STILL-PRESENT | gradle/libs.versions.toml:84 pins eclipse-jetty 12.1.12. The ticket asks for 12.1.14 (the CSV says a PR is available; not checked). fixVersion 10.2 recorded in JIRA. |
| SOLR-18458 | Blocker | STILL-PRESENT | debugQuery is still honored: solr/core/.../handler/component/DebugComponent.java:166,176 and handler/MoreLikeThisHandler.java:232. Linked SOLR-17931 not read. |
| SOLR-17252 | Critical | STILL-PRESENT | dev-tools/scripts/releaseWizard.py:196 still lists nano in the editor branch. The ticket's hang is in the release wizard flow; the line was read, the hang was not reproduced. |
| SOLR-18486 | Blocker | NEEDS-OWNER | Release-process task (reference guide complete before a 10.1.0 announcement). Not a code fix. The owner or release manager decides the gate. |
| SOLR-18240 | Blocker | NEEDS-OWNER | Release-process task (9.11.0 major changes and upgrade notes). Not a code fix. |
| SOLR-17988 | Blocker | NEEDS-OWNER | Release-documentation task (Solr 10 property-name changes). Not a code fix. |
| SOLR-17842 | Blocker | NEEDS-OWNER | Benchmarks publication task. Not a code fix. Owner decides scope. |
| SOLR-18501 | Blocker | NEEDS-OWNER | Legal and IP clearance for solr-mcp (CLA, clearance). Not a code item; the owner handles it. |

## Not classified in this pass

SOLR-18010, SOLR-17788, SOLR-17767 (umbrella, SIP-24), SOLR-17762, SOLR-17546 (.htaccess for removed pages; playbook.template.yml:25 only read), SOLR-17463, SOLR-17433 (SolrStream no longer contains the client construction; SolrClientCache not found in solrj/src/java; the new location was not traced), SOLR-17375, SOLR-17370, SOLR-17297, SOLR-17291 (the message "indexDeletionPolicy must not be null" is not in SolrCore; IndexDeletionPolicyWrapper.java is the likely home, not read), SOLR-17281, SOLR-17844.

## Limits

No Jira or GitHub posts. No branches, gates, or receipts were made in this pass. The STILL-PRESENT items each need their own fix branch, gate run, and receipt under the assignment's step 4 before they can be counted as done.
