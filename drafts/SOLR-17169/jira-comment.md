This is answered by the reference guide, for the certificate part. We checked against Solr main at commit 498a09aea9.

By default the JWT authentication plugin trusts the certificates in the Java truststore. If the identity provider's certificate chain is not in that truststore, Solr fails to load the plugin with "unable to find valid certification path". The fix is to tell the plugin which certificates to trust, with trustedCertsFile (or trustedCerts). The JWT authentication plugin page describes both options: https://solr.apache.org/guide/solr/latest/deployment-guide/jwt-authentication-plugin.html (the "Trusting the IdP server" section).

The part we cannot explain from the code is why the restart succeeds about half the time. The plugin uses the same trust set on every start, so the difference is likely in the environment at startup (the truststore the service sees, or the path to the identity provider when Solr starts).

If it still fails after trustedCertsFile is set: please send the certificate chain you configured, the JVM version, and the startup log for one failed start and one successful start.
