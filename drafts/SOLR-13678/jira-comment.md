The deadlock is in `removeCollectionPropsWatcher`. Commit a052fb5436 (2019) only hardened `CollectionPropsTest.testReadWriteCached` and did not fix the lock ordering. The fix is SOLR-17720, commit 67a642fe02 (April 2025). It moves the lock on the collection outside `collectionPropsObservers.compute()`, which removes the lock-order inversion this ticket describes (`solr/solrj-zookeeper/src/java/org/apache/solr/common/cloud/CollectionPropertiesZkStateReader.java`).

I checked the code change, not the thread dump attached to this ticket.

If you still see this:

- Reopen with the Solr version and steps, and say which of the commits named here does not cover it.
