This is no longer present on Solr main. We checked against main at commit 498a09aea9.

What we checked:

- The class named in the report, AdminHandlersProxy, was removed in SOLR-16738 (commit 730398a940, April 2026). The refactor moved node requests to a new proxy package.
- The new proxy sends each node request through a shared HTTP client. It does not create a client per node, so there is no per-node client that could stay open after an error.
- A search of the code finds no remaining use of the class. One comment still names it in SystemInfoResponse; that comment does not affect behavior.

What this means: the reported leak path no longer exists, so there is nothing left to fix for this ticket. We recommend closing it as obsolete.

If you still see this: please send the Solr version and the stack trace. If it shows an unclosed client on a current release, we will reopen the ticket and check it.
