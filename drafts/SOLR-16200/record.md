# Skip record: SOLR-16200

- Ticket: SOLR-16200, "cant cancel scheme/protocol redirect when accessing the admin UI". JIRA status Open, resolution none, fix versions none, created 2022-05-16, updated 2022-06-22. Checked 2026-10-19 UTC.
- Verdict: keep-open.
- Draft branch: solr-16200-draft-open.
- Pinned head: fork origin/main 498a09aea97aac86c2cc7ac114113b946d249942. No upstream sha cited.
- Checked by: vm2 (pool Linux host), 2026-10-19 UTC, for the remediation redo of this ticket (claim claims/skip-SOLR-16200-vm2.md).

## Ticket reading

The reporter runs SolrCloud behind a reverse proxy that terminates TLS. Opening the admin UI redirects the browser to http:// even when the request came in over https://. Changing the URL scheme in Solr breaks internal connections behind the proxy. The reporter asks how to keep the redirect from / to /solr/ while keeping the scheme that the client used.

The comments (13, 2022-05-16 to 2022-05-17 in the first pages read) say the admin UI uses relative URLs in its own requests, so the scheme comes from the browser. One commenter reports that an out-of-the-box install behind a proxy keeps https. With a security.json added, the same commenter reports that the admin UI stayed on https after login. Later the commenter says that with Solr listening only on localhost and the proxy using the same port, they could not reproduce the problem. No comment names a fix or a commit.

## Code reading

- solr/server/etc/jetty.xml:70-74 (origin/main 498a09aea97): the ForwardedRequestCustomizer block, which handles X-Forwarded-Proto and related headers, is commented out ("Uncomment to enable handling of X-Forwarded- style headers"). With the block commented out, Jetty does not take the scheme and host from the proxy headers, so a redirect that Jetty builds uses the scheme of the connection it received (http, when the proxy talks to Solr on plain HTTP).
- solr/core/src/java/org/apache/solr/servlet/HttpSolrCall.java:591 (origin/main): shouldAuthorize() exempts "", "/" and "/solr/" from authorization, so the root path reaches the static admin UI without a login redirect. This is the Solr-side handling of the root path; it does not build a Location header.
- solr/core/src/java/org/apache/solr/servlet/RedirectServlet.java (origin/main): the only sendRedirect-style servlet in Solr's own code. It is used for an old ZooKeeper page (solr/webapp/web/WEB-INF/web.xml around lines 153 to 166), not for the admin root. The root-to-/solr/ redirect is not in this file.
- solr/webapp/web/js/angular/controllers/login.js:85, 265, 279 and 292: the admin UI's login flow sends redirect_uri = $window.location.href, so the scheme of the page the browser is on is used there.

Commands at the pinned head:
- git grep -n 'sendRedirect\|RedirectServlet\|Location' origin/main -- solr/core/src/java solr/webapp: RedirectServlet only for Solr's own redirect code.
- git grep -n 'ForwardedRequestCustomizer\|X-Forwarded-Proto' origin/main -- solr/server solr/core/src/java solr/webapp/web/WEB-INF: jetty.xml:70-74 only (commented out).

## History

- Commits on origin/main naming SOLR-16200: none (evidence helper and git log --grep).
- The commented ForwardedRequestCustomizer block was introduced by 9464d2afb7b (SOLR-4839, reorganising the Jetty configs). Its ancestry on origin/main was checked: it is an ancestor of the pinned head.

## Reproduction attempts

Not attempted in this pass. The reproduction needs the packaged server (solr/packaging, built from the distribution) started behind a request that sends X-Forwarded-Proto: https, with the response Location header read. The test harness used for SOLR-16253 runs Solr in-process and does not load server/etc/jetty.xml, so it cannot show this behaviour. This pass did not build the distribution.

## Gap analysis

The likely cause is that the forwarded-headers customizer is not enabled in the packaged Jetty configuration. Jetty then ignores X-Forwarded-Proto when it builds redirect URLs. This is an inference from the configuration and from Jetty's usual behaviour; it has not been observed on this pinned head.

Two uncertainties remain. First, the redirect from / to /solr/ is not in Solr's own servlet code, so the Location header is probably built by Jetty's context handling, which has not been checked. Second, the reporter's proxy setup and version (8.11) are not known in detail, and the report was not checked against a 8.11 build. Uncommenting the block would change the behaviour for every deployment that sets forwarded headers, which is a decision for the owner, not for this record.

## Verdict justification

keep-open meets the rubric: the pinned head sha is cited, the Jira status and date are read in this pass, code paths are cited with file and line, and the attempts and the missing piece are listed. The missing piece is a reproduction on the packaged server: start the distribution, send the root request with X-Forwarded-Proto: https through the proxy path, read the Location header, and repeat with the block uncommented. Without that, the verdict cannot move to pr-needed, and close-cannot-reproduce needs a reproduction attempt that this pass did not make.

## Limits

This record does not establish the cause on a running server. It does not check the Helm chart or the reporter's proxy settings. It does not decide whether uncommenting the Jetty block is the right fix.
