# Skip record: SOLR-15388

- Ticket: SOLR-15388, PKIAuthenticationPlugin intercepts every outgoing requests not just inter-nodes. JIRA read 2026-10-19 (this pass, evidence tool): status Open, resolution None, created 2021-04-30, updated 2021-06-23, no fix versions, no links.
- Verdict: keep-open.
- Draft branch: solr-15388-draft-open (the existing keep-open branch; this record replaces its draft files).
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. Code read at this head (detached worktree).
- Source: remediation redo of batch 03, item 10, under assignments/pool-aaa-skip-verdict-remediation-03.md. The earlier record (round audit-1, 2026-10-04, confirmed-skip) is background only.

## Ticket reading

Solr sends audit logs to another Solr through Ranger, which uses a CloudSolrClient. The PKI authentication plugin intercepts that outgoing request, adds Solr's internode headers, and the receiving Solr, which uses Kerberos, rejects them with a PKI decryption failure. The reporter asks that PKI apply only to internode requests. Discussion in the ticket (janhoy, gezan, gus, 2021-04-30 to 2021-05-12) finds that the interceptors are registered globally in HttpClientUtil, so every client gets them, and that a client built with `withHttpClient()` avoids them.

## Code reading

- solr/core/src/java/org/apache/solr/security/PKIAuthenticationPlugin.java:317-327: the PKI listener's `onQueued` runs for every request on the clients it is registered with, and calls `cores.getAuthenticationPlugin().interceptInternodeRequest(request)` at :327. There is no destination check in this listener; it only asks the authentication plugin whether it handles the request.
- solr/core/src/java/org/apache/solr/security/AuthenticationPlugin.java:134-136: the base `interceptInternodeRequest` returns `this instanceof HttpClientBuilderPlugin`. A plugin that is not a client-builder plugin returns false, which makes the PKI listener add its own headers to that request. The overrides at BasicAuthPlugin.java:215 and MultiAuthPlugin.java:278-280 decide per request. The Kerberos plugin has no override in this tree, so it uses the base method. None of these checks whether the destination is inside the cluster.
- The listener is registered through the `setup(MutableListenerFactory)` hook of the plugin (PKIAuthenticationPlugin.java:315). The ticket's point about global registration fits this design: the listener is in a shared factory, not on a per-client basis.

So the code path that the ticket names is still present at the pinned head: a request to an external Solr from a client created by the shared factory gets PKI headers unless the authentication plugin says it handled the request.

## History

- SOLR-12799 (81dbad54e0, 2018-12-13) added the per-request `interceptInternodeRequest` hook. It is the origin of the current design, and it is an ancestor of the pin.
- No commit names SOLR-15388.

## Reproduction attempts

Not run. A reproduction needs two Solr nodes, one with PKI and Kerberos and one external, and a client created by the shared factory sending to the external node. That is an integration setup this pass did not build.

## Gap analysis

The mechanism is clear and still present. The fix is not a one-line change: the ticket discussion (janhoy, 2021-05-11) says the long-term fix is per-client registration, which changes how the interceptors are installed for every client. The short-term workaround in the ticket (a `withHttpClient()` builder for the external client) is a Ranger-side change, not a Solr change, and the ticket does not record whether it was tested.

## Verdict justification

keep-open: the attempts made are the code path reads above at the pinned head and the Jira read. The missing piece is the design decision (per-client interceptor registration, or an allow rule for external destinations), and a run that shows the Kerberos failure on the pin. The ticket's own discussion leaves that decision open; a closing verdict would need the maintainers to answer it.

Missing for a closing or PR verdict: (1) an integration run with an external Solr node and PKI on the pin; (2) a maintainer decision between per-client registration and a destination rule.

## Limits

This record does not reproduce the decryption failure. It does not post anything to JIRA or GitHub. The draft comment is a draft until someone posts it by hand.
