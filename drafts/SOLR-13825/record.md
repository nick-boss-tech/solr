# Skip record: SOLR-13825

- Ticket: SOLR-13825, AccessDeniedException on a Lucene index file under an Azure App Service (Windows) path. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2019-10-08, updated 2020-01-21, no fix versions, no links, no comments.
- Verdict: keep-open.
- Draft branch: solr-13825-draft-open (the existing keep-open branch; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Greps run at this head (detached worktree).
- Source: remediation redo of batch 05, item 20, under assignments/pool-aaa-skip-verdict-remediation-05.md. The earlier record (vm2, from the B3 reading) is re-checked here; its FSDirectory list was incomplete and is corrected below.

## Ticket reading

On one Solr node hosted on Azure App Service (Windows), a request failed with AccessDeniedException on a Lucene index file under the Solr data directory, during fsync. The reporter saw a 5xx wave.

## Grep at the pinned head

- `git grep -i -l "azure" 498a09aea97aac86c2cc7ac114113b946d249942 -- solr/solr-ref-guide solr/core/src/java | wc -l` gives 0. Neither the reference guide nor the core code mentions Azure.
- `git grep -l "AccessDenied" 498a09aea97aac86c2cc7ac114113b946d249942 -- solr/core/src/java | wc -l` gives 0. Solr does not raise or map this exception itself.
- `git grep -l "FSDirectory" 498a09aea97aac86c2cc7ac114113b946d249942 -- solr/core/src/java` lists several files, among them blockcache/BlockDirectory.java, core/MMapDirectoryFactory.java, core/NIOFSDirectoryFactory.java, core/NRTCachingDirectoryFactory.java and core/SolrDeletionPolicy.java. The earlier record listed fewer; the set is larger but the conclusion is the same.

## History

- No commit on the pin names SOLR-13825.

## Reproduction attempts

Not applicable here. The exception comes from the operating system's file permissions on a Windows host under Azure App Service, which this environment does not have.

## Gap analysis

The exception is raised by the file system during Lucene's fsync, after Solr hands it a file it created. Nothing in the Solr tree maps or handles this case, and the ticket gives no permission or hosting detail that would change that. The cause is the hosting environment, which the project does not control.

## Verdict justification

keep-open: the attempts made are the greps above at the pinned head, the Jira read, and the mechanism reading. The missing piece is the environment detail (the file's owner and permissions on the App Service path, and whether another process held the file) and a Windows run if the project is to treat it as a Solr issue. close-answered is not supported: no documentation covers Azure or Windows file locking here. close-obsolete and close-fixed do not apply.

Missing for a closing verdict: the permission detail from the reporter, or a decision that this is a hosting issue (and a note in the docs).

## Limits

This record does not reproduce the access failure. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
