We checked this against Solr main at commit 498a09aea9. This is an improvement idea, and we did not find a defect here.

What we checked:

- The block that publishes the node as DOWN during shutdown is still commented out in CoreContainerProvider.close(). It was commented out in SOLR-15590.
- The method the ticket would call, ZkController.publishNodeAsDown, exists and is used on other paths.
- The comments on this ticket record one test failure, in ZkFailoverTest for some seeds, when the call is enabled. No failing seed or output is attached, and the cause is not recorded.

What this means: the change is not a bug fix, and it is not settled whether it is wanted. We are keeping the ticket open until someone either records the cause of the ZkFailoverTest failure or decides that live-node loss is enough.

If you still see this: please attach the test seed and output for the ZkFailoverTest failure, or describe the shutdown symptom that the change would fix. That would let us decide whether to keep the ticket open for the change or close it.
