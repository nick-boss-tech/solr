# Skip verdict remediation, batch 9

One line per item: ticket, claim, verdict recorded, draft branch head, evidence.

- SOLR-12236 | claims/skip-SOLR-12236-vm2.md | keep-open (was close-fixed; no fixing commit at 498a09aea97) | solr-12236-draft-open 1fc6474479 | JIRA read 2026-10-20 Open; code path Log4j2Watcher.java:297-318, MDCLoggingContext.java:83; repro not attempted, missing piece named
- SOLR-12234 | claims/skip-SOLR-12234-vm2.md | keep-open (kept; re-read at pinned head, no reproducer, query and documents missing) | solr-12234-draft-open 4c62501dc1 | JIRA read 2026-10-20T05:40:19Z Open; no code path named by the ticket; history none
- SOLR-12220 | claims/skip-SOLR-12220-vm2.md | keep-open (kept; re-read at pinned head; Windows-only, no Windows run possible here) | solr-12220-draft-open | JIRA read 2026-10-20T05:44:37Z Open; CachingDirectoryFactory.java:262 and StandardDirectoryFactory.java:90 present; stress method name not found
- SOLR-12166 | claims/skip-SOLR-12166-vm2.md | close-obsolete (changed from keep-open; LIR mechanism and test absent, grep empty at pinned head; removal commit not named) | solr-12166-draft-close f78590958f | JIRA read 2026-10-20T05:53:10Z Open
