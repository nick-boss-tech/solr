# Draft note text: URL allow-list does not cover stream sources

Branch: `docs-streaming-allowlist-note` on the fork (head 4b185504ef, based on 498a09aea9). No PR opened.

Two documentation spots, one line each:

1. `configuration-guide/pages/configuring-solr-xml.adoc`, under `allowUrls`, after the paragraph on `solr.security.allow.urls.enabled`:

   > The allow-list does not cover stream sources. The `zkHost` and `solrConnection` operands of a streaming expression connect directly to the named target and are not checked against this list; bound those outbound connections with the operator's network controls. See xref:query-guide:stream-source-reference.adoc[stream sources].

2. `query-guide/pages/stream-source-reference.adoc`, a NOTE above the first section:

   > NOTE: The URL allow-list does not cover stream sources. The `zkHost` and `solrConnection` operands connect directly to the named target and are not checked against `allowUrls`; bound those connections with the operator's network controls. See xref:configuration-guide:configuring-solr-xml.adoc#allow-urls[`allowUrls`].

Code basis at the pinned head 498a09aea9: the allow-list checkers (`AllowListUrlChecker`, `AllowListZkHostChecker`) are referenced by the shard handler, replica sources, index fetch, core create, restore, and join parsers. The streaming source code does not reference them.

Gate status: GREEN at unchanged head `4b185504ef7dd5e4530664995719a0e743564382`, phone-agent-2, 2026-10-23T07:21:40.292402+00:00. Antora build passed. Both rendered notes and cross-reference targets, including `#allow-urls`, passed. Gradle `checkSiteLinks` returned exit 0 but its output handler failed; a direct run of the same pinned checker produced a complete report: 835 files, zero errors and zero warnings. External links were disabled. No product repair or PR. Receipt: `receipts/consistency-docs-round-1-item4.md`; gate: `gates/consistency-docs-round-1-item4.md`; evidence: `material/consistency-docs-round-1/item4-4b185504ef-docs-evidence.tar.gz`.
