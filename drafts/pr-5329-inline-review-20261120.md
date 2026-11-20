# Assignment: review draft inline comments for Solr PR 5329

- **Source:** [SOLR-18659](https://github.com/apache/solr/pull/5329), head `201446e25e676d85860e3ed378635eab7d7235dd`.
- **Task:** Check the comments against the latest PR diff and choose which to use. The prompts below are drafts, not GitHub comments.
- **Evidence:** Static review of the Solr diff and ZooKeeper 3.9.6 and JDK behavior. No Solr build or runtime test was run.
- **Boundaries:** Do not post these on the PR, edit the PR, or open another PR unless Nick expressly asks. If the head moves, recheck changed lines and remove findings that are resolved. Route runtime verification separately.

Each heading is for the assignment only. Copy just the disclosure and body for any comment Nick chooses to post. For the removed prompt, use the LEFT side; the rest use the RIGHT side.

## 1. Keep the destructive reset opt-in

[RunExampleTool.java:684](https://github.com/apache/solr/blob/8b2e99721e28c66b96654d0dff440d74785febb2/solr/core/src/java/org/apache/solr/cli/RunExampleTool.java#L684) · `solr/core/src/java/org/apache/solr/cli/RunExampleTool.java` · LEFT `684`

🤖 *AI text below* 🤖

Dropping this prompt makes an interactive `secure` run delete the selected Solr home immediately, including an existing home passed with `--solr-home`. Can the prompt stay unless the caller passes `--no-prompt`?

## 2. Preserve client-certificate policy

[ZookeeperSSLConfigurations.java:59](https://github.com/epugh/solr/blob/201446e25e676d85860e3ed378635eab7d7235dd/solr/core/src/java/org/apache/solr/util/configuration/ZookeeperSSLConfigurations.java#L59) · `solr/core/src/java/org/apache/solr/util/configuration/ZookeeperSSLConfigurations.java` · RIGHT `59`

🤖 *AI text below* 🤖

ZooKeeper requires a client certificate when `ssl.clientAuth` is unset. This default also applies to listeners already configured in `zoo.cfg`, so upgrading can change their authentication policy. Could `none` apply only to the listener Solr creates automatically?

## 3. Preserve trust anchors during revocation checks

[ZookeeperSSLContextSupplier.java:99](https://github.com/epugh/solr/blob/201446e25e676d85860e3ed378635eab7d7235dd/solr/core/src/java/org/apache/solr/util/configuration/ZookeeperSSLContextSupplier.java#L99) · `solr/core/src/java/org/apache/solr/util/configuration/ZookeeperSSLContextSupplier.java` · RIGHT `99`

🤖 *AI text below* 🤖

This copies certificates from key entries into the trust-anchor set, which differs from ZooKeeper’s normal PKIX setup. Could the new path preserve the same trust anchors and add a test that verifies revocation is enforced, not just that a handshake succeeds?

## 4. Honor hostname verification on the status socket

[ZookeeperStatusHandler.java:405](https://github.com/epugh/solr/blob/201446e25e676d85860e3ed378635eab7d7235dd/solr/core/src/java/org/apache/solr/handler/admin/ZookeeperStatusHandler.java#L405) · `solr/core/src/java/org/apache/solr/handler/admin/ZookeeperStatusHandler.java` · RIGHT `405`

🤖 *AI text below* 🤖

In non-FIPS mode with default JVM trust, this socket path does not inherit the endpoint-name check that ZooKeeper’s Netty client applies. Could the status probe use the same hostname verification policy, including the explicit opt-out?

## 5. Keep the hostname used for the embedded client

[SolrZkServer.java:85](https://github.com/epugh/solr/blob/201446e25e676d85860e3ed378635eab7d7235dd/solr/core/src/java/org/apache/solr/cloud/SolrZkServer.java#L85) · `solr/core/src/java/org/apache/solr/cloud/SolrZkServer.java` · RIGHT `85`

🤖 *AI text below* 🤖

The embedded connect string still uses the numeric bind address by default. With TLS now on, a certificate valid for the configured DNS name can fail the local ZooKeeper connection without an IP SAN. Could the client use a certificate-compatible name independently of the bind address?

## 6. Separate embedded-server and client store defaults

[ZkContainer.java:109](https://github.com/epugh/solr/blob/201446e25e676d85860e3ed378635eab7d7235dd/solr/core/src/java/org/apache/solr/core/ZkContainer.java#L109) · `solr/core/src/java/org/apache/solr/core/ZkContainer.java` · RIGHT `109`

🤖 *AI text below* 🤖

The ZooKeeper config parser publishes the server’s derived SSL settings to JVM properties before client initialization. The client then treats them as explicit overrides, which can discard the separate outbound store settings. Could the server and client defaults stay separate while preserving intentional ZooKeeper overrides?

## 7. Preserve an explicit secure bind address

[SolrZkServer.java:150](https://github.com/epugh/solr/blob/201446e25e676d85860e3ed378635eab7d7235dd/solr/core/src/java/org/apache/solr/cloud/SolrZkServer.java#L150) · `solr/core/src/java/org/apache/solr/cloud/SolrZkServer.java` · RIGHT `150`

🤖 *AI text below* 🤖

This overwrites `secureClientPortAddress` even when the value was explicitly set in `zoo.cfg`. An existing secure listener can move to loopback on upgrade and lose its joining nodes. Could the Solr bind address be a fallback for an already-configured secure listener?

## 8. Use secure endpoints for status probes

[ZookeeperStatusHandler.java:357](https://github.com/epugh/solr/blob/201446e25e676d85860e3ed378635eab7d7235dd/solr/core/src/java/org/apache/solr/handler/admin/ZookeeperStatusHandler.java#L357) · `solr/core/src/java/org/apache/solr/handler/admin/ZookeeperStatusHandler.java` · RIGHT `357`

🤖 *AI text below* 🤖

The status handler can take its addresses from ZooKeeper’s dynamic ensemble metadata. In a dual-port ensemble that metadata can advertise the plain client port, so the probe can report a healthy TLS ensemble as down. Could TLS probes use the secure connect string when the metadata does not identify secure ports?

## 9. Resolve CLI store paths before deriving ZooKeeper settings

[SolrCLI.java:275](https://github.com/epugh/solr/blob/201446e25e676d85860e3ed378635eab7d7235dd/solr/core/src/java/org/apache/solr/cli/SolrCLI.java#L275) · `solr/core/src/java/org/apache/solr/cli/SolrCLI.java` · RIGHT `275`

🤖 *AI text below* 🤖

This copies relative store paths before the legacy CLI resolves them against the install directory, and Picocli does not use that resolver. Could both entry points resolve the effective paths before ZooKeeper initialization, so the stock `etc/…` paths work from the caller’s directory?

## 10. Honor existing Jetty password providers

[ZookeeperSSLConfigurations.java:57](https://github.com/epugh/solr/blob/201446e25e676d85860e3ed378635eab7d7235dd/solr/core/src/java/org/apache/solr/util/configuration/ZookeeperSSLConfigurations.java#L57) · `solr/core/src/java/org/apache/solr/util/configuration/ZookeeperSSLConfigurations.java` · RIGHT `57`

🤖 *AI text below* 🤖

Jetty decodes obfuscated passwords supplied by its credential provider, but ZooKeeper receives the derived value verbatim here. Could inherited Jetty values be normalized before opening the ZooKeeper stores, without changing explicitly supplied ZooKeeper passwords?

## 11. Validate TLS transport compatibility

[ZookeeperSSLConfigurations.java:43](https://github.com/epugh/solr/blob/201446e25e676d85860e3ed378635eab7d7235dd/solr/core/src/java/org/apache/solr/util/configuration/ZookeeperSSLConfigurations.java#L43) · `solr/core/src/java/org/apache/solr/util/configuration/ZookeeperSSLConfigurations.java` · RIGHT `43`

🤖 *AI text below* 🤖

An explicit NIO factory is left in place when an existing listener is converted to TLS, but ZooKeeper’s NIO factory cannot open a secure listener. Could startup reject incompatible explicit server or client transport settings with a message that tells operators what to change?

## 12. Isolate the tutorial’s negative trust check

[tutorial-securing-solr.adoc:431](https://github.com/epugh/solr/blob/201446e25e676d85860e3ed378635eab7d7235dd/solr/solr-ref-guide/modules/getting-started/pages/tutorial-securing-solr.adoc#L431) · `solr/solr-ref-guide/modules/getting-started/pages/tutorial-securing-solr.adoc` · RIGHT `431`

🤖 *AI text below* 🤖

This step replaces node 2’s outbound trust before it joins the TLS-only ZooKeeper, so the node can fail startup before the distributed HTTPS query. Could the tutorial leave ZooKeeper trust intact for this check, or show the ZooKeeper startup failure as the expected result?

