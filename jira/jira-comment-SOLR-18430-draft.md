# Jira comment draft: SOLR-18430

Status: DRAFT (not posted). Shape: staged ask, evidence first. Addressee: Eric Pugh (reporter). Prepared by vm6, 2026-10-27, from `report-v2-read.md` (pinned main `e59ce3c97d`). Posting needs Nick Shanin's explicit go.

---

I checked the replica property endpoints at apache/solr main {{e59ce3c97d}}. The v2 API has PUT and DELETE on a single named property ({{AddReplicaPropertyApi.java:29}} and {{DeleteReplicaPropertyApi.java:32}}), and no GET that lists them.

The properties are stored in the replica's state as {{property.<name>}} keys ({{solrj/src/java/org/apache/solr/common/cloud/Replica.java:357-358}} and {{:451}}), so a list read has a source.

Eric, is a flat map of property name to value the shape you want, as the ticket proposes?

Possible states: implement the list as a flat map; implement it with the {{property.}} prefix stripped, as the ticket's shape implies; or hold for the single-property read (SOLR-18431) so both use one shape. The recommendation is the first, with SOLR-18431 reusing its value shape.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
