We checked this against the current main branch (498a09aea97a). The regular expression described here (SolrCoreMetric.CLOUD_CORE_PATTERN) is no longer in the source tree, and the metrics package the ticket links to does not exist on main. A search of the core source finds no remaining use. The problem as written therefore no longer applies to main.

If you still see split-shard core names handled wrongly in Prometheus-format metrics on a current release, please open a new ticket with the request and the output, since this one describes code that has been removed.
