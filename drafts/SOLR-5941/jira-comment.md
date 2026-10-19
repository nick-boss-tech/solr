We checked this against Solr main at commit 498a09aea9. The gap in the report is still there: autocommit calls the update handler directly, so a custom processor's commit action does not run on autocommit.

What we checked:

- The autocommit thread calls the update handler's commit directly, not the update processing chain.
- In SolrCloud, the chain's commit path forwards commits to the other replicas. Routing autocommit through the chain would therefore make every SolrCloud autocommit forward a commit to the replicas.

What this means: the change is not a simple switch. The maintainers need to choose whether autocommit should run the chain everywhere, only on standalone cores, or behind a new option.

If you still see this: please describe the custom processor's commit action and whether you run SolrCloud or standalone, so the choice can be made for your case.
