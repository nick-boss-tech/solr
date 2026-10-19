# Skip record: SOLR-17610

- Ticket: SOLR-17610, "Solr 9.7 on CentOS 7 with OpenJDK 11 has a 10-second delay for local network client access". JIRA status read 2026-10-19 (UTC): Open, resolution none, created 2024-12-23, no fix version, no comments, no maintainer reply.
- Verdict: keep-open. Unchanged from the earlier record.
- Draft branch: solr-17610-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942 (2026-10-07). Upstream sha not cited.
- Checked by: vm4, 2026-10-19 13:20-13:55 UTC.

## Ticket reading

Solr 9.7 on CentOS 7 with OpenJDK 11. A client on the local network waits about 10 seconds. Server and client use different nameservers; adding the client's hostname to the server's `/etc/hosts` fixes it. The reporter suspected Jetty's reverse lookup of the client hostname. Turning off the request log did not fix it. Turning off the security manager (`SOLR_SECURITY_MANAGER_ENABLED=false`) or granting all permissions does. The ticket has no comments and no reproduction steps.

## Code reading

- Solr code has no per-request reverse lookup. `git grep -n -E 'getCanonicalHostName|getRemoteHost\(|InetAddress\.getBy|getHostName\(\)|getLocalHost\(' 498a09aea97... -- solr/core/src/java solr/solrj/src/java` finds `SystemInfoProvider.java:511-512` (host name for system info, `getCanonicalHostName`), `AddressUtils.java:37`, and the node-name helpers (`ZkController.getHostName`, `CoreContainer.getHostName`), which do not do reverse lookups per request.
- `solr/core/src/java/org/apache/solr/security/HttpServletAuthorizationContext.java:87-88` defines `getRemoteHost()` as `servletRequest.getRemoteHost()`, which can reverse-resolve the client address. `git grep` finds no caller of `getRemoteHost()` in `solr/`. `AuditEvent.java:133` uses `getRemoteAddr()`, which does not resolve.
- `solr/server/etc/security.policy:51-53` grants `resolve` for localhost, 127.0.0.1 and ::1. `security.policy:217` grants `accept,listen,connect,resolve` for `${solr.internal.network.permission}`.
- `solr/bin/solr:1176-1182`: with the security manager enabled (the default below Java 24), the script sets `-Dsolr.internal.network.permission=*`. So under the default startup, the policy allows resolution to any host. A resolve denial is not the explanation for the default setup.
- `solr/bin/solr:1170-1172`: the security manager is turned off on Java 24 and later.

## History

- No commit on the pinned head names SOLR-17610 (evidence tool: no hits).
- No commit found in this pass for the security policy or the startup wildcard; not traced by history.

## Reproduction attempts

Not attempted. A reproduction needs CentOS 7 with OpenJDK 11, Solr 9.7, and two resolvers that differ for the client's address. This pass is review-only and runs no builds.

## Gap analysis

The code read does not find a Solr-side per-request reverse lookup, which is the go-if the ticket's note names. The security policy grants resolution under default startup. The effect the reporter saw therefore likely comes from the JVM or the environment (name service lookup order, the JDK's reverse lookup cache, or the security manager's interaction with the lookup under JDK 11). That is a hypothesis; the ticket does not say which stack frame blocked.

Missing piece for a verdict change: a thread dump (`jstack`) taken during a delayed request on a host that reproduces the delay, which would name the blocked frame; or the reporter's `/etc/nsswitch.conf` and resolver setup.

## Verdict justification

keep-open meets the rubric: the attempts are listed (code read, no build), and the missing piece is named (the blocked frame in a thread dump during a delayed request, or the environment's resolver setup). A close needs a Solr-side fix or a demonstrated non-Solr cause, and neither is shown. A pr-needed needs a Solr code line that causes the delay, and none was found.

## Limits

This record does not reproduce the delay and does not identify the JVM frame. It does not show that the cause is outside Solr. It does not post anything to JIRA or GitHub.
