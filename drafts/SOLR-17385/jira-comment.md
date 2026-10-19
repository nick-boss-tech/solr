We checked this against Solr main at commit 498a09aea9. The limitation is in the code, and a fix is needed.

What we checked:

- The coordinator only federates a request when its path ends in /select. The check is in CoordinatorHttpSolrCall (getCoreByCollection). A request to a custom request handler with another name returns no core, so it is not federated.
- The check dates from SOLR-15715 (commit d029eb14aa) and has not changed since.
- The RH-list design from the comments is not on main.

What this means: a custom request handler cannot use the coordinator today unless its path ends in /select. A change is needed to let operators choose which handlers are federated. The design is still open; the proposal in the comments (a list in solrconfig.xml) is one option.

If you still see this: please send the name of the handler you want federated and the Solr version you run, so the test for the change can use the same setup.
