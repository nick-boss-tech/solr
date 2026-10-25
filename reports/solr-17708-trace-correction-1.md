# SOLR-17708 trace, correction 1

Corrects `reports/solr-17708-double-auth-trace-round-1.md`. The report stays as published; this file supersedes one sentence.

The report says, under "Fix options", that layer one "has no resource-method permission name". The code supports a narrower statement:

- Layer one builds its handler from `_getHandler()`, which returns `api` (`solr/core/src/java/org/apache/solr/api/V2HttpCall.java:527-528`). That is the `ApiBag` entry, not the Jersey resource method.
- So layer one does not read the Jersey resource method's `@PermissionName`. The annotation is read only by layer two (`SolrRequestAuthorizer.java:123`).
- Not checked: whether `api` is null or a placeholder for a JAX-RS route. The corrected statement holds either way.

Corrected first fix option: removing layer two needs layer one to get the permission name for a JAX-RS route from somewhere other than the Jersey resource annotation. The report's other claims are unchanged.
