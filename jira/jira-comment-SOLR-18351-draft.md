# Jira comment draft: SOLR-18351

Status: DRAFT (not posted). Shape: staged ask. Addressee: Eric Pugh (reporter). Prepared by vm6, 2026-10-27, from `reports/eric-fresh-scan-round-1.md` (pinned main `e59ce3c97d`). Posting needs Nick Shanin's explicit go.

---

The gap raised here on 2026-08-21 has been addressed. {{78862e7f54}} (SOLR-18351: remove ContentStream from SolrJ's client write path, #4811, merged 2026-09-02) adds {{RequestWriter.MultipartContentWriter}} with named parts ({{solr/solrj/src/java/org/apache/solr/client/solrj/request/RequestWriter.java:47}}), and the SolrJ module no longer references {{ContentStream}} outside its own two definition files.

At apache/solr main {{e59ce3c97d}} the type is still declared {{@Deprecated(since = "10.0")}} ({{ContentStream.java:28}}). The remaining references are:
 * core: 29 main files, 109 references (for example {{EmbeddedSolrServer.java:310}}, which builds the {{List<ContentStream>}} for request parts)
 * {{cross-dc}}: 3 files, 12 references
 * {{extraction}}: 1 file, 2 references
 * {{scripting}}: 1 file, 2 references
 * tests: 14 files

Eric, the ticket says "Remove". Is this remaining list what you expected for it, or should the removal wait until the core handlers move?

Possible states: close the ticket once the core handlers and the modules move, or keep it open and split the core handlers into their own sub-task first. The recommendation is the second: the core handlers are the bulk of the 109 references, and a sub-task lets each handler change land on its own.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
