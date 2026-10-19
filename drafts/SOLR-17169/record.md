# Skip record: SOLR-17169

- Ticket: SOLR-17169, "Solr restart issue with JWT authentication plugin enabled". JIRA status Open, resolution none, created 2024-02-20, last updated 2024-05-29, checked 2026-10-19 UTC.
- Verdict: close-answered.
- Draft branch: solr-17169-draft-close.
- Pinned head: fork origin/main `498a09aea97aac86c2cc7ac114113b946d249942`.
- Checked by: vm5, 2026-10-19 UTC.

## Ticket reading

The reporter configures the JWT authentication plugin with Azure AD. Login works when Solr starts, but after `service solr restart` Solr fails to start about half the time, with `SunCertPathBuilderException: unable to find valid certification path to requested target` while fetching the IdP's well-known configuration. The reporter says the same certificate is found on the successful starts. No comments.

## Code reading (at `498a09aea97aac86c2cc7ac114113b946d249942`)

- `solr/modules/jwt-auth/src/java/org/apache/solr/security/jwt/JWTIssuerConfig.java:119-122`: `init()` fetches `wellKnownUrl` once, with `fetchWellKnown(...)`. Only `MalformedURLException` is caught there; any other failure propagates out of init. There is no retry.
- `JWTIssuerConfig.java:218-220`: `fetchWellKnown` calls `WellKnownDiscoveryConfig.parse(url, trustedCerts)`, so the trust set passed in is the configured one, and it is empty when none is configured.
- `solr/modules/jwt-auth/src/java/org/apache/solr/security/jwt/JWTAuthPlugin.java:93-94, 117-118, 201-203`: the plugin accepts `trustedCertsFile` and `trustedCerts` and rejects configuring both.
- `solr/solr-ref-guide/modules/deployment-guide/pages/jwt-authentication-plugin.adoc:67-68`: both parameters default to the Java truststore.
- `jwt-authentication-plugin.adoc:195-201`: "By default, Java's built-in TrustStore is used. However, by configuring one of the options `trustedCertsFile` or `trustedCerts`, the plugin will *instead* trust the set of certificates provided, not any certificate signed by a root CA. Please configure either the `trustedCerts` or `trustedCertsFile` option."

So the error text is the one the plugin raises when the IdP's certificate chain is not in the trust set used at that start. The trust set is the JVM truststore unless `trustedCertsFile` or `trustedCerts` is configured.

## History

- No commit on the fork names SOLR-17169 (empty `git log --grep` at the pinned head).
- The trust options are documented at the pinned head; the history of the init path was not traced.

## Reproduction attempts

Not attempted. A reproduction would need an IdP with a certificate that the JVM truststore does not contain, a startup, and a second startup to see the intermittency. The intermittency is the part that the code cannot explain: the same fetch with the same trust set either passes or fails at startup, so the difference is in the environment at startup (the truststore the service sees, or the network path to the IdP when Solr starts), not in a Solr code path.

## Gap analysis

The question the reporter needs answered is how to make Solr trust the IdP certificate, and the documented answer is `trustedCertsFile` (or `trustedCerts`). The 50% restart failure is not explained by Solr code: a single fetch with a fixed trust set does not vary by restart except through the environment. A retry around the fetch in `init()` would change a restart from failing to succeeding when the failure is transient, but the reported error is a certificate-path failure, which a retry does not fix.

## Verdict justification

close-answered: "A support question that the reference guide or docs answer. Link the page." Minimum evidence: the page and the sentences (`jwt-authentication-plugin.adoc:195-201`), and the code that uses the trust set (`JWTIssuerConfig.java:119-122, 218-220`). The intermittent part is environmental and is not answered by the docs; the record says so, and the reporter should reopen with the trust configuration and the startup log if the failure stays after `trustedCertsFile` is set.

## Limits

This record does not reproduce the intermittent restart failure. It does not check the reporter's truststore or the Azure AD certificate chain. It does not establish whether a startup-time retry is wanted.
