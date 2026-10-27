# Jira comment draft: SOLR-18431

Status: DRAFT (not posted). Shape: staged ask, evidence first. Addressee: Eric Pugh (reporter). Prepared by vm6, 2026-10-27, from `report-v2-read.md` (pinned main `e59ce3c97d`). Posting needs Nick Shanin's explicit go.

---

I checked the replica property endpoints at apache/solr main {{e59ce3c97d}}. There is a PUT and a DELETE on a single property ([AddReplicaPropertyApi.java:29|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/api/src/java/org/apache/solr/client/api/endpoint/AddReplicaPropertyApi.java#L29] and [DeleteReplicaPropertyApi.java:32|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/api/src/java/org/apache/solr/client/api/endpoint/DeleteReplicaPropertyApi.java#L32]), and no GET for one property. Replica properties are kept as {{property.<name>}} keys in the replica's state ([Replica.java:357-358|https://github.com/apache/solr/blob/e59ce3c97ddbe030eafadf0623fdcd80c4529c31/solr/solrj/src/java/org/apache/solr/common/cloud/Replica.java#L357-L358]), which is the same source SOLR-18430 uses.

Eric, when the property is not set on the replica, should the GET return 404, or a 200 with no value? The ticket does not say.

Possible states: implement with 404 for a missing property; implement with an empty value; or hold until SOLR-18430 settles the list shape. The recommendation is 404, which keeps a missing property distinct from an empty one.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
