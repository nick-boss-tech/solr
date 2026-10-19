# Skip record: SOLR-15330

- Ticket: SOLR-15330, Solr 7.5 memory leak and crash with sql injection type queries. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2021-04-12, updated 2021-07-14, no fix versions, no links.
- Verdict: keep-open.
- Draft branch: solr-15330-draft-open (the existing keep-open branch; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read and history checked at this head (detached worktree).
- Source: remediation redo of batch 03, item 13, under assignments/pool-aaa-skip-verdict-remediation-03.md. The earlier record (round audit-1, 2026-10-04, confirmed-skip) is background only.

## Ticket reading

On Solr 7.5 standalone nodes, a long q parameter with SQL-injection text (a UNION ALL SELECT built from CHR() calls) makes a query time out and then the node crashes after a memory climb. The reporter's analysis chain uses SynonymGraphFilterFactory on index and query. The reporter says 8.8 does not show the problem. The maintainer comment (magibney, 2021-04-12 and 2021-07-14) suggests the cause may interact with SOLR-13336 (maxBooleanClauses and query expansion), and says a guess, not a confirmed cause.

## Code reading and history

- SOLR-13336 is Closed (Fixed) with fix versions 8.1 and 9.0. Its commits on the pin are d90034f0d6 (2019-04-15, "add maxBooleanClauses (default to 1024) setting to solr.xml ... to restrict risk of pathalogical query expansion") and 1c3d23e58a (2019-04-19, CloudInspectUtil filter change). `git merge-base --is-ancestor d90034f0d6 498a09aea97aac86c2cc7ac114113b946d249942` succeeds, so the fix is on the pin.
- The limit is enforced on the pin: solr/core/src/java/org/apache/solr/core/CoreContainer.java:417 sets the global IndexSearcher max clause count from the node config; solr/core/src/java/org/apache/solr/core/SolrConfig.java:270 reads the per-core `maxBooleanClauses`; solr/core/src/java/org/apache/solr/search/QueryUtils.java:228 produces the clause-limit error message.

So a query that expands past the limit is rejected with an error on the pin, rather than being built. Whether the reporter's query expands past the limit, and whether a rejected query avoids the memory growth the reporter saw, is not shown.

## Reproduction attempts

Not run. A reproduction needs the reporter's index analysis chain (the synonym files are not in the ticket) and the q string, run against the pinned head, and a check of the clause count it produces and the memory behavior. That needs a built core with the synonym set.

## Gap analysis

The candidate fix (d90034f0d6) is on the pin, and it bounds the expansion that the maintainer suspected. The ticket does not show that the reporter's crash was that expansion, and the reporter's crash was on 7.5. The reporter's "fixed on 8.8" is an observation, not a diagnosis. So neither close-fixed (the cause is not confirmed) nor pr-needed (no defect shown on the pin) is supported.

## Verdict justification

keep-open: the attempts made are the code path reads above at the pinned head, the Jira read, and the history check. The missing piece is a run of the reporter's query (with a synonym analysis chain that matches the report) on the pinned head, showing that it is rejected by the clause limit and does not grow memory, and the synonym files from the reporter. If the run shows the clause limit rejects the query, the next verdict is close-fixed with d90034f0d6 named.

Missing for a closing verdict: (1) the run with the reporter's analysis chain and q string, command and result, and log path; (2) the reporter's synonym configuration.

## Limits

This record does not reproduce the crash. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
