# Draft note text: URL allow-list does not cover stream sources

Branch: `docs-streaming-allowlist-note` on the fork (head 4b185504ef, based on 498a09aea9). No PR opened.

Two documentation spots, one line each:

1. `configuration-guide/pages/configuring-solr-xml.adoc`, under `allowUrls`, after the paragraph on `solr.security.allow.urls.enabled`:

   > The allow-list does not cover stream sources. The `zkHost` and `solrConnection` operands of a streaming expression connect directly to the named target and are not checked against this list; bound those outbound connections with the operator's network controls. See xref:query-guide:stream-source-reference.adoc[stream sources].

2. `query-guide/pages/stream-source-reference.adoc`, a NOTE above the first section:

   > NOTE: The URL allow-list does not cover stream sources. The `zkHost` and `solrConnection` operands connect directly to the named target and are not checked against `allowUrls`; bound those connections with the operator's network controls. See xref:configuration-guide:configuring-solr-xml.adoc#allow-urls[`allowUrls`].

Code basis at the pinned head 498a09aea9: the allow-list checkers (`AllowListUrlChecker`, `AllowListZkHostChecker`) are referenced by the shard handler, replica sources, index fetch, core create, restore, and join parsers. The streaming source code does not reference them.

Gate status: BLOCKED. `:solr:solr-ref-guide:buildLocalAntoraSite` fails with "Cannot find module '@asciidoctor/tabs'". The `downloadAsciidoctorTabsExtension` task reports "changed 1 package", but the module is not in `solr/solr-ref-guide/.gradle/node/node_modules/@asciidoctor` (only `core` is present). Logs: `/workspace/gates/logs/vm4/docs-item4-site.log` and `/workspace/gates/logs/vm4/docs-item4-site-2.log`. `checkSiteLinks` did not run. The docs build check is not passed.
