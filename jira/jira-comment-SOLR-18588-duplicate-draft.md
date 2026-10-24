# Jira comment draft: SOLR-18588 (close as duplicate of SOLR-18564)

Status: DRAFT (not posted). For use only if Nick Shanin rules that SOLR-18588 folds into SOLR-18564. Posting and the closure need his go.

---

Closing this as a duplicate of SOLR-18564. The 16 unreleased {{SolrMetricsContext}} objects and the close-pool timeout that occurred in the same smoke run match that ticket's mechanism: the timeout throw in {{CoreContainer.shutdown()}} skips the root metrics-context close, and the JVM-global tracker charges the leak to a later test. The RC3 sighting is recorded on SOLR-18564.

🤖 _AI text above_ 🤖 _(reviewed by Nick Shanin)_
