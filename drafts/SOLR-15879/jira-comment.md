We checked this against Solr main at commit 498a09aea97 and found that the cache class named in this report no longer exists.

What we checked:

- The FastLRUCache class from the report was removed from Solr in 2019 (SOLR-13817, which removed the legacy cache implementations). A search of the current code finds no remaining use.
- The maxRamMB setting still exists, on the cache class that replaced the legacy ones. That class applies the RAM limit as the cache's weight limit.

How we tried to reproduce it:

- We did not run the reported setup. The reported class is no longer in Solr, so the report's exact configuration cannot be run on current code.

What this means:

- The leak described here is in a cache class that was removed in 2019. The current cache class has not been tested under the reported load in this review.

If you still see this:

- If you see a growing cache on current Solr with maxRamMB set, please send the Solr version, the cache configuration, and the cache statistics over time, and we will check it. Please reopen with that information.
