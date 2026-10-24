# Jira comment draft: SOLR-18564 (RC3 sighting)

Status: DRAFT (not posted). Posting needs Nick Shanin's explicit go.

---

A sighting of this failure shape in the Solr 10.1.0 RC3 smoke tests, reported here because the counts and the trigger match this ticket.

In one of five completed RC3 smoke runs (JDK 21.0.12.1), {{TestTaskManagement}} failed at teardown with an error shutting down MiniSolrCloudCluster, its suppressed cause a timeout waiting for the {{closeThreadPool}} to shut down. In the same run, {{TestTermsQParserPlugin}} was charged at class teardown with 16 unreleased {{SolrMetricsContext}} objects, created through {{CoreAdminHandler.initializeMetrics}} during {{CoreContainer.loadInternal}}.

That is the signature this ticket describes: the close-pool timeout throw in {{CoreContainer.shutdown()}} skips the trailing closes, including the root metrics-context close, and the JVM-global tracker charges the leak to a later test. I am not claiming the charged test is the source; on this ticket's own analysis it usually is not. I am recording that the shape, the count of 16, and the co-occurring close-pool timeout all appeared in the RC3 artifacts.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
