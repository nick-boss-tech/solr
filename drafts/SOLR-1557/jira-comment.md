We looked at this against the current main branch (498a09aea97a). The shards parameter is still read as a single value (solr/core/src/java/org/apache/solr/handler/component/HttpShardHandler.java, around line 473), and the 2009 comments on this ticket still describe the same trade-off: keeping the comma-separated format for compatibility against accepting whitespace and multiple values.

Choosing between those is a decision for the project, and the ticket does not settle it, so we are leaving this open until a maintainer decides the parsing contract.
