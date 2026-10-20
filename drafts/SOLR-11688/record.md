# Skip record: SOLR-11688

- Ticket: SOLR-11688, Transient cache should not get initiated unless explicitly configured. JIRA read in this pass: Open, resolution none, fix versions none, 1 comment (2017-11-28, erickerickson on the default transient-cache nuance); updated 2019-06-08.
- Verdict: close-obsolete (changed from close-fixed in the earlier record on this branch; the earlier close-fixed reasoning cited the removal commit cb6dd31eca and an empty grep, and its gap analysis already said close-obsolete fits, so this record uses that verdict).
- Draft branch: solr-11688-draft-close (same close draft; the status is unchanged, so the branch is updated).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (checked at this head for this record).
- Source: the queue entry (audit verdict confirmed-skip, audit date 2026-10-19) is background only; the reading below is fresh at the pin.

## Ticket reading

The ticket: Solr always allocates a default transient-core cache (log "Allocating transient cache for 2147483647 transient cores", a 1000-entry LinkedHashMap) even when no transient cores are configured. The reporter asks that the cache not be initialized unless configured. The comment raises the nuance that a core can still be created with transient=true under the default cache.

## Code reading

- The named class TransientSolrCoreCacheDefault and its log line "Allocating transient cache" are absent from the whole solr tree at the pin (git grep -- solr: 0 hits for "Allocating transient" and for TransientSolrCoreCacheDefault).
- The removal: commit cb6dd31eca (SOLR-17932, "Remove deprecated Solr Transient Cores", 2025-09-27) deleted solr/core/src/java/org/apache/solr/core/TransientSolrCoreCache.java, TransientSolrCoreCacheDefault.java and TransientSolrCores.java, and changed solr/core/src/java/org/apache/solr/core/SolrCores.java (112 lines, including the removal of the pendingCloses list and the core closer thread). Ancestry checked: cb6dd31eca is an ancestor of the pin.
- What remains at the pin mentions "transient cores" only in comments in solr/core/src/java/org/apache/solr/core/SolrCores.java (for example :137, :186, :194) and in solr/core/src/test/org/apache/solr/core/TestLazyCores.java (:271, :513); these describe the removed concept or lazy-core tests, not an allocated cache.

So the cache the ticket asks to avoid no longer exists, and the transient-core feature it configures was removed by SOLR-17932.

## History

- No commit at the pin names SOLR-11688 (git log --grep at the pin: none).
- SOLR-17932 (cb6dd31eca) is the removal; it is the commit the close verdict rests on.

## Reproduction attempts

No run was attempted. For close-obsolete the rubric's check is the removed code and the whole-tree grep, which are done above.

## Gap analysis

The ticket's behavior (a default transient cache allocated at startup) cannot occur at the pin because the feature was removed. The comment's nuance (a core created with transient=true under the default cache) concerns the removed feature too; if a core option still named transient is accepted, that would be a separate question and should be a new ticket.

## Verdict justification

close-obsolete: the named class and log line are absent from the whole tree at the pin, and the commit that removed them (cb6dd31eca, SOLR-17932) is an ancestor of the pin. The rubric's close-obsolete check (removed code, empty whole-tree grep) is met. close-fixed was not supported: no fixing commit for this ticket was found, only the removal.

## Limits

This record does not run the core-loading tests at the pin and does not check whether a core.properties transient option is still accepted. It does not post anything to JIRA or GitHub.
