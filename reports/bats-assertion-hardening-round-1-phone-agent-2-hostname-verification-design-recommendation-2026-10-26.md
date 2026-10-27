# TLS client-hostname verification: implementation decision

**Bottom line:** I did not make a source change or create a patch. The pinned setting’s server-side identity policy is underspecified, so guessing at an IP, reverse-DNS, or client-certificate principal rule would change security semantics. A test-only adjustment cannot make the current behavior a valid hostname check.

## What the pinned source does

All inspection used a fresh, isolated local worktree at baseline `0d7c6f4e6cb6d2211b59ed9eed36db90182b122e`, branch `local/tls-client-hostname-fix-20261026`, path `/dev/shm/solr-tls-client-hostname-fix-0d7c6f4`. It is clean and unchanged.

`solr/bin/solr:243–245` maps `SOLR_SSL_CLIENT_HOSTNAME_VERIFICATION=true` to `-Dsolr.jetty.ssl.verifyClientHostName=HTTPS`. `solr/server/etc/jetty-ssl.xml:9,26` applies that value to Jetty’s `SslContextFactory.Server` as `EndpointIdentificationAlgorithm`. Solr’s upgrade note (`solr/solr-ref-guide/modules/upgrade-notes/pages/major-changes-in-solr-9.adoc:265–267`) says this setting should check the originating client hostname against the client certificate during mTLS.

The test setup contradicts that direction: `solr/packaging/test/test_ssl.bats:282–294` creates a client certificate with `DNS:test.solr.apache.org` and `IP:127.0.0.2`; lines `321–323` explain that it does not use `localhost`; lines `357–366` connect to the server at `https://localhost` and expect the server to reject the client certificate for that mismatch. `localhost` here is the client’s target server name. The server does not learn that target name as the client’s identity; its TLS peer is the connecting client.

OpenJDK’s `X509TrustManagerImpl` validates the chain, then uses `SSLSession.getPeerHost()` for endpoint identification. When `checkClientTrusted` fails under `HTTPS`, it reports `Endpoint Identification Algorithm HTTPS is not supported on the server side`. OpenJDK issue [JDK-8229972](https://bugs.openjdk.org/browse/JDK-8229972) explains that HTTPS hostname checks are client-side; a server usually has the client IP, and a different client-IP policy would require a custom trust manager. Java’s [SSLSession API](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/javax/net/ssl/SSLSession.html) explicitly says the server’s `getPeerHost()` may just be an address string and is not authenticated or reliable. [RFC 2818 §3.2](https://www.rfc-editor.org/rfc/rfc2818.html#section-3.2) likewise says a server typically has no external knowledge of the client’s expected identity beyond its trusted certificate chain.

Jetty 12.1.14 has a technical extension point: [SslContextFactory source](https://github.com/jetty/jetty.project/blob/jetty-12.1.14/jetty-core/jetty-util/src/main/java/org/eclipse/jetty/util/ssl/SslContextFactory.java) exposes protected trust-manager creation and an `X509ExtendedTrustManagerWrapper`. That makes a custom policy implementable, but does not define which identity Solr should trust. The server-side engine receives a remote peer address; its `peerHost` is only an unauthenticated hint, not proof of the client’s DNS identity.

## Decision needed before implementation

Choose and document one explicit policy: (1) trust the mTLS certificate chain and map its certificate principal/SAN to an application identity; (2) bind the certificate’s IP SAN to the actual observed transport source IP, with explicit NAT/proxy/address-change limitations; or (3) compare a DNS client identity supplied by a separately configured, trusted mapping. Do not substitute reverse DNS, the server’s `localhost` destination name, or SNI as the client identity. If source-IP binding is selected, it is an IP-address authorization rule—not hostname verification—and the default-enabled setting may have compatibility impact.

After the policy is chosen, add a focused positive/negative integration test for that exact identity source while preserving ordinary certificate-chain validation. Until then, clearing the server endpoint algorithm or weakening the BATS assertion would hide the defect rather than implement the documented behavior.

## Scope and safety

No source files were edited; no patch was produced; no Gradle or BATS command was run; no gate, claim, report, remote ref, or coordination record was changed. The fresh worktree remains clean at the pinned baseline. The host lock was free at the final check; root had 178,255 free inodes (21.3%), and `/dev/shm` had 3.2 GiB free. The existing gate remains OPEN/INCOMPLETE.

Additional source notes: `/tmp/tls-hostname-fix-investigation-20261026/external-sources.md`.