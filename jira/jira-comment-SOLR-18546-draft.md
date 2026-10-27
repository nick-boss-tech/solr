# Jira comment draft: SOLR-18546

Status: DRAFT (not posted). Shape: staged ask, evidence first. Addressee: Eric Pugh (reporter). Prepared by vm6, 2026-10-27, from `report-18546.md` (pinned main `e59ce3c97d`). Posting needs Nick Shanin's explicit go.

---

I counted where {{gettingstarted}} is used at apache/solr main {{e59ce3c97d}}:

 * Ref Guide: 177 lines in 36 files. The largest are {{post-command.adoc}} (21), {{solr-control-script-reference.adoc}} (19), and {{solr-in-docker.adoc}} (13).
 * CLI default: {{bin/solr start -e cloud}} creates a {{gettingstarted}} collection ({{RunExampleTool.java:1188}}), and the schemaless example maps to it ({{RunExampleTool.java:607}}). The {{post}} and {{healthcheck}} help text uses it ({{PostTool.java:95-101}}, {{HealthcheckTool.java:61}}).
 * Tests: {{SolrCloudExampleTest.java:57}} and {{CloudSolrClientCacheTest.java:69}} assert the name.

A docs-only change does not alter running behaviour. Changing the CLI default would, for anyone who posts to {{gettingstarted}} after {{-e cloud}}.

Eric, when you say "moving our ref guide", do you mean the Ref Guide examples only, or the {{bin/solr -e cloud}} default too?

Possible states: close after the Ref Guide examples change; keep open for the CLI default, pending a dev-list decision; or split the CLI default into its own ticket. The recommendation is the first, with the CLI default split out, since it changes what users get.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
