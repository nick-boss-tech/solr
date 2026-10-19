The Overseer's requests no longer use a hardcoded timeout. SOLR-14354 (commit a80eb84d56, 2020-07-07) changed the collection command classes to send requests through the shard handler factory's configured client, so the connection and socket timeouts follow the shard handler configuration instead of a fixed value. We checked this at the current main (498a09aea9): the default-client calls are gone and the configured path is in place.

If you still see a fixed timeout on current Solr, please reopen with the Solr version and the stack trace, and say which call still uses a hardcoded value.
